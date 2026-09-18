<?php
// insert_contacto.php — registra una visita nueva desde TecnicosProyectos.
// Mismas validaciones que Proyectos2/Pintuco/getters/insert_contacto.php,
// pero: usuario/tecnico se fijan al técnico logueado (no se eligen de una
// lista), y fecha_agendamiento/hora/tecnico son opcionales — igual que
// ContactoFragment del app, que permite agendar después o marcar
// "No requiere visita".
require_once __DIR__ . '/../includes/auth_guard.php';
require_once __DIR__ . '/../includes/functions.php';
require_once __DIR__ . '/../db_connect.php';

$usuario = exigir_sesion();

if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
    responder_json(["success" => false, "message" => "Método no permitido."], 405);
}

$input = json_decode(file_get_contents('php://input'), true) ?? [];

$codigo_pdv = trim($input['codigo_pdv'] ?? '');
$pdv = trim($input['pdv'] ?? '');
$ciudad_pdv = trim($input['ciudad_pdv'] ?? '');
$contacto = trim($input['contacto'] ?? '');
$empresa = trim($input['empresa'] ?? '');
$mail = trim($input['mail'] ?? '');
$direccion = trim($input['direccion'] ?? '');
$latitud = isset($input['latitud']) && $input['latitud'] !== '' ? (float)$input['latitud'] : null;
$longitud = isset($input['longitud']) && $input['longitud'] !== '' ? (float)$input['longitud'] : null;
$telefono = trim($input['telefono'] ?? '');
$telefono_convencional = isset($input['telefono_convencional']) && $input['telefono_convencional'] !== ''
    ? trim($input['telefono_convencional']) : null;
$fecha_agendamiento = isset($input['fecha_agendamiento']) && $input['fecha_agendamiento'] !== ''
    ? $input['fecha_agendamiento'] : null;
$hora = isset($input['hora']) && $input['hora'] !== '' ? $input['hora'] : null;
$no_requiere_visita = !empty($input['no_requiere_visita']);

// El técnico y el usuario siempre son el técnico logueado — no se eligen.
$tecnico = $usuario;

if ($codigo_pdv === '' || $pdv === '') {
    responder_json(["success" => false, "message" => "Falta seleccionar un PDV de la lista."]);
}
if ($error = validar_contacto_nombre($contacto)) responder_json(["success" => false, "message" => $error]);
if ($error = validar_empresa($empresa)) responder_json(["success" => false, "message" => $error]);
if ($error = validar_mail($mail)) responder_json(["success" => false, "message" => $error]);
if ($error = validar_direccion($direccion)) responder_json(["success" => false, "message" => $error]);
if ($error = validar_telefono($telefono)) responder_json(["success" => false, "message" => $error]);
if ($error = validar_telefono_convencional($telefono_convencional)) responder_json(["success" => false, "message" => $error]);

if (!$no_requiere_visita) {
    if ($error = validar_fecha_agendamiento($fecha_agendamiento)) {
        responder_json(["success" => false, "message" => $error]);
    }
    if ($fecha_agendamiento && $hora) {
        $conflicto = buscar_conflicto_horario($mysqli, $fecha_agendamiento, $tecnico, $hora);
        if ($conflicto) {
            responder_json([
                "success" => false,
                "message" => "Ya tienes una visita agendada a esa hora.",
                "conflicto" => $conflicto,
            ]);
        }
    }
}

// Igual que Proyectos2: si fecha+hora ya vienen, la visita nace 'confirmado';
// si no, queda 'pendiente' — coincide con el contrato del app (agendar después).
$estado_agenda = ($fecha_agendamiento && $hora) ? 'confirmado' : 'pendiente';

$query = "INSERT INTO insert_proyectos_contacto
    (codigo_pdv, pdv, ciudad_pdv, usuario, fecha, fecha_registro, contacto, empresa, mail, direccion,
     latitud, longitud, telefono, telefono_convencional, fecha_agendamiento, titulo, hora,
     lugar, tecnico, estado_agenda, activar, no_requiere_visita)
    VALUES (?, ?, ?, ?, CURDATE(), NOW(), ?, ?, ?, ?, ?, ?, ?, ?, ?, 'Visita Técnica', ?, ?, ?, ?, 'SI', ?)";

$sql = $mysqli->prepare($query);
if (!$sql) {
    responder_json(["success" => false, "message" => "Error preparando la consulta."], 500);
}

$noRequiereVal = $no_requiere_visita ? 'SI' : 'NO';

// 18 placeholders: 8 string, 2 double (lat/long), 8 string.
$sql->bind_param(
    "ssssssssddssssssss",
    $codigo_pdv, $pdv, $ciudad_pdv, $usuario, $contacto, $empresa, $mail, $direccion,
    $latitud, $longitud, $telefono, $telefono_convencional, $fecha_agendamiento,
    $hora, $direccion, $tecnico, $estado_agenda, $noRequiereVal
);

$ok = $sql->execute();
$nuevoId = $mysqli->insert_id;
$sql->close();

responder_json([
    "success" => $ok,
    "id" => $nuevoId,
    "message" => $ok ? "Visita registrada." : $mysqli->error,
]);
