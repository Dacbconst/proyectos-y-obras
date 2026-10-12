(function () {
    'use strict';

    const app = document.getElementById('usuariosTecnicosApp');
    const GETTERS_BASE = app.dataset.gettersBase;
    const REGIONES = ['COSTA', 'SIERRA', 'AUSTRO', 'TODAS'];
    const ETIQUETA_REGION = { TODAS: 'Todas', COSTA: 'Costa', SIERRA: 'Sierra', AUSTRO: 'Austro' };

    const el = (id) => document.getElementById(id);
    let tecnicos = [];
    let pdvPorRegion = {};
    let filtro = 'TODOS';
    let filasPrevia = [];

    const esc = (t) => String(t == null ? '' : t).replace(/[&<>"']/g, (c) => ({ '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#39;' }[c]));
    const sinTildes = (t) => String(t == null ? '' : t).normalize('NFD').replace(/[̀-ͯ]/g, '');

    function mostrarMensaje(texto, esError) {
        const m = el('utMensaje');
        m.textContent = texto;
        m.classList.toggle('es-error', !!esError);
        m.hidden = !texto;
    }

    async function pedir(url, cuerpo) {
        const opciones = cuerpo ? { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(cuerpo) } : {};
        const resp = await fetch(GETTERS_BASE + url, opciones);
        return resp.json();
    }

    // ---------- Lista ----------
    function pintarPills() {
        const grupos = ['TODOS', 'COSTA', 'SIERRA', 'AUSTRO', 'TODAS'];
        el('utPills').innerHTML = grupos.map((g) => {
            const n = g === 'TODOS' ? tecnicos.length : tecnicos.filter((t) => t.region === g).length;
            const pdv = g === 'TODOS' ? '' : ' · ' + (pdvPorRegion[g] || 0) + ' PDV';
            const nombre = g === 'TODOS' ? 'Todos' : ETIQUETA_REGION[g];
            return '<button type="button" class="ut-pill' + (g === filtro ? ' is-activo' : '') + '" data-region="' + g + '" aria-pressed="' + (g === filtro) + '">' +
                nombre + ' ' + n + '<small>' + pdv + '</small></button>';
        }).join('');
    }

    function pintarLista() {
        const visibles = tecnicos.filter((t) => filtro === 'TODOS' || t.region === filtro);
        const activos = tecnicos.filter((t) => t.activo).length;
        el('utResumen').textContent = activos + (activos === 1 ? ' técnico activo' : ' técnicos activos');
        el('utCuerpo').innerHTML = visibles.map((t) =>
            '<tr><td>' + esc(t.nombre_completo) + '</td><td>' + esc(t.usuario_tecnico) + '</td>' +
            '<td><span class="ut-region">' + ETIQUETA_REGION[t.region] + '</span></td>' +
            '<td><label class="ut-switch"><input type="checkbox" data-usuario="' + esc(t.usuario_tecnico) + '"' + (t.activo ? ' checked' : '') + '>' +
            '<span class="ut-switch-pista"></span><span>' + (t.activo ? 'Activo' : 'Inactivo') + '</span></label></td>' +
            '<td>' + esc(t.ultimo_login || 'Nunca') + '</td></tr>'
        ).join('');
        el('utTabla').hidden = !visibles.length;
        el('utVacio').hidden = !!visibles.length;
        pintarPills();
    }

    async function recargar() {
        try {
            const r = await pedir('get_usuarios_tecnicos.php');
            if (!r.success) throw new Error();
            tecnicos = r.tecnicos;
            pdvPorRegion = r.pdv_por_region;
            pintarLista();
        } catch (e) {
            mostrarMensaje('No se pudo cargar la lista. Intenta de nuevo.', true);
        }
    }

    el('utPills').addEventListener('click', (ev) => {
        const b = ev.target.closest('.ut-pill');
        if (!b) return;
        filtro = b.dataset.region;
        pintarLista();
    });

    el('utCuerpo').addEventListener('change', async (ev) => {
        const caja = ev.target;
        const activo = caja.checked;
        if (!activo && !confirm('¿Desactivar a ' + caja.dataset.usuario + '? No podrá iniciar sesión; sus obras siguen en Técnicos.')) {
            caja.checked = true;
            return;
        }
        try {
            const r = await pedir('toggle_usuario_tecnico.php', { usuario: caja.dataset.usuario, activo: activo });
            if (!r.success) throw new Error();
            mostrarMensaje('');
        } catch (e) {
            mostrarMensaje('No se pudo cambiar el estado. Intenta de nuevo.', true);
        }
        recargar();
    });

    // ---------- Excel ----------
    function generarUsuario(nombre) {
        const partes = sinTildes(nombre).toLowerCase().replace(/[^a-z0-9 ]/g, '').split(/\s+/).filter(Boolean);
        if (partes.length < 2) return partes[0] || '';
        return partes[0][0] + partes[partes.length - 1];
    }

    function normalizarEncabezado(t) {
        const h = sinTildes(t).toLowerCase().trim();
        if (h === 'nombre') return 'nombre';
        if (h === 'usuario') return 'usuario';
        if (h === 'region') return 'region';
        if (['contrasena', 'password', 'clave'].indexOf(h) >= 0) return 'password';
        return null;
    }

    function validarFila(f, vistos, existentes) {
        if (!f.nombre) return 'Falta el nombre.';
        if (REGIONES.indexOf(f.region) < 0) return 'Región "' + f.regionOriginal + '" no existe. Usa Costa, Sierra, Austro o Todas.';
        if (!/^[a-z0-9._-]{3,50}$/.test(f.usuario)) return 'Usuario inválido (mínimo 3 letras o números, sin espacios).';
        if (vistos[f.usuario]) return 'Usuario repetido en el archivo.';
        if (!existentes[f.usuario] && !f.password) return 'Falta la contraseña de un técnico nuevo.';
        return '';
    }

    function leerExcel(archivo) {
        const lector = new FileReader();
        lector.onload = function () {
            const libro = XLSX.read(lector.result, { type: 'array' });
            const crudas = XLSX.utils.sheet_to_json(libro.Sheets[libro.SheetNames[0]], { defval: '' });
            const existentes = {};
            tecnicos.forEach((t) => { existentes[t.usuario_tecnico] = true; });
            const vistos = {};
            filasPrevia = crudas.map((cruda) => {
                const f = { nombre: '', usuario: '', password: '', region: '', regionOriginal: '' };
                Object.keys(cruda).forEach((k) => {
                    const campo = normalizarEncabezado(k);
                    if (campo) f[campo] = String(cruda[k]).trim();
                });
                f.regionOriginal = f.region;
                f.region = sinTildes(f.region).toUpperCase();
                f.usuario = (f.usuario ? sinTildes(f.usuario).toLowerCase() : generarUsuario(f.nombre)).trim();
                f.error = validarFila(f, vistos, existentes);
                f.estado = f.error ? 'error' : (existentes[f.usuario] ? 'actualiza' : 'nuevo');
                if (!f.error) vistos[f.usuario] = true;
                return f;
            }).filter((f) => f.nombre || f.usuario || f.region);
            pintarPrevia();
        };
        lector.readAsArrayBuffer(archivo);
    }

    function pintarPrevia() {
        const buenas = filasPrevia.filter((f) => !f.error);
        const nuevas = buenas.filter((f) => f.estado === 'nuevo').length;
        el('utPreviaConteo').textContent = nuevas + ' nuevos · ' + (buenas.length - nuevas) + ' actualizan · ' + (filasPrevia.length - buenas.length) + ' con error';
        el('utPreviaCuerpo').innerHTML = filasPrevia.map((f) => {
            const rotulo = { nuevo: '✓ Nuevo', actualiza: '↻ Actualiza', error: '✕ Error' }[f.estado];
            return '<tr><td><span class="ut-estado ' + f.estado + '">' + rotulo + '</span>' +
                (f.error ? '<span class="ut-error-detalle">' + esc(f.error) + '</span>' : '') + '</td>' +
                '<td>' + esc(f.nombre) + '</td><td>' + esc(f.usuario) + '</td><td>' + esc(f.regionOriginal) + '</td>' +
                '<td>' + (f.password ? 'Se guardará' : 'Sin cambio') + '</td></tr>';
        }).join('');
        el('utAplicar').disabled = !buenas.length;
        el('utAplicar').textContent = 'Aplicar ' + buenas.length + (buenas.length === 1 ? ' cambio' : ' cambios');
        el('utPrevia').hidden = !filasPrevia.length;
        if (!filasPrevia.length) mostrarMensaje('El archivo no tiene filas con datos.', true);
    }

    el('utArchivo').addEventListener('change', (ev) => {
        const archivo = ev.target.files[0];
        mostrarMensaje('');
        if (archivo) leerExcel(archivo);
        ev.target.value = '';
    });

    el('utCancelar').addEventListener('click', () => { filasPrevia = []; el('utPrevia').hidden = true; });

    el('utAplicar').addEventListener('click', async () => {
        const boton = el('utAplicar');
        boton.disabled = true;
        boton.textContent = 'Aplicando…';
        try {
            const filas = filasPrevia.filter((f) => !f.error).map((f) => ({ nombre: f.nombre, usuario: f.usuario, password: f.password, region: f.region }));
            const r = await pedir('upsert_usuarios_tecnicos.php', { filas: filas });
            if (!r.success) throw new Error(r.message);
            mostrarMensaje(r.creados + ' creados, ' + r.actualizados + ' actualizados' + (r.errores.length ? '. ' + r.errores.join(' ') : '.'), r.errores.length > 0);
            filasPrevia = [];
            el('utPrevia').hidden = true;
            recargar();
        } catch (e) {
            mostrarMensaje(e.message || 'No se pudo aplicar. Intenta de nuevo.', true);
            boton.disabled = false;
            boton.textContent = 'Reintentar';
        }
    });

    el('utPlantilla').addEventListener('click', () => {
        const hoja = XLSX.utils.aoa_to_sheet([
            ['Nombre', 'Usuario', 'Contraseña', 'Región'],
            ['Nombre Apellido', '', 'clave-inicial', 'Sierra'],
        ]);
        const libro = XLSX.utils.book_new();
        XLSX.utils.book_append_sheet(libro, hoja, 'Tecnicos');
        XLSX.writeFile(libro, 'plantilla_usuarios_tecnicos.xlsx');
    });

    window.UsuariosTecnicosRecargar = recargar;
    recargar();
})();
