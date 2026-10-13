<?php
// PDVs para "Nueva visita": catálogo activo más los locales que registran los técnicos, limitados a las ciudades (o región) del técnico.
require_once __DIR__ . '/../includes/auth_guard.php';
require_once __DIR__ . '/../includes/functions.php';
require_once __DIR__ . '/../db_connect.php';

$usuario = exigir_sesion();
$tLocales = TABLA_LOCALES;

$sql = $mysqli->prepare("SELECT region, ciudades FROM repositorio_usuario_tecnicos WHERE usuario_tecnico = ?");
$sql->bind_param("s", $usuario);
$sql->execute();
$tecnico = $sql->get_result()->fetch_assoc() ?? [];
$sql->close();

$ciudades = array_values(array_filter(array_map('trim', explode(',', strtoupper($tecnico['ciudades'] ?? '')))));
$region = $tecnico['region'] ?? '';
$filtraRegion = !$ciudades && $region !== '' && $region !== 'TODAS';

$filtroCatalogo = '';
$filtroLocales = '';
$tipos = '';
$params = [];
if ($ciudades) {
    $marcas = implode(',', array_fill(0, count($ciudades), '?'));
    $filtroCatalogo = " AND city IN ($marcas)";
    $filtroLocales = " AND ciudad IN ($marcas)";
    $tipos = str_repeat('s', count($ciudades) * 2);
    $params = array_merge($ciudades, $ciudades);
} elseif ($filtraRegion) {
    $filtroCatalogo = " AND region = ?";
    $filtroLocales = " AND ciudad IN (SELECT DISTINCT city FROM repositorio_locales_dtt2 WHERE region = ?)";
    $tipos = 'ss';
    $params = [$region, $region];
}

$stmt = $mysqli->prepare(
    "SELECT pos_id, pos_name, city FROM (
        SELECT pos_id, pos_name, city FROM repositorio_locales_dtt2 WHERE activar = 'SI'$filtroCatalogo
        UNION ALL
        SELECT CONCAT('T', id), nombre, ciudad FROM $tLocales WHERE activo = 1$filtroLocales
     ) pdvs ORDER BY pos_name ASC"
);
if ($params) $stmt->bind_param($tipos, ...$params);
$stmt->execute();
$registros = $stmt->get_result()->fetch_all(MYSQLI_ASSOC);
$stmt->close();

// Ciudades entre las que el técnico puede registrar un local nuevo.
$ciudadesDisponibles = $ciudades ?: array_values(array_unique(array_column($registros, 'city')));
sort($ciudadesDisponibles);

responder_json(["success" => true, "data" => $registros, "ciudades" => $ciudadesDisponibles]);
