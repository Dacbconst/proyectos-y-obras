<?php
if (session_status() === PHP_SESSION_NONE) {
    // read_and_close suelta el bloqueo de sesión: las peticiones del mismo técnico ya no hacen cola entre sí.
    session_start(['read_and_close' => true]);
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
            header('Location: ../auth/login.php');
        }
        exit;
    }
    return $usuario;
}

// Ruta de un recurso estático con su fecha de modificación: cada cambio fuerza la descarga y el resto se cachea.
function asset(string $ruta): string
{
    $version = @filemtime(__DIR__ . '/../pages/' . $ruta);
    return $version ? $ruta . '?v=' . $version : $ruta;
}
