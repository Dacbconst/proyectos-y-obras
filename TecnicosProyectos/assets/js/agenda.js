// agenda.js — Réplica exacta 1:1 de la app Android Pintuco y versión desktop

const ESTADOS_VISITADOS = ['completada', 'visitado'];
const ESTADOS_PENDIENTES = ['pendiente', 'confirmado', 'reagendada', 'vencida'];

const MESES = [
    'Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio',
    'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'
];
const MESES_ABREV = ['Ene', 'Feb', 'Mar', 'Abr', 'May', 'Jun', 'Jul', 'Ago', 'Sep', 'Oct', 'Nov', 'Dic'];
const DIAS_SEMANA_ABREV = ['Dom', 'Lun', 'Mar', 'Mié', 'Jue', 'Vie', 'Sáb'];

// Elementos del DOM principales
const alerta = document.getElementById('alerta');
const tvMesActual = document.getElementById('tv-mes-actual');
const btnMesAnterior = document.getElementById('btn-mes-anterior');
const btnMesSiguiente = document.getElementById('btn-mes-siguiente');
const pillPendientes = document.getElementById('pill-pendientes');
const pillVisitados = document.getElementById('pill-visitados');
const calendarGrid = document.getElementById('agenda-calendar-grid');
const listaAgenda = document.getElementById('lista-agenda');
const vacio = document.getElementById('vacio');
const vacioTitulo = document.getElementById('vacio-titulo');
const vacioSub = document.getElementById('vacio-sub');
const sectionTitle = document.getElementById('agenda-section-title');
const eventsCount = document.getElementById('agenda-events-count');

// Ventanita: Selector de Mes
const modalSelectorMes = document.getElementById('modal-selector-mes');
const selectorMesTitulo = document.getElementById('selector-mes-titulo');
const gridSelectorMeses = document.getElementById('grid-selector-meses');
const btnSelectorAnioAnt = document.getElementById('btn-selector-anio-ant');
const btnSelectorAnioSig = document.getElementById('btn-selector-anio-sig');
const btnCerrarSelectorMes = document.getElementById('btn-cerrar-selector-mes');

// Ventanita: Modal Detalle de Visita
const modalDetalle = document.getElementById('modal-detalle-visita');
const modalBtnCerrar = document.getElementById('modal-btn-cerrar');
const modalTitulo = document.getElementById('modal-titulo');
const modalRegistrado = document.getElementById('modal-registrado');
const modalBadgeEstado = document.getElementById('modal-badge-estado');
const switchModoEdicion = document.getElementById('switch-modo-edicion');
const btnEliminarVisita = document.getElementById('btn-eliminar-visita');
const modalFechaValor = document.getElementById('modal-fecha-valor');
const modalHoraValor = document.getElementById('modal-hora-valor');
const modalInfoLectura = document.getElementById('modal-info-lectura');
const modalContactoValor = document.getElementById('modal-contacto-valor');
const modalEmpresaValor = document.getElementById('modal-empresa-valor');
const modalLinkTelefono = document.getElementById('modal-link-telefono');
const modalTelefonoValor = document.getElementById('modal-telefono-valor');
const modalLinkCorreo = document.getElementById('modal-link-correo');
const modalCorreoValor = document.getElementById('modal-correo-valor');
const modalLinkDireccion = document.getElementById('modal-link-direccion');
const modalDireccionValor = document.getElementById('modal-direccion-valor');
const modalPdvValor = document.getElementById('modal-pdv-valor');
const modalFormEditar = document.getElementById('modal-form-editar');
const btnToggleReagendar = document.getElementById('btn-toggle-reagendar');
const modalFormReagendar = document.getElementById('modal-form-reagendar');

// Ventanitas de Confirmación
const modalConfirmarEliminar = document.getElementById('modal-confirmar-eliminar');
const btnCancelarEliminar = document.getElementById('btn-cancelar-eliminar');
const btnConfirmarEliminarAccion = document.getElementById('btn-confirmar-eliminar-accion');
const modalConfirmarDescartar = document.getElementById('modal-confirmar-descartar');
const btnQuedarseEditar = document.getElementById('btn-quedarse-editar');
const btnSalirDescartar = document.getElementById('btn-salir-descartar');

