<?php

/**
 * Obtiene los contactos registrados de un usuario
 */

require '../Data/Funciones.php';

if ($_SERVER['REQUEST_METHOD'] == 'POST') {

    $body = json_decode(file_get_contents("php://input"), true);

    $retorno = FuncionesSamsung::getContactos(
        $body['usuario']);

    if ($retorno !== false) {
        $datos["estado"] = "1";
        $datos["contactos"] = $retorno;

        print json_encode($datos);
    } else {
        print json_encode(array(
            "estado" => 2,
            "mensaje" => "No se encontraron registros en el servidor."
        ));
    }
}

?>
