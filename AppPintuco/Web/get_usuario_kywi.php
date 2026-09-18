<?php
require '../Data/Funciones.php';

header('Content-Type: application/json');

$user = isset($_GET['user']) ? $_GET['user'] : '';

if (empty($user)) {
    echo json_encode(array('error' => 'Parametro user requerido'));
    exit;
}

$result = FuncionesSamsung::esUsuarioKywi($user);

echo json_encode(array('es_kywi' => $result !== false && $result !== null));
?>
