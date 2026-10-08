<?php
require_once __DIR__ . '/../includes/auth_guard.php';
$usuario = exigir_sesion();
$tabActiva = 'contacto';
$tituloPagina = 'Nueva visita';
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
        <div id="alerta" class="alert alert-error" style="display:none" role="alert"></div>

        <form id="form-contacto" class="contacto-grid" novalidate>
            <div class="contacto-col">
                <div class="card">
                    <div class="card-pad">
                        <div class="field">
                            <label for="pdv-combo-trigger">Punto de venta</label>
                            <!-- El <select> oculto es la fuente de verdad (value, change); el combo con buscador lo envuelve. -->
                            <div class="pdv-combo" id="pdv-combo-wrap">
                                <select id="pdv-select" name="pdv_select" class="pdv-combo-select-oculto" tabindex="-1" aria-hidden="true">
                                    <option value="">Seleccionar PDV</option>
                                </select>
                                <button type="button" class="pdv-combo-trigger" id="pdv-combo-trigger" aria-haspopup="listbox" aria-expanded="false">
                                    <span class="pdv-combo-texto">Cargando…</span>
                                    <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round" class="pdv-combo-chevron" aria-hidden="true"><polyline points="6 9 12 15 18 9"></polyline></svg>
                                </button>
                                <div class="pdv-combo-panel" id="pdv-combo-panel" role="listbox" hidden>
                                    <div class="pdv-combo-buscador-wrap">
                                        <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><circle cx="11" cy="11" r="8"></circle><line x1="21" y1="21" x2="16.65" y2="16.65"></line></svg>
                                        <input type="text" class="pdv-combo-buscador" id="pdv-combo-buscador" placeholder="Buscar…" autocomplete="off" aria-label="Buscar punto de venta">
                                        <button type="button" class="pdv-combo-cerrar" id="pdv-combo-cerrar" aria-label="Cerrar">
                                            <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><line x1="18" y1="6" x2="6" y2="18"></line><line x1="6" y1="6" x2="18" y2="18"></line></svg>
                                        </button>
                                    </div>
                                    <div class="pdv-combo-lista" id="pdv-combo-lista"></div>
                                </div>
                            </div>
                            <input type="hidden" id="codigo_pdv" name="codigo_pdv">
                            <input type="hidden" id="ciudad_pdv" name="ciudad_pdv">
                            <span class="field-error" data-error-for="pdv" hidden></span>
                        </div>

                        <div class="field">
                            <label for="contacto">Contacto</label>
                            <input type="text" id="contacto" name="contacto" placeholder="Nombre y apellido" autocomplete="name" autocapitalize="words" required>
                            <span class="field-error" data-error-for="contacto" hidden></span>
                        </div>

                        <div class="field">
                            <label for="empresa">Empresa</label>
                            <input type="text" id="empresa" name="empresa" placeholder="Nombre de la empresa" autocomplete="organization" autocapitalize="words" required>
                            <span class="field-error" data-error-for="empresa" hidden></span>
                        </div>

                        <div class="field-pair">
                            <div class="field">
                                <label for="telefono">Teléfono</label>
                                <input type="tel" id="telefono" name="telefono" inputmode="numeric" pattern="[0-9]*" maxlength="10" placeholder="10 dígitos" autocomplete="tel" required>
                                <span class="field-error" data-error-for="telefono" hidden></span>
                            </div>
                            <div class="field">
                                <label for="telefono_convencional">Convencional</label>
                                <input type="tel" id="telefono_convencional" name="telefono_convencional" inputmode="numeric" pattern="[0-9]*" placeholder="Opcional" autocomplete="tel">
                                <span class="field-error" data-error-for="telefono_convencional" hidden></span>
                            </div>
                        </div>

                        <div class="field">
                            <label for="mail">Correo</label>
                            <input type="email" id="mail" name="mail" placeholder="correo@ejemplo.com" autocomplete="email" autocapitalize="none" spellcheck="false" required>
                            <span class="field-error" data-error-for="mail" hidden></span>
                        </div>

                        <div class="field">
                            <label for="direccion">Dirección</label>
                            <textarea id="direccion" name="direccion" rows="2" placeholder="Calle, número, referencia" autocomplete="street-address" autocapitalize="sentences" required></textarea>
                            <span class="field-error" data-error-for="direccion" hidden></span>
                            <span class="field-hint gps-estado" id="gps-estado">
                                <span id="gps-texto">Ubicación…</span>
                                <button type="button" class="btn-link" id="gps-reintentar" hidden>Reintentar</button>
                            </span>
                        </div>
                    </div>
                </div>
            </div>

            <div class="contacto-col">
                <div class="card">
                    <div class="card-pad">
                        <label class="toggle-card" for="no_requiere_visita">
                            <input type="checkbox" id="no_requiere_visita" name="no_requiere_visita" class="toggle-card-input">
                            <div class="toggle-card-body">
                                <div class="toggle-card-text">
                                    <span class="toggle-card-title">Sin visita técnica</span>
                                    <span class="toggle-card-sub">Pasa directo a proforma</span>
                                </div>
                                <div class="toggle-switch" aria-hidden="true">
                                    <span class="toggle-knob"></span>
                                </div>
                            </div>
                        </label>

                        <div id="campos-agenda">
                            <div class="field-pair">
                                <div class="field">
                                    <label for="fecha_agendamiento">Fecha</label>
                                    <input type="date" id="fecha_agendamiento" name="fecha_agendamiento">
                                    <span class="field-error" data-error-for="fecha" hidden></span>
                                </div>
                                <div class="field">
                                    <label for="hora">Hora</label>
                                    <input type="time" id="hora" name="hora">
                                    <span class="field-error" data-error-for="hora" hidden></span>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="form-actions">
                    <button type="reset" class="btn btn-outline">Cancelar</button>
                    <button type="submit" class="btn btn-primary btn-block" id="btn-guardar">Guardar visita</button>
                </div>
            </div>
        </form>
    </main>

    <dialog class="dialog-cerrar" id="dialog-guardado">
        <div class="dialog-cerrar-header">
            <span class="dialog-cerrar-icon dialog-cerrar-icon-ok">
                <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><polyline points="20 6 9 17 4 12"></polyline></svg>
            </span>
            <strong>Visita registrada</strong>
        </div>
        <p id="dialog-guardado-texto" style="margin:0 0 14px;font-size:13px;color:var(--color-text-muted);line-height:1.5;"></p>
        <div class="form-actions">
            <a href="agenda.php" class="btn btn-primary" id="btn-ir-agenda" style="text-decoration:none;">Ir a la agenda</a>
        </div>
    </dialog>

    <?php include __DIR__ . '/_nav.php'; ?>

    <script src="<?= asset('../assets/js/api.js') ?>"></script>
    <script src="<?= asset('../assets/js/contacto.js') ?>"></script>
</body>
</html>
