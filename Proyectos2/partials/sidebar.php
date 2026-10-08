<?php
// Íconos de trazo por sección (clave = id de la sección); si falta alguno se usa el glyphicon de mock_data.
$iconos_svg = [
    'principal'     => '<rect x="3" y="3" width="7" height="9" rx="1"></rect><rect x="14" y="3" width="7" height="5" rx="1"></rect><rect x="14" y="12" width="7" height="9" rx="1"></rect><rect x="3" y="16" width="7" height="5" rx="1"></rect>',
    'agendamientos' => '<rect x="3" y="4" width="18" height="18" rx="2"></rect><line x1="16" y1="2" x2="16" y2="6"></line><line x1="8" y1="2" x2="8" y2="6"></line><line x1="3" y1="10" x2="21" y2="10"></line>',
    'proforma'      => '<path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"></path><polyline points="14 2 14 8 20 8"></polyline><line x1="8" y1="13" x2="16" y2="13"></line><line x1="8" y1="17" x2="16" y2="17"></line>',
    'factura'       => '<path d="M4 3h16v18l-3-2-2 2-2-2-2 2-2-2-3 2z"></path><path d="M8 8h8"></path><path d="M8 12h8"></path>',
    'estado-flujo'  => '<circle cx="6" cy="6" r="2.5"></circle><circle cx="18" cy="18" r="2.5"></circle><path d="M8.5 6H15a3 3 0 0 1 3 3v6.5"></path>',
    'contactados'   => '<path d="M16 21v-2a4 4 0 0 0-4-4H6a4 4 0 0 0-4 4v2"></path><circle cx="9" cy="7" r="4"></circle><polyline points="16 11 18 13 22 9"></polyline>',
];
?>
<nav id="sidebar">
    <div class="sidebar-header">
        <div class="sidebar-brand">Proyectos y Obras</div>
        <button type="button" id="sidebarCollapse" class="sidebar-toggle" title="Mostrar/ocultar menú" aria-label="Mostrar u ocultar el menú">
            <span class="sidebar-toggle-icono">
                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><rect x="3" y="4" width="18" height="16" rx="2"></rect><line x1="9" y1="4" x2="9" y2="20"></line></svg>
            </span>
        </button>
    </div>

    <div class="sidebar-user">
        <div class="avatar"><img src="assets/img/avatar-default.webp" alt="" onerror="this.parentElement.innerHTML='&lt;i class=&quot;glyphicon glyphicon-user&quot;&gt;&lt;/i&gt;'"></div>
        <div>
            <div class="name"><?= htmlspecialchars($usuario_actual['nombre']) ?></div>
        </div>
    </div>

    <div class="sidebar-cuenta">
        <label for="selectCuenta">Cuenta</label>
        <select id="selectCuenta" class="form-control input-sm" onchange="location.href='?cuenta='+this.value">
            <option value="">Seleccione</option>
            <?php foreach ($cuentas_disponibles as $codigo => $detalle): ?>
            <option value="<?= htmlspecialchars($codigo) ?>" <?= $codigo === $cuenta_actual ? 'selected' : '' ?>>
                <?= htmlspecialchars($detalle) ?>
            </option>
            <?php endforeach; ?>
        </select>
    </div>

    <?php if ($cuenta_habilitada): ?>
    <ul class="sidebar-nav">
        <?php foreach ($secciones as $i => $seccion): ?>
        <li class="<?= $i === 0 ? 'active' : '' ?>">
            <a href="#sec-<?= $seccion['id'] ?>" data-toggle="section" title="<?= htmlspecialchars($seccion['label']) ?>">
                <?php if (isset($iconos_svg[$seccion['id']])): ?>
                <svg class="nav-icono" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><?= $iconos_svg[$seccion['id']] ?></svg>
                <?php else: ?>
                <i class="glyphicon glyphicon-<?= $seccion['icono'] ?>"></i>
                <?php endif; ?>
                <span class="nav-texto"><?= htmlspecialchars($seccion['label']) ?></span>
            </a>
        </li>
        <?php endforeach; ?>
    </ul>

    <!-- Switch de canales: separa tiendas (promotores) de obras (técnicos) — ver sección 5 de CLAUDE.md. -->
    <div class="sidebar-canal" id="sidebarCanal">
        <label>Canal</label>
        <div class="sidebar-canal-switch" role="tablist">
            <button type="button" class="sidebar-canal-btn is-activo" data-canal="promotores" title="Promotores">
                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><path d="M3 9.5 12 3l9 6.5"></path><path d="M5 9.5V20a1 1 0 0 0 1 1h3v-6h6v6h3a1 1 0 0 0 1-1V9.5"></path></svg>
                <span>Promotores</span>
            </button>
            <button type="button" class="sidebar-canal-btn" data-canal="tecnicos" title="Técnicos">
                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><path d="M14.7 6.3a4 4 0 0 0-5.4 5.4l-6 6a1.5 1.5 0 0 0 2.1 2.1l6-6a4 4 0 0 0 5.4-5.4l-2.1 2.1-2-2z"></path></svg>
                <span>Técnicos</span>
            </button>
        </div>
    </div>
    <?php endif; ?>
</nav>
