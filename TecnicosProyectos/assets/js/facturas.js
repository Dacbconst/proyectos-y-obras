// facturas.js — réplica del módulo "Facturas" de Android (FacturasFragment + AdapterFacturas).

const listaFacturas = document.getElementById('lista-facturas');
const vacioFacturas = document.getElementById('vacio');
const alertaFacturas = document.getElementById('alerta');
const contadorFacturas = document.getElementById('contador-facturas');
const tplSeparador = document.getElementById('tpl-separador');
const tplFactura = document.getElementById('tpl-factura');
const tplPago = document.getElementById('tpl-pago');
const inputBuscarEmpresa = document.getElementById('buscar-empresa');
const selectorMesWrap = document.getElementById('selector-mes-wrap');
const selectorMes = document.getElementById('selector-mes');
const pillsTipo = document.querySelectorAll('#pills-tipo .pill');

const dialogCerrar = document.getElementById('dialog-cerrar');
const textareaCerrar = document.getElementById('cerrar-motivo');
const contadorCerrar = document.getElementById('cerrar-contador');
const formCerrarDialog = document.getElementById('form-cerrar-dialog');

const dialogFoto = document.getElementById('dialog-foto');
const dialogFotoImg = document.getElementById('dialog-foto-img');
const dialogFotoTexto = document.getElementById('dialog-foto-texto');

const BLOB_BASE_URL = 'https://luckyecuadorweb.blob.core.windows.net/app/AppPintuco/Inserts/';

let facturasData = [];
let pagosData = [];
let tipoActivo = 'a_plazos'; // mismo default que Android: filtroActualFacturas = A_PLAZOS
let idFacturaParaCerrar = null;
let idAcordeonAbierto = null; // acordeón de una sola tarjeta a la vez, igual que Proforma
try { idAcordeonAbierto = sessionStorage.getItem('facturas:abierta'); } catch (e) { idAcordeonAbierto = null; }

function recordarAbierta(id) {
    idAcordeonAbierto = id;
    try {
        if (id) sessionStorage.setItem('facturas:abierta', id); else sessionStorage.removeItem('facturas:abierta');
    } catch (e) { /* sin storage */ }
}

// Arranca en el mes calendario actual, igual que FacturasFragment.onViewCreated.
const hoy = new Date();
selectorMes.value = hoy.getFullYear() + '-' + String(hoy.getMonth() + 1).padStart(2, '0');
selectorMes.max = selectorMes.value;

function urlFoto(valor) {
    if (!valor) return '';
    return valor.startsWith('http') ? valor : BLOB_BASE_URL + valor;
}

// Modelo: fila cruda del backend -> objeto con los cómputos de FacturaConPagos.java.
function construirFactura(f) {
    const cuotas = pagosData
        .filter((p) => p.id_proforma == f.id)
        .sort((a, b) => (parseInt(a.numero_cuota, 10) || 0) - (parseInt(b.numero_cuota, 10) || 0));
    const montoPagado = cuotas.reduce((acc, c) => acc + (parseFloat(c.monto_pago) || 0), 0);
    const esAPlazos = parseInt(f.plazo_meses, 10) > 0;
    const motivoCierre = (f.motivo_cierre_pago || '').trim();
    const estaCerradaManualmente = motivoCierre !== '' || f.estado_pago === 'cerrado';
    const estaCompletadaAutomaticamente = !estaCerradaManualmente && f.estado_pago === 'completado';
    return {
        ...f,
        cuotas,
        montoPagado,
        esAPlazos,
        estaCerradaManualmente,
        estaCompletadaAutomaticamente,
        estaCerrada: estaCerradaManualmente || estaCompletadaAutomaticamente,
        siguienteNumeroCuota: cuotas.length + 1,
    };
}

function coincideEmpresa(f) {
    const texto = inputBuscarEmpresa.value.trim().toLowerCase();
    if (!texto) return true;
    return (f.empresa || '').toLowerCase().includes(texto);
}

function estaEnMesFiltrado(f) {
    if (!f.fecha_proforma) return false;
    return String(f.fecha_proforma).slice(0, 7) === selectorMes.value;
}

function claveMes(fechaProforma) {
    return fechaProforma ? String(fechaProforma).slice(0, 7) : 'sin-fecha';
}

function etiquetaMes(clave) {
    if (clave === 'sin-fecha') return 'Sin fecha';
    const [anio, mes] = clave.split('-');
    const nombre = new Date(anio, parseInt(mes, 10) - 1, 1)
        .toLocaleDateString('es-EC', { month: 'long', year: 'numeric' });
    return nombre.charAt(0).toUpperCase() + nombre.slice(1);
}

