<?php
// insert_pago_factura.php — registra una cuota de pago (a plazos), con
// foto de comprobante subida a Azure Blob (contenedor PagoFactura), mismo
// convenio que AppPintuco/Inserts/insert_pago_factura.php.
require_once __DIR__ . '/../includes/auth_guard.php';
require_once __DIR__ . '/../includes/functions.php';
require_once __DIR__ . '/../includes/blob_upload.php';
require_once __DIR__ . '/../db_connect.php';

$usuario = exigir_sesion();

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

$verificar = $mysqli->prepare("SELECT id_agendamiento FROM insert_proforma WHERE id = ? AND usuario = ?");
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
    $rutaFoto = subir_foto_blob('app/AppPintuco/Inserts/PagoFactura', 'PagoFactura', $fotoBase64, $nombreArchivo);
    if (!$rutaFoto) {
        responder_json(["success" => false, "message" => "No se pudo subir el comprobante."], 500);
    }
}

$query = "INSERT INTO insert_pago_factura
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

responder_json(["success" => $ok, "id" => $nuevoId, "message" => $ok ? "Cuota registrada." : $mysqli->error]);
