<?php
require '../Data/Funciones.php';

if ($_SERVER['REQUEST_METHOD'] == 'POST') {

    $body = json_decode(file_get_contents("php://input"), true);

    $retorno = FuncionesSamsung::eliminarProforma($body['id']);

    if ($retorno !== false) {
        $datos["estado"] = "1";
        print json_encode($datos);
    } else {
        print json_encode(array(
            "estado" => 2,
            "mensaje" => "No se pudo eliminar el registro."
        ));
    }
}
?>
