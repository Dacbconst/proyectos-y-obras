<?php
require_once __DIR__ . '/../includes/auth_guard.php';
$usuario = exigir_sesion();
$tabActiva = 'contacto';
?>
<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1, viewport-fit=cover, interactive-widget=resizes-content">
<meta name="theme-color" content="#2E1A54">
<title>Contacto — Proyectos y Obras</title>
<link rel="stylesheet" href="../assets/css/base.css">
<link rel="stylesheet" href="../assets/css/components.css">
<link rel="stylesheet" href="../assets/css/layout.css">
</head>
<body>
    <header class="app-header">
        <h1>Nueva visita</h1>
        <a class="logout-link" href="../auth/logout.php">Salir</a>
    </header>

    <main class="app-main">
        <div id="alerta" class="alert alert-error" style="display:none"></div>
        <div id="alerta-ok" class="alert alert-success" style="display:none"></div>

        <form id="form-contacto">
            <div class="field">
                <label for="pdv">Punto de venta (PDV)</label>
                <input type="text" id="pdv" name="pdv" list="lista-pdvs" autocomplete="off" placeholder="Escribe para buscar…" required>
                <datalist id="lista-pdvs"></datalist>
                <input type="hidden" id="codigo_pdv" name="codigo_pdv">
                <input type="hidden" id="ciudad_pdv" name="ciudad_pdv">
            </div>

            <div class="field">
                <label for="contacto">Nombre de contacto</label>
                <input type="text" id="contacto" name="contacto" required>
            </div>

            <div class="field">
                <label for="empresa">Empresa</label>
                <input type="text" id="empresa" name="empresa" required>
            </div>

            <div class="field">
                <label for="mail">Correo</label>
                <input type="email" id="mail" name="mail" required>
            </div>

            <div class="field">
                <label for="direccion">Dirección</label>
                <textarea id="direccion" name="direccion" rows="2" required></textarea>
            </div>

            <div class="field">
                <label for="telefono">Teléfono (10 dígitos)</label>
                <input type="tel" id="telefono" name="telefono" inputmode="numeric" maxlength="10" required>
            </div>

            <div class="field">
                <label for="telefono_convencional">Teléfono convencional (opcional)</label>
                <input type="tel" id="telefono_convencional" name="telefono_convencional" inputmode="numeric">
            </div>

            <div class="checkbox-field">
                <input type="checkbox" id="no_requiere_visita" name="no_requiere_visita">
                <label for="no_requiere_visita" style="margin:0">No requiere visita (pasa directo a Proforma)</label>
            </div>

            <div id="campos-agenda">
                <div class="field">
                    <label for="fecha_agendamiento">Fecha de visita</label>
                    <input type="date" id="fecha_agendamiento" name="fecha_agendamiento">
                </div>
                <div class="field">
                    <label for="hora">Hora</label>
                    <input type="time" id="hora" name="hora">
                </div>
            </div>

            <button type="submit" class="btn btn-primary btn-block">Guardar visita</button>
        </form>
    </main>

    <?php include __DIR__ . '/_nav.php'; ?>

    <script src="../assets/js/api.js"></script>
    <script src="../assets/js/contacto.js"></script>
</body>
</html>
