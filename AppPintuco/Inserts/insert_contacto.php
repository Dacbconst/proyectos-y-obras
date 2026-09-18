<?php
/**
 * Insertar o actualizar registro de Contacto - Proyectos y Obras
 */

require '../Data/Funciones.php';

if ($_SERVER['REQUEST_METHOD'] == 'POST') {

    $body = json_decode(file_get_contents("php://input"), true);

        $id_remota = $body['idRemota'] ?? null;
        $codigo_pdv = $body['codigo_pdv'];
        $pdv = $body['pdv'];
        $usuario = $body['usuario'];
        $fecha = $body['fecha'];
        $contacto = $body['contacto'];
        $empresa = $body['empresa'];
        $mail = $body['mail'];
        $direccion = $body['direccion'];
        $latitud = $body['latitud'] ?? null;
        $longitud = $body['longitud'] ?? null;
        $telefono = $body['telefono'];
        $telefono_convencional = $body['telefono_convencional'] ?? null;
        $fecha_agendamiento = $body['fecha_agendamiento'];
        $titulo = $body['titulo'] ?? null;
        $hora = $body['hora'] ?? null;
        $ciudad_pdv = $body['ciudad_pdv'] ?? null;
        $no_requiere_visita = $body['no_requiere_visita'] ?? 'NO';
        $lugar = $body['lugar'] ?? null;

    if (!empty($id_remota)) {
        // Ya existe en el servidor (la app mandó idRemota): actualiza la fila
        // en vez de crear una nueva. Antes de este cambio, cualquier edición desde
        // el detalle de Agenda caía igual acá abajo en el INSERT y duplicaba la fila.
        $retorno = FuncionesSamsung::updateProyectosContacto(
            $id_remota,
            $contacto,
            $empresa,
            $mail,
            $direccion,
            $telefono,
            $telefono_convencional,
            $fecha_agendamiento,
            $titulo,
            $hora,
            $ciudad_pdv,
            $lugar
        );
        $idParaRespuesta = $id_remota;
    } else {
        $retorno = FuncionesSamsung::insertProyectosContacto(
            $codigo_pdv,
            $pdv,
            $usuario,
            $fecha,
            $contacto,
            $empresa,
            $mail,
            $direccion,
            $latitud,
            $longitud,
            $telefono,
            $telefono_convencional,
            $fecha_agendamiento,
            $titulo,
            $hora,
            $ciudad_pdv,
            $no_requiere_visita
        );
        $idParaRespuesta = $retorno;
    }

    if ($retorno) {

        print json_encode(
            array(
                'estado' => '1',
                'mensaje' => 'Creación exitosa',
                'ultimoId' => $idParaRespuesta)
        );
    } else {

        print json_encode(
            array(
                'estado' => '2',
                'mensaje' => 'Creación fallida')
        );
    }
}

?>
