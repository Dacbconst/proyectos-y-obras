<?php
require_once __DIR__ . '/../includes/auth_guard.php';
$usuario = exigir_sesion();
$tabActiva = 'agenda';
$tituloPagina = 'Agenda';
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
<link rel="stylesheet" href="<?= asset('../assets/css/agenda.css') ?>">
</head>
<body class="agenda-page">
    <?php include __DIR__ . '/_header.php'; ?>

    <main class="app-main app-main-wide agenda-main-container">
        <div id="alerta" class="alert alert-error" style="display:none; margin:14px 18px 0"></div>

        <div class="agenda-layout">
            <!-- Calendario: en móvil queda fijo y compacto (una semana), se despliega al mes completo -->
            <aside class="agenda-calendar-panel">
                <div class="agenda-month-nav">
                    <button type="button" id="btn-mes-anterior" class="agenda-month-btn" aria-label="Semana anterior">
                        <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.4" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><polyline points="15 18 9 12 15 6"></polyline></svg>
                    </button>
                    <button type="button" id="tv-mes-actual" class="agenda-month-title" aria-expanded="false" title="Cambiar mes">
                        <span id="tv-mes-texto"></span>
                        <svg class="agenda-month-chevron" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.6" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><polyline points="6 9 12 15 18 9"></polyline></svg>
                    </button>
                    <button type="button" id="btn-mes-siguiente" class="agenda-month-btn" aria-label="Semana siguiente">
                        <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.4" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><polyline points="9 18 15 12 9 6"></polyline></svg>
                    </button>
                </div>

                <div class="agenda-pills">
                    <button type="button" id="pill-pendientes" class="agenda-pill active" data-filtro="pendientes">Agenda (0)</button>
                    <button type="button" id="pill-visitados" class="agenda-pill" data-filtro="visitados">Visitados (0)</button>
                </div>

                <div class="agenda-weekdays">
                    <span class="agenda-weekday">L</span>
                    <span class="agenda-weekday">M</span>
                    <span class="agenda-weekday">M</span>
                    <span class="agenda-weekday">J</span>
                    <span class="agenda-weekday">V</span>
                    <span class="agenda-weekday">S</span>
                    <span class="agenda-weekday">D</span>
                </div>

                <div id="agenda-calendar-grid" class="agenda-calendar-grid"></div>

                <button type="button" id="agenda-handle" class="agenda-handle" aria-label="Contraer calendario"><span></span></button>
            </aside>

            <!-- Panel de eventos agrupados: rvAgenda -->
            <section class="agenda-events-panel">
                <div class="agenda-events-header">
                    <h2 id="agenda-section-title">Visitas del mes</h2>
                    <span id="agenda-events-count" class="agenda-events-count">0 visitas</span>
                </div>

                <div id="lista-agenda"></div>

                <!-- Estado vacío sin card: centrado, elegante y en gris oscuro -->
                <div id="vacio" class="agenda-empty-clean" style="display:none">
                    <div class="agenda-empty-icon" aria-hidden="true">
                        <svg width="30" height="30" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round">
                            <rect x="3" y="4" width="18" height="18" rx="2" ry="2"></rect>
                            <line x1="16" y1="2" x2="16" y2="6"></line>
                            <line x1="8" y1="2" x2="8" y2="6"></line>
                            <line x1="3" y1="10" x2="21" y2="10"></line>
                        </svg>
                    </div>
                    <div class="agenda-empty-content">
                        <h3 id="vacio-titulo" class="agenda-empty-title">Sin visitas agendadas</h3>
                        <p id="vacio-sub" class="agenda-empty-sub">No hay actividades programadas para este mes.</p>
                    </div>
                </div>
            </section>
        </div>
    </main>

    <!-- Ventanita 1: Selector de Mes (mostrarSelectorMes de Android) -->
    <div id="modal-selector-mes" class="agenda-modal-overlay" role="dialog" aria-modal="true" aria-labelledby="selector-mes-titulo">
        <div class="agenda-modal-dialog" style="max-width: 360px; padding: 18px">
            <div style="display:flex; justify-content:space-between; align-items:center; margin-bottom:14px">
                <button type="button" id="btn-selector-anio-ant" class="agenda-month-btn" aria-label="Año anterior">
                    <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5"><polyline points="15 18 9 12 15 6"></polyline></svg>
                </button>
                <h3 id="selector-mes-titulo" style="font-size:17px; font-weight:700; color:var(--color-header-bg); margin:0">2026</h3>
                <button type="button" id="btn-selector-anio-sig" class="agenda-month-btn" aria-label="Año siguiente">
                    <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5"><polyline points="9 18 15 12 9 6"></polyline></svg>
                </button>
            </div>
            <div id="grid-selector-meses" class="grid-meses"></div>
            <div style="text-align:right; margin-top:16px">
                <button type="button" id="btn-cerrar-selector-mes" class="btn btn-secondary btn-sm">Cerrar</button>
            </div>
        </div>
    </div>

    <!-- Ventanita 2: Modal Detalle de Visita (dialog_proforma_detalle_visita.xml) -->
    <div id="modal-detalle-visita" class="agenda-modal-overlay" role="dialog" aria-modal="true" aria-labelledby="modal-titulo">
        <div class="agenda-modal-dialog">
            <div class="modal-header-row">
                <div>
                    <h3 id="modal-titulo" class="modal-title">Visita Técnica</h3>
                    <div id="modal-registrado" class="modal-registrado"></div>
                </div>
                <button type="button" id="modal-btn-cerrar" class="modal-close-btn" aria-label="Cerrar">
                    <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round">
                        <line x1="18" y1="6" x2="6" y2="18"></line>
                        <line x1="6" y1="6" x2="18" y2="18"></line>
                    </svg>
                </button>
            </div>

            <div class="modal-status-row">
                <span id="modal-badge-estado" class="badge"></span>
                <div class="modal-actions-right">
                    <label class="switch-edicion" title="Activar modo edición">
                        <input type="checkbox" id="switch-modo-edicion">
                        <span class="switch-edicion-slider"></span>
                        <span class="switch-edicion-label">Editar</span>
                    </label>
                    <button type="button" id="btn-eliminar-visita" class="modal-trash-btn" title="Eliminar visita">
                        <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                            <polyline points="3 6 5 6 21 6"></polyline>
                            <path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"></path>
                            <line x1="10" y1="11" x2="10" y2="17"></line>
                            <line x1="14" y1="11" x2="14" y2="17"></line>
                        </svg>
                    </button>
                </div>
            </div>

            <!-- Bloque no editable: fecha + hora -->
            <div id="modal-bloque-datetime" class="modal-datetime-box">
                <div class="modal-datetime-col">
                    <div class="modal-label-small">Fecha Agendada</div>
                    <div id="modal-fecha-valor" class="modal-value-bold">—</div>
                </div>
                <div class="modal-datetime-col">
                    <div class="modal-label-small">Hora</div>
                    <div id="modal-hora-valor" class="modal-value-bold">—</div>
                </div>
            </div>

            <!-- Técnico (no editable) -->
            <div style="margin-top: 14px">
                <div class="modal-label-small">Técnico</div>
                <div id="modal-tecnico-valor" style="font-size:13px; font-weight:600; color:var(--color-text); margin-top:2px"><?php echo htmlspecialchars($usuario); ?></div>
            </div>

            <!-- Modo Lectura con Enlaces Interactivos -->
            <div id="modal-info-lectura">
                <div style="margin-top: 12px">
                    <div class="modal-label-small">Contacto</div>
                    <div id="modal-contacto-valor" style="font-size:13px; font-weight:600; color:var(--color-text); margin-top:2px">—</div>
                </div>

                <div style="margin-top: 12px">
                    <div class="modal-label-small">Empresa</div>
                    <div id="modal-empresa-valor" style="font-size:13px; font-weight:600; color:var(--color-text); margin-top:2px">—</div>
                </div>

                <a id="modal-link-telefono" href="#" class="modal-item-link">
                    <div class="modal-icon-circle">
                        <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                            <path d="M22 16.92v3a2 2 0 0 1-2.18 2 19.79 19.79 0 0 1-8.63-3.07 19.5 19.5 0 0 1-6-6 19.79 19.79 0 0 1-3.07-8.67A2 2 0 0 1 4.11 2h3a2 2 0 0 1 2 1.72 12.84 12.84 0 0 0 .7 2.81 2 2 0 0 1-.45 2.11L8.09 9.91a16 16 0 0 0 6 6l1.27-1.27a2 2 0 0 1 2.11-.45 12.84 12.84 0 0 0 2.81.7A2 2 0 0 1 22 16.92z"></path>
                        </svg>
                    </div>
                    <div class="modal-item-texts">
                        <div class="modal-item-label">Teléfono (Llamar / WhatsApp)</div>
                        <div id="modal-telefono-valor" class="modal-item-val">—</div>
                    </div>
                </a>

                <a id="modal-link-correo" href="#" class="modal-item-link">
                    <div class="modal-icon-circle">
                        <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                            <path d="M4 4h16c1.1 0 2 .9 2 2v12c0 1.1-.9 2-2 2H4c-1.1 0-2-.9-2-2V6c0-1.1.9-2 2-2z"></path>
                            <polyline points="22,6 12,13 2,6"></polyline>
                        </svg>
                    </div>
                    <div class="modal-item-texts">
                        <div class="modal-item-label">Correo Electrónico</div>
                        <div id="modal-correo-valor" class="modal-item-val">—</div>
                    </div>
                </a>

                <a id="modal-link-direccion" href="#" target="_blank" rel="noopener noreferrer" class="modal-item-link">
                    <div class="modal-icon-circle">
                        <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                            <path d="M21 10c0 7-9 13-9 13s-9-6-9-13a9 9 0 0 1 18 0z"></path>
                            <circle cx="12" cy="10" r="3"></circle>
                        </svg>
                    </div>
                    <div class="modal-item-texts">
                        <div class="modal-item-label">Dirección (Abrir mapa)</div>
                        <div id="modal-direccion-valor" class="modal-item-val">—</div>
                    </div>
                </a>

                <div style="display:flex; justify-content:space-between; align-items:center; margin-top:14px; padding-top:10px; border-top:1px solid var(--color-border)">
                    <span class="modal-label-small">PDV / Punto de Venta</span>
                    <strong id="modal-pdv-valor" style="font-size:12.5px; color:var(--color-text)">—</strong>
                </div>
            </div>

            <!-- Modo Edición Inline -->
            <form id="modal-form-editar" class="modal-edit-section">
                <div class="field"><label>Contacto</label><input name="contacto" id="edit-contacto"></div>
                <div class="field"><label>Empresa</label><input name="empresa" id="edit-empresa"></div>
                <div class="field"><label>Teléfono</label><input name="telefono" id="edit-telefono" type="tel"></div>
                <div class="field"><label>Correo</label><input name="mail" id="edit-mail" type="email"></div>
                <div class="field"><label>Dirección</label><textarea name="direccion" id="edit-direccion" rows="2"></textarea></div>
                <div class="form-actions" style="margin-top:16px">
                    <button type="submit" id="btn-guardar-cambios-detalle" class="btn btn-primary btn-sm btn-block">Guardar cambios</button>
                </div>
            </form>

            <!-- Bloque de reagendamiento -->
            <div class="modal-reagendar-box">
                <div style="display:flex; justify-content:space-between; align-items:center">
                    <strong style="font-size:13px; color:var(--color-accent-strong)">Reagendar esta visita</strong>
                    <button type="button" id="btn-toggle-reagendar" class="btn btn-sm" style="background:transparent; color:var(--color-accent); font-weight:700">Cambiar fecha</button>
                </div>
                <form id="modal-form-reagendar" style="display:none; margin-top:12px">
                    <div class="field-pair">
                        <div class="field">
                            <label>Nueva fecha</label>
                            <input name="fecha_agendamiento" id="reagendar-fecha" type="date" required>
                        </div>
                        <div class="field">
                            <label>Nueva hora</label>
                            <input name="hora" id="reagendar-hora" type="time" required>
                        </div>
                    </div>
                    <button type="submit" class="btn btn-primary btn-sm btn-block" style="margin-top:10px">Confirmar reagendamiento</button>
                </form>
            </div>
        </div>
    </div>

    <!-- Ventanita 3: Confirmación de Eliminación -->
    <div id="modal-confirmar-eliminar" class="agenda-modal-overlay" role="dialog" aria-modal="true">
        <div class="agenda-modal-dialog" style="max-width: 380px; padding: 20px">
            <h3 style="font-size: 16px; font-weight: 700; color: var(--color-text); margin: 0 0 8px 0">¿Eliminar agendamiento?</h3>
            <p style="font-size: 13px; color: var(--color-text-muted); margin: 0 0 20px 0; line-height: 1.4">¿Estás seguro de que deseas eliminar este agendamiento de visita?</p>
            <div style="display: flex; gap: 10px; justify-content: flex-end">
                <button type="button" id="btn-cancelar-eliminar" class="btn btn-secondary btn-sm">Cancelar</button>
                <button type="button" id="btn-confirmar-eliminar-accion" class="btn btn-danger btn-sm">Eliminar</button>
            </div>
        </div>
    </div>

    <!-- Ventanita 4: Confirmación de Descartar Cambios -->
    <div id="modal-confirmar-descartar" class="agenda-modal-overlay" role="dialog" aria-modal="true">
        <div class="agenda-modal-dialog" style="max-width: 380px; padding: 20px">
            <h3 style="font-size: 16px; font-weight: 700; color: var(--color-text); margin: 0 0 8px 0">Cambios sin guardar</h3>
            <p style="font-size: 13px; color: var(--color-text-muted); margin: 0 0 20px 0; line-height: 1.4">Tienes cambios sin guardar. ¿Deseas salir y descartar los cambios?</p>
            <div style="display: flex; gap: 10px; justify-content: flex-end">
                <button type="button" id="btn-quedarse-editar" class="btn btn-secondary btn-sm">Quedarse</button>
                <button type="button" id="btn-salir-descartar" class="btn btn-danger btn-sm">Salir</button>
            </div>
        </div>
    </div>

    <?php include __DIR__ . '/_nav.php'; ?>

    <script src="<?= asset('../assets/js/api.js') ?>"></script>
    <script src="<?= asset('../assets/js/agenda.js') ?>"></script>
</body>
</html>
