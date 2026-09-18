<?php
/**
 * Eliminar (soft-delete) un agendamiento - Proyectos y Obras
 */

require '../Data/Funciones.php';

if ($_SERVER['REQUEST_METHOD'] == 'POST') {

    $body = json_decode(file_get_contents("php://input"), true);
    $id = isset($body['id']) ? trim($body['id']) : null;

    if (empty($id)) {
        print json_encode(
            array('estado' => '2', 'mensaje' => 'Falta el id del agendamiento')
        );
        exit;
    }

    $retorno = FuncionesSamsung::eliminarAgendamiento($id);

    if ($retorno) {

        print json_encode(
            array(
                'estado' => '1',
                'mensaje' => 'Agendamiento eliminado')
        );
    } else {

        print json_encode(
            array(
                'estado' => '2',
                'mensaje' => 'No se encontró el agendamiento')
        );
    }
}

?>
