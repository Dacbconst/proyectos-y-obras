<?php
if (session_status() === PHP_SESSION_NONE) {
    session_start();
}

require_once __DIR__ . '/../db_connect.php';

if (!empty($_SESSION['usuario_tecnico'])) {
    header('Location: ../pages/contacto.php');
    exit;
}

$error = null;

// Detección de petición AJAX
$isAjax = (!empty($_SERVER['HTTP_X_REQUESTED_WITH']) && strtolower($_SERVER['HTTP_X_REQUESTED_WITH']) === 'xmlhttprequest')
    || (isset($_SERVER['HTTP_ACCEPT']) && strpos($_SERVER['HTTP_ACCEPT'], 'application/json') !== false)
    || (isset($_POST['ajax']) && $_POST['ajax'] === '1');

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $usuario_tecnico = trim($_POST['usuario_tecnico'] ?? '');
    $password = (string)($_POST['password'] ?? '');

    if ($usuario_tecnico === '' || $password === '') {
        $error = 'Ingresa usuario y contraseña.';
        if ($isAjax) {
            header('Content-Type: application/json; charset=utf-8');
            echo json_encode(['success' => false, 'message' => $error]);
            exit;
        }
    } else {
        $sql = $mysqli->prepare(
            "SELECT id, usuario_tecnico, nombre_completo, password
               FROM repositorio_usuario_tecnicos
              WHERE usuario_tecnico = ? AND activo = 1"
        );
        $sql->bind_param("s", $usuario_tecnico);
        $sql->execute();
        $fila = $sql->get_result()->fetch_assoc();
        $sql->close();

        if ($fila && $password === $fila['password']) {
            $_SESSION['usuario_tecnico'] = $fila['usuario_tecnico'];
            $_SESSION['nombre_completo'] = $fila['nombre_completo'];

            $update = $mysqli->prepare("UPDATE repositorio_usuario_tecnicos SET ultimo_login = NOW() WHERE id = ?");
            $update->bind_param("i", $fila['id']);
            $update->execute();
            $update->close();

            if ($isAjax) {
                header('Content-Type: application/json; charset=utf-8');
                echo json_encode(['success' => true, 'redirect' => '../pages/contacto.php', 'nombre' => ($fila['nombre_completo'] ?: $fila['usuario_tecnico']), 'usuario' => $fila['usuario_tecnico']]);
                exit;
            }

            header('Location: ../pages/contacto.php');
            exit;
        }

        $error = 'Usuario o contraseña incorrectos.';
        if ($isAjax) {
            header('Content-Type: application/json; charset=utf-8');
            echo json_encode(['success' => false, 'message' => $error]);
            exit;
        }
    }
}
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
    <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600;700;800&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="../assets/css/login.css?v=<?= filemtime(__DIR__ . '/../assets/css/login.css') ?>">
