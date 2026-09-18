<?php
/**
 * Insertar/actualizar pago de una factura a plazos - Proyectos y Obras
 */

require '../Data/Funciones.php';
require 'upload_azure.php';

if ($_SERVER['REQUEST_METHOD'] == 'POST') {

    $body = json_decode(file_get_contents("php://input"), true);

    $id_remota = $body['idRemota'] ?? null;
    $id_proforma = $body['id_proforma'] ?? null;
    $id_agendamiento = $body['id_agendamiento'] ?? null;
    $codigo_pdv = $body['codigo_pdv'] ?? null;
    $usuario = $body['usuario'] ?? null;
    $numero_cuota = $body['numero_cuota'] ?? null;
    $monto_pago = $body['monto_pago'] ?? null;
    $foto_pago = $body['foto_pago'] ?? null;
    $fecha_pago = $body['fecha_pago'] ?? null;
    $observacion = $body['observacion'] ?? null;

    $unique = date('dmYHis');
    $name = "$unique$usuario$codigo_pdv";
    $name_final = str_replace(str_split('\\/:*?"<>|%+#'), '', $name);
    $photo_name = str_replace(' ', '', $name_final);

    $path = null;
    if (!empty($foto_pago) && strpos($foto_pago, 'PagoFactura/') !== 0) {
        $path = "PagoFactura/$photo_name.png";
    }

    if (!empty($id_remota)) {
        $retorno = FuncionesSamsung::updatePagoFactura(
            $id_remota,
            $path,
            $monto_pago,
            $fecha_pago,
            $observacion
        );
        $idParaRespuesta = $id_remota;
    } else {
        $retorno = FuncionesSamsung::insertPagoFactura(
            $id_proforma,
            $id_agendamiento,
            $codigo_pdv,
            $usuario,
            $numero_cuota,
            $monto_pago,
            $path,
            $fecha_pago,
            $observacion
        );
        $idParaRespuesta = $retorno;
    }

    if ($retorno) {
        if ($path !== null) {
            $container = 'app/AppPintuco/Inserts/PagoFactura';
            uploadBlobSample($blobClient, $container, $foto_pago, $photo_name . '.png');
        }

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
