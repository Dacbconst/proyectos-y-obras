// Hoja "Enviar factura": paso final que cierra el ciclo de proformas de una obra.
const dialogFactura = document.getElementById('dialog-factura');
const formFactura = document.getElementById('form-factura');
const campoFotoFactura = formFactura.querySelector('[data-campo="foto"]');
const alertaFactura = document.getElementById('fac-alerta');
let contextoFactura = null;

const claveFotoFactura = () => `factura-${contextoFactura.ag.id}`;
activarCampoFoto(campoFotoFactura, claveFotoFactura, (mensaje) => mostrarError(alertaFactura, mensaje));

const FORMATO_USD = new Intl.NumberFormat('en-US', { style: 'currency', currency: 'USD' });

// A plazos la factura de hoy es la cuota 1 y cuenta en este mes; la meta del plan sale de la cotización.
function actualizarModalidad() {
    const aPlazos = formFactura.estado_pago.value === 'a_plazos';
    const cotizacion = contextoFactura ? contextoFactura.cotizacion : 0;
    formFactura.querySelector('[data-campo="campo-plazo"]').hidden = !aPlazos;
    formFactura.querySelector('[data-campo="campo-meta"]').hidden = !aPlazos;
    document.getElementById('fac-meta-wrap').hidden = cotizacion > 0;
    document.getElementById('fac-meta-texto').hidden = !(cotizacion > 0);
    document.getElementById('fac-meta-texto').textContent = cotizacion > 0 ? `${FORMATO_USD.format(cotizacion)} (tu proforma)` : '';
    document.getElementById('fac-monto-label').textContent = aPlazos ? '¿Cuánto facturas ahora?' : 'Valor de la factura';
    const sugerido = !aPlazos && cotizacion > 0 && cotizacion <= 9999.99;
    formFactura.monto_total_factura.placeholder = sugerido ? cotizacion.toFixed(2) : '0.00';
}

function marcarChipPlazo() {
    formFactura.querySelectorAll('.fac-chip').forEach((chip) => {
        chip.classList.toggle('is-activo', chip.dataset.meses === formFactura.plazo_meses.value);
    });
}

formFactura.querySelectorAll('input[name="estado_pago"]').forEach((radio) => radio.addEventListener('change', actualizarModalidad));
formFactura.querySelectorAll('.fac-chip').forEach((chip) => chip.addEventListener('click', () => {
    formFactura.plazo_meses.value = chip.dataset.meses;
    marcarChipPlazo();
}));
formFactura.plazo_meses.addEventListener('input', marcarChipPlazo);

// Sin tocar el borrador guardado: así una foto recuperada tras una recarga vuelve a mostrarse.
function restaurarFotoFactura() {
    campoFotoFactura.fotoBase64 = null;
    campoFotoFactura.classList.remove('has-photo');
    campoFotoFactura.querySelector('.photo-preview').removeAttribute('src');
    const borrador = leerBorradorFoto(claveFotoFactura());
    if (borrador) mostrarFotoEnCampo(campoFotoFactura, borrador);
}

function abrirFacturaEnvio(contexto) {
    if (dialogFactura.open) return;
    contextoFactura = contexto;
    formFactura.reset();
    mostrarError(alertaFactura, null);
    document.getElementById('fac-obra').textContent = contexto.ag.empresa || contexto.ag.contacto || '';
    document.getElementById('fac-propuesto').textContent = contexto.cotizacion > 0 ? FORMATO_USD.format(contexto.cotizacion) : '—';
    const n = contexto.totalRondas;
    document.getElementById('fac-rondas').textContent = `${n} proforma${n === 1 ? '' : 's'} enviada${n === 1 ? '' : 's'}`;
    actualizarModalidad();
    marcarChipPlazo();
    restaurarFotoFactura();
    dialogFactura.showModal();
}

document.getElementById('fac-cerrar').addEventListener('click', () => dialogFactura.close());
dialogFactura.addEventListener('click', (ev) => { if (ev.target === dialogFactura) dialogFactura.close(); });
document.getElementById('fac-cancelar').addEventListener('click', () => {
    limpiarCampoFoto(campoFotoFactura, claveFotoFactura());
    formFactura.reset();
    dialogFactura.close();
});

formFactura.addEventListener('submit', async (ev) => {
    ev.preventDefault();
    mostrarError(alertaFactura, null);
    if (!campoFotoFactura.fotoBase64) {
        mostrarError(alertaFactura, 'La foto de la factura es obligatoria.');
        return;
    }
    const aPlazos = formFactura.estado_pago.value === 'a_plazos';
    if (aPlazos && !(parseInt(formFactura.plazo_meses.value, 10) >= 2)) {
        mostrarError(alertaFactura, 'Indica un plazo de al menos 2 meses.');
        return;
    }
    if (!(parseFloat(formFactura.monto_total_factura.value) > 0)) {
        mostrarError(alertaFactura, 'Escribe el valor de la factura.');
        formFactura.monto_total_factura.focus();
        return;
    }
    if (aPlazos && !(contextoFactura.cotizacion > 0) && !(parseFloat(formFactura.meta_plan.value) > 0)) {
        mostrarError(alertaFactura, 'Escribe la meta del plan (el total de la proforma).');
        formFactura.meta_plan.focus();
        return;
    }
    const { ag, ultima, alEnviar } = contextoFactura;
    const boton = document.getElementById('fac-confirmar');
    boton.disabled = true;
    try {
        const resp = await Api.post('../getters/confirmar_factura.php', {
            id: ultima.id,
            codigo_pdv: ag.codigo_pdv,
            foto_factura_base64: campoFotoFactura.fotoBase64,
            estado_pago: formFactura.estado_pago.value,
            valor_factura: formFactura.monto_total_factura.value,
            meta_plan: aPlazos ? formFactura.meta_plan.value : '',
            plazo_meses: aPlazos ? formFactura.plazo_meses.value : '',
        });
        if (resp.success) {
            borrarBorradorFoto(claveFotoFactura());
            dialogFactura.close();
            mostrarToast('Factura enviada correctamente.');
            await alEnviar();
        } else {
            mostrarError(alertaFactura, resp.message || 'No se pudo confirmar la factura.');
        }
    } catch (e) {
        mostrarError(alertaFactura, e.message);
    } finally {
        boton.disabled = false;
    }
});