// Estado inicial: fecha de hoy por defecto
const hoy = new Date();
let anioVisible = hoy.getFullYear();
let mesVisible = hoy.getMonth(); // 0-11
let anioSelector = hoy.getFullYear();
let fechaSeleccionada = new Date(hoy.getFullYear(), hoy.getMonth(), hoy.getDate());
let filtroActivo = 'pendientes';
let registros = [];
let visitaActual = null;

function pad(num) {
    return num < 10 ? '0' + num : String(num);
}

function formatearIso(anio, mes, dia) {
    return `${anio}-${pad(mes + 1)}-${pad(dia)}`;
}

function parsearFechaLocal(str) {
    if (!str || typeof str !== 'string') return null;
    const partes = str.split(' ')[0].split('-');
    if (partes.length < 3) return null;
    return new Date(Number(partes[0]), Number(partes[1]) - 1, Number(partes[2]));
}

function textoEstado(estado) {
    const mapa = {
        pendiente: 'PENDIENTE TÉCNICO',
        confirmado: 'TÉCNICO CONFIRMADO',
        reagendada: 'REAGENDADA',
        completada: 'VISITADO',
        visitado: 'VISITADO',
        cancelada: 'CANCELADA',
        vencida: 'VENCIDA'
    };
    return mapa[estado] || (estado ? estado.toUpperCase() : 'PENDIENTE TÉCNICO');
}

function claseChipEstado(estado) {
    const norm = (estado || '').toLowerCase();
    if (norm === 'confirmado') return 'agenda-chip-confirmado';
    if (norm === 'reagendada') return 'agenda-chip-reagendada';
    if (norm === 'completada' || norm === 'visitado') return 'agenda-chip-completada';
    if (norm === 'vencida') return 'agenda-chip-vencida';
    if (norm === 'cancelada') return 'agenda-chip-cancelada';
    return 'agenda-chip-pendiente';
}

function obtenerListaActiva() {
    return registros.filter((r) => {
        const est = (r.estado_agenda || '').toLowerCase();
        return filtroActivo === 'pendientes'
            ? ESTADOS_PENDIENTES.includes(est)
            : ESTADOS_VISITADOS.includes(est);
    });
}

function actualizarPillsYConteo() {
    const listaPendientes = registros.filter((r) => {
        const f = parsearFechaLocal(r.fecha_agendamiento);
        return f && f.getFullYear() === anioVisible && f.getMonth() === mesVisible &&
            ESTADOS_PENDIENTES.includes((r.estado_agenda || '').toLowerCase());
    });
    const listaVisitados = registros.filter((r) => {
        const f = parsearFechaLocal(r.fecha_agendamiento);
        return f && f.getFullYear() === anioVisible && f.getMonth() === mesVisible &&
            ESTADOS_VISITADOS.includes((r.estado_agenda || '').toLowerCase());
    });

    pillPendientes.textContent = `Agenda (${listaPendientes.length})`;
    pillVisitados.textContent = `Visitados (${listaVisitados.length})`;

    pillPendientes.classList.toggle('active', filtroActivo === 'pendientes');
    pillVisitados.classList.toggle('active', filtroActivo === 'visitados');
}

