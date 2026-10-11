const listaProforma = document.getElementById('lista-proforma');
const vacioProforma = document.getElementById('vacio');
const alertaProforma = document.getElementById('alerta');
const tplAgendamiento = document.getElementById('tpl-agendamiento');
const tplRonda = document.getElementById('tpl-ronda');
const buscadorEmpresa = document.getElementById('buscador-empresa');
const pillsFiltro = document.getElementById('pills-filtro');
const dialogCerrar = document.getElementById('dialog-cerrar');
const textareaCerrar = document.getElementById('cerrar-motivo');
const contadorCerrar = document.getElementById('cerrar-contador');
const formCerrarDialog = document.getElementById('form-cerrar-dialog');

// Mismo contenedor Azure Blob que ya sirve las fotos al app/Proyectos2.
const BLOB_BASE_URL = 'https://luckyecuadorweb.blob.core.windows.net/app/AppPintuco/Inserts/';
const CLAVE_ABIERTA = 'proforma:abierta';
const MESES = ['ENE', 'FEB', 'MAR', 'ABR', 'MAY', 'JUN', 'JUL', 'AGO', 'SEP', 'OCT', 'NOV', 'DIC'];

let datosAgendamientos = [];
let datosProformas = [];
let filtroActual = 'activos';
let idProformaParaCerrar = null;

// La tarjeta abierta sobrevive a una recarga: el celular puede recargar la página al volver de la cámara.
let idAbierta = null;
try { idAbierta = sessionStorage.getItem(CLAVE_ABIERTA); } catch (e) { idAbierta = null; }

function recordarAbierta(id) {
    idAbierta = id;
    try {
        if (id) sessionStorage.setItem(CLAVE_ABIERTA, id); else sessionStorage.removeItem(CLAVE_ABIERTA);
    } catch (e) { /* sin storage: solo se pierde el recordatorio */ }
}

function badgeProforma(estado) {
    const mapa = {
        en_proceso: 'En proceso', correccion_solicitada: 'Corrección solicitada',
        rechazado: 'Cerrado', aprobado: 'Aprobado', realizado: 'Realizado',
        en_negociacion: 'En negociación', pendiente: 'Sin proforma',
    };
    return mapa[estado] || estado || 'Sin proforma';
}

// Activos = obra con ciclo abierto; Completados = ya facturada o cerrada.
function categoriaDeAgendamiento(ag) {
    const rondas = datosProformas.filter((p) => p.id_agendamiento == ag.id);
    const ultima = rondas[0]; // ya viene ordenado por id DESC
    const cerrada = rondas.some((r) => (r.motivo_cierre || '').trim() !== '') || (!!ultima && ultima.estado_proforma === 'rechazado');
    return cerrada || !!(ultima && ultima.foto_factura) ? 'completados' : 'activos';
}

function aplicarRespuestaProforma(resp) {
    if (!resp.success) return;
    datosAgendamientos = resp.agendamientos;
    datosProformas = resp.proformas;
    aplicarFiltrosYRenderizar();
}

// conCache: al entrar a la página muestra al instante lo último que se vio y luego lo refresca desde el servidor.
async function cargarProforma(conCache = false) {
    const url = '../getters/get_proforma.php';
    try {
        if (conCache) await Api.getConCache(url, aplicarRespuestaProforma);
        else aplicarRespuestaProforma(await Api.get(url));
    } catch (e) {
        mostrarError(alertaProforma, e.message);
    }
}

function aplicarFiltrosYRenderizar() {
    const conteos = { activos: 0, completados: 0 };
    datosAgendamientos.forEach((ag) => { conteos[categoriaDeAgendamiento(ag)]++; });
    pillsFiltro.querySelectorAll('[data-contador]').forEach((span) => {
        span.textContent = `· ${conteos[span.dataset.contador]}`;
    });

    const termino = buscadorEmpresa.value.trim().toLowerCase();
    const visibles = datosAgendamientos.filter((ag) => {
        if (categoriaDeAgendamiento(ag) !== filtroActual) return false;
        if (!termino) return true;
        const texto = `${ag.empresa || ''} ${ag.contacto || ''}`.toLowerCase();
        return texto.includes(termino);
    });

    renderProforma(visibles, datosProformas);
}

pillsFiltro.addEventListener('click', (ev) => {
    const boton = ev.target.closest('[data-filtro]');
    if (!boton) return;
    filtroActual = boton.dataset.filtro;
    pillsFiltro.querySelectorAll('.pill').forEach((p) => p.classList.toggle('active', p === boton));
    aplicarFiltrosYRenderizar();
});

buscadorEmpresa.addEventListener('input', aplicarFiltrosYRenderizar);

