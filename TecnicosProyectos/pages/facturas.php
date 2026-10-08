<?php
require_once __DIR__ . '/../includes/auth_guard.php';
$usuario = exigir_sesion();
$tabActiva = 'facturas';
$tituloPagina = 'Facturas';
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
        <div class="field-icon-wrap" style="margin-bottom: var(--gap);">
            <span class="field-input-icon">
                <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="11" cy="11" r="8"></circle><path d="m21 21-4.35-4.35"></path></svg>
            </span>
            <input type="text" id="buscar-empresa" placeholder="Buscar por empresa…" style="height:40px;border-radius:10px;">
        </div>

        <!-- Selector de mes: oculto en la pill "Todo" (esa mira todos los meses). -->
        <div class="field" id="selector-mes-wrap" style="margin-bottom: var(--gap);">
            <input type="month" id="selector-mes" style="height:44px;border-radius:10px;font-weight:700;">
        </div>

        <!-- Pills A plazos / Directo / Todo — categoría real viene de plazo_meses
             (ver facturas.js: esAPlazos), no de estado_pago. -->
        <div class="pills" id="pills-tipo">
            <button type="button" class="pill active" data-tipo="a_plazos">A plazos <span data-contador="a_plazos"></span></button>
            <button type="button" class="pill" data-tipo="directo">Directo <span data-contador="directo"></span></button>
            <button type="button" class="pill" data-tipo="todo">Todo <span data-contador="todo"></span></button>
        </div>

        <div id="alerta" class="alert alert-error" style="display:none"></div>
        <div id="contador-facturas" class="card-subtitle" style="margin-bottom:8px"></div>
        <div id="lista-facturas"></div>
        <div id="vacio" class="empty-state" style="display:none">No hay facturas en esta vista.</div>
    </main>

    <!-- Mismo diálogo de "Cerrar proceso" que usa Proforma — acá cierra el plan de pago a plazos. -->
    <dialog class="dialog-cerrar" id="dialog-cerrar">
        <div class="dialog-cerrar-header">
            <span class="dialog-cerrar-icon">
                <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="12" cy="12" r="10"></circle><line x1="15" y1="9" x2="9" y2="15"></line><line x1="9" y1="9" x2="15" y2="15"></line></svg>
            </span>
            <strong>Cierre Factura</strong>
        </div>
        <p style="margin:0 0 12px;font-size:12.5px;color:var(--color-text-muted);line-height:1.5;">Corta el cobro de esta factura a plazos antes de llegar al total cotizado. Esta acción es irreversible.</p>
        <form method="dialog" id="form-cerrar-dialog">
            <div class="field" style="margin-bottom:4px;">
                <textarea id="cerrar-motivo" rows="3" maxlength="250" placeholder="Motivo del cierre…"></textarea>
            </div>
            <div class="dialog-cerrar-contador"><span id="cerrar-contador">0</span>/250</div>
            <div class="form-actions" style="margin-top:14px;">
                <button type="button" class="btn btn-outline" id="btn-cancelar-cerrar" style="display:inline-flex;">Cancelar</button>
                <button type="submit" class="btn btn-danger" id="btn-confirmar-cerrar">Confirmar cierre</button>
            </div>
        </form>
    </dialog>

    <!-- Visor de foto (factura o comprobante de pago) — solo lectura, con zoom. -->
    <dialog class="dialog-foto" id="dialog-foto">
        <img class="dialog-foto-img" id="dialog-foto-img" alt="">
        <div class="dialog-foto-footer">
            <span id="dialog-foto-texto" style="font-size:12.5px;color:var(--color-text-muted);"></span>
            <button type="button" class="btn btn-outline" id="btn-cerrar-foto">Cerrar</button>
        </div>
    </dialog>

    <?php include __DIR__ . '/_nav.php'; ?>

    <template id="tpl-separador">
        <div class="separador-mes" data-campo="texto"></div>
    </template>

    <template id="tpl-factura">
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

            <div class="card-body" style="display:none;padding:0" data-campo="detalle">
                <div class="factura-detalle">
                    <div class="factura-fila" data-campo="fila-foto" style="cursor:pointer">
                        <img class="factura-foto" data-campo="foto" alt="">
                        <div class="factura-info">
                            <div class="factura-label" data-campo="tipo"></div>
                            <div class="factura-sub" data-campo="sub"></div>
                        </div>
                        <div class="factura-montos">
                            <div class="factura-monto-pagado" data-campo="pagado"></div>
                            <div class="factura-monto-total" data-campo="total" style="display:none"></div>
                        </div>
                    </div>

                    <button type="button" class="btn-link" data-campo="btn-cerrar" style="float:right;margin-top:6px;display:none">Cierre Factura</button>
                    <span class="badge badge-cerrado" data-campo="chip-cerrada" style="float:right;margin-top:6px;display:none">Factura cerrada</span>
                    <div style="clear:both"></div>

                    <div data-campo="cuotas" style="margin-top:4px"></div>

                    <button type="button" class="btn-adjuntar-factura" data-campo="btn-adjuntar" style="display:none">
                        <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M23 19a2 2 0 0 1-2 2H3a2 2 0 0 1-2-2V8a2 2 0 0 1 2-2h4l2-3h6l2 3h4a2 2 0 0 1 2 2z"></path><circle cx="12" cy="13" r="4"></circle></svg>
                        Adjuntar otra factura
                    </button>

                    <form data-form="nuevo-pago" style="display:none;margin-top:10px">
                        <div class="field photo-field">
                            <label>Comprobante</label>
                            <button type="button" class="photo-dropzone" data-campo="dropzone-pago">
                                <svg width="26" height="26" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"><path d="M23 19a2 2 0 0 1-2 2H3a2 2 0 0 1-2-2V8a2 2 0 0 1 2-2h4l2-3h6l2 3h4a2 2 0 0 1 2 2z"></path><circle cx="12" cy="13" r="4"></circle></svg>
                                <span>Toca para tomar o cargar la foto</span>
                            </button>
                            <div class="photo-preview-wrap"><img class="photo-preview"></div>
                            <input type="file" name="foto_pago" accept="image/*" style="display:none" required>
                            <button type="button" class="btn-link" data-campo="cambiar-foto-pago" style="display:none;margin-top:4px;">Cambiar foto</button>
                        </div>
                        <div class="field">
                            <label>Monto</label>
                            <input type="number" name="monto_pago" step="0.01" min="0" placeholder="$ 0.00" required>
                        </div>
                        <div class="field">
                            <label>Observación (opcional)</label>
                            <input type="text" name="observacion">
                        </div>
                        <div class="form-actions" style="margin-top:10px">
                            <button type="button" class="btn btn-outline" data-campo="cancelar-pago">Cancelar</button>
                            <button type="submit" class="btn btn-primary">Guardar</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </template>

    <template id="tpl-pago">
        <div class="pago-row">
            <img class="pago-thumb" data-campo="thumb" alt="">
            <div class="pago-info">
                <div class="pago-titulo" data-campo="titulo"></div>
                <div class="pago-fecha" data-campo="fecha"></div>
            </div>
        </div>
    </template>

    <script src="../assets/js/api.js"></script>
    <script src="../assets/js/camera-upload.js"></script>
    <script src="../assets/js/facturas.js"></script>
</body>
</html>
