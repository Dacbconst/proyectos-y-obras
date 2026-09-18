<?php
require_once __DIR__ . '/../includes/auth_guard.php';
$usuario = exigir_sesion();
$tabActiva = 'facturas';
?>
<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1, viewport-fit=cover, interactive-widget=resizes-content">
<meta name="theme-color" content="#2E1A54">
<title>Facturas — Proyectos y Obras</title>
<link rel="stylesheet" href="../assets/css/base.css">
<link rel="stylesheet" href="../assets/css/components.css">
<link rel="stylesheet" href="../assets/css/layout.css">
</head>
<body>
    <header class="app-header">
        <h1>Facturas</h1>
        <a class="logout-link" href="../auth/logout.php">Salir</a>
    </header>

    <main class="app-main">
        <button type="button" class="filters-toggle" id="btn-filtros">Filtros ▾</button>
        <div class="filters-panel" id="panel-filtros">
            <div class="pills">
                <button class="pill active" data-tipo="todo">Todo</button>
                <button class="pill" data-tipo="directo">Directo</button>
                <button class="pill" data-tipo="a_plazos">A plazos</button>
            </div>
            <div class="field">
                <label>Buscar por empresa</label>
                <input type="text" id="buscar-empresa" placeholder="Empresa…">
            </div>
        </div>

        <div id="alerta" class="alert alert-error" style="display:none"></div>
        <div id="lista-facturas"></div>
        <div id="vacio" class="empty-state" style="display:none">No hay facturas registradas.</div>
    </main>

    <?php include __DIR__ . '/_nav.php'; ?>

    <template id="tpl-factura">
        <div class="card">
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
            <div class="card-body" style="display:none;padding:0" data-campo="detalle">
                <div class="factura-detalle">
                    <div class="factura-fila">
                        <img class="factura-foto" data-campo="foto" alt="">
                        <div class="factura-info">
                            <div class="factura-label" data-campo="tipo"></div>
                            <div class="factura-sub" data-campo="sub"></div>
                        </div>
                        <div class="factura-montos">
                            <div class="factura-monto-pagado" data-campo="pagado"></div>
                            <div class="factura-monto-total" data-campo="total"></div>
                        </div>
                    </div>

                    <div data-campo="cuotas" style="margin-top:10px"></div>

                    <form data-form="nueva-cuota" style="margin-top:10px">
                        <div class="field"><label>N° de cuota</label><input type="number" name="numero_cuota" min="1" required></div>
                        <div class="field"><label>Monto</label><input type="number" name="monto_pago" step="0.01" min="0" required></div>
                        <div class="field"><label>Fecha de pago</label><input type="date" name="fecha_pago" required></div>
                        <div class="field"><label>Comprobante</label>
                            <input type="file" name="foto_pago" accept="image/*" capture="environment">
                            <img class="photo-preview" style="display:none">
                        </div>
                        <div class="field"><label>Observación (opcional)</label><input type="text" name="observacion"></div>
                        <button type="submit" class="btn btn-secondary btn-sm">📷 Adjuntar otra factura</button>
                    </form>

                    <form data-form="cerrar-plan" style="margin-top:10px">
                        <div class="field"><label>Motivo de cierre del plan</label><input type="text" name="motivo_cierre_pago"></div>
                        <button type="submit" class="btn btn-danger btn-sm">Cierre Factura</button>
                    </form>
                </div>
            </div>
        </div>
    </template>

    <template id="tpl-cuota">
        <div class="registro-row">
            <div class="registro-info">
                <div class="registro-titulo" data-campo="linea"></div>
            </div>
        </div>
    </template>

    <script src="../assets/js/api.js"></script>
    <script src="../assets/js/camera-upload.js"></script>
    <script src="../assets/js/facturas.js"></script>
</body>
</html>
