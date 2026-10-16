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
            <p>Envía la proforma de cada obra visitada y luego su factura.</p>
        </div>

        <div class="ag-barra">
        <div class="pills" id="pills-filtro">
            <button type="button" class="pill active" data-filtro="activos">Activos <span data-contador="activos"></span></button>
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
        <div id="vacio" class="empty-state" style="display:none">No hay obras en esta vista.</div>
    </main>

    <!-- Enviar factura: paso final que cierra el ciclo; hoja inferior con identidad ámbar, distinta del detalle de proforma. -->
    <dialog class="dialog-ver dialog-factura" id="dialog-factura" aria-labelledby="fac-titulo">
        <span class="ver-asa" aria-hidden="true"></span>
        <div class="ver-cabecera">
            <span class="fac-icono"><svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><path d="M4 3h16v17l-3-2-2 2-2-2-2 2-2-2-2 2-3-2z"></path><path d="M8 8h8"></path><path d="M8 12h8"></path></svg></span>
            <div class="ver-titulos">
                <strong id="fac-titulo">Enviar factura</strong>
                <span class="ver-subtitulo" id="fac-obra"></span>
            </div>
            <span class="fac-etiqueta">Paso final</span>
            <button type="button" class="ver-cerrar" id="fac-cerrar" aria-label="Cerrar"><svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.4" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><line x1="18" y1="6" x2="6" y2="18"></line><line x1="6" y1="6" x2="18" y2="18"></line></svg></button>
        </div>

        <form id="form-factura" novalidate>
            <div class="fac-resumen">
                Proforma: <strong id="fac-propuesto"></strong> · <span id="fac-rondas"></span>
            </div>

            <div class="ag-campo">
                <span class="ag-campo-label">Foto de la factura (opcional)</span>
                <div class="photo-field" data-campo="foto">
                            <input type="file" accept="image/*" capture="environment" data-origen="camara" hidden>
                            <input type="file" accept="image/*" data-origen="galeria" hidden>
                            <span class="photo-dropzone">
                                <svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"><path d="M4 3h16v17l-3-2-2 2-2-2-2 2-2-2-2 2-3-2z"></path><path d="M8 8h8"></path><path d="M8 12h8"></path></svg>
                                <span class="photo-dropzone-titulo">Agrega la foto</span>
<span class="photo-acciones"><button type="button" class="photo-btn" data-foto="camara"><svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><path d="M23 19a2 2 0 0 1-2 2H3a2 2 0 0 1-2-2V8a2 2 0 0 1 2-2h4l2-3h6l2 3h4a2 2 0 0 1 2 2z"></path><circle cx="12" cy="13" r="4"></circle></svg> Cámara</button>
<button type="button" class="photo-btn" data-foto="galeria"><svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><rect x="3" y="3" width="18" height="18" rx="2"></rect><circle cx="8.5" cy="8.5" r="1.5"></circle><path d="m21 15-5-5L5 21"></path></svg> Galería</button></span>
                            </span>
                            <span class="photo-preview-wrap">
                                <img class="photo-preview" alt="Vista previa de la factura">
                                <span class="photo-cambiar"><button type="button" class="photo-btn" data-foto="camara"><svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><path d="M23 19a2 2 0 0 1-2 2H3a2 2 0 0 1-2-2V8a2 2 0 0 1 2-2h4l2-3h6l2 3h4a2 2 0 0 1 2 2z"></path><circle cx="12" cy="13" r="4"></circle></svg> Cámara</button>
