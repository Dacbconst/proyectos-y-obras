package com.luckyecuador.app.PintucoAPP.Conexion;

/**
 * Created by Lucky Ecuador on 29/06/2016.
 */
public class Constantes {

    /**
     * URLs del Web Service
     */

    private static final String URL = "https://webecuador.azurewebsites.net";

    private static final String IP = "https://webecuador-desarrollo.azurewebsites.net";

    //OBTENER TODOS LOS PUNTOVENTAS, PRODUCTOs
    public static final String GET_PUNTOVENTAS = URL + "/App/AppPintuco/Web/get_locales_rutero.php";
    public static final String GET_FLOORING = URL + "/App/AppPintuco/Web/get_productos2021.php";
    public static final String GET_PREGUNTAS = URL + "/App/AppPintuco/Web/get_preguntas.php";
    public static final String GET_TIEMPO_ALMUERZO = URL + "/App/AppPintuco/Web/get_tiempo_almuerzo.php";

    public static final String GET_JUSTIFICACION = URL + "/App/AppPintuco/Web/get_justificacion.php";

    public static final String GET_TESTS = URL + "/App/AppPintuco/Web/get_tests.php";

    public static final String GET_PRODUCTOS_PVC = URL + "/App/AppPintuco/Web/get_productos_pvc.php";

    public static final String GET_TIPO_EXH = URL + "/App/AppPintuco/Web/get_tipo_exhibiciones.php";
    public static final String GET_TIPO_PRECIOS = URL + "/App/AppPintuco/Web/get_tipo_precios.php";

    public static final String GET_EVIDENCIAS = URL + "/App/AppPintuco/Web/get_evidencias.php";

    public static final String GET_USER_DEVICE = URL + "/App/AppPintuco/Web/get_user_device.php";

    public static final String  UPDATE_USER_DEVICE = URL + "/App/AppPintuco/Inserts/update_device_id.php";

    public static final String GET_MARCAS_BLANCAS = URL + "/App/AppPintuco/Web/get_marcas_blancas.php";
    public static final String GET_PROMOCIONES = URL + "/App/AppPintuco/Web/get_promociones.php";
    public static final String GET_ROTACION = URL + "/App/AppPintuco/Web/get_rotacion.php";
    public static final String GET_TAREAS = URL + "/App/AppPintuco/Web/get_tareas.php";
    public static final String GET_POPSUGERIDO = URL + "/App/AppPintuco/Web/get_pop_sugerido.php";
    public static final String GET_PRIORITARIO = URL + "/App/AppPintuco/Web/get_prioritario.php";
    public static final String GET_COMBO_CANJES = URL + "/App/AppPintuco/Web/get_combos_canjes.php";
    public static final String GET_CAUSALES_MCI = URL + "/App/AppPintuco/Web/get_causales_mci.php";
    public static final String GET_CAUSALES_OSA = URL + "/App/AppPintuco/Web/get_causales_osa.php";
    public static final String GET_MATERIALES_ALERTAS = URL + "/App/AppPintuco/Web/get_materiales_alertas.php";
    public static final String GET_PDI = URL + "/App/AppPintuco/Web/get_pdi.php";
    public static final String GET_CAUSALES_PROD_MAL_EST = URL + "/App/AppPintuco/Web/get_causales_prod_mal_est.php";
    public static final String GET_VERSIONES = URL + "/App/AppPintuco/Web/get_versiones.php";
    public static final String GET_RANGOS_PRECIOS = URL + "/App/AppPintuco/Web/get_rangos_precios.php";
    public static final String GET_RANGOS_PRECIOS_SKU = URL + "/App/AppPintuco/Web/get_rangos_precios_sku.php";
    public static final String GET_ULTIMO_PVC = URL + "/App/AppPintuco/Web/get_ultimo_pvc.php";
    public static final String GET_ULTIMO_PRECIO_VENTAS = URL+ "/App/AppPintuco/Web/get_ultimo_precio_ventas.php";
    public static final String GET_ULTIMO_PVC_ANTERIOR_PROMO = URL + "/App/AppPintuco/Web/get_ultimo_pvc_anterior_promo.php";

    public static final String GET_CAUSALES_ASISTENCIA = URL + "/App/AppPintuco/Web/get_causales_asistencia.php";
    public static final String GET_CAUSALES_ASISTENCIA_ATRASO = URL + "/App/AppPintuco/Web/get_causales_asistencia_atraso.php";


