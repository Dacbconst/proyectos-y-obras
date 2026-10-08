// -----------------------------------------------------------------------
// contacto.js — Formulario "Nueva visita"
// PDV: combo-box con buscador incorporado (mismo patrón que Proyectos2
// agenda-crear.js / habilitarComboBuscador).
// -----------------------------------------------------------------------

const form           = document.getElementById('form-contacto');
const alerta         = document.getElementById('alerta');
const dialogGuardado = document.getElementById('dialog-guardado');
const dialogGuardadoTexto = document.getElementById('dialog-guardado-texto');
const checkNoRequiere = document.getElementById('no_requiere_visita');
const camposAgenda   = document.getElementById('campos-agenda');
const inputCodigoPdv = document.getElementById('codigo_pdv');
const inputCiudadPdv = document.getElementById('ciudad_pdv');

// Elementos del combo PDV
const pdvSelect  = document.getElementById('pdv-select');
const pdvTrigger = document.getElementById('pdv-combo-trigger');
const pdvTexto   = pdvTrigger.querySelector('.pdv-combo-texto');
const pdvPanel   = document.getElementById('pdv-combo-panel');
const pdvBuscador = document.getElementById('pdv-combo-buscador');
const pdvLista   = document.getElementById('pdv-combo-lista');

let pdvsData = [];   // cache completo de PDVs

// GPS: se pide apenas carga la página para no retrasar el guardado; si falla se puede reintentar o guardar sin coordenadas.
const gpsTexto = document.getElementById('gps-texto');
const gpsReintentar = document.getElementById('gps-reintentar');
let gpsCoords = null;

function pedirUbicacion() {
    gpsReintentar.hidden = true;
    gpsTexto.textContent = 'Ubicación…';
    if (!navigator.geolocation) {
        gpsTexto.textContent = 'Sin ubicación';
        return;
    }
    navigator.geolocation.getCurrentPosition(
        (pos) => {
            gpsCoords = { lat: pos.coords.latitude, lng: pos.coords.longitude };
            gpsTexto.textContent = 'Ubicación lista';
        },
        () => {
            gpsCoords = null;
            gpsTexto.textContent = 'Sin ubicación';
            gpsReintentar.hidden = false;
        },
        { enableHighAccuracy: true, timeout: 10000 }
    );
}
gpsReintentar.addEventListener('click', pedirUbicacion);
pedirUbicacion();

// -----------------------------------------------------------------------
// Carga de PDVs — una sola vez
// -----------------------------------------------------------------------
async function cargarPdvs() {
    pdvTrigger.disabled = true;
    pdvTexto.textContent = 'Cargando…';

    try {
        const resp = await Api.get('../getters/get_pdvs.php');
        if (!resp.success || !Array.isArray(resp.data)) {
            pdvTexto.textContent = 'Error al cargar PDVs';
            return;
        }
        pdvsData = resp.data;

        // Poblar el <select> real (fuente de verdad)
        pdvSelect.innerHTML = '<option value="">Seleccionar PDV</option>';
        pdvsData.forEach((p) => {
            const opt = document.createElement('option');
            opt.value = p.pos_id;
            opt.textContent = `${p.pos_name} — ${p.city}`;
            opt.dataset.nombre = p.pos_name;
            opt.dataset.ciudad = p.city || '';
            pdvSelect.appendChild(opt);
        });

        pdvTrigger.disabled = false;
        actualizarTriggerTexto();
    } catch (e) {
        pdvTexto.textContent = 'Error de conexión';
        mostrarError(alerta, e.message);
    }
}

// -----------------------------------------------------------------------
// Texto del botón trigger refleja la opción elegida en el <select>
// -----------------------------------------------------------------------
function actualizarTriggerTexto() {
    const opt = pdvSelect.options[pdvSelect.selectedIndex];
    const hayValor = opt && opt.value !== '';
    pdvTexto.textContent = hayValor ? opt.textContent : 'Seleccionar PDV';
    pdvTexto.classList.toggle('pdv-combo-placeholder', !hayValor);

    // Sincronizar campos ocultos
    if (hayValor) {
        inputCodigoPdv.value = opt.value;
        inputCiudadPdv.value = opt.dataset.ciudad || '';
    } else {
        inputCodigoPdv.value = '';
        inputCiudadPdv.value = '';
    }
}

// -----------------------------------------------------------------------
// Pintar lista filtrada en el panel
// -----------------------------------------------------------------------
function pintarLista(filtro) {
    pdvLista.innerHTML = '';
    const q = (filtro || '').toLowerCase().trim();

    const opciones = Array.from(pdvSelect.options).filter((o) => {
        if (!o.value) return false;
        return !q || o.textContent.toLowerCase().includes(q);
    });

    if (!opciones.length) {
        const vacio = document.createElement('div');
        vacio.className = 'pdv-combo-vacio';
        vacio.textContent = 'Sin resultados.';
        pdvLista.appendChild(vacio);
        return;
    }

    opciones.forEach((o) => {
        const item = document.createElement('div');
        item.className = 'pdv-combo-item';
        if (o.value === pdvSelect.value) item.classList.add('is-activo');

        // Nombre principal + ciudad en gris
        const nombre = document.createElement('span');
        nombre.textContent = o.dataset.nombre || o.textContent;
        const ciudad = document.createElement('small');
        ciudad.textContent = o.dataset.ciudad || '';
        ciudad.className = 'pdv-combo-ciudad';
        item.appendChild(nombre);
        if (ciudad.textContent) item.appendChild(ciudad);

        item.addEventListener('click', () => {
            pdvSelect.value = o.value;
            pdvSelect.dispatchEvent(new Event('change'));
            cerrarPanel();
        });
        pdvLista.appendChild(item);
    });
}

