const MSG_SIN_INTERNET = 'Sin conexión a internet. Revisa tu señal e intenta de nuevo.';
const MSG_SIN_SERVIDOR = 'No se pudo conectar con el servidor. Revisa tu conexión e intenta de nuevo.';
const MSG_TIMEOUT = 'La conexión tardó demasiado y no se confirmó el envío. Intenta de nuevo.';

const Api = {
    async post(url, body) {
        return Api._request(url, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(body),
        }, 60000);
    },
    async get(url, params) {
        const query = params ? '?' + new URLSearchParams(params).toString() : '';
        const resp = await Api._request(url + query, { method: 'GET' }, 30000);
        if (resp.success && url.includes('/getters/')) {
            try { sessionStorage.setItem('cache:' + url + query, JSON.stringify(resp)); } catch (e) { /* sin espacio: solo se pierde el atajo */ }
        }
        return resp;
    },
    // Entrega primero la última respuesta guardada de esa URL (si hay) y luego la del servidor.
    async getConCache(url, alDatos) {
        let guardada = null;
        try {
            guardada = sessionStorage.getItem('cache:' + url);
            if (guardada) alDatos(JSON.parse(guardada));
        } catch (e) { /* sin cache: se espera al servidor */ }
        const resp = await Api.get(url);
        // Si el servidor devuelve lo mismo que ya se pintó, no se vuelve a pintar (evita parpadeo y perder lo escrito).
        if (JSON.stringify(resp) !== guardada) alDatos(resp);
    },
    // Traduce fallos de red a mensajes claros para el técnico; los catch solo muestran e.message.
    async _request(url, opciones, timeoutMs) {
        if (!navigator.onLine) throw new Error(MSG_SIN_INTERNET);
        const control = new AbortController();
        const temporizador = setTimeout(() => control.abort(), timeoutMs);
        let res;
        try {
            res = await fetch(url, { ...opciones, signal: control.signal });
        } catch (e) {
            throw new Error(e.name === 'AbortError' ? MSG_TIMEOUT : MSG_SIN_SERVIDOR);
        } finally {
            clearTimeout(temporizador);
        }
        return Api._parse(res);
    },
    async _parse(res) {
        let data;
        try {
            data = await res.json();
        } catch (e) {
            throw new Error('El servidor respondió con un error. Intenta de nuevo en unos minutos.');
        }
        if (res.status === 401) {
            window.location.href = '../auth/login.php';
            throw new Error('Sesión expirada.');
        }
        return data;
    },
};

function mostrarError(contenedor, mensaje) {
    contenedor.textContent = mensaje;
    contenedor.style.display = mensaje ? 'block' : 'none';
    if (mensaje) contenedor.scrollIntoView({ behavior: 'smooth', block: 'nearest' });
}

let temporizadorToast = null;
function mostrarToast(texto) {
    let toast = document.getElementById('toast-guardado');
    if (!toast) {
        toast = document.createElement('div');
        toast.id = 'toast-guardado';
        toast.className = 'toast';
        toast.setAttribute('role', 'status');
        document.body.appendChild(toast);
    }
    toast.textContent = texto;
    toast.classList.add('visible');
    clearTimeout(temporizadorToast);
    temporizadorToast = setTimeout(() => toast.classList.remove('visible'), 3000);
}

function formatearFecha(fechaISO) {
    if (!fechaISO) return '';
    const [y, m, d] = fechaISO.split('-');
    return `${d}/${m}/${y}`;
}
