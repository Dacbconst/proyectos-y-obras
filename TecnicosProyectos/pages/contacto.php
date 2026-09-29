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
<link rel="stylesheet" href="../assets/css/base.css">
<link rel="stylesheet" href="../assets/css/components.css">
<link rel="stylesheet" href="../assets/css/layout.css">
</head>
<body>
    <?php include __DIR__ . '/_header.php'; ?>

    <main class="app-main app-main-wide">
        <div class="page-intro">
            <h1>Nueva visita</h1>
            <p>Registra los datos del punto de venta y programa la visita técnica.</p>
        </div>

        <div id="alerta" class="alert alert-error" style="display:none"></div>
        <div id="alerta-ok" class="alert alert-success" style="display:none"></div>

        <form id="form-contacto" class="contacto-grid">
            <div class="contacto-col">
                <p class="section-label">Punto de venta</p>
                <div class="card">
                    <div class="card-pad">
                        <div class="field">
                            <label for="pdv-select">Punto de venta (PDV) *</label>
                            <!-- El <select> oculto es la fuente de verdad (value, change) —
                                 el combo con buscador lo envuelve sin tocarlo. -->
                            <div class="pdv-combo" id="pdv-combo-wrap">
                                <select id="pdv-select" name="pdv_select" class="pdv-combo-select-oculto" required>
                                    <option value="">Seleccione un PDV</option>
                                </select>
                                <button type="button" class="pdv-combo-trigger" id="pdv-combo-trigger" aria-haspopup="listbox" aria-expanded="false">
                                    <span class="pdv-combo-texto">Cargando PDVs…</span>
                                    <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round" class="pdv-combo-chevron"><polyline points="6 9 12 15 18 9"></polyline></svg>
                                </button>
                                <div class="pdv-combo-panel" id="pdv-combo-panel" role="listbox" hidden>
                                    <div class="pdv-combo-buscador-wrap">
                                        <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="11" cy="11" r="8"></circle><line x1="21" y1="21" x2="16.65" y2="16.65"></line></svg>
                                        <input type="text" class="pdv-combo-buscador" id="pdv-combo-buscador" placeholder="Buscar PDV por nombre o ciudad…" autocomplete="off">
                                    </div>
                                    <div class="pdv-combo-lista" id="pdv-combo-lista"></div>
                                </div>
                            </div>
                            <input type="hidden" id="codigo_pdv" name="codigo_pdv">
                            <input type="hidden" id="ciudad_pdv" name="ciudad_pdv">
                            <span class="field-hint">Busca por nombre o ciudad en la lista de PDVs asignados.</span>
                        </div>
                    </div>
                </div>

                <p class="section-label">Datos de contacto</p>
                <div class="card">
                    <div class="card-pad">
                        <div class="field">
                            <label for="contacto">Nombre de contacto</label>
                            <input type="text" id="contacto" name="contacto" placeholder="Ej. María Torres" autocomplete="name" autocapitalize="words" required>
                            <span class="field-hint">Solo letras y espacios, mínimo 2 letras.</span>
                        </div>

                        <div class="field">
                            <label for="empresa">Empresa</label>
                            <input type="text" id="empresa" name="empresa" placeholder="Ej. Ferretería Sur" autocomplete="organization" autocapitalize="words" required>
                            <span class="field-hint">Letras, números y . - &amp; '</span>
                        </div>

                        <div class="field-pair">
                            <div class="field">
                                <label for="telefono">Teléfono</label>
                                <input type="tel" id="telefono" name="telefono" inputmode="numeric" pattern="[0-9]*" maxlength="10" placeholder="10 dígitos" autocomplete="tel" required>
                                <span class="field-hint">Exactamente 10 dígitos.</span>
                            </div>
                            <div class="field">
                                <label for="telefono_convencional">Convencional</label>
                                <input type="tel" id="telefono_convencional" name="telefono_convencional" inputmode="numeric" pattern="[0-9]*" placeholder="Opcional" autocomplete="tel">
                                <span class="field-hint">Opcional, solo dígitos.</span>
                            </div>
                        </div>

                        <div class="field">
                            <label for="mail">Correo</label>
                            <input type="email" id="mail" name="mail" placeholder="correo@ejemplo.com" autocomplete="email" autocapitalize="none" spellcheck="false" required>
                            <span class="field-hint">Se usará para confirmaciones de visita.</span>
                        </div>

                        <div class="field">
                            <label for="direccion">Dirección</label>
                            <textarea id="direccion" name="direccion" rows="2" placeholder="Calle, número, referencia…" autocomplete="street-address" autocapitalize="sentences" required></textarea>
                            <span class="field-hint">Dirección legible; no se acepta un Plus Code.</span>
                        </div>
                    </div>
                </div>
            </div>

            <div class="contacto-col">
                <p class="section-label">Visita técnica</p>
                <div class="card">
                    <div class="card-pad">
                        <label class="toggle-card" for="no_requiere_visita">
                            <input type="checkbox" id="no_requiere_visita" name="no_requiere_visita" class="toggle-card-input">
                            <div class="toggle-card-body">
                                <div class="toggle-card-text">
                                    <span class="toggle-card-title">No requiere visita técnica</span>
                                    <span class="toggle-card-sub">Pasa directo a cotización / proforma</span>
                                </div>
                                <div class="toggle-switch" aria-hidden="true">
                                    <span class="toggle-knob"></span>
                                </div>
                            </div>
                        </label>

                        <div id="campos-agenda">
                            <div class="field-pair">
                                <div class="field">
                                    <label for="fecha_agendamiento">Fecha de visita</label>
                                    <input type="date" id="fecha_agendamiento" name="fecha_agendamiento">
                                </div>
                                <div class="field">
                                    <label for="hora">Hora</label>
                                    <input type="time" id="hora" name="hora">
                                </div>
                            </div>
                            <span class="field-hint">Puedes agendar la fecha más adelante si aún no la tienes.</span>
                        </div>
                    </div>
                </div>

                <div class="form-actions">
                    <button type="reset" class="btn btn-outline">Cancelar</button>
                    <button type="submit" class="btn btn-primary btn-block">Guardar visita</button>
                </div>
            </div>
        </form>
    </main>

    <?php include __DIR__ . '/_nav.php'; ?>

    <script src="../assets/js/api.js"></script>
    <script src="../assets/js/contacto.js"></script>
</body>
</html>
