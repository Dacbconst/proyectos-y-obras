<?php
// get_proforma.php — agendamientos del técnico (con o sin proforma aún) y
// sus rondas de proforma existentes, para armar el acordeón.
require_once __DIR__ . '/../includes/auth_guard.php';
require_once __DIR__ . '/../includes/functions.php';
require_once __DIR__ . '/../db_connect.php';

$usuario = exigir_sesion();

$queryAgendamientos = "SELECT id, codigo_pdv, pdv, contacto, empresa, fecha_agendamiento, estado_agenda
                        FROM insert_proyectos_contacto
                        WHERE usuario = ? AND activar = 'SI' AND estado_agenda != 'cancelada'
                        ORDER BY fecha_agendamiento IS NULL, fecha_agendamiento DESC";
$sql = $mysqli->prepare($queryAgendamientos);
$sql->bind_param("s", $usuario);
$sql->execute();
$agendamientos = $sql->get_result()->fetch_all(MYSQLI_ASSOC);
$sql->close();

$queryProformas = "SELECT id, id_agendamiento, fecha_proforma, estado_proforma, evidencia,
                          caracteristica_visita, acompanamiento_tecnico, foto_factura,
                          monto_validado, monto_total_factura, plazo_meses, estado_pago,
                          motivo_cierre, fase_actual
                   FROM insert_proforma
                   WHERE usuario = ?
                   ORDER BY id DESC";
$sql = $mysqli->prepare($queryProformas);
$sql->bind_param("s", $usuario);
$sql->execute();
$proformas = $sql->get_result()->fetch_all(MYSQLI_ASSOC);
$sql->close();

responder_json(["success" => true, "agendamientos" => $agendamientos, "proformas" => $proformas]);