// ----- Pills / buscador / mes -----
pillsTipo.forEach((p) => p.addEventListener('click', () => {
    pillsTipo.forEach((x) => x.classList.remove('active'));
    p.classList.add('active');
    tipoActivo = p.dataset.tipo;
    // "Todo" ignora el mes seleccionado — mismo criterio que Android.
    selectorMesWrap.style.display = tipoActivo === 'todo' ? 'none' : 'block';
    renderizar();
}));
inputBuscarEmpresa.addEventListener('input', renderizar);
selectorMes.addEventListener('change', renderizar);

function renderizar() {
    const conEmpresa = facturasData.filter(coincideEmpresa);

    let cantidadAPlazos = 0;
    let cantidadDirecto = 0;
    let filtradas = [];

    if (tipoActivo === 'todo') {
        filtradas = conEmpresa.slice();
    } else {
        conEmpresa.forEach((f) => {
            if (!estaEnMesFiltrado(f)) return;
            if (f.esAPlazos) cantidadAPlazos++; else cantidadDirecto++;
            const categoria = f.esAPlazos ? 'a_plazos' : 'directo';
            if (categoria === tipoActivo) filtradas.push(f);
        });
    }

    filtradas.sort((a, b) => String(b.fecha_proforma || '').localeCompare(String(a.fecha_proforma || '')));

    document.querySelector('[data-contador="a_plazos"]').textContent = `· ${cantidadAPlazos}`;
    document.querySelector('[data-contador="directo"]').textContent = `· ${cantidadDirecto}`;
    document.querySelector('[data-contador="todo"]').textContent = `· ${conEmpresa.length}`;

    vacioFacturas.style.display = filtradas.length ? 'none' : 'block';
    contadorFacturas.textContent = filtradas.length + (filtradas.length === 1 ? ' factura' : ' facturas');
    listaFacturas.innerHTML = '';
    construirGrupos(filtradas).forEach((grupo) => {
        if (grupo.tipo === 'separador') {
            const nodo = tplSeparador.content.cloneNode(true);
            nodo.querySelector('[data-campo="texto"]').textContent = grupo.texto;
            listaFacturas.appendChild(nodo);
        } else {
            renderFactura(grupo.factura);
        }
    });
}

// "Todo" agrupa por mes, "A plazos" por estado (Activos/Completado/Cerrados), "Directo" es una sola sección.
function construirGrupos(lista) {
    const items = [];

    if (tipoActivo === 'todo') {
        let claveActual = null;
        let grupoActual = [];
        const cerrarGrupo = () => {
            if (!grupoActual.length) return;
            const total = grupoActual.reduce((acc, f) => acc + (f.cuotas.length ? f.montoPagado : (parseFloat(f.monto_total_factura) || 0)), 0);
            items.push({ tipo: 'separador', texto: `${etiquetaMes(claveActual)} · $${total.toFixed(2)}` });
            grupoActual.forEach((f) => items.push({ tipo: 'factura', factura: f }));
        };
        lista.forEach((f) => {
            const clave = claveMes(f.fecha_proforma);
            if (claveActual !== null && clave !== claveActual) {
                cerrarGrupo();
                grupoActual = [];
            }
            claveActual = clave;
            grupoActual.push(f);
        });
        cerrarGrupo();
        return items;
    }

    if (tipoActivo === 'directo') {
        if (lista.length) items.push({ tipo: 'separador', texto: 'Completados' });
        lista.forEach((f) => items.push({ tipo: 'factura', factura: f }));
        return items;
    }

    // a_plazos
    const activos = lista.filter((f) => !f.estaCerrada);
    const completados = lista.filter((f) => f.estaCompletadaAutomaticamente);
    const cerrados = lista.filter((f) => f.estaCerradaManualmente);
    const agregar = (texto, grupo) => {
        if (!grupo.length) return;
        items.push({ tipo: 'separador', texto });
        grupo.forEach((f) => items.push({ tipo: 'factura', factura: f }));
    };
    agregar('Activos', activos);
    agregar('Completado', completados);
    agregar('Cerrados', cerrados);
    return items;
}

