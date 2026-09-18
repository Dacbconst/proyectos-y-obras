<?php
if (session_status() === PHP_SESSION_NONE) {
    session_start();
}

function usuario_actual(): ?string
{
    return $_SESSION['usuario_tecnico'] ?? null;
}

function exigir_sesion(): string
{
    $usuario = usuario_actual();
    if ($usuario === null) {
        $esApi = strpos($_SERVER['REQUEST_URI'] ?? '', '/getters/') !== false;
        if ($esApi) {
            http_response_code(401);
            header('Content-Type: application/json');
            echo json_encode(["success" => false, "message" => "Sesión no iniciada."]);
        } else {
            // Ruta absoluta real de despliegue: /App/XploraEcuador/TecnicosProyectos/
            header('Location: /App/XploraEcuador/TecnicosProyectos/auth/login.php');
        }
        exit;
    }
    return $usuario;
}