function renderizarCalendario() {
    tvMesActual.textContent = `${MESES[mesVisible].toUpperCase()} ${anioVisible}`;
    calendarGrid.innerHTML = '';

    const fechaSeleccionadaIso = fechaSeleccionada
        ? formatearIso(fechaSeleccionada.getFullYear(), fechaSeleccionada.getMonth(), fechaSeleccionada.getDate())
        : null;

    const diasConVisitas = new Set();
    obtenerListaActiva().forEach((r) => {
        const f = parsearFechaLocal(r.fecha_agendamiento);
        if (f && f.getFullYear() === anioVisible && f.getMonth() === mesVisible) {
            diasConVisitas.add(formatearIso(f.getFullYear(), f.getMonth(), f.getDate()));
        }
    });

    // 1. Días del mes anterior (out-dates e.g. 31)
    const primerDiaSemana = new Date(anioVisible, mesVisible, 1).getDay();
    const desfaseLunes = (primerDiaSemana + 6) % 7;
    const diasEnMesAnterior = new Date(anioVisible, mesVisible, 0).getDate();
    const mesAnterior = mesVisible === 0 ? 11 : mesVisible - 1;
    const anioMesAnterior = mesVisible === 0 ? anioVisible - 1 : anioVisible;

    for (let i = desfaseLunes - 1; i >= 0; i--) {
        const numDia = diasEnMesAnterior - i;
        const fechaIso = formatearIso(anioMesAnterior, mesAnterior, numDia);
        const celda = crearCeldaDia(numDia, fechaIso, true, false);
        celda.addEventListener('click', () => {
            anioVisible = anioMesAnterior;
            mesVisible = mesAnterior;
            fechaSeleccionada = new Date(anioMesAnterior, mesAnterior, numDia);
            actualizarPillsYConteo();
            renderizarCalendario();
            renderizarLista();
        });
        calendarGrid.appendChild(celda);
    }

    // 2. Días del mes visible (1 al 30/31)
    const diasEnMes = new Date(anioVisible, mesVisible + 1, 0).getDate();
    for (let dia = 1; dia <= diasEnMes; dia++) {
        const fechaIso = formatearIso(anioVisible, mesVisible, dia);
        const esSeleccionado = fechaIso === fechaSeleccionadaIso;
        const tieneVisitas = diasConVisitas.has(fechaIso);
        const celda = crearCeldaDia(dia, fechaIso, false, esSeleccionado, tieneVisitas);

        celda.addEventListener('click', () => {
            fechaSeleccionada = new Date(anioVisible, mesVisible, dia);
            renderizarCalendario();
            renderizarLista();

            const targetHeader = document.getElementById(`grupo-fecha-${fechaIso}`);
            if (targetHeader) {
                targetHeader.scrollIntoView({ behavior: 'smooth', block: 'nearest' });
                targetHeader.classList.remove('highlight');
                void targetHeader.offsetWidth;
                targetHeader.classList.add('highlight');
            }
        });

        calendarGrid.appendChild(celda);
    }

    // 3. Días del mes siguiente (out-dates e.g. 1, 2, 3, 4)
    const ultimoDiaSemana = (new Date(anioVisible, mesVisible, diasEnMes).getDay() + 6) % 7;
    const diasRestantesFila = 6 - ultimoDiaSemana;
    const mesSiguiente = mesVisible === 11 ? 0 : mesVisible + 1;
    const anioMesSiguiente = mesVisible === 11 ? anioVisible + 1 : anioVisible;

    for (let dia = 1; dia <= diasRestantesFila; dia++) {
        const fechaIso = formatearIso(anioMesSiguiente, mesSiguiente, dia);
        const celda = crearCeldaDia(dia, fechaIso, true, false);
        celda.addEventListener('click', () => {
            anioVisible = anioMesSiguiente;
            mesVisible = mesSiguiente;
            fechaSeleccionada = new Date(anioMesSiguiente, mesSiguiente, dia);
            actualizarPillsYConteo();
            renderizarCalendario();
            renderizarLista();
        });
        calendarGrid.appendChild(celda);
    }
}

function crearCeldaDia(numero, fechaIso, esOtroMes, esSeleccionado, tieneVisitas = false) {
    const celda = document.createElement('div');
    celda.className = 'agenda-day-cell';
    if (esOtroMes) celda.classList.add('other-month');
    if (esSeleccionado) celda.classList.add('selected');
    if (tieneVisitas) celda.classList.add('has-events');

    const texto = document.createElement('span');
    texto.className = 'agenda-day-text';
    texto.textContent = numero;
    celda.appendChild(texto);

    const badge = document.createElement('span');
    badge.className = 'agenda-day-badge';
    celda.appendChild(badge);

    return celda;
}

