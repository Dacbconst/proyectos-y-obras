package com.luckyecuador.app.PintucoAPP.DataBase;


import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractAlertas;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractAsistenciasLocal;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractCausalesAsistencia;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractCausalesAsistenciaAtraso;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractCausalesMCI;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractCausalesOSA;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractCausalesProdMalEst;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractComboCanjes;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertAlmuerzo;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertAsistencia;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertCanjes;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertEjecucionMateriales;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertEvidencias;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertLogisticoRelevo;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertMCIPdv;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertMallaCodificados;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertMaterialesRecibidos;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertPDI;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertProyectosContacto;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertPagoFactura;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertProforma;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertPdvFotografico;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertPropensosYProdMalEst;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertResultadoPreguntas;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertSugeridos;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertTareas;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertVentas;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractJustificacion;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractLog;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractMarcasBlancas;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractPDI;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractPopSugerido;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractPortafolioProductosAASS;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractPortafolioProductosMAYO;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractPreciosPvc;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractPrioritario;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractPromociones;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractRotacion;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertFotografico;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertImpulso;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertPacks;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertPreguntas;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertProdCaducar;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertRotacion;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertVenta;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractPortafolioProductos;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertAgotados;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertExh;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertGps;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertImplementacion;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertInicial;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertPdv;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertPrecios;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertPromocion;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertRastreo;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertShare;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertValores;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractNotificacion;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractPrecios;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractPharmaValue;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractPreguntas;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractRangosPrecios;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractRangosPreciosSku;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractTareas;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractTests;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractTiempoAlmuerzo;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractTipoExh;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractTipoPrecios;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractVersiones;
import com.luckyecuador.app.PintucoAPP.Contracts.InsertFlooring;

/**
 * Created by Lucky Ecuador on 24/11/2016.
 */

public class Projection {

    /**
     * Proyección para las consultas bajada
     */

    public static final String[] PROJECTION_PUNTOS = new String[]{
            ContractPharmaValue.Columnas._ID,
            Constantes.ID_REMOTA,
            ContractPharmaValue.Columnas.CHANNEL,
            ContractPharmaValue.Columnas.SUBCHANNEL,
            ContractPharmaValue.Columnas.CHANNEL_SEGMENT,
            ContractPharmaValue.Columnas.FORMAT,
            ContractPharmaValue.Columnas.CUSTOMER_OWNER,
            ContractPharmaValue.Columnas.POS_ID,
            ContractPharmaValue.Columnas.POS_NAME,
            ContractPharmaValue.Columnas.POS_NAME_DPSM,
            ContractPharmaValue.Columnas.ZONA,
            ContractPharmaValue.Columnas.REGION,
            ContractPharmaValue.Columnas.PROVINCIA,
            ContractPharmaValue.Columnas.CIUDAD,
            ContractPharmaValue.Columnas.DIRECCION,
            ContractPharmaValue.Columnas.KAM,
            ContractPharmaValue.Columnas.SALES_EXECUTIVE,
            ContractPharmaValue.Columnas.MERCHANDISING,
            ContractPharmaValue.Columnas.SUPERVISOR,
            ContractPharmaValue.Columnas.MERCADERISTA,
            ContractPharmaValue.Columnas.USER,
            ContractPharmaValue.Columnas.DPSM,
            ContractPharmaValue.Columnas.STATUS,
            ContractPharmaValue.Columnas.TIPO,
            ContractPharmaValue.Columnas.LATITUD,
            ContractPharmaValue.Columnas.LONGITUD,
            ContractPharmaValue.Columnas.FOTO,
            ContractPharmaValue.Columnas.SEGMENTACION,
            ContractPharmaValue.Columnas.COMPRAS,
            ContractPharmaValue.Columnas.PASS,
            ContractPharmaValue.Columnas.NUMERO_CONTROLLER,
            ContractPharmaValue.Columnas.FECHA_VISITA,
            ContractPharmaValue.Columnas.DEVICE_ID,
            ContractPharmaValue.Columnas.PERIMETRO,
            ContractPharmaValue.Columnas.DISTANCIA,
            ContractPharmaValue.Columnas.TERMOMETRO,
            ContractPharmaValue.Columnas.HORA_INICIO,
            ContractPharmaValue.Columnas.HORA_FIN
    };

    public static final String[] PROJECTION_PROMOCIONES = new String[]{
            ContractPromociones.Columnas._ID,
            Constantes.ID_REMOTA,
            ContractPromociones.Columnas.CANAL,
            ContractPromociones.Columnas.TIPO,
            ContractPromociones.Columnas.DESCRIPCION,
            ContractPromociones.Columnas.MECANICA
    };

    public static final String[] PROJECTION_COMBO_CANJES = new String[]{
            ContractComboCanjes.Columnas._ID,
            Constantes.ID_REMOTA,
            ContractComboCanjes.Columnas.TIPO_COMBO,
            ContractComboCanjes.Columnas.MECANICA
    };

    public static final String[] PROJECTION_CAUSALES_MCI = new String[]{
            ContractCausalesMCI.Columnas._ID,
            Constantes.ID_REMOTA,
            ContractCausalesMCI.Columnas.CAUSAL
    };

    public static final String[] PROJECTION_CAUSALES_OSA = new String[]{
            ContractCausalesOSA.Columnas._ID,
            Constantes.ID_REMOTA,
            ContractCausalesOSA.Columnas.CANAL,
            ContractCausalesOSA.Columnas.RESPONSABLE,
            ContractCausalesOSA.Columnas.CAUSAL
    };

    public static final String[] PROJECTION_MATERIALES_ALERTAS = new String[]{
            ContractAlertas.Columnas._ID,
            Constantes.ID_REMOTA,
            ContractAlertas.Columnas.TIPO_ALERTA,
            ContractAlertas.Columnas.CATEGORIA,
            ContractAlertas.Columnas.MATERIAL
    };

    public static final String[] PROJECTION_PDI = new String[]{
            ContractPDI.Columnas._ID,
            Constantes.ID_REMOTA,
            ContractPDI.Columnas.CANAL,
            ContractPDI.Columnas.CATEGORIA,
            ContractPDI.Columnas.SUBCATEGORIA,
            ContractPDI.Columnas.MARCA,
            ContractPDI.Columnas.OBJETIVO,
            ContractPDI.Columnas.PLATAFORMA
    };

    public static final String[] PROJECTION_POPSUGERIDO = new String[]{
            ContractPopSugerido.Columnas._ID,
            Constantes.ID_REMOTA,
            ContractPopSugerido.Columnas.CANAL,
            ContractPopSugerido.Columnas.CODIGO_PDV,
            ContractPopSugerido.Columnas.POP_SUGERIDO
    };

    public static final String[] PROJECTION_PRIORITARIO = new String[]{
            ContractPrioritario.Columnas._ID,
            Constantes.ID_REMOTA,
            ContractPrioritario.Columnas.CANAL,
            ContractPrioritario.Columnas.CODIGO_PDV,
            ContractPrioritario.Columnas.CATEGORIA,
            ContractPrioritario.Columnas.SUBCATEGORIA,
            ContractPrioritario.Columnas.MARCA,
            ContractPrioritario.Columnas.CONTENIDO,
            ContractPrioritario.Columnas.SKU
    };

    public static final String[] PROJECTION_ROTACION = new String[]{
            ContractRotacion.Columnas._ID,
            Constantes.ID_REMOTA,
            ContractRotacion.Columnas.CATEGORIA,
            ContractRotacion.Columnas.SUBCATEGORIA,
            ContractRotacion.Columnas.MARCA,
            ContractRotacion.Columnas.PRODUCTO,
            ContractRotacion.Columnas.PROMOCIONAL,
            ContractRotacion.Columnas.MECANICA,
            ContractRotacion.Columnas.PESO,
            ContractRotacion.Columnas.TIPO,
            ContractRotacion.Columnas.PLATAFORMA
    };

    public static final String[] PROJECTION_TAREAS = new String[]{
            ContractTareas.Columnas._ID,
            Constantes.ID_REMOTA,
            ContractTareas.Columnas.CANAL,
            ContractTareas.Columnas.CODIGOPDV,
            ContractTareas.Columnas.MERCADERISTA,
            ContractTareas.Columnas.TAREAS,
            ContractTareas.Columnas.PERIODO,
            ContractTareas.Columnas.FECHA_INGRESO
    };


    public static final String[] PROJECTION_JUSTIFICACION = new String[]{
            ContractJustificacion.Columnas._ID,
            Constantes.ID_REMOTA,
            ContractJustificacion.Columnas.JUSTIFICACION
    };

    public static final String[] PROJECTION_TIPO_PRECIOS = new String[]{
            ContractTipoPrecios.Columnas._ID,
            Constantes.ID_REMOTA,
            ContractTipoPrecios.Columnas.CANAL,
            ContractTipoPrecios.Columnas.SUBCANAL,
            ContractTipoPrecios.Columnas.TIPO_PRECIO
    };

    public static final String[] PROJECTION_MARCAS_BLANCAS = new String[]{
            ContractMarcasBlancas.Columnas._ID,
            Constantes.ID_REMOTA,
            ContractMarcasBlancas.Columnas.MARCAS
    };


    public static final String[] PROJECTION_PREGUNTAS = new String[]{
            ContractPreguntas.Columnas._ID,
            Constantes.ID_REMOTA,
            ContractPreguntas.Columnas.KEY_QUES,
            ContractPreguntas.Columnas.KEY_ANSWER,
            ContractPreguntas.Columnas.KEY_OPTA,
            ContractPreguntas.Columnas.KEY_OPTB,
            ContractPreguntas.Columnas.KEY_OPTC,
            ContractPreguntas.Columnas.KEY_CANAL,
            ContractPreguntas.Columnas.KEY_TIEMPO,
            ContractPreguntas.Columnas.KEY_TEST_ID
    };

