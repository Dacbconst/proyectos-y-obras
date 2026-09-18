<?php


header('Content-Type: application/json');
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Methods: POST, OPTIONS');
header('Access-Control-Allow-Headers: Content-Type, Accept, User-Agent');

require_once '../Data/Funciones.php'; 

if ($_SERVER['REQUEST_METHOD'] != 'POST') {
    http_response_code(405);
    echo json_encode(array('estado' => '0', 'mensaje' => 'Solo POST'));
    exit;
}

$input = file_get_contents('php://input');
$body = json_decode($input, true);

if (json_last_error() !== JSON_ERROR_NONE) {
    echo json_encode(array('estado' => '0', 'mensaje' => 'JSON inválido'));
    exit;
}

// =====  FECHA DEL ÚLTIMO RELEVO =====
if (isset($body['solo_fecha']) && $body['solo_fecha'] == '1') {
    $usuario = trim($body['usuario']);
    $codigo_pdv = trim($body['codigo_pdv']);
    $tipo_logistico = isset($body['tipo_logistico']) ? trim($body['tipo_logistico']) : '';
    
    $fecha = FuncionesSamsung::getFechaUltimoRelevo($usuario, $codigo_pdv, $tipo_logistico);
    
    if ($fecha) {
        echo json_encode(array(
            'estado' => '1',
            'fecha_ultimo_relevo' => $fecha,
            'mensaje' => 'Fecha encontrada'
        ));
    } else {
        echo json_encode(array(
            'estado' => '2',
            'mensaje' => 'No hay relevos previos'
        ));
    }
    exit;
}

// =====  CATEGORÍA =====
$required = ['usuario', 'categoria', 'codigo_pdv'];
foreach ($required as $param) {
    if (!isset($body[$param]) || empty(trim($body[$param]))) {
        echo json_encode(array('estado' => '0', 'mensaje' => "Falta: $param"));
        exit;
    }
}

$usuario = trim($body['usuario']);
$categoria = trim($body['categoria']);
$codigo_pdv = trim($body['codigo_pdv']);
$tipo_logistico = isset($body['tipo_logistico']) ? trim($body['tipo_logistico']) : '';

// Obtener la última fecha de relevo ANTERIOR a hoy
$ultimaFecha = FuncionesSamsung::getFechaUltimoRelevo($usuario, $codigo_pdv, $tipo_logistico);

if (!$ultimaFecha) {
    echo json_encode(array(
        'estado' => '2', 
        'mensaje' => 'No hay relevos previos'
    ));
    exit;
}

// Obtener registros de la ÚLTIMA FECHA (anterior a hoy)
$registrosAnteriores = FuncionesSamsung::getRegistrosPorFecha($usuario, $categoria, $codigo_pdv, $ultimaFecha, $tipo_logistico);

// Obtener registros de HOY para comparar
$hoy = date('d/m/Y');
$registrosHoy = FuncionesSamsung::getRegistrosPorFecha($usuario, $categoria, $codigo_pdv, $hoy, $tipo_logistico);

// Crear mapa de cantidades de hoy
$cantidadesHoy = array();
if ($registrosHoy && !empty($registrosHoy)) {
    foreach ($registrosHoy as $reg) {
        $cantidadesHoy[$reg['sku_code']] = $reg['regular_price'];
    }
}

$resultado = array();
if ($registrosAnteriores && !empty($registrosAnteriores)) {
    foreach ($registrosAnteriores as $registro) {
        $sku = $registro['sku_code'];
        $registro['cantidad_anterior'] = $registro['regular_price']; // Cantidad del último relevo
        $registro['cantidad_actual'] = isset($cantidadesHoy[$sku]) ? $cantidadesHoy[$sku] : '0'; // Cantidad de hoy (0 si no hay)
        $resultado[] = $registro;
    }
}

if (empty($resultado)) {
    echo json_encode(array(
        'estado' => '2', 
        'mensaje' => 'No hay registros para esta categoría',
        'registros' => array()
    ));
} else {
    echo json_encode(array(
        'estado' => '1',
        'mensaje' => 'Registros: ' . count($resultado),
        'registros' => $resultado,
        'fecha_relevo' => $ultimaFecha,
        'fecha_hoy' => $hoy
    ));
}
exit;

?>