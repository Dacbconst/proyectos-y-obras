<?php

// Devuelve la hora del servidor (GMT-5) para validar atraso sin depender del reloj del celular.

$zona = new DateTimeZone('America/Guayaquil'); // GMT-5, misma zona que ya usa el app
$ahora = new DateTime('now', $zona);

$datos["estado"] = "1";
$datos["fecha"] = $ahora->format('d/m/Y');
$datos["hora"] = $ahora->format('H:i:s');

print json_encode($datos);

?>