    public static final String[] PROJECTION_TIPO_EXH = new String[]{
            ContractTipoExh.Columnas._ID,
            Constantes.ID_REMOTA,
            ContractTipoExh.Columnas.CANAL,
            ContractTipoExh.Columnas.EXHIBICION,
            ContractTipoExh.Columnas.TIPO,
            ContractTipoExh.Columnas.FOTO
    };

    public static final String[] PROJECTION_TIEMPO_ALMUERZO = new String[]{
            ContractTiempoAlmuerzo.Columnas._ID,
            Constantes.ID_REMOTA,
            ContractTiempoAlmuerzo.Columnas.TIEMPO_ALMUERZO
    };

    public static final String[] PROJECTION_ASISTENCIA_LOCAL = new String[]{
            ContractAsistenciasLocal.Columnas._ID,
            Constantes.ID_REMOTA,
            ContractAsistenciasLocal.Columnas.HORA,
            ContractAsistenciasLocal.Columnas.REGISTRADO,
            ContractAsistenciasLocal.Columnas.ESTADO_VALIDACION
    };


    public static final String[] PROJECTION_PRECIOS_PVC = new String[]{
            ContractPreciosPvc.Columnas._ID,
            Constantes.ID_REMOTA,
            ContractPreciosPvc.Columnas.USUARIO,
            ContractPreciosPvc.Columnas.FECHA,
            ContractPreciosPvc.Columnas.CODIGO,
            ContractPreciosPvc.Columnas.CANAL,
            ContractPreciosPvc.Columnas.CADENA,
            ContractPreciosPvc.Columnas.CATEGORIA,
            ContractPreciosPvc.Columnas.SUBCATEGORIA,
            ContractPreciosPvc.Columnas.MARCA,
            ContractPreciosPvc.Columnas.SKU,
            ContractPreciosPvc.Columnas.PVC
    };



    public static final String[] PROJECTION_TESTS = new String[]{
            ContractTests.Columnas.KEY_TEST_ID,
            Constantes.ID_REMOTA,
            ContractTests.Columnas.KEY_TEST,
            ContractTests.Columnas.KEY_DESCRIPTION,
            ContractTests.Columnas.KEY_DATE_START,
            ContractTests.Columnas.KEY_HOUR_START,
            ContractTests.Columnas.KEY_DATE_LIMIT,
            ContractTests.Columnas.KEY_HOUR_LIMIT,
            ContractTests.Columnas.KEY_ACTIVE
    };


    public static final String[] PROJECTION_INSERTALMUERZO = new String[]{
            ContractInsertAlmuerzo.Columnas._ID,
            Constantes.ID_REMOTA,
            ContractInsertAlmuerzo.Columnas.USUARIO,
            ContractInsertAlmuerzo.Columnas.JORNADA_LABORAL,
            ContractInsertAlmuerzo.Columnas.TIEMPO_ALMUERZO,
            ContractInsertAlmuerzo.Columnas.TIEMPO_FUERA,
            ContractInsertAlmuerzo.Columnas.FOTO,
            ContractInsertAlmuerzo.Columnas.LATITUD,
            ContractInsertAlmuerzo.Columnas.LONGITUD,
            ContractInsertAlmuerzo.Columnas.FECHA,
            ContractInsertAlmuerzo.Columnas.HORA_INI_ALMUERZO,
            ContractInsertAlmuerzo.Columnas.HORA_FIN_ALMUERZO
    };


    public static final String[] PROJECTION_INSERTPROYECTOSCONTACTO = new String[]{
            ContractInsertProyectosContacto.Columnas._ID,
            Constantes.ID_REMOTA,
            ContractInsertProyectosContacto.Columnas.CODIGO_PDV,
            ContractInsertProyectosContacto.Columnas.PDV,
            ContractInsertProyectosContacto.Columnas.USUARIO,
            ContractInsertProyectosContacto.Columnas.FECHA,
            ContractInsertProyectosContacto.Columnas.CONTACTO,
            ContractInsertProyectosContacto.Columnas.EMPRESA,
            ContractInsertProyectosContacto.Columnas.MAIL,
            ContractInsertProyectosContacto.Columnas.DIRECCION,
            ContractInsertProyectosContacto.Columnas.TELEFONO,
            ContractInsertProyectosContacto.Columnas.FECHA_AGENDAMIENTO,
            ContractInsertProyectosContacto.Columnas.TITULO,
            ContractInsertProyectosContacto.Columnas.HORA,
            ContractInsertProyectosContacto.Columnas.LUGAR,
            ContractInsertProyectosContacto.Columnas.LATITUD,
            ContractInsertProyectosContacto.Columnas.LONGITUD,
            ContractInsertProyectosContacto.Columnas.TECNICO,
            ContractInsertProyectosContacto.Columnas.ESTADO_AGENDA,
            ContractInsertProyectosContacto.Columnas.ACTIVAR,
            ContractInsertProyectosContacto.Columnas.TELEFONO_CONVENCIONAL,
            ContractInsertProyectosContacto.Columnas.REAGENDADO,
            ContractInsertProyectosContacto.Columnas.CIUDAD_PDV,
            ContractInsertProyectosContacto.Columnas.NO_REQUIERE_VISITA
    };

    public static final String[] PROJECTION_INSERTPROFORMA = new String[]{
            ContractInsertProforma.Columnas._ID,
            Constantes.ID_REMOTA,
            ContractInsertProforma.Columnas.ID_AGENDAMIENTO,
            ContractInsertProforma.Columnas.CODIGO_PDV,
            ContractInsertProforma.Columnas.USUARIO,
            ContractInsertProforma.Columnas.FECHA_PROFORMA,
            ContractInsertProforma.Columnas.ESTADO_PROFORMA,
            ContractInsertProforma.Columnas.EVIDENCIA,
            ContractInsertProforma.Columnas.CARACTERISTICA_VISITA,
            ContractInsertProforma.Columnas.ACOMPANAMIENTO_TECNICO,
            ContractInsertProforma.Columnas.FASE_ACTUAL,
            ContractInsertProforma.Columnas.FOTO_FACTURA,
            ContractInsertProforma.Columnas.FECHA_FACTURA,
            ContractInsertProforma.Columnas.MONTO_VALIDADO,
            ContractInsertProforma.Columnas.OBSERVACIONES_AUDITORIA,
            ContractInsertProforma.Columnas.FECHA_AUDITORIA,
            ContractInsertProforma.Columnas.MONTO_TOTAL_FACTURA,
            ContractInsertProforma.Columnas.PLAZO_MESES,
            ContractInsertProforma.Columnas.ESTADO_PAGO,
            ContractInsertProforma.Columnas.MOTIVO_CIERRE,
            ContractInsertProforma.Columnas.MOTIVO_CIERRE_PAGO
    };

    public static final String[] PROJECTION_INSERT_PAGO_FACTURA = new String[]{
            ContractInsertPagoFactura.Columnas._ID,
            Constantes.ID_REMOTA,
            ContractInsertPagoFactura.Columnas.ID_PROFORMA,
            ContractInsertPagoFactura.Columnas.ID_AGENDAMIENTO,
            ContractInsertPagoFactura.Columnas.CODIGO_PDV,
            ContractInsertPagoFactura.Columnas.USUARIO,
            ContractInsertPagoFactura.Columnas.NUMERO_CUOTA,
            ContractInsertPagoFactura.Columnas.MONTO_PAGO,
            ContractInsertPagoFactura.Columnas.FOTO_PAGO,
            ContractInsertPagoFactura.Columnas.FECHA_PAGO,
            ContractInsertPagoFactura.Columnas.OBSERVACION
    };




    public static final String[] PROJECTION_INSERT_PREGUNTAS = new String[]{
            ContractInsertPreguntas.Columnas._ID,
            Constantes.ID_REMOTA,
            ContractInsertPreguntas.Columnas.USUARIO,
            ContractInsertPreguntas.Columnas.TEST_ID,
            ContractInsertPreguntas.Columnas.TEST,
            ContractInsertPreguntas.Columnas.P1,
            ContractInsertPreguntas.Columnas.P2,
            ContractInsertPreguntas.Columnas.P3,
            ContractInsertPreguntas.Columnas.P4,
            ContractInsertPreguntas.Columnas.P5,
            ContractInsertPreguntas.Columnas.P6,
            ContractInsertPreguntas.Columnas.P7,
            ContractInsertPreguntas.Columnas.P8,
            ContractInsertPreguntas.Columnas.P9,
            ContractInsertPreguntas.Columnas.P10,
            ContractInsertPreguntas.Columnas.P11,
            ContractInsertPreguntas.Columnas.P12,
            ContractInsertPreguntas.Columnas.P13,
            ContractInsertPreguntas.Columnas.P14,
            ContractInsertPreguntas.Columnas.P15,
            ContractInsertPreguntas.Columnas.CORRECTAS,
            ContractInsertPreguntas.Columnas.INCORRECTAS,
            ContractInsertPreguntas.Columnas.CALIFICACION,
            ContractInsertPreguntas.Columnas.OBSERVACION,
            ContractInsertPreguntas.Columnas.FECHA,
            ContractInsertPreguntas.Columnas.HORA,
            ContractInsertPreguntas.Columnas.CRONOMETO,
            ContractInsertPreguntas.Columnas.ESTADO_TEST
    };

