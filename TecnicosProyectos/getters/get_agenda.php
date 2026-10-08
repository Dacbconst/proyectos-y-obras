<?php
// Agenda del técnico logueado; corre las 2 transiciones perezosas de estado antes de leer.
require_once __DIR__ . '/../includes/auth_guard.php';
require_once __DIR__ . '/../includes/functions.php';
require_once __DIR__ . '/../db_connect.php';

$usuario = exigir_sesion();
$tContacto = TABLA_CONTACTO;
$tProforma = TABLA_PROFORMA;

// Vencidas y completadas se ponen al día antes de leer.
actualizar_estados_agenda($mysqli);

$query = "SELECT id, codigo_pdv, pdv, ciudad_pdv, contacto, empresa, mail, direccion,
                 latitud, longitud, telefono, telefono_convencional, fecha_agendamiento,
                 titulo, hora, lugar, estado_agenda, no_requiere_visita
          FROM $tContacto
          WHERE activar = 'SI' AND (tecnico = ? OR usuario = ?)
          ORDER BY fecha_agendamiento IS NULL, fecha_agendamiento, hora";

$registros = [];
if ($sql = $mysqli->prepare($query)) {
    $sql->bind_param("ss", $usuario, $usuario);
    $sql->execute();
    $resultado = $sql->get_result();
    while ($fila = $resultado->fetch_assoc()) {
        $registros[] = $fila;
    }
    $sql->close();
}

responder_json(["success" => true, "data" => $registros]);
