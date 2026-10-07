<nav id="sidebar">
    <div class="sidebar-header">
        <div class="sidebar-brand">Proyectos y Obras</div>
        <button type="button" id="sidebarCollapse" class="sidebar-toggle" title="Mostrar/ocultar menú">
            <i class="glyphicon glyphicon-align-justify"></i>
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
            <a href="#sec-<?= $seccion['id'] ?>" data-toggle="section">
                <i class="glyphicon glyphicon-<?= $seccion['icono'] ?>"></i>
                <?= htmlspecialchars($seccion['label']) ?>
            </a>
        </li>
        <?php endforeach; ?>
    </ul>

    <!-- Switch de canales: separa tiendas Kywi (promotores) de obras
         (técnicos de TecnicosProyectos) para no corromper la analítica de
         ventas retail — ver sección 5 de CLAUDE.md. Global al sidebar (no
         por sección) porque varios módulos lo van a consumir. Anclado abajo
         del todo (sidebar-nav ocupa el espacio restante arriba). -->
    <div class="sidebar-canal" id="sidebarCanal">
        <label>Canal</label>
        <div class="sidebar-canal-switch" role="tablist">
            <button type="button" class="sidebar-canal-btn is-activo" data-canal="promotores">
                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M3 9.5 12 3l9 6.5"></path><path d="M5 9.5V20a1 1 0 0 0 1 1h3v-6h6v6h3a1 1 0 0 0 1-1V9.5"></path></svg>
                <span>Tiendas</span>
            </button>
            <button type="button" class="sidebar-canal-btn" data-canal="tecnicos">
                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M14.7 6.3a4 4 0 0 0-5.4 5.4l-6 6a1.5 1.5 0 0 0 2.1 2.1l6-6a4 4 0 0 0 5.4-5.4l-2.1 2.1-2-2z"></path></svg>
                <span>Técnicos</span>
            </button>
        </div>
    </div>
    <?php endif; ?>
</nav>
