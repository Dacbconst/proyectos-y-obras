<?php
/** @var string $tabActiva 'contacto'|'agenda'|'proforma'|'facturas' */
$tabs = [
    'contacto' => ['label' => 'Contacto', 'icon' => '👤', 'href' => 'contacto.php'],
    'agenda' => ['label' => 'Agenda', 'icon' => '📅', 'href' => 'agenda.php'],
    'proforma' => ['label' => 'Proforma', 'icon' => '📋', 'href' => 'proforma.php'],
    'facturas' => ['label' => 'Facturas', 'icon' => '🧾', 'href' => 'facturas.php'],
];
?>
<nav class="bottom-nav">
    <?php foreach ($tabs as $key => $tab): ?>
        <a href="<?= $tab['href'] ?>" class="<?= $key === $tabActiva ? 'active' : '' ?>">
            <span class="nav-icon"><?= $tab['icon'] ?></span>
            <span><?= $tab['label'] ?></span>
        </a>
    <?php endforeach; ?>
</nav>
