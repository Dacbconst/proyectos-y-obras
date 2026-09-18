<?php
require_once __DIR__ . '/../includes/auth_guard.php';
$usuario = exigir_sesion();
$tabActiva = 'proforma';
?>
<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1, viewport-fit=cover, interactive-widget=resizes-content">
<meta name="theme-color" content="#2E1A54">
<title>Proforma — Proyectos y Obras</title>
<link rel="stylesheet" href="../assets/css/base.css">
<link rel="stylesheet" href="../assets/css/components.css">
<link rel="stylesheet" href="../assets/css/layout.css">
</head>
<body>
    <header class="app-header">
        <h1>Gestión de Visitas</h1>
        <a class="logout-link" href="../auth/logout.php">Salir</a>
    </header>

    <main class="app-main">
        <div id="alerta" class="alert alert-error" style="display:none"></div>
        <div id="lista-proforma"></div>
        <div id="vacio" class="empty-state" style="display:none">No tienes agendamientos todavía.</div>
    </main>

    <?php include __DIR__ . '/_nav.php'; ?>

    <template id="tpl-agendamiento">
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
                <div data-campo="rondas"></div>

                <form data-form="nueva-ronda" style="margin-top:12px">
                    <div class="field">
                        <label>Características de la visita</label>
                        <textarea name="caracteristica_visita" rows="2"></textarea>
                    </div>
                    <div class="field">
                        <label>Acompañamiento técnico</label>
                        <textarea name="acompanamiento_tecnico" rows="2"></textarea>
                    </div>
                    <div class="field">
                        <label>Foto de evidencia</label>
                        <input type="file" name="evidencia" accept="image/*" capture="environment">
                        <img class="photo-preview" style="display:none">
                    </div>
                    <div class="field">
                        <label>Monto negociado (opcional)</label>
                        <input type="number" name="monto_total_factura" step="0.01" min="0">
                    </div>
                    <div class="field">
                        <label>Plazo en meses (opcional)</label>
                        <input type="number" name="plazo_meses" min="0">
                    </div>
                    <button type="submit" class="btn btn-primary btn-sm">Guardar ronda</button>
                </form>

                <form data-form="confirmar-factura" style="margin-top:12px">
                    <div class="field">
                        <label>Foto de factura</label>
                        <input type="file" name="foto_factura" accept="image/*" capture="environment">
                        <img class="photo-preview" style="display:none">
                    </div>
                    <div class="field">
                        <label>Condición de pago</label>
                        <select name="estado_pago">
                            <option value="directo">Directo</option>
                            <option value="a_plazos">A plazos</option>
                        </select>
                    </div>
                    <div class="field">
                        <label>Monto total factura</label>
                        <input type="number" name="monto_total_factura" step="0.01" min="0">
                    </div>
                    <div class="field">
                        <label>Plazo en meses (si es a plazos)</label>
                        <input type="number" name="plazo_meses" min="0">
                    </div>
                    <button type="submit" class="btn btn-secondary btn-sm">Confirmar factura</button>
                </form>

                <form data-form="cerrar" style="margin-top:12px">
                    <div class="field">
                        <label>Motivo de cierre (si ya no sigue)</label>
                        <input type="text" name="motivo_cierre">
                    </div>
                    <button type="submit" class="btn btn-danger btn-sm">Cerrar Proforma</button>
                </form>
            </div>
        </div>
    </template>

    <template id="tpl-ronda">
        <div class="registro-row">
            <div class="registro-numero" data-campo="numero"></div>
            <div class="registro-info">
                <div class="registro-titulo" data-campo="titulo"></div>
                <div class="registro-fecha" data-campo="fecha"></div>
            </div>
            <span class="registro-estado" data-campo="badge"></span>
            <a class="registro-ver" data-campo="ver" href="#" target="_blank" rel="noopener" style="display:none;text-decoration:none">Ver</a>
        </div>
        <div class="card-subtitle" data-campo="detalle" style="margin:2px 0 6px 32px"></div>
    </template>

    <script src="../assets/js/api.js"></script>
    <script src="../assets/js/camera-upload.js"></script>
    <script src="../assets/js/proforma.js"></script>
</body>
</html>
