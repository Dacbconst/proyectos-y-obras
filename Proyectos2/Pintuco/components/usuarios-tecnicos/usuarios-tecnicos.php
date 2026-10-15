<?php
// COMPONENTE: usuarios-tecnicos — repositorio de técnicos (login y región); la región filtra los PDV que ve cada técnico.
$modulo_base = basename((string) $cuenta_dir);
$ut_dir      = __DIR__;
$ut_assets   = $modulo_base . '/components/usuarios-tecnicos/assets';
$ut_css_v    = @filemtime($ut_dir . '/assets/usuarios-tecnicos.css') ?: time();
$ut_js_v     = @filemtime($ut_dir . '/assets/usuarios-tecnicos.js') ?: time();
?>
<link rel="stylesheet" href="<?= htmlspecialchars($ut_assets, ENT_QUOTES) ?>/usuarios-tecnicos.css?v=<?= $ut_css_v ?>">
<script src="https://cdnjs.cloudflare.com/ajax/libs/xlsx/0.18.5/xlsx.full.min.js"></script>

<div id="usuariosTecnicosApp" data-getters-base="<?= htmlspecialchars($modulo_base, ENT_QUOTES) ?>/getters/">

    <div class="ut-cabecera">
        <div>
            <h2 class="ut-titulo">Gestión de Técnicos</h2>
            <p class="ut-subtitulo">Administra el acceso y la región de los técnicos de obra.</p>
        </div>
        <div class="ut-acciones">
            <button type="button" class="btn btn-mod-actualizar" id="utPlantilla">
                <i class="glyphicon glyphicon-download-alt" aria-hidden="true"></i> Plantilla
            </button>
            <label class="btn ut-btn-principal" for="utArchivo">
                <i class="glyphicon glyphicon-upload" aria-hidden="true"></i> Cargar Excel
            </label>
            <input type="file" id="utArchivo" class="ut-archivo-oculto" accept=".xlsx,.xls">
        </div>
    </div>

    <div class="ut-previa" id="utPrevia" hidden>
        <div class="ut-previa-titulo">
            <strong>Revisa antes de aplicar</strong>
            <span id="utPreviaConteo"></span>
        </div>
        <div class="ut-tabla-wrap">
            <table class="ut-tabla">
                <thead><tr><th>Estado</th><th>Nombre</th><th>Usuario</th><th>Región</th><th>Contraseña</th></tr></thead>
                <tbody id="utPreviaCuerpo"></tbody>
            </table>
        </div>
        <div class="ut-previa-botones">
            <button type="button" class="btn btn-mod-actualizar" id="utCancelar">Cancelar</button>
            <button type="button" class="btn ut-btn-principal" id="utAplicar">Aplicar</button>
        </div>
    </div>

    <p class="ut-mensaje" id="utMensaje" role="status" hidden></p>

    <dialog class="ut-dialogo" id="utExito" aria-labelledby="utExitoTitulo">
        <div class="ut-dialogo-cab">
            <span class="ut-dialogo-icono" aria-hidden="true">
                <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.4" stroke-linecap="round" stroke-linejoin="round"><polyline points="20 6 9 17 4 12"></polyline></svg>
            </span>
            <div>
                <strong class="ut-dialogo-titulo" id="utExitoTitulo"></strong>
                <p class="ut-dialogo-sub">Entrega estos datos al técnico. La contraseña solo se muestra ahora.</p>
            </div>
        </div>
        <dl class="ut-dialogo-datos">
            <div class="ut-dato">
                <dt>Usuario</dt>
                <dd id="utExitoUsuario"></dd>
                <button type="button" class="ut-copiar" data-copiar="utExitoUsuario" aria-label="Copiar usuario">Copiar</button>
            </div>
            <div class="ut-dato">
                <dt>Contraseña</dt>
                <dd id="utExitoClave"></dd>
                <button type="button" class="ut-copiar" data-copiar="utExitoClave" aria-label="Copiar contraseña">Copiar</button>
            </div>
        </dl>
        <div class="ut-dialogo-pie">
            <button type="button" class="btn btn-mod-actualizar" id="utExitoCopiar">Copiar ambos</button>
            <button type="button" class="btn ut-btn-principal" id="utExitoCerrar">Listo</button>
        </div>
    </dialog>

    <div class="ut-layout">
        <form class="ut-card ut-form" id="utForm" autocomplete="off" novalidate>
            <div class="ut-card-cabecera"><i class="glyphicon glyphicon-user" aria-hidden="true"></i> <span id="utFormTitulo">Nuevo Técnico</span></div>
            <div class="ut-form-cuerpo">
                <label class="ut-label" for="utUsuario">Usuario de acceso *</label>
                <input class="ut-input" type="text" id="utUsuario" maxlength="50" placeholder="Ej. diego constante" autocomplete="off" autocapitalize="none" spellcheck="false" aria-describedby="utUsuarioError">
                <p class="ut-error-campo" id="utUsuarioError" hidden></p>

                <label class="ut-label" for="utNombre">Nombre completo <span class="ut-opcional">(opcional)</span></label>
                <input class="ut-input" type="text" id="utNombre" maxlength="100" placeholder="Nombre Apellido">

                <label class="ut-label" for="utClave">Contraseña <span id="utClaveReq">*</span></label>
                <div class="ut-clave">
                    <input class="ut-input" type="password" id="utClave" maxlength="100" autocomplete="new-password" placeholder="Mínimo 4 caracteres" aria-describedby="utClaveError">
                    <button type="button" class="ut-ojo" id="utOjo" aria-label="Mostrar contraseña" aria-pressed="false"><i class="glyphicon glyphicon-eye-open" aria-hidden="true"></i></button>
                </div>
                <p class="ut-error-campo" id="utClaveError" hidden></p>

                <label class="ut-label" for="utRegion">Región *</label>
                <select class="ut-input" id="utRegion" aria-describedby="utRegionError">
                    <option value="">Elige una región</option>
                    <option value="TODAS">Todas (todos los PDV)</option>
                    <option value="COSTA">Costa</option>
                    <option value="SIERRA">Sierra</option>
                    <option value="AUSTRO">Austro</option>
                </select>
                <p class="ut-error-campo" id="utRegionError" hidden></p>

                <p class="ut-form-error" id="utFormError" role="alert" hidden></p>
                <button type="submit" class="ut-btn-crear" id="utGuardar">+ Crear Técnico</button>
                <button type="button" class="ut-btn-cancelar" id="utCancelarEdicion" hidden>Cancelar edición</button>
            </div>
        </form>

        <section class="ut-card ut-lista">
            <div class="ut-card-cabecera ut-lista-cabecera">
                <span class="ut-lista-titulo">Técnicos Registrados <small id="utResumen"></small></span>
                <div class="ut-buscar">
                    <i class="glyphicon glyphicon-search" aria-hidden="true"></i>
                    <input type="search" id="utBuscar" placeholder="Buscar por nombre o usuario…" aria-label="Buscar técnico">
                </div>
            </div>
            <div class="ut-barra">
                <span class="ut-pagina-texto" id="utPaginaTexto"></span>
                <div class="ut-paginas" id="utPaginas" role="navigation" aria-label="Paginación"></div>
            </div>
            <div class="ut-tabla-wrap">
                <table class="ut-tabla" id="utTabla">
                    <thead><tr>
                        <th data-th="nombre_completo" aria-sort="ascending"><button type="button" class="ut-orden" data-orden="nombre_completo">Técnico</button></th>
                        <th data-th="usuario_tecnico" aria-sort="none"><button type="button" class="ut-orden" data-orden="usuario_tecnico">Usuario</button></th>
                        <th data-th="region" aria-sort="none"><button type="button" class="ut-orden" data-orden="region">Región</button></th>
                        <th data-th="ultimo_login" aria-sort="none"><button type="button" class="ut-orden" data-orden="ultimo_login">Último ingreso</button></th>
                        <th>Estado</th>
                        <th class="ut-th-acciones">Acciones</th>
                    </tr></thead>
                    <tbody id="utCuerpo"></tbody>
                </table>
            </div>
            <div class="ut-vacio ef-vacio-sin-datos" id="utVacio" hidden>
                <p id="utVacioTexto"></p>
                <label class="btn ut-btn-principal" id="utVacioAccion" for="utArchivo" hidden>Cargar Excel</label>
            </div>
        </section>
    </div>
</div>

<script src="<?= htmlspecialchars($ut_assets, ENT_QUOTES) ?>/usuarios-tecnicos.js?v=<?= $ut_js_v ?>"></script>
