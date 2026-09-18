<?php
// insert_proforma.php — nueva ronda de proforma para un agendamiento.
// Modelo de ciclos: cada ronda de negociación es una fila nueva (mismo
// id_agendamiento, id distinto) — ver header de
// Proyectos2/Pintuco/getters/update_proforma.php. La foto de evidencia se
// recibe en base64 y se sube a Azure Blob con el mismo convenio que usa
// AppPintuco/Inserts/insert_proforma.php.
require_once __DIR__ . '/../includes/auth_guard.php';
require_once __DIR__ . '/../includes/functions.php';
require_once __DIR__ . '/../includes/blob_upload.php';
require_once __DIR__ . '/../db_connect.php';

$usuario = exigir_sesion();

if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
    responder_json(["success" => false, "message" => "Método no permitido."], 405);
}

$input = json_decode(file_get_contents('php://input'), true) ?? [];

$id_agendamiento = isset($input['id_agendamiento']) ? (int)$input['id_agendamiento'] : 0;
$codigo_pdv = trim($input['codigo_pdv'] ?? '');
$estado_proforma = $input['estado_proforma'] ?? 'en_proceso';
$caracteristica_visita = trim($input['caracteristica_visita'] ?? '');
$acompanamiento_tecnico = trim($input['acompanamiento_tecnico'] ?? '');
$evidenciaBase64 = $input['evidencia_base64'] ?? null;
$monto_total_factura = isset($input['monto_total_factura']) && $input['monto_total_factura'] !== ''
    ? (float)$input['monto_total_factura'] : null;
$plazo_meses = isset($input['plazo_meses']) && $input['plazo_meses'] !== '' ? (int)$input['plazo_meses'] : null;
$estado_pago = $input['estado_pago'] ?? null;

if ($id_agendamiento <= 0 || $codigo_pdv === '') {
    responder_json(["success" => false, "message" => "Falta el agendamiento o el PDV."]);
}

// Verifica que el agendamiento sea del técnico logueado.
$verificar = $mysqli->prepare("SELECT id FROM insert_proyectos_contacto WHERE id = ? AND usuario = ? AND activar = 'SI'");
$verificar->bind_param("is", $id_agendamiento, $usuario);
$verificar->execute();
if (!$verificar->get_result()->fetch_assoc()) {
    responder_json(["success" => false, "message" => "Agendamiento no encontrado."], 404);
}
$verificar->close();

$rutaEvidencia = null;
if ($evidenciaBase64) {
    $nombreArchivo = nombre_archivo_evidencia($usuario, $codigo_pdv);
    $rutaEvidencia = subir_foto_blob('app/AppPintuco/Inserts/Proforma', 'Proforma', $evidenciaBase64, $nombreArchivo);
    if (!$rutaEvidencia) {
        responder_json(["success" => false, "message" => "No se pudo subir la foto de evidencia."], 500);
    }
}

$query = "INSERT INTO insert_proforma
    (id_agendamiento, codigo_pdv, usuario, fecha_proforma, estado_proforma, evidencia,
     caracteristica_visita, acompanamiento_tecnico, monto_total_factura, plazo_meses, estado_pago)
    VALUES (?, ?, ?, CURDATE(), ?, ?, ?, ?, ?, ?, ?)";

$sql = $mysqli->prepare($query);
// 10 placeholders: id_agendamiento(i), codigo_pdv(s), usuario(s), estado_proforma(s),
// evidencia(s), caracteristica_visita(s), acompanamiento_tecnico(s), monto_total_factura(d),
// plazo_meses(i), estado_pago(s).
$sql->bind_param(
    "issssssdis",
    $id_agendamiento, $codigo_pdv, $usuario, $estado_proforma, $rutaEvidencia,
    $caracteristica_visita, $acompanamiento_tecnico, $monto_total_factura, $plazo_meses, $estado_pago
);
$ok = $sql->execute();
$nuevoId = $mysqli->insert_id;
$sql->close();

responder_json(["success" => $ok, "id" => $nuevoId, "message" => $ok ? "Proforma registrada." : $mysqli->error]);