function pintarFecha(nodo, fechaISO) {
    const dia = nodo.querySelector('[data-campo="dia"]');
    const mes = nodo.querySelector('[data-campo="mes"]');
    if (!fechaISO) {
        dia.textContent = '—';
        mes.textContent = 'S/F';
        return;
    }
    const [, m, d] = fechaISO.split('-');
    dia.textContent = d;
    mes.textContent = MESES[parseInt(m, 10) - 1];
}

const dialogVer = document.getElementById('dialog-ver');
const verFoto = document.getElementById('ver-foto');
const verImg = document.getElementById('ver-img');
const verSinFoto = document.getElementById('ver-sin-foto');
const FORMATO_MONEDA = new Intl.NumberFormat('en-US', { style: 'currency', currency: 'USD' });

// El monto propuesto vive en monto_validado (el analista lo reemplaza al validar); las rondas viejas lo traen en monto_total_factura.
function montoDeRonda(ronda) {
    return ronda.monto_validado || ronda.monto_total_factura;
}

function formatearMonto(valor) {
    const numero = parseFloat(valor);
    return numero > 0 ? FORMATO_MONEDA.format(numero) : '—';
}

function mostrarSinFoto(texto) {
    verFoto.hidden = true;
    document.getElementById('ver-sin-foto-texto').textContent = texto;
    verSinFoto.hidden = false;
}

function cargarFotoVer(evidencia) {
    verFoto.hidden = false;
    verSinFoto.hidden = true;
    verFoto.classList.remove('is-cargada');
    verImg.removeAttribute('src');
    if (!evidencia) {
        mostrarSinFoto('Sin evidencia fotográfica.');
        return;
    }
    verImg.onload = () => verFoto.classList.add('is-cargada');
    verImg.onerror = () => mostrarSinFoto('Foto no disponible.');
    verImg.src = BLOB_BASE_URL + evidencia;
}

// Abre el detalle en la misma pantalla; el aviso de la analista solo aparece si hay observaciones o corrección pedida.
function abrirVerProforma(ronda, numero) {
    document.getElementById('ver-titulo').textContent = `Proforma ${numero}`;
    document.getElementById('ver-fecha').textContent = formatearFecha(ronda.fecha_proforma);
    const badge = document.getElementById('ver-badge');
    badge.textContent = badgeProforma(ronda.estado_proforma);
    badge.className = 'badge badge-' + ronda.estado_proforma;

    document.getElementById('ver-monto-label').textContent = ronda.fecha_auditoria ? 'Monto validado' : 'Monto propuesto';
    document.getElementById('ver-monto').textContent = formatearMonto(montoDeRonda(ronda));
    document.getElementById('ver-detalle').textContent = ronda.caracteristica_visita || '—';

    const observaciones = (ronda.observaciones_auditoria || '').trim();
    const textoAviso = observaciones || (ronda.estado_proforma === 'correccion_solicitada' ? 'La analista pidió repetir la evidencia.' : '');
    document.getElementById('ver-aviso').hidden = !textoAviso;
    document.getElementById('ver-aviso-texto').textContent = textoAviso;

    cargarFotoVer(ronda.evidencia);
    dialogVer.showModal();
}

document.getElementById('ver-cerrar').addEventListener('click', () => dialogVer.close());
document.getElementById('ver-cerrar-pie').addEventListener('click', () => dialogVer.close());
dialogVer.addEventListener('click', (ev) => { if (ev.target === dialogVer) dialogVer.close(); });
verFoto.addEventListener('click', () => abrirZoomFoto(verImg.src));

function pintarRondas(nodo, rondasCronologico) {
    const contRondas = nodo.querySelector('[data-campo="rondas"]');
    nodo.querySelector('[data-campo="sin-rondas"]').hidden = rondasCronologico.length > 0;
    rondasCronologico.forEach((r, i) => {
        const rondaNodo = tplRonda.content.cloneNode(true);
        rondaNodo.querySelector('[data-campo="numero"]').textContent = i + 1;
        rondaNodo.querySelector('[data-campo="titulo"]').textContent = `Proforma ${i + 1}`;
        rondaNodo.querySelector('[data-campo="fecha"]').textContent = formatearFecha(r.fecha_proforma);
        const b = rondaNodo.querySelector('[data-campo="badge"]');
        b.textContent = badgeProforma(r.estado_proforma);
        b.classList.add('badge-' + r.estado_proforma);
        rondaNodo.querySelector('[data-campo="ver"]').addEventListener('click', () => abrirVerProforma(r, i + 1));
        const detalleTexto = [];
        if (montoDeRonda(r)) detalleTexto.push(`Monto: ${montoDeRonda(r)}`);
        if (r.plazo_meses) detalleTexto.push(`Plazo: ${r.plazo_meses} meses`);
        if (r.motivo_cierre) detalleTexto.push(`Motivo cierre: ${r.motivo_cierre}`);
        const detalle = rondaNodo.querySelector('[data-campo="detalle"]');
        detalle.textContent = detalleTexto.join(' · ');
        detalle.hidden = detalleTexto.length === 0;
        contRondas.appendChild(rondaNodo);
    });
}

