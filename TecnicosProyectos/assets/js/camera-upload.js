// Reduce la foto a máx. 1024px de ancho (como hace Android) y la devuelve en base64 como JPEG, sin agrandar fotos chicas.
function leerFotoComoBase64(inputFile) {
    return new Promise((resolve, reject) => {
        const archivo = inputFile.files && inputFile.files[0];
        if (!archivo) {
            resolve(null);
            return;
        }
        const anchoMaximo = 1024;
        const url = URL.createObjectURL(archivo);
        const imagen = new Image();
        imagen.onload = () => {
            URL.revokeObjectURL(url);
            const escala = Math.min(1, anchoMaximo / imagen.width);
            const ancho = Math.round(imagen.width * escala);
            const alto = Math.round(imagen.height * escala);

            const canvas = document.createElement('canvas');
            canvas.width = ancho;
            canvas.height = alto;
            canvas.getContext('2d').drawImage(imagen, 0, 0, ancho, alto);

            const dataUrl = canvas.toDataURL('image/jpeg', 0.85);
            resolve(dataUrl.substring(dataUrl.indexOf(',') + 1));
        };
        imagen.onerror = () => {
            URL.revokeObjectURL(url);
            reject(new Error('No se pudo leer la foto.'));
        };
        imagen.src = url;
    });
}

function previsualizarFoto(inputFile, imgElemento) {
    const archivo = inputFile.files && inputFile.files[0];
    if (!archivo) return;
    const url = URL.createObjectURL(archivo);
    imgElemento.src = url;
    imgElemento.style.display = 'block';
}
