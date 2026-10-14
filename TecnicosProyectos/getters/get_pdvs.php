<?php
// PDVs para "Nueva visita": catálogo activo más los locales que registran los técnicos, limitados a la región del técnico si tiene una.
require_once __DIR__ . '/../includes/auth_guard.php';
require_once __DIR__ . '/../includes/functions.php';
require_once __DIR__ . '/../db_connect.php';

$usuario = exigir_sesion();
$tLocales = TABLA_LOCALES;

$sql = $mysqli->prepare("SELECT region FROM repositorio_usuario_tecnicos WHERE usuario_tecnico = ?");
$sql->bind_param("s", $usuario);
$sql->execute();
$region = $sql->get_result()->fetch_assoc()['region'] ?? null;
$sql->close();

$filtraRegion = $region !== null && $region !== '' && $region !== 'TODAS';
$filtroCatalogo = $filtraRegion ? " AND region = ?" : "";
$filtroLocales = $filtraRegion ? " AND ciudad IN (SELECT DISTINCT city FROM repositorio_locales_dtt2 WHERE region = ?)" : "";

$stmt = $mysqli->prepare(
    "SELECT pos_id, pos_name, city FROM (
        SELECT pos_id, pos_name, city FROM repositorio_locales_dtt2 WHERE activar = 'SI'$filtroCatalogo
        UNION ALL
        SELECT CONCAT('T', id), nombre, ciudad FROM $tLocales WHERE activo = 1$filtroLocales
     ) pdvs ORDER BY pos_name ASC"
);
if ($filtraRegion) $stmt->bind_param("ss", $region, $region);
$stmt->execute();
$registros = $stmt->get_result()->fetch_all(MYSQLI_ASSOC);
$stmt->close();

// Ciudades de la región del técnico, para registrar un local nuevo.
$ciudades = array_values(array_unique(array_column($registros, 'city')));
sort($ciudades);

responder_json(["success" => true, "data" => $registros, "ciudades" => $ciudades]);
