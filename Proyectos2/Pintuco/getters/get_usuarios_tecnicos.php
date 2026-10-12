<?php
// Lista de técnicos (sin contraseña) y cuántos PDV activos ve cada región, para la pantalla "Usuarios técnicos".
header('Content-Type: application/json');
include_once '../db_connect.php';

$tecnicos = [];
$q = $mysqli->query("SELECT usuario_tecnico, nombre_completo, region, activo, ultimo_login FROM repositorio_usuario_tecnicos ORDER BY nombre_completo ASC");
while ($q && $fila = $q->fetch_assoc()) {
    $fila['activo'] = (int)$fila['activo'];
    $fila['region'] = $fila['region'] ?: 'TODAS';
    $tecnicos[] = $fila;
}

$pdvPorRegion = [];
$q = $mysqli->query("SELECT region, COUNT(*) AS n FROM repositorio_locales_dtt2 WHERE activar = 'SI' GROUP BY region");
while ($q && $fila = $q->fetch_assoc()) {
    $pdvPorRegion[$fila['region']] = (int)$fila['n'];
}
$pdvPorRegion['TODAS'] = array_sum($pdvPorRegion);

echo json_encode(["success" => true, "tecnicos" => $tecnicos, "pdv_por_region" => $pdvPorRegion]);
