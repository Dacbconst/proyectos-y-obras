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
        <p class="ut-resumen" id="utResumen">Cargando…</p>
        <div class="ut-acciones">
            <button type="button" class="btn btn-mod-actualizar" id="utPlantilla">
                <i class="glyphicon glyphicon-download-alt"></i> Plantilla
            </button>
            <label class="btn ut-btn-principal" for="utArchivo">
                <i class="glyphicon glyphicon-upload"></i> Cargar Excel
            </label>
            <input type="file" id="utArchivo" class="ut-archivo-oculto" accept=".xlsx,.xls">
        </div>
    </div>

    <div class="ut-pills" id="utPills" role="tablist" aria-label="Filtrar por región"></div>

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

    <div class="ut-tabla-wrap">
        <table class="ut-tabla" id="utTabla">
            <thead><tr><th>Nombre</th><th>Usuario</th><th>Región</th><th>Estado</th><th>Último ingreso</th></tr></thead>
            <tbody id="utCuerpo"></tbody>
        </table>
    </div>

    <div class="ut-vacio" id="utVacio" hidden>
        <p>No hay técnicos en esta región.</p>
    </div>
</div>

<script src="<?= htmlspecialchars($ut_assets, ENT_QUOTES) ?>/usuarios-tecnicos.js?v=<?= $ut_js_v ?>"></script>
