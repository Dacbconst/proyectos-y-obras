const ANCHO_MAXIMO_FOTO = 1024;
const BASE64_MAXIMO = 1500000;
const MSG_FOTO_GRANDE = 'La foto sigue pesando demasiado. Toma otra con menos resolución o más cerca del documento.';
const MSG_FOTO_PESADA = 'No se pudo procesar la foto por falta de memoria del teléfono. Cierra otras apps e intenta de nuevo.';

// Decodifica ya reducida (createImageBitmap) para no cargar los 12-48 MP de la cámara; si no hay soporte usa <img>.
async function abrirImagenReducida(archivo) {
    if (window.createImageBitmap) {
        try {
            return await createImageBitmap(archivo, { resizeWidth: ANCHO_MAXIMO_FOTO, resizeQuality: 'medium' });
        } catch (e) { /* sin soporte de resize: se intenta con <img> */ }
    }
    const url = URL.createObjectURL(archivo);
    try {
        const imagen = new Image();
        imagen.src = url;
        await imagen.decode();
        return imagen;
    } finally {
        URL.revokeObjectURL(url);
    }
}

// Reduce la foto a máx. 1024px de ancho (como hace Android) y la devuelve en base64 como JPEG, sin agrandar fotos grandes de más.
async function leerFotoComoBase64(inputFile) {
    const archivo = inputFile.files && inputFile.files[0];
    if (!archivo) return null;
    let origen;
    try {
        origen = await abrirImagenReducida(archivo);
    } catch (e) {
        throw new Error(MSG_FOTO_PESADA);
    }
    const escala = Math.min(1, ANCHO_MAXIMO_FOTO / origen.width);
    const canvas = document.createElement('canvas');
    canvas.width = Math.round(origen.width * escala);
    canvas.height = Math.round(origen.height * escala);
    canvas.getContext('2d').drawImage(origen, 0, 0, canvas.width, canvas.height);
    if (origen.close) origen.close();

    // Si pesa de más, baja la calidad en pasos antes de rendirse.
    let dataUrl = '';
    for (const calidad of [0.85, 0.7, 0.55]) {
        dataUrl = canvas.toDataURL('image/jpeg', calidad);
        if (dataUrl.length <= BASE64_MAXIMO) break;
    }
    canvas.width = 0;
    canvas.height = 0;
    if (dataUrl.length > BASE64_MAXIMO) throw new Error(MSG_FOTO_GRANDE);
    return dataUrl.substring(dataUrl.indexOf(',') + 1);
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

// Visor a pantalla completa compartido: lo usan el detalle de proforma y la vista previa de cada campo de foto.
const dialogZoomFoto = document.getElementById('dialog-zoom');
if (dialogZoomFoto) {
    document.getElementById('zoom-cerrar').addEventListener('click', () => dialogZoomFoto.close());
    dialogZoomFoto.addEventListener('click', (ev) => { if (ev.target === dialogZoomFoto) dialogZoomFoto.close(); });
}

function abrirZoomFoto(src) {
    if (!dialogZoomFoto || !src) return;
    document.getElementById('zoom-img').src = src;
    dialogZoomFoto.showModal();
}

// Dos inputs ocultos (cámara directa y galería) que abren los botones del campo; la foto se reduce al elegirla.
// Se guarda en sessionStorage por si el celular recarga la página mientras la cámara estaba abierta.
function activarCampoFoto(campoFoto, clave, alFallar) {
    const inputs = campoFoto.querySelectorAll('input[type="file"]');
    const claveActual = () => (typeof clave === 'function' ? clave() : clave);
    const borrador = typeof clave === 'function' ? null : leerBorradorFoto(clave);
    if (borrador) mostrarFotoEnCampo(campoFoto, borrador);

    const vistaPrevia = campoFoto.querySelector('.photo-preview');
    vistaPrevia.addEventListener('click', () => abrirZoomFoto(vistaPrevia.getAttribute('src')));

    campoFoto.querySelectorAll('[data-foto]').forEach((boton) => {
        boton.addEventListener('click', () => campoFoto.querySelector(`input[data-origen="${boton.dataset.foto}"]`).click());
    });

    inputs.forEach((input) => input.addEventListener('change', async () => {
        if (!input.files || !input.files[0]) return;
        campoFoto.classList.add('is-cargando');
        try {
            const base64 = await leerFotoComoBase64(input);
            mostrarFotoEnCampo(campoFoto, base64);
            guardarBorradorFoto(claveActual(), base64);
        } catch (e) {
            if (alFallar) alFallar(e.message);
        } finally {
            campoFoto.classList.remove('is-cargando');
            input.value = '';
        }
    }));
}

function limpiarCampoFoto(campoFoto, clave) {
    campoFoto.fotoBase64 = null;
    campoFoto.classList.remove('has-photo');
    campoFoto.querySelector('.photo-preview').removeAttribute('src');
    borrarBorradorFoto(clave);
}