function renderizarLista() {
    listaAgenda.innerHTML = '';
    const activa = obtenerListaActiva();

    const visitasDelMes = activa.filter((r) => {
        const f = parsearFechaLocal(r.fecha_agendamiento);
        return f && f.getFullYear() === anioVisible && f.getMonth() === mesVisible;
    });

    if (sectionTitle) {
        sectionTitle.textContent = `Visitas de ${MESES[mesVisible]} ${anioVisible}`;
    }
    if (eventsCount) {
        eventsCount.textContent = `${visitasDelMes.length} visita${visitasDelMes.length === 1 ? '' : 's'}`;
    }

    if (visitasDelMes.length === 0) {
        if (filtroActivo === 'visitados') {
            if (vacioTitulo) vacioTitulo.textContent = 'Sin visitas completadas';
            if (vacioSub) vacioSub.textContent = 'No hay registros de visitas finalizadas en este mes.';
        } else {
            if (vacioTitulo) vacioTitulo.textContent = 'Sin visitas agendadas';
            if (vacioSub) vacioSub.textContent = 'No hay actividades programadas para este mes.';
        }
        vacio.style.display = 'flex';
        return;
    }
    vacio.style.display = 'none';

    // Agrupar visitas por fecha
    const grupos = {};
    visitasDelMes.forEach((r) => {
        const clave = r.fecha_agendamiento ? r.fecha_agendamiento.split(' ')[0] : 'sin_fecha';
        if (!grupos[clave]) grupos[clave] = [];
        grupos[clave].push(r);
    });

    Object.keys(grupos).sort().forEach((claveFecha) => {
        const visitasDelDia = grupos[claveFecha];
        const f = parsearFechaLocal(claveFecha);

        if (f) {
            const encabezado = document.createElement('div');
            encabezado.className = 'agenda-date-group-header';
            encabezado.id = `grupo-fecha-${claveFecha}`;
            encabezado.innerHTML = `
                <span class="agenda-header-day">${f.getDate()}</span>
                <span class="agenda-header-month">${MESES_ABREV[f.getMonth()]}</span>
                <span class="agenda-header-weekday">${DIAS_SEMANA_ABREV[f.getDay()]}</span>
            `;
            listaAgenda.appendChild(encabezado);
        }

        visitasDelDia.forEach((r, idx) => {
            const card = document.createElement('div');
            card.className = 'agenda-event-card';
            card.style.animationDelay = `${Math.min(idx, 5) * 0.03}s`;
            const horaStr = r.hora ? r.hora.substring(0, 5) : 'Por definir';
            const estadoStr = textoEstado(r.estado_agenda);
            const chipClass = claseChipEstado(r.estado_agenda);
            const titulo = r.pdv || r.titulo || r.contacto || 'Visita Técnica';
            const empresa = r.empresa || 'Sin empresa';
            const direccion = r.direccion || r.lugar || 'Dirección no registrada';

            card.innerHTML = `
                <div class="agenda-time-col">
                    <div class="agenda-event-time">${horaStr}</div>
                    <div class="agenda-event-duration">45 min aprox.</div>
                </div>
                <div class="agenda-event-bar"></div>
                <div class="agenda-event-info">
                    <div class="agenda-event-title">${titulo}</div>
                    <div class="agenda-event-empresa">${empresa}</div>
                    <div class="agenda-event-desc">${direccion}</div>
                </div>
                <span class="agenda-chip-status ${chipClass}">${estadoStr}</span>
            `;

            card.addEventListener('click', () => abrirModalDetalle(r));
            listaAgenda.appendChild(card);
        });
    });
}

// Ventanita: Selector de Mes
function abrirSelectorMes() {
    anioSelector = anioVisible;
    renderizarGridSelectorMeses();
    modalSelectorMes.classList.add('open');
}

function renderizarGridSelectorMeses() {
    selectorMesTitulo.textContent = anioSelector;
    gridSelectorMeses.innerHTML = '';

    MESES.forEach((nombre, idx) => {
        const btn = document.createElement('button');
        btn.type = 'button';
        btn.className = 'btn-mes-item';
        if (anioSelector === anioVisible && idx === mesVisible) {
            btn.classList.add('active');
        }
        btn.textContent = MESES_ABREV[idx];
        btn.addEventListener('click', () => {
            anioVisible = anioSelector;
            mesVisible = idx;
            fechaSeleccionada = new Date(anioVisible, mesVisible, 1);
            modalSelectorMes.classList.remove('open');
            actualizarPillsYConteo();
            renderizarCalendario();
            renderizarLista();
        });
        gridSelectorMeses.appendChild(btn);
    });
}

tvMesActual.addEventListener('click', abrirSelectorMes);
btnCerrarSelectorMes.addEventListener('click', () => modalSelectorMes.classList.remove('open'));
modalSelectorMes.addEventListener('click', (ev) => {
    if (ev.target === modalSelectorMes) modalSelectorMes.classList.remove('open');
});

btnSelectorAnioAnt.addEventListener('click', () => {
    anioSelector--;
    renderizarGridSelectorMeses();
});

btnSelectorAnioSig.addEventListener('click', () => {
    anioSelector++;
    renderizarGridSelectorMeses();
});