    public static final String[] PROJECTION_RESULTADO_PREGUNTAS = new String[]{
            ContractInsertResultadoPreguntas.Columnas._ID,
            Constantes.ID_REMOTA,
            ContractInsertResultadoPreguntas.Columnas.KEY_USUARIO,
            ContractInsertResultadoPreguntas.Columnas.KEY_TEST_ID,
            ContractInsertResultadoPreguntas.Columnas.KEY_TEST,
            ContractInsertResultadoPreguntas.Columnas.KEY_QUES,
            ContractInsertResultadoPreguntas.Columnas.KEY_OPTA,
            ContractInsertResultadoPreguntas.Columnas.KEY_OPTB,
            ContractInsertResultadoPreguntas.Columnas.KEY_OPTC,
            ContractInsertResultadoPreguntas.Columnas.KEY_ANSWER,
            ContractInsertResultadoPreguntas.Columnas.KEY_ANSWER_USER,
            ContractInsertResultadoPreguntas.Columnas.KEY_RESULT,
            ContractInsertResultadoPreguntas.Columnas.KEY_FECHA,
            ContractInsertResultadoPreguntas.Columnas.KEY_HORA
    };




    public static final String[] PROJECTION_INSERT_CANJES = new String[]{
            ContractInsertCanjes.Columnas._ID,
            Constantes.ID_REMOTA,
            ContractInsertCanjes.Columnas.PHARMA_ID,
            ContractInsertCanjes.Columnas.CODIGO,
            ContractInsertCanjes.Columnas.CANAL,
            ContractInsertCanjes.Columnas.NOMBRE_COMERCIAL,
            ContractInsertCanjes.Columnas.LOCAL,
            ContractInsertCanjes.Columnas.REGION,
            ContractInsertCanjes.Columnas.PROVINCIA,
            ContractInsertCanjes.Columnas.CIUDAD,
            ContractInsertCanjes.Columnas.ZONA,
            ContractInsertCanjes.Columnas.DIRECCION,
            ContractInsertCanjes.Columnas.SUPERVISOR,
            ContractInsertCanjes.Columnas.MERCADERISTA,
            ContractInsertCanjes.Columnas.USUARIO,
            ContractInsertCanjes.Columnas.LATITUD,
            ContractInsertCanjes.Columnas.LONGITUD,
            ContractInsertCanjes.Columnas.TERRITORIO,
            ContractInsertCanjes.Columnas.ZONA_TERRITORIO,
            ContractInsertCanjes.Columnas.CATEGORIA,
            ContractInsertCanjes.Columnas.SUBCATEGORIA,
            ContractInsertCanjes.Columnas.MARCA,
            ContractInsertCanjes.Columnas.PRODUCTO,
            ContractInsertCanjes.Columnas.TIPO_COMBO,
            ContractInsertCanjes.Columnas.MECANICA,
            ContractInsertCanjes.Columnas.COMBOS_ARMADOS,
            ContractInsertCanjes.Columnas.STOCK,
            ContractInsertCanjes.Columnas.PVC_COMBO,
            ContractInsertCanjes.Columnas.PVC_UNITARIO,
            ContractInsertCanjes.Columnas.VISITA,
            ContractInsertCanjes.Columnas.MES,
            ContractInsertCanjes.Columnas.OBSERVACIONES,
            ContractInsertCanjes.Columnas.FOTO,
            ContractInsertCanjes.Columnas.FOTO_GUIA,
            ContractInsertCanjes.Columnas.FECHA,
            ContractInsertCanjes.Columnas.HORA,
            ContractInsertCanjes.Columnas.POS_NAME,
            ContractInsertCanjes.Columnas.PLATAFORMA
    };

    public static final String[] PROJECTION_INSERT_MATERIALES_RECIBIDOS = new String[]{
            ContractInsertMaterialesRecibidos.Columnas._ID,
            Constantes.ID_REMOTA,
            ContractInsertMaterialesRecibidos.Columnas.PHARMA_ID,
            ContractInsertMaterialesRecibidos.Columnas.CODIGO,
            ContractInsertMaterialesRecibidos.Columnas.CANAL,
            ContractInsertMaterialesRecibidos.Columnas.NOMBRE_COMERCIAL,
            ContractInsertMaterialesRecibidos.Columnas.LOCAL,
            ContractInsertMaterialesRecibidos.Columnas.REGION,
            ContractInsertMaterialesRecibidos.Columnas.PROVINCIA,
            ContractInsertMaterialesRecibidos.Columnas.CIUDAD,
            ContractInsertMaterialesRecibidos.Columnas.ZONA,
            ContractInsertMaterialesRecibidos.Columnas.DIRECCION,
            ContractInsertMaterialesRecibidos.Columnas.SUPERVISOR,
            ContractInsertMaterialesRecibidos.Columnas.MERCADERISTA,
            ContractInsertMaterialesRecibidos.Columnas.USUARIO,
            ContractInsertMaterialesRecibidos.Columnas.LATITUD,
            ContractInsertMaterialesRecibidos.Columnas.LONGITUD,
            ContractInsertMaterialesRecibidos.Columnas.TERRITORIO,
            ContractInsertMaterialesRecibidos.Columnas.ZONA_TERRITORIO,
            ContractInsertMaterialesRecibidos.Columnas.ALERTA,
            ContractInsertMaterialesRecibidos.Columnas.TIPO,
            ContractInsertMaterialesRecibidos.Columnas.MATERIAL,
            ContractInsertMaterialesRecibidos.Columnas.CANTIDAD,
            ContractInsertMaterialesRecibidos.Columnas.ESTADO_MATERIAL,
            ContractInsertMaterialesRecibidos.Columnas.PRIORIDAD,
            ContractInsertMaterialesRecibidos.Columnas.OBSERVACIONES,
            ContractInsertMaterialesRecibidos.Columnas.FOTO,
            ContractInsertMaterialesRecibidos.Columnas.FECHA,
            ContractInsertMaterialesRecibidos.Columnas.HORA
    };

    public static final String[] PROJECTION_INSERT_EJECUCION_MATERIALES = new String[]{
            ContractInsertEjecucionMateriales.Columnas._ID,
            Constantes.ID_REMOTA,
            ContractInsertEjecucionMateriales.Columnas.PHARMA_ID,
            ContractInsertEjecucionMateriales.Columnas.CODIGO,
            ContractInsertEjecucionMateriales.Columnas.CANAL,
            ContractInsertEjecucionMateriales.Columnas.NOMBRE_COMERCIAL,
            ContractInsertEjecucionMateriales.Columnas.LOCAL,
            ContractInsertEjecucionMateriales.Columnas.REGION,
            ContractInsertEjecucionMateriales.Columnas.PROVINCIA,
            ContractInsertEjecucionMateriales.Columnas.CIUDAD,
            ContractInsertEjecucionMateriales.Columnas.ZONA,
            ContractInsertEjecucionMateriales.Columnas.DIRECCION,
            ContractInsertEjecucionMateriales.Columnas.SUPERVISOR,
            ContractInsertEjecucionMateriales.Columnas.MERCADERISTA,
            ContractInsertEjecucionMateriales.Columnas.USUARIO,
            ContractInsertEjecucionMateriales.Columnas.LATITUD,
            ContractInsertEjecucionMateriales.Columnas.LONGITUD,
            ContractInsertEjecucionMateriales.Columnas.TERRITORIO,
            ContractInsertEjecucionMateriales.Columnas.ZONA_TERRITORIO,
            ContractInsertEjecucionMateriales.Columnas.TIPO,
            ContractInsertEjecucionMateriales.Columnas.MATERIAL,
            ContractInsertEjecucionMateriales.Columnas.ESTADO_MATERIAL,
            ContractInsertEjecucionMateriales.Columnas.PRIORIDAD,
            ContractInsertEjecucionMateriales.Columnas.OBSERVACIONES,
            ContractInsertEjecucionMateriales.Columnas.FOTO,
            ContractInsertEjecucionMateriales.Columnas.FECHA,
            ContractInsertEjecucionMateriales.Columnas.HORA
    };

    public static final String[] PROJECTION_INSERT_EVIDENCIAS = new String[]{
            ContractInsertEvidencias.Columnas._ID,
            Constantes.ID_REMOTA,
            ContractInsertEvidencias.Columnas.PHARMA_ID,
            ContractInsertEvidencias.Columnas.CODIGO,
            ContractInsertEvidencias.Columnas.USUARIO,
            ContractInsertEvidencias.Columnas.CATEGORIA,
            ContractInsertEvidencias.Columnas.COMENTARIO,
            ContractInsertEvidencias.Columnas.FOTO_ANTES,
            ContractInsertEvidencias.Columnas.FOTO_DESPUES,
            ContractInsertEvidencias.Columnas.FECHA,
            ContractInsertEvidencias.Columnas.HORA
    };

    public static final String[] PROJECTION_INSERT_PDV_FOTOGRAFICO = new String[]{
            ContractInsertPdvFotografico.Columnas._ID,
            Constantes.ID_REMOTA,
            ContractInsertPdvFotografico.Columnas.PHARMA_ID,
            ContractInsertPdvFotografico.Columnas.CODIGO,
            ContractInsertPdvFotografico.Columnas.USUARIO,
            ContractInsertPdvFotografico.Columnas.SUPERVISOR,
            ContractInsertPdvFotografico.Columnas.FECHA,
            ContractInsertPdvFotografico.Columnas.HORA,
            ContractInsertPdvFotografico.Columnas.CANAL,
            ContractInsertPdvFotografico.Columnas.CODIGO_PDV,
            ContractInsertPdvFotografico.Columnas.MERCADERISTA,
            ContractInsertPdvFotografico.Columnas.FOTO
    };

