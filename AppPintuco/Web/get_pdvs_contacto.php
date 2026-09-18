<?php
/**
 * PintucoAPP - Obtiene PDVs filtrados por la región del usuario
 */
require '../Data/Funciones.php';

header('Content-Type: application/json; charset=utf-8');

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $body = json_decode(file_get_contents("php://input"), true);
    
    // Capturamos el usuario que envía el Android
    $usuario = isset($body['usuario']) ? $body['usuario'] : null;

    if ($usuario) {
        $retorno = FuncionesSamsung::getPdvsContacto($usuario);

        if ($retorno) {
            echo json_encode(array(
                "estado" => "1",
                "pdvs" => $retorno
            ));
        } else {
            echo json_encode(array(
                "estado" => "2",
                "mensaje" => "No se encontraron PDVs para las regiones de este usuario."
            ));
        }
    } else {
        echo json_encode(array(
            "estado" => "3",
            "mensaje" => "Error: No se recibió el identificador de usuario."
        ));
    }
}
?>