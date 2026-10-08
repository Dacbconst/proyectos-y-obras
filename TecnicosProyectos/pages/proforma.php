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
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@700;800&family=Roboto:wght@400;500;700&display=swap">
<link rel="stylesheet" href="<?= asset('../assets/css/base.css') ?>">
<link rel="stylesheet" href="<?= asset('../assets/css/components.css') ?>">
<link rel="stylesheet" href="<?= asset('../assets/css/layout.css') ?>">
</head>
<body>
    <?php include __DIR__ . '/_header.php'; ?>

    <main class="app-main app-main-wide">
        <div class="page-intro">
            <p>Registra tus visitas con evidencia y envía la factura de cada obra.</p>
        </div>

        <div class="ag-barra">
        <div class="pills" id="pills-filtro">
            <button type="button" class="pill active" data-filtro="activos">Activos <span data-contador="activos"></span></button>
            <button type="button" class="pill" data-filtro="vencidas">Vencidas <span data-contador="vencidas"></span></button>
            <button type="button" class="pill" data-filtro="completados">Completados <span data-contador="completados"></span></button>
        </div>

        <div class="field-icon-wrap">
            <span class="field-input-icon">
                <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="11" cy="11" r="8"></circle><path d="m21 21-4.35-4.35"></path></svg>
            </span>
            <input type="text" id="buscador-empresa" placeholder="Buscar por empresa…">
        </div>
        </div>

        <div id="alerta" class="alert alert-error" style="display:none"></div>
        <div id="lista-proforma" class="ag-lista"></div>
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
                <button type="button" class="btn btn-ghost" id="btn-cancelar-cerrar">Cancelar</button>
                <button type="submit" class="btn btn-danger" id="btn-confirmar-cerrar">Confirmar rechazo</button>
            </div>
        </form>
    </dialog>

    <?php include __DIR__ . '/_nav.php'; ?>

    <template id="tpl-agendamiento">
        <article class="ag-card" data-id="">
            <button type="button" class="ag-head" data-campo="cabecera" aria-expanded="false">
                <span class="ag-fecha">
                    <span class="ag-fecha-dia" data-campo="dia"></span>
                    <span class="ag-fecha-mes" data-campo="mes"></span>
                </span>
                <span class="ag-resumen">
                    <span class="ag-empresa" data-campo="empresa"></span>
                    <span class="ag-pdv" data-campo="pdv"></span>
                </span>
                <span class="ag-lado">
                    <span class="badge" data-campo="badge"></span>
                    <svg class="ag-chevron" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><polyline points="6 9 12 15 18 9"></polyline></svg>
                </span>
            </button>

            <ol class="ag-pasos" data-campo="pasos">
                <li class="ag-paso" data-paso="visita">Visita</li>
                <li class="ag-paso" data-paso="validada">Validada</li>
                <li class="ag-paso" data-paso="facturada">Facturada</li>
            </ol>

            <div class="ag-body" data-campo="detalle" hidden>
                <section>
                    <h3 class="ag-titulo">Visitas registradas</h3>
                    <div data-campo="rondas"></div>
                    <p class="ag-vacio" data-campo="sin-rondas" hidden>Aún no registras ninguna visita.</p>
                </section>

                <div class="alert-warning" data-campo="alerta-correccion" hidden>
                    <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M10.29 3.86 1.82 18a2 2 0 0 0 1.71 3h16.94a2 2 0 0 0 1.71-3L13.71 3.86a2 2 0 0 0-3.42 0z"></path><line x1="12" y1="9" x2="12" y2="13"></line><line x1="12" y1="17" x2="12.01" y2="17"></line></svg>
                    <span data-campo="alerta-texto"></span>
                </div>

                <div class="ag-acciones" data-campo="acciones">
                    <button type="button" class="ag-accion ag-accion-visita" data-campo="btn-nueva-ronda">
                        <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M23 19a2 2 0 0 1-2 2H3a2 2 0 0 1-2-2V8a2 2 0 0 1 2-2h4l2-3h6l2 3h4a2 2 0 0 1 2 2z"></path><circle cx="12" cy="13" r="4"></circle></svg>
                        Registrar visita
                    </button>
                    <button type="button" class="ag-accion ag-accion-factura" data-campo="btn-enviar-factura">
                        <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M4 3h16v17l-3-2-2 2-2-2-2 2-2-2-2 2-3-2z"></path><path d="M8 8h8"></path><path d="M8 12h8"></path></svg>
                        Enviar factura
                    </button>
                </div>
                <p class="field-hint" data-campo="hint-factura" hidden>Registra una visita para poder enviar la factura.</p>

                <form class="ag-form" data-form="nueva-ronda" hidden>
                    <h3 class="ag-titulo">Nueva visita</h3>
                    <div class="ag-campo">
                        <span class="ag-campo-label">Evidencia fotográfica</span>
                        <label class="photo-field" data-campo="foto">
                            <input type="file" accept="image/*" hidden>
                            <span class="photo-dropzone">
                                <svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"><path d="M23 19a2 2 0 0 1-2 2H3a2 2 0 0 1-2-2V8a2 2 0 0 1 2-2h4l2-3h6l2 3h4a2 2 0 0 1 2 2z"></path><circle cx="12" cy="13" r="4"></circle></svg>
                                <span class="photo-dropzone-titulo">Tomar o subir foto</span>
                            </span>
                            <span class="photo-preview-wrap">
                                <img class="photo-preview" alt="Vista previa de la evidencia">
                                <span class="photo-cambiar">Cambiar foto</span>
                            </span>
                        </label>
                    </div>
                    <div class="field">
                        <label>Monto propuesto</label>
                        <input type="number" name="monto_total_factura" step="0.01" min="0" inputmode="decimal" placeholder="$ 0.00">
                    </div>
                    <div class="field">
                        <label>Característica de la visita</label>
                        <textarea name="caracteristica_visita" rows="3" placeholder="Escribe aquí…" required></textarea>
                    </div>
                    <div class="ag-form-botones">
                        <button type="button" class="btn btn-ghost" data-campo="cancelar-ronda">Cancelar</button>
                        <button type="submit" class="btn btn-primary">Guardar visita</button>
                    </div>
                </form>

                <form class="ag-form" data-form="confirmar-factura" hidden>
                    <h3 class="ag-titulo">Enviar factura</h3>
                    <div class="ag-campo">
                        <span class="ag-campo-label">Foto de la factura</span>
                        <label class="photo-field" data-campo="foto">
                            <input type="file" accept="image/*" hidden>
                            <span class="photo-dropzone">
                                <svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"><path d="M4 3h16v17l-3-2-2 2-2-2-2 2-2-2-2 2-3-2z"></path><path d="M8 8h8"></path><path d="M8 12h8"></path></svg>
                                <span class="photo-dropzone-titulo">Tomar o subir foto</span>
                            </span>
                            <span class="photo-preview-wrap">
                                <img class="photo-preview" alt="Vista previa de la factura">
                                <span class="photo-cambiar">Cambiar foto</span>
                            </span>
                        </label>
                    </div>
                    <div class="field">
                        <label>Condición de pago</label>
                        <select name="estado_pago">
                            <option value="directo">Directo</option>
                            <option value="a_plazos">A plazos</option>
                        </select>
                    </div>
                    <div class="field">
                        <label>Monto total de la factura</label>
                        <input type="number" name="monto_total_factura" step="0.01" min="0" inputmode="decimal" placeholder="$ 0.00" required>
                    </div>
                    <div class="field" data-campo="campo-plazo" hidden>
                        <label>Plazo en meses</label>
                        <input type="number" name="plazo_meses" min="2" inputmode="numeric" placeholder="Mínimo 2">
                    </div>
                    <div class="ag-form-botones">
                        <button type="button" class="btn btn-ghost" data-campo="cancelar-factura">Cancelar</button>
                        <button type="submit" class="btn btn-primary">Confirmar factura</button>
                    </div>
                </form>

                <div class="ag-pie">
                    <button type="button" class="btn-link" data-accion="abrir-cierre">Cierre Proforma</button>
                </div>
            </div>
        </article>
    </template>

    <template id="tpl-ronda">
        <div class="registro-row">
            <div class="registro-numero" data-campo="numero"></div>
            <div class="registro-info">
                <div class="registro-titulo" data-campo="titulo"></div>
                <div class="registro-fecha" data-campo="fecha"></div>
                <div class="registro-detalle" data-campo="detalle" hidden></div>
            </div>
            <span class="registro-estado" data-campo="badge"></span>
            <a class="registro-ver" data-campo="ver" href="#" target="_blank" rel="noopener" hidden>Ver</a>
        </div>
    </template>

    <script src="<?= asset('../assets/js/api.js') ?>"></script>
    <script src="<?= asset('../assets/js/camera-upload.js') ?>"></script>
    <script src="<?= asset('../assets/js/proforma.js') ?>"></script>
</body>
</html>