    public static final String[] PROJECTION_INSERT_MCI = new String[]{
            ContractInsertMCIPdv.Columnas._ID,
            Constantes.ID_REMOTA,
            ContractInsertMCIPdv.Columnas.PHARMA_ID,
            ContractInsertMCIPdv.Columnas.CODIGO,
            ContractInsertMCIPdv.Columnas.CANAL,
            ContractInsertMCIPdv.Columnas.NOMBRE_COMERCIAL,
            ContractInsertMCIPdv.Columnas.LOCAL,
            ContractInsertMCIPdv.Columnas.REGION,
            ContractInsertMCIPdv.Columnas.PROVINCIA,
            ContractInsertMCIPdv.Columnas.CIUDAD,
            ContractInsertMCIPdv.Columnas.ZONA,
            ContractInsertMCIPdv.Columnas.DIRECCION,
            ContractInsertMCIPdv.Columnas.SUPERVISOR,
            ContractInsertMCIPdv.Columnas.MERCADERISTA,
            ContractInsertMCIPdv.Columnas.USUARIO,
            ContractInsertMCIPdv.Columnas.LATITUD,
            ContractInsertMCIPdv.Columnas.LONGITUD,
            ContractInsertMCIPdv.Columnas.TERRITORIO,
            ContractInsertMCIPdv.Columnas.ZONA_TERRITORIO,
            ContractInsertMCIPdv.Columnas.CAUSAL,
            ContractInsertMCIPdv.Columnas.OBSERVACIONES,
            ContractInsertMCIPdv.Columnas.FOTO,
            ContractInsertMCIPdv.Columnas.FECHA,
            ContractInsertMCIPdv.Columnas.HORA,
            ContractInsertMCIPdv.Columnas.POS_NAME
    };

    public static final String[] PROJECTION_PRECIOS = new String[]{
            ContractPrecios.Columnas._ID,
            Constantes.ID_REMOTA,
            ContractPrecios.Columnas.PRODUCTO,
            ContractPrecios.Columnas.SEGMENTO,
            ContractPrecios.Columnas.MARCA,
            ContractPrecios.Columnas.CATEGORIA,
            ContractPrecios.Columnas.SUBCATEGORIA
    };

    public static final String[] PROJECTION_VERSIONES = new String[]{
            ContractVersiones.Columnas._ID,
            Constantes.ID_REMOTA,
            ContractVersiones.Columnas.FECHA_VERSION,
            ContractVersiones.Columnas.LINK_APLICATIVO,
            ContractVersiones.Columnas.TAMAÑO

    };


    public static final String[] PROJECTION_CAUSALES_PROD_MAL_EST = new String[]{
            ContractCausalesProdMalEst.Columnas._ID,
            Constantes.ID_REMOTA,
            ContractCausalesProdMalEst.Columnas.CAUSAL,
            ContractCausalesProdMalEst.Columnas.CANTIDAD_DEFECTUOSA
    };
    public static final String[] PROJECTION_CAUSALES_ASISTENCIA = new String[]{
            ContractCausalesAsistencia.Columnas._ID,
            Constantes.ID_REMOTA,
            ContractCausalesAsistencia.Columnas.DESCRIPCION
    };

    public static final String[] PROJECTION_CAUSALES_ASISTENCIA_ATRASO = new String[]{
            ContractCausalesAsistenciaAtraso.Columnas._ID,
            Constantes.ID_REMOTA,
            ContractCausalesAsistenciaAtraso.Columnas.DESCRIPCION
    };


    public static final String[] PROJECTION_RANGOS_PRECIOS = new String[]{
            ContractRangosPrecios.Columnas._ID,
            Constantes.ID_REMOTA,
            ContractRangosPrecios.Columnas.PRESENTACION,
            ContractRangosPrecios.Columnas.MINIMO,
            ContractRangosPrecios.Columnas.MAXIMO,
    };


    public static final String[] PROJECTION_RANGOS_PRECIOS_SKU = new String[]{
            ContractRangosPreciosSku.Columnas._ID,
            Constantes.ID_REMOTA,
            ContractRangosPreciosSku.Columnas.SKU,
            ContractRangosPreciosSku.Columnas.MINIMO,
            ContractRangosPreciosSku.Columnas.MAXIMO,
    };



    public static final String[] PROJECTION_FLOORING = new String[]{
            ContractPortafolioProductos.Columnas._ID,
            Constantes.ID_REMOTA,
            //ContractPortafolioProductos.Columnas.CODIGO_PRODUCTO,
            ContractPortafolioProductos.Columnas.SECTOR,
            ContractPortafolioProductos.Columnas.CATEGORY,
            ContractPortafolioProductos.Columnas.SUBCATEGORIA,
            ContractPortafolioProductos.Columnas.SEGMENTO,
            ContractPortafolioProductos.Columnas.PRESENTACION,
            ContractPortafolioProductos.Columnas.VARIANTE1,
            ContractPortafolioProductos.Columnas.VARIANTE2,
            ContractPortafolioProductos.Columnas.CONTENIDO,
            ContractPortafolioProductos.Columnas.SKU,
            ContractPortafolioProductos.Columnas.MARCA,
            ContractPortafolioProductos.Columnas.FABRICANTE,
            ContractPortafolioProductos.Columnas.PVP,
            ContractPortafolioProductos.Columnas.CADENAS,
            ContractPortafolioProductos.Columnas.FOTO,
            ContractPortafolioProductos.Columnas.PLATAFORMA,
            ContractPortafolioProductos.Columnas.LOCALES,
            ContractPortafolioProductos.Columnas.DOLAR
            /*ContractPortafolioProductos.Columnas.MANUFACTURER,
            ContractPortafolioProductos.Columnas.FORMAT*/
    };

    public static final String[] PROJECTION_FLOORING_AASS = new String[]{
            ContractPortafolioProductosAASS.Columnas._ID,
            Constantes.ID_REMOTA,
            ContractPortafolioProductosAASS.Columnas.CATEGORIA,
            ContractPortafolioProductosAASS.Columnas.SUBCATEGORIA,
            ContractPortafolioProductosAASS.Columnas.MARCA,
            ContractPortafolioProductosAASS.Columnas.FABRICANTE,
            ContractPortafolioProductosAASS.Columnas.SKU,
            ContractPortafolioProductosAASS.Columnas.CADENAS
    };

    public static final String[] PROJECTION_FLOORING_MAYO = new String[]{
            ContractPortafolioProductosMAYO.Columnas._ID,
            Constantes.ID_REMOTA,
            ContractPortafolioProductosMAYO.Columnas.CODIGO,
            ContractPortafolioProductosMAYO.Columnas.USUARIO,
            ContractPortafolioProductosMAYO.Columnas.CATEGORIA,
            ContractPortafolioProductosMAYO.Columnas.SUBCATEGORIA,
            ContractPortafolioProductosMAYO.Columnas.MARCA,
            ContractPortafolioProductosMAYO.Columnas.FABRICANTE,
            ContractPortafolioProductosMAYO.Columnas.SKU,
            ContractPortafolioProductosMAYO.Columnas.STATUS
    };

    public static final String[] PROJECTION_INSERTPRECIO = new String[]{
            ContractInsertPrecios.Columnas._ID,
            Constantes.ID_REMOTA,
            ContractInsertPrecios.Columnas.PHARMA_ID,
            ContractInsertPrecios.Columnas.CODIGO,
            ContractInsertPrecios.Columnas.USUARIO,
            ContractInsertPrecios.Columnas.SUPERVISOR,
            ContractInsertPrecios.Columnas.FECHA,
            ContractInsertPrecios.Columnas.HORA,
            ContractInsertPrecios.Columnas.CATEGORIA,
            ContractInsertPrecios.Columnas.SUBCATEGORIA,
            ContractInsertPrecios.Columnas.PRESENTACION,
            ContractInsertPrecios.Columnas.POFERTA,
            ContractInsertPrecios.Columnas.BRAND,
            /*ContractInsertPrecios.Columnas.TAMANO,
            ContractInsertPrecios.Columnas.CANTIDAD,*/
            ContractInsertPrecios.Columnas.SKU_CODE,
            ContractInsertPrecios.Columnas.PREGULAR,
            ContractInsertPrecios.Columnas.PPROMOCION,
            ContractInsertPrecios.Columnas.MANUFACTURER,
            ContractInsertPrecios.Columnas.POS_NAME,
            ContractInsertPrecios.Columnas.PLATAFORMA,
            ContractInsertPrecios.Columnas.TIPO,
            ContractInsertPrecios.Columnas.PVM
    };

    public static final String[] PROJECTION_INSERT_PACKS = new String[]{
            ContractInsertPacks.Columnas._ID,
            Constantes.ID_REMOTA,
            ContractInsertPacks.Columnas.PHARMA_ID,
            ContractInsertPacks.Columnas.CODIGO,
            ContractInsertPacks.Columnas.USUARIO,
            ContractInsertPacks.Columnas.SUPERVISOR,
            ContractInsertPacks.Columnas.FECHA,
            ContractInsertPacks.Columnas.HORA,
            ContractInsertPacks.Columnas.CATEGORIA,
            ContractInsertPacks.Columnas.SUBCATEGORIA,
            ContractInsertPacks.Columnas.PRESENTACION,
            ContractInsertPacks.Columnas.BRAND,
            ContractInsertPacks.Columnas.SKU_CODE,
            ContractInsertPacks.Columnas.OBSERVACION,
            ContractInsertPacks.Columnas.FOTO,
            ContractInsertPacks.Columnas.MANUFACTURER
    };