</head>
<body>
    <script>try { sessionStorage.clear(); } catch (e) { /* sin storage */ }</script>
    <!-- Capa de fondo que se revela en el centro al abrirse la división -->
        <!-- Pantalla limpia de bienvenida al iniciar sesión -->
    <div class="unlock-portal-layer" id="unlockPortal" aria-hidden="true">
        <div class="portal-content">
            <p class="portal-greeting" id="portalGreeting"></p>
            <h2 class="portal-username" id="portalUsername"></h2>
        </div>
    </div>

    <!-- Contenedor Dividido de la Pantalla -->
    <div class="login-layout" id="loginLayout">
        <!-- Línea sutil de división en el medio (Desktop) -->
        <div class="split-seam-line" aria-hidden="true"></div>

        <!-- Panel Izquierdo Hero (Desktop >= 900px) -->
        <aside class="desktop-hero-panel" id="desktopHeroPanel" aria-label="Información de plataforma">
            <div class="hero-content">
                <h1 class="hero-title">Control y trazabilidad de proyectos y obras</h1>
                <p class="hero-subtitle">Seguimiento operativo, gestión técnica y visibilidad en tiempo real en campo y punto de venta.</p>
            </div>

            <div class="hero-footer">
                <div class="security-badge">
                    <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true">
                        <rect x="3" y="11" width="18" height="11" rx="2" ry="2"></rect>
                        <path d="M7 11V7a5 5 0 0 1 10 0v4"></path>
                    </svg>
                    <span>Acceso seguro autorizado</span>
                </div>
            </div>
        </aside>

        <!-- Panel Derecho: Formulario (Móvil y Escritorio) -->
        <main class="form-container-panel" id="formContainerPanel">
            <!-- Header Móvil: TECNICOS - PROYECTOS Y OBRAS -->
            <header class="mobile-header">
                <h1 class="mobile-brand-title">TECNICOS</h1>
                <p class="mobile-brand-subtitle">PROYECTOS Y OBRAS</p>
            </header>

            <!-- Tarjeta Principal de Inicio de Sesión -->
            <div class="login-card" id="loginCard">
                <h2 class="desktop-card-title">Iniciar sesión</h2>

                <!-- Alerta de Error Dinámica -->
                <div class="alert-box" id="alertBox" role="alert" style="display: <?= $error ? 'flex' : 'none' ?>;">
                    <svg class="alert-icon" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                        <circle cx="12" cy="12" r="10"></circle>
                        <line x1="12" y1="8" x2="12" y2="12"></line>
                        <line x1="12" y1="16" x2="12.01" y2="16"></line>
                    </svg>
                    <span id="alertMessage"><?= htmlspecialchars($error ?? '') ?></span>
                </div>

                <form method="post" class="login-form" id="loginForm" autocomplete="on">
                    <!-- Campo Usuario -->
                    <div class="form-group">
                        <label for="usuario_tecnico" class="field-label">Usuario</label>
                        <div class="input-wrap">
                            <span class="field-icon" aria-hidden="true">
                                <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                                    <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"></path>
                                    <circle cx="12" cy="7" r="4"></circle>
                                </svg>
                            </span>
                            <input
                                type="text"
                                id="usuario_tecnico"
                                name="usuario_tecnico"
                                class="input-field"
                                autocomplete="username"
                                autocapitalize="none"
                                autocorrect="off"
                                spellcheck="false"
                                placeholder="Iniciar sesión"
                                required
                                autofocus
                            >
                        </div>
                    </div>

                    <!-- Campo Contraseña -->
                    <div class="form-group">
                        <label for="password" class="field-label">Contraseña</label>
                        <div class="input-wrap">
                            <span class="field-icon" aria-hidden="true">
                                <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                                    <rect x="3" y="11" width="18" height="11" rx="2" ry="2"></rect>
                                    <path d="M7 11V7a5 5 0 0 1 10 0v4"></path>
                                </svg>
                            </span>
                            <input
                                type="password"
                                id="password"
                                name="password"
                                class="input-field has-toggle"
                                autocomplete="current-password"
                                placeholder="Contraseña"
                                required
                            >
                            <button type="button" class="pw-toggle-btn" id="togglePassword" aria-label="Mostrar u ocultar contraseña" title="Mostrar u ocultar contraseña">
                                <svg id="eyeIcon" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                                    <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"></path>
                                    <circle cx="12" cy="12" r="3"></circle>
                                </svg>
                            </button>
                        </div>
                    </div>

                    <!-- Botón de Envío -->
                    <button type="submit" class="submit-btn" id="btnSubmit">
                        <span class="btn-text-mobile" id="btnTextMobile">Iniciar sesión</span>
                        <span class="btn-text-desktop" id="btnTextDesktop">Ingresar</span>
                    </button>
                </form>

                <!-- Logos Grupo Lucky y Pintuco -->
                <div class="brand-logos-row">
                    <img src="../assets/img/grupo_lucky.png" alt="Grupo Lucky" class="logo-img logo-lucky" loading="eager">
                    <img src="../assets/img/pintuco.png" alt="Pintuco" class="logo-img logo-pintuco" loading="eager">
                </div>
            </div>

            <!-- Footer Desktop -->
            <footer class="desktop-page-footer">
                <p>© PromoLucky 2026</p>
            </footer>
        </main>
    </div>

    <script src="../assets/js/login.js?v=639262026262680148"></script>
</body>
</html>
