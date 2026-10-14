<?php
// Adjunta la factura a la ronda activa; a plazos además registra esa factura como cuota 1 (mismo contrato que el app).
require_once __DIR__ . '/../includes/auth_guard.php';
require_once __DIR__ . '/../includes/functions.php';
require_once __DIR__ . '/../includes/blob_upload.php';
require_once __DIR__ . '/../db_connect.php';

$usuario = exigir_sesion();
$tProforma = TABLA_PROFORMA;
$tContacto = TABLA_CONTACTO;

if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
    responder_json(["success" => false, "message" => "Método no permitido."], 405);
}

$input = json_decode(file_get_contents('php://input'), true) ?? [];
$id = isset($input['id']) ? (int)$input['id'] : 0;
$codigo_pdv = trim($input['codigo_pdv'] ?? '');
$facturaBase64 = $input['foto_factura_base64'] ?? null;
$estado_pago = $input['estado_pago'] ?? null; // 'directo' | 'a_plazos'
$plazo_meses = isset($input['plazo_meses']) && $input['plazo_meses'] !== '' ? (int)$input['plazo_meses'] : null;
$valor_factura = isset($input['valor_factura']) && $input['valor_factura'] !== '' ? (float)$input['valor_factura'] : null;
$meta_plan = isset($input['meta_plan']) && $input['meta_plan'] !== '' ? (float)$input['meta_plan'] : null;
$aPlazos = $estado_pago === 'a_plazos';

if ($error = validar_monto($valor_factura) ?? validar_monto($meta_plan)) responder_json(["success" => false, "message" => $error]);
if (!($valor_factura > 0)) responder_json(["success" => false, "message" => "Indica el valor de la factura."]);
if ($aPlazos && !($plazo_meses >= 2)) responder_json(["success" => false, "message" => "Indica un plazo de al menos 2 meses."]);

if ($id <= 0 || $codigo_pdv === '') {
    responder_json(["success" => false, "message" => "Falta la proforma o el PDV."]);
}

$verificar = $mysqli->prepare(
    "SELECT p.evidencia, p.foto_factura, p.id_agendamiento,
            (SELECT COUNT(*) FROM $tProforma x
             WHERE x.id_agendamiento = p.id_agendamiento AND x.motivo_cierre IS NOT NULL AND TRIM(x.motivo_cierre) NOT IN ('', 'Reactivada')) AS cerradas
     FROM $tProforma p
     JOIN $tContacto c ON c.id = p.id_agendamiento
     WHERE p.id = ? AND (p.usuario = ? OR c.tecnico = ? OR c.usuario = ?)"
);
$verificar->bind_param("isss", $id, $usuario, $usuario, $usuario);
$verificar->execute();
$proforma = $verificar->get_result()->fetch_assoc();
$verificar->close();
if (!$proforma) {
    responder_json(["success" => false, "message" => "Proforma no encontrada."], 404);
}
// Se factura desde la primera visita enviada (con evidencia), nunca sobre una obra cerrada o ya facturada.
if (empty($proforma['evidencia']) || (int)$proforma['cerradas'] > 0 || !empty($proforma['foto_factura'])) {
    responder_json(["success" => false, "message" => "Esta visita aún no se puede facturar."]);
}

// A plazos la meta es la última cotización de la obra; solo si no hay ninguna se usa la que escribió el técnico.
$meta = $valor_factura;
if ($aPlazos) {
    $cotiza = $mysqli->prepare(
        "SELECT COALESCE(NULLIF(monto_validado, 0), monto_total_factura) AS cotizacion FROM $tProforma
         WHERE id_agendamiento = ? AND COALESCE(NULLIF(monto_validado, 0), monto_total_factura) > 0
         ORDER BY (monto_validado > 0) DESC, id DESC LIMIT 1"
    );
    $cotiza->bind_param("i", $proforma['id_agendamiento']);
    $cotiza->execute();
    $meta = (float)($cotiza->get_result()->fetch_assoc()['cotizacion'] ?? 0) ?: $meta_plan;
    $cotiza->close();
    if (!($meta > 0)) {
        responder_json(["success" => false, "message" => "Indica la meta del plan."]);
    }
}

$rutaFactura = null;
if ($facturaBase64) {
    $nombreArchivo = nombre_archivo_evidencia($usuario, $codigo_pdv);
    $rutaFactura = subir_foto_blob('app/AppPintuco/Inserts', 'Factura' . BLOB_SUFIJO, $facturaBase64, $nombreArchivo);
    if (!$rutaFactura) {
        responder_json(["success" => false, "message" => "No se pudo subir la foto de factura."], 500);
    }
}

// Directo cierra completo; a plazos queda en proceso salvo que esta factura ya cubra la meta.
$estadoPagoFinal = (!$aPlazos || $valor_factura >= $meta) ? 'completado' : 'en_proceso';
$tPagos = TABLA_PAGOS;

$mysqli->begin_transaction();
$sql = $mysqli->prepare(
    "UPDATE $tProforma SET
        foto_factura = COALESCE(?, foto_factura),
        estado_pago = ?,
        plazo_meses = ?,
        monto_total_factura = ?
     WHERE id = ?"
);
$sql->bind_param("ssidi", $rutaFactura, $estadoPagoFinal, $plazo_meses, $meta, $id);
$ok = $sql->execute();
$sql->close();

if ($ok && $aPlazos) {
    $cuota = $mysqli->prepare(
        "INSERT INTO $tPagos
            (id_proforma, id_agendamiento, codigo_pdv, usuario, numero_cuota, monto_pago, foto_pago, fecha_pago, fecha_registro)
         VALUES (?, ?, ?, ?, 1, ?, ?, CURDATE(), NOW())"
    );
    $cuota->bind_param("iissds", $id, $proforma['id_agendamiento'], $codigo_pdv, $usuario, $valor_factura, $rutaFactura);
    $ok = $cuota->execute();
    $cuota->close();
}

if ($ok) {
    $mysqli->commit();
} else {
    $error = $mysqli->error;
    $mysqli->rollback();
}

responder_json(["success" => $ok, "message" => $ok ? "Factura confirmada." : $error]);