    public static final String[] PROJECTION_INSERT_PROD_CADUCAR = new String[]{
            ContractInsertProdCaducar.Columnas._ID,
            Constantes.ID_REMOTA,
            ContractInsertProdCaducar.Columnas.PHARMA_ID,
            ContractInsertProdCaducar.Columnas.CODIGO,
            ContractInsertProdCaducar.Columnas.USUARIO,
            ContractInsertProdCaducar.Columnas.SUPERVISOR,
            ContractInsertProdCaducar.Columnas.FECHA,
            ContractInsertProdCaducar.Columnas.HORA,
            ContractInsertProdCaducar.Columnas.CATEGORIA,
            ContractInsertProdCaducar.Columnas.SUBCATEGORIA,
            ContractInsertProdCaducar.Columnas.BRAND,
            ContractInsertProdCaducar.Columnas.SKU_CODE,
            ContractInsertProdCaducar.Columnas.FECHA_PROD,
            ContractInsertProdCaducar.Columnas.CANTIDAD_PROD,
            ContractInsertProdCaducar.Columnas.MANUFACTURER
    };

    public static final String[] PROJECTION_INSERT_SUGERIDOS = new String[]{
            ContractInsertSugeridos.Columnas._ID,
            Constantes.ID_REMOTA,
            ContractInsertSugeridos.Columnas.PHARMA_ID,
            ContractInsertSugeridos.Columnas.CODIGO,
            ContractInsertSugeridos.Columnas.USUARIO,
            ContractInsertSugeridos.Columnas.SUPERVISOR,
            ContractInsertSugeridos.Columnas.FECHA,
            ContractInsertSugeridos.Columnas.HORA,
            ContractInsertSugeridos.Columnas.LOCAL,
            ContractInsertSugeridos.Columnas.CODIGO_FABRIL,
            ContractInsertSugeridos.Columnas.VENDEDOR_FABRIL,
            ContractInsertSugeridos.Columnas.CATEGORIA,
            ContractInsertSugeridos.Columnas.SUBCATEGORIA,
            ContractInsertSugeridos.Columnas.BRAND,
            ContractInsertSugeridos.Columnas.SKU_CODE,
            ContractInsertSugeridos.Columnas.QUIEBRE,
            ContractInsertSugeridos.Columnas.UNIDAD_DISPONIBLE,
            ContractInsertSugeridos.Columnas.SUGERIDO,
            ContractInsertSugeridos.Columnas.CANTIDAD,
            ContractInsertSugeridos.Columnas.OBSERVACIONES,
            ContractInsertSugeridos.Columnas.ENTREGA
    };


    public static final String[] PROJECTION_INSERT_TAREAS = new String[]{
            ContractInsertTareas.Columnas._ID,
            Constantes.ID_REMOTA,
            ContractInsertTareas.Columnas.PHARMA_ID,
            ContractInsertTareas.Columnas.CODIGO,
            ContractInsertTareas.Columnas.USUARIO,
            ContractInsertTareas.Columnas.SUPERVISOR,
            ContractInsertTareas.Columnas.FECHA,
            ContractInsertTareas.Columnas.HORA,
            ContractInsertTareas.Columnas.CHANNEL,
            ContractInsertTareas.Columnas.CODIGOPDV,
            ContractInsertTareas.Columnas.MERCADERISTA,
            ContractInsertTareas.Columnas.TAREAS,
            ContractInsertTareas.Columnas.REALIZADO,
            ContractInsertTareas.Columnas.FOTO
    };


    public static final String[] PROJECTION_INSERT_ROTACION= new String[]{
            ContractInsertRotacion.Columnas._ID,
            Constantes.ID_REMOTA,
            ContractInsertRotacion.Columnas.PHARMA_ID,
            ContractInsertRotacion.Columnas.CODIGO,
            ContractInsertRotacion.Columnas.USUARIO,
            ContractInsertRotacion.Columnas.SUPERVISOR,
            ContractInsertRotacion.Columnas.FECHA,
            ContractInsertRotacion.Columnas.HORA,
            ContractInsertRotacion.Columnas.CATEGORIA,
            ContractInsertRotacion.Columnas.PRODUCTO,
            ContractInsertRotacion.Columnas.PROMOCIONAL,
            ContractInsertRotacion.Columnas.MECANICA,
            ContractInsertRotacion.Columnas.PESO,
            ContractInsertRotacion.Columnas.CANTIDAD,
            ContractInsertRotacion.Columnas.FECHA_ROT,
            ContractInsertRotacion.Columnas.FOTO_GUIA,
            ContractInsertRotacion.Columnas.OBSERVACIONES
    };

    public static final String[] PROJECTION_INSERTVALORES = new String[]{
            ContractInsertValores.Columnas._ID,
            Constantes.ID_REMOTA,
            ContractInsertValores.Columnas.PHARMA_ID,
            ContractInsertValores.Columnas.CODIGO,
            ContractInsertValores.Columnas.USUARIO,
            ContractInsertValores.Columnas.SUPERVISOR,
            ContractInsertValores.Columnas.FECHA,
            ContractInsertValores.Columnas.HORA,
            ContractInsertValores.Columnas.SECTOR,
            ContractInsertValores.Columnas.CATEGORIA,
            ContractInsertValores.Columnas.SEGMENTO1,
            //ContractInsertValores.Columnas.POFERTA,
            ContractInsertValores.Columnas.BRAND,
            //ContractInsertValores.Columnas.TAMANO,
            //ContractInsertValores.Columnas.CANTIDAD,
            ContractInsertValores.Columnas.SKU_CODE,
            ContractInsertValores.Columnas.CODIFICA ,
            ContractInsertValores.Columnas.AUSENCIA,
            ContractInsertValores.Columnas.DISPONIBLE,
            ContractInsertValores.Columnas.RESPONSABLE,
            ContractInsertValores.Columnas.RAZONES,
            ContractInsertValores.Columnas.SUGERIDO,
            ContractInsertValores.Columnas.TIPO_SUGERIDO,
            ContractInsertValores.Columnas.PVP,
            ContractInsertValores.Columnas.PVC,
            ContractInsertValores.Columnas.POFERTA,
            ContractInsertValores.Columnas.MANUFACTURER,
            ContractInsertValores.Columnas.QUIEBRE_PERCHA,
            ContractInsertValores.Columnas.QUIEBRE_BODEGA,
            ContractInsertValores.Columnas.POS_NAME,
            ContractInsertValores.Columnas.PLATAFORMA
    };

    public static final String[] PROJECTION_INSERT_MALLA_CODIFICADOS = new String[]{
            ContractInsertMallaCodificados.Columnas._ID,
            Constantes.ID_REMOTA,
            ContractInsertMallaCodificados.Columnas.PHARMA_ID,
            ContractInsertMallaCodificados.Columnas.CODIGO,
            ContractInsertMallaCodificados.Columnas.USUARIO,
            ContractInsertMallaCodificados.Columnas.SUPERVISOR,
            ContractInsertMallaCodificados.Columnas.FECHA,
            ContractInsertMallaCodificados.Columnas.HORA,
            ContractInsertMallaCodificados.Columnas.SECTOR,
            ContractInsertMallaCodificados.Columnas.CATEGORIA,
            ContractInsertMallaCodificados.Columnas.SUBCATEGORIA,
            ContractInsertMallaCodificados.Columnas.SEGMENTO1,
            ContractInsertMallaCodificados.Columnas.BRAND,
            ContractInsertMallaCodificados.Columnas.SKU_CODE,
            ContractInsertMallaCodificados.Columnas.CODIFICA ,
            ContractInsertMallaCodificados.Columnas.OBSERVACION,
            ContractInsertMallaCodificados.Columnas.MANUFACTURER,
            ContractInsertMallaCodificados.Columnas.POS_NAME
    };

    public static final String[] PROJECTION_INSERTPROMO = new String[]{
            ContractInsertPromocion.Columnas._ID,
            Constantes.ID_REMOTA,
            ContractInsertPromocion.Columnas.PHARMA_ID,
            ContractInsertPromocion.Columnas.CODIGO,
            ContractInsertPromocion.Columnas.USUARIO,
            ContractInsertPromocion.Columnas.SUPERVISOR,
            ContractInsertPromocion.Columnas.FECHA,
            ContractInsertPromocion.Columnas.HORA,
            ContractInsertPromocion.Columnas.CATEGORIA,
            ContractInsertPromocion.Columnas.SUBCATEGORIA,
            ContractInsertPromocion.Columnas.MARCA,
            ContractInsertPromocion.Columnas.CANAL ,
            ContractInsertPromocion.Columnas.TIPO_PROMOCION ,
            ContractInsertPromocion.Columnas.DESCRIPCION_PROMOCION ,
            ContractInsertPromocion.Columnas.PRODUCTO ,
            ContractInsertPromocion.Columnas.MECANICA ,
            ContractInsertPromocion.Columnas.INI_PROMO,
            ContractInsertPromocion.Columnas.FIN_PROMO,
            ContractInsertPromocion.Columnas.AGOTAR_STOCK,
            ContractInsertPromocion.Columnas.PVC_ANTERIOR,
            ContractInsertPromocion.Columnas.PVC_ACTUAL,
            ContractInsertPromocion.Columnas.MARGEN_DSCTO,
            ContractInsertPromocion.Columnas.FOTO,
            ContractInsertPromocion.Columnas.MANUFACTURER,
            ContractInsertPromocion.Columnas.SKU,
            ContractInsertPromocion.Columnas.POS_NAME,
            ContractInsertPromocion.Columnas.PLATAFORMA
    };


