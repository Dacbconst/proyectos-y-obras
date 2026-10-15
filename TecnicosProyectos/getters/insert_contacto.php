<?php
// Registra una visita nueva; usuario/tecnico son siempre el técnico logueado.
require_once __DIR__ . '/../includes/auth_guard.php';
require_once __DIR__ . '/../includes/functions.php';
require_once __DIR__ . '/../db_connect.php';

$usuario = exigir_sesion();
$tContacto = TABLA_CONTACTO;
$tLocales = TABLA_LOCALES;

if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
    responder_json(["success" => false, "message" => "Método no permitido."], 405);
}

$input = json_decode(file_get_contents('php://input'), true) ?? [];

$codigo_pdv = trim($input['codigo_pdv'] ?? '');
$pdv = trim($input['pdv'] ?? '');
$ciudad_pdv = trim($input['ciudad_pdv'] ?? '');
$provincia_pdv = strtoupper(trim($input['provincia_pdv'] ?? ''));
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
// Sin visita no hay fecha ni hora: la columna fecha_agendamiento no acepta NULL y el sistema usa 0000-00-00 para "sin fecha".
if ($no_requiere_visita) {
    $fecha_agendamiento = '0000-00-00';
    $hora = null;
}

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
    if (!$hora) {
        responder_json(["success" => false, "message" => "Indica la hora de la visita."]);
    }
    if (!$fecha_agendamiento) {
        responder_json(["success" => false, "message" => "Indica la fecha de la visita."]);
    }
    if ($fecha_agendamiento && $hora) {
        $conflicto = buscar_conflicto_horario($mysqli, $fecha_agendamiento, $tecnico, $hora);
        if ($conflicto) {
            responder_json([
                "success" => false,
                "message" => "Ya tienes una visita a las " . substr($conflicto['hora'], 0, 5) . " (" . ($conflicto['empresa'] ?: $conflicto['pdv']) . "). Elige una hora con al menos 45 minutos de diferencia.",
                "conflicto" => $conflicto,
            ]);
        }
    }
}

// Local que no está en el catálogo: se registra una sola vez y su código queda como PDVTEC<id con 3 dígitos>.
if ($codigo_pdv === 'nuevo') {
    if (mb_strlen($pdv) < 3 || mb_strlen($pdv) > 120) {
        responder_json(["success" => false, "message" => "El nombre del local debe tener entre 3 y 120 caracteres."]);
    }
    if ($ciudad_pdv === '') {
        responder_json(["success" => false, "message" => "Elige la ciudad del local nuevo."]);
    }
    $ubicacionValida = (bool)array_filter(ubicaciones_ecuador($mysqli), fn($u) => $u['province'] === $provincia_pdv && $u['city'] === $ciudad_pdv);
    if (!$ubicacionValida) {
        responder_json(["success" => false, "message" => "La provincia y la ciudad del local no son válidas."]);
    }
    // La región del local es la del técnico; si cubre TODAS, sale de la provincia en el catálogo (queda vacía si Pintuco no la tiene).
    $consultaRegion = $mysqli->prepare("SELECT region FROM repositorio_usuario_tecnicos WHERE usuario_tecnico = ?");
    $consultaRegion->bind_param("s", $usuario);
    $consultaRegion->execute();
    $regionLocal = strtoupper(trim($consultaRegion->get_result()->fetch_assoc()['region'] ?? ''));
    $consultaRegion->close();
    if (!in_array($regionLocal, ['COSTA', 'SIERRA', 'AUSTRO'], true)) {
        $consultaProvincia = $mysqli->prepare("SELECT region FROM repositorio_locales_dtt2 WHERE province = ? AND region != '' GROUP BY region ORDER BY COUNT(*) DESC LIMIT 1");
        $consultaProvincia->bind_param("s", $provincia_pdv);
        $consultaProvincia->execute();
        $regionLocal = strtoupper(trim($consultaProvincia->get_result()->fetch_assoc()['region'] ?? ''));
        $consultaProvincia->close();
    }
    $nuevoLocal = $mysqli->prepare("INSERT INTO $tLocales (nombre, ciudad, provincia, region, direccion, usuario_creador, fecha_creacion) VALUES (?, ?, ?, ?, ?, ?, NOW())
                                    ON DUPLICATE KEY UPDATE id = LAST_INSERT_ID(id)");
    $nuevoLocal->bind_param("ssssss", $pdv, $ciudad_pdv, $provincia_pdv, $regionLocal, $direccion, $usuario);
    if (!$nuevoLocal->execute()) {
        responder_json(["success" => false, "message" => "No se pudo registrar el local nuevo."], 500);
    }
    $idLocal = $mysqli->insert_id;
    $nuevoLocal->close();
    $codigo_pdv = sprintf('PDVTEC%03d', $idLocal);
    $asignarCodigo = $mysqli->prepare("UPDATE $tLocales SET codigo_pdv = COALESCE(codigo_pdv, ?) WHERE id = ?");
    $asignarCodigo->bind_param("si", $codigo_pdv, $idLocal);
    $asignarCodigo->execute();
    $asignarCodigo->close();
}

// Con fecha+hora nace 'confirmado'; sin visita pasa directo a Proforma ('completada'); si no, 'pendiente'.
$estado_agenda = $no_requiere_visita ? 'completada' : (($fecha_agendamiento && $hora) ? 'confirmado' : 'pendiente');

$query = "INSERT INTO $tContacto
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