// ----- Tarjeta -----
function renderFactura(f) {
    const nodo = tplFactura.content.cloneNode(true);
    const card = nodo.querySelector('.card');
    card.dataset.id = f.id;

    nodo.querySelector('[data-campo="fecha"]').textContent = f.no_requiere_visita === 'SI'
        ? 'No requirió' : formatearFecha(f.fecha_agendamiento);
    nodo.querySelector('[data-campo="empresa"]').textContent = f.empresa || f.contacto || '—';
    nodo.querySelector('[data-campo="pdv"]').textContent = f.pdv || '(sin PDV)';

    const badge = nodo.querySelector('[data-campo="badge"]');
    const estadoBadge = f.estaCerradaManualmente ? 'cerrado' : (f.esAPlazos ? (f.estado_pago || 'pendiente') : 'completado');
    badge.textContent = { pendiente: 'Pendiente', en_proceso: 'En proceso', completado: 'Completado', cerrado: 'Cerrado' }[estadoBadge] || estadoBadge;
    badge.classList.add('badge-' + estadoBadge);

    const detalle = nodo.querySelector('[data-campo="detalle"]');
    detalle.style.display = idAcordeonAbierto == f.id ? 'block' : 'none';
    if (idAcordeonAbierto == f.id) card.classList.add('open');

    const foto = nodo.querySelector('[data-campo="foto"]');
    foto.src = urlFoto(f.foto_factura);
    nodo.querySelector('[data-campo="fila-foto"]').addEventListener('click', () => {
        abrirVisorFoto(f.foto_factura, (f.esAPlazos ? 'Factura a plazos — ' : 'Factura directa — ') + formatearFecha(f.fecha_proforma));
    });

    nodo.querySelector('[data-campo="tipo"]').textContent = f.esAPlazos ? 'Factura a plazos' : 'Factura directa';
    let sub = formatearFecha(f.fecha_proforma);
    if (f.esAPlazos) sub += ` · ${f.plazo_meses} meses`;
    nodo.querySelector('[data-campo="sub"]').textContent = sub;

    const campoPagado = nodo.querySelector('[data-campo="pagado"]');
    const campoTotal = nodo.querySelector('[data-campo="total"]');
    const contCuotas = nodo.querySelector('[data-campo="cuotas"]');
    campoPagado.textContent = f.monto_total_factura ? `$${parseFloat(f.monto_total_factura).toFixed(2)}` : '';

    if (f.esAPlazos) {
        if (f.cuotas.length) {
            campoTotal.style.display = 'block';
            campoTotal.textContent = `Facturado: $${f.montoPagado.toFixed(2)}`;
        }
        f.cuotas.forEach((c) => {
            const cNodo = tplPago.content.cloneNode(true);
            cNodo.querySelector('[data-campo="thumb"]').src = urlFoto(c.foto_pago);
            cNodo.querySelector('[data-campo="titulo"]').textContent = `Factura ${c.numero_cuota} — $${parseFloat(c.monto_pago).toFixed(2)}`;
            cNodo.querySelector('[data-campo="fecha"]').textContent = formatearFecha(c.fecha_pago) + (c.observacion ? ' · ' + c.observacion : '');
            const fila = cNodo.querySelector('.pago-row');
            fila.addEventListener('click', () => abrirVisorFoto(c.foto_pago, `Cuota ${c.numero_cuota} — $${parseFloat(c.monto_pago).toFixed(2)} · ${formatearFecha(c.fecha_pago)}`));
            contCuotas.appendChild(cNodo);
        });
    }

    // Botón "Adjuntar otra factura": solo a plazos, sin límite, mientras no esté cerrada.
    const btnAdjuntar = nodo.querySelector('[data-campo="btn-adjuntar"]');
    const formPago = nodo.querySelector('[data-form="nuevo-pago"]');
    const mostrarBoton = f.esAPlazos && !f.estaCerrada;
    if (mostrarBoton) {
        btnAdjuntar.style.display = 'flex';
        btnAdjuntar.addEventListener('click', () => {
            btnAdjuntar.style.display = 'none';
            formPago.style.display = 'block';
        });
        const campoFotoPago = formPago.querySelector('[data-campo="foto"]');
        const clavePago = `pago-${f.id}`;
        activarCampoFoto(campoFotoPago, clavePago, (mensaje) => mostrarError(alertaFacturas, mensaje));
        const abrirFormPago = () => {
            btnAdjuntar.style.display = 'none';
            formPago.style.display = 'block';
        };
        // Una foto recuperada tras recargar deja el formulario abierto.
        if (campoFotoPago.fotoBase64 && idAcordeonAbierto == f.id) abrirFormPago();
        formPago.querySelector('[data-campo="cancelar-pago"]').addEventListener('click', () => {
            formPago.style.display = 'none';
            formPago.reset();
            limpiarCampoFoto(campoFotoPago, clavePago);
            btnAdjuntar.style.display = 'flex';
        });
        formPago.addEventListener('submit', async (ev) => {
            ev.preventDefault();
            mostrarError(alertaFacturas, null);
            if (!campoFotoPago.fotoBase64) {
                mostrarError(alertaFacturas, 'El comprobante es obligatorio.');
                return;
            }
            const boton = formPago.querySelector('button[type="submit"]');
            boton.disabled = true;
            try {
                const fotoBase64 = campoFotoPago.fotoBase64;
                const hoyIso = new Date().toISOString().slice(0, 10);
                const resp = await Api.post('../getters/insert_pago_factura.php', {
                    id_proforma: f.id,
                    codigo_pdv: f.codigo_pdv || '',
                    numero_cuota: f.siguienteNumeroCuota,
                    monto_pago: formPago.monto_pago.value,
                    fecha_pago: hoyIso,
                    observacion: formPago.observacion.value.trim(),
                    foto_pago_base64: fotoBase64,
                });
                if (resp.success) {
                    recordarAbierta(f.id);
                    borrarBorradorFoto(clavePago);
                    mostrarToast('Factura adjuntada correctamente.');
                    await cargarFacturas();
                } else {
                    mostrarError(alertaFacturas, resp.message || 'No se pudo registrar la cuota.');
                }
            } catch (e) {
                mostrarError(alertaFacturas, e.message);
            } finally {
                boton.disabled = false;
            }
        });
    }

    // Cierre manual: solo a plazos, sin cerrar todavía.
    const btnCerrar = nodo.querySelector('[data-campo="btn-cerrar"]');
    const chipCerrada = nodo.querySelector('[data-campo="chip-cerrada"]');
    if (f.esAPlazos && f.estaCerradaManualmente) {
        chipCerrada.style.display = 'inline-block';
    } else if (f.esAPlazos && !f.estaCerrada) {
        btnCerrar.style.display = 'inline-block';
        btnCerrar.addEventListener('click', () => {
            idFacturaParaCerrar = f.id;
            textareaCerrar.value = '';
            contadorCerrar.textContent = '0';
            dialogCerrar.showModal();
        });
    }

    card.querySelector('.card-header').addEventListener('click', () => {
        const abierta = idAcordeonAbierto == f.id;
        recordarAbierta(abierta ? null : f.id);
        renderizar();
    });

    listaFacturas.appendChild(nodo);
}

