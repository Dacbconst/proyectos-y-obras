<?php
// PDVs del canal para el selector de "Nueva visita"; sin filtrar por región, el técnico busca.
require_once __DIR__ . '/../includes/auth_guard.php';
require_once __DIR__ . '/../includes/functions.php';
require_once __DIR__ . '/../db_connect.php';

exigir_sesion();

// PDVs de todos los canales para el selector de "Nueva visita" (los técnicos cubren todos los PDVs, no solo Kywi).
$query = "SELECT DISTINCT pos_id, pos_name, city
          FROM lvi_rutero
          WHERE activar = 'SI'
          ORDER BY pos_name ASC";

$registros = [];
if ($resultado = $mysqli->query($query)) {
    while ($fila = $resultado->fetch_assoc()) {
        $registros[] = $fila;
    }
}

responder_json(["success" => true, "data" => $registros]);