// Ventanita: Detalle de Visita
function abrirModalDetalle(r) {
    visitaActual = r;
    modalTitulo.textContent = r.pdv || r.titulo || 'Visita Técnica';
    modalRegistrado.textContent = r.fecha_agendamiento ? `Agendamiento: ${r.fecha_agendamiento}` : '';

    const estadoNorm = (r.estado_agenda || '').toLowerCase();
    const claseBadge = estadoNorm === 'visitado' ? 'completada' : (estadoNorm || 'pendiente');
    modalBadgeEstado.textContent = textoEstado(r.estado_agenda);
    modalBadgeEstado.className = `badge badge-${claseBadge}`;

    modalFechaValor.textContent = r.fecha_agendamiento ? r.fecha_agendamiento.split(' ')[0] : 'Por definir';
    modalHoraValor.textContent = r.hora ? r.hora.substring(0, 5) : 'Por definir';

    modalContactoValor.textContent = r.contacto || '—';
    modalEmpresaValor.textContent = r.empresa || '—';

    // Teléfono
    const tel = r.telefono || r.telefono_convencional || '';
    if (tel) {
        modalTelefonoValor.textContent = tel;
        modalLinkTelefono.href = `tel:${tel.replace(/\s+/g, '')}`;
        modalLinkTelefono.style.display = 'flex';
    } else {
        modalTelefonoValor.textContent = 'Sin teléfono';
        modalLinkTelefono.href = '#';
    }

    // Correo
    if (r.mail) {
        modalCorreoValor.textContent = r.mail;
        modalLinkCorreo.href = `mailto:${r.mail}`;
        modalLinkCorreo.style.display = 'flex';
    } else {
        modalCorreoValor.textContent = 'Sin correo';
        modalLinkCorreo.href = '#';
    }

    // Dirección con Google Maps
    const dir = r.direccion || r.lugar || '';
    if (dir) {
        modalDireccionValor.textContent = dir;
        modalLinkDireccion.href = `https://www.google.com/maps/search/?api=1&query=${encodeURIComponent(dir)}`;
        modalLinkDireccion.style.display = 'flex';
    } else {
        modalDireccionValor.textContent = 'Sin dirección';
        modalLinkDireccion.href = '#';
    }

    modalPdvValor.textContent = r.pdv || r.codigo_pdv || '—';

    // Precargar formularios
    modalFormEditar.contacto.value = r.contacto || '';
    modalFormEditar.empresa.value = r.empresa || '';
    modalFormEditar.telefono.value = r.telefono || '';
    modalFormEditar.mail.value = r.mail || '';
    modalFormEditar.direccion.value = r.direccion || '';

    // Resetear switches y acordeones
    switchModoEdicion.checked = false;
    modalFormEditar.classList.remove('active');
    modalInfoLectura.style.display = 'block';
    modalFormReagendar.style.display = 'none';

    modalDetalle.classList.add('open');
}

function hayCambiosSinGuardar() {
    if (!visitaActual || !switchModoEdicion.checked) return false;
    return (
        modalFormEditar.contacto.value.trim() !== (visitaActual.contacto || '').trim() ||
        modalFormEditar.empresa.value.trim() !== (visitaActual.empresa || '').trim() ||
        modalFormEditar.telefono.value.trim() !== (visitaActual.telefono || '').trim() ||
        modalFormEditar.mail.value.trim() !== (visitaActual.mail || '').trim() ||
        modalFormEditar.direccion.value.trim() !== (visitaActual.direccion || '').trim()
    );
}

function intentarCerrarModalDetalle() {
    if (hayCambiosSinGuardar()) {
        modalConfirmarDescartar.classList.add('open');
    } else {
        cerrarModalDetalle();
    }
}

function cerrarModalDetalle() {
    modalDetalle.classList.remove('open');
    visitaActual = null;
}

modalBtnCerrar.addEventListener('click', intentarCerrarModalDetalle);
modalDetalle.addEventListener('click', (ev) => {
    if (ev.target === modalDetalle) intentarCerrarModalDetalle();
});

// Descartar cambios popup
btnQuedarseEditar.addEventListener('click', () => modalConfirmarDescartar.classList.remove('open'));
btnSalirDescartar.addEventListener('click', () => {
    modalConfirmarDescartar.classList.remove('open');
    cerrarModalDetalle();
});

// Switch de modo edición
switchModoEdicion.addEventListener('change', () => {
    const editando = switchModoEdicion.checked;
    modalFormEditar.classList.toggle('active', editando);
    modalInfoLectura.style.display = editando ? 'none' : 'block';
});

