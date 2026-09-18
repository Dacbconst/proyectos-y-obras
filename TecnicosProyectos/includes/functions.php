<?php
/**
 * Validaciones portadas literal de Proyectos2/Pintuco/getters/insert_contacto.php
 * para que un registro creado desde TecnicosProyectos pase exactamente las
 * mismas reglas que ya protegen esa tabla del lado web.
 */

function validar_contacto_nombre(string $contacto): ?string
{
    if ($contacto === ''
        || !preg_match("/^[A-Za-zÁÉÍÓÚÑáéíóúñ' -]+$/u", $contacto)
        || preg_match_all('/[A-Za-zÁÉÍÓÚÑáéíóúñ]/u', $contacto) < 2) {
        return "Contacto inválido: solo letras, espacios, apóstrofes y guiones (mínimo 2 letras).";
    }
    return null;
}

function validar_empresa(string $empresa): ?string
{
    if ($empresa === '' || !preg_match("/^[A-Za-z0-9ÁÉÍÓÚÑáéíóúñ.\\-&' ]+$/u", $empresa)) {
        return "Empresa inválida.";
    }
    return null;
}

function validar_mail(string $mail): ?string
{
    if ($mail === ''
        || !preg_match('/^[^\s@.][^\s@]*[^\s@.]@[^\s@]+\.[^\s@]+$/', $mail)
        || strpos($mail, '..') !== false) {
        return "Correo inválido.";
    }
    return null;
}

function validar_telefono(string $telefono): ?string
{
    if ($telefono === '' || !preg_match('/^\d{10}$/', $telefono)) {
        return "Teléfono inválido: debe ser numérico y de exactamente 10 dígitos.";
    }
    return null;
}

function validar_telefono_convencional(?string $telefono_convencional): ?string
{
    if ($telefono_convencional && !preg_match('/^\d+$/', $telefono_convencional)) {
        return "El teléfono convencional solo admite dígitos.";
    }
    return null;
}

function es_plus_code(string $direccion): bool
{
    return (bool)preg_match('/^[23456789CFGHJMPQRVWX]{4,8}\+[23456789CFGHJMPQRVWX]{2,3}$/i', $direccion);
}

function validar_direccion(string $direccion): ?string
{
    if ($direccion === '') {
        return "La dirección es obligatoria.";
    }
    if (es_plus_code($direccion)) {
        return "La dirección parece un Plus Code — se necesita una dirección legible.";
    }
    return null;
}

function validar_fecha_agendamiento(?string $fecha_agendamiento): ?string
{
    if (!$fecha_agendamiento) {
        return null; // opcional en TecnicosProyectos (igual que el app: se puede agendar después)
    }
    if ($fecha_agendamiento < date('Y-m-d')) {
        return "La fecha de agendamiento no puede ser pasada.";
    }
    return null;
}

/**
 * Un técnico no puede estar en dos visitas a la vez — mismo criterio que
 * Proyectos2/Pintuco/getters/insert_contacto.php y update_agenda.php.
 * Devuelve el registro en conflicto, o null si no hay choque.
 */
function buscar_conflicto_horario(mysqli $mysqli, string $fecha_agendamiento, string $tecnico, string $hora, int $duracionAproxMin = 45, ?int $excluirId = null): ?array
{
    $query = "SELECT id, hora, titulo, pdv, contacto, empresa, estado_agenda FROM insert_proyectos_contacto
              WHERE fecha_agendamiento = ? AND tecnico = ? AND activar = 'SI'
                AND estado_agenda != 'cancelada' AND hora IS NOT NULL AND hora != ''";
    $tipos = "ss";
    $parametros = [$fecha_agendamiento, $tecnico];
    if ($excluirId !== null) {
        $query .= " AND id != ?";
        $tipos .= "i";
        $parametros[] = $excluirId;
    }

    $sql = $mysqli->prepare($query);
    if (!$sql) {
        return null;
    }
    $sql->bind_param($tipos, ...$parametros);
    $sql->execute();
    $resultado = $sql->get_result();

    $minutosNuevaHora = (int)substr($hora, 0, 2) * 60 + (int)substr($hora, 3, 2);
    $conflicto = null;
    while ($fila = $resultado->fetch_assoc()) {
        $horaExistente = $fila['hora'];
        $minutosExistente = (int)substr($horaExistente, 0, 2) * 60 + (int)substr($horaExistente, 3, 2);
        if (abs($minutosNuevaHora - $minutosExistente) < $duracionAproxMin) {
            $conflicto = $fila;
            break;
        }
    }
    $sql->close();
    return $conflicto;
}

function responder_json(array $payload, int $status = 200): void
{
    http_response_code($status);
    header('Content-Type: application/json');
    echo json_encode($payload);
    exit;
}

function nombre_archivo_evidencia(string $usuario_tecnico, string $codigo_pdv): string
{
    $nombre = date('dmYHis') . $usuario_tecnico . $codigo_pdv;
    return preg_replace('/[\\\\\/:\*\?"<>\|%\+#\s]/', '', $nombre);
}
