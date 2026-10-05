<?php
// Agendamientos del técnico y sus rondas de proforma, para armar el acordeón.
require_once __DIR__ . '/../includes/auth_guard.php';
require_once __DIR__ . '/../includes/functions.php';
require_once __DIR__ . '/../db_connect.php';

$usuario = exigir_sesion();
$tContacto = TABLA_CONTACTO;
$tProforma = TABLA_PROFORMA;

$queryAgendamientos = "SELECT id, codigo_pdv, pdv, contacto, empresa, fecha_agendamiento, estado_agenda
                        FROM $tContacto
                        WHERE (tecnico = ? OR usuario = ?) AND activar = 'SI' AND estado_agenda != 'cancelada'
                        ORDER BY fecha_agendamiento IS NULL, fecha_agendamiento DESC";
$sql = $mysqli->prepare($queryAgendamientos);
$sql->bind_param("ss", $usuario, $usuario);
$sql->execute();
$agendamientos = $sql->get_result()->fetch_all(MYSQLI_ASSOC);
$sql->close();

$queryProformas = "SELECT p.id, p.id_agendamiento, p.fecha_proforma, p.estado_proforma, p.evidencia,
                          p.caracteristica_visita, p.acompanamiento_tecnico, p.foto_factura,
                          p.monto_validado, p.monto_total_factura, p.plazo_meses, p.estado_pago,
                          p.motivo_cierre, p.fase_actual
                   FROM $tProforma p
                   JOIN $tContacto c ON c.id = p.id_agendamiento
                   WHERE (c.tecnico = ? OR c.usuario = ? OR p.usuario = ?)
                   ORDER BY p.id DESC";
$sql = $mysqli->prepare($queryProformas);
$sql->bind_param("sss", $usuario, $usuario, $usuario);
$sql->execute();
$proformas = $sql->get_result()->fetch_all(MYSQLI_ASSOC);
$sql->close();

responder_json(["success" => true, "agendamientos" => $agendamientos, "proformas" => $proformas]);
