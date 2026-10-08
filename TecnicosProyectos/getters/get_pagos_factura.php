<?php
// Facturas confirmadas del técnico logueado con sus cuotas; filtros se aplican en el cliente.
require_once __DIR__ . '/../includes/auth_guard.php';
require_once __DIR__ . '/../includes/functions.php';
require_once __DIR__ . '/../db_connect.php';

$usuario = exigir_sesion();
$tContacto = TABLA_CONTACTO;
$tProforma = TABLA_PROFORMA;
$tPagos = TABLA_PAGOS;

$queryFacturas = "SELECT p.id, p.id_agendamiento, p.codigo_pdv, p.fecha_proforma, p.foto_factura,
                          p.monto_total_factura, p.plazo_meses, p.estado_pago, p.motivo_cierre_pago,
                          c.pdv, c.contacto, c.empresa, c.fecha_agendamiento, c.no_requiere_visita
                   FROM $tProforma p
                   JOIN $tContacto c ON c.id = p.id_agendamiento
                   WHERE (p.usuario = ? OR c.tecnico = ? OR c.usuario = ?) AND p.foto_factura IS NOT NULL AND p.foto_factura != ''
                   ORDER BY p.fecha_proforma DESC";
$sql = $mysqli->prepare($queryFacturas);
$sql->bind_param("sss", $usuario, $usuario, $usuario);
$sql->execute();
$facturas = $sql->get_result()->fetch_all(MYSQLI_ASSOC);
$sql->close();

$queryPagos = "SELECT pg.id, pg.id_proforma, pg.id_agendamiento, pg.numero_cuota, pg.monto_pago, pg.foto_pago, pg.fecha_pago, pg.observacion
               FROM $tPagos pg
               JOIN $tContacto c ON c.id = pg.id_agendamiento
               WHERE pg.usuario = ? OR c.tecnico = ? OR c.usuario = ?
               ORDER BY pg.id_proforma, pg.numero_cuota ASC";
$sql = $mysqli->prepare($queryPagos);
$sql->bind_param("sss", $usuario, $usuario, $usuario);
$sql->execute();
$pagos = $sql->get_result()->fetch_all(MYSQLI_ASSOC);
$sql->close();

responder_json(["success" => true, "facturas" => $facturas, "pagos" => $pagos]);
