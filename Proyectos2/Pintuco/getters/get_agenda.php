<?php
// get_agenda.php — lista las visitas agendadas para el panel del analista (Web)
header('Access-Control-Allow-Origin: *');
header("Access-Control-Allow-Headers: Origin, X-Requested-With, Content-Type, Accept");
header('Access-Control-Allow-Methods: GET, POST, PUT, DELETE, OPTIONS');

include_once '../db_connect.php';
require_once __DIR__ . '/_canal.php';
$tContacto = TABLA_CONTACTO;
$tProforma = TABLA_PROFORMA;

// "vencida" no la elige nadie a mano: el contrato con la app móvil (que lee
// esta misma tabla directo por sync) dice que una visita pasa a 'vencida'
// en cuanto se le acaba el día sin reagendarse, sin importar si antes era
// 'pendiente', 'confirmado' o 'reagendada'. No hay un cron/job en este
// hosting, así que se corrige de forma perezosa acá: cada vez que se abre
// la agenda (web o, indirectamente, el próximo sync del celular) se ponen
// al día las filas vencidas antes de leer.
$mysqli->query(
    "UPDATE $tContacto
     SET estado_agenda = 'vencida'
     WHERE activar = 'SI'
       AND fecha_agendamiento IS NOT NULL
       AND fecha_agendamiento != '0000-00-00'
       AND fecha_agendamiento < CURDATE()
       AND estado_agenda NOT IN ('cancelada', 'completada', 'vencida')"
);

// Promotores: la primera foto de proforma completa la cita; en citas de técnicos la confirma el técnico desde su Agenda.
$mysqli->query(
    "UPDATE $tContacto c
     JOIN $tProforma p ON p.id_agendamiento = c.id
     SET c.estado_agenda = 'completada'
     WHERE c.activar = 'SI'
       AND p.evidencia IS NOT NULL AND p.evidencia != ''
       AND c.estado_agenda NOT IN ('cancelada', 'completada')
       AND c.usuario NOT IN (SELECT usuario_tecnico FROM repositorio_usuario_tecnicos WHERE usuario_tecnico IS NOT NULL)
       AND (c.tecnico IS NULL OR c.tecnico NOT IN (SELECT usuario_tecnico FROM repositorio_usuario_tecnicos WHERE usuario_tecnico IS NOT NULL))"
);

// Filtros opcionales por GET (todos opcionales, si no llegan no se aplican)
$fecha_inicio = isset($_GET['fecha_inicio']) ? $_GET['fecha_inicio'] : '';
$fecha_fin    = isset($_GET['fecha_fin'])    ? $_GET['fecha_fin']    : '';
$estado       = isset($_GET['estado_agenda'])? $_GET['estado_agenda']: '';
$usuario      = isset($_GET['usuario'])      ? $_GET['usuario']      : '';
$tecnico      = isset($_GET['tecnico'])      ? $_GET['tecnico']      : '';
$pdv          = isset($_GET['pdv'])          ? $_GET['pdv']          : '';
$empresa      = isset($_GET['empresa'])      ? $_GET['empresa']      : '';

// fecha_agendamiento es columna DATE real (no string dd/mm/yyyy); se compara directo.
// "activar" es varchar(2) NOT NULL con valores 'SI'/'NO' (default 'SI'),
// confirmado contra la tabla real — es el borrado lógico: filas en 'NO' son
// visitas eliminadas desde el panel y no deben aparecer en la agenda.
$condiciones = [
    "fecha_agendamiento IS NOT NULL",
    "fecha_agendamiento != '0000-00-00'",
    "activar = 'SI'",
];
$parametros  = [];
$tipos       = "";

// Switch de canales (sidebar): 'promotores' por defecto, igual que el resto.
$canal = isset($_GET['canal']) ? $_GET['canal'] : 'promotores';
// Vía de escape de los filtros de opciones y de "Agendas pendientes": necesitan ver también las completadas.
$incluirCompletadas = isset($_GET['incluir_completadas']) && $_GET['incluir_completadas'] === '1';
if ($condicionCanal = canal_condicion_sql($canal, 'usuario')) {
    $condiciones[] = $condicionCanal;
}

if ($fecha_inicio !== '' && $fecha_fin !== '') {
    $condiciones[] = "fecha_agendamiento BETWEEN ? AND ?";
    $parametros[] = $fecha_inicio;
    $parametros[] = $fecha_fin;
    $tipos .= "ss";
}
if ($estado !== '') {
    $condiciones[] = "estado_agenda = ?";
    $parametros[] = $estado;
    $tipos .= "s";
} elseif ($canal !== 'tecnicos' && !$incluirCompletadas) {
    // Promotores: la visita completada sale del calendario por defecto (igual que producción); en Técnicos se queda visible.
    $condiciones[] = "estado_agenda != 'completada'";
}
if ($usuario !== '') {
    $condiciones[] = "usuario = ?";
    $parametros[] = $usuario;
    $tipos .= "s";
}
if ($tecnico !== '') {
    $condiciones[] = "tecnico = ?";
    $parametros[] = $tecnico;
    $tipos .= "s";
}
if ($pdv !== '') {
    $condiciones[] = "pdv = ?";
    $parametros[] = $pdv;
    $tipos .= "s";
}
if ($empresa !== '') {
    $condiciones[] = "empresa = ?";
    $parametros[] = $empresa;
    $tipos .= "s";
}

$query = "SELECT id, codigo_pdv, pdv, usuario, fecha, fecha_registro, contacto, empresa, mail, direccion,
                 latitud, longitud, telefono, telefono_convencional, fecha_agendamiento, titulo, hora, lugar,
                 tecnico, estado_agenda, activar, motivo_reagendacion, foto_pdv
          FROM $tContacto
          WHERE " . implode(" AND ", $condiciones) . "
          ORDER BY fecha_agendamiento, hora";

$registros = [];

if ($sql = $mysqli->prepare($query)) {
    if (!empty($parametros)) {
        $sql->bind_param($tipos, ...$parametros);
    }
    $sql->execute();
    $resultado = $sql->get_result();
    while ($fila = $resultado->fetch_assoc()) {
        $registros[] = $fila;
    }
    $sql->close();
}

echo json_encode(["data" => $registros, "count" => count($registros)]);
?>
