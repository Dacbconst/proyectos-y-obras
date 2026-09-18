<?php
require_once __DIR__ . '/../includes/auth_guard.php';
$usuario = exigir_sesion();
$tabActiva = 'agenda';
?>
<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1, viewport-fit=cover, interactive-widget=resizes-content">
<meta name="theme-color" content="#2E1A54">
<title>Agenda — Proyectos y Obras</title>
<link rel="stylesheet" href="../assets/css/base.css">
<link rel="stylesheet" href="../assets/css/components.css">
<link rel="stylesheet" href="../assets/css/layout.css">
</head>
<body>
    <header class="app-header">
        <h1>Agenda</h1>
        <a class="logout-link" href="../auth/logout.php">Salir</a>
    </header>

    <main class="app-main">
        <div class="pills">
            <button class="pill active" data-filtro="pendientes">Pendientes</button>
            <button class="pill" data-filtro="visitadas">Visitados/Completadas</button>
        </div>

        <div id="alerta" class="alert alert-error" style="display:none"></div>
        <div id="lista-agenda"></div>
        <div id="vacio" class="empty-state" style="display:none">No hay visitas en esta vista.</div>
    </main>

    <?php include __DIR__ . '/_nav.php'; ?>

    <template id="tpl-tarjeta">
        <div class="card" data-id="">
            <div class="card-header card-header-dark">
                <div class="header-cols">
                    <div class="header-col">
                        <span class="header-col-label">Agendamiento</span>
                        <span class="header-col-value" data-campo="fecha"></span>
                    </div>
                    <div class="header-col">
                        <span class="header-col-label">Empresa</span>
                        <span class="header-col-value" data-campo="empresa"></span>
                    </div>
                    <div class="header-col">
                        <span class="header-col-label">Local</span>
                        <span class="header-col-value muted" data-campo="pdv"></span>
                    </div>
                </div>
                <div class="header-side">
                    <span class="badge" data-campo="badge"></span>
                    <svg class="chevron" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><polyline points="6 9 12 15 18 9"></polyline></svg>
                </div>
            </div>
            <div class="card-body" style="display:none" data-campo="detalle">
                <form data-form="editar">
                    <div class="field"><label>Contacto</label><input name="contacto"></div>
                    <div class="field"><label>Empresa</label><input name="empresa"></div>
                    <div class="field"><label>Correo</label><input name="mail" type="email"></div>
                    <div class="field"><label>Teléfono</label><input name="telefono" type="tel"></div>
                    <div class="field"><label>Dirección</label><textarea name="direccion" rows="2"></textarea></div>
                    <button type="submit" class="btn btn-secondary btn-sm">Guardar cambios</button>
                </form>
                <form data-form="reagendar" style="margin-top:10px">
                    <div class="field"><label>Nueva fecha</label><input name="fecha_agendamiento" type="date"></div>
                    <div class="field"><label>Nueva hora</label><input name="hora" type="time"></div>
                    <button type="submit" class="btn btn-secondary btn-sm">Reagendar</button>
                </form>
                <button type="button" class="btn btn-danger btn-sm" data-accion="eliminar" style="margin-top:10px">Eliminar visita</button>
            </div>
        </div>
    </template>

    <script src="../assets/js/api.js"></script>
    <script src="../assets/js/agenda.js"></script>
</body>
</html>
