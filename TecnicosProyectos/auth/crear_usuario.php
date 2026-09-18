<?php
/**
 * Utilidad de alta manual de técnicos. Ábrela una vez en el navegador,
 * llena el formulario y guarda — inserta en repositorio_usuario_tecnicos.
 *
 * Usuario/contraseña de TecnicosProyectos son propios de esta web, sin
 * relación con ningún otro sistema — los defines aquí, no tienen que
 * coincidir con nada externo.
 *
 * IMPORTANTE: esta página no pide login para poder usarla la primera vez.
 * Bórrala o protégela (ej. renómbrala a algo no adivinable) una vez que ya
 * tengas dados de alta a los técnicos que necesitas — dejarla accesible
 * permite crear cuentas nuevas sin autenticación.
 */
require_once __DIR__ . '/../db_connect.php';

$mensaje = null;
$sqlGenerado = null;
$esError = false;

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $usuario_tecnico = trim($_POST['usuario_tecnico'] ?? '');
    $nombre_completo = trim($_POST['nombre_completo'] ?? '');
    $password = (string)($_POST['password'] ?? '');

    if ($usuario_tecnico === '' || $nombre_completo === '' || $password === '') {
        $mensaje = 'Completa todos los campos.';
        $esError = true;
    } else {
        $sql = $mysqli->prepare(
            "INSERT INTO repositorio_usuario_tecnicos (usuario_tecnico, nombre_completo, password, activo)
             VALUES (?, ?, ?, 1)"
        );
        $sql->bind_param("sss", $usuario_tecnico, $nombre_completo, $password);
        $ok = $sql->execute();
        $sql->close();

        if ($ok) {
            $mensaje = "Usuario '{$usuario_tecnico}' creado correctamente. Ya puede ingresar en login.php.";
            $sqlGenerado = "INSERT INTO repositorio_usuario_tecnicos (usuario_tecnico, nombre_completo, password, activo)\nVALUES ('" . addslashes($usuario_tecnico) . "', '" . addslashes($nombre_completo) . "', '" . addslashes($password) . "', 1);";
        } else {
            $mensaje = 'Error: ' . $mysqli->error . ' (¿el usuario_tecnico ya existe?)';
            $esError = true;
        }
    }
}
?>
<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1, viewport-fit=cover, interactive-widget=resizes-content">
<meta name="theme-color" content="#2E1A54">
<title>Alta de técnico — TecnicosProyectos</title>
<link rel="stylesheet" href="../assets/css/base.css">
<link rel="stylesheet" href="../assets/css/components.css">
</head>
<body class="login-body">
    <main class="login-card" style="max-width:420px">
        <h1 class="login-title">Crear usuario técnico</h1>
        <p class="login-subtitle">Inserta el usuario nuevo en repositorio_usuario_tecnicos</p>

        <?php if ($mensaje): ?>
            <div class="alert <?= $esError ? 'alert-error' : 'alert-success' ?>"><?= htmlspecialchars($mensaje) ?></div>
        <?php endif; ?>

        <?php if ($sqlGenerado): ?>
            <div class="field">
                <label>SQL ejecutado (referencia)</label>
                <textarea rows="4" readonly style="width:100%;font-family:monospace;font-size:12px;padding:8px;border:1px solid var(--color-border);border-radius:8px"><?= htmlspecialchars($sqlGenerado) ?></textarea>
            </div>
        <?php endif; ?>

        <form method="post" class="login-form">
            <label for="usuario_tecnico">Usuario técnico (lo defines tú, libre)</label>
            <input type="text" id="usuario_tecnico" name="usuario_tecnico" required>

            <label for="nombre_completo">Nombre completo</label>
            <input type="text" id="nombre_completo" name="nombre_completo" required>

            <label for="password">Contraseña para la web</label>
            <input type="text" id="password" name="password" required>

            <button type="submit" class="btn btn-primary btn-block">Crear usuario</button>
        </form>

        <p style="margin-top:16px;font-size:12px;color:var(--color-text-muted)">
            Borra o renombra este archivo cuando termines de dar de alta a los técnicos que necesitas.
        </p>
    </main>
</body>
</html>
