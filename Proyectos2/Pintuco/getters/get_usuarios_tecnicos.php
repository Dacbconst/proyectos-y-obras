<?php
// Lista de técnicos (sin contraseña) para la pantalla "Usuarios técnicos".
header('Content-Type: application/json');
include_once '../db_connect.php';

$tecnicos = [];
$q = $mysqli->query("SELECT usuario_tecnico, nombre_completo, region, activo, ultimo_login FROM repositorio_usuario_tecnicos ORDER BY nombre_completo ASC");
while ($q && $fila = $q->fetch_assoc()) {
    $fila['activo'] = (int)$fila['activo'];
    $fila['region'] = $fila['region'] ?: 'TODAS';
    $tecnicos[] = $fila;
}

echo json_encode(["success" => true, "tecnicos" => $tecnicos]);