    public static final String GET_METAS = URL + "/App/AppPintuco/Web/get_metas.php";
    public static final String GET_CONTACTOS = URL + "/App/AppPintuco/Web/get_contactos.php";
    public static final String GET_PDVS_CONTACTO = URL + "/App/AppPintuco/Web/get_pdvs_contacto.php";
    public static final String GET_PROFORMA = URL + "/App/AppPintuco/Web/get_proforma.php";
    public static final String GET_PAGO_FACTURA = URL + "/App/AppPintuco/Web/get_pago_factura.php";
    public static final String GET_CARACTERISTICAS_VISITA = URL + "/App/AppPintuco/Web/get_caracteristicas_visita.php";
    // GET con ?user=..., no POST con body JSON (mismo estilo que get_ivi_rutero.php).
    public static final String GET_USUARIO_KYWI = URL + "/App/AppPintuco/Web/get_usuario_kywi.php";
    // Prefijo del blob de Azure para fotos de Proforma/Factura ya subidas — insert_proforma.php
    // guarda solo "Proforma/archivo.png" o "Factura/archivo.png" en evidencia/foto_factura,
    // hay que anteponer esto para tener la URL completa y poder mostrarla (confirmado con el
    // usuario 2026-07-02: https://luckyecuadorweb.blob.core.windows.net/app/AppPintuco/Inserts/Proforma/...).
    public static final String PROFORMA_BLOB_BASE_URL = "https://luckyecuadorweb.blob.core.windows.net/app/AppPintuco/Inserts/";

    //INSERTS
    public static final String SEND_NOTIFICATION = URL + "/App/AppPintucoSupervision/Inserts/send_notificacion_almuerzo04_2025.php";
    public static final String INSERTAR_FLOORING = URL + "/App/AppPintuco/Inserts/insert_inventario012025.php";

    public static final String INSERTAR_ASISTENCIA = URL + "/App/AppPintuco/Inserts/insert_registro_asistencia.php";
    public static final String INSERTAR_PRECIO = URL + "/App/AppPintuco/Inserts/insert_precios072023.php";


    public static final String INSERTAR_EXH = URL + "/App/AppPintuco/Inserts/insert_exhibicion092023.php";
    public static final String INSERTAR_VENTA = URL + "/App/AppPintuco/Inserts/insert_venta.php";

    public static final String INSERTAR_PDV_FOTOGRAFICO = URL + "/App/AppPintuco/Inserts/insert_pdv_fotografico.php";

    public static final String INSERTAR_PROMO = URL + "/App/AppPintuco/Inserts/insert_promocion062023.php";

    public static final String INSERTAR_SHARE = URL + "/App/AppPintuco/Inserts/insert_share022023.php";

    public static final String INSERTAR_PDI = URL + "/App/AppPintuco/Inserts/insert_pdi022023.php";

    public static final String INSERTAR_VALORES = URL + "/App/AppPintuco/Inserts/insert_codificados022023.php";


    public static final String INSERTAR_GEO = URL + "/App/AppPintuco/Inserts/insert_rastreo.php";
    public static final String INSERTAR_INICIAL = URL + "/App/AppPintuco/Inserts/insert_inicial2018.php";
    public static final String INSERTAR_GPS = URL + "/App/AppPintuco/Inserts/insert_registro08_2022.php";
    public static final String INSERTAR_IMPLEM = URL + "/App/AppPintuco/Inserts/insert_implementacion.php";
    public static final String INSERTAR_AGOTADOS = URL + "/App/AppPintuco/Inserts/insert_agotados2018.php";
    public static final String INSERTAR_ROTACION = URL + "/App/AppPintuco/Inserts/insert_rotacion.php";
    public static final String INSERTAR_FOTOGRAFICO = URL + "/App/AppPintuco/Inserts/insert_fotografico.php";

    public static final String INSERTAR_EVIDENCIA = URL + "/App/AppPintuco/Inserts/insert_evidencias_03_2023.php";
    public static final String INSERTAR_PREGUNTAS = URL + "/App/AppPintuco/Inserts/insert_preguntas092023.php";