// ----- Visor de foto (factura/pago) -----
function abrirVisorFoto(valor, texto) {
    if (!valor) return;
    dialogFotoImg.src = urlFoto(valor);
    dialogFotoTexto.textContent = texto || '';
    dialogFoto.showModal();
}
document.getElementById('btn-cerrar-foto').addEventListener('click', () => dialogFoto.close());
dialogFoto.addEventListener('click', (ev) => { if (ev.target === dialogFoto) dialogFoto.close(); });

// ----- Diálogo "Cierre Factura" (mismo patrón que Proforma) -----
textareaCerrar.addEventListener('input', () => {
    contadorCerrar.textContent = String(textareaCerrar.value.length);
});
document.getElementById('btn-cancelar-cerrar').addEventListener('click', () => dialogCerrar.close());

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
            id: idFacturaParaCerrar, accion: 'cerrar_plan_pago', motivo_cierre_pago: motivo,
        });
        if (resp.success) {
            dialogCerrar.close();
            mostrarToast('Factura cerrada correctamente.');
            await cargarFacturas();
        } else {
            mostrarError(alertaFacturas, resp.message || (resp.stale ? 'El estado cambió, recarga.' : 'No se pudo cerrar.'));
            dialogCerrar.close();
        }
    } catch (e) {
        mostrarError(alertaFacturas, e.message);
        dialogCerrar.close();
    } finally {
        boton.disabled = false;
    }
});

function aplicarRespuestaFacturas(resp) {
    if (!resp.success) return;
    pagosData = resp.pagos;
    facturasData = resp.facturas.map(construirFactura);
    renderizar();
}

// conCache: al entrar a la página muestra al instante lo último que se vio y luego lo refresca desde el servidor.
async function cargarFacturas(conCache = false) {
    const url = '../getters/get_pagos_factura.php';
    try {
        if (conCache) await Api.getConCache(url, aplicarRespuestaFacturas);
        else aplicarRespuestaFacturas(await Api.get(url));
    } catch (e) {
        mostrarError(alertaFacturas, e.message);
    }
}

cargarFacturas(true);
