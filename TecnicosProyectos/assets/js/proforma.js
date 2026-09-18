const listaProforma = document.getElementById('lista-proforma');
const vacioProforma = document.getElementById('vacio');
const alertaProforma = document.getElementById('alerta');
const tplAgendamiento = document.getElementById('tpl-agendamiento');
const tplRonda = document.getElementById('tpl-ronda');

// Mismo contenedor Azure Blob que ya sirve las fotos al app/Proyectos2.
const BLOB_BASE_URL = 'https://luckyecuadorweb.blob.core.windows.net/app/AppPintuco/Inserts/';

function badgeProforma(estado) {
    const mapa = {
        en_proceso: 'En proceso', correccion_solicitada: 'Corrección solicitada',
        rechazado: 'Cerrado', pendiente: 'Sin proforma',
    };
    return mapa[estado] || estado || 'Sin proforma';
}

async function cargarProforma() {
    try {
        const resp = await Api.get('../getters/get_proforma.php');
        if (!resp.success) return;
        renderProforma(resp.agendamientos, resp.proformas);
    } catch (e) {
        mostrarError(alertaProforma, 'No se pudo cargar la información.');
    }
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
        // rondas viene ordenado por id DESC (más reciente primero); se numera
        // en orden cronológico ascendente, igual que "Proforma #1, #2…" del app.
        const rondasCronologico = [...rondas].reverse();
        rondasCronologico.forEach((r, i) => {
            const rondaNodo = tplRonda.content.cloneNode(true);
            rondaNodo.querySelector('[data-campo="numero"]').textContent = i + 1;
            rondaNodo.querySelector('[data-campo="titulo"]').textContent = `Proforma #${i + 1}`;
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

        card.querySelector('.card-header').addEventListener('click', () => {
            const abierta = detalle.style.display !== 'none';
            detalle.style.display = abierta ? 'none' : 'block';
            card.classList.toggle('open', !abierta);
        });

        const formNueva = nodo.querySelector('[data-form="nueva-ronda"]');
        const inputFoto = formNueva.querySelector('input[name="evidencia"]');
        const imgPreview = formNueva.querySelector('.photo-preview');
        inputFoto.addEventListener('change', () => previsualizarFoto(inputFoto, imgPreview));

        formNueva.addEventListener('submit', async (ev) => {
            ev.preventDefault();
            mostrarError(alertaProforma, null);
            const boton = formNueva.querySelector('button[type="submit"]');
            boton.disabled = true;
            try {
                const evidenciaBase64 = await leerFotoComoBase64(inputFoto);
                const resp = await Api.post('../getters/insert_proforma.php', {
                    id_agendamiento: ag.id,
                    codigo_pdv: ag.codigo_pdv,
                    estado_proforma: 'en_proceso',
                    caracteristica_visita: formNueva.caracteristica_visita.value.trim(),
                    acompanamiento_tecnico: formNueva.acompanamiento_tecnico.value.trim(),
                    evidencia_base64: evidenciaBase64,
                    monto_total_factura: formNueva.monto_total_factura.value,
                    plazo_meses: formNueva.plazo_meses.value,
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

        const formFactura = nodo.querySelector('[data-form="confirmar-factura"]');
        const inputFotoFactura = formFactura.querySelector('input[name="foto_factura"]');
        const imgPreviewFactura = formFactura.querySelector('.photo-preview');
        inputFotoFactura.addEventListener('change', () => previsualizarFoto(inputFotoFactura, imgPreviewFactura));

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

        const formCerrar = nodo.querySelector('[data-form="cerrar"]');
        formCerrar.addEventListener('submit', async (ev) => {
            ev.preventDefault();
            if (!ultima) {
                mostrarError(alertaProforma, 'Primero registra una ronda de proforma.');
                return;
            }
            const motivo = formCerrar.motivo_cierre.value.trim();
            if (!motivo) {
                mostrarError(alertaProforma, 'El motivo de cierre es obligatorio.');
                return;
            }
            const resp = await Api.post('../getters/update_proforma.php', {
                id: ultima.id, accion: 'rechazar', motivo_cierre: motivo,
            });
            if (resp.success) {
                await cargarProforma();
            } else {
                mostrarError(alertaProforma, resp.message || (resp.stale ? 'El estado cambió, recarga.' : 'No se pudo cerrar.'));
            }
        });

        listaProforma.appendChild(nodo);
    });
}

cargarProforma();