    public static final String INSERTAR_RESULTADO_PREGUNTAS = URL + "/App/AppPintuco/Inserts/insert_resultado_preguntas.php";
    public static final String INSERTAR_ALMUERZO = URL + "/App/AppPintuco/Inserts/insert_almuerzo04042025.php";
    public static final String INSERTAR_PROD_CAD = URL + "/App/AppPintuco/Inserts/insert_prod_caducar.php";
    public static final String INSERTAR_PACKS = URL + "/App/AppPintuco/Inserts/insert_packs.php";
    public static final String INSERTAR_IMPULSO = URL + "/App/AppPintuco/Inserts/insert_impulso022023.php";
    public static final String INSERTAR_TAREAS = URL + "/App/AppPintuco/Inserts/insert_tareas.php";
    public static final String INSERTAR_SUGERIDOS = URL + "/App/AppPintuco/Inserts/insert_sugeridos_2022.php";
    public static final String INSERTAR_CANJES = URL + "/App/AppPintuco/Inserts/insert_canjes_022023.php";
    public static final String INSERTAR_CONTACTO = URL + "/App/AppPintuco/Inserts/insert_contacto.php";
    public static final String INSERTAR_PROFORMA = URL  + "/App/AppPintuco/Inserts/insert_proforma.php";
    public static final String INSERTAR_PAGO_FACTURA = URL  + "/App/AppPintuco/Inserts/insert_pago_factura.php";
    public static final String DELETE_AGENDAMIENTO = URL  + "/App/AppPintuco/Inserts/delete_agendamiento.php";
    public static final String ELIMINAR_PROFORMA = URL  + "/App/AppPintuco/Inserts/eliminar_proforma.php";
    public static final String INSERTAR_MCI = URL + "/App/AppPintuco/Inserts/insert_mci.php";
    public static final String INSERTAR_MATERIALES_RECIBIDOS = URL + "/App/AppPintuco/Inserts/insert_materiales_recibidos.php";
    public static final String INSERTAR_EJECUCION_MATERIALES = URL + "/App/AppPintuco/Inserts/insert_ejecucion_materiales.php";
    public static final String INSERTAR_PROPENSOS_Y_PROD_MAL_ESTADO = URL + "/App/AppPintuco/Inserts/insert_propensos_y_prod_mal_estado072025.php";
    public static final String INSERTAR_MALLA_CODIFICADOS = URL + "/App/AppPintuco/Inserts/insert_malla_codificados.php";
    public static final String INSERTAR_NOTIFICACION = URL + "/App/AppPintuco/Inserts/insert_notificacion0622.php";
    public static final String INSERTAR_VENTAS = URL + "/App/AppPintuco/Inserts/insert_ventas012025.php";
    public static final String INSERTAR_LOG = URL + "/App/AppPintuco/Inserts/insert_log.php";
    public static final String INSERTAR_EVIDENCIAS = URL + "/App/AppPintuco/Inserts/insert_evidencias.php";
    public static final String INSERTAR_LOGISTICO_RELEVO = URL + "/App/AppPintuco/Inserts/insert_logistico_relevo.php";

    //MODIFICAR
    public static final String GET_PRECIOS = URL + "/App/CtaEpson/AppEpson/Web/epson_obtenerprecios.php";
    public static final String INSERTAR_PDV = URL + "/App/CtaEpson/AppEpson/Inserts/insert_noti.php";

    /**
     * Rastreo
     */
    public static final String ACTION_GPS_UPDATE = "android.intent.action.GPS_UPDATE";
    public static final String ACTION_PDV_LOCATION_UPDATE = "android.intent.action.PDV_LOCATION_UPDATE";
    public static final String LATITUD = "latitud";
    public static final String LONGITUD = "longitud";
    public static final String LATITUD_PDV_ACTUAL = "latitude_pdv";
    public static final String LONGITUDE_PDV_ACTUAL = "longitude_pdv";
    public static final String CODIGO_PDV_ACTUAL = "codigo_pdv_pdv";
    public static final String NOMBRE_PDV_ACTUAL = "nombre_pdv_pdv";
    public static final String PERIMETRO_PDV_ACTUAL = "perimetro_pdv_pdv";
    public static final String DISTANCE_PDV_ACTUAL = "distance_pdv_pdv";
    /**
     * Campos de las respuestas Json
     */
    public static final String MENSAJE = "mensaje";
    public static final String NODATA = "NoDisponible";
    public static final String URI = "uri";
    public static final String PHARMA_ID = "id";