    public static final String[] PROJECTION_INSERTIMPLEM = new String[]{
            ContractInsertImplementacion.Columnas._ID,
            Constantes.ID_REMOTA,
            ContractInsertImplementacion.Columnas.USUARIO,
            ContractInsertImplementacion.Columnas.FECHA,
            ContractInsertImplementacion.Columnas.HORA,
            ContractInsertImplementacion.Columnas.CIUDAD,
            ContractInsertImplementacion.Columnas.CANAL,
            ContractInsertImplementacion.Columnas.CLIENTE,
            ContractInsertImplementacion.Columnas.FORMATO,
            ContractInsertImplementacion.Columnas.ZONA,
            ContractInsertImplementacion.Columnas.PDV,
            ContractInsertImplementacion.Columnas.DIRECCION,
            ContractInsertImplementacion.Columnas.LOCAL,
            ContractInsertImplementacion.Columnas.LATITUD,
            ContractInsertImplementacion.Columnas.LONGITUD,
            ContractInsertImplementacion.Columnas.FOTO
    };

    public static final String[] PROJECTION_INSERTAGOTADOS = new String[]{
            ContractInsertAgotados.Columnas._ID,
            Constantes.ID_REMOTA,
            ContractInsertAgotados.Columnas.PHARMA_ID,
            ContractInsertAgotados.Columnas.CODIGO,
            ContractInsertAgotados.Columnas.USUARIO,
            ContractInsertAgotados.Columnas.SUPERVISOR,
            ContractInsertAgotados.Columnas.TIEMPO_INICIO,
            ContractInsertAgotados.Columnas.TIEMPO_FIN,
            ContractInsertAgotados.Columnas.FECHA,
            ContractInsertAgotados.Columnas.HORA
    };

    public static final String[] PROJECTION_INSERTVENTA = new String[]{
            ContractInsertVenta.Columnas._ID,
            Constantes.ID_REMOTA,
            ContractInsertVenta.Columnas.PHARMA_ID,
            ContractInsertVenta.Columnas.CODIGO,
            ContractInsertVenta.Columnas.USUARIO,
            ContractInsertVenta.Columnas.SUPERVISOR,
            ContractInsertVenta.Columnas.TIPO_FACTURA,
            ContractInsertVenta.Columnas.NUM_FACTURA,
            ContractInsertVenta.Columnas.MONTO_FACTURA,
            ContractInsertVenta.Columnas.FECHA_VENTA,
            ContractInsertVenta.Columnas.KEY_IMAGE,
            ContractInsertVenta.Columnas.FECHA,
            ContractInsertVenta.Columnas.HORA
    };

    public static final String[] PROJECTION_INSERTSHARE = new String[]{
            ContractInsertShare.Columnas._ID,
            Constantes.ID_REMOTA,
            ContractInsertShare.Columnas.PHARMA_ID,
            ContractInsertShare.Columnas.CODIGO,
            ContractInsertShare.Columnas.USUARIO,
            ContractInsertShare.Columnas.SUPERVISOR,
            ContractInsertShare.Columnas.FECHA,
            ContractInsertShare.Columnas.HORA,
            ContractInsertShare.Columnas.SECTOR,
            ContractInsertShare.Columnas.CATEGORIA,
            ContractInsertShare.Columnas.SEGMENTO,
            ContractInsertShare.Columnas.MARCA_SELECCIONADA,
            ContractInsertShare.Columnas.BRAND,
            ContractInsertShare.Columnas.CTMS_PERCHA,
            ContractInsertShare.Columnas.CTMS_MARCA,
            ContractInsertShare.Columnas.OTROS,
            ContractInsertShare.Columnas.MANUFACTURER,
            ContractInsertShare.Columnas.RAZONES,
            ContractInsertShare.Columnas.FOTO,
            ContractInsertShare.Columnas.POS_NAME,
            ContractInsertShare.Columnas.PLATAFORMA
          //  ContractInsertShare.Columnas.COMENTARIO
    };

    public static final String[] PROJECTION_INSERT_PDI = new String[]{
            ContractInsertPDI.Columnas._ID,
            Constantes.ID_REMOTA,
            ContractInsertPDI.Columnas.PHARMA_ID,
            ContractInsertPDI.Columnas.CODIGO,
            ContractInsertPDI.Columnas.USUARIO,
            ContractInsertPDI.Columnas.SUPERVISOR,
            ContractInsertPDI.Columnas.FECHA,
            ContractInsertPDI.Columnas.HORA,
            ContractInsertPDI.Columnas.CATEGORIA,
            ContractInsertPDI.Columnas.MARCA_SELECCIONADA,
            ContractInsertPDI.Columnas.CUMPLIMIENTO,
            ContractInsertPDI.Columnas.UNIVERSO,
            ContractInsertPDI.Columnas.CARAS,
            ContractInsertPDI.Columnas.OTROS,
            ContractInsertPDI.Columnas.OBJ_CATEGORIA,
            ContractInsertPDI.Columnas.PART_CATEGORIA,
            ContractInsertPDI.Columnas.FOTO,
            ContractInsertPDI.Columnas.CANAL,
            ContractInsertPDI.Columnas.POS_NAME,
            ContractInsertPDI.Columnas.PLATAFORMA
    };

    public static final String[] PROJECTION_INSERTPDV = new String[]{
            ContractInsertPdv.Columnas._ID,
            Constantes.ID_REMOTA,
            ContractInsertPdv.Columnas.IDPDV,
            ContractInsertPdv.Columnas.ESTADOVISITA,
            ContractInsertPdv.Columnas.NOVEDADES,
            ContractInsertPdv.Columnas.FOTO,
            ContractInsertPdv.Columnas.FECHA,
            ContractInsertPdv.Columnas.HORA
    };

    public static final String[] PROJECTION_INSERTEXH = new String[]{
            ContractInsertExh.Columnas._ID,
            Constantes.ID_REMOTA,
            ContractInsertExh.Columnas.PHARMA_ID,
            ContractInsertExh.Columnas.CODIGO,
            ContractInsertExh.Columnas.USUARIO,
            ContractInsertExh.Columnas.SUPERVISOR,
            ContractInsertExh.Columnas.FECHA,
            ContractInsertExh.Columnas.HORA,
            ContractInsertExh.Columnas.SECTOR,
            ContractInsertExh.Columnas.CATEGORIA,
            ContractInsertExh.Columnas.SUBCATEGORIA,
            ContractInsertExh.Columnas.SEGMENTO,
            ContractInsertExh.Columnas.BRAND,
            ContractInsertExh.Columnas.TIPO_EXH,
            ContractInsertExh.Columnas.ZONA_EX,
            ContractInsertExh.Columnas.NIVEL,
            ContractInsertExh.Columnas.TIPO,
            ContractInsertExh.Columnas.CONTRATADA,
            ContractInsertExh.Columnas.CONDICION,
            ContractInsertExh.Columnas.FOTO,
            ContractInsertExh.Columnas.POS_NAME,
            ContractInsertExh.Columnas.PLATAFORMA
    };

    public static final String[] PROJECTION_INSERTFOT = new String[]{
            ContractInsertFotografico.Columnas._ID,
            Constantes.ID_REMOTA,
            ContractInsertFotografico.Columnas.PHARMA_ID,
            ContractInsertFotografico.Columnas.CODIGO,
            ContractInsertFotografico.Columnas.USUARIO,
            ContractInsertFotografico.Columnas.CATEGORIA,
            ContractInsertFotografico.Columnas.SUBCATEGORIA,
            ContractInsertFotografico.Columnas.MARCA,
            ContractInsertFotografico.Columnas.LOGRO,
            ContractInsertFotografico.Columnas.KEY_IMAGE,
            ContractInsertFotografico.Columnas.FECHA,
            ContractInsertFotografico.Columnas.HORA
    };


    public static final String[] PROJECTION_INSERTFLOORING = new String[]{
            InsertFlooring.Columnas._ID,
            Constantes.ID_REMOTA,
            InsertFlooring.Columnas.PHARMA_ID,
            InsertFlooring.Columnas.CODIGO,
            InsertFlooring.Columnas.USUARIO,
            InsertFlooring.Columnas.SUPERVISOR,
            InsertFlooring.Columnas.FECHA,
            InsertFlooring.Columnas.HORA,
            InsertFlooring.Columnas.SECTOR,
            InsertFlooring.Columnas.CATEGORIA,
            InsertFlooring.Columnas.SUBCATEGORIA,
            InsertFlooring.Columnas.PRESENTACION,
            InsertFlooring.Columnas.BRAND,
            InsertFlooring.Columnas.CONTENIDO,
            InsertFlooring.Columnas.SKU_CODE,
            InsertFlooring.Columnas.INVENTARIOS,
            InsertFlooring.Columnas.SEMANA,
            InsertFlooring.Columnas.SUGERIDOS,
            InsertFlooring.Columnas.TIPO,
            InsertFlooring.Columnas.ENTREGA,
            InsertFlooring.Columnas.CAUSAL,
            InsertFlooring.Columnas.OTROS,
            InsertFlooring.Columnas.FECHA_CADUCIDAD,
            InsertFlooring.Columnas.POS_NAME,
            InsertFlooring.Columnas.PLATAFORMA,
            InsertFlooring.Columnas.FECHA_INVENTARIO
    };