// -----------------------------------------------------------------------
// Abrir / cerrar panel
// -----------------------------------------------------------------------
function abrirPanel() {
    if (pdvTrigger.disabled) return;

    // En móvil el panel ocupa toda la pantalla visible (dvh baja con el teclado), así la lista no queda tapada.
    pdvPanel.hidden = false;
    document.documentElement.classList.add('pdv-abierto');
    pdvTrigger.setAttribute('aria-expanded', 'true');
    pdvBuscador.value = '';
    pintarLista('');
    setTimeout(() => pdvBuscador.focus(), 0);
}

function cerrarPanel() {
    pdvPanel.hidden = true;
    document.documentElement.classList.remove('pdv-abierto');
    pdvTrigger.setAttribute('aria-expanded', 'false');
    actualizarTriggerTexto();
}

// -----------------------------------------------------------------------
// Eventos del combo
// -----------------------------------------------------------------------
pdvTrigger.addEventListener('click', (ev) => {
    ev.stopPropagation();
    pdvPanel.hidden ? abrirPanel() : cerrarPanel();
});

document.getElementById('pdv-combo-cerrar').addEventListener('click', cerrarPanel);
pdvBuscador.addEventListener('input', () => pintarLista(pdvBuscador.value));
pdvBuscador.addEventListener('click', (ev) => ev.stopPropagation());

document.addEventListener('click', (ev) => {
    if (!pdvPanel.hidden && !pdvPanel.closest('.pdv-combo').contains(ev.target)) {
        cerrarPanel();
    }
});

document.addEventListener('keydown', (ev) => {
    if (ev.key === 'Escape' && !pdvPanel.hidden) cerrarPanel();
});

pdvSelect.addEventListener('change', actualizarTriggerTexto);

// Reflejar cambios en el <select> (MutationObserver por si se repobla)
new MutationObserver(() => {
    actualizarTriggerTexto();
    if (!pdvPanel.hidden) pintarLista(pdvBuscador.value);
}).observe(pdvSelect, { childList: true });

// -----------------------------------------------------------------------
// Formulario
// -----------------------------------------------------------------------
// -----------------------------------------------------------------------
// Validación en línea: mismas reglas que getters/insert_contacto.php, con el error bajo cada campo.
// -----------------------------------------------------------------------
const inputFecha = document.getElementById('fecha_agendamiento');
const inputHora = document.getElementById('hora');
const botonGuardar = document.getElementById('btn-guardar');
const LETRAS = /[A-Za-zÁÉÍÓÚÑáéíóúñ]/g;
const hoyIso = new Date(Date.now() - new Date().getTimezoneOffset() * 60000).toISOString().slice(0, 10);
inputFecha.min = hoyIso;

