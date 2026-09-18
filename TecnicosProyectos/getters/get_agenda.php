<?php
// get_agenda.php — agenda del técnico logueado. Antes de leer, corre las
// mismas 2 transiciones perezosas de estado que
// Proyectos2/Pintuco/getters/get_agenda.php, en el mismo orden, para no
// desincronizar lo que ve el analista/el app respecto a lo que ve aquí.
require_once __DIR__ . '/../includes/auth_guard.php';
require_once __DIR__ . '/../includes/functions.php';
require_once __DIR__ . '/../db_connect.php';

$usuario = exigir_sesion();

// 1) Vencidas: pasó la fecha y no se reagendó/canceló/completó.
$mysqli->query(
    "UPDATE insert_proyectos_contacto
     SET estado_agenda = 'vencida'
     WHERE activar = 'SI'
       AND fecha_agendamiento IS NOT NULL
       AND fecha_agendamiento != '0000-00-00'
       AND fecha_agendamiento < CURDATE()
       AND estado_agenda NOT IN ('cancelada', 'completada', 'vencida')"
);

// 2) Completadas: ya llegó su primera foto de proforma (corre después de la
// #1 a propósito, para que una vencida con evidencia sí pase a completada).
$mysqli->query(
    "UPDATE insert_proyectos_contacto c
     JOIN insert_proforma p ON p.id_agendamiento = c.id
     SET c.estado_agenda = 'completada'
     WHERE c.activar = 'SI'
       AND p.evidencia IS NOT NULL AND p.evidencia != ''
       AND c.estado_agenda NOT IN ('cancelada', 'completada')"
);

$query = "SELECT id, codigo_pdv, pdv, ciudad_pdv, contacto, empresa, mail, direccion,
                 latitud, longitud, telefono, telefono_convencional, fecha_agendamiento,
                 titulo, hora, lugar, estado_agenda, no_requiere_visita
          FROM insert_proyectos_contacto
          WHERE activar = 'SI' AND usuario = ?
          ORDER BY fecha_agendamiento IS NULL, fecha_agendamiento, hora";

$registros = [];
if ($sql = $mysqli->prepare($query)) {
    $sql->bind_param("s", $usuario);
    $sql->execute();
    $resultado = $sql->get_result();
    while ($fila = $resultado->fetch_assoc()) {
        $registros[] = $fila;
    }
    $sql->close();
}

responder_json(["success" => true, "data" => $registros]);