    public static final String[] PROJECTION_INSERT_PROPENSOS_Y_PROD_MAL_EST = new String[]{
            ContractInsertPropensosYProdMalEst.Columnas._ID,
            Constantes.ID_REMOTA,
            ContractInsertPropensosYProdMalEst.Columnas.PHARMA_ID,
            ContractInsertPropensosYProdMalEst.Columnas.CODIGO,
            ContractInsertPropensosYProdMalEst.Columnas.USUARIO,
            ContractInsertPropensosYProdMalEst.Columnas.SUPERVISOR,
            ContractInsertPropensosYProdMalEst.Columnas.FECHA,
            ContractInsertPropensosYProdMalEst.Columnas.HORA,
            ContractInsertPropensosYProdMalEst.Columnas.SECTOR,
            ContractInsertPropensosYProdMalEst.Columnas.CATEGORIA,
            ContractInsertPropensosYProdMalEst.Columnas.SUBCATEGORIA,
            ContractInsertPropensosYProdMalEst.Columnas.PRESENTACION,
            ContractInsertPropensosYProdMalEst.Columnas.BRAND,
            ContractInsertPropensosYProdMalEst.Columnas.CONTENIDO,
            ContractInsertPropensosYProdMalEst.Columnas.SKU_CODE,
            ContractInsertPropensosYProdMalEst.Columnas.INVENTARIOS,
            ContractInsertPropensosYProdMalEst.Columnas.SOUVENIRS,
            ContractInsertPropensosYProdMalEst.Columnas.TIPO_CONTEO_TOTAL,
            ContractInsertPropensosYProdMalEst.Columnas.TOTAL,
            ContractInsertPropensosYProdMalEst.Columnas.TIPO_CONTEO_DEFECTUOSAS,
            ContractInsertPropensosYProdMalEst.Columnas.CANT_DEFECTUOSAS,
            ContractInsertPropensosYProdMalEst.Columnas.FECHA_CADUCIDAD_TOTAL,
            ContractInsertPropensosYProdMalEst.Columnas.FECHA_CADUCIDAD,
            ContractInsertPropensosYProdMalEst.Columnas.CAUSAL,
            ContractInsertPropensosYProdMalEst.Columnas.FOTO,
            ContractInsertPropensosYProdMalEst.Columnas.PLATAFORMA,
            ContractInsertPropensosYProdMalEst.Columnas.MODULO,
            ContractInsertPropensosYProdMalEst.Columnas.LITROS,
           ContractInsertPropensosYProdMalEst.Columnas.DIAS_RESTANTES,
           ContractInsertPropensosYProdMalEst.Columnas.VALORIZADO,
           ContractInsertPropensosYProdMalEst.Columnas.SKU_CLIENTE
    };


    public static final String[] PROJECTION_INSERT_IMPULSO = new String[]{
            ContractInsertImpulso.Columnas._ID,
            Constantes.ID_REMOTA,
            ContractInsertImpulso.Columnas.PHARMA_ID,
            ContractInsertImpulso.Columnas.CODIGO,
            ContractInsertImpulso.Columnas.USUARIO,
            ContractInsertImpulso.Columnas.SUPERVISOR,
            ContractInsertImpulso.Columnas.FECHA,
            ContractInsertImpulso.Columnas.HORA,
            ContractInsertImpulso.Columnas.CATEGORIA,
            ContractInsertImpulso.Columnas.BRAND,
            ContractInsertImpulso.Columnas.SKU_CODE,
            ContractInsertImpulso.Columnas.CANTIDAD_ASIGNADA,
            ContractInsertImpulso.Columnas.CANTIDAD_VENDIDA,
            ContractInsertImpulso.Columnas.CANTIDAD_ADICIONAL,
            ContractInsertImpulso.Columnas.CUMPLIMIENTO,
            ContractInsertImpulso.Columnas.IMPULSADORA,
            ContractInsertImpulso.Columnas.OBSERVACION,
            ContractInsertImpulso.Columnas.FOTO,
            ContractInsertImpulso.Columnas.POS_NAME,
            ContractInsertImpulso.Columnas.PRECIO_VENTA,
            ContractInsertImpulso.Columnas.ALERTA_STOCK,
            ContractInsertImpulso.Columnas.PLATAFORMA
    };


    public static final String[] PROJECTION_INSERT_VENTAS = new String[]{
            ContractInsertVentas.Columnas._ID,
            Constantes.ID_REMOTA,
            ContractInsertVentas.Columnas.PHARMA_ID,
            ContractInsertVentas.Columnas.CODIGO,
            ContractInsertVentas.Columnas.USUARIO,
            ContractInsertVentas.Columnas.SUPERVISOR,
            ContractInsertVentas.Columnas.FECHA,
            ContractInsertVentas.Columnas.HORA,
            ContractInsertVentas.Columnas.FECHA_VENTA,
            ContractInsertVentas.Columnas.CATEGORIA,
            ContractInsertVentas.Columnas.SUBCATEGORIA,
            ContractInsertVentas.Columnas.PRESENTACION,
            ContractInsertVentas.Columnas.MARCA,
            ContractInsertVentas.Columnas.SKU_CODE,
            ContractInsertVentas.Columnas.TIPO_VENTA,
            ContractInsertVentas.Columnas.STOCK_INICIAL,
            ContractInsertVentas.Columnas.CANTIDAD,
            ContractInsertVentas.Columnas.PREGULAR,
            ContractInsertVentas.Columnas.PPROMOCION,
            ContractInsertVentas.Columnas.POFERTA,
            ContractInsertVentas.Columnas.STOCK_FINAL,
            ContractInsertVentas.Columnas.MANUFACTURER,
            ContractInsertVentas.Columnas.POS_NAME,
            ContractInsertVentas.Columnas.FOTO
    };

    public static final String[] PROJECTION_INSERTINICIAL= new String[]{
            ContractInsertInicial.Columnas._ID,
            Constantes.ID_REMOTA,
            ContractInsertInicial.Columnas.IDPDV,
            ContractInsertInicial.Columnas.CODIGO,
            ContractInsertInicial.Columnas.TIPO,
            ContractInsertInicial.Columnas.DEALER,
            ContractInsertInicial.Columnas.UBICACION,
            ContractInsertInicial.Columnas.CORREO,
            ContractInsertInicial.Columnas.LATITUD,
            ContractInsertInicial.Columnas.LONGITUD,
            ContractInsertInicial.Columnas.FECHA,
            ContractInsertInicial.Columnas.HORA
    };

    public static final String[] PROJECTION_INSERT_GPS = new String[]{
            ContractInsertGps.Columnas._ID,
            Constantes.ID_REMOTA,
            ContractInsertGps.Columnas.IDPDV,
            ContractInsertGps.Columnas.USUARIO,
            ContractInsertGps.Columnas.TIPO,
            ContractInsertGps.Columnas.VERSION,
            ContractInsertGps.Columnas.LATITUDE,
            ContractInsertGps.Columnas.LONGITUDE,
            ContractInsertGps.Columnas.FECHA,
            ContractInsertGps.Columnas.HORA,
            ContractInsertGps.Columnas.CAUSAL,
            ContractInsertGps.Columnas.FOTO,
            ContractInsertGps.Columnas.DISTANCIA,
            ContractInsertGps.Columnas.TIPO_RELEVO,
            ContractInsertGps.Columnas.POS_NAME,
            Constantes.ID_REMOTA_RUTA
    };

    public static final String[] PROJECTION_INSERT_ASISTENCIA = new String[]{
            ContractInsertAsistencia.Columnas._ID,
            Constantes.ID_REMOTA,
            ContractInsertAsistencia.Columnas.IDPDV,
            ContractInsertAsistencia.Columnas.USUARIO,
            ContractInsertAsistencia.Columnas.FOTO,
            ContractInsertAsistencia.Columnas.VERSION,
            ContractInsertAsistencia.Columnas.LATITUDE,
            ContractInsertAsistencia.Columnas.LONGITUDE,
            ContractInsertAsistencia.Columnas.FECHA,
            ContractInsertAsistencia.Columnas.HORA,
            ContractInsertAsistencia.Columnas.DISTANCIA,
            ContractInsertAsistencia.Columnas.POS_NAME,
            ContractInsertAsistencia.Columnas.BATERIA,
            ContractInsertAsistencia.Columnas.ESTADO_ASISTENCIA,
            ContractInsertAsistencia.Columnas.SUPERVISOR
    };

    public static final String[] PROJECTION_INSERT_RASTREO = new String[]{
            ContractInsertRastreo.Columnas._ID,
            Constantes.ID_REMOTA,
            ContractInsertRastreo.Columnas.USUARIO,
            ContractInsertRastreo.Columnas.LATITUD,
            ContractInsertRastreo.Columnas.LONGITUD,
            ContractInsertRastreo.Columnas.FECHA,
            ContractInsertRastreo.Columnas.HORA
    };

    public static final String[] PROJECTION_INSERT_LOGISTICO_RELEVO = new String[]{
            ContractInsertLogisticoRelevo.Columnas._ID,
            Constantes.ID_REMOTA,
            ContractInsertLogisticoRelevo.Columnas.PHARMA_ID,
            ContractInsertLogisticoRelevo.Columnas.CODIGO,
            ContractInsertLogisticoRelevo.Columnas.USUARIO,

            ContractInsertLogisticoRelevo.Columnas.SUPERVISOR,
            ContractInsertLogisticoRelevo.Columnas.FECHA,
            ContractInsertLogisticoRelevo.Columnas.HORA,

            ContractInsertLogisticoRelevo.Columnas.CATEGORIA,
            ContractInsertLogisticoRelevo.Columnas.BRAND,
            ContractInsertLogisticoRelevo.Columnas.SKU_CODE,

            ContractInsertLogisticoRelevo.Columnas.PREGULAR,
            ContractInsertLogisticoRelevo.Columnas.CAUSAL,
            ContractInsertLogisticoRelevo.Columnas.TIPO_LOGISTICO,

            ContractInsertLogisticoRelevo.Columnas.FOTO,
            ContractInsertLogisticoRelevo.Columnas.COMENTARIO,
            ContractInsertLogisticoRelevo.Columnas.FECHA_PROD_CADUCADO,

            ContractInsertLogisticoRelevo.Columnas.FECHA_PROD_PROPENSO
    };