btnToggleReagendar.addEventListener('click', () => {
    const visible = modalFormReagendar.style.display !== 'none';
    modalFormReagendar.style.display = visible ? 'none' : 'block';
    if (!visible && visitaActual) {
        if (visitaActual.fecha_agendamiento) {
            document.getElementById('reagendar-fecha').value = visitaActual.fecha_agendamiento.split(' ')[0];
        }
        if (visitaActual.hora) {
            document.getElementById('reagendar-hora').value = visitaActual.hora.substring(0, 5);
        }
    }
});

// Guardar edición
modalFormEditar.addEventListener('submit', async (ev) => {
    ev.preventDefault();
    if (!visitaActual) return;
    await ejecutarAccion({
        id: visitaActual.id,
        accion: 'editar',
        contacto: modalFormEditar.contacto.value.trim(),
        empresa: modalFormEditar.empresa.value.trim(),
        mail: modalFormEditar.mail.value.trim(),
        telefono: modalFormEditar.telefono.value.trim(),
        direccion: modalFormEditar.direccion.value.trim()
    });
});

// Reagendar
modalFormReagendar.addEventListener('submit', async (ev) => {
    ev.preventDefault();
    if (!visitaActual) return;
    const fecha = document.getElementById('reagendar-fecha').value;
    const hora = document.getElementById('reagendar-hora').value;
    await ejecutarAccion({
        id: visitaActual.id,
        accion: 'reagendar',
        fecha_agendamiento: fecha,
        hora: hora
    });
});

// Eliminar agendamiento popup
btnEliminarVisita.addEventListener('click', () => {
    if (!visitaActual) return;
    modalConfirmarEliminar.classList.add('open');
});

btnCancelarEliminar.addEventListener('click', () => modalConfirmarEliminar.classList.remove('open'));
btnConfirmarEliminarAccion.addEventListener('click', async () => {
    modalConfirmarEliminar.classList.remove('open');
    if (!visitaActual) return;
    await ejecutarAccion({ id: visitaActual.id, accion: 'eliminar' });
});

async function ejecutarAccion(body) {
    mostrarError(alerta, null);
    try {
        const resp = await Api.post('../getters/update_agenda.php', body);
        if (resp.success) {
            cerrarModalDetalle();
            await cargarAgenda();
        } else {
            alert(resp.message || 'No se pudo completar la acción.');
        }
    } catch (e) {
        alert('Error de conexión con el servidor.');
    }
}

function animarCuadricula(direccion) {
    calendarGrid.classList.remove('slide-left', 'slide-right');
    void calendarGrid.offsetWidth;
    calendarGrid.classList.add(direccion === 'siguiente' ? 'slide-left' : 'slide-right');
}

// Flechas navegación de mes con animación direccional de guía
btnMesAnterior.addEventListener('click', () => {
    mesVisible--;
    if (mesVisible < 0) {
        mesVisible = 11;
        anioVisible--;
    }
    fechaSeleccionada = new Date(anioVisible, mesVisible, 1);
    actualizarPillsYConteo();
    renderizarCalendario();
    animarCuadricula('anterior');
    renderizarLista();
});

btnMesSiguiente.addEventListener('click', () => {
    mesVisible++;
    if (mesVisible > 11) {
        mesVisible = 0;
        anioVisible++;
    }
    fechaSeleccionada = new Date(anioVisible, mesVisible, 1);
    actualizarPillsYConteo();
    renderizarCalendario();
    animarCuadricula('siguiente');
    renderizarLista();
});

// Píldoras
pillPendientes.addEventListener('click', () => {
    if (filtroActivo === 'pendientes') return;
    filtroActivo = 'pendientes';
    actualizarPillsYConteo();
    renderizarCalendario();
    renderizarLista();
});

pillVisitados.addEventListener('click', () => {
    if (filtroActivo === 'visitados') return;
    filtroActivo = 'visitados';
    actualizarPillsYConteo();
    renderizarCalendario();
    renderizarLista();
});

async function cargarAgenda() {
    try {
        const resp = await Api.get('../getters/get_agenda.php');
        if (resp.success) {
            registros = resp.data || [];
            actualizarPillsYConteo();
            renderizarCalendario();
            renderizarLista();
        } else {
            mostrarError(alerta, resp.message || 'No se pudo cargar la agenda.');
        }
    } catch (e) {
        mostrarError(alerta, 'No se pudo cargar la agenda.');
    }
}

// 1. Render inicial inmediato sincrónico
actualizarPillsYConteo();
renderizarCalendario();
renderizarLista();

// 2. Cargar visitas del backend
cargarAgenda();
