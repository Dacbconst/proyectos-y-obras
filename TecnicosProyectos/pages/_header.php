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
            <img src="<?= asset('../assets/img/pintuco-header.png') ?>" alt="Pintuco" class="header-logo" width="56" height="26" decoding="async">
            <span class="header-identity-divider" aria-hidden="true"></span>
            <span class="header-brand-title">Proyectos y Obras</span>
        </div>

        <div class="header-user-menu" id="header-user-menu">
            <button type="button" class="header-user-trigger" id="header-user-trigger" aria-haspopup="true" aria-expanded="false" aria-controls="header-user-dropdown" aria-label="Perfil de usuario">
                <span class="header-user-name"><?= htmlspecialchars($nombreCompleto) ?></span>
                <span class="header-avatar-wrap">
                    <span class="header-user-avatar" aria-hidden="true"><?= htmlspecialchars($iniciales) ?></span>
                    <span class="status-indicator-dot" id="header-status-dot" aria-hidden="true"></span>
                </span>
                <svg class="header-user-chevron" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><polyline points="6 9 12 15 18 9"></polyline></svg>
            </button>

            <div class="header-user-dropdown" id="header-user-dropdown" role="menu" hidden>
                <div class="header-dropdown-user">
                    <span class="header-dropdown-avatar" aria-hidden="true"><?= htmlspecialchars($iniciales) ?></span>
                    <div>
                        <div class="header-dropdown-nombre"><?= htmlspecialchars($nombreCompleto) ?></div>
                        <div class="header-dropdown-role">Técnico de campo</div>
                    </div>
                </div>
                <span class="status-badge-live" id="header-status-badge">En línea</span>
                <div class="header-dropdown-divider"></div>
                <a href="../auth/logout.php" class="header-dropdown-logout" role="menuitem" onclick="return confirm('¿Seguro que deseas cerrar tu sesión?');">
                    <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4"></path><polyline points="16 17 21 12 16 7"></polyline><line x1="21" y1="12" x2="9" y2="12"></line></svg>
                    <span>Cerrar sesión</span>
                </a>
            </div>
        </div>
    </div>

    <div class="offline-banner" id="offline-banner" role="status" hidden>
        <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><line x1="1" y1="1" x2="23" y2="23"></line><path d="M16.72 11.06A10.94 10.94 0 0 1 19 12.55"></path><path d="M5 12.55a10.94 10.94 0 0 1 5.17-2.39"></path><path d="M10.71 5.05A16 16 0 0 1 22.58 9"></path><path d="M1.42 9a15.91 15.91 0 0 1 4.7-2.88"></path><path d="M8.53 16.11a6 6 0 0 1 6.95 0"></path><line x1="12" y1="20" x2="12.01" y2="20"></line></svg>
        <span><strong>Sin conexión.</strong> No podrás guardar ni enviar hasta que vuelva la señal.</span>
    </div>

    <div class="header-title-row">
        <h1 class="header-page-title"><?= htmlspecialchars($tituloPagina) ?></h1>
    </div>
</header>
<div class="header-scrim" id="header-scrim" hidden></div>
<script>
(function() {
    var trigger = document.getElementById('header-user-trigger');
    var dropdown = document.getElementById('header-user-dropdown');
    var scrim = document.getElementById('header-scrim');
    var banner = document.getElementById('offline-banner');
    var dot = document.getElementById('header-status-dot');
    var badge = document.getElementById('header-status-badge');
    if (!trigger || !dropdown) return;

    function setMenuState(open) {
        trigger.setAttribute('aria-expanded', open ? 'true' : 'false');
        dropdown.hidden = !open;
        scrim.hidden = !open;
    }

    function setConnectionState() {
        var online = navigator.onLine;
        banner.hidden = online;
        dot.classList.toggle('is-offline', !online);
        badge.classList.toggle('is-offline', !online);
        badge.textContent = online ? 'En línea' : 'Sin conexión';
    }

    trigger.addEventListener('click', function(e) {
        e.stopPropagation();
        setMenuState(trigger.getAttribute('aria-expanded') !== 'true');
    });

    scrim.addEventListener('click', function() { setMenuState(false); });

    document.addEventListener('keydown', function(e) {
        if (e.key === 'Escape' && !dropdown.hidden) {
            setMenuState(false);
            trigger.focus();
        }
    });

    window.addEventListener('online', setConnectionState);
    window.addEventListener('offline', setConnectionState);
    setConnectionState();
})();
</script>
