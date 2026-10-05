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
                          c.pdv, c.contacto, c.empresa
                   FROM $tProforma p
                   JOIN $tContacto c ON c.id = p.id_agendamiento
                   WHERE p.usuario = ? AND p.foto_factura IS NOT NULL AND p.foto_factura != ''
                   ORDER BY p.fecha_proforma DESC";
$sql = $mysqli->prepare($queryFacturas);
$sql->bind_param("s", $usuario);
$sql->execute();
$facturas = $sql->get_result()->fetch_all(MYSQLI_ASSOC);
$sql->close();

$queryPagos = "SELECT id, id_proforma, id_agendamiento, numero_cuota, monto_pago, foto_pago, fecha_pago, observacion
               FROM $tPagos
               WHERE usuario = ?
               ORDER BY id_proforma, numero_cuota ASC";
$sql = $mysqli->prepare($queryPagos);
$sql->bind_param("s", $usuario);
$sql->execute();
$pagos = $sql->get_result()->fetch_all(MYSQLI_ASSOC);
$sql->close();

responder_json(["success" => true, "facturas" => $facturas, "pagos" => $pagos]);
