package com.luckyecuador.app.PintucoAPP.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.luckyecuador.app.PintucoAPP.Clase.Asistencia;
import com.luckyecuador.app.PintucoAPP.Clase.BaseAlertas;
import com.luckyecuador.app.PintucoAPP.Clase.BasePharmaValue;
import com.luckyecuador.app.PintucoAPP.Clase.BasePortafolioProductos;
import com.luckyecuador.app.PintucoAPP.Clase.BasePreguntas;
import com.luckyecuador.app.PintucoAPP.Clase.BaseRangosPrecios;
import com.luckyecuador.app.PintucoAPP.Clase.BaseRangosPreciosSku;
import com.luckyecuador.app.PintucoAPP.Clase.Base_Inventario;
import com.luckyecuador.app.PintucoAPP.Clase.Base_pharma_value;
import com.luckyecuador.app.PintucoAPP.Clase.Base_portafolio_productos;
import com.luckyecuador.app.PintucoAPP.Clase.Base_precio_pvc;
import com.luckyecuador.app.PintucoAPP.Clase.Base_preguntas;
import com.luckyecuador.app.PintucoAPP.Clase.Base_tests;
import com.luckyecuador.app.PintucoAPP.Clase.Base_tipo_exh;
import com.luckyecuador.app.PintucoAPP.Clase.Base_versiones;
import com.luckyecuador.app.PintucoAPP.Clase.Flooring;
import com.luckyecuador.app.PintucoAPP.Clase.InsertExhibiciones;
import com.luckyecuador.app.PintucoAPP.Clase.InsertCanjes;
import com.luckyecuador.app.PintucoAPP.Clase.InsertOsa;
import com.luckyecuador.app.PintucoAPP.Clase.InsertPdi;
import com.luckyecuador.app.PintucoAPP.Clase.InsertSms;
import com.luckyecuador.app.PintucoAPP.Clase.LogUser;
import com.luckyecuador.app.PintucoAPP.Clase.Precio;
import com.luckyecuador.app.PintucoAPP.Clase.BaseTareas;
import com.luckyecuador.app.PintucoAPP.Clase.InsertValores;
import com.luckyecuador.app.PintucoAPP.Clase.BaseRotacion;
import com.luckyecuador.app.PintucoAPP.Clase.ShareElements;
import com.luckyecuador.app.PintucoAPP.Clase.Ventas;
import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractAlertas;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractAsistenciasLocal;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractCausalesAsistencia;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractCausalesAsistenciaAtraso;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractCausalesMCI;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractCausalesOSA;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractCausalesProdMalEst;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractComboCanjes;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractHoraMarcacion;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertAlmuerzo;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertProyectosContacto;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertPagoFactura;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertProforma;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertAsistencia;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertCanjes;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertEjecucionMateriales;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertEvidencias;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertLogisticoRelevo;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertMCIPdv;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertMallaCodificados;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertMaterialesRecibidos;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertPDI;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertPdvFotografico;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertPropensosYProdMalEst;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertResultadoPreguntas;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertSugeridos;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertTareas;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertVentas;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractJustificacion;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractLog;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractMarcasBlancas;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractMetas;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractPDI;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractPopSugerido;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractPortafolioProductosAASS;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractPortafolioProductosMAYO;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractPreciosPvc;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractPrioritario;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractPromociones;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertFotografico;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertImpulso;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertPacks;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertPreguntas;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertProdCaducar;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertVenta;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractPharmaValue;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractPortafolioProductos;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertAgotados;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertExh;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertGps;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertImplementacion;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertInicial;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertPdv;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertPrecios;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertPromocion;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertRotacion;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertRastreo;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertShare;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertValores;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractNotificacion;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractPrecios;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractPreguntas;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractRangosPrecios;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractRangosPreciosSku;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractRotacion;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractSesiones.ContractInsertExhSesion;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractSesiones.ContractInsertPreciosSesion;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractSesiones.ContractInsertValoresSesion;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractTareas;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractTests;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractTiempoAlmuerzo;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractTipoExh;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractTipoPrecios;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractVersiones;
import com.luckyecuador.app.PintucoAPP.Contracts.InsertFlooring;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Lucky Ecuador on 15/07/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    SQLiteDatabase db;

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        createTablePuntos(database); // Crear taba "login"
        createTablePrecios(database); // Crear taba "precios"
        createTableVersiones(database);
       // createTablePrecioPvc(database);
        createTableFlooring(database);
        createTableTests(database);
        createTableJustificacion(database);
        createTableTipoPrecios(database);
        createTableMarcasBlancas(database);
        createTablePrecioPvc(database);
        createTableTiempoAlmuerzo(database);
        createTableFlooringAASS(database);
        createTableFlooringMAYO(database);
        createTablePreguntas(database);
        createTableTipoExh(database);
        createTableAsistenciasLocal(database);
        createTableHoraMarcacion(database);
        insertTableHoraMarcacion(database);
        createTableInsertResultadoPreguntas(database);
        createTablePromociones(database);
        createTableRotacion(database);
        createTableTareas(database);
        createTableCausalesProdMalEst(database);
        createTableComboCanjes(database);
        createTableCausalesMCI(database);
        createTableCausalesOSA(database);
        createTableMaterialesAlertas(database);
        createTablePDI(database);
        createTableRangosPrecios(database);
        createTableRangosPreciosSku(database);
        createTableMetas(database);
        createTablePopSugerido(database);
        createTablePrioritario(database);
        createTableInsertPrecios(database);
        createTableInsertTareas(database);
        createTableInsertExh(database);
        createTableInsertGps(database);
        createTableInsertAsistencia(database);
        createTableInsertFotografico(database);
        createTableNotificacion(database);
        createTableInsertRastreo(database);
        createTableInsertPdvFotografico(database);
        createTableInsertFlooring(database);
        createTableInsertAlmuerzo(database);
        createTableInsertEvidencias(database);
        createTableInsertInicial(database);
        createTableInsertPromocion(database);
        createTableInsertImplementacion(database);
        createTableInsertValores(database);
        createTableInsertPDV(database);
        createTableInsertShare(database);
        createTableInsertPDI(database);
        createTableInsertAgotados(database);
        createTableInsertVenta(database);
        createTableInsertVentas(database);
        createTableInsertPreguntas(database);
        createTableInsertProdCaducar(database);
        createTableInsertSugeridos(database);
        createTableInsertRotacion(database);
        createTableInsertPacks(database);
        createTableInsertImpulso(database);
        createTableInsertCanjes(database);
        createTableInsertMaterialesRecibidos(database);
        createTableInsertEjecucionMateriales(database);
        createTableInsertMallaCodificados(database);
        createTableInsertMCIPDV(database);
        createTableInsertPropensosYProdMalEst(database);
        createTableInsertLogisticoRelevo(database);
        createTableInsertProyectosContacto(database);
        createTableInsertProforma(database);
        createTableInsertPagoFactura(database);
        createTableLog(database);
        //SESIONES
        createTableInsertPreciosSesiones(database);
        createTableInsertValoresSesiones(database);
        createTableInsertExhSesiones(database);
        //Causales Aaistencia
        createTableCausalesAsistencia(database);
        createTableCausalesAsistenciaAtraso(database);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.e("DATABASEHELPER", "Updating table from " + oldVersion + " to " + newVersion);
        try
        {
           /* if (newVersion > oldVersion) {
                db.execSQL(DATABASE_ALTER_PDV_1);
            }*/
        }
        catch (SQLiteException e) {
            Log.e("Upgrade",e.getMessage());
        }
        agregarColumnasAgendaProyectosContacto(db);
        createTableInsertProforma(db);
        createTableInsertPagoFactura(db);
        onCreate(db);
    }



    @Override
    public void onConfigure(SQLiteDatabase db) {
        db.setForeignKeyConstraintsEnabled(true);
    }

    /*
     *  CREAR REPOSITORIOS
     */
    private void createTablePuntos(SQLiteDatabase database) {
        String cmd = "CREATE TABLE " + ContractPharmaValue.POS + " (" +
                ContractPharmaValue.Columnas._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ContractPharmaValue.Columnas.CHANNEL + " TEXT, " +
                ContractPharmaValue.Columnas.SUBCHANNEL + " TEXT, " +
                ContractPharmaValue.Columnas.CHANNEL_SEGMENT + " TEXT, " +
                ContractPharmaValue.Columnas.FORMAT + " TEXT, " +
                ContractPharmaValue.Columnas.CUSTOMER_OWNER + " TEXT, " +
                ContractPharmaValue.Columnas.POS_ID + " TEXT, " +
                ContractPharmaValue.Columnas.POS_NAME + " TEXT, " +
                ContractPharmaValue.Columnas.POS_NAME_DPSM + " TEXT, " +
                ContractPharmaValue.Columnas.ZONA + " TEXT, " +
                ContractPharmaValue.Columnas.REGION + " TEXT, " +
                ContractPharmaValue.Columnas.PROVINCIA + " TEXT, " +
                ContractPharmaValue.Columnas.CIUDAD + " TEXT, " +
                ContractPharmaValue.Columnas.DIRECCION + " TEXT, " +
                ContractPharmaValue.Columnas.KAM + " TEXT, " +
                ContractPharmaValue.Columnas.SALES_EXECUTIVE + " TEXT, " +
                ContractPharmaValue.Columnas.MERCHANDISING + " TEXT, " +
                ContractPharmaValue.Columnas.SUPERVISOR + " TEXT, " +
                ContractPharmaValue.Columnas.MERCADERISTA + " TEXT, " +
                ContractPharmaValue.Columnas.USER + " TEXT, " +
                ContractPharmaValue.Columnas.DPSM + " TEXT, " +
                ContractPharmaValue.Columnas.STATUS + " TEXT, " +
                ContractPharmaValue.Columnas.TIPO + " TEXT, " +
                ContractPharmaValue.Columnas.LATITUD + " TEXT, " +
                ContractPharmaValue.Columnas.LONGITUD + " TEXT, " +
                ContractPharmaValue.Columnas.FOTO + " TEXT, " +
                ContractPharmaValue.Columnas.SEGMENTACION + " TEXT, " +
                ContractPharmaValue.Columnas.COMPRAS + " TEXT, " +
                ContractPharmaValue.Columnas.PASS + " TEXT, " +
                ContractPharmaValue.Columnas.NUMERO_CONTROLLER + " TEXT, " +
                ContractPharmaValue.Columnas.FECHA_VISITA + " TEXT, " +
                ContractPharmaValue.Columnas.DEVICE_ID + " TEXT, " +
                ContractPharmaValue.Columnas.PERIMETRO + " TEXT, " +
                ContractPharmaValue.Columnas.DISTANCIA + " TEXT, " +
                ContractPharmaValue.Columnas.TERMOMETRO + " TEXT, " +
                ContractPharmaValue.Columnas.HORA_INICIO + " TEXT, " +
                ContractPharmaValue.Columnas.HORA_FIN + " TEXT, " +
                Constantes.ID_REMOTA + " TEXT UNIQUE," +
                Constantes.ESTADO + " INTEGER NOT NULL DEFAULT "+ Constantes.ESTADO_OK +"," +
                Constantes.PENDIENTE_INSERCION + " INTEGER NOT NULL DEFAULT 0)";
        database.execSQL(cmd);
    }


    private void createTablePromociones(SQLiteDatabase database) {
        String cmd = "CREATE TABLE " + ContractPromociones.PROMOCIONES + " (" +
                ContractPromociones.Columnas._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ContractPromociones.Columnas.CANAL + " TEXT, " +
                ContractPromociones.Columnas.TIPO + " TEXT, " +
                ContractPromociones.Columnas.DESCRIPCION + " TEXT, " +
                ContractPromociones.Columnas.MECANICA + " TEXT, " +
                Constantes.ID_REMOTA + " TEXT UNIQUE," +
                Constantes.ESTADO + " INTEGER NOT NULL DEFAULT "+ Constantes.ESTADO_OK +"," +
                Constantes.PENDIENTE_INSERCION + " INTEGER NOT NULL DEFAULT 0)";
        database.execSQL(cmd);
    }

    private void createTableComboCanjes(SQLiteDatabase database) {
        String cmd = "CREATE TABLE " + ContractComboCanjes.COMBO_CANJES + " (" +
                ContractComboCanjes.Columnas._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ContractComboCanjes.Columnas.TIPO_COMBO + " TEXT, " +
                ContractComboCanjes.Columnas.MECANICA + " TEXT, " +
                Constantes.ID_REMOTA + " TEXT UNIQUE," +
                Constantes.ESTADO + " INTEGER NOT NULL DEFAULT "+ Constantes.ESTADO_OK +"," +
                Constantes.PENDIENTE_INSERCION + " INTEGER NOT NULL DEFAULT 0)";
        database.execSQL(cmd);
    }

    private void createTableCausalesMCI(SQLiteDatabase database) {
        String cmd = "CREATE TABLE " + ContractCausalesMCI.CAUSALES_MCI + " (" +
                ContractCausalesMCI.Columnas._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ContractCausalesMCI.Columnas.CAUSAL + " TEXT, " +
                Constantes.ID_REMOTA + " TEXT UNIQUE," +
                Constantes.ESTADO + " INTEGER NOT NULL DEFAULT "+ Constantes.ESTADO_OK +"," +
                Constantes.PENDIENTE_INSERCION + " INTEGER NOT NULL DEFAULT 0)";
        database.execSQL(cmd);
    }

    private void createTableTiempoAlmuerzo(SQLiteDatabase database){
        String cmd = "CREATE TABLE " + ContractTiempoAlmuerzo.REPO_TIEMPO_ALMUERZO + " (" +
                ContractTiempoAlmuerzo.Columnas._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ContractTiempoAlmuerzo.Columnas.TIEMPO_ALMUERZO + " TEXT, " +
                Constantes.ID_REMOTA + " TEXT UNIQUE," +
                Constantes.ESTADO + " INTEGER NOT NULL DEFAULT "+ Constantes.ESTADO_OK +"," +
                Constantes.PENDIENTE_INSERCION + " INTEGER NOT NULL DEFAULT 0)";
        database.execSQL(cmd);
    }

    private void createTableCausalesOSA(SQLiteDatabase database) {
        String cmd = "CREATE TABLE " + ContractCausalesOSA.CAUSALES_OSA + " (" +
                ContractCausalesOSA.Columnas._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ContractCausalesOSA.Columnas.CANAL + " TEXT, " +
                ContractCausalesOSA.Columnas.RESPONSABLE + " TEXT, " +
                ContractCausalesOSA.Columnas.CAUSAL + " TEXT, " +
                Constantes.ID_REMOTA + " TEXT UNIQUE," +
                Constantes.ESTADO + " INTEGER NOT NULL DEFAULT "+ Constantes.ESTADO_OK +"," +
                Constantes.PENDIENTE_INSERCION + " INTEGER NOT NULL DEFAULT 0)";
        database.execSQL(cmd);
    }

    private void createTableMaterialesAlertas(SQLiteDatabase database) {
        String cmd = "CREATE TABLE " + ContractAlertas.ALERTAS + " (" +
                ContractAlertas.Columnas._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ContractAlertas.Columnas.TIPO_ALERTA + " TEXT, " +
                ContractAlertas.Columnas.CATEGORIA + " TEXT, " +
                ContractAlertas.Columnas.MATERIAL + " TEXT, " +
                Constantes.ID_REMOTA + " TEXT UNIQUE," +
                Constantes.ESTADO + " INTEGER NOT NULL DEFAULT "+ Constantes.ESTADO_OK +"," +
                Constantes.PENDIENTE_INSERCION + " INTEGER NOT NULL DEFAULT 0)";
        database.execSQL(cmd);
    }

    private void createTablePDI(SQLiteDatabase database) {
        String cmd = "CREATE TABLE " + ContractPDI.PDI + " (" +
                ContractPDI.Columnas._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ContractPDI.Columnas.CANAL + " TEXT, " +
                ContractPDI.Columnas.CATEGORIA + " TEXT, " +
                ContractPDI.Columnas.SUBCATEGORIA + " TEXT, " +
                ContractPDI.Columnas.MARCA + " TEXT, " +
                ContractPDI.Columnas.OBJETIVO + " TEXT, " +
                ContractPDI.Columnas.PLATAFORMA + " TEXT, " +
                Constantes.ID_REMOTA + " TEXT UNIQUE," +
                Constantes.ESTADO + " INTEGER NOT NULL DEFAULT "+ Constantes.ESTADO_OK +"," +
                Constantes.PENDIENTE_INSERCION + " INTEGER NOT NULL DEFAULT 0)";
        database.execSQL(cmd);
    }

    private void createTableRangosPrecios(SQLiteDatabase database) {
        String cmd = "CREATE TABLE " + ContractRangosPrecios.RANGOS_PRECIOS + " (" +
                ContractRangosPrecios.Columnas._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ContractRangosPrecios.Columnas.PRESENTACION + " TEXT UNIQUE, " +
                ContractRangosPrecios.Columnas.MINIMO + " REAL, " +
                ContractRangosPrecios.Columnas.MAXIMO + " REAL, " +
                Constantes.ID_REMOTA + " TEXT UNIQUE)";
        database.execSQL(cmd);
    }

    private void createTableRangosPreciosSku(SQLiteDatabase database) {
        String cmd = "CREATE TABLE " + ContractRangosPreciosSku.RANGOS_PRECIOS_SKU + " (" +
                ContractRangosPreciosSku.Columnas._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ContractRangosPreciosSku.Columnas.SKU + " TEXT UNIQUE, " +
                ContractRangosPreciosSku.Columnas.MINIMO + " REAL, " +
                ContractRangosPreciosSku.Columnas.MAXIMO + " REAL, " +
                Constantes.ID_REMOTA + " TEXT UNIQUE)";
        database.execSQL(cmd);
    }

    private void createTableMetas(SQLiteDatabase database) {
        String cmd = "CREATE TABLE " + ContractMetas.METAS + " (" +
                ContractMetas.Columnas._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ContractMetas.Columnas.MODULO + " TEXT, " +
                ContractMetas.Columnas.USUARIO + " TEXT, " +
                ContractMetas.Columnas.CODIGO + " TEXT, " +
                ContractMetas.Columnas.CANAL + " TEXT, " +
                ContractMetas.Columnas.METAF + " TEXT, " +
                ContractMetas.Columnas.METAHC + " TEXT, " +
                ContractMetas.Columnas.FECHAINICIO + " TEXT, " +
                ContractMetas.Columnas.FECHAFIN + " TEXT, " +
                Constantes.ID_REMOTA + " TEXT UNIQUE," +
                Constantes.ESTADO + " INTEGER NOT NULL DEFAULT "+ Constantes.ESTADO_OK +"," +
                Constantes.PENDIENTE_INSERCION + " INTEGER NOT NULL DEFAULT 0)";
        database.execSQL(cmd);
    }

    private void createTableRotacion(SQLiteDatabase database) {
        String cmd = "CREATE TABLE " + ContractRotacion.ROTACION + " (" +
                ContractRotacion.Columnas._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ContractRotacion.Columnas.CATEGORIA + " TEXT, " +
                ContractRotacion.Columnas.SUBCATEGORIA + " TEXT, " +
                ContractRotacion.Columnas.MARCA + " TEXT, " +
                ContractRotacion.Columnas.PRODUCTO + " TEXT, " +
                ContractRotacion.Columnas.PROMOCIONAL + " TEXT, " +
                ContractRotacion.Columnas.MECANICA + " TEXT, " +
                ContractRotacion.Columnas.PESO + " TEXT, " +
                ContractRotacion.Columnas.TIPO + " TEXT, " +
                ContractRotacion.Columnas.PLATAFORMA + " TEXT, " +
                Constantes.ID_REMOTA + " TEXT UNIQUE," +
                Constantes.ESTADO + " INTEGER NOT NULL DEFAULT "+ Constantes.ESTADO_OK +"," +
                Constantes.PENDIENTE_INSERCION + " INTEGER NOT NULL DEFAULT 0)";
        database.execSQL(cmd);
    }

    private void createTableTareas(SQLiteDatabase database) {
        String cmd = "CREATE TABLE " + ContractTareas.TAREA + " (" +
                ContractTareas.Columnas._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ContractTareas.Columnas.CANAL + " TEXT, " +
                ContractTareas.Columnas.CODIGOPDV + " TEXT, " +
                ContractTareas.Columnas.MERCADERISTA + " TEXT, " +
                ContractTareas.Columnas.TAREAS + " TEXT, " +
                ContractTareas.Columnas.PERIODO + " TEXT, " +
                ContractTareas.Columnas.FECHA_INGRESO + " TEXT, " +
                Constantes.ID_REMOTA + " TEXT UNIQUE," +
                Constantes.ESTADO + " INTEGER NOT NULL DEFAULT "+ Constantes.ESTADO_OK +"," +
                Constantes.PENDIENTE_INSERCION + " INTEGER NOT NULL DEFAULT 0)";
        database.execSQL(cmd);
    }

    private void createTablePopSugerido(SQLiteDatabase database) {
        String cmd = "CREATE TABLE " + ContractPopSugerido.POPSUGERIDO + " (" +
                ContractPopSugerido.Columnas._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ContractPopSugerido.Columnas.CANAL + " TEXT, " +
                ContractPopSugerido.Columnas.CODIGO_PDV + " TEXT, " +
                ContractPopSugerido.Columnas.POP_SUGERIDO + " TEXT, " +
                Constantes.ID_REMOTA + " TEXT UNIQUE," +
                Constantes.ESTADO + " INTEGER NOT NULL DEFAULT "+ Constantes.ESTADO_OK +"," +
                Constantes.PENDIENTE_INSERCION + " INTEGER NOT NULL DEFAULT 0)";
        database.execSQL(cmd);
    }

    private void createTablePrioritario(SQLiteDatabase database) {
        String cmd = "CREATE TABLE " + ContractPrioritario.PRIORITARIO + " (" +
                ContractPrioritario.Columnas._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ContractPrioritario.Columnas.CANAL + " TEXT, " +
                ContractPrioritario.Columnas.CODIGO_PDV + " TEXT, " +
                ContractPrioritario.Columnas.CATEGORIA + " TEXT, " +
                ContractPrioritario.Columnas.SUBCATEGORIA + " TEXT, " +
                ContractPrioritario.Columnas.MARCA + " TEXT, " +
                ContractPrioritario.Columnas.CONTENIDO + " TEXT, " +
                ContractPrioritario.Columnas.SKU + " TEXT, " +
                Constantes.ID_REMOTA + " TEXT UNIQUE," +
                Constantes.ESTADO + " INTEGER NOT NULL DEFAULT "+ Constantes.ESTADO_OK +"," +
                Constantes.PENDIENTE_INSERCION + " INTEGER NOT NULL DEFAULT 0)";
        database.execSQL(cmd);
    }

    private void createTableCausalesProdMalEst(SQLiteDatabase database){
        String cmd = "CREATE TABLE " + ContractCausalesProdMalEst.CAUSALES_PROD_MAL_EST + " (" +
                ContractCausalesProdMalEst.Columnas._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ContractCausalesProdMalEst.Columnas.CAUSAL + " TEXT, " +
                ContractCausalesProdMalEst.Columnas.CANTIDAD_DEFECTUOSA + " TEXT, " +
                Constantes.ID_REMOTA + " TEXT UNIQUE," +
                Constantes.ESTADO + " INTEGER NOT NULL DEFAULT "+ Constantes.ESTADO_OK +"," +
                Constantes.PENDIENTE_INSERCION + " INTEGER NOT NULL DEFAULT 0)";
        database.execSQL(cmd);
    }


    private void createTableTests(SQLiteDatabase database){
        String cmd = "CREATE TABLE IF NOT EXISTS " + ContractTests.TABLE_TEST + " ( "
                + ContractTests.Columnas.KEY_TEST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ContractTests.Columnas.KEY_TEST + " TEXT, " +
                ContractTests.Columnas.KEY_DESCRIPTION + " TEXT, " +
                ContractTests.Columnas.KEY_DATE_START +" TEXT, " +
                ContractTests.Columnas.KEY_HOUR_START +" TEXT, " +
                ContractTests.Columnas.KEY_DATE_LIMIT +" TEXT, "+
                ContractTests.Columnas.KEY_HOUR_LIMIT +" TEXT, "+
                ContractTests.Columnas.KEY_ACTIVE + " TEXT, "+
                Constantes.ID_REMOTA + " TEXT UNIQUE," +
                Constantes.ESTADO + " INTEGER NOT NULL DEFAULT "+ Constantes.ESTADO_OK+"," +
                Constantes.PENDIENTE_INSERCION + " INTEGER NOT NULL DEFAULT 0)";
        database.execSQL(cmd);
    }


    private void createTableInsertResultadoPreguntas(SQLiteDatabase database) {
        String cmd = "CREATE TABLE IF NOT EXISTS " + ContractInsertResultadoPreguntas.INSERT_RESULTADO_PREGUNTAS + " ( "
                + ContractInsertResultadoPreguntas.Columnas._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ContractInsertResultadoPreguntas.Columnas.KEY_USUARIO + " TEXT, " +
                ContractInsertResultadoPreguntas.Columnas.KEY_TEST_ID + " TEXT, " +
                ContractInsertResultadoPreguntas.Columnas.KEY_TEST + " TEXT, " +
                ContractInsertResultadoPreguntas.Columnas.KEY_QUES + " TEXT, " +
                ContractInsertResultadoPreguntas.Columnas.KEY_OPTA + " TEXT, " +
                ContractInsertResultadoPreguntas.Columnas.KEY_OPTB + " TEXT, " +
                ContractInsertResultadoPreguntas.Columnas.KEY_OPTC + " TEXT, " +
                ContractInsertResultadoPreguntas.Columnas.KEY_ANSWER + " TEXT, " +
                ContractInsertResultadoPreguntas.Columnas.KEY_ANSWER_USER + " TEXT, " +
                ContractInsertResultadoPreguntas.Columnas.KEY_RESULT + " TEXT, " +
                ContractInsertResultadoPreguntas.Columnas.KEY_FECHA + " TEXT, " +
                ContractInsertResultadoPreguntas.Columnas.KEY_HORA + " TEXT, " +
                Constantes.ID_REMOTA + " TEXT UNIQUE," +
                Constantes.ESTADO + " INTEGER NOT NULL DEFAULT " + Constantes.ESTADO_OK + "," +
                Constantes.PENDIENTE_INSERCION + " INTEGER NOT NULL DEFAULT 0)";
        database.execSQL(cmd);
    }







    private void createTablePrecios(SQLiteDatabase database) {
        String cmd = "CREATE TABLE " + ContractPrecios.PRECIOS + " (" +
                ContractPrecios.Columnas._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ContractPrecios.Columnas.PRODUCTO + " TEXT, " +
                ContractPrecios.Columnas.SEGMENTO + " TEXT, " +
                ContractPrecios.Columnas.MARCA + " TEXT, " +
                ContractPrecios.Columnas.CATEGORIA + " TEXT, " +
                ContractPrecios.Columnas.SUBCATEGORIA + " TEXT, " +
                Constantes.ID_REMOTA + " TEXT UNIQUE," +
                Constantes.ESTADO + " INTEGER NOT NULL DEFAULT "+ Constantes.ESTADO_OK+"," +
                Constantes.PENDIENTE_INSERCION + " INTEGER NOT NULL DEFAULT 0)";
        database.execSQL(cmd);
    }

    private void createTableVersiones(SQLiteDatabase database) {
        String cmd = "CREATE TABLE " + ContractVersiones.VERSIONES + " (" +
                ContractVersiones.Columnas._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ContractVersiones.Columnas.FECHA_VERSION + " DATETIME, " +
                ContractVersiones.Columnas.LINK_APLICATIVO + " TEXT, " +
                ContractVersiones.Columnas.TAMAÑO + " TEXT, " +
                Constantes.ID_REMOTA + " TEXT UNIQUE," +
                Constantes.ESTADO + " INTEGER NOT NULL DEFAULT "+ Constantes.ESTADO_OK+"," +
                Constantes.PENDIENTE_INSERCION + " INTEGER NOT NULL DEFAULT 0)";
        database.execSQL(cmd);
    }

    private void createTableFlooring(SQLiteDatabase database) {
        String cmd = "CREATE TABLE " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " (" +
                ContractPortafolioProductos.Columnas._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                //ContractPortafolioProductos.Columnas.CODIGO_PRODUCTO + " TEXT, "+
                ContractPortafolioProductos.Columnas.SECTOR + " TEXT, "+
                ContractPortafolioProductos.Columnas.CATEGORY + " TEXT, "+
                ContractPortafolioProductos.Columnas.SUBCATEGORIA + " TEXT, "+
                ContractPortafolioProductos.Columnas.SEGMENTO + " TEXT, "+
                ContractPortafolioProductos.Columnas.PRESENTACION + " TEXT, "+
                ContractPortafolioProductos.Columnas.VARIANTE1 + " TEXT, "+
                ContractPortafolioProductos.Columnas.VARIANTE2 + " TEXT, "+
                ContractPortafolioProductos.Columnas.CONTENIDO + " TEXT, "+
                ContractPortafolioProductos.Columnas.SKU + " TEXT, "+
                ContractPortafolioProductos.Columnas.MARCA + " TEXT, "+
                ContractPortafolioProductos.Columnas.FABRICANTE + " TEXT, "+
                ContractPortafolioProductos.Columnas.PVP + " TEXT, "+
                ContractPortafolioProductos.Columnas.CADENAS + " TEXT, "+
                ContractPortafolioProductos.Columnas.FOTO + " TEXT, "+
                ContractPortafolioProductos.Columnas.PLATAFORMA + " TEXT, "+
                ContractPortafolioProductos.Columnas.LOCALES + " TEXT, "+
                ContractPortafolioProductos.Columnas.DOLAR + " TEXT, "+
                /*ContractPortafolioProductos.Columnas.MANUFACTURER + " TEXT, "+
                ContractPortafolioProductos.Columnas.FORMAT + " TEXT, "+*/
                Constantes.ID_REMOTA + " TEXT UNIQUE," +
                Constantes.ESTADO + " INTEGER NOT NULL DEFAULT "+ Constantes.ESTADO_OK+"," +
                Constantes.PENDIENTE_INSERCION + " INTEGER NOT NULL DEFAULT 0)";
        database.execSQL(cmd);
    }

    private void createTableFlooringAASS(SQLiteDatabase database) {
        String cmd = "CREATE TABLE " + ContractPortafolioProductosAASS.PORTAFOLIOPRODUCTOS_AASS + " (" +
                ContractPortafolioProductosAASS.Columnas._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ContractPortafolioProductosAASS.Columnas.CATEGORIA + " TEXT, "+
                ContractPortafolioProductosAASS.Columnas.SUBCATEGORIA + " TEXT, "+
                ContractPortafolioProductosAASS.Columnas.MARCA + " TEXT, "+
                ContractPortafolioProductosAASS.Columnas.FABRICANTE + " TEXT, "+
                ContractPortafolioProductosAASS.Columnas.SKU + " TEXT, "+
                ContractPortafolioProductosAASS.Columnas.CADENAS + " TEXT, "+
                Constantes.ID_REMOTA + " TEXT UNIQUE," +
                Constantes.ESTADO + " INTEGER NOT NULL DEFAULT "+ Constantes.ESTADO_OK+"," +
                Constantes.PENDIENTE_INSERCION + " INTEGER NOT NULL DEFAULT 0)";
        database.execSQL(cmd);
    }

    private void createTableFlooringMAYO(SQLiteDatabase database) {
        String cmd = "CREATE TABLE " + ContractPortafolioProductosMAYO.PORTAFOLIOPRODUCTOS_MAYO + " (" +
                ContractPortafolioProductosMAYO.Columnas._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ContractPortafolioProductosMAYO.Columnas.CODIGO + " TEXT, "+
                ContractPortafolioProductosMAYO.Columnas.USUARIO + " TEXT, "+
                ContractPortafolioProductosMAYO.Columnas.CATEGORIA + " TEXT, "+
                ContractPortafolioProductosMAYO.Columnas.SUBCATEGORIA + " TEXT, "+
                ContractPortafolioProductosMAYO.Columnas.MARCA + " TEXT, "+
                ContractPortafolioProductosMAYO.Columnas.FABRICANTE + " TEXT, "+
                ContractPortafolioProductosMAYO.Columnas.SKU + " TEXT, "+
                ContractPortafolioProductosMAYO.Columnas.STATUS + " TEXT, "+
                Constantes.ID_REMOTA + " TEXT UNIQUE," +
                Constantes.ESTADO + " INTEGER NOT NULL DEFAULT "+ Constantes.ESTADO_OK+"," +
                Constantes.PENDIENTE_INSERCION + " INTEGER NOT NULL DEFAULT 0)";
        database.execSQL(cmd);
    }

    private void createTablePreguntas(SQLiteDatabase database) {
        String cmd = "CREATE TABLE IF NOT EXISTS " + ContractPreguntas.TABLE_QUEST + " ( "
                + ContractPreguntas.Columnas._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ContractPreguntas.Columnas.KEY_QUES + " TEXT, " +
                ContractPreguntas.Columnas.KEY_ANSWER + " TEXT, " +
                ContractPreguntas.Columnas.KEY_OPTA +" TEXT, " +
                ContractPreguntas.Columnas.KEY_OPTB +" TEXT, "+
                ContractPreguntas.Columnas.KEY_OPTC + " TEXT, "+
                ContractPreguntas.Columnas.KEY_CANAL + " TEXT, "+
                ContractPreguntas.Columnas.KEY_TIEMPO + " TEXT, "+
                ContractPreguntas.Columnas.KEY_TEST_ID + " TEXT, "+
                Constantes.ID_REMOTA + " TEXT UNIQUE," +
                Constantes.ESTADO + " INTEGER NOT NULL DEFAULT "+ Constantes.ESTADO_OK+"," +
                Constantes.PENDIENTE_INSERCION + " INTEGER NOT NULL DEFAULT 0)";
        database.execSQL(cmd);
    }

    private void createTableTipoExh(SQLiteDatabase database) {
        String cmd = "CREATE TABLE IF NOT EXISTS " + ContractTipoExh.TABLE_NAME + " ( "
                + ContractTipoExh.Columnas._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ContractTipoExh.Columnas.CANAL + " TEXT, " +
                ContractTipoExh.Columnas.EXHIBICION + " TEXT, " +
                ContractTipoExh.Columnas.TIPO + " TEXT, " +
                ContractTipoExh.Columnas.FOTO +" TEXT, " +
                Constantes.ID_REMOTA + " TEXT UNIQUE," +
                Constantes.ESTADO + " INTEGER NOT NULL DEFAULT "+ Constantes.ESTADO_OK+"," +
                Constantes.PENDIENTE_INSERCION + " INTEGER NOT NULL DEFAULT 0)";
        database.execSQL(cmd);
    }

    private void createTableAsistenciasLocal(SQLiteDatabase database) {
        String cmd = "CREATE TABLE IF NOT EXISTS " + ContractAsistenciasLocal.TABLE_NAME + " ( "
                + ContractAsistenciasLocal.Columnas._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ContractAsistenciasLocal.Columnas.HORA + " TEXT, " +
                ContractAsistenciasLocal.Columnas.REGISTRADO + " TEXT, " +
                ContractAsistenciasLocal.Columnas.ESTADO_VALIDACION + " TEXT, " +
                Constantes.ID_REMOTA + " TEXT UNIQUE," +
                Constantes.ESTADO + " INTEGER NOT NULL DEFAULT "+ Constantes.ESTADO_OK+"," +
                Constantes.PENDIENTE_INSERCION + " INTEGER NOT NULL DEFAULT 0)";
        database.execSQL(cmd);
    }

    private void createTableHoraMarcacion(SQLiteDatabase database) {
        String cmd = "CREATE TABLE IF NOT EXISTS " + ContractHoraMarcacion.TABLE_NAME + " ( "
                + ContractHoraMarcacion.Columnas._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ContractHoraMarcacion.Columnas.HORA + " TEXT, " +
                Constantes.ID_REMOTA + " TEXT UNIQUE," +
                Constantes.ESTADO + " INTEGER NOT NULL DEFAULT "+ Constantes.ESTADO_OK+"," +
                Constantes.PENDIENTE_INSERCION + " INTEGER NOT NULL DEFAULT 0)";
        database.execSQL(cmd);
    }
    private void insertTableHoraMarcacion(SQLiteDatabase database) {
        String cmd = "INSERT INTO " + ContractHoraMarcacion.TABLE_NAME +
                " ( " + ContractHoraMarcacion.Columnas.HORA + " ) VALUES (0) ";
        database.execSQL(cmd);
    }

    private void createTableInsertPrecios(SQLiteDatabase database) {
        String cmd = "CREATE TABLE " + ContractInsertPrecios.INSERT_PRECIOS + " (" +
                ContractInsertPrecios.Columnas._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ContractInsertPrecios.Columnas.PHARMA_ID + " TEXT, " +
                ContractInsertPrecios.Columnas.CODIGO + " TEXT, " +
                ContractInsertPrecios.Columnas.USUARIO + " TEXT, " +
                ContractInsertPrecios.Columnas.SUPERVISOR + " TEXT, " +
                ContractInsertPrecios.Columnas.FECHA + " TEXT, " +
                ContractInsertPrecios.Columnas.HORA + " TEXT, " +
                ContractInsertPrecios.Columnas.CATEGORIA + " TEXT, " +
                ContractInsertPrecios.Columnas.SUBCATEGORIA + " TEXT, " +
                ContractInsertPrecios.Columnas.PRESENTACION + " TEXT, " +
                ContractInsertPrecios.Columnas.POFERTA + " TEXT, " +
                ContractInsertPrecios.Columnas.BRAND + " TEXT, " +
                /*ContractInsertPrecios.Columnas.TAMANO + " TEXT, " +
                ContractInsertPrecios.Columnas.CANTIDAD + " TEXT, " +*/
                ContractInsertPrecios.Columnas.SKU_CODE + " TEXT, " +
                ContractInsertPrecios.Columnas.PREGULAR + " TEXT, " +
                ContractInsertPrecios.Columnas.PPROMOCION + " TEXT, " +
                ContractInsertPrecios.Columnas.MANUFACTURER + " TEXT, " +
                ContractInsertPrecios.Columnas.POS_NAME + " TEXT, " +
                ContractInsertPrecios.Columnas.PLATAFORMA + " TEXT, " +
                ContractInsertPrecios.Columnas.TIPO + " TEXT, " +
                ContractInsertPrecios.Columnas.PVM + " TEXT, " +
                Constantes.ID_REMOTA + " TEXT UNIQUE," +
                Constantes.ESTADO + " INTEGER NOT NULL DEFAULT "+ Constantes.ESTADO_OK+"," +
                Constantes.PENDIENTE_INSERCION + " INTEGER NOT NULL DEFAULT 0)";
        database.execSQL(cmd);
    }


    private void createTableInsertAlmuerzo(SQLiteDatabase database){
        String cmd = "CREATE TABLE " + ContractInsertAlmuerzo.INSERT_ALMUERZO + " (" +
                ContractInsertAlmuerzo.Columnas._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ContractInsertAlmuerzo.Columnas.USUARIO + " TEXT, " +
                ContractInsertAlmuerzo.Columnas.JORNADA_LABORAL + " TEXT, " +
                ContractInsertAlmuerzo.Columnas.TIEMPO_ALMUERZO + " TEXT, " +
                ContractInsertAlmuerzo.Columnas.TIEMPO_FUERA + " TEXT, " +
                ContractInsertAlmuerzo.Columnas.FOTO + " TEXT, " +
                ContractInsertAlmuerzo.Columnas.LATITUD + " TEXT, " +
                ContractInsertAlmuerzo.Columnas.LONGITUD + " TEXT, " +
                ContractInsertAlmuerzo.Columnas.FECHA + " TEXT, " +
                ContractInsertAlmuerzo.Columnas.HORA_INI_ALMUERZO + " TEXT, " +
                ContractInsertAlmuerzo.Columnas.HORA_FIN_ALMUERZO + " TEXT, " +
                Constantes.ID_REMOTA + " TEXT UNIQUE," +
                Constantes.ESTADO + " INTEGER NOT NULL DEFAULT "+ Constantes.ESTADO_OK+"," +
                Constantes.PENDIENTE_INSERCION + " INTEGER NOT NULL DEFAULT 0)";
        database.execSQL(cmd);
    }

    private void createTableInsertProyectosContacto(SQLiteDatabase database){
        String cmd = "CREATE TABLE " + ContractInsertProyectosContacto.INSERT_PROYECTOS_CONTACTO + " (" +
                ContractInsertProyectosContacto.Columnas._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ContractInsertProyectosContacto.Columnas.CODIGO_PDV + " TEXT, " +
                ContractInsertProyectosContacto.Columnas.PDV + " TEXT, " +
                ContractInsertProyectosContacto.Columnas.CIUDAD_PDV + " TEXT, " +
                ContractInsertProyectosContacto.Columnas.USUARIO + " TEXT, " +
                ContractInsertProyectosContacto.Columnas.FECHA + " TEXT, " +
                ContractInsertProyectosContacto.Columnas.CONTACTO + " TEXT, " +
                ContractInsertProyectosContacto.Columnas.EMPRESA + " TEXT, " +
                ContractInsertProyectosContacto.Columnas.MAIL + " TEXT, " +
                ContractInsertProyectosContacto.Columnas.DIRECCION + " TEXT, " +
                ContractInsertProyectosContacto.Columnas.TELEFONO + " TEXT, " +
                ContractInsertProyectosContacto.Columnas.TELEFONO_CONVENCIONAL + " TEXT, " +
                ContractInsertProyectosContacto.Columnas.FECHA_AGENDAMIENTO + " TEXT, " +
                ContractInsertProyectosContacto.Columnas.TITULO + " TEXT, " +
                ContractInsertProyectosContacto.Columnas.HORA + " TEXT, " +
                ContractInsertProyectosContacto.Columnas.LUGAR + " TEXT, " +
                ContractInsertProyectosContacto.Columnas.LATITUD + " TEXT, " +
                ContractInsertProyectosContacto.Columnas.LONGITUD + " TEXT, " +
                ContractInsertProyectosContacto.Columnas.TECNICO + " TEXT, " +
                ContractInsertProyectosContacto.Columnas.ESTADO_AGENDA + " TEXT NOT NULL DEFAULT '" + Constantes.ESTADO_AGENDA_PENDIENTE + "', " +
                ContractInsertProyectosContacto.Columnas.ACTIVAR + " TEXT NOT NULL DEFAULT 'SI', " +
                ContractInsertProyectosContacto.Columnas.REAGENDADO + " TEXT NOT NULL DEFAULT 'NO', " +
                ContractInsertProyectosContacto.Columnas.NO_REQUIERE_VISITA + " TEXT NOT NULL DEFAULT 'NO', " +
                ContractInsertProyectosContacto.Columnas.FECHA_REGISTRO + " TEXT, " +
                Constantes.ID_REMOTA + " TEXT UNIQUE," +
                Constantes.ESTADO + " INTEGER NOT NULL DEFAULT "+ Constantes.ESTADO_OK+"," +
                Constantes.PENDIENTE_INSERCION + " INTEGER NOT NULL DEFAULT 0)";
        database.execSQL(cmd);
    }

    /**
     * Agrega las columnas de agenda a instalaciones que ya tenían la tabla de
     * Contacto creada sin ellas (commit 04f50d0). Cada ALTER va en su propio
     * try/catch porque SQLite no soporta "ADD COLUMN IF NOT EXISTS": si la
     * columna ya existe (instalación nueva, onCreate ya la trae) simplemente
     * se ignora ese error puntual y se sigue con las demás.
     */
    private void agregarColumnasAgendaProyectosContacto(SQLiteDatabase database) {
        String tabla = ContractInsertProyectosContacto.INSERT_PROYECTOS_CONTACTO;
        String[] alters = {
                "ALTER TABLE " + tabla + " ADD COLUMN " + ContractInsertProyectosContacto.Columnas.TITULO + " TEXT",
                "ALTER TABLE " + tabla + " ADD COLUMN " + ContractInsertProyectosContacto.Columnas.HORA + " TEXT",
                "ALTER TABLE " + tabla + " ADD COLUMN " + ContractInsertProyectosContacto.Columnas.LUGAR + " TEXT",
                "ALTER TABLE " + tabla + " ADD COLUMN " + ContractInsertProyectosContacto.Columnas.LATITUD + " TEXT",
                "ALTER TABLE " + tabla + " ADD COLUMN " + ContractInsertProyectosContacto.Columnas.LONGITUD + " TEXT",
                "ALTER TABLE " + tabla + " ADD COLUMN " + ContractInsertProyectosContacto.Columnas.TECNICO + " TEXT",
                "ALTER TABLE " + tabla + " ADD COLUMN " + ContractInsertProyectosContacto.Columnas.ESTADO_AGENDA
                        + " TEXT NOT NULL DEFAULT '" + Constantes.ESTADO_AGENDA_PENDIENTE + "'",
                "ALTER TABLE " + tabla + " ADD COLUMN " + ContractInsertProyectosContacto.Columnas.ACTIVAR + " TEXT NOT NULL DEFAULT 'SI'",
                "ALTER TABLE " + tabla + " ADD COLUMN " + ContractInsertProyectosContacto.Columnas.TELEFONO_CONVENCIONAL + " TEXT",
                "ALTER TABLE " + tabla + " ADD COLUMN " + ContractInsertProyectosContacto.Columnas.REAGENDADO + " TEXT NOT NULL DEFAULT 'NO'",
                "ALTER TABLE " + tabla + " ADD COLUMN " + ContractInsertProyectosContacto.Columnas.NO_REQUIERE_VISITA + " TEXT NOT NULL DEFAULT 'NO'",
                "ALTER TABLE " + tabla + " ADD COLUMN " + ContractInsertProyectosContacto.Columnas.FECHA_REGISTRO + " TEXT",
                "ALTER TABLE " + tabla + " ADD COLUMN " + ContractInsertProyectosContacto.Columnas.CIUDAD_PDV + " TEXT"
        };

        for (String alter : alters) {
            try {
                database.execSQL(alter);
            } catch (SQLiteException e) {
                Log.i("DATABASEHELPER", "Columna de agenda ya existia, se omite: " + e.getMessage());
            }
        }
    }

    /**
     * Una o varias filas por agendamiento (ID_AGENDAMIENTO referencia el
     * id_remota de insert_proyectos_contacto). IF NOT EXISTS porque también se
     * llama desde onUpgrade, que no debe fallar si la tabla ya existe.
     */
    private void createTableInsertProforma(SQLiteDatabase database) {
        String cmd = "CREATE TABLE IF NOT EXISTS " + ContractInsertProforma.INSERT_PROFORMA + " (" +
                ContractInsertProforma.Columnas._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ContractInsertProforma.Columnas.ID_AGENDAMIENTO + " TEXT, " +
                ContractInsertProforma.Columnas.CODIGO_PDV + " TEXT, " +
                ContractInsertProforma.Columnas.USUARIO + " TEXT, " +
                ContractInsertProforma.Columnas.FECHA_PROFORMA + " TEXT, " +
                ContractInsertProforma.Columnas.ESTADO_PROFORMA + " TEXT NOT NULL DEFAULT '" + Constantes.ESTADO_PROFORMA_PENDIENTE + "', " +
                ContractInsertProforma.Columnas.EVIDENCIA + " TEXT, " +
                ContractInsertProforma.Columnas.CARACTERISTICA_VISITA + " TEXT, " +
                ContractInsertProforma.Columnas.ACOMPANAMIENTO_TECNICO + " TEXT, " +
                ContractInsertProforma.Columnas.FASE_ACTUAL + " INTEGER NOT NULL DEFAULT 3, " +
                ContractInsertProforma.Columnas.FOTO_FACTURA + " TEXT, " +
                ContractInsertProforma.Columnas.FECHA_FACTURA + " TEXT, " +
                ContractInsertProforma.Columnas.MONTO_VALIDADO + " REAL, " +
                ContractInsertProforma.Columnas.OBSERVACIONES_AUDITORIA + " TEXT, " +
                ContractInsertProforma.Columnas.FECHA_AUDITORIA + " TEXT, " +
                ContractInsertProforma.Columnas.MONTO_TOTAL_FACTURA + " REAL, " +
                ContractInsertProforma.Columnas.PLAZO_MESES + " INTEGER, " +
                ContractInsertProforma.Columnas.ESTADO_PAGO + " TEXT, " +
                ContractInsertProforma.Columnas.MOTIVO_CIERRE + " TEXT, " +
                ContractInsertProforma.Columnas.MOTIVO_CIERRE_PAGO + " TEXT, " +
                Constantes.ID_REMOTA + " TEXT UNIQUE," +
                Constantes.ESTADO + " INTEGER NOT NULL DEFAULT " + Constantes.ESTADO_OK + "," +
                Constantes.PENDIENTE_INSERCION + " INTEGER NOT NULL DEFAULT 0)";
        database.execSQL(cmd);
        agregarColumnasProforma(database);
    }

    private void agregarColumnasProforma(SQLiteDatabase database) {
        String tabla = ContractInsertProforma.INSERT_PROFORMA;
        String[] alters = {
                "ALTER TABLE " + tabla + " ADD COLUMN " + ContractInsertProforma.Columnas.FASE_ACTUAL + " INTEGER NOT NULL DEFAULT 3",
                "ALTER TABLE " + tabla + " ADD COLUMN " + ContractInsertProforma.Columnas.FOTO_FACTURA + " TEXT",
                "ALTER TABLE " + tabla + " ADD COLUMN " + ContractInsertProforma.Columnas.FECHA_FACTURA + " TEXT",
                "ALTER TABLE " + tabla + " ADD COLUMN " + ContractInsertProforma.Columnas.MONTO_VALIDADO + " REAL",
                "ALTER TABLE " + tabla + " ADD COLUMN " + ContractInsertProforma.Columnas.OBSERVACIONES_AUDITORIA + " TEXT",
                "ALTER TABLE " + tabla + " ADD COLUMN " + ContractInsertProforma.Columnas.FECHA_AUDITORIA + " TEXT",
                "ALTER TABLE " + tabla + " ADD COLUMN " + ContractInsertProforma.Columnas.MONTO_TOTAL_FACTURA + " REAL",
                "ALTER TABLE " + tabla + " ADD COLUMN " + ContractInsertProforma.Columnas.PLAZO_MESES + " INTEGER",
                "ALTER TABLE " + tabla + " ADD COLUMN " + ContractInsertProforma.Columnas.ESTADO_PAGO + " TEXT",
                "ALTER TABLE " + tabla + " ADD COLUMN " + ContractInsertProforma.Columnas.MOTIVO_CIERRE + " TEXT",
                "ALTER TABLE " + tabla + " ADD COLUMN " + ContractInsertProforma.Columnas.MOTIVO_CIERRE_PAGO + " TEXT"
        };
        for (String alter : alters) {
            try {
                database.execSQL(alter);
            } catch (SQLiteException e) {
                Log.i("DATABASEHELPER", "Columna proforma ya existia: " + e.getMessage());
            }
        }
    }

    /**
     * Un pago/cuota por fila, de una factura a plazos (fila de insert_proforma
     * con plazo_meses > 0). IF NOT EXISTS porque también se llama desde
     * onUpgrade, que no debe fallar si la tabla ya existe.
     */
    private void createTableInsertPagoFactura(SQLiteDatabase database) {
        String cmd = "CREATE TABLE IF NOT EXISTS " + ContractInsertPagoFactura.INSERT_PAGO_FACTURA + " (" +
                ContractInsertPagoFactura.Columnas._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ContractInsertPagoFactura.Columnas.ID_PROFORMA + " TEXT, " +
                ContractInsertPagoFactura.Columnas.ID_AGENDAMIENTO + " TEXT, " +
                ContractInsertPagoFactura.Columnas.CODIGO_PDV + " TEXT, " +
                ContractInsertPagoFactura.Columnas.USUARIO + " TEXT, " +
                ContractInsertPagoFactura.Columnas.NUMERO_CUOTA + " INTEGER, " +
                ContractInsertPagoFactura.Columnas.MONTO_PAGO + " REAL, " +
                ContractInsertPagoFactura.Columnas.FOTO_PAGO + " TEXT, " +
                ContractInsertPagoFactura.Columnas.FECHA_PAGO + " TEXT, " +
                ContractInsertPagoFactura.Columnas.OBSERVACION + " TEXT, " +
                Constantes.ID_REMOTA + " TEXT UNIQUE," +
                Constantes.ESTADO + " INTEGER NOT NULL DEFAULT " + Constantes.ESTADO_OK + "," +
                Constantes.PENDIENTE_INSERCION + " INTEGER NOT NULL DEFAULT 0)";
        database.execSQL(cmd);
        agregarColumnasPagoFactura(database);
    }

    private void agregarColumnasPagoFactura(SQLiteDatabase database) {
        try {
            database.execSQL("ALTER TABLE " + ContractInsertPagoFactura.INSERT_PAGO_FACTURA
                    + " ADD COLUMN " + ContractInsertPagoFactura.Columnas.OBSERVACION + " TEXT");
        } catch (SQLiteException e) {
            Log.i("DATABASEHELPER", "Columna pago_factura ya existia: " + e.getMessage());
        }
    }

    private void createTableInsertMallaCodificados(SQLiteDatabase database){
        String cmd = "CREATE TABLE " + ContractInsertMallaCodificados.INSERT_MALLA_CODIFICADOS + " (" +
                ContractInsertMallaCodificados.Columnas._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ContractInsertMallaCodificados.Columnas.PHARMA_ID + " TEXT, " +
                ContractInsertMallaCodificados.Columnas.CODIGO + " TEXT, " +
                ContractInsertMallaCodificados.Columnas.USUARIO + " TEXT, " +
                ContractInsertMallaCodificados.Columnas.SUPERVISOR + " TEXT, " +
                ContractInsertMallaCodificados.Columnas.FECHA + " TEXT, " +
                ContractInsertMallaCodificados.Columnas.HORA + " TEXT, " +
                ContractInsertMallaCodificados.Columnas.SECTOR + " TEXT, " +
                ContractInsertMallaCodificados.Columnas.CATEGORIA + " TEXT, " +
                ContractInsertMallaCodificados.Columnas.SUBCATEGORIA + " TEXT, " +
                ContractInsertMallaCodificados.Columnas.SEGMENTO1 + " TEXT, " +
                ContractInsertMallaCodificados.Columnas.BRAND + " TEXT, " +
                ContractInsertMallaCodificados.Columnas.SKU_CODE + " TEXT, " +
                ContractInsertMallaCodificados.Columnas.CODIFICA + " TEXT, " +
                ContractInsertMallaCodificados.Columnas.OBSERVACION + " TEXT, " +
                ContractInsertMallaCodificados.Columnas.MANUFACTURER + " TEXT, " +
                ContractInsertMallaCodificados.Columnas.POS_NAME + " TEXT, " +
                Constantes.ID_REMOTA + " TEXT UNIQUE," +
                Constantes.ESTADO + " INTEGER NOT NULL DEFAULT "+ Constantes.ESTADO_OK+"," +
                Constantes.PENDIENTE_INSERCION + " INTEGER NOT NULL DEFAULT 0)";
        database.execSQL(cmd);
    }



    private void createTableInsertPropensosYProdMalEst(SQLiteDatabase database){
        String cmd = "CREATE TABLE " + ContractInsertPropensosYProdMalEst.INSERT_PROP_MAL_EST + " (" +
                ContractInsertPropensosYProdMalEst.Columnas._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ContractInsertPropensosYProdMalEst.Columnas.PHARMA_ID + " TEXT, " +
                ContractInsertPropensosYProdMalEst.Columnas.CODIGO + " TEXT, " +
                ContractInsertPropensosYProdMalEst.Columnas.USUARIO + " TEXT, " +
                ContractInsertPropensosYProdMalEst.Columnas.SUPERVISOR + " TEXT, " +
                ContractInsertPropensosYProdMalEst.Columnas.FECHA + " TEXT, " +
                ContractInsertPropensosYProdMalEst.Columnas.HORA + " TEXT, " +
                ContractInsertPropensosYProdMalEst.Columnas.SECTOR + " TEXT, " +
                ContractInsertPropensosYProdMalEst.Columnas.CATEGORIA + " TEXT, " +
                ContractInsertPropensosYProdMalEst.Columnas.SUBCATEGORIA + " TEXT, " +
                ContractInsertPropensosYProdMalEst.Columnas.PRESENTACION + " TEXT, " +
                ContractInsertPropensosYProdMalEst.Columnas.BRAND + " TEXT, " +
                ContractInsertPropensosYProdMalEst.Columnas.CONTENIDO + " TEXT, " +
                ContractInsertPropensosYProdMalEst.Columnas.SKU_CODE + " TEXT, " +
                ContractInsertPropensosYProdMalEst.Columnas.INVENTARIOS + " TEXT, " +
                ContractInsertPropensosYProdMalEst.Columnas.SOUVENIRS + " TEXT, " +
                ContractInsertPropensosYProdMalEst.Columnas.TIPO_CONTEO_TOTAL + " TEXT, " +
                ContractInsertPropensosYProdMalEst.Columnas.TOTAL + " TEXT, " +
                ContractInsertPropensosYProdMalEst.Columnas.TIPO_CONTEO_DEFECTUOSAS + " TEXT, " +
                ContractInsertPropensosYProdMalEst.Columnas.CANT_DEFECTUOSAS + " TEXT, " +
                ContractInsertPropensosYProdMalEst.Columnas.FECHA_CADUCIDAD_TOTAL + " TEXT, " +
                ContractInsertPropensosYProdMalEst.Columnas.FECHA_CADUCIDAD + " TEXT, " +
                ContractInsertPropensosYProdMalEst.Columnas.CAUSAL + " TEXT, " +
                ContractInsertPropensosYProdMalEst.Columnas.FOTO + " TEXT, " +
                ContractInsertPropensosYProdMalEst.Columnas.PLATAFORMA + " TEXT, " +
                ContractInsertPropensosYProdMalEst.Columnas.MODULO + " TEXT, " +
               ContractInsertPropensosYProdMalEst.Columnas.LITROS + " TEXT, " +
               ContractInsertPropensosYProdMalEst.Columnas.DIAS_RESTANTES + " TEXT, " +
                ContractInsertPropensosYProdMalEst.Columnas.VALORIZADO + " TEXT, " +
                ContractInsertPropensosYProdMalEst.Columnas.SKU_CLIENTE + " TEXT, " +
                Constantes.ID_REMOTA + " TEXT UNIQUE," +
                Constantes.ESTADO + " INTEGER NOT NULL DEFAULT "+ Constantes.ESTADO_OK+"," +
                Constantes.PENDIENTE_INSERCION + " INTEGER NOT NULL DEFAULT 0)";
        database.execSQL(cmd);
    }



    private void createTableInsertPacks(SQLiteDatabase database) {
        String cmd = "CREATE TABLE " + ContractInsertPacks.INSERT_PACKS + " (" +
                ContractInsertPacks.Columnas._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ContractInsertPacks.Columnas.PHARMA_ID + " TEXT, " +
                ContractInsertPacks.Columnas.CODIGO + " TEXT, " +
                ContractInsertPacks.Columnas.USUARIO + " TEXT, " +
                ContractInsertPacks.Columnas.SUPERVISOR + " TEXT, " +
                ContractInsertPacks.Columnas.FECHA + " TEXT, " +
                ContractInsertPacks.Columnas.HORA + " TEXT, " +
                ContractInsertPacks.Columnas.CATEGORIA + " TEXT, " +
                ContractInsertPacks.Columnas.SUBCATEGORIA + " TEXT, " +
                ContractInsertPacks.Columnas.PRESENTACION + " TEXT, " +
                ContractInsertPacks.Columnas.BRAND + " TEXT, " +
                ContractInsertPacks.Columnas.SKU_CODE + " TEXT, " +
                ContractInsertPacks.Columnas.OBSERVACION + " TEXT, " +
                ContractInsertPacks.Columnas.FOTO + " TEXT, " +
                ContractInsertPacks.Columnas.MANUFACTURER + " TEXT, " +
                Constantes.ID_REMOTA + " TEXT UNIQUE," +
                Constantes.ESTADO + " INTEGER NOT NULL DEFAULT "+ Constantes.ESTADO_OK+"," +
                Constantes.PENDIENTE_INSERCION + " INTEGER NOT NULL DEFAULT 0)";
        database.execSQL(cmd);
    }

    private void createTableInsertProdCaducar(SQLiteDatabase database) {
        String cmd = "CREATE TABLE " + ContractInsertProdCaducar.INSERT_PROD_CADUCAR + " (" +
                ContractInsertProdCaducar.Columnas._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ContractInsertProdCaducar.Columnas.PHARMA_ID + " TEXT, " +
                ContractInsertProdCaducar.Columnas.CODIGO + " TEXT, " +
                ContractInsertProdCaducar.Columnas.USUARIO + " TEXT, " +
                ContractInsertProdCaducar.Columnas.SUPERVISOR + " TEXT, " +
                ContractInsertProdCaducar.Columnas.FECHA + " TEXT, " +
                ContractInsertProdCaducar.Columnas.HORA + " TEXT, " +
                ContractInsertProdCaducar.Columnas.CATEGORIA + " TEXT, " +
                ContractInsertProdCaducar.Columnas.SUBCATEGORIA + " TEXT, " +
                ContractInsertProdCaducar.Columnas.BRAND + " TEXT, " +
                ContractInsertProdCaducar.Columnas.SKU_CODE + " TEXT, " +
                ContractInsertProdCaducar.Columnas.FECHA_PROD + " TEXT, " +
                ContractInsertProdCaducar.Columnas.CANTIDAD_PROD + " TEXT, " +
                ContractInsertProdCaducar.Columnas.MANUFACTURER + " TEXT, " +
                Constantes.ID_REMOTA + " TEXT UNIQUE," +
                Constantes.ESTADO + " INTEGER NOT NULL DEFAULT "+ Constantes.ESTADO_OK+"," +
                Constantes.PENDIENTE_INSERCION + " INTEGER NOT NULL DEFAULT 0)";
        database.execSQL(cmd);
    }

    private void createTableInsertSugeridos(SQLiteDatabase database) {
        String cmd = "CREATE TABLE " + ContractInsertSugeridos.INSERT_SUGERIDOS + " (" +
                ContractInsertSugeridos.Columnas._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ContractInsertSugeridos.Columnas.PHARMA_ID + " TEXT, " +
                ContractInsertSugeridos.Columnas.CODIGO + " TEXT, " +
                ContractInsertSugeridos.Columnas.USUARIO + " TEXT, " +
                ContractInsertSugeridos.Columnas.SUPERVISOR + " TEXT, " +
                ContractInsertSugeridos.Columnas.FECHA + " TEXT, " +
                ContractInsertSugeridos.Columnas.HORA + " TEXT, " +
                ContractInsertSugeridos.Columnas.LOCAL + " TEXT, " +
                ContractInsertSugeridos.Columnas.CODIGO_FABRIL + " TEXT, " +
                ContractInsertSugeridos.Columnas.VENDEDOR_FABRIL + " TEXT, " +
                ContractInsertSugeridos.Columnas.CATEGORIA + " TEXT, " +
                ContractInsertSugeridos.Columnas.SUBCATEGORIA + " TEXT, " +
                ContractInsertSugeridos.Columnas.BRAND + " TEXT, " +
                ContractInsertSugeridos.Columnas.SKU_CODE + " TEXT, " +
                ContractInsertSugeridos.Columnas.QUIEBRE + " TEXT, " +
                ContractInsertSugeridos.Columnas.UNIDAD_DISPONIBLE + " TEXT, " +
                ContractInsertSugeridos.Columnas.SUGERIDO + " TEXT, " +
                ContractInsertSugeridos.Columnas.CANTIDAD + " TEXT, " +
                ContractInsertSugeridos.Columnas.OBSERVACIONES + " TEXT, " +
                ContractInsertSugeridos.Columnas.ENTREGA + " TEXT, " +
                Constantes.ID_REMOTA + " TEXT UNIQUE," +
                Constantes.ESTADO + " INTEGER NOT NULL DEFAULT "+ Constantes.ESTADO_OK+"," +
                Constantes.PENDIENTE_INSERCION + " INTEGER NOT NULL DEFAULT 0)";
        database.execSQL(cmd);
    }





    private void createTableInsertVentas(SQLiteDatabase database) {
        String cmd = "CREATE TABLE " + ContractInsertVentas.INSERT_VENTAS + " (" +
                ContractInsertVentas.Columnas._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ContractInsertVentas.Columnas.PHARMA_ID + " TEXT, " +
                ContractInsertVentas.Columnas.CODIGO + " TEXT, " +
                ContractInsertVentas.Columnas.USUARIO + " TEXT, " +
                ContractInsertVentas.Columnas.SUPERVISOR + " TEXT, " +
                ContractInsertVentas.Columnas.FECHA + " TEXT, " +
                ContractInsertVentas.Columnas.HORA + " TEXT, " +
                ContractInsertVentas.Columnas.FECHA_VENTA + " TEXT, " +
                ContractInsertVentas.Columnas.CATEGORIA + " TEXT, " +
                ContractInsertVentas.Columnas.SUBCATEGORIA + " TEXT, " +
                ContractInsertVentas.Columnas.PRESENTACION + " TEXT, " +
                ContractInsertVentas.Columnas.MARCA + " TEXT, " +
                ContractInsertVentas.Columnas.SKU_CODE + " TEXT, " +
                ContractInsertVentas.Columnas.TIPO_VENTA + " TEXT, " +
                ContractInsertVentas.Columnas.STOCK_INICIAL + " TEXT, " +
                ContractInsertVentas.Columnas.CANTIDAD + " TEXT, " +
                ContractInsertVentas.Columnas.PREGULAR + " TEXT, " +
                ContractInsertVentas.Columnas.PPROMOCION + " TEXT, " +
                ContractInsertVentas.Columnas.POFERTA + " TEXT, " +
                ContractInsertVentas.Columnas.STOCK_FINAL + " TEXT, " +
                ContractInsertVentas.Columnas.MANUFACTURER + " TEXT, " +
                ContractInsertVentas.Columnas.POS_NAME + " TEXT, " +
                ContractInsertVentas.Columnas.FOTO + " TEXT, " +
                Constantes.ID_REMOTA + " TEXT UNIQUE," +
                Constantes.ESTADO + " INTEGER NOT NULL DEFAULT " + Constantes.ESTADO_OK + "," +
                Constantes.PENDIENTE_INSERCION + " INTEGER NOT NULL DEFAULT 0)";
        database.execSQL(cmd);
    }






    private void createTableInsertRotacion(SQLiteDatabase database) {
        String cmd = "CREATE TABLE " + ContractInsertRotacion.INSERT_ROTACION + " (" +
                ContractInsertRotacion.Columnas._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ContractInsertRotacion.Columnas.PHARMA_ID + " TEXT, " +
                ContractInsertRotacion.Columnas.CODIGO + " TEXT, " +
                ContractInsertRotacion.Columnas.USUARIO + " TEXT, " +
                ContractInsertRotacion.Columnas.SUPERVISOR + " TEXT, " +
                ContractInsertRotacion.Columnas.FECHA + " TEXT, " +
                ContractInsertRotacion.Columnas.HORA + " TEXT, " +
                ContractInsertRotacion.Columnas.CATEGORIA + " TEXT, " +
                ContractInsertRotacion.Columnas.PRODUCTO + " TEXT, " +
                ContractInsertRotacion.Columnas.PROMOCIONAL + " TEXT, " +
                ContractInsertRotacion.Columnas.MECANICA + " TEXT, " +
                ContractInsertRotacion.Columnas.PESO + " TEXT, " +
                ContractInsertRotacion.Columnas.CANTIDAD + " TEXT, " +
                ContractInsertRotacion.Columnas.FECHA_ROT + " TEXT, " +
                ContractInsertRotacion.Columnas.FOTO_GUIA + " TEXT, " +
                ContractInsertRotacion.Columnas.OBSERVACIONES + " TEXT, " +
                Constantes.ID_REMOTA + " TEXT UNIQUE," +
                Constantes.ESTADO + " INTEGER NOT NULL DEFAULT "+ Constantes.ESTADO_OK+"," +
                Constantes.PENDIENTE_INSERCION + " INTEGER NOT NULL DEFAULT 0)";
        database.execSQL(cmd);
    }

    private void createTableInsertCanjes(SQLiteDatabase database) {
        String cmd = "CREATE TABLE " + ContractInsertCanjes.INSERT_CANJES + " (" +
                ContractInsertCanjes.Columnas._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ContractInsertCanjes.Columnas.PHARMA_ID + " TEXT, " +
                ContractInsertCanjes.Columnas.CODIGO + " TEXT, " +
                ContractInsertCanjes.Columnas.CANAL + " TEXT, " +
                ContractInsertCanjes.Columnas.NOMBRE_COMERCIAL + " TEXT, " +
                ContractInsertCanjes.Columnas.LOCAL + " TEXT, " +
                ContractInsertCanjes.Columnas.REGION + " TEXT, " +
                ContractInsertCanjes.Columnas.PROVINCIA + " TEXT, " +
                ContractInsertCanjes.Columnas.CIUDAD + " TEXT, " +
                ContractInsertCanjes.Columnas.ZONA + " TEXT, " +
                ContractInsertCanjes.Columnas.DIRECCION + " TEXT, " +
                ContractInsertCanjes.Columnas.SUPERVISOR + " TEXT, " +
                ContractInsertCanjes.Columnas.MERCADERISTA + " TEXT, " +
                ContractInsertCanjes.Columnas.USUARIO + " TEXT, " +
                ContractInsertCanjes.Columnas.LATITUD + " TEXT, " +
                ContractInsertCanjes.Columnas.LONGITUD + " TEXT, " +
                ContractInsertCanjes.Columnas.TERRITORIO + " TEXT, " +
                ContractInsertCanjes.Columnas.ZONA_TERRITORIO + " TEXT, " +
                ContractInsertCanjes.Columnas.CATEGORIA + " TEXT, " +
                ContractInsertCanjes.Columnas.SUBCATEGORIA + " TEXT, " +
                ContractInsertCanjes.Columnas.MARCA + " TEXT, " +
                ContractInsertCanjes.Columnas.PRODUCTO + " TEXT, " +
                ContractInsertCanjes.Columnas.TIPO_COMBO + " TEXT, " +
                ContractInsertCanjes.Columnas.MECANICA + " TEXT, " +
                ContractInsertCanjes.Columnas.COMBOS_ARMADOS + " TEXT, " +
                ContractInsertCanjes.Columnas.STOCK + " TEXT, " +
                ContractInsertCanjes.Columnas.PVC_COMBO + " TEXT, " +
                ContractInsertCanjes.Columnas.PVC_UNITARIO + " TEXT, " +
                ContractInsertCanjes.Columnas.VISITA + " TEXT, " +
                ContractInsertCanjes.Columnas.MES + " TEXT, " +
                ContractInsertCanjes.Columnas.OBSERVACIONES + " TEXT, " +
                ContractInsertCanjes.Columnas.FOTO + " TEXT, " +
                ContractInsertCanjes.Columnas.FOTO_GUIA + " TEXT, " +
                ContractInsertCanjes.Columnas.FECHA + " TEXT, " +
                ContractInsertCanjes.Columnas.HORA + " TEXT, " +
                ContractInsertCanjes.Columnas.POS_NAME + " TEXT, " +
                ContractInsertCanjes.Columnas.PLATAFORMA + " TEXT, " +
                Constantes.ID_REMOTA + " TEXT UNIQUE," +
                Constantes.ESTADO + " INTEGER NOT NULL DEFAULT "+ Constantes.ESTADO_OK+"," +
                Constantes.PENDIENTE_INSERCION + " INTEGER NOT NULL DEFAULT 0)";
        database.execSQL(cmd);
    }

    private void createTableInsertMaterialesRecibidos(SQLiteDatabase database) {
        String cmd = "CREATE TABLE " + ContractInsertMaterialesRecibidos.INSERT_MATERIALES_RECIBIDOS + " (" +
                ContractInsertMaterialesRecibidos.Columnas._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ContractInsertMaterialesRecibidos.Columnas.PHARMA_ID + " TEXT, " +
                ContractInsertMaterialesRecibidos.Columnas.CODIGO + " TEXT, " +
                ContractInsertMaterialesRecibidos.Columnas.CANAL + " TEXT, " +
                ContractInsertMaterialesRecibidos.Columnas.NOMBRE_COMERCIAL + " TEXT, " +
                ContractInsertMaterialesRecibidos.Columnas.LOCAL + " TEXT, " +
                ContractInsertMaterialesRecibidos.Columnas.REGION + " TEXT, " +
                ContractInsertMaterialesRecibidos.Columnas.PROVINCIA + " TEXT, " +
                ContractInsertMaterialesRecibidos.Columnas.CIUDAD + " TEXT, " +
                ContractInsertMaterialesRecibidos.Columnas.ZONA + " TEXT, " +
                ContractInsertMaterialesRecibidos.Columnas.DIRECCION + " TEXT, " +
                ContractInsertMaterialesRecibidos.Columnas.SUPERVISOR + " TEXT, " +
                ContractInsertMaterialesRecibidos.Columnas.MERCADERISTA + " TEXT, " +
                ContractInsertMaterialesRecibidos.Columnas.USUARIO + " TEXT, " +
                ContractInsertMaterialesRecibidos.Columnas.LATITUD + " TEXT, " +
                ContractInsertMaterialesRecibidos.Columnas.LONGITUD + " TEXT, " +
                ContractInsertMaterialesRecibidos.Columnas.TERRITORIO + " TEXT, " +
                ContractInsertMaterialesRecibidos.Columnas.ZONA_TERRITORIO + " TEXT, " +
                ContractInsertMaterialesRecibidos.Columnas.ALERTA + " TEXT, " +
                ContractInsertMaterialesRecibidos.Columnas.TIPO + " TEXT, " +
                ContractInsertMaterialesRecibidos.Columnas.MATERIAL + " TEXT, " +
                ContractInsertMaterialesRecibidos.Columnas.CANTIDAD + " TEXT, " +
                ContractInsertMaterialesRecibidos.Columnas.ESTADO_MATERIAL + " TEXT, " +
                ContractInsertMaterialesRecibidos.Columnas.PRIORIDAD + " TEXT, " +
                ContractInsertMaterialesRecibidos.Columnas.OBSERVACIONES + " TEXT, " +
                ContractInsertMaterialesRecibidos.Columnas.FOTO + " TEXT, " +
                ContractInsertMaterialesRecibidos.Columnas.FECHA + " TEXT, " +
                ContractInsertMaterialesRecibidos.Columnas.HORA + " TEXT, " +
                Constantes.ID_REMOTA + " TEXT UNIQUE," +
                Constantes.ESTADO + " INTEGER NOT NULL DEFAULT "+ Constantes.ESTADO_OK+"," +
                Constantes.PENDIENTE_INSERCION + " INTEGER NOT NULL DEFAULT 0)";
        database.execSQL(cmd);
    }

    private void createTableInsertEjecucionMateriales(SQLiteDatabase database) {
        String cmd = "CREATE TABLE " + ContractInsertEjecucionMateriales.INSERT_EJECUCION_MATERIALES + " (" +
                ContractInsertEjecucionMateriales.Columnas._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ContractInsertEjecucionMateriales.Columnas.PHARMA_ID + " TEXT, " +
                ContractInsertEjecucionMateriales.Columnas.CODIGO + " TEXT, " +
                ContractInsertEjecucionMateriales.Columnas.CANAL + " TEXT, " +
                ContractInsertEjecucionMateriales.Columnas.NOMBRE_COMERCIAL + " TEXT, " +
                ContractInsertEjecucionMateriales.Columnas.LOCAL + " TEXT, " +
                ContractInsertEjecucionMateriales.Columnas.REGION + " TEXT, " +
                ContractInsertEjecucionMateriales.Columnas.PROVINCIA + " TEXT, " +
                ContractInsertEjecucionMateriales.Columnas.CIUDAD + " TEXT, " +
                ContractInsertEjecucionMateriales.Columnas.ZONA + " TEXT, " +
                ContractInsertEjecucionMateriales.Columnas.DIRECCION + " TEXT, " +
                ContractInsertEjecucionMateriales.Columnas.SUPERVISOR + " TEXT, " +
                ContractInsertEjecucionMateriales.Columnas.MERCADERISTA + " TEXT, " +
                ContractInsertEjecucionMateriales.Columnas.USUARIO + " TEXT, " +
                ContractInsertEjecucionMateriales.Columnas.LATITUD + " TEXT, " +
                ContractInsertEjecucionMateriales.Columnas.LONGITUD + " TEXT, " +
                ContractInsertEjecucionMateriales.Columnas.TERRITORIO + " TEXT, " +
                ContractInsertEjecucionMateriales.Columnas.ZONA_TERRITORIO + " TEXT, " +
                ContractInsertEjecucionMateriales.Columnas.TIPO + " TEXT, " +
                ContractInsertEjecucionMateriales.Columnas.MATERIAL + " TEXT, " +
                ContractInsertEjecucionMateriales.Columnas.ESTADO_MATERIAL + " TEXT, " +
                ContractInsertEjecucionMateriales.Columnas.PRIORIDAD + " TEXT, " +
                ContractInsertEjecucionMateriales.Columnas.OBSERVACIONES + " TEXT, " +
                ContractInsertEjecucionMateriales.Columnas.FOTO + " TEXT, " +
                ContractInsertEjecucionMateriales.Columnas.FECHA + " TEXT, " +
                ContractInsertEjecucionMateriales.Columnas.HORA + " TEXT, " +
                Constantes.ID_REMOTA + " TEXT UNIQUE," +
                Constantes.ESTADO + " INTEGER NOT NULL DEFAULT "+ Constantes.ESTADO_OK+"," +
                Constantes.PENDIENTE_INSERCION + " INTEGER NOT NULL DEFAULT 0)";
        database.execSQL(cmd);
    }

    private void createTableInsertMCIPDV(SQLiteDatabase database) {
        String cmd = "CREATE TABLE " + ContractInsertMCIPdv.INSERT_MCI + " (" +
                ContractInsertMCIPdv.Columnas._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ContractInsertMCIPdv.Columnas.PHARMA_ID + " TEXT, " +
                ContractInsertMCIPdv.Columnas.CODIGO + " TEXT, " +
                ContractInsertMCIPdv.Columnas.CANAL + " TEXT, " +
                ContractInsertMCIPdv.Columnas.NOMBRE_COMERCIAL + " TEXT, " +
                ContractInsertMCIPdv.Columnas.LOCAL + " TEXT, " +
                ContractInsertMCIPdv.Columnas.REGION + " TEXT, " +
                ContractInsertMCIPdv.Columnas.PROVINCIA + " TEXT, " +
                ContractInsertMCIPdv.Columnas.CIUDAD + " TEXT, " +
                ContractInsertMCIPdv.Columnas.ZONA + " TEXT, " +
                ContractInsertMCIPdv.Columnas.DIRECCION + " TEXT, " +
                ContractInsertMCIPdv.Columnas.SUPERVISOR + " TEXT, " +
                ContractInsertMCIPdv.Columnas.MERCADERISTA + " TEXT, " +
                ContractInsertMCIPdv.Columnas.USUARIO + " TEXT, " +
                ContractInsertMCIPdv.Columnas.LATITUD + " TEXT, " +
                ContractInsertMCIPdv.Columnas.LONGITUD + " TEXT, " +
                ContractInsertMCIPdv.Columnas.TERRITORIO + " TEXT, " +
                ContractInsertMCIPdv.Columnas.ZONA_TERRITORIO + " TEXT, " +
                ContractInsertMCIPdv.Columnas.CAUSAL + " TEXT, " +
                ContractInsertMCIPdv.Columnas.OBSERVACIONES + " TEXT, " +
                ContractInsertMCIPdv.Columnas.FOTO + " TEXT, " +
                ContractInsertMCIPdv.Columnas.FECHA + " TEXT, " +
                ContractInsertMCIPdv.Columnas.HORA + " TEXT, " +
                ContractInsertMCIPdv.Columnas.POS_NAME + " TEXT, " +
                Constantes.ID_REMOTA + " TEXT UNIQUE," +
                Constantes.ESTADO + " INTEGER NOT NULL DEFAULT "+ Constantes.ESTADO_OK+"," +
                Constantes.PENDIENTE_INSERCION + " INTEGER NOT NULL DEFAULT 0)";
        database.execSQL(cmd);
    }

    private void createTableLog(SQLiteDatabase database) {
        String cmd = "CREATE TABLE " + ContractLog.LOG + " (" +
                ContractLog.Columnas._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ContractLog.Columnas.USUARIO + " TEXT, " +
                ContractLog.Columnas.FECHA + " TEXT, " +
                ContractLog.Columnas.HORA + " TEXT, " +
                ContractLog.Columnas.ACCION + " TEXT, " +
                Constantes.ID_REMOTA + " TEXT UNIQUE," +
                Constantes.ESTADO + " INTEGER NOT NULL DEFAULT "+ Constantes.ESTADO_OK+"," +
                Constantes.PENDIENTE_INSERCION + " INTEGER NOT NULL DEFAULT 0)";
        database.execSQL(cmd);
    }

    private void createTablePrecioPvc(SQLiteDatabase database) {
        String cmd = "CREATE TABLE " + ContractPreciosPvc.PRECIOS_PVC + " (" +
                ContractPreciosPvc.Columnas._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ContractPreciosPvc.Columnas.USUARIO + " TEXT, " +
                ContractPreciosPvc.Columnas.FECHA + " TEXT, " +
                ContractPreciosPvc.Columnas.CODIGO + " TEXT, " +
                ContractPreciosPvc.Columnas.CANAL + " TEXT, " +
                ContractPreciosPvc.Columnas.CADENA + " TEXT, " +
                ContractPreciosPvc.Columnas.CATEGORIA + " TEXT, " +
                ContractPreciosPvc.Columnas.SUBCATEGORIA + " TEXT, " +
                ContractPreciosPvc.Columnas.MARCA + " TEXT, " +
                ContractPreciosPvc.Columnas.SKU + " TEXT, " +
                ContractPreciosPvc.Columnas.PVC + " TEXT, " +
                Constantes.ID_REMOTA + " TEXT UNIQUE," +
                Constantes.ESTADO + " INTEGER NOT NULL DEFAULT "+ Constantes.ESTADO_OK+"," +
                Constantes.PENDIENTE_INSERCION + " INTEGER NOT NULL DEFAULT 0)";
        database.execSQL(cmd);
    }

    private void createTableInsertPreciosSesiones(SQLiteDatabase database) {
        String cmd = "CREATE TABLE " + ContractInsertPreciosSesion.INSERT_PRECIOS_SESION + " (" +
                ContractInsertPreciosSesion.Columnas._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ContractInsertPreciosSesion.Columnas.PHARMA_ID + " TEXT, " +
                ContractInsertPreciosSesion.Columnas.CODIGO + " TEXT, " +
                ContractInsertPreciosSesion.Columnas.USUARIO + " TEXT, " +
                ContractInsertPreciosSesion.Columnas.SUPERVISOR + " TEXT, " +
                ContractInsertPreciosSesion.Columnas.FECHA + " TEXT, " +
                ContractInsertPreciosSesion.Columnas.HORA + " TEXT, " +
                ContractInsertPreciosSesion.Columnas.SECTOR + " TEXT, " +
                ContractInsertPreciosSesion.Columnas.CATEGORIA + " TEXT, " +
                ContractInsertPreciosSesion.Columnas.SEGMENTO1 + " TEXT, " +
                ContractInsertPreciosSesion.Columnas.SEGMENTO2 + " TEXT, " +
                ContractInsertPreciosSesion.Columnas.BRAND + " TEXT, " +
                ContractInsertPreciosSesion.Columnas.TAMANO + " TEXT, " +
                ContractInsertPreciosSesion.Columnas.CANTIDAD + " TEXT, " +
                ContractInsertPreciosSesion.Columnas.SKU_CODE + " TEXT, " +
                ContractInsertPreciosSesion.Columnas.PREGULAR + " TEXT, " +
                ContractInsertPreciosSesion.Columnas.PPROMOCION + " TEXT, " +
                ContractInsertPreciosSesion.Columnas.MANUFACTURER + " TEXT, " +
                Constantes.ID_REMOTA + " TEXT UNIQUE," +
                Constantes.ESTADO + " INTEGER NOT NULL DEFAULT "+ Constantes.ESTADO_OK+"," +
                Constantes.PENDIENTE_INSERCION + " INTEGER NOT NULL DEFAULT 0)";
        database.execSQL(cmd);
    }

    private void createTableInsertAgotados(SQLiteDatabase database) {
        String cmd = "CREATE TABLE " + ContractInsertAgotados.INSERT_AGOTADOS + " (" +
                ContractInsertAgotados.Columnas._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                ContractInsertAgotados.Columnas.PHARMA_ID + " TEXT, " +
                ContractInsertAgotados.Columnas.CODIGO + " TEXT, " +
                ContractInsertAgotados.Columnas.USUARIO + " TEXT, " +
                ContractInsertAgotados.Columnas.SUPERVISOR + " TEXT, " +
                ContractInsertAgotados.Columnas.TIEMPO_INICIO + " TEXT, " +
                ContractInsertAgotados.Columnas.TIEMPO_FIN + " TEXT, " +
                ContractInsertAgotados.Columnas.FECHA + " TEXT, " +
                ContractInsertAgotados.Columnas.HORA + " TEXT, " +

                Constantes.ID_REMOTA + " TEXT UNIQUE," +
                Constantes.ESTADO + " INTEGER NOT NULL DEFAULT "+ Constantes.ESTADO_OK+"," +
                Constantes.PENDIENTE_INSERCION + " INTEGER NOT NULL DEFAULT 0)";
        database.execSQL(cmd);
    }

    private void createTableInsertTareas(SQLiteDatabase database) {
        String cmd = "CREATE TABLE " + ContractInsertTareas.INSERT_TAREAS + " (" +
                ContractInsertTareas.Columnas._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                ContractInsertTareas.Columnas.PHARMA_ID + " TEXT, " +
                ContractInsertTareas.Columnas.CODIGO + " TEXT, " +
                ContractInsertTareas.Columnas.USUARIO + " TEXT, " +
                ContractInsertTareas.Columnas.SUPERVISOR + " TEXT, " +
                ContractInsertTareas.Columnas.FECHA + " TEXT, " +
                ContractInsertTareas.Columnas.HORA + " TEXT, " +
                ContractInsertTareas.Columnas.CHANNEL + " TEXT, " +
                ContractInsertTareas.Columnas.CODIGOPDV + " TEXT, " +
                ContractInsertTareas.Columnas.MERCADERISTA + " TEXT, " +
                ContractInsertTareas.Columnas.TAREAS + " TEXT, " +
                ContractInsertTareas.Columnas.REALIZADO + " TEXT, " +
                ContractInsertTareas.Columnas.FOTO + " TEXT, " +

                Constantes.ID_REMOTA + " TEXT UNIQUE," +
                Constantes.ESTADO + " INTEGER NOT NULL DEFAULT "+ Constantes.ESTADO_OK+"," +
                Constantes.PENDIENTE_INSERCION + " INTEGER NOT NULL DEFAULT 0)";
        database.execSQL(cmd);
    }

    private void createTableInsertVenta(SQLiteDatabase database) {
        String cmd = "CREATE TABLE " + ContractInsertVenta.INSERT_VENTA + " (" +
                ContractInsertVenta.Columnas._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ContractInsertVenta.Columnas.PHARMA_ID + " TEXT, " +
                ContractInsertVenta.Columnas.CODIGO + " TEXT, " +
                ContractInsertVenta.Columnas.USUARIO + " TEXT, " +
                ContractInsertVenta.Columnas.SUPERVISOR + " TEXT, " +
                ContractInsertVenta.Columnas.TIPO_FACTURA + " TEXT, " +
                ContractInsertVenta.Columnas.NUM_FACTURA + " TEXT, " +
                ContractInsertVenta.Columnas.MONTO_FACTURA + " TEXT, " +
                ContractInsertVenta.Columnas.FECHA_VENTA + " TEXT, " +
                ContractInsertVenta.Columnas.KEY_IMAGE + " TEXT, " +
                ContractInsertVenta.Columnas.FECHA + " TEXT, " +
                ContractInsertVenta.Columnas.HORA + " TEXT, " +

                Constantes.ID_REMOTA + " TEXT UNIQUE," +
                Constantes.ESTADO + " INTEGER NOT NULL DEFAULT "+ Constantes.ESTADO_OK+"," +
                Constantes.PENDIENTE_INSERCION + " INTEGER NOT NULL DEFAULT 0)";
        database.execSQL(cmd);
    }

    private void createTableInsertPreguntas(SQLiteDatabase database){
        String cmd = "CREATE TABLE " + ContractInsertPreguntas.INSERT_PREGUNTAS + " (" +
                ContractInsertPreguntas.Columnas._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ContractInsertPreguntas.Columnas.USUARIO + " TEXT, " +
                ContractInsertPreguntas.Columnas.TEST_ID + " TEXT, " +
                ContractInsertPreguntas.Columnas.TEST + " TEXT, " +
                ContractInsertPreguntas.Columnas.P1 + " TEXT, " +
                ContractInsertPreguntas.Columnas.P2 + " TEXT, " +
                ContractInsertPreguntas.Columnas.P3 + " TEXT, " +
                ContractInsertPreguntas.Columnas.P4 + " TEXT, " +
                ContractInsertPreguntas.Columnas.P5 + " TEXT, " +
                ContractInsertPreguntas.Columnas.P6 + " TEXT, " +
                ContractInsertPreguntas.Columnas.P7 + " TEXT, " +
                ContractInsertPreguntas.Columnas.P8 + " TEXT, " +
                ContractInsertPreguntas.Columnas.P9 + " TEXT, " +
                ContractInsertPreguntas.Columnas.P10 + " TEXT, " +
                ContractInsertPreguntas.Columnas.P11 + " TEXT, " +
                ContractInsertPreguntas.Columnas.P12 + " TEXT, " +
                ContractInsertPreguntas.Columnas.P13 + " TEXT, " +
                ContractInsertPreguntas.Columnas.P14 + " TEXT, " +
                ContractInsertPreguntas.Columnas.P15 + " TEXT, " +
                ContractInsertPreguntas.Columnas.CORRECTAS + " TEXT, " +
                ContractInsertPreguntas.Columnas.INCORRECTAS + " TEXT, " +
                ContractInsertPreguntas.Columnas.CALIFICACION + " TEXT, " +
                ContractInsertPreguntas.Columnas.OBSERVACION + " TEXT, " +
                ContractInsertPreguntas.Columnas.FECHA + " TEXT, " +
                ContractInsertPreguntas.Columnas.HORA + " TEXT, " +
                ContractInsertPreguntas.Columnas.CRONOMETO + " TEXT, " +
                ContractInsertPreguntas.Columnas.ESTADO_TEST + " TEXT, " +
                Constantes.ID_REMOTA + " TEXT UNIQUE," +
                Constantes.ESTADO + " INTEGER NOT NULL DEFAULT "+ Constantes.ESTADO_OK+"," +
                Constantes.PENDIENTE_INSERCION + " INTEGER NOT NULL DEFAULT 0)";
        database.execSQL(cmd);
    }

    private void createTableInsertFotografico(SQLiteDatabase database) {
        String cmd = "CREATE TABLE " + ContractInsertFotografico.INSERT_FOTOGRAFICO + " (" +
                ContractInsertFotografico.Columnas._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ContractInsertFotografico.Columnas.PHARMA_ID + " TEXT, " +
                ContractInsertFotografico.Columnas.CODIGO + " TEXT, " +
                ContractInsertFotografico.Columnas.USUARIO + " TEXT, " +
                ContractInsertFotografico.Columnas.CATEGORIA + " TEXT, " +
                ContractInsertFotografico.Columnas.SUBCATEGORIA + " TEXT, " +
                ContractInsertFotografico.Columnas.MARCA + " TEXT, " +
                ContractInsertFotografico.Columnas.LOGRO + " TEXT, " +
                ContractInsertFotografico.Columnas.KEY_IMAGE + " TEXT, " +
                ContractInsertFotografico.Columnas.FECHA + " TEXT, " +
                ContractInsertFotografico.Columnas.HORA + " TEXT, " +
                ContractInsertFotografico.Columnas.ID_REMOTA + " TEXT UNIQUE," +
                ContractInsertFotografico.Columnas.ESTADO + " INTEGER NOT NULL DEFAULT "+ ContractInsertFotografico.ESTADO_OK+"," +
                ContractInsertFotografico.Columnas.PENDIENTE_INSERCION + " INTEGER NOT NULL DEFAULT 0)";
        database.execSQL(cmd);
    }

    private void createTableInsertValores(SQLiteDatabase database) {
        String cmd = "CREATE TABLE " + ContractInsertValores.INSERT_VALORES + " (" +
                ContractInsertValores.Columnas._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ContractInsertValores.Columnas.PHARMA_ID + " TEXT, " +
                ContractInsertValores.Columnas.CODIGO + " TEXT, " +
                ContractInsertValores.Columnas.USUARIO + " TEXT, " +
                ContractInsertValores.Columnas.SUPERVISOR + " TEXT, " +
                ContractInsertValores.Columnas.FECHA + " TEXT, " +
                ContractInsertValores.Columnas.HORA + " TEXT, " +
                ContractInsertValores.Columnas.SECTOR + " TEXT, " +
                ContractInsertValores.Columnas.CATEGORIA + " TEXT, " +
                ContractInsertValores.Columnas.SEGMENTO1 + " TEXT, " +
                //ContractInsertValores.Columnas.POFERTA + " TEXT, " +
                ContractInsertValores.Columnas.BRAND + " TEXT, " +
                /*ContractInsertValores.Columnas.TAMANO + " TEXT, " +
                ContractInsertValores.Columnas.CANTIDAD + " TEXT, " +*/
                ContractInsertValores.Columnas.SKU_CODE + " TEXT, " +
                ContractInsertValores.Columnas.CODIFICA + " TEXT, " +
                ContractInsertValores.Columnas.AUSENCIA + " TEXT, " +
                ContractInsertValores.Columnas.DISPONIBLE + " TEXT, " +
                ContractInsertValores.Columnas.RESPONSABLE + " TEXT, " +
                ContractInsertValores.Columnas.RAZONES + " TEXT, " +
                ContractInsertValores.Columnas.SUGERIDO + " TEXT, " +
                ContractInsertValores.Columnas.TIPO_SUGERIDO + " TEXT, " +
                ContractInsertValores.Columnas.PVP + " TEXT, " +
                ContractInsertValores.Columnas.PVC + " TEXT, " +
                ContractInsertValores.Columnas.POFERTA + " TEXT, " +
                ContractInsertValores.Columnas.MANUFACTURER + " TEXT, " +
                ContractInsertValores.Columnas.QUIEBRE_PERCHA + " TEXT, " +
                ContractInsertValores.Columnas.QUIEBRE_BODEGA + " TEXT, " +
                ContractInsertValores.Columnas.POS_NAME + " TEXT, " +
                ContractInsertValores.Columnas.PLATAFORMA + " TEXT, " +
                Constantes.ID_REMOTA + " TEXT UNIQUE," +
                Constantes.ESTADO + " INTEGER NOT NULL DEFAULT "+ Constantes.ESTADO_OK+"," +
                Constantes.PENDIENTE_INSERCION + " INTEGER NOT NULL DEFAULT 0)";
        database.execSQL(cmd);
    }

    private void createTableInsertValoresSesiones(SQLiteDatabase database) {
        String cmd = "CREATE TABLE " + ContractInsertValoresSesion.INSERT_VALORES_SESION + " (" +
                ContractInsertValoresSesion.Columnas._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ContractInsertValoresSesion.Columnas.PHARMA_ID + " TEXT, " +
                ContractInsertValoresSesion.Columnas.CODIGO + " TEXT, " +
                ContractInsertValoresSesion.Columnas.USUARIO + " TEXT, " +
                ContractInsertValoresSesion.Columnas.SUPERVISOR + " TEXT, " +
                ContractInsertValoresSesion.Columnas.FECHA + " TEXT, " +
                ContractInsertValoresSesion.Columnas.HORA + " TEXT, " +
                ContractInsertValoresSesion.Columnas.SECTOR + " TEXT, " +
                ContractInsertValoresSesion.Columnas.CATEGORIA + " TEXT, " +
                ContractInsertValoresSesion.Columnas.SEGMENTO1 + " TEXT, " +
                ContractInsertValoresSesion.Columnas.SEGMENTO2 + " TEXT, " +
                ContractInsertValoresSesion.Columnas.BRAND + " TEXT, " +
                ContractInsertValoresSesion.Columnas.TAMANO + " TEXT, " +
                ContractInsertValoresSesion.Columnas.CANTIDAD + " TEXT, " +
                ContractInsertValoresSesion.Columnas.SKU_CODE + " TEXT, " +
                ContractInsertValoresSesion.Columnas.CODIFICA + " TEXT, " +
                ContractInsertValoresSesion.Columnas.AUSENCIA + " TEXT, " +
                ContractInsertValoresSesion.Columnas.RESPONSABLE + " TEXT, " +
                ContractInsertValoresSesion.Columnas.RAZONES + " TEXT, " +
                ContractInsertValoresSesion.Columnas.MANUFACTURER + " TEXT, " +
                Constantes.ID_REMOTA + " TEXT UNIQUE," +
                Constantes.ESTADO + " INTEGER NOT NULL DEFAULT "+ Constantes.ESTADO_OK+"," +
                Constantes.PENDIENTE_INSERCION + " INTEGER NOT NULL DEFAULT 0)";
        database.execSQL(cmd);
    }

    private void createTableInsertShare(SQLiteDatabase database) {
        String cmd = "CREATE TABLE " + ContractInsertShare.INSERT_SHARE + " (" +
                ContractInsertShare.Columnas._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ContractInsertShare.Columnas.PHARMA_ID + " TEXT, " +
                ContractInsertShare.Columnas.CODIGO + " TEXT, " +
                ContractInsertShare.Columnas.USUARIO + " TEXT, " +
                ContractInsertShare.Columnas.SUPERVISOR + " TEXT, " +
                ContractInsertShare.Columnas.FECHA + " TEXT, " +
                ContractInsertShare.Columnas.HORA + " TEXT, " +
                ContractInsertShare.Columnas.SECTOR + " TEXT, " +
                ContractInsertShare.Columnas.CATEGORIA + " TEXT, " +
                ContractInsertShare.Columnas.SEGMENTO + " TEXT, " +
                ContractInsertShare.Columnas.MARCA_SELECCIONADA + " TEXT, " +
                ContractInsertShare.Columnas.BRAND  + " TEXT, " +
                ContractInsertShare.Columnas.CTMS_PERCHA + " TEXT, " +
                ContractInsertShare.Columnas.CTMS_MARCA + " TEXT, " +
                ContractInsertShare.Columnas.OTROS + " TEXT, " +
                ContractInsertShare.Columnas.MANUFACTURER + " TEXT, " +
                ContractInsertShare.Columnas.RAZONES + " TEXT, " +
                ContractInsertShare.Columnas.FOTO + " TEXT, " +
                ContractInsertShare.Columnas.POS_NAME + " TEXT, " +
                ContractInsertShare.Columnas.PLATAFORMA + " TEXT, " +
             //   ContractInsertShare.Columnas.COMENTARIO + " TEXT, " +
                Constantes.ID_REMOTA + " TEXT UNIQUE," +
                Constantes.ESTADO + " INTEGER NOT NULL DEFAULT "+ Constantes.ESTADO_OK+"," +
                Constantes.PENDIENTE_INSERCION + " INTEGER NOT NULL DEFAULT 0)";
        database.execSQL(cmd);
    }

    private void createTableInsertPDI(SQLiteDatabase database) {
        String cmd = "CREATE TABLE " + ContractInsertPDI.INSERT_PDI + " (" +
                ContractInsertPDI.Columnas._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ContractInsertPDI.Columnas.PHARMA_ID + " TEXT, " +
                ContractInsertPDI.Columnas.CODIGO + " TEXT, " +
                ContractInsertPDI.Columnas.USUARIO + " TEXT, " +
                ContractInsertPDI.Columnas.SUPERVISOR + " TEXT, " +
                ContractInsertPDI.Columnas.FECHA + " TEXT, " +
                ContractInsertPDI.Columnas.HORA + " TEXT, " +
                ContractInsertPDI.Columnas.CATEGORIA + " TEXT, " +
                ContractInsertPDI.Columnas.MARCA_SELECCIONADA + " TEXT, " +
                ContractInsertPDI.Columnas.CUMPLIMIENTO + " TEXT, " +
                ContractInsertPDI.Columnas.UNIVERSO + " TEXT, " +
                ContractInsertPDI.Columnas.CARAS + " TEXT, " +
                ContractInsertPDI.Columnas.OTROS + " TEXT, " +
                ContractInsertPDI.Columnas.OBJ_CATEGORIA + " TEXT, " +
                ContractInsertPDI.Columnas.PART_CATEGORIA + " TEXT, " +
                ContractInsertPDI.Columnas.FOTO + " TEXT, " +
                ContractInsertPDI.Columnas.CANAL + " TEXT, " +
                ContractInsertPDI.Columnas.POS_NAME + " TEXT, " +
                ContractInsertPDI.Columnas.PLATAFORMA + " TEXT, " +
                Constantes.ID_REMOTA + " TEXT UNIQUE," +
                Constantes.ESTADO + " INTEGER NOT NULL DEFAULT "+ Constantes.ESTADO_OK+"," +
                Constantes.PENDIENTE_INSERCION + " INTEGER NOT NULL DEFAULT 0)";
        database.execSQL(cmd);
    }

    private void createTableJustificacion(SQLiteDatabase database){
        String cmd = "CREATE TABLE " + ContractJustificacion.JUSTIFICACIONES + " (" +
                ContractJustificacion.Columnas._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ContractJustificacion.Columnas.JUSTIFICACION + " TEXT, " +
                Constantes.ID_REMOTA + " TEXT UNIQUE," +
                Constantes.ESTADO + " INTEGER NOT NULL DEFAULT "+ Constantes.ESTADO_OK +"," +
                Constantes.PENDIENTE_INSERCION + " INTEGER NOT NULL DEFAULT 0)";
        database.execSQL(cmd);
    }

    private void createTableTipoPrecios(SQLiteDatabase database){
        String cmd = "CREATE TABLE " + ContractTipoPrecios.TIPO_PRECIOS + " (" +
                ContractTipoPrecios.Columnas._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ContractTipoPrecios.Columnas.CANAL + " TEXT, " +
                ContractTipoPrecios.Columnas.SUBCANAL + " TEXT, " +
                ContractTipoPrecios.Columnas.TIPO_PRECIO + " TEXT, " +
                Constantes.ID_REMOTA + " TEXT UNIQUE," +
                Constantes.ESTADO + " INTEGER NOT NULL DEFAULT "+ Constantes.ESTADO_OK +"," +
                Constantes.PENDIENTE_INSERCION + " INTEGER NOT NULL DEFAULT 0)";
        database.execSQL(cmd);
    }

    private void createTableMarcasBlancas(SQLiteDatabase database){
        String cmd = "CREATE TABLE " + ContractMarcasBlancas.MARCAS_BLANCAS + " (" +
                ContractMarcasBlancas.Columnas._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ContractMarcasBlancas.Columnas.MARCAS + " TEXT, " +
                Constantes.ID_REMOTA + " TEXT UNIQUE," +
                Constantes.ESTADO + " INTEGER NOT NULL DEFAULT "+ Constantes.ESTADO_OK +"," +
                Constantes.PENDIENTE_INSERCION + " INTEGER NOT NULL DEFAULT 0)";
        database.execSQL(cmd);
    }

    private void createTableInsertPromocion(SQLiteDatabase database) {
        String cmd = "CREATE TABLE " + ContractInsertPromocion.INSERT_PROMO + " (" +
                ContractInsertPromocion.Columnas._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ContractInsertPromocion.Columnas.PHARMA_ID + " TEXT, " +
                ContractInsertPromocion.Columnas.CODIGO + " TEXT, " +
                ContractInsertPromocion.Columnas.USUARIO + " TEXT, " +
                ContractInsertPromocion.Columnas.SUPERVISOR + " TEXT, " +
                ContractInsertPromocion.Columnas.FECHA + " TEXT, " +
                ContractInsertPromocion.Columnas.HORA + " TEXT, " +
                ContractInsertPromocion.Columnas.CATEGORIA + " TEXT, " +
                ContractInsertPromocion.Columnas.SUBCATEGORIA + " TEXT, " +
                ContractInsertPromocion.Columnas.MARCA + " TEXT, " +
                ContractInsertPromocion.Columnas.CANAL  + " TEXT, " +
                ContractInsertPromocion.Columnas.TIPO_PROMOCION  + " TEXT, " +
                ContractInsertPromocion.Columnas.DESCRIPCION_PROMOCION  + " TEXT, " +
                ContractInsertPromocion.Columnas.PRODUCTO  + " TEXT, " +
                ContractInsertPromocion.Columnas.MECANICA  + " TEXT, " +
                ContractInsertPromocion.Columnas.INI_PROMO + " TEXT, " +
                ContractInsertPromocion.Columnas.FIN_PROMO + " TEXT, " +
                ContractInsertPromocion.Columnas.AGOTAR_STOCK + " TEXT, " +
                ContractInsertPromocion.Columnas.PVC_ANTERIOR + " TEXT, " +
                ContractInsertPromocion.Columnas.PVC_ACTUAL + " TEXT, " +
                ContractInsertPromocion.Columnas.MARGEN_DSCTO + " TEXT, " +
                ContractInsertPromocion.Columnas.FOTO + " TEXT, " +
                ContractInsertPromocion.Columnas.MANUFACTURER + " TEXT, " +
                ContractInsertPromocion.Columnas.SKU + " TEXT, " +
                ContractInsertPromocion.Columnas.POS_NAME + " TEXT, " +
                ContractInsertPromocion.Columnas.PLATAFORMA + " TEXT, " +
                Constantes.ID_REMOTA + " TEXT UNIQUE," +
                Constantes.ESTADO + " INTEGER NOT NULL DEFAULT "+ Constantes.ESTADO_OK+"," +
                Constantes.PENDIENTE_INSERCION + " INTEGER NOT NULL DEFAULT 0)";
        database.execSQL(cmd);
    }


    private void createTableInsertFlooring(SQLiteDatabase database) {
        String cmd = "CREATE TABLE " + InsertFlooring.INSERT_FLOORING + " (" +
                InsertFlooring.Columnas._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                InsertFlooring.Columnas.PHARMA_ID + " TEXT, " +
                InsertFlooring.Columnas.CODIGO + " TEXT, " +
                InsertFlooring.Columnas.USUARIO + " TEXT, " +
                InsertFlooring.Columnas.SUPERVISOR + " TEXT, " +
                InsertFlooring.Columnas.FECHA + " TEXT, " +
                InsertFlooring.Columnas.HORA + " TEXT, " +
                InsertFlooring.Columnas.SECTOR + " TEXT, " +
                InsertFlooring.Columnas.CATEGORIA + " TEXT, " +
                InsertFlooring.Columnas.SUBCATEGORIA + " TEXT, " +
                InsertFlooring.Columnas.PRESENTACION + " TEXT, " +
                InsertFlooring.Columnas.BRAND + " TEXT, " +
                InsertFlooring.Columnas.CONTENIDO + " TEXT, " +
                InsertFlooring.Columnas.SKU_CODE + " TEXT, " +
                InsertFlooring.Columnas.INVENTARIOS + " TEXT, " +
                InsertFlooring.Columnas.SEMANA + " TEXT, " +
                InsertFlooring.Columnas.SUGERIDOS + " TEXT, " +
                InsertFlooring.Columnas.TIPO + " TEXT, " +
                InsertFlooring.Columnas.ENTREGA + " TEXT, " +
                InsertFlooring.Columnas.CAUSAL + " TEXT, " +
                InsertFlooring.Columnas.OTROS + " TEXT, " +
                InsertFlooring.Columnas.FECHA_CADUCIDAD + " TEXT, " +
                InsertFlooring.Columnas.POS_NAME + " TEXT, " +
                InsertFlooring.Columnas.PLATAFORMA + " TEXT, " +
                InsertFlooring.Columnas.FECHA_INVENTARIO + " TEXT, " +
                Constantes.ID_REMOTA + " TEXT UNIQUE," +
                Constantes.ESTADO + " INTEGER NOT NULL DEFAULT "+ Constantes.ESTADO_OK+"," +
                Constantes.PENDIENTE_INSERCION + " INTEGER NOT NULL DEFAULT 0)";
        database.execSQL(cmd);
    }

    private void createTableInsertEvidencias(SQLiteDatabase database){
        String cmd = "CREATE TABLE " + ContractInsertEvidencias.INSERT_EVIDENCIAS + " (" +
                ContractInsertEvidencias.Columnas._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ContractInsertEvidencias.Columnas.PHARMA_ID + " TEXT, " +
                ContractInsertEvidencias.Columnas.CODIGO + " TEXT, " +
                ContractInsertEvidencias.Columnas.USUARIO + " TEXT, " +
                ContractInsertEvidencias.Columnas.CATEGORIA + " TEXT, " +
                ContractInsertEvidencias.Columnas.COMENTARIO + " TEXT, " +
                ContractInsertEvidencias.Columnas.FOTO_ANTES + " TEXT, " +
                ContractInsertEvidencias.Columnas.FOTO_DESPUES + " TEXT, " +
                ContractInsertEvidencias.Columnas.FECHA + " TEXT, " +
                ContractInsertEvidencias.Columnas.HORA + " TEXT, " +
                Constantes.ID_REMOTA + " TEXT UNIQUE," +
                Constantes.ESTADO + " INTEGER NOT NULL DEFAULT "+ Constantes.ESTADO_OK+"," +
                Constantes.PENDIENTE_INSERCION + " INTEGER NOT NULL DEFAULT 0)";
        database.execSQL(cmd);
    }

    private void createTableInsertPdvFotografico(SQLiteDatabase database){
        String cmd = "CREATE TABLE " + ContractInsertPdvFotografico.INSERT_PVD_FOTOGRAFICO + " (" +
                ContractInsertPdvFotografico.Columnas._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ContractInsertPdvFotografico.Columnas.PHARMA_ID + " TEXT, " +
                ContractInsertPdvFotografico.Columnas.CODIGO + " TEXT, " +
                ContractInsertPdvFotografico.Columnas.USUARIO + " TEXT, " +
                ContractInsertPdvFotografico.Columnas.SUPERVISOR + " TEXT, " +
                ContractInsertPdvFotografico.Columnas.FECHA + " TEXT, " +
                ContractInsertPdvFotografico.Columnas.HORA + " TEXT, " +
                ContractInsertPdvFotografico.Columnas.CANAL + " TEXT, " +
                ContractInsertPdvFotografico.Columnas.CODIGO_PDV + " TEXT, " +
                ContractInsertPdvFotografico.Columnas.MERCADERISTA + " TEXT, " +
                ContractInsertPdvFotografico.Columnas.FOTO + " TEXT, " +
                Constantes.ID_REMOTA + " TEXT UNIQUE," +
                Constantes.ESTADO + " INTEGER NOT NULL DEFAULT "+ Constantes.ESTADO_OK+"," +
                Constantes.PENDIENTE_INSERCION + " INTEGER NOT NULL DEFAULT 0)";
        database.execSQL(cmd);
    }

    private void createTableInsertImpulso(SQLiteDatabase database) {
        String cmd = "CREATE TABLE " + ContractInsertImpulso.INSERT_IMPUSLO + " (" +
                ContractInsertImpulso.Columnas._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ContractInsertImpulso.Columnas.PHARMA_ID + " TEXT, " +
                ContractInsertImpulso.Columnas.CODIGO + " TEXT, " +
                ContractInsertImpulso.Columnas.USUARIO + " TEXT, " +
                ContractInsertImpulso.Columnas.SUPERVISOR + " TEXT, " +
                ContractInsertImpulso.Columnas.FECHA + " TEXT, " +
                ContractInsertImpulso.Columnas.HORA + " TEXT, " +
                ContractInsertImpulso.Columnas.CATEGORIA + " TEXT, " +
                ContractInsertImpulso.Columnas.BRAND + " TEXT, " +
                ContractInsertImpulso.Columnas.SKU_CODE + " TEXT, " +
                ContractInsertImpulso.Columnas.CANTIDAD_ASIGNADA + " TEXT, " +
                ContractInsertImpulso.Columnas.CANTIDAD_VENDIDA + " TEXT, " +
                ContractInsertImpulso.Columnas.CANTIDAD_ADICIONAL + " TEXT, " +
                ContractInsertImpulso.Columnas.CUMPLIMIENTO + " TEXT, " +
                ContractInsertImpulso.Columnas.IMPULSADORA + " TEXT, " +
                ContractInsertImpulso.Columnas.OBSERVACION + " TEXT, " +
                ContractInsertImpulso.Columnas.FOTO + " TEXT, " +
                ContractInsertImpulso.Columnas.POS_NAME + " TEXT, " +
                ContractInsertImpulso.Columnas.PRECIO_VENTA + " TEXT, " +
                ContractInsertImpulso.Columnas.ALERTA_STOCK + " TEXT, " +
                ContractInsertImpulso.Columnas.PLATAFORMA + " TEXT, " +
                Constantes.ID_REMOTA + " TEXT UNIQUE," +
                Constantes.ESTADO + " INTEGER NOT NULL DEFAULT "+ Constantes.ESTADO_OK+"," +
                Constantes.PENDIENTE_INSERCION + " INTEGER NOT NULL DEFAULT 0)";
        database.execSQL(cmd);
    }

    private void createTableInsertExh(SQLiteDatabase database) {
        String cmd = "CREATE TABLE " + ContractInsertExh.INSERT_EXH + " (" +
                ContractInsertExh.Columnas._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ContractInsertExh.Columnas.PHARMA_ID + " TEXT, " +
                ContractInsertExh.Columnas.CODIGO + " TEXT, " +
                ContractInsertExh.Columnas.USUARIO + " TEXT, " +
                ContractInsertExh.Columnas.SUPERVISOR + " TEXT, " +
                ContractInsertExh.Columnas.FECHA + " TEXT, " +
                ContractInsertExh.Columnas.HORA + " TEXT, " +
                ContractInsertExh.Columnas.SECTOR + " TEXT, " +
                ContractInsertExh.Columnas.CATEGORIA + " TEXT, " +
                ContractInsertExh.Columnas.SUBCATEGORIA + " TEXT, " +
                ContractInsertExh.Columnas.SEGMENTO + " TEXT, " +
                ContractInsertExh.Columnas.BRAND + " TEXT, " +
                ContractInsertExh.Columnas.TIPO_EXH + " TEXT, " +
                ContractInsertExh.Columnas.ZONA_EX + " TEXT, " +
                ContractInsertExh.Columnas.NIVEL + " TEXT, " +
                ContractInsertExh.Columnas.TIPO + " TEXT, " +
                ContractInsertExh.Columnas.CONTRATADA + " TEXT, " +
                ContractInsertExh.Columnas.CONDICION + " TEXT, " +
                ContractInsertExh.Columnas.FOTO + " TEXT, " +
                ContractInsertExh.Columnas.POS_NAME + " TEXT, " +
                ContractInsertExh.Columnas.PLATAFORMA + " TEXT, " +
                Constantes.ID_REMOTA + " TEXT UNIQUE," +
                Constantes.ESTADO + " INTEGER NOT NULL DEFAULT "+ Constantes.ESTADO_OK+"," +
                Constantes.PENDIENTE_INSERCION + " INTEGER NOT NULL DEFAULT 0)";
        database.execSQL(cmd);
    }

    private void createTableInsertExhSesiones(SQLiteDatabase database) {
        String cmd = "CREATE TABLE " + ContractInsertExhSesion.INSERT_EXH_SESION + " (" +
                ContractInsertExhSesion.Columnas._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                ContractInsertExhSesion.Columnas.PHARMA_ID + " TEXT, " +
                ContractInsertExhSesion.Columnas.CODIGO + " TEXT, " +
                ContractInsertExhSesion.Columnas.USUARIO + " TEXT, " +
                ContractInsertExhSesion.Columnas.SUPERVISOR + " TEXT, " +
                ContractInsertExhSesion.Columnas.FECHA + " TEXT, " +
                ContractInsertExhSesion.Columnas.HORA + " TEXT, " +
                ContractInsertExhSesion.Columnas.SECTOR + " TEXT, " +
                ContractInsertExhSesion.Columnas.CATEGORIA + " TEXT, " +
                ContractInsertExhSesion.Columnas.BRAND + " TEXT, " +
                ContractInsertExhSesion.Columnas.TIPO_EXH + " TEXT, " +
                ContractInsertExhSesion.Columnas.ZONA_EX + " TEXT, " +
                ContractInsertExhSesion.Columnas.CONTRATADA + " TEXT, " +
                ContractInsertExhSesion.Columnas.CONDICION + " TEXT, " +
                ContractInsertExhSesion.Columnas.FOTO + " TEXT, " +
                Constantes.ID_REMOTA + " TEXT UNIQUE," +
                Constantes.ESTADO + " INTEGER NOT NULL DEFAULT "+ Constantes.ESTADO_OK+"," +
                Constantes.PENDIENTE_INSERCION + " INTEGER NOT NULL DEFAULT 0)";
        database.execSQL(cmd);
    }

    private void createTableInsertPDV(SQLiteDatabase database) {
        String cmd = "CREATE TABLE " + ContractInsertPdv.INSERT_PDV + " (" +
                ContractInsertPdv.Columnas._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ContractInsertPdv.Columnas.IDPDV + " TEXT, " +
                ContractInsertPdv.Columnas.ESTADOVISITA + " TEXT, " +
                ContractInsertPdv.Columnas.NOVEDADES + " TEXT, " +
                ContractInsertPdv.Columnas.FOTO + " TEXT, " +
                ContractInsertPdv.Columnas.FECHA + " TEXT, " +
                ContractInsertPdv.Columnas.HORA + " TEXT, " +
                Constantes.ID_REMOTA + " TEXT UNIQUE," +
                Constantes.ESTADO + " INTEGER NOT NULL DEFAULT "+ Constantes.ESTADO_OK+"," +
                Constantes.PENDIENTE_INSERCION + " INTEGER NOT NULL DEFAULT 0)";
        database.execSQL(cmd);
    }

    private void createTableInsertImplementacion(SQLiteDatabase database) {
        String cmd = "CREATE TABLE " + ContractInsertImplementacion.INSERT_IMPLEM + " (" +
                ContractInsertImplementacion.Columnas._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                ContractInsertImplementacion.Columnas.USUARIO + " TEXT, " +
                ContractInsertImplementacion.Columnas.FECHA + " TEXT, " +
                ContractInsertImplementacion.Columnas.HORA + " TEXT, " +
                ContractInsertImplementacion.Columnas.CIUDAD + " TEXT, " +
                ContractInsertImplementacion.Columnas.CANAL + " TEXT, " +
                ContractInsertImplementacion.Columnas.CLIENTE + " TEXT, " +
                ContractInsertImplementacion.Columnas.FORMATO + " TEXT, " +
                ContractInsertImplementacion.Columnas.ZONA + " TEXT, " +
                ContractInsertImplementacion.Columnas.PDV + " TEXT, " +
                ContractInsertImplementacion.Columnas.DIRECCION + " TEXT, " +
                ContractInsertImplementacion.Columnas.LOCAL + " TEXT, " +
                ContractInsertImplementacion.Columnas.LATITUD + " TEXT, " +
                ContractInsertImplementacion.Columnas.LONGITUD + " TEXT, " +
                ContractInsertImplementacion.Columnas.FOTO + " TEXT, " +

                Constantes.ID_REMOTA + " TEXT UNIQUE," +
                Constantes.ESTADO + " INTEGER NOT NULL DEFAULT "+ Constantes.ESTADO_OK+"," +
                Constantes.PENDIENTE_INSERCION + " INTEGER NOT NULL DEFAULT 0)";
        database.execSQL(cmd);
    }



    private void createTableInsertGps(SQLiteDatabase database) {
        String cmd = "CREATE TABLE " + ContractInsertGps.INSERT_GPS + " (" +
                ContractInsertGps.Columnas._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ContractInsertGps.Columnas.IDPDV + " TEXT, " +
                ContractInsertGps.Columnas.USUARIO + " TEXT, " +
                ContractInsertGps.Columnas.TIPO + " TEXT, " +
                ContractInsertGps.Columnas.VERSION + " TEXT, " +
                ContractInsertGps.Columnas.LATITUDE + " TEXT, " +
                ContractInsertGps.Columnas.LONGITUDE + " TEXT, " +
                ContractInsertGps.Columnas.FECHA + " TEXT, " +
                ContractInsertGps.Columnas.HORA + " TEXT, " +
                ContractInsertGps.Columnas.CAUSAL + " TEXT, " +
                ContractInsertGps.Columnas.FOTO + " TEXT, " +
                ContractInsertGps.Columnas.DISTANCIA + " TEXT, " +
                ContractInsertGps.Columnas.TIPO_RELEVO + " TEXT, " +
                ContractInsertGps.Columnas.POS_NAME + " TEXT, " +
                Constantes.ID_REMOTA_RUTA + " TEXT," +
                Constantes.ID_REMOTA + " TEXT UNIQUE," +
                Constantes.ESTADO + " INTEGER NOT NULL DEFAULT "+ Constantes.ESTADO_OK+"," +
                Constantes.PENDIENTE_INSERCION + " INTEGER NOT NULL DEFAULT 0)";
        database.execSQL(cmd);
    }

    private void createTableInsertAsistencia(SQLiteDatabase database) {
        String cmd = "CREATE TABLE " + ContractInsertAsistencia.INSERT_ASISTENCIA + " (" +
                ContractInsertAsistencia.Columnas._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ContractInsertAsistencia.Columnas.IDPDV + " TEXT, " +
                ContractInsertAsistencia.Columnas.USUARIO + " TEXT, " +
                ContractInsertAsistencia.Columnas.FOTO + " TEXT, " +
                ContractInsertAsistencia.Columnas.VERSION + " TEXT, " +
                ContractInsertAsistencia.Columnas.LATITUDE + " TEXT, " +
                ContractInsertAsistencia.Columnas.LONGITUDE + " TEXT, " +
                ContractInsertAsistencia.Columnas.FECHA + " TEXT, " +
                ContractInsertAsistencia.Columnas.HORA + " TEXT, " +
                ContractInsertAsistencia.Columnas.DISTANCIA + " TEXT, " +
                ContractInsertAsistencia.Columnas.POS_NAME + " TEXT, " +
                ContractInsertAsistencia.Columnas.BATERIA + " TEXT, " +
                ContractInsertAsistencia.Columnas.ESTADO_ASISTENCIA + " TEXT, " +
                ContractInsertAsistencia.Columnas.SUPERVISOR + " TEXT, " +
                Constantes.ID_REMOTA + " TEXT UNIQUE," +
                Constantes.ESTADO + " INTEGER NOT NULL DEFAULT "+ Constantes.ESTADO_OK+"," +
                Constantes.PENDIENTE_INSERCION + " INTEGER NOT NULL DEFAULT 0)";
        database.execSQL(cmd);
    }

    private void createTableInsertRastreo(SQLiteDatabase database) {
        String cmd = "CREATE TABLE " + ContractInsertRastreo.INSERT_GEO + " (" +
                ContractInsertRastreo.Columnas._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ContractInsertRastreo.Columnas.USUARIO + " TEXT, " +
                ContractInsertRastreo.Columnas.LATITUD + " TEXT, " +
                ContractInsertRastreo.Columnas.LONGITUD + " TEXT, " +
                ContractInsertRastreo.Columnas.FECHA + " TEXT, " +
                ContractInsertRastreo.Columnas.HORA + " TEXT, " +
                Constantes.ID_REMOTA + " TEXT UNIQUE," +
                Constantes.ESTADO + " INTEGER NOT NULL DEFAULT "+ Constantes.ESTADO_OK+"," +
                Constantes.PENDIENTE_INSERCION + " INTEGER NOT NULL DEFAULT 0)";
        database.execSQL(cmd);
    }

    private void createTableNotificacion(SQLiteDatabase database){
        String cmd = "CREATE TABLE " + ContractNotificacion.NOTIFICACION + " (" +
                ContractNotificacion.Columnas._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ContractNotificacion.Columnas.USER + " TEXT, " +
                ContractNotificacion.Columnas.SUPERVISOR + " TEXT, " +
                ContractNotificacion.Columnas.DESCRIPCION + " TEXT, " +
                ContractNotificacion.Columnas.FECHA + " TEXT, " +
                ContractNotificacion.Columnas.HORA + " TEXT, " +
                Constantes.ID_REMOTA + " TEXT UNIQUE," +
                Constantes.ESTADO + " INTEGER NOT NULL DEFAULT "+ Constantes.ESTADO_OK+"," +
                Constantes.PENDIENTE_INSERCION + " INTEGER NOT NULL DEFAULT 0)";
        database.execSQL(cmd);
    }

    private void createTableInsertInicial(SQLiteDatabase database) {
        String cmd = "CREATE TABLE " + ContractInsertInicial.INSERT_INICIAL + " (" +
                ContractInsertInicial.Columnas._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ContractInsertInicial.Columnas.IDPDV + " TEXT, " +
                ContractInsertInicial.Columnas.CODIGO + " TEXT, " +
                ContractInsertInicial.Columnas.TIPO + " TEXT, " +
                ContractInsertInicial.Columnas.DEALER + " TEXT, " +
                ContractInsertInicial.Columnas.UBICACION + " TEXT, " +
                ContractInsertInicial.Columnas.CORREO + " TEXT, " +
                ContractInsertInicial.Columnas.LATITUD + " TEXT, " +
                ContractInsertInicial.Columnas.LONGITUD + " TEXT, " +
                ContractInsertInicial.Columnas.FECHA + " TEXT, " +
                ContractInsertInicial.Columnas.HORA + " TEXT, " +
                Constantes.ID_REMOTA + " TEXT UNIQUE," +
                Constantes.ESTADO + " INTEGER NOT NULL DEFAULT "+ Constantes.ESTADO_OK+"," +
                Constantes.PENDIENTE_INSERCION + " INTEGER NOT NULL DEFAULT 0)";
        database.execSQL(cmd);
    }
    private void createTableCausalesAsistencia(SQLiteDatabase database) {
        String cmd = "CREATE TABLE " + ContractCausalesAsistencia.CAUSALES_ASISTENCIA + " (" +
                ContractCausalesAsistencia.Columnas._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ContractCausalesAsistencia.Columnas.DESCRIPCION + " TEXT, " +
                Constantes.ID_REMOTA + " TEXT UNIQUE," +
                Constantes.ESTADO + " INTEGER NOT NULL DEFAULT "+ Constantes.ESTADO_OK +"," +
                Constantes.PENDIENTE_INSERCION + " INTEGER NOT NULL DEFAULT 0)";
        database.execSQL(cmd);
    }
    private void createTableCausalesAsistenciaAtraso(SQLiteDatabase database) {
        String cmd = "CREATE TABLE " + ContractCausalesAsistenciaAtraso.CAUSALES_ASISTENCIA_ATRASO + " (" +
                ContractCausalesAsistenciaAtraso.Columnas._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ContractCausalesAsistenciaAtraso.Columnas.DESCRIPCION + " TEXT, " +
                Constantes.ID_REMOTA + " TEXT UNIQUE," +
                Constantes.ESTADO + " INTEGER NOT NULL DEFAULT "+ Constantes.ESTADO_OK +"," +
                Constantes.PENDIENTE_INSERCION + " INTEGER NOT NULL DEFAULT 0)";
        database.execSQL(cmd);
    }

    private void createTableInsertLogisticoRelevo(SQLiteDatabase database) {
        String cmd = "CREATE TABLE " + ContractInsertLogisticoRelevo.INSERT_LOGISTICO_RELEVO + " (" +
                ContractInsertLogisticoRelevo.Columnas._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ContractInsertLogisticoRelevo.Columnas.PHARMA_ID + " TEXT, " +
                ContractInsertLogisticoRelevo.Columnas.CODIGO + " TEXT, " +
                ContractInsertLogisticoRelevo.Columnas.USUARIO + " TEXT, " +

                ContractInsertLogisticoRelevo.Columnas.SUPERVISOR + " TEXT, " +
                ContractInsertLogisticoRelevo.Columnas.FECHA + " TEXT, " +
                ContractInsertLogisticoRelevo.Columnas.HORA + " TEXT, " +

                ContractInsertLogisticoRelevo.Columnas.CATEGORIA + " TEXT, " +
                ContractInsertLogisticoRelevo.Columnas.BRAND + " TEXT, " +
                ContractInsertLogisticoRelevo.Columnas.SKU_CODE + " TEXT, " +

                ContractInsertLogisticoRelevo.Columnas.PREGULAR + " TEXT, " +
                ContractInsertLogisticoRelevo.Columnas.CAUSAL + " TEXT, " +
                ContractInsertLogisticoRelevo.Columnas.TIPO_LOGISTICO + " TEXT, " +

                ContractInsertLogisticoRelevo.Columnas.FOTO + " TEXT, " +
                ContractInsertLogisticoRelevo.Columnas.COMENTARIO + " TEXT, " +
                ContractInsertLogisticoRelevo.Columnas.FECHA_PROD_CADUCADO + " TEXT, " +

                ContractInsertLogisticoRelevo.Columnas.FECHA_PROD_PROPENSO + " TEXT, " +
                Constantes.ID_REMOTA + " TEXT UNIQUE," +
                Constantes.ESTADO + " INTEGER NOT NULL DEFAULT "+ Constantes.ESTADO_OK+"," +
                Constantes.PENDIENTE_INSERCION + " INTEGER NOT NULL DEFAULT 0)";
        database.execSQL(cmd);
    }


    // Modificaciones

   /* private static final String DATABASE_ALTER_PDV_1 = "ALTER TABLE "
            + ContractPrecios.PRECIOS + " ADD COLUMN " + ContractPrecios.Columnas.TARGET + " TEXT;";*/

    /*
      **  OPERACIONES
     */

    public Boolean verificarLogin(String name, String pass) {
        // Select All Query
        String selectQuery = "SELECT  * FROM " + ContractPharmaValue.POS + " WHERE "+
                ContractPharmaValue.Columnas.USER + "=? AND " +
                ContractPharmaValue.Columnas.PASS + "=?";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{name, pass});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            return true;
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return false;
    }

    /*
     *  PUNTOVENTA
     */

    public String getIdPdv(String re, String sucursal, String user) {
        String id = "";
        /*String query = "SELECT * FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS_MAYO + " WHERE " +
              ContractPortafolioProductos.Columnas.PDV + "=? AND "+
                ContractPortafolioProductos.Columnas.POS_ID + "=? AND "+
                ContractPortafolioProductos.Columnas.USER + "=?";
        db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery(query, new String[]{re, sucursal, user});

        if (cursor != null && cursor.moveToFirst()) {
            id = cursor.getString(cursor.getColumnIndexOrThrow(Constantes.ID_REMOTA));
        }

        cursor.close();
        db.close();*/
        return id;
    }


    public String getIdPdv(String codigo) {
        String id = "";
        String query = "SELECT * FROM " + ContractPharmaValue.POS + " WHERE " +
                ContractPharmaValue.Columnas.POS_ID +" =? ";
        db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery(query, new String[]{codigo});

        if (cursor != null && cursor.moveToFirst()) {
            id = cursor.getString(cursor.getColumnIndexOrThrow(Constantes.ID_REMOTA));
        }

        cursor.close();
        db.close();
        return id;
    }

    public String getIdPdv2(String codigo) {
        String id = "";
        String query = "SELECT * FROM " + ContractPharmaValue.POS + " WHERE " +
                ContractPharmaValue.Columnas.POS_NAME +" =? ";
        db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery(query, new String[]{codigo});

        if (cursor != null && cursor.moveToFirst()) {
            id = cursor.getString(cursor.getColumnIndexOrThrow(ContractPharmaValue.Columnas.POS_ID));
        }

        cursor.close();
        db.close();
        return id;
    }

    public String getIdPdvInsert(String codigo,String fecha) {
        String id = "";
        String query = "SELECT * FROM " + ContractPharmaValue.POS + " WHERE " +
                ContractPharmaValue.Columnas.POS_ID +" =? AND "+
                ContractPharmaValue.Columnas.FECHA_VISITA +" =?";
        db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery(query, new String[]{codigo});

        if (cursor != null && cursor.moveToFirst()) {
            id = cursor.getString(cursor.getColumnIndexOrThrow(Constantes.ID_REMOTA));
        }

        cursor.close();
        db.close();
        return id;
    }

    public String getCityPdv(String codigo) {
        String id = "";
        String query = "SELECT " + ContractPharmaValue.Columnas.CIUDAD + " FROM " + ContractPharmaValue.POS + " WHERE " +
                ContractPharmaValue.Columnas.POS_ID +" =? ";
        db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery(query, new String[]{codigo});

        if (cursor != null && cursor.moveToFirst()) {
            id = cursor.getString(cursor.getColumnIndexOrThrow(ContractPharmaValue.Columnas.CIUDAD));
        }

        cursor.close();
        db.close();
        return id;
    }

    public String getCityPdvByName(String pos_name) {
        String id = "";
        String query = "SELECT " + ContractPharmaValue.Columnas.CIUDAD + " FROM " + ContractPharmaValue.POS + " WHERE " +
                ContractPharmaValue.Columnas.POS_NAME +" =? ";
        db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery(query, new String[]{pos_name});

        if (cursor != null && cursor.moveToFirst()) {
            id = cursor.getString(cursor.getColumnIndexOrThrow(ContractPharmaValue.Columnas.CIUDAD));
        }

        cursor.close();
        db.close();
        Log.i("CIUDAD F", id);
        return id;
    }

    public Base_pharma_value getInfoPDV(String codigo_pdv){
        Base_pharma_value bpv = new Base_pharma_value();

        String selectQuery = "SELECT * FROM " + ContractPharmaValue.POS +
                " WHERE "+ ContractPharmaValue.Columnas.POS_ID +"=?";

        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{codigo_pdv});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                bpv.setPos_id(cursor.getString(cursor.getColumnIndexOrThrow(ContractPharmaValue.Columnas.POS_ID)));
                bpv.setChannel(cursor.getString(cursor.getColumnIndexOrThrow(ContractPharmaValue.Columnas.CHANNEL)));
                bpv.setCustomer_owner(cursor.getString(cursor.getColumnIndexOrThrow(ContractPharmaValue.Columnas.CUSTOMER_OWNER)));
                bpv.setPos_name(cursor.getString(cursor.getColumnIndexOrThrow(ContractPharmaValue.Columnas.POS_NAME)));
                bpv.setPos_name_dpsm(cursor.getString(cursor.getColumnIndexOrThrow(ContractPharmaValue.Columnas.POS_NAME_DPSM)));
                bpv.setRegion(cursor.getString(cursor.getColumnIndexOrThrow(ContractPharmaValue.Columnas.REGION)));
           //     bpv.setKam(cursor.getString(cursor.getColumnIndexOrThrow(ContractPharmaValue.Columnas.KAM)));
                bpv.setProvince(cursor.getString(cursor.getColumnIndexOrThrow(ContractPharmaValue.Columnas.PROVINCIA)));
                bpv.setCity(cursor.getString(cursor.getColumnIndexOrThrow(ContractPharmaValue.Columnas.CIUDAD)));
                bpv.setZone(cursor.getString(cursor.getColumnIndexOrThrow(ContractPharmaValue.Columnas.ZONA)));
                bpv.setAddress(cursor.getString(cursor.getColumnIndexOrThrow(ContractPharmaValue.Columnas.DIRECCION)));
                bpv.setSupervisor(cursor.getString(cursor.getColumnIndexOrThrow(ContractPharmaValue.Columnas.SUPERVISOR)));
         //       bpv.setMercaderista(cursor.getString(cursor.getColumnIndexOrThrow(ContractPharmaValue.Columnas.MERCADERISTA)));
                bpv.setLatitud(cursor.getString(cursor.getColumnIndexOrThrow(ContractPharmaValue.Columnas.LATITUD)));
                bpv.setLongitud(cursor.getString(cursor.getColumnIndexOrThrow(ContractPharmaValue.Columnas.LONGITUD)));
           //     bpv.setFoto(cursor.getString(cursor.getColumnIndexOrThrow(ContractPharmaValue.Columnas.FOTO)));
           //     bpv.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(ContractPharmaValue.Columnas.STATUS)));
          //      bpv.setDpsm(cursor.getString(cursor.getColumnIndexOrThrow(ContractPharmaValue.Columnas.DPSM)));
                bpv.setTipo(cursor.getString(cursor.getColumnIndexOrThrow(ContractPharmaValue.Columnas.TIPO)));
        //        bpv.setMerchandising(cursor.getString(cursor.getColumnIndexOrThrow(ContractPharmaValue.Columnas.MERCHANDISING)));
                bpv.setSubchannel(cursor.getString(cursor.getColumnIndexOrThrow(ContractPharmaValue.Columnas.SUBCHANNEL)));
                bpv.setChannel_segment(cursor.getString(cursor.getColumnIndexOrThrow(ContractPharmaValue.Columnas.CHANNEL_SEGMENT)));
                bpv.setFormat(cursor.getString(cursor.getColumnIndexOrThrow(ContractPharmaValue.Columnas.FORMAT)));
        //        bpv.setSales_executive(cursor.getString(cursor.getColumnIndexOrThrow(ContractPharmaValue.Columnas.SALES_EXECUTIVE)));

            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return bpv;
    }


    public String getChannelSegmentPdv(String codigo) {
        String id = "";
        String query = "SELECT * FROM " + ContractPharmaValue.POS + " WHERE " +
                ContractPharmaValue.Columnas.POS_ID +" =? ";
        db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery(query, new String[]{codigo});

        if (cursor != null && cursor.moveToFirst()) {
            id = cursor.getString(cursor.getColumnIndexOrThrow(ContractPharmaValue.Columnas.CHANNEL));
        }

        cursor.close();
        db.close();
        return id;
    }

    public String getChannelSegmentPreguntas(String usuario) {
        String id = "";
        String query = "SELECT " + ContractPharmaValue.Columnas.CHANNEL + " FROM " + ContractPharmaValue.POS +
                " INNER JOIN " + ContractPreguntas.TABLE_QUEST +
                " ON " + ContractPreguntas.Columnas.KEY_CANAL + "=" + ContractPharmaValue.Columnas.CHANNEL +
                " WHERE " + ContractPharmaValue.Columnas.USER +" =? ";
        db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery(query, new String[]{usuario});

        if (cursor != null && cursor.moveToFirst()) {
            id = cursor.getString(cursor.getColumnIndexOrThrow(ContractPharmaValue.Columnas.CHANNEL));
        }

        cursor.close();
        db.close();
        return id;
    }
//zone obtener
    public String getZonePdv(String codigo) {
        String id = "";
        String query = "SELECT * FROM " + ContractPharmaValue.POS + " WHERE " +
                ContractPharmaValue.Columnas.POS_ID +" =? ";
        db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery(query, new String[]{codigo});

        if (cursor != null && cursor.moveToFirst()) {
            id = cursor.getString(cursor.getColumnIndexOrThrow(ContractPharmaValue.Columnas.ZONA));
        }

        cursor.close();
        db.close();
        return id;
    }

    public String getSubcanalPdv(String codigo) {
        String id = "";
        String query = "SELECT * FROM " + ContractPharmaValue.POS + " WHERE " +
                ContractPharmaValue.Columnas.POS_ID +" =? ";
        db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery(query, new String[]{codigo});

        if (cursor != null && cursor.moveToFirst()) {
            id = cursor.getString(cursor.getColumnIndexOrThrow(ContractPharmaValue.Columnas.SUBCHANNEL));
        }

        cursor.close();
        db.close();
        return id;
    }

    public String getFormatPdv(String codigo) {
        String id = "";
        String query = "SELECT * FROM " + ContractPharmaValue.POS + " WHERE " +
                ContractPharmaValue.Columnas.POS_ID +" =? ";
        db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery(query, new String[]{codigo});

        if (cursor != null && cursor.moveToFirst()) {
            id = cursor.getString(cursor.getColumnIndexOrThrow(ContractPharmaValue.Columnas.CHANNEL));
        }

        cursor.close();
        db.close();
        return id;
    }

    public String getDireccionPdv(String codigo) {
        String id = "";
        String query = "SELECT * FROM " + ContractPharmaValue.POS + " WHERE " +
                ContractPharmaValue.Columnas.POS_ID +" =? ";
        db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery(query, new String[]{codigo});

        if (cursor != null && cursor.moveToFirst()) {
            id = cursor.getString(cursor.getColumnIndexOrThrow(ContractPharmaValue.Columnas.DIRECCION));
        }

        cursor.close();
        db.close();
        return id;
    }

    public String getPosNamePdv(String codigo) {
        String id = "";
        String query = "SELECT * FROM " + ContractPharmaValue.POS + " WHERE " +
                ContractPharmaValue.Columnas.POS_ID +" =? ";
        db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery(query, new String[]{codigo});

        if (cursor != null && cursor.moveToFirst()) {
            id = cursor.getString(cursor.getColumnIndexOrThrow(ContractPharmaValue.Columnas.POS_NAME));
        }

        cursor.close();
        db.close();
        return id;
    }

    public String getChannelPdv(String codigo) {
        String id = "";
        String query = "SELECT * FROM " + ContractPharmaValue.POS + " WHERE " +
                ContractPharmaValue.Columnas.POS_ID +" =? ";
        db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery(query, new String[]{codigo});

        if (cursor != null && cursor.moveToFirst()) {
            id = cursor.getString(cursor.getColumnIndexOrThrow(ContractPharmaValue.Columnas.CHANNEL));
        }

        cursor.close();
        db.close();
        return id;
    }


    public String getSegment1Valores(String sku) {
        String id = "";
        String query = "SELECT * FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " +
                ContractPortafolioProductos.Columnas.SKU +" =? ";
        db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery(query, new String[]{sku});

        if (cursor != null && cursor.moveToFirst()) {
            id = cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.SEGMENTO));
        }

        cursor.close();
        db.close();
        return id;
    }


    public String getManufacterPromocionBySku(String sku) {
        String id = "";
        String query = "SELECT DISTINCT " + ContractPortafolioProductos.Columnas.FABRICANTE + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " +
                ContractPortafolioProductos.Columnas.SKU +" =? ";
        db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery(query, new String[]{sku});

        if (cursor != null && cursor.moveToFirst()) {
            id = cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.FABRICANTE));
        }

        cursor.close();
        db.close();
        return id;
    }

    public String getPlataformaBySku(String sku) {
        String id = "";
        String query = "SELECT DISTINCT " + ContractPortafolioProductos.Columnas.PLATAFORMA + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " +
                ContractPortafolioProductos.Columnas.SKU +" =? ";
        db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery(query, new String[]{sku});

        if (cursor != null && cursor.moveToFirst()) {
            id = cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.PLATAFORMA));
        }

        cursor.close();
        db.close();
        return id;
    }

    public String getCategoriaBySku(String sku) {
        String id = "";
        String query = "SELECT DISTINCT " + ContractPortafolioProductos.Columnas.SECTOR + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " +
                ContractPortafolioProductos.Columnas.SKU +" =? ";
        db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery(query, new String[]{sku});

        if (cursor != null && cursor.moveToFirst()) {
            id = cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.SECTOR));
        }

        cursor.close();
        db.close();
        return id;
    }

    public String getPlataformaBySkuCanjes(String sku) {
        String id = "";
        String query = "SELECT DISTINCT " + ContractRotacion.Columnas.PLATAFORMA + " FROM " + ContractRotacion.ROTACION + " WHERE " +
                ContractRotacion.Columnas.PRODUCTO +" =? ";
        db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery(query, new String[]{sku});

        if (cursor != null && cursor.moveToFirst()) {
            id = cursor.getString(cursor.getColumnIndexOrThrow(ContractRotacion.Columnas.PLATAFORMA));
        }

        cursor.close();
        db.close();
        return id;
    }


    public String getPlataformaByMarcaExh(String categoria, String marca) {
        String id = "";
        String query = "SELECT DISTINCT " + ContractPortafolioProductos.Columnas.PLATAFORMA +
                " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " +
                ContractPortafolioProductos.Columnas.SECTOR +" =? AND " +
                ContractPortafolioProductos.Columnas.MARCA +" =? ";
        db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery(query, new String[]{categoria, marca});

        if (cursor != null && cursor.moveToFirst()) {
            id = cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.PLATAFORMA));
        }

        cursor.close();
        db.close();
        return id;
    }




    public String getPlataformaByMarca(String categoria, String subcategoria, String marca) {
        String id = "";
        Cursor cursor = null;

        if (categoria.equalsIgnoreCase("Pastas") && subcategoria.equalsIgnoreCase("Todos")){

            String query = "SELECT DISTINCT " + ContractPortafolioProductos.Columnas.PLATAFORMA +
                    " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " +
                    ContractPortafolioProductos.Columnas.SECTOR +" =? AND " +
                    ContractPortafolioProductos.Columnas.MARCA +" =? ";
            db = this.getReadableDatabase();
            cursor =  db.rawQuery(query, new String[]{categoria ,marca});

        }else{
            String query = "SELECT DISTINCT " + ContractPortafolioProductos.Columnas.PLATAFORMA +
                    " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " +
                    ContractPortafolioProductos.Columnas.SECTOR +" =? AND " +
                  //  ContractPortafolioProductos.Columnas.CATEGORY +" =? AND " +
                    ContractPortafolioProductos.Columnas.MARCA +" =? ";
            db = this.getReadableDatabase();
            cursor =  db.rawQuery(query, new String[]{categoria ,/*subcategoria, */marca});
        }




        if (cursor != null && cursor.moveToFirst()) {
            id = cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.PLATAFORMA));
        }

        cursor.close();
        db.close();
        return id;
    }

    public String getPlataformaForOtros(String categoria, String subcategoria) {
        String id = "";
        Cursor cursor = null;

        if (categoria.equalsIgnoreCase("Pastas") && subcategoria.equalsIgnoreCase("Todos")){

            String query = "SELECT DISTINCT " + ContractPortafolioProductos.Columnas.PLATAFORMA +
                    " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " +
                    ContractPortafolioProductos.Columnas.SECTOR +" =?";
            db = this.getReadableDatabase();
            cursor =  db.rawQuery(query, new String[]{categoria});

        }else{
            String query = "SELECT DISTINCT " + ContractPortafolioProductos.Columnas.PLATAFORMA +
                    " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " +
                    ContractPortafolioProductos.Columnas.SECTOR +" =? ";
                 //   ContractPortafolioProductos.Columnas.CATEGORY +" =?";
            db = this.getReadableDatabase();
            cursor =  db.rawQuery(query, new String[]{categoria /*,subcategoria*/});
        }




        if (cursor != null && cursor.moveToFirst()) {
            id = cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.PLATAFORMA));
        }

        cursor.close();
        db.close();
        return id;
    }

    public String getPlataformaByMarcaPDI(String categoria, String marca) {
        String id = "";
        String query = "SELECT " + ContractPDI.Columnas.PLATAFORMA + ", " +
                ContractPDI.Columnas.MARCA + " || ' ' || " + ContractPDI.Columnas.SUBCATEGORIA + " AS " + ContractPDI.Columnas.MARCA_FULL +
                " FROM " + ContractPDI.PDI + " WHERE " +
                ContractPDI.Columnas.CATEGORIA + "=? AND " +
                ContractPDI.Columnas.MARCA_FULL + "=? " +
                " GROUP BY " + ContractPDI.Columnas.PLATAFORMA;
        db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery(query, new String[]{categoria, marca});

        if (cursor != null && cursor.moveToFirst()) {
            id = cursor.getString(cursor.getColumnIndexOrThrow(ContractPDI.Columnas.PLATAFORMA));
        }

        cursor.close();
        db.close();
        return id;
    }

    public String getSegment1Sugeridos(String sku) {
        String id = "";
        String query = "SELECT * FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " +
                ContractPortafolioProductos.Columnas.SKU +" =? ";
        db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery(query, new String[]{sku});

        if (cursor != null && cursor.moveToFirst()) {
            id = cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.SEGMENTO));
        }

        cursor.close();
        db.close();
        return id;
    }


    public void eliminarExh(String codigo) {
        String query = "DELETE FROM " + ContractInsertExh.INSERT_EXH + " WHERE " +
                Constantes.PENDIENTE_INSERCION +" =? ";
        db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery(query, new String[]{codigo});
        db.execSQL(query);
        cursor.close();
        db.close();
    }

    public void eliminarPrductosPvc() {
        String query = "DELETE FROM " + ContractPreciosPvc.PRECIOS_PVC;
        db = this.getWritableDatabase();
        Cursor cursor =  db.rawQuery(query, new String[]{});
        db.execSQL(query);
    }

    public String getSegment2Valores(String sku) {
        String id = "";
        String query = "SELECT * FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " +
                ContractPortafolioProductos.Columnas.SKU +" =? ";
        db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery(query, new String[]{sku});

        if (cursor != null && cursor.moveToFirst()) {
            id = cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.SEGMENTO));
        }

        cursor.close();
        db.close();
        return id;
    }

    public String getTamanoValores(String sku) {
        String id = "";
        String query = "SELECT * FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " +
                ContractPortafolioProductos.Columnas.SKU +" =? ";
        db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery(query, new String[]{sku});

        if (cursor != null && cursor.moveToFirst()) {
            id = cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.MARCA));
        }

        cursor.close();
        db.close();
        return id;
    }

    public String getCantidadValores(String sku) {
        String id = "";
        String query = "SELECT * FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " +
                ContractPortafolioProductos.Columnas.SKU +" =? ";
        db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery(query, new String[]{sku});

        if (cursor != null && cursor.moveToFirst()) {
            id = cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.FABRICANTE));
        }

        cursor.close();
        db.close();
        return id;
    }

    public String getSubcategoryPrecios(String sku) {
        String id = "";
        String query = "SELECT * FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " +
                ContractPortafolioProductos.Columnas.SKU +" =? ";
        db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery(query, new String[]{sku});

        if (cursor != null && cursor.moveToFirst()) {
            id = cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.SUBCATEGORIA));
        }

        cursor.close();
        db.close();
        return id;
    }

    public String getFabricantePrecios(String sku) {
        String id = "";
        String query = "SELECT * FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " +
                ContractPortafolioProductos.Columnas.SKU +" =? ";
        db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery(query, new String[]{sku});

        if (cursor != null && cursor.moveToFirst()) {
            id = cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.PRESENTACION));
        }

        cursor.close();
        db.close();
        return id;
    }

    public String getContenidoPrecios(String sku) {
        String id = "";
        String query = "SELECT * FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " +
                ContractPortafolioProductos.Columnas.SKU +" =? ";
        db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery(query, new String[]{sku});

        if (cursor != null && cursor.moveToFirst()) {
            id = cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.CONTENIDO));
        }

        cursor.close();
        db.close();
        return id;
    }



    //new mpin para cuando es sku
    public String getContenidoPropensos(String sku, String cadenas) {
        String id = "";
        String selectQuery = "";


         if (cadenas.contains("KYWI")) {
             selectQuery = "SELECT * FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " +
                     ContractPortafolioProductos.Columnas.VARIANTE1 +" =? ";
         }

         else {
             selectQuery = "SELECT * FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " +
                     ContractPortafolioProductos.Columnas.SKU +" =? ";

         }

        db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery(selectQuery, new String[]{sku});

        if (cursor != null && cursor.moveToFirst()) {
            id = cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.CONTENIDO));
        }





        cursor.close();
        db.close();
        return id;
    }


//mpin
public String getDolarPropensos(String sku, String cadena) { // el nuevo paraaetro para cadena
    String id = "";
    String selectQuery = ""; //

    if (cadena.contains("KYWI")) {
        selectQuery = "SELECT " + ContractPortafolioProductos.Columnas.DOLAR +
                " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS +
                " WHERE " + ContractPortafolioProductos.Columnas.VARIANTE1 + " =?"; // Usamos VARIANTE1
    } else {
        selectQuery = "SELECT " + ContractPortafolioProductos.Columnas.DOLAR +
                " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS +
                " WHERE " + ContractPortafolioProductos.Columnas.SKU + " =?"; // Usamos SKU
    }

    db = this.getReadableDatabase();
    Cursor cursor = db.rawQuery(selectQuery, new String[]{sku});

    if (cursor != null && cursor.moveToFirst()) {
        id = cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.DOLAR));
    }

    cursor.close();
    db.close();
    return id;
}


    public String getSectorPrecios(String sku) {
        String id = "";
        String query = "SELECT * FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " +
                ContractPortafolioProductos.Columnas.SKU +" =? ";
        db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery(query, new String[]{sku});

        if (cursor != null && cursor.moveToFirst()) {
            id = cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.SECTOR));
        }

        cursor.close();
        db.close();
        return id;
    }

    public String getCantidadPrecios(String sku) {
        String id = "";
        String query = "SELECT * FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " +
                ContractPortafolioProductos.Columnas.SKU +" =? ";
        db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery(query, new String[]{sku});

        if (cursor != null && cursor.moveToFirst()) {
            id = cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.FABRICANTE));
        }

        cursor.close();
        db.close();
        return id;
    }

    public String getSegment1Flooring(String sku) {
        String id = "";
        String query = "SELECT * FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " +
                ContractPortafolioProductos.Columnas.SKU +" =? ";
        db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery(query, new String[]{sku});

        if (cursor != null && cursor.moveToFirst()) {
            id = cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.SUBCATEGORIA));
        }

        cursor.close();
        db.close();
        return id;
    }

    public String getSegment2Flooring(String sku) {
        String id = "";
        String query = "SELECT * FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " +
                ContractPortafolioProductos.Columnas.SKU +" =? ";
        db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery(query, new String[]{sku});

        if (cursor != null && cursor.moveToFirst()) {
            id = cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.SEGMENTO));
        }

        cursor.close();
        db.close();
        return id;
    }

    public String getTamanoFlooring(String sku) {
        String id = "";
        String query = "SELECT * FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " +
                ContractPortafolioProductos.Columnas.SKU +" =? ";
        db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery(query, new String[]{sku});

        if (cursor != null && cursor.moveToFirst()) {
            id = cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.MARCA));
        }

        cursor.close();
        db.close();
        return id;
    }

    public String getCantidadFlooring(String sku) {
        String id = "";
        String query = "SELECT * FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " +
                ContractPortafolioProductos.Columnas.SKU +" =? ";
        db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery(query, new String[]{sku});

        if (cursor != null && cursor.moveToFirst()) {
            id = cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.FABRICANTE));
        }

        cursor.close();
        db.close();
        return id;
    }

    public String getManufacturerShare(String brand) {
        String manufacturer = "";

        // Select All Query
        String selectQuery = "SELECT DISTINCT " + ContractPortafolioProductos.Columnas.FABRICANTE + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE "+
                ContractPortafolioProductos.Columnas.MARCA + "=?";
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{brand});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                manufacturer = (cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.FABRICANTE)));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return manufacturer;
    }

    public String getManufacturerOSA(String sector, String categoria, String brand) {
        String manufacturer = "";

        // Select All Query
        String selectQuery = "SELECT DISTINCT " + ContractPortafolioProductos.Columnas.FABRICANTE + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE "+
                ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                ContractPortafolioProductos.Columnas.CATEGORY + "=? AND " +
                ContractPortafolioProductos.Columnas.CONTENIDO + "=?";
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{sector,categoria,brand});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                manufacturer = (cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.FABRICANTE)));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return manufacturer;
    }

    public List<String> getCodigo(String user) {
        List<String> operadores = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT DISTINCT "+ ContractPharmaValue.Columnas.POS_ID + " FROM " + ContractPharmaValue.POS + " WHERE "+
                ContractPharmaValue.Columnas.USER +"=? ORDER BY " + ContractPharmaValue.Columnas.POS_ID + " ASC " ;
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{user});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractPharmaValue.Columnas.POS_ID)));
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }


    public ArrayList<Base_tipo_exh> getTipoExhByChannel(String canal) {
        ArrayList<Base_tipo_exh> operadores = new ArrayList<Base_tipo_exh>();

        // Select All Query
        String selectQuery = "SELECT * " + " FROM " + ContractTipoExh.TABLE_NAME + " WHERE "+
                ContractTipoExh.Columnas.CANAL +"=? ORDER BY " + ContractTipoExh.Columnas.EXHIBICION + " ASC " ;
        db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, new String[]{canal});

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {

                Base_tipo_exh tipo_exh = new Base_tipo_exh();
                tipo_exh.setCanal(c.getString(c.getColumnIndexOrThrow(ContractTipoExh.Columnas.CANAL)));
                tipo_exh.setExhibicion(c.getString(c.getColumnIndexOrThrow(ContractTipoExh.Columnas.EXHIBICION)));
                tipo_exh.setFoto(c.getString(c.getColumnIndexOrThrow(ContractTipoExh.Columnas.FOTO)));
                operadores.add(tipo_exh);
            } while (c.moveToNext());
        }
        // closing connection
        c.close();
        db.close();
        // returning lables
        return operadores;
    }


    public List<String> getTipoExhByChannel2(String canal) {
        List<String> operadores = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT DISTINCT " + ContractTipoExh.Columnas.EXHIBICION + "  FROM " + ContractTipoExh.TABLE_NAME + " WHERE "+
                ContractTipoExh.Columnas.CANAL +"=? ORDER BY " + ContractTipoExh.Columnas.EXHIBICION + " ASC " ;
        db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, new String[]{canal});

        operadores.add("Seleccione");
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                operadores.add(c.getString(c.getColumnIndexOrThrow(ContractTipoExh.Columnas.EXHIBICION)));
            } while (c.moveToNext());
        }


        // closing connection
        c.close();
        db.close();
        // returning lables
        return operadores;
    }



    public List<String> filtrarTipo(String exh) {
        List<String> operadores = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT DISTINCT " + ContractTipoExh.Columnas.TIPO + "  FROM " + ContractTipoExh.TABLE_NAME + " WHERE "+
                ContractTipoExh.Columnas.EXHIBICION +"=? ORDER BY " + ContractTipoExh.Columnas.EXHIBICION + " ASC " ;
        db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, new String[]{exh});

        operadores.add("Seleccione");
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                operadores.add(c.getString(c.getColumnIndexOrThrow(ContractTipoExh.Columnas.TIPO)));
            } while (c.moveToNext());
        }


        // closing connection
        c.close();
        db.close();
        // returning lables
        return operadores;
    }



    public ArrayList<BasePharmaValue> consultarBasePharmaValue(String user, String por_codigo, String descripcion_codigo, String por_nombre, String descripcion_nombre) {
        ArrayList<BasePharmaValue> operadores = new ArrayList<BasePharmaValue>();

        String selectQuery = "";

        if (por_codigo.equals("TODOS") || por_nombre.equals("TODOS")) {
            selectQuery = "SELECT * FROM " + ContractPharmaValue.POS + " WHERE "+
                    ContractPharmaValue.Columnas.USER +"=? ORDER BY " + ContractPharmaValue.Columnas.POS_ID + " ASC " ;
        }else{
            selectQuery = "SELECT * FROM " + ContractPharmaValue.POS + " WHERE "+
                    ContractPharmaValue.Columnas.USER +"=? AND " +
                    por_codigo +" LIKE '%" + descripcion_codigo + "%' AND " +
                    por_nombre +" LIKE '%" + descripcion_nombre + "%'" +
                    " ORDER BY " + ContractPharmaValue.Columnas.POS_ID + " ASC " ;
        }
        // Select All Query

        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{user});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                BasePharmaValue bpv = new BasePharmaValue();
                bpv.setId(cursor.getString(cursor.getColumnIndexOrThrow(Constantes.ID_REMOTA)));
                bpv.setPos_id(cursor.getString(cursor.getColumnIndexOrThrow(ContractPharmaValue.Columnas.POS_ID)));
                bpv.setPos_name(cursor.getString(cursor.getColumnIndexOrThrow(ContractPharmaValue.Columnas.POS_NAME)));
                bpv.setChannel(cursor.getString(cursor.getColumnIndexOrThrow(ContractPharmaValue.Columnas.CHANNEL)));
                bpv.setAddress(cursor.getString(cursor.getColumnIndexOrThrow(ContractPharmaValue.Columnas.DIRECCION)));
                bpv.setCity(cursor.getString(cursor.getColumnIndexOrThrow(ContractPharmaValue.Columnas.CIUDAD)));
                operadores.add(bpv);
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public ArrayList<BasePharmaValue> consultarBasePharmaValue2(String user, String text, String fecha) {
        ArrayList<BasePharmaValue> operadores = new ArrayList<BasePharmaValue>();

        String selectQuery = "";

        if (text.trim().isEmpty()) {
            selectQuery = "SELECT * FROM " + ContractPharmaValue.POS + " WHERE "+
                    ContractPharmaValue.Columnas.USER +"=? AND " +
                    ContractPharmaValue.Columnas.FECHA_VISITA +"=? " +
                    " ORDER BY " + ContractPharmaValue.Columnas.POS_ID + " ASC " ;
        }else{
            selectQuery = "SELECT * FROM " + ContractPharmaValue.POS + " WHERE "+
                    ContractPharmaValue.Columnas.USER +"=? AND " +
                    ContractPharmaValue.Columnas.FECHA_VISITA +"=? AND (" +
                    ContractPharmaValue.Columnas.POS_ID +" LIKE '%" + text + "%' OR " +
                    ContractPharmaValue.Columnas.POS_NAME +" LIKE '%" + text + "%' OR " +
                    ContractPharmaValue.Columnas.CHANNEL +" LIKE '%" + text + "%' OR " +
                    ContractPharmaValue.Columnas.DIRECCION +" LIKE '%" + text + "%' OR " +
                    ContractPharmaValue.Columnas.CIUDAD +" LIKE '%" + text + "%'" +
                    ") ORDER BY " + ContractPharmaValue.Columnas.POS_ID + " ASC " ;
        }
        // Select All Query
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{user, fecha});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                BasePharmaValue bpv = new BasePharmaValue();
                bpv.setId(cursor.getString(cursor.getColumnIndexOrThrow(Constantes.ID_REMOTA)));
                bpv.setPos_id(cursor.getString(cursor.getColumnIndexOrThrow(ContractPharmaValue.Columnas.POS_ID)));
                bpv.setPos_name(cursor.getString(cursor.getColumnIndexOrThrow(ContractPharmaValue.Columnas.POS_NAME)));
                bpv.setChannel(cursor.getString(cursor.getColumnIndexOrThrow(ContractPharmaValue.Columnas.CHANNEL)));
                bpv.setAddress(cursor.getString(cursor.getColumnIndexOrThrow(ContractPharmaValue.Columnas.DIRECCION)));
                bpv.setCity(cursor.getString(cursor.getColumnIndexOrThrow(ContractPharmaValue.Columnas.CIUDAD)));
                bpv.setTermometro(cursor.getString(cursor.getColumnIndexOrThrow(ContractPharmaValue.Columnas.TERMOMETRO)));
                bpv.setModulo(Constantes.MODULO_PUNTOS_PRINCIPAL);
                operadores.add(bpv);
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public ArrayList<BasePharmaValue> consultarBasePharmaValueTardio(String user, String text) {
        ArrayList<BasePharmaValue> operadores = new ArrayList<BasePharmaValue>();

        String selectQuery = "";

        if (text.trim().isEmpty()) {
            selectQuery = "SELECT * FROM " + ContractPharmaValue.POS + " WHERE "+
                    ContractPharmaValue.Columnas.USER +"=? " +
                    " GROUP BY " + ContractPharmaValue.Columnas.POS_ID +
                    " ORDER BY " + ContractPharmaValue.Columnas.POS_ID + " ASC " ;
        }else{
            selectQuery = "SELECT * FROM " + ContractPharmaValue.POS + " WHERE "+
                    ContractPharmaValue.Columnas.USER +"=? AND (" +
                    ContractPharmaValue.Columnas.POS_ID +" LIKE '%" + text + "%' OR " +
                    ContractPharmaValue.Columnas.POS_NAME +" LIKE '%" + text + "%' OR " +
                    ContractPharmaValue.Columnas.CHANNEL +" LIKE '%" + text + "%' OR " +
                    ContractPharmaValue.Columnas.DIRECCION +" LIKE '%" + text + "%' OR " +
                    ContractPharmaValue.Columnas.CIUDAD +" LIKE '%" + text + "%'" +
                    ") GROUP BY " + ContractPharmaValue.Columnas.POS_ID +
                    " ORDER BY " + ContractPharmaValue.Columnas.POS_ID + " ASC " ;
        }
        // Select All Query
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{user});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                BasePharmaValue bpv = new BasePharmaValue();
                bpv.setId(cursor.getString(cursor.getColumnIndexOrThrow(Constantes.ID_REMOTA)));
                bpv.setPos_id(cursor.getString(cursor.getColumnIndexOrThrow(ContractPharmaValue.Columnas.POS_ID)));
                bpv.setPos_name(cursor.getString(cursor.getColumnIndexOrThrow(ContractPharmaValue.Columnas.POS_NAME)));
                bpv.setChannel(cursor.getString(cursor.getColumnIndexOrThrow(ContractPharmaValue.Columnas.CHANNEL)));
                bpv.setAddress(cursor.getString(cursor.getColumnIndexOrThrow(ContractPharmaValue.Columnas.DIRECCION)));
                bpv.setCity(cursor.getString(cursor.getColumnIndexOrThrow(ContractPharmaValue.Columnas.CIUDAD)));
                bpv.setTermometro(cursor.getString(cursor.getColumnIndexOrThrow(ContractPharmaValue.Columnas.TERMOMETRO)));
                bpv.setModulo(Constantes.MODULO_PUNTOS_TARDIO);
                operadores.add(bpv);
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public List<String> getTipo(String user, String codigo) {
        List<String> operadores = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT DISTINCT " + ContractPharmaValue.Columnas.CHANNEL_SEGMENT  + " FROM " + ContractPharmaValue.POS + " WHERE " +
                ContractPharmaValue.Columnas.USER +"=? AND " +
                ContractPharmaValue.Columnas.POS_ID + "=? ";
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{user,codigo});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractPharmaValue.Columnas.CHANNEL_SEGMENT)));
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public List<String> getDealer(String user, String codigo, String tipo) {
        List<String> operadores = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT DISTINCT " + ContractPharmaValue.Columnas.ZONA + " FROM " + ContractPharmaValue.POS + " WHERE "+
                ContractPharmaValue.Columnas.USER +"=? AND " +
                ContractPharmaValue.Columnas.POS_ID + "=? AND " +
                ContractPharmaValue.Columnas.CHANNEL_SEGMENT + "=?";
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{user, codigo, tipo});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractPharmaValue.Columnas.ZONA)));
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public List<String> getUbicacion(String user, String codigo, String tipo, String dealer) {
        List<String> operadores = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT DISTINCT " + ContractPharmaValue.Columnas.SUBCHANNEL + " FROM " + ContractPharmaValue.POS + " WHERE "+
                ContractPharmaValue.Columnas.USER +"=? AND " +
                ContractPharmaValue.Columnas.POS_ID + "=? AND " +
                ContractPharmaValue.Columnas.CHANNEL_SEGMENT + "=? AND " +
                ContractPharmaValue.Columnas.ZONA + "=?";
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{user, codigo, tipo, dealer});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractPharmaValue.Columnas.SUBCHANNEL)));
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public List<String> getCorreo(String user, String codigo, String tipo, String dealer, String ubicacion) {
        List<String> operadores = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT DISTINCT " + ContractPharmaValue.Columnas.DIRECCION + " FROM " + ContractPharmaValue.POS + " WHERE "+
                ContractPharmaValue.Columnas.USER +"=? AND " +
                ContractPharmaValue.Columnas.POS_ID + "=? AND " +
                ContractPharmaValue.Columnas.CHANNEL_SEGMENT + "=? AND " +
                ContractPharmaValue.Columnas.ZONA + "=? AND " +
                ContractPharmaValue.Columnas.SUBCHANNEL +  "=?";

        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{user, codigo, tipo, dealer, ubicacion});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractPharmaValue.Columnas.DIRECCION)));
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public List<String> getCliente(String user, String codigo, String tipo, String dealer, String ubicacion) {
        List<String> operadores = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT DISTINCT " + ContractPharmaValue.Columnas.POS_NAME + " FROM " + ContractPharmaValue.POS + " WHERE "+
                ContractPharmaValue.Columnas.USER +"=? AND " +
                ContractPharmaValue.Columnas.POS_ID + "=? AND " +
                ContractPharmaValue.Columnas.CHANNEL_SEGMENT + "=? AND " +
                ContractPharmaValue.Columnas.ZONA + "=? AND " +
                ContractPharmaValue.Columnas.DIRECCION +  "=?";

        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{user, codigo, tipo, dealer, ubicacion});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractPharmaValue.Columnas.POS_NAME)));
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables

        return operadores;
    }

    public List<String> getZona(String user, String codigo, String tipo, String dealer, String ubicacion) {
        List<String> operadores = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT DISTINCT " + ContractPharmaValue.Columnas.CHANNEL + " FROM " + ContractPharmaValue.POS + " WHERE "+
                ContractPharmaValue.Columnas.USER +"=? AND " +
                ContractPharmaValue.Columnas.POS_ID + "=? AND " +
                ContractPharmaValue.Columnas.CHANNEL_SEGMENT + "=? AND " +
                ContractPharmaValue.Columnas.ZONA + "=? AND " +
                ContractPharmaValue.Columnas.POS_NAME +  "=?";

        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{user, codigo, tipo, dealer, ubicacion});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractPharmaValue.Columnas.CHANNEL)));
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables

        return operadores ;
    }

    public String getFormat(String codigo) {
        String operadores = "";

        // Select All Query
        String selectQuery = "SELECT DISTINCT " + ContractPharmaValue.Columnas.FORMAT + " FROM " + ContractPharmaValue.POS + " WHERE "+
                ContractPharmaValue.Columnas.POS_ID + "=?";

        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{codigo});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores = cursor.getString(cursor.getColumnIndexOrThrow(ContractPharmaValue.Columnas.FORMAT));
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables

        return operadores ;
    }

    public String getNombreComercial(String codigo) {
        String operadores = "";

        // Select All Query
        String selectQuery = "SELECT DISTINCT " + ContractPharmaValue.Columnas.CUSTOMER_OWNER + " FROM " + ContractPharmaValue.POS + " WHERE "+
                ContractPharmaValue.Columnas.POS_ID + "=?";

        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{codigo});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores = cursor.getString(cursor.getColumnIndexOrThrow(ContractPharmaValue.Columnas.CUSTOMER_OWNER));
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables

        return operadores ;
    }

    public String getPosNameDpsm(String codigo) {
        String operadores = "";

        // Select All Query
        String selectQuery = "SELECT DISTINCT " + ContractPharmaValue.Columnas.POS_NAME_DPSM + " FROM " + ContractPharmaValue.POS + " WHERE "+
                ContractPharmaValue.Columnas.POS_ID + "=?";

        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{codigo});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores = cursor.getString(cursor.getColumnIndexOrThrow(ContractPharmaValue.Columnas.POS_NAME_DPSM));
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables

        return operadores ;
    }

    public String getCelularPDV(String codigo) {
        String operadores = "";

        // Select All Query
        String selectQuery = "SELECT DISTINCT " + ContractPharmaValue.Columnas.NUMERO_CONTROLLER + " FROM " + ContractPharmaValue.POS + " WHERE "+
                ContractPharmaValue.Columnas.POS_ID + "=?";

        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{codigo});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores = cursor.getString(cursor.getColumnIndexOrThrow(ContractPharmaValue.Columnas.NUMERO_CONTROLLER));
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables

        return operadores ;
    }

    public String getCanalPDV(String codigo) {
        String operadores = "";

        // Select All Query
        String selectQuery = "SELECT DISTINCT " + ContractPharmaValue.Columnas.CHANNEL + " FROM " + ContractPharmaValue.POS + " WHERE "+
                ContractPharmaValue.Columnas.POS_ID + "=?";

        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{codigo});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores = cursor.getString(cursor.getColumnIndexOrThrow(ContractPharmaValue.Columnas.CHANNEL));
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables

        return operadores ;
    }


    public ArrayList<Base_versiones> getNuevaVersion(String fecha) {
        ArrayList<Base_versiones> array = new ArrayList<>();

        String link_aplicativo = "";
        String fechayyyymmdd = "";

        String dia = fecha.split("/")[0];
        String mes = fecha.split("/")[1];
        String año = fecha.split("/")[2];
        fechayyyymmdd = año + "-" + mes + "-" + dia;

        Log.i("fechayyyymmdd",""+fechayyyymmdd);

//        String parts[] = fecha.split("/");
//        fechayyyymmdd = parts[2] + "-" + parts[1] + "-" + parts[0];


        // Select All Query
        String selectQuery = "SELECT * FROM " + ContractVersiones.VERSIONES
                + " WHERE "+ ContractVersiones.Columnas.FECHA_VERSION + "> ? " +
                " ORDER BY " + ContractVersiones.Columnas.FECHA_VERSION + " DESC LIMIT 1";


        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{fechayyyymmdd});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Base_versiones baseVersiones = new Base_versiones();
                baseVersiones.setFecha_version(cursor.getString(cursor.getColumnIndexOrThrow(ContractVersiones.Columnas.FECHA_VERSION)));
                baseVersiones.setLink_aplicativo(cursor.getString(cursor.getColumnIndexOrThrow(ContractVersiones.Columnas.LINK_APLICATIVO)));
                baseVersiones.setTamaño(cursor.getString(cursor.getColumnIndexOrThrow(ContractVersiones.Columnas.TAMAÑO)));
                array.add(baseVersiones);
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables


        return  array;
    }

    public String getTamañoVersion(String fecha) {

        String fechayyyymmdd = "";
        String tamaño = "";

        String dia = fecha.split("/")[0];
        String mes = fecha.split("/")[1];
        String año = fecha.split("/")[2];
        fechayyyymmdd = año + "-" + mes + "-" + dia;

        Log.i("fechayyyymmdd",""+fechayyyymmdd);

//        String parts[] = fecha.split("/");
//        fechayyyymmdd = parts[2] + "-" + parts[1] + "-" + parts[0];


        // Select All Query
        String selectQuery = "SELECT " + ContractVersiones.Columnas.TAMAÑO + " FROM " + ContractVersiones.VERSIONES
                + " WHERE "+ ContractVersiones.Columnas.FECHA_VERSION + "= ? " +
                " ORDER BY " + ContractVersiones.Columnas.FECHA_VERSION + " LIMIT 1";


        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{fechayyyymmdd});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
               tamaño = cursor.getString(cursor.getColumnIndexOrThrow(ContractVersiones.Columnas.TAMAÑO));
            } while (cursor.moveToNext());
        }

        if (tamaño == null || tamaño == ""){
            tamaño = "No Disponible";
        } else {
            tamaño = tamaño.toUpperCase();
        }


        // closing connection
        cursor.close();
        db.close();
        // returning lables


        return tamaño;
    }


    public List<String> getCategoriaPropensosYMalEstado(String manufacturer,String modulo){
        List<String> operadores = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.SECTOR + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS
                + " WHERE " + ContractPortafolioProductos.Columnas.FABRICANTE + " LIKE ? AND " +
                ContractPortafolioProductos.Columnas.LOCALES + " LIKE ? " ;
           //    ContractPortafolioProductos.Columnas.CADENAS + " LIKE ? "; // PARA FILTRAR POR TIPO DE CADENA QUE SEA DE KYWI

        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{"%" + manufacturer + "%","%" + modulo  + "%"});

        operadores.add("Seleccione");
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.SECTOR)));
            } while (cursor.moveToNext() && cursor!=null);
        }
        Log.i("OPERADORES",operadores.size()+"");
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }


    public List<String> getSubcategoriaFlooring(String categoria, String manufacturer){
        List<String> operadores = new ArrayList<String>();

        Cursor cursor;
        String selectQuery = "";

        /*if(categoria.equalsIgnoreCase("TODOS")){
            selectQuery = "SELECT DISTINCT " + ContractPortafolioProductos.Columnas.CATEGORY + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " +
                    ContractPortafolioProductos.Columnas.FABRICANTE + " LIKE ? ";
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{manufacturer});
        }else{*/
        selectQuery = "SELECT DISTINCT " + ContractPortafolioProductos.Columnas.SUBCATEGORIA + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " +
                ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                ContractPortafolioProductos.Columnas.FABRICANTE + " LIKE ? ";
        db = this.getReadableDatabase();
        cursor = db.rawQuery(selectQuery, new String[]{categoria, "%" + manufacturer + "%"});
        //}

        operadores.add("Seleccione");
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.SUBCATEGORIA)));
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }
    public ArrayList<Base_portafolio_productos> filtrarListProductosMalEstado(String categoria, String brand, String manufacturer, String modulo) {
        ArrayList<Base_portafolio_productos> operadores = new ArrayList<Base_portafolio_productos>();

        Cursor cursor = null;
        String selectQuery = "";


            // Select All Query
            selectQuery = "SELECT DISTINCT " + ContractPortafolioProductos.Columnas.SKU + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " +
                    ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                    ContractPortafolioProductos.Columnas.MARCA + "=? AND " +
                    ContractPortafolioProductos.Columnas.FABRICANTE + " LIKE ? AND " +
                    ContractPortafolioProductos.Columnas.LOCALES + " LIKE ?";
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{categoria, brand, "%" + manufacturer + "%", "%" + modulo + "%"});

            if (cursor.moveToFirst()) {
                do {
                    Base_portafolio_productos bpv = new Base_portafolio_productos();
                    bpv.setSku(cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.SKU)));
                    operadores.add(bpv);
                } while (cursor.moveToNext());
            }


//        operadores.add(new Base_portafolio_productos("HEADER"));
        // looping through all rows and adding to list

        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }


//    public ArrayList<Base_portafolio_productos> filtrarListProductosPropensos(String categoria, String brand, String manufacturer, String modulo, String cadena) {
//        ArrayList<Base_portafolio_productos> operadores = new ArrayList<Base_portafolio_productos>();
//
//        Cursor cursor = null;
//        String selectQuery = "";
//
//        if (cadena.contains("KYWI")) {
//            // Select All Query
//            selectQuery = "SELECT DISTINCT " + ContractPortafolioProductos.Columnas.VARIANTE1 + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " +
//                    ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
//                    ContractPortafolioProductos.Columnas.MARCA + "=? AND " +
//                    ContractPortafolioProductos.Columnas.FABRICANTE + " LIKE ? AND " +
//                    ContractPortafolioProductos.Columnas.LOCALES + " LIKE ?";
//            db = this.getReadableDatabase();
//            cursor = db.rawQuery(selectQuery, new String[]{categoria, brand, "%" + manufacturer + "%", "%" + modulo + "%"});
//
//            if (cursor.moveToFirst()) {
//                do {
//                    Base_portafolio_productos bpv = new Base_portafolio_productos();
//                    bpv.setSku(cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.VARIANTE1)));
//                    operadores.add(bpv);
//                } while (cursor.moveToNext());
//            }
//
//        } else {
//            // Select All Query
//            selectQuery = "SELECT DISTINCT " + ContractPortafolioProductos.Columnas.SKU + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " +
//                    ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
//                    ContractPortafolioProductos.Columnas.MARCA + "=? AND " +
//                    ContractPortafolioProductos.Columnas.FABRICANTE + " LIKE ? AND " +
//                    ContractPortafolioProductos.Columnas.LOCALES + " LIKE ?";
//            db = this.getReadableDatabase();
//            cursor = db.rawQuery(selectQuery, new String[]{categoria, brand, "%" + manufacturer + "%", "%" + modulo + "%"});
//
//            if (cursor.moveToFirst()) {
//                do {
//                    Base_portafolio_productos bpv = new Base_portafolio_productos();
//                    bpv.setSku(cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.SKU)));
//                    operadores.add(bpv);
//                } while (cursor.moveToNext());
//            }
//
//        }

    public ArrayList<Base_portafolio_productos> filtrarListProductosPropensos(String categoria, String brand, String manufacturer, String modulo, String cadena) {
        ArrayList<Base_portafolio_productos> operadores = new ArrayList<Base_portafolio_productos>();

        Cursor cursor = null;
        String selectQuery = "";


            // Select All Query
            selectQuery = "SELECT DISTINCT " + ContractPortafolioProductos.Columnas.VARIANTE1 +", "+ ContractPortafolioProductos.Columnas.SKU +
                    " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " +
                    ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                    ContractPortafolioProductos.Columnas.MARCA + "=? AND " +
                    ContractPortafolioProductos.Columnas.FABRICANTE + " LIKE ? AND " +
                    ContractPortafolioProductos.Columnas.LOCALES + " LIKE ?";
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{categoria, brand, "%" + manufacturer + "%", "%" + modulo + "%"});

            if (cursor.moveToFirst()) {
                do {
                    Base_portafolio_productos bpv = new Base_portafolio_productos();
                    bpv.setSku(cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.SKU)));
                    bpv.setSkuCliente(cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.VARIANTE1)));

                    operadores.add(bpv);
                } while (cursor.moveToNext());
            }


//        operadores.add(new Base_portafolio_productos("HEADER"));
        // looping through all rows and adding to list

        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }


    public String getEstadoPropensosYProductosMalEstado(String codigo, String user, String sku, String modulo) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-5"));
        Date currentLocalTime = cal.getTime();
        DateFormat date = new SimpleDateFormat("dd/MM/yyy");
        date.setTimeZone(TimeZone.getTimeZone("GMT-5"));
        String fecha_actual = date.format(currentLocalTime);
        String mes_actual = fecha_actual.substring(3);

        Log.i("FECHAS", "FECHA ACTUAL: " + fecha_actual + " - MES ACTUAL: " + mes_actual);

        String operadores = "PENDIENTE";
        String selectQuery = "";
        Cursor cursor = null;

        selectQuery = "SELECT * FROM " + ContractInsertPropensosYProdMalEst.INSERT_PROP_MAL_EST +
                " WHERE " + ContractInsertPropensosYProdMalEst.Columnas.CODIGO + " =? AND " +
                ContractInsertPropensosYProdMalEst.Columnas.USUARIO + "=? AND " +
                ContractInsertPropensosYProdMalEst.Columnas.MODULO + "=? AND " +
                ContractInsertPropensosYProdMalEst.Columnas.FECHA + "=? AND " +
                ContractInsertPropensosYProdMalEst.Columnas.SKU_CODE + "=?";
        db = this.getReadableDatabase();
        cursor = db.rawQuery(selectQuery, new String[]{codigo, user,modulo, fecha_actual, sku});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores = "REALIZADO";
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }



//    public List<String> getCausalesInventario(int unidades_defectuosas){
//        if (unidades_defectuosas > 0) {
//            unidades_defectuosas = 1;
//        }
//        String uni_defectuosas = String.valueOf(unidades_defectuosas);
//        List<String> operadores = new ArrayList<String>();
//
//        Cursor cursor;
//        String selectQuery = "";
//
//        selectQuery = "SELECT DISTINCT " + ContractCausalesInventario.Columnas.CAUSAL + " FROM " + ContractCausalesInventario.CAUSALES_INVENTARIO +
//                " WHERE " + ContractCausalesInventario.Columnas.CANTIDAD_DEFECTUOSA + "=?";
//        db = this.getReadableDatabase();
//        cursor = db.rawQuery(selectQuery, new String[]{uni_defectuosas});
//
//        operadores.add("Seleccione");
//        // looping through all rows and adding to list
//        if (cursor.moveToFirst()) {
//            do {
//                operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractCausalesInventario.Columnas.CAUSAL)));
//            } while (cursor.moveToNext());
//        }
//        // closing connection
//        cursor.close();
//        db.close();
//        // returning lables
//        return operadores;
//    }


    public List<String> getMarcaPropensosYMalEstado(String categoria, String manufacturer, String modulo){
        List<String> operadores = new ArrayList<String>();

        Cursor cursor;
        String selectQueryTodos = "", selectQuery = "";
        selectQuery = "SELECT DISTINCT " + ContractPortafolioProductos.Columnas.MARCA + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE "+
                ContractPortafolioProductos.Columnas.SECTOR +"=? AND " +
                ContractPortafolioProductos.Columnas.FABRICANTE +" LIKE ? AND " +
                ContractPortafolioProductos.Columnas.LOCALES +" LIKE ?";
        db = this.getReadableDatabase();
        cursor = db.rawQuery(selectQuery, new String[]{categoria,"%" + manufacturer + "%","%" + modulo + "%"});
        //}

        operadores.add("Seleccione");
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.MARCA)));
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }


    public String getNumeroController(String codigo) {
        String operadores = "";

        // Select All Query
        String selectQuery = "SELECT DISTINCT " + ContractPharmaValue.Columnas.NUMERO_CONTROLLER + " FROM " + ContractPharmaValue.POS + " WHERE "+
                ContractPharmaValue.Columnas.POS_ID + "=?";

        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{codigo});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores = cursor.getString(cursor.getColumnIndexOrThrow(ContractPharmaValue.Columnas.NUMERO_CONTROLLER));
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables

        return operadores ;
    }

    public List<String> getCategoria() {
        List<String> operadores = new ArrayList<String>();

        // Select All Query
       String selectQuery = "SELECT DISTINCT "+ ContractPrecios.Columnas.CATEGORIA + " FROM " + ContractPrecios.PRECIOS;
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractPrecios.Columnas.CATEGORIA)));
            } while (cursor.moveToNext() && cursor!=null);
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public List<String> getPresentacionFlooring(String categoria, String subcategoria, String manufacturer) {
        List<String> operadores = new ArrayList<String>();

        Cursor cursor;
        String selectQueryTodos = "", selectQuery = "";
        // Select All Query
        /*if (sector.equalsIgnoreCase("TODOS") && categoria.equalsIgnoreCase("TODOS")) {
            selectQueryTodos = "SELECT DISTINCT " + ContractPortafolioProductos.Columnas.CUMPLIMIENTO + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS_MAYO + " WHERE " +
                    ContractPortafolioProductos.Columnas.FABRICANTE +" LIKE ?";
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQueryTodos, new String[]{"%" + manufacturer + "%"});
        }else if (sector.equalsIgnoreCase("TODOS") && !categoria.equalsIgnoreCase("TODOS")) {
            selectQueryTodos = "SELECT DISTINCT " + ContractPortafolioProductos.Columnas.CUMPLIMIENTO + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS_MAYO + " WHERE " +
                    ContractPortafolioProductos.Columnas.CATEGORY +"=? AND " +
                    ContractPortafolioProductos.Columnas.FABRICANTE +" LIKE ?";
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQueryTodos, new String[]{categoria,"%" + manufacturer + "%"});
        }else if (!sector.equalsIgnoreCase("TODOS") && categoria.equalsIgnoreCase("TODOS")) {
            selectQueryTodos = "SELECT DISTINCT " + ContractPortafolioProductos.Columnas.CUMPLIMIENTO + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS_MAYO + " WHERE " +
                    ContractPortafolioProductos.Columnas.CATEGORIA +"=? AND " +
                    ContractPortafolioProductos.Columnas.FABRICANTE +" LIKE ?";
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQueryTodos, new String[]{sector,"%" + manufacturer + "%"});
        }else{*/
            selectQuery = "SELECT DISTINCT " + ContractPortafolioProductos.Columnas.PRESENTACION + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE "+
                    ContractPortafolioProductos.Columnas.CATEGORY +"=? AND " +
                    ContractPortafolioProductos.Columnas.SUBCATEGORIA +"=? AND " +
                    ContractPortafolioProductos.Columnas.FABRICANTE +" LIKE ?";
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{categoria,subcategoria,"%" + manufacturer + "%"});
        //}

        //operadores.add("Todos");
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.PRESENTACION)));
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }


    public String getPresentacionPropensos(String sku){
        String id = "";
        String query = "SELECT * FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " +
                ContractPortafolioProductos.Columnas.SKU +" =? ";
        db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery(query, new String[]{sku});

        if(cursor != null && cursor.moveToFirst()){
            id = cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.PRESENTACION));
        }

        cursor.close();
        db.close();
        return id;
    }


    public List<String> getBrandFlooring(String categoria, String subcategoria, String codigo, String canal, String subcanal, String manufacturer) {
        List<String> operadores = new ArrayList<String>();

        Cursor cursor;
        String selectQuery = "";

        if (canal.equalsIgnoreCase("MAYORISTA")) {
            selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.MARCA + " FROM " +
                    ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " +
                    ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                    ContractPortafolioProductos.Columnas.CATEGORY + "=? AND " +
                    ContractPortafolioProductos.Columnas.FABRICANTE + " LIKE ?";
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{categoria, subcategoria, "%" + manufacturer + "%"});
        }else{
            selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.MARCA + " FROM " +
                    ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " +
                    ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                    ContractPortafolioProductos.Columnas.CATEGORY + "=? AND " +
                    ContractPortafolioProductos.Columnas.CADENAS + " LIKE ? AND " +
                    ContractPortafolioProductos.Columnas.FABRICANTE + " LIKE ?";
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{categoria, subcategoria, "%" + subcanal.trim() + "%", "%" + manufacturer + "%"});
        }
        //}

        //operadores.add("Todos");
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.MARCA)));
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public List<String> getBrandFlooring2(String categoria, String subcategoria, String codigo, String canal, String subcanal, String manufacturer, String modulo) {
        List<String> operadores = new ArrayList<String>();

        Cursor cursor;
        String selectQuery = "";

        if (canal.equalsIgnoreCase("MAYORISTA")) {

//            if (categoria.equalsIgnoreCase("INSECTICIDAS")) {
//                selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.MARCA + " FROM " +
//                        ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " +
//                        ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
//                        ContractPortafolioProductos.Columnas.CATEGORY + "=? AND (" +
//                        ContractPortafolioProductos.Columnas.FABRICANTE + " LIKE ? OR " +
//                        ContractPortafolioProductos.Columnas.FABRICANTE + "='Sc Jhonson')" +
//                        " ORDER BY " + ContractPortafolioProductos.Columnas.MARCA;
//                db = this.getReadableDatabase();
//                cursor = db.rawQuery(selectQuery, new String[]{categoria, subcategoria, "%" + manufacturer + "%"});
//            } else {
                selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.MARCA + " FROM " +
                        ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " +
                        ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                    //    ContractPortafolioProductos.Columnas.CATEGORY + "=? AND " +
                        ContractPortafolioProductos.Columnas.FABRICANTE + " LIKE ? AND " +
                        ContractPortafolioProductos.Columnas.LOCALES + " LIKE ? " +
                        " ORDER BY " + ContractPortafolioProductos.Columnas.MARCA;
                db = this.getReadableDatabase();
                cursor = db.rawQuery(selectQuery, new String[]{categoria /*, subcategoria */, "%" + manufacturer + "%","%"+ modulo +"%"});
 //           }
        }else{


                selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.MARCA + " FROM " +
                        ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " +
                        ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                //        ContractPortafolioProductos.Columnas.CATEGORY + "=? AND " +
                        ContractPortafolioProductos.Columnas.CADENAS + " LIKE ? AND " +
                        ContractPortafolioProductos.Columnas.FABRICANTE + " LIKE ? AND " +
                        ContractPortafolioProductos.Columnas.LOCALES + " LIKE ? " +
                        " ORDER BY " + ContractPortafolioProductos.Columnas.MARCA;
                db = this.getReadableDatabase();
                cursor = db.rawQuery(selectQuery, new String[]{categoria , /*subcategoria,*/ "%" + subcanal.trim() + "%","%" + manufacturer + "%","%"+ modulo +"%"});

        }
        //}

        operadores.add("Seleccione");
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.MARCA)));
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }



    public List<String> getGramajeFlooring2(String categoria, String subcategoria, String marca, String codigo, String canal, String subcanal, String manufacturer, String modulo) {
        List<String> operadores = new ArrayList<String>();

        Cursor cursor;
        String selectQuery = "";

        if (canal.equalsIgnoreCase("MAYORISTA")) {
                selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.CONTENIDO + " FROM " +
                        ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " +
                        ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                //        ContractPortafolioProductos.Columnas.CATEGORY + "=? AND " +
                        ContractPortafolioProductos.Columnas.MARCA + "=? AND " +
                        ContractPortafolioProductos.Columnas.FABRICANTE + " LIKE ? AND " +
                        ContractPortafolioProductos.Columnas.LOCALES + " LIKE ?" +
                        " ORDER BY " + ContractPortafolioProductos.Columnas.CONTENIDO;
                db = this.getReadableDatabase();
                cursor = db.rawQuery(selectQuery, new String[]{categoria,/* subcategoria, */ marca, "%" + manufacturer + "%","%" + modulo + "%"});
        }else{
            selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.CONTENIDO + " FROM " +
                    ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " +
                    ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
            //        ContractPortafolioProductos.Columnas.CATEGORY + "=? AND " +
                    ContractPortafolioProductos.Columnas.MARCA + "=? AND " +
                    ContractPortafolioProductos.Columnas.CADENAS + " LIKE ? AND " +
                    ContractPortafolioProductos.Columnas.LOCALES + " LIKE ?" +
                    " ORDER BY " + ContractPortafolioProductos.Columnas.CONTENIDO;
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{categoria,/* subcategoria,*/ marca, "%" + subcanal.trim() + "%","%" + modulo + "%"});
        }
        //}

        operadores.add("Seleccione");
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.CONTENIDO)));
            } while (cursor.moveToNext());
        }

        /*if(categoria.equalsIgnoreCase("DETERGENTES") && subcategoria.equalsIgnoreCase("POLVO")){
            operadores.add("Surf");
        }*/

        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }




    public List<String> getBrandImpulso(String categoria, String subcategoria, String manufacturer) {
        List<String> operadores = new ArrayList<String>();

        Cursor cursor;
        String selectQueryTodos = "", selectQuery = "";
        // Select All Query
        selectQuery = "SELECT DISTINCT " + ContractPortafolioProductos.Columnas.MARCA + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE "+
                ContractPortafolioProductos.Columnas.SECTOR +"=? AND " +
                ContractPortafolioProductos.Columnas.CATEGORY +"=? AND " +
                ContractPortafolioProductos.Columnas.FABRICANTE +" LIKE ?" +
                " ORDER BY " + ContractPortafolioProductos.Columnas.MARCA;
        db = this.getReadableDatabase();
        cursor = db.rawQuery(selectQuery, new String[]{categoria,subcategoria,"%" + manufacturer + "%"});
        //}

        operadores.add("Seleccione");
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.MARCA)));
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public List<String> getSKUImpulso(String categoria, String subcategoria, String marca, String manufacturer) {
        List<String> operadores = new ArrayList<String>();

        Cursor cursor;
        String selectQueryTodos = "", selectQuery = "";
        // Select All Query
        selectQuery = "SELECT DISTINCT " + ContractPortafolioProductos.Columnas.SKU + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE "+
                ContractPortafolioProductos.Columnas.SECTOR +"=? AND " +
                ContractPortafolioProductos.Columnas.CATEGORY +"=? AND " +
                ContractPortafolioProductos.Columnas.MARCA +"=? AND " +
                ContractPortafolioProductos.Columnas.FABRICANTE +" LIKE ?" +
                " ORDER BY " + ContractPortafolioProductos.Columnas.SKU;
        db = this.getReadableDatabase();
        cursor = db.rawQuery(selectQuery, new String[]{categoria,subcategoria,marca,"%" + manufacturer + "%"});
        //}

        operadores.add("Seleccione");
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.SKU)));
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public List<String> getMarcaPrecios(String categoria, String subcategoria, String tipo, String fabricante, String codigo, String canal, String subcanal/*, List<String> marcas_blancas*/, String modulo) {
        List<String> operadores = new ArrayList<String>();

        Cursor cursor = null;
        String selectQuery = "";
        // Select All Query

        operadores.add("Seleccione");

        String byFabricante = ContractPortafolioProductos.Columnas.FABRICANTE + " NOT LIKE ? ";

        if (tipo.equalsIgnoreCase("MARCA_PROPIA")) {
            byFabricante = ContractPortafolioProductos.Columnas.FABRICANTE + " LIKE ? ";
        }

        if (canal.equalsIgnoreCase("MAYORISTA")) {
            selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.MARCA + " FROM " +
                    ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " + byFabricante + " AND " +
                    ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
            //        ContractPortafolioProductos.Columnas.CATEGORY + "=? AND " +
                    ContractPortafolioProductos.Columnas.FABRICANTE + " NOT LIKE '% Rosado%' AND " +
                    ContractPortafolioProductos.Columnas.FABRICANTE + " NOT LIKE '% Coral%' AND " +
                    ContractPortafolioProductos.Columnas.FABRICANTE + " NOT LIKE '% Favorita%' AND " +
                    ContractPortafolioProductos.Columnas.FABRICANTE + " NOT LIKE '% Maria%' AND " +
                    ContractPortafolioProductos.Columnas.FABRICANTE + " NOT LIKE '% Tia%' AND " +
                    ContractPortafolioProductos.Columnas.LOCALES + " LIKE ? " +
                    " ORDER BY " + ContractPortafolioProductos.Columnas.MARCA;
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{"%" + fabricante + "%", categoria, /* subcategoria, */ "%" + modulo + "%"});
        }else if (canal.equalsIgnoreCase("AUTOSERVICIO")) {
            selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.MARCA + " FROM " +
                    ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " + byFabricante + " AND " +
                    ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
            //        ContractPortafolioProductos.Columnas.CATEGORY + "=? AND " +
                    ContractPortafolioProductos.Columnas.CADENAS + " LIKE ? AND " +
                    ContractPortafolioProductos.Columnas.LOCALES + " LIKE ? " +
                    " ORDER BY " + ContractPortafolioProductos.Columnas.MARCA;
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{"%" + fabricante + "%", categoria, /* subcategoria, */ "%" + subcanal.trim() + "%", "%" + modulo + "%"});
        }else{
            selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.MARCA + " FROM " +
                    ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " + byFabricante + " AND " +
                    ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
            //        ContractPortafolioProductos.Columnas.CATEGORY + "=? AND " +
                    ContractPortafolioProductos.Columnas.FABRICANTE + " NOT LIKE '% Rosado%' AND " +
                    ContractPortafolioProductos.Columnas.FABRICANTE + " NOT LIKE '% Coral%' AND " +
                    ContractPortafolioProductos.Columnas.FABRICANTE + " NOT LIKE '% Favorita%' AND " +
                    ContractPortafolioProductos.Columnas.FABRICANTE + " NOT LIKE '% Maria%' AND " +
                    ContractPortafolioProductos.Columnas.FABRICANTE + " NOT LIKE '% Tia%' AND " +
                    ContractPortafolioProductos.Columnas.LOCALES + " LIKE ? " +
                    " ORDER BY " + ContractPortafolioProductos.Columnas.MARCA;
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{"%" + fabricante + "%", categoria, /* subcategoria, */ "%" + modulo + "%"});
        }

        // Select All Query
        //operadores.add("Todos");
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.MARCA)));
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();

        //quitando las marcas que no aplican
        /*
        for (int j=0;j<marcas_blancas.size();j++){
            if(operadores.contains(marcas_blancas.get(j))){
                operadores.remove(marcas_blancas.get(j));
            }
        }*/

        // returning lables
        return operadores;
    }


    public List<String> getMarcaLogistico(String categoria, String subcategoria, String tipo, String fabricante, String codigo, String canal, String subcanal/*, List<String> marcas_blancas*/, String modulo) {
            List<String> operadores = new ArrayList<String>();

            Cursor cursor = null;
            String selectQuery = "";
            // Select All Query

            operadores.add("Seleccione");

            String byFabricante = ContractPortafolioProductos.Columnas.FABRICANTE + " NOT LIKE ? ";

            if (tipo.equalsIgnoreCase("RELEVO")) {
                byFabricante = ContractPortafolioProductos.Columnas.FABRICANTE + " LIKE ? ";
            }

            if (canal.equalsIgnoreCase("MAYORISTA")) {
                selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.MARCA + " FROM " +
                        ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " + byFabricante + " AND " +
                        ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                //        ContractPortafolioProductos.Columnas.CATEGORY + "=? AND " +
                        ContractPortafolioProductos.Columnas.FABRICANTE + " NOT LIKE '% Rosado%' AND " +
                        ContractPortafolioProductos.Columnas.FABRICANTE + " NOT LIKE '% Coral%' AND " +
                        ContractPortafolioProductos.Columnas.FABRICANTE + " NOT LIKE '% Favorita%' AND " +
                        ContractPortafolioProductos.Columnas.FABRICANTE + " NOT LIKE '% Maria%' AND " +
                        ContractPortafolioProductos.Columnas.FABRICANTE + " NOT LIKE '% Tia%' AND " +
                        ContractPortafolioProductos.Columnas.LOCALES + " LIKE ? " +
                        " ORDER BY " + ContractPortafolioProductos.Columnas.MARCA;
                db = this.getReadableDatabase();
                cursor = db.rawQuery(selectQuery, new String[]{"%" + fabricante + "%", categoria, /* subcategoria, */ "%" + modulo + "%"});
            }else if (canal.equalsIgnoreCase("AUTOSERVICIO")) {
                selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.MARCA + " FROM " +
                        ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " + byFabricante + " AND " +
                        ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                //        ContractPortafolioProductos.Columnas.CATEGORY + "=? AND " +
                        ContractPortafolioProductos.Columnas.CADENAS + " LIKE ? AND " +
                        ContractPortafolioProductos.Columnas.LOCALES + " LIKE ? " +
                        " ORDER BY " + ContractPortafolioProductos.Columnas.MARCA;
                db = this.getReadableDatabase();
                cursor = db.rawQuery(selectQuery, new String[]{"%" + fabricante + "%", categoria, /* subcategoria, */ "%" + subcanal.trim() + "%", "%" + modulo + "%"});
            }else{
                selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.MARCA + " FROM " +
                        ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " + byFabricante + " AND " +
                        ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                //        ContractPortafolioProductos.Columnas.CATEGORY + "=? AND " +
                        ContractPortafolioProductos.Columnas.FABRICANTE + " NOT LIKE '% Rosado%' AND " +
                        ContractPortafolioProductos.Columnas.FABRICANTE + " NOT LIKE '% Coral%' AND " +
                        ContractPortafolioProductos.Columnas.FABRICANTE + " NOT LIKE '% Favorita%' AND " +
                        ContractPortafolioProductos.Columnas.FABRICANTE + " NOT LIKE '% Maria%' AND " +
                        ContractPortafolioProductos.Columnas.FABRICANTE + " NOT LIKE '% Tia%' AND " +
                        ContractPortafolioProductos.Columnas.LOCALES + " LIKE ? " +
                        " ORDER BY " + ContractPortafolioProductos.Columnas.MARCA;
                db = this.getReadableDatabase();
                cursor = db.rawQuery(selectQuery, new String[]{"%" + fabricante + "%", categoria, /* subcategoria, */ "%" + modulo + "%"});
            }

            // Select All Query
            //operadores.add("Todos");
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.MARCA)));
                } while (cursor.moveToNext());
            }
            // closing connection
            cursor.close();
            db.close();

            //quitando las marcas que no aplican
            /*
            for (int j=0;j<marcas_blancas.size();j++){
                if(operadores.contains(marcas_blancas.get(j))){
                    operadores.remove(marcas_blancas.get(j));
                }
            }*/

            // returning lables
            return operadores;
        }



    public List<String> getMarcaVentas(String canal, String subcanal, String categoria , String tipo, String fabricante, String modulo){
        List<String> operadores = new ArrayList<String>();

        Cursor cursor = null;
        String selectQuery = "";
        // Select All Query

        Log.i("Canal Ventas:",""+canal);
        Log.i("Canal Subcanal:",""+subcanal);
        Log.i("Canal Tipo:",""+tipo);
        Log.i("Canal Fabricante:",""+fabricante);




        operadores.add("Seleccione");

        String byFabricante = ContractPortafolioProductos.Columnas.FABRICANTE + " NOT LIKE ? ";

        if (tipo.equalsIgnoreCase("MARCA_PROPIA")) {
            byFabricante = ContractPortafolioProductos.Columnas.FABRICANTE + " LIKE ? ";
        }

        if(canal.equalsIgnoreCase("MAYORISTA")){
            selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.MARCA + " FROM " +
                    ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " + byFabricante + " AND " +
                    ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                    ContractPortafolioProductos.Columnas.LOCALES + " LIKE ? " +
                    " ORDER BY " + ContractPortafolioProductos.Columnas.MARCA;
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{"%" + fabricante + "%",categoria,"%" + modulo + "%"});
        }else if(canal.equalsIgnoreCase("AUTOSERVICIO")){
            selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.MARCA + " FROM " +
                    ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " + byFabricante + " AND " +
                    ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                    ContractPortafolioProductos.Columnas.CADENAS + " LIKE ? AND " +
                    ContractPortafolioProductos.Columnas.LOCALES + " LIKE ? " +
                    " ORDER BY " + ContractPortafolioProductos.Columnas.MARCA;
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{"%" + fabricante + "%", categoria,"%" + subcanal.trim() + "%","%" + modulo + "%"});
        }else{
            selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.MARCA + " FROM " +
                    ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " + byFabricante + " AND " +
                    ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                    ContractPortafolioProductos.Columnas.LOCALES + " LIKE ? " +
                    " ORDER BY " + ContractPortafolioProductos.Columnas.MARCA;
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{"%" + fabricante + "%", categoria,"%" + modulo + "%"});
        }

        // Select All Query
        //operadores.add("Todos");
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.MARCA)));
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        Log.i("operadores", operadores+"");
        // returning lables
        return operadores;
    }






    public ArrayList<Ventas> filtrarListProductosVentas(String fabricante, String categoria, String tipo, String marca, String modulo){
        ArrayList<Ventas> operadores = new ArrayList<>();

        Cursor cursor = null;
        String selectQuery = null;

        selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.SKU + ", " + ContractPortafolioProductos.Columnas.PVP + ", " + ContractPortafolioProductos.Columnas.CONTENIDO + " FROM " +
                ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " +
                ContractPortafolioProductos.Columnas.FABRICANTE + " LIKE ? AND " +
                ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                ContractPortafolioProductos.Columnas.MARCA + "=? AND " +
                ContractPortafolioProductos.Columnas.LOCALES + " LIKE ? " +
                " ORDER BY " + ContractPortafolioProductos.Columnas.SKU + " ASC ";
        db = this.getReadableDatabase();
        cursor = db.rawQuery(selectQuery, new String[]{"%" + fabricante + "%" ,categoria,marca,"%" + modulo + "%"});


        if (cursor.moveToFirst()) {
            do {
                Ventas  v = new Ventas();
                v.setSku(cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.SKU)));
                v.setContenido(cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.CONTENIDO)));
                //  p.setPvp(cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.PVP)));
                operadores.add(v);
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }




    public ArrayList<Ventas> getListGuardadoPrecios3(String codigo){
        ArrayList<Ventas> operadores = new ArrayList<Ventas>();

        String selectQuery = "SELECT " + ContractInsertVentas.Columnas.SKU_CODE +
//                ContractInsertImpulsoVentas.Columnas.PREGULAR + ", " +
//                ContractInsertImpulsoVentas.Columnas.PPROMOCION +
                " FROM " + ContractInsertVentas.INSERT_VENTAS +
                " WHERE " + ContractInsertVentas.Columnas.CODIGO + " =?;";
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{codigo});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Ventas v = new Ventas();
                v.setSku(cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertVentas.Columnas.SKU_CODE)));
//                p.setPvp(cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertPreciosSesion.Columnas.PREGULAR)));
//                p.setPvc(cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertPreciosSesion.Columnas.PPROMOCION)));
                operadores.add(v);
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }




    public List<String> getMarcaProdCad(String fabricante, String categoria, String subcategoria) {
        List<String> operadores = new ArrayList<String>();

        Cursor cursor;
        String selectQueryTodos = "", selectQuery = "";

        selectQuery = "SELECT DISTINCT " + ContractPortafolioProductos.Columnas.MARCA + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE "+
                ContractPortafolioProductos.Columnas.CATEGORY +"=? AND " +
                ContractPortafolioProductos.Columnas.SUBCATEGORIA +"=? AND " +
                ContractPortafolioProductos.Columnas.FABRICANTE +" LIKE ?";
        db = this.getReadableDatabase();
        cursor = db.rawQuery(selectQuery, new String[]{categoria,subcategoria,"%" + fabricante + "%"});

        // Select All Query
        //operadores.add("Todos");
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.MARCA)));
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public List<String> getMarcaPacks(String fabricante, String categoria, String subcategoria) {
        List<String> operadores = new ArrayList<String>();

        Cursor cursor;
        String selectQueryTodos = "", selectQuery = "";

        selectQuery = "SELECT DISTINCT " + ContractPortafolioProductos.Columnas.MARCA + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE "+
                ContractPortafolioProductos.Columnas.CATEGORY +"=? AND " +
                ContractPortafolioProductos.Columnas.SUBCATEGORIA +"=? AND " +
                ContractPortafolioProductos.Columnas.FABRICANTE +" LIKE ?";
        db = this.getReadableDatabase();
        cursor = db.rawQuery(selectQuery, new String[]{categoria,subcategoria,"%" + fabricante + "%"});

        // Select All Query
        //operadores.add("Todos");
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.MARCA)));
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }


    public String getPresentacionVentas(String sku){
        String id = "";
        String query = "SELECT * FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " +
                ContractPortafolioProductos.Columnas.SKU +" =? ";
        db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery(query, new String[]{sku});

        if(cursor != null && cursor.moveToFirst()){
            id = cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.PRESENTACION));
        }

        cursor.close();
        db.close();
        return id;
    }

    public ArrayList<BaseRangosPrecios> getAllRangosPrecios() {
        ArrayList<BaseRangosPrecios> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + ContractRangosPrecios.RANGOS_PRECIOS +
                " ORDER BY LENGTH(" + ContractRangosPrecios.Columnas.PRESENTACION + ") DESC";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                BaseRangosPrecios rango = new BaseRangosPrecios();
                rango.setPresentacion(cursor.getString(cursor.getColumnIndexOrThrow(ContractRangosPrecios.Columnas.PRESENTACION)));
                rango.setMinimo(cursor.getDouble(cursor.getColumnIndexOrThrow(ContractRangosPrecios.Columnas.MINIMO)));
                rango.setMaximo(cursor.getDouble(cursor.getColumnIndexOrThrow(ContractRangosPrecios.Columnas.MAXIMO)));
                list.add(rango);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return list;
    }

    public List<String> getCausalesAsistencia() {
        List<String> operadores = new ArrayList<String>();

        String selectQuery =
                "SELECT * FROM " + ContractCausalesAsistencia.CAUSALES_ASISTENCIA +
                        " GROUP BY " + ContractCausalesAsistencia.Columnas.DESCRIPCION +
                        " ORDER BY " + ContractCausalesAsistencia.Columnas.DESCRIPCION;

        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        operadores.add("Seleccione");
        if (cursor.moveToFirst()) {
            do {
                operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractCausalesAsistencia.Columnas.DESCRIPCION)));
            } while (cursor.moveToNext() && cursor != null);
        }
        cursor.close();
        db.close();
        return operadores;
    }
    public List<String> getCausalesAsistenciaAtraso() {
        List<String> operadores = new ArrayList<String>();

        String selectQuery =
                "SELECT * FROM " + ContractCausalesAsistenciaAtraso.CAUSALES_ASISTENCIA_ATRASO +
                        " GROUP BY " + ContractCausalesAsistenciaAtraso.Columnas.DESCRIPCION +
                        " ORDER BY " + ContractCausalesAsistenciaAtraso.Columnas.DESCRIPCION;

        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        operadores.add("Seleccione");
        if (cursor.moveToFirst()) {
            do {
                operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractCausalesAsistenciaAtraso.Columnas.DESCRIPCION)));
            } while (cursor.moveToNext() && cursor != null);
        }
        cursor.close();
        db.close();
        return operadores;
    }

    public BaseRangosPrecios getRangoPrecioByPresentacion(String presentacion) {
        BaseRangosPrecios rango = null;

        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + ContractRangosPrecios.RANGOS_PRECIOS + " WHERE " +
                ContractRangosPrecios.Columnas.PRESENTACION + " =? ";

        Cursor cursor = db.rawQuery(query, new String[]{presentacion});

        if (cursor != null && cursor.moveToFirst()) {
            rango = new BaseRangosPrecios();

            rango.setPresentacion(cursor.getString(cursor.getColumnIndexOrThrow(ContractRangosPrecios.Columnas.PRESENTACION)));
            rango.setMinimo(cursor.getDouble(cursor.getColumnIndexOrThrow(ContractRangosPrecios.Columnas.MINIMO)));
            rango.setMaximo(cursor.getDouble(cursor.getColumnIndexOrThrow(ContractRangosPrecios.Columnas.MAXIMO)));

            cursor.close();
        }

        db.close();

        return rango;
    }

    public BaseRangosPreciosSku getRangoPreciosBySku(String sku) {
        BaseRangosPreciosSku rango = null;

        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + ContractRangosPreciosSku.RANGOS_PRECIOS_SKU + " WHERE " +
                "TRIM(UPPER(" + ContractRangosPreciosSku.Columnas.SKU + ")) = TRIM(UPPER(?)) ";

        Cursor cursor = db.rawQuery(query, new String[]{sku});

        if (cursor != null && cursor.moveToFirst()) {
            rango = new BaseRangosPreciosSku();

            rango.setSku(cursor.getString(cursor.getColumnIndexOrThrow(ContractRangosPreciosSku.Columnas.SKU)));
            rango.setMinimo(cursor.getDouble(cursor.getColumnIndexOrThrow(ContractRangosPreciosSku.Columnas.MINIMO)));
            rango.setMaximo(cursor.getDouble(cursor.getColumnIndexOrThrow(ContractRangosPreciosSku.Columnas.MAXIMO)));

            cursor.close();
        }

        db.close();

        return rango;
    }


    public List<String> getFabricantePrecios(String categoria, String subcategoria, String marca, String tipo, String fabricante, String codigo, String canal, String subcanal) {
        List<String> operadores = new ArrayList<String>();

        Cursor cursor;
        String selectQuery = null;

        operadores.add("Seleccione");

        String byFabricante = ContractPortafolioProductos.Columnas.FABRICANTE + " NOT LIKE ? ";

        if (tipo.equalsIgnoreCase("MARCA_PROPIA")) {
            byFabricante = ContractPortafolioProductos.Columnas.FABRICANTE + " LIKE ? ";
        }

        if (canal.equalsIgnoreCase("MAYORISTA")) {
            selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.FABRICANTE + " FROM " +
                    ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " + byFabricante + " AND " +
                    ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                    ContractPortafolioProductos.Columnas.CATEGORY + "=? AND " +
                    ContractPortafolioProductos.Columnas.MARCA + "=?";
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{"%" + fabricante + "%", categoria, subcategoria, marca});
        }else if (canal.equalsIgnoreCase("AUTOSERVICIO")) {
            selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.FABRICANTE + " FROM " +
                    ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " + byFabricante + " AND " +
                    ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                    ContractPortafolioProductos.Columnas.CATEGORY + "=? AND " +
                    ContractPortafolioProductos.Columnas.MARCA + "=? AND " +
                    ContractPortafolioProductos.Columnas.CADENAS + " LIKE ?";
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{"%" + fabricante + "%", categoria, subcategoria, marca, "%" + subcanal.trim() + "%"});
        }else{
            selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.FABRICANTE + " FROM " +
                    ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " + byFabricante + " AND " +
                    ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                    ContractPortafolioProductos.Columnas.CATEGORY + "=? AND " +
                    ContractPortafolioProductos.Columnas.MARCA + "=?";
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{"%" + fabricante + "%", categoria, subcategoria, marca});
        }

        // Select All Query
        //operadores.add("Todos");
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.FABRICANTE)));
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public List<String> getPresentacionPacks(String fabricante, String categoria, String subcategoria, String marca) {
        List<String> operadores = new ArrayList<String>();

        Cursor cursor;
        String selectQuery = "SELECT DISTINCT " + ContractPortafolioProductos.Columnas.PRESENTACION + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE "+
                ContractPortafolioProductos.Columnas.FABRICANTE +"=? AND " +
                ContractPortafolioProductos.Columnas.CATEGORY +"=? AND " +
                ContractPortafolioProductos.Columnas.SUBCATEGORIA +"=? AND " +
                ContractPortafolioProductos.Columnas.MARCA +"=?";
        db = this.getReadableDatabase();
        cursor = db.rawQuery(selectQuery, new String[]{fabricante,categoria,subcategoria,marca});
        //}

        // Select All Query
        //operadores.add("Todos");
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.PRESENTACION)));
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public List<String> getProductoPacks(String fabricante, String categoria, String subcategoria, String marca, String presentacion) {
        List<String> operadores = new ArrayList<String>();

        Cursor cursor;
        String selectQuery = "SELECT DISTINCT " + ContractPortafolioProductos.Columnas.SKU + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE "+
                ContractPortafolioProductos.Columnas.FABRICANTE +"=? AND " +
                ContractPortafolioProductos.Columnas.CATEGORY +"=? AND " +
                ContractPortafolioProductos.Columnas.SUBCATEGORIA +"=? AND " +
                ContractPortafolioProductos.Columnas.MARCA +"=? AND " +
                ContractPortafolioProductos.Columnas.PRESENTACION +"=?";
        db = this.getReadableDatabase();
        cursor = db.rawQuery(selectQuery, new String[]{fabricante,categoria,subcategoria,marca,presentacion});
        //}

        // Select All Query
        //operadores.add("Todos");
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.SKU)));
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public List<String> getSubcategoriaPromocion(String categoria, String tipo, String fabricante, String canal, String subcanal, String modulo) {
        List<String> operadores = new ArrayList<String>();

        String selectQuery = "";
        Cursor cursor = null;
        // Select All Query
//        String selectQuery = "SELECT DISTINCT " + ContractPortafolioProductos.Columnas.CATEGORY + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " +
//                ContractPortafolioProductos.Columnas.SECTOR + "=?";
//        db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery(selectQuery, new String[]{categoria});

        String byFabricante = ContractPortafolioProductos.Columnas.FABRICANTE + " NOT LIKE ? ";

        if (tipo.equalsIgnoreCase("MARCA_PROPIA")) {
            byFabricante = ContractPortafolioProductos.Columnas.FABRICANTE + " LIKE ? ";
        }

        if (canal.equalsIgnoreCase("MAYORISTA")) {
            selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.CATEGORY + " FROM " +
                    ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " +
                    ContractPortafolioProductos.Columnas.SECTOR + "=?" + " AND " +
                    ContractPortafolioProductos.Columnas.LOCALES + " LIKE ? AND " +
                    byFabricante;
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{categoria,"%" + modulo + "%","%" + fabricante + "%"});
        }else{
            selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.CATEGORY + " FROM " +
                    ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " +
                    ContractPortafolioProductos.Columnas.SECTOR + "=?" + " AND " +
                    byFabricante + " AND " +
                    ContractPortafolioProductos.Columnas.CADENAS + " LIKE ? AND " +
                    ContractPortafolioProductos.Columnas.LOCALES + " LIKE ? ";
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{categoria,"%" + fabricante + "%", "%" + subcanal.trim() + "%","%" + modulo + "%"});
        }

        operadores.add("Seleccione");
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.CATEGORY)));
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public List<String> getPresentacionPromocion(String categoria, String marca, String segmento1) {
        List<String> operadores = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT DISTINCT " + ContractPortafolioProductos.Columnas.PRESENTACION + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " +
                ContractPortafolioProductos.Columnas.CATEGORY + "=? AND " +
                ContractPortafolioProductos.Columnas.MARCA + "=? AND " +
                ContractPortafolioProductos.Columnas.SUBCATEGORIA + "=?";
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{categoria,marca,segmento1});

        operadores.add("Seleccione");
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.PRESENTACION)));
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }


    public List<String> getCanal(){

        List<String> operadores = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT DISTINCT " + ContractPharmaValue.Columnas.CHANNEL + " FROM " +ContractPharmaValue.POS;
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{});

        if (cursor.moveToFirst()) {
            do {
                operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractPharmaValue.Columnas.CHANNEL)));
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;

    }


    public ArrayList<Base_tests> getAllQuizByCanal(ArrayList<String> canales, String usuario) {
        ArrayList<Base_tests> quizList = new ArrayList<Base_tests>();

        Cursor c = null;

        // En caso de ver el gestor tener asignado pvs con diferentes canales
        if(canales.size() > 1){

        //    String canal1 = canales.get(0).toString();
        //    String canal2 = canales.get(1).toString();
          //  String canal3 = canales.get(2).toString();


            // Select All Query
            String selectQuery = "" +
                    "SELECT rt.idRemota, rt.test , rt.descripcion , rt.f_inicio , rt.h_inicio , rt.f_limite , rt.h_limite , rt.active " +
                    "FROM " + ContractTests.TABLE_TEST  + " rt " +
                    " INNER JOIN " + ContractPreguntas.TABLE_QUEST + " rp " +
                    " ON rp.test_id=rt.idRemota " +
                    " WHERE rp.canal IN ('MAYORISTA','AUTOSERVICIO','IMPULSO') " +  "AND rt.test NOT IN (select test FROM insert_preguntas WHERE user =? AND estado_test ='1' " + " ) " +
                    "GROUP BY rt.idRemota,rt.test";
            db=this.getReadableDatabase();


             c = db.rawQuery(selectQuery, new String[]{usuario});

        }

        if(canales.size() == 1){

            String canal = canales.get(0).toString();


            // Select All Query
            String selectQuery = "" +
                    "SELECT rt.idRemota, rt.test , rt.descripcion , rt.f_inicio , rt.h_inicio , rt.f_limite , rt.h_limite , rt.active " +
                    "FROM " + ContractTests.TABLE_TEST  + " rt " +
                    " INNER JOIN " + ContractPreguntas.TABLE_QUEST + " rp " +
                    " ON rp.test_id=rt.idRemota " +
                    " WHERE rp.canal =? " +  "AND rt.test NOT IN (select test FROM insert_preguntas WHERE user =? AND estado_test ='1' " + " ) " +
                    "GROUP BY rt.idRemota,rt.test";
            db=this.getReadableDatabase();

             c = db.rawQuery(selectQuery, new String[]{canal,usuario});

        }


        if(c.moveToFirst()){
            do {
                Base_tests quiz = new Base_tests();
                quiz.setTest_id(c.getString(c.getColumnIndexOrThrow(Constantes.ID_REMOTA)));
                quiz.setTest(c.getString(c.getColumnIndexOrThrow(ContractTests.Columnas.KEY_TEST)));
                quiz.setDescripcion(c.getString(c.getColumnIndexOrThrow(ContractTests.Columnas.KEY_DESCRIPTION)));
                quiz.setF_inicio(c.getString(c.getColumnIndexOrThrow(ContractTests.Columnas.KEY_DATE_START)));
                quiz.setH_inicio(c.getString(c.getColumnIndexOrThrow(ContractTests.Columnas.KEY_HOUR_START)));
                quiz.setF_limite(c.getString(c.getColumnIndexOrThrow(ContractTests.Columnas.KEY_DATE_LIMIT)));
                quiz.setH_limite(c.getString(c.getColumnIndexOrThrow(ContractTests.Columnas.KEY_HOUR_LIMIT)));
                quiz.setActive(c.getString(c.getColumnIndexOrThrow(ContractTests.Columnas.KEY_ACTIVE)));
                quizList.add(quiz);
            } while(c.moveToNext());

        }
        return quizList;
    }


    public List<Base_preguntas> getAllQuestionsById(String test_id){
        List<Base_preguntas> QuestionList = new ArrayList<>();
        // Iniciando la BaseDeDatos
        db = getReadableDatabase();
        String query = "SELECT * FROM " + ContractPreguntas.TABLE_QUEST + " WHERE " + ContractPreguntas.Columnas.KEY_TEST_ID + "=?";
        Cursor c = db.rawQuery(query, new String[]{test_id});

        if(c.moveToFirst()){
            do {
                Base_preguntas question = new Base_preguntas();
                question.setQuestion(c.getString(c.getColumnIndexOrThrow(ContractPreguntas.Columnas.KEY_QUES)));
                question.setAnswer(c.getString(c.getColumnIndexOrThrow(ContractPreguntas.Columnas.KEY_ANSWER)));
                question.setOpta(c.getString(c.getColumnIndexOrThrow(ContractPreguntas.Columnas.KEY_OPTA)));
                question.setOptb(c.getString(c.getColumnIndexOrThrow(ContractPreguntas.Columnas.KEY_OPTB)));
                question.setOptc(c.getString(c.getColumnIndexOrThrow(ContractPreguntas.Columnas.KEY_OPTC)));
                question.setCanal(c.getString(c.getColumnIndexOrThrow(ContractPreguntas.Columnas.KEY_CANAL)));
                question.setTiempo(c.getString(c.getColumnIndexOrThrow(ContractPreguntas.Columnas.KEY_TIEMPO)));
                question.setTest_id(c.getString(c.getColumnIndexOrThrow(ContractPreguntas.Columnas.KEY_TEST_ID)));

                QuestionList.add(question);
            } while(c.moveToNext());
        }
        return QuestionList;
    }



    public List<String> getTipoPromocion(String canal) {
        List<String> operadores = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT DISTINCT " + ContractPromociones.Columnas.TIPO + " FROM " + ContractPromociones.PROMOCIONES +
                        " WHERE " + ContractPromociones.Columnas.CANAL + "=? ORDER BY " + ContractPromociones.Columnas.TIPO;
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{canal});

        operadores.add("Seleccione");
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractPromociones.Columnas.TIPO)));
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public List<String> getDescripcionPromocion(String canal) {
        List<String> operadores = new ArrayList<String>();
        Cursor cursor = null;



        if (canal.equalsIgnoreCase("AUTOSERVICIO")){

            // Select All Query
            String selectQuery = "SELECT DISTINCT " + ContractPromociones.Columnas.DESCRIPCION + " FROM " + ContractPromociones.PROMOCIONES + " WHERE " +
                    ContractPromociones.Columnas.CANAL +"=?"
//                    ContractPromociones.Columnas.DESCRIPCION + " LIKE '%Descuento en precio%' " + " OR "+
//                    ContractPromociones.Columnas.DESCRIPCION + " LIKE '%Producto gratis%' " + " OR "+
//                    ContractPromociones.Columnas.DESCRIPCION + " LIKE '%Promocional gratis%' " + " OR "+
//                    ContractPromociones.Columnas.DESCRIPCION + " LIKE '%Canje / Cartilla%' " + " OR "+
//                    ContractPromociones.Columnas.DESCRIPCION + " LIKE '%Cupon%' " + " OR "+
//                    ContractPromociones.Columnas.DESCRIPCION + " LIKE '%Combo Descuento%' " + " OR "+
//                    ContractPromociones.Columnas.DESCRIPCION + " LIKE '%Cambio de imagen%' " + " OR "+
//                    ContractPromociones.Columnas.DESCRIPCION + " LIKE '%2do a mitad de precio%' " + " OR "+
//                    ContractPromociones.Columnas.DESCRIPCION + " LIKE '%3 x 2%' "
                    + " ORDER BY " + ContractPromociones.Columnas.DESCRIPCION;
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{canal});


        }else if(canal.equalsIgnoreCase("MAYORISTA")){

            // Select All Query
            String selectQuery = "SELECT DISTINCT " + ContractPromociones.Columnas.DESCRIPCION + " FROM " + ContractPromociones.PROMOCIONES + " WHERE " +
                    ContractPromociones.Columnas.CANAL +"=? "+
//                    ContractPromociones.Columnas.DESCRIPCION + " LIKE '%Descuento en precio%' " + " OR "+
//                    ContractPromociones.Columnas.DESCRIPCION + " LIKE '%Producto gratis%' " + " OR "+
//                    ContractPromociones.Columnas.DESCRIPCION + " LIKE '%Promocional gratis%' " + " OR "+
//                    ContractPromociones.Columnas.DESCRIPCION + " LIKE '%Canje / Cartilla%' "
                     " ORDER BY " + ContractPromociones.Columnas.DESCRIPCION;
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{canal});


        }


        operadores.add("Seleccione");
        // looping through all rows and adding to list
        if (cursor != null && cursor.moveToFirst()) {
            do {
                operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractPromociones.Columnas.DESCRIPCION)));
            } while (cursor.moveToNext());
        }
        // closing connection
        if (cursor != null) cursor.close();
        if (db != null) db.close();
        // returning lables
        return operadores;
    }

    public List<String> getBrandExhibicion(String categoria, String sector) {
        List<String> operadores = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT DISTINCT " + ContractPortafolioProductos.Columnas.CONTENIDO + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " +
                ContractPortafolioProductos.Columnas.CATEGORY +"=? AND " +
                ContractPortafolioProductos.Columnas.SECTOR + "=?";
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{categoria, sector});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.CONTENIDO)));
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public ArrayList<LogUser> getLog(String usuario, String fecha) {
        ArrayList<LogUser> operadores = new ArrayList<LogUser>();

        // Select All Query
        String selectQuery = "SELECT * FROM " + ContractLog.LOG + " WHERE " +
                ContractLog.Columnas.USUARIO +"=? AND " +
                ContractLog.Columnas.FECHA +"=? ";
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{usuario, fecha});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                LogUser logUser = new LogUser();
                logUser.setUsuario(cursor.getString(cursor.getColumnIndexOrThrow(ContractLog.Columnas.USUARIO)));
                logUser.setFecha(cursor.getString(cursor.getColumnIndexOrThrow(ContractLog.Columnas.FECHA)));
                logUser.setHora(cursor.getString(cursor.getColumnIndexOrThrow(ContractLog.Columnas.HORA)));
                logUser.setAccion(cursor.getString(cursor.getColumnIndexOrThrow(ContractLog.Columnas.ACCION)));
                operadores.add(logUser);
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public List<String> getSubcategoriaExhibicion(String categoria, String tipo, String fabricante) {
        List<String> operadores = new ArrayList<String>();

        Cursor cursor;
        String selectQuery = "";

        String byFabricante = ContractPortafolioProductos.Columnas.FABRICANTE + " NOT LIKE ? ";

        if (tipo.equalsIgnoreCase("MARCA_PROPIA")) {
            byFabricante = ContractPortafolioProductos.Columnas.FABRICANTE + " LIKE ? ";
        }

        selectQuery = "SELECT DISTINCT " + ContractPortafolioProductos.Columnas.CATEGORY +
                " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS +
                " WHERE "  + byFabricante + " AND " +
                ContractPortafolioProductos.Columnas.SECTOR +"=?";
        db = this.getReadableDatabase();
        cursor = db.rawQuery(selectQuery, new String[]{"%" + fabricante + "%", categoria});

        operadores.add("Seleccione");
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.CATEGORY)));
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public List<String> getSegmentoExhibicion(String categoria, String sector, String segmento1) {
        List<String> operadores = new ArrayList<String>();

        Cursor cursor;
        String selectQuery = "";
        /*if (sector.equalsIgnoreCase("TODOS") && categoria.equalsIgnoreCase("TODOS") && segmento1.equalsIgnoreCase("TODOS")) {
            selectQuery = "SELECT DISTINCT " + ContractPortafolioProductos.Columnas.SEGMENTO + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS_MAYO;
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{});
        }else if (sector.equalsIgnoreCase("TODOS") && categoria.equalsIgnoreCase("TODOS") && !segmento1.equalsIgnoreCase("TODOS")) {
            selectQuery = "SELECT DISTINCT " + ContractPortafolioProductos.Columnas.SEGMENTO + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS_MAYO + " WHERE " +
                    ContractPortafolioProductos.Columnas.CATEGORIA +"=?";
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{segmento1});
        }else if (sector.equalsIgnoreCase("TODOS") && !categoria.equalsIgnoreCase("TODOS") && segmento1.equalsIgnoreCase("TODOS")) {
            selectQuery = "SELECT DISTINCT " + ContractPortafolioProductos.Columnas.SEGMENTO + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS_MAYO + " WHERE " +
                    ContractPortafolioProductos.Columnas.CATEGORY +"=?";
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{categoria});
        }else if (sector.equalsIgnoreCase("TODOS") && !categoria.equalsIgnoreCase("TODOS") && !segmento1.equalsIgnoreCase("TODOS")) {
            selectQuery = "SELECT DISTINCT " + ContractPortafolioProductos.Columnas.SEGMENTO + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS_MAYO + " WHERE " +
                    ContractPortafolioProductos.Columnas.CATEGORY +"=? AND " +
                    ContractPortafolioProductos.Columnas.CATEGORIA +"=?";
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{categoria,segmento1});
        }else if (!sector.equalsIgnoreCase("TODOS") && categoria.equalsIgnoreCase("TODOS") && segmento1.equalsIgnoreCase("TODOS")) {
            selectQuery = "SELECT DISTINCT " + ContractPortafolioProductos.Columnas.SEGMENTO + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS_MAYO + " WHERE " +
                    ContractPortafolioProductos.Columnas.CATEGORIA +"=?";
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{sector});
        }else if (!sector.equalsIgnoreCase("TODOS") && categoria.equalsIgnoreCase("TODOS") && !segmento1.equalsIgnoreCase("TODOS")) {
            selectQuery = "SELECT DISTINCT " + ContractPortafolioProductos.Columnas.SEGMENTO + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS_MAYO + " WHERE " +
                    ContractPortafolioProductos.Columnas.CATEGORIA +"=? AND " +
                    ContractPortafolioProductos.Columnas.CATEGORIA +"=?";
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{sector,segmento1});
        }else if (!sector.equalsIgnoreCase("TODOS") && !categoria.equalsIgnoreCase("TODOS") && segmento1.equalsIgnoreCase("TODOS")) {
            selectQuery = "SELECT DISTINCT " + ContractPortafolioProductos.Columnas.SEGMENTO + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS_MAYO + " WHERE " +
                    ContractPortafolioProductos.Columnas.CATEGORIA +"=? AND " +
                    ContractPortafolioProductos.Columnas.CATEGORY +"=?";
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{sector,categoria});
        }else{*/
            selectQuery = "SELECT DISTINCT " + ContractPortafolioProductos.Columnas.SEGMENTO + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " +
                    ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                    ContractPortafolioProductos.Columnas.CATEGORY +"=? AND " +
                    ContractPortafolioProductos.Columnas.SUBCATEGORIA + "=?";
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{sector, categoria, segmento1});
        //}

        ////operadores.add("Todos");
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.SEGMENTO)));
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public List<String> getBrandValores(String categoria, String subcategoria, String codigo, String canal, String subcanal, String manufacturer) {
        List<String> operadores = new ArrayList<String>();

        Cursor cursor;
        String selectQuery = "";

        if (canal.equalsIgnoreCase("MAYORISTA")) {
            selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.MARCA + " FROM " +
                    ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " +
                    ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                    ContractPortafolioProductos.Columnas.CATEGORY + "=? AND " +
                    ContractPortafolioProductos.Columnas.FABRICANTE + " LIKE ?";
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{categoria, subcategoria, "%" + manufacturer + "%"});
        }else{
            selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.MARCA + " FROM " +
                    ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " +
                    ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                    ContractPortafolioProductos.Columnas.CATEGORY + "=? AND " +
                    ContractPortafolioProductos.Columnas.CADENAS + " LIKE ? AND " +
                    ContractPortafolioProductos.Columnas.FABRICANTE + " LIKE ?";
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{categoria, subcategoria, "%" + subcanal.trim() + "%", "%" + manufacturer + "%"});
        }
        //}

        //operadores.add("Todos");
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.MARCA)));
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public List<String> getBrandValores2(String categoria, String subcategoria, String codigo, String canal, String subcanal, String manufacturer) {
        List<String> operadores = new ArrayList<String>();

        Cursor cursor = null;
        String selectQuery = "";

        if (subcategoria.equalsIgnoreCase("TODOS")) {
            if (canal.equalsIgnoreCase("MAYORISTA")) {
                selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.MARCA + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS +
                        " WHERE " + /*ContractPortafolioProductos.Columnas.MARCA + " <> 'Mi Comisariato' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Coral' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Supermaxi' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Aki' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Ta´Riko' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Santa Maria' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Herbal Supermaxi' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Mayik' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Frac' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Muuu' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Milo' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Nestle' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Maria' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'La Universal' AND " +*/
                        ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                        ContractPortafolioProductos.Columnas.FABRICANTE + " LIKE ? " +
                        " ORDER BY " + ContractPortafolioProductos.Columnas.MARCA;
                db = this.getReadableDatabase();
                cursor = db.rawQuery(selectQuery, new String[]{categoria,"%"+manufacturer+"%"});
            }else if (canal.equalsIgnoreCase("AUTOSERVICIO") && subcanal.equalsIgnoreCase("ALMACENES TIA")) {
                selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.MARCA + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS +
                        " WHERE " + ContractPortafolioProductos.Columnas.MARCA + " <> 'Mi Comisariato' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Coral' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Supermaxi' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Aki' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Santa Maria' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Herbal Supermaxi' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Frac' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Muuu' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Milo' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Nestle' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Maria' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'La Universal' AND " +
                        ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                        ContractPortafolioProductos.Columnas.FABRICANTE + " LIKE ? " +
                        " ORDER BY " + ContractPortafolioProductos.Columnas.MARCA;
                db = this.getReadableDatabase();
                cursor = db.rawQuery(selectQuery, new String[]{categoria,"%"+manufacturer+"%"});
            }else if (canal.equalsIgnoreCase("AUTOSERVICIO") && subcanal.equalsIgnoreCase("AKI")) {
                selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.MARCA + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS +
                        " WHERE " + ContractPortafolioProductos.Columnas.MARCA + " <> 'Mayik' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Ta´Riko' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Herbal Supermaxi' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Supermaxi' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Santa Maria' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Coral' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Mi Comisariato' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Frac' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Muuu' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Milo' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Nestle' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Maria' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'La Universal' AND " +
                        ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                        ContractPortafolioProductos.Columnas.FABRICANTE + " LIKE ?" +
                        " ORDER BY " + ContractPortafolioProductos.Columnas.MARCA;
                db = this.getReadableDatabase();
                cursor = db.rawQuery(selectQuery, new String[]{categoria,"%"+manufacturer+"%"});
            }else if (canal.equalsIgnoreCase("AUTOSERVICIO") && subcanal.equalsIgnoreCase("CORAL HIPERMERCADOS")) {
                selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.MARCA + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS +
                        " WHERE " + ContractPortafolioProductos.Columnas.MARCA + " <> 'Mayik' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Ta´Riko' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Herbal Supermaxi' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Supermaxi' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Santa Maria' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Aki' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Mi Comisariato' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Frac' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Muuu' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Milo' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Nestle' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Maria' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'La Universal' AND " +
                        ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                        ContractPortafolioProductos.Columnas.FABRICANTE + " LIKE ?" +
                        " ORDER BY " + ContractPortafolioProductos.Columnas.MARCA;
                db = this.getReadableDatabase();
                cursor = db.rawQuery(selectQuery, new String[]{categoria,"%"+manufacturer+"%"});
            }else if (canal.equalsIgnoreCase("AUTOSERVICIO") && subcanal.equalsIgnoreCase("SUPERMAXI")) {
                selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.MARCA + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS +
                        " WHERE " + ContractPortafolioProductos.Columnas.MARCA + " <> 'Ta´Riko' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Coral' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Aki' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Mi Comisariato' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Santa Maria' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Mayik' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Frac' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Muuu' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Milo' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Nestle' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Maria' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'La Universal' AND " +
                        ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                        ContractPortafolioProductos.Columnas.FABRICANTE + " LIKE ?" +
                        " ORDER BY " + ContractPortafolioProductos.Columnas.MARCA;
                db = this.getReadableDatabase();
                cursor = db.rawQuery(selectQuery, new String[]{categoria,"%"+manufacturer+"%"});
            }else if (canal.equalsIgnoreCase("AUTOSERVICIO") && subcanal.equalsIgnoreCase("MI COMISARIATO")) {
                selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.MARCA + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS +
                        " WHERE " + ContractPortafolioProductos.Columnas.MARCA + " <> 'Ta´Riko' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Coral' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Aki' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Supermaxi' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Santa Maria' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Mayik' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Herbal Supermaxi' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Frac' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Muuu' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Milo' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Nestle' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Maria' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'La Universal' AND " +
                        ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                        ContractPortafolioProductos.Columnas.FABRICANTE + " LIKE ?" +
                        " ORDER BY " + ContractPortafolioProductos.Columnas.MARCA;
                db = this.getReadableDatabase();
                cursor = db.rawQuery(selectQuery, new String[]{categoria,"%"+manufacturer+"%"});
            }else if (canal.equalsIgnoreCase("AUTOSERVICIO") && subcanal.equalsIgnoreCase("SANTA MARIA")) {
                selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.MARCA + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS +
                        " WHERE " + ContractPortafolioProductos.Columnas.MARCA + " <> 'Ta´Riko' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Coral' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Aki' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Supermaxi' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Mi Comisariato' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Herbal Supermaxi' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Mayik' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Frac' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Muuu' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Milo' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Nestle' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Maria' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'La Universal' AND " +
                        ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                        ContractPortafolioProductos.Columnas.FABRICANTE + " LIKE ?" +
                        " ORDER BY " + ContractPortafolioProductos.Columnas.MARCA;
                db = this.getReadableDatabase();
                cursor = db.rawQuery(selectQuery, new String[]{categoria,"%"+manufacturer+"%"});
            }else{
                selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.MARCA + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS +
                        " WHERE " + ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Frac' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Muuu' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Milo' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Nestle' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Maria' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'La Universal' AND " +
                        ContractPortafolioProductos.Columnas.FABRICANTE + " LIKE ? " +
                        " ORDER BY " + ContractPortafolioProductos.Columnas.MARCA;
                db = this.getReadableDatabase();
                cursor = db.rawQuery(selectQuery, new String[]{categoria,"%"+manufacturer+"%"});
            }
        }else{
            if (canal.equalsIgnoreCase("MAYORISTA")) {
                selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.MARCA + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS +
                        " WHERE " + /*ContractPortafolioProductos.Columnas.MARCA + " <> 'Mi Comisariato' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Coral' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Supermaxi' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Aki' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Ta´Riko' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Santa Maria' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Herbal Supermaxi' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Mayik' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Frac' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Muuu' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Milo' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Nestle' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Maria' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'La Universal' AND " +*/
                        ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                        ContractPortafolioProductos.Columnas.CATEGORY + "=? AND " +
                        ContractPortafolioProductos.Columnas.FABRICANTE + " LIKE ?" +
                        " ORDER BY " + ContractPortafolioProductos.Columnas.MARCA;
                db = this.getReadableDatabase();
                cursor = db.rawQuery(selectQuery, new String[]{categoria,subcategoria,"%"+manufacturer+"%"});
            }else if (canal.equalsIgnoreCase("AUTOSERVICIO") && subcanal.equalsIgnoreCase("ALMACENES TIA")) {
                selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.MARCA + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS +
                        " WHERE " + ContractPortafolioProductos.Columnas.MARCA + " <> 'Mi Comisariato' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Coral' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Supermaxi' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Aki' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Santa Maria' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Herbal Supermaxi' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Frac' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Muuu' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Milo' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Nestle' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Maria' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'La Universal' AND " +
                        ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                        ContractPortafolioProductos.Columnas.CATEGORY + "=? AND " +
                        ContractPortafolioProductos.Columnas.FABRICANTE + " LIKE ?" +
                        " ORDER BY " + ContractPortafolioProductos.Columnas.MARCA;
                db = this.getReadableDatabase();
                cursor = db.rawQuery(selectQuery, new String[]{categoria,subcategoria,"%"+manufacturer+"%"});
            }else if (canal.equalsIgnoreCase("AUTOSERVICIO") && subcanal.equalsIgnoreCase("AKI")) {
                selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.MARCA + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS +
                        " WHERE " + ContractPortafolioProductos.Columnas.MARCA + " <> 'Mayik' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Ta´Riko' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Herbal Supermaxi' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Supermaxi' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Santa Maria' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Coral' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Mi Comisariato' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Frac' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Muuu' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Milo' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Nestle' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Maria' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'La Universal' AND " +
                        ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                        ContractPortafolioProductos.Columnas.CATEGORY + "=? AND " +
                        ContractPortafolioProductos.Columnas.FABRICANTE + " LIKE ?" +
                        " ORDER BY " + ContractPortafolioProductos.Columnas.MARCA;
                db = this.getReadableDatabase();
                cursor = db.rawQuery(selectQuery, new String[]{categoria,subcategoria,"%"+manufacturer+"%"});
            }else if (canal.equalsIgnoreCase("AUTOSERVICIO") && subcanal.equalsIgnoreCase("CORAL HIPERMERCADOS")) {
                selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.MARCA + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS +
                        " WHERE " + ContractPortafolioProductos.Columnas.MARCA + " <> 'Mayik' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Ta´Riko' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Herbal Supermaxi' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Supermaxi' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Santa Maria' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Aki' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Mi Comisariato' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Frac' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Muuu' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Milo' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Nestle' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Maria' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'La Universal' AND " +
                        ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                        ContractPortafolioProductos.Columnas.CATEGORY + "=? AND "  +
                        ContractPortafolioProductos.Columnas.FABRICANTE + " LIKE ? "+
                        " ORDER BY " + ContractPortafolioProductos.Columnas.MARCA;
                db = this.getReadableDatabase();
                cursor = db.rawQuery(selectQuery, new String[]{categoria,subcategoria,"%"+manufacturer+"%"});
            }else if (canal.equalsIgnoreCase("AUTOSERVICIO") && subcanal.equalsIgnoreCase("SUPERMAXI")) {
                selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.MARCA + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS +
                        " WHERE " + ContractPortafolioProductos.Columnas.MARCA + " <> 'Ta´Riko' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Coral' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Aki' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Mi Comisariato' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Santa Maria' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Mayik' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Frac' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Muuu' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Milo' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Nestle' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Maria' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'La Universal' AND " +
                        ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                        ContractPortafolioProductos.Columnas.CATEGORY + "=? AND " +
                        ContractPortafolioProductos.Columnas.FABRICANTE + " LIKE ?" +
                        " ORDER BY " + ContractPortafolioProductos.Columnas.MARCA;
                db = this.getReadableDatabase();
                cursor = db.rawQuery(selectQuery, new String[]{categoria,subcategoria,"%"+manufacturer+"%"});
            }else if (canal.equalsIgnoreCase("AUTOSERVICIO") && subcanal.equalsIgnoreCase("MI COMISARIATO")) {
                selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.MARCA + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS +
                        " WHERE " + ContractPortafolioProductos.Columnas.MARCA + " <> 'Ta´Riko' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Coral' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Aki' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Supermaxi' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Santa Maria' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Mayik' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Herbal Supermaxi' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Frac' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Muuu' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Milo' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Nestle' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Maria' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'La Universal' AND " +
                        ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                        ContractPortafolioProductos.Columnas.CATEGORY + "=? AND " +
                        ContractPortafolioProductos.Columnas.FABRICANTE + " LIKE ? " +
                        " ORDER BY " + ContractPortafolioProductos.Columnas.MARCA;
                db = this.getReadableDatabase();
                cursor = db.rawQuery(selectQuery, new String[]{categoria,subcategoria,"%"+manufacturer+"%"});
            }else if (canal.equalsIgnoreCase("AUTOSERVICIO") && subcanal.equalsIgnoreCase("SANTA MARIA")) {
                selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.MARCA + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS +
                        " WHERE " + ContractPortafolioProductos.Columnas.MARCA + " <> 'Ta´Riko' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Coral' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Aki' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Supermaxi' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Mi Comisariato' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Herbal Supermaxi' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Frac' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Muuu' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Milo' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Nestle' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Maria' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'La Universal' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Mayik' AND " +
                        ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                        ContractPortafolioProductos.Columnas.CATEGORY + "=? AND " +
                        ContractPortafolioProductos.Columnas.FABRICANTE + " LIKE ?" +
                        " ORDER BY " + ContractPortafolioProductos.Columnas.MARCA;
                db = this.getReadableDatabase();
                cursor = db.rawQuery(selectQuery, new String[]{categoria,subcategoria,"%"+manufacturer+"%"});
            }else{
                selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.MARCA + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS +
                        " WHERE " + ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                        ContractPortafolioProductos.Columnas.CATEGORY + "=? AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Frac' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Muuu' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Milo' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Nestle' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Maria' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'La Universal' AND " +
                        ContractPortafolioProductos.Columnas.FABRICANTE + " LIKE ?" +
                        " ORDER BY " + ContractPortafolioProductos.Columnas.MARCA;
                db = this.getReadableDatabase();
                cursor = db.rawQuery(selectQuery, new String[]{categoria,subcategoria,"%"+manufacturer+"%"});
            }
        }

        operadores.add("Seleccione");
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.MARCA)));
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public List<String> getBrandSugeridos(String categoria, String subcategoria, String codigo_pdv, String canal, String subcanal) {
        List<String> operadores = new ArrayList<String>();

        Cursor cursor = null;
        String selectQuery = "";

        if (subcategoria.equalsIgnoreCase("TODOS")) {
            if (canal.equalsIgnoreCase("MAYORISTA")) {
                selectQuery = "SELECT DISTINCT "+ ContractPrioritario.Columnas.MARCA + " FROM " + ContractPrioritario.PRIORITARIO +
                        " WHERE " + ContractPrioritario.Columnas.CATEGORIA + "=? AND " +
                        ContractPrioritario.Columnas.CODIGO_PDV + "=? " +
                        " ORDER BY " + ContractPrioritario.Columnas.MARCA;
                db = this.getReadableDatabase();
                cursor = db.rawQuery(selectQuery, new String[]{categoria, codigo_pdv});
            }else if (canal.equalsIgnoreCase("AUTOSERVICIO") && subcanal.equalsIgnoreCase("ALMACENES TIA")) {
                selectQuery = "SELECT DISTINCT "+ ContractPrioritario.Columnas.MARCA + " FROM " + ContractPrioritario.PRIORITARIO +
                        " WHERE " + ContractPrioritario.Columnas.MARCA + " <> 'Mi Comisariato' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Coral' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Supermaxi' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Aki' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Santa Maria' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Herbal Supermaxi' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Frac' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Muuu' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Milo' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Nestle' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Maria' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'La Universal' AND " +
                        ContractPrioritario.Columnas.CATEGORIA + "=? AND " +
                        ContractPrioritario.Columnas.CODIGO_PDV + "=? " +
                        " ORDER BY " + ContractPrioritario.Columnas.MARCA;
                db = this.getReadableDatabase();
                cursor = db.rawQuery(selectQuery, new String[]{categoria,codigo_pdv});
            }else if (canal.equalsIgnoreCase("AUTOSERVICIO") && subcanal.equalsIgnoreCase("AKI")) {
                selectQuery = "SELECT DISTINCT "+ ContractPrioritario.Columnas.MARCA + " FROM " + ContractPrioritario.PRIORITARIO +
                        " WHERE " + ContractPrioritario.Columnas.MARCA + " <> 'Mayik' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Ta´Riko' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Herbal Supermaxi' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Supermaxi' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Santa Maria' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Coral' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Mi Comisariato' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Frac' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Muuu' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Milo' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Nestle' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Maria' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'La Universal' AND " +
                        ContractPrioritario.Columnas.CATEGORIA + "=? AND " +
                        ContractPrioritario.Columnas.CODIGO_PDV + "=?" +
                        " ORDER BY " + ContractPrioritario.Columnas.MARCA;
                db = this.getReadableDatabase();
                cursor = db.rawQuery(selectQuery, new String[]{categoria,codigo_pdv});
            }else if (canal.equalsIgnoreCase("AUTOSERVICIO") && subcanal.equalsIgnoreCase("CORAL HIPERMERCADOS")) {
                selectQuery = "SELECT DISTINCT "+ ContractPrioritario.Columnas.MARCA + " FROM " + ContractPrioritario.PRIORITARIO +
                        " WHERE " + ContractPrioritario.Columnas.MARCA + " <> 'Mayik' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Ta´Riko' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Herbal Supermaxi' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Supermaxi' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Santa Maria' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Aki' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Mi Comisariato' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Frac' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Muuu' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Milo' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Nestle' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Maria' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'La Universal' AND " +
                        ContractPrioritario.Columnas.CATEGORIA + "=? AND " +
                        ContractPrioritario.Columnas.CODIGO_PDV + "=?" +
                        " ORDER BY " + ContractPrioritario.Columnas.MARCA;
                db = this.getReadableDatabase();
                cursor = db.rawQuery(selectQuery, new String[]{categoria,codigo_pdv});
            }else if (canal.equalsIgnoreCase("AUTOSERVICIO") && subcanal.equalsIgnoreCase("SUPERMAXI")) {
                selectQuery = "SELECT DISTINCT "+ ContractPrioritario.Columnas.MARCA + " FROM " + ContractPrioritario.PRIORITARIO +
                        " WHERE " + ContractPrioritario.Columnas.MARCA + " <> 'Ta´Riko' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Coral' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Aki' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Mi Comisariato' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Santa Maria' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Mayik' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Frac' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Muuu' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Milo' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Nestle' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Maria' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'La Universal' AND " +
                        ContractPrioritario.Columnas.CATEGORIA + "=? AND " +
                        ContractPrioritario.Columnas.CODIGO_PDV + "=?" +
                        " ORDER BY " + ContractPrioritario.Columnas.MARCA;
                db = this.getReadableDatabase();
                cursor = db.rawQuery(selectQuery, new String[]{categoria,codigo_pdv});
            }else if (canal.equalsIgnoreCase("AUTOSERVICIO") && subcanal.equalsIgnoreCase("MI COMISARIATO")) {
                selectQuery = "SELECT DISTINCT "+ ContractPrioritario.Columnas.MARCA + " FROM " + ContractPrioritario.PRIORITARIO +
                        " WHERE " + ContractPrioritario.Columnas.MARCA + " <> 'Ta´Riko' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Coral' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Aki' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Supermaxi' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Santa Maria' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Mayik' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Herbal Supermaxi' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Frac' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Muuu' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Milo' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Nestle' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Maria' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'La Universal' AND " +
                        ContractPrioritario.Columnas.CATEGORIA + "=? AND " +
                        ContractPrioritario.Columnas.CODIGO_PDV + "=?" +
                        " ORDER BY " + ContractPrioritario.Columnas.MARCA;
                db = this.getReadableDatabase();
                cursor = db.rawQuery(selectQuery, new String[]{categoria,codigo_pdv});
            }else if (canal.equalsIgnoreCase("AUTOSERVICIO") && subcanal.equalsIgnoreCase("SANTA MARIA")) {
                selectQuery = "SELECT DISTINCT "+ ContractPrioritario.Columnas.MARCA + " FROM " + ContractPrioritario.PRIORITARIO +
                        " WHERE " + ContractPrioritario.Columnas.MARCA + " <> 'Ta´Riko' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Coral' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Aki' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Supermaxi' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Mi Comisariato' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Herbal Supermaxi' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Mayik' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Frac' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Muuu' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Milo' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Nestle' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Maria' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'La Universal' AND " +
                        ContractPrioritario.Columnas.CATEGORIA + "=? AND " +
                        ContractPrioritario.Columnas.CODIGO_PDV + "=?" +
                        " ORDER BY " + ContractPrioritario.Columnas.MARCA;
                db = this.getReadableDatabase();
                cursor = db.rawQuery(selectQuery, new String[]{categoria,codigo_pdv});
            }else{
                selectQuery = "SELECT DISTINCT "+ ContractPrioritario.Columnas.MARCA + " FROM " + ContractPrioritario.PRIORITARIO +
                        " WHERE " + ContractPrioritario.Columnas.CATEGORIA + "=? AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Frac' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Muuu' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Milo' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Nestle' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Maria' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'La Universal' AND " +
                        ContractPrioritario.Columnas.CODIGO_PDV + "=? " +
                        " ORDER BY " + ContractPrioritario.Columnas.MARCA;
                db = this.getReadableDatabase();
                cursor = db.rawQuery(selectQuery, new String[]{categoria,codigo_pdv});
            }
        }else{
            if (canal.equalsIgnoreCase("MAYORISTA")) {
                selectQuery = "SELECT DISTINCT "+ ContractPrioritario.Columnas.MARCA + " FROM " + ContractPrioritario.PRIORITARIO +
                        " WHERE " + /*ContractPrioritario.Columnas.MARCA + " <> 'Mi Comisariato' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Coral' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Supermaxi' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Aki' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Ta´Riko' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Santa Maria' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Herbal Supermaxi' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Mayik' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Frac' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Muuu' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Milo' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Nestle' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Maria' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'La Universal' AND " +*/
                        ContractPrioritario.Columnas.CATEGORIA + "=? AND " +
                        ContractPrioritario.Columnas.SUBCATEGORIA + "=? AND " +
                        ContractPrioritario.Columnas.CODIGO_PDV + "=?" +
                        " ORDER BY " + ContractPrioritario.Columnas.MARCA;
                db = this.getReadableDatabase();
                cursor = db.rawQuery(selectQuery, new String[]{categoria,subcategoria,codigo_pdv});
            }else if (canal.equalsIgnoreCase("AUTOSERVICIO") && subcanal.equalsIgnoreCase("ALMACENES TIA")) {
                selectQuery = "SELECT DISTINCT "+ ContractPrioritario.Columnas.MARCA + " FROM " + ContractPrioritario.PRIORITARIO +
                        " WHERE " + ContractPrioritario.Columnas.MARCA + " <> 'Mi Comisariato' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Coral' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Supermaxi' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Aki' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Santa Maria' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Herbal Supermaxi' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Frac' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Muuu' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Milo' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Nestle' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Maria' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'La Universal' AND " +
                        ContractPrioritario.Columnas.CATEGORIA + "=? AND " +
                        ContractPrioritario.Columnas.SUBCATEGORIA + "=? AND " +
                        ContractPrioritario.Columnas.CODIGO_PDV + "=?" +
                        " ORDER BY " + ContractPrioritario.Columnas.MARCA;
                db = this.getReadableDatabase();
                cursor = db.rawQuery(selectQuery, new String[]{categoria,subcategoria,codigo_pdv});
            }else if (canal.equalsIgnoreCase("AUTOSERVICIO") && subcanal.equalsIgnoreCase("AKI")) {
                selectQuery = "SELECT DISTINCT "+ ContractPrioritario.Columnas.MARCA + " FROM " + ContractPrioritario.PRIORITARIO +
                        " WHERE " + ContractPrioritario.Columnas.MARCA + " <> 'Mayik' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Ta´Riko' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Herbal Supermaxi' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Supermaxi' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Santa Maria' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Coral' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Mi Comisariato' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Frac' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Muuu' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Milo' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Nestle' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Maria' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'La Universal' AND " +
                        ContractPrioritario.Columnas.CATEGORIA + "=? AND " +
                        ContractPrioritario.Columnas.SUBCATEGORIA + "=? AND " +
                        ContractPrioritario.Columnas.CODIGO_PDV + "=?" +
                        " ORDER BY " + ContractPrioritario.Columnas.MARCA;
                db = this.getReadableDatabase();
                cursor = db.rawQuery(selectQuery, new String[]{categoria,subcategoria,codigo_pdv});
            }else if (canal.equalsIgnoreCase("AUTOSERVICIO") && subcanal.equalsIgnoreCase("CORAL HIPERMERCADOS")) {
                selectQuery = "SELECT DISTINCT "+ ContractPrioritario.Columnas.MARCA + " FROM " + ContractPrioritario.PRIORITARIO +
                        " WHERE " + ContractPrioritario.Columnas.MARCA + " <> 'Mayik' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Ta´Riko' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Herbal Supermaxi' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Supermaxi' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Santa Maria' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Aki' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Mi Comisariato' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Frac' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Muuu' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Milo' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Nestle' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Maria' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'La Universal' AND " +
                        ContractPrioritario.Columnas.CATEGORIA + "=? AND " +
                        ContractPrioritario.Columnas.SUBCATEGORIA + "=? AND "  +
                        ContractPrioritario.Columnas.CODIGO_PDV + "=? "+
                        " ORDER BY " + ContractPrioritario.Columnas.MARCA;
                db = this.getReadableDatabase();
                cursor = db.rawQuery(selectQuery, new String[]{categoria,subcategoria,codigo_pdv});
            }else if (canal.equalsIgnoreCase("AUTOSERVICIO") && subcanal.equalsIgnoreCase("SUPERMAXI")) {
                selectQuery = "SELECT DISTINCT "+ ContractPrioritario.Columnas.MARCA + " FROM " + ContractPrioritario.PRIORITARIO +
                        " WHERE " + ContractPrioritario.Columnas.MARCA + " <> 'Ta´Riko' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Coral' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Aki' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Mi Comisariato' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Santa Maria' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Mayik' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Frac' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Muuu' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Milo' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Nestle' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Maria' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'La Universal' AND " +
                        ContractPrioritario.Columnas.CATEGORIA + "=? AND " +
                        ContractPrioritario.Columnas.SUBCATEGORIA + "=? AND " +
                        ContractPrioritario.Columnas.CODIGO_PDV + "=?" +
                        " ORDER BY " + ContractPrioritario.Columnas.MARCA;
                db = this.getReadableDatabase();
                cursor = db.rawQuery(selectQuery, new String[]{categoria,subcategoria,codigo_pdv});
            }else if (canal.equalsIgnoreCase("AUTOSERVICIO") && subcanal.equalsIgnoreCase("MI COMISARIATO")) {
                selectQuery = "SELECT DISTINCT "+ ContractPrioritario.Columnas.MARCA + " FROM " + ContractPrioritario.PRIORITARIO +
                        " WHERE " + ContractPrioritario.Columnas.MARCA + " <> 'Ta´Riko' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Coral' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Aki' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Supermaxi' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Santa Maria' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Mayik' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Herbal Supermaxi' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Frac' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Muuu' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Milo' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Nestle' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Maria' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'La Universal' AND " +
                        ContractPrioritario.Columnas.CATEGORIA + "=? AND " +
                        ContractPrioritario.Columnas.SUBCATEGORIA + "=? AND " +
                        ContractPrioritario.Columnas.CODIGO_PDV + "=? " +
                        " ORDER BY " + ContractPrioritario.Columnas.MARCA;
                db = this.getReadableDatabase();
                cursor = db.rawQuery(selectQuery, new String[]{categoria,subcategoria,codigo_pdv});
            }else if (canal.equalsIgnoreCase("AUTOSERVICIO") && subcanal.equalsIgnoreCase("SANTA MARIA")) {
                selectQuery = "SELECT DISTINCT "+ ContractPrioritario.Columnas.MARCA + " FROM " + ContractPrioritario.PRIORITARIO +
                        " WHERE " + ContractPrioritario.Columnas.MARCA + " <> 'Ta´Riko' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Coral' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Aki' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Supermaxi' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Mi Comisariato' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Herbal Supermaxi' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Frac' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Muuu' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Milo' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Nestle' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Maria' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'La Universal' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Mayik' AND " +
                        ContractPrioritario.Columnas.CATEGORIA + "=? AND " +
                        ContractPrioritario.Columnas.SUBCATEGORIA + "=? AND " +
                        ContractPrioritario.Columnas.CODIGO_PDV + "=?" +
                        " ORDER BY " + ContractPrioritario.Columnas.MARCA;
                db = this.getReadableDatabase();
                cursor = db.rawQuery(selectQuery, new String[]{categoria,subcategoria,codigo_pdv});
            }else{
                selectQuery = "SELECT DISTINCT "+ ContractPrioritario.Columnas.MARCA + " FROM " + ContractPrioritario.PRIORITARIO +
                        " WHERE " + ContractPrioritario.Columnas.CATEGORIA + "=? AND " +
                        ContractPrioritario.Columnas.SUBCATEGORIA + "=? AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Frac' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Muuu' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Milo' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Nestle' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'Maria' AND " +
                        ContractPrioritario.Columnas.MARCA + " <> 'La Universal' AND " +
                        ContractPrioritario.Columnas.CODIGO_PDV + "=?" +
                        " ORDER BY " + ContractPrioritario.Columnas.MARCA;
                db = this.getReadableDatabase();
                cursor = db.rawQuery(selectQuery, new String[]{categoria,subcategoria,codigo_pdv});
            }
        }

        operadores.add("Seleccione");
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractPrioritario.Columnas.MARCA)));
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public List<String> getProductosSugeridos(String categoria, String subcategoria, String codigo, String canal, String subcanal, String manufacturer) {
        List<String> operadores = new ArrayList<String>();

        Cursor cursor = null;
        String selectQuery = "";


        if (canal.equalsIgnoreCase("MAYORISTA")) {
            selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.SKU + " FROM " +
                    ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " +
                    ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                    ContractPortafolioProductos.Columnas.CATEGORY + "=? AND " +
                    ContractPortafolioProductos.Columnas.FABRICANTE + " LIKE ?";
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{categoria,subcategoria,"%"+manufacturer+"%"});
        }else{
            selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.SKU + " FROM " +
                    ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " +
                    ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                    ContractPortafolioProductos.Columnas.CATEGORY + "=? AND " +
                    ContractPortafolioProductos.Columnas.CADENAS + " LIKE ?";
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{categoria,subcategoria, "%" + subcanal + "%"});
        }

        operadores.add("Seleccione");
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.SKU)));
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public List<String> getProductosSugeridos2(String categoria, String subcategoria, String codigo, String canal, String subcanal, String manufacturer) {
        List<String> operadores = new ArrayList<String>();

        Cursor cursor = null;
        String selectQuery = "";


        if (canal.equalsIgnoreCase("MAYORISTA")) {
            selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.SEGMENTO + " FROM " +
                    ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " +
                    ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                    ContractPortafolioProductos.Columnas.CATEGORY + "=? AND " +
                    ContractPortafolioProductos.Columnas.FABRICANTE + " LIKE ?";
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{categoria,subcategoria,"%"+manufacturer+"%"});
        }else{
            selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.SEGMENTO + " FROM " +
                    ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " +
                    ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                    ContractPortafolioProductos.Columnas.CATEGORY + "=? AND " +
                    ContractPortafolioProductos.Columnas.CADENAS + " LIKE ?";
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{categoria,subcategoria, "%" + subcanal + "%"});
        }

        operadores.add("Seleccione");
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.SEGMENTO)));
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public List<String> getBrandLogros(String categoria, String sector, String fabricante) {
        List<String> operadores = new ArrayList<String>();

        Cursor cursor;
        String selectQuery = "";
        selectQuery = "SELECT DISTINCT " + ContractPortafolioProductos.Columnas.MARCA + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " +
                ContractPortafolioProductos.Columnas.SECTOR +"=? AND " +
                ContractPortafolioProductos.Columnas.CATEGORY +"=? AND " +
                ContractPortafolioProductos.Columnas.FABRICANTE +" LIKE ?" +
                " ORDER BY " + ContractPortafolioProductos.Columnas.MARCA;
        db = this.getReadableDatabase();
        cursor = db.rawQuery(selectQuery, new String[]{sector,categoria,"%" + fabricante + "%"});
        //}

        //operadores.add("Todos");
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.MARCA)));
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public List<String> getSector() {
        List<String> operadores = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.SECTOR + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS;

        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.SECTOR)));
            } while (cursor.moveToNext() && cursor!=null);
        }
        Log.i("OPERADORES",operadores.size()+"");
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public String getSubCanalPDV(String codigo) {
        String operadores = "";

        // Select All Query
        String selectQuery = "SELECT DISTINCT " + ContractPharmaValue.Columnas.SUBCHANNEL + " FROM " + ContractPharmaValue.POS + " WHERE "+
                ContractPharmaValue.Columnas.POS_ID + "=?";

        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{codigo});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores = cursor.getString(cursor.getColumnIndexOrThrow(ContractPharmaValue.Columnas.SUBCHANNEL));
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables

        return operadores ;
    }

    public String getSupervisorByUser(String user) {
        String operadores = "";

        // Select All Query
        String selectQuery = "SELECT DISTINCT " + ContractPharmaValue.Columnas.SUPERVISOR + " FROM " + ContractPharmaValue.POS + " WHERE "+
                ContractPharmaValue.Columnas.USER + "=? LIMIT 1";

        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{user});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores = cursor.getString(cursor.getColumnIndexOrThrow(ContractPharmaValue.Columnas.SUPERVISOR));
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables

        return operadores ;
    }

    public String getSupervisorByCodigo(String codigo) {
        String operadores = "";

        // Select All Query
        String selectQuery = "SELECT DISTINCT " + ContractPharmaValue.Columnas.SUPERVISOR + " FROM " + ContractPharmaValue.POS + " WHERE "+
                ContractPharmaValue.Columnas.POS_ID + "=?";

        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{codigo});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores = cursor.getString(cursor.getColumnIndexOrThrow(ContractPharmaValue.Columnas.SUPERVISOR));
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables

        return operadores ;
    }

    public List<String> getSectorShare(String modulo) {
        List<String> operadores = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.SECTOR + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS +
                " WHERE " + ContractPortafolioProductos.Columnas.LOCALES + " LIKE ? " +
                " ORDER BY " + ContractPortafolioProductos.Columnas.SECTOR;

        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{"%" + modulo + "%"});

        operadores.add("Seleccione");
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.SECTOR)));
            } while (cursor.moveToNext() && cursor!=null);
        }
        Log.i("OPERADORES",operadores.size()+"");
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public List<String> getCategoriaPDI(String canal) {
        List<String> operadores = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT DISTINCT "+ ContractPDI.Columnas.CATEGORIA + " FROM " + ContractPDI.PDI +
                " WHERE " + ContractPDI.Columnas.CANAL + "=? " +
                " ORDER BY " + ContractPDI.Columnas.CATEGORIA;

        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{canal});

        operadores.add("Seleccione");
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractPDI.Columnas.CATEGORIA)));
            } while (cursor.moveToNext() && cursor!=null);
        }
        Log.i("OPERADORES",operadores.size()+"");
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public String getObjetivoCategoria(String categoria, String canal) {
        String operadores = "%";

        // Select All Query
        String selectQuery = "SELECT DISTINCT "+ ContractPDI.Columnas.OBJETIVO + " FROM " + ContractPDI.PDI +
                " WHERE " + ContractPDI.Columnas.CATEGORIA + "=? " +
                " AND " + ContractPDI.Columnas.CANAL + "=? " +
                " ORDER BY " + ContractPDI.Columnas.CATEGORIA;

        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{categoria, canal});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores = cursor.getString(cursor.getColumnIndexOrThrow(ContractPDI.Columnas.OBJETIVO));
            } while (cursor.moveToNext() && cursor!=null);
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public List<String> getCausalMCI() {
        List<String> operadores = new ArrayList<String>();

        // Select All Query
        String selectQuery =
                "SELECT * FROM " + ContractCausalesMCI.CAUSALES_MCI +
                " GROUP BY " + ContractCausalesMCI.Columnas.CAUSAL +
                " ORDER BY " + ContractCausalesMCI.Columnas.CAUSAL;

        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        operadores.add("Seleccione");
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractCausalesMCI.Columnas.CAUSAL)));
            } while (cursor.moveToNext() && cursor!=null);
        }
        Log.i("OPERADORES",operadores.size()+"");
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public List<String> getCategoriaSugeridos(String codigo_pdv) {
        List<String> operadores = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT DISTINCT "+ ContractPrioritario.Columnas.CATEGORIA +
                            " FROM " + ContractPrioritario.PRIORITARIO +
                            " WHERE " + ContractPrioritario.Columnas.CODIGO_PDV + "=? " +
                            " ORDER BY " + ContractPrioritario.Columnas.CATEGORIA;
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{codigo_pdv});

        operadores.add("Seleccione");
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractPrioritario.Columnas.CATEGORIA)));
            } while (cursor.moveToNext() && cursor!=null);
        }
        Log.i("OPERADORES",operadores.size()+"");
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public String getTotalUniverso(String codigo, String categoria, String subcategoria) {
        String operadores = "";

        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-5"));
        cal.set(Calendar.DAY_OF_MONTH,1);
        DateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        formato.setTimeZone(TimeZone.getTimeZone("GMT-5"));
        // Obtenemos la fecha inicio del mes
        String fecha_inicio_mes = formato.format(cal.getTime());
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.DATE,31);
        formato.setTimeZone(TimeZone.getTimeZone("GMT-5"));
        // Obtenemos la fecha final del mes
        String  fecha_fin_mes = formato.format(cal.getTime());


        // Select All Query
        String selectQuery = "SELECT "+ ContractInsertShare.Columnas.CTMS_PERCHA + " FROM " + ContractInsertShare.INSERT_SHARE +
                " WHERE " + ContractInsertShare.Columnas.CODIGO + "=? " +
                " AND datetime(substr(fecha, 7, 4) || '-' || substr(fecha, 4, 2) || '-' || substr(fecha, 1, 2))" +
                " BETWEEN ? AND ? AND " +
                ContractInsertShare.Columnas.SECTOR + "=? " +
              //  ContractInsertShare.Columnas.CATEGORIA + "=?" +
                " ORDER BY " + ContractInsertShare.Columnas._ID + " DESC LIMIT 1";

        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{codigo,fecha_inicio_mes,fecha_fin_mes,categoria /*,subcategoria*/});

        //operadores.add("Todos");
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertShare.Columnas.CTMS_PERCHA));
            } while (cursor.moveToNext() && cursor!=null);
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public int getTotalUniversoMesAnterior(String codigo, String categoria, String subcategoria) {
        int totalUniversoMesAnterior = 0;

        // Obtenemos las fechas del primero y ultimo dia del mes anterior
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-5"));
        DateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        cal.set(Calendar.DAY_OF_MONTH,1);
        cal.add(Calendar.MONTH,-1);
        formato.setTimeZone(TimeZone.getTimeZone("GMT-5"));
        // Obtenemos la fecha inicio del mes
        String fecha_inicio_mes = formato.format(cal.getTime());
        cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-5"));
        cal.add(Calendar.MONTH,-1);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        formato.setTimeZone(TimeZone.getTimeZone("GMT-5"));
        // Obtenemos la fecha final del mes
        String  fecha_fin_mes = formato.format(cal.getTime());


        // Select All Query
        String selectQuery = "SELECT "+ ContractInsertShare.Columnas.CTMS_PERCHA + " FROM " + ContractInsertShare.INSERT_SHARE +
                " WHERE " + ContractInsertShare.Columnas.CODIGO + "=? " +
                " AND datetime(substr(fecha, 7, 4) || '-' || substr(fecha, 4, 2) || '-' || substr(fecha, 1, 2))" +
                " BETWEEN ? AND ? AND " +
                ContractInsertShare.Columnas.SECTOR + "=? AND " +
                ContractInsertShare.Columnas.CATEGORIA + "=?" +
                " ORDER BY " + ContractInsertShare.Columnas._ID + " DESC LIMIT 1";

        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{codigo,fecha_inicio_mes,fecha_fin_mes,categoria,subcategoria});

        //operadores.add("Todos");
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                totalUniversoMesAnterior = Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertShare.Columnas.CTMS_PERCHA)));
            } while (cursor.moveToNext() && cursor!=null);
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return totalUniversoMesAnterior;
    }



    public String getOtros(String codigo, String categoria, String subcategoria) {
        String operadores = "";

        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-5"));
        cal.set(Calendar.DAY_OF_MONTH,1);
        DateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        formato.setTimeZone(TimeZone.getTimeZone("GMT-5"));
        // Obtenemos la fecha inicio del mes
        String fecha_inicio_mes = formato.format(cal.getTime());
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.DATE,31);
        formato.setTimeZone(TimeZone.getTimeZone("GMT-5"));
        // Obtenemos la fecha final del mes
        String  fecha_fin_mes = formato.format(cal.getTime());




        // Select All Query
        String selectQuery = "SELECT "+ ContractInsertShare.Columnas.OTROS + " FROM " + ContractInsertShare.INSERT_SHARE +
                " WHERE " + ContractInsertShare.Columnas.MANUFACTURER + "='' AND " +
                ContractInsertShare.Columnas.CODIGO + "=? AND " +
                " datetime(substr(fecha, 7, 4) || '-' || substr(fecha, 4, 2) || '-' || substr(fecha, 1, 2))" +
                " BETWEEN ? AND ? AND " +
                ContractInsertShare.Columnas.SECTOR + "=? " +
             //   ContractInsertShare.Columnas.CATEGORIA + "=?" +
                " ORDER BY " + ContractInsertShare.Columnas._ID + " DESC LIMIT 1";

        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{codigo,fecha_inicio_mes,fecha_fin_mes,categoria /*,subcategoria*/});

        //operadores.add("Todos");
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertShare.Columnas.OTROS));

            } while (cursor.moveToNext() && cursor!=null);
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public String getHora(String codigo, String categoria, String subcategoria) {
        String operadores = "";

        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-5"));
        cal.set(Calendar.DAY_OF_MONTH,1);
        DateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        formato.setTimeZone(TimeZone.getTimeZone("GMT-5"));
        // Obtenemos la fecha inicio del mes
        String fecha_inicio_mes = formato.format(cal.getTime());
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.DATE,31);
        formato.setTimeZone(TimeZone.getTimeZone("GMT-5"));
        // Obtenemos la fecha final del mes
        String  fecha_fin_mes = formato.format(cal.getTime());




        // Select All Query
        String selectQuery = "SELECT "+ ContractInsertShare.Columnas.HORA + " FROM " + ContractInsertShare.INSERT_SHARE +
                " WHERE " + ContractInsertShare.Columnas.MANUFACTURER + "='' AND " +
                ContractInsertShare.Columnas.CODIGO + "=? AND " +
                " datetime(substr(fecha, 7, 4) || '-' || substr(fecha, 4, 2) || '-' || substr(fecha, 1, 2))" +
                " BETWEEN ? AND ? AND " +
                ContractInsertShare.Columnas.SECTOR + "=? " +
            //    ContractInsertShare.Columnas.CATEGORIA + "=?" +
                " ORDER BY " + ContractInsertShare.Columnas._ID + " DESC LIMIT 1";

        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{codigo,fecha_inicio_mes,fecha_fin_mes,categoria /*,subcategoria*/});

        //operadores.add("Todos");
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertShare.Columnas.HORA));

            } while (cursor.moveToNext() && cursor!=null);
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public String getTotalCaras(String codigo, String categoria, String subcategoria, String marca) {
        String operadores = "";

        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-5"));
        cal.set(Calendar.DAY_OF_MONTH,1);
        DateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        formato.setTimeZone(TimeZone.getTimeZone("GMT-5"));
        // Obtenemos la fecha inicio del mes
        String fecha_inicio_mes = formato.format(cal.getTime());
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.DATE,31);
        formato.setTimeZone(TimeZone.getTimeZone("GMT-5"));
        // Obtenemos la fecha final del mes
        String  fecha_fin_mes = formato.format(cal.getTime());


        // Select All Query
        String selectQuery = "SELECT "+ ContractInsertShare.Columnas.CTMS_MARCA + " FROM " + ContractInsertShare.INSERT_SHARE +
                " WHERE " + ContractInsertShare.Columnas.CODIGO + "=? " +
                " AND datetime(substr(fecha, 7, 4) || '-' || substr(fecha, 4, 2) || '-' || substr(fecha, 1, 2))" +
                " BETWEEN ? AND ? AND " +
                ContractInsertShare.Columnas.SECTOR + "=? AND " +
             //   ContractInsertShare.Columnas.CATEGORIA + "=? AND " +
                ContractInsertShare.Columnas.MARCA_SELECCIONADA + "=?" +
                " ORDER BY " + ContractInsertShare.Columnas._ID + " DESC LIMIT 1";

        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{codigo,fecha_inicio_mes,fecha_fin_mes,categoria /*,subcategoria */,marca});

        //operadores.add("Todos");
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores = cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertShare.Columnas.CTMS_MARCA));
            } while (cursor.moveToNext() && cursor!=null);
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public List<String> getSector2Promocion(String marca, String categoria, String subcategoria, String presentacion) {
        List<String> operadores = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.SECTOR + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS +
                " WHERE " + ContractPortafolioProductos.Columnas.MARCA + "=? AND " +
                ContractPortafolioProductos.Columnas.CATEGORY + "=? AND " +
                ContractPortafolioProductos.Columnas.SUBCATEGORIA + "=? AND " +
                ContractPortafolioProductos.Columnas.PRESENTACION + "=?";

        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{marca,categoria,subcategoria,presentacion});

        operadores.add("Seleccione");
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.SECTOR)));
            } while (cursor.moveToNext() && cursor!=null);
        }
        Log.i("OPERADORES",operadores.size()+"");
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public List<String> getMarcaPromocion(String categoria,String subcategoria, String tipo, String fabricante, String canal, String subcanal, List<String> marcas_blancas, String modulo) {
        List<String> operadores = new ArrayList<String>();

        String selectQuery = "";
        Cursor cursor = null;
        // Select All Query
//        String selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.MARCA + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS +
//                " WHERE " + ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
//                ContractPortafolioProductos.Columnas.CATEGORY + "=?" +
//                " ORDER BY " + ContractPortafolioProductos.Columnas.MARCA;
//
//        db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery(selectQuery, new String[]{categoria,subcategoria});

        String byFabricante = ContractPortafolioProductos.Columnas.FABRICANTE + " NOT LIKE ? ";

        if (tipo.equalsIgnoreCase("MARCA_PROPIA")) {
            byFabricante = ContractPortafolioProductos.Columnas.FABRICANTE + " LIKE ? ";
        }

        if (canal.equalsIgnoreCase("MAYORISTA")) {
            selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.MARCA + " FROM " +
                    ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " + byFabricante + " AND " +
                    ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                 //   ContractPortafolioProductos.Columnas.CATEGORY + "=? AND " +
                    ContractPortafolioProductos.Columnas.FABRICANTE + " NOT LIKE '% Rosado%' AND " +
                    ContractPortafolioProductos.Columnas.FABRICANTE + " NOT LIKE '% Coral%' AND " +
                    ContractPortafolioProductos.Columnas.FABRICANTE + " NOT LIKE '% Favorita%' AND " +
                    ContractPortafolioProductos.Columnas.FABRICANTE + " NOT LIKE '% Maria%' AND " +
                    ContractPortafolioProductos.Columnas.FABRICANTE + " NOT LIKE '% Tia%' AND " +
                    ContractPortafolioProductos.Columnas.LOCALES + " LIKE ? " +
                    " ORDER BY " + ContractPortafolioProductos.Columnas.MARCA;
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{"%" + fabricante + "%", categoria,/* subcategoria,*/ "%"+ modulo +"%"});
        } else {
            selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.MARCA + " FROM " +
                    ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " + byFabricante + " AND " +
                    ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                //    ContractPortafolioProductos.Columnas.CATEGORY + "=? AND " +
                    ContractPortafolioProductos.Columnas.CADENAS + " LIKE ? AND " +
                    ContractPortafolioProductos.Columnas.LOCALES + " LIKE ? " +
                    " ORDER BY " + ContractPortafolioProductos.Columnas.MARCA;
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{"%" + fabricante + "%", categoria, /* subcategoria,*/ "%" + subcanal.trim() + "%","%"+ modulo +"%"});
        }

        operadores.add("Seleccione");
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.MARCA)));
            } while (cursor.moveToNext() && cursor!=null);
        }
        Log.i("OPERADORES",operadores.size()+"");
        // closing connection
        cursor.close();
        db.close();

        //quitando las marcas que no aplican
        for (int j=0;j<marcas_blancas.size();j++){
            if(operadores.contains(marcas_blancas.get(j))){
                operadores.remove(marcas_blancas.get(j));
            }
        }

        // returning lables
        return operadores;
    }

    public List<String> getSKUPromocion2(String categoria,String subcategoria, String marca, String tipo, String fabricante, String canal, String subcanal, String modulo) {
        List<String> operadores = new ArrayList<String>();

        // Select All Query
//        String selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.SKU + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS +
//                " WHERE " + ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
//                ContractPortafolioProductos.Columnas.CATEGORY + "=? AND " +
//                ContractPortafolioProductos.Columnas.MARCA + "=?" +
//                " ORDER BY " + ContractPortafolioProductos.Columnas.SKU;
//
//        db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery(selectQuery, new String[]{categoria, subcategoria, marca});

        String selectQuery = "";
        Cursor cursor = null;

        String byFabricante = ContractPortafolioProductos.Columnas.FABRICANTE + " NOT LIKE ? ";

        if (tipo.equalsIgnoreCase("MARCA_PROPIA")) {
            byFabricante = ContractPortafolioProductos.Columnas.FABRICANTE + " LIKE ? ";
        }

        if (canal.equalsIgnoreCase("MAYORISTA")) {
            selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.SKU + " FROM " +
                    ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " + byFabricante + " AND " +
                    ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
            //        ContractPortafolioProductos.Columnas.CATEGORY + "=? AND " +
            //        ContractPortafolioProductos.Columnas.MARCA + "=? AND " +
                    ContractPortafolioProductos.Columnas.LOCALES + " LIKE ? " +
                    " ORDER BY " + ContractPortafolioProductos.Columnas.SKU;
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{"%" + fabricante + "%",categoria, /* subcategoria,  marca, */ "%"+ modulo +"%"});
        }else{
            selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.SKU + " FROM " +
                    ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " + byFabricante + " AND " +
                    ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
            //        ContractPortafolioProductos.Columnas.CATEGORY + "=? AND " +
            //        ContractPortafolioProductos.Columnas.MARCA + "=? AND " +
                    ContractPortafolioProductos.Columnas.CADENAS + " LIKE ? AND " +
                    ContractPortafolioProductos.Columnas.LOCALES + " LIKE ? " +
                    " ORDER BY " + ContractPortafolioProductos.Columnas.SKU;
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{"%" + fabricante + "%",categoria ,/* subcategoria, marca,*/ "%" + subcanal.trim() + "%","%"+ modulo +"%"});
        }

        operadores.add("Seleccione Sku");
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.SKU)));
            } while (cursor.moveToNext() && cursor!=null);
        }
        Log.i("OPERADORES",operadores.size()+"");
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public List<String> getCategoriaPromocion(String tipo, String fabricante, String canal, String subcanal, String modulo) {
        List<String> operadores = new ArrayList<String>();

        // Select All Query
//        String selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.SECTOR + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS;
//
//        db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery(selectQuery, new String[]{});

        Cursor cursor = null;
        String selectQuery = null;

        String byFabricante = ContractPortafolioProductos.Columnas.FABRICANTE + " NOT LIKE ? ";

        if (tipo.equalsIgnoreCase("MARCA_PROPIA")) {
            byFabricante = ContractPortafolioProductos.Columnas.FABRICANTE + " LIKE ? ";
        }

        if (canal.equalsIgnoreCase("MAYORISTA")) {
            selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.SECTOR + " FROM " +
                    ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " + byFabricante + " AND " +
                    ContractPortafolioProductos.Columnas.LOCALES + " LIKE ? " ;
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{"%" + fabricante + "%", "%" + modulo + "%"});
        } else if (canal.equalsIgnoreCase("AUTOSERVICIO")) {
            selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.SECTOR + " FROM " +
                    ContractPortafolioProductos.PORTAFOLIOPRODUCTOS +
                    " WHERE " + byFabricante + " AND " + ContractPortafolioProductos.Columnas.CADENAS + " LIKE ? AND " +
                    ContractPortafolioProductos.Columnas.LOCALES + " LIKE ? ";
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{"%" + fabricante + "%", "%" + subcanal.trim() + "%", "%" + modulo + "%"});
        } else {
            selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.SECTOR + " FROM " +
                    ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " + byFabricante + " AND " +
                    ContractPortafolioProductos.Columnas.LOCALES + " LIKE ? ";
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{"%" + fabricante + "%", "%" + modulo + "%"});
        }

        operadores.add("Seleccione");
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.SECTOR)));
            } while (cursor.moveToNext() && cursor!=null);
        }
        Log.i("OPERADORES",operadores.size()+"");
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public List<String> getSectorExhibicion() {
        List<String> operadores = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.SECTOR + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS;

        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        ////operadores.add("Todos");
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.SECTOR)));
            } while (cursor.moveToNext() && cursor!=null);
        }
        Log.i("OPERADORES",operadores.size()+"");
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public List<String> getCategoriaFlooring(String codigo, String canal, String subcanal, String manufacturer) {
        List<String> operadores = new ArrayList<String>();
        //format = "Cruz Azul";

        // Select All Query
        Cursor cursor = null;
        String selectQuery = null;
        if (canal.equalsIgnoreCase("MAYORISTA")) {
            selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.SECTOR + " FROM " +
                    ContractPortafolioProductos.PORTAFOLIOPRODUCTOS +
                    " WHERE " + ContractPortafolioProductos.Columnas.FABRICANTE + " LIKE ?";
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{manufacturer});
        }else if (canal.equalsIgnoreCase("AUTOSERVICIO")) {
            selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.SECTOR + " FROM " +
                    ContractPortafolioProductos.PORTAFOLIOPRODUCTOS +
                    " WHERE " + ContractPortafolioProductos.Columnas.CADENAS + " LIKE ? AND " +
                    ContractPortafolioProductos.Columnas.FABRICANTE + " LIKE ?";
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{"%" + subcanal + "%","%" + manufacturer + "%"});
        }

        //operadores.add("Todos");
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.SECTOR)));
            } while (cursor.moveToNext() && cursor!=null);
        }
        Log.i("OPERADORES",operadores.size()+"");
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public List<String> getCategoriaFlooring2(String codigo, String canal, String subcanal, String manufacturer, String modulo) {
        List<String> operadores = new ArrayList<String>();
        //format = "Cruz Azul";

        // Select All Query
        Cursor cursor = null;
        String selectQuery = null;
        if (canal.equalsIgnoreCase("MAYORISTA")) {
            selectQuery = "SELECT DISTINCT " + ContractPortafolioProductos.Columnas.SECTOR + " FROM " +
                    ContractPortafolioProductos.PORTAFOLIOPRODUCTOS +
                    " WHERE " + ContractPortafolioProductos.Columnas.FABRICANTE + " LIKE ? AND " +
                    ContractPortafolioProductos.Columnas.LOCALES + " LIKE ? " +
                    " ORDER BY " + ContractPortafolioProductos.Columnas.SECTOR;
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{"%" + manufacturer + "%", "%"+ modulo +"%"});
        }else if (canal.equalsIgnoreCase("AUTOSERVICIO")) {
            selectQuery = "SELECT DISTINCT " + ContractPortafolioProductos.Columnas.SECTOR + " FROM " +
                    ContractPortafolioProductos.PORTAFOLIOPRODUCTOS +
                    " WHERE " + ContractPortafolioProductos.Columnas.CADENAS + " LIKE ? AND " +
                    ContractPortafolioProductos.Columnas.FABRICANTE + " LIKE ? AND " +
                    ContractPortafolioProductos.Columnas.LOCALES + " LIKE ? " +
                   // "AND " +
                  //  ContractPortafolioProductos.Columnas.SECTOR + " REGEXP 'Salsas|Pastas|Insecticidas|Detergentes'" +
                    " ORDER BY " + ContractPortafolioProductos.Columnas.SECTOR;
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{"%" + subcanal + "%","%" + manufacturer + "%","%"+ modulo +"%"});
        }
        //Log.i("vvvv",""+manufacturer);

        operadores.add("Seleccione");
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.SECTOR)));
            } while (cursor.moveToNext() && cursor!=null);
        }
        Log.i("OPERADORES",operadores.size()+"");
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public List<String> getCategoriaImpulso(String manufacturer) {
        List<String> operadores = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.SECTOR + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS
                + " WHERE " + ContractPortafolioProductos.Columnas.FABRICANTE + " LIKE ?";

        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{"%" + manufacturer + "%"});

        operadores.add("Seleccione");
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.SECTOR)));
            } while (cursor.moveToNext() && cursor!=null);
        }
        Log.i("OPERADORES",operadores.size()+"");
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public List<String> getSubCategoriaImpulso(String categoria, String manufacturer) {
        List<String> operadores = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.CATEGORY + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS
                + " WHERE " + ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                ContractPortafolioProductos.Columnas.FABRICANTE +" LIKE ?";

        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{categoria, "%" + manufacturer + "%"});

        operadores.add("Seleccione");
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.CATEGORY)));
            } while (cursor.moveToNext() && cursor!=null);
        }
        Log.i("OPERADORES",operadores.size()+"");
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public List<String> getResponsablesValores(String nom_comercial) {
        List<String> operadores = new ArrayList<String>();
        //format = "Cruz Azul";

        // Select All Query
        String selectQuery = "SELECT DISTINCT "+ ContractPromociones.Columnas.TIPO + " FROM " + ContractPromociones.PROMOCIONES + " WHERE " +
                ContractPromociones.Columnas.CANAL + "=?";
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{nom_comercial});

        operadores.add("Seleccione");
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractPromociones.Columnas.TIPO)));
                Log.i("TIPO",cursor.getString(cursor.getColumnIndexOrThrow(ContractPromociones.Columnas.TIPO)));
            } while (cursor.moveToNext() && cursor!=null);
        }
        Log.i("OPERADORES",operadores.size()+"");
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public List<String> getResponsablesOSA(String canal) {
        List<String> operadores = new ArrayList<String>();
        //format = "Cruz Azul";

        // Select All Query
        String selectQuery = "SELECT DISTINCT "+ ContractCausalesOSA.Columnas.RESPONSABLE + " FROM " + ContractCausalesOSA.CAUSALES_OSA + " WHERE " +
                ContractCausalesOSA.Columnas.CANAL + "=?";
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{canal});

        operadores.add("Seleccione");
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractCausalesOSA.Columnas.RESPONSABLE)));
                Log.i("TIPO",cursor.getString(cursor.getColumnIndexOrThrow(ContractCausalesOSA.Columnas.RESPONSABLE)));
            } while (cursor.moveToNext() && cursor!=null);
        }
        Log.i("OPERADORES RESPONSABLES",operadores.size()+"");
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public List<String> getRazonesOSA(String canal, String responsable) {
        List<String> operadores = new ArrayList<String>();
        //format = "Cruz Azul";

        // Select All Query
        String selectQuery = "SELECT DISTINCT "+ ContractCausalesOSA.Columnas.CAUSAL + " FROM " + ContractCausalesOSA.CAUSALES_OSA + " WHERE " +
                ContractCausalesOSA.Columnas.CANAL + "=? AND " +
                ContractCausalesOSA.Columnas.RESPONSABLE + "=?";
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{canal, responsable});

        operadores.add("Seleccione");
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractCausalesOSA.Columnas.CAUSAL)));
                Log.i("TIPO",cursor.getString(cursor.getColumnIndexOrThrow(ContractCausalesOSA.Columnas.CAUSAL)));
            } while (cursor.moveToNext() && cursor!=null);
        }
        Log.i("OPERADORES CAUSALES",operadores.size()+"");
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public List<String> getRazonesValores(String nom_comercial, String responsable) {
        List<String> operadores = new ArrayList<String>();
        //format = "Cruz Azul";

        // Select All Query
        String selectQuery = "SELECT DISTINCT "+ ContractPromociones.Columnas.DESCRIPCION + " FROM " + ContractPromociones.PROMOCIONES + " WHERE " +
                ContractPromociones.Columnas.CANAL + "=? AND " +
                ContractPromociones.Columnas.TIPO + "=?";
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{nom_comercial, responsable});

        operadores.add("Seleccione");
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractPromociones.Columnas.DESCRIPCION)));
            } while (cursor.moveToNext() && cursor!=null);
        }
        Log.i("OPERADORES",operadores.size()+"");
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public boolean tieneJustificacion(String codigo,String usuario){

        boolean id = false;

        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-5"));
        Date currentLocalTime = cal.getTime();
        DateFormat date = new SimpleDateFormat("dd/MM/yyyy");
        date.setTimeZone(TimeZone.getTimeZone("GMT-5"));
        String fechaser = date.format(currentLocalTime);

        String selectQuery = "";

        String tipo = "JUSTIFICACION";

        selectQuery = "SELECT * "+ " FROM " + ContractInsertGps.INSERT_GPS + " WHERE "+
                ContractInsertGps.Columnas.IDPDV +"=? AND " +
                ContractInsertGps.Columnas.USUARIO +"=? AND " +
                ContractInsertGps.Columnas.TIPO +"=? AND " +
                ContractInsertGps.Columnas.FECHA +"=? ";

        // Select All Query

        db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery(selectQuery, new String[]{codigo,usuario,tipo,fechaser});

        if(cursor != null && cursor.moveToFirst()){
            id = true;
        }

        cursor.close();
        db.close();
        return id;

    }


    public boolean tieneAlmuerzoHoy(){

        boolean id = false;

        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-5"));
        Date currentLocalTime = cal.getTime();
        DateFormat date = new SimpleDateFormat("dd/MM/yyyy");
        date.setTimeZone(TimeZone.getTimeZone("GMT-5"));
        String fechaser = date.format(currentLocalTime);

        String selectQuery = "";

        selectQuery = "SELECT * " + " FROM " + ContractInsertAlmuerzo.INSERT_ALMUERZO + " WHERE "+
                ContractInsertGps.Columnas.FECHA +"=? ";

        // Select All Query

        db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery(selectQuery, new String[]{});

        if(cursor != null && cursor.moveToFirst()){
            id = true;
        }

        cursor.close();
        db.close();
        return id;

    }


    public boolean tieneMarcacionesHoy(String codigo,String usuario){

        boolean id = false;

        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-5"));
        Date currentLocalTime = cal.getTime();
        DateFormat date = new SimpleDateFormat("dd/MM/yyyy");
        date.setTimeZone(TimeZone.getTimeZone("GMT-5"));
        String fechaser = date.format(currentLocalTime);

        String selectQuery = "";

        String tipo = "JUSTIFICACION";

        selectQuery = "SELECT * "+ " FROM " + ContractInsertGps.INSERT_GPS + " WHERE "+
                ContractInsertGps.Columnas.IDPDV +"=? AND " +
                ContractInsertGps.Columnas.USUARIO +"=? AND " +
                ContractInsertGps.Columnas.TIPO + " IN ('JUSTIFICACION','ENTRADA','ENTRADA TARDIA','SALIDA') AND " +
             //   ContractInsertGps.Columnas.CAUSAL +" LIKE ? OR " +
             //   ContractInsertGps.Columnas.CAUSAL +" LIKE ? AND " +
                ContractInsertGps.Columnas.FECHA +"=? ";

        // Select All Query

        db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery(selectQuery, new String[]{codigo,usuario,fechaser});

        if(cursor != null && cursor.moveToFirst()){
            id = true;
        }

        cursor.close();
        db.close();
        return id;

    }

    public List<String> getJustificacion(){
        List<String> operadores = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT DISTINCT "+ ContractJustificacion.Columnas.JUSTIFICACION + " FROM " + ContractJustificacion.JUSTIFICACIONES + " ORDER BY _id ASC";
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractJustificacion.Columnas.JUSTIFICACION)));
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public List<String> getTipoPrecios(String subcanal){
        List<String> operadores = new ArrayList<String>();

        operadores.add("Seleccione");
        // Select All Query
        String selectQuery = "SELECT DISTINCT "+ ContractTipoPrecios.Columnas.TIPO_PRECIO + " FROM " + ContractTipoPrecios.TIPO_PRECIOS +
                " WHERE " + ContractTipoPrecios.Columnas.SUBCANAL + " LIKE ?" +
                " ORDER BY _id ASC";
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{"%" + subcanal + "%"});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractTipoPrecios.Columnas.TIPO_PRECIO)));
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public List<String> getMarcasBlancas(){
        List<String> operadores = new ArrayList<String>();

        //operadores.add("Seleccione");
        // Select All Query
        String selectQuery = "SELECT DISTINCT "+ ContractMarcasBlancas.Columnas.MARCAS + " FROM " + ContractMarcasBlancas.MARCAS_BLANCAS + " ORDER BY _id ASC";
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractMarcasBlancas.Columnas.MARCAS)));
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public List<String> getCategoriaValores(String codigo, String canal, String subcanal, String manufacturer) {
        List<String> operadores = new ArrayList<String>();
        //format = "Cruz Azul";

        // Select All Query
        Cursor cursor = null;
        String selectQuery = null;
        if (canal.equalsIgnoreCase("MAYORISTA")) {
            selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.SECTOR + " FROM " +
                    ContractPortafolioProductos.PORTAFOLIOPRODUCTOS +
                    " WHERE " + ContractPortafolioProductos.Columnas.FABRICANTE + " LIKE ?";
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{manufacturer});
        }else if (canal.equalsIgnoreCase("AUTOSERVICIO")) {
            selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.SECTOR + " FROM " +
                    ContractPortafolioProductos.PORTAFOLIOPRODUCTOS +
                    " WHERE " + ContractPortafolioProductos.Columnas.CADENAS + " LIKE ? AND " +
                    ContractPortafolioProductos.Columnas.FABRICANTE + " LIKE ?";
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{"%" + subcanal + "%","%" + manufacturer + "%"});
        }

        //operadores.add("Todos");
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.SECTOR)));
            } while (cursor.moveToNext() && cursor!=null);
        }
        Log.i("OPERADORES",operadores.size()+"");
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public List<String> getCategoriaValores2(String codigo, String canal, String subcanal, String manufacturer) {
        List<String> operadores = new ArrayList<String>();
        //format = "Cruz Azul";

        // Select All Query
        Cursor cursor = null;
        String selectQuery = null;
        if (canal.equalsIgnoreCase("AUTOSERVICIO")) {
            selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.SECTOR + " FROM " +
                    ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " +
                    ContractPortafolioProductos.Columnas.CADENAS + " LIKE ? AND " +
                    ContractPortafolioProductos.Columnas.FABRICANTE + " LIKE ?";
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{"%" + subcanal + "%", "%"+manufacturer+"%"});
        } else {
            selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.SECTOR + " FROM " +
                    ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " +
                    ContractPortafolioProductos.Columnas.FABRICANTE + " LIKE ?";
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{"%"+manufacturer+"%"});
        }

        operadores.add("Seleccione");
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.SECTOR)));
            } while (cursor.moveToNext() && cursor!=null);
        }
        Log.i("OPERADORES",operadores.size()+"");
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public List<String> getCategoriaSugeridos(String codigo, String canal, String subcanal, String manufacturer) {
        List<String> operadores = new ArrayList<String>();
        //format = "Cruz Azul";

        // Select All Query
        Cursor cursor = null;
        String selectQuery = null;
        if (canal.equalsIgnoreCase("MAYORISTA")) {
            selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.SECTOR + " FROM " +
                    ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " +
                    ContractPortafolioProductos.Columnas.FABRICANTE + " LIKE ?";
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{"%"+manufacturer+"%"});
        }else if (canal.equalsIgnoreCase("AUTOSERVICIO")) {
            selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.SECTOR + " FROM " +
                    ContractPortafolioProductos.PORTAFOLIOPRODUCTOS +
                    " WHERE " + ContractPortafolioProductos.Columnas.CADENAS + " LIKE ?";
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{"%" + subcanal + "%"});
        }

        operadores.add("Seleccione");
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.SECTOR)));
            } while (cursor.moveToNext() && cursor!=null);
        }
        Log.i("OPERADORES",operadores.size()+"");
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }


    public List<String> getSectorLogros(String fabricante) {
        List<String> operadores = new ArrayList<String>();
        //format = "Cruz Azul";

        // Select All Query
        String selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.SECTOR + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS +
                            " WHERE " +ContractPortafolioProductos.Columnas.FABRICANTE +" LIKE ?";
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{"%" + fabricante + "%"});

        //operadores.add("Todos");
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.SECTOR)));
            } while (cursor.moveToNext() && cursor!=null);
        }
        Log.i("OPERADORES",operadores.size()+"");
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }




    public List<String> getCausalesProdMalEst(int unidades_defectuosas){
        if (unidades_defectuosas > 0) {
            unidades_defectuosas = 1;
        }
        String uni_defectuosas = String.valueOf(unidades_defectuosas);
        List<String> operadores = new ArrayList<String>();

        Cursor cursor;
        String selectQuery = "";

        selectQuery = "SELECT DISTINCT " + ContractCausalesProdMalEst.Columnas.CAUSAL + " FROM " + ContractCausalesProdMalEst.CAUSALES_PROD_MAL_EST +
                " WHERE " + ContractCausalesProdMalEst.Columnas.CANTIDAD_DEFECTUOSA + "=?";
        db = this.getReadableDatabase();
        cursor = db.rawQuery(selectQuery, new String[]{uni_defectuosas});

        operadores.add("Seleccione");
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractCausalesProdMalEst.Columnas.CAUSAL)));
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public List<String> getCausalLogisticoRelevo() {
        List<String> operadores = new ArrayList<String>();

        Cursor cursor;
        String selectQuery = "";

        selectQuery = "SELECT DISTINCT " + ContractCausalesMCI.Columnas.CAUSAL +
                " FROM " + ContractCausalesMCI.CAUSALES_MCI +
                " ORDER BY " + ContractCausalesMCI.Columnas.CAUSAL;
        db = this.getReadableDatabase();
        cursor = db.rawQuery(selectQuery, new String[]{});  // Sin parámetros WHERE

        operadores.add("Seleccione");
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractCausalesMCI.Columnas.CAUSAL)));
            } while (cursor.moveToNext());
        }

//        // closing connection
//        if (cursor != null && !cursor.isClosed()) {
//            cursor.close();
//        }
//        if (db != null && db.isOpen()) {
//            db.close();
//        }

        // returning labels
        return operadores;
    }





    public List<String> getCategoriaPrecios(String tipo, String fabricante, String codigo, String canal, String subcanal, String modulo) {
        List<String> operadores = new ArrayList<String>();
        //format = "Cruz Azul";
        Log.i("CANAL", canal);
        Log.i("SUBCANAL", subcanal);
        // Select All Query
        String selectQuery = null;
        Cursor cursor = null;

        operadores.add("Seleccione");

        String byFabricante = ContractPortafolioProductos.Columnas.FABRICANTE + " NOT LIKE ? ";

        if (tipo.equalsIgnoreCase("MARCA_PROPIA")) {
            byFabricante = ContractPortafolioProductos.Columnas.FABRICANTE + " LIKE ? ";
        }

        if (canal.equalsIgnoreCase("MAYORISTA")) {
            selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.SECTOR + " FROM " +
                    ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " + byFabricante +
                    " AND " + ContractPortafolioProductos.Columnas.LOCALES + " LIKE ? ";
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{"%" + fabricante + "%", "%" + modulo + "%"});
        } else if (canal.equalsIgnoreCase("AUTOSERVICIO")) {
            selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.SECTOR + " FROM " +
                    ContractPortafolioProductos.PORTAFOLIOPRODUCTOS +
                    " WHERE " + byFabricante + " AND " + ContractPortafolioProductos.Columnas.CADENAS + " LIKE ? " +
                    " AND " + ContractPortafolioProductos.Columnas.LOCALES + " LIKE ? ";
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{"%" + fabricante + "%", "%" + subcanal.trim() + "%", "%" + modulo + "%"});
        } else {
            selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.SECTOR + " FROM " +
                    ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " + byFabricante +
                    " AND " + ContractPortafolioProductos.Columnas.LOCALES + " LIKE ? ";
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{"%" + fabricante + "%", "%" + modulo + "%"});
        }

        //operadores.add("Todos");
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.SECTOR)));
            } while (cursor.moveToNext() && cursor!=null);
        }
        Log.i("OPERADORES_CATEGORIA",operadores.size()+"");
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public List<String> getCategoriaLogistico(String tipo, String fabricante, String codigo, String canal, String subcanal, String modulo) {
        List<String> operadores = new ArrayList<String>();
        //format = "Cruz Azul";
        Log.i("CANAL", canal);
        Log.i("SUBCANAL", subcanal);
        // Select All Query
        String selectQuery = null;
        Cursor cursor = null;

        operadores.add("Seleccione");

        String byFabricante = ContractPortafolioProductos.Columnas.FABRICANTE + " NOT LIKE ? ";

        if (tipo.equalsIgnoreCase("RELEVO")) {
            byFabricante = ContractPortafolioProductos.Columnas.FABRICANTE + " LIKE ? ";
        }

        if (canal.equalsIgnoreCase("MAYORISTA")) {
            selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.SECTOR + " FROM " +
                    ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " + byFabricante +
                    " AND " + ContractPortafolioProductos.Columnas.LOCALES + " LIKE ? ";
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{"%" + fabricante + "%", "%" + modulo + "%"});
        } else if (canal.equalsIgnoreCase("AUTOSERVICIO")) {
            selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.SECTOR + " FROM " +
                    ContractPortafolioProductos.PORTAFOLIOPRODUCTOS +
                    " WHERE " + byFabricante + " AND " + ContractPortafolioProductos.Columnas.CADENAS + " LIKE ? " +
                    " AND " + ContractPortafolioProductos.Columnas.LOCALES + " LIKE ? ";
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{"%" + fabricante + "%", "%" + subcanal.trim() + "%", "%" + modulo + "%"});
        } else {
            selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.SECTOR + " FROM " +
                    ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " + byFabricante +
                    " AND " + ContractPortafolioProductos.Columnas.LOCALES + " LIKE ? ";
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{"%" + fabricante + "%", "%" + modulo + "%"});
        }

        //operadores.add("Todos");
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.SECTOR)));
            } while (cursor.moveToNext() && cursor!=null);
        }
        Log.i("OPERADORES_CATEGORIA",operadores.size()+"");
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }
    public List<String> getCategoriaLogisticoAlerta(String tipo, String fabricante, String codigo, String canal, String subcanal, String modulo) {
        List<String> operadores = new ArrayList<String>();
        //format = "Cruz Azul";
        Log.i("CANAL", canal);
        Log.i("SUBCANAL", subcanal);
        // Select All Query
        String selectQuery = null;
        Cursor cursor = null;

        operadores.add("Seleccione");

        String byFabricante = ContractPortafolioProductos.Columnas.FABRICANTE + " NOT LIKE ? ";

        if (tipo.equalsIgnoreCase("RELEVO")) {
            byFabricante = ContractPortafolioProductos.Columnas.FABRICANTE + " LIKE ? ";
        }

        if (canal.equalsIgnoreCase("MAYORISTA")) {
            selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.SECTOR + " FROM " +
                    ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " + byFabricante +
                    " AND " + ContractPortafolioProductos.Columnas.LOCALES + " LIKE ? ";
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{"%" + fabricante + "%", "%" + modulo + "%"});
        } else if (canal.equalsIgnoreCase("AUTOSERVICIO")) {
            selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.SECTOR + " FROM " +
                    ContractPortafolioProductos.PORTAFOLIOPRODUCTOS +
                    " WHERE " + byFabricante + " AND " + ContractPortafolioProductos.Columnas.CADENAS + " LIKE ? " +
                    " AND " + ContractPortafolioProductos.Columnas.LOCALES + " LIKE ? ";
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{"%" + fabricante + "%", "%" + subcanal.trim() + "%", "%" + modulo + "%"});
        } else {
            selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.SECTOR + " FROM " +
                    ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " + byFabricante +
                    " AND " + ContractPortafolioProductos.Columnas.LOCALES + " LIKE ? ";
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{"%" + fabricante + "%", "%" + modulo + "%"});
        }

        //operadores.add("Todos");
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.SECTOR)));
            } while (cursor.moveToNext() && cursor!=null);
        }
        Log.i("OPERADORES_CATEGORIA",operadores.size()+"");
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }


    public List<String> getCausalLogisticoAlerta() {
        List<String> operadores = new ArrayList<String>();

        Cursor cursor;
        String selectQuery = "";
        selectQuery = "SELECT DISTINCT " + ContractCausalesMCI.Columnas.CAUSAL +
                " FROM " + ContractCausalesMCI.CAUSALES_MCI +
                " ORDER BY " + ContractCausalesMCI.Columnas.CAUSAL;

        db = this.getReadableDatabase();
        cursor = db.rawQuery(selectQuery, new String[]{});

        operadores.add("Todos");

        if (cursor.moveToFirst()) {
            do {
                operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractCausalesMCI.Columnas.CAUSAL)));
            } while (cursor.moveToNext());
        }
        return operadores;
    }


    public List<String> getCategoriaVentas(String canal, String subcanal, String tipo, String fabricante, String modulo){
        List<String> operadores = new ArrayList<String>();
        // Select All Query
        String selectQuery = null;
        Cursor cursor = null;

        operadores.add("Seleccione");

        String byFabricante = ContractPortafolioProductos.Columnas.FABRICANTE + " LIKE ? ";

        if (tipo.equalsIgnoreCase("COMPETENCIA")) {
            byFabricante = ContractPortafolioProductos.Columnas.FABRICANTE + " NOT LIKE ? ";
        }

        if(canal.equalsIgnoreCase("MAYORISTA")){
            selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.SECTOR + " FROM " +
                    ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " + byFabricante +
                   " AND " + ContractPortafolioProductos.Columnas.LOCALES + " LIKE ?";
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{"%" + fabricante + "%","%" + modulo + "%"});
        }else if(canal.equalsIgnoreCase("AUTOSERVICIO")){
            selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.SECTOR + " FROM " +
                    ContractPortafolioProductos.PORTAFOLIOPRODUCTOS +
                    " WHERE " + byFabricante + " AND " + ContractPortafolioProductos.Columnas.CADENAS + " LIKE ? AND " +
                    ContractPortafolioProductos.Columnas.LOCALES + " LIKE ?";
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{"%" + fabricante + "%","%" + subcanal.trim() + "%","%" + modulo + "%"});
        }else{
            selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.SECTOR + " FROM " +
                    ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " + byFabricante +
                    " AND " + ContractPortafolioProductos.Columnas.LOCALES + " LIKE ?";
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{"%" + fabricante + "%","%" + modulo + "%"});
        }

        //operadores.add("Todos");
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.SECTOR)));
            } while (cursor.moveToNext() && cursor!=null);
        }
        Log.i("OPERADORES_CATEGORIA",operadores.size()+"");
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }


    public List<String> getCategoriaEvidencia(){
        List<String> operadores = new ArrayList<String>();

        Cursor cursor;
        String selectQuery = "";

        selectQuery = "SELECT DISTINCT " + ContractPortafolioProductos.Columnas.SECTOR + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " + ContractPortafolioProductos.Columnas.SECTOR + " IS NOT NULL AND " +
                                           ContractPortafolioProductos.Columnas.SECTOR + " !='' ORDER BY " + ContractPortafolioProductos.Columnas.SECTOR + " ASC";
        db = this.getReadableDatabase();
        cursor = db.rawQuery(selectQuery, new String[]{});

        operadores.add("Seleccione");
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.SECTOR)));
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }




    public List<String> getCategoriaProdCad(String fabricante) {
        List<String> operadores = new ArrayList<String>();
        //format = "Cruz Azul";

        // Select All Query
        String selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.CATEGORY + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " +
                            ContractPortafolioProductos.Columnas.FABRICANTE +" LIKE ?";
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{"%" + fabricante + "%"});

        //operadores.add("Todos");
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.CATEGORY)));
            } while (cursor.moveToNext() && cursor!=null);
        }
        Log.i("OPERADORES",operadores.size()+"");
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public List<String> getTipoComboCanjes() {
        List<String> operadores = new ArrayList<String>();
        Cursor cursor;
        String selectQuery = "SELECT DISTINCT " + ContractComboCanjes.Columnas.TIPO_COMBO +
                " FROM " + ContractComboCanjes.COMBO_CANJES +
                " ORDER BY " + ContractComboCanjes.Columnas.TIPO_COMBO;
        db = this.getReadableDatabase();
        cursor = db.rawQuery(selectQuery, new String[]{});
        //}

        operadores.add("Seleccione Combo");
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractComboCanjes.Columnas.TIPO_COMBO)));
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }




    public long getTiempoAlmuerzoEnMs(){
        String tiempo_almuerzo = "";
        //Se Define el formato de hora
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT 5"));
        long milisegundos = 0;


        // Select All Query
        String selectQuery = "SELECT DISTINCT " + ContractTiempoAlmuerzo.Columnas.TIEMPO_ALMUERZO + " FROM " + ContractTiempoAlmuerzo.REPO_TIEMPO_ALMUERZO
                + " ORDER BY " + ContractTiempoAlmuerzo.Columnas._ID + " ASC LIMIT 1";

        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                tiempo_almuerzo = cursor.getString(cursor.getColumnIndexOrThrow(ContractTiempoAlmuerzo.Columnas.TIEMPO_ALMUERZO));
            } while (cursor.moveToNext());
        }

        // En caso, que no exista registro por defecto será de una hora
        if (tiempo_almuerzo == null){
            tiempo_almuerzo = "01:00:00";
        }



        try {
            // Parsea la hora en string a un objeto Date
            Date hora = sdf.parse(tiempo_almuerzo);

            // Obtiene la representación de la hora en milisegundos
            milisegundos = hora.getTime();

            // Muestra el resultado
            System.out.println("Hora en milisegundos: " + milisegundos);
        } catch (ParseException e) {
            // e.printStackTrace();
            System.out.println("error: " + e.getMessage());
        }

        // closing connection
        cursor.close();
        db.close();
        // returning lables

        return milisegundos;
    }





    public String getMecanicaPromo(String descripcion, String canal) {
        String mecanica = "";

        // Select All Query
        String selectQuery = "SELECT DISTINCT " + ContractPromociones.Columnas.MECANICA +
                " FROM " + ContractPromociones.PROMOCIONES + " WHERE " +
                ContractPromociones.Columnas.DESCRIPCION +"=? AND " +
                ContractPromociones.Columnas.CANAL +"=?";
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{descripcion, canal});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                mecanica = cursor.getString(cursor.getColumnIndexOrThrow(ContractPromociones.Columnas.MECANICA));
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return mecanica;
    }

    public List<String> getMecanica(String tipo_combo) {
        List<String> operadores = new ArrayList<String>();
        Cursor cursor;
        String selectQuery = "SELECT DISTINCT " + ContractComboCanjes.Columnas.MECANICA +
                " FROM " + ContractComboCanjes.COMBO_CANJES +
                " WHERE " + ContractComboCanjes.Columnas.TIPO_COMBO + "=? " +
                " ORDER BY " + ContractComboCanjes.Columnas.MECANICA;
        db = this.getReadableDatabase();
        cursor = db.rawQuery(selectQuery, new String[]{tipo_combo});
        //}

        operadores.add("Seleccione Mecanica");
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractComboCanjes.Columnas.MECANICA)));
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public List<String> getCategoriaRotacion(String tipo) {
        List<String> operadores = new ArrayList<String>();

        Cursor cursor;
        String selectQuery = "";

        /*if (sector.equalsIgnoreCase("TODOS")) {
            selectQuery = "SELECT DISTINCT " + ContractPortafolioProductos.Columnas.CATEGORY + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS_MAYO;
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{});
        }else{*/
        selectQuery = "SELECT DISTINCT " + ContractRotacion.Columnas.CATEGORIA +
                " FROM " + ContractRotacion.ROTACION +
                " WHERE " + ContractRotacion.Columnas.TIPO + "=? " +
                " ORDER BY " + ContractRotacion.Columnas.CATEGORIA;
        db = this.getReadableDatabase();
        cursor = db.rawQuery(selectQuery, new String[]{tipo});
        //}

        operadores.add("Seleccione");
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractRotacion.Columnas.CATEGORIA)));
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public List<String> getAlertas() {
        List<String> operadores = new ArrayList<String>();

        Cursor cursor;
        String selectQuery = "";

        selectQuery = "SELECT DISTINCT " + ContractAlertas.Columnas.TIPO_ALERTA +
                " FROM " + ContractAlertas.ALERTAS +
                " ORDER BY " + ContractAlertas.Columnas.TIPO_ALERTA;
        db = this.getReadableDatabase();
        cursor = db.rawQuery(selectQuery, new String[]{});
        //}

        operadores.add("Seleccione");
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractAlertas.Columnas.TIPO_ALERTA)));
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public List<String> getSubcategoriaRotacion(String tipo, String categoria) {
        List<String> operadores = new ArrayList<String>();

        Cursor cursor;
        String selectQuery = "";

        selectQuery = "SELECT DISTINCT " + ContractRotacion.Columnas.SUBCATEGORIA +
                " FROM " + ContractRotacion.ROTACION +
                " WHERE " + ContractRotacion.Columnas.TIPO + "=? AND " +
                ContractRotacion.Columnas.CATEGORIA + "=? " +
                " ORDER BY " + ContractRotacion.Columnas.CATEGORIA;
        db = this.getReadableDatabase();
        cursor = db.rawQuery(selectQuery, new String[]{tipo, categoria});
        //}

        operadores.add("Seleccione");
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractRotacion.Columnas.SUBCATEGORIA)));
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public List<String> getTipoAlerta(String alerta) {
        List<String> operadores = new ArrayList<String>();

        operadores.add("Seleccione");
        operadores.add("Ingreso");
        operadores.add("Solicitud");

        return operadores;
    }

    public List<String> getTipoEjecucionMateriales(String alerta) {
        List<String> operadores = new ArrayList<String>();

        Cursor cursor;
        String selectQuery = "";

        selectQuery = "SELECT DISTINCT " + ContractAlertas.Columnas.CATEGORIA +
                " FROM " + ContractAlertas.ALERTAS +
                " WHERE " + ContractAlertas.Columnas.TIPO_ALERTA + " LIKE ?" +
                " ORDER BY " + ContractAlertas.Columnas.CATEGORIA;
        db = this.getReadableDatabase();
        cursor = db.rawQuery(selectQuery, new String[]{"%" + alerta + "%"});
        //}

        operadores.add("Seleccione");
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractAlertas.Columnas.CATEGORIA)));
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public List<String> getMarcaRotacion(String tipo, String categoria, String subcategoria) {
        List<String> operadores = new ArrayList<String>();

        Cursor cursor;
        String selectQuery = "";

        selectQuery = "SELECT DISTINCT " + ContractRotacion.Columnas.MARCA +
                " FROM " + ContractRotacion.ROTACION +
                " WHERE " + ContractRotacion.Columnas.TIPO + "=? AND " +
                ContractRotacion.Columnas.CATEGORIA + "=? AND " +
                ContractRotacion.Columnas.SUBCATEGORIA + "=? " +
                " ORDER BY " + ContractRotacion.Columnas.MARCA;
        db = this.getReadableDatabase();
        cursor = db.rawQuery(selectQuery, new String[]{tipo, categoria, subcategoria});
        //}

        operadores.add("Seleccione");
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractRotacion.Columnas.MARCA)));
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public List<String> getCategoriaPacks(String fabricante) {
        List<String> operadores = new ArrayList<String>();
        //format = "Cruz Azul";

        // Select All Query
        String selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.CATEGORY + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " +
                ContractPortafolioProductos.Columnas.FABRICANTE +" LIKE ?";
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{"%" + fabricante + "%"});

        //operadores.add("Todos");
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.CATEGORY)));
            } while (cursor.moveToNext() && cursor!=null);
        }
        Log.i("OPERADORES",operadores.size()+"");
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public List<String> getCategoriaShare(String categoria) {
        List<String> operadores = new ArrayList<String>();

        Cursor cursor = null;
        String selectQuery = "";

        operadores.add("Seleccione");

  /*      if (categoria.equalsIgnoreCase("Pastas") || categoria.equalsIgnoreCase("Cuidado de la Piel")) {
            operadores.add("Todos");*/

        if (categoria.equalsIgnoreCase("Pastas")) {
            operadores.add("Todos");
            operadores.add("Especiales");

        }else if (categoria.equalsIgnoreCase("Cuidado de la Piel")) {
            operadores.add("Todos");
        }else{
            selectQuery = "SELECT DISTINCT " + ContractPortafolioProductos.Columnas.CATEGORY + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " +
                    ContractPortafolioProductos.Columnas.SECTOR + "=?" +
                    " ORDER BY " + ContractPortafolioProductos.Columnas.CATEGORY;
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{categoria});
            if (cursor.moveToFirst()) {
                do {
                    operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.CATEGORY)));
                } while (cursor.moveToNext());
            }
            // closing connection
            cursor.close();
            db.close();
        }

        //operadores.add("Todos");
        // looping through all rows and adding to list

        // returning lables
        return operadores;
    }

    public List<String> getSubcategoriaSms(String categoria, String codigo_pdv) {
        List<String> operadores = new ArrayList<String>();

        Cursor cursor = null;
        String selectQuery = "";

            selectQuery = "SELECT DISTINCT " + ContractPrioritario.Columnas.SUBCATEGORIA +
                    " FROM " + ContractPrioritario.PRIORITARIO +
                    " WHERE " + ContractPrioritario.Columnas.CATEGORIA + "=? AND " +
                    ContractPrioritario.Columnas.CODIGO_PDV + "=?" +
                    " ORDER BY " + ContractPrioritario.Columnas.SUBCATEGORIA;
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{categoria, codigo_pdv});

        operadores.add("Seleccione");

        if (cursor.moveToFirst()) {
                do {
                    operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractPrioritario.Columnas.SUBCATEGORIA)));
                } while (cursor.moveToNext());
            }
            // closing connection
            cursor.close();
            db.close();

        //operadores.add("Todos");
        // looping through all rows and adding to list

        // returning lables
        return operadores;
    }

    public List<String> getSegmentoShare(String sector, String categoria) {
        List<String> operadores = new ArrayList<String>();

        Cursor cursor;
        String selectQuery = "";
        /*if (categoria.equalsIgnoreCase("TODOS") && subcategoria.equalsIgnoreCase("TODOS")) {
            selectQuery = "SELECT DISTINCT " + ContractPortafolioProductos.Columnas.SEGMENTO + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS_MAYO;
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{});
        }else if (!categoria.equalsIgnoreCase("TODOS") && subcategoria.equalsIgnoreCase("TODOS")) {
            selectQuery = "SELECT DISTINCT " + ContractPortafolioProductos.Columnas.SEGMENTO + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS_MAYO + " WHERE " +
                    ContractPortafolioProductos.Columnas.CATEGORY + "=?";
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{categoria});
        }else if (categoria.equalsIgnoreCase("TODOS") && !subcategoria.equalsIgnoreCase("TODOS")) {
            selectQuery = "SELECT DISTINCT " + ContractPortafolioProductos.Columnas.SEGMENTO + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS_MAYO + " WHERE " +
                    ContractPortafolioProductos.Columnas.CATEGORIA + "=?";
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{subcategoria});
        }else{*/
            selectQuery = "SELECT DISTINCT " + ContractPortafolioProductos.Columnas.SEGMENTO + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " +
                    ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                    ContractPortafolioProductos.Columnas.CATEGORY + "=?";
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{sector, categoria});
        //}

        //operadores.add("Todos");
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.SEGMENTO)));
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public List<String> getCategoriaPromocion(String marca) {
        List<String> operadores = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT DISTINCT " + ContractPortafolioProductos.Columnas.CATEGORY  + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " +
                ContractPortafolioProductos.Columnas.MARCA + "=?";
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{marca});

        operadores.add("Seleccione");
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.CATEGORY)));
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public List<String> getCategoriaExhibicion(String tipo, String fabricante, String modulo) {
        List<String> operadores = new ArrayList<String>();

        Cursor cursor;
        String selectQuery = "";

        String byFabricante = ContractPortafolioProductos.Columnas.FABRICANTE + " NOT LIKE ? ";

        if (tipo.equalsIgnoreCase("MARCA_PROPIA")) {
            byFabricante = ContractPortafolioProductos.Columnas.FABRICANTE + " LIKE ? ";
        }

        selectQuery = "SELECT DISTINCT " + ContractPortafolioProductos.Columnas.SECTOR + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS
                + " WHERE " + byFabricante + " AND " + ContractPortafolioProductos.Columnas.LOCALES + " LIKE ? ";
        db = this.getReadableDatabase();
        cursor = db.rawQuery(selectQuery, new String[]{"%" + fabricante + "%","%" + modulo + "%"});

        operadores.add("Seleccione");
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.SECTOR)));
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public List<String> getCategoriaLogisticoPreforma(String tipo, String fabricante, String modulo) {
        List<String> operadores = new ArrayList<String>();

        Cursor cursor;
        String selectQuery = "";

        String byFabricante = ContractPortafolioProductos.Columnas.FABRICANTE + " NOT LIKE ? ";

        if (tipo.equalsIgnoreCase("PREFORMA")) {
            byFabricante = ContractPortafolioProductos.Columnas.FABRICANTE + " LIKE ? ";
        }

        selectQuery = "SELECT DISTINCT " + ContractPortafolioProductos.Columnas.SECTOR + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS
                + " WHERE " + byFabricante + " AND " + ContractPortafolioProductos.Columnas.LOCALES + " LIKE ? ";
        db = this.getReadableDatabase();
        cursor = db.rawQuery(selectQuery, new String[]{"%" + fabricante + "%","%" + modulo + "%"});

        operadores.add("Seleccione");
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.SECTOR)));
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }


    public List<String> getTipoExhibicion() {
        List<String> operadores = new ArrayList<String>();

        Cursor cursor;
        String selectQuery = "";


        selectQuery = "SELECT DISTINCT " + ContractTipoExh.Columnas.EXHIBICION + " FROM " + ContractTipoExh.TABLE_NAME;
        db = this.getReadableDatabase();
        cursor = db.rawQuery(selectQuery, new String[]{});

        operadores.add("Seleccione");
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractTipoExh.Columnas.EXHIBICION)));
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public List<String> getSubcategoriaFlooring(String categoria, String codigo, String canal, String subcanal, String manufacturer) {
        List<String> operadores = new ArrayList<String>();

        Cursor cursor;
        String selectQuery = "";


        if (canal.equalsIgnoreCase("MAYORISTA")) {
            selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.CATEGORY + " FROM " +
                    ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " +
                    ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                    ContractPortafolioProductos.Columnas.FABRICANTE + " LIKE ?";
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{categoria, "%" + manufacturer + "%"});
        }else{
            selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.CATEGORY + " FROM " +
                    ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " +
                    ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                    ContractPortafolioProductos.Columnas.CADENAS + " LIKE ? AND " +
                    ContractPortafolioProductos.Columnas.FABRICANTE + " LIKE ?";
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{categoria, "%" + subcanal + "%", "%" + manufacturer + "%"});
        }

        //operadores.add("Todos");
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.CATEGORY)));
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public List<String> getSubcategoriaFlooring2(String categoria, String codigo, String canal, String subcanal, String manufacturer, String modulo) {
        List<String> operadores = new ArrayList<String>();

        Cursor cursor;
        String selectQuery = "";

        if (canal.equalsIgnoreCase("MAYORISTA")) {
//            if (categoria.equalsIgnoreCase("INSECTICIDAS")) {
//
//                selectQuery = "SELECT DISTINCT " + ContractPortafolioProductos.Columnas.CATEGORY + " FROM " +
//                        ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " +
//                        ContractPortafolioProductos.Columnas.SECTOR + "=? AND (" +
//                        ContractPortafolioProductos.Columnas.FABRICANTE + " LIKE ? OR " +
//                        ContractPortafolioProductos.Columnas.FABRICANTE + "='Sc Jhonson')" +
//                        " ORDER BY " + ContractPortafolioProductos.Columnas.CATEGORY;
//                db = this.getReadableDatabase();
//                cursor = db.rawQuery(selectQuery, new String[]{categoria, "%" + manufacturer + "%"});
//            } else {
                selectQuery = "SELECT DISTINCT " + ContractPortafolioProductos.Columnas.CATEGORY + " FROM " +
                        ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " +
                        ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                        ContractPortafolioProductos.Columnas.FABRICANTE + " LIKE ? AND " +
                        ContractPortafolioProductos.Columnas.LOCALES + " LIKE ? " +
                        " ORDER BY " + ContractPortafolioProductos.Columnas.CATEGORY;
                db = this.getReadableDatabase();
                cursor = db.rawQuery(selectQuery, new String[]{categoria, "%" + manufacturer + "%","%"+ modulo +"%"});
//            }
        }else{
            selectQuery = "SELECT DISTINCT " + ContractPortafolioProductos.Columnas.CATEGORY + " FROM " +
                    ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " +
                    ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                    ContractPortafolioProductos.Columnas.CADENAS + " LIKE ? AND " +
                    ContractPortafolioProductos.Columnas.FABRICANTE + " LIKE ? AND " +
                    ContractPortafolioProductos.Columnas.LOCALES + " LIKE ? " +
                    " ORDER BY " + ContractPortafolioProductos.Columnas.CATEGORY;
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{categoria, "%" + subcanal + "%","%" + manufacturer + "%","%"+ modulo +"%"});
        }

        operadores.add("Seleccione");
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.CATEGORY)));
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public List<String> getSubcategoriaPrecios(String categoria, String tipo, String fabricante, String codigo, String canal, String subcanal, String modulo) {
        List<String> operadores = new ArrayList<String>();

        Cursor cursor = null;
        String selectQuery = "";
        // Select All Query

        operadores.add("Seleccione");

        String byFabricante = ContractPortafolioProductos.Columnas.FABRICANTE + " NOT LIKE ? ";

        if (tipo.equalsIgnoreCase("MARCA_PROPIA")) {
            byFabricante = ContractPortafolioProductos.Columnas.FABRICANTE + " LIKE ? ";
        }

        if (canal.equalsIgnoreCase("MAYORISTA")) {
            selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.CATEGORY + " FROM " +
                    ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " + byFabricante + " AND " +
                    ContractPortafolioProductos.Columnas.SECTOR + "=? " +
                    " AND " + ContractPortafolioProductos.Columnas.LOCALES + " LIKE ? ";
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{"%" + fabricante + "%", categoria, "%" + modulo + "%"});
        }else if (canal.equalsIgnoreCase("AUTOSERVICIO")) {
            selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.CATEGORY + " FROM " +
                    ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " + byFabricante + " AND " +
                    ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                    ContractPortafolioProductos.Columnas.CADENAS + " LIKE ? " +
                    " AND " + ContractPortafolioProductos.Columnas.LOCALES + " LIKE ? ";
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{"%" + fabricante + "%", categoria, "%" + subcanal.trim() + "%","%" + modulo + "%"});
        }else{
            selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.CATEGORY + " FROM " +
                    ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " + byFabricante + " AND " +
                    ContractPortafolioProductos.Columnas.SECTOR + "=? " +
                    " AND " + ContractPortafolioProductos.Columnas.LOCALES + " LIKE ? ";
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{"%" + fabricante + "%", categoria,"%" + modulo + "%"});
        }

        //operadores.add("Todos");
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.CATEGORY)));
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public List<String> getCausalesLogistico(String fabricante, String categoria, String tipo) {
        List<String> operadores = new ArrayList<String>();

        Cursor cursor;
        String selectQueryTodos = "", selectQuery = "";
        // Select All Query
        /*if (sector.equalsIgnoreCase("TODOS")) {
            selectQueryTodos = "SELECT DISTINCT " + ContractPortafolioProductos.Columnas.CATEGORY + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS_MAYO;
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQueryTodos, new String[]{});
        }else{*/
        selectQuery = "SELECT DISTINCT " + ContractCausalesMCI.Columnas.CAUSAL + " FROM " + ContractCausalesMCI.CAUSALES_MCI + " WHERE " +
                ContractPortafolioProductos.Columnas.CATEGORY + "=? AND " +
                ContractCausalesMCI.Columnas.CAUSAL + "=? AND " +
                ContractPortafolioProductos.Columnas.FABRICANTE +" LIKE ?";
        db = this.getReadableDatabase();
        cursor = db.rawQuery(selectQuery, new String[]{categoria,"%" + fabricante + "%"});
        //}

        //operadores.add("Todos");
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractCausalesMCI.Columnas.CAUSAL)));
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public List<String> getSubcategoriaProdCad(String fabricante, String categoria) {
        List<String> operadores = new ArrayList<String>();

        Cursor cursor;
        String selectQueryTodos = "", selectQuery = "";
        // Select All Query
        /*if (sector.equalsIgnoreCase("TODOS")) {
            selectQueryTodos = "SELECT DISTINCT " + ContractPortafolioProductos.Columnas.CATEGORY + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS_MAYO;
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQueryTodos, new String[]{});
        }else{*/
        selectQuery = "SELECT DISTINCT " + ContractPortafolioProductos.Columnas.SUBCATEGORIA + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " +
                    ContractPortafolioProductos.Columnas.CATEGORY + "=? AND " +
                    ContractPortafolioProductos.Columnas.FABRICANTE +" LIKE ?";
        db = this.getReadableDatabase();
        cursor = db.rawQuery(selectQuery, new String[]{categoria,"%" + fabricante + "%"});
        //}

        //operadores.add("Todos");
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.SUBCATEGORIA)));
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public List<String> getSubcategoriaPacks(String fabricante, String categoria) {
        List<String> operadores = new ArrayList<String>();

        Cursor cursor;
        String selectQueryTodos = "", selectQuery = "";
        // Select All Query
        /*if (sector.equalsIgnoreCase("TODOS")) {
            selectQueryTodos = "SELECT DISTINCT " + ContractPortafolioProductos.Columnas.CATEGORY + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS_MAYO;
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQueryTodos, new String[]{});
        }else{*/
        selectQuery = "SELECT DISTINCT " + ContractPortafolioProductos.Columnas.SUBCATEGORIA + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " +
                ContractPortafolioProductos.Columnas.CATEGORY + "=? AND " +
                ContractPortafolioProductos.Columnas.FABRICANTE +" LIKE ?";
        db = this.getReadableDatabase();
        cursor = db.rawQuery(selectQuery, new String[]{categoria,"%" + fabricante + "%"});
        //}

        //operadores.add("Todos");
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.SUBCATEGORIA)));
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public List<String> getSubCategoriaValores(String categoria, String codigo, String canal, String subcanal, String manufacturer) {
        List<String> operadores = new ArrayList<String>();

        Cursor cursor;
        String selectQuery = "";


        if (canal.equalsIgnoreCase("MAYORISTA")) {
            selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.CATEGORY + " FROM " +
                    ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " +
                    ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                    ContractPortafolioProductos.Columnas.FABRICANTE + " LIKE ?";
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{categoria, "%" + manufacturer + "%"});
        }else{
            selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.CATEGORY + " FROM " +
                    ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " +
                    ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                    ContractPortafolioProductos.Columnas.CADENAS + " LIKE ? AND " +
                    ContractPortafolioProductos.Columnas.FABRICANTE + " LIKE ?";
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{categoria, "%" + subcanal + "%", "%" + manufacturer + "%"});
        }

        //operadores.add("Todos");
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.CATEGORY)));
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public List<String> getSubCategoriaSugeridos(String categoria, String codigo, String canal, String subcanal, String manufacturer) {
        List<String> operadores = new ArrayList<String>();

        Cursor cursor = null;
        String selectQuery = "";


        if (canal.equalsIgnoreCase("MAYORISTA")) {
            selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.CATEGORY + " FROM " +
                    ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " +
                    ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                    ContractPortafolioProductos.Columnas.FABRICANTE + " LIKE ?";
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{categoria,"%"+manufacturer+"%"});
        }else{
            selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.CATEGORY + " FROM " +
                    ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " +
                    ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                    ContractPortafolioProductos.Columnas.CADENAS + " LIKE ?";
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{categoria, "%" + subcanal + "%"});
        }

        operadores.add("Seleccione");
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.CATEGORY)));
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public List<String> getContenidoSugeridos(String categoria, String subcategoria, String brand, String codigo_pdv, String canal, String subcanal) {
        List<String> operadores = new ArrayList<String>();

        Cursor cursor = null;
        String selectQuery = "";


        if (canal.equalsIgnoreCase("MAYORISTA")) {
            selectQuery = "SELECT DISTINCT " + ContractPrioritario.Columnas.CONTENIDO + " FROM " +
                    ContractPrioritario.PRIORITARIO + " WHERE " +
                    ContractPrioritario.Columnas.CATEGORIA + "=? AND " +
                    ContractPrioritario.Columnas.SUBCATEGORIA + "=? AND " +
                    ContractPrioritario.Columnas.MARCA + "=? AND " +
                    ContractPrioritario.Columnas.CODIGO_PDV + "=?" +
                    " ORDER BY " + ContractPrioritario.Columnas.CONTENIDO;
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{categoria, subcategoria, brand, codigo_pdv});
        }else{
            selectQuery = "SELECT DISTINCT "+ ContractPrioritario.Columnas.CONTENIDO + " FROM " +
                    ContractPrioritario.PRIORITARIO + " WHERE " +
                    ContractPrioritario.Columnas.CATEGORIA + "=? AND " +
                    ContractPrioritario.Columnas.SUBCATEGORIA + "=? AND " +
                    ContractPrioritario.Columnas.MARCA + "=? AND " +
                    ContractPrioritario.Columnas.CODIGO_PDV + "=?" +
                    " ORDER BY " + ContractPrioritario.Columnas.CONTENIDO;
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{categoria, subcategoria, brand, codigo_pdv});
        }

        operadores.add("Seleccione");
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractPrioritario.Columnas.CONTENIDO)));
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public List<String> getBrandValores3(String categoria, String subcategoria, String codigo, String canal, String subcanal, String manufacturer) {
        List<String> operadores = new ArrayList<String>();

        Cursor cursor = null;
        String selectQuery = "";

        Log.i("CANAL-OSA", canal);
        Log.i("SUBCANAL-OSA", subcanal);
        Log.i("CATEGORIA-OSA", categoria);

        if (canal.equalsIgnoreCase("AUTOSERVICIO")) {
            selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.MARCA + " FROM " +
                    ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " +
                    ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                    ContractPortafolioProductos.Columnas.CATEGORY + "=? AND " +
                    ContractPortafolioProductos.Columnas.CADENAS + " LIKE ? AND " +
                    ContractPortafolioProductos.Columnas.FABRICANTE + " LIKE ?";
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{categoria, subcategoria, "%" + subcanal + "%", "%"+manufacturer+"%"});
        } else {
            selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.MARCA + " FROM " +
                    ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " +
                    ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                    ContractPortafolioProductos.Columnas.CATEGORY + "=? AND " +
                    ContractPortafolioProductos.Columnas.FABRICANTE + " LIKE ?";
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{categoria, subcategoria,"%"+manufacturer+"%"});
        }

        operadores.add("Seleccione");
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.MARCA)));
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public List<String> getSubCategoriaValores2(String categoria, String codigo, String canal, String subcanal, String manufacturer) {
        List<String> operadores = new ArrayList<String>();

        Cursor cursor = null;
        String selectQuery = "";

        Log.i("CANAL-OSA", canal);
        Log.i("SUBCANAL-OSA", subcanal);
        Log.i("CATEGORIA-OSA", categoria);

        if (canal.equalsIgnoreCase("AUTOSERVICIO")) {
            selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.CATEGORY + " FROM " +
                    ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " +
                    ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                    ContractPortafolioProductos.Columnas.CADENAS + " LIKE ? AND " +
                    ContractPortafolioProductos.Columnas.FABRICANTE + " LIKE ?";
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{categoria, "%" + subcanal + "%", "%"+manufacturer+"%"});
        } else {
            selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.CATEGORY + " FROM " +
                    ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " +
                    ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                    ContractPortafolioProductos.Columnas.FABRICANTE + " LIKE ?";
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{categoria, "%"+manufacturer+"%"});
        }

        operadores.add("Seleccione");
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.CATEGORY)));
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public List<String> getCategoriaLogros(String sector, String fabricante) {
        List<String> operadores = new ArrayList<String>();

        Cursor cursor;
        String selectQuery = "";

        // Select All Query
        selectQuery = "SELECT DISTINCT " + ContractPortafolioProductos.Columnas.CATEGORY  + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " +
                ContractPortafolioProductos.Columnas.SECTOR +"=? AND " +
                ContractPortafolioProductos.Columnas.FABRICANTE +" LIKE ?";
        db = this.getReadableDatabase();
        cursor = db.rawQuery(selectQuery, new String[]{sector,"%" + fabricante + "%"});
        //}

        //operadores.add("Todos");
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.CATEGORY)));
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    //Para consumo en Share of Display
    /*public List<String> filtrarListProductos2Share(String sector, String categoria, String segmento) {
        List<String> operadores = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT DISTINCT " + ContractPortafolioProductos.Columnas.MARCA + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS_MAYO + " WHERE "+
                ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                ContractPortafolioProductos.Columnas.CATEGORY + "=? AND " +
                ContractPortafolioProductos.Columnas.SEGMENTO + "=?";
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{sector,categoria,segmento});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.MARCA)));
            } while (cursor.moveToNext());
        }

        Log.i("SHARE",operadores.size()+"");
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }*/
    public ArrayList<BasePortafolioProductos> filtrarListProductos2Share(String categoria, String subcategoria, String canal, String subcanal, List<String> marcas_blancas, String modulo) {
        ArrayList<BasePortafolioProductos> operadores = new ArrayList<BasePortafolioProductos>();

        Log.i("CANAL", canal);
        Log.i("SUBCANAL", subcanal);
        Log.i("MODULO SHARE", modulo);
        subcategoria = "";
        Cursor cursor = null;
        String selectQuery = "";

        if (subcanal.equalsIgnoreCase("NO CADENA")){
            subcanal = "N/A";
        }

        if (subcategoria.equalsIgnoreCase("TODOS")) {
            if (canal.equalsIgnoreCase("MAYORISTA")) {
                selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.MARCA + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS +
                        " WHERE " + ContractPortafolioProductos.Columnas.MARCA + " <> 'Mi Comisariato' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Coral' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Supermaxi' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Aki' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Ta´Riko' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Santa Maria' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Herbal Supermaxi' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Mayik' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Frac' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Muuu' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Milo' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Nestle' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Maria' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'La Universal' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Mas Ahorro' AND " +
                        ContractPortafolioProductos.Columnas.SECTOR + "=? AND "  +
                        ContractPortafolioProductos.Columnas.LOCALES + " LIKE ? "  +
                        " ORDER BY " + ContractPortafolioProductos.Columnas.MARCA;
                db = this.getReadableDatabase();
                cursor = db.rawQuery(selectQuery, new String[]{categoria,"%" + modulo + "%"});
            }else if (canal.equalsIgnoreCase("AUTOSERVICIO") && subcanal.equalsIgnoreCase("ALMACENES TIA")) {
                selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.MARCA + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS +
                        " WHERE " + ContractPortafolioProductos.Columnas.MARCA + " <> 'Mi Comisariato' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Coral' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Supermaxi' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Aki' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Santa Maria' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Herbal Supermaxi' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Frac' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Muuu' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Milo' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Nestle' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Maria' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'La Universal' AND " +
                        ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                        ContractPortafolioProductos.Columnas.LOCALES + " LIKE ? "  +
                        " ORDER BY " + ContractPortafolioProductos.Columnas.MARCA;
                db = this.getReadableDatabase();
                cursor = db.rawQuery(selectQuery, new String[]{categoria,"%" + modulo + "%"});
            }else if (canal.equalsIgnoreCase("AUTOSERVICIO") && subcanal.equalsIgnoreCase("AKI")) {
                selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.MARCA + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS +
                        " WHERE " + ContractPortafolioProductos.Columnas.MARCA + " <> 'Mayik' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Ta´Riko' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Herbal Supermaxi' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Supermaxi' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Santa Maria' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Coral' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Mi Comisariato' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Frac' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Muuu' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Milo' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Nestle' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Maria' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'La Universal' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Mas Ahorro' AND " +
                        ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                        ContractPortafolioProductos.Columnas.LOCALES + " LIKE ? "  +
                        " ORDER BY " + ContractPortafolioProductos.Columnas.MARCA;
                db = this.getReadableDatabase();
                cursor = db.rawQuery(selectQuery, new String[]{categoria, "%" + modulo + "%"});
            }else if (canal.equalsIgnoreCase("AUTOSERVICIO") && subcanal.equalsIgnoreCase("CORAL HIPERMERCADOS")) {
                selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.MARCA + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS +
                        " WHERE " + ContractPortafolioProductos.Columnas.MARCA + " <> 'Mayik' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Ta´Riko' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Herbal Supermaxi' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Supermaxi' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Santa Maria' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Aki' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Mi Comisariato' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Frac' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Muuu' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Milo' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Nestle' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Maria' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'La Universal' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Mas Ahorro' AND " +
                        ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                        ContractPortafolioProductos.Columnas.LOCALES + " LIKE ? "  +
                        " ORDER BY " + ContractPortafolioProductos.Columnas.MARCA;
                db = this.getReadableDatabase();
                cursor = db.rawQuery(selectQuery, new String[]{categoria,"%" + modulo + "%"});
            }else if (canal.equalsIgnoreCase("AUTOSERVICIO") && subcanal.equalsIgnoreCase("SUPERMAXI")) {
                selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.MARCA + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS +
                        " WHERE " + ContractPortafolioProductos.Columnas.MARCA + " <> 'Ta´Riko' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Coral' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Aki' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Mi Comisariato' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Santa Maria' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Mayik' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Frac' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Muuu' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Milo' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Nestle' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Maria' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'La Universal' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Mas Ahorro' AND " +
                        ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                        ContractPortafolioProductos.Columnas.LOCALES + " LIKE ? "  +
                        " ORDER BY " + ContractPortafolioProductos.Columnas.MARCA;
                db = this.getReadableDatabase();
                cursor = db.rawQuery(selectQuery, new String[]{categoria,"%" + modulo + "%"});
            }else if (canal.equalsIgnoreCase("AUTOSERVICIO") && subcanal.equalsIgnoreCase("MI COMISARIATO")) {
                selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.MARCA + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS +
                        " WHERE " + ContractPortafolioProductos.Columnas.MARCA + " <> 'Ta´Riko' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Coral' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Aki' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Supermaxi' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Santa Maria' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Mayik' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Herbal Supermaxi' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Frac' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Muuu' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Milo' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Nestle' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Maria' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'La Universal' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Mas Ahorro' AND " +
                        ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                        ContractPortafolioProductos.Columnas.LOCALES + " LIKE ? "  +
                        " ORDER BY " + ContractPortafolioProductos.Columnas.MARCA;
                db = this.getReadableDatabase();
                cursor = db.rawQuery(selectQuery, new String[]{categoria, "%" + modulo + "%"});
            }else if (canal.equalsIgnoreCase("AUTOSERVICIO") && subcanal.equalsIgnoreCase("SANTA MARIA")) {
                selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.MARCA + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS +
                        " WHERE " + ContractPortafolioProductos.Columnas.MARCA + " <> 'Ta´Riko' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Coral' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Aki' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Supermaxi' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Mi Comisariato' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Herbal Supermaxi' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Mayik' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Frac' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Muuu' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Milo' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Nestle' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Maria' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'La Universal' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Mas Ahorro' AND " +
                        ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                        ContractPortafolioProductos.Columnas.LOCALES + " LIKE ? "  +
                        " ORDER BY " + ContractPortafolioProductos.Columnas.MARCA;
                db = this.getReadableDatabase();
                cursor = db.rawQuery(selectQuery, new String[]{categoria,"%" + modulo + "%"});
            }else{
                selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.MARCA + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS +
                        " WHERE " + ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Frac' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Muuu' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Milo' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Nestle' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Maria' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'La Universal' AND " +
                        ContractPortafolioProductos.Columnas.LOCALES + " LIKE ? "  +
                        " ORDER BY " + ContractPortafolioProductos.Columnas.MARCA;
                db = this.getReadableDatabase();
                cursor = db.rawQuery(selectQuery, new String[]{categoria,"%" + modulo + "%"});
            }
        }else{

            if (canal.equalsIgnoreCase("MAYORISTA")) {
                selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.MARCA + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS +
                        " WHERE " + ContractPortafolioProductos.Columnas.MARCA + " <> 'Mi Comisariato' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Coral' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Supermaxi' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Aki' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Ta´Riko' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Santa Maria' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Herbal Supermaxi' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Mayik' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Frac' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Muuu' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Milo' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Nestle' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Maria' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'La Universal' AND " +
                        ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                        ContractPortafolioProductos.Columnas.LOCALES + " LIKE ? "  +
                        //  ContractPortafolioProductos.Columnas.CATEGORY + "=?" +
                        " ORDER BY " + ContractPortafolioProductos.Columnas.MARCA;
                db = this.getReadableDatabase();
                cursor = db.rawQuery(selectQuery, new String[]{categoria, "%" + modulo + "%"});
            }else if (canal.equalsIgnoreCase("AUTOSERVICIO")) {
                selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.MARCA + " FROM " +
                        ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " +
                        ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                     //   ContractPortafolioProductos.Columnas.CATEGORY + "=? AND " +
                        ContractPortafolioProductos.Columnas.CADENAS + " LIKE ? AND " +
                        ContractPortafolioProductos.Columnas.LOCALES + " LIKE ? "  +
                        " ORDER BY " + ContractPortafolioProductos.Columnas.MARCA;
                db = this.getReadableDatabase();
                cursor = db.rawQuery(selectQuery, new String[]{categoria /*, subcategoria */,"%" + subcanal.trim() + "%","%" + modulo + "%"});
            }

            /*else if (canal.equalsIgnoreCase("AUTOSERVICIO") && subcanal.equalsIgnoreCase("ALMACENES TIA")) {
                selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.MARCA + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS +
                        " WHERE " + ContractPortafolioProductos.Columnas.MARCA + " <> 'Mi Comisariato' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Coral' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Supermaxi' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Aki' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Santa Maria' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Herbal Supermaxi' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Frac' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Muuu' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Milo' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Nestle' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Maria' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'La Universal' AND " +
                        ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                        ContractPortafolioProductos.Columnas.CATEGORY + "=?" +
                        " ORDER BY " + ContractPortafolioProductos.Columnas.MARCA;
                db = this.getReadableDatabase();
                cursor = db.rawQuery(selectQuery, new String[]{categoria,subcategoria});
            }else if (canal.equalsIgnoreCase("AUTOSERVICIO") && subcanal.equalsIgnoreCase("AKI")) {
                selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.MARCA + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS +
                        " WHERE " + ContractPortafolioProductos.Columnas.MARCA + " <> 'Mayik' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Ta´Riko' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Herbal Supermaxi' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Supermaxi' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Santa Maria' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Coral' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Mi Comisariato' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Frac' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Muuu' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Milo' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Nestle' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Maria' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'La Universal' AND " +
                        ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                        ContractPortafolioProductos.Columnas.CATEGORY + "=?" +
                        " ORDER BY " + ContractPortafolioProductos.Columnas.MARCA;
                db = this.getReadableDatabase();
                cursor = db.rawQuery(selectQuery, new String[]{categoria,subcategoria});
            }else if (canal.equalsIgnoreCase("AUTOSERVICIO") && subcanal.equalsIgnoreCase("CORAL HIPERMERCADOS")) {
                selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.MARCA + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS +
                        " WHERE " + ContractPortafolioProductos.Columnas.MARCA + " <> 'Mayik' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Ta´Riko' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Herbal Supermaxi' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Supermaxi' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Santa Maria' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Aki' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Mi Comisariato' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Frac' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Muuu' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Milo' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Nestle' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Maria' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'La Universal' AND " +
                        ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                        ContractPortafolioProductos.Columnas.CATEGORY + "=?" +
                        " ORDER BY " + ContractPortafolioProductos.Columnas.MARCA;
                db = this.getReadableDatabase();
                cursor = db.rawQuery(selectQuery, new String[]{categoria,subcategoria});
            }else if (canal.equalsIgnoreCase("AUTOSERVICIO") && subcanal.equalsIgnoreCase("SUPERMAXI")) {
                selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.MARCA + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS +
                        " WHERE " + ContractPortafolioProductos.Columnas.MARCA + " <> 'Ta´Riko' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Coral' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Aki' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Mi Comisariato' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Santa Maria' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Mayik' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Frac' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Muuu' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Milo' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Nestle' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Maria' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'La Universal' AND " +
                        ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                        ContractPortafolioProductos.Columnas.CATEGORY + "=?" +
                        " ORDER BY " + ContractPortafolioProductos.Columnas.MARCA;
                db = this.getReadableDatabase();
                cursor = db.rawQuery(selectQuery, new String[]{categoria,subcategoria});
            }else if (canal.equalsIgnoreCase("AUTOSERVICIO") && subcanal.equalsIgnoreCase("MI COMISARIATO")) {
                selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.MARCA + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS +
                        " WHERE " + ContractPortafolioProductos.Columnas.MARCA + " <> 'Ta´Riko' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Coral' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Aki' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Supermaxi' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Santa Maria' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Mayik' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Herbal Supermaxi' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Frac' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Muuu' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Milo' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Nestle' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Maria' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'La Universal' AND " +
                        ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                        ContractPortafolioProductos.Columnas.CATEGORY + "=?" +
                        " ORDER BY " + ContractPortafolioProductos.Columnas.MARCA;
                db = this.getReadableDatabase();
                cursor = db.rawQuery(selectQuery, new String[]{categoria,subcategoria});
            }else if (canal.equalsIgnoreCase("AUTOSERVICIO") && subcanal.equalsIgnoreCase("SANTA MARIA")) {
                selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.MARCA + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS +
                        " WHERE " + ContractPortafolioProductos.Columnas.MARCA + " <> 'Ta´Riko' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Coral' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Aki' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Supermaxi' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Mi Comisariato' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Herbal Supermaxi' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Frac' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Muuu' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Milo' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Nestle' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Maria' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'La Universal' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Mayik' AND " +
                        ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                        ContractPortafolioProductos.Columnas.CATEGORY + "=?" +
                        " ORDER BY " + ContractPortafolioProductos.Columnas.MARCA;
                db = this.getReadableDatabase();
                cursor = db.rawQuery(selectQuery, new String[]{categoria,subcategoria});
            }*/else{
                selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.MARCA + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS +
                        " WHERE " + ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                      //  ContractPortafolioProductos.Columnas.CATEGORY + "=? AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Frac' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Muuu' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Milo' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Nestle' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Maria' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'La Universal' AND " +
                        ContractPortafolioProductos.Columnas.LOCALES + " LIKE ? "  +
                        " ORDER BY " + ContractPortafolioProductos.Columnas.MARCA;
                db = this.getReadableDatabase();
                cursor = db.rawQuery(selectQuery, new String[]{categoria,"%" + modulo + "%" /*,subcategoria */});
            }
        }

        BasePortafolioProductos bpv_adicionales = new BasePortafolioProductos();
        BasePortafolioProductos bpv_adicionales2 = new BasePortafolioProductos();
        if (categoria.equalsIgnoreCase("Salsas") && subcategoria.equalsIgnoreCase("Salsa Roja")) {
            bpv_adicionales.setMarca("Gustadina");
            bpv_adicionales2.setMarca("Facundo");
            operadores.add(bpv_adicionales);
            operadores.add(bpv_adicionales2);
        }

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                BasePortafolioProductos bpv = new BasePortafolioProductos();
                bpv.setMarca(cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.MARCA)));
                operadores.add(bpv);
            } while (cursor.moveToNext());
        }

        Log.i("SHARE",operadores.size()+"");

        // closing connection
        cursor.close();
        db.close();

        List<String> nuevo = new ArrayList<String>();
        for (int g=0;g<operadores.size();g++){
            nuevo.add(operadores.get(g).getMarca());
        }
        //quitando las marcas que no aplican
        for (int j=0;j<marcas_blancas.size();j++){
            if(nuevo.contains(marcas_blancas.get(j))){
                nuevo.remove(marcas_blancas.get(j));
            }
        }

        ArrayList<BasePortafolioProductos> nuevo_marcas = new ArrayList<>();
        for (int k=0;k<nuevo.size();k++){
            BasePortafolioProductos bpv_nuevo = new BasePortafolioProductos();
            bpv_nuevo.setMarca(nuevo.get(k));
            nuevo_marcas.add(bpv_nuevo);
        }



        // returning lables
        return nuevo_marcas;
    }

    public ArrayList<BasePortafolioProductos> filtrarListProductosPDI(String categoria, String canal) {
        ArrayList<BasePortafolioProductos> operadores = new ArrayList<BasePortafolioProductos>();

        Log.i("CANAL", canal);
        Cursor cursor = null;
        String selectQuery = "SELECT * FROM " + ContractPDI.PDI +
                " WHERE " + ContractPDI.Columnas.CATEGORIA + "=? AND " +
                ContractPDI.Columnas.CANAL + "=?" +
//                " GROUP BY " + ContractPDI.Columnas.MARCA +
                " ORDER BY " + ContractPDI.Columnas.MARCA;
        db = this.getReadableDatabase();
        cursor = db.rawQuery(selectQuery, new String[]{categoria, canal});


        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                BasePortafolioProductos bpv = new BasePortafolioProductos();
                String marca = cursor.getString(cursor.getColumnIndexOrThrow(ContractPDI.Columnas.MARCA)) + " " + cursor.getString(cursor.getColumnIndexOrThrow(ContractPDI.Columnas.SUBCATEGORIA));
                bpv.setMarca(marca);
                operadores.add(bpv);
            } while (cursor.moveToNext());
        }

        Log.i("SHARE",operadores.size()+"");
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public List<String> filtrarListProductos4Promocion() {
        List<String> operadores = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT DISTINCT " + ContractPortafolioProductos.Columnas.MARCA + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS;
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{});

        operadores.add("Seleccione");
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.MARCA)));
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        Log.i("OPERADORES CUMPLIMIENTO",operadores.size()+"");
        // returning lables
        return operadores;
    }
/*
    public String getManufacturerPromocion(String categoria, String subcategoria, String marca) {
        String operadores = "";

        // Select All Query
        String selectQuery = "SELECT DISTINCT " + ContractPortafolioProductos.Columnas.FABRICANTE + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE "+
                ContractPortafolioProductos.Columnas.SECTOR +"=? AND " +
                ContractPortafolioProductos.Columnas.CATEGORY +"=? AND " +
                ContractPortafolioProductos.Columnas.MARCA +"=?";
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{categoria,subcategoria,marca});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores = (cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.FABRICANTE)));
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }
*/

    public boolean tieneAlmuerzo(String user, String fecha){
        boolean id = false;
        String query = "SELECT * FROM " + ContractInsertAlmuerzo.INSERT_ALMUERZO + " WHERE " +
                ContractInsertAlmuerzo.Columnas.USUARIO +" =? AND " +
                ContractInsertAlmuerzo.Columnas.FECHA +" =? ";
        db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery(query, new String[]{user, fecha});

        if(cursor != null && cursor.moveToFirst()){
            id = true;
        }

        cursor.close();
        db.close();
        return id;
    }

    public List<String> filtrarPdvsHistorial(String tabla_insert, String columna_pdv,String campo) {
        List<String> operadores = new ArrayList<String>();

        operadores.add("PDV");


        String byModulo = "";
        if (campo.equals("PROPENSOS")){
            byModulo = " WHERE modulo = 'PROPENSOS'";
        } else if(campo.equals("PRODUCTOS EN MAL ESTADO")){
            byModulo = " WHERE modulo = 'PRODUCTOS EN MAL ESTADO'";
        } else {
            byModulo = "";
        }

        if (!tabla_insert.isEmpty() && !columna_pdv.isEmpty()){
            // Select All Query
            String selectQuery = "SELECT DISTINCT " + columna_pdv + " FROM " + tabla_insert + byModulo ;
            db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, new String[]{});

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(columna_pdv)));
                } while (cursor.moveToNext());
            }
            // closing connection
            cursor.close();
            db.close();
        }

        // returning lables
        return operadores;
    }


    public List<String> filtrarCategoriasHistorial(String tabla_insert, String columna_pdv, String columna_categoria, String pdv, String campo, String tipoLogistico) {
        List<String> operadores = new ArrayList<String>();



        operadores.add("Categoria");


        String byModulo = "";
        if (campo.equals("PROPENSOS")){
            byModulo = " AND modulo = 'PROPENSOS'";
        } else if(campo.equals("PRODUCTOS EN MAL ESTADO")){
            byModulo = " AND modulo = 'PRODUCTOS EN MAL ESTADO'";
        } else {
            byModulo = "";
        }


        String byTipoLogistico = "";

        if (tabla_insert.equals(ContractInsertLogisticoRelevo.INSERT_LOGISTICO_RELEVO)
                && tipoLogistico != null && !tipoLogistico.isEmpty()) {
            byTipoLogistico = " AND tipo_logistico = '" + tipoLogistico + "'";
        }

        if (!tabla_insert.isEmpty() && !columna_categoria.isEmpty()){
            // Select All Query
            String selectQuery = "SELECT DISTINCT " + columna_categoria + " FROM " + tabla_insert +
                    " WHERE "+ columna_pdv + "= ?" + byModulo + byTipoLogistico;
            db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, new String[]{pdv});

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(columna_categoria)));
                } while (cursor.moveToNext());
            }
            // closing connection
            cursor.close();
            db.close();
        }

        // returning lables
        return operadores;
    }

    public List<String> filtrarMarcaHistorial(String tabla_insert, String columna_pdv, String columna_categoria, String columna_marca,
                                                      String pdv, String categoria, String campo,
                                              String tipoLogistico) {
        List<String> operadores = new ArrayList<String>();
        Log.i("vfdcs",""+tabla_insert+"-"+columna_pdv+"-"+ columna_categoria+"-"+ columna_marca +"-"+pdv +"-"+categoria);
        operadores.add("Marca");

        String byModulo = "";
        if (campo.equals("PROPENSOS")){
            byModulo = " AND modulo = 'PROPENSOS'";
        } else if(campo.equals("PRODUCTOS EN MAL ESTADO")){
            byModulo = " AND modulo = 'PRODUCTOS EN MAL ESTADO'";
        } else {
            byModulo = "";
        }

        String byTipoLogistico = "";
        if (tabla_insert.equals(ContractInsertLogisticoRelevo.INSERT_LOGISTICO_RELEVO)
                && tipoLogistico != null && !tipoLogistico.isEmpty()) {
            byTipoLogistico = " AND tipo_logistico = '" + tipoLogistico + "'";
        }

        if (!tabla_insert.isEmpty() && !columna_marca.isEmpty()){
            // Select All Query
            String selectQuery = "SELECT DISTINCT " + columna_marca + " FROM " + tabla_insert +
                    " WHERE "+ columna_pdv + "= ? AND " +
                    columna_categoria + "= ?" + byModulo + byTipoLogistico;
            db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, new String[]{pdv, categoria});

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(columna_marca)));
                } while (cursor.moveToNext());
            }
            // closing connection
            cursor.close();
            db.close();
        }

        // returning lables
        return operadores;
    }






    public String getManufacturerPromocion(String subcategoria, String marca) {
        String operadores = "";

        // Select All Query
        String selectQuery = "SELECT DISTINCT " + ContractPortafolioProductos.Columnas.FABRICANTE + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE "+
              //  ContractPortafolioProductos.Columnas.CATEGORY +"=? AND " +
                ContractPortafolioProductos.Columnas.MARCA +"=?";
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{/*subcategoria,*/ marca});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores = (cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.FABRICANTE)));
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }



    public boolean faltaSalida(String codigo, String user, String fecha){
        boolean id = true;
        String query = "SELECT * FROM " + ContractInsertGps.INSERT_GPS + " WHERE " +
                ContractInsertGps.Columnas.IDPDV +" =? AND " +
                ContractInsertGps.Columnas.USUARIO +" =? AND " +
                ContractInsertGps.Columnas.FECHA +" =? AND " +
                ContractInsertGps.Columnas.TIPO +" ='SALIDA'";
        db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery(query, new String[]{codigo, user, fecha});

        if(cursor != null && cursor.moveToFirst() && !codigo.equalsIgnoreCase(Constantes.NODATA)){
            id = false;
        }

        cursor.close();
        db.close();
        return id;
    }

    public boolean faltaSalidaRuta(String codigo, String user, String fecha, String idRuta) {
        boolean id = true;
        String query = "SELECT * FROM " + ContractInsertGps.INSERT_GPS + " WHERE " +
                ContractInsertGps.Columnas.IDPDV + " =? AND " +
                ContractInsertGps.Columnas.USUARIO + " =? AND " +
                ContractInsertGps.Columnas.FECHA + " =? AND " +
                ContractInsertGps.Columnas.TIPO + " ='SALIDA' AND " +
                Constantes.ID_REMOTA_RUTA + " =?";
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, new String[]{codigo, user, fecha, idRuta != null ? idRuta : ""});

        if (cursor != null && cursor.moveToFirst() && !codigo.equalsIgnoreCase(Constantes.NODATA)) {
            id = false;
        }

        if (cursor != null) cursor.close();
        db.close();
        return id;
    }

    public boolean tieneEntrada(String codigo, String user, String fecha){
        boolean id = false;
        String query = "SELECT * FROM " + ContractInsertGps.INSERT_GPS + " WHERE " +
                ContractInsertGps.Columnas.IDPDV +" =? AND " +
                ContractInsertGps.Columnas.USUARIO +" =? AND " +
                ContractInsertGps.Columnas.FECHA +" =? AND " +
                ContractInsertGps.Columnas.TIPO +" IN ('ENTRADA','ENTRADA TARDIA') ";
        db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery(query, new String[]{codigo, user, fecha});

        if(cursor != null && cursor.moveToFirst()){
            id = true;
        }

        cursor.close();
        db.close();
        return id;
    }

    public boolean tieneEntradaRuta(String codigo, String user, String fecha, String idRuta) {
        boolean id = false;
        String query = "SELECT * FROM " + ContractInsertGps.INSERT_GPS + " WHERE " +
                ContractInsertGps.Columnas.IDPDV + " =? AND " +
                ContractInsertGps.Columnas.USUARIO + " =? AND " +
                ContractInsertGps.Columnas.FECHA + " =? AND " +
                ContractInsertGps.Columnas.TIPO + " IN ('ENTRADA','ENTRADA TARDIA') AND " +
                Constantes.ID_REMOTA_RUTA + " =?";
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, new String[]{codigo, user, fecha, idRuta != null ? idRuta : ""});

        if (cursor != null && cursor.moveToFirst()) {
            id = true;
        }

        if (cursor != null) cursor.close();
        db.close();
        return id;
    }

    public boolean visitaEnCurso(String codigo, String user, String fecha) {
        int entradas = 0, salidas = 0;
        db = this.getReadableDatabase();

        Cursor c1 = db.rawQuery(
            "SELECT COUNT(*) FROM " + ContractInsertGps.INSERT_GPS +
            " WHERE " + ContractInsertGps.Columnas.IDPDV + "=? AND " +
            ContractInsertGps.Columnas.USUARIO + "=? AND " +
            ContractInsertGps.Columnas.FECHA + "=? AND " +
            ContractInsertGps.Columnas.TIPO + " IN ('ENTRADA','ENTRADA TARDIA')",
            new String[]{codigo, user, fecha});
        if (c1 != null && c1.moveToFirst()) entradas = c1.getInt(0);
        if (c1 != null) c1.close();

        Cursor c2 = db.rawQuery(
            "SELECT COUNT(*) FROM " + ContractInsertGps.INSERT_GPS +
            " WHERE " + ContractInsertGps.Columnas.IDPDV + "=? AND " +
            ContractInsertGps.Columnas.USUARIO + "=? AND " +
            ContractInsertGps.Columnas.FECHA + "=? AND " +
            ContractInsertGps.Columnas.TIPO + " ='SALIDA'",
            new String[]{codigo, user, fecha});
        if (c2 != null && c2.moveToFirst()) salidas = c2.getInt(0);
        if (c2 != null) c2.close();

        db.close();
        return entradas > salidas;
    }


    public boolean tieneJustificacionEntrada(String codigo, String user, String fecha){
        boolean id = false;
        String query = "SELECT * FROM " + ContractInsertGps.INSERT_GPS + " WHERE " +
                ContractInsertGps.Columnas.IDPDV +" =? AND " +
                ContractInsertGps.Columnas.USUARIO +" =? AND " +
                ContractInsertGps.Columnas.FECHA +" =? AND " +
                ContractInsertGps.Columnas.TIPO +" =? AND " +
                ContractInsertGps.Columnas.CAUSAL +" LIKE ? ";
        db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery(query, new String[]{codigo, user, fecha,"JUSTIFICACION","%"+"VERIFICAR ENTRADA"+"%"});

        if(cursor != null && cursor.moveToFirst()){
            id = true;
        }

        cursor.close();
        db.close();
        return id;
    }

    public boolean faltaJustificacionSalida(String codigo, String user, String fecha){
        boolean id = true;
        String query = "SELECT * FROM " + ContractInsertGps.INSERT_GPS + " WHERE " +
                ContractInsertGps.Columnas.IDPDV +" =? AND " +
                ContractInsertGps.Columnas.USUARIO +" =? AND " +
                ContractInsertGps.Columnas.FECHA +" =? AND " +
                ContractInsertGps.Columnas.TIPO +" =? AND " +
                ContractInsertGps.Columnas.CAUSAL +" LIKE ? ";
        db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery(query, new String[]{codigo, user, fecha,"JUSTIFICACION","%"+"VERIFICAR SALIDA"+"%"});

        if(cursor != null && cursor.moveToFirst()){
            id = false;
        }

        cursor.close();
        db.close();
        return id;
    }


    public String getSegmentacionMenu() {
        String operadores = "";

        // Select All Query
        String selectQuery = "SELECT DISTINCT " + ContractPharmaValue.Columnas.SEGMENTACION + " FROM " + ContractPharmaValue.POS;
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores = (cursor.getString(cursor.getColumnIndexOrThrow(ContractPharmaValue.Columnas.SEGMENTACION)));
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public String getComprasMenu() {
        String operadores = "";

        // Select All Query
        String selectQuery = "SELECT DISTINCT " + ContractPharmaValue.Columnas.COMPRAS + " FROM " + ContractPharmaValue.POS;
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores = (cursor.getString(cursor.getColumnIndexOrThrow(ContractPharmaValue.Columnas.COMPRAS)));
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public String getSKUPromocion(String sector, String categoria, String subcategoria, String presentacion, String marca) {
        String operadores = "";

        // Select All Query
        String selectQuery = "SELECT DISTINCT " + ContractPortafolioProductos.Columnas.SKU + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE "+
                ContractPortafolioProductos.Columnas.SECTOR +"=? AND " +
                ContractPortafolioProductos.Columnas.CATEGORY +"=? AND " +
                ContractPortafolioProductos.Columnas.SUBCATEGORIA +"=? AND " +
                ContractPortafolioProductos.Columnas.PRESENTACION +"=? AND " +
                ContractPortafolioProductos.Columnas.MARCA +"=?";
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{sector,categoria,subcategoria,presentacion,marca});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores = (cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.SKU)));
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }
/*
    public ArrayList<String> filtrarMarcaExhibicion(String categoria ,String subcategoria,String tipo, String fabricante, String canal, String subcanal) {
        ArrayList<String> operadores = new ArrayList<>();

        Cursor cursor = null;
        String selectQuery = "";


        String byFabricante = ContractPortafolioProductos.Columnas.FABRICANTE + " NOT LIKE ? ";

        if (tipo.equalsIgnoreCase("MARCA_PROPIA")) {
            byFabricante = ContractPortafolioProductos.Columnas.FABRICANTE + " LIKE ? ";
        }

        if (subcategoria.equalsIgnoreCase("")) {
            if (canal.equalsIgnoreCase("MAYORISTA")) {
                selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.MARCA + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS +
                        " WHERE " + ContractPortafolioProductos.Columnas.MARCA + " <> 'Mi Comisariato' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Coral' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Supermaxi' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Aki' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Ta´Riko' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Santa Maria' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Herbal Supermaxi' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Mayik' AND " +
                        byFabricante + " AND " +
                        ContractPortafolioProductos.Columnas.SECTOR + "=?" +
                        " ORDER BY " + ContractPortafolioProductos.Columnas.MARCA;
                db = this.getReadableDatabase();
                cursor = db.rawQuery(selectQuery, new String[]{"%" + fabricante + "%", categoria});
            }else if (canal.equalsIgnoreCase("AUTOSERVICIO") && subcanal.equalsIgnoreCase("ALMACENES TIA")) {
                selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.MARCA + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS +
                        " WHERE " + ContractPortafolioProductos.Columnas.MARCA + " <> 'Mi Comisariato' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Coral' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Supermaxi' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Aki' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Santa Maria' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Herbal Supermaxi' AND "
                        + byFabricante + " AND " +
                        ContractPortafolioProductos.Columnas.SECTOR + "=?" +
                        " ORDER BY " + ContractPortafolioProductos.Columnas.MARCA;
                db = this.getReadableDatabase();
                cursor = db.rawQuery(selectQuery, new String[]{"%" + fabricante + "%", categoria});
            }else if (canal.equalsIgnoreCase("AUTOSERVICIO") && subcanal.equalsIgnoreCase("AKI")) {
                selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.MARCA + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS +
                        " WHERE " + ContractPortafolioProductos.Columnas.MARCA + " <> 'Mayik' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Ta´Riko' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Herbal Supermaxi' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Supermaxi' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Santa Maria' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Coral' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Mi Comisariato' AND "
                        + byFabricante + " AND " +
                        ContractPortafolioProductos.Columnas.SECTOR + "=?" +
                        " ORDER BY " + ContractPortafolioProductos.Columnas.MARCA;
                db = this.getReadableDatabase();
                cursor = db.rawQuery(selectQuery, new String[]{"%" + fabricante + "%", categoria});
            }else if (canal.equalsIgnoreCase("AUTOSERVICIO") && subcanal.equalsIgnoreCase("CORAL HIPERMERCADOS")) {
                selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.MARCA + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS +
                        " WHERE " + ContractPortafolioProductos.Columnas.MARCA + " <> 'Mayik' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Ta´Riko' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Herbal Supermaxi' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Supermaxi' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Santa Maria' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Aki' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Mi Comisariato' AND " +
                        byFabricante + " AND " +
                        ContractPortafolioProductos.Columnas.SECTOR + "=?" +
                        " ORDER BY " + ContractPortafolioProductos.Columnas.MARCA;
                db = this.getReadableDatabase();
                cursor = db.rawQuery(selectQuery, new String[]{"%" + fabricante + "%", categoria});
            }else if (canal.equalsIgnoreCase("AUTOSERVICIO") && subcanal.equalsIgnoreCase("SUPERMAXI")) {
                selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.MARCA + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS +
                        " WHERE " + ContractPortafolioProductos.Columnas.MARCA + " <> 'Ta´Riko' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Coral' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Aki' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Mi Comisariato' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Santa Maria' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Mayik' AND " +
                        byFabricante + " AND " +
                        ContractPortafolioProductos.Columnas.SECTOR + "=?" +
                        " ORDER BY " + ContractPortafolioProductos.Columnas.MARCA;
                db = this.getReadableDatabase();
                cursor = db.rawQuery(selectQuery, new String[]{"%" + fabricante + "%", categoria});
            }else if (canal.equalsIgnoreCase("AUTOSERVICIO") && subcanal.equalsIgnoreCase("MI COMISARIATO")) {
                selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.MARCA + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS +
                        " WHERE " + ContractPortafolioProductos.Columnas.MARCA + " <> 'Ta´Riko' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Coral' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Aki' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Supermaxi' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Santa Maria' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Mayik' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Herbal Supermaxi' AND " +
                        byFabricante + " AND " +
                        ContractPortafolioProductos.Columnas.SECTOR + "=?" +
                        " ORDER BY " + ContractPortafolioProductos.Columnas.MARCA;
                db = this.getReadableDatabase();
                cursor = db.rawQuery(selectQuery, new String[]{"%" + fabricante + "%", categoria});
            }else if (canal.equalsIgnoreCase("AUTOSERVICIO") && subcanal.equalsIgnoreCase("SANTA MARIA")) {
                selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.MARCA + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS +
                        " WHERE " + ContractPortafolioProductos.Columnas.MARCA + " <> 'Ta´Riko' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Coral' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Aki' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Supermaxi' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Mi Comisariato' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Herbal Supermaxi' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Mayik' AND " +
                        byFabricante + " AND " +
                        ContractPortafolioProductos.Columnas.SECTOR + "=?" +
                        " ORDER BY " + ContractPortafolioProductos.Columnas.MARCA;
                db = this.getReadableDatabase();
                cursor = db.rawQuery(selectQuery, new String[]{"%" + fabricante + "%", categoria});
            }else{
                selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.MARCA + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS +
                        " WHERE " + byFabricante + " AND " +
                        ContractPortafolioProductos.Columnas.SECTOR + "=?" +
                        " ORDER BY " + ContractPortafolioProductos.Columnas.MARCA;
                db = this.getReadableDatabase();
                cursor = db.rawQuery(selectQuery, new String[]{"%" + fabricante + "%", categoria});
            }
        }else{
            if (canal.equalsIgnoreCase("MAYORISTA")) {
                selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.MARCA + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS +
                        " WHERE " + ContractPortafolioProductos.Columnas.MARCA + " <> 'Mi Comisariato' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Coral' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Supermaxi' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Aki' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Ta´Riko' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Santa Maria' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Herbal Supermaxi' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Mayik' AND " +
                        byFabricante + " AND " +
                        ContractPortafolioProductos.Columnas.SECTOR + "=? " +
                        " ORDER BY " + ContractPortafolioProductos.Columnas.MARCA;
                db = this.getReadableDatabase();
                cursor = db.rawQuery(selectQuery, new String[]{"%" + fabricante + "%", categoria});
            }else if (canal.equalsIgnoreCase("AUTOSERVICIO") && subcanal.equalsIgnoreCase("ALMACENES TIA")) {
                selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.MARCA + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS +
                        " WHERE " + ContractPortafolioProductos.Columnas.MARCA + " <> 'Mi Comisariato' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Coral' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Supermaxi' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Aki' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Santa Maria' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Herbal Supermaxi' AND " +
                        byFabricante + " AND " +
                        ContractPortafolioProductos.Columnas.SECTOR + "=? " +
                        " ORDER BY " + ContractPortafolioProductos.Columnas.MARCA;
                db = this.getReadableDatabase();
                cursor = db.rawQuery(selectQuery, new String[]{"%" + fabricante + "%", categoria});
            }else if (canal.equalsIgnoreCase("AUTOSERVICIO") && subcanal.equalsIgnoreCase("AKI")) {
                selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.MARCA + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS +
                        " WHERE " + ContractPortafolioProductos.Columnas.MARCA + " <> 'Mayik' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Ta´Riko' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Herbal Supermaxi' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Supermaxi' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Santa Maria' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Coral' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Mi Comisariato' AND " +
                        byFabricante + " AND " +
                        ContractPortafolioProductos.Columnas.SECTOR + "=? " +
                        " ORDER BY " + ContractPortafolioProductos.Columnas.MARCA;
                db = this.getReadableDatabase();
                cursor = db.rawQuery(selectQuery, new String[]{"%" + fabricante + "%", categoria});
            }else if (canal.equalsIgnoreCase("AUTOSERVICIO") && subcanal.equalsIgnoreCase("CORAL HIPERMERCADOS")) {
                selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.MARCA + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS +
                        " WHERE " + ContractPortafolioProductos.Columnas.MARCA + " <> 'Mayik' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Ta´Riko' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Herbal Supermaxi' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Supermaxi' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Santa Maria' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Aki' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Mi Comisariato' AND " +
                        byFabricante + " AND " +
                        ContractPortafolioProductos.Columnas.SECTOR + "=? " +
                        " ORDER BY " + ContractPortafolioProductos.Columnas.MARCA;
                db = this.getReadableDatabase();
                cursor = db.rawQuery(selectQuery, new String[]{"%" + fabricante + "%", categoria});
            }else if (canal.equalsIgnoreCase("AUTOSERVICIO") && subcanal.equalsIgnoreCase("SUPERMAXI")) {
                selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.MARCA + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS +
                        " WHERE " + ContractPortafolioProductos.Columnas.MARCA + " <> 'Ta´Riko' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Coral' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Aki' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Mi Comisariato' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Santa Maria' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Mayik' AND " +
                        byFabricante + " AND " +
                        ContractPortafolioProductos.Columnas.SECTOR + "=? " +
                        " ORDER BY " + ContractPortafolioProductos.Columnas.MARCA;
                db = this.getReadableDatabase();
                cursor = db.rawQuery(selectQuery, new String[]{"%" + fabricante + "%", categoria});
            }else if (canal.equalsIgnoreCase("AUTOSERVICIO") && subcanal.equalsIgnoreCase("MI COMISARIATO")) {
                selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.MARCA + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS +
                        " WHERE " + ContractPortafolioProductos.Columnas.MARCA + " <> 'Ta´Riko' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Coral' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Aki' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Supermaxi' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Santa Maria' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Mayik' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Herbal Supermaxi' AND " +
                        byFabricante + " AND " +
                        ContractPortafolioProductos.Columnas.SECTOR + "=? " +
                        " ORDER BY " + ContractPortafolioProductos.Columnas.MARCA;
                db = this.getReadableDatabase();
                cursor = db.rawQuery(selectQuery, new String[]{"%" + fabricante + "%", categoria});
            }else if (canal.equalsIgnoreCase("AUTOSERVICIO") && subcanal.equalsIgnoreCase("SANTA MARIA")) {
                selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.MARCA + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS +
                        " WHERE " + ContractPortafolioProductos.Columnas.MARCA + " <> 'Ta´Riko' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Coral' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Aki' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Supermaxi' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Mi Comisariato' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Herbal Supermaxi' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Mayik' AND " +
                        byFabricante + " AND " +
                        ContractPortafolioProductos.Columnas.SECTOR + "=? " +
                        " ORDER BY " + ContractPortafolioProductos.Columnas.MARCA;
                db = this.getReadableDatabase();
                cursor = db.rawQuery(selectQuery, new String[]{"%" + fabricante + "%", categoria});
            }else{
                selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.MARCA + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS +
                        " WHERE " + byFabricante + " AND " +
                        ContractPortafolioProductos.Columnas.SECTOR + "=? " +
                        " ORDER BY " + ContractPortafolioProductos.Columnas.MARCA;
                db = this.getReadableDatabase();
                cursor = db.rawQuery(selectQuery, new String[]{"%" + fabricante + "%", categoria});
            }
        }

        String header = "HEADER FOR RECYCLERVIEW";
        operadores.add("Seleccione");
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.MARCA)));
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }
*/

    public ArrayList<String> filtrarMarcaExhibicion(String categoria ,String subcategoria,String tipo, String fabricante, String canal, String subcanal,String modulo/*, List<String> marcas_blancas */) {
        ArrayList<String> operadores = new ArrayList<>();

        Log.i("MODULO EXH",""+modulo);

        Cursor cursor = null;
        String selectQuery = "";

        if (subcanal.equalsIgnoreCase("NO CADENA")){
            subcanal = "N/A";
        }


        String byFabricante = ContractPortafolioProductos.Columnas.FABRICANTE + " NOT LIKE ? ";

        if (tipo.equalsIgnoreCase("MARCA_PROPIA")) {
            byFabricante = ContractPortafolioProductos.Columnas.FABRICANTE + " LIKE ? ";
        }


        if (canal.equalsIgnoreCase("MAYORISTA")) {
            selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.MARCA + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS +
                    " WHERE " + ContractPortafolioProductos.Columnas.MARCA + " <> 'Mi Comisariato' AND " +
                    ContractPortafolioProductos.Columnas.MARCA + " <> 'Coral' AND " +
                    ContractPortafolioProductos.Columnas.MARCA + " <> 'Supermaxi' AND " +
                    ContractPortafolioProductos.Columnas.MARCA + " <> 'Aki' AND " +
                    ContractPortafolioProductos.Columnas.MARCA + " <> 'Ta´Riko' AND " +
                    ContractPortafolioProductos.Columnas.MARCA + " <> 'Santa Maria' AND " +
                    ContractPortafolioProductos.Columnas.MARCA + " <> 'Herbal Supermaxi' AND " +
                    ContractPortafolioProductos.Columnas.MARCA + " <> 'Mayik' AND " +
                    byFabricante + " AND " +
                    ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                    ContractPortafolioProductos.Columnas.LOCALES + " LIKE ? " +
                    " ORDER BY " + ContractPortafolioProductos.Columnas.MARCA;
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{"%" + fabricante + "%", categoria,"%" + modulo + "%"});
        }else if (canal.equalsIgnoreCase("AUTOSERVICIO")) {
            //Log.i("fabricante",""+fabricante);
            //Log.i("fabricante",""+tipo);
            //Log.i("fabricante",""+subcanal);
            selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.MARCA + " FROM " +
                    ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " +
                    ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                    byFabricante + " AND " +
                    ContractPortafolioProductos.Columnas.CADENAS + " LIKE ? AND " +
                    ContractPortafolioProductos.Columnas.LOCALES + " LIKE ? " +
                    " ORDER BY " + ContractPortafolioProductos.Columnas.MARCA;
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{categoria,"%" + fabricante + "%","%" + subcanal.trim() + "%","%" + modulo + "%"});
        }
        /*else if (canal.equalsIgnoreCase("AUTOSERVICIO") && subcanal.equalsIgnoreCase("ALMACENES TIA")) {
            selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.MARCA + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS +
                    " WHERE " + ContractPortafolioProductos.Columnas.MARCA + " <> 'Mi Comisariato' AND " +
                    ContractPortafolioProductos.Columnas.MARCA + " <> 'Coral' AND " +
                    ContractPortafolioProductos.Columnas.MARCA + " <> 'Supermaxi' AND " +
                    ContractPortafolioProductos.Columnas.MARCA + " <> 'Aki' AND " +
                    ContractPortafolioProductos.Columnas.MARCA + " <> 'Santa Maria' AND " +
                    ContractPortafolioProductos.Columnas.MARCA + " <> 'Herbal Supermaxi' AND " +
                    byFabricante + " AND " +
                    ContractPortafolioProductos.Columnas.SECTOR + "=? " +
                    " ORDER BY " + ContractPortafolioProductos.Columnas.MARCA;
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{"%" + fabricante + "%", categoria});
        }else if (canal.equalsIgnoreCase("AUTOSERVICIO") && subcanal.equalsIgnoreCase("AKI")) {
            selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.MARCA + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS +
                    " WHERE " + ContractPortafolioProductos.Columnas.MARCA + " <> 'Mayik' AND " +
                    ContractPortafolioProductos.Columnas.MARCA + " <> 'Ta´Riko' AND " +
                    ContractPortafolioProductos.Columnas.MARCA + " <> 'Herbal Supermaxi' AND " +
                    ContractPortafolioProductos.Columnas.MARCA + " <> 'Supermaxi' AND " +
                    ContractPortafolioProductos.Columnas.MARCA + " <> 'Santa Maria' AND " +
                    ContractPortafolioProductos.Columnas.MARCA + " <> 'Coral' AND " +
                    ContractPortafolioProductos.Columnas.MARCA + " <> 'Mi Comisariato' AND " +
                    byFabricante + " AND " +
                    ContractPortafolioProductos.Columnas.SECTOR + "=? " +
                    " ORDER BY " + ContractPortafolioProductos.Columnas.MARCA;
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{"%" + fabricante + "%", categoria});
        }else if (canal.equalsIgnoreCase("AUTOSERVICIO") && subcanal.equalsIgnoreCase("CORAL HIPERMERCADOS")) {
            selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.MARCA + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS +
                    " WHERE " + ContractPortafolioProductos.Columnas.MARCA + " <> 'Mayik' AND " +
                    ContractPortafolioProductos.Columnas.MARCA + " <> 'Ta´Riko' AND " +
                    ContractPortafolioProductos.Columnas.MARCA + " <> 'Herbal Supermaxi' AND " +
                    ContractPortafolioProductos.Columnas.MARCA + " <> 'Supermaxi' AND " +
                    ContractPortafolioProductos.Columnas.MARCA + " <> 'Santa Maria' AND " +
                    ContractPortafolioProductos.Columnas.MARCA + " <> 'Aki' AND " +
                    ContractPortafolioProductos.Columnas.MARCA + " <> 'Mi Comisariato' AND " +
                    byFabricante + " AND " +
                    ContractPortafolioProductos.Columnas.SECTOR + "=? " +
                    " ORDER BY " + ContractPortafolioProductos.Columnas.MARCA;
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{"%" + fabricante + "%", categoria});
        }else if (canal.equalsIgnoreCase("AUTOSERVICIO") && subcanal.equalsIgnoreCase("SUPERMAXI")) {
            selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.MARCA + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS +
                    " WHERE " + ContractPortafolioProductos.Columnas.MARCA + " <> 'Ta´Riko' AND " +
                    ContractPortafolioProductos.Columnas.MARCA + " <> 'Coral' AND " +
                    ContractPortafolioProductos.Columnas.MARCA + " <> 'Aki' AND " +
                    ContractPortafolioProductos.Columnas.MARCA + " <> 'Mi Comisariato' AND " +
                    ContractPortafolioProductos.Columnas.MARCA + " <> 'Santa Maria' AND " +
                    ContractPortafolioProductos.Columnas.MARCA + " <> 'Mayik' AND " +
                    byFabricante + " AND " +
                    ContractPortafolioProductos.Columnas.SECTOR + "=? " +
                    " ORDER BY " + ContractPortafolioProductos.Columnas.MARCA;
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{"%" + fabricante + "%", categoria});
        }else if (canal.equalsIgnoreCase("AUTOSERVICIO") && subcanal.equalsIgnoreCase("MI COMISARIATO")) {
            selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.MARCA + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS +
                    " WHERE " + ContractPortafolioProductos.Columnas.MARCA + " <> 'Ta´Riko' AND " +
                    ContractPortafolioProductos.Columnas.MARCA + " <> 'Coral' AND " +
                    ContractPortafolioProductos.Columnas.MARCA + " <> 'Aki' AND " +
                    ContractPortafolioProductos.Columnas.MARCA + " <> 'Supermaxi' AND " +
                    ContractPortafolioProductos.Columnas.MARCA + " <> 'Santa Maria' AND " +
                    ContractPortafolioProductos.Columnas.MARCA + " <> 'Mayik' AND " +
                    ContractPortafolioProductos.Columnas.MARCA + " <> 'Herbal Supermaxi' AND " +
                    byFabricante + " AND " +
                    ContractPortafolioProductos.Columnas.SECTOR + "=? " +
                    " ORDER BY " + ContractPortafolioProductos.Columnas.MARCA;
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{"%" + fabricante + "%", categoria});
        }else if (canal.equalsIgnoreCase("AUTOSERVICIO") && subcanal.equalsIgnoreCase("SANTA MARIA")) {
            selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.MARCA + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS +
                    " WHERE " + ContractPortafolioProductos.Columnas.MARCA + " <> 'Ta´Riko' AND " +
                    ContractPortafolioProductos.Columnas.MARCA + " <> 'Coral' AND " +
                    ContractPortafolioProductos.Columnas.MARCA + " <> 'Aki' AND " +
                    ContractPortafolioProductos.Columnas.MARCA + " <> 'Supermaxi' AND " +
                    ContractPortafolioProductos.Columnas.MARCA + " <> 'Mi Comisariato' AND " +
                    ContractPortafolioProductos.Columnas.MARCA + " <> 'Herbal Supermaxi' AND " +
                    ContractPortafolioProductos.Columnas.MARCA + " <> 'Mayik' AND " +
                    byFabricante + " AND " +
                    ContractPortafolioProductos.Columnas.SECTOR + "=? " +
                    " ORDER BY " + ContractPortafolioProductos.Columnas.MARCA;
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{"%" + fabricante + "%", categoria});
        }*/else{
            selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.MARCA + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS +
                    " WHERE " + byFabricante + " AND " +
                    ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                    ContractPortafolioProductos.Columnas.LOCALES + " LIKE ? " +
                    " ORDER BY " + ContractPortafolioProductos.Columnas.MARCA;
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{"%" + fabricante + "%", categoria, "%" + modulo + "%"});
        }


        String header = "HEADER FOR RECYCLERVIEW";
        operadores.add("Seleccione");
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.MARCA)));
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();

        //quitando las marcas que no aplican
        /*
        for (int j=0;j<marcas_blancas.size();j++){
            if(operadores.contains(marcas_blancas.get(j))){
                operadores.remove(marcas_blancas.get(j));
            }
        }*/

        // returning lables
        return operadores;
    }

    public ArrayList<BasePortafolioProductos> filtrarListProductos3Exhibicion(String categoria, String subcategoria, String tipo, String fabricante, String canal, String subcanal) {
        ArrayList<BasePortafolioProductos> operadores = new ArrayList<>();

        Cursor cursor = null;
        String selectQuery = "";

        String byFabricante = ContractPortafolioProductos.Columnas.FABRICANTE + " NOT LIKE ? ";

        if (tipo.equalsIgnoreCase("MARCA_PROPIA")) {
            byFabricante = ContractPortafolioProductos.Columnas.FABRICANTE + " LIKE ? ";
        }

        if (subcategoria.equalsIgnoreCase("TODOS")) {
            if (canal.equalsIgnoreCase("MAYORISTA")) {
                selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.MARCA + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS +
                        " WHERE " + ContractPortafolioProductos.Columnas.MARCA + " <> 'Mi Comisariato' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Coral' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Supermaxi' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Aki' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Ta´Riko' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Santa Maria' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Herbal Supermaxi' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Mayik' AND " +
                        byFabricante + " AND " +
                        ContractPortafolioProductos.Columnas.SECTOR + "=?" +
                        " ORDER BY " + ContractPortafolioProductos.Columnas.MARCA;
                db = this.getReadableDatabase();
                cursor = db.rawQuery(selectQuery, new String[]{"%" + fabricante + "%", categoria});
            }else if (canal.equalsIgnoreCase("AUTOSERVICIO") && subcanal.equalsIgnoreCase("ALMACENES TIA")) {
                selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.MARCA + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS +
                        " WHERE " + ContractPortafolioProductos.Columnas.MARCA + " <> 'Mi Comisariato' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Coral' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Supermaxi' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Aki' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Santa Maria' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Herbal Supermaxi' AND "
                        + byFabricante + " AND " +
                        ContractPortafolioProductos.Columnas.SECTOR + "=?" +
                        " ORDER BY " + ContractPortafolioProductos.Columnas.MARCA;
                db = this.getReadableDatabase();
                cursor = db.rawQuery(selectQuery, new String[]{"%" + fabricante + "%", categoria});
            }else if (canal.equalsIgnoreCase("AUTOSERVICIO") && subcanal.equalsIgnoreCase("AKI")) {
                selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.MARCA + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS +
                        " WHERE " + ContractPortafolioProductos.Columnas.MARCA + " <> 'Mayik' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Ta´Riko' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Herbal Supermaxi' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Supermaxi' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Santa Maria' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Coral' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Mi Comisariato' AND "
                        + byFabricante + " AND " +
                        ContractPortafolioProductos.Columnas.SECTOR + "=?" +
                        " ORDER BY " + ContractPortafolioProductos.Columnas.MARCA;
                db = this.getReadableDatabase();
                cursor = db.rawQuery(selectQuery, new String[]{"%" + fabricante + "%", categoria});
            }else if (canal.equalsIgnoreCase("AUTOSERVICIO") && subcanal.equalsIgnoreCase("CORAL HIPERMERCADOS")) {
                selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.MARCA + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS +
                        " WHERE " + ContractPortafolioProductos.Columnas.MARCA + " <> 'Mayik' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Ta´Riko' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Herbal Supermaxi' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Supermaxi' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Santa Maria' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Aki' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Mi Comisariato' AND " +
                        byFabricante + " AND " +
                        ContractPortafolioProductos.Columnas.SECTOR + "=?" +
                        " ORDER BY " + ContractPortafolioProductos.Columnas.MARCA;
                db = this.getReadableDatabase();
                cursor = db.rawQuery(selectQuery, new String[]{"%" + fabricante + "%", categoria});
            }else if (canal.equalsIgnoreCase("AUTOSERVICIO") && subcanal.equalsIgnoreCase("SUPERMAXI")) {
                selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.MARCA + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS +
                        " WHERE " + ContractPortafolioProductos.Columnas.MARCA + " <> 'Ta´Riko' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Coral' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Aki' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Mi Comisariato' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Santa Maria' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Mayik' AND " +
                        byFabricante + " AND " +
                        ContractPortafolioProductos.Columnas.SECTOR + "=?" +
                        " ORDER BY " + ContractPortafolioProductos.Columnas.MARCA;
                db = this.getReadableDatabase();
                cursor = db.rawQuery(selectQuery, new String[]{"%" + fabricante + "%", categoria});
            }else if (canal.equalsIgnoreCase("AUTOSERVICIO") && subcanal.equalsIgnoreCase("MI COMISARIATO")) {
                selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.MARCA + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS +
                        " WHERE " + ContractPortafolioProductos.Columnas.MARCA + " <> 'Ta´Riko' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Coral' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Aki' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Supermaxi' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Santa Maria' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Mayik' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Herbal Supermaxi' AND " +
                        byFabricante + " AND " +
                        ContractPortafolioProductos.Columnas.SECTOR + "=?" +
                        " ORDER BY " + ContractPortafolioProductos.Columnas.MARCA;
                db = this.getReadableDatabase();
                cursor = db.rawQuery(selectQuery, new String[]{"%" + fabricante + "%", categoria});
            }else if (canal.equalsIgnoreCase("AUTOSERVICIO") && subcanal.equalsIgnoreCase("SANTA MARIA")) {
                selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.MARCA + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS +
                        " WHERE " + ContractPortafolioProductos.Columnas.MARCA + " <> 'Ta´Riko' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Coral' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Aki' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Supermaxi' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Mi Comisariato' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Herbal Supermaxi' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Mayik' AND " +
                        byFabricante + " AND " +
                        ContractPortafolioProductos.Columnas.SECTOR + "=?" +
                        " ORDER BY " + ContractPortafolioProductos.Columnas.MARCA;
                db = this.getReadableDatabase();
                cursor = db.rawQuery(selectQuery, new String[]{"%" + fabricante + "%", categoria});
            }else{
                selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.MARCA + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS +
                        " WHERE " + byFabricante + " AND " +
                        ContractPortafolioProductos.Columnas.SECTOR + "=?" +
                        " ORDER BY " + ContractPortafolioProductos.Columnas.MARCA;
                db = this.getReadableDatabase();
                cursor = db.rawQuery(selectQuery, new String[]{"%" + fabricante + "%", categoria});
            }
        }else{
            if (canal.equalsIgnoreCase("MAYORISTA")) {
                selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.MARCA + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS +
                        " WHERE " + ContractPortafolioProductos.Columnas.MARCA + " <> 'Mi Comisariato' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Coral' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Supermaxi' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Aki' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Ta´Riko' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Santa Maria' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Herbal Supermaxi' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Mayik' AND " +
                        byFabricante + " AND " +
                        ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                        ContractPortafolioProductos.Columnas.CATEGORY + "=?" +
                        " ORDER BY " + ContractPortafolioProductos.Columnas.MARCA;
                db = this.getReadableDatabase();
                cursor = db.rawQuery(selectQuery, new String[]{"%" + fabricante + "%", categoria, subcategoria});
            }else if (canal.equalsIgnoreCase("AUTOSERVICIO") && subcanal.equalsIgnoreCase("ALMACENES TIA")) {
                selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.MARCA + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS +
                        " WHERE " + ContractPortafolioProductos.Columnas.MARCA + " <> 'Mi Comisariato' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Coral' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Supermaxi' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Aki' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Santa Maria' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Herbal Supermaxi' AND " +
                        byFabricante + " AND " +
                        ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                        ContractPortafolioProductos.Columnas.CATEGORY + "=?" +
                        " ORDER BY " + ContractPortafolioProductos.Columnas.MARCA;
                db = this.getReadableDatabase();
                cursor = db.rawQuery(selectQuery, new String[]{"%" + fabricante + "%", categoria, subcategoria});
            }else if (canal.equalsIgnoreCase("AUTOSERVICIO") && subcanal.equalsIgnoreCase("AKI")) {
                selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.MARCA + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS +
                        " WHERE " + ContractPortafolioProductos.Columnas.MARCA + " <> 'Mayik' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Ta´Riko' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Herbal Supermaxi' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Supermaxi' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Santa Maria' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Coral' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Mi Comisariato' AND " +
                        byFabricante + " AND " +
                        ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                        ContractPortafolioProductos.Columnas.CATEGORY + "=?" +
                        " ORDER BY " + ContractPortafolioProductos.Columnas.MARCA;
                db = this.getReadableDatabase();
                cursor = db.rawQuery(selectQuery, new String[]{"%" + fabricante + "%", categoria, subcategoria});
            }else if (canal.equalsIgnoreCase("AUTOSERVICIO") && subcanal.equalsIgnoreCase("CORAL HIPERMERCADOS")) {
                selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.MARCA + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS +
                        " WHERE " + ContractPortafolioProductos.Columnas.MARCA + " <> 'Mayik' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Ta´Riko' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Herbal Supermaxi' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Supermaxi' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Santa Maria' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Aki' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Mi Comisariato' AND " +
                        byFabricante + " AND " +
                        ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                        ContractPortafolioProductos.Columnas.CATEGORY + "=?" +
                        " ORDER BY " + ContractPortafolioProductos.Columnas.MARCA;
                db = this.getReadableDatabase();
                cursor = db.rawQuery(selectQuery, new String[]{"%" + fabricante + "%", categoria, subcategoria});
            }else if (canal.equalsIgnoreCase("AUTOSERVICIO") && subcanal.equalsIgnoreCase("SUPERMAXI")) {
                selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.MARCA + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS +
                        " WHERE " + ContractPortafolioProductos.Columnas.MARCA + " <> 'Ta´Riko' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Coral' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Aki' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Mi Comisariato' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Santa Maria' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Mayik' AND " +
                        byFabricante + " AND " +
                        ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                        ContractPortafolioProductos.Columnas.CATEGORY + "=?" +
                        " ORDER BY " + ContractPortafolioProductos.Columnas.MARCA;
                db = this.getReadableDatabase();
                cursor = db.rawQuery(selectQuery, new String[]{"%" + fabricante + "%", categoria, subcategoria});
            }else if (canal.equalsIgnoreCase("AUTOSERVICIO") && subcanal.equalsIgnoreCase("MI COMISARIATO")) {
                selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.MARCA + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS +
                        " WHERE " + ContractPortafolioProductos.Columnas.MARCA + " <> 'Ta´Riko' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Coral' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Aki' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Supermaxi' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Santa Maria' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Mayik' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Herbal Supermaxi' AND " +
                        byFabricante + " AND " +
                        ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                        ContractPortafolioProductos.Columnas.CATEGORY + "=?" +
                        " ORDER BY " + ContractPortafolioProductos.Columnas.MARCA;
                db = this.getReadableDatabase();
                cursor = db.rawQuery(selectQuery, new String[]{"%" + fabricante + "%", categoria, subcategoria});
            }else if (canal.equalsIgnoreCase("AUTOSERVICIO") && subcanal.equalsIgnoreCase("SANTA MARIA")) {
                selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.MARCA + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS +
                        " WHERE " + ContractPortafolioProductos.Columnas.MARCA + " <> 'Ta´Riko' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Coral' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Aki' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Supermaxi' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Mi Comisariato' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Herbal Supermaxi' AND " +
                        ContractPortafolioProductos.Columnas.MARCA + " <> 'Mayik' AND " +
                        byFabricante + " AND " +
                        ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                        ContractPortafolioProductos.Columnas.CATEGORY + "=?" +
                        " ORDER BY " + ContractPortafolioProductos.Columnas.MARCA;
                db = this.getReadableDatabase();
                cursor = db.rawQuery(selectQuery, new String[]{"%" + fabricante + "%", categoria, subcategoria});
            }else{
                selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.MARCA + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS +
                        " WHERE " + byFabricante + " AND " +
                        ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                        ContractPortafolioProductos.Columnas.CATEGORY + "=?" +
                        " ORDER BY " + ContractPortafolioProductos.Columnas.MARCA;
                db = this.getReadableDatabase();
                cursor = db.rawQuery(selectQuery, new String[]{"%" + fabricante + "%", categoria, subcategoria});
            }
        }

        String header = "HEADER FOR RECYCLERVIEW";
        operadores.add(new BasePortafolioProductos(header));
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                BasePortafolioProductos bpv = new BasePortafolioProductos();
                bpv.setSku(cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.MARCA)));
                operadores.add(bpv);
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }



    public ArrayList<Base_precio_pvc> getPreciosPvc(String user,String codigo,String canal,String categoria,String marca) {
        ArrayList<Base_precio_pvc> operadores = new ArrayList<>();

        Cursor cursor = null;
        String selectQuery = "";

        Log.i("PVC usuario",""+user);
        Log.i("PVC codigo_pdv",""+codigo);
        Log.i("PVC canal",""+canal);
        Log.i("PVC categoria",""+categoria);
    //    Log.i("PVC subcategoria",""+subcategoria);
        Log.i("PVC marca",""+marca);

                selectQuery = "SELECT DISTINCT "+ ContractPreciosPvc.Columnas.SKU +" , " + ContractPreciosPvc.Columnas.PVC + " FROM "+ ContractPreciosPvc.PRECIOS_PVC +
                        " WHERE " + ContractPreciosPvc.Columnas.USUARIO + "=? AND "
                        + ContractPreciosPvc.Columnas.CODIGO + "=? AND "
                        + ContractPreciosPvc.Columnas.CANAL + "=? AND "
                        + ContractPreciosPvc.Columnas.CATEGORIA + "=? AND "
                     //   + ContractPreciosPvc.Columnas.SUBCATEGORIA + "=? AND "
                        + ContractPreciosPvc.Columnas.MARCA + "=? ";
                db = this.getReadableDatabase();
                cursor = db.rawQuery(selectQuery, new String[]{user,codigo,canal,categoria,/* subcategoria, */ marca});


        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Base_precio_pvc precio_pvc = new Base_precio_pvc();
                precio_pvc.setSku(cursor.getString(cursor.getColumnIndexOrThrow(ContractPreciosPvc.Columnas.SKU)));
                precio_pvc.setPvc(cursor.getString(cursor.getColumnIndexOrThrow(ContractPreciosPvc.Columnas.PVC)));
             //   bpv.setSku(cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.MARCA)));
                operadores.add(precio_pvc);
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

//    //getultimopvc filtro x usuario --
//    public ArrayList<Base_precio_pvc> getUltimoPvcDesdeInsertPrecios(String user, String codigo, String categoria, String marca) {
//        ArrayList<Base_precio_pvc> operadores = new ArrayList<>();
//
//        db = this.getReadableDatabase();
//
//        // TOTAL de filas en la tabla
//        Cursor cursorTotal = db.rawQuery(
//                "SELECT COUNT(*) FROM " + ContractInsertPrecios.INSERT_PRECIOS, null);
//        if (cursorTotal.moveToFirst()) {
//            Log.i("PVC_INSERT", "TOTAL filas en insert_precios: " + cursorTotal.getString(0));
//        }
//        cursorTotal.close();
//
//        Log.i("PVC_INSERT", "user=" + user + " | codigo=" + codigo + " | categoria=" + categoria + " | marca=" + marca);
//
//        // Primero verificamos cuantos registros hay sin filtro de marca/categoria
//        Cursor cursorDebug = db.rawQuery(
//                "SELECT " + ContractInsertPrecios.Columnas.SKU_CODE
//                        + ", " + ContractInsertPrecios.Columnas.PPROMOCION
//                        + ", " + ContractInsertPrecios.Columnas.USUARIO
//                        + ", " + ContractInsertPrecios.Columnas.CODIGO
//                        + ", " + ContractInsertPrecios.Columnas.CATEGORIA
//                        + ", " + ContractInsertPrecios.Columnas.BRAND
//                        + " FROM " + ContractInsertPrecios.INSERT_PRECIOS
//                        + " WHERE " + ContractInsertPrecios.Columnas.USUARIO + "=? AND "
//                        + ContractInsertPrecios.Columnas.CODIGO + "=?"
//                        + " LIMIT 5",
//                new String[]{user, codigo});
//
//        Log.i("PVC_INSERT", "Filas solo con user+codigo: " + cursorDebug.getCount());
//        if (cursorDebug.moveToFirst()) {
//            do {
//                Log.i("PVC_INSERT", "SKU=" + cursorDebug.getString(0)
//                        + " | PVC=" + cursorDebug.getString(1)
//                        + " | user=" + cursorDebug.getString(2)
//                        + " | codigo=" + cursorDebug.getString(3)
//                        + " | categoria=" + cursorDebug.getString(4)
//                        + " | marca=" + cursorDebug.getString(5));
//            } while (cursorDebug.moveToNext());
//        }
//        cursorDebug.close();
//
//        // Query principal - trae el ULTIMO promotional_price por SKU
//        // Ordena por rowid DESC para garantizar el registro mas reciente
//        String selectQuery = "SELECT " + ContractInsertPrecios.Columnas.SKU_CODE
//                + ", " + ContractInsertPrecios.Columnas.PPROMOCION
//                + " FROM " + ContractInsertPrecios.INSERT_PRECIOS
//                + " WHERE rowid IN ("
//                + " SELECT MAX(rowid) FROM " + ContractInsertPrecios.INSERT_PRECIOS
//                + " WHERE " + ContractInsertPrecios.Columnas.USUARIO + "=? AND "
//                + ContractInsertPrecios.Columnas.CODIGO + "=? AND "
//                + ContractInsertPrecios.Columnas.CATEGORIA + "=? AND "
//                + ContractInsertPrecios.Columnas.BRAND + "=?"
//                + " GROUP BY " + ContractInsertPrecios.Columnas.SKU_CODE
//                + ")";
//
//        Cursor cursor = db.rawQuery(selectQuery, new String[]{user, codigo, categoria, marca});
//        Log.i("PVC_INSERT", "Filas con todos los filtros: " + cursor.getCount());
//
//        if (cursor.moveToFirst()) {
//            do {
//                Base_precio_pvc precio_pvc = new Base_precio_pvc();
//                precio_pvc.setSku(cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertPrecios.Columnas.SKU_CODE)));
//                precio_pvc.setPvc(cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertPrecios.Columnas.PPROMOCION)));
//                operadores.add(precio_pvc);
//                Log.i("PVC_INSERT", "SKU=" + precio_pvc.getSku() + " | PVC=" + precio_pvc.getPvc());
//            } while (cursor.moveToNext());
//        }
//
//        cursor.close();
//        db.close();
//        return operadores;
//    }



    public ArrayList<Base_precio_pvc> getUltimoPvcDesdeInsertPrecios(String user, String codigo, String categoria, String marca) {
        ArrayList<Base_precio_pvc> operadores = new ArrayList<>();

        db = this.getReadableDatabase();

        Log.i("PVC_INSERT", "Buscando últimos PVC para user=" + user + " | codigo=" + codigo);

        // Quitamos los filtros de CATEGORIA y BRAND en el WHERE
        String selectQuery = "SELECT " + ContractInsertPrecios.Columnas.SKU_CODE
                + ", " + ContractInsertPrecios.Columnas.PPROMOCION
                + " FROM " + ContractInsertPrecios.INSERT_PRECIOS
                + " WHERE rowid IN ("
                + " SELECT MAX(rowid)"
                + " FROM " + ContractInsertPrecios.INSERT_PRECIOS
                + " WHERE " + ContractInsertPrecios.Columnas.USUARIO + "=? AND "
                + ContractInsertPrecios.Columnas.CODIGO + "=?"
                + " GROUP BY " + ContractInsertPrecios.Columnas.SKU_CODE
                + ")";

        // Solo pasamos user y codigo como parámetros
        Cursor cursor = db.rawQuery(selectQuery, new String[]{user, codigo});
        Log.i("PVC_INSERT", "Filas encontradas (incluyendo SYNC): " + cursor.getCount());

        if (cursor.moveToFirst()) {
            do {
                Base_precio_pvc precio_pvc = new Base_precio_pvc();
                precio_pvc.setSku(cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertPrecios.Columnas.SKU_CODE)));
                precio_pvc.setPvc(cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertPrecios.Columnas.PPROMOCION)));
                operadores.add(precio_pvc);
                Log.i("PVC_INSERT", "SKU=" + precio_pvc.getSku() + " | PVC=" + precio_pvc.getPvc());
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return operadores;
    }

    public void guardarUltimoPvcLocal(JSONArray array, String user, String codigo) {
        try {
            db = this.getWritableDatabase();

            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                String sku    = obj.getString("sku_code");
                String pvc    = obj.getString("pvc");

                Cursor cursor = db.rawQuery(
                        "SELECT COUNT(*) FROM " + ContractInsertPrecios.INSERT_PRECIOS
                                + " WHERE " + ContractInsertPrecios.Columnas.SKU_CODE + "=?"
                                + " AND " + ContractInsertPrecios.Columnas.USUARIO + "=?"
                                + " AND " + ContractInsertPrecios.Columnas.CODIGO + "=?",
                        new String[]{sku, user, codigo}
                );

                cursor.moveToFirst();
                int count = cursor.getInt(0);
                cursor.close();

                if (count == 0) {
                    ContentValues values = new ContentValues();
                    values.put(ContractInsertPrecios.Columnas.SKU_CODE,   sku);
                    values.put(ContractInsertPrecios.Columnas.PPROMOCION, pvc);
                    values.put(ContractInsertPrecios.Columnas.USUARIO,    user);
                    values.put(ContractInsertPrecios.Columnas.CODIGO,     codigo);
                    values.put(ContractInsertPrecios.Columnas.CATEGORIA,  "SYNC");
                    values.put(ContractInsertPrecios.Columnas.BRAND,      "SYNC");
                    values.put(ContractInsertPrecios.Columnas.FECHA,      "01/01/1900");
                    values.put(ContractInsertPrecios.Columnas.HORA,       "00:00:00");
                    values.put(Constantes.PENDIENTE_INSERCION,            0);
                    db.insert(ContractInsertPrecios.INSERT_PRECIOS, null, values);
                    Log.i("PVC_SYNC", "Insertado desde servidor: " + sku + " = " + pvc);
                } else {
                    Log.i("PVC_SYNC", "Ya existe localmente, se omite: " + sku);
                }
            }

            db.close();

        } catch (Exception e) {
            Log.e("PVC_SYNC", "Error guardando PVC local: " + e.getMessage());
        }
    }
    public void guardarUltimoPrecioVentasLocal(JSONArray array, String user, String codigo) {
        try {
            db = this.getWritableDatabase();
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                String sku    = obj.getString("sku_code");
                String precio = obj.getString("precio_unitario");

                Cursor cursor = db.rawQuery(
                        "SELECT COUNT(*) FROM " + ContractInsertPrecios.INSERT_PRECIOS
                                + " WHERE " + ContractInsertPrecios.Columnas.SKU_CODE + "=?"
                                + " AND " + ContractInsertPrecios.Columnas.USUARIO + "=?"
                                + " AND " + ContractInsertPrecios.Columnas.CODIGO + "=?"
                                + " AND " + ContractInsertPrecios.Columnas.CATEGORIA + "=?",
                        new String[]{sku, user, codigo, "SYNC_VENTAS"}
                );
                cursor.moveToFirst();
                int count = cursor.getInt(0);
                cursor.close();

                ContentValues values = new ContentValues();
                values.put(ContractInsertPrecios.Columnas.SKU_CODE,   sku);
                values.put(ContractInsertPrecios.Columnas.PPROMOCION, precio);
                values.put(ContractInsertPrecios.Columnas.USUARIO,    user);
                values.put(ContractInsertPrecios.Columnas.CODIGO,     codigo);
                values.put(ContractInsertPrecios.Columnas.CATEGORIA,  "SYNC_VENTAS");
                values.put(ContractInsertPrecios.Columnas.BRAND,      "SYNC_VENTAS");
                values.put(ContractInsertPrecios.Columnas.FECHA,      "01/01/1900");
                values.put(ContractInsertPrecios.Columnas.HORA,       "00:00:00");
                values.put(Constantes.PENDIENTE_INSERCION,            0);

                if (count == 0) {
                    db.insert(ContractInsertPrecios.INSERT_PRECIOS, null, values);
                    Log.i("VENTAS_SYNC", "Insertado: " + sku + " = " + precio);
                } else {
                    // Actualiza el precio si ya existe
                    db.update(ContractInsertPrecios.INSERT_PRECIOS, values,
                            ContractInsertPrecios.Columnas.SKU_CODE + "=? AND "
                                    + ContractInsertPrecios.Columnas.USUARIO + "=? AND "
                                    + ContractInsertPrecios.Columnas.CODIGO + "=? AND "
                                    + ContractInsertPrecios.Columnas.CATEGORIA + "=?",
                            new String[]{sku, user, codigo, "SYNC_VENTAS"});
                    Log.i("VENTAS_SYNC", "Actualizado: " + sku + " = " + precio);
                }
            }
            db.close();
        } catch (Exception e) {
            Log.e("VENTAS_SYNC", "Error: " + e.getMessage());
        }
    }

    public ArrayList<Base_precio_pvc> getUltimoPrecioVentasDesdeLocal(String user, String codigo) {
        ArrayList<Base_precio_pvc> lista = new ArrayList<>();
        db = this.getReadableDatabase();

        // Consulta unificada: obtiene el último registro por SKU sin importar la categoría
        String selectQuery =
                "SELECT " + ContractInsertPrecios.Columnas.SKU_CODE
                        + ", " + ContractInsertPrecios.Columnas.PPROMOCION
                        + " FROM " + ContractInsertPrecios.INSERT_PRECIOS
                        + " WHERE rowid IN ("
                        + " SELECT MAX(rowid) FROM " + ContractInsertPrecios.INSERT_PRECIOS
                        + " WHERE " + ContractInsertPrecios.Columnas.USUARIO + "=?"
                        + " AND " + ContractInsertPrecios.Columnas.CODIGO + "=?"
                        + " GROUP BY " + ContractInsertPrecios.Columnas.SKU_CODE
                        + ")";

        Cursor cursor = db.rawQuery(selectQuery, new String[]{user, codigo});

        if (cursor.moveToFirst()) {
            do {
                Base_precio_pvc p = new Base_precio_pvc();
                p.setSku(cursor.getString(0));
                p.setPvc(cursor.getString(1));
                lista.add(p);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return lista;
    }

    public void guardarUltimoPvcAnteriorPromoLocal(JSONArray array, String user, String codigo) {
        try {
            db = this.getWritableDatabase();
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                String sku       = obj.getString("sku");
                String pvcAnterior = obj.getString("pvc_anterior");

                Cursor cursor = db.rawQuery(
                        "SELECT COUNT(*) FROM " + ContractInsertPrecios.INSERT_PRECIOS
                                + " WHERE " + ContractInsertPrecios.Columnas.SKU_CODE + "=?"
                                + " AND " + ContractInsertPrecios.Columnas.USUARIO + "=?"
                                + " AND " + ContractInsertPrecios.Columnas.CODIGO + "=?"
                                + " AND " + ContractInsertPrecios.Columnas.CATEGORIA + "=?",
                        new String[]{sku, user, codigo, "SYNC_PROMO"}
                );
                cursor.moveToFirst();
                int count = cursor.getInt(0);
                cursor.close();

                ContentValues values = new ContentValues();
                values.put(ContractInsertPrecios.Columnas.SKU_CODE,   sku);
                values.put(ContractInsertPrecios.Columnas.PPROMOCION, pvcAnterior);
                values.put(ContractInsertPrecios.Columnas.USUARIO,    user);
                values.put(ContractInsertPrecios.Columnas.CODIGO,     codigo);
                values.put(ContractInsertPrecios.Columnas.CATEGORIA,  "SYNC_PROMO");
                values.put(ContractInsertPrecios.Columnas.BRAND,      "SYNC_PROMO");
                values.put(ContractInsertPrecios.Columnas.FECHA,      "01/01/1900");
                values.put(ContractInsertPrecios.Columnas.HORA,       "00:00:00");
                values.put(Constantes.PENDIENTE_INSERCION,            0);

                if (count == 0) {
                    db.insert(ContractInsertPrecios.INSERT_PRECIOS, null, values);
                    Log.i("PROMO_SYNC", "Insertado: " + sku + " = " + pvcAnterior);
                } else {
                    db.update(ContractInsertPrecios.INSERT_PRECIOS, values,
                            ContractInsertPrecios.Columnas.SKU_CODE + "=? AND "
                                    + ContractInsertPrecios.Columnas.USUARIO + "=? AND "
                                    + ContractInsertPrecios.Columnas.CODIGO + "=? AND "
                                    + ContractInsertPrecios.Columnas.CATEGORIA + "=?",
                            new String[]{sku, user, codigo, "SYNC_PROMO"});
                    Log.i("PROMO_SYNC", "Actualizado: " + sku + " = " + pvcAnterior);
                }
            }
            db.close();
        } catch (Exception e) {
            Log.e("PROMO_SYNC", "Error: " + e.getMessage());
        }
    }

    public ArrayList<Base_precio_pvc> getUltimoPvcAnteriorPromoDesdeLocal(String user, String codigo) {
        ArrayList<Base_precio_pvc> lista = new ArrayList<>();
        db = this.getReadableDatabase();

        String selectQuery =
                "SELECT " + ContractInsertPrecios.Columnas.SKU_CODE
                        + ", " + ContractInsertPrecios.Columnas.PPROMOCION
                        + " FROM " + ContractInsertPrecios.INSERT_PRECIOS
                        + " WHERE rowid IN ("
                        + " SELECT MAX(rowid) FROM " + ContractInsertPrecios.INSERT_PRECIOS
                        + " WHERE " + ContractInsertPrecios.Columnas.USUARIO + "=?"
                        + " AND " + ContractInsertPrecios.Columnas.CODIGO + "=?"
                        + " AND " + ContractInsertPrecios.Columnas.CATEGORIA + "=?"
                        + " GROUP BY " + ContractInsertPrecios.Columnas.SKU_CODE
                        + ")";

        Cursor cursor = db.rawQuery(selectQuery, new String[]{user, codigo, "SYNC_PROMO"});

        if (cursor.moveToFirst()) {
            do {
                Base_precio_pvc p = new Base_precio_pvc();
                p.setSku(cursor.getString(0));
                p.setPvc(cursor.getString(1));
                lista.add(p);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return lista;
    }


    public ArrayList<Base_Inventario> filtrarListProductos3Flooring(String categoria, String subcategoria, String brand , String gramaje,
                                                                    String codigo, String canal, String subcanal, String manufacturer, String modulo) {
        ArrayList<Base_Inventario> operadores = new ArrayList<Base_Inventario>();

        Log.i("CATEGORIA", categoria);
    //    Log.i("SUBCATEGORIA", subcategoria);
        Log.i("MARCA", brand);
      //  Log.i("GRAMAJE", gramaje);

        Cursor cursor = null;
        String selectQuery = "";

        if (canal.equalsIgnoreCase("MAYORISTA")) {
//            if (categoria.equalsIgnoreCase("INSECTICIDAS")) {
//                selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.SKU + " FROM " +
//                        ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " +
//                        ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
//                        ContractPortafolioProductos.Columnas.CATEGORY + "=? AND " +
//                        ContractPortafolioProductos.Columnas.LOCALES + " LIKE ? AND " +
//                        ContractPortafolioProductos.Columnas.MARCA + "=? AND (" +
//                        ContractPortafolioProductos.Columnas.FABRICANTE + " LIKE ? OR " +
//                        ContractPortafolioProductos.Columnas.FABRICANTE + "='Sc Jhonson')" +
//                        " ORDER BY " + ContractPortafolioProductos.Columnas.SKU;
//                db = this.getReadableDatabase();
//                cursor = db.rawQuery(selectQuery, new String[]{categoria, subcategoria, "%"+ modulo +"%", brand, "%" + manufacturer + "%"});
//            } else {
                selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.SKU + " FROM " +
                        ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " +
                        ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                //        ContractPortafolioProductos.Columnas.CATEGORY + "=? AND " +
                        ContractPortafolioProductos.Columnas.MARCA + "=? AND " +
                        ContractPortafolioProductos.Columnas.CONTENIDO + "=? AND " +
                        ContractPortafolioProductos.Columnas.FABRICANTE + " LIKE ? AND " +
                        ContractPortafolioProductos.Columnas.LOCALES + " LIKE ? " +
                        " ORDER BY " + ContractPortafolioProductos.Columnas.SKU;
                db = this.getReadableDatabase();
                cursor = db.rawQuery(selectQuery, new String[]{categoria,/* subcategoria,*/ brand,gramaje, "%" + manufacturer + "%","%"+ modulo +"%"});
//            }
        }else{
            selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.SKU + " FROM " +
                    ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " +
                    ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
            //        ContractPortafolioProductos.Columnas.CATEGORY + "=? AND " +
                    ContractPortafolioProductos.Columnas.MARCA + "=? AND " +
                    ContractPortafolioProductos.Columnas.CONTENIDO + "=? AND " +
                    ContractPortafolioProductos.Columnas.CADENAS + " LIKE ? AND " +
                    ContractPortafolioProductos.Columnas.FABRICANTE + " LIKE ? AND " +
                    ContractPortafolioProductos.Columnas.LOCALES + " LIKE ? " +
                    " ORDER BY " + ContractPortafolioProductos.Columnas.SKU;
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{categoria,/* subcategoria,*/ brand,gramaje, "%" + subcanal.trim() + "%","%" + manufacturer + "%","%"+ modulo +"%"});
        }

        // looping through all rows and adding to list
        BasePortafolioProductos bpv_test = new BasePortafolioProductos();
     //   bpv_test.setSku("TEST");
      //  operadores.add(bpv_test);
        if (cursor.moveToFirst() && !cursor.isClosed()) {
            do {
                Base_Inventario bpv = new Base_Inventario();
                bpv.setSku(cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.SKU)));
                operadores.add(bpv);
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public ArrayList<Precio> filtrarListProductos2Precios(String categoria, /* String subcategoria, */ String brand,String tipo,
                                                          String fabricante, String codigo, String canal, String subcanal, String modulo) {
        ArrayList<Precio> operadores = new ArrayList<>();

        if (subcanal.equalsIgnoreCase("NO CADENA")){
            subcanal = "N/A";
        }

        String byFabricante = ContractPortafolioProductos.Columnas.FABRICANTE + " NOT LIKE ? ";

        if (tipo.equalsIgnoreCase("MARCA_PROPIA")) {
            byFabricante = ContractPortafolioProductos.Columnas.FABRICANTE + " LIKE ? ";
        }

        Cursor cursor = null;
        String selectQuery = null;

        Log.i("CATEGORIA", categoria);
//        Log.i("SUBCATEGORIA", subcategoria);
        Log.i("MARCA", brand);
        Log.i("FABRICANTE", fabricante);
        Log.i("CODIGO", codigo);
        Log.i("CANAL", canal);

        if (canal.equalsIgnoreCase("MAYORISTA")) {
            selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.SKU + ", " + ContractPortafolioProductos.Columnas.PVP + ", " + ContractPortafolioProductos.Columnas.CONTENIDO + " FROM " +
                    ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " +
                    ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
            //        ContractPortafolioProductos.Columnas.CATEGORY + "=? AND " +
                    ContractPortafolioProductos.Columnas.MARCA + "=? AND " +
                    ContractPortafolioProductos.Columnas.LOCALES + " LIKE ? AND " +
                    byFabricante +
                    " ORDER BY " + ContractPortafolioProductos.Columnas.SKU;
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{categoria, /* subcategoria, */ brand, "%" + modulo + "%","%" + fabricante + "%"});
        }else if (canal.equalsIgnoreCase("AUTOSERVICIO")) {
            selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.SKU + ", " + ContractPortafolioProductos.Columnas.PVP + ", " + ContractPortafolioProductos.Columnas.CONTENIDO + " FROM " +
                    ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " +
                    ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
            //        ContractPortafolioProductos.Columnas.CATEGORY + "=? AND " +
                    ContractPortafolioProductos.Columnas.MARCA + "=? AND " +
                    ContractPortafolioProductos.Columnas.CADENAS + " LIKE ? AND " +
                    ContractPortafolioProductos.Columnas.LOCALES + " LIKE ? AND " +
                    byFabricante +
                    " ORDER BY " + ContractPortafolioProductos.Columnas.SKU;
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{categoria, /* subcategoria, */ brand,"%" + subcanal.trim() + "%", "%" + modulo + "%","%" + fabricante + "%"});
        }else{
            selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.SKU + ", " + ContractPortafolioProductos.Columnas.PVP + ", " + ContractPortafolioProductos.Columnas.CONTENIDO + " FROM " +
                    ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " +
                    ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
            //        ContractPortafolioProductos.Columnas.CATEGORY + "=? AND " +
                    ContractPortafolioProductos.Columnas.MARCA + "=? AND " +
                    ContractPortafolioProductos.Columnas.FABRICANTE + "=? AND " +
                    ContractPortafolioProductos.Columnas.LOCALES + " LIKE ? " +
                    " ORDER BY " + ContractPortafolioProductos.Columnas.SKU;
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{categoria, /* subcategoria, */ brand, fabricante, "%" + modulo + "%"});
        }

        if (cursor.moveToFirst()) {
            do {
                Precio p = new Precio();
                p.setSku_code(cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.SKU)));
                p.setPvp(cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.PVP)));
                p.setContenido(cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.CONTENIDO)));
                operadores.add(p);
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }
    public ArrayList<Precio> filtrarListProductosLogistico(String categoria, /* String subcategoria, */ String brand,String tipo,
                                                              String fabricante, String codigo, String canal, String subcanal, String modulo) {
            ArrayList<Precio> operadores = new ArrayList<>();

            if (subcanal.equalsIgnoreCase("NO CADENA")){
                subcanal = "N/A";
            }

            String byFabricante = ContractPortafolioProductos.Columnas.FABRICANTE + " NOT LIKE ? ";

            if (tipo.equalsIgnoreCase("RELEVO")) {
                byFabricante = ContractPortafolioProductos.Columnas.FABRICANTE + " LIKE ? ";
            }

            Cursor cursor = null;
            String selectQuery = null;

            Log.i("CATEGORIA", categoria);
    //        Log.i("SUBCATEGORIA", subcategoria);
            Log.i("MARCA", brand);
            Log.i("FABRICANTE", fabricante);
            Log.i("CODIGO", codigo);
            Log.i("CANAL", canal);

            if (canal.equalsIgnoreCase("MAYORISTA")) {
                selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.SKU + ", " + ContractPortafolioProductos.Columnas.PVP + " FROM " +
                        ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " +
                        ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                //        ContractPortafolioProductos.Columnas.CATEGORY + "=? AND " +
                        ContractPortafolioProductos.Columnas.MARCA + "=? AND " +
                        ContractPortafolioProductos.Columnas.LOCALES + " LIKE ? AND " +
                        byFabricante +
                        " ORDER BY " + ContractPortafolioProductos.Columnas.SKU;
                db = this.getReadableDatabase();
                cursor = db.rawQuery(selectQuery, new String[]{categoria, /* subcategoria, */ brand, "%" + modulo + "%","%" + fabricante + "%"});
            }else if (canal.equalsIgnoreCase("AUTOSERVICIO")) {
                selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.SKU + ", " + ContractPortafolioProductos.Columnas.PVP + " FROM " +
                        ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " +
                        ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                //        ContractPortafolioProductos.Columnas.CATEGORY + "=? AND " +
                        ContractPortafolioProductos.Columnas.MARCA + "=? AND " +
                        ContractPortafolioProductos.Columnas.CADENAS + " LIKE ? AND " +
                        ContractPortafolioProductos.Columnas.LOCALES + " LIKE ? AND " +
                        byFabricante +
                        " ORDER BY " + ContractPortafolioProductos.Columnas.SKU;
                db = this.getReadableDatabase();
                cursor = db.rawQuery(selectQuery, new String[]{categoria, /* subcategoria, */ brand,"%" + subcanal.trim() + "%", "%" + modulo + "%","%" + fabricante + "%"});
            }else{
                selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.SKU + ", " + ContractPortafolioProductos.Columnas.PVP + " FROM " +
                        ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " +
                        ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                //        ContractPortafolioProductos.Columnas.CATEGORY + "=? AND " +
                        ContractPortafolioProductos.Columnas.MARCA + "=? AND " +
                        ContractPortafolioProductos.Columnas.FABRICANTE + "=? AND " +
                        ContractPortafolioProductos.Columnas.LOCALES + " LIKE ? " +
                        " ORDER BY " + ContractPortafolioProductos.Columnas.SKU;
                db = this.getReadableDatabase();
                cursor = db.rawQuery(selectQuery, new String[]{categoria, /* subcategoria, */ brand, fabricante, "%" + modulo + "%"});
            }

            if (cursor.moveToFirst()) {
                do {
                    Precio p = new Precio();
                    p.setSku_code(cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.SKU)));
                    p.setPvp(cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.PVP)));
                    operadores.add(p);
                } while (cursor.moveToNext());
            }

            // closing connection
            cursor.close();
            db.close();
            // returning lables
            return operadores;
        }

    /**
     *  SECCION PRODUCTOS ALERTA
     */

    public ArrayList<Precio> filtrarListProductosAlerta(String categoria, /* String subcategoria,  String brand,*/String tipo,
                                                        String fabricante, String codigo, String canal, String subcanal, String modulo) {
        ArrayList<Precio> operadores = new ArrayList<>();

        if (subcanal.equalsIgnoreCase("NO CADENA")){
            subcanal = "N/A";
        }

        String byFabricante = ContractPortafolioProductos.Columnas.FABRICANTE + " NOT LIKE ? ";

        if (tipo.equalsIgnoreCase("ALERTA")) {
            byFabricante = ContractPortafolioProductos.Columnas.FABRICANTE + " LIKE ? ";
        }

        Cursor cursor = null;
        String selectQuery = null;

        Log.i("CATEGORIA", categoria);
        //        Log.i("SUBCATEGORIA", subcategoria);
        /*Log.i("MARCA", brand);*/
        Log.i("FABRICANTE", fabricante);
        Log.i("CODIGO", codigo);
        Log.i("CANAL", canal);

        if (canal.equalsIgnoreCase("MAYORISTA")) {
            selectQuery = "SELECT DISTINCT "+ ContractInsertLogisticoRelevo.Columnas.SKU_CODE + " FROM " +
                    ContractInsertLogisticoRelevo.INSERT_LOGISTICO_RELEVO + " WHERE " +
                    ContractInsertLogisticoRelevo.Columnas.CATEGORIA + "=? AND " +
                    //        ContractPortafolioProductos.Columnas.CATEGORY + "=? AND " +
                    /*ContractPortafolioProductos.Columnas.MARCA + "=? AND " +*/
                    ContractInsertLogisticoRelevo.Columnas.PREGULAR + " LIKE ? AND " +
                    byFabricante +
                    " ORDER BY " + ContractInsertLogisticoRelevo.Columnas.SKU_CODE;
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{categoria, /* subcategoria, brand,*/ "%" + modulo + "%","%" + fabricante + "%"});
        }else if (canal.equalsIgnoreCase("AUTOSERVICIO")) {
            selectQuery = "SELECT DISTINCT "+ ContractInsertLogisticoRelevo.Columnas.SKU_CODE + " FROM " +
                    ContractInsertLogisticoRelevo.INSERT_LOGISTICO_RELEVO + " WHERE " +
                    ContractInsertLogisticoRelevo.Columnas.CATEGORIA + "=? AND " +
                    //        ContractPortafolioProductos.Columnas.CATEGORY + "=? AND " +
                    /*ContractPortafolioProductos.Columnas.MARCA + "=? AND " +
                    ContractInsertLogisticoRelevo.Columnas.CADENAS + " LIKE ? AND " +*/
                    ContractInsertLogisticoRelevo.Columnas.PREGULAR + " LIKE ? AND " +
                    byFabricante +
                    " ORDER BY " + ContractInsertLogisticoRelevo.Columnas.SKU_CODE;
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{categoria, /* subcategoria,  brand,*/"%" + subcanal.trim() + "%", "%" + modulo + "%","%" + fabricante + "%"});
        }else{
            selectQuery = "SELECT DISTINCT "+ ContractInsertLogisticoRelevo.Columnas.SKU_CODE + " FROM " +
                    ContractInsertLogisticoRelevo.INSERT_LOGISTICO_RELEVO + " WHERE " +
                    ContractInsertLogisticoRelevo.Columnas.CATEGORIA + "=? AND " +
                    //        ContractPortafolioProductos.Columnas.CATEGORY + "=? AND " +
                    /*ContractPortafolioProductos.Columnas.MARCA + "=? AND " +
                    ContractInsertLogisticoRelevo.Columnas.FABRICANTE + "=? AND " +*/
                    ContractInsertLogisticoRelevo.Columnas.PREGULAR + " LIKE ? " +
                    " ORDER BY " + ContractInsertLogisticoRelevo.Columnas.SKU_CODE;
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{categoria, /* subcategoria,  brand,*/ fabricante, "%" + modulo + "%"});
        }

        if (cursor.moveToFirst()) {
            do {
                Precio p = new Precio();
                p.setSku_code(cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertLogisticoRelevo.Columnas.SKU_CODE)));

                operadores.add(p);
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }


    /**
     *  SECCION PRODUCTOS ALERTA
     */

    public ArrayList<BaseTareas> filtrarListProductosTareas(String codigo_pdv, String user) {
        ArrayList<BaseTareas> operadores = new ArrayList<>();

        // Select All Query
//        String selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.SECTOR + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS;
//
//        db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery(selectQuery, new String[]{});

        Cursor cursor = null;
        String selectQuery = null;
//        if (canal.equalsIgnoreCase("MAYORISTA")) {
//            selectQuery = "SELECT DISTINCT "+ ContractTareas.Columnas.TAREAS + " FROM " +
//                    ContractTareas.TAREA + " WHERE " + ContractTareas.Columnas.CANAL + "=?";
//            db = this.getReadableDatabase();
//            cursor = db.rawQuery(selectQuery, new String[]{canal});
//        }else if (canal.equalsIgnoreCase("AUTOSERVICIO")) {
            selectQuery = "SELECT * FROM " + ContractTareas.TAREA +
                    " WHERE " + ContractTareas.Columnas.CODIGOPDV + "=? AND " +
                    ContractTareas.Columnas.MERCADERISTA + "=? " +
                    " GROUP BY " + ContractTareas.Columnas.TAREAS;
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{codigo_pdv, user});
//        }//revisa xq no te trae todo, sino solo eso

//        operadores.add("Seleccione");
        // looping through all rows and adding to list

        if (cursor.moveToFirst()) {
            do {
                BaseTareas p = new BaseTareas();
                p.setTareas(cursor.getString(cursor.getColumnIndexOrThrow(ContractTareas.Columnas.TAREAS)));
                p.setPeriodo(cursor.getString(cursor.getColumnIndexOrThrow(ContractTareas.Columnas.PERIODO)));
                operadores.add(p);
            } while (cursor.moveToNext());
        }
        Log.i("OPERADORES",operadores.size()+"");
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }


    public List<String> filtrarListPortafolioPrioritario(String canal, String codigo_pdv) {
        List<String> operadores = new ArrayList<String>();

        // Select All Query
//        String selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.SECTOR + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS;
//
//        db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery(selectQuery, new String[]{});
        Log.i("PRIORITARIO", "CANAL: " + canal + " - CODIGO PDV: " + codigo_pdv);
        Cursor cursor = null;
        String selectQuery = null;
       /* if (canal.equalsIgnoreCase("MAYORISTA")) {*/
            selectQuery = "SELECT DISTINCT "+ ContractPrioritario.Columnas.SKU +
                    " FROM " + ContractPrioritario.PRIORITARIO +
                    " WHERE " + ContractPrioritario.Columnas.CANAL + "=? AND " +
                    ContractPrioritario.Columnas.CODIGO_PDV + "=?";
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{canal, codigo_pdv});
       /* }else if (canal.equalsIgnoreCase("AUTOSERVICIO")) {
            selectQuery = "SELECT DISTINCT "+ ContractPrioritario.Columnas.SKU + " FROM " +
                    ContractPrioritario.PRIORITARIO;
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{});
        }*/

//        operadores.add("Seleccione");
        // looping through all rows and adding to list

        if (cursor.moveToFirst()) {
            do {
                operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractPrioritario.Columnas.SKU)));
            } while (cursor.moveToNext());
        }
        Log.i("OPERADORES",operadores.size()+"");
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public List<String> filtrarListPop(String canal, String codigo_pdv) {
        List<String> operadores = new ArrayList<String>();

        // Select All Query
//        String selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.SECTOR + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS;
//
//        db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery(selectQuery, new String[]{});

        Cursor cursor = null;
        String selectQuery = null;
        /* if (canal.equalsIgnoreCase("MAYORISTA")) {*/
        selectQuery = "SELECT DISTINCT "+ ContractPopSugerido.Columnas.POP_SUGERIDO +
                " FROM " + ContractPopSugerido.POPSUGERIDO +
                " WHERE " + ContractPopSugerido.Columnas.CANAL + "=? AND " +
                ContractPopSugerido.Columnas.CODIGO_PDV + "=?";
        db = this.getReadableDatabase();
        cursor = db.rawQuery(selectQuery, new String[]{canal, codigo_pdv});
       /* }else if (canal.equalsIgnoreCase("AUTOSERVICIO")) {
            selectQuery = "SELECT DISTINCT "+ ContractPrioritario.Columnas.SKU + " FROM " +
                    ContractPrioritario.PRIORITARIO;
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{});
        }*/

//        operadores.add("Seleccione");
        // looping through all rows and adding to list

        if (cursor.moveToFirst()) {
            do {
                operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractPopSugerido.Columnas.POP_SUGERIDO)));
            } while (cursor.moveToNext());
        }
        Log.i("OPERADORES",operadores.size()+"");
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public ArrayList<Precio> filtrarListProductosProdCad(String fabricante, String categoria, String subcategoria, String brand) {
        ArrayList<Precio> operadores = new ArrayList<>();

        Cursor cursor = null;
        String selectQuery = "";
        // Select All Query
        selectQuery = "SELECT DISTINCT " + ContractPortafolioProductos.Columnas.SKU + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE "+
                ContractPortafolioProductos.Columnas.CATEGORY +"=? AND " +
                ContractPortafolioProductos.Columnas.SUBCATEGORIA +"=? AND " +
                ContractPortafolioProductos.Columnas.MARCA +"=? AND " +
                ContractPortafolioProductos.Columnas.FABRICANTE +" LIKE ?";
        db = this.getReadableDatabase();
        cursor = db.rawQuery(selectQuery, new String[]{categoria,subcategoria,brand,"%" + fabricante + "%"});
        //}

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Precio p = new Precio();
                p.setSku_code(cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.SKU)));
                operadores.add(p);
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public ArrayList<BaseRotacion> filtrarListRotacion(String tipo, String categoria, String subcategoria, String marca) {
        ArrayList<BaseRotacion> operadores = new ArrayList<>();

        Cursor cursor = null;
        String selectQuery = "";
        // Select All Query
        selectQuery = "SELECT DISTINCT " + ContractRotacion.Columnas.PRODUCTO +
                " FROM " + ContractRotacion.ROTACION +
                " WHERE " + ContractRotacion.Columnas.TIPO +"=? AND " +
                ContractRotacion.Columnas.CATEGORIA +"=? AND " +
                ContractRotacion.Columnas.SUBCATEGORIA +"=? AND " +
                ContractRotacion.Columnas.MARCA +"=?" +
                " ORDER BY " + ContractRotacion.Columnas.PRODUCTO;
        db = this.getReadableDatabase();
        cursor = db.rawQuery(selectQuery, new String[]{tipo, categoria, subcategoria, marca});
        //}

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                BaseRotacion p = new BaseRotacion();
                p.setProducto(cursor.getString(cursor.getColumnIndexOrThrow(ContractRotacion.Columnas.PRODUCTO)));
                operadores.add(p);
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public ArrayList<BaseAlertas> filtrarListMateriales(String alerta) {
        ArrayList<BaseAlertas> operadores = new ArrayList<>();

        Cursor cursor = null;
        String selectQuery = "";
        // Select All Query
        selectQuery = "SELECT DISTINCT " + ContractAlertas.Columnas.MATERIAL +
                " FROM " + ContractAlertas.ALERTAS +
                " WHERE " + ContractAlertas.Columnas.TIPO_ALERTA + " LIKE ?" +
                " ORDER BY " + ContractAlertas.Columnas.MATERIAL;
        db = this.getReadableDatabase();
        cursor = db.rawQuery(selectQuery, new String[]{"%" + alerta + "%"});
        //}

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                BaseAlertas p = new BaseAlertas();
                p.setMaterial(cursor.getString(cursor.getColumnIndexOrThrow(ContractAlertas.Columnas.MATERIAL)));
                operadores.add(p);
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public ArrayList<BaseAlertas> filtrarListEjecucionMateriales(String alerta, String categoria) {
        ArrayList<BaseAlertas> operadores = new ArrayList<>();

        Cursor cursor = null;
        String selectQuery = "";
        // Select All Query
        selectQuery = "SELECT DISTINCT " + ContractAlertas.Columnas.MATERIAL +
                " FROM " + ContractAlertas.ALERTAS +
                " WHERE " + ContractAlertas.Columnas.TIPO_ALERTA + " LIKE ? AND " +
                ContractAlertas.Columnas.CATEGORIA + "=?" +
                " ORDER BY " + ContractAlertas.Columnas.MATERIAL;
        db = this.getReadableDatabase();
        cursor = db.rawQuery(selectQuery, new String[]{"%" + alerta + "%", categoria});
        //}

        Log.i("ENTRA", "EJECUCION DE MATERIALES " + categoria);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                BaseAlertas p = new BaseAlertas();
                p.setMaterial(cursor.getString(cursor.getColumnIndexOrThrow(ContractAlertas.Columnas.MATERIAL)));
                operadores.add(p);
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public ArrayList<BaseRotacion> filtrarListMateriales(String tipo, String categoria, String subcategoria, String marca) {
        ArrayList<BaseRotacion> operadores = new ArrayList<>();

        Cursor cursor = null;
        String selectQuery = "";
        // Select All Query
        selectQuery = "SELECT DISTINCT " + ContractRotacion.Columnas.PRODUCTO +
                " FROM " + ContractRotacion.ROTACION +
                " WHERE " + ContractRotacion.Columnas.TIPO +"=? AND " +
                ContractRotacion.Columnas.CATEGORIA +"=? AND " +
                ContractRotacion.Columnas.SUBCATEGORIA +"=? AND " +
                ContractRotacion.Columnas.MARCA +"=?" +
                " ORDER BY " + ContractRotacion.Columnas.PRODUCTO;
        db = this.getReadableDatabase();
        cursor = db.rawQuery(selectQuery, new String[]{tipo, categoria, subcategoria, marca});
        //}

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                BaseRotacion p = new BaseRotacion();
                p.setProducto(cursor.getString(cursor.getColumnIndexOrThrow(ContractRotacion.Columnas.PRODUCTO)));
                operadores.add(p);
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public String getManufacturerPrecios(String categoria,String subcategoria, String brand, String descripcion) {
        String manufacturer = "";

        // Select All Query
        String selectQuery = "SELECT DISTINCT " + ContractPortafolioProductos.Columnas.FABRICANTE + " FROM " +
                ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE "+
                ContractPortafolioProductos.Columnas.SECTOR +"=? AND " +
        //        ContractPortafolioProductos.Columnas.CATEGORY +"=? AND " +
                ContractPortafolioProductos.Columnas.MARCA +"=? AND " +
                ContractPortafolioProductos.Columnas.SKU +"=?";
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{categoria,/* subcategoria, */brand,descripcion});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                manufacturer = cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.FABRICANTE));
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return manufacturer;
    }

    public BasePharmaValue getPdv(String codigo) {
        BasePharmaValue bpv = null;

        // Select All Query
        String selectQuery = "SELECT * FROM " + ContractPharmaValue.POS + " WHERE " + ContractPharmaValue.Columnas.POS_ID +"=?";
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{codigo});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                bpv = new BasePharmaValue();
                bpv.setChannel(cursor.getString(cursor.getColumnIndexOrThrow(ContractPharmaValue.Columnas.CHANNEL)));
                bpv.setSubchannel(cursor.getString(cursor.getColumnIndexOrThrow(ContractPharmaValue.Columnas.SUBCHANNEL)));
                bpv.setChannel_segment(cursor.getString(cursor.getColumnIndexOrThrow(ContractPharmaValue.Columnas.CHANNEL_SEGMENT)));
                bpv.setFormat(cursor.getString(cursor.getColumnIndexOrThrow(ContractPharmaValue.Columnas.FORMAT)));
                bpv.setCustomer_owner(cursor.getString(cursor.getColumnIndexOrThrow(ContractPharmaValue.Columnas.CUSTOMER_OWNER)));
                bpv.setPos_id(cursor.getString(cursor.getColumnIndexOrThrow(ContractPharmaValue.Columnas.POS_ID)));
                bpv.setPos_name(cursor.getString(cursor.getColumnIndexOrThrow(ContractPharmaValue.Columnas.POS_NAME)));
                bpv.setPos_name_dpsm(cursor.getString(cursor.getColumnIndexOrThrow(ContractPharmaValue.Columnas.POS_NAME_DPSM)));
                bpv.setZone(cursor.getString(cursor.getColumnIndexOrThrow(ContractPharmaValue.Columnas.ZONA)));
                bpv.setRegion(cursor.getString(cursor.getColumnIndexOrThrow(ContractPharmaValue.Columnas.REGION)));
                bpv.setProvince(cursor.getString(cursor.getColumnIndexOrThrow(ContractPharmaValue.Columnas.PROVINCIA)));
                bpv.setCity(cursor.getString(cursor.getColumnIndexOrThrow(ContractPharmaValue.Columnas.CIUDAD)));
                bpv.setAddress(cursor.getString(cursor.getColumnIndexOrThrow(ContractPharmaValue.Columnas.DIRECCION)));
                bpv.setKam(cursor.getString(cursor.getColumnIndexOrThrow(ContractPharmaValue.Columnas.KAM)));
                bpv.setSales_executive(cursor.getString(cursor.getColumnIndexOrThrow(ContractPharmaValue.Columnas.SALES_EXECUTIVE)));
                bpv.setMerchandising(cursor.getString(cursor.getColumnIndexOrThrow(ContractPharmaValue.Columnas.MERCHANDISING)));
                bpv.setSupervisor(cursor.getString(cursor.getColumnIndexOrThrow(ContractPharmaValue.Columnas.SUPERVISOR)));
                bpv.setMercaderista(cursor.getString(cursor.getColumnIndexOrThrow(ContractPharmaValue.Columnas.MERCADERISTA)));
                bpv.setUser(cursor.getString(cursor.getColumnIndexOrThrow(ContractPharmaValue.Columnas.USER)));
                bpv.setDpsm(cursor.getString(cursor.getColumnIndexOrThrow(ContractPharmaValue.Columnas.DPSM)));
                bpv.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(ContractPharmaValue.Columnas.STATUS)));
                bpv.setTipo(cursor.getString(cursor.getColumnIndexOrThrow(ContractPharmaValue.Columnas.TIPO)));
                bpv.setDistancia(cursor.getString(cursor.getColumnIndexOrThrow(ContractPharmaValue.Columnas.DISTANCIA)));
                bpv.setLatitud(cursor.getString(cursor.getColumnIndexOrThrow(ContractPharmaValue.Columnas.LATITUD)));
                bpv.setLongitud(cursor.getString(cursor.getColumnIndexOrThrow(ContractPharmaValue.Columnas.LONGITUD)));
                bpv.setFoto(cursor.getString(cursor.getColumnIndexOrThrow(ContractPharmaValue.Columnas.FOTO)));
                bpv.setSegmentacion(cursor.getString(cursor.getColumnIndexOrThrow(ContractPharmaValue.Columnas.SEGMENTACION)));
                bpv.setCompras(cursor.getString(cursor.getColumnIndexOrThrow(ContractPharmaValue.Columnas.COMPRAS)));
                bpv.setHora_inicio(cursor.getString(cursor.getColumnIndexOrThrow(ContractPharmaValue.Columnas.HORA_INICIO)));
                bpv.setHora_fin(cursor.getString(cursor.getColumnIndexOrThrow(ContractPharmaValue.Columnas.HORA_FIN)));
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return bpv;
    }

    public ArrayList<BasePortafolioProductos> filtrarListProductos2Valores(String categoria, String subcategoria, String brand,
                                                                           String codigo, String canal, String subcanal, String manufacturer) {
        ArrayList<BasePortafolioProductos> operadores = new ArrayList<>();

        Cursor cursor = null;
        String selectQuery = "";
        if (canal.equalsIgnoreCase("MAYORISTA")) {
            selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.SKU + ", " + ContractPortafolioProductos.Columnas.PVP + " FROM " +
                    ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " +
                    ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                    ContractPortafolioProductos.Columnas.CATEGORY + "=? AND " +
                    ContractPortafolioProductos.Columnas.MARCA + "=? AND " +
                    ContractPortafolioProductos.Columnas.FABRICANTE + " LIKE ?";
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{categoria, subcategoria, brand, "%" + manufacturer + "%"});
        }else{
            selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.SKU + ", " + ContractPortafolioProductos.Columnas.PVP + " FROM " +
                    ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " +
                    ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                    ContractPortafolioProductos.Columnas.CATEGORY + "=? AND " +
                    ContractPortafolioProductos.Columnas.MARCA + "=? AND " +
                    ContractPortafolioProductos.Columnas.CADENAS + " LIKE ? AND " +
                    ContractPortafolioProductos.Columnas.FABRICANTE + " LIKE ?";
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{categoria, subcategoria, brand, "%" + subcanal + "%", "%" + manufacturer + "%"});
        }

        String header = "HEADER FOR RECYCLERVIEW";

        operadores.add(new BasePortafolioProductos(header));
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                BasePortafolioProductos bpv = new BasePortafolioProductos();
                bpv.setSku(cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.SKU)));
                operadores.add(bpv);
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public ArrayList<BasePortafolioProductos> filtrarListSugProductos3(String categoria, String subcategoria, String brand, String contenido,
                                                                       String codigo, String canal, String subcanal, String manufacturer) {
        ArrayList<BasePortafolioProductos> operadores = new ArrayList<>();

        Cursor cursor = null;
        String selectQuery = "";
        if (canal.equalsIgnoreCase("MAYORISTA")) {
            selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.SEGMENTO + ", " + ContractPortafolioProductos.Columnas.PVP + " FROM " +
                    ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " +
                    ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                    ContractPortafolioProductos.Columnas.CATEGORY + "=? AND " +
                    ContractPortafolioProductos.Columnas.MARCA + "=? AND " +
                    ContractPortafolioProductos.Columnas.CONTENIDO + "=? AND " +
                    ContractPortafolioProductos.Columnas.FABRICANTE + " LIKE ?" +
                    " ORDER BY " + ContractPortafolioProductos.Columnas.SEGMENTO;
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{categoria, subcategoria, brand, contenido, "%"+manufacturer+"%"});
        }else{
            selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.SEGMENTO + ", " + ContractPortafolioProductos.Columnas.PVP + " FROM " +
                    ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " +
                    ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                    ContractPortafolioProductos.Columnas.CATEGORY + "=? AND " +
                    ContractPortafolioProductos.Columnas.MARCA + "=? AND " +
                    ContractPortafolioProductos.Columnas.CONTENIDO + "=? AND " +
                    ContractPortafolioProductos.Columnas.CADENAS + " LIKE ?" +
                    " ORDER BY " + ContractPortafolioProductos.Columnas.SKU;
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{categoria, subcategoria, brand, contenido, "%" + subcanal + "%"});
        }

    //    String header = "HEADER FOR RECYCLERVIEW";

    //    operadores.add(new BasePortafolioProductos(header));
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                BasePortafolioProductos bpv = new BasePortafolioProductos();
                bpv.setSku(cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.SEGMENTO)));
                bpv.setPvp(cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.PVP)));
                operadores.add(bpv);
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public ArrayList<BasePortafolioProductos> filtrarListSugProductos4(String categoria, String subcategoria, String brand, String contenido,
                                                                       String codigo, String canal, String subcanal) {
        ArrayList<BasePortafolioProductos> operadores = new ArrayList<>();

        Cursor cursor = null;
        String selectQuery = "";
//        if (canal.equalsIgnoreCase("MAYORISTA")) {
            selectQuery = "SELECT DISTINCT "+ ContractPrioritario.Columnas.SKU + " FROM " +
                    ContractPrioritario.PRIORITARIO + " WHERE " +
                    ContractPrioritario.Columnas.CATEGORIA + "=? AND " +
                    ContractPrioritario.Columnas.SUBCATEGORIA + "=? AND " +
                    ContractPrioritario.Columnas.MARCA + "=? AND " +
                    ContractPrioritario.Columnas.CONTENIDO + "=? AND " +
                    ContractPrioritario.Columnas.CODIGO_PDV + "=?" +
                    " ORDER BY " + ContractPrioritario.Columnas.SKU;
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{categoria, subcategoria, brand, contenido, codigo});
//        }else{
//            selectQuery = "SELECT DISTINCT "+ ContractPrioritario.Columnas.SKU + " FROM " +
//                    ContractPrioritario.PRIORITARIO + " WHERE " +
//                    ContractPrioritario.Columnas.CATEGORIA + "=? AND " +
//                    ContractPrioritario.Columnas.SUBCATEGORIA + "=? AND " +
//                    ContractPrioritario.Columnas.MARCA + "=? AND " +
//                    ContractPrioritario.Columnas.CONTENIDO + "=? AND " +
//                    ContractPrioritario.Columnas.CODIGO_PDV + "=?" +
//                    " ORDER BY " + ContractPrioritario.Columnas.SKU;
//            db = this.getReadableDatabase();
//            cursor = db.rawQuery(selectQuery, new String[]{categoria, subcategoria, brand, contenido, codigo});
//        }

        //    String header = "HEADER FOR RECYCLERVIEW";

        //    operadores.add(new BasePortafolioProductos(header));
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                BasePortafolioProductos bpv = new BasePortafolioProductos();
                bpv.setSku(cursor.getString(cursor.getColumnIndexOrThrow(ContractPrioritario.Columnas.SKU)));
                operadores.add(bpv);
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public ArrayList<BasePortafolioProductos> filtrarListSugProductos(String categoria, String subcategoria, String brand, String contenido,
                                                                      String codigo, String canal, String subcanal, String manufacturer) {
        ArrayList<BasePortafolioProductos> operadores = new ArrayList<>();

        Cursor cursor = null;
        String selectQuery = "";
        if (canal.equalsIgnoreCase("MAYORISTA")) {
            selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.SEGMENTO + ", " + ContractPortafolioProductos.Columnas.PVP + " FROM " +
                    ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " +
                    ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                    ContractPortafolioProductos.Columnas.CATEGORY + "=? AND " +
                    ContractPortafolioProductos.Columnas.MARCA + "=? AND " +
                    ContractPortafolioProductos.Columnas.CONTENIDO + "=? AND " +
                    ContractPortafolioProductos.Columnas.FABRICANTE + " LIKE ?" +
                    " ORDER BY " + ContractPortafolioProductos.Columnas.SEGMENTO;
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{categoria, subcategoria, brand, contenido, "%"+manufacturer+"%"});
        }else{
            selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.SEGMENTO + ", " + ContractPortafolioProductos.Columnas.PVP + " FROM " +
                    ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " +
                    ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                    ContractPortafolioProductos.Columnas.CATEGORY + "=? AND " +
                    ContractPortafolioProductos.Columnas.MARCA + "=? AND " +
                    ContractPortafolioProductos.Columnas.CONTENIDO + "=? AND " +
                    ContractPortafolioProductos.Columnas.CADENAS + " LIKE ?" +
                    " ORDER BY " + ContractPortafolioProductos.Columnas.SKU;
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{categoria, subcategoria, brand, contenido, "%" + subcanal + "%"});
        }

        String header = "HEADER FOR RECYCLERVIEW";

        operadores.add(new BasePortafolioProductos(header));
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                BasePortafolioProductos bpv = new BasePortafolioProductos();
                bpv.setSku(cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.SEGMENTO)));
                bpv.setPvp(cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.PVP)));
                operadores.add(bpv);
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public ArrayList<BasePortafolioProductos> filtrarListProductos(String categoria, String subcategoria, String brand,
                                                                   String codigo, String canal, String subcanal, String manufacturer) {
        ArrayList<BasePortafolioProductos> operadores = new ArrayList<>();

        Log.i("CADENA", subcanal);
        Cursor cursor = null;
        String selectQuery = "";
        if (canal.equalsIgnoreCase("AUTOSERVICIO")) {
            selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.SKU + ", " + ContractPortafolioProductos.Columnas.PVP + " FROM " +
                    ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " +
                    ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                    ContractPortafolioProductos.Columnas.CATEGORY + "=? AND " +
                    ContractPortafolioProductos.Columnas.MARCA + "=? AND " +
                    ContractPortafolioProductos.Columnas.CADENAS + " LIKE ? AND " +
                    ContractPortafolioProductos.Columnas.FABRICANTE + " LIKE ?" +
                    " ORDER BY " + ContractPortafolioProductos.Columnas.SKU;
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{categoria, subcategoria, brand, "%" + subcanal + "%", "%"+manufacturer+"%"});
        }else{
            selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.SKU + ", " + ContractPortafolioProductos.Columnas.PVP + " FROM " +
                    ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " +
                    ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                    ContractPortafolioProductos.Columnas.CATEGORY + "=? AND " +
                    ContractPortafolioProductos.Columnas.MARCA + "=? AND " +
                    ContractPortafolioProductos.Columnas.FABRICANTE + " LIKE ?" +
                    " ORDER BY " + ContractPortafolioProductos.Columnas.SKU;
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{categoria, subcategoria, brand, "%"+manufacturer+"%"});
        }

//        String header = "HEADER FOR RECYCLERVIEW";
//
//        operadores.add(new BasePortafolioProductos(header));
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                BasePortafolioProductos bpv = new BasePortafolioProductos();
                bpv.setSku(cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.SKU)));
                bpv.setPvp(cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.PVP)));
                operadores.add(bpv);
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public ArrayList<BasePortafolioProductos> filtrarListProductosSinBrand(String categoria, String subcategoria, String producto,
                                                                           String codigo, String canal, String subcanal, String manufacturer) {
        ArrayList<BasePortafolioProductos> operadores = new ArrayList<>();

        Cursor cursor = null;
        String selectQuery = "";
        if (canal.equalsIgnoreCase("MAYORISTA")) {
            selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.FOTO + ", " + ContractPortafolioProductos.Columnas.PVP + " FROM " +
                    ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " +
                    ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                    ContractPortafolioProductos.Columnas.CATEGORY + "=? AND " +
                    ContractPortafolioProductos.Columnas.SKU + "=? AND " +
                    ContractPortafolioProductos.Columnas.FABRICANTE + " LIKE ?" +
                    " ORDER BY " + ContractPortafolioProductos.Columnas.SKU;
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{categoria, subcategoria, producto, "%"+manufacturer+"%"});
        }else{
            selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.FOTO + ", " + ContractPortafolioProductos.Columnas.PVP + " FROM " +
                    ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " +
                    ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                    ContractPortafolioProductos.Columnas.CATEGORY + "=? AND " +
                    ContractPortafolioProductos.Columnas.SKU + "=? AND " +
                    ContractPortafolioProductos.Columnas.CADENAS + " LIKE ?" +
                    " ORDER BY " + ContractPortafolioProductos.Columnas.SKU;
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{categoria, subcategoria, "%" + subcanal + "%"});
        }

        String header = "HEADER FOR RECYCLERVIEW";

        operadores.add(new BasePortafolioProductos(header));
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                BasePortafolioProductos bpv = new BasePortafolioProductos();
                bpv.setFoto(cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.FOTO)));
                bpv.setPvp(cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.PVP)));
                operadores.add(bpv);
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

 /*   public ArrayList<BasePortafolioProductos> filtrarListProductosSugeridos(String categoria, String subcategoria, String producto,
                                                                             String codigo, String canal, String subcanal, String manufacturer) {
        ArrayList<BasePortafolioProductos> operadores = new ArrayList<>();

        Cursor cursor = null;
        String selectQuery = "";
        if (canal.equalsIgnoreCase("MAYORISTA")) {
            selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.FOTO + ", " + ContractPortafolioProductos.Columnas.PVP + " FROM " +
                    ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " +
                    ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                    ContractPortafolioProductos.Columnas.CATEGORY + "=? AND " +
                    ContractPortafolioProductos.Columnas.SEGMENTO + "=? AND " +
                    ContractPortafolioProductos.Columnas.FABRICANTE + " LIKE ?" +
                    " ORDER BY " + ContractPortafolioProductos.Columnas.SEGMENTO;
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{categoria, subcategoria, producto, "%"+manufacturer+"%"});
        }else{
            selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.FOTO + ", " + ContractPortafolioProductos.Columnas.PVP + " FROM " +
                    ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " +
                    ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                    ContractPortafolioProductos.Columnas.CATEGORY + "=? AND " +
                    ContractPortafolioProductos.Columnas.SEGMENTO + "=? AND " +
                    ContractPortafolioProductos.Columnas.CADENAS + " LIKE ?" +
                    " ORDER BY " + ContractPortafolioProductos.Columnas.SEGMENTO;
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{categoria, subcategoria, "%" + subcanal + "%"});
        }

        String header = "HEADER FOR RECYCLERVIEW";

        operadores.add(new BasePortafolioProductos(header));
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                BasePortafolioProductos bpv = new BasePortafolioProductos();
                bpv.setFoto(cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.FOTO)));
                bpv.setPvp(cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.PVP)));
                operadores.add(bpv);
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }*/

    public ArrayList<BasePortafolioProductos> filtrarListProductosSugeridos(String categoria, String subcategoria, String brand, String contenido,
                                                                            String codigo, String canal, String subcanal, String manufacturer) {
        ArrayList<BasePortafolioProductos> operadores = new ArrayList<>();

        Cursor cursor = null;
        String selectQuery = "";
        if (canal.equalsIgnoreCase("MAYORISTA")) {
            selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.FOTO + ", " + ContractPortafolioProductos.Columnas.PVP + " FROM " +
                    ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " +
                    ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                    ContractPortafolioProductos.Columnas.CATEGORY + "=? AND " +
                    ContractPortafolioProductos.Columnas.MARCA + "=? AND " +
                    ContractPortafolioProductos.Columnas.CONTENIDO + "=? AND " +
                    ContractPortafolioProductos.Columnas.FABRICANTE + " LIKE ?" +
                    " ORDER BY " + ContractPortafolioProductos.Columnas.CONTENIDO;
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{categoria, subcategoria, brand, contenido, "%"+manufacturer+"%"});
        }else{
            selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.FOTO + ", " + ContractPortafolioProductos.Columnas.PVP + " FROM " +
                    ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " +
                    ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                    ContractPortafolioProductos.Columnas.CATEGORY + "=? AND " +
                    ContractPortafolioProductos.Columnas.MARCA + "=? AND " +
                    ContractPortafolioProductos.Columnas.CONTENIDO + "=? AND " +
                    ContractPortafolioProductos.Columnas.CADENAS + " LIKE ?" +
                    " ORDER BY " + ContractPortafolioProductos.Columnas.CONTENIDO;
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{categoria, subcategoria, brand, contenido, "%" + subcanal + "%"});
        }

        String header = "HEADER FOR RECYCLERVIEW";

        operadores.add(new BasePortafolioProductos(header));
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                BasePortafolioProductos bpv = new BasePortafolioProductos();
                bpv.setFoto(cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.FOTO)));
                bpv.setPvp(cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.PVP)));
                operadores.add(bpv);
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public ArrayList<BasePortafolioProductos> filtrarListProductosCodificanValores(String categoria, String subcategoria, String brand,
                                                                                   String codigo, String canal, String subcanal, String manufacturer) {
        ArrayList<BasePortafolioProductos> operadores = new ArrayList<BasePortafolioProductos>();

        Log.i("CATEGORIA", categoria);
        Log.i("SUBCATEGORIA", subcategoria);
        Log.i("MARCA", brand);
        Log.i("CANAL", canal);
        Log.i("SUBCANAL", subcanal);
        Log.i("FABRICANTE", manufacturer);

        Cursor cursor = null;
        String selectQuery = "";
        if (canal.equalsIgnoreCase("MAYORISTA")) {
            selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.SKU + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " +
                    ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                    ContractPortafolioProductos.Columnas.CATEGORY + "=? AND " +
                    ContractPortafolioProductos.Columnas.MARCA + "=? AND " +
                    ContractPortafolioProductos.Columnas.FABRICANTE + " LIKE ? " +
                    " ORDER BY " + ContractPortafolioProductos.Columnas.SKU;
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{categoria, subcategoria, brand, "%" + manufacturer + "%"});
            Log.i("MAYORISTA", "MAYORISTA");
        }else if (canal.equalsIgnoreCase("AUTOSERVICIO")) {
            selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.SKU + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " +
                    ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                    ContractPortafolioProductos.Columnas.CATEGORY + "=? AND " +
                    ContractPortafolioProductos.Columnas.MARCA + "=? AND " +
                    ContractPortafolioProductos.Columnas.FABRICANTE + " LIKE ? AND " +
                    ContractPortafolioProductos.Columnas.CADENAS + " LIKE ?" +
                    " ORDER BY " + ContractPortafolioProductos.Columnas.SKU;
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{categoria, subcategoria, brand, "%" + manufacturer + "%", "%" + subcanal + "%"});
            Log.i("AASS", "AASS");
        }else{
            selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.SKU + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " +
                    ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                    ContractPortafolioProductos.Columnas.CATEGORY + "=? AND " +
                    ContractPortafolioProductos.Columnas.MARCA + "=? AND " +
                    ContractPortafolioProductos.Columnas.FABRICANTE + " LIKE ?" +
                    " ORDER BY " + ContractPortafolioProductos.Columnas.SKU;
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{categoria, subcategoria, brand, "%" + manufacturer + "%"});
            Log.i("NA", "NA");
        }

//        String header = "HEADER FOR RECYCLERVIEW";
//        operadores.add(new BasePortafolioProductos(header));
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                BasePortafolioProductos bpv = new BasePortafolioProductos();
                bpv.setSku(cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.SKU)));
                operadores.add(bpv);
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public ArrayList<BasePortafolioProductos> filtrarListProductosCodificanValoresSinBrand(String categoria, String subcategoria,
                                                                                           String codigo, String canal, String subcanal, String manufacturer) {
        ArrayList<BasePortafolioProductos> operadores = new ArrayList<BasePortafolioProductos>();

        Log.i("CATEGORIA", categoria);
        Log.i("SUBCATEGORIA", subcategoria);
        Log.i("CANAL", canal);
        Log.i("SUBCANAL", subcanal);
        Log.i("FABRICANTE", manufacturer);

        Cursor cursor = null;
        String selectQuery = "";
        if (canal.equalsIgnoreCase("MAYORISTA")) {
            selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.SKU + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " +
                    ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                    ContractPortafolioProductos.Columnas.CATEGORY + "=? AND " +
                    ContractPortafolioProductos.Columnas.FABRICANTE + " LIKE ? " +
                    " ORDER BY " + ContractPortafolioProductos.Columnas.SKU;
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{categoria, subcategoria, "%" + manufacturer + "%"});
            Log.i("MAYORISTA", "MAYORISTA");
        }else if (canal.equalsIgnoreCase("AUTOSERVICIO")) {
            selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.SKU + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " +
                    ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                    ContractPortafolioProductos.Columnas.CATEGORY + "=? AND " +
                    ContractPortafolioProductos.Columnas.FABRICANTE + " LIKE ? AND " +
                    ContractPortafolioProductos.Columnas.CADENAS + " LIKE ?" +
                    " ORDER BY " + ContractPortafolioProductos.Columnas.SKU;
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{categoria, subcategoria, "%" + manufacturer + "%", "%" + subcanal + "%"});
            Log.i("AASS", "AASS");
        }else{
            selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.SKU + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " +
                    ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                    ContractPortafolioProductos.Columnas.CATEGORY + "=? AND " +
                    ContractPortafolioProductos.Columnas.FABRICANTE + " LIKE ?" +
                    " ORDER BY " + ContractPortafolioProductos.Columnas.SKU;
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{categoria, subcategoria, "%" + manufacturer + "%"});
            Log.i("NA", "NA");
        }

//        String header = "HEADER FOR RECYCLERVIEW";
//        operadores.add(new BasePortafolioProductos(header));
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                BasePortafolioProductos bpv = new BasePortafolioProductos();
                bpv.setSku(cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.SKU)));
                operadores.add(bpv);
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public ArrayList<BasePortafolioProductos> filtrarListProductosPreciosValores(String categoria, String subcategoria, String brand,
                                                                                 String codigo, String canal, String subcanal, String manufacturer) {
        ArrayList<BasePortafolioProductos> operadores = new ArrayList<BasePortafolioProductos>();

        Log.i("CATEGORIA", categoria);
        Log.i("SUBCATEGORIA", subcategoria);
        Log.i("MARCA", brand);
        Log.i("CANAL", canal);
        Log.i("SUBCANAL", subcanal);
        Log.i("FABRICANTE", manufacturer);

        Cursor cursor = null;
        String selectQuery = "";
        if (canal.equalsIgnoreCase("MAYORISTA")) {
            selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.SKU + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " +
                    ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                    ContractPortafolioProductos.Columnas.CATEGORY + "=? AND " +
                    ContractPortafolioProductos.Columnas.MARCA + "=? " +
                    " ORDER BY " + ContractPortafolioProductos.Columnas.SKU;
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{categoria, subcategoria, brand});
            Log.i("MAYORISTA", "MAYORISTA");
        }else if (canal.equalsIgnoreCase("AUTOSERVICIO")) {
            selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.SKU + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " +
                    ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                    ContractPortafolioProductos.Columnas.CATEGORY + "=? AND " +
                    ContractPortafolioProductos.Columnas.MARCA + "=? AND (" +
                    ContractPortafolioProductos.Columnas.CADENAS + " LIKE ? OR " +
                    ContractPortafolioProductos.Columnas.CADENAS + " REGEXP 'N/A')" +
                    " ORDER BY " + ContractPortafolioProductos.Columnas.SKU;
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{categoria, subcategoria, brand, "%" + subcanal + "%"});
            Log.i("AASS", "AASS");
        }else{
            selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.SKU + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " +
                    ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                    ContractPortafolioProductos.Columnas.CATEGORY + "=? AND " +
                    ContractPortafolioProductos.Columnas.MARCA + "=? " +
                    " ORDER BY " + ContractPortafolioProductos.Columnas.SKU;
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{categoria, subcategoria, brand});
            Log.i("NA", "NA");
        }

//        String header = "HEADER FOR RECYCLERVIEW";
//        operadores.add(new BasePortafolioProductos(header));
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                BasePortafolioProductos bpv = new BasePortafolioProductos();
                bpv.setSku(cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.SKU)));
                operadores.add(bpv);
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public ArrayList<BasePortafolioProductos> filtrarListProductosPreciosValoresSinBrand(String categoria, String subcategoria,
                                                                                         String codigo, String canal, String subcanal, String manufacturer) {
        ArrayList<BasePortafolioProductos> operadores = new ArrayList<BasePortafolioProductos>();

        Log.i("CATEGORIA", categoria);
        Log.i("SUBCATEGORIA", subcategoria);
        Log.i("CANAL", canal);
        Log.i("SUBCANAL", subcanal);
        Log.i("FABRICANTE", manufacturer);

        Cursor cursor = null;
        String selectQuery = "";
        if (canal.equalsIgnoreCase("MAYORISTA")) {
            selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.SKU + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " +
                    ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                    ContractPortafolioProductos.Columnas.CATEGORY + "=? " +
                    " ORDER BY " + ContractPortafolioProductos.Columnas.SKU;
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{categoria, subcategoria});
            Log.i("MAYORISTA", "MAYORISTA");
        }else if (canal.equalsIgnoreCase("AUTOSERVICIO")) {
            selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.SKU + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " +
                    ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                    ContractPortafolioProductos.Columnas.CATEGORY + "=? AND (" +
                    ContractPortafolioProductos.Columnas.CADENAS + " LIKE ? OR " +
                    ContractPortafolioProductos.Columnas.CADENAS + " REGEXP 'N/A')" +
                    " ORDER BY " + ContractPortafolioProductos.Columnas.SKU;
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{categoria, subcategoria, "%" + subcanal + "%"});
            Log.i("AASS", "AASS");
        }else{
            selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.SKU + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " +
                    ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                    ContractPortafolioProductos.Columnas.CATEGORY + "=? " +
                    " ORDER BY " + ContractPortafolioProductos.Columnas.SKU;
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{categoria, subcategoria});
            Log.i("NA", "NA");
        }

//        String header = "HEADER FOR RECYCLERVIEW";
//        operadores.add(new BasePortafolioProductos(header));
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                BasePortafolioProductos bpv = new BasePortafolioProductos();
                bpv.setSku(cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.SKU)));
                operadores.add(bpv);
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public List<String> filtrarListDescripcion(String marca, String categoria, String segmento1, String segmento2, String brand, String tamano, String cantidad) {
        List<String> operadores = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT DISTINCT " + ContractPortafolioProductos.Columnas.SKU + " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE "+
                ContractPortafolioProductos.Columnas.SECTOR +"=? AND " +
                ContractPortafolioProductos.Columnas.CATEGORY +"=? AND " +
                ContractPortafolioProductos.Columnas.SUBCATEGORIA +"=? AND " +
                ContractPortafolioProductos.Columnas.SEGMENTO +"=? AND " +
                ContractPortafolioProductos.Columnas.CONTENIDO +"=? AND " +
                ContractPortafolioProductos.Columnas.MARCA +"=? AND " +
                ContractPortafolioProductos.Columnas.FABRICANTE +" LIKE ?";
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{marca,categoria,segmento1,segmento2,brand,tamano,cantidad});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.SKU)));
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public List<String> filtrarListShare(String categoria, String subcategoria) {
        List<String> operadores = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT DISTINCT " + ContractPrecios.Columnas.MARCA + " FROM " + ContractPrecios.PRECIOS + " WHERE "+
                ContractPrecios.Columnas.CATEGORIA +"=? AND " +
                ContractPrecios.Columnas.SUBCATEGORIA +"=? ";
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{categoria,subcategoria});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractPrecios.Columnas.MARCA)));
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }


    public List<String> filtrarList(String marca, String categoria, String tipo) {
        List<String> operadores = new ArrayList<String>();

        // Select All Query
     String selectQuery = "SELECT DISTINCT "+ ContractPrecios.Columnas.PRODUCTO + " FROM " + ContractPrecios.PRECIOS + " WHERE "+
                ContractPrecios.Columnas.MARCA +"=? AND " +
                ContractPrecios.Columnas.CATEGORIA +"=? AND " +
                ContractPrecios.Columnas.SUBCATEGORIA +"=?";
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{marca,categoria,tipo});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractPrecios.Columnas.PRODUCTO)));
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public ArrayList<String> filtrarListMecanicasRotacion(String producto, String categoria) {
        ArrayList<String> operadores = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT DISTINCT "+ ContractRotacion.Columnas.MECANICA + " FROM " + ContractRotacion.ROTACION + " WHERE "+
                ContractRotacion.Columnas.PRODUCTO +"=? AND " +
                ContractRotacion.Columnas.CATEGORIA +"=?";
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{producto, categoria});

        operadores.add("Seleccione");
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractRotacion.Columnas.MECANICA)));
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    //HISTORIAL
    public List<String> getModulo(String basedatos, int dimension) {
        List<String> contenido = new ArrayList<String>();
        StringBuilder sb = new StringBuilder();
        String strdata, estado;

        // Select All Query
        String selectQuery = "SELECT  * FROM " + basedatos;
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);
//        int i=2;


        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            if (cursor.getString(cursor.getColumnIndexOrThrow(Constantes.PENDIENTE_INSERCION)).equals("1")) {
                estado = "No Enviado";
            }else{
                estado = "Enviado";
            }
            sb.append(estado);
            for(int i=2; i<dimension;i++) {
                String prefix = " | ";
                sb.append(prefix);
                sb.append(cursor.getString(i));
            }
            strdata = sb.toString();
            sb.setLength(0);
            contenido.add(strdata);
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return contenido;
    }

    public String getSurpervisor(){
        String fecha = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String supervisor = "";
        String query = "SELECT " + ContractPharmaValue.Columnas.SUPERVISOR + " FROM " + ContractPharmaValue.POS + /* " WHERE " +
                ContractPharmaValue.Columnas.FECHA_VISITA +" =? */ " GROUP BY " + ContractPharmaValue.Columnas.SUPERVISOR + " LIMIT 1";
        db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery(query, new String[]{/*fecha*/});

        if(cursor != null && cursor.moveToFirst()){
            supervisor = cursor.getString(cursor.getColumnIndexOrThrow(ContractPharmaValue.Columnas.SUPERVISOR));
        }

        cursor.close();
        db.close();
        return supervisor;
    }

    public List<String> getNombreCom(String codigo) {
        List<String> operadores = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + ContractPharmaValue.POS + " WHERE "+
                ContractPharmaValue.Columnas.POS_ID + "=? "+
                "GROUP BY " + ContractPharmaValue.Columnas.POS_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{codigo});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractPharmaValue.Columnas.POS_NAME)));
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public boolean SKUDuplicadoPrecios(String sku_code) {
        // Select All Query
        String selectQuery = "SELECT  * FROM " + ContractInsertPreciosSesion.INSERT_PRECIOS_SESION + " WHERE "+
                ContractInsertPreciosSesion.Columnas.SKU_CODE + "=? "+
                "GROUP BY " + ContractInsertPreciosSesion.Columnas.SKU_CODE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{sku_code});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            return true;
        }
        // closing connection
        cursor.close();
        db.close();

        return false;
    }

    public boolean registroDuplicadoLogisticoRelevo(String skuCode, String cantidad, String causal, String tipoLogistico) {
        String selectQuery = "SELECT COUNT(*) FROM " +
                ContractInsertLogisticoRelevo.INSERT_LOGISTICO_RELEVO +
                " WHERE " + ContractInsertLogisticoRelevo.Columnas.SKU_CODE + "=? AND " +
                ContractInsertLogisticoRelevo.Columnas.PREGULAR + "=? AND " +
                ContractInsertLogisticoRelevo.Columnas.CAUSAL + "=? AND " +
                ContractInsertLogisticoRelevo.Columnas.TIPO_LOGISTICO + "=?";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{skuCode, cantidad, causal, tipoLogistico});

        boolean existeDuplicado = false;
        if (cursor.moveToFirst()) {
            int count = cursor.getInt(0);
            existeDuplicado = count > 0;
        }

        cursor.close();
        db.close();

        return existeDuplicado;
    }

    public String getHoraInicioJornada(String usuario){
        String hora_ini = "";

        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-5"));
        Date currentLocalTime = cal.getTime();
        DateFormat date = new SimpleDateFormat("yyyy-MM-dd");
        String fecha_actual = date.format(currentLocalTime);



        // Select All Query
        String selectQuery = "SELECT " + ContractPharmaValue.Columnas.HORA_INICIO + " FROM " + ContractPharmaValue.POS + " WHERE "+
                ContractPharmaValue.Columnas.USER + "=? AND " + ContractPharmaValue.Columnas.FECHA_VISITA
                + "=? ORDER BY " + ContractPharmaValue.Columnas.HORA_INICIO + " ASC LIMIT 1";

        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{usuario,fecha_actual});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                hora_ini = cursor.getString(cursor.getColumnIndexOrThrow(ContractPharmaValue.Columnas.HORA_INICIO));
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables

        return hora_ini ;
    }


    public String getHoraFinJornada(String usuario){
        String hora_fin = "";

        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-5"));
        Date currentLocalTime = cal.getTime();
        DateFormat date = new SimpleDateFormat("yyyy-MM-dd");
        String fecha_actual = date.format(currentLocalTime);


        // Select All Query
        String selectQuery = "SELECT " + ContractPharmaValue.Columnas.HORA_FIN + " FROM " + ContractPharmaValue.POS + " WHERE "+
                ContractPharmaValue.Columnas.USER + "=? AND " + ContractPharmaValue.Columnas.FECHA_VISITA
                + "=? ORDER BY " + ContractPharmaValue.Columnas.HORA_FIN + " DESC LIMIT 1";

        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{usuario,fecha_actual});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                hora_fin = cursor.getString(cursor.getColumnIndexOrThrow(ContractPharmaValue.Columnas.HORA_FIN));
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables

        return hora_fin ;
    }




    public void eliminarSKUDuplicadoPrecios(String sku_code) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(ContractInsertPreciosSesion.INSERT_PRECIOS_SESION, ContractInsertPreciosSesion.Columnas.SKU_CODE + "='" + sku_code + "'", null);
        db.close();
    }

    public ArrayList<Precio> getListGuardadoPrecios(String codigo) {
        ArrayList<Precio> operadores = new ArrayList<Precio>();

        String selectQuery = "SELECT " + ContractInsertPrecios.Columnas.SKU_CODE + ", " +
                ContractInsertPrecios.Columnas.PREGULAR + ", " +
                ContractInsertPrecios.Columnas.PPROMOCION +
                " FROM " + ContractInsertPrecios.INSERT_PRECIOS +
                " WHERE " + ContractInsertPrecios.Columnas.CODIGO + " =?" +
                " GROUP BY " + ContractInsertPrecios.Columnas.SKU_CODE +
                " ORDER BY " + ContractInsertPrecios.Columnas.SKU_CODE + " DESC";;
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{codigo});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Precio p = new Precio();
                p.setSku_code(cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertPreciosSesion.Columnas.SKU_CODE)));
                p.setPvp(cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertPreciosSesion.Columnas.PREGULAR)));
                p.setPvc(cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertPreciosSesion.Columnas.PPROMOCION)));
                operadores.add(p);
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public ArrayList<Precio> getListGuardadoPrecios2(String codigo) {
        ArrayList<Precio> operadores = new ArrayList<Precio>();

        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-5"));
        cal.set(Calendar.DAY_OF_MONTH,1);
        DateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        formato.setTimeZone(TimeZone.getTimeZone("GMT-5"));
        // Obtenemos la fecha inicio del mes
        String fecha_inicio_mes = formato.format(cal.getTime());
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.DATE,31);
        formato.setTimeZone(TimeZone.getTimeZone("GMT-5"));
        // Obtenemos la fecha final del mes
        String  fecha_fin_mes = formato.format(cal.getTime());



        String selectQuery = "SELECT " + ContractInsertPrecios.Columnas.SKU_CODE +
                " FROM " + ContractInsertPrecios.INSERT_PRECIOS +
                " WHERE " + ContractInsertPrecios.Columnas.CODIGO + " =?"+
                // Formateamos la fecha de dd/mm/yyyy a yyyy-mm-dd
                " AND datetime(substr(fecha, 7, 4) || '-' || substr(fecha, 4, 2) || '-' || substr(fecha, 1, 2))" +
                " BETWEEN ? AND ? " +
                " GROUP BY " + ContractInsertPrecios.Columnas.SKU_CODE +
                " ORDER BY " + ContractInsertPrecios.Columnas.SKU_CODE + " DESC";
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{codigo,fecha_inicio_mes,fecha_fin_mes});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Precio p = new Precio();
                p.setSku_code(cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertPrecios.Columnas.SKU_CODE)));
                operadores.add(p);
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

//    public ArrayList<InsertTareas> getListGuardadoTareas(String codigo, String user, String periodo) {
//        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-5"));
//        Date currentLocalTime = cal.getTime();
//        DateFormat date = new SimpleDateFormat("dd/MM/yyy");
//        date.setTimeZone(TimeZone.getTimeZone("GMT-5"));
//        String fecha_actual = date.format(currentLocalTime);
//        String mes_actual = fecha_actual.substring(3);
//
//        Log.i("FECHAS", "FECHA ACTUAL: " + fecha_actual + " - " + "MES ACTUAL: " + mes_actual);
//
//        ArrayList<InsertTareas> operadores = new ArrayList<InsertTareas>();
//
//        String selectQuery = "";
//        Cursor cursor = null;
//        if (periodo.equalsIgnoreCase("DIARIO")) {
//            selectQuery = "SELECT * FROM " + ContractInsertTareas.INSERT_TAREAS +
//                    " WHERE " + ContractInsertTareas.Columnas.CODIGO + " =? AND " +
//                    ContractInsertTareas.Columnas.MERCADERISTA + "=? AND " +
//                    ContractInsertTareas.Columnas.FECHA + "=? AND " +
//                    ContractInsertTareas.Columnas.REALIZADO + "='SI'";
//            db = this.getReadableDatabase();
//            cursor = db.rawQuery(selectQuery, new String[]{codigo, user, fecha_actual});
//        } else if (periodo.equalsIgnoreCase("MENSUAL")) {
//            selectQuery = "SELECT * FROM " + ContractInsertTareas.INSERT_TAREAS +
//                    " WHERE " + ContractInsertTareas.Columnas.CODIGO + " =? AND " +
//                    ContractInsertTareas.Columnas.MERCADERISTA + "=? AND " +
//                    ContractInsertTareas.Columnas.FECHA + "=? AND " +
//                    ContractInsertTareas.Columnas.REALIZADO + "='SI'";
//            db = this.getReadableDatabase();
//            cursor = db.rawQuery(selectQuery, new String[]{codigo, user, mes_actual});
//        }
//        // looping through all rows and adding to list
//        if (cursor.moveToFirst()) {
//            do {
//                InsertTareas p = new InsertTareas();
//                p.setTareas(cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertTareas.Columnas.TAREAS)));
//                p.setRealizado("REALIZADO");
//                operadores.add(p);
//            } while (cursor.moveToNext());
//        }
//        // closing connection
//        cursor.close();
//        db.close();
//        // returning lables
//        return operadores;
//    }

    public String getEstadoTarea(String codigo, String user, String periodo, String tarea) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-5"));
        Date currentLocalTime = cal.getTime();
        DateFormat date = new SimpleDateFormat("dd/MM/yyy");
        date.setTimeZone(TimeZone.getTimeZone("GMT-5"));
        String fecha_actual = date.format(currentLocalTime);
        String mes_actual = fecha_actual.substring(3);

        Log.i("FECHAS", "PERIODO: " + periodo + " - FECHA ACTUAL: " + fecha_actual + " - MES ACTUAL: " + mes_actual);

        String operadores = "PENDIENTE";
        String selectQuery = "";
        Cursor cursor = null;

        if (periodo.equalsIgnoreCase("DIARIO")) {
            selectQuery = "SELECT * FROM " + ContractInsertTareas.INSERT_TAREAS +
                    " WHERE " + ContractInsertTareas.Columnas.CODIGO + " =? AND " +
                    ContractInsertTareas.Columnas.MERCADERISTA + "=? AND " +
                    ContractInsertTareas.Columnas.FECHA + "=? AND " +
                    ContractInsertTareas.Columnas.TAREAS + "=? AND " +
                    ContractInsertTareas.Columnas.REALIZADO + "='SI'";
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{codigo, user, fecha_actual, tarea});
        } else if (periodo.equalsIgnoreCase("MENSUAL")) {
            selectQuery = "SELECT * FROM " + ContractInsertTareas.INSERT_TAREAS +
                    " WHERE " + ContractInsertTareas.Columnas.CODIGO + " =? AND " +
                    ContractInsertTareas.Columnas.MERCADERISTA + "=? AND " +
                    ContractInsertTareas.Columnas.FECHA + "=? AND " +
                    ContractInsertTareas.Columnas.TAREAS + "=? AND " +
                    ContractInsertTareas.Columnas.REALIZADO + "='SI'";
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{codigo, user, mes_actual, tarea});
        }
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores = "REALIZADO";
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public ArrayList<InsertOsa> getListGuardadoOSA(String codigo,String user) {
        ArrayList<InsertOsa> operadores = new ArrayList<InsertOsa>();

        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-5"));
        cal.set(Calendar.DAY_OF_MONTH,1);
        DateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        formato.setTimeZone(TimeZone.getTimeZone("GMT-5"));
        // Obtenemos la fecha inicio del mes
        String fecha_inicio_mes = formato.format(cal.getTime());
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.DATE,31);
        formato.setTimeZone(TimeZone.getTimeZone("GMT-5"));
        // Obtenemos la fecha final del mes
        String  fecha_fin_mes = formato.format(cal.getTime());

        String selectQuery = "SELECT * FROM " + ContractInsertValores.INSERT_VALORES +
                " WHERE " + ContractInsertValores.Columnas.CODIGO + " =?" +
                " AND " + ContractInsertValores.Columnas.USUARIO + " =?" +
                " AND datetime(substr(fecha, 7, 4) || '-' || substr(fecha, 4, 2) || '-' || substr(fecha, 1, 2))" +
                " BETWEEN ? AND ? " +
                " GROUP BY " + ContractInsertValores.Columnas.SKU_CODE +
                " ORDER BY " + ContractInsertValores.Columnas.SKU_CODE + " DESC";
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{codigo,user,fecha_inicio_mes,fecha_fin_mes});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                InsertOsa p = new InsertOsa();
                p.setSku(cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertValores.Columnas.SKU_CODE)));
                p.setSugerido(cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertValores.Columnas.SUGERIDO)));
                operadores.add(p);
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public ArrayList<Base_Inventario> getListGuardadoInventario(String codigo) {
        ArrayList<Base_Inventario> operadores = new ArrayList<Base_Inventario>();


//        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-5"));
//        cal.set(Calendar.DAY_OF_MONTH,1);
//        DateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
//        formato.setTimeZone(TimeZone.getTimeZone("GMT-5"));
//        // Obtenemos la fecha inicio del mes
//        String fecha_inicio_mes = formato.format(cal.getTime());
//        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
//        cal.set(Calendar.DATE,31);
//        formato.setTimeZone(TimeZone.getTimeZone("GMT-5"));
//        // Obtenemos la fecha final del mes
//        String  fecha_fin_mes = formato.format(cal.getTime());

        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-5"));
        Date currentLocalTime = cal.getTime();
        DateFormat date = new SimpleDateFormat("MM/yyy");
        date.setTimeZone(TimeZone.getTimeZone("GMT-5"));
        String fechaser = date.format(currentLocalTime);

        Log.i("fechaser",""+fechaser);


        String selectQuery = "SELECT " + InsertFlooring.Columnas.SKU_CODE +
                " FROM " + InsertFlooring.INSERT_FLOORING +
                " WHERE " + InsertFlooring.Columnas.CODIGO + " =? AND " +
                InsertFlooring.Columnas.FECHA + " LIKE ? ";
                // Formateamos la fecha de dd/mm/yyyy a yyyy-mm-dd
              //  " AND datetime(substr(fecha, 7, 4) || '-' || substr(fecha, 4, 2) || '-' || substr(fecha, 1, 2))" +
              //  " BETWEEN ? AND ? ";

        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{codigo,"%" + fechaser + "%"});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Base_Inventario p = new Base_Inventario();
                p.setSku(cursor.getString(cursor.getColumnIndexOrThrow(InsertFlooring.Columnas.SKU_CODE)));
                operadores.add(p);
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public ArrayList<Flooring> getSkuRelevadosInventario(String codigo, String categoria, String subCategoria, String marca) {
        ArrayList<Flooring> operadores = new ArrayList<Flooring>();


        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-5"));
        cal.set(Calendar.DAY_OF_MONTH,1);
        DateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        formato.setTimeZone(TimeZone.getTimeZone("GMT-5"));
        // Obtenemos la fecha inicio del mes
        String fecha_inicio_mes = formato.format(cal.getTime());
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.DATE,31);
        formato.setTimeZone(TimeZone.getTimeZone("GMT-5"));
        // Obtenemos la fecha final del mes
        String  fecha_fin_mes = formato.format(cal.getTime());





        String selectQuery = "SELECT DISTINCT " + InsertFlooring.Columnas.SKU_CODE + " , " +
                InsertFlooring.Columnas.SEMANA +
                " FROM " + InsertFlooring.INSERT_FLOORING +
                " WHERE " + InsertFlooring.Columnas.CODIGO + " =?" +
                        " AND " + InsertFlooring.Columnas.CATEGORIA + "=?" +
                        " AND " + InsertFlooring.Columnas.SUBCATEGORIA + "=?" +
                        " AND " + InsertFlooring.Columnas.BRAND + "=?" +
                // Formateamos la fecha de dd/mm/yyyy a yyyy-mm-dd
                " AND datetime(substr(fecha, 7, 4) || '-' || substr(fecha, 4, 2) || '-' || substr(fecha, 1, 2))" +
                " BETWEEN ? AND ? " ;

        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{codigo,categoria,subCategoria,marca,fecha_inicio_mes,fecha_fin_mes});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Flooring f = new Flooring();
                f.setSku_code(cursor.getString(cursor.getColumnIndexOrThrow(InsertFlooring.Columnas.SKU_CODE)));
                f.setSemana(cursor.getString(cursor.getColumnIndexOrThrow(InsertFlooring.Columnas.SEMANA)));
                operadores.add(f);
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }



    public ArrayList<Flooring> getSkuRelevadosInventarioSemanaAnterior(String codigo, String categoria, String subCategoria, String marca,int semana) {
        ArrayList<Flooring> operadores = new ArrayList<Flooring>();


        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-5"));
        cal.set(Calendar.DAY_OF_MONTH,1);
        DateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        formato.setTimeZone(TimeZone.getTimeZone("GMT-5"));
        // Obtenemos la fecha inicio del mes
        String fecha_inicio_mes = formato.format(cal.getTime());
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.DATE,31);
        formato.setTimeZone(TimeZone.getTimeZone("GMT-5"));
        // Obtenemos la fecha final del mes
        String  fecha_fin_mes = formato.format(cal.getTime());
        Cursor cursor = null;
        String selectQuery = "";


        String strSemana  = "";


                switch (semana) {
                    case 1:
                        strSemana = "Semana 1";
                        break;
                    case 2:
                        strSemana = "Semana 2";
                        break;
                    case 3:
                        strSemana = "Semana 3";
                        break;
                    case 4:
                        strSemana = "Semana 4";
                        break;
                    case 5:
                        strSemana = "Semana 5";
                        break;
                }





                if (strSemana.equalsIgnoreCase("Semana 2")){


                    selectQuery = "SELECT DISTINCT " + InsertFlooring.Columnas.SKU_CODE +
                            " FROM " + InsertFlooring.INSERT_FLOORING +
                            " WHERE " + InsertFlooring.Columnas.CODIGO + " =?" +
                            " AND " + InsertFlooring.Columnas.CATEGORIA + "=?" +
                            " AND " + InsertFlooring.Columnas.SUBCATEGORIA + "=?" +
                            " AND " + InsertFlooring.Columnas.BRAND + "=?" +
                            " AND " + InsertFlooring.Columnas.SEMANA + " REGEXP 'Semana 1'" +
                            // Formateamos la fecha de dd/mm/yyyy a yyyy-mm-dd
                            " AND datetime(substr(fecha, 7, 4) || '-' || substr(fecha, 4, 2) || '-' || substr(fecha, 1, 2))" +
                            " BETWEEN ? AND ? " ;


                    db = this.getReadableDatabase();
                    cursor = db.rawQuery(selectQuery, new String[]{codigo,categoria,subCategoria,marca,fecha_inicio_mes,fecha_fin_mes});


                }else if(strSemana.equalsIgnoreCase("Semana 3")){

                    selectQuery = "SELECT DISTINCT " + InsertFlooring.Columnas.SKU_CODE +
                            " FROM " + InsertFlooring.INSERT_FLOORING +
                            " WHERE " + InsertFlooring.Columnas.CODIGO + " =?" +
                            " AND " + InsertFlooring.Columnas.CATEGORIA + "=?" +
                            " AND " + InsertFlooring.Columnas.SUBCATEGORIA + "=?" +
                            " AND " + InsertFlooring.Columnas.BRAND + "=?" +
                            " AND " + InsertFlooring.Columnas.SEMANA + " REGEXP 'Semana 1|Semana 2'" +
                            // Formateamos la fecha de dd/mm/yyyy a yyyy-mm-dd
                            " AND datetime(substr(fecha, 7, 4) || '-' || substr(fecha, 4, 2) || '-' || substr(fecha, 1, 2))" +
                            " BETWEEN ? AND ? " ;


                    db = this.getReadableDatabase();
                    cursor = db.rawQuery(selectQuery, new String[]{codigo,categoria,subCategoria,marca,fecha_inicio_mes,fecha_fin_mes});





                }else if(strSemana.equalsIgnoreCase("Semana 4")){

                    selectQuery = "SELECT DISTINCT " + InsertFlooring.Columnas.SKU_CODE +
                            " FROM " + InsertFlooring.INSERT_FLOORING +
                            " WHERE " + InsertFlooring.Columnas.CODIGO + " =?" +
                            " AND " + InsertFlooring.Columnas.CATEGORIA + "=?" +
                            " AND " + InsertFlooring.Columnas.SUBCATEGORIA + "=?" +
                            " AND " + InsertFlooring.Columnas.BRAND + "=?" +
                            " AND " + InsertFlooring.Columnas.SEMANA + " REGEXP 'Semana 1|Semana 2|Semana 3'" +
                            // Formateamos la fecha de dd/mm/yyyy a yyyy-mm-dd
                            " AND datetime(substr(fecha, 7, 4) || '-' || substr(fecha, 4, 2) || '-' || substr(fecha, 1, 2))" +
                            " BETWEEN ? AND ? " ;


                    db = this.getReadableDatabase();
                    cursor = db.rawQuery(selectQuery, new String[]{codigo,categoria,subCategoria,marca,fecha_inicio_mes,fecha_fin_mes});


                }else if(strSemana.equalsIgnoreCase("Semana 5")){

                    selectQuery = "SELECT DISTINCT " + InsertFlooring.Columnas.SKU_CODE +
                            " FROM " + InsertFlooring.INSERT_FLOORING +
                            " WHERE " + InsertFlooring.Columnas.CODIGO + " =?" +
                            " AND " + InsertFlooring.Columnas.CATEGORIA + "=?" +
                            " AND " + InsertFlooring.Columnas.SUBCATEGORIA + "=?" +
                            " AND " + InsertFlooring.Columnas.BRAND + "=?" +
                            " AND " + InsertFlooring.Columnas.SEMANA + " REGEXP 'Semana 1|Semana 2|Semana 3|Semana 4'" +
                            // Formateamos la fecha de dd/mm/yyyy a yyyy-mm-dd
                            " AND datetime(substr(fecha, 7, 4) || '-' || substr(fecha, 4, 2) || '-' || substr(fecha, 1, 2))" +
                            " BETWEEN ? AND ? " ;


                    db = this.getReadableDatabase();
                    cursor = db.rawQuery(selectQuery, new String[]{codigo,categoria,subCategoria,marca,fecha_inicio_mes,fecha_fin_mes});


                }


/*
                selectQuery = "SELECT DISTINCT " + InsertFlooring.Columnas.SKU_CODE + " , " +
                        InsertFlooring.Columnas.SEMANA +
                        " FROM " + InsertFlooring.INSERT_FLOORING +
                        " WHERE " + InsertFlooring.Columnas.CODIGO + " =?" +
                        " AND " + InsertFlooring.Columnas.CATEGORIA + "=?" +
                        " AND " + InsertFlooring.Columnas.SUBCATEGORIA + "=?" +
                        " AND " + InsertFlooring.Columnas.BRAND + "=?" +
               //         " AND " + InsertFlooring.Columnas.SEMANA + "=?" +
                        // Formateamos la fecha de dd/mm/yyyy a yyyy-mm-dd
                        " AND datetime(substr(fecha, 7, 4) || '-' || substr(fecha, 4, 2) || '-' || substr(fecha, 1, 2))" +
                        " BETWEEN ? AND ? " ;


                db = this.getReadableDatabase();
                cursor = db.rawQuery(selectQuery, new String[]{codigo,categoria,subCategoria,marca,fecha_inicio_mes,fecha_fin_mes});

                if (cursor.moveToFirst()) {
                    do {
                        Flooring f = new Flooring();
                        f.setSku_code(cursor.getString(cursor.getColumnIndexOrThrow(InsertFlooring.Columnas.SKU_CODE)));
                        f.setSemana(cursor.getString(cursor.getColumnIndexOrThrow(InsertFlooring.Columnas.SEMANA)));
                        operadores.add(f);
                    } while (cursor.moveToNext());

                }
*/

/*
                if (operadores.size() == 0){
                    semana--;
                }else{
                    break;
                }
                */

        if (cursor.moveToFirst()) {
            do {
                Flooring f = new Flooring();
                f.setSku_code(cursor.getString(cursor.getColumnIndexOrThrow(InsertFlooring.Columnas.SKU_CODE)));
             //   f.setSemana(cursor.getString(cursor.getColumnIndexOrThrow(InsertFlooring.Columnas.SEMANA)));
                operadores.add(f);
            } while (cursor.moveToNext());

        }


            // closing connection
            cursor.close();
            db.close();
            // returning lables


            return operadores;



    }

    public ArrayList<Flooring> getSkuRelevadosInventarioSemanaActual(String codigo, String categoria, String subCategoria, String marca,int semana) {
        ArrayList<Flooring> operadores = new ArrayList<Flooring>();


        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-5"));
        cal.set(Calendar.DAY_OF_MONTH,1);
        DateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        formato.setTimeZone(TimeZone.getTimeZone("GMT-5"));
        // Obtenemos la fecha inicio del mes
        String fecha_inicio_mes = formato.format(cal.getTime());
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.DATE,31);
        formato.setTimeZone(TimeZone.getTimeZone("GMT-5"));
        // Obtenemos la fecha final del mes
        String  fecha_fin_mes = formato.format(cal.getTime());
        Cursor cursor = null;
        String selectQuery = "";


        String strSemana  = "";


                switch (semana) {
                    case 1:
                        strSemana = "Semana 1";
                        break;
                    case 2:
                        strSemana = "Semana 2";
                        break;
                    case 3:
                        strSemana = "Semana 3";
                        break;
                    case 4:
                        strSemana = "Semana 4";
                        break;
                }


                selectQuery = "SELECT DISTINCT " + InsertFlooring.Columnas.SKU_CODE + " , " +
                        InsertFlooring.Columnas.SEMANA +
                        " FROM " + InsertFlooring.INSERT_FLOORING +
                        " WHERE " + InsertFlooring.Columnas.CODIGO + " =?" +
                        " AND " + InsertFlooring.Columnas.CATEGORIA + "=?" +
                        " AND " + InsertFlooring.Columnas.SUBCATEGORIA + "=?" +
                        " AND " + InsertFlooring.Columnas.BRAND + "=?" +
                        " AND " + InsertFlooring.Columnas.SEMANA + "=?" +
                        // Formateamos la fecha de dd/mm/yyyy a yyyy-mm-dd
                        " AND datetime(substr(fecha, 7, 4) || '-' || substr(fecha, 4, 2) || '-' || substr(fecha, 1, 2))" +
                        " BETWEEN ? AND ? " ;


                db = this.getReadableDatabase();
                cursor = db.rawQuery(selectQuery, new String[]{codigo,categoria,subCategoria,marca,strSemana,fecha_inicio_mes,fecha_fin_mes});

                if (cursor.moveToFirst()) {
                    do {
                        Flooring f = new Flooring();
                        f.setSku_code(cursor.getString(cursor.getColumnIndexOrThrow(InsertFlooring.Columnas.SKU_CODE)));
                        f.setSemana(cursor.getString(cursor.getColumnIndexOrThrow(InsertFlooring.Columnas.SEMANA)));
                        operadores.add(f);
                    } while (cursor.moveToNext());

                }


/*
                if (operadores.size() == 0){
                    semana--;
                }else{
                    break;
                }
                */



            // closing connection
            cursor.close();
            db.close();
            // returning lables


            return operadores;



    }




    public ArrayList<InsertSms> getListGuardadoSms(String codigo,String user) {
        ArrayList<InsertSms> operadores = new ArrayList<InsertSms>();


        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-5"));
        cal.set(Calendar.DAY_OF_MONTH,1);
        DateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        formato.setTimeZone(TimeZone.getTimeZone("GMT-5"));
        // Obtenemos la fecha inicio del mes
        String fecha_inicio_mes = formato.format(cal.getTime());
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.DATE,31);
        formato.setTimeZone(TimeZone.getTimeZone("GMT-5"));
        // Obtenemos la fecha final del mes
        String  fecha_fin_mes = formato.format(cal.getTime());


        String selectQuery = "SELECT DISTINCT " + ContractInsertSugeridos.Columnas.SKU_CODE +
                " FROM " + ContractInsertSugeridos.INSERT_SUGERIDOS +
                " WHERE " + ContractInsertSugeridos.Columnas.CODIGO + " =? AND " +
                ContractInsertSugeridos.Columnas.USUARIO + " =?" +
                // Formateamos la fecha de dd/mm/yyyy a yyyy-mm-dd
                " AND datetime(substr(fecha, 7, 4) || '-' || substr(fecha, 4, 2) || '-' || substr(fecha, 1, 2))" +
                " BETWEEN ? AND ? " +
                " GROUP BY " + ContractInsertSugeridos.Columnas.SKU_CODE +
                " ORDER BY " + ContractInsertSugeridos.Columnas.SKU_CODE + " DESC";;

        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{codigo,user,fecha_inicio_mes,fecha_fin_mes});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                InsertSms p = new InsertSms();
                p.setSku_code(cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertSugeridos.Columnas.SKU_CODE)));
                operadores.add(p);
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }


    public ArrayList<InsertPdi> getListGuardadoPdi(String codigo, String user) {
        ArrayList<InsertPdi> operadores = new ArrayList<InsertPdi>();


        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-5"));
        cal.set(Calendar.DAY_OF_MONTH,1);
        DateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        formato.setTimeZone(TimeZone.getTimeZone("GMT-5"));
        // Obtenemos la fecha inicio del mes
        String fecha_inicio_mes = formato.format(cal.getTime());
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.DATE,31);
        formato.setTimeZone(TimeZone.getTimeZone("GMT-5"));
        // Obtenemos la fecha final del mes
        String  fecha_fin_mes = formato.format(cal.getTime());


        String selectQuery = "SELECT DISTINCT " + ContractInsertPDI.Columnas.MARCA_SELECCIONADA +
                " FROM " + ContractInsertPDI.INSERT_PDI +
                " WHERE " + ContractInsertPDI.Columnas.CODIGO + " =? AND " +
                ContractInsertPDI.Columnas.USUARIO + " =?" +
                // Formateamos la fecha de dd/mm/yyyy a yyyy-mm-dd
                " AND datetime(substr(fecha, 7, 4) || '-' || substr(fecha, 4, 2) || '-' || substr(fecha, 1, 2))" +
                " BETWEEN ? AND ? " +
                " GROUP BY " + ContractInsertPDI.Columnas.MARCA_SELECCIONADA +
                " ORDER BY " + ContractInsertPDI.Columnas.MARCA_SELECCIONADA + " DESC";;

        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{codigo,user,fecha_inicio_mes,fecha_fin_mes});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                InsertPdi p = new InsertPdi();
                p.setMarca_seleccionada(cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertPDI.Columnas.MARCA_SELECCIONADA)));
                operadores.add(p);
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }


    public boolean SKUDuplicadoValores(String sku_code, String codigo) {
        // Select All Query
        String selectQuery = "SELECT  * FROM " + ContractInsertValoresSesion.INSERT_VALORES_SESION + " WHERE "+
                ContractInsertValoresSesion.Columnas.SKU_CODE + "=? "+ " AND "+
                ContractInsertValoresSesion.Columnas.CODIGO + "=? " +
                "GROUP BY " + ContractInsertValoresSesion.Columnas.SKU_CODE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{sku_code,codigo});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            return true;
        }
        // closing connection
        cursor.close();
        db.close();

        return false;
    }

    public void eliminarSKUDuplicadoValores(String sku_code, String codigo) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(ContractInsertValoresSesion.INSERT_VALORES_SESION, ContractInsertValoresSesion.Columnas.SKU_CODE + "='" + sku_code + "'" + " AND " + ContractInsertValoresSesion.Columnas.CODIGO + "='" + codigo + "'", null);
        db.close();
    }

    public ArrayList<InsertValores> getListGuardadoValores(String codigo) {
        ArrayList<InsertValores> operadores = new ArrayList<InsertValores>();

        String selectQuery = "SELECT " + ContractInsertValoresSesion.Columnas.SKU_CODE + ", " +
                ContractInsertValoresSesion.Columnas.AUSENCIA + ", " +
                ContractInsertValoresSesion.Columnas.CODIFICA + ", " +
                ContractInsertValoresSesion.Columnas.RESPONSABLE + ", " +
                ContractInsertValoresSesion.Columnas.RAZONES +
                " FROM " + ContractInsertValoresSesion.INSERT_VALORES_SESION +
                " WHERE " + ContractInsertValoresSesion.Columnas.CODIGO + " =?";
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{codigo});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                InsertValores v = new InsertValores();
                v.setSku_code(cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertValoresSesion.Columnas.SKU_CODE)));
                v.setAusencia(cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertValoresSesion.Columnas.AUSENCIA)));
                v.setCodifica(cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertValoresSesion.Columnas.CODIFICA)));
                v.setResponsable(cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertValoresSesion.Columnas.RESPONSABLE)));
                v.setRazones(cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertValoresSesion.Columnas.RAZONES)));
                operadores.add(v);
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public boolean marcaDuplicadaExh(String brand) {
        // Select All Query
        String selectQuery = "SELECT  * FROM " + ContractInsertExhSesion.INSERT_EXH_SESION + " WHERE "+
                ContractInsertExhSesion.Columnas.BRAND + "=? "+
                "GROUP BY " + ContractInsertExhSesion.Columnas.BRAND;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{brand});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            return true;
        }
        // closing connection
        cursor.close();
        db.close();

        return false;
    }

    public void eliminarMarcaDuplicadaExh(String brand) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(ContractInsertExhSesion.INSERT_EXH_SESION, ContractInsertExhSesion.Columnas.BRAND + "='" + brand + "'", null);
        db.close();
    }

    public ArrayList<InsertExhibiciones> getListGuardadoExh(String codigo) {
        ArrayList<InsertExhibiciones> operadores = new ArrayList<InsertExhibiciones>();

        String selectQuery = "SELECT " + ContractInsertExhSesion.Columnas.BRAND + ", " +
                ContractInsertExhSesion.Columnas.TIPO_EXH + ", " +
                ContractInsertExhSesion.Columnas.ZONA_EX + ", " +
                ContractInsertExhSesion.Columnas.CONTRATADA + ", " +
                ContractInsertExhSesion.Columnas.CONDICION + ", " +
                ContractInsertExhSesion.Columnas.FOTO +
                " FROM " + ContractInsertExhSesion.INSERT_EXH_SESION +
                " WHERE " + ContractInsertExhSesion.Columnas.CODIGO + " =?";
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{codigo});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                InsertExhibiciones v = new InsertExhibiciones();
                v.setMarca(cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertExhSesion.Columnas.BRAND)));
                v.setTipo_exh(cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertExhSesion.Columnas.TIPO_EXH)));
                v.setZona(cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertExhSesion.Columnas.ZONA_EX)));
                v.setContratada(cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertExhSesion.Columnas.CONTRATADA)));
                v.setCondicion(cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertExhSesion.Columnas.CONDICION)));
                v.setFoto(cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertExhSesion.Columnas.FOTO)));
                operadores.add(v);
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public ArrayList<InsertCanjes> getListGuardadoCanjes(String usuario, String codigo) {
        ArrayList<InsertCanjes> operadores = new ArrayList<InsertCanjes>();

        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-5"));
        cal.set(Calendar.DAY_OF_MONTH,1);
        DateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        formato.setTimeZone(TimeZone.getTimeZone("GMT-5"));
        // Obtenemos la fecha inicio del mes
        String fecha_inicio_mes = formato.format(cal.getTime());
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.DATE,31);
        formato.setTimeZone(TimeZone.getTimeZone("GMT-5"));
        // Obtenemos la fecha final del mes
        String  fecha_fin_mes = formato.format(cal.getTime());


        String selectQuery = "SELECT * FROM " + ContractInsertCanjes.INSERT_CANJES + " WHERE " +

                        ContractInsertCanjes.Columnas.USUARIO + " =? AND " +
                        ContractInsertCanjes.Columnas.CODIGO + " =?" +
                        // Formateamos la fecha de dd/mm/yyyy a yyyy-mm-dd
                        " AND datetime(substr(fecha, 7, 4) || '-' || substr(fecha, 4, 2) || '-' || substr(fecha, 1, 2))" +
                        " BETWEEN ? AND ? " +
                        " GROUP BY " + ContractInsertCanjes.Columnas.PRODUCTO +
                        " ORDER BY " + ContractInsertCanjes.Columnas._ID + " DESC";
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{usuario, codigo,fecha_inicio_mes,fecha_fin_mes});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                InsertCanjes ins = new InsertCanjes();
                ins.setProducto(cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertCanjes.Columnas.PRODUCTO)));
                ins.setTipo_combo(cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertCanjes.Columnas.TIPO_COMBO)));
                ins.setMecanica(cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertCanjes.Columnas.MECANICA)));
                ins.setCombos_armados(cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertCanjes.Columnas.COMBOS_ARMADOS)));
                ins.setStock(cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertCanjes.Columnas.STOCK)));
                ins.setPvc_combo(cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertCanjes.Columnas.PVC_COMBO)));
                ins.setPvc_unitario(cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertCanjes.Columnas.PVC_UNITARIO)));
                ins.setVisita(cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertCanjes.Columnas.VISITA)));
                ins.setMes(cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertCanjes.Columnas.MES)));
                ins.setObservaciones(cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertCanjes.Columnas.OBSERVACIONES)));
                operadores.add(ins);
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }





    public ArrayList<BasePharmaValue> consultarRutaOptima(String user) {
        ArrayList<BasePharmaValue> operadores = new ArrayList<BasePharmaValue>();

//        String selectQuery = "SELECT " + ContractPharmaValue.Columnas.POS_ID + ", "+
//                    ContractPharmaValue.Columnas.POS_NAME + ", " +
//                    ContractPharmaValue.Columnas.CHANNEL + ", " +
//                    ContractPharmaValue.Columnas.DIRECCION + ", " +
//                    ContractPharmaValue.Columnas.CIUDAD + ", " +
//                    ContractPharmaValue.Columnas.FOTO + ", " +
//                    ContractPharmaValue.Columnas.LATITUD + ", " +
//                    ContractPharmaValue.Columnas.LONGITUD + ", " +
//                    " (3959 * ACOS(COS(RADIANS(37))" +
//                    " *COS(RADIANS(" + ContractPharmaValue.Columnas.LATITUD + "))" +
//                    " *COS(RADIANS(" + ContractPharmaValue.Columnas.LONGITUD + ") - RADIANS('-79.907835'))" +
//                    " +SIN(RADIANS('-2.172486')) * SIN(RADIANS(" + ContractPharmaValue.Columnas.LATITUD + ")))) AS distance" +
//                    " FROM " + ContractPharmaValue.POS +
//                    " WHERE "+ ContractPharmaValue.Columnas.USER +"=? " +
//                    " AND "+ ContractPharmaValue.Columnas.LATITUD +" NOT REGEXP '*' " +
//                    " AND "+ ContractPharmaValue.Columnas.LONGITUD +" NOT REGEXP '*' " +
//                    " GROUP BY distance" +
//                    " HAVING distance < 1000 " +
//                    " ORDER BY distance";

        String selectQuery = "SELECT " + ContractPharmaValue.Columnas.POS_ID + ", "+
                ContractPharmaValue.Columnas.POS_NAME + ", " +
                ContractPharmaValue.Columnas.CHANNEL + ", " +
                ContractPharmaValue.Columnas.DIRECCION + ", " +
                ContractPharmaValue.Columnas.CIUDAD + ", " +
                ContractPharmaValue.Columnas.FOTO + ", " +
                ContractPharmaValue.Columnas.LATITUD + ", " +
                ContractPharmaValue.Columnas.LONGITUD +
                " FROM " + ContractPharmaValue.POS +
                " WHERE "+ ContractPharmaValue.Columnas.USER +"=? " +
                " AND "+ ContractPharmaValue.Columnas.LATITUD +" NOT LIKE '%*%' " +
                " AND "+ ContractPharmaValue.Columnas.LONGITUD +" NOT LIKE '%*%'";

        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{user});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                BasePharmaValue bpv = new BasePharmaValue();
                bpv.setPos_id(cursor.getString(cursor.getColumnIndexOrThrow(ContractPharmaValue.Columnas.POS_ID)));
                bpv.setPos_name(cursor.getString(cursor.getColumnIndexOrThrow(ContractPharmaValue.Columnas.POS_NAME)));
                bpv.setChannel(cursor.getString(cursor.getColumnIndexOrThrow(ContractPharmaValue.Columnas.CHANNEL)));
                bpv.setAddress(cursor.getString(cursor.getColumnIndexOrThrow(ContractPharmaValue.Columnas.DIRECCION)));
                bpv.setCity(cursor.getString(cursor.getColumnIndexOrThrow(ContractPharmaValue.Columnas.CIUDAD)));
                bpv.setLatitud(cursor.getString(cursor.getColumnIndexOrThrow(ContractPharmaValue.Columnas.LATITUD)));
                bpv.setLongitud(cursor.getString(cursor.getColumnIndexOrThrow(ContractPharmaValue.Columnas.LONGITUD)));
                bpv.setFoto(cursor.getString(cursor.getColumnIndexOrThrow(ContractPharmaValue.Columnas.FOTO)));
                operadores.add(bpv);
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public List<BasePreguntas> getAllQuestions(String canal) {
        List<BasePreguntas> quesList = new ArrayList<BasePreguntas>();
        Log.i("CANAL", canal);
        // Select All Query
        String selectQuery = "SELECT  * FROM " + ContractPreguntas.TABLE_QUEST + " WHERE " + ContractPreguntas.Columnas.KEY_CANAL + "=? "+ "  ORDER BY " + Constantes.ID_REMOTA + " + 0";
//        String selectQuery = "SELECT  * FROM " + ContractPreguntas.TABLE_QUEST + " WHERE " + ContractPreguntas.Columnas.KEY_CANAL + "=? "+ "  ORDER BY " + Constantes.ID_REMOTA;
        db=this.getReadableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, new String[]{canal});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                BasePreguntas quest = new BasePreguntas();
                quest.setQuestion(cursor.getString(1));
                quest.setAnswer(cursor.getString(2));
                quest.setOpta(cursor.getString(3));
                quest.setOptb(cursor.getString(4));
                quest.setOptc(cursor.getString(5));
                quest.setCanal(cursor.getString(6));
                quesList.add(quest);
            } while (cursor.moveToNext());
        }
        Log.i("SIZE", quesList.size() + "");
        // return quest list
        return quesList;
    }

    public int rowcount(String canal) {
        int row=0;
        String selectQuery = "SELECT * FROM " + ContractPreguntas.TABLE_QUEST + " WHERE " + ContractPreguntas.Columnas.KEY_CANAL + "=?";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{canal});
        row=cursor.getCount();
        return row;
    }

//    public int rowcount() {
//        int row=0;
//        String selectQuery = "SELECT  * FROM " + ContractPreguntas.TABLE_QUEST;
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor cursor = db.rawQuery(selectQuery, null);
//        row=cursor.getCount();
//        return row;
//    }
    public ArrayList<BasePharmaValue> consultarBasePharmaValueByDay(String user, String fecha){
        Log.i("USUARIO", user);
        Log.i("FECHA", fecha);
        ArrayList<BasePharmaValue> operadores = new ArrayList<BasePharmaValue>();

        String selectQuery = "SELECT " +
                ContractPharmaValue.Columnas.POS_ID + ", "+
                ContractPharmaValue.Columnas.POS_NAME +
                " FROM " + ContractPharmaValue.POS + " WHERE "+
                ContractPharmaValue.Columnas.USER +"=? AND " +
                ContractPharmaValue.Columnas.FECHA_VISITA +"=? " +
                " ORDER BY " + ContractPharmaValue.Columnas.POS_ID + " ASC " ;
        // Select All Query

        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{user, fecha});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                BasePharmaValue bpv = new BasePharmaValue();
                bpv.setPos_id(cursor.getString(cursor.getColumnIndexOrThrow(ContractPharmaValue.Columnas.POS_ID)));
                bpv.setPos_name(cursor.getString(cursor.getColumnIndexOrThrow(ContractPharmaValue.Columnas.POS_NAME)));
                operadores.add(bpv);
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public ArrayList<String> getEstadoMarcacionPdv(String codigo, String usuario, String fecha){
        ArrayList<String> operadores = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT * FROM " + ContractInsertGps.INSERT_GPS + " WHERE "+
                ContractInsertGps.Columnas.IDPDV + "=? AND " +
                ContractInsertGps.Columnas.USUARIO + "=? AND " +
                ContractInsertGps.Columnas.FECHA + "=? " +
                " GROUP BY " + ContractInsertGps.Columnas.TIPO;

        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{codigo, usuario, fecha});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertGps.Columnas.TIPO)));
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables

        return operadores ;
    }

    public List<String> getCategoriaMallaCodificados(String tipo, String fabricante, String canal, String subcanal) {
        List<String> operadores = new ArrayList<String>();
        String selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.SECTOR +
                " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE ";

        List<String> parametersList = new ArrayList<String>();
        parametersList.add("%" + fabricante + "%");

        if (tipo.equalsIgnoreCase("COMPETENCIA")) {
            selectQuery += ContractPortafolioProductos.Columnas.FABRICANTE + " NOT LIKE ?";
        } else {
            selectQuery += ContractPortafolioProductos.Columnas.FABRICANTE + " LIKE ?";
        }

        if (canal.equalsIgnoreCase("AUTOSERVICIO")) {
            selectQuery += " AND " + ContractPortafolioProductos.Columnas.CADENAS + " LIKE ?";
            parametersList.add("%" + subcanal.trim() + "%");
        }

        String[] parametersArr = new String[parametersList.size()];
        parametersArr = parametersList.toArray(parametersArr);

        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, parametersArr);

        operadores.add("Seleccione");
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.SECTOR)));
            } while (cursor.moveToNext() && cursor!=null);
        }
        Log.i("OPERADORES",operadores.size()+"");
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public List<String> getSubcategoriaMallaCodificados(String categoria, String tipo, String manufacturer, String canal, String subcanal){
        List<String> operadores = new ArrayList<String>();

        String selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.SUBCATEGORIA +
                " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS +
                " WHERE " + ContractPortafolioProductos.Columnas.SECTOR + "=? AND ";

        List<String> parametersList = new ArrayList<String>();
        parametersList.add(categoria);
        parametersList.add("%" + manufacturer + "%");

        if (tipo.equalsIgnoreCase("COMPETENCIA")) {
            selectQuery += ContractPortafolioProductos.Columnas.FABRICANTE + " NOT LIKE ?";
        } else {
            selectQuery += ContractPortafolioProductos.Columnas.FABRICANTE + " LIKE ?";
        }

        if (canal.equalsIgnoreCase("AUTOSERVICIO")) {
            selectQuery += " AND " + ContractPortafolioProductos.Columnas.CADENAS + " LIKE ?";
            parametersList.add("%" + subcanal.trim() + "%");
        }

        String[] parametersArr = new String[parametersList.size()];
        parametersArr = parametersList.toArray(parametersArr);

        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, parametersArr);

        operadores.add("Seleccione");
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.SUBCATEGORIA)));
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public List<String> getBrand2MallaCodificados(String categoria, String subcategoria, String tipo, String manufacturer, String canal, String subcanal){
        List<String> operadores = new ArrayList<String>();

        String selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.MARCA +
                " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS +
                " WHERE " + ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                ContractPortafolioProductos.Columnas.SUBCATEGORIA + "=? AND ";

        List<String> parametersList = new ArrayList<String>();
        parametersList.add(categoria);
        parametersList.add(subcategoria);
        parametersList.add("%" + manufacturer + "%");

        if (tipo.equalsIgnoreCase("COMPETENCIA")) {
            selectQuery += ContractPortafolioProductos.Columnas.FABRICANTE + " NOT LIKE ?";
        } else {
            selectQuery += ContractPortafolioProductos.Columnas.FABRICANTE + " LIKE ?";
        }

        if (canal.equalsIgnoreCase("AUTOSERVICIO")) {
            selectQuery += " AND " + ContractPortafolioProductos.Columnas.CADENAS + " LIKE ?";
            parametersList.add("%" + subcanal.trim() + "%");
        }

        String[] parametersArr = new String[parametersList.size()];
        parametersArr = parametersList.toArray(parametersArr);

        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, parametersArr);

        operadores.add("Seleccione");
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.MARCA)));
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public ArrayList<BasePortafolioProductos> filtrarListProductosMallaCodificados(String categoria, String subcategoria, String brand, String tipo, String manufacturer, String canal, String subcanal){
        ArrayList<BasePortafolioProductos> operadores = new ArrayList<BasePortafolioProductos>();

        String selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.SKU +
                " FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS +
                " WHERE " + ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                ContractPortafolioProductos.Columnas.SUBCATEGORIA + "=? AND " +
                ContractPortafolioProductos.Columnas.MARCA + "=? AND ";

        List<String> parametersList = new ArrayList<String>();
        parametersList.add(categoria);
        parametersList.add(subcategoria);
        parametersList.add(brand);
        parametersList.add("%" + manufacturer + "%");

        if (tipo.equalsIgnoreCase("COMPETENCIA")) {
            selectQuery += ContractPortafolioProductos.Columnas.FABRICANTE + " NOT LIKE ?";
        } else {
            selectQuery += ContractPortafolioProductos.Columnas.FABRICANTE + " LIKE ?";
        }

        if (canal.equalsIgnoreCase("AUTOSERVICIO")) {
            selectQuery += " AND " + ContractPortafolioProductos.Columnas.CADENAS + " LIKE ?";
            parametersList.add("%" + subcanal.trim() + "%");
        }

        String[] parametersArr = new String[parametersList.size()];
        parametersArr = parametersList.toArray(parametersArr);

        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, parametersArr);

//        operadores.add(new Base_portafolio_productos("HEADER"));
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                BasePortafolioProductos bpv = new BasePortafolioProductos();
                bpv.setSku(cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.SKU)));
                operadores.add(bpv);
            } while (cursor.moveToNext());
        }

        if (!brand.equalsIgnoreCase("SELECCIONE")) {
            operadores.add(new BasePortafolioProductos("Otros"));
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public String getEstadoMallaCodificados(String codigo, String user, String sku) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-5"));
              //  cal.set(2023,Calendar.FEBRUARY,10);
        Date currentLocalTime = cal.getTime();
        DateFormat date = new SimpleDateFormat("dd/MM/yyy");
        date.setTimeZone(TimeZone.getTimeZone("GMT-5"));
        String fecha_actual = date.format(currentLocalTime);
        String mes_actual = fecha_actual.substring(3);


        cal.set(Calendar.DAY_OF_MONTH,1);
        DateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        formato.setTimeZone(TimeZone.getTimeZone("GMT-5"));
        String fecha_inicio_mes = formato.format(cal.getTime());


        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.DATE,31);
        formato.setTimeZone(TimeZone.getTimeZone("GMT-5"));
        String  fecha_fin_mes = formato.format(cal.getTime());



        Log.i("FECHAS", "FECHA ACTUAL: " + fecha_actual + " - MES ACTUAL: " + mes_actual);

        String operadores = "PENDIENTE";
        String selectQuery = "";
        Cursor cursor = null;

        selectQuery = "SELECT * FROM " + ContractInsertMallaCodificados.INSERT_MALLA_CODIFICADOS +
                " WHERE " + ContractInsertMallaCodificados.Columnas.CODIGO + " =? AND " +
                ContractInsertMallaCodificados.Columnas.USUARIO + "=? AND " +
                ContractInsertMallaCodificados.Columnas.FECHA + "=? AND " +
                // Formateamos la fecha de dd/mm/yyyy a yyyy-mm-dd
                " datetime(substr(fecha, 7, 4) || '-' || substr(fecha, 4, 2) || '-' || substr(fecha, 1, 2))" +
                " BETWEEN ? AND ? AND " +
                ContractInsertMallaCodificados.Columnas.SKU_CODE + "=?";
        db = this.getReadableDatabase();
        cursor = db.rawQuery(selectQuery, new String[]{codigo, user, fecha_actual,fecha_inicio_mes,fecha_fin_mes, sku});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores = "REALIZADO";
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public String getSkuOsa(String codigo, String user,String sku) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-5"));
        //  cal.set(2023,Calendar.FEBRUARY,10);
    //    Date currentLocalTime = cal.getTime();
    //    DateFormat date = new SimpleDateFormat("dd/MM/yyy");
    //    date.setTimeZone(TimeZone.getTimeZone("GMT-5"));
    //     String fecha_actual = date.format(currentLocalTime);
   //      String mes_actual = fecha_actual.substring(3);


        cal.set(Calendar.DAY_OF_MONTH,1);
        DateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        formato.setTimeZone(TimeZone.getTimeZone("GMT-5"));
        String fecha_inicio_mes = formato.format(cal.getTime());


        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        formato.setTimeZone(TimeZone.getTimeZone("GMT-5"));
        String  fecha_fin_mes = formato.format(cal.getTime());



     //   Log.i("FECHAS", "FECHA ACTUAL: " + fecha_actual + " - MES ACTUAL: " + mes_actual);

        String operadores = "NO REVELADO";
        String selectQuery = "";
        Cursor cursor = null;

        selectQuery = "SELECT * FROM " + ContractInsertValores.INSERT_VALORES +
                " WHERE " +
                ContractInsertValores.Columnas.CODIGO + " =? AND " +
                ContractInsertValores.Columnas.USUARIO + "=? AND " +
                ContractInsertValores.Columnas.SKU_CODE + "=? AND " +
                " strftime('%Y-%m-%d',fecha) " + " BETWEEN ? AND ?" +
                " ORDER BY fecha DESC LIMIT 1";
        db = this.getReadableDatabase();
        cursor = db.rawQuery(selectQuery, new String[]{codigo,user,sku,fecha_inicio_mes,fecha_fin_mes});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores = "REVELADO";
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }


    public BasePortafolioProductos getProducto(String sku) {
        BasePortafolioProductos bpp = null;

        // Select All Query
        String selectQuery = "SELECT * FROM " + ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE "+
                ContractPortafolioProductos.Columnas.SKU + "=?";
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{sku});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                bpp = new BasePortafolioProductos();
                bpp.setSector(cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.SECTOR)));
                bpp.setSegmento(cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.SEGMENTO)));
                bpp.setFabricante(cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.FABRICANTE)));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return bpp;
    }

    // below is the method for updating our courses
    public void updateCourse() {

        // calling a method to get writable database.
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // on below line we are passing all values
        // along with its key and value pair.
        values.putNull(Constantes.ID_REMOTA);
        values.put(Constantes.ESTADO, "0");
        values.put(Constantes.PENDIENTE_INSERCION, "1");

        // on below line we are calling a update method to update our database and passing our values.
        // and we are comparing it with name of our course which is stored in original name variable.
        db.update(InsertFlooring.INSERT_FLOORING, values, InsertFlooring.Columnas.FECHA+" LIKE '%/04/2023%'", null);
        db.close();
    }

    public List<String> getSKUFlooring(String categoria,String subcategoria,String brand,String codigo_pdv, String canal, String subcanal, String manufacturer) {
        List<String> operadores = new ArrayList<>();

        Log.i("CATEGORIA", categoria);
        Log.i("SUBCATEGORIA", subcategoria);
        Log.i("MARCA", brand);

        Cursor cursor = null;
        String selectQuery = "";

        if (canal.equalsIgnoreCase("MAYORISTA")) {
            if (categoria.equalsIgnoreCase("INSECTICIDAS")) {
                selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.SKU + " FROM " +
                        ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " +
                        ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                        ContractPortafolioProductos.Columnas.CATEGORY + "=? AND " +
                        ContractPortafolioProductos.Columnas.MARCA + "=? AND (" +
                        ContractPortafolioProductos.Columnas.FABRICANTE + " LIKE ? OR " +
                        ContractPortafolioProductos.Columnas.FABRICANTE + "='Sc Jhonson')" +
                        " ORDER BY " + ContractPortafolioProductos.Columnas.SKU;
                db = this.getReadableDatabase();
                cursor = db.rawQuery(selectQuery, new String[]{categoria, subcategoria, brand, "%" + manufacturer + "%"});
            } else {
                selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.SKU + " FROM " +
                        ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " +
                        ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                        ContractPortafolioProductos.Columnas.CATEGORY + "=? AND " +
                        ContractPortafolioProductos.Columnas.MARCA + "=? AND " +
                        ContractPortafolioProductos.Columnas.FABRICANTE + " LIKE ?" +
                        " ORDER BY " + ContractPortafolioProductos.Columnas.SKU;
                db = this.getReadableDatabase();
                cursor = db.rawQuery(selectQuery, new String[]{categoria, subcategoria, brand, "%" + manufacturer + "%"});
            }
        }else{
            selectQuery = "SELECT DISTINCT "+ ContractPortafolioProductos.Columnas.SKU + " FROM " +
                    ContractPortafolioProductos.PORTAFOLIOPRODUCTOS + " WHERE " +
                    ContractPortafolioProductos.Columnas.SECTOR + "=? AND " +
                    ContractPortafolioProductos.Columnas.CATEGORY + "=? AND " +
                    ContractPortafolioProductos.Columnas.MARCA + "=? AND " +
                    ContractPortafolioProductos.Columnas.CADENAS + " LIKE ?" +
                    " ORDER BY " + ContractPortafolioProductos.Columnas.SKU;
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{categoria, subcategoria, brand, "%" + subcanal.trim() + "%"});
        }

        operadores.add("Seleccione");
        if (cursor.moveToFirst()) {
            do {
                operadores.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractPortafolioProductos.Columnas.SKU)));
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public ArrayList<ShareElements> getOtrasMarcas(String codigo,String categoria,String subcategoria,String hora) {
        ArrayList<ShareElements> operadores = new ArrayList<ShareElements>();

        // Select All Query
        String selectQuery = "SELECT " + ContractInsertShare.Columnas.MARCA_SELECCIONADA + " , " +
                ContractInsertShare.Columnas.CTMS_MARCA +
                " FROM " + ContractInsertShare.INSERT_SHARE +
                " WHERE " +
                ContractInsertShare.Columnas.CODIGO + "=? AND " +
                ContractInsertShare.Columnas.SECTOR + "=? AND " +
            //    ContractInsertShare.Columnas.CATEGORIA + "=? AND " +
                ContractInsertShare.Columnas.HORA + "=? AND (" +
                ContractInsertShare.Columnas.MARCA_SELECCIONADA + " LIKE '%OTROS 1%' OR " +
                ContractInsertShare.Columnas.MARCA_SELECCIONADA + " LIKE '%OTROS 2%' OR " +
                ContractInsertShare.Columnas.MARCA_SELECCIONADA + " LIKE '%OTROS 3%')";
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{codigo,categoria /*,subcategoria*/,hora});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ShareElements shareElements = new ShareElements();
                shareElements.setMarca(cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertShare.Columnas.MARCA_SELECCIONADA)));
                shareElements.setCaras(cursor.getString(cursor.getColumnIndexOrThrow(ContractInsertShare.Columnas.CTMS_MARCA)));
                operadores.add(shareElements);
            } while (cursor.moveToNext() && cursor!=null);
        }
        Log.i("OPERADORES",operadores.size()+"");
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public boolean esDispositivoDelUsuario(String device_id) {
        List<String> operadores = new ArrayList<String>();
        String codigo = "";

        // Select All Query
        String selectQuery = "SELECT  DISTINCT "+ ContractPharmaValue.Columnas.USER +" FROM " + ContractPharmaValue.POS + " WHERE "+
                ContractPharmaValue.Columnas.DEVICE_ID + "=? ";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{device_id});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                codigo = cursor.getString(cursor.getColumnIndexOrThrow(ContractPharmaValue.Columnas.USER));
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return !codigo.equals("") && !codigo.equals("-");
    }

    public String getDispositivoDelUsuario(String user) {
        List<String> operadores = new ArrayList<String>();
        String codigo = "";

        // Select All Query
        String selectQuery = "SELECT  DISTINCT "+ ContractPharmaValue.Columnas.DEVICE_ID +" FROM " + ContractPharmaValue.POS + " WHERE "+
                ContractPharmaValue.Columnas.USER + "=? ";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{user});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                codigo = cursor.getString(cursor.getColumnIndexOrThrow(ContractPharmaValue.Columnas.DEVICE_ID));
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables

        if(codigo == null){
            return "";
        }else{
            return codigo;
        }


    }

    public String getLastInsertOpcDes() {
        String operadores = "NO DATA";

        //String fechaActual = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        //String horaActual = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

        String desc = "Opciones de Desarrollador Activadas";
        Cursor cursor;
        String selectQuery = "";

        selectQuery = "SELECT * " +
                " FROM " + ContractNotificacion.NOTIFICACION +
                " WHERE " + ContractNotificacion.Columnas.DESCRIPCION + "=? " +
                " ORDER BY " + ContractNotificacion.Columnas._ID + " DESC LIMIT 1";
        db = this.getReadableDatabase();
        cursor = db.rawQuery(selectQuery, new String[]{desc});
        //}

        //operadores.add("Seleccione");
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                operadores = cursor.getString(cursor.getColumnIndexOrThrow(ContractNotificacion.Columnas.HORA));
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
        //return operadores;
    }


    public ArrayList<Asistencia> getNoRegistrados() {
        ArrayList<Asistencia> operadores = new ArrayList<Asistencia>();

        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-5"));
        Date currentLocalTime = cal.getTime();

        DateFormat hour = new SimpleDateFormat("HH:mm:ss");
        hour.setTimeZone(TimeZone.getTimeZone("GMT-5"));
        String horaser = hour.format(currentLocalTime);

        // Select All Query
        String selectQuery = "SELECT * FROM " + ContractAsistenciasLocal.TABLE_NAME +
                " WHERE " +
                ContractAsistenciasLocal.Columnas.REGISTRADO + "=? AND " +
                ContractAsistenciasLocal.Columnas.HORA + " < ?";
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{"no enviado",horaser});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Asistencia asistencia = new Asistencia();
                asistencia.setHora(cursor.getString(cursor.getColumnIndexOrThrow(ContractAsistenciasLocal.Columnas.HORA)));
                asistencia.setRegistrado(cursor.getString(cursor.getColumnIndexOrThrow(ContractAsistenciasLocal.Columnas.REGISTRADO)));
                operadores.add(asistencia);
            } while (cursor.moveToNext() && cursor!=null);
        }
        Log.i("OPERADORES",operadores.size()+"");
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }


    public boolean marcacionEnviada (String horaMarcacion){
        Log.i("Database","Datos recibidos, hora marcacion: "+ horaMarcacion);
        boolean id = false;
        ArrayList<Asistencia> operadores = new ArrayList<Asistencia>();

        String selectQuery = "SELECT * FROM " + ContractAsistenciasLocal.TABLE_NAME +
                " WHERE " +
                ContractAsistenciasLocal.Columnas.HORA + "=? AND " +
                ContractAsistenciasLocal.Columnas.REGISTRADO + " =? ";
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{horaMarcacion,"no enviado"});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Asistencia asistencia = new Asistencia();
                asistencia.setHora(cursor.getString(cursor.getColumnIndexOrThrow(ContractAsistenciasLocal.Columnas.HORA)));
                asistencia.setRegistrado(cursor.getString(cursor.getColumnIndexOrThrow(ContractAsistenciasLocal.Columnas.REGISTRADO)));
                operadores.add(asistencia);
            } while (cursor.moveToNext() && cursor!=null);
        }
        Log.i("Database","si es 0 SÍ tuvo marcacion "+operadores.size()+"");

        if(operadores.size() == 0){
            id = true;

        }

        Log.i("Database","tiene marcacion? "+ id);
        return id;
    }

    public String getHoraMarcacion(){
        String hora_marcacion = "";

        String selectQuery = "SELECT * FROM " + ContractHoraMarcacion.TABLE_NAME;
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{});

        if (cursor.moveToFirst()) {
            do {
                hora_marcacion = cursor.getString(cursor.getColumnIndexOrThrow(ContractHoraMarcacion.Columnas.HORA));
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        Log.i("Database", "get hora marcacion: "+ hora_marcacion);
        return hora_marcacion;

    }

    public ArrayList<Asistencia> getNoRegistradosFaltantes() {
        ArrayList<Asistencia> operadores = new ArrayList<Asistencia>();

        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-5"));
        Date currentLocalTime = cal.getTime();

        DateFormat hour = new SimpleDateFormat("HH:mm:ss");
        hour.setTimeZone(TimeZone.getTimeZone("GMT-5"));
        String horaser = hour.format(currentLocalTime);

        // Select All Query
        String selectQuery = "SELECT * FROM " + ContractAsistenciasLocal.TABLE_NAME +
                " WHERE " +
                ContractAsistenciasLocal.Columnas.REGISTRADO + "=? ";
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{"no enviado"});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Asistencia asistencia = new Asistencia();
                asistencia.setHora(cursor.getString(cursor.getColumnIndexOrThrow(ContractAsistenciasLocal.Columnas.HORA)));
                asistencia.setRegistrado(cursor.getString(cursor.getColumnIndexOrThrow(ContractAsistenciasLocal.Columnas.REGISTRADO)));
                operadores.add(asistencia);
            } while (cursor.moveToNext() && cursor!=null);
        }
        Log.i("OPERADORES",operadores.size()+"");
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return operadores;
    }

    public ArrayList<Asistencia> getHoraInsert() {
        ArrayList<Asistencia> operadores = new ArrayList<Asistencia>();
        operadores.add(new Asistencia("-","-","-"));

        // Obtener la hora actual
        Calendar calendar = Calendar.getInstance();

        int segundos = calendar.get(Calendar.SECOND);
        if (segundos < 30) {
            calendar.set(Calendar.SECOND, 0);

            // Formatear la hora en un formato específico
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
            String horaFormateada = dateFormat.format(calendar.getTime());


            // Select All Query
            String selectQuery = "SELECT * FROM " + ContractAsistenciasLocal.TABLE_NAME +
                    " WHERE " +
                    ContractAsistenciasLocal.Columnas.HORA + "=? AND " +
                    ContractAsistenciasLocal.Columnas.REGISTRADO + " =? ";
            db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, new String[]{horaFormateada,"no enviado"});

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    Asistencia asistencia = new Asistencia();
                    asistencia.setHora(cursor.getString(cursor.getColumnIndexOrThrow(ContractAsistenciasLocal.Columnas.HORA)));
                    asistencia.setRegistrado(cursor.getString(cursor.getColumnIndexOrThrow(ContractAsistenciasLocal.Columnas.REGISTRADO)));
                    operadores.add(asistencia);
                } while (cursor.moveToNext() && cursor!=null);
            }
            Log.i("OPERADORESdav",operadores.size()+"");
            // closing connection
            cursor.close();
            db.close();
            // returning lables

        } else {
            calendar.add(Calendar.MINUTE, 1);
            calendar.set(Calendar.SECOND, 0);
        }


        return operadores;
    }

    public boolean VerificarHora(int seg_espera){

        boolean id = false;

        Calendar calendar = Calendar.getInstance();

        int segundos = calendar.get(Calendar.SECOND);
        if (segundos < 30) {
            calendar.set(Calendar.SECOND, 0);

            // Formatear la hora en un formato específico
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
            String horaFormateada = dateFormat.format(calendar.getTime());
        }


        return id;

    }

    public boolean getTablaAsistencia() {
        boolean valor = false;
        String count = "0";

        // Select All Query
        String selectQuery = "SELECT COUNT(*) as 'contador' FROM " + ContractAsistenciasLocal.TABLE_NAME;
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                valor = true;
                count = cursor.getString(cursor.getColumnIndexOrThrow("contador"));
            } while (cursor.moveToNext() && cursor!=null);
        }
        //Log.i("OPERADORES",operadores.size()+"");
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        if(count.equalsIgnoreCase("0")){
            return false;
        }else{
            return true;
        }
    }

    public List<Asistencia> getHorasInsert() {
        boolean valor = false;
        String count = "0";
        ArrayList<Asistencia> n = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT * FROM " + ContractAsistenciasLocal.TABLE_NAME +
                " WHERE " + ContractAsistenciasLocal.Columnas.REGISTRADO + " =? ";
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{"no enviado"});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                valor = true;
                Asistencia asistencia = new Asistencia();
                asistencia.setHora(cursor.getString(cursor.getColumnIndexOrThrow("hora")));
                asistencia.setEstado(cursor.getString(cursor.getColumnIndexOrThrow("estado")));
                asistencia.setRegistrado(cursor.getString(cursor.getColumnIndexOrThrow("registrado")));
                n.add(asistencia);
            } while (cursor.moveToNext() && cursor!=null);
        }
        //Log.i("OPERADORES",operadores.size()+"");
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return n;
    }

    public List<String> getPdvs() {
        boolean valor = false;
        String count = "0";
        ArrayList<String> n = new ArrayList<>();
        n.add("Seleccione");

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String fechaActual = dateFormat.format(calendar.getTime());

        // Select All Query
        String selectQuery = "SELECT * FROM " + ContractPharmaValue.POS +
                " WHERE " + ContractPharmaValue.Columnas.FECHA_VISITA + " =? ";
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{fechaActual});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                valor = true;
                n.add(cursor.getString(cursor.getColumnIndexOrThrow("pos_name")));
            } while (cursor.moveToNext() && cursor!=null);
        }
        //Log.i("OPERADORES",operadores.size()+"");
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return n;
    }

    public String getHoraFin(String pos_id) {
        String hora= "";

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String fechaActual = dateFormat.format(calendar.getTime());

        // Select All Query
        String selectQuery = "SELECT * FROM " + ContractPharmaValue.POS +
                " WHERE " + ContractPharmaValue.Columnas.FECHA_VISITA + " =? AND " +
                ContractPharmaValue.Columnas.POS_ID + " =? LIMIT 1";
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{fechaActual, pos_id});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                //valor = true;
                hora = (cursor.getString(cursor.getColumnIndexOrThrow("hora_fin")));
            } while (cursor.moveToNext() && cursor!=null);
        }
        //Log.i("OPERADORES",operadores.size()+"");
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return hora;
    }

}