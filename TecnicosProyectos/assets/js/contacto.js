// -----------------------------------------------------------------------
// contacto.js — Formulario "Nueva visita"
// PDV: combo-box con buscador incorporado (mismo patrón que Proyectos2
// agenda-crear.js / habilitarComboBuscador).
// -----------------------------------------------------------------------

const form           = document.getElementById('form-contacto');
const alerta         = document.getElementById('alerta');
const alertaOk       = document.getElementById('alerta-ok');
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

// -----------------------------------------------------------------------
// Carga de PDVs — una sola vez
// -----------------------------------------------------------------------
async function cargarPdvs() {
    pdvTrigger.disabled = true;
    pdvTexto.textContent = 'Cargando PDVs…';

    try {
        const resp = await Api.get('../getters/get_pdvs.php');
        if (!resp.success || !Array.isArray(resp.data)) {
            pdvTexto.textContent = 'Error al cargar PDVs';
            return;
        }
        pdvsData = resp.data;

        // Poblar el <select> real (fuente de verdad)
        pdvSelect.innerHTML = '<option value="">Seleccione un PDV</option>';
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
    } catch {
        pdvTexto.textContent = 'Error de conexión';
        mostrarError(alerta, 'No se pudo cargar la lista de PDVs.');
    }
}

// -----------------------------------------------------------------------
// Texto del botón trigger refleja la opción elegida en el <select>
// -----------------------------------------------------------------------
function actualizarTriggerTexto() {
    const opt = pdvSelect.options[pdvSelect.selectedIndex];
    const hayValor = opt && opt.value !== '';
    pdvTexto.textContent = hayValor ? opt.textContent : 'Seleccione un PDV';
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

    // Opción "limpiar selección" siempre arriba, no se filtra con el buscador
    const borrar = document.createElement('div');
    borrar.className = 'pdv-combo-item pdv-combo-item-reset';
    borrar.textContent = 'Seleccione un PDV';
    if (pdvSelect.value === '') borrar.classList.add('is-activo');
    borrar.addEventListener('click', () => {
        pdvSelect.value = '';
        pdvSelect.dispatchEvent(new Event('change'));
        cerrarPanel();
    });
    pdvLista.appendChild(borrar);

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

    // Posicionamiento fixed igual al patrón de Proyectos2
    const rect = pdvTrigger.getBoundingClientRect();
    const ancho = Math.max(rect.width, 300);
    const izquierda = Math.min(rect.left, window.innerWidth - ancho - 12);
    pdvPanel.style.position = 'fixed';
    pdvPanel.style.top  = (rect.bottom + 4) + 'px';
    pdvPanel.style.left = Math.max(izquierda, 12) + 'px';
    pdvPanel.style.width = ancho + 'px';
    pdvPanel.style.right = 'auto';

    pdvPanel.hidden = false;
    pdvTrigger.setAttribute('aria-expanded', 'true');
    pdvBuscador.value = '';
    pintarLista('');
    setTimeout(() => pdvBuscador.focus(), 0);
}

function cerrarPanel() {
    pdvPanel.hidden = true;
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
checkNoRequiere.addEventListener('change', () => {
    camposAgenda.style.display = checkNoRequiere.checked ? 'none' : 'block';
});

form.addEventListener('reset', () => {
    camposAgenda.style.display = 'block';
    pdvSelect.value = '';
    inputCodigoPdv.value = '';
    inputCiudadPdv.value = '';
    actualizarTriggerTexto();
});

form.addEventListener('submit', async (ev) => {
    ev.preventDefault();
    mostrarError(alerta, null);
    mostrarError(alertaOk, null);

    const codigoPdv = inputCodigoPdv.value.trim();
    const opt = pdvSelect.options[pdvSelect.selectedIndex];
    const nombrePdv = (opt && opt.dataset.nombre) ? opt.dataset.nombre : '';
    const ciudadPdv = inputCiudadPdv.value.trim();

    if (!codigoPdv || !nombrePdv) {
        mostrarError(alerta, 'Por favor selecciona un punto de venta válido de la lista.');
        return;
    }

    const body = {
        codigo_pdv:            codigoPdv,
        pdv:                   nombrePdv,
        ciudad_pdv:            ciudadPdv,
        contacto:              document.getElementById('contacto').value.trim(),
        empresa:               document.getElementById('empresa').value.trim(),
        mail:                  document.getElementById('mail').value.trim(),
        direccion:             document.getElementById('direccion').value.trim(),
        telefono:              document.getElementById('telefono').value.trim(),
        telefono_convencional: document.getElementById('telefono_convencional').value.trim(),
        no_requiere_visita:    checkNoRequiere.checked,
        fecha_agendamiento:    checkNoRequiere.checked ? null : document.getElementById('fecha_agendamiento').value,
        hora:                  checkNoRequiere.checked ? null : document.getElementById('hora').value,
    };

    const boton = form.querySelector('button[type="submit"]');
    boton.disabled = true;
    try {
        const resp = await Api.post('../getters/insert_contacto.php', body);
        if (resp.success) {
            alertaOk.textContent = 'Visita registrada correctamente.';
            alertaOk.style.display = 'block';
            form.reset();
            camposAgenda.style.display = 'block';
            actualizarTriggerTexto();
        } else {
            mostrarError(alerta, resp.message || 'No se pudo guardar la visita.');
        }
    } catch {
        mostrarError(alerta, 'Error de conexión. Intenta de nuevo.');
    } finally {
        boton.disabled = false;
    }
});

cargarPdvs();
