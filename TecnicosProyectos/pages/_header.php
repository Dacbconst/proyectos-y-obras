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
    <div class="header-mobile">
        <span class="header-eyebrow">Técnicos</span>
        <h1><?= htmlspecialchars($tituloPagina) ?></h1>
    </div>

    <div class="header-brand">
        <span class="header-brand-icon">
            <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="#FFFFFF" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><rect x="3" y="4" width="18" height="16" rx="2"></rect><path d="M3 9h18"></path><path d="M9 4v16"></path></svg>
        </span>
        <span class="header-brand-text">
            <span class="header-brand-title">Proyectos y Obras</span>
            <span class="header-brand-sub">Técnicos</span>
        </span>
    </div>

    <div class="header-user">
        <span class="header-user-name"><?= htmlspecialchars($nombreCompleto) ?></span>
        <span class="header-user-avatar"><?= htmlspecialchars($iniciales) ?></span>
    </div>

    <a class="header-logout-btn" href="../auth/logout.php" aria-label="Cerrar sesión" title="Cerrar sesión">
        <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4"></path><polyline points="16 17 21 12 16 7"></polyline><line x1="21" y1="12" x2="9" y2="12"></line></svg>
    </a>
</header>