    public static final String FECHA_SYNC = "FechaSync";
    public static final String HORA_SYNC = "HoraSync";
    public static final String SYNC = "SYNC";
    public static final String ACTUALIZADO = "NO";
    public static final String USER = "User";
    public static final String SUPERVISOR = "supervisor";
    public static final String AUTOTIME = "autotime";
    public static final String PDV = "zone";
    public static final String CODIGO = "pos_id";
    public static final String TIEMPO_BASE = "tiempo_base";
    public static final String TIEMPO_ESPERA = "tiempo_espera";
    public static final String MARCACION_ANTERIOR = "marcacion_anterior";
    public static final String TIEMPO_NOTIFICACION = "tiempo_notificacion";
    public static final String NOM_COMERCIAL = "canal";
    public static final String CANAL = "canal";
    public static final String SUBCANAL = "subcanal";
    public static final String FECHA = "fecha";
    public static final String HORA = "hora";
    public static final String IDPDV = "idpdv";
    public static final String TIPO = "tipo";
    public static final String INTERNO="interno";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String FORMAT = "format";
    public static final String VENDEDOR = "vendedor";
    public static final String CELULAR = "celular";
   public static final String ZONE = "zona";
    public static final String LITROS = "litros";
    public static final String DIAS_RESTANTES = "dias_restantes";
    public static final String VALORIZADO = "valorizado";
    public static final String FALTA_SALIDA ="falta_salida";
    public static final String HORA_ENTRADA ="hora_entrada";
    public static final String ACTIVIDAD ="activity";
    public static final String NUEVO ="nuevo";
    public static final String HORA_MARCACION ="hora_marcacion";
    public static final String FECHA_FALTA_SALIDA = "fecha_falta_salida";
    public static final String ID_RUTA ="id_ruta";
    public static final String DISTANCIA ="distancia";
    public static final String SHOW_PRIORITARIOS ="show_prioritarios";
    public static final String MODULO ="modulo";
    public static final String TIPO_MODULO ="tipo_modulo";
    public static final String MODULO_ACTUAL ="modulo_actual";
    public static final String MODULO_PUNTOS_PRINCIPAL ="modulo_puntos_principal";
    public static final String MODULO_PUNTOS_TARDIO ="modulo_puntos_tardio";

    public static final String ALMUERZO_EN_CURSO ="almuerzo_en_curso";
    public static final String NOTIF_ALMUERZO_FUERA ="notf_almuerzo_fuera";
    public static final String FUERO_TIEMPO_ACTIVO ="fuero_tiempo_activo";
    public static final String HORA_INICIO_ALMUERZO ="hora_inicio_almuerzo";
    public static final String FOTO_ALMUERZO ="foto_almuerzo";
    public static final String AUTO = "AUTOSERVICIO";
    public static final String MAYO = "MAYORISTA";
    public static final String DIST = "DISTRIBUIDOR";

    public static final String PDV_RESULT = "promotores";
    public static final String TIEMPO_ALMUERZO_RESULT ="tiempo_almuerzo";

    public static final String TESTS_RESULT = "tests";

    public static final String TIPO_EXH_RESULT = "tipo_exhibiciones";

    public static final String TIPO_MARCAS_RESULT = "marcas_blancas";

    public static final String TIPO_PRECIOS_RESULT = "tipo_precios";
    public static final String PRECIOS_RESULT = "precios";
    public static final String FLOORING_RESULT = "flooring";
    public static final String PREGUNTAS_RESULT = "preguntas";

    public static final String JUSTIFICACIONESRESULT="justificaciones";
    public static final String PRECIOS_PVC_RESULT = "productos_pvc";
    public static final String PROMOCIONES_RESULT = "promocion";
    public static final String ROTACION_RESULT = "rotacion";
    public static final String TAREAS_RESULT = "tareas";
    public static final String POPSUGERIDO_RESULT = "pop_sugerido";
    public static final String PRIORITARIO_RESULT = "prioritario";
    public static final String COMBO_CANJES_RESULT = "combo_canjes";
    public static final String CAUSALES_MCI_RESULT = "causales_mci";
    public static final String CAUSALES_OSA_RESULT = "causales_osa";
    public static final String MATERIALES_ALERTAS_RESULT = "materiales_alertas";
    public static final String CAUSALES_PROD_MAL_EST_RESULT="causales_prod_mal_est";
    public static final String VERSIONES_RESULT="versiones";
    public static final String PDI_RESULT = "pdi";
    public static final String RANGOS_PRECIOS_RESULT = "rango_precios";
    public static final String RANGOS_PRECIOS_SKU_RESULT = "rango_precios_sku";
    public static final String MARCA_PROPIA = "P";
    public static final String ULTIMO_PVC_RESULT = "productos_pvc";
    public static final String ULTIMO_PRECIO_VENTAS_RESULT = "productos_ventas";
    public static final String ULTIMO_PVC_ANTERIOR_PROMO_RESULT = "productos_promo";
    public static final String CAUSALES_ASISTENCIA_RESULT = "causales_asistencia";
    public static final String CAUSALES_ASISTENCIA_ATRASO_RESULT = "causales_asistencia_atraso";
    public static final String CONTACTOS_RESULT = "contactos";
    public static final String PROFORMA_RESULT = "proformas";
    public static final String PAGO_FACTURA_RESULT = "pagos_factura";
    public static final String CARACTERISTICAS_VISITA_RESULT = "caracteristicas";

