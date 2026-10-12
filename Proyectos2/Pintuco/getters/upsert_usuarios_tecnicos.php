<?php
// Crea o actualiza técnicos desde las filas del Excel; la contraseña vacía conserva la actual (queda en texto plano, igual que el login).
header('Content-Type: application/json');
include_once '../db_connect.php';

const REGIONES_VALIDAS = ['COSTA', 'SIERRA', 'AUSTRO', 'TODAS'];

$input = json_decode(file_get_contents('php://input'), true) ?? [];
$filas = $input['filas'] ?? [];
if (!is_array($filas) || !$filas) {
    echo json_encode(["success" => false, "message" => "No llegaron filas para aplicar."]);
    exit;
}

$buscar = $mysqli->prepare("SELECT id FROM repositorio_usuario_tecnicos WHERE usuario_tecnico = ?");
$insertar = $mysqli->prepare("INSERT INTO repositorio_usuario_tecnicos (usuario_tecnico, nombre_completo, region, password, activo, fecha_creacion) VALUES (?, ?, ?, ?, 1, NOW())");
$actualizar = $mysqli->prepare("UPDATE repositorio_usuario_tecnicos SET nombre_completo = ?, region = ? WHERE id = ?");
$actualizarClave = $mysqli->prepare("UPDATE repositorio_usuario_tecnicos SET nombre_completo = ?, region = ?, password = ? WHERE id = ?");

$creados = 0;
$actualizados = 0;
$errores = [];

foreach ($filas as $i => $f) {
    $usuario = strtolower(trim($f['usuario'] ?? ''));
    $nombre = trim($f['nombre'] ?? '');
    $region = strtoupper(trim($f['region'] ?? ''));
    $clave = (string)($f['password'] ?? '');
    $n = $i + 1;

    if (!preg_match('/^[a-z0-9._-]{3,50}$/', $usuario) || $nombre === '' || !in_array($region, REGIONES_VALIDAS, true)) {
        $errores[] = "Fila $n: datos inválidos.";
        continue;
    }

    $buscar->bind_param("s", $usuario);
    $buscar->execute();
    $existente = $buscar->get_result()->fetch_assoc();

    if (!$existente) {
        if ($clave === '') {
            $errores[] = "Fila $n: falta la contraseña de un técnico nuevo.";
            continue;
        }
        $insertar->bind_param("ssss", $usuario, $nombre, $region, $clave);
        $insertar->execute() ? $creados++ : $errores[] = "Fila $n: " . $mysqli->error;
        continue;
    }

    if ($clave === '') {
        $actualizar->bind_param("ssi", $nombre, $region, $existente['id']);
        $ok = $actualizar->execute();
    } else {
        $actualizarClave->bind_param("sssi", $nombre, $region, $clave, $existente['id']);
        $ok = $actualizarClave->execute();
    }
    $ok ? $actualizados++ : $errores[] = "Fila $n: " . $mysqli->error;
}

echo json_encode(["success" => true, "creados" => $creados, "actualizados" => $actualizados, "errores" => $errores]);
