<?php
// Conexión persistente (p:): evita repetir el handshake con Azure MySQL en cada petición.
include_once __DIR__ . '/config.php';
$mysqli = new mysqli('p:' . HOST, USER, PASS, DB);
if ($mysqli->connect_errno) {
    http_response_code(500);
    die(json_encode(["success" => false, "message" => "Error de conexión a la base de datos."]));
}
$mysqli->set_charset('utf8mb4');
