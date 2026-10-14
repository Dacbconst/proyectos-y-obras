// Al iniciar sesión avisa, una sola vez, de las visitas cuya hora ya pasó y siguen sin confirmar.
(async function () {
    const CLAVE = 'recordatorio:visto';
    const ABIERTAS = ['pendiente', 'confirmado', 'reagendada', 'vencida'];
    try { if (sessionStorage.getItem(CLAVE)) return; } catch (e) { return; }

    let visitas;
    try {
        const resp = await Api.get('../getters/get_agenda.php');
        if (!resp.success) return;
        visitas = resp.data;
    } catch (e) { return; }
    try { sessionStorage.setItem(CLAVE, '1'); } catch (e) { /* sin storage: el aviso puede repetirse */ }

    const ahora = new Date();
    const porConfirmar = visitas.filter((v) => {
        if (!ABIERTAS.includes((v.estado_agenda || '').toLowerCase()) || !v.fecha_agendamiento) return false;
        const hora = v.hora ? v.hora.substring(0, 5) : '00:00';
        return new Date(`${v.fecha_agendamiento.split(' ')[0]}T${hora}`) <= ahora;
    });
    if (!porConfirmar.length) return;

    const hoy = `${ahora.getFullYear()}-${String(ahora.getMonth() + 1).padStart(2, '0')}-${String(ahora.getDate()).padStart(2, '0')}`;
    const hayAtrasadas = porConfirmar.some((v) => v.fecha_agendamiento.split(' ')[0] < hoy);
    const nombre = (v) => v.empresa || v.contacto || 'el cliente';

    const dialogo = document.createElement('dialog');
    dialogo.className = 'dialog-cerrar dialog-aviso';
    dialogo.setAttribute('aria-labelledby', 'aviso-titulo');
    dialogo.innerHTML = `
        <button type="button" class="aviso-cerrar" aria-label="Cerrar">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.4" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><line x1="18" y1="6" x2="6" y2="18"></line><line x1="6" y1="6" x2="18" y2="18"></line></svg>
        </button>
        <span class="aviso-icono">
            <svg width="40" height="40" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><rect x="3" y="4" width="18" height="18" rx="2"></rect><path d="M16 2v4"></path><path d="M8 2v4"></path><path d="M3 10h18"></path><path d="m9 16 2 2 4-4"></path></svg>
        </span>
        <strong class="exito-titulo" id="aviso-titulo"></strong>
        <p class="exito-texto"></p>
        <ul class="aviso-lista"></ul>
        <a href="agenda.php?resaltar=${porConfirmar.map((v) => v.id).join(',')}" class="btn btn-primary btn-block">Ir a la agenda</a>
        <button type="button" class="btn btn-ghost btn-block aviso-luego">Más tarde</button>`;
    dialogo.querySelector('.exito-titulo').textContent = hayAtrasadas ? 'Tienes visitas sin confirmar' : '¿Pudiste asistir a la visita?';
    dialogo.querySelector('.exito-texto').textContent = porConfirmar.length === 1
        ? `Tu visita con ${nombre(porConfirmar[0])} ya pasó de hora. Confírmala en la Agenda.`
        : `Tienes ${porConfirmar.length} visitas por confirmar.`;
    const lista = dialogo.querySelector('.aviso-lista');
    porConfirmar.slice(0, 3).forEach((v) => {
        const item = document.createElement('li');
        item.textContent = `${nombre(v)} · ${formatearFecha(v.fecha_agendamiento.split(' ')[0])}${v.hora ? ' ' + v.hora.substring(0, 5) : ''}`;
        lista.appendChild(item);
    });
    if (porConfirmar.length > 3) {
        const mas = document.createElement('li');
        mas.textContent = `y ${porConfirmar.length - 3} más`;
        lista.appendChild(mas);
    }
    if (porConfirmar.length === 1) lista.remove();
    dialogo.querySelector('.aviso-cerrar').addEventListener('click', () => dialogo.close());
    dialogo.querySelector('.aviso-luego').addEventListener('click', () => dialogo.close());
    dialogo.addEventListener('click', (ev) => { if (clicFueraDelDialogo(ev, dialogo)) dialogo.close(); });
    dialogo.addEventListener('close', () => dialogo.remove());
    document.body.appendChild(dialogo);
    dialogo.showModal();
})();
