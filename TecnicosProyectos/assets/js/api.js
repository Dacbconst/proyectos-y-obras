// Con 4 GB de RAM o menos se activa el modo ligero (ver base.css).
if (navigator.deviceMemory && navigator.deviceMemory <= 4) document.documentElement.classList.add('modo-ligero');

const MSG_SIN_INTERNET ='Sin conexión a internet. Revisa tu señal e intenta de nuevo.';
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

// Montos: hasta 5 enteros y 2 decimales (99999.99); acepta coma como separador y descarta lo demás al escribir.
document.addEventListener('input', (ev) => {
    const campo = ev.target;
    if (!campo.matches || !campo.matches('input[data-monto]')) return;
    const [entero, ...decimales] = campo.value.replace(',', '.').replace(/[^\d.]/g, '').split('.');
    const limpio = entero.slice(0, 5) + (decimales.length ? '.' + decimales.join('').slice(0, 2) : '');
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
    cierre: '<path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"></path><polyline points="22 4 12 14.01 9 11.01"></polyline>',
};

// Diálogo propio en lugar de confirm()/alert(): devuelve una promesa con true si se acepta (con soloAviso solo hay un botón).
function mostrarDialogo(titulo, texto, boton, soloAviso, variante = 'positivo', nota = '', causal = null) {
    const dialogo = document.createElement('dialog');
    dialogo.className = 'dialog-cerrar dialog-confirmar dialog-' + variante;
    dialogo.setAttribute('aria-labelledby', 'confirmar-titulo-activo');
    dialogo.innerHTML = `
        <div class="dialog-cerrar-header">
            <span class="dialog-cerrar-icon"><svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true">${ICONOS_DIALOGO[variante]}</svg></span>
            <strong class="confirmar-titulo" id="confirmar-titulo-activo"></strong>
        </div>
        <p class="confirmar-texto"></p>
        <div class="field confirmar-causal" hidden>
            <label for="confirmar-causal-campo">Causal <span class="req" aria-hidden="true">*</span></label>
            <textarea id="confirmar-causal-campo" rows="3" maxlength="200" aria-describedby="confirmar-causal-error"></textarea>
            <div class="confirmar-causal-pie">
                <p class="field-error" id="confirmar-causal-error" role="alert" hidden><svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><circle cx="12" cy="12" r="10"></circle><line x1="12" y1="8" x2="12" y2="12"></line><line x1="12" y1="16" x2="12.01" y2="16"></line></svg><span></span></p>
                <span class="confirmar-causal-contador"><span>0</span>/200</span>
            </div>
        </div>
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
    const campoCausal = dialogo.querySelector('#confirmar-causal-campo');
    const errorCausal = dialogo.querySelector('#confirmar-causal-error');
    if (causal) {
        dialogo.querySelector('.confirmar-causal').hidden = false;
        campoCausal.placeholder = causal.placeholder;
        errorCausal.querySelector('span').textContent = causal.error;
        campoCausal.addEventListener('input', () => {
            dialogo.querySelector('.confirmar-causal-contador span').textContent = campoCausal.value.length;
            if (!campoCausal.value.trim()) return;
            errorCausal.hidden = true;
            campoCausal.removeAttribute('aria-invalid');
        });
    }
    dialogo.querySelector('form').addEventListener('submit', (ev) => {
        if (!causal || ev.submitter.value !== 'ok' || campoCausal.value.trim()) return;
        ev.preventDefault();
        errorCausal.hidden = false;
        campoCausal.setAttribute('aria-invalid', 'true');
        campoCausal.focus();
    });
    return new Promise((resolver) => {
        dialogo.addEventListener('close', () => {
            const aceptado = dialogo.returnValue === 'ok';
            resolver(causal ? (aceptado ? campoCausal.value.trim() : null) : aceptado);
            dialogo.remove();
        });
        // Tocar el fondo oscuro cancela: el clic cae fuera del rectángulo del <dialog>.
        dialogo.addEventListener('click', (ev) => { if (clicFueraDelDialogo(ev, dialogo)) dialogo.close(); });
        document.body.appendChild(dialogo);
        dialogo.showModal();
    });
}

const confirmarDialogo = (titulo, texto, boton, variante, nota) => mostrarDialogo(titulo, texto, boton, false, variante, nota);
const avisarDialogo = (titulo, texto) => mostrarDialogo(titulo, texto, 'Entendido', true, 'error');
const pedirCausalDialogo = (titulo, texto, boton, nota, causal, variante = 'positivo') => mostrarDialogo(titulo, texto, boton, false, variante, nota, causal);

document.addEventListener('click', async (ev) => {
    const salir = ev.target.closest('.header-dropdown-logout');
    if (!salir) return;
    ev.preventDefault();
    if (await confirmarDialogo('¿Cerrar sesión?', 'Tendrás que ingresar de nuevo.', 'Cerrar sesión', 'peligro')) window.location.href = salir.href;
});

// Guarda en sessionStorage lo escrito en un formulario (sin la foto) para recuperarlo si el celular recarga la página.
function autoguardarFormulario(form, clave) {
    const llave = () => 'form:' + (typeof clave === 'function' ? clave() : clave);
    const guardar = () => {
        const datos = {};
        Array.from(form.elements).forEach((el) => {
            if (!el.name || ['file', 'button', 'submit', 'reset'].includes(el.type)) return;
            if (el.type === 'radio') { if (el.checked) datos[el.name] = el.value; return; }
            datos[el.name] = el.type === 'checkbox' ? el.checked : el.value;
        });
        try { sessionStorage.setItem(llave(), JSON.stringify(datos)); } catch (e) { /* sin espacio: se pierde solo el respaldo */ }
    };
    form.addEventListener('input', guardar);
    form.addEventListener('change', guardar);
    return {
        // Devuelve true si había algo escrito que recuperar.
        restaurar() {
            let datos = null;
            try { datos = JSON.parse(sessionStorage.getItem(llave())); } catch (e) { return false; }
            if (!datos) return false;
            Array.from(form.elements).forEach((el) => {
                if (!(el.name in datos) || ['file', 'button', 'submit', 'reset'].includes(el.type)) return;
                if (el.type === 'radio') el.checked = datos[el.name] === el.value;
                else if (el.type === 'checkbox') el.checked = !!datos[el.name];
                else el.value = datos[el.name];
            });
            return Object.values(datos).some((v) => v === true || (typeof v === 'string' && v.trim() !== ''));
        },
        borrar() {
            try { sessionStorage.removeItem(llave()); } catch (e) { /* sin storage */ }
        },
    };
}

function hayBorradorFormulario(clave) {
    try { return !!sessionStorage.getItem('form:' + clave); } catch (e) { return false; }
}

// Deslizar hacia abajo desde el tope de la página actualiza (ruedita de carga); alRefrescar devuelve una promesa.
function activarPullToRefresh(alRefrescar) {
    if (!('ontouchstart' in window)) return;
    const UMBRAL = 70;
    const indicador = document.createElement('div');
    indicador.className = 'ptr-indicador';
    indicador.setAttribute('role', 'status');
    indicador.setAttribute('aria-label', 'Actualizando');
    indicador.innerHTML = '<svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.4" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><path d="M21 12a9 9 0 1 1-3-6.7"></path><polyline points="21 3 21 9 15 9"></polyline></svg>';
    document.body.appendChild(indicador);

    let inicioY = null;
    let ocupado = false;
    const ocultar = () => {
        indicador.classList.remove('is-cargando');
        indicador.style.opacity = 0;
        indicador.style.transform = 'translateY(-60px)';
    };
    // No interfiere con diálogos abiertos ni con listas que ya traen scroll propio.
    const bloqueado = (el) => {
        if (document.querySelector('dialog[open]')) return true;
        for (let nodo = el; nodo && nodo !== document.body; nodo = nodo.parentElement) {
            if (nodo.scrollTop > 0) return true;
        }
        return false;
    };

    document.addEventListener('touchstart', (ev) => {
        inicioY = window.scrollY <= 0 && !ocupado && !bloqueado(ev.target) ? ev.touches[0].clientY : null;
    }, { passive: true });
    document.addEventListener('touchmove', (ev) => {
        if (inicioY === null) return;
        const distancia = ev.touches[0].clientY - inicioY;
        if (distancia <= 0) { inicioY = null; ocultar(); return; }
        indicador.style.opacity = Math.min(1, distancia / UMBRAL);
        indicador.style.transform = `translateY(${Math.min(distancia, UMBRAL * 1.5) - 60}px) rotate(${distancia * 3}deg)`;
    }, { passive: true });
    document.addEventListener('touchend', async (ev) => {
        if (inicioY === null) return;
        const distancia = ev.changedTouches[0].clientY - inicioY;
        inicioY = null;
        if (distancia < UMBRAL) { ocultar(); return; }
        ocupado = true;
        indicador.classList.add('is-cargando');
        indicador.style.opacity = 1;
        indicador.style.transform = 'translateY(0)';
        try { await alRefrescar(); } catch (e) { /* cada página ya avisa sus errores */ }
        ocupado = false;
        ocultar();
    });
}
