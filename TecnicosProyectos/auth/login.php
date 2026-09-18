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
<meta name="theme-color" content="#2E1A54">
<title>Proyectos y Obras — Ingreso</title>
<link rel="stylesheet" href="../assets/css/base.css">
<link rel="stylesheet" href="../assets/css/components.css">
</head>
<body class="login-body">
    <main class="login-card">
        <h1 class="login-title">Proyectos y Obras</h1>
        <p class="login-subtitle">Ingresa con tu usuario técnico</p>

        <?php if ($error): ?>
            <div class="alert alert-error"><?= htmlspecialchars($error) ?></div>
        <?php endif; ?>

        <form method="post" class="login-form">
            <label for="usuario_tecnico">Usuario</label>
            <input type="text" id="usuario_tecnico" name="usuario_tecnico" autocomplete="username" required autofocus>

            <label for="password">Contraseña</label>
            <input type="password" id="password" name="password" autocomplete="current-password" required>

            <button type="submit" class="btn btn-primary btn-block">Ingresar</button>
        </form>
    </main>
</body>
</html>
