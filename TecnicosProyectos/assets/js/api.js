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
    contenedor.setAttribute('role', 'alert');
    contenedor.textContent = mensaje;
    contenedor.style.display = mensaje ? 'block' : 'none';
    if (mensaje) contenedor.scrollIntoView({ behavior: 'smooth', block: 'nearest' });
}

let temporizadorToast = null;
function mostrarToast(texto, tipo = 'exito') {
    let toast = document.getElementById('toast-guardado');
    if (!toast) {
        toast = document.createElement('div');
        toast.id = 'toast-guardado';
        toast.className = 'toast';
        toast.setAttribute('role', 'status');
        document.body.appendChild(toast);
    }
    toast.textContent = texto;
    toast.classList.toggle('toast-error', tipo === 'error');
    toast.classList.add('visible');
    clearTimeout(temporizadorToast);
    temporizadorToast = setTimeout(() => toast.classList.remove('visible'), 3000);
}

function formatearFecha(fechaISO) {
    if (!fechaISO) return '';
    const [y, m, d] = fechaISO.split('-');
    return `${d}/${m}/${y}`;
}

// Montos: hasta 4 enteros y 2 decimales (9999.99); acepta coma como separador y descarta lo demás al escribir.
document.addEventListener('input', (ev) => {
    const campo = ev.target;
    if (!campo.matches || !campo.matches('input[data-monto]')) return;
    const [entero, ...decimales] = campo.value.replace(',', '.').replace(/[^\d.]/g, '').split('.');
    const limpio = entero.slice(0, 4) + (decimales.length ? '.' + decimales.join('').slice(0, 2) : '');
    if (limpio !== campo.value) campo.value = limpio;
});

function clicFueraDelDialogo(ev, dialogo) {
    const caja = dialogo.getBoundingClientRect();
    return ev.clientX < caja.left || ev.clientX > caja.right || ev.clientY < caja.top || ev.clientY > caja.bottom;
}

// Íconos del diálogo por variante: peligro (acción de salida), error (algo falló) y positivo (acción que reactiva).
const ICONOS_DIALOGO = {
    peligro: '<path d="M10.3 3.9 1.8 18a2 2 0 0 0 1.7 3h17a2 2 0 0 0 1.7-3L13.7 3.9a2 2 0 0 0-3.4 0z"></path><line x1="12" y1="9" x2="12" y2="13"></line><line x1="12" y1="17" x2="12.01" y2="17"></line>',
    error: '<circle cx="12" cy="12" r="10"></circle><line x1="15" y1="9" x2="9" y2="15"></line><line x1="9" y1="9" x2="15" y2="15"></line>',
    positivo: '<path d="M21 12a9 9 0 1 1-3-6.7"></path><polyline points="21 3 21 9 15 9"></polyline>',
};

// Diálogo propio en lugar de confirm()/alert(): devuelve una promesa con true si se acepta (con soloAviso solo hay un botón).
function mostrarDialogo(titulo, texto, boton, soloAviso, variante = 'positivo', nota = '') {
    const dialogo = document.createElement('dialog');
    dialogo.className = 'dialog-cerrar dialog-confirmar dialog-' + variante;
    dialogo.setAttribute('aria-labelledby', 'confirmar-titulo-activo');
    dialogo.innerHTML = `
        <div class="dialog-cerrar-header">
            <span class="dialog-cerrar-icon"><svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true">${ICONOS_DIALOGO[variante]}</svg></span>
            <strong class="confirmar-titulo" id="confirmar-titulo-activo"></strong>
        </div>
        <p class="confirmar-texto"></p>
        <p class="confirmar-nota" hidden><svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><circle cx="12" cy="12" r="10"></circle><line x1="12" y1="16" x2="12" y2="12"></line><line x1="12" y1="8" x2="12.01" y2="8"></line></svg><span></span></p>
        <form method="dialog" class="form-actions">
            <button type="submit" value="" class="btn btn-ghost">Cancelar</button>
            <button type="submit" value="ok" class="btn ${variante === 'peligro' ? 'btn-danger' : 'btn-primary'}"></button>
        </form>`;
    dialogo.querySelector('.confirmar-titulo').textContent = titulo;
    dialogo.querySelector('.confirmar-texto').textContent = texto || '';
    dialogo.querySelector('[value="ok"]').textContent = boton || 'Aceptar';
    dialogo.querySelector('.confirmar-nota span').textContent = nota;
    dialogo.querySelector('.confirmar-nota').hidden = !nota;
    if (soloAviso) dialogo.querySelector('.btn-ghost').remove();
    return new Promise((resolver) => {
        dialogo.addEventListener('close', () => { resolver(dialogo.returnValue === 'ok'); dialogo.remove(); });
        // Tocar el fondo oscuro cancela: el clic cae fuera del rectángulo del <dialog>.
        dialogo.addEventListener('click', (ev) => { if (clicFueraDelDialogo(ev, dialogo)) dialogo.close(); });
        document.body.appendChild(dialogo);
        dialogo.showModal();
    });
}

const confirmarDialogo = (titulo, texto, boton, variante, nota) => mostrarDialogo(titulo, texto, boton, false, variante, nota);
const avisarDialogo = (titulo, texto) => mostrarDialogo(titulo, texto, 'Entendido', true, 'error');

document.addEventListener('click', async (ev) => {
    const salir = ev.target.closest('.header-dropdown-logout');
    if (!salir) return;
    ev.preventDefault();
    if (await confirmarDialogo('¿Cerrar sesión?', 'Tendrás que ingresar de nuevo.', 'Cerrar sesión', 'peligro')) window.location.href = salir.href;
});
