<?php
// update_agenda.php — edición inline y borrado lógico desde la Agenda.
// Acciones: 'editar' (contacto/empresa/telefono/mail/direccion),
// 'reagendar' (fecha/hora, revalida choque de horario), 'eliminar'
// (borrado lógico activar='NO', nunca DELETE — mismo criterio que
// eliminarAgendamiento() del app).
require_once __DIR__ . '/../includes/auth_guard.php';
require_once __DIR__ . '/../includes/functions.php';
require_once __DIR__ . '/../db_connect.php';

$usuario = exigir_sesion();

if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
    responder_json(["success" => false, "message" => "Método no permitido."], 405);
}

$input = json_decode(file_get_contents('php://input'), true) ?? [];
$id = isset($input['id']) ? (int)$input['id'] : 0;
$accion = $input['accion'] ?? '';

if ($id <= 0) {
    responder_json(["success" => false, "message" => "Falta el id de la visita."]);
}

// El técnico solo puede tocar sus propias visitas.
$verificar = $mysqli->prepare("SELECT id FROM insert_proyectos_contacto WHERE id = ? AND usuario = ? AND activar = 'SI'");
$verificar->bind_param("is", $id, $usuario);
$verificar->execute();
if (!$verificar->get_result()->fetch_assoc()) {
    responder_json(["success" => false, "message" => "Visita no encontrada."], 404);
}
$verificar->close();

if ($accion === 'eliminar') {
    $sql = $mysqli->prepare("UPDATE insert_proyectos_contacto SET activar = 'NO' WHERE id = ?");
    $sql->bind_param("i", $id);
    $ok = $sql->execute();
    $sql->close();
    responder_json(["success" => $ok, "message" => $ok ? "Visita eliminada." : $mysqli->error]);
}

if ($accion === 'editar') {
    $contacto = trim($input['contacto'] ?? '');
    $empresa = trim($input['empresa'] ?? '');
    $mail = trim($input['mail'] ?? '');
    $telefono = trim($input['telefono'] ?? '');
    $direccion = trim($input['direccion'] ?? '');

    if ($error = validar_contacto_nombre($contacto)) responder_json(["success" => false, "message" => $error]);
    if ($error = validar_empresa($empresa)) responder_json(["success" => false, "message" => $error]);
    if ($error = validar_mail($mail)) responder_json(["success" => false, "message" => $error]);
    if ($error = validar_telefono($telefono)) responder_json(["success" => false, "message" => $error]);
    if ($error = validar_direccion($direccion)) responder_json(["success" => false, "message" => $error]);

    $sql = $mysqli->prepare(
        "UPDATE insert_proyectos_contacto
            SET contacto = ?, empresa = ?, mail = ?, telefono = ?, direccion = ?, lugar = ?
          WHERE id = ?"
    );
    $sql->bind_param("ssssssi", $contacto, $empresa, $mail, $telefono, $direccion, $direccion, $id);
    $ok = $sql->execute();
    $sql->close();
    responder_json(["success" => $ok, "message" => $ok ? "Visita actualizada." : $mysqli->error]);
}

if ($accion === 'reagendar') {
    $fecha_agendamiento = $input['fecha_agendamiento'] ?? '';
    $hora = $input['hora'] ?? '';

    if ($error = validar_fecha_agendamiento($fecha_agendamiento)) {
        responder_json(["success" => false, "message" => $error]);
    }
    if (!$hora) {
        responder_json(["success" => false, "message" => "La hora es obligatoria."]);
    }

    $conflicto = buscar_conflicto_horario($mysqli, $fecha_agendamiento, $usuario, $hora, 45, $id);
    if ($conflicto) {
        responder_json([
            "success" => false,
            "message" => "Ya tienes una visita agendada a esa hora.",
            "conflicto" => $conflicto,
        ]);
    }

    $sql = $mysqli->prepare(
        "UPDATE insert_proyectos_contacto
            SET fecha_agendamiento = ?, hora = ?, estado_agenda = 'reagendada', reagendado = 'SI'
          WHERE id = ?"
    );
    $sql->bind_param("ssi", $fecha_agendamiento, $hora, $id);
    $ok = $sql->execute();
    $sql->close();
    responder_json(["success" => $ok, "message" => $ok ? "Visita reagendada." : $mysqli->error]);
}

responder_json(["success" => false, "message" => "Acción no reconocida."]);
