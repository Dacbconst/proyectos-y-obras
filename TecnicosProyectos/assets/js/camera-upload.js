/**
 * Lee un <input type="file"> capturado con la cámara y devuelve el
 * contenido en base64 (sin el prefijo data:image/...;base64,), listo para
 * mandar en JSON al mismo contrato de wire que ya usan insert_proforma.php /
 * insert_pago_factura.php del lado app.
 */
function leerFotoComoBase64(inputFile) {
    return new Promise((resolve, reject) => {
        const archivo = inputFile.files && inputFile.files[0];
        if (!archivo) {
            resolve(null);
            return;
        }
        const lector = new FileReader();
        lector.onload = () => {
            const resultado = lector.result;
            const base64 = resultado.substring(resultado.indexOf(',') + 1);
            resolve(base64);
        };
        lector.onerror = () => reject(new Error('No se pudo leer la foto.'));
        lector.readAsDataURL(archivo);
    });
}

function previsualizarFoto(inputFile, imgElemento) {
    const archivo = inputFile.files && inputFile.files[0];
    if (!archivo) return;
    const url = URL.createObjectURL(archivo);
    imgElemento.src = url;
    imgElemento.style.display = 'block';
}
