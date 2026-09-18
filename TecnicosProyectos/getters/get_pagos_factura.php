<?php
// get_pagos_factura.php — facturas confirmadas (proformas con foto_factura)
// del técnico logueado, con sus cuotas de pago. Filtro por mes/empresa/tipo
// de pago se hace en el cliente (mismo patrón que Proyectos2).
require_once __DIR__ . '/../includes/auth_guard.php';
require_once __DIR__ . '/../includes/functions.php';
require_once __DIR__ . '/../db_connect.php';

$usuario = exigir_sesion();

$queryFacturas = "SELECT p.id, p.id_agendamiento, p.codigo_pdv, p.fecha_proforma, p.foto_factura,
                          p.monto_total_factura, p.plazo_meses, p.estado_pago, p.motivo_cierre_pago,
                          c.pdv, c.contacto, c.empresa
                   FROM insert_proforma p
                   JOIN insert_proyectos_contacto c ON c.id = p.id_agendamiento
                   WHERE p.usuario = ? AND p.foto_factura IS NOT NULL AND p.foto_factura != ''
                   ORDER BY p.fecha_proforma DESC";
$sql = $mysqli->prepare($queryFacturas);
$sql->bind_param("s", $usuario);
$sql->execute();
$facturas = $sql->get_result()->fetch_all(MYSQLI_ASSOC);
$sql->close();

$queryPagos = "SELECT id, id_proforma, id_agendamiento, numero_cuota, monto_pago, foto_pago, fecha_pago, observacion
               FROM insert_pago_factura
               WHERE usuario = ?
               ORDER BY id_proforma, numero_cuota ASC";
$sql = $mysqli->prepare($queryPagos);
$sql->bind_param("s", $usuario);
$sql->execute();
$pagos = $sql->get_result()->fetch_all(MYSQLI_ASSOC);
$sql->close();

responder_json(["success" => true, "facturas" => $facturas, "pagos" => $pagos]);
