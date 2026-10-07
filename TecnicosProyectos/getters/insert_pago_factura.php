<?php
// Registra una cuota de pago a plazos, con comprobante subido a Blob.
require_once __DIR__ . '/../includes/auth_guard.php';
require_once __DIR__ . '/../includes/functions.php';
require_once __DIR__ . '/../includes/blob_upload.php';
require_once __DIR__ . '/../db_connect.php';

$usuario = exigir_sesion();
$tProforma = TABLA_PROFORMA;
$tPagos = TABLA_PAGOS;

if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
    responder_json(["success" => false, "message" => "Método no permitido."], 405);
}

$input = json_decode(file_get_contents('php://input'), true) ?? [];

$id_proforma = isset($input['id_proforma']) ? (int)$input['id_proforma'] : 0;
$codigo_pdv = trim($input['codigo_pdv'] ?? '');
$numero_cuota = isset($input['numero_cuota']) ? (int)$input['numero_cuota'] : 0;
$monto_pago = isset($input['monto_pago']) ? (float)$input['monto_pago'] : 0;
$fecha_pago = $input['fecha_pago'] ?? date('Y-m-d');
$observacion = trim($input['observacion'] ?? '') ?: null;
$fotoBase64 = $input['foto_pago_base64'] ?? null;

$verificar = $mysqli->prepare("SELECT id_agendamiento, monto_total_factura FROM $tProforma WHERE id = ? AND usuario = ?");
$verificar->bind_param("is", $id_proforma, $usuario);
$verificar->execute();
$proforma = $verificar->get_result()->fetch_assoc();
$verificar->close();

if (!$proforma) {
    responder_json(["success" => false, "message" => "Proforma no encontrada."], 404);
}
if ($numero_cuota <= 0 || $monto_pago <= 0) {
    responder_json(["success" => false, "message" => "Número de cuota y monto son obligatorios."]);
}

$rutaFoto = null;
if ($fotoBase64) {
    $nombreArchivo = nombre_archivo_evidencia($usuario, $codigo_pdv) . '_cuota' . $numero_cuota;
    $rutaFoto = subir_foto_blob('app/AppPintuco/Inserts/PagoFactura', 'PagoFactura' . BLOB_SUFIJO, $fotoBase64, $nombreArchivo);
    if (!$rutaFoto) {
        responder_json(["success" => false, "message" => "No se pudo subir el comprobante."], 500);
    }
}

$query = "INSERT INTO $tPagos
    (id_proforma, id_agendamiento, codigo_pdv, usuario, numero_cuota, monto_pago, foto_pago, fecha_pago, observacion, fecha_registro)
    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, NOW())";
$sql = $mysqli->prepare($query);
$sql->bind_param(
    "iissidsss",
    $id_proforma, $proforma['id_agendamiento'], $codigo_pdv, $usuario, $numero_cuota,
    $monto_pago, $rutaFoto, $fecha_pago, $observacion
);
$ok = $sql->execute();
$nuevoId = $mysqli->insert_id;
$sql->close();

// Recálculo obligatorio de estado_pago tras registrar la cuota (CLAUDE.md
// sección 4, punto 5): suma todo lo abonado contra la meta fija de la
// factura y sella 'completado'/'en_proceso'. 'cerrado' es terminal — un
// abono tardío sobre un plan ya cerrado no lo reabre.
if ($ok) {
    $sumaStmt = $mysqli->prepare("SELECT SUM(monto_pago) AS total FROM $tPagos WHERE id_proforma = ?");
    $sumaStmt->bind_param("i", $id_proforma);
    $sumaStmt->execute();
    $totalAbonado = (float)($sumaStmt->get_result()->fetch_assoc()['total'] ?? 0);
    $sumaStmt->close();

    $montoTotalFactura = (float)($proforma['monto_total_factura'] ?? 0);
    $nuevoEstadoPago = ($montoTotalFactura > 0 && $totalAbonado >= $montoTotalFactura) ? 'completado' : 'en_proceso';

    $actualizar = $mysqli->prepare("UPDATE $tProforma SET estado_pago = ? WHERE id = ? AND estado_pago <> 'cerrado'");
    $actualizar->bind_param("si", $nuevoEstadoPago, $id_proforma);
    $actualizar->execute();
    $actualizar->close();
}

responder_json(["success" => $ok, "id" => $nuevoId, "message" => $ok ? "Cuota registrada." : $mysqli->error]);