    //recupera el id del ultimo registro insertado
    public static final String LAST_ID = "ultimoId";

    //CONSTANTES BAJADA
    public static final String bajar_Oper = "bajarOper";
    public static final String bajar_Oper_Check = "bajarOperCheck";
    public static final String bajar_Precios = "bajarDif";
    public static final String bajar_floo = "flooring";
    public static final String bajar_preg = "preguntas";

    public static final String bajar_tests = "tests";

    public static final String bajar_justificaciones="justificaciones";

    public static final String bajar_causales_prod_mal_est="causalesProdMalEst";

    public static final String bajar_causales_asistencia = "causales_asistencia";

    public static final String bajar_versiones="versiones";
    public static final String bajar_tiempo_almuerzo="tiempo_almuerzo";

    public static final String bajar_marcas_blancas="marcas_blancas";

    public static final String bajar_tipo_precios="tipo_precios";
    public static final String bajar_promociones = "promociones";
    public static final String bajar_rotacion = "rotacion";
    public static final String bajar_tareas = "tareas";
    public static final String bajar_pop_sugerido = "pop_sugerido";
    public static final String bajar_prioritario = "prioritario";
    public static final String bajar_combo_canjes = "combo_canjes";
    public static final String bajar_causales_mci = "causales_mci";
    public static final String bajar_causales_osa = "causales_osa";
    public static final String bajar_materiales_alertas = "materiales_alertas";
    public static final String bajar_pdi = "pdi";

    public static final String bajar_rangos_precios = "rangos_precios";
    public static final String bajar_rangos_precios_sku = "rangos_precios_sku";
    public static final String bajar_ultimo_pvc = "ultimo_pvc";
    public static final String bajar_ultimo_precio_ventas = "ultimo_precio_ventas";

    public static final String bajar_metas = "metas";

    public static final String bajar_precios_pvc = "precios_pvc";

    public static final String bajar_tipo_exhibicion = "tipo_exhibicion";
    public static final String bajar_causales_asistencia_atraso = "causales_asistencia_atraso";
    public static final String bajar_contactos = "contactos";
    public static final String bajar_proforma = "proforma";
    public static final String bajar_pago_factura = "pago_factura";

    //CONSTANTES SUBIDA
    public static final String SUBIR_TODO = "subir";
    public static final String insertAlmuerzo = "insertAlmuerzo";
    public static final String insertProyectosContacto = "insertProyectosContacto";
    public static final String insertProforma = "insertProforma";
    public static final String insertPagoFactura = "insertPagoFactura";
    public static final String insertPrecio = "insertPrecio";
    public static final String insertExh = "insertExh";
    public static final String insertVenta = "insertVenta";
    public static final String insertGps = "insertGps";
    public static final String insertAsistencia = "insertAsistencia";
    public static final String insertFlooring = "insertflooring";
    public static final String insertPropensosYProdMalEst = "insertPropensosYProdMalEst";
    public static final String insertinicial = "insertInicial";
    public static final String insertPromocion = "insertpromocion";
    public static final String insertPdvFotografico = "insertPdvFotografico";
    public static final String insertImplementacion = "insertimplementacion";
    public static final String insertvalores = "insertvalores";
    public static final String insertpdv = "insertpdv";
    public static final String insertShare = "insertshare";
    public static final String insertarPdI = "insertpdi";
    public static final String insertAgotados = "insertagotados";
    public static final String insertRotacion = "insertrotacion";
    public static final String insertFoto = "insertfoto";
    public static final String insertVentas = "insertVentas";
    public static final String insertPreguntas = "insertpreguntas";
    public static final String insertProdCad = "insertProdCad";

