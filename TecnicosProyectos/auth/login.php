<?php
require_once __DIR__ . '/../db_connect.php';

if (session_status() === PHP_SESSION_NONE) {
    session_start();
}

if (!empty($_SESSION['usuario_tecnico'])) {
    header('Location: ../pages/contacto.php');
    exit;
}

$error = null;

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $usuario_tecnico = trim($_POST['usuario_tecnico'] ?? '');
    $password = (string)($_POST['password'] ?? '');

    if ($usuario_tecnico === '' || $password === '') {
        $error = 'Ingresa usuario y contraseña.';
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

            header('Location: ../pages/contacto.php');
            exit;
        }
        $error = 'Usuario o contraseña incorrectos.';
    }
}
?>
<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1, viewport-fit=cover, interactive-widget=resizes-content">
<meta name="theme-color" content="#090514">
<title>Proyectos y Obras</title>
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600;700&display=swap" rel="stylesheet">
<style>
* {
    box-sizing: border-box;
    margin: 0;
    padding: 0;
}

html, body {
    width: 100%;
    min-height: 100vh;
    min-height: 100dvh;
    font-family: 'Plus Jakarta Sans', -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif;
    -webkit-tap-highlight-color: transparent;
    -webkit-text-size-adjust: 100%;
    background-color: #090514;
    color: #FFFFFF;
    overflow-x: hidden;
}

.login-body {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    min-height: 100vh;
    min-height: 100dvh;
    padding: max(20px, env(safe-area-inset-top)) max(18px, env(safe-area-inset-right)) max(20px, env(safe-area-inset-bottom)) max(18px, env(safe-area-inset-left));
    position: relative;
}

/* Atmósfera arquitectónica sutil */
.ambient-glow {
    position: fixed;
    inset: 0;
    pointer-events: none;
    background: 
        radial-gradient(ellipse 70% 50% at 50% -10%, rgba(106, 79, 209, 0.32), transparent 75%),
        radial-gradient(circle at 50% 110%, rgba(46, 26, 84, 0.25), transparent 60%);
    z-index: 0;
}

/* Arquitectura Doble-Bezel */
.login-card-shell {
    width: 100%;
    max-width: 370px;
    padding: 1.5px;
    border-radius: 28px;
    background: linear-gradient(180deg, rgba(255, 255, 255, 0.14) 0%, rgba(255, 255, 255, 0.03) 100%);
    box-shadow: 
        0 30px 60px -15px rgba(0, 0, 0, 0.8),
        0 0 0 1px rgba(0, 0, 0, 0.5);
    position: relative;
    z-index: 1;
    margin: auto 0;
}

@media (min-width: 640px) {
    .login-card-shell {
        max-width: 390px;
    }
}

.login-card-core {
    background: rgba(18, 11, 34, 0.94);
    backdrop-filter: blur(28px);
    -webkit-backdrop-filter: blur(28px);
    border-radius: 26.5px;
    padding: 36px 26px 32px;
    box-shadow: inset 0 1px 1px rgba(255, 255, 255, 0.12);
}

@media (min-width: 640px) {
    .login-card-core {
        padding: 42px 32px 36px;
    }
}

/* Cabecera limpia: solo ícono y nombre */
.login-header {
    display: flex;
    flex-direction: column;
    align-items: center;
    text-align: center;
    margin-bottom: 28px;
}