const campos = {
    pdv: {
        el: pdvTrigger,
        leer: () => inputCodigoPdv.value.trim(),
        validar: (v) => (v ? '' : 'Selecciona un PDV.'),
    },
    contacto: {
        el: document.getElementById('contacto'),
        validar: (v) => (/^[A-Za-zÁÉÍÓÚÑáéíóúñ' -]+$/.test(v) && (v.match(LETRAS) || []).length >= 2 ? '' : 'Solo letras, mínimo 2.'),
    },
    empresa: {
        el: document.getElementById('empresa'),
        validar: (v) => (/^[A-Za-z0-9ÁÉÍÓÚÑáéíóúñ.\-&' ]+$/.test(v) ? '' : 'Escribe el nombre de la empresa.'),
    },
    telefono: {
        el: document.getElementById('telefono'),
        validar: (v) => (/^\d{10}$/.test(v) ? '' : 'Deben ser 10 dígitos.'),
    },
    telefono_convencional: {
        el: document.getElementById('telefono_convencional'),
        validar: (v) => (/^\d*$/.test(v) ? '' : 'Solo dígitos.'),
    },
    mail: {
        el: document.getElementById('mail'),
        validar: (v) => (/^[^\s@.][^\s@]*[^\s@.]@[^\s@]+\.[^\s@]+$/.test(v) && !v.includes('..') ? '' : 'Correo no válido.'),
    },
    direccion: {
        el: document.getElementById('direccion'),
        validar: (v) => {
            if (!v) return 'Escribe la dirección.';
            return /^[23456789CFGHJMPQRVWX]{4,8}\+[23456789CFGHJMPQRVWX]{2,3}$/i.test(v) ? 'Escribe una dirección, no un Plus Code.' : '';
        },
    },
    fecha: {
        el: inputFecha,
        leer: () => (checkNoRequiere.checked ? '' : inputFecha.value),
        validar: (v) => {
            if (v && v < hoyIso) return 'No puede ser una fecha pasada.';
            return !v && !checkNoRequiere.checked && inputHora.value ? 'Indica la fecha.' : '';
        },
    },
    hora: {
        el: inputHora,
        leer: () => (checkNoRequiere.checked ? '' : inputHora.value),
        validar: (v) => (!v && !checkNoRequiere.checked && inputFecha.value ? 'Indica la hora.' : ''),
    },
};

function mensajeCampo(nombre) {
    const campo = campos[nombre];
    return campo.validar(campo.leer ? campo.leer() : campo.el.value.trim());
}

function pintarErrorCampo(nombre, mensaje) {
    const span = form.querySelector(`[data-error-for="${nombre}"]`);
    span.textContent = mensaje;
    span.hidden = !mensaje;
    campos[nombre].el.setAttribute('aria-invalid', mensaje ? 'true' : 'false');
}

function validarCampo(nombre) {
    const mensaje = mensajeCampo(nombre);
    pintarErrorCampo(nombre, mensaje);
    return mensaje === '';
}

Object.entries(campos).forEach(([nombre, campo]) => {
    if (nombre === 'pdv') return;
    const revalidar = nombre === 'fecha' || nombre === 'hora' ? ['fecha', 'hora'] : [nombre];
    campo.el.addEventListener('blur', () => revalidar.forEach(validarCampo));
    // Con un error a la vista, se limpia apenas el valor deja de estar mal.
    campo.el.addEventListener('input', () => {
        if (campo.el.getAttribute('aria-invalid') === 'true') revalidar.forEach(validarCampo);
    });
});
pdvSelect.addEventListener('change', () => { if (pdvSelect.value) pintarErrorCampo('pdv', ''); });

checkNoRequiere.addEventListener('change', () => {
    camposAgenda.style.display = checkNoRequiere.checked ? 'none' : 'block';
    ['fecha', 'hora'].forEach((nombre) => pintarErrorCampo(nombre, ''));
});

form.addEventListener('reset', () => {
    camposAgenda.style.display = 'block';
    pdvSelect.value = '';
    inputCodigoPdv.value = '';
    inputCiudadPdv.value = '';
    actualizarTriggerTexto();
    Object.keys(campos).forEach((nombre) => pintarErrorCampo(nombre, ''));
    mostrarError(alerta, null);
});

form.addEventListener('submit', async (ev) => {
    ev.preventDefault();
    mostrarError(alerta, null);

    const invalidos = Object.keys(campos).filter((nombre) => !validarCampo(nombre));
    if (invalidos.length) {
        const primero = campos[invalidos[0]].el;
        primero.scrollIntoView({ behavior: 'smooth', block: 'center' });
        primero.focus({ preventScroll: true });
        return;
    }

    const opt = pdvSelect.options[pdvSelect.selectedIndex];
    const body = {
        codigo_pdv:            inputCodigoPdv.value.trim(),
        pdv:                   (opt && opt.dataset.nombre) || '',
        ciudad_pdv:            inputCiudadPdv.value.trim(),
        contacto:              campos.contacto.el.value.trim(),
        empresa:               campos.empresa.el.value.trim(),
        mail:                  campos.mail.el.value.trim(),
        direccion:             campos.direccion.el.value.trim(),
        telefono:              campos.telefono.el.value.trim(),
        telefono_convencional: campos.telefono_convencional.el.value.trim(),
        latitud:               gpsCoords ? gpsCoords.lat : null,
        longitud:              gpsCoords ? gpsCoords.lng : null,
        no_requiere_visita:    checkNoRequiere.checked,
        fecha_agendamiento:    checkNoRequiere.checked ? null : inputFecha.value,
        hora:                  checkNoRequiere.checked ? null : inputHora.value,
    };

    botonGuardar.disabled = true;
    botonGuardar.textContent = 'Guardando…';
    let enviado = false;
    try {
        const resp = await Api.post('../getters/insert_contacto.php', body);
        if (resp.success) {
            dialogGuardadoTexto.textContent = body.no_requiere_visita
                ? 'La obra quedó registrada y pasa directo a proforma.'
                : `Quedó agendada para el ${formatearFecha(body.fecha_agendamiento)}${body.hora ? ' a las ' + body.hora : ''}.`;
            dialogGuardado.showModal();
            enviado = true;
        } else {
            mostrarError(alerta, resp.message || 'No se pudo guardar la visita.');
        }
    } catch (e) {
        mostrarError(alerta, e.message);
    }
    if (!enviado) {
        botonGuardar.disabled = false;
        botonGuardar.textContent = 'Guardar visita';
    }
});

// Cerrar el aviso (botón, Esc o toque fuera) lleva igual a la agenda; el botón queda bloqueado para no duplicar la visita.
dialogGuardado.addEventListener('close', () => { window.location.href = 'agenda.php'; });

cargarPdvs();
