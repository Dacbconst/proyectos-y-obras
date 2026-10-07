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

let datosAgendamientos = [];
let datosProformas = [];
let filtroActual = 'activos';
let idProformaParaCerrar = null;

function badgeProforma(estado) {
    const mapa = {
        en_proceso: 'En proceso', correccion_solicitada: 'Corrección solicitada',
        rechazado: 'Cerrado', aprobado: 'Aprobado', realizado: 'Realizado',
        en_negociacion: 'En negociación', pendiente: 'Sin proforma',
    };
    return mapa[estado] || estado || 'Sin proforma';
}

function categoriaDeAgendamiento(ag) {
    if (ag.estado_agenda === 'vencida') return 'vencidas';
    if (ag.estado_agenda === 'completada') return 'completados';
    return 'activos';
}

async function cargarProforma() {
    try {
        const resp = await Api.get('../getters/get_proforma.php');
        if (!resp.success) return;
        datosAgendamientos = resp.agendamientos;
        datosProformas = resp.proformas;
        aplicarFiltrosYRenderizar();
    } catch (e) {
        mostrarError(alertaProforma, 'No se pudo cargar la información.');
    }
}

function aplicarFiltrosYRenderizar() {
    const conteos = { activos: 0, vencidas: 0, completados: 0 };
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

function activarCampoFoto(campoFoto, input, dropzone, btnCambiar) {
    dropzone.addEventListener('click', () => input.click());
    btnCambiar.addEventListener('click', () => input.click());
    input.addEventListener('change', () => {
        if (!input.files || !input.files[0]) return;
        campoFoto.classList.add('has-photo');
        btnCambiar.style.display = 'inline';
        previsualizarFoto(input, campoFoto.querySelector('.photo-preview'));
    });
}

function renderProforma(agendamientos, proformas) {
    listaProforma.innerHTML = '';
    vacioProforma.style.display = agendamientos.length ? 'none' : 'block';

    agendamientos.forEach((ag) => {
        const rondas = proformas.filter((p) => p.id_agendamiento == ag.id);
        const ultima = rondas[0]; // ya viene ordenado por id DESC

        const nodo = tplAgendamiento.content.cloneNode(true);
        const card = nodo.querySelector('.card');
        card.dataset.id = ag.id;
        nodo.querySelector('[data-campo="fecha"]').textContent = ag.fecha_agendamiento ? formatearFecha(ag.fecha_agendamiento) : 'Sin fecha';
        nodo.querySelector('[data-campo="empresa"]').textContent = ag.empresa || ag.contacto || '—';
        nodo.querySelector('[data-campo="pdv"]').textContent = ag.pdv || '(sin PDV)';

        const badge = nodo.querySelector('[data-campo="badge"]');
        const estadoBadge = ultima ? ultima.estado_proforma : 'pendiente';
        badge.textContent = badgeProforma(estadoBadge);
        badge.classList.add('badge-' + estadoBadge);

        const detalle = nodo.querySelector('[data-campo="detalle"]');
        const contRondas = nodo.querySelector('[data-campo="rondas"]');
        // Se numera en orden cronológico ascendente, no por id DESC.
        const rondasCronologico = [...rondas].reverse();
        rondasCronologico.forEach((r, i) => {
            const rondaNodo = tplRonda.content.cloneNode(true);
            rondaNodo.querySelector('[data-campo="numero"]').textContent = i + 1;
            rondaNodo.querySelector('[data-campo="titulo"]').textContent = `Visita ${i + 1}`;
            rondaNodo.querySelector('[data-campo="fecha"]').textContent = formatearFecha(r.fecha_proforma);
            const b = rondaNodo.querySelector('[data-campo="badge"]');
            b.textContent = badgeProforma(r.estado_proforma);
            b.classList.add('badge-' + r.estado_proforma);
            const enlaceVer = rondaNodo.querySelector('[data-campo="ver"]');
            if (r.evidencia) {
                enlaceVer.href = BLOB_BASE_URL + r.evidencia;
                enlaceVer.style.display = 'inline-block';
            }
            const detalleTexto = [];
            if (r.monto_total_factura) detalleTexto.push(`Monto: ${r.monto_total_factura}`);
            if (r.plazo_meses) detalleTexto.push(`Plazo: ${r.plazo_meses} meses`);
            if (r.motivo_cierre) detalleTexto.push(`Motivo cierre: ${r.motivo_cierre}`);
            rondaNodo.querySelector('[data-campo="detalle"]').textContent = detalleTexto.join(' · ');
            contRondas.appendChild(rondaNodo);
        });

        // Alerta ámbar: reemplaza en visibilidad al botón normal cuando el
        // auditor pidió corrección o cerró la ronda por calidad.
        if (ultima && (ultima.estado_proforma === 'correccion_solicitada' || ultima.estado_proforma === 'rechazado')) {
            const alerta = nodo.querySelector('[data-campo="alerta-correccion"]');
            alerta.style.display = 'flex';
            const numeroUltima = rondasCronologico.length;
            const texto = ultima.estado_proforma === 'correccion_solicitada'
                ? `Corrección solicitada en Visita ${numeroUltima} (${formatearFecha(ultima.fecha_proforma)}). Vuelve a tomar la evidencia.`
                : `Esta obra fue cerrada (Visita ${numeroUltima}, ${formatearFecha(ultima.fecha_proforma)}).`;
            nodo.querySelector('[data-campo="alerta-texto"]').textContent = texto;
        }

        card.querySelector('.card-header').addEventListener('click', () => {
            const abierta = detalle.style.display !== 'none';
            detalle.style.display = abierta ? 'none' : 'block';
            card.classList.toggle('open', !abierta);
        });

        // --- Nueva ronda ---
        const formNueva = nodo.querySelector('[data-form="nueva-ronda"]');
        const inputFoto = formNueva.querySelector('input[name="evidencia"]');
        const campoFoto = formNueva.querySelector('.photo-field');
        activarCampoFoto(campoFoto, inputFoto, nodo.querySelector('[data-campo="dropzone"]'), nodo.querySelector('[data-campo="cambiar-foto"]'));

        formNueva.addEventListener('submit', async (ev) => {
            ev.preventDefault();
            mostrarError(alertaProforma, null);
            if (!inputFoto.files || !inputFoto.files[0]) {
                mostrarError(alertaProforma, 'La evidencia fotográfica es obligatoria.');
                return;
            }
            const boton = formNueva.querySelector('button[type="submit"]');
            boton.disabled = true;
            try {
                const evidenciaBase64 = await leerFotoComoBase64(inputFoto);
                const resp = await Api.post('../getters/insert_proforma.php', {
                    id_agendamiento: ag.id,
                    codigo_pdv: ag.codigo_pdv,
                    estado_proforma: 'en_proceso',
                    caracteristica_visita: formNueva.caracteristica_visita.value.trim(),
                    acompanamiento_tecnico: formNueva.acompanamiento_tecnico.value,
                    evidencia_base64: evidenciaBase64,
                    monto_total_factura: formNueva.monto_total_factura.value,
                });
                if (resp.success) {
                    await cargarProforma();
                } else {
                    mostrarError(alertaProforma, resp.message || 'No se pudo guardar la ronda.');
                }
            } catch (e) {
                mostrarError(alertaProforma, 'Error de conexión.');
            } finally {
                boton.disabled = false;
            }
        });

        // --- Factura ---
        const formFactura = nodo.querySelector('[data-form="confirmar-factura"]');
        const inputFotoFactura = formFactura.querySelector('input[name="foto_factura"]');
        const campoFotoFactura = formFactura.querySelector('.photo-field');
        activarCampoFoto(campoFotoFactura, inputFotoFactura, nodo.querySelector('[data-campo="dropzone-factura"]'), nodo.querySelector('[data-campo="cambiar-foto-factura"]'));

        formFactura.addEventListener('submit', async (ev) => {
            ev.preventDefault();
            mostrarError(alertaProforma, null);
            if (!ultima) {
                mostrarError(alertaProforma, 'Primero registra una ronda de proforma.');
                return;
            }
            const boton = formFactura.querySelector('button[type="submit"]');
            boton.disabled = true;
            try {
                const facturaBase64 = await leerFotoComoBase64(inputFotoFactura);
                const resp = await Api.post('../getters/confirmar_factura.php', {
                    id: ultima.id,
                    codigo_pdv: ag.codigo_pdv,
                    foto_factura_base64: facturaBase64,
                    estado_pago: formFactura.estado_pago.value,
                    monto_total_factura: formFactura.monto_total_factura.value,
                    plazo_meses: formFactura.plazo_meses.value,
                });
                if (resp.success) {
                    await cargarProforma();
                } else {
                    mostrarError(alertaProforma, resp.message || 'No se pudo confirmar la factura.');
                }
            } catch (e) {
                mostrarError(alertaProforma, 'Error de conexión.');
            } finally {
                boton.disabled = false;
            }
        });

        // --- Cierre de proceso (diálogo compartido) ---
        nodo.querySelector('[data-accion="abrir-cierre"]').addEventListener('click', () => {
            if (!ultima) {
                mostrarError(alertaProforma, 'Primero registra una ronda de proforma.');
                return;
            }
            idProformaParaCerrar = ultima.id;
            textareaCerrar.value = '';
            contadorCerrar.textContent = '0';
            dialogCerrar.showModal();
        });

        listaProforma.appendChild(nodo);
    });
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
            await cargarProforma();
        } else {
            mostrarError(alertaProforma, resp.message || (resp.stale ? 'El estado cambió, recarga.' : 'No se pudo cerrar.'));
            dialogCerrar.close();
        }
    } catch (e) {
        mostrarError(alertaProforma, 'Error de conexión.');
        dialogCerrar.close();
    } finally {
        boton.disabled = false;
    }
});

cargarProforma();
