<?php
// update_proforma.php — acciones de cierre disponibles para el técnico:
//  'rechazar'          → Cierre Proforma (terminal): ronda que se queda en
//                        pura cotización y nunca llega a factura. Requiere
//                        motivo_cierre. Mismo botón "Cerrar Proforma" que
//                        ya tiene el propio app.
//  'cerrar_plan_pago'  → Cierre Factura: cierra un plan de pago a plazos
//                        incompleto. Requiere motivo_cierre_pago.
// NO se incluye la acción 'guardar' de auditoría (monto_validado /
// observaciones_auditoria) — es exclusiva del analista, fuera de alcance
// de TecnicosProyectos (decisión confirmada con el usuario).
// Ambas acciones usan el mismo patrón de guarda de carrera que
// Proyectos2/Pintuco/getters/update_proforma.php: UPDATE condicionado al
// estado esperado + revisar affected_rows, para no pisar en silencio un
// cambio concurrente del analista o del propio app móvil.
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
    responder_json(["success" => false, "message" => "Falta el id de la proforma."]);
}

// El técnico solo puede tocar sus propias proformas.
$verificar = $mysqli->prepare("SELECT estado_proforma, estado_pago FROM insert_proforma WHERE id = ? AND usuario = ?");
$verificar->bind_param("is", $id, $usuario);
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
        "UPDATE insert_proforma
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
            "UPDATE insert_proforma
                SET estado_pago = 'cerrado', motivo_cierre_pago = ?
              WHERE id = ? AND estado_pago IS NULL"
        );
        $sql->bind_param("si", $motivo_cierre_pago, $id);
    } else {
        $sql = $mysqli->prepare(
            "UPDATE insert_proforma
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
