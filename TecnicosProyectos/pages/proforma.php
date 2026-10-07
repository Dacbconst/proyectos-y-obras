<?php
require_once __DIR__ . '/../includes/auth_guard.php';
$usuario = exigir_sesion();
$tabActiva = 'proforma';
$tituloPagina = 'Gestión de Visitas';
?>
<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1, viewport-fit=cover, interactive-widget=resizes-content">
<meta name="theme-color" content="#2E124D">
<title>Proyectos y Obras Técnicos</title>
<link rel="stylesheet" href="../assets/css/base.css">
<link rel="stylesheet" href="../assets/css/components.css">
<link rel="stylesheet" href="../assets/css/layout.css">
</head>
<body>
    <?php include __DIR__ . '/_header.php'; ?>

    <main class="app-main">
        <div class="page-intro">
            <h1>Gestión de Visitas</h1>
            <p>Registra rondas de proforma, evidencia y factura de cada obra.</p>
        </div>

        <div class="pills" id="pills-filtro">
            <button type="button" class="pill active" data-filtro="activos">Activos <span data-contador="activos"></span></button>
            <button type="button" class="pill" data-filtro="vencidas">Vencidas <span data-contador="vencidas"></span></button>
            <button type="button" class="pill" data-filtro="completados">Completados <span data-contador="completados"></span></button>
        </div>

        <div class="field-icon-wrap" style="margin-bottom: var(--gap);">
            <span class="field-input-icon">
                <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="11" cy="11" r="8"></circle><path d="m21 21-4.35-4.35"></path></svg>
            </span>
            <input type="text" id="buscador-empresa" placeholder="Buscar por empresa…" style="height:40px;border-radius:10px;">
        </div>

        <div id="alerta" class="alert alert-error" style="display:none"></div>
        <div id="lista-proforma"></div>
        <div id="vacio" class="empty-state" style="display:none">No hay visitas en esta vista.</div>
    </main>

    <dialog class="dialog-cerrar" id="dialog-cerrar">
        <div class="dialog-cerrar-header">
            <span class="dialog-cerrar-icon">
                <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="12" cy="12" r="10"></circle><line x1="15" y1="9" x2="9" y2="15"></line><line x1="9" y1="9" x2="15" y2="15"></line></svg>
            </span>
            <strong>Cerrar proceso</strong>
        </div>
        <p style="margin:0 0 12px;font-size:12.5px;color:var(--color-text-muted);line-height:1.5;">Esta acción es irreversible. Indica el motivo del cierre.</p>
        <form method="dialog" id="form-cerrar-dialog">
            <div class="field" style="margin-bottom:4px;">
                <textarea id="cerrar-motivo" rows="3" maxlength="250" placeholder="Motivo del cierre…"></textarea>
            </div>
            <div class="dialog-cerrar-contador"><span id="cerrar-contador">0</span>/250</div>
            <div class="form-actions" style="margin-top:14px;">
                <button type="button" class="btn btn-outline" id="btn-cancelar-cerrar" style="display:inline-flex;">Cancelar</button>
                <button type="submit" class="btn btn-danger" id="btn-confirmar-cerrar">Confirmar rechazo</button>
            </div>
        </form>
    </dialog>

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
                <p class="section-label" style="margin-top:0">Historial de rondas</p>
                <div data-campo="rondas"></div>

                <div class="alert-warning" data-campo="alerta-correccion" style="display:none;margin-top:10px">
                    <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M10.29 3.86 1.82 18a2 2 0 0 0 1.71 3h16.94a2 2 0 0 0 1.71-3L13.71 3.86a2 2 0 0 0-3.42 0z"></path><line x1="12" y1="9" x2="12" y2="13"></line><line x1="12" y1="17" x2="12.01" y2="17"></line></svg>
                    <span data-campo="alerta-texto"></span>
                </div>

                <form data-form="nueva-ronda" style="margin-top:14px" data-campo="form-nueva-ronda">
                    <p class="section-label">Nueva ronda</p>
                    <div class="field photo-field">
                        <label>Evidencia fotográfica</label>
                        <button type="button" class="photo-dropzone" data-campo="dropzone">
                            <svg width="26" height="26" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"><path d="M23 19a2 2 0 0 1-2 2H3a2 2 0 0 1-2-2V8a2 2 0 0 1 2-2h4l2-3h6l2 3h4a2 2 0 0 1 2 2z"></path><circle cx="12" cy="13" r="4"></circle></svg>
                            <span>Toca para tomar o cargar la foto</span>
                        </button>
                        <div class="photo-preview-wrap">
                            <img class="photo-preview">
                        </div>
                        <input type="file" name="evidencia" accept="image/*" capture="environment" style="display:none" required>
                        <button type="button" class="btn-link" data-campo="cambiar-foto" style="display:none;margin-top:4px;">Cambiar foto</button>
                    </div>
                    <div class="field">
                        <label>Monto propuesto</label>
                        <input type="number" name="monto_total_factura" step="0.01" min="0" placeholder="$ 0.00">
                    </div>
                    <div class="field">
                        <label>Característica de la visita</label>
                        <textarea name="caracteristica_visita" rows="2" placeholder="Ej. Fachada 120m², requiere resane previo…" required></textarea>
                    </div>
                    <div class="field">
                        <label>Acompañamiento técnico</label>
                        <div class="radio-pill-group">
                            <label class="radio-pill"><input type="radio" name="acompanamiento_tecnico" value="SI" checked> Sí</label>
                            <label class="radio-pill"><input type="radio" name="acompanamiento_tecnico" value="NO"> No</label>
                        </div>
                    </div>
                    <button type="submit" class="btn btn-primary btn-block">Guardar ronda</button>
                </form>

                <p class="section-label">Factura</p>
                <form data-form="confirmar-factura">
                    <div class="field photo-field">
                        <label>Foto de factura</label>
                        <button type="button" class="photo-dropzone" data-campo="dropzone-factura">
                            <svg width="26" height="26" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"><path d="M4 3h16v17l-3-2-2 2-2-2-2 2-2-2-2 2-3-2z"></path><path d="M8 8h8"></path><path d="M8 12h8"></path></svg>
                            <span>Toca para tomar o cargar la foto</span>
                        </button>
                        <div class="photo-preview-wrap">
                            <img class="photo-preview">
                        </div>
                        <input type="file" name="foto_factura" accept="image/*" capture="environment" style="display:none">
                        <button type="button" class="btn-link" data-campo="cambiar-foto-factura" style="display:none;margin-top:4px;">Cambiar foto</button>
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
                    <button type="submit" class="btn btn-primary btn-block">Guardar</button>
                </form>

                <div style="text-align:center;margin-top:14px;">
                    <button type="button" class="btn-link" data-accion="abrir-cierre">Cierre Proforma</button>
                </div>
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
