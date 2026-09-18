const selectPdv = document.getElementById('pdv');
const form = document.getElementById('form-contacto');
const alerta = document.getElementById('alerta');
const alertaOk = document.getElementById('alerta-ok');
const checkNoRequiere = document.getElementById('no_requiere_visita');
const camposAgenda = document.getElementById('campos-agenda');

async function cargarPdvs() {
    try {
        const resp = await Api.get('../getters/get_pdvs.php');
        if (!resp.success) return;
        resp.data.forEach((pdv) => {
            const opt = document.createElement('option');
            opt.value = pdv.pos_id;
            opt.textContent = `${pdv.pos_name} (${pdv.city})`;
            opt.dataset.nombre = pdv.pos_name;
            opt.dataset.ciudad = pdv.city;
            selectPdv.appendChild(opt);
        });
    } catch (e) {
        mostrarError(alerta, 'No se pudo cargar la lista de PDVs.');
    }
}

checkNoRequiere.addEventListener('change', () => {
    camposAgenda.style.display = checkNoRequiere.checked ? 'none' : 'block';
});

form.addEventListener('submit', async (ev) => {
    ev.preventDefault();
    mostrarError(alerta, null);
    mostrarError(alertaOk, null);

    const pdvOption = selectPdv.options[selectPdv.selectedIndex];
    const body = {
        codigo_pdv: selectPdv.value,
        pdv: pdvOption ? pdvOption.dataset.nombre : '',
        ciudad_pdv: pdvOption ? pdvOption.dataset.ciudad : '',
        contacto: document.getElementById('contacto').value.trim(),
        empresa: document.getElementById('empresa').value.trim(),
        mail: document.getElementById('mail').value.trim(),
        direccion: document.getElementById('direccion').value.trim(),
        telefono: document.getElementById('telefono').value.trim(),
        telefono_convencional: document.getElementById('telefono_convencional').value.trim(),
        no_requiere_visita: checkNoRequiere.checked,
        fecha_agendamiento: checkNoRequiere.checked ? null : document.getElementById('fecha_agendamiento').value,
        hora: checkNoRequiere.checked ? null : document.getElementById('hora').value,
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
        } else {
            mostrarError(alerta, resp.message || 'No se pudo guardar la visita.');
        }
    } catch (e) {
        mostrarError(alerta, 'Error de conexión. Intenta de nuevo.');
    } finally {
        boton.disabled = false;
    }
});

cargarPdvs();
