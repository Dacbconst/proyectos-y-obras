<?php
// get_pdvs.php — PDVs para el selector de "Nueva visita".
// Solo lectura sobre lvi_rutero. El usuario_tecnico de TecnicosProyectos es
// independiente del usuario del app (no existe en lvi_rutero), así que no
// se puede filtrar por región como hace el app (getPdvsContacto). Se listan
// todos los PDVs del canal sin filtrar — mismo criterio que
// Proyectos2/Pintuco/getters/get_pdvs.php — y el técnico busca/escribe el
// nombre del PDV en el selector.
require_once __DIR__ . '/../includes/auth_guard.php';
require_once __DIR__ . '/../includes/functions.php';
require_once __DIR__ . '/../db_connect.php';

exigir_sesion();

$query = "SELECT DISTINCT pos_id, pos_name, city
          FROM lvi_rutero
          WHERE subchannel LIKE '%COMERCIAL KYWI S.A.%'
            AND activar = 'SI'
          ORDER BY pos_name ASC";

$registros = [];
if ($resultado = $mysqli->query($query)) {
    while ($fila = $resultado->fetch_assoc()) {
        $registros[] = $fila;
    }
}

responder_json(["success" => true, "data" => $registros]);
