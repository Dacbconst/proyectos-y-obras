<?php
/**
 * Subida a Azure Blob Storage — mismo patrón/cuenta que
 * AppPintuco/Inserts/upload_azure.php, para que las fotos tomadas desde
 * TecnicosProyectos se vean igual en el app Android y en Proyectos2.
 *
 * Requiere que TecnicosProyectos esté hospedado en el mismo servidor que
 * AppPintuco/Proyectos2 (confirmado con el usuario), para reutilizar el
 * mismo autoload de Composer sin instalar nada nuevo.
 */
require_once realpath($_SERVER["DOCUMENT_ROOT"]) . '/App/XploraEcuador/assets/pluginsV4/vendor/autoload.php';

use MicrosoftAzure\Storage\Blob\BlobRestProxy;
use MicrosoftAzure\Storage\Blob\Models\CreateBlockBlobOptions;
use MicrosoftAzure\Storage\Common\Exceptions\ServiceException;

const AZURE_BLOB_CONNECTION_STRING = 'DefaultEndpointsProtocol=https;AccountName=luckyecuadorweb;AccountKey=1NR1OHQjEVkwUmFTCtktU9j0/iMbVq7szdh41DOSac4icyhIzStRfyD0sAMha0ZSRWT+ZRGucKeksMR0iEaFzQ==';

/**
 * Sube una imagen en base64 al contenedor dado y devuelve la ruta relativa
 * a guardar en la BD (ej. "Proforma/<nombre>.png"), o null si falla.
 *
 * @param string $container Ej. "app/AppPintuco/Inserts/Proforma"
 * @param string $subcarpeta Ej. "Proforma" (prefijo que se guarda en la BD)
 * @param string $imagenBase64 Contenido de la imagen, sin el prefijo data:
 * @param string $nombreArchivo Nombre ya sanitizado (ver nombre_archivo_evidencia())
 */
function subir_foto_blob(string $container, string $subcarpeta, string $imagenBase64, string $nombreArchivo): ?string
{
    $blobClient = BlobRestProxy::createBlobService(AZURE_BLOB_CONNECTION_STRING);

    $rutaRelativa = $subcarpeta . '/' . $nombreArchivo . '.png';
    $rutaTemporal = sys_get_temp_dir() . '/' . uniqid('tecproy_', true) . '.png';

    file_put_contents($rutaTemporal, base64_decode($imagenBase64));
    $content = fopen($rutaTemporal, "r");

    try {
        $options = new CreateBlockBlobOptions();
        $options->setContentType('image/png');
        $blobClient->createBlockBlob($container, $rutaRelativa, $content, $options);
        unlink($rutaTemporal);
        return $rutaRelativa;
    } catch (ServiceException $e) {
        unlink($rutaTemporal);
        error_log('Error subiendo blob: ' . $e->getCode() . ' ' . $e->getMessage());
        return null;
    }
}

/** true si el valor ya es una ruta guardada anteriormente (no una foto nueva en base64) */
function ya_es_ruta_guardada(string $valor): bool
{
    return strpos($valor, 'Proforma/') === 0
        || strpos($valor, 'Factura/') === 0
        || strpos($valor, 'PagoFactura/') === 0;
}
