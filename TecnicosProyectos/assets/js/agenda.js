const listaAgenda = document.getElementById('lista-agenda');
const vacio = document.getElementById('vacio');
const alerta = document.getElementById('alerta');
const plantilla = document.getElementById('tpl-tarjeta');
const pills = document.querySelectorAll('.pill');

let registros = [];
let filtroActivo = 'pendientes';

const ESTADOS_VISITADOS = ['completada'];
const ESTADOS_PENDIENTES = ['pendiente', 'confirmado', 'reagendada', 'vencida'];

function badgeTexto(estado) {
    const mapa = {
        pendiente: 'Pendiente', confirmado: 'Confirmado', reagendada: 'Reagendada',
        completada: 'Completada', cancelada: 'Cancelada', vencida: 'Vencida',
    };
    return mapa[estado] || estado;
}

function renderLista() {
    listaAgenda.innerHTML = '';
    const filtrados = registros.filter((r) =>
        filtroActivo === 'pendientes'
            ? ESTADOS_PENDIENTES.includes(r.estado_agenda)
            : ESTADOS_VISITADOS.includes(r.estado_agenda)
    );

    vacio.style.display = filtrados.length ? 'none' : 'block';

    filtrados.forEach((r) => {
        const nodo = plantilla.content.cloneNode(true);
        const card = nodo.querySelector('.card');
        card.dataset.id = r.id;
        nodo.querySelector('[data-campo="fecha"]').textContent = r.fecha_agendamiento
            ? `${formatearFecha(r.fecha_agendamiento)}${r.hora ? ' ' + r.hora.substring(0, 5) : ''}`
            : 'Sin fecha';
        nodo.querySelector('[data-campo="empresa"]').textContent = r.empresa || r.contacto || '—';
        nodo.querySelector('[data-campo="pdv"]').textContent = r.pdv || '(sin PDV)';

        const badge = nodo.querySelector('[data-campo="badge"]');
        badge.textContent = badgeTexto(r.estado_agenda);
        badge.classList.add('badge-' + r.estado_agenda);

        const detalle = nodo.querySelector('[data-campo="detalle"]');
        const formEditar = nodo.querySelector('[data-form="editar"]');
        formEditar.contacto.value = r.contacto || '';
        formEditar.empresa.value = r.empresa || '';
        formEditar.mail.value = r.mail || '';
        formEditar.telefono.value = r.telefono || '';
        formEditar.direccion.value = r.direccion || '';

        card.querySelector('.card-header').addEventListener('click', () => {
            const abierta = detalle.style.display !== 'none';
            detalle.style.display = abierta ? 'none' : 'block';
            card.classList.toggle('open', !abierta);
        });

        formEditar.addEventListener('submit', async (ev) => {
            ev.preventDefault();
            await ejecutarAccion({
                id: r.id, accion: 'editar',
                contacto: formEditar.contacto.value.trim(),
                empresa: formEditar.empresa.value.trim(),
                mail: formEditar.mail.value.trim(),
                telefono: formEditar.telefono.value.trim(),
                direccion: formEditar.direccion.value.trim(),
            });
        });

        const formReagendar = nodo.querySelector('[data-form="reagendar"]');
        formReagendar.addEventListener('submit', async (ev) => {
            ev.preventDefault();
            await ejecutarAccion({
                id: r.id, accion: 'reagendar',
                fecha_agendamiento: formReagendar.fecha_agendamiento.value,
                hora: formReagendar.hora.value,
            });
        });

        nodo.querySelector('[data-accion="eliminar"]').addEventListener('click', async () => {
            if (!confirm('¿Eliminar esta visita?')) return;
            await ejecutarAccion({ id: r.id, accion: 'eliminar' });
        });

        listaAgenda.appendChild(nodo);
    });
}

async function ejecutarAccion(body) {
    mostrarError(alerta, null);
    try {
        const resp = await Api.post('../getters/update_agenda.php', body);
        if (resp.success) {
            await cargarAgenda();
        } else {
            mostrarError(alerta, resp.message || 'No se pudo completar la acción.');
        }
    } catch (e) {
        mostrarError(alerta, 'Error de conexión.');
    }
}

async function cargarAgenda() {
    try {
        const resp = await Api.get('../getters/get_agenda.php');
        if (resp.success) {
            registros = resp.data;
            renderLista();
        }
    } catch (e) {
        mostrarError(alerta, 'No se pudo cargar la agenda.');
    }
}

pills.forEach((p) => p.addEventListener('click', () => {
    pills.forEach((x) => x.classList.remove('active'));
    p.classList.add('active');
    filtroActivo = p.dataset.filtro;
    renderLista();
}));

cargarAgenda();