<button type="button" class="photo-btn" data-foto="galeria"><svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><rect x="3" y="3" width="18" height="18" rx="2"></rect><circle cx="8.5" cy="8.5" r="1.5"></circle><path d="m21 15-5-5L5 21"></path></svg> Galería</button></span>
                            </span>
                        </div>
            </div>

            <fieldset class="ag-campo fac-pago-wrap" hidden>
                <legend class="ag-campo-label">Condición de pago</legend>
                <div class="fac-pago">
                    <label class="fac-opcion">
                        <input type="radio" name="estado_pago" value="directo" checked>
                        <span class="fac-opcion-cuerpo"><strong>Directo</strong><small>Una sola factura por el 100 %</small></span>
                    </label>
                    <label class="fac-opcion">
                        <input type="radio" name="estado_pago" value="a_plazos">
                        <span class="fac-opcion-cuerpo"><strong>A plazos</strong><small>Se paga en cuotas; las registras luego en Facturas</small></span>
                    </label>
                </div>
            </fieldset>

            <div class="ag-campo" data-campo="campo-plazo" hidden>
                <label class="ag-campo-label" for="fac-plazo">Plazo en meses</label>
                <div class="fac-chips">
                    <button type="button" class="fac-chip" data-meses="3">3</button>
                    <button type="button" class="fac-chip" data-meses="6">6</button>
                    <button type="button" class="fac-chip" data-meses="12">12</button>
                    <button type="button" class="fac-chip" data-meses="18">18</button>
                    <input type="number" id="fac-plazo" class="fac-plazo-otro" name="plazo_meses" min="2" max="120" inputmode="numeric" placeholder="Otro">
                </div>
            </div>

            <div class="ag-campo" data-campo="campo-meta" hidden>
                <span class="ag-campo-label">Meta del plan</span>
                <p class="fac-meta" id="fac-meta-texto"></p>
                <div class="fac-monto" id="fac-meta-wrap" hidden>
                    <span aria-hidden="true">$</span>
                    <input type="text" id="fac-meta" name="meta_plan" data-monto inputmode="decimal" autocomplete="off" maxlength="8" placeholder="Total de la proforma">
                </div>
            </div>

            <div class="ag-campo">
                <label class="ag-campo-label" for="fac-monto" id="fac-monto-label">Valor de la factura</label>
                <div class="fac-monto">
                    <span aria-hidden="true">$</span>
                    <input type="text" id="fac-monto" name="monto_total_factura" data-monto inputmode="decimal" autocomplete="off" maxlength="8" placeholder="0.00">
                </div>
            </div>

            <div class="alert-warning">
                <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M10.29 3.86 1.82 18a2 2 0 0 0 1.71 3h16.94a2 2 0 0 0 1.71-3L13.71 3.86a2 2 0 0 0-3.42 0z"></path><line x1="12" y1="9" x2="12" y2="13"></line><line x1="12" y1="17" x2="12.01" y2="17"></line></svg>
                <span><strong>Esto cierra el ciclo de proformas.</strong> Registra el monto de la factura y confirma.</span>
            </div>
            <div id="fac-alerta" class="alert alert-error" style="display:none"></div>

            <div class="ag-form-botones">
                <button type="button" class="btn btn-ghost" id="fac-cancelar">Cancelar</button>
                <button type="submit" class="btn fac-confirmar" id="fac-confirmar">Confirmar y cerrar</button>
            </div>
        </form>
    </dialog>

    <!-- Detalle de una proforma enviada: hoja inferior en celular, tarjeta centrada en escritorio. -->
    <dialog class="dialog-ver" id="dialog-ver" aria-labelledby="ver-titulo">
        <span class="ver-asa" aria-hidden="true"></span>
        <div class="ver-cabecera">
            <div class="ver-titulos">
                <strong id="ver-titulo"></strong>
                <span class="ver-subtitulo" id="ver-fecha"></span>
            </div>
            <span class="badge" id="ver-badge"></span>
            <button type="button" class="ver-cerrar" id="ver-cerrar" aria-label="Cerrar"><svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.4" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><line x1="18" y1="6" x2="6" y2="18"></line><line x1="6" y1="6" x2="18" y2="18"></line></svg></button>
        </div>

        <button type="button" class="ver-foto" id="ver-foto" aria-label="Ampliar foto">
            <span class="ver-cargando" aria-hidden="true"></span>
            <img id="ver-img" alt="Evidencia de la proforma">
            <span class="ver-ampliar" aria-hidden="true">
                <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.2" stroke-linecap="round" stroke-linejoin="round"><circle cx="11" cy="11" r="8"></circle><line x1="21" y1="21" x2="16.65" y2="16.65"></line><line x1="11" y1="8" x2="11" y2="14"></line><line x1="8" y1="11" x2="14" y2="11"></line></svg>
                Toca para ampliar
            </span>
        </button>
        <div class="ver-sin-foto" id="ver-sin-foto" hidden>
            <svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><rect x="3" y="3" width="18" height="18" rx="2"></rect><circle cx="8.5" cy="8.5" r="1.5"></circle><path d="m21 15-5-5L5 21"></path><line x1="3" y1="3" x2="21" y2="21"></line></svg>
            <span id="ver-sin-foto-texto"></span>
        </div>

        <div class="alert-warning" id="ver-aviso" hidden>
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M10.29 3.86 1.82 18a2 2 0 0 0 1.71 3h16.94a2 2 0 0 0 1.71-3L13.71 3.86a2 2 0 0 0-3.42 0z"></path><line x1="12" y1="9" x2="12" y2="13"></line><line x1="12" y1="17" x2="12.01" y2="17"></line></svg>
            <span id="ver-aviso-texto"></span>
        </div>

        <div class="ver-resumen">
            <div class="ver-monto">
                <span class="ver-dato-label" id="ver-monto-label">Monto propuesto</span>
                <strong id="ver-monto"></strong>
            </div>
        </div>

        <div class="ver-detalle">
            <span class="ver-dato-label">Detalle de la proforma</span>
            <p id="ver-detalle"></p>
        </div>

        <button type="button" class="btn btn-ghost btn-block" id="ver-cerrar-pie">Cerrar</button>
    </dialog>

    <!-- Foto a pantalla completa, fondo negro y cerrar arriba a la derecha (dialog_proforma_zoom_foto del app). -->
    <dialog class="dialog-zoom" id="dialog-zoom">
        <img id="zoom-img" alt="Evidencia ampliada">
        <button type="button" class="zoom-cerrar" id="zoom-cerrar" aria-label="Cerrar"><svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.4" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><line x1="18" y1="6" x2="6" y2="18"></line><line x1="6" y1="6" x2="18" y2="18"></line></svg></button>
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

            <div class="ag-body" data-campo="detalle" hidden>
                <section>
                    <h3 class="ag-titulo">Proformas enviadas</h3>
                    <div data-campo="rondas"></div>
                    <p class="ag-vacio" data-campo="sin-rondas" hidden>Aún no envías ninguna proforma.</p>
                </section>

                <div class="alert-warning" data-campo="alerta-correccion" hidden>
                    <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M10.29 3.86 1.82 18a2 2 0 0 0 1.71 3h16.94a2 2 0 0 0 1.71-3L13.71 3.86a2 2 0 0 0-3.42 0z"></path><line x1="12" y1="9" x2="12" y2="13"></line><line x1="12" y1="17" x2="12.01" y2="17"></line></svg>
                    <span data-campo="alerta-texto"></span>
                </div>

                <div class="ag-acciones" data-campo="acciones">
                    <button type="button" class="ag-accion ag-accion-visita" data-campo="btn-nueva-ronda">
                        <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M23 19a2 2 0 0 1-2 2H3a2 2 0 0 1-2-2V8a2 2 0 0 1 2-2h4l2-3h6l2 3h4a2 2 0 0 1 2 2z"></path><circle cx="12" cy="13" r="4"></circle></svg>
                        Registrar proforma
                    </button>
                    <button type="button" class="ag-accion ag-accion-factura" data-campo="btn-enviar-factura">
                        <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M4 3h16v17l-3-2-2 2-2-2-2 2-2-2-2 2-3-2z"></path><path d="M8 8h8"></path><path d="M8 12h8"></path></svg>
                        Enviar factura
                    </button>
                </div>
                <p class="field-hint" data-campo="hint-factura" hidden>Envía una proforma para poder enviar la factura.</p>

                <form class="ag-form" data-form="nueva-ronda" novalidate hidden>
                    <h3 class="ag-titulo">Registrar proforma</h3>
                    <div class="ag-campo">
                        <span class="ag-campo-label">Evidencia fotográfica (opcional)</span>
                        <div class="photo-field" data-campo="foto">
                            <input type="file" accept="image/*" capture="environment" data-origen="camara" hidden>
                            <input type="file" accept="image/*" data-origen="galeria" hidden>
                            <span class="photo-dropzone">
                                <span class="photo-dropzone-titulo">Agrega la foto</span>
