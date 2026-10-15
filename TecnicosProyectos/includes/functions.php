<?php
// Validaciones equivalentes a las de Proyectos2 para insert_proyectos_contacto.

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
    if ($empresa === '' || !preg_match("/^[A-Za-z0-9ÁÉÍÓÚÑÜáéíóúñü ]+$/u", $empresa)) {
        return "Empresa inválida: solo letras, números y espacios.";
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

function validar_monto(?float $monto): ?string
{
    if ($monto !== null && ($monto < 0 || $monto > 99999.99)) {
        return "El monto debe estar entre 0 y 99999.99.";
    }
    return null;
}

// Foto recibida en base64: debe ser una imagen real, de hasta 3 MB y 4096 px de ancho (el alto no se limita). Devuelve [error, mime].
function validar_foto_base64(string $base64): array
{
    $binario = base64_decode($base64, true);
    if ($binario === false || $binario === '') {
        return ['error' => 'La foto no llegó completa. Vuelve a tomarla e intenta de nuevo.', 'mime' => null];
    }
    if (strlen($binario) > 3 * 1024 * 1024) {
        return ['error' => 'La foto es demasiado pesada. Toma otra con menos resolución.', 'mime' => null];
    }
    $info = @getimagesizefromstring($binario);
    if (!$info || !in_array($info['mime'], ['image/jpeg', 'image/png', 'image/webp'], true)) {
        return ['error' => 'El archivo no es una foto válida (usa JPG, PNG o WEBP).', 'mime' => null];
    }
    if ($info[0] > 4096) {
        return ['error' => 'La foto es demasiado grande. Toma otra con menos resolución.', 'mime' => null];
    }
    return ['error' => null, 'mime' => $info['mime']];
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

// Un técnico no puede estar en dos visitas a la vez; devuelve el conflicto o null.
function buscar_conflicto_horario(mysqli $mysqli, string $fecha_agendamiento, string $tecnico, string $hora, int $duracionAproxMin = 45, ?int $excluirId = null): ?array
{
    $query = "SELECT id, hora, titulo, pdv, contacto, empresa, estado_agenda FROM " . TABLA_CONTACTO . "
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

// Vence las citas cuya fecha ya pasó sin confirmar; "completada" solo la pone el técnico al confirmar la visita.
function actualizar_estados_agenda(mysqli $mysqli): void
{
    $tContacto = TABLA_CONTACTO;
    $mysqli->query(
        "UPDATE $tContacto
         SET estado_agenda = 'vencida'
         WHERE activar = 'SI'
           AND fecha_agendamiento IS NOT NULL
           AND fecha_agendamiento != '0000-00-00'
           AND fecha_agendamiento < CURDATE()
           AND estado_agenda NOT IN ('cancelada', 'completada', 'vencida', 'asistio_pendiente_proforma')"
    );
}

/** Copia la foto del PDV de una visita al local de técnicos (PDVTEC...) para que el catálogo la tenga. */
function guardar_foto_en_local(mysqli $mysqli, string $codigoPdv, string $ruta): void
{
    if (strpos($codigoPdv, 'PDVTEC') !== 0) return;
    $tLocales = TABLA_LOCALES;
    $sql = $mysqli->prepare("UPDATE $tLocales SET foto_pdv = ? WHERE codigo_pdv = ?");
    $sql->bind_param("ss", $ruta, $codigoPdv);
    $sql->execute();
    $sql->close();
}

/** Provincias y ciudades de Ecuador (cantones, en data/) más las ciudades del catálogo de Pintuco que no son cantón. */
function ubicaciones_ecuador(mysqli $mysqli): array
{
    $mapa = json_decode(file_get_contents(__DIR__ . '/../data/ecuador_ubicaciones.json'), true) ?? [];
    $conocidas = array_merge(...array_values($mapa));
    $catalogo = $mysqli->query("SELECT DISTINCT province, city FROM repositorio_locales_dtt2 WHERE activar = 'SI' AND province != ''");
    while ($catalogo && $fila = $catalogo->fetch_assoc()) {
        if (!in_array($fila['city'], $conocidas, true)) $mapa[$fila['province']][] = $fila['city'];
    }
    $ubicaciones = [];
    foreach ($mapa as $provincia => $ciudades) {
        foreach (array_unique($ciudades) as $ciudad) $ubicaciones[] = ['province' => $provincia, 'city' => $ciudad];
    }
    usort($ubicaciones, fn($a, $b) => [$a['province'], $a['city']] <=> [$b['province'], $b['city']]);
    return $ubicaciones;
}
