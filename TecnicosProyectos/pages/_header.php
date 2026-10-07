<?php
/**
 * @var string $tituloPagina Título de la página (ej. "Agenda")
 * @var string $usuario Usuario técnico de la sesión (viene de exigir_sesion())
 */
$nombreCompleto = $_SESSION['nombre_completo'] ?? $usuario;
$iniciales = '';
foreach (array_slice(preg_split('/\s+/', trim($nombreCompleto)), 0, 2) as $parte) {
    $iniciales .= mb_strtoupper(mb_substr($parte, 0, 1));
}
?>
<header class="app-header">
    <div class="header-top-row">
        <div class="header-identity">
            <span class="header-brand-icon" aria-hidden="true">
                <svg width="19" height="19" viewBox="0 0 24 24" fill="none" stroke="#FFFFFF" stroke-width="2.2" stroke-linecap="round" stroke-linejoin="round">
                    <rect x="3" y="4" width="18" height="16" rx="2"></rect>
                    <path d="M3 9h18"></path>
                    <path d="M9 4v16"></path>
                </svg>
            </span>
            <div class="header-brand-info">
                <span class="header-brand-title">Proyectos y Obras</span>
                <span class="header-brand-badge">Técnicos</span>
            </div>
        </div>

        <div class="header-user-menu" id="header-user-menu">
            <button type="button" class="header-user-trigger" id="header-user-trigger" aria-haspopup="true" aria-expanded="false" aria-label="Perfil de usuario">
                <span class="header-user-name"><?= htmlspecialchars($nombreCompleto) ?></span>
                <div class="header-avatar-wrap">
                    <span class="header-user-avatar"><?= htmlspecialchars($iniciales) ?></span>
                    <span class="status-indicator-dot" title="En línea"></span>
                </div>
                <svg class="header-user-chevron" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><polyline points="6 9 12 15 18 9"></polyline></svg>
            </button>

            <div class="header-user-dropdown" id="header-user-dropdown" role="menu" hidden>
                <div class="header-dropdown-user-mobile">
                    <strong><?= htmlspecialchars($nombreCompleto) ?></strong>
                </div>
                <div class="header-dropdown-status-row">
                    <span class="status-badge-live">● En línea</span>
                    <span class="header-dropdown-role">Técnico de Campo</span>
                </div>
                <div class="header-dropdown-divider"></div>
                <a href="../auth/logout.php" class="header-dropdown-logout" role="menuitem" onclick="return confirm('¿Seguro que deseas cerrar tu sesión?');">
                    <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4"></path><polyline points="16 17 21 12 16 7"></polyline><line x1="21" y1="12" x2="9" y2="12"></line></svg>
                    <span>Cerrar sesión</span>
                </a>
            </div>
        </div>
    </div>

    <div class="header-title-row">
        <h1 class="header-page-title"><?= htmlspecialchars($tituloPagina) ?></h1>
    </div>
</header>
<script>
(function() {
    var trigger = document.getElementById('header-user-trigger');
    var dropdown = document.getElementById('header-user-dropdown');
    if (!trigger || !dropdown) return;

    function setMenuState(open) {
        trigger.setAttribute('aria-expanded', open ? 'true' : 'false');
        dropdown.hidden = !open;
    }

    trigger.addEventListener('click', function(e) {
        e.stopPropagation();
        var isOpen = trigger.getAttribute('aria-expanded') === 'true';
        setMenuState(!isOpen);
    });

    document.addEventListener('click', function(e) {
        if (!dropdown.hidden && !dropdown.contains(e.target) && !trigger.contains(e.target)) {
            setMenuState(false);
        }
    });

    document.addEventListener('keydown', function(e) {
        if (e.key === 'Escape' && !dropdown.hidden) {
            setMenuState(false);
            trigger.focus();
        }
    });
})();
</script>
