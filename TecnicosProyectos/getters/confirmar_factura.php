<?php
// confirmar_factura.php — "Confirmar factura": adjunta la foto de factura y
// las condiciones de pago (directo o a plazos) a la ronda de proforma
// activa. Mismo patrón COALESCE de Funciones::updateProforma
// (AppPintuco/Data/Funciones.php:7857) para no pisar con NULL columnas que
// no vienen en este request.
require_once __DIR__ . '/../includes/auth_guard.php';
require_once __DIR__ . '/../includes/functions.php';
require_once __DIR__ . '/../includes/blob_upload.php';
require_once __DIR__ . '/../db_connect.php';

$usuario = exigir_sesion();

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

$verificar = $mysqli->prepare("SELECT id FROM insert_proforma WHERE id = ? AND usuario = ?");
$verificar->bind_param("is", $id, $usuario);
$verificar->execute();
if (!$verificar->get_result()->fetch_assoc()) {
    responder_json(["success" => false, "message" => "Proforma no encontrada."], 404);
}
$verificar->close();

$rutaFactura = null;
if ($facturaBase64) {
    $nombreArchivo = nombre_archivo_evidencia($usuario, $codigo_pdv);
    $rutaFactura = subir_foto_blob('app/AppPintuco/Inserts/Factura', 'Factura', $facturaBase64, $nombreArchivo);
    if (!$rutaFactura) {
        responder_json(["success" => false, "message" => "No se pudo subir la foto de factura."], 500);
    }
}

$sql = $mysqli->prepare(
    "UPDATE insert_proforma SET
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
