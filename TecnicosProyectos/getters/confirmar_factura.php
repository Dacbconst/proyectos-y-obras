<?php
// Adjunta foto de factura y condiciones de pago a la ronda activa (COALESCE evita pisar con NULL).
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
$monto_total_factura = isset($input['monto_total_factura']) && $input['monto_total_factura'] !== ''
    ? (float)$input['monto_total_factura'] : null;

if ($id <= 0 || $codigo_pdv === '') {
    responder_json(["success" => false, "message" => "Falta la proforma o el PDV."]);
}

$verificar = $mysqli->prepare(
    "SELECT p.id FROM $tProforma p
     JOIN $tContacto c ON c.id = p.id_agendamiento
     WHERE p.id = ? AND (p.usuario = ? OR c.tecnico = ? OR c.usuario = ?)"
);
$verificar->bind_param("isss", $id, $usuario, $usuario, $usuario);
$verificar->execute();
if (!$verificar->get_result()->fetch_assoc()) {
    responder_json(["success" => false, "message" => "Proforma no encontrada."], 404);
}
$verificar->close();

$rutaFactura = null;
if ($facturaBase64) {
    $nombreArchivo = nombre_archivo_evidencia($usuario, $codigo_pdv);
    $rutaFactura = subir_foto_blob('app/AppPintuco/Inserts/Factura', 'Factura' . BLOB_SUFIJO, $facturaBase64, $nombreArchivo);
    if (!$rutaFactura) {
        responder_json(["success" => false, "message" => "No se pudo subir la foto de factura."], 500);
    }
}

$sql = $mysqli->prepare(
    "UPDATE $tProforma SET
        foto_factura = COALESCE(?, foto_factura),
        estado_pago = COALESCE(?, estado_pago),
        plazo_meses = COALESCE(?, plazo_meses),
        monto_total_factura = COALESCE(?, monto_total_factura)
     WHERE id = ?"
);
$sql->bind_param("ssidi", $rutaFactura, $estado_pago, $plazo_meses, $monto_total_factura, $id);
$ok = $sql->execute();
$sql->close();

responder_json(["success" => $ok, "message" => $ok ? "Factura confirmada." : $mysqli->error]);
