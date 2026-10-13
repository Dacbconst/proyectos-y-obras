<?php
// Edición inline y borrado lógico de la Agenda: editar / reagendar / eliminar.
require_once __DIR__ . '/../includes/auth_guard.php';
require_once __DIR__ . '/../includes/functions.php';
require_once __DIR__ . '/../includes/blob_upload.php';
require_once __DIR__ . '/../db_connect.php';

$usuario = exigir_sesion();
$tContacto = TABLA_CONTACTO;

if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
    responder_json(["success" => false, "message" => "Método no permitido."], 405);
}

$input = json_decode(file_get_contents('php://input'), true) ?? [];
$id = isset($input['id']) ? (int)$input['id'] : 0;
$accion = $input['accion'] ?? '';

if ($id <= 0) {
    responder_json(["success" => false, "message" => "Falta el id de la visita."]);
}

// El técnico solo puede tocar sus propias visitas o las asignadas a él.
$verificar = $mysqli->prepare("SELECT id, codigo_pdv FROM $tContacto WHERE id = ? AND (tecnico = ? OR usuario = ?) AND activar = 'SI'");
$verificar->bind_param("iss", $id, $usuario, $usuario);
$verificar->execute();
$visita = $verificar->get_result()->fetch_assoc();
if (!$visita) {
    responder_json(["success" => false, "message" => "Visita no encontrada."], 404);
}
$verificar->close();

if ($accion === 'eliminar') {
    $sql = $mysqli->prepare("UPDATE $tContacto SET activar = 'NO' WHERE id = ?");
    $sql->bind_param("i", $id);
    $ok = $sql->execute();
    $sql->close();
    responder_json(["success" => $ok, "message" => $ok ? "Visita eliminada." : $mysqli->error]);
}

if ($accion === 'confirmar_visita') {
    // La foto del PDV es opcional; si llega, se sube antes de cerrar la visita.
    $rutaFotoPdv = null;
    if (!empty($input['foto_pdv_base64'])) {
        $nombreArchivo = nombre_archivo_evidencia($usuario, $visita['codigo_pdv'] ?? '');
        $rutaFotoPdv = subir_foto_blob('app/AppPintuco/Inserts', 'Visita' . BLOB_SUFIJO, $input['foto_pdv_base64'], $nombreArchivo);
        if (!$rutaFotoPdv) {
            responder_json(["success" => false, "message" => "No se pudo subir la foto del PDV."], 500);
        }
    }

    // Solo se confirma una visita cuya hora ya llegó y que sigue abierta.
    $sql = $mysqli->prepare(
        "UPDATE $tContacto
            SET estado_agenda = 'completada', foto_pdv = COALESCE(?, foto_pdv)
          WHERE id = ? AND estado_agenda NOT IN ('cancelada', 'completada')
            AND fecha_agendamiento IS NOT NULL AND fecha_agendamiento != '0000-00-00'
            AND TIMESTAMP(fecha_agendamiento, IFNULL(NULLIF(hora, ''), '00:00:00')) <= NOW()"
    );
    $sql->bind_param("si", $rutaFotoPdv, $id);
    $sql->execute();
    $filas = $sql->affected_rows;
    $sql->close();
    responder_json([
        "success" => $filas > 0,
        "message" => $filas > 0 ? "Visita confirmada." : "Esta visita todavía no se puede confirmar.",
    ]);
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
        "UPDATE $tContacto
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
    $motivo = mb_substr(trim($input['motivo'] ?? ''), 0, 255);
    if ($motivo === '') {
        responder_json(["success" => false, "message" => "Escribe el motivo del reagendamiento."]);
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
        "UPDATE $tContacto
            SET fecha_agendamiento = ?, hora = ?, estado_agenda = 'reagendada', reagendado = 'SI', motivo_reagendacion = ?
          WHERE id = ?"
    );
    $sql->bind_param("sssi", $fecha_agendamiento, $hora, $motivo, $id);
    $ok = $sql->execute();
    $sql->close();
    responder_json(["success" => $ok, "message" => $ok ? "Visita reagendada." : $mysqli->error]);
}

responder_json(["success" => false, "message" => "Acción no reconocida."]);
