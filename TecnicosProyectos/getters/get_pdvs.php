<?php
// PDVs para el selector de "Nueva visita": todos los activos, o solo los de la región del técnico si tiene una.
require_once __DIR__ . '/../includes/auth_guard.php';
require_once __DIR__ . '/../includes/functions.php';
require_once __DIR__ . '/../db_connect.php';

$usuario = exigir_sesion();

$sql = $mysqli->prepare("SELECT region FROM repositorio_usuario_tecnicos WHERE usuario_tecnico = ?");
$sql->bind_param("s", $usuario);
$sql->execute();
$region = $sql->get_result()->fetch_assoc()['region'] ?? null;
$sql->close();

$filtraRegion = $region !== null && $region !== '' && $region !== 'TODAS';
$query = "SELECT pos_id, pos_name, city FROM repositorio_locales_dtt2 WHERE activar = 'SI'"
       . ($filtraRegion ? " AND region = ?" : "")
       . " ORDER BY pos_name ASC";

$stmt = $mysqli->prepare($query);
if ($filtraRegion) $stmt->bind_param("s", $region);
$stmt->execute();
$registros = $stmt->get_result()->fetch_all(MYSQLI_ASSOC);
$stmt->close();

responder_json(["success" => true, "data" => $registros]);