.brand-emblem {
    width: 52px;
    height: 52px;
    border-radius: 16px;
    background: linear-gradient(145deg, #2E1A54, #48278A);
    border: 1px solid rgba(255, 255, 255, 0.16);
    box-shadow: 
        inset 0 1px 1px rgba(255, 255, 255, 0.28),
        0 10px 24px -4px rgba(46, 26, 84, 0.6);
    display: flex;
    align-items: center;
    justify-content: center;
    color: #EDE9FE;
    margin-bottom: 16px;
}

.brand-title {
    font-size: 21px;
    font-weight: 700;
    color: #FFFFFF;
    letter-spacing: -0.03em;
    margin: 0;
    line-height: 1.2;
}

/* Campos ergonómicos */
.field-wrap {
    margin-bottom: 14px;
}

.input-box {
    position: relative;
    width: 100%;
    height: 52px;
    background: rgba(255, 255, 255, 0.04);
    border: 1px solid rgba(255, 255, 255, 0.09);
    border-radius: 15px;
    display: flex;
    align-items: center;
    transition: all 0.2s cubic-bezier(0.16, 1, 0.3, 1);
    box-shadow: inset 0 2px 4px rgba(0, 0, 0, 0.2);
}

.input-box:focus-within {
    background: rgba(255, 255, 255, 0.07);
    border-color: #8B5CF6;
    box-shadow: 
        0 0 0 3px rgba(139, 92, 246, 0.22),
        inset 0 1px 2px rgba(0, 0, 0, 0.15);
}

.field-icon {
    position: absolute;
    left: 16px;
    color: #7B7094;
    pointer-events: none;
    display: flex;
    align-items: center;
    justify-content: center;
    transition: color 0.2s cubic-bezier(0.16, 1, 0.3, 1);
    z-index: 2;
}

.input-box:focus-within .field-icon {
    color: #A78BFA;
}

.input-box input {
    background: transparent;
    border: none;
    outline: none;
    width: 100%;
    height: 100%;
    padding: 0 16px 0 48px;
    color: #FFFFFF;
    font-size: 16px;
    font-family: inherit;
    box-sizing: border-box;
    -webkit-appearance: none;
}

.input-box input.has-toggle {
    padding-right: 50px;
}

.input-box input::placeholder {
    color: #63577D;
    font-weight: 400;
}

/* Botón ver/ocultar contraseña */
.pw-toggle-btn {
    position: absolute;
    right: 8px;
    width: 36px;
    height: 36px;
    border-radius: 10px;
    background: transparent;
    border: none;
    color: #7B7094;
    display: flex;
    align-items: center;
    justify-content: center;
    cursor: pointer;
    transition: all 0.15s ease;
    z-index: 2;
    padding: 0;
}

.pw-toggle-btn:hover {
    color: #EDE9FE;
    background: rgba(255, 255, 255, 0.07);
}

.pw-toggle-btn:active {
    transform: scale(0.92);
}

/* Botón con icon wrapper integrado */
.submit-btn {
    width: 100%;
    height: 52px;
    margin-top: 18px;
    background: linear-gradient(180deg, #6E4AD6 0%, #5837B5 100%);
    border: 1px solid rgba(255, 255, 255, 0.2);
    border-radius: 15px;
    color: #FFFFFF;
    font-size: 15px;
    font-weight: 600;
    font-family: inherit;
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 0 10px 0 22px;
    cursor: pointer;
    box-shadow: 
        inset 0 1px 1px rgba(255, 255, 255, 0.32),
        0 10px 24px -6px rgba(106, 79, 209, 0.55);
    transition: all 0.2s cubic-bezier(0.16, 1, 0.3, 1);
    box-sizing: border-box;
    touch-action: manipulation;
}

@media (hover: hover) and (pointer: fine) {
    .submit-btn:hover {
        background: linear-gradient(180deg, #7853E3 0%, #613DC2 100%);
        box-shadow: 
            inset 0 1px 1px rgba(255, 255, 255, 0.4),
            0 14px 28px -6px rgba(106, 79, 209, 0.65);
        transform: translateY(-1px);
    }
    .submit-btn:hover .btn-icon-wrapper {
        transform: translateX(2px);
    }
}

.submit-btn:active {
    transform: scale(0.985);
    filter: brightness(0.95);
}

.submit-btn:disabled {
    opacity: 0.65;
    cursor: not-allowed;
    transform: none;
}

.btn-icon-wrapper {
    width: 32px;
    height: 32px;
    border-radius: 50%;
    background: rgba(255, 255, 255, 0.18);
    display: flex;
    align-items: center;
    justify-content: center;
    color: #FFFFFF;
    transition: transform 0.2s cubic-bezier(0.16, 1, 0.3, 1);
}

/* Alerta de error concisa */
.alert-box {
    display: flex;
    align-items: center;
    gap: 10px;
    background: rgba(239, 68, 68, 0.12);
    border: 1px solid rgba(239, 68, 68, 0.3);
    border-radius: 12px;
    color: #FCA5A5;
    padding: 10px 14px;
    font-size: 13px;
    font-weight: 500;
    margin-bottom: 18px;
    line-height: 1.35;
}
</style>
</head>
<body class="login-body">
    <div class="ambient-glow" aria-hidden="true"></div>

    <main class="login-card-shell" role="main">
        <div class="login-card-core">
            <header class="login-header">
                <div class="brand-emblem" aria-hidden="true">
                    <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.9" stroke-linecap="round" stroke-linejoin="round">
                        <path d="m21.12 6.4-6.05-3.5a2 2 0 0 0-2.14 0L6.88 6.4a2 2 0 0 0-1 1.73v7a2 2 0 0 0 1 1.73l6.05 3.5a2 2 0 0 0 2.14 0l6.05-3.5a2 2 0 0 0 1-1.73v-7a2 2 0 0 0-1-1.73z"></path>
                        <path d="M12 22V12"></path>
                        <polyline points="3.29 7 12 12 20.71 7"></polyline>
                    </svg>
                </div>
                <h1 class="brand-title">Proyectos y Obras</h1>
            </header>

            <?php if ($error): ?>
                <div class="alert-box" role="alert">
                    <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" style="flex-shrink:0;">
                        <circle cx="12" cy="12" r="10"></circle>
                        <line x1="12" y1="8" x2="12" y2="12"></line>
                        <line x1="12" y1="16" x2="12.01" y2="16"></line>
                    </svg>
                    <span><?= htmlspecialchars($error) ?></span>
                </div>
            <?php endif; ?>

            <form method="post" class="login-form" id="loginForm" autocomplete="on">
                <div class="field-wrap">
                    <div class="input-box">
                        <span class="field-icon" aria-hidden="true">
                            <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round">
                                <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"></path>
                                <circle cx="12" cy="7" r="4"></circle>
                            </svg>
                        </span>
                        <input 
                            type="text" 
                            id="usuario_tecnico" 
                            name="usuario_tecnico" 
                            autocomplete="username" 
                            autocapitalize="none" 
                            autocorrect="off" 
                            spellcheck="false" 
                            placeholder="Usuario" 
                            required 
                            autofocus
                        >
                    </div>
                </div>

                <div class="field-wrap">
                    <div class="input-box">
                        <span class="field-icon" aria-hidden="true">
                            <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round">
                                <rect x="3" y="11" width="18" height="11" rx="2" ry="2"></rect>
                                <path d="M7 11V7a5 5 0 0 1 10 0v4"></path>
                            </svg>
                        </span>
                        <input 
                            type="password" 
                            id="password" 
                            name="password" 
                            autocomplete="current-password" 
                            placeholder="Contraseña" 
                            required 
                            class="has-toggle"
                        >
                        <button type="button" class="pw-toggle-btn" id="togglePassword" aria-label="Mostrar contraseña" title="Mostrar u ocultar contraseña">
                            <svg id="eyeIcon" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round">
                                <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"></path>
                                <circle cx="12" cy="12" r="3"></circle>
                            </svg>
                        </button>
                    </div>
                </div>

                <button type="submit" class="submit-btn" id="btnSubmit">
                    <span id="btnText">Ingresar</span>
                    <span class="btn-icon-wrapper" aria-hidden="true">
                        <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.2" stroke-linecap="round" stroke-linejoin="round">
                            <line x1="5" y1="12" x2="19" y2="12"></line>
                            <polyline points="12 5 19 12 12 19"></polyline>
                        </svg>
                    </span>
                </button>
            </form>
        </div>
    </main>

    <script>
    (function() {
        const toggleBtn = document.getElementById('togglePassword');
        const pwInput = document.getElementById('password');
        const eyeIcon = document.getElementById('eyeIcon');
        const form = document.getElementById('loginForm');
        const btnSubmit = document.getElementById('btnSubmit');
        const btnText = document.getElementById('btnText');

        if (toggleBtn && pwInput) {
            toggleBtn.addEventListener('click', function(e) {
                e.preventDefault();
                const isPassword = pwInput.getAttribute('type') === 'password';
                pwInput.setAttribute('type', isPassword ? 'text' : 'password');
                
                if (isPassword) {
                    toggleBtn.setAttribute('aria-label', 'Ocultar contraseña');
                    eyeIcon.innerHTML = `
                        <path d="M17.94 17.94A10.07 10.07 0 0 1 12 20c-7 0-11-8-11-8a18.45 18.45 0 0 1 5.06-5.94M9.9 4.24A9.12 9.12 0 0 1 12 4c7 0 11 8 11 8a18.5 18.5 0 0 1-2.16 3.19m-6.72-1.07a3 3 0 1 1-4.24-4.24"></path>
                        <line x1="1" y1="1" x2="23" y2="23"></line>
                    `;
                } else {
                    toggleBtn.setAttribute('aria-label', 'Mostrar contraseña');
                    eyeIcon.innerHTML = `
                        <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"></path>
                        <circle cx="12" cy="12" r="3"></circle>
                    `;
                }
            });
        }

        if (form && btnSubmit) {
            form.addEventListener('submit', function() {
                btnSubmit.disabled = true;
                if (btnText) btnText.textContent = 'Verificando…';
            });
        }
    })();
    </script>
</body>
</html>