<span class="photo-acciones"><button type="button" class="photo-btn" data-foto="camara"><svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><path d="M23 19a2 2 0 0 1-2 2H3a2 2 0 0 1-2-2V8a2 2 0 0 1 2-2h4l2-3h6l2 3h4a2 2 0 0 1 2 2z"></path><circle cx="12" cy="13" r="4"></circle></svg> Cámara</button>
<button type="button" class="photo-btn" data-foto="galeria"><svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><rect x="3" y="3" width="18" height="18" rx="2"></rect><circle cx="8.5" cy="8.5" r="1.5"></circle><path d="m21 15-5-5L5 21"></path></svg> Galería</button></span>
                            </span>
                            <span class="photo-preview-wrap">
                                <img class="photo-preview" alt="Vista previa de la evidencia">
                                <span class="photo-cambiar"><span class="photo-cambiar-titulo">Cambiar foto</span><button type="button" class="photo-btn" data-foto="camara"><svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><path d="M23 19a2 2 0 0 1-2 2H3a2 2 0 0 1-2-2V8a2 2 0 0 1 2-2h4l2-3h6l2 3h4a2 2 0 0 1 2 2z"></path><circle cx="12" cy="13" r="4"></circle></svg> Cámara</button>
<button type="button" class="photo-btn" data-foto="galeria"><svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><rect x="3" y="3" width="18" height="18" rx="2"></rect><circle cx="8.5" cy="8.5" r="1.5"></circle><path d="m21 15-5-5L5 21"></path></svg> Galería</button></span>
                            </span>
                        </div>
                        <p class="field-error" data-error="foto" hidden></p>
                    </div>
                    <div class="field">
                        <label>Monto propuesto <span class="req" aria-hidden="true">*</span></label>
                        <div class="fac-monto">
                            <span aria-hidden="true">$</span>
                            <input type="text" name="monto_total_factura" data-monto inputmode="decimal" autocomplete="off" maxlength="8" placeholder="0.00">
                        </div>
                        <p class="field-error" data-error="monto" hidden></p>
                    </div>
                    <div class="field">
                        <label>Detalle de la proforma <span class="opcional">(opcional)</span></label>
                        <textarea name="caracteristica_visita" rows="2" placeholder="Escribe aquí…"></textarea>
                    </div>
                    <div class="alert alert-error" data-campo="error-form" style="display:none"></div>
                    <div class="ag-form-botones">
                        <button type="button" class="btn btn-ghost" data-campo="cancelar-ronda">Cancelar</button>
                        <button type="submit" class="btn btn-primary">Guardar proforma</button>
                    </div>
                </form>

                

                <div class="ag-pie">
                    <button type="button" class="btn-link" data-accion="solicitar-cierre">Solicitar cierre</button>
                    <button type="button" class="btn btn-secondary btn-block" data-accion="reactivar" hidden>Reactivar negociación</button>
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
            <button type="button" class="registro-ver" data-campo="ver">Ver</button>
        </div>
    </template>

    <script src="<?= asset('../assets/js/api.js') ?>"></script>
    <script src="<?= asset('../assets/js/camera-upload.js') ?>"></script>
    <script src="<?= asset('../assets/js/factura-envio.js') ?>"></script>
    <script src="<?= asset('../assets/js/proforma.js') ?>"></script>
    <script src="<?= asset('../assets/js/recordatorio-visita.js') ?>"></script>
</body>
</html>
