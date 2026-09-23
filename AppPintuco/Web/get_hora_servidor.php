<?php

/**
 * Devuelve la hora del servidor (no la del celular) en GMT-5, para que el
 * app pueda validar contra una fuente confiable antes de decidir si una
 * ENTRADA es tardía, en vez de confiar únicamente en el reloj del equipo
 * (que puede estar momentáneamente desincronizado).
 */

$zona = new DateTimeZone('America/Guayaquil'); // GMT-5, misma zona que ya usa el app
$ahora = new DateTime('now', $zona);

$datos["estado"] = "1";
$datos["fecha"] = $ahora->format('d/m/Y');
$datos["hora"] = $ahora->format('H:i:s');

print json_encode($datos);

?>
