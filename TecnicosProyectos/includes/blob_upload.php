<?php
// Subida a Azure Blob Storage, mismo patrón/cuenta que usa el app.
use MicrosoftAzure\Storage\Blob\BlobRestProxy;
use MicrosoftAzure\Storage\Blob\Models\CreateBlockBlobOptions;
use MicrosoftAzure\Storage\Common\Exceptions\ServiceException;

const AZURE_BLOB_CONNECTION_STRING = 'DefaultEndpointsProtocol=https;AccountName=luckyecuadorweb;AccountKey=1NR1OHQjEVkwUmFTCtktU9j0/iMbVq7szdh41DOSac4icyhIzStRfyD0sAMha0ZSRWT+ZRGucKeksMR0iEaFzQ==';

// Sube una imagen base64 al contenedor dado y devuelve la ruta relativa, o null si falla.
function subir_foto_blob(string $container, string $subcarpeta, string $imagenBase64, string $nombreArchivo): ?string
{
    // Cargado acá adentro (no al incluir el archivo): así los endpoints que
    // incluyen blob_upload.php pero no suben foto en esa request (ej. un
    // guardado sin foto nueva) no dependen del SDK de Azure para funcionar.
    require_once realpath($_SERVER["DOCUMENT_ROOT"]) . '/App/XploraEcuador/assets/pluginsV4/vendor/autoload.php';

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
        || strpos($valor, 'PagoFactura/') === 0
        || strpos($valor, 'Proforma_Test/') === 0
        || strpos($valor, 'Factura_Test/') === 0
        || strpos($valor, 'PagoFactura_Test/') === 0;
}
