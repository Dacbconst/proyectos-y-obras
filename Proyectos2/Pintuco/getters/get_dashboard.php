<?php
error_reporting(0);
ini_set('display_errors', '0');
header('Access-Control-Allow-Origin: *');
header('Content-Type: application/json');
header('Cache-Control: no-store, no-cache, must-revalidate');

include_once '../db_connect.php';
require_once __DIR__ . '/_canal.php';
$tContacto = TABLA_CONTACTO;
$tProforma = TABLA_PROFORMA;
$tPagos = TABLA_PAGOS;

// Fuente cruda (un registro por agendamiento×ciclo de proforma, igual patrón
// que proformas_listar.php): el agregado (KPIs, embudo, top promotores) se
// calcula en el cliente para que los filtros de Promotor y Período (mismos
// que Proforma) recalculen todo sin ida y vuelta al servidor — mismo
// criterio que proforma.js / contactados.js, que ya filtran así.
// foto_factura confirmada en producción vía ALTER TABLE (2026-07-03).
$q = $mysqli->query("
    SELECT
        c.id               AS agendamiento_id,
        c.usuario,
        c.empresa, c.contacto, c.pdv,
        c.hora, c.tecnico, c.no_requiere_visita,
        c.fecha_registro   AS contacto_fecha_registro,
        c.fecha_agendamiento, c.estado_agenda,
        p.id               AS proforma_id,
        p.estado_proforma,
        p.foto_factura,
        p.monto_validado,
        p.monto_total_factura,
        p.plazo_meses,
        p.fecha_registro   AS proforma_fecha_registro
    FROM $tContacto c
    LEFT JOIN $tProforma p ON p.id_agendamiento = c.id
    WHERE c.activar = 'SI'
    ORDER BY p.id ASC
");
$registros = [];
if ($q) {
    while ($r = $q->fetch_assoc()) { $registros[] = $r; }
}

// Pagos/cuotas reales — mecánica de facturación a plazos confirmada con la
// app móvil (2026-07-03). El monto negociado (monto_validado) NO equivale
// al monto facturado: ver comentario de ultimaProformaDe() en
// estado-flujo.js — cada "Guardar" cierra la ronda vigente con su monto y
// abre una ronda nueva vacía esperando la próxima foto, así que ambos casi
// nunca coinciden en la misma fila de insert_proforma.
// id_proforma agrupa por ciclo de factura; fecha_pago/fecha_registro son la fecha real del pago (no la del agendamiento).
$q = $mysqli->query("SELECT id_proforma, id_agendamiento, usuario, monto_pago, fecha_pago, fecha_registro FROM $tPagos");
$pagos = [];
if ($q) {
    while ($r = $q->fetch_assoc()) { $pagos[] = $r; }
}

// Usuarios de canal "técnicos" (ver switch de canales, sección 5 de CLAUDE.md):
// el cliente usa esta lista para separar obras (TecnicosProyectos) de tiendas
// Kywi (promotores) sin tocar el esquema de insert_proyectos_contacto.
$usuariosTecnicos = canal_usuarios_tecnicos($mysqli);

echo json_encode([
    'registros'         => $registros,
    'pagos'             => $pagos,
    'usuarios_tecnicos' => $usuariosTecnicos,
], JSON_UNESCAPED_UNICODE | JSON_INVALID_UTF8_SUBSTITUTE);
?>
