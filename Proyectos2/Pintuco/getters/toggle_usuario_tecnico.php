<?php
// Activa o desactiva el login de un técnico; sus obras siguen en el canal Técnicos.
header('Content-Type: application/json');
include_once '../db_connect.php';

$input = json_decode(file_get_contents('php://input'), true) ?? [];
$usuario = trim($input['usuario'] ?? '');
$activo = !empty($input['activo']) ? 1 : 0;

if ($usuario === '') {
    echo json_encode(["success" => false, "message" => "Falta el usuario."]);
    exit;
}

$sql = $mysqli->prepare("UPDATE repositorio_usuario_tecnicos SET activo = ? WHERE usuario_tecnico = ?");
$sql->bind_param("is", $activo, $usuario);
$ok = $sql->execute();

echo json_encode(["success" => $ok, "message" => $ok ? "Listo." : $mysqli->error]);
