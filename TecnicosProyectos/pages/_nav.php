<?php
/** @var string $tabActiva 'contacto'|'agenda'|'proforma'|'facturas' */
$tabs = [
    'contacto' => [
        'label' => 'Contacto',
        'href' => 'contacto.php',
        'icon' => '<path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"></path><circle cx="12" cy="7" r="4"></circle>',
    ],
    'agenda' => [
        'label' => 'Agenda',
        'href' => 'agenda.php',
        'icon' => '<rect x="3" y="4" width="18" height="18" rx="2"></rect><path d="M16 2v4"></path><path d="M8 2v4"></path><path d="M3 10h18"></path>',
    ],
    'proforma' => [
        'label' => 'Proforma',
        'href' => 'proforma.php',
        'icon' => '<path d="M14 3v4a1 1 0 0 0 1 1h4"></path><path d="M17 21H7a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h7l5 5v11a2 2 0 0 1-2 2z"></path><path d="M9 13h6"></path><path d="M9 17h6"></path>',
    ],
    'facturas' => [
        'label' => 'Facturas',
        'href' => 'facturas.php',
        'icon' => '<path d="M4 3h16v17l-3-2-2 2-2-2-2 2-2-2-2 2-3-2z"></path><path d="M8 8h8"></path><path d="M8 12h8"></path>',
    ],
];
?>
<nav class="bottom-nav">
    <?php foreach ($tabs as $key => $tab): ?>
        <a href="<?= $tab['href'] ?>" class="<?= $key === $tabActiva ? 'active' : '' ?>">
            <span class="nav-indicator"></span>
            <svg class="nav-icon" width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><?= $tab['icon'] ?></svg>
            <span class="nav-label"><?= $tab['label'] ?></span>
        </a>
    <?php endforeach; ?>
    <a href="../auth/logout.php" class="nav-logout">
        <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4"></path><polyline points="16 17 21 12 16 7"></polyline><line x1="21" y1="12" x2="9" y2="12"></line></svg>
        <span>Cerrar sesión</span>
    </a>
</nav>
