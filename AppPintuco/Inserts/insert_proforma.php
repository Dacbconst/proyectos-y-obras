<?php
/**
 * Insertar/actualizar registro de Proforma (Gestión de Visitas) - Proyectos y Obras
 */

require '../Data/Funciones.php';
require 'upload_azure.php';

if ($_SERVER['REQUEST_METHOD'] == 'POST') {

    $body = json_decode(file_get_contents("php://input"), true);

    $id_remota = $body['idRemota'] ?? null;
    $id_agendamiento = $body['id_agendamiento'];
    $codigo_pdv = $body['codigo_pdv'] ?? null;
    $usuario = $body['usuario'] ?? null;
    $fecha_proforma = $body['fecha_proforma'] ?? null;
    $estado_proforma = $body['estado_proforma'] ?? 'pendiente';
    $evidencia = $body['evidencia'] ?? null;
    $caracteristica_visita = $body['caracteristica_visita'] ?? null;
    $acompanamiento_tecnico = $body['acompanamiento_tecnico'] ?? null;
    $foto_factura = $body['foto_factura'] ?? null;
    $monto_validado = $body['monto_validado'] ?? null;
    $monto_total_factura = $body['monto_total_factura'] ?? null;
    $plazo_meses = $body['plazo_meses'] ?? null;
    $estado_pago = $body['estado_pago'] ?? null;
    $motivo_cierre = $body['motivo_cierre'] ?? null;
    $motivo_cierre_pago = $body['motivo_cierre_pago'] ?? null;
    $fase_actual = $body['fase_actual'] ?? null;

    // Mismo esquema de nombre único que usa Exhibiciones (fecha+hora+usuario+pdv).
    $unique = date('dmYHis');
    $name = "$unique$usuario$codigo_pdv";
    $name_final = str_replace(str_split('\\/:*?"<>|%+#'), '', $name);
    $photo_name = str_replace(' ', '', $name_final);

    $esEvidenciaNueva = !empty($evidencia) && strpos($evidencia, 'Proforma/') !== 0 && strpos($evidencia, 'Factura/') !== 0;
    $path = $esEvidenciaNueva ? "Proforma/$photo_name.png" : null;

    $valorEvidenciaDb = $esEvidenciaNueva ? $path : (empty($id_remota) ? $evidencia : null);

    $esFotoFacturaNueva = !empty($foto_factura) && strpos($foto_factura, 'Proforma/') !== 0 && strpos($foto_factura, 'Factura/') !== 0;
    $path_factura = $esFotoFacturaNueva ? "Factura/$photo_name.png" : null;
    $valorFotoFacturaDb = $esFotoFacturaNueva ? $path_factura : (empty($id_remota) ? $foto_factura : null);

    if (!empty($id_remota)) {

        $retorno = FuncionesSamsung::updateProforma(
            $id_remota,
            $estado_proforma,
            $valorEvidenciaDb,
            $caracteristica_visita,
            $acompanamiento_tecnico,
            $valorFotoFacturaDb,
            $monto_validado,
            $monto_total_factura,
            $plazo_meses,
            $estado_pago,
            $motivo_cierre,
            $motivo_cierre_pago,
            $fase_actual
        );
        $idParaRespuesta = $id_remota;
    } else {
        $retorno = FuncionesSamsung::insertProforma(
            $id_agendamiento,
            $codigo_pdv,
            $usuario,
            $fecha_proforma,
            $estado_proforma,
            $valorEvidenciaDb,
            $caracteristica_visita,
            $acompanamiento_tecnico,
            $valorFotoFacturaDb,
            $monto_validado,
            $monto_total_factura,
            $plazo_meses,
            $estado_pago,
            $motivo_cierre,
            $motivo_cierre_pago,
            $fase_actual
        );
        $idParaRespuesta = $retorno;
    }

    if ($retorno) {
        if ($path !== null) {
            $container = 'app/AppPintuco/Inserts/Proforma';
            uploadBlobSample($blobClient, $container, $evidencia, $photo_name . '.png');
        }
        if ($path_factura !== null) {
            $container_factura = 'app/AppPintuco/Inserts/Factura';
            uploadBlobSample($blobClient, $container_factura, $foto_factura, $photo_name . '.png');
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
