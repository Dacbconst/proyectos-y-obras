(function () {
    'use strict';

    const app = document.getElementById('usuariosTecnicosApp');
    const GETTERS_BASE = app.dataset.gettersBase;
    const REGIONES = ['COSTA', 'SIERRA', 'AUSTRO', 'TODAS'];
    const ETIQUETA_REGION = { TODAS: 'Todas', COSTA: 'Costa', SIERRA: 'Sierra', AUSTRO: 'Austro' };

    const el = (id) => document.getElementById(id);
    let tecnicos = [];
    let filasPrevia = [];

    const esc = (t) => String(t == null ? '' : t).replace(/[&<>"']/g, (c) => ({ '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#39;' }[c]));
    const sinTildes = (t) => String(t == null ? '' : t).normalize('NFD').replace(/[̀-ͯ]/g, '');

    let temporizadorMensaje = null;

    function mostrarMensaje(texto, esError) {
        const m = el('utMensaje');
        m.textContent = texto;
        m.classList.toggle('es-error', !!esError);
        m.hidden = !texto;
        clearTimeout(temporizadorMensaje);
        if (texto && !esError) temporizadorMensaje = setTimeout(() => { m.hidden = true; }, 4000);
    }

    async function pedir(url, cuerpo) {
        const opciones = cuerpo ? { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(cuerpo) } : {};
        const resp = await fetch(GETTERS_BASE + url, opciones);
        return resp.json();
    }

    // ---------- Lista ----------
    const POR_PAGINA = 10;
    let orden = { campo: 'nombre_completo', dir: 1 };
    let pagina = 1;
    let busqueda = '';
    let editando = null;

    const iniciales = (n) => esc(String(n || '?').trim().split(/\s+/).slice(0, 2).map((p) => p[0]).join('').toUpperCase());

    // Sin ingreso (null) va siempre al final; el resto se ordena sin tildes ni mayúsculas.
    function compararPor(a, b) {
        const va = a[orden.campo];
        const vb = b[orden.campo];
        if (!va && !vb) return 0;
        if (!va) return 1;
        if (!vb) return -1;
        return sinTildes(String(va)).toLowerCase().localeCompare(sinTildes(String(vb)).toLowerCase()) * orden.dir;
    }

    function pintarPaginas(total) {
        const n = Math.max(1, Math.ceil(total / POR_PAGINA));
        pagina = Math.min(pagina, n);
        let h = '<button type="button" class="ut-pag" data-pag="' + (pagina - 1) + '" aria-label="Página anterior"' + (pagina <= 1 ? ' disabled' : '') + '>&lsaquo;</button>';
        for (let i = 1; i <= n; i++) h += '<button type="button" class="ut-pag' + (i === pagina ? ' is-activo' : '') + '" data-pag="' + i + '" aria-label="Página ' + i + '"' + (i === pagina ? ' aria-current="page"' : '') + '>' + i + '</button>';
        h += '<button type="button" class="ut-pag" data-pag="' + (pagina + 1) + '" aria-label="Página siguiente"' + (pagina >= n ? ' disabled' : '') + '>&rsaquo;</button>';
        el('utPaginas').innerHTML = h;
        el('utPaginaTexto').textContent = 'Página ' + pagina + ' de ' + n;
    }

    function pintarLista() {
        const q = sinTildes(busqueda).toLowerCase().trim();
        const visibles = tecnicos.filter((t) => !q || sinTildes(t.nombre_completo + ' ' + t.usuario_tecnico).toLowerCase().indexOf(q) >= 0)
            .sort((a, b) => compararPor(a, b));
        document.querySelectorAll('#utTabla th[data-th]').forEach((th) => {
            th.setAttribute('aria-sort', th.dataset.th === orden.campo ? (orden.dir === 1 ? 'ascending' : 'descending') : 'none');
        });
        const activos = tecnicos.filter((t) => t.activo).length;
        el('utResumen').textContent = '· ' + activos + (activos === 1 ? ' activo' : ' activos');
        pintarPaginas(visibles.length);
        el('utCuerpo').innerHTML = visibles.slice((pagina - 1) * POR_PAGINA, pagina * POR_PAGINA).map((t) =>
            '<tr' + (t.usuario_tecnico === editando ? ' class="is-editando"' : '') + '><td data-label="Técnico"><span class="ut-avatar" aria-hidden="true">' + iniciales(t.nombre_completo) + '</span><strong>' + esc(t.nombre_completo) + '</strong></td>' +
            '<td data-label="Usuario">' + esc(t.usuario_tecnico) + '</td>' +
            '<td data-label="Región"><span class="ut-region">' + ETIQUETA_REGION[t.region] + '</span></td>' +
            '<td data-label="Último ingreso" class="ut-fecha">' + esc(t.ultimo_login || 'Nunca') + '</td>' +
            '<td data-label="Estado"><label class="ut-switch"><input type="checkbox" role="switch" aria-label="Acceso de ' + esc(t.nombre_completo) + '" data-usuario="' + esc(t.usuario_tecnico) + '"' + (t.activo ? ' checked' : '') + '>' +
            '<span class="ut-switch-pista" aria-hidden="true"></span><span class="ut-switch-texto">' + (t.activo ? 'Activo' : 'Inactivo') + '</span></label></td>' +
            '<td class="ut-td-acciones"><button type="button" class="ut-accion" data-accion="clave" data-usuario="' + esc(t.usuario_tecnico) + '" aria-label="Cambiar contraseña de ' + esc(t.nombre_completo) + '"><i class="glyphicon glyphicon-lock" aria-hidden="true"></i> Contraseña</button>' +
            '<button type="button" class="ut-accion" data-accion="editar" data-usuario="' + esc(t.usuario_tecnico) + '" aria-label="Editar a ' + esc(t.nombre_completo) + '"><i class="glyphicon glyphicon-pencil" aria-hidden="true"></i> Editar</button></td></tr>'
        ).join('');
        el('utTabla').hidden = !visibles.length;
        el('utVacio').hidden = !!visibles.length;
        el('utVacioTexto').textContent = tecnicos.length ? 'No hay técnicos que coincidan con la búsqueda. Prueba con otro nombre o usuario.' : 'Aún no hay técnicos. Crea el primero con el formulario o carga varios con un Excel.';
        el('utVacioAccion').hidden = !!tecnicos.length;
    }

    // ---------- Formulario ----------
    function mostrarErrorForm(texto) {
        el('utFormError').textContent = texto;
        el('utFormError').hidden = !texto;
    }

    const CAMPOS_ERROR = { utUsuario: 'utUsuarioError', utClave: 'utClaveError', utRegion: 'utRegionError' };

    function errorCampo(idCampo, texto) {
        const mensaje = el(CAMPOS_ERROR[idCampo]);
        mensaje.textContent = texto || '';
        mensaje.hidden = !texto;
        if (texto) el(idCampo).setAttribute('aria-invalid', 'true'); else el(idCampo).removeAttribute('aria-invalid');
    }

    const limpiarErroresCampos = () => Object.keys(CAMPOS_ERROR).forEach((id) => errorCampo(id, ''));
    const existeUsuario = (usuario) => tecnicos.some((t) => t.usuario_tecnico.trim().toLowerCase() === usuario);
    const usuarioLimpio = () => sinTildes(el('utUsuario').value).toLowerCase().replace(/\s+/g, '');

    function validarUsuarioCampo() {
        const usuario = usuarioLimpio();
        const error = !/^[a-z0-9._-]{3,50}$/.test(usuario) ? 'Escribe un usuario de al menos 3 letras o números.' :
            (!editando && existeUsuario(usuario)) ? 'Ese usuario ya existe. Edítalo desde la lista.' : '';
        errorCampo('utUsuario', error);
        return !error;
    }

    function mostrarExito(usuario, clave, esNuevo) {
        el('utExitoTitulo').textContent = esNuevo ? 'Técnico creado' : 'Contraseña actualizada';
        el('utExitoUsuario').textContent = usuario;
        el('utExitoClave').textContent = clave;
        el('utExito').querySelectorAll('.ut-copiar').forEach((b) => { b.textContent = 'Copiar'; });
        el('utExitoCopiar').textContent = 'Copiar ambos';
        el('utExito').showModal();
    }

    function modoFormulario(tecnico, enfocarClave) {
        editando = tecnico ? tecnico.usuario_tecnico : null;
        el('utFormTitulo').textContent = tecnico ? 'Editando: ' + tecnico.nombre_completo : 'Nuevo Técnico';
        el('utGuardar').textContent = tecnico ? 'Guardar cambios' : '+ Crear Técnico';
        el('utClave').placeholder = tecnico ? 'Vacía = conservar la actual' : 'Mínimo 4 caracteres';
        el('utClaveReq').hidden = !!tecnico;
        el('utNombre').value = tecnico ? tecnico.nombre_completo : '';
        el('utUsuario').value = tecnico ? tecnico.usuario_tecnico : '';
        el('utUsuario').readOnly = !!tecnico;
        el('utClave').value = '';
        el('utRegion').value = tecnico ? tecnico.region : '';
        el('utCancelarEdicion').hidden = !tecnico;
        mostrarErrorForm('');
        limpiarErroresCampos();
        const form = el('utForm');
        form.classList.remove('ut-destello');
        form.classList.toggle('is-editando', !!tecnico);
        if (tecnico) {
            void form.offsetWidth;
            form.classList.add('ut-destello');
        }
        pintarLista();
        if (tecnico) el('utForm').scrollIntoView({ behavior: 'smooth', block: 'nearest' });
        el(enfocarClave ? 'utClave' : (tecnico ? 'utNombre' : 'utUsuario')).focus({ preventScroll: true });
    }

    el('utCancelarEdicion').addEventListener('click', async () => {
        const t = tecnicos.find((x) => x.usuario_tecnico === editando);
        const hayCambios = el('utClave').value !== '' || (!!t && (el('utNombre').value.trim() !== t.nombre_completo || el('utRegion').value !== t.region));
        if (hayCambios && !(await Confirmar('¿Descartar los cambios?', 'Se perderá lo que cambiaste de este técnico.', 'Descartar', true))) return;
        modoFormulario(null);
    });

    // El usuario va sin espacios (igual que en la carga por Excel): se quitan mientras se escribe.
    el('utUsuario').addEventListener('input', (ev) => { ev.target.value = ev.target.value.replace(/\s+/g, ''); errorCampo('utUsuario', ''); });
    el('utUsuario').addEventListener('blur', () => { if (!editando && el('utUsuario').value) validarUsuarioCampo(); });
    el('utClave').addEventListener('input', () => errorCampo('utClave', ''));
    el('utRegion').addEventListener('change', () => errorCampo('utRegion', ''));

    el('utOjo').addEventListener('click', () => {
        const campo = el('utClave');
        const visible = campo.type === 'password';
        campo.type = visible ? 'text' : 'password';
        el('utOjo').setAttribute('aria-pressed', visible);
        el('utOjo').setAttribute('aria-label', visible ? 'Ocultar contraseña' : 'Mostrar contraseña');
    });

    el('utForm').addEventListener('submit', async (ev) => {
        ev.preventDefault();
        const f = {
            nombre: el('utNombre').value.trim() || sinTildes(el('utUsuario').value).toLowerCase().replace(/\s+/g, ''),
            usuario: sinTildes(el('utUsuario').value).toLowerCase().replace(/\s+/g, ''),
            password: el('utClave').value,
            region: el('utRegion').value
        };
        const existe = existeUsuario(f.usuario);
        mostrarErrorForm('');
        const usuarioOk = validarUsuarioCampo();
        const errorClave = !existe && !f.password ? 'Escribe una contraseña para el técnico nuevo.' : (f.password && f.password.length < 4 ? 'La contraseña debe tener al menos 4 caracteres.' : '');
        errorCampo('utClave', errorClave);
        errorCampo('utRegion', f.region ? '' : 'Elige una región para el técnico.');
        const primerInvalido = !usuarioOk ? 'utUsuario' : errorClave ? 'utClave' : !f.region ? 'utRegion' : '';
        if (primerInvalido) {
            el(primerInvalido).focus();
            return;
        }
        const boton = el('utGuardar');
        const textoBoton = boton.textContent;
        boton.disabled = true;
        boton.textContent = 'Guardando…';
        try {
            const r = await pedir('upsert_usuarios_tecnicos.php', { filas: [f] });
            if (!r.success || r.errores.length) throw new Error(r.errores && r.errores[0] || r.message);
            const eraNuevo = !editando;
            mostrarMensaje(eraNuevo ? 'Técnico creado.' : 'Técnico actualizado.');
            if (f.password) mostrarExito(f.usuario, f.password, eraNuevo);
            modoFormulario(null);
            recargar();
        } catch (e) {
            mostrarErrorForm(e.message || 'No se pudo guardar. Intenta de nuevo.');
            boton.textContent = textoBoton;
        }
        boton.disabled = false;
    });

    // Copia un texto y lo confirma en el propio botón; la contraseña se borra de la pantalla al cerrar.
    async function copiarTexto(texto, boton, original) {
        try {
            await navigator.clipboard.writeText(texto);
            boton.textContent = 'Copiado ✓';
            setTimeout(() => { boton.textContent = original; }, 1800);
        } catch (e) {
            mostrarMensaje('No se pudo copiar. Selecciona el texto y cópialo a mano.', true);
        }
    }

    el('utExito').addEventListener('click', (ev) => {
        const b = ev.target.closest('[data-copiar]');
        if (b) copiarTexto(el(b.dataset.copiar).textContent, b, 'Copiar');
    });
    el('utExitoCopiar').addEventListener('click', (ev) => {
        copiarTexto('Usuario: ' + el('utExitoUsuario').textContent + '\nContraseña: ' + el('utExitoClave').textContent, ev.currentTarget, 'Copiar ambos');
    });
    el('utExitoCerrar').addEventListener('click', () => el('utExito').close());
    el('utExito').addEventListener('close', () => { el('utExitoClave').textContent = ''; });

    el('utTabla').querySelector('thead').addEventListener('click', (ev) => {
        const b = ev.target.closest('[data-orden]');
        if (!b) return;
        orden = { campo: b.dataset.orden, dir: orden.campo === b.dataset.orden ? -orden.dir : 1 };
        pagina = 1;
        pintarLista();
    });

    el('utBuscar').addEventListener('input', (ev) => { busqueda = ev.target.value; pagina = 1; pintarLista(); });

    el('utPaginas').addEventListener('click', (ev) => {
        const b = ev.target.closest('.ut-pag');
        if (!b || b.disabled) return;
        pagina = Number(b.dataset.pag);
        pintarLista();
    });

    el('utCuerpo').addEventListener('click', (ev) => {
        const b = ev.target.closest('[data-accion]');
        if (!b) return;
        const t = tecnicos.find((x) => x.usuario_tecnico === b.dataset.usuario);
        if (t) modoFormulario(t, b.dataset.accion === 'clave');
    });

    async function recargar() {
        try {
            const r = await pedir('get_usuarios_tecnicos.php');
            if (!r.success) throw new Error();
            tecnicos = r.tecnicos;
            pintarLista();
        } catch (e) {
            mostrarMensaje('No se pudo cargar la lista. Intenta de nuevo.', true);
        }
    }

    el('utCuerpo').addEventListener('change', async (ev) => {
        const caja = ev.target;
        const activo = caja.checked;
        if (!activo && !(await Confirmar('¿Desactivar a ' + caja.dataset.usuario + '?', 'No podrá iniciar sesión; sus obras siguen en Técnicos.', 'Desactivar', true))) {
            caja.checked = true;
            return;
        }
        caja.disabled = true;
        try {
            const r = await pedir('toggle_usuario_tecnico.php', { usuario: caja.dataset.usuario, activo: activo });
            if (!r.success) throw new Error();
            mostrarMensaje('Acceso de ' + caja.dataset.usuario + (activo ? ' activado.' : ' desactivado.'));
        } catch (e) {
            caja.checked = !activo;
            mostrarMensaje('No se pudo cambiar el acceso de ' + caja.dataset.usuario + '. Revisa tu conexión e intenta de nuevo.', true);
        }
        recargar();
    });

    // ---------- Excel ----------
    function normalizarEncabezado(t) {
        const h = sinTildes(t).toLowerCase().trim();
        if (h === 'nombre') return 'nombre';
        if (h === 'usuario') return 'usuario';
        if (h === 'region') return 'region';
        if (['contrasena', 'password', 'clave'].indexOf(h) >= 0) return 'password';
        return null;
    }

    function validarFila(f, vistos, existentes) {
        if (REGIONES.indexOf(f.region) < 0) return 'Región "' + f.regionOriginal + '" no existe. Usa Costa, Sierra, Austro o Todas.';
        if (!/^[a-z0-9._-]{3,50}$/.test(f.usuario)) return 'Falta un usuario válido (mínimo 3 letras o números).';
        if (vistos[f.usuario]) return 'Usuario repetido en el archivo.';
        if (!existentes[f.usuario] && !f.password) return 'Falta la contraseña de un técnico nuevo.';
        if (f.password && f.password.length < 4) return 'La contraseña debe tener al menos 4 caracteres.';
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
                f.region = sinTildes(f.region).toUpperCase().replace(/^TODOS$/, 'TODAS');
                f.usuario = sinTildes(f.usuario).toLowerCase().replace(/[ 	]+/g, '');
                f.nombre = f.nombre || f.usuario;
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
            ['Usuario', 'Contraseña', 'Región', 'Nombre'],
            ['jperez', 'clave-inicial', 'Sierra', 'Juan Pérez'],
        ]);
        const libro = XLSX.utils.book_new();
        XLSX.utils.book_append_sheet(libro, hoja, 'Tecnicos');
        XLSX.writeFile(libro, 'plantilla_usuarios_tecnicos.xlsx');
    });

    window.UsuariosTecnicosRecargar = recargar;
    recargar();
})();
