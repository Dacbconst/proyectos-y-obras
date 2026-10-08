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
            reject(new Error('No se pudo leer la foto. Intenta con otra.'));
        };
        imagen.src = url;
    });
}

function guardarBorradorFoto(clave, base64) {
    try { sessionStorage.setItem('foto:' + clave, base64); } catch (e) { /* sin espacio: la foto sigue en memoria */ }
}

function leerBorradorFoto(clave) {
    try { return sessionStorage.getItem('foto:' + clave); } catch (e) { return null; }
}

function borrarBorradorFoto(clave) {
    try { sessionStorage.removeItem('foto:' + clave); } catch (e) { /* sin storage */ }
}

function mostrarFotoEnCampo(campoFoto, base64) {
    campoFoto.fotoBase64 = base64;
    campoFoto.querySelector('.photo-preview').src = 'data:image/jpeg;base64,' + base64;
    campoFoto.classList.add('has-photo');
}

// El campo es un <label> con el input oculto dentro: tocarlo abre cámara/galería sin JS. La foto se reduce al elegirla
// y se guarda en sessionStorage por si el celular recarga la página mientras la cámara estaba abierta.
function activarCampoFoto(campoFoto, clave, alFallar) {
    const input = campoFoto.querySelector('input[type="file"]');
    const borrador = leerBorradorFoto(clave);
    if (borrador) mostrarFotoEnCampo(campoFoto, borrador);

    input.addEventListener('change', async () => {
        if (!input.files || !input.files[0]) return;
        campoFoto.classList.add('is-cargando');
        try {
            const base64 = await leerFotoComoBase64(input);
            mostrarFotoEnCampo(campoFoto, base64);
            guardarBorradorFoto(clave, base64);
        } catch (e) {
            if (alFallar) alFallar(e.message);
        } finally {
            campoFoto.classList.remove('is-cargando');
            input.value = '';
        }
    });
}

function limpiarCampoFoto(campoFoto, clave) {
    campoFoto.fotoBase64 = null;
    campoFoto.classList.remove('has-photo');
    campoFoto.querySelector('.photo-preview').removeAttribute('src');
    borrarBorradorFoto(clave);
}