    public static final String[] PROJECTION_NOTIFICACION = new String[]{
            ContractNotificacion.Columnas._ID,
            Constantes.ID_REMOTA,
            ContractNotificacion.Columnas.USER,
            ContractNotificacion.Columnas.SUPERVISOR,
            ContractNotificacion.Columnas.DESCRIPCION,
            ContractNotificacion.Columnas.FECHA,
            ContractNotificacion.Columnas.HORA
    };

    public static final String[] PROJECTION_LOG = new String[]{
            ContractLog.Columnas._ID,
            Constantes.ID_REMOTA,
            ContractLog.Columnas.USUARIO,
            ContractLog.Columnas.FECHA,
            ContractLog.Columnas.HORA,
            ContractLog.Columnas.ACCION
    };


    /*
    *** Indices para las columnas indicadas en la proyección
     */

    public static final int COLUMNA_ID = 0;
    public static final int COLUMNA_ID_REMOTA = 1;

    //PUNTO DE VENTAS
    public final static int CHANNEL = 2;
    public final static int SUBCHANNEL = 3;
    public final static int CHANNEL_SEGMENT = 4;
    public final static int FORMAT = 5;
    public final static int CUSTOMER_OWNER = 6;
    public final static int POS_ID = 7;
    public final static int POS_NAME = 8;
    public final static int POS_NAME_DPSM = 9;
    public final static int ZONA = 10;
    public final static int REGION = 11;
    public final static int PROVINCIA = 12;
    public final static int CIUDAD = 13;
    public final static int DIRECCION = 14;
    public final static int KAM = 15;
    public final static int SALES_EXECUTIVE = 16;
    public final static int MERCHANDISING = 17;
    public final static int SUPERVISOR = 18;
    public final static int MERCADERISTA = 19;
    public final static int USUARIO = 20;
    public final static int DPSM = 21;
    public final static int STATUS = 22;
    public final static int TIPO_PDV = 23;
    public final static int LATITUD = 24;
    public final static int LONGITUD = 25;
    public final static int FOTO_PDV = 26;
    public final static int SEGMENTACION = 27;
    public final static int COMPRAS = 28;
    public final static int PASS = 29;
    public final static int NUMERO_CONTROLLER = 30;
    public final static int FECHA_VISITA = 31;
    public final static int DEVICE_ID = 32;
    public final static int PERIMETRO = 33;
    public final static int DISTANCIA = 34;
    public final static int TERMOMETRO = 35;
    public final static int HORA_INICIO = 36;
    public final static int HORA_FIN = 37;

    //PRECIOS
    public static final int COLUMNA_PRODUCTO = 2;
    public static final int COLUMNA_SEGMENTO = 3;
    public static final int COLUMNA_MARCA = 4;
    public static final int COLUMNA_CATEGORIA = 5;
    public static final int COLUMNA_SUBCATEGORIA = 6;


    // PRECIOS_PVC

    public static final int COLUMNA_USUARIO_PVC = 2;
    public static final int COLUMNA_FECHA_PVC = 3;
    public static final int COLUMNA_CANAL = 4;
    public static final int COLUMNA_CADENA = 5;
    public static final int COLUMNA_CODIGO_PVC = 6;
    public static final int COLUMNA_CATEGORIA_PVC = 7;
    public static final int COLUMNA_SUBCATEGORIA_PVC = 8;
    public static final int COLUMNA_MARCA_PVC = 9;
    public static final int COLUMNA_SKU_PVC = 10;
    public static final int COLUMNA_PVC = 11;


    //TIPO EXHIBICION
    public static final int CANAL_E = 2;
    public static final int EXHIBICION = 3;
    public static final int TIPO_E = 4;
    public static final int FOTO_E = 5;




    //TESTS
    public static final int TEST = 2;
    public static final int DESCRIPCION = 3;
    public static final int F_INICIO = 4;
    public static final int H_INICIO = 5;
    public static final int F_LIMITE = 6;
    public static final int H_LIMITE = 7;
    public static final int ACTIVE = 8;

    //PREGUNTAS
    public static final int QUESTION = 2;
    public static final int ANSWER = 3;
    public static final int OPTA = 4;
    public static final int OPTB = 5;
    public static final int OPTC = 6;
    public static final int QCANAL = 7;
    public static final int QTIEMPO = 8;
    public static final int QTEST_ID = 9;

    //PRODUCTOS
    //public final static int CODIGO_PRODUCTO= 2;
    public final static int SECTOR = 2;
    public final static int CATEGORY = 3;
    public final static int SUBCATEGORIA = 4;
    public final static int SEGMENTO = 5;
    public final static int PRESENTACION = 6;
    public final static int VARIANTE1 = 7;
    public final static int VARIANTE2 = 8;
    public final static int CONTENIDO = 9;
    public final static int SKU = 10;
    public final static int MARCA = 11;
    public final static int FABRICANTE = 12;
    public final static int PVP = 13;
    public final static int CADENAS = 14;
    public final static int FOTO = 15;
    public final static int PLATAFORMA = 16;
    public final static int LOCALES = 17;
    public final static int DOLAR = 18;

    //public final static int FORMAT_PRODUCTO= 14;

    //PROMO
    public final static int CANAL = 2;
    public final static int TIPO = 3;
    public final static int DESCRIP = 4;

    //PRODUCTOS_AASS
    public final static int CATEGORIA_AASS = 2;
    public final static int SUBCATEGORIA_AASS = 3;
    public final static int MARCA_AASS = 4;
    public final static int FABRICANTE_AASS = 5;
    public final static int SKU_AASS = 6;
    public final static int CADENAS_AASS = 7;

    //PRODUCTOS_MAYO
    public final static int CODIGO_MAYO = 2;
    public final static int USUARIO_MAYO = 3;
    public final static int CATEGORIA_MAYO = 4;
    public final static int SUBCATEGORIA_MAYO = 5;
    public final static int MARCA_MAYO = 6;
    public final static int FABRICANTE_MAYO = 7;
    public final static int SKU_MAYO = 8;
    public final static int STATUS_MAYO = 9;

    //ROTACION
    public final static int ROT_CATEGORIA = 2;
    public final static int ROT_SUBCATEGORIA = 3;
    public final static int ROT_MARCA = 4;
    public final static int ROT_PRODUCTO = 5;
    public final static int ROT_PROMOCIONAL = 6;
    public final static int ROT_MECANICA = 7;
    public final static int ROT_PESO = 8;
    public final static int ROT_TIPO = 9;
    public final static int ROT_PLATAFORMA = 10;

    //CAUSALES PRODUCTOS MAL ESTADO
    public final static int P_CAUSAL = 2;
    public final static int P_CANT_DEFECTUOSA = 3;

    //CAUSALES ASISTENCIA
    public final static int CAUSAL_ASISTENCIA_DESCRIPCION = 2;
    public final static int CAUSAL_ASISTENCIA_ATRASO_DESCRIPCION = 2;

    // Busca la sección de RANGOS PRECIOS
    public final static int R_PRESENTACION = 2;
    public final static int R_MINIMO = 3;
    public final static int R_MAXIMO = 4;

    // Busca la sección de RANGOS PRECIOS SKU
    public final static int RS_SKU = 2;
    public final static int RS_MINIMO = 3;
    public final static int RS_MAXIMO = 4;

    //VERSIONES
    public final static int FECHA_VERSION = 2;
    public final static int LINK_APLICATIVO = 3;
    public final static int TAMAÑO = 4;

    //POPSUGERIDO
    public static final int COLUMNA_CANAL_POP = 2;
    public static final int COLUMNA_CODIGO_PDV_POP = 3;
    public static final int COLUMNA_POP_SUGERIDO = 4;

    //PRIOTIRARIOS
    public static final int COLUMNA_CANAL_PRIORITARIOS = 2;
    public static final int COLUMNA_CODIGO_PDV_PRIORITARIOS = 3;
    public static final int COLUMNA_CATEGORIA_PRIORITARIOS = 4;
    public static final int COLUMNA_SUBCATEGORIA_PRIORITARIOS = 5;
    public static final int COLUMNA_MARCA_PRIORITARIOS = 6;
    public static final int COLUMNA_CONTENIDO_PRIORITARIOS = 7;
    public static final int COLUMNA_SKU_PRIORITARIOS = 8;

    //TAREAS
    public final static int CANALTAR = 2;
    public final static int CODIGOPDVTAR = 3;
    public final static int MERCADERISTATAR = 4;
    public final static int TAREAS = 5;
    public final static int PERIODO = 6;
    public final static int FECHA_INGRESOTAR = 7;

    //COMBO CANJES
    public final static int TIPO_COMBO = 2;
    public final static int MECANICA = 3;

    //CAUSAL MCI
    public final static int CAUSAL_MCI = 2;

    // JUSTIFICACION
    public static final int JUSTIFICACION = 2;

    // TIPO PRECIOS
    public static final int TIPO_PRECIO = 2;

    // MARCAS BLANCAS
    public static final int MARCAS = 2;

    //CAUSAL OSA
    public final static int CANAL_OSA = 2;
    public final static int RESPONSABLE_OSA = 3;
    public final static int CAUSAL_OSA = 4;

    //LINKS TIEMPO ALMUERZO
    public final static int TIEMPO_ALMUERZO = 2;
    public final static int URL_LINK = 3;

    //ALERTAS
    public final static int TIPO_ALERTA = 2;
    public final static int CATEGORIA_MATERIAL = 3;
    public final static int MATERIALES = 4;

    //PDI
    public final static int CANAL_PDI = 2;
    public final static int CATEGORIA_PDI = 3;
    public final static int SUBCATEGORIA_PDI = 4;
    public final static int MARCA_PDI = 5;
    public final static int OBJETIVO_PDI = 6;
    public final static int PLATAFORMA_PDI = 7;

}