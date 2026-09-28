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
<title>Contacto — Proyectos y Obras</title>
<link rel="stylesheet" href="../assets/css/base.css">
<link rel="stylesheet" href="../assets/css/components.css">
<link rel="stylesheet" href="../assets/css/layout.css">
</head>
<body>
    <?php include __DIR__ . '/_header.php'; ?>

    <main class="app-main">
        <div class="page-intro">
            <h1>Nueva visita</h1>
            <p>Registra los datos del punto de venta y programa la visita técnica.</p>
        </div>

        <div id="alerta" class="alert alert-error" style="display:none"></div>
        <div id="alerta-ok" class="alert alert-success" style="display:none"></div>

        <form id="form-contacto">
            <p class="section-label">Punto de venta</p>
            <div class="card">
                <div class="card-pad">
                    <div class="field">
                        <label for="pdv">Punto de venta (PDV)</label>
                        <div class="field-icon-wrap">
                            <span class="field-input-icon">
                                <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M3 21h18"></path><path d="M5 21V7l8-4v18"></path><path d="M19 21V11l-6-4"></path></svg>
                            </span>
                            <input type="text" id="pdv" name="pdv" list="lista-pdvs" autocomplete="off" placeholder="Escribe para buscar…" required>
                        </div>
                        <datalist id="lista-pdvs"></datalist>
                        <input type="hidden" id="codigo_pdv" name="codigo_pdv">
                        <input type="hidden" id="ciudad_pdv" name="ciudad_pdv">
                        <span class="field-hint">Busca por nombre o ciudad en la lista existente.</span>
                    </div>
                </div>
            </div>

            <p class="section-label">Datos de contacto</p>
            <div class="card">
                <div class="card-pad field-grid-2">
                    <div class="field">
                        <label for="contacto">Nombre de contacto</label>
                        <input type="text" id="contacto" name="contacto" placeholder="Ej. María Torres" required>
                        <span class="field-hint">Solo letras y espacios, mínimo 2 letras.</span>
                    </div>

                    <div class="field">
                        <label for="empresa">Empresa</label>
                        <input type="text" id="empresa" name="empresa" placeholder="Ej. Ferretería Sur" required>
                        <span class="field-hint">Letras, números y . - &amp; '</span>
                    </div>

                    <div class="field field-span-2">
                        <label for="mail">Correo</label>
                        <input type="email" id="mail" name="mail" placeholder="correo@ejemplo.com" required>
                        <span class="field-hint">Se usará para confirmaciones de visita.</span>
                    </div>

                    <div class="field field-span-2">
                        <label for="direccion">Dirección</label>
                        <textarea id="direccion" name="direccion" rows="2" placeholder="Calle, número, referencia…" required></textarea>
                        <span class="field-hint">Dirección legible; no se acepta un Plus Code.</span>
                    </div>

                    <div class="field-pair field-span-2">
                        <div class="field">
                            <label for="telefono">Teléfono</label>
                            <input type="tel" id="telefono" name="telefono" inputmode="numeric" maxlength="10" placeholder="10 dígitos" required>
                            <span class="field-hint">Exactamente 10 dígitos.</span>
                        </div>
                        <div class="field">
                            <label for="telefono_convencional">Convencional</label>
                            <input type="tel" id="telefono_convencional" name="telefono_convencional" inputmode="numeric" placeholder="Opcional">
                            <span class="field-hint">Opcional, solo dígitos.</span>
                        </div>
                    </div>
                </div>
            </div>

            <p class="section-label">Visita</p>
            <div class="card">
                <div class="card-pad">
                    <div class="checkbox-field">
                        <input type="checkbox" id="no_requiere_visita" name="no_requiere_visita">
                        <label for="no_requiere_visita" style="margin:0">No requiere visita (pasa directo a Proforma)</label>
                    </div>

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
        </form>
    </main>

    <?php include __DIR__ . '/_nav.php'; ?>

    <script src="../assets/js/api.js"></script>
    <script src="../assets/js/contacto.js"></script>
</body>
</html>
