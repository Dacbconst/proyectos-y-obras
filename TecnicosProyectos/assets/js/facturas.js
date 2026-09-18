const listaFacturas = document.getElementById('lista-facturas');
const vacioFacturas = document.getElementById('vacio');
const alertaFacturas = document.getElementById('alerta');
const tplFactura = document.getElementById('tpl-factura');
const tplCuota = document.getElementById('tpl-cuota');
const btnFiltros = document.getElementById('btn-filtros');
const panelFiltros = document.getElementById('panel-filtros');
const inputBuscarEmpresa = document.getElementById('buscar-empresa');
const pillsTipo = document.querySelectorAll('#panel-filtros .pill');

let facturasData = [];
let pagosData = [];
let tipoActivo = 'todo';

// Mismo contenedor Azure Blob que ya sirve las fotos al app/Proyectos2.
const BLOB_BASE_URL = 'https://luckyecuadorweb.blob.core.windows.net/app/AppPintuco/Inserts/';

btnFiltros.addEventListener('click', () => panelFiltros.classList.toggle('open'));
pillsTipo.forEach((p) => p.addEventListener('click', () => {
    pillsTipo.forEach((x) => x.classList.remove('active'));
    p.classList.add('active');
    tipoActivo = p.dataset.tipo;
    renderFacturas();
}));
inputBuscarEmpresa.addEventListener('input', renderFacturas);

function coincideTipo(f) {
    if (tipoActivo === 'todo') return true;
    return f.estado_pago === tipoActivo;
}

function coincideEmpresa(f) {
    const texto = inputBuscarEmpresa.value.trim().toLowerCase();
    if (!texto) return true;
    return (f.empresa || '').toLowerCase().includes(texto);
}

function renderFacturas() {
    listaFacturas.innerHTML = '';
    const filtradas = facturasData.filter((f) => coincideTipo(f) && coincideEmpresa(f));
    vacioFacturas.style.display = filtradas.length ? 'none' : 'block';

    filtradas.forEach((f) => {
        const cuotas = pagosData.filter((p) => p.id_proforma == f.id);
        const totalPagado = cuotas.reduce((acc, c) => acc + parseFloat(c.monto_pago || 0), 0);

        const nodo = tplFactura.content.cloneNode(true);
        const card = nodo.querySelector('.card');
        nodo.querySelector('[data-campo="fecha"]').textContent = formatearFecha(f.fecha_proforma);
        nodo.querySelector('[data-campo="empresa"]').textContent = f.empresa || f.contacto || '—';
        nodo.querySelector('[data-campo="pdv"]').textContent = f.pdv || '(sin PDV)';

        const badge = nodo.querySelector('[data-campo="badge"]');
        const estado = f.estado_pago || 'directo';
        badge.textContent = estado === 'a_plazos' ? 'A plazos' : (estado === 'cerrado' ? 'Cerrado' : 'Directo');
        badge.classList.add('badge-' + (estado === 'cerrado' ? 'cancelada' : 'confirmado'));

        const foto = nodo.querySelector('[data-campo="foto"]');
        foto.src = f.foto_factura ? BLOB_BASE_URL + f.foto_factura : '';
        nodo.querySelector('[data-campo="tipo"]').textContent = estado === 'a_plazos' ? 'Factura a plazos' : 'Factura directa';
        nodo.querySelector('[data-campo="sub"]').textContent =
            (f.plazo_meses ? `${f.plazo_meses} meses · ` : '') + formatearFecha(f.fecha_proforma);
        nodo.querySelector('[data-campo="pagado"]').textContent = `$${totalPagado.toFixed(2)}`;
        nodo.querySelector('[data-campo="total"]').textContent = f.monto_total_factura ? `de $${f.monto_total_factura}` : '';

        const detalle = nodo.querySelector('[data-campo="detalle"]');
        const contCuotas = nodo.querySelector('[data-campo="cuotas"]');
        cuotas.forEach((c) => {
            const cuotaNodo = tplCuota.content.cloneNode(true);
            cuotaNodo.querySelector('[data-campo="linea"]').textContent =
                `Cuota ${c.numero_cuota}: $${c.monto_pago} — ${formatearFecha(c.fecha_pago)}${c.observacion ? ' · ' + c.observacion : ''}`;
            contCuotas.appendChild(cuotaNodo);
        });

        card.querySelector('.card-header').addEventListener('click', () => {
            const abierta = detalle.style.display !== 'none';
            detalle.style.display = abierta ? 'none' : 'block';
            card.classList.toggle('open', !abierta);
        });

        const formCuota = nodo.querySelector('[data-form="nueva-cuota"]');
        const inputFoto = formCuota.querySelector('input[name="foto_pago"]');
        const imgPreview = formCuota.querySelector('.photo-preview');
        inputFoto.addEventListener('change', () => previsualizarFoto(inputFoto, imgPreview));

        formCuota.addEventListener('submit', async (ev) => {
            ev.preventDefault();
            mostrarError(alertaFacturas, null);
            const boton = formCuota.querySelector('button[type="submit"]');
            boton.disabled = true;
            try {
                const fotoBase64 = await leerFotoComoBase64(inputFoto);
                const resp = await Api.post('../getters/insert_pago_factura.php', {
                    id_proforma: f.id,
                    codigo_pdv: f.codigo_pdv || '',
                    numero_cuota: formCuota.numero_cuota.value,
                    monto_pago: formCuota.monto_pago.value,
                    fecha_pago: formCuota.fecha_pago.value,
                    observacion: formCuota.observacion.value.trim(),
                    foto_pago_base64: fotoBase64,
                });
                if (resp.success) {
                    await cargarFacturas();
                } else {
                    mostrarError(alertaFacturas, resp.message || 'No se pudo registrar la cuota.');
                }
            } catch (e) {
                mostrarError(alertaFacturas, 'Error de conexión.');
            } finally {
                boton.disabled = false;
            }
        });

        const formCerrar = nodo.querySelector('[data-form="cerrar-plan"]');
        formCerrar.addEventListener('submit', async (ev) => {
            ev.preventDefault();
            const motivo = formCerrar.motivo_cierre_pago.value.trim();
            if (!motivo) {
                mostrarError(alertaFacturas, 'El motivo de cierre es obligatorio.');
                return;
            }
            const resp = await Api.post('../getters/update_proforma.php', {
                id: f.id, accion: 'cerrar_plan_pago', motivo_cierre_pago: motivo,
            });
            if (resp.success) {
                await cargarFacturas();
            } else {
                mostrarError(alertaFacturas, resp.message || (resp.stale ? 'El estado cambió, recarga.' : 'No se pudo cerrar.'));
            }
        });

        listaFacturas.appendChild(nodo);
    });
}

async function cargarFacturas() {
    try {
        const resp = await Api.get('../getters/get_pagos_factura.php');
        if (resp.success) {
            facturasData = resp.facturas;
            pagosData = resp.pagos;
            renderFacturas();
        }
    } catch (e) {
        mostrarError(alertaFacturas, 'No se pudieron cargar las facturas.');
    }
}

cargarFacturas();