function renderProforma(agendamientos, proformas) {
    listaProforma.innerHTML = '';
    vacioProforma.style.display = agendamientos.length ? 'none' : 'block';
    agendamientos.forEach((ag) => listaProforma.appendChild(construirTarjeta(ag, proformas)));
}

function construirTarjeta(ag, proformas) {
    const rondas = proformas.filter((p) => p.id_agendamiento == ag.id);
    const ultima = rondas[0]; // ya viene ordenado por id DESC
    const rondasCronologico = [...rondas].reverse();

    const proformaCerrada = rondas.some((r) => (r.motivo_cierre || '').trim() !== '') || (!!ultima && ultima.estado_proforma === 'rechazado');
    const facturaEnviada = !!(ultima && ultima.foto_factura);
    const cicloAbierto = !proformaCerrada && !facturaEnviada;
    const puedeFacturar = cicloAbierto && !!(ultima && ultima.evidencia);

    const nodo = tplAgendamiento.content.cloneNode(true);
    const card = nodo.querySelector('.ag-card');
    card.dataset.id = ag.id;
    pintarFecha(nodo, ag.fecha_agendamiento);
    nodo.querySelector('[data-campo="empresa"]').textContent = ag.empresa || ag.contacto || '—';
    nodo.querySelector('[data-campo="pdv"]').textContent = ag.pdv || '(sin PDV)';

    const badge = nodo.querySelector('[data-campo="badge"]');
    let claveBadge = ultima ? ultima.estado_proforma : 'pendiente';
    let textoBadge = badgeProforma(claveBadge);
    if (facturaEnviada) { claveBadge = 'completado'; textoBadge = 'Facturada'; }
    if (proformaCerrada && !facturaEnviada) { claveBadge = 'cerrado'; textoBadge = 'Cerrada'; }
    badge.textContent = textoBadge;
    badge.classList.add('badge-' + claveBadge);

    pintarRondas(nodo, rondasCronologico);

    // Alerta ámbar: el auditor pidió corrección o cerró la ronda por calidad.
    if (ultima && (ultima.estado_proforma === 'correccion_solicitada' || ultima.estado_proforma === 'rechazado')) {
        const numeroUltima = rondasCronologico.length;
        const texto = ultima.estado_proforma === 'correccion_solicitada'
            ? `Corrección solicitada en la Proforma ${numeroUltima} (${formatearFecha(ultima.fecha_proforma)}). Vuelve a tomar la evidencia.`
            : `Esta obra fue cerrada (Proforma ${numeroUltima}, ${formatearFecha(ultima.fecha_proforma)}).`;
        nodo.querySelector('[data-campo="alerta-texto"]').textContent = texto;
        nodo.querySelector('[data-campo="alerta-correccion"]').hidden = false;
    }

    // --- Acordeón ---
    const cabecera = nodo.querySelector('[data-campo="cabecera"]');
    const detalle = nodo.querySelector('[data-campo="detalle"]');
    const abrirTarjeta = (abrir) => {
        detalle.hidden = !abrir;
        card.classList.toggle('open', abrir);
        cabecera.setAttribute('aria-expanded', String(abrir));
    };
    abrirTarjeta(String(ag.id) === String(idAbierta));
    cabecera.addEventListener('click', () => {
        const abrir = detalle.hidden;
        document.querySelectorAll('.ag-card.open').forEach((c) => {
            if (c !== card) {
                c.classList.remove('open');
                c.querySelector('[data-campo="detalle"]').hidden = true;
                c.querySelector('[data-campo="cabecera"]').setAttribute('aria-expanded', 'false');
            }
        });
        abrirTarjeta(abrir);
        recordarAbierta(abrir ? String(ag.id) : null);
    });

    // --- Acciones: los formularios se abren en línea, uno a la vez ---
    const btnNuevaRonda = nodo.querySelector('[data-campo="btn-nueva-ronda"]');
    const btnEnviarFactura = nodo.querySelector('[data-campo="btn-enviar-factura"]');
    const hintFactura = nodo.querySelector('[data-campo="hint-factura"]');
    const acciones = nodo.querySelector('[data-campo="acciones"]');
    const formNueva = nodo.querySelector('[data-form="nueva-ronda"]');

    acciones.hidden = !cicloAbierto;
    btnEnviarFactura.disabled = !puedeFacturar;
    hintFactura.hidden = !cicloAbierto || puedeFacturar;

    const mostrarFormulario = (form) => {
        formNueva.hidden = form !== formNueva;
        acciones.hidden = !!form || !cicloAbierto;
        hintFactura.hidden = !!form || !cicloAbierto || puedeFacturar;
        if (form) form.scrollIntoView({ behavior: 'smooth', block: 'nearest' });
    };

    const claveRonda = `ronda-${ag.id}`;
    const campoFotoRonda = formNueva.querySelector('[data-campo="foto"]');
    const errorFoto = (mensaje) => mostrarError(alertaProforma, mensaje);
    activarCampoFoto(campoFotoRonda, claveRonda, errorFoto);

    btnNuevaRonda.addEventListener('click', () => mostrarFormulario(formNueva));
    const rondaCotizada = rondas.find((r) => parseFloat(r.monto_validado) > 0) || rondas.find((r) => parseFloat(montoDeRonda(r)) > 0);
    const abrirFactura = () => abrirFacturaEnvio({
        ag,
        ultima,
        totalRondas: rondas.length,
        cotizacion: rondaCotizada ? parseFloat(montoDeRonda(rondaCotizada)) : 0,
        alEnviar: cargarProforma,
    });
    btnEnviarFactura.addEventListener('click', abrirFactura);
    nodo.querySelector('[data-campo="cancelar-ronda"]').addEventListener('click', () => {
        formNueva.reset();
        limpiarCampoFoto(campoFotoRonda, claveRonda);
        mostrarFormulario(null);
    });
    // Una foto recuperada tras recargar deja abierto su formulario para no perderla de vista.
    if (campoFotoRonda.fotoBase64 && cicloAbierto) mostrarFormulario(formNueva);
    else if (puedeFacturar && String(ag.id) === String(idAbierta) && leerBorradorFoto(`factura-${ag.id}`)) abrirFactura();

    // --- Nueva ronda ---
    formNueva.addEventListener('submit', async (ev) => {
        ev.preventDefault();
        mostrarError(alertaProforma, null);
        if (!campoFotoRonda.fotoBase64) {
            mostrarError(alertaProforma, 'La evidencia fotográfica es obligatoria.');
            return;
        }
        const boton = formNueva.querySelector('button[type="submit"]');
        boton.disabled = true;
        try {
            const resp = await Api.post('../getters/insert_proforma.php', {
                id_agendamiento: ag.id,
                codigo_pdv: ag.codigo_pdv,
                estado_proforma: 'en_proceso',
                caracteristica_visita: formNueva.caracteristica_visita.value.trim(),
                evidencia_base64: campoFotoRonda.fotoBase64,
                monto_total_factura: formNueva.monto_total_factura.value,
            });
            if (resp.success) {
                borrarBorradorFoto(claveRonda);
                mostrarToast('Proforma guardada correctamente.');
                await cargarProforma();
            } else {
                mostrarError(alertaProforma, resp.message || 'No se pudo guardar la proforma.');
            }
        } catch (e) {
            mostrarError(alertaProforma, e.message);
        } finally {
            boton.disabled = false;
        }
    });

    // --- Cierre de proceso (diálogo compartido) ---
    const pie = nodo.querySelector('.ag-pie');
    pie.hidden = !cicloAbierto || !ultima;
    nodo.querySelector('[data-accion="abrir-cierre"]').addEventListener('click', () => {
        idProformaParaCerrar = ultima.id;
        textareaCerrar.value = '';
        contadorCerrar.textContent = '0';
        dialogCerrar.showModal();
    });

    return nodo;
}

textareaCerrar.addEventListener('input', () => {
    contadorCerrar.textContent = String(textareaCerrar.value.length);
});

document.getElementById('btn-cancelar-cerrar').addEventListener('click', () => {
    dialogCerrar.close();
});

formCerrarDialog.addEventListener('submit', async (ev) => {
    ev.preventDefault();
    const motivo = textareaCerrar.value.trim();
    if (!motivo) {
        textareaCerrar.focus();
        return;
    }
    const boton = document.getElementById('btn-confirmar-cerrar');
    boton.disabled = true;
    try {
        const resp = await Api.post('../getters/update_proforma.php', {
            id: idProformaParaCerrar, accion: 'rechazar', motivo_cierre: motivo,
        });
        if (resp.success) {
            dialogCerrar.close();
            mostrarToast('Proceso cerrado correctamente.');
            await cargarProforma();
        } else {
            mostrarError(alertaProforma, resp.message || (resp.stale ? 'El estado cambió, recarga.' : 'No se pudo cerrar.'));
            dialogCerrar.close();
        }
    } catch (e) {
        mostrarError(alertaProforma, e.message);
        dialogCerrar.close();
    } finally {
        boton.disabled = false;
    }
});

cargarProforma(true);
