<?php
// Cierre de Proforma o de plan de pago; usa guarda de carrera (affected_rows) contra cambios concurrentes.
require_once __DIR__ . '/../includes/auth_guard.php';
require_once __DIR__ . '/../includes/functions.php';
require_once __DIR__ . '/../db_connect.php';

$usuario = exigir_sesion();
$tProforma = TABLA_PROFORMA;
$tContacto = TABLA_CONTACTO;

if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
    responder_json(["success" => false, "message" => "Método no permitido."], 405);
}

$input = json_decode(file_get_contents('php://input'), true) ?? [];
$id = isset($input['id']) ? (int)$input['id'] : 0;
$accion = $input['accion'] ?? '';

if ($id <= 0) {
    responder_json(["success" => false, "message" => "Falta el id de la proforma."]);
}

// El técnico puede tocar proformas propias o asignadas a él por terceros
// (analista/otro técnico) — ver regla de permisos cruzados del CLAUDE.md.
$verificar = $mysqli->prepare(
    "SELECT p.estado_proforma, p.estado_pago FROM $tProforma p
     JOIN $tContacto c ON c.id = p.id_agendamiento
     WHERE p.id = ? AND (p.usuario = ? OR c.tecnico = ? OR c.usuario = ?)"
);
$verificar->bind_param("isss", $id, $usuario, $usuario, $usuario);
$verificar->execute();
$fila = $verificar->get_result()->fetch_assoc();
$verificar->close();

if (!$fila) {
    responder_json(["success" => false, "message" => "Proforma no encontrada."], 404);
}

if ($accion === 'rechazar') {
    $motivo_cierre = trim($input['motivo_cierre'] ?? '');
    if ($motivo_cierre === '') {
        responder_json(["success" => false, "message" => "El motivo de cierre es obligatorio."]);
    }

    $estadoEsperado = $fila['estado_proforma'];
    $sql = $mysqli->prepare(
        "UPDATE $tProforma
            SET estado_proforma = 'rechazado', motivo_cierre = ?, fecha_auditoria = NOW()
          WHERE id = ? AND estado_proforma = ?"
    );
    $sql->bind_param("sis", $motivo_cierre, $id, $estadoEsperado);
    $sql->execute();
    $afectadas = $sql->affected_rows;
    $sql->close();

    if ($afectadas === 0) {
        responder_json(["success" => false, "stale" => true, "message" => "El estado cambió mientras editabas. Recarga e intenta de nuevo."]);
    }
    responder_json(["success" => true, "message" => "Proforma cerrada."]);
}

if ($accion === 'cerrar_plan_pago') {
    $motivo_cierre_pago = trim($input['motivo_cierre_pago'] ?? '');
    if ($motivo_cierre_pago === '') {
        responder_json(["success" => false, "message" => "El motivo de cierre es obligatorio."]);
    }

    $estadoEsperado = $fila['estado_pago'];
    if ($estadoEsperado === null) {
        $sql = $mysqli->prepare(
            "UPDATE $tProforma
                SET estado_pago = 'cerrado', motivo_cierre_pago = ?
              WHERE id = ? AND estado_pago IS NULL"
        );
        $sql->bind_param("si", $motivo_cierre_pago, $id);
    } else {
        $sql = $mysqli->prepare(
            "UPDATE $tProforma
                SET estado_pago = 'cerrado', motivo_cierre_pago = ?
              WHERE id = ? AND estado_pago = ?"
        );
        $sql->bind_param("sis", $motivo_cierre_pago, $id, $estadoEsperado);
    }
    $sql->execute();
    $afectadas = $sql->affected_rows;
    $sql->close();

    if ($afectadas === 0) {
        responder_json(["success" => false, "stale" => true, "message" => "El estado de pago cambió mientras editabas. Recarga e intenta de nuevo."]);
    }
    responder_json(["success" => true, "message" => "Plan de pago cerrado."]);
}

responder_json(["success" => false, "message" => "Acción no reconocida."]);