    public static final String insertResultadoPreguntas="insertResultadoPreguntas";
    public static final String insertPacks = "insertPacks";
    public static final String insertTareas = "insertTareas";
    public static final String insertSugeridos = "insertSugeridos";
    public static final String insertImpulso = "insertImpulso";
    public static final String insertGeo = "insertgeo";
    public static final String insertCanjes = "insertCanjes";
    public static final String insertMci = "insertMci";
    public static final String insertMaterialesRecibidos = "insertMaterialesRecibidos";
    public static final String insertEjecucionMateriales = "insertEjecucionMateriales";
    public static final String insertNot="insertNot";
    public static final String insertMallaCodificados="insertMallaCodificados";
    public static final String insertEvidencias="insertEvidencias";
    public static final String insertLogisticoRelevo="insertLogisticoRelevo";

    //SHARED PREFERENCES
    public static final String SHARED_PREFERENCES = "MisPreferencias";

    /**
     * Constantes Timeline
     */
    public static String SUN = "Domingo";
    public static String MON = "Lunes";
    public static String TUE = "Martes";
    public static String WED = "Miércoles";
    public static String THU = "Jueves";
    public static String FRI = "Viernes";
    public static String SAT = "Sábado";

    public static String JAN = "Enero";
    public static String FEB = "Febrero";
    public static String MAR = "Marzo";
    public static String APR = "Abril";
    public static String MAY = "Mayo";
    public static String JUN = "Junio";
    public static String JUL = "Julio";
    public static String AUG = "Agosto";
    public static String SEP = "Septiembre";
    public static String OCT = "Octubre";
    public static String NOV = "Noviembre";
    public static String DEC = "Diciembre";

    /**
     * Constantes Contracts
     */
    public static final int ESTADO_OK = 0;
    public static final int ESTADO_SYNC = 1;

    public static final String ESTADO = "estado";
    public static final String ID_REMOTA = "idRemota";
    public static final String ID_REMOTA_RUTA = "idRemotaRuta";
    public final static String PENDIENTE_INSERCION = "pendiente_insercion";





    /**
     * Códigos del campo {@link //ESTADO}
     */
    public static final String SUCCESS = "1";
    public static final String FAILED = "2";

    /**
     * Estados de la Agenda de Proyectos y Obras
     */
    public static final String ESTADO_AGENDA_PENDIENTE = "pendiente";
    public static final String ESTADO_AGENDA_CONFIRMADO = "confirmado";
    public static final String ESTADO_AGENDA_REAGENDADA = "reagendada";
    public static final String ESTADO_AGENDA_CANCELADA = "cancelada";
    public static final String ESTADO_AGENDA_VENCIDA = "vencida";
    public static final String ESTADO_AGENDA_COMPLETADA = "completada";

    /**
     * Estados del módulo Proforma (Gestión de Visitas) — campo bidireccional:
     * tanto la web como la app pueden cambiarlo.
     */
    public static final String ESTADO_PROFORMA_PENDIENTE = "pendiente";
    public static final String ESTADO_PROFORMA_EN_PROCESO = "en_proceso";
    public static final String ESTADO_PROFORMA_REALIZADO = "realizado";
    public static final String ESTADO_PROFORMA_EN_NEGOCIACION = "en_negociacion";
    public static final String ESTADO_PROFORMA_RECHAZADO = "rechazado";
    public static final String ESTADO_PROFORMA_APROBADO = "aprobado";
    /** El analista rechazó la foto por mala calidad; pide una nueva sin perder la ronda. */
    public static final String ESTADO_PROFORMA_CORRECCION_SOLICITADA = "correccion_solicitada";

    /**
     * Progreso de pago de la fila de factura (columna estado_pago) — la app es
     * la única que lo escribe (al confirmar la factura y al subir cada pago),
     * para que la web pueda filtrar sin contar filas de insert_pago_factura.
     */
    public static final String ESTADO_PAGO_PENDIENTE = "pendiente";
    public static final String ESTADO_PAGO_EN_PROCESO = "en_proceso";
    public static final String ESTADO_PAGO_COMPLETADO = "completado";
    /** Cierre manual antes de llegar al total cotizado (botón "Cierre Factura"). */
    public static final String ESTADO_PAGO_CERRADO = "cerrado";

    /**
     * Tipo de cuenta para la sincronización
     */
    public static final String ACCOUNT_TYPE = "com.luckyecuador.app.PintucoAPP.account";

    /**
     * Autoridad del Content Provider
     */
    public final static String AUTHORITY = "com.luckyecuador.app.PintucoAPP";
}
