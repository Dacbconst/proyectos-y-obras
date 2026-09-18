package com.luckyecuador.app.PintucoAPP.DataBase;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import androidx.annotation.Nullable;
import android.text.TextUtils;

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
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertAsistencia;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertCanjes;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertEjecucionMateriales;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertEvidencias;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertLogisticoRelevo;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertMCIPdv;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertProyectosContacto;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertPagoFactura;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertProforma;
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
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertRastreo;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertShare;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertValores;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractNotificacion;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractPrecios;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractPreguntas;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractRangosPrecios;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractRangosPreciosSku;
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


/**
 * Created by Lucky Ecuador on 15/07/2016.
 */
public class Provider extends ContentProvider {
    /**
     * Nombre de la base de datos
     */
    //public static final String DATABASE_NAME = "colgate_transf.db";
    public static final String DATABASE_NAME = "Pintuco_V1.39.db";
    /**
     * Versión actual de la base de datos
     */
    private static final int DATABASE_VERSION = 1;
    /**
     * Instancia global del Content Resolver
     */
    private ContentResolver resolver;
    /**
     * Instancia del administrador de BD
     */
    private DatabaseHelper databaseHelper;

    @Override
    public boolean onCreate() {
        // Inicializando gestor BD
        databaseHelper = new DatabaseHelper(
                getContext(),
                DATABASE_NAME,
                null,
                DATABASE_VERSION
        );

        resolver = getContext().getContentResolver();

        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        // Obtener base de datos
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        Cursor c;
        // Comparar Uri
        if (ContractPharmaValue.uriMatcher.match(uri)== ContractPharmaValue.ALLROWS) {
            // Consultando todos los registros
            c = db.query(ContractPharmaValue.POS, projection,
                    selection, selectionArgs,
                    null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractPharmaValue.CONTENT_URI);
        }else if (ContractPharmaValue.uriMatcher.match(uri)== ContractPharmaValue.SINGLE_ROW) {
            // Consultando un solo registro basado en el Id del Uri
            long idGasto = ContentUris.parseId(uri);
            c = db.query(ContractPharmaValue.POS, projection,
                    ContractPharmaValue.Columnas._ID + " = " + idGasto,
                    selectionArgs, null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractPharmaValue.CONTENT_URI);
        }

        else if (ContractPrecios.uriMatcher.match(uri)== ContractPrecios.ALLROWS) {
            // Consultando todos los registros
            c = db.query(ContractPrecios.PRECIOS, projection,
                    selection, selectionArgs,
                    null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractPrecios.CONTENT_URI);
        }else if (ContractPrecios.uriMatcher.match(uri)== ContractPrecios.SINGLE_ROW) {
            // Consultando un solo registro basado en el Id del Uri
            long idGasto = ContentUris.parseId(uri);
            c = db.query(ContractPrecios.PRECIOS, projection,
                    ContractPrecios.Columnas._ID + " = " + idGasto,
                    selectionArgs, null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractPrecios.CONTENT_URI);
        }




        else if (ContractPopSugerido.uriMatcher.match(uri)== ContractPopSugerido.ALLROWS) {
            // Consultando todos los registros
            c = db.query(ContractPopSugerido.POPSUGERIDO, projection,
                    selection, selectionArgs,
                    null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractPopSugerido.CONTENT_URI);
        }else if (ContractPopSugerido.uriMatcher.match(uri)== ContractPopSugerido.SINGLE_ROW) {
            // Consultando un solo registro basado en el Id del Uri
            long idGasto = ContentUris.parseId(uri);
            c = db.query(ContractPopSugerido.POPSUGERIDO, projection,
                    ContractPopSugerido.Columnas._ID + " = " + idGasto,
                    selectionArgs, null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractPopSugerido.CONTENT_URI);
        }

        else if (ContractCausalesProdMalEst.uriMatcher.match(uri)== ContractCausalesProdMalEst.ALLROWS) {
            // Consultando todos los registros
            c = db.query(ContractCausalesProdMalEst.CAUSALES_PROD_MAL_EST, projection,
                    selection, selectionArgs,
                    null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractCausalesProdMalEst.CONTENT_URI);
        }else if (ContractCausalesProdMalEst.uriMatcher.match(uri)== ContractCausalesProdMalEst.SINGLE_ROW) {
            // Consultando un solo registro basado en el Id del Uri
            long idGasto = ContentUris.parseId(uri);
            c = db.query(ContractCausalesProdMalEst.CAUSALES_PROD_MAL_EST, projection,
                    ContractCausalesProdMalEst.Columnas._ID + " = " + idGasto,
                    selectionArgs, null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractCausalesProdMalEst.CONTENT_URI);
        }


        else if (ContractVersiones.uriMatcher.match(uri)== ContractVersiones.ALLROWS) {
            // Consultando todos los registros
            c = db.query(ContractVersiones.VERSIONES, projection,
                    selection, selectionArgs,
                    null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractVersiones.CONTENT_URI);
        }else if (ContractVersiones.uriMatcher.match(uri)== ContractVersiones.SINGLE_ROW) {
            // Consultando un solo registro basado en el Id del Uri
            long idGasto = ContentUris.parseId(uri);
            c = db.query(ContractVersiones.VERSIONES, projection,
                    ContractVersiones.Columnas._ID + " = " + idGasto,
                    selectionArgs, null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractVersiones.CONTENT_URI);
        }

        else if (ContractComboCanjes.uriMatcher.match(uri)== ContractComboCanjes.ALLROWS) {
            // Consultando todos los registros
            c = db.query(ContractComboCanjes.COMBO_CANJES, projection,
                    selection, selectionArgs,
                    null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractComboCanjes.CONTENT_URI);
        }else if (ContractComboCanjes.uriMatcher.match(uri)== ContractComboCanjes.SINGLE_ROW) {
            // Consultando un solo registro basado en el Id del Uri
            long idGasto = ContentUris.parseId(uri);
            c = db.query(ContractComboCanjes.COMBO_CANJES, projection,
                    ContractComboCanjes.Columnas._ID + " = " + idGasto,
                    selectionArgs, null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractComboCanjes.CONTENT_URI);
        }

        else if (ContractCausalesMCI.uriMatcher.match(uri)== ContractCausalesMCI.ALLROWS) {
            // Consultando todos los registros
            c = db.query(ContractCausalesMCI.CAUSALES_MCI, projection,
                    selection, selectionArgs,
                    null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractCausalesMCI.CONTENT_URI);
        }else if (ContractCausalesMCI.uriMatcher.match(uri)== ContractCausalesMCI.SINGLE_ROW) {
            // Consultando un solo registro basado en el Id del Uri
            long idGasto = ContentUris.parseId(uri);
            c = db.query(ContractCausalesMCI.CAUSALES_MCI, projection,
                    ContractCausalesMCI.Columnas._ID + " = " + idGasto,
                    selectionArgs, null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractCausalesMCI.CONTENT_URI);
        }

        else if (ContractRangosPrecios.uriMatcher.match(uri) == ContractRangosPrecios.ALLROWS) {
            c = db.query(ContractRangosPrecios.RANGOS_PRECIOS, projection,
                    selection, selectionArgs,
                    null, null, sortOrder);
            c.setNotificationUri(resolver, ContractRangosPrecios.CONTENT_URI);
        } else if (ContractRangosPrecios.uriMatcher.match(uri) == ContractRangosPrecios.SINGLE_ROW) {
            long id = ContentUris.parseId(uri);
            c = db.query(ContractRangosPrecios.RANGOS_PRECIOS, projection,
                    ContractRangosPrecios.Columnas._ID + " = " + id,
                    selectionArgs, null, null, sortOrder);
            c.setNotificationUri(resolver, ContractRangosPrecios.CONTENT_URI);
        }

        else if (ContractRangosPreciosSku.uriMatcher.match(uri) == ContractRangosPreciosSku.ALLROWS) {
            c = db.query(ContractRangosPreciosSku.RANGOS_PRECIOS_SKU, projection,
                    selection, selectionArgs,
                    null, null, sortOrder);
            c.setNotificationUri(resolver, ContractRangosPreciosSku.CONTENT_URI);
        } else if (ContractRangosPreciosSku.uriMatcher.match(uri) == ContractRangosPreciosSku.SINGLE_ROW) {
            long id = ContentUris.parseId(uri);
            c = db.query(ContractRangosPreciosSku.RANGOS_PRECIOS_SKU, projection,
                    ContractRangosPreciosSku.Columnas._ID + " = " + id,
                    selectionArgs, null, null, sortOrder);
            c.setNotificationUri(resolver, ContractRangosPreciosSku.CONTENT_URI);
        }

        else if (ContractCausalesOSA.uriMatcher.match(uri)== ContractCausalesOSA.ALLROWS) {
            // Consultando todos los registros
            c = db.query(ContractCausalesOSA.CAUSALES_OSA, projection,
                    selection, selectionArgs,
                    null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractCausalesOSA.CONTENT_URI);
        }else if (ContractCausalesOSA.uriMatcher.match(uri)== ContractCausalesOSA.SINGLE_ROW) {
            // Consultando un solo registro basado en el Id del Uri
            long idGasto = ContentUris.parseId(uri);
            c = db.query(ContractCausalesOSA.CAUSALES_OSA, projection,
                    ContractCausalesOSA.Columnas._ID + " = " + idGasto,
                    selectionArgs, null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractCausalesOSA.CONTENT_URI);
        }

        else if (ContractAlertas.uriMatcher.match(uri)== ContractAlertas.ALLROWS) {
            // Consultando todos los registros
            c = db.query(ContractAlertas.ALERTAS, projection,
                    selection, selectionArgs,
                    null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractAlertas.CONTENT_URI);
        }else if (ContractAlertas.uriMatcher.match(uri)== ContractAlertas.SINGLE_ROW) {
            // Consultando un solo registro basado en el Id del Uri
            long idGasto = ContentUris.parseId(uri);
            c = db.query(ContractAlertas.ALERTAS, projection,
                    ContractAlertas.Columnas._ID + " = " + idGasto,
                    selectionArgs, null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractAlertas.CONTENT_URI);
        }



        else if (ContractTiempoAlmuerzo.uriMatcher.match(uri)== ContractTiempoAlmuerzo.ALLROWS) {
            // Consultando todos los registros
            c = db.query(ContractTiempoAlmuerzo.REPO_TIEMPO_ALMUERZO, projection,
                    selection, selectionArgs,
                    null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractTiempoAlmuerzo.CONTENT_URI);
        }else if (ContractTiempoAlmuerzo.uriMatcher.match(uri)== ContractTiempoAlmuerzo.SINGLE_ROW) {
            // Consultando un solo registro basado en el Id del Uri
            long idGasto = ContentUris.parseId(uri);
            c = db.query(ContractTiempoAlmuerzo.REPO_TIEMPO_ALMUERZO, projection,
                    ContractTiempoAlmuerzo.Columnas._ID + " = " + idGasto,
                    selectionArgs, null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractTiempoAlmuerzo.CONTENT_URI);
        }




        else if (ContractPDI.uriMatcher.match(uri)== ContractPDI.ALLROWS) {
            // Consultando todos los registros
            c = db.query(ContractPDI.PDI, projection,
                    selection, selectionArgs,
                    null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractPDI.CONTENT_URI);
        }else if (ContractPDI.uriMatcher.match(uri)== ContractPDI.SINGLE_ROW) {
            // Consultando un solo registro basado en el Id del Uri
            long idGasto = ContentUris.parseId(uri);
            c = db.query(ContractPDI.PDI, projection,
                    ContractPDI.Columnas._ID + " = " + idGasto,
                    selectionArgs, null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractPDI.CONTENT_URI);
        }

        else if (ContractLog.uriMatcher.match(uri)== ContractLog.ALLROWS) {
            // Consultando todos los registros
            c = db.query(ContractLog.LOG, projection,
                    selection, selectionArgs,
                    null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractLog.CONTENT_URI);
        }else if (ContractLog.uriMatcher.match(uri)== ContractLog.SINGLE_ROW) {
            // Consultando un solo registro basado en el Id del Uri
            long idGasto = ContentUris.parseId(uri);
            c = db.query(ContractLog.LOG, projection,
                    ContractLog.Columnas._ID + " = " + idGasto,
                    selectionArgs, null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractLog.CONTENT_URI);
        }

        else if (ContractPrioritario.uriMatcher.match(uri)== ContractPrioritario.ALLROWS) {
            // Consultando todos los registros
            c = db.query(ContractPrioritario.PRIORITARIO, projection,
                    selection, selectionArgs,
                    null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractPrioritario.CONTENT_URI);
        }else if (ContractPrioritario.uriMatcher.match(uri)== ContractPrioritario.SINGLE_ROW) {
            // Consultando un solo registro basado en el Id del Uri
            long idGasto = ContentUris.parseId(uri);
            c = db.query(ContractPrioritario.PRIORITARIO, projection,
                    ContractPrioritario.Columnas._ID + " = " + idGasto,
                    selectionArgs, null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractPrioritario.CONTENT_URI);
        }

        else if (ContractTareas.uriMatcher.match(uri)== ContractTareas.ALLROWS) {
            // Consultando todos los registros
            c = db.query(ContractTareas.TAREA, projection,
                    selection, selectionArgs,
                    null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractTareas.CONTENT_URI);
        }else if (ContractTareas.uriMatcher.match(uri)== ContractTareas.SINGLE_ROW) {
            // Consultando un solo registro basado en el Id del Uri
            long idGasto = ContentUris.parseId(uri);
            c = db.query(ContractTareas.TAREA, projection,
                    ContractTareas.Columnas._ID + " = " + idGasto,
                    selectionArgs, null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractTareas.CONTENT_URI);
        }

        else if(ContractTests.uriMatcher.match(uri)== ContractTests.ALLROWS){
            // Consultando todos los registros
            c = db.query(ContractTests.TABLE_TEST, projection,
                    selection, selectionArgs,
                    null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractTests.CONTENT_URI);
        }else if(ContractTests.uriMatcher.match(uri)== ContractTests.SINGLE_ROW){
            // Consultando un solo registro basado en el Id del Uri
            long idGasto = ContentUris.parseId(uri);
            c = db.query(ContractTests.TABLE_TEST, projection,
                    ContractTests.Columnas._ID + " = " + idGasto,
                    selectionArgs, null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractTests.CONTENT_URI);
        }




        else if (ContractPreguntas.uriMatcher.match(uri)== ContractPreguntas.ALLROWS) {
            // Consultando todos los registros
            c = db.query(ContractPreguntas.TABLE_QUEST, projection,
                    selection, selectionArgs,
                    null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractPreguntas.CONTENT_URI);
        }else if (ContractPreguntas.uriMatcher.match(uri)== ContractPreguntas.SINGLE_ROW) {
            // Consultando un solo registro basado en el Id del Uri
            long idGasto = ContentUris.parseId(uri);
            c = db.query(ContractPreguntas.TABLE_QUEST, projection,
                    ContractPreguntas.Columnas._ID + " = " + idGasto,
                    selectionArgs, null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractPreguntas.CONTENT_URI);
        }


        else if(ContractJustificacion.uriMatcher.match(uri)== ContractJustificacion.ALLROWS){
            // Consultando todos los registros
            c = db.query(ContractJustificacion.JUSTIFICACIONES, projection,
                    selection, selectionArgs,
                    null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractJustificacion.CONTENT_URI);
        }else if(ContractJustificacion.uriMatcher.match(uri)== ContractJustificacion.SINGLE_ROW){
            // Consultando un solo registro basado en el Id del Uri
            long idGasto = ContentUris.parseId(uri);
            c = db.query(ContractJustificacion.JUSTIFICACIONES, projection,
                    ContractJustificacion.Columnas._ID + " = " + idGasto,
                    selectionArgs, null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractJustificacion.CONTENT_URI);
        }

        else if(ContractTipoPrecios.uriMatcher.match(uri)== ContractTipoPrecios.ALLROWS){
            // Consultando todos los registros
            c = db.query(ContractTipoPrecios.TIPO_PRECIOS, projection,
                    selection, selectionArgs,
                    null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractTipoPrecios.CONTENT_URI);
        }else if(ContractTipoPrecios.uriMatcher.match(uri)== ContractTipoPrecios.SINGLE_ROW){
            // Consultando un solo registro basado en el Id del Uri
            long idGasto = ContentUris.parseId(uri);
            c = db.query(ContractTipoPrecios.TIPO_PRECIOS, projection,
                    ContractTipoPrecios.Columnas._ID + " = " + idGasto,
                    selectionArgs, null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractTipoPrecios.CONTENT_URI);
        }


        else if(ContractMarcasBlancas.uriMatcher.match(uri)== ContractMarcasBlancas.ALLROWS){
            // Consultando todos los registros
            c = db.query(ContractMarcasBlancas.MARCAS_BLANCAS, projection,
                    selection, selectionArgs,
                    null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractMarcasBlancas.CONTENT_URI);
        }else if(ContractMarcasBlancas.uriMatcher.match(uri)== ContractMarcasBlancas.SINGLE_ROW){
            // Consultando un solo registro basado en el Id del Uri
            long idGasto = ContentUris.parseId(uri);
            c = db.query(ContractMarcasBlancas.MARCAS_BLANCAS, projection,
                    ContractMarcasBlancas.Columnas._ID + " = " + idGasto,
                    selectionArgs, null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractMarcasBlancas.CONTENT_URI);
        }

        else if (ContractPreciosPvc.uriMatcher.match(uri)== ContractPreciosPvc.ALLROWS) {
            // Consultando todos los registros
            c = db.query(ContractPreciosPvc.PRECIOS_PVC, projection,
                    selection, selectionArgs,
                    null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractPreciosPvc.CONTENT_URI);
        }else if (ContractPreciosPvc.uriMatcher.match(uri)== ContractPreciosPvc.SINGLE_ROW) {
            // Consultando un solo registro basado en el Id del Uri
            long idGasto = ContentUris.parseId(uri);
            c = db.query(ContractPreciosPvc.PRECIOS_PVC, projection,
                    ContractPreciosPvc.Columnas._ID + " = " + idGasto,
                    selectionArgs, null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractPreciosPvc.CONTENT_URI);
        }


        else if (ContractTipoExh.uriMatcher.match(uri)== ContractTipoExh.ALLROWS) {
            // Consultando todos los registros
            c = db.query(ContractTipoExh.TABLE_NAME, projection,
                    selection, selectionArgs,
                    null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractTipoExh.CONTENT_URI);
        }else if (ContractTipoExh.uriMatcher.match(uri)== ContractTipoExh.SINGLE_ROW) {
            // Consultando un solo registro basado en el Id del Uri
            long idGasto = ContentUris.parseId(uri);
            c = db.query(ContractTipoExh.TABLE_NAME, projection,
                    ContractTipoExh.Columnas._ID + " = " + idGasto,
                    selectionArgs, null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractTipoExh.CONTENT_URI);
        }




        else if (ContractPromociones.uriMatcher.match(uri)== ContractPromociones.ALLROWS) {
            // Consultando todos los registros
            c = db.query(ContractPromociones.PROMOCIONES, projection,
                    selection, selectionArgs,
                    null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractPromociones.CONTENT_URI);

        }else if (ContractPromociones.uriMatcher.match(uri)== ContractPromociones.SINGLE_ROW) {
            // Consultando un solo registro basado en el Id del Uri
            long idGasto = ContentUris.parseId(uri);
            c = db.query(ContractPromociones.PROMOCIONES, projection,
                    ContractPromociones.Columnas._ID + " = " + idGasto,
                    selectionArgs, null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractPromociones.CONTENT_URI);
        }

        else if (ContractRotacion.uriMatcher.match(uri)== ContractRotacion.ALLROWS) {
            // Consultando todos los registros
            c = db.query(ContractRotacion.ROTACION, projection,
                    selection, selectionArgs,
                    null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractRotacion.CONTENT_URI);
        }else if (ContractRotacion.uriMatcher.match(uri)== ContractRotacion.SINGLE_ROW) {
            // Consultando un solo registro basado en el Id del Uri
            long idGasto = ContentUris.parseId(uri);
            c = db.query(ContractRotacion.ROTACION, projection,
                    ContractRotacion.Columnas._ID + " = " + idGasto,
                    selectionArgs, null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractRotacion.CONTENT_URI);
        }

        else if (ContractPortafolioProductos.uriMatcher.match(uri)== ContractPortafolioProductos.ALLROWS) {
            // Consultando todos los registros
            c = db.query(ContractPortafolioProductos.PORTAFOLIOPRODUCTOS, projection,
                    selection, selectionArgs,
                    null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractPortafolioProductos.CONTENT_URI);
        }else if (ContractPortafolioProductos.uriMatcher.match(uri)== ContractPortafolioProductos.SINGLE_ROW) {
            // Consultando un solo registro basado en el Id del Uri
            long idGasto = ContentUris.parseId(uri);
            c = db.query(ContractPortafolioProductos.PORTAFOLIOPRODUCTOS, projection,
                    ContractPortafolioProductos.Columnas._ID + " = " + idGasto,
                    selectionArgs, null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractPortafolioProductos.CONTENT_URI);
        }

        else if (ContractPortafolioProductosAASS.uriMatcher.match(uri)== ContractPortafolioProductosAASS.ALLROWS) {
            // Consultando todos los registros
            c = db.query(ContractPortafolioProductosAASS.PORTAFOLIOPRODUCTOS_AASS, projection,
                    selection, selectionArgs,
                    null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractPortafolioProductosAASS.CONTENT_URI);
        }else if (ContractPortafolioProductosAASS.uriMatcher.match(uri)== ContractPortafolioProductosAASS.SINGLE_ROW) {
            // Consultando un solo registro basado en el Id del Uri
            long idGasto = ContentUris.parseId(uri);
            c = db.query(ContractPortafolioProductosAASS.PORTAFOLIOPRODUCTOS_AASS, projection,
                    ContractPortafolioProductosAASS.Columnas._ID + " = " + idGasto,
                    selectionArgs, null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractPortafolioProductosAASS.CONTENT_URI);
        }

        else if (ContractPortafolioProductosMAYO.uriMatcher.match(uri)== ContractPortafolioProductosMAYO.ALLROWS) {
            // Consultando todos los registros
            c = db.query(ContractPortafolioProductosMAYO.PORTAFOLIOPRODUCTOS_MAYO, projection,
                    selection, selectionArgs,
                    null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractPortafolioProductosMAYO.CONTENT_URI);
        }else if (ContractPortafolioProductosMAYO.uriMatcher.match(uri)== ContractPortafolioProductosMAYO.SINGLE_ROW) {
            // Consultando un solo registro basado en el Id del Uri
            long idGasto = ContentUris.parseId(uri);
            c = db.query(ContractPortafolioProductosMAYO.PORTAFOLIOPRODUCTOS_MAYO, projection,
                    ContractPortafolioProductosMAYO.Columnas._ID + " = " + idGasto,
                    selectionArgs, null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractPortafolioProductosMAYO.CONTENT_URI);
        }

        /*
         *      INSERTS
         */

        else if (ContractInsertPrecios.uriMatcher.match(uri)==ContractInsertPrecios.ALLROWS) {
            // Consultando todos los registros
            c = db.query(ContractInsertPrecios.INSERT_PRECIOS, projection,
                    selection, selectionArgs,
                    null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractInsertPrecios.CONTENT_URI);
        }else if (ContractInsertPrecios.uriMatcher.match(uri)==ContractInsertPrecios.SINGLE_ROW) {
            // Consultando un solo registro basado en el Id del Uri
            long idGasto = ContentUris.parseId(uri);
            c = db.query(ContractInsertPrecios.INSERT_PRECIOS, projection,
                    ContractInsertPrecios.Columnas._ID + " = " + idGasto,
                    selectionArgs, null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractInsertPrecios.CONTENT_URI);
        }

        else if (ContractInsertPacks.uriMatcher.match(uri)==ContractInsertPacks.ALLROWS) {
            // Consultando todos los registros
            c = db.query(ContractInsertPacks.INSERT_PACKS, projection,
                    selection, selectionArgs,
                    null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractInsertPacks.CONTENT_URI);
        }else if (ContractInsertPacks.uriMatcher.match(uri)==ContractInsertPacks.SINGLE_ROW) {
            // Consultando un solo registro basado en el Id del Uri
            long idGasto = ContentUris.parseId(uri);
            c = db.query(ContractInsertPacks.INSERT_PACKS, projection,
                    ContractInsertPacks.Columnas._ID + " = " + idGasto,
                    selectionArgs, null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractInsertPacks.CONTENT_URI);
        }


        else if (ContractInsertPropensosYProdMalEst.uriMatcher.match(uri)==ContractInsertPropensosYProdMalEst.ALLROWS) {
            // Consultando todos los registros
            c = db.query(ContractInsertPropensosYProdMalEst.INSERT_PROP_MAL_EST, projection,
                    selection, selectionArgs,
                    null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractInsertPropensosYProdMalEst.CONTENT_URI);
        }else if (ContractInsertPropensosYProdMalEst.uriMatcher.match(uri)==ContractInsertPropensosYProdMalEst.SINGLE_ROW) {
            // Consultando un solo registro basado en el Id del Uri
            long idGasto = ContentUris.parseId(uri);
            c = db.query(ContractInsertPropensosYProdMalEst.INSERT_PROP_MAL_EST, projection,
                    ContractInsertPropensosYProdMalEst.Columnas._ID + " = " + idGasto,
                    selectionArgs, null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractInsertPropensosYProdMalEst.CONTENT_URI);
        }



        else if (ContractInsertAlmuerzo.uriMatcher.match(uri)==ContractInsertAlmuerzo.ALLROWS) {
            // Consultando todos los registros
            c = db.query(ContractInsertAlmuerzo.INSERT_ALMUERZO, projection,
                    selection, selectionArgs,
                    null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractInsertAlmuerzo.CONTENT_URI);
        }else if (ContractInsertAlmuerzo.uriMatcher.match(uri)==ContractInsertAlmuerzo.SINGLE_ROW) {
            // Consultando un solo registro basado en el Id del Uri
            long idGasto = ContentUris.parseId(uri);
            c = db.query(ContractInsertAlmuerzo.INSERT_ALMUERZO, projection,
                    ContractInsertAlmuerzo.Columnas._ID + " = " + idGasto,
                    selectionArgs, null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractInsertAlmuerzo.CONTENT_URI);
        }


        else if (ContractInsertProyectosContacto.uriMatcher.match(uri)==ContractInsertProyectosContacto.ALLROWS) {
            // Consultando todos los registros
            c = db.query(ContractInsertProyectosContacto.INSERT_PROYECTOS_CONTACTO, projection,
                    selection, selectionArgs,
                    null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractInsertProyectosContacto.CONTENT_URI);
        }else if (ContractInsertProyectosContacto.uriMatcher.match(uri)==ContractInsertProyectosContacto.SINGLE_ROW) {
            // Consultando un solo registro basado en el Id del Uri
            long idGasto = ContentUris.parseId(uri);
            c = db.query(ContractInsertProyectosContacto.INSERT_PROYECTOS_CONTACTO, projection,
                    ContractInsertProyectosContacto.Columnas._ID + " = " + idGasto,
                    selectionArgs, null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractInsertProyectosContacto.CONTENT_URI);
        }


        else if (ContractInsertProforma.uriMatcher.match(uri)==ContractInsertProforma.ALLROWS) {
            c = db.query(ContractInsertProforma.INSERT_PROFORMA, projection,
                    selection, selectionArgs,
                    null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractInsertProforma.CONTENT_URI);
        }else if (ContractInsertProforma.uriMatcher.match(uri)==ContractInsertProforma.SINGLE_ROW) {
            long idProforma = ContentUris.parseId(uri);
            c = db.query(ContractInsertProforma.INSERT_PROFORMA, projection,
                    ContractInsertProforma.Columnas._ID + " = " + idProforma,
                    selectionArgs, null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractInsertProforma.CONTENT_URI);
        }

        else if (ContractInsertPagoFactura.uriMatcher.match(uri)==ContractInsertPagoFactura.ALLROWS) {
            c = db.query(ContractInsertPagoFactura.INSERT_PAGO_FACTURA, projection,
                    selection, selectionArgs,
                    null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractInsertPagoFactura.CONTENT_URI);
        }else if (ContractInsertPagoFactura.uriMatcher.match(uri)==ContractInsertPagoFactura.SINGLE_ROW) {
            long idPagoFactura = ContentUris.parseId(uri);
            c = db.query(ContractInsertPagoFactura.INSERT_PAGO_FACTURA, projection,
                    ContractInsertPagoFactura.Columnas._ID + " = " + idPagoFactura,
                    selectionArgs, null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractInsertPagoFactura.CONTENT_URI);
        }



        else if (ContractInsertPdvFotografico.uriMatcher.match(uri)==ContractInsertPdvFotografico.ALLROWS) {
            // Consultando todos los registros
            c = db.query(ContractInsertPdvFotografico.INSERT_PVD_FOTOGRAFICO, projection,
                    selection, selectionArgs,
                    null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractInsertPdvFotografico.CONTENT_URI);
        }else if (ContractInsertPdvFotografico.uriMatcher.match(uri)==ContractInsertPdvFotografico.SINGLE_ROW) {
            // Consultando un solo registro basado en el Id del Uri
            long idGasto = ContentUris.parseId(uri);
            c = db.query(ContractInsertPdvFotografico.INSERT_PVD_FOTOGRAFICO, projection,
                    ContractInsertPdvFotografico.Columnas._ID + " = " + idGasto,
                    selectionArgs, null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractInsertPdvFotografico.CONTENT_URI);
        }


        else if (ContractInsertVentas.uriMatcher.match(uri)==ContractInsertVentas.ALLROWS) {
            // Consultando todos los registros
            c = db.query(ContractInsertVentas.INSERT_VENTAS, projection,
                    selection, selectionArgs,
                    null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractInsertVentas.CONTENT_URI);
        }else if (ContractInsertVentas.uriMatcher.match(uri)==ContractInsertVentas.SINGLE_ROW) {
            // Consultando un solo registro basado en el Id del Uri
            long idGasto = ContentUris.parseId(uri);
            c = db.query(ContractInsertVentas.INSERT_VENTAS, projection,
                    ContractInsertVentas.Columnas._ID + " = " + idGasto,
                    selectionArgs, null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractInsertVentas.CONTENT_URI);
        }

        else if (ContractInsertProdCaducar.uriMatcher.match(uri)==ContractInsertProdCaducar.ALLROWS) {
            // Consultando todos los registros
            c = db.query(ContractInsertProdCaducar.INSERT_PROD_CADUCAR, projection,
                    selection, selectionArgs,
                    null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractInsertProdCaducar.CONTENT_URI);
        }else if (ContractInsertProdCaducar.uriMatcher.match(uri)==ContractInsertProdCaducar.SINGLE_ROW) {
            // Consultando un solo registro basado en el Id del Uri
            long idGasto = ContentUris.parseId(uri);
            c = db.query(ContractInsertProdCaducar.INSERT_PROD_CADUCAR, projection,
                    ContractInsertProdCaducar.Columnas._ID + " = " + idGasto,
                    selectionArgs, null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractInsertProdCaducar.CONTENT_URI);
        }

        else if (ContractInsertSugeridos.uriMatcher.match(uri)==ContractInsertSugeridos.ALLROWS) {
            // Consultando todos los registros
            c = db.query(ContractInsertSugeridos.INSERT_SUGERIDOS, projection,
                    selection, selectionArgs,
                    null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractInsertSugeridos.CONTENT_URI);
        }else if (ContractInsertSugeridos.uriMatcher.match(uri)==ContractInsertSugeridos.SINGLE_ROW) {
            // Consultando un solo registro basado en el Id del Uri
            long idGasto = ContentUris.parseId(uri);
            c = db.query(ContractInsertSugeridos.INSERT_SUGERIDOS, projection,
                    ContractInsertSugeridos.Columnas._ID + " = " + idGasto,
                    selectionArgs, null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractInsertSugeridos.CONTENT_URI);
        }

        else if (ContractInsertCanjes.uriMatcher.match(uri)==ContractInsertCanjes.ALLROWS) {
            // Consultando todos los registros
            c = db.query(ContractInsertCanjes.INSERT_CANJES, projection,
                    selection, selectionArgs,
                    null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractInsertCanjes.CONTENT_URI);
        }else if (ContractInsertCanjes.uriMatcher.match(uri)==ContractInsertCanjes.SINGLE_ROW) {
            // Consultando un solo registro basado en el Id del Uri
            long idGasto = ContentUris.parseId(uri);
            c = db.query(ContractInsertCanjes.INSERT_CANJES, projection,
                    ContractInsertCanjes.Columnas._ID + " = " + idGasto,
                    selectionArgs, null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractInsertCanjes.CONTENT_URI);
        }

        else if (ContractInsertMaterialesRecibidos.uriMatcher.match(uri)==ContractInsertMaterialesRecibidos.ALLROWS) {
            // Consultando todos los registros
            c = db.query(ContractInsertMaterialesRecibidos.INSERT_MATERIALES_RECIBIDOS, projection,
                    selection, selectionArgs,
                    null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractInsertMaterialesRecibidos.CONTENT_URI);
        }else if (ContractInsertMaterialesRecibidos.uriMatcher.match(uri)==ContractInsertMaterialesRecibidos.SINGLE_ROW) {
            // Consultando un solo registro basado en el Id del Uri
            long idGasto = ContentUris.parseId(uri);
            c = db.query(ContractInsertMaterialesRecibidos.INSERT_MATERIALES_RECIBIDOS, projection,
                    ContractInsertMaterialesRecibidos.Columnas._ID + " = " + idGasto,
                    selectionArgs, null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractInsertMaterialesRecibidos.CONTENT_URI);
        }

        else if (ContractInsertEjecucionMateriales.uriMatcher.match(uri)==ContractInsertEjecucionMateriales.ALLROWS) {
            // Consultando todos los registros
            c = db.query(ContractInsertEjecucionMateriales.INSERT_EJECUCION_MATERIALES, projection,
                    selection, selectionArgs,
                    null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractInsertEjecucionMateriales.CONTENT_URI);
        }else if (ContractInsertEjecucionMateriales.uriMatcher.match(uri)==ContractInsertEjecucionMateriales.SINGLE_ROW) {
            // Consultando un solo registro basado en el Id del Uri
            long idGasto = ContentUris.parseId(uri);
            c = db.query(ContractInsertEjecucionMateriales.INSERT_EJECUCION_MATERIALES, projection,
                    ContractInsertEjecucionMateriales.Columnas._ID + " = " + idGasto,
                    selectionArgs, null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractInsertEjecucionMateriales.CONTENT_URI);
        }

        else if (ContractInsertMCIPdv.uriMatcher.match(uri)==ContractInsertMCIPdv.ALLROWS) {
            // Consultando todos los registros
            c = db.query(ContractInsertMCIPdv.INSERT_MCI, projection,
                    selection, selectionArgs,
                    null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractInsertMCIPdv.CONTENT_URI);
        }else if (ContractInsertMCIPdv.uriMatcher.match(uri)==ContractInsertMCIPdv.SINGLE_ROW) {
            // Consultando un solo registro basado en el Id del Uri
            long idGasto = ContentUris.parseId(uri);
            c = db.query(ContractInsertMCIPdv.INSERT_MCI, projection,
                    ContractInsertMCIPdv.Columnas._ID + " = " + idGasto,
                    selectionArgs, null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractInsertMCIPdv.CONTENT_URI);
        }

        else if (ContractInsertMallaCodificados.uriMatcher.match(uri)==ContractInsertMallaCodificados.ALLROWS) {
            // Consultando todos los registros
            c = db.query(ContractInsertMallaCodificados.INSERT_MALLA_CODIFICADOS, projection,
                    selection, selectionArgs,
                    null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractInsertMallaCodificados.CONTENT_URI);
        }else if (ContractInsertMallaCodificados.uriMatcher.match(uri)==ContractInsertMallaCodificados.SINGLE_ROW) {
            // Consultando un solo registro basado en el Id del Uri
            long idGasto = ContentUris.parseId(uri);
            c = db.query(ContractInsertMallaCodificados.INSERT_MALLA_CODIFICADOS, projection,
                    ContractInsertMallaCodificados.Columnas._ID + " = " + idGasto,
                    selectionArgs, null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractInsertMallaCodificados.CONTENT_URI);
        }

        else if (ContractInsertRotacion.uriMatcher.match(uri)==ContractInsertRotacion.ALLROWS) {
            // Consultando todos los registros
            c = db.query(ContractInsertRotacion.INSERT_ROTACION, projection,
                    selection, selectionArgs,
                    null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractInsertRotacion.CONTENT_URI);
        }else if (ContractInsertRotacion.uriMatcher.match(uri)==ContractInsertRotacion.SINGLE_ROW) {
            // Consultando un solo registro basado en el Id del Uri
            long idGasto = ContentUris.parseId(uri);
            c = db.query(ContractInsertRotacion.INSERT_ROTACION, projection,
                    ContractInsertRotacion.Columnas._ID + " = " + idGasto,
                    selectionArgs, null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractInsertRotacion.CONTENT_URI);
        }

        else if (ContractInsertPreguntas.uriMatcher.match(uri)==ContractInsertPreguntas.ALLROWS) {
            // Consultando todos los registros
            c = db.query(ContractInsertPreguntas.INSERT_PREGUNTAS, projection,
                    selection, selectionArgs,
                    null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractInsertPreguntas.CONTENT_URI);
        }else if (ContractInsertPreguntas.uriMatcher.match(uri)==ContractInsertPreguntas.SINGLE_ROW) {
            // Consultando un solo registro basado en el Id del Uri
            long idGasto = ContentUris.parseId(uri);
            c = db.query(ContractInsertPreguntas.INSERT_PREGUNTAS, projection,
                    ContractInsertPreguntas.Columnas._ID + " = " + idGasto,
                    selectionArgs, null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractInsertPreguntas.CONTENT_URI);
        }


        else if(ContractInsertResultadoPreguntas.uriMatcher.match(uri)==ContractInsertResultadoPreguntas.ALLROWS){
            // Consultando todos los registros
            c = db.query(ContractInsertResultadoPreguntas.INSERT_RESULTADO_PREGUNTAS, projection,
                    selection, selectionArgs,
                    null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractInsertResultadoPreguntas.CONTENT_URI);
        }else if(ContractInsertResultadoPreguntas.uriMatcher.match(uri)==ContractInsertResultadoPreguntas.SINGLE_ROW){
            // Consultando un solo registro basado en el Id del Uri
            long idGasto = ContentUris.parseId(uri);
            c = db.query(ContractInsertResultadoPreguntas.INSERT_RESULTADO_PREGUNTAS, projection,
                    ContractInsertResultadoPreguntas.Columnas._ID + " = " + idGasto,
                    selectionArgs, null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractInsertResultadoPreguntas.CONTENT_URI);
        }



        else if (ContractInsertPreciosSesion.uriMatcher.match(uri)==ContractInsertPreciosSesion.ALLROWS) {
            // Consultando todos los registros
            c = db.query(ContractInsertPreciosSesion.INSERT_PRECIOS_SESION, projection,
                    selection, selectionArgs,
                    null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractInsertPreciosSesion.CONTENT_URI);
        }else if (ContractInsertPreciosSesion.uriMatcher.match(uri)==ContractInsertPreciosSesion.SINGLE_ROW) {
            // Consultando un solo registro basado en el Id del Uri
            long idGasto = ContentUris.parseId(uri);
            c = db.query(ContractInsertPreciosSesion.INSERT_PRECIOS_SESION, projection,
                    ContractInsertPreciosSesion.Columnas._ID + " = " + idGasto,
                    selectionArgs, null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractInsertPreciosSesion.CONTENT_URI);
        }

        else if (ContractInsertExh.uriMatcher.match(uri)==ContractInsertExh.ALLROWS) {
            // Consultando todos los registros
            c = db.query(ContractInsertExh.INSERT_EXH, projection,
                    selection, selectionArgs,
                    null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractInsertExh.CONTENT_URI);
        }else if (ContractInsertExh.uriMatcher.match(uri)==ContractInsertExh.SINGLE_ROW) {
            // Consultando un solo registro basado en el Id del Uri
            long idGasto = ContentUris.parseId(uri);
            c = db.query(ContractInsertExh.INSERT_EXH, projection,
                    ContractInsertExh.Columnas._ID + " = " + idGasto,
                    selectionArgs, null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractInsertExh.CONTENT_URI);
        }

        else if (ContractInsertExhSesion.uriMatcher.match(uri)==ContractInsertExhSesion.ALLROWS) {
            // Consultando todos los registros
            c = db.query(ContractInsertExhSesion.INSERT_EXH_SESION, projection,
                    selection, selectionArgs,
                    null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractInsertExhSesion.CONTENT_URI);
        }else if (ContractInsertExhSesion.uriMatcher.match(uri)==ContractInsertExhSesion.SINGLE_ROW) {
            // Consultando un solo registro basado en el Id del Uri
            long idGasto = ContentUris.parseId(uri);
            c = db.query(ContractInsertExhSesion.INSERT_EXH_SESION, projection,
                    ContractInsertExhSesion.Columnas._ID + " = " + idGasto,
                    selectionArgs, null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractInsertExhSesion.CONTENT_URI);
        }

        else if (ContractInsertFotografico.uriMatcher.match(uri)==ContractInsertFotografico.ALLROWS) {
            // Consultando todos los registros
            c = db.query(ContractInsertFotografico.INSERT_FOTOGRAFICO, projection,
                    selection, selectionArgs,
                    null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractInsertFotografico.CONTENT_URI);
        }else if (ContractInsertFotografico.uriMatcher.match(uri)==ContractInsertFotografico.SINGLE_ROW) {
            // Consultando un solo registro basado en el Id del Uri
            long idGasto = ContentUris.parseId(uri);
            c = db.query(ContractInsertFotografico.INSERT_FOTOGRAFICO, projection,
                    ContractInsertFotografico.Columnas._ID + " = " + idGasto,
                    selectionArgs, null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractInsertFotografico.CONTENT_URI);
        }

        else if (ContractInsertAsistencia.uriMatcher.match(uri)==ContractInsertAsistencia.ALLROWS) {
            // Consultando todos los registros
            c = db.query(ContractInsertAsistencia.INSERT_ASISTENCIA, projection,
                    selection, selectionArgs,
                    null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractInsertAsistencia.CONTENT_URI);
        }else if (ContractInsertAsistencia.uriMatcher.match(uri)==ContractInsertAsistencia.SINGLE_ROW) {
            // Consultando un solo registro basado en el Id del Uri
            long idGasto = ContentUris.parseId(uri);
            c = db.query(ContractInsertAsistencia.INSERT_ASISTENCIA, projection,
                    ContractInsertAsistencia.Columnas._ID + " = " + idGasto,
                    selectionArgs, null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractInsertAsistencia.CONTENT_URI);
        }

        else if (ContractAsistenciasLocal.uriMatcher.match(uri)==ContractAsistenciasLocal.ALLROWS) {
            // Consultando todos los registros
            c = db.query(ContractAsistenciasLocal.TABLE_NAME, projection,
                    selection, selectionArgs,
                    null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractAsistenciasLocal.CONTENT_URI);
        }else if (ContractAsistenciasLocal.uriMatcher.match(uri)==ContractAsistenciasLocal.SINGLE_ROW) {
            // Consultando un solo registro basado en el Id del Uri
            long idGasto = ContentUris.parseId(uri);
            c = db.query(ContractAsistenciasLocal.TABLE_NAME, projection,
                    ContractAsistenciasLocal.Columnas._ID + " = " + idGasto,
                    selectionArgs, null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractAsistenciasLocal.CONTENT_URI);
        }

        else if (ContractHoraMarcacion.uriMatcher.match(uri)==ContractHoraMarcacion.ALLROWS) {
            // Consultando todos los registros
            c = db.query(ContractHoraMarcacion.TABLE_NAME, projection,
                    selection, selectionArgs,
                    null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractHoraMarcacion.CONTENT_URI);
        }else if (ContractHoraMarcacion.uriMatcher.match(uri)==ContractHoraMarcacion.SINGLE_ROW) {
            // Consultando un solo registro basado en el Id del Uri
            long idGasto = ContentUris.parseId(uri);
            c = db.query(ContractHoraMarcacion.TABLE_NAME, projection,
                    ContractHoraMarcacion.Columnas._ID + " = " + idGasto,
                    selectionArgs, null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractHoraMarcacion.CONTENT_URI);
        }

        else if (ContractInsertEvidencias.uriMatcher.match(uri)==ContractInsertEvidencias.ALLROWS) {
            // Consultando todos los registros
            c = db.query(ContractInsertEvidencias.INSERT_EVIDENCIAS, projection,
                    selection, selectionArgs,
                    null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractInsertEvidencias.CONTENT_URI);
        }else if (ContractInsertEvidencias.uriMatcher.match(uri)==ContractInsertEvidencias.SINGLE_ROW) {
            // Consultando un solo registro basado en el Id del Uri
            long idGasto = ContentUris.parseId(uri);
            c = db.query(ContractInsertEvidencias.INSERT_EVIDENCIAS, projection,
                    ContractInsertEvidencias.Columnas._ID + " = " + idGasto,
                    selectionArgs, null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractInsertEvidencias.CONTENT_URI);
        }

        else if (ContractInsertGps.uriMatcher.match(uri)==ContractInsertGps.ALLROWS) {
            // Consultando todos los registros
            c = db.query(ContractInsertGps.INSERT_GPS, projection,
                    selection, selectionArgs,
                    null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractInsertGps.CONTENT_URI);
        }else if (ContractInsertGps.uriMatcher.match(uri)==ContractInsertGps.SINGLE_ROW) {
            // Consultando un solo registro basado en el Id del Uri
            long idGasto = ContentUris.parseId(uri);
            c = db.query(ContractInsertGps.INSERT_GPS, projection,
                    ContractInsertGps.Columnas._ID + " = " + idGasto,
                    selectionArgs, null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractInsertGps.CONTENT_URI);
        }

        else if (ContractInsertRastreo.uriMatcher.match(uri)==ContractInsertRastreo.ALLROWS) {
            // Consultando todos los registros
            c = db.query(ContractInsertRastreo.INSERT_GEO, projection,
                    selection, selectionArgs,
                    null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractInsertRastreo.CONTENT_URI);
        }else if (ContractInsertRastreo.uriMatcher.match(uri)==ContractInsertRastreo.SINGLE_ROW) {
            // Consultando un solo registro basado en el Id del Uri
            long idGasto = ContentUris.parseId(uri);
            c = db.query(ContractInsertRastreo.INSERT_GEO, projection,
                    ContractInsertRastreo.Columnas._ID + " = " + idGasto,
                    selectionArgs, null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractInsertRastreo.CONTENT_URI);
        }

        else if (InsertFlooring.uriMatcher.match(uri)==InsertFlooring.ALLROWS) {
            // Consultando todos los registros
            c = db.query(InsertFlooring.INSERT_FLOORING, projection,
                    selection, selectionArgs,
                    null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    InsertFlooring.CONTENT_URI);
        }else if (InsertFlooring.uriMatcher.match(uri)==InsertFlooring.SINGLE_ROW) {
            // Consultando un solo registro basado en el Id del Uri
            long idGasto = ContentUris.parseId(uri);
            c = db.query(InsertFlooring.INSERT_FLOORING, projection,
                    InsertFlooring.Columnas._ID + " = " + idGasto,
                    selectionArgs, null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    InsertFlooring.CONTENT_URI);
        }

        else if (ContractInsertImpulso.uriMatcher.match(uri)==ContractInsertImpulso.ALLROWS) {
            // Consultando todos los registros
            c = db.query(ContractInsertImpulso.INSERT_IMPUSLO, projection,
                    selection, selectionArgs,
                    null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractInsertImpulso.CONTENT_URI);
        }else if (ContractInsertImpulso.uriMatcher.match(uri)==ContractInsertImpulso.SINGLE_ROW) {
            // Consultando un solo registro basado en el Id del Uri
            long idGasto = ContentUris.parseId(uri);
            c = db.query(ContractInsertImpulso.INSERT_IMPUSLO, projection,
                    ContractInsertImpulso.Columnas._ID + " = " + idGasto,
                    selectionArgs, null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractInsertImpulso.CONTENT_URI);
        }

        else if (ContractNotificacion.uriMatcher.match(uri)==ContractNotificacion.ALLROWS) {
            // Consultando todos los registros
            c = db.query(ContractNotificacion.NOTIFICACION, projection,
                    selection, selectionArgs,
                    null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractNotificacion.CONTENT_URI);
        }else if (ContractNotificacion.uriMatcher.match(uri)==ContractNotificacion.SINGLE_ROW) {
            // Consultando un solo registro basado en el Id del Uri
            long idGasto = ContentUris.parseId(uri);
            c = db.query(ContractNotificacion.NOTIFICACION, projection,
                    ContractNotificacion.Columnas._ID + " = " + idGasto,
                    selectionArgs, null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractNotificacion.CONTENT_URI);
        }

        else if (ContractInsertInicial.uriMatcher.match(uri)==ContractInsertInicial.ALLROWS) {
            // Consultando todos los registros
            c = db.query(ContractInsertInicial.INSERT_INICIAL, projection,
                    selection, selectionArgs,
                    null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractInsertInicial.CONTENT_URI);
        }else if (ContractInsertInicial.uriMatcher.match(uri)==ContractInsertInicial.SINGLE_ROW) {
            // Consultando un solo registro basado en el Id del Uri
            long idGasto = ContentUris.parseId(uri);
            c = db.query(ContractInsertInicial.INSERT_INICIAL, projection,
                    ContractInsertInicial.Columnas._ID + " = " + idGasto,
                    selectionArgs, null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractInsertInicial.CONTENT_URI);
        }

        else if (ContractInsertLogisticoRelevo.uriMatcher.match(uri)==ContractInsertLogisticoRelevo.ALLROWS) {
            // Consultando todos los registros
            c = db.query(ContractInsertLogisticoRelevo.INSERT_LOGISTICO_RELEVO, projection,
                    selection, selectionArgs,
                    null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractInsertLogisticoRelevo.CONTENT_URI);
        }else if (ContractInsertLogisticoRelevo.uriMatcher.match(uri)==ContractInsertLogisticoRelevo.SINGLE_ROW) {
            // Consultando un solo registro basado en el Id del Uri
            long idGasto = ContentUris.parseId(uri);
            c = db.query(ContractInsertLogisticoRelevo.INSERT_LOGISTICO_RELEVO, projection,
                    ContractInsertLogisticoRelevo.Columnas._ID + " = " + idGasto,
                    selectionArgs, null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractInsertLogisticoRelevo.CONTENT_URI);
        }

        else if (ContractInsertPromocion.uriMatcher.match(uri)==ContractInsertPromocion.ALLROWS) {
            // Consultando todos los registros
            c = db.query(ContractInsertPromocion.INSERT_PROMO, projection,
                    selection, selectionArgs,
                    null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractInsertPromocion.CONTENT_URI);
        }else if (ContractInsertPromocion.uriMatcher.match(uri)==ContractInsertPromocion.SINGLE_ROW) {
            // Consultando un solo registro basado en el Id del Uri
            long idGasto = ContentUris.parseId(uri);
            c = db.query(ContractInsertPromocion.INSERT_PROMO, projection,
                    ContractInsertPromocion.Columnas._ID + " = " + idGasto,
                    selectionArgs, null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractInsertPromocion.CONTENT_URI);
        }

        else if (ContractInsertImplementacion.uriMatcher.match(uri)==ContractInsertImplementacion.ALLROWS) {
            // Consultando todos los registros
            c = db.query(ContractInsertImplementacion.INSERT_IMPLEM, projection,
                    selection, selectionArgs,
                    null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractInsertImplementacion.CONTENT_URI);
        }else if (ContractInsertImplementacion.uriMatcher.match(uri)==ContractInsertImplementacion.SINGLE_ROW) {
            // Consultando un solo registro basado en el Id del Uri
            long idGasto = ContentUris.parseId(uri);
            c = db.query(ContractInsertImplementacion.INSERT_IMPLEM, projection,
                    ContractInsertImplementacion.Columnas._ID + " = " + idGasto,
                    selectionArgs, null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractInsertImplementacion.CONTENT_URI);
        }

        else if (ContractInsertValores.uriMatcher.match(uri)==ContractInsertValores.ALLROWS) {
            // Consultando todos los registros
            c = db.query(ContractInsertValores.INSERT_VALORES, projection,
                    selection, selectionArgs,
                    null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractInsertValores.CONTENT_URI);
        }else if (ContractInsertValores.uriMatcher.match(uri)==ContractInsertValores.SINGLE_ROW) {
            // Consultando un solo registro basado en el Id del Uri
            long idGasto = ContentUris.parseId(uri);
            c = db.query(ContractInsertValores.INSERT_VALORES, projection,
                    ContractInsertValores.Columnas._ID + " = " + idGasto,
                    selectionArgs, null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractInsertValores.CONTENT_URI);
        }

        else if (ContractInsertValoresSesion.uriMatcher.match(uri)==ContractInsertValoresSesion.ALLROWS) {
            // Consultando todos los registros
            c = db.query(ContractInsertValoresSesion.INSERT_VALORES_SESION, projection,
                    selection, selectionArgs,
                    null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractInsertValoresSesion.CONTENT_URI);
        }else if (ContractInsertValoresSesion.uriMatcher.match(uri)==ContractInsertValoresSesion.SINGLE_ROW) {
            // Consultando un solo registro basado en el Id del Uri
            long idGasto = ContentUris.parseId(uri);
            c = db.query(ContractInsertValoresSesion.INSERT_VALORES_SESION, projection,
                    ContractInsertValoresSesion.Columnas._ID + " = " + idGasto,
                    selectionArgs, null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractInsertValoresSesion.CONTENT_URI);
        }

        else if (ContractInsertPdv.uriMatcher.match(uri)==ContractInsertPdv.ALLROWS) {
            // Consultando todos los registros
            c = db.query(ContractInsertPdv.INSERT_PDV, projection,
                    selection, selectionArgs,
                    null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractInsertPdv.CONTENT_URI);
        }else if (ContractInsertPdv.uriMatcher.match(uri)==ContractInsertPdv.SINGLE_ROW) {
            // Consultando un solo registro basado en el Id del Uri
            long idGasto = ContentUris.parseId(uri);
            c = db.query(ContractInsertPdv.INSERT_PDV, projection,
                    ContractInsertPdv.Columnas._ID + " = " + idGasto,
                    selectionArgs, null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractInsertPdv.CONTENT_URI);
        }

        else if (ContractInsertShare.uriMatcher.match(uri)==ContractInsertShare.ALLROWS) {
            // Consultando todos los registros
            c = db.query(ContractInsertShare.INSERT_SHARE, projection,
                    selection, selectionArgs,
                    null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractInsertShare.CONTENT_URI);
        }else if (ContractInsertShare.uriMatcher.match(uri)==ContractInsertShare.SINGLE_ROW) {
            // Consultando un solo registro basado en el Id del Uri
            long idGasto = ContentUris.parseId(uri);
            c = db.query(ContractInsertShare.INSERT_SHARE, projection,
                    ContractInsertShare.Columnas._ID + " = " + idGasto,
                    selectionArgs, null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractInsertShare.CONTENT_URI);
        }

        else if (ContractInsertPDI.uriMatcher.match(uri)==ContractInsertPDI.ALLROWS) {
            // Consultando todos los registros
            c = db.query(ContractInsertPDI.INSERT_PDI, projection,
                    selection, selectionArgs,
                    null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractInsertPDI.CONTENT_URI);
        }else if (ContractInsertPDI.uriMatcher.match(uri)==ContractInsertPDI.SINGLE_ROW) {
            // Consultando un solo registro basado en el Id del Uri
            long idGasto = ContentUris.parseId(uri);
            c = db.query(ContractInsertPDI.INSERT_PDI, projection,
                    ContractInsertPDI.Columnas._ID + " = " + idGasto,
                    selectionArgs, null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractInsertPDI.CONTENT_URI);
        }

        else if (ContractInsertAgotados.uriMatcher.match(uri)==ContractInsertAgotados.ALLROWS) {
            // Consultando todos los registros
            c = db.query(ContractInsertAgotados.INSERT_AGOTADOS, projection,
                    selection, selectionArgs,
                    null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractInsertAgotados.CONTENT_URI);
        }else if (ContractInsertAgotados.uriMatcher.match(uri)==ContractInsertAgotados.SINGLE_ROW) {
            // Consultando un solo registro basado en el Id del Uri
            long idGasto = ContentUris.parseId(uri);
            c = db.query(ContractInsertAgotados.INSERT_AGOTADOS, projection,
                    ContractInsertAgotados.Columnas._ID + " = " + idGasto,
                    selectionArgs, null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractInsertAgotados.CONTENT_URI);
        }

        else if (ContractInsertTareas.uriMatcher.match(uri)==ContractInsertTareas.ALLROWS) {
            // Consultando todos los registros
            c = db.query(ContractInsertTareas.INSERT_TAREAS, projection,
                    selection, selectionArgs,
                    null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractInsertTareas.CONTENT_URI);
        }else if (ContractInsertTareas.uriMatcher.match(uri)==ContractInsertTareas.SINGLE_ROW) {
            // Consultando un solo registro basado en el Id del Uri
            long idGasto = ContentUris.parseId(uri);
            c = db.query(ContractInsertTareas.INSERT_TAREAS, projection,
                    ContractInsertTareas.Columnas._ID + " = " + idGasto,
                    selectionArgs, null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractInsertTareas.CONTENT_URI);
        }

        else if (ContractInsertVenta.uriMatcher.match(uri)==ContractInsertVenta.ALLROWS) {
            // Consultando todos los registros
            c = db.query(ContractInsertVenta.INSERT_VENTA, projection,
                    selection, selectionArgs,
                    null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractInsertVenta.CONTENT_URI);
        }else if (ContractInsertVenta.uriMatcher.match(uri)==ContractInsertVenta.SINGLE_ROW) {
            // Consultando un solo registro basado en el Id del Uri
            long idGasto = ContentUris.parseId(uri);
            c = db.query(ContractInsertVenta.INSERT_VENTA, projection,
                    ContractInsertVenta.Columnas._ID + " = " + idGasto,
                    selectionArgs, null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractInsertVenta.CONTENT_URI);
        }
        else if (ContractCausalesAsistenciaAtraso.uriMatcher.match(uri) == ContractCausalesAsistenciaAtraso.ALLROWS) {
            c = db.query(ContractCausalesAsistenciaAtraso.CAUSALES_ASISTENCIA_ATRASO, projection,
                    selection, selectionArgs, null, null, sortOrder);
            c.setNotificationUri(resolver, ContractCausalesAsistenciaAtraso.CONTENT_URI);
        } else if (ContractCausalesAsistenciaAtraso.uriMatcher.match(uri) == ContractCausalesAsistenciaAtraso.SINGLE_ROW) {
            long idGasto = ContentUris.parseId(uri);
            c = db.query(ContractCausalesAsistenciaAtraso.CAUSALES_ASISTENCIA_ATRASO, projection,
                    ContractCausalesAsistenciaAtraso.Columnas._ID + " = " + idGasto,
                    selectionArgs, null, null, sortOrder);
            c.setNotificationUri(resolver, ContractCausalesAsistenciaAtraso.CONTENT_URI);
        }

        else if (ContractCausalesAsistencia.uriMatcher.match(uri) == ContractCausalesAsistencia.ALLROWS) {
            // Consultando todos los registros
            c = db.query(ContractCausalesAsistencia.CAUSALES_ASISTENCIA, projection,
                    selection, selectionArgs,
                    null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractCausalesAsistencia.CONTENT_URI);
        } else if (ContractCausalesAsistencia.uriMatcher.match(uri) == ContractCausalesAsistencia.SINGLE_ROW) {
            // Consultando un solo registro basado en el Id del Uri
            long idGasto = ContentUris.parseId(uri);
            c = db.query(ContractCausalesAsistencia.CAUSALES_ASISTENCIA, projection,
                    ContractCausalesAsistencia.Columnas.ID + " = " + idGasto,
                    selectionArgs, null, null, sortOrder);
            c.setNotificationUri(
                    resolver,
                    ContractCausalesAsistencia.CONTENT_URI);
        }

        else{
            throw new IllegalArgumentException("URI no soportada: " + uri);
        }
        return c;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        if (ContractPharmaValue.uriMatcher.match(uri)== ContractPharmaValue.ALLROWS) {
            return ContractPharmaValue.MULTIPLE_MIME;
        }else if (ContractPharmaValue.uriMatcher.match(uri)== ContractPharmaValue.SINGLE_ROW) {
            return ContractPharmaValue.SINGLE_MIME;
        }

        if (ContractPrecios.uriMatcher.match(uri)== ContractPrecios.ALLROWS) {
            return ContractPrecios.MULTIPLE_MIME;
        }else if (ContractPrecios.uriMatcher.match(uri)== ContractPrecios.SINGLE_ROW) {
            return ContractPrecios.SINGLE_MIME;
        }

        if (ContractPreciosPvc.uriMatcher.match(uri)== ContractPreciosPvc.ALLROWS) {
            return ContractPreciosPvc.MULTIPLE_MIME;
        }else if (ContractPreciosPvc.uriMatcher.match(uri)== ContractPreciosPvc.SINGLE_ROW) {
            return ContractPreciosPvc.SINGLE_MIME;
        }

        if (ContractTests.uriMatcher.match(uri)== ContractTests.ALLROWS) {
            return ContractTests.MULTIPLE_MIME;
        }else if (ContractTests.uriMatcher.match(uri)== ContractTests.SINGLE_ROW) {
            return ContractTests.SINGLE_MIME;
        }


        if (ContractTipoExh.uriMatcher.match(uri)== ContractTipoExh.ALLROWS) {
            return ContractTipoExh.MULTIPLE_MIME;
        }else if (ContractTipoExh.uriMatcher.match(uri)== ContractTipoExh.SINGLE_ROW) {
            return ContractTipoExh.SINGLE_MIME;
        }

        if (ContractPreguntas.uriMatcher.match(uri)== ContractPreguntas.ALLROWS) {
            return ContractPreguntas.MULTIPLE_MIME;
        }else if (ContractPreguntas.uriMatcher.match(uri)== ContractPreguntas.SINGLE_ROW) {
            return ContractPreguntas.SINGLE_MIME;
        }

        if(ContractJustificacion.uriMatcher.match(uri)== ContractJustificacion.ALLROWS){
            return ContractJustificacion.MULTIPLE_MIME;
        }else if(ContractJustificacion.uriMatcher.match(uri)== ContractJustificacion.SINGLE_ROW){
            return ContractJustificacion.SINGLE_MIME;
        }

        if(ContractTipoPrecios.uriMatcher.match(uri)== ContractTipoPrecios.ALLROWS){
            return ContractTipoPrecios.MULTIPLE_MIME;
        }else if(ContractTipoPrecios.uriMatcher.match(uri)== ContractTipoPrecios.SINGLE_ROW){
            return ContractTipoPrecios.SINGLE_MIME;
        }

        if(ContractMarcasBlancas.uriMatcher.match(uri)== ContractMarcasBlancas.ALLROWS){
            return ContractMarcasBlancas.MULTIPLE_MIME;
        }else if(ContractMarcasBlancas.uriMatcher.match(uri)== ContractMarcasBlancas.SINGLE_ROW){
            return ContractMarcasBlancas.SINGLE_MIME;
        }

        if (ContractPromociones.uriMatcher.match(uri)== ContractPromociones.ALLROWS) {
            return ContractPromociones.MULTIPLE_MIME;
        }else if (ContractPromociones.uriMatcher.match(uri)== ContractPromociones.SINGLE_ROW) {
            return ContractPromociones.SINGLE_MIME;
        }

        if (ContractRotacion.uriMatcher.match(uri)== ContractRotacion.ALLROWS) {
            return ContractRotacion.MULTIPLE_MIME;
        }else if (ContractRotacion.uriMatcher.match(uri)== ContractRotacion.SINGLE_ROW) {
            return ContractRotacion.SINGLE_MIME;
        }

        if (ContractPopSugerido.uriMatcher.match(uri)== ContractPopSugerido.ALLROWS) {
            return ContractPopSugerido.MULTIPLE_MIME;
        }else if (ContractPopSugerido.uriMatcher.match(uri)== ContractPopSugerido.SINGLE_ROW) {
            return ContractPopSugerido.SINGLE_MIME;
        }


        if (ContractCausalesAsistencia.uriMatcher.match(uri) == ContractCausalesAsistencia.ALLROWS) {
            return ContractCausalesAsistencia.MULTIPLE_MIME;
        } else if (ContractCausalesAsistencia.uriMatcher.match(uri) == ContractCausalesAsistencia.SINGLE_ROW) {
            return ContractCausalesAsistencia.SINGLE_MIME;
        }

        if (ContractCausalesAsistenciaAtraso.uriMatcher.match(uri) == ContractCausalesAsistenciaAtraso.ALLROWS) {
            return ContractCausalesAsistenciaAtraso.MULTIPLE_MIME;
        } else if (ContractCausalesAsistenciaAtraso.uriMatcher.match(uri) == ContractCausalesAsistenciaAtraso.SINGLE_ROW) {
            return ContractCausalesAsistenciaAtraso.SINGLE_MIME;
        }

        if (ContractCausalesProdMalEst.uriMatcher.match(uri)== ContractCausalesProdMalEst.ALLROWS) {
            return ContractCausalesProdMalEst.MULTIPLE_MIME;
        }else if (ContractCausalesProdMalEst.uriMatcher.match(uri)== ContractCausalesProdMalEst.SINGLE_ROW) {
            return ContractCausalesProdMalEst.SINGLE_MIME;
        }

        if (ContractVersiones.uriMatcher.match(uri)== ContractVersiones.ALLROWS) {
            return ContractVersiones.MULTIPLE_MIME;
        }else if (ContractVersiones.uriMatcher.match(uri)== ContractVersiones.SINGLE_ROW) {
            return ContractVersiones.SINGLE_MIME;
        }
        
        if (ContractComboCanjes.uriMatcher.match(uri)== ContractComboCanjes.ALLROWS) {
            return ContractComboCanjes.MULTIPLE_MIME;
        }else if (ContractComboCanjes.uriMatcher.match(uri)== ContractComboCanjes.SINGLE_ROW) {
            return ContractComboCanjes.SINGLE_MIME;
        }

        if (ContractCausalesMCI.uriMatcher.match(uri)== ContractCausalesMCI.ALLROWS) {
            return ContractCausalesMCI.MULTIPLE_MIME;
        }else if (ContractCausalesMCI.uriMatcher.match(uri)== ContractCausalesMCI.SINGLE_ROW) {
            return ContractCausalesMCI.SINGLE_MIME;
        }

        if (ContractCausalesOSA.uriMatcher.match(uri)== ContractCausalesOSA.ALLROWS) {
            return ContractCausalesOSA.MULTIPLE_MIME;
        }else if (ContractCausalesOSA.uriMatcher.match(uri)== ContractCausalesOSA.SINGLE_ROW) {
            return ContractCausalesOSA.SINGLE_MIME;
        }

        if (ContractAlertas.uriMatcher.match(uri)== ContractAlertas.ALLROWS) {
            return ContractAlertas.MULTIPLE_MIME;
        }else if (ContractAlertas.uriMatcher.match(uri)== ContractAlertas.SINGLE_ROW) {
            return ContractAlertas.SINGLE_MIME;
        }

        if (ContractTiempoAlmuerzo.uriMatcher.match(uri)== ContractTiempoAlmuerzo.ALLROWS) {
            return ContractTiempoAlmuerzo.MULTIPLE_MIME;
        }else if (ContractTiempoAlmuerzo.uriMatcher.match(uri)== ContractTiempoAlmuerzo.SINGLE_ROW) {
            return ContractTiempoAlmuerzo.SINGLE_MIME;
        }

        if (ContractPDI.uriMatcher.match(uri)== ContractPDI.ALLROWS) {
            return ContractPDI.MULTIPLE_MIME;
        }else if (ContractPDI.uriMatcher.match(uri)== ContractPDI.SINGLE_ROW) {
            return ContractPDI.SINGLE_MIME;
        }

        if (ContractLog.uriMatcher.match(uri)== ContractLog.ALLROWS) {
            return ContractLog.MULTIPLE_MIME;
        }else if (ContractLog.uriMatcher.match(uri)== ContractLog.SINGLE_ROW) {
            return ContractLog.SINGLE_MIME;
        }

        if (ContractPrioritario.uriMatcher.match(uri)== ContractPrioritario.ALLROWS) {
            return ContractPrioritario.MULTIPLE_MIME;
        }else if (ContractPrioritario.uriMatcher.match(uri)== ContractPrioritario.SINGLE_ROW) {
            return ContractPrioritario.SINGLE_MIME;
        }

        if (ContractTareas.uriMatcher.match(uri)== ContractTareas.ALLROWS) {
            return ContractTareas.MULTIPLE_MIME;
        }else if (ContractTareas.uriMatcher.match(uri)== ContractTareas.SINGLE_ROW) {
            return ContractTareas.SINGLE_MIME;
        }

        if (ContractPortafolioProductos.uriMatcher.match(uri)== ContractPortafolioProductos.ALLROWS) {
            return ContractPortafolioProductos.MULTIPLE_MIME;
        }else if (ContractPortafolioProductos.uriMatcher.match(uri)== ContractPortafolioProductos.SINGLE_ROW) {
            return ContractPortafolioProductos.SINGLE_MIME;
        }

        if (ContractPortafolioProductosAASS.uriMatcher.match(uri)== ContractPortafolioProductosAASS.ALLROWS) {
            return ContractPortafolioProductosAASS.MULTIPLE_MIME;
        }else if (ContractPortafolioProductosAASS.uriMatcher.match(uri)== ContractPortafolioProductosAASS.SINGLE_ROW) {
            return ContractPortafolioProductosAASS.SINGLE_MIME;
        }

        if (ContractPortafolioProductosMAYO.uriMatcher.match(uri)== ContractPortafolioProductosMAYO.ALLROWS) {
            return ContractPortafolioProductosMAYO.MULTIPLE_MIME;
        }else if (ContractPortafolioProductosMAYO.uriMatcher.match(uri)== ContractPortafolioProductosMAYO.SINGLE_ROW) {
            return ContractPortafolioProductosMAYO.SINGLE_MIME;
        }

        else{
            throw new IllegalArgumentException("Tipo de URI desconocida: " + uri);
        }

    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        if (ContractPharmaValue.uriMatcher.match(uri) == ContractPharmaValue.ALLROWS) {
            ContentValues contentValues;
            if (values != null) {
                contentValues = new ContentValues(values);
            } else {
                contentValues = new ContentValues();
            }

            // Inserción de nueva fila
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            long rowId = db.insert(ContractPharmaValue.POS, null, contentValues);
            if (rowId > 0) {
                Uri uri_stock = ContentUris.withAppendedId(
                        ContractPharmaValue.CONTENT_URI, rowId);
                resolver.notifyChange(uri_stock, null, false);
                return uri_stock;
            }
            throw new SQLException("Falla al insertar fila en : " + uri);
        }

        if (ContractPrecios.uriMatcher.match(uri) == ContractPrecios.ALLROWS) {
            ContentValues contentValues;
            if (values != null) {
                contentValues = new ContentValues(values);
            } else {
                contentValues = new ContentValues();
            }

            // Inserción de nueva fila
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            long rowId = db.insert(ContractPrecios.PRECIOS, null, contentValues);
            if (rowId > 0) {
                Uri uri_stock = ContentUris.withAppendedId(
                        ContractPrecios.CONTENT_URI, rowId);
                resolver.notifyChange(uri_stock, null, false);
                return uri_stock;
            }
            throw new SQLException("Falla al insertar fila en : " + uri);
        }

        if (ContractPreciosPvc.uriMatcher.match(uri) == ContractPreciosPvc.ALLROWS) {
            ContentValues contentValues;
            if (values != null) {
                contentValues = new ContentValues(values);
            } else {
                contentValues = new ContentValues();
            }

            // Inserción de nueva fila
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            long rowId = db.insert(ContractPreciosPvc.PRECIOS_PVC, null, contentValues);
            if (rowId > 0) {
                Uri uri_stock = ContentUris.withAppendedId(
                        ContractPreciosPvc.CONTENT_URI, rowId);
                resolver.notifyChange(uri_stock, null, false);
                return uri_stock;
            }
            throw new SQLException("Falla al insertar fila en : " + uri);
        }



        if (ContractTests.uriMatcher.match(uri) == ContractTests.ALLROWS) {
            ContentValues contentValues;
            if (values != null) {
                contentValues = new ContentValues(values);
            } else {
                contentValues = new ContentValues();
            }

            // Inserción de nueva fila
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            long rowId = db.insert(ContractTests.TABLE_TEST, null, contentValues);
            if (rowId > 0) {
                Uri uri_stock = ContentUris.withAppendedId(
                        ContractTests.CONTENT_URI, rowId);
                resolver.notifyChange(uri_stock, null, false);
                return uri_stock;
            }
            throw new SQLException("Falla al insertar fila en : " + uri);
        }

        if (ContractJustificacion.uriMatcher.match(uri) == ContractJustificacion.ALLROWS) {
            ContentValues contentValues;
            if (values != null) {
                contentValues = new ContentValues(values);
            } else {
                contentValues = new ContentValues();
            }

            // Inserción de nueva fila
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            long rowId = db.insert(ContractJustificacion.JUSTIFICACIONES, null, contentValues);
            if (rowId > 0) {
                Uri uri_stock = ContentUris.withAppendedId(
                        ContractJustificacion.CONTENT_URI, rowId);
                resolver.notifyChange(uri_stock, null, false);
                return uri_stock;
            }
            throw new SQLException("Falla al insertar fila en : " + uri);
        }

        if (ContractTipoPrecios.uriMatcher.match(uri) == ContractTipoPrecios.ALLROWS) {
            ContentValues contentValues;
            if (values != null) {
                contentValues = new ContentValues(values);
            } else {
                contentValues = new ContentValues();
            }

            // Inserción de nueva fila
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            long rowId = db.insert(ContractTipoPrecios.TIPO_PRECIOS, null, contentValues);
            if (rowId > 0) {
                Uri uri_stock = ContentUris.withAppendedId(
                        ContractTipoPrecios.CONTENT_URI, rowId);
                resolver.notifyChange(uri_stock, null, false);
                return uri_stock;
            }
            throw new SQLException("Falla al insertar fila en : " + uri);
        }

        if (ContractMarcasBlancas.uriMatcher.match(uri) == ContractMarcasBlancas.ALLROWS) {
            ContentValues contentValues;
            if (values != null) {
                contentValues = new ContentValues(values);
            } else {
                contentValues = new ContentValues();
            }

            // Inserción de nueva fila
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            long rowId = db.insert(ContractMarcasBlancas.MARCAS_BLANCAS, null, contentValues);
            if (rowId > 0) {
                Uri uri_stock = ContentUris.withAppendedId(
                        ContractMarcasBlancas.CONTENT_URI, rowId);
                resolver.notifyChange(uri_stock, null, false);
                return uri_stock;
            }
            throw new SQLException("Falla al insertar fila en : " + uri);
        }

        if (ContractTipoExh.uriMatcher.match(uri) == ContractTipoExh.ALLROWS) {
            ContentValues contentValues;
            if (values != null) {
                contentValues = new ContentValues(values);
            } else {
                contentValues = new ContentValues();
            }

            // Inserción de nueva fila
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            long rowId = db.insert(ContractTipoExh.TABLE_NAME, null, contentValues);
            if (rowId > 0) {
                Uri uri_stock = ContentUris.withAppendedId(
                        ContractTipoExh.CONTENT_URI, rowId);
                resolver.notifyChange(uri_stock, null, false);
                return uri_stock;
            }
            throw new SQLException("Falla al insertar fila en : " + uri);
        }



        if (ContractPreguntas.uriMatcher.match(uri) == ContractPreguntas.ALLROWS) {
            ContentValues contentValues;
            if (values != null) {
                contentValues = new ContentValues(values);
            } else {
                contentValues = new ContentValues();
            }

            // Inserción de nueva fila
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            long rowId = db.insert(ContractPreguntas.TABLE_QUEST, null, contentValues);
            if (rowId > 0) {
                Uri uri_stock = ContentUris.withAppendedId(
                        ContractPreguntas.CONTENT_URI, rowId);
                resolver.notifyChange(uri_stock, null, false);
                return uri_stock;
            }
            throw new SQLException("Falla al insertar fila en : " + uri);
        }




        if (ContractPromociones.uriMatcher.match(uri) == ContractPromociones.ALLROWS) {
            ContentValues contentValues;
            if (values != null) {
                contentValues = new ContentValues(values);
            } else {
                contentValues = new ContentValues();
            }

            // Inserción de nueva fila
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            long rowId = db.insert(ContractPromociones.PROMOCIONES, null, contentValues);
            if (rowId > 0) {
                Uri uri_stock = ContentUris.withAppendedId(
                        ContractPromociones.CONTENT_URI, rowId);
                resolver.notifyChange(uri_stock, null, false);
                return uri_stock;
            }
            throw new SQLException("Falla al insertar fila en : " + uri);
        }

        if (ContractRotacion.uriMatcher.match(uri) == ContractRotacion.ALLROWS) {
            ContentValues contentValues;
            if (values != null) {
                contentValues = new ContentValues(values);
            } else {
                contentValues = new ContentValues();
            }

            // Inserción de nueva fila
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            long rowId = db.insert(ContractRotacion.ROTACION, null, contentValues);
            if (rowId > 0) {
                Uri uri_stock = ContentUris.withAppendedId(
                        ContractRotacion.CONTENT_URI, rowId);
                resolver.notifyChange(uri_stock, null, false);
                return uri_stock;
            }
            throw new SQLException("Falla al insertar fila en : " + uri);
        }

        if (ContractPopSugerido.uriMatcher.match(uri) == ContractPopSugerido.ALLROWS) {
            ContentValues contentValues;
            if (values != null) {
                contentValues = new ContentValues(values);
            } else {
                contentValues = new ContentValues();
            }

            // Inserción de nueva fila
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            long rowId = db.insert(ContractPopSugerido.POPSUGERIDO, null, contentValues);
            if (rowId > 0) {
                Uri uri_stock = ContentUris.withAppendedId(
                        ContractPopSugerido.CONTENT_URI, rowId);
                resolver.notifyChange(uri_stock, null, false);
                return uri_stock;
            }
            throw new SQLException("Falla al insertar fila en : " + uri);
        }



        if (ContractCausalesProdMalEst.uriMatcher.match(uri) == ContractCausalesProdMalEst.ALLROWS) {
            ContentValues contentValues;
            if (values != null) {
                contentValues = new ContentValues(values);
            } else {
                contentValues = new ContentValues();
            }

            // Inserción de nueva fila
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            long rowId = db.insert(ContractCausalesProdMalEst.CAUSALES_PROD_MAL_EST, null, contentValues);
            if (rowId > 0) {
                Uri uri_stock = ContentUris.withAppendedId(
                        ContractCausalesProdMalEst.CONTENT_URI, rowId);
                resolver.notifyChange(uri_stock, null, false);
                return uri_stock;
            }
            throw new SQLException("Falla al insertar fila en : " + uri);
        }



        if (ContractVersiones.uriMatcher.match(uri) == ContractVersiones.ALLROWS) {
            ContentValues contentValues;
            if (values != null) {
                contentValues = new ContentValues(values);
            } else {
                contentValues = new ContentValues();
            }

            // Inserción de nueva fila
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            long rowId = db.insert(ContractVersiones.VERSIONES, null, contentValues);
            if (rowId > 0) {
                Uri uri_stock = ContentUris.withAppendedId(
                        ContractVersiones.CONTENT_URI, rowId);
                resolver.notifyChange(uri_stock, null, false);
                return uri_stock;
            }
            throw new SQLException("Falla al insertar fila en : " + uri);
        }




        if (ContractComboCanjes.uriMatcher.match(uri) == ContractComboCanjes.ALLROWS) {
            ContentValues contentValues;
            if (values != null) {
                contentValues = new ContentValues(values);
            } else {
                contentValues = new ContentValues();
            }

            // Inserción de nueva fila
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            long rowId = db.insert(ContractComboCanjes.COMBO_CANJES, null, contentValues);
            if (rowId > 0) {
                Uri uri_stock = ContentUris.withAppendedId(
                        ContractComboCanjes.CONTENT_URI, rowId);
                resolver.notifyChange(uri_stock, null, false);
                return uri_stock;
            }
            throw new SQLException("Falla al insertar fila en : " + uri);
        }

        if (ContractCausalesMCI.uriMatcher.match(uri) == ContractCausalesMCI.ALLROWS) {
            ContentValues contentValues;
            if (values != null) {
                contentValues = new ContentValues(values);
            } else {
                contentValues = new ContentValues();
            }

            // Inserción de nueva fila
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            long rowId = db.insert(ContractCausalesMCI.CAUSALES_MCI, null, contentValues);
            if (rowId > 0) {
                Uri uri_stock = ContentUris.withAppendedId(
                        ContractCausalesMCI.CONTENT_URI, rowId);
                resolver.notifyChange(uri_stock, null, false);
                return uri_stock;
            }
            throw new SQLException("Falla al insertar fila en : " + uri);
        }

        if (ContractCausalesOSA.uriMatcher.match(uri) == ContractCausalesOSA.ALLROWS) {
            ContentValues contentValues;
            if (values != null) {
                contentValues = new ContentValues(values);
            } else {
                contentValues = new ContentValues();
            }

            // Inserción de nueva fila
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            long rowId = db.insert(ContractCausalesOSA.CAUSALES_OSA, null, contentValues);
            if (rowId > 0) {
                Uri uri_stock = ContentUris.withAppendedId(
                        ContractCausalesOSA.CONTENT_URI, rowId);
                resolver.notifyChange(uri_stock, null, false);
                return uri_stock;
            }
            throw new SQLException("Falla al insertar fila en : " + uri);
        }

        if (ContractAlertas.uriMatcher.match(uri) == ContractAlertas.ALLROWS) {
            ContentValues contentValues;
            if (values != null) {
                contentValues = new ContentValues(values);
            } else {
                contentValues = new ContentValues();
            }

            // Inserción de nueva fila
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            long rowId = db.insert(ContractAlertas.ALERTAS, null, contentValues);
            if (rowId > 0) {
                Uri uri_stock = ContentUris.withAppendedId(
                        ContractAlertas.CONTENT_URI, rowId);
                resolver.notifyChange(uri_stock, null, false);
                return uri_stock;
            }
            throw new SQLException("Falla al insertar fila en : " + uri);
        }


        if (ContractTiempoAlmuerzo.uriMatcher.match(uri) == ContractTiempoAlmuerzo.ALLROWS) {
            ContentValues contentValues;
            if (values != null) {
                contentValues = new ContentValues(values);
            } else {
                contentValues = new ContentValues();
            }

            // Inserción de nueva fila
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            long rowId = db.insert(ContractTiempoAlmuerzo.REPO_TIEMPO_ALMUERZO, null, contentValues);
            if (rowId > 0) {
                Uri uri_stock = ContentUris.withAppendedId(
                        ContractTiempoAlmuerzo.CONTENT_URI, rowId);
                resolver.notifyChange(uri_stock, null, false);
                return uri_stock;
            }
            throw new SQLException("Falla al insertar fila en : " + uri);
        }



        if (ContractPDI.uriMatcher.match(uri) == ContractPDI.ALLROWS) {
            ContentValues contentValues;
            if (values != null) {
                contentValues = new ContentValues(values);
            } else {
                contentValues = new ContentValues();
            }

            // Inserción de nueva fila
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            long rowId = db.insert(ContractPDI.PDI, null, contentValues);
            if (rowId > 0) {
                Uri uri_stock = ContentUris.withAppendedId(
                        ContractPDI.CONTENT_URI, rowId);
                resolver.notifyChange(uri_stock, null, false);
                return uri_stock;
            }
            throw new SQLException("Falla al insertar fila en : " + uri);
        }

        if (ContractLog.uriMatcher.match(uri) == ContractLog.ALLROWS) {
            ContentValues contentValues;
            if (values != null) {
                contentValues = new ContentValues(values);
            } else {
                contentValues = new ContentValues();
            }

            // Inserción de nueva fila
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            long rowId = db.insert(ContractLog.LOG, null, contentValues);
            if (rowId > 0) {
                Uri uri_stock = ContentUris.withAppendedId(
                        ContractLog.CONTENT_URI, rowId);
                resolver.notifyChange(uri_stock, null, false);
                return uri_stock;
            }
            throw new SQLException("Falla al insertar fila en : " + uri);
        }

        if (ContractPrioritario.uriMatcher.match(uri) == ContractPrioritario.ALLROWS) {
            ContentValues contentValues;
            if (values != null) {
                contentValues = new ContentValues(values);
            } else {
                contentValues = new ContentValues();
            }

            // Inserción de nueva fila
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            long rowId = db.insert(ContractPrioritario.PRIORITARIO, null, contentValues);
            if (rowId > 0) {
                Uri uri_stock = ContentUris.withAppendedId(
                        ContractPrioritario.CONTENT_URI, rowId);
                resolver.notifyChange(uri_stock, null, false);
                return uri_stock;
            }
            throw new SQLException("Falla al insertar fila en : " + uri);
        }

        if (ContractTareas.uriMatcher.match(uri) == ContractTareas.ALLROWS) {
            ContentValues contentValues;
            if (values != null) {
                contentValues = new ContentValues(values);
            } else {
                contentValues = new ContentValues();
            }

            // Inserción de nueva fila
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            long rowId = db.insert(ContractTareas.TAREA, null, contentValues);
            if (rowId > 0) {
                Uri uri_stock = ContentUris.withAppendedId(
                        ContractTareas.CONTENT_URI, rowId);
                resolver.notifyChange(uri_stock, null, false);
                return uri_stock;
            }
            throw new SQLException("Falla al insertar fila en : " + uri);
        }

        if (ContractPortafolioProductos.uriMatcher.match(uri) == ContractPortafolioProductos.ALLROWS) {
            ContentValues contentValues;
            if (values != null) {
                contentValues = new ContentValues(values);
            } else {
                contentValues = new ContentValues();
            }

            // Inserción de nueva fila
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            long rowId = db.insert(ContractPortafolioProductos.PORTAFOLIOPRODUCTOS, null, contentValues);
            if (rowId > 0) {
                Uri uri_stock = ContentUris.withAppendedId(
                        ContractPortafolioProductos.CONTENT_URI, rowId);
                resolver.notifyChange(uri_stock, null, false);
                return uri_stock;
            }
            throw new SQLException("Falla al insertar fila en : " + uri);
        }

        if (ContractPortafolioProductosAASS.uriMatcher.match(uri) == ContractPortafolioProductosAASS.ALLROWS) {
            ContentValues contentValues;
            if (values != null) {
                contentValues = new ContentValues(values);
            } else {
                contentValues = new ContentValues();
            }

            // Inserción de nueva fila
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            long rowId = db.insert(ContractPortafolioProductosAASS.PORTAFOLIOPRODUCTOS_AASS, null, contentValues);
            if (rowId > 0) {
                Uri uri_stock = ContentUris.withAppendedId(
                        ContractPortafolioProductosAASS.CONTENT_URI, rowId);
                resolver.notifyChange(uri_stock, null, false);
                return uri_stock;
            }
            throw new SQLException("Falla al insertar fila en : " + uri);
        }

        if (ContractPortafolioProductosMAYO.uriMatcher.match(uri) == ContractPortafolioProductosMAYO.ALLROWS) {
            ContentValues contentValues;
            if (values != null) {
                contentValues = new ContentValues(values);
            } else {
                contentValues = new ContentValues();
            }

            // Inserción de nueva fila
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            long rowId = db.insert(ContractPortafolioProductosMAYO.PORTAFOLIOPRODUCTOS_MAYO, null, contentValues);
            if (rowId > 0) {
                Uri uri_stock = ContentUris.withAppendedId(
                        ContractPortafolioProductosMAYO.CONTENT_URI, rowId);
                resolver.notifyChange(uri_stock, null, false);
                return uri_stock;
            }
            throw new SQLException("Falla al insertar fila en : " + uri);
        }

        //INSERTS//

        if (ContractInsertPrecios.uriMatcher.match(uri) == ContractInsertPrecios.ALLROWS) {
            ContentValues contentValues;
            if (values != null) {
                contentValues = new ContentValues(values);
            } else {
                contentValues = new ContentValues();
            }

            // Inserción de nueva fila
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            long rowId = db.insert(ContractInsertPrecios.INSERT_PRECIOS, null, contentValues);
            if (rowId > 0) {
                Uri uri_stock = ContentUris.withAppendedId(
                        ContractInsertPrecios.CONTENT_URI, rowId);
                resolver.notifyChange(uri_stock, null, false);
                return uri_stock;
            }
            throw new SQLException("Falla al insertar fila en : " + uri);
        }

        if (ContractCausalesAsistencia.uriMatcher.match(uri) == ContractCausalesAsistencia.ALLROWS) {
            ContentValues contentValues;
            if (values != null) {
                contentValues = new ContentValues(values);
            } else {
                contentValues = new ContentValues();
            }

            // Inserción de nueva fila
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            long rowId = db.insert(ContractCausalesAsistencia.CAUSALES_ASISTENCIA, null, contentValues);
            if (rowId > 0) {
                Uri uri_stock = ContentUris.withAppendedId(
                        ContractCausalesAsistencia.CONTENT_URI, rowId);
                resolver.notifyChange(uri_stock, null, false);
                return uri_stock;
            }
            throw new SQLException("Falla al insertar fila en : " + uri);
        }

        if (ContractCausalesAsistenciaAtraso.uriMatcher.match(uri) == ContractCausalesAsistenciaAtraso.ALLROWS) {
            ContentValues contentValues;
            if (values != null) {
                contentValues = new ContentValues(values);
            } else {
                contentValues = new ContentValues();
            }
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            long rowId = db.insert(ContractCausalesAsistenciaAtraso.CAUSALES_ASISTENCIA_ATRASO, null, contentValues);
            if (rowId > 0) {
                Uri uri_stock = ContentUris.withAppendedId(ContractCausalesAsistenciaAtraso.CONTENT_URI, rowId);
                resolver.notifyChange(uri_stock, null, false);
                return uri_stock;
            }
            throw new SQLException("Falla al insertar fila en : " + uri);
        }

        if (ContractInsertPacks.uriMatcher.match(uri) == ContractInsertPacks.ALLROWS) {
            ContentValues contentValues;
            if (values != null) {
                contentValues = new ContentValues(values);
            } else {
                contentValues = new ContentValues();
            }

            // Inserción de nueva fila
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            long rowId = db.insert(ContractInsertPacks.INSERT_PACKS, null, contentValues);
            if (rowId > 0) {
                Uri uri_stock = ContentUris.withAppendedId(
                        ContractInsertPacks.CONTENT_URI, rowId);
                resolver.notifyChange(uri_stock, null, false);
                return uri_stock;
            }
            throw new SQLException("Falla al insertar fila en : " + uri);
        }





        if (ContractInsertPropensosYProdMalEst.uriMatcher.match(uri) == ContractInsertPropensosYProdMalEst.ALLROWS) {
            ContentValues contentValues;
            if (values != null) {
                contentValues = new ContentValues(values);
            } else {
                contentValues = new ContentValues();
            }

            // Inserción de nueva fila
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            long rowId = db.insert(ContractInsertPropensosYProdMalEst.INSERT_PROP_MAL_EST, null, contentValues);
            if (rowId > 0) {
                Uri uri_stock = ContentUris.withAppendedId(
                        ContractInsertPropensosYProdMalEst.CONTENT_URI, rowId);
                resolver.notifyChange(uri_stock, null, false);
                return uri_stock;
            }
            throw new SQLException("Falla al insertar fila en : " + uri);
        }



        if (ContractInsertAlmuerzo.uriMatcher.match(uri) == ContractInsertAlmuerzo.ALLROWS) {
            ContentValues contentValues;
            if (values != null) {
                contentValues = new ContentValues(values);
            } else {
                contentValues = new ContentValues();
            }

            // Inserción de nueva fila
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            long rowId = db.insert(ContractInsertAlmuerzo.INSERT_ALMUERZO, null, contentValues);
            if (rowId > 0) {
                Uri uri_stock = ContentUris.withAppendedId(
                        ContractInsertAlmuerzo.CONTENT_URI, rowId);
                resolver.notifyChange(uri_stock, null, false);
                return uri_stock;
            }
            throw new SQLException("Falla al insertar fila en : " + uri);
        }


        if (ContractInsertProyectosContacto.uriMatcher.match(uri) == ContractInsertProyectosContacto.ALLROWS) {
            ContentValues contentValues;
            if (values != null) {
                contentValues = new ContentValues(values);
            } else {
                contentValues = new ContentValues();
            }

            // Inserción de nueva fila
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            long rowId = db.insert(ContractInsertProyectosContacto.INSERT_PROYECTOS_CONTACTO, null, contentValues);
            if (rowId > 0) {
                Uri uri_stock = ContentUris.withAppendedId(
                        ContractInsertProyectosContacto.CONTENT_URI, rowId);
                resolver.notifyChange(uri_stock, null, false);
                return uri_stock;
            }
            throw new SQLException("Falla al insertar fila en : " + uri);
        }

        if (ContractInsertProforma.uriMatcher.match(uri) == ContractInsertProforma.ALLROWS) {
            ContentValues contentValues;
            if (values != null) {
                contentValues = new ContentValues(values);
            } else {
                contentValues = new ContentValues();
            }

            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            long rowId = db.insert(ContractInsertProforma.INSERT_PROFORMA, null, contentValues);
            if (rowId > 0) {
                Uri uri_proforma = ContentUris.withAppendedId(
                        ContractInsertProforma.CONTENT_URI, rowId);
                resolver.notifyChange(uri_proforma, null, false);
                return uri_proforma;
            }
            throw new SQLException("Falla al insertar fila en : " + uri);
        }

        if (ContractInsertPagoFactura.uriMatcher.match(uri) == ContractInsertPagoFactura.ALLROWS) {
            ContentValues contentValues;
            if (values != null) {
                contentValues = new ContentValues(values);
            } else {
                contentValues = new ContentValues();
            }

            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            long rowId = db.insert(ContractInsertPagoFactura.INSERT_PAGO_FACTURA, null, contentValues);
            if (rowId > 0) {
                Uri uri_pago_factura = ContentUris.withAppendedId(
                        ContractInsertPagoFactura.CONTENT_URI, rowId);
                resolver.notifyChange(uri_pago_factura, null, false);
                return uri_pago_factura;
            }
            throw new SQLException("Falla al insertar fila en : " + uri);
        }



        if (ContractInsertPdvFotografico.uriMatcher.match(uri) == ContractInsertPdvFotografico.ALLROWS) {
            ContentValues contentValues;
            if (values != null) {
                contentValues = new ContentValues(values);
            } else {
                contentValues = new ContentValues();
            }

            // Inserción de nueva fila
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            long rowId = db.insert(ContractInsertPdvFotografico.INSERT_PVD_FOTOGRAFICO, null, contentValues);
            if (rowId > 0) {
                Uri uri_stock = ContentUris.withAppendedId(
                        ContractInsertPdvFotografico.CONTENT_URI, rowId);
                resolver.notifyChange(uri_stock, null, false);
                return uri_stock;
            }
            throw new SQLException("Falla al insertar fila en : " + uri);
        }




        if (ContractInsertVentas.uriMatcher.match(uri) == ContractInsertVentas.ALLROWS) {
            ContentValues contentValues;
            if (values != null) {
                contentValues = new ContentValues(values);
            } else {
                contentValues = new ContentValues();
            }

            // Inserción de nueva fila
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            long rowId = db.insert(ContractInsertVentas.INSERT_VENTAS, null, contentValues);
            if (rowId > 0) {
                Uri uri_stock = ContentUris.withAppendedId(
                        ContractInsertVentas.CONTENT_URI, rowId);
                resolver.notifyChange(uri_stock, null, false);
                return uri_stock;
            }
            throw new SQLException("Falla al insertar fila en : " + uri);
        }





        if (ContractInsertProdCaducar.uriMatcher.match(uri) == ContractInsertProdCaducar.ALLROWS) {
            ContentValues contentValues;
            if (values != null) {
                contentValues = new ContentValues(values);
            } else {
                contentValues = new ContentValues();
            }

            // Inserción de nueva fila
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            long rowId = db.insert(ContractInsertProdCaducar.INSERT_PROD_CADUCAR, null, contentValues);
            if (rowId > 0) {
                Uri uri_stock = ContentUris.withAppendedId(
                        ContractInsertProdCaducar.CONTENT_URI, rowId);
                resolver.notifyChange(uri_stock, null, false);
                return uri_stock;
            }
            throw new SQLException("Falla al insertar fila en : " + uri);
        }

        if (ContractInsertSugeridos.uriMatcher.match(uri) == ContractInsertSugeridos.ALLROWS) {
            ContentValues contentValues;
            if (values != null) {
                contentValues = new ContentValues(values);
            } else {
                contentValues = new ContentValues();
            }

            // Inserción de nueva fila
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            long rowId = db.insert(ContractInsertSugeridos.INSERT_SUGERIDOS, null, contentValues);
            if (rowId > 0) {
                Uri uri_stock = ContentUris.withAppendedId(
                        ContractInsertSugeridos.CONTENT_URI, rowId);
                resolver.notifyChange(uri_stock, null, false);
                return uri_stock;
            }
            throw new SQLException("Falla al insertar fila en : " + uri);
        }

        if (ContractInsertCanjes.uriMatcher.match(uri) == ContractInsertCanjes.ALLROWS) {
            ContentValues contentValues;
            if (values != null) {
                contentValues = new ContentValues(values);
            } else {
                contentValues = new ContentValues();
            }

            // Inserción de nueva fila
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            long rowId = db.insert(ContractInsertCanjes.INSERT_CANJES, null, contentValues);
            if (rowId > 0) {
                Uri uri_stock = ContentUris.withAppendedId(
                        ContractInsertCanjes.CONTENT_URI, rowId);
                resolver.notifyChange(uri_stock, null, false);
                return uri_stock;
            }
            throw new SQLException("Falla al insertar fila en : " + uri);
        }

        if (ContractInsertMaterialesRecibidos.uriMatcher.match(uri) == ContractInsertMaterialesRecibidos.ALLROWS) {
            ContentValues contentValues;
            if (values != null) {
                contentValues = new ContentValues(values);
            } else {
                contentValues = new ContentValues();
            }

            // Inserción de nueva fila
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            long rowId = db.insert(ContractInsertMaterialesRecibidos.INSERT_MATERIALES_RECIBIDOS, null, contentValues);
            if (rowId > 0) {
                Uri uri_stock = ContentUris.withAppendedId(
                        ContractInsertMaterialesRecibidos.CONTENT_URI, rowId);
                resolver.notifyChange(uri_stock, null, false);
                return uri_stock;
            }
            throw new SQLException("Falla al insertar fila en : " + uri);
        }

        if (ContractInsertEjecucionMateriales.uriMatcher.match(uri) == ContractInsertEjecucionMateriales.ALLROWS) {
            ContentValues contentValues;
            if (values != null) {
                contentValues = new ContentValues(values);
            } else {
                contentValues = new ContentValues();
            }

            // Inserción de nueva fila
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            long rowId = db.insert(ContractInsertEjecucionMateriales.INSERT_EJECUCION_MATERIALES, null, contentValues);
            if (rowId > 0) {
                Uri uri_stock = ContentUris.withAppendedId(
                        ContractInsertEjecucionMateriales.CONTENT_URI, rowId);
                resolver.notifyChange(uri_stock, null, false);
                return uri_stock;
            }
            throw new SQLException("Falla al insertar fila en : " + uri);
        }

        if (ContractInsertMCIPdv.uriMatcher.match(uri) == ContractInsertMCIPdv.ALLROWS) {
            ContentValues contentValues;
            if (values != null) {
                contentValues = new ContentValues(values);
            } else {
                contentValues = new ContentValues();
            }

            // Inserción de nueva fila
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            long rowId = db.insert(ContractInsertMCIPdv.INSERT_MCI, null, contentValues);
            if (rowId > 0) {
                Uri uri_stock = ContentUris.withAppendedId(
                        ContractInsertMCIPdv.CONTENT_URI, rowId);
                resolver.notifyChange(uri_stock, null, false);
                return uri_stock;
            }
            throw new SQLException("Falla al insertar fila en : " + uri);
        }

        if (ContractInsertMallaCodificados.uriMatcher.match(uri) == ContractInsertMallaCodificados.ALLROWS) {
            ContentValues contentValues;
            if (values != null) {
                contentValues = new ContentValues(values);
            } else {
                contentValues = new ContentValues();
            }

            // Inserción de nueva fila
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            long rowId = db.insert(ContractInsertMallaCodificados.INSERT_MALLA_CODIFICADOS, null, contentValues);
            if (rowId > 0) {
                Uri uri_stock = ContentUris.withAppendedId(
                        ContractInsertMallaCodificados.CONTENT_URI, rowId);
                resolver.notifyChange(uri_stock, null, false);
                return uri_stock;
            }
            throw new SQLException("Falla al insertar fila en : " + uri);
        }

        if (ContractInsertTareas.uriMatcher.match(uri) == ContractInsertTareas.ALLROWS) {
            ContentValues contentValues;
            if (values != null) {
                contentValues = new ContentValues(values);
            } else {
                contentValues = new ContentValues();
            }

            // Inserción de nueva fila
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            long rowId = db.insert(ContractInsertTareas.INSERT_TAREAS, null, contentValues);
            if (rowId > 0) {
                Uri uri_stock = ContentUris.withAppendedId(
                        ContractInsertTareas.CONTENT_URI, rowId);
                resolver.notifyChange(uri_stock, null, false);
                return uri_stock;
            }
            throw new SQLException("Falla al insertar fila en : " + uri);
        }

        if (ContractInsertRotacion.uriMatcher.match(uri) == ContractInsertRotacion.ALLROWS) {
            ContentValues contentValues;
            if (values != null) {
                contentValues = new ContentValues(values);
            } else {
                contentValues = new ContentValues();
            }

            // Inserción de nueva fila
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            long rowId = db.insert(ContractInsertRotacion.INSERT_ROTACION, null, contentValues);
            if (rowId > 0) {
                Uri uri_stock = ContentUris.withAppendedId(
                        ContractInsertRotacion.CONTENT_URI, rowId);
                resolver.notifyChange(uri_stock, null, false);
                return uri_stock;
            }
            throw new SQLException("Falla al insertar fila en : " + uri);
        }

        if (ContractInsertPreguntas.uriMatcher.match(uri) == ContractInsertPreguntas.ALLROWS) {
            ContentValues contentValues;
            if (values != null) {
                contentValues = new ContentValues(values);
            } else {
                contentValues = new ContentValues();
            }

            // Inserción de nueva fila
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            long rowId = db.insert(ContractInsertPreguntas.INSERT_PREGUNTAS, null, contentValues);
            if (rowId > 0) {
                Uri uri_stock = ContentUris.withAppendedId(
                        ContractInsertPreguntas.CONTENT_URI, rowId);
                resolver.notifyChange(uri_stock, null, false);
                return uri_stock;
            }
            throw new SQLException("Falla al insertar fila en : " + uri);
        }


        if (ContractInsertResultadoPreguntas.uriMatcher.match(uri) == ContractInsertResultadoPreguntas.ALLROWS) {
            ContentValues contentValues;
            if (values != null) {
                contentValues = new ContentValues(values);
            } else {
                contentValues = new ContentValues();
            }

            // Inserción de nueva fila
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            long rowId = db.insert(ContractInsertResultadoPreguntas.INSERT_RESULTADO_PREGUNTAS, null, contentValues);
            if (rowId > 0) {
                Uri uri_stock = ContentUris.withAppendedId(
                        ContractInsertResultadoPreguntas.CONTENT_URI, rowId);
                resolver.notifyChange(uri_stock, null, false);
                return uri_stock;
            }
            throw new SQLException("Falla al insertar fila en : " + uri);
        }



        if (ContractInsertPreciosSesion.uriMatcher.match(uri) == ContractInsertPreciosSesion.ALLROWS) {
            ContentValues contentValues;
            if (values != null) {
                contentValues = new ContentValues(values);
            } else {
                contentValues = new ContentValues();
            }

            // Inserción de nueva fila
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            long rowId = db.insert(ContractInsertPreciosSesion.INSERT_PRECIOS_SESION, null, contentValues);
            if (rowId > 0) {
                Uri uri_stock = ContentUris.withAppendedId(
                        ContractInsertPreciosSesion.CONTENT_URI, rowId);
                resolver.notifyChange(uri_stock, null, false);
                return uri_stock;
            }
            throw new SQLException("Falla al insertar fila en : " + uri);
        }

        if (InsertFlooring.uriMatcher.match(uri) == InsertFlooring.ALLROWS) {
            ContentValues contentValues;
            if (values != null) {
                contentValues = new ContentValues(values);
            } else {
                contentValues = new ContentValues();
            }

            // Inserción de nueva fila
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            long rowId = db.insert(InsertFlooring.INSERT_FLOORING, null, contentValues);
            if (rowId > 0) {
                Uri uri_stock = ContentUris.withAppendedId(
                        InsertFlooring.CONTENT_URI, rowId);
                resolver.notifyChange(uri_stock, null, false);
                return uri_stock;
            }
            throw new SQLException("Falla al insertar fila en : " + uri);
        }

        if (ContractInsertImpulso.uriMatcher.match(uri) == ContractInsertImpulso.ALLROWS) {
            ContentValues contentValues;
            if (values != null) {
                contentValues = new ContentValues(values);
            } else {
                contentValues = new ContentValues();
            }

            // Inserción de nueva fila
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            long rowId = db.insert(ContractInsertImpulso.INSERT_IMPUSLO, null, contentValues);
            if (rowId > 0) {
                Uri uri_stock = ContentUris.withAppendedId(
                        ContractInsertImpulso.CONTENT_URI, rowId);
                resolver.notifyChange(uri_stock, null, false);
                return uri_stock;
            }
            throw new SQLException("Falla al insertar fila en : " + uri);
        }

        if (ContractInsertExh.uriMatcher.match(uri) == ContractInsertExh.ALLROWS) {
            ContentValues contentValues;
            if (values != null) {
                contentValues = new ContentValues(values);
            } else {
                contentValues = new ContentValues();
            }

            // Inserción de nueva fila
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            long rowId = db.insert(ContractInsertExh.INSERT_EXH, null, contentValues);
            if (rowId > 0) {
                Uri uri_stock = ContentUris.withAppendedId(
                        ContractInsertExh.CONTENT_URI, rowId);
                resolver.notifyChange(uri_stock, null, false);
                return uri_stock;
            }
            throw new SQLException("Falla al insertar fila en : " + uri);
        }

        if (ContractInsertExhSesion.uriMatcher.match(uri) == ContractInsertExhSesion.ALLROWS) {
            ContentValues contentValues;
            if (values != null) {
                contentValues = new ContentValues(values);
            } else {
                contentValues = new ContentValues();
            }

            // Inserción de nueva fila
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            long rowId = db.insert(ContractInsertExhSesion.INSERT_EXH_SESION, null, contentValues);
            if (rowId > 0) {
                Uri uri_stock = ContentUris.withAppendedId(
                        ContractInsertExhSesion.CONTENT_URI, rowId);
                resolver.notifyChange(uri_stock, null, false);
                return uri_stock;
            }
            throw new SQLException("Falla al insertar fila en : " + uri);
        }

        if (ContractInsertFotografico.uriMatcher.match(uri) == ContractInsertFotografico.ALLROWS) {
            ContentValues contentValues;
            if (values != null) {
                contentValues = new ContentValues(values);
            } else {
                contentValues = new ContentValues();
            }

            // Inserción de nueva fila
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            long rowId = db.insert(ContractInsertFotografico.INSERT_FOTOGRAFICO, null, contentValues);
            if (rowId > 0) {
                Uri uri_stock = ContentUris.withAppendedId(
                        ContractInsertFotografico.CONTENT_URI, rowId);
                resolver.notifyChange(uri_stock, null, false);
                return uri_stock;
            }
            throw new SQLException("Falla al insertar fila en : " + uri);
        }

        if (ContractRangosPrecios.uriMatcher.match(uri) == ContractRangosPrecios.ALLROWS) {
            ContentValues contentValues;
            if (values != null) {
                contentValues = new ContentValues(values);
            } else {
                contentValues = new ContentValues();
            }

            // Inserción de nueva fila
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            long rowId = db.insert(ContractRangosPrecios.RANGOS_PRECIOS, null, contentValues);
            if (rowId > 0) {
                Uri uri_stock = ContentUris.withAppendedId(
                        ContractRangosPrecios.CONTENT_URI, rowId);
                resolver.notifyChange(uri_stock, null, false);
                return uri_stock;
            }
            throw new SQLException("Falla al insertar fila en : " + uri);
        }

        if (ContractRangosPreciosSku.uriMatcher.match(uri) == ContractRangosPreciosSku.ALLROWS) {
            ContentValues contentValues;
            if (values != null) {
                contentValues = new ContentValues(values);
            } else {
                contentValues = new ContentValues();
            }

            // Inserción de nueva fila
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            long rowId = db.insert(ContractRangosPreciosSku.RANGOS_PRECIOS_SKU, null, contentValues);
            if (rowId > 0) {
                Uri uri_stock = ContentUris.withAppendedId(
                        ContractRangosPreciosSku.CONTENT_URI, rowId);
                resolver.notifyChange(uri_stock, null, false);
                return uri_stock;
            }
            throw new SQLException("Falla al insertar fila en : " + uri);
        }

        if (ContractInsertAsistencia.uriMatcher.match(uri) == ContractInsertAsistencia.ALLROWS) {
            ContentValues contentValues;
            if (values != null) {
                contentValues = new ContentValues(values);
            } else {
                contentValues = new ContentValues();
            }

            // Inserción de nueva fila
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            long rowId = db.insert(ContractInsertAsistencia.INSERT_ASISTENCIA, null, contentValues);
            if (rowId > 0) {
                Uri uri_stock = ContentUris.withAppendedId(
                        ContractInsertAsistencia.CONTENT_URI, rowId);
                resolver.notifyChange(uri_stock, null, false);
                return uri_stock;
            }
            throw new SQLException("Falla al insertar fila en : " + uri);
        }

        if (ContractAsistenciasLocal.uriMatcher.match(uri) == ContractAsistenciasLocal.ALLROWS) {
            ContentValues contentValues;
            if (values != null) {
                contentValues = new ContentValues(values);
            } else {
                contentValues = new ContentValues();
            }

            // Inserción de nueva fila
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            long rowId = db.insert(ContractAsistenciasLocal.TABLE_NAME, null, contentValues);
            if (rowId > 0) {
                Uri uri_stock = ContentUris.withAppendedId(
                        ContractAsistenciasLocal.CONTENT_URI, rowId);
                resolver.notifyChange(uri_stock, null, false);
                return uri_stock;
            }
            throw new SQLException("Falla al insertar fila en : " + uri);
        }

        if (ContractHoraMarcacion.uriMatcher.match(uri) == ContractHoraMarcacion.ALLROWS) {
            ContentValues contentValues;
            if (values != null) {
                contentValues = new ContentValues(values);
            } else {
                contentValues = new ContentValues();
            }

            // Inserción de nueva fila
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            long rowId = db.insert(ContractHoraMarcacion.TABLE_NAME, null, contentValues);
            if (rowId > 0) {
                Uri uri_stock = ContentUris.withAppendedId(
                        ContractHoraMarcacion.CONTENT_URI, rowId);
                resolver.notifyChange(uri_stock, null, false);
                return uri_stock;
            }
            throw new SQLException("Falla al insertar fila en : " + uri);
        }

        if (ContractInsertEvidencias.uriMatcher.match(uri) == ContractInsertEvidencias.ALLROWS) {
            ContentValues contentValues;
            if (values != null) {
                contentValues = new ContentValues(values);
            } else {
                contentValues = new ContentValues();
            }

            // Inserción de nueva fila
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            long rowId = db.insert(ContractInsertEvidencias.INSERT_EVIDENCIAS, null, contentValues);
            if (rowId > 0) {
                Uri uri_stock = ContentUris.withAppendedId(
                        ContractInsertEvidencias.CONTENT_URI, rowId);
                resolver.notifyChange(uri_stock, null, false);
                return uri_stock;
            }
            throw new SQLException("Falla al insertar fila en : " + uri);
        }

        if (ContractInsertGps.uriMatcher.match(uri) == ContractInsertGps.ALLROWS) {
            ContentValues contentValues;
            if (values != null) {
                contentValues = new ContentValues(values);
            } else {
                contentValues = new ContentValues();
            }

            // Inserción de nueva fila
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            long rowId = db.insert(ContractInsertGps.INSERT_GPS, null, contentValues);
            if (rowId > 0) {
                Uri uri_stock = ContentUris.withAppendedId(
                        ContractInsertGps.CONTENT_URI, rowId);
                resolver.notifyChange(uri_stock, null, false);
                return uri_stock;
            }
            throw new SQLException("Falla al insertar fila en : " + uri);
        }

        if (ContractInsertRastreo.uriMatcher.match(uri) == ContractInsertRastreo.ALLROWS) {
            ContentValues contentValues;
            if (values != null) {
                contentValues = new ContentValues(values);
            } else {
                contentValues = new ContentValues();
            }

            // Inserción de nueva fila
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            long rowId = db.insert(ContractInsertRastreo.INSERT_GEO, null, contentValues);
            if (rowId > 0) {
                Uri uri_stock = ContentUris.withAppendedId(
                        ContractInsertRastreo.CONTENT_URI, rowId);
                resolver.notifyChange(uri_stock, null, false);
                return uri_stock;
            }
            throw new SQLException("Falla al insertar fila en : " + uri);
        }

        if (ContractNotificacion.uriMatcher.match(uri) == ContractNotificacion.ALLROWS) {
            ContentValues contentValues;
            if (values != null) {
                contentValues = new ContentValues(values);
            } else {
                contentValues = new ContentValues();
            }

            // Inserción de nueva fila
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            long rowId = db.insert(ContractNotificacion.NOTIFICACION, null, contentValues);
            if (rowId > 0) {
                Uri uri_stock = ContentUris.withAppendedId(
                        ContractNotificacion.CONTENT_URI, rowId);
                resolver.notifyChange(uri_stock, null, false);
                return uri_stock;
            }
            throw new SQLException("Falla al insertar fila en : " + uri);
        }

        if (ContractInsertInicial.uriMatcher.match(uri) == ContractInsertInicial.ALLROWS) {
            ContentValues contentValues;
            if (values != null) {
                contentValues = new ContentValues(values);
            } else {
                contentValues = new ContentValues();
            }

            // Inserción de nueva fila
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            long rowId = db.insert(ContractInsertInicial.INSERT_INICIAL, null, contentValues);
            if (rowId > 0) {
                Uri uri_stock = ContentUris.withAppendedId(
                        ContractInsertInicial.CONTENT_URI, rowId);
                resolver.notifyChange(uri_stock, null, false);
                return uri_stock;
            }
            throw new SQLException("Falla al insertar fila en : " + uri);
        }

        if (ContractInsertPromocion.uriMatcher.match(uri) == ContractInsertPromocion.ALLROWS) {
            ContentValues contentValues;
            if (values != null) {
                contentValues = new ContentValues(values);
            } else {
                contentValues = new ContentValues();
            }

            // Inserción de nueva fila
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            long rowId = db.insert(ContractInsertPromocion.INSERT_PROMO, null, contentValues);
            if (rowId > 0) {
                Uri uri_stock = ContentUris.withAppendedId(
                        ContractInsertPromocion.CONTENT_URI, rowId);
                resolver.notifyChange(uri_stock, null, false);
                return uri_stock;
            }
            throw new SQLException("Falla al insertar fila en : " + uri);
        }

        if (ContractInsertImplementacion.uriMatcher.match(uri) == ContractInsertImplementacion.ALLROWS) {
            ContentValues contentValues;
            if (values != null) {
                contentValues = new ContentValues(values);
            } else {
                contentValues = new ContentValues();
            }

            // Inserción de nueva fila
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            long rowId = db.insert(ContractInsertImplementacion.INSERT_IMPLEM, null, contentValues);
            if (rowId > 0) {
                Uri uri_stock = ContentUris.withAppendedId(
                        ContractInsertImplementacion.CONTENT_URI, rowId);
                resolver.notifyChange(uri_stock, null, false);
                return uri_stock;
            }
            throw new SQLException("Falla al insertar fila en : " + uri);
        }

        if (ContractInsertValores.uriMatcher.match(uri) == ContractInsertValores.ALLROWS) {
            ContentValues contentValues;
            if (values != null) {
                contentValues = new ContentValues(values);
            } else {
                contentValues = new ContentValues();
            }

            // Inserción de nueva fila
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            long rowId = db.insert(ContractInsertValores.INSERT_VALORES, null, contentValues);
            if (rowId > 0) {
                Uri uri_stock = ContentUris.withAppendedId(
                        ContractInsertValores.CONTENT_URI, rowId);
                resolver.notifyChange(uri_stock, null, false);
                return uri_stock;
            }
            throw new SQLException("Falla al insertar fila en : " + uri);
        }

        if (ContractInsertValoresSesion.uriMatcher.match(uri) == ContractInsertValoresSesion.ALLROWS) {
            ContentValues contentValues;
            if (values != null) {
                contentValues = new ContentValues(values);
            } else {
                contentValues = new ContentValues();
            }

            // Inserción de nueva fila
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            long rowId = db.insert(ContractInsertValoresSesion.INSERT_VALORES_SESION, null, contentValues);
            if (rowId > 0) {
                Uri uri_stock = ContentUris.withAppendedId(
                        ContractInsertValoresSesion.CONTENT_URI, rowId);
                resolver.notifyChange(uri_stock, null, false);
                return uri_stock;
            }
            throw new SQLException("Falla al insertar fila en : " + uri);
        }

        if (ContractInsertPdv.uriMatcher.match(uri) == ContractInsertPdv.ALLROWS) {
            ContentValues contentValues;
            if (values != null) {
                contentValues = new ContentValues(values);
            } else {
                contentValues = new ContentValues();
            }

            // Inserción de nueva fila
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            long rowId = db.insert(ContractInsertPdv.INSERT_PDV, null, contentValues);
            if (rowId > 0) {
                Uri uri_stock = ContentUris.withAppendedId(
                        ContractInsertPdv.CONTENT_URI, rowId);
                resolver.notifyChange(uri_stock, null, false);
                return uri_stock;
            }
            throw new SQLException("Falla al insertar fila en : " + uri);
        }

        if (ContractInsertShare.uriMatcher.match(uri) == ContractInsertShare.ALLROWS) {
            ContentValues contentValues;
            if (values != null) {
                contentValues = new ContentValues(values);
            } else {
                contentValues = new ContentValues();
            }

            // Inserción de nueva fila
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            long rowId = db.insert(ContractInsertShare.INSERT_SHARE, null, contentValues);
            if (rowId > 0) {
                Uri uri_stock = ContentUris.withAppendedId(
                        ContractInsertShare.CONTENT_URI, rowId);
                resolver.notifyChange(uri_stock, null, false);
                return uri_stock;
            }
            throw new SQLException("Falla al insertar fila en : " + uri);
        }

        if (ContractInsertPDI.uriMatcher.match(uri) == ContractInsertPDI.ALLROWS) {
            ContentValues contentValues;
            if (values != null) {
                contentValues = new ContentValues(values);
            } else {
                contentValues = new ContentValues();
            }

            // Inserción de nueva fila
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            long rowId = db.insert(ContractInsertPDI.INSERT_PDI, null, contentValues);
            if (rowId > 0) {
                Uri uri_stock = ContentUris.withAppendedId(
                        ContractInsertPDI.CONTENT_URI, rowId);
                resolver.notifyChange(uri_stock, null, false);
                return uri_stock;
            }
            throw new SQLException("Falla al insertar fila en : " + uri);
        }

        if (ContractInsertAgotados.uriMatcher.match(uri) == ContractInsertAgotados.ALLROWS) {
            ContentValues contentValues;
            if (values != null) {
                contentValues = new ContentValues(values);
            } else {
                contentValues = new ContentValues();
            }

            // Inserción de nueva fila
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            long rowId = db.insert(ContractInsertAgotados.INSERT_AGOTADOS, null, contentValues);
            if (rowId > 0) {
                Uri uri_stock = ContentUris.withAppendedId(
                        ContractInsertAgotados.CONTENT_URI, rowId);
                resolver.notifyChange(uri_stock, null, false);
                return uri_stock;
            }
            throw new SQLException("Falla al insertar fila en : " + uri);
        }

        if (ContractInsertVenta.uriMatcher.match(uri) == ContractInsertVenta.ALLROWS) {
            ContentValues contentValues;
            if (values != null) {
                contentValues = new ContentValues(values);
            } else {
                contentValues = new ContentValues();
            }

            // Inserción de nueva fila
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            long rowId = db.insert(ContractInsertVenta.INSERT_VENTA, null, contentValues);
            if (rowId > 0) {
                Uri uri_stock = ContentUris.withAppendedId(
                        ContractInsertVenta.CONTENT_URI, rowId);
                resolver.notifyChange(uri_stock, null, false);
                return uri_stock;
            }
            throw new SQLException("Falla al insertar fila en : " + uri);
        }

        if (ContractInsertLogisticoRelevo.uriMatcher.match(uri) == ContractInsertLogisticoRelevo.ALLROWS) {
            ContentValues contentValues;
            if (values != null) {
                contentValues = new ContentValues(values);
            } else {
                contentValues = new ContentValues();
            }

            // Inserción de nueva fila
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            long rowId = db.insert(ContractInsertLogisticoRelevo.INSERT_LOGISTICO_RELEVO, null, contentValues);
            if (rowId > 0) {
                Uri uri_stock = ContentUris.withAppendedId(
                        ContractInsertLogisticoRelevo.CONTENT_URI, rowId);
                resolver.notifyChange(uri_stock, null, false);
                return uri_stock;
            }
            throw new SQLException("Falla al insertar fila en : " + uri);
        }

        throw new IllegalArgumentException("URI desconocida : " + uri);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        int affected;

        if (ContractPharmaValue.uriMatcher.match(uri)== ContractPharmaValue.ALLROWS) {
            affected = db.delete(ContractPharmaValue.POS,
                    selection,
                    selectionArgs);
        }else if (ContractPharmaValue.uriMatcher.match(uri)== ContractPharmaValue.SINGLE_ROW) {
            long idStock = ContentUris.parseId(uri);
            affected = db.delete(ContractPharmaValue.POS,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
            // Notificar cambio asociado a la uri
            resolver.
                    notifyChange(uri, null, false);
        }

        else if (ContractPrecios.uriMatcher.match(uri)==ContractPrecios.ALLROWS) {
            affected = db.delete(ContractPrecios.PRECIOS,
                    selection,
                    selectionArgs);
        }else if (ContractPrecios.uriMatcher.match(uri)==ContractPrecios.SINGLE_ROW) {
            long idStock = ContentUris.parseId(uri);
            affected = db.delete(ContractPrecios.PRECIOS,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
            // Notificar cambio asociado a la uri
            resolver.
                    notifyChange(uri, null, false);
        }


        else if (ContractPreciosPvc.uriMatcher.match(uri)==ContractPreciosPvc.ALLROWS) {
            affected = db.delete(ContractPreciosPvc.PRECIOS_PVC,
                    selection,
                    selectionArgs);
        }else if (ContractPreciosPvc.uriMatcher.match(uri)==ContractPreciosPvc.SINGLE_ROW) {
            long idStock = ContentUris.parseId(uri);
            affected = db.delete(ContractPreciosPvc.PRECIOS_PVC,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
            // Notificar cambio asociado a la uri
            resolver.
                    notifyChange(uri, null, false);
        }

        else if (ContractPreguntas.uriMatcher.match(uri)==ContractPreguntas.ALLROWS) {
            affected = db.delete(ContractPreguntas.TABLE_QUEST,
                    selection,
                    selectionArgs);
        }else if (ContractPreguntas.uriMatcher.match(uri)==ContractPreguntas.SINGLE_ROW) {
            long idStock = ContentUris.parseId(uri);
            affected = db.delete(ContractPreguntas.TABLE_QUEST,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
            // Notificar cambio asociado a la uri
            resolver.
                    notifyChange(uri, null, false);
        }


        else if(ContractJustificacion.uriMatcher.match(uri)==ContractJustificacion.ALLROWS){
            affected = db.delete(ContractJustificacion.JUSTIFICACIONES,
                    selection,
                    selectionArgs);
        }else if(ContractJustificacion.uriMatcher.match(uri)==ContractJustificacion.SINGLE_ROW){
            long idStock = ContentUris.parseId(uri);
            affected = db.delete(ContractJustificacion.JUSTIFICACIONES,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
            // Notificar cambio asociado a la uri
            resolver.
                    notifyChange(uri, null, false);
        }

        else if(ContractTipoPrecios.uriMatcher.match(uri)==ContractTipoPrecios.ALLROWS){
            affected = db.delete(ContractTipoPrecios.TIPO_PRECIOS,
                    selection,
                    selectionArgs);
        }else if(ContractTipoPrecios.uriMatcher.match(uri)==ContractTipoPrecios.SINGLE_ROW){
            long idStock = ContentUris.parseId(uri);
            affected = db.delete(ContractTipoPrecios.TIPO_PRECIOS,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
            // Notificar cambio asociado a la uri
            resolver.
                    notifyChange(uri, null, false);
        }

        else if(ContractMarcasBlancas.uriMatcher.match(uri)==ContractMarcasBlancas.ALLROWS){
            affected = db.delete(ContractMarcasBlancas.MARCAS_BLANCAS,
                    selection,
                    selectionArgs);
        }else if(ContractMarcasBlancas.uriMatcher.match(uri)==ContractMarcasBlancas.SINGLE_ROW){
            long idStock = ContentUris.parseId(uri);
            affected = db.delete(ContractMarcasBlancas.MARCAS_BLANCAS,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
            // Notificar cambio asociado a la uri
            resolver.
                    notifyChange(uri, null, false);
        }

        else if (ContractTests.uriMatcher.match(uri)==ContractTests.ALLROWS) {
            affected = db.delete(ContractTests.TABLE_TEST,
                    selection,
                    selectionArgs);
        }else if (ContractTests.uriMatcher.match(uri)==ContractTests.SINGLE_ROW) {
            long idStock = ContentUris.parseId(uri);
            affected = db.delete(ContractTests.TABLE_TEST,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
            // Notificar cambio asociado a la uri
            resolver.
                    notifyChange(uri, null, false);
        }


        else if (ContractTipoExh.uriMatcher.match(uri)==ContractTipoExh.ALLROWS) {
            affected = db.delete(ContractTipoExh.TABLE_NAME,
                    selection,
                    selectionArgs);
        }else if (ContractTipoExh.uriMatcher.match(uri)==ContractTipoExh.SINGLE_ROW) {
            long idStock = ContentUris.parseId(uri);
            affected = db.delete(ContractTipoExh.TABLE_NAME,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
            // Notificar cambio asociado a la uri
            resolver.
                    notifyChange(uri, null, false);
        }


        else if (ContractPromociones.uriMatcher.match(uri)== ContractPromociones.ALLROWS) {
            affected = db.delete(ContractPromociones.PROMOCIONES,
                    selection,
                    selectionArgs);
        }else if (ContractPromociones.uriMatcher.match(uri)== ContractPromociones.SINGLE_ROW) {
            long idStock = ContentUris.parseId(uri);
            affected = db.delete(ContractPromociones.PROMOCIONES,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
            // Notificar cambio asociado a la uri
            resolver.
                    notifyChange(uri, null, false);
        }

        else if (ContractRotacion.uriMatcher.match(uri)== ContractRotacion.ALLROWS) {
            affected = db.delete(ContractRotacion.ROTACION,
                    selection,
                    selectionArgs);
        }else if (ContractRotacion.uriMatcher.match(uri)== ContractRotacion.SINGLE_ROW) {
            long idStock = ContentUris.parseId(uri);
            affected = db.delete(ContractRotacion.ROTACION,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
            // Notificar cambio asociado a la uri
            resolver.
                    notifyChange(uri, null, false);
        }

        else if (ContractPopSugerido.uriMatcher.match(uri)== ContractPopSugerido.ALLROWS) {
            affected = db.delete(ContractPopSugerido.POPSUGERIDO,
                    selection,
                    selectionArgs);
        }else if (ContractPopSugerido.uriMatcher.match(uri)== ContractPopSugerido.SINGLE_ROW) {
            long idStock = ContentUris.parseId(uri);
            affected = db.delete(ContractPopSugerido.POPSUGERIDO,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
            // Notificar cambio asociado a la uri
            resolver.
                    notifyChange(uri, null, false);
        }


        else if (ContractCausalesProdMalEst.uriMatcher.match(uri)== ContractCausalesProdMalEst.ALLROWS) {
            affected = db.delete(ContractCausalesProdMalEst.CAUSALES_PROD_MAL_EST,
                    selection,
                    selectionArgs);
        }else if (ContractCausalesProdMalEst.uriMatcher.match(uri)== ContractCausalesProdMalEst.SINGLE_ROW) {
            long idStock = ContentUris.parseId(uri);
            affected = db.delete(ContractCausalesProdMalEst.CAUSALES_PROD_MAL_EST,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
            // Notificar cambio asociado a la uri
            resolver.
                    notifyChange(uri, null, false);
        }


        else if (ContractVersiones.uriMatcher.match(uri)== ContractVersiones.ALLROWS) {
            affected = db.delete(ContractVersiones.VERSIONES,
                    selection,
                    selectionArgs);
        }else if (ContractVersiones.uriMatcher.match(uri)== ContractVersiones.SINGLE_ROW) {
            long idStock = ContentUris.parseId(uri);
            affected = db.delete(ContractVersiones.VERSIONES,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
            // Notificar cambio asociado a la uri
            resolver.
                    notifyChange(uri, null, false);
        }


        else if (ContractComboCanjes.uriMatcher.match(uri)== ContractComboCanjes.ALLROWS) {
            affected = db.delete(ContractComboCanjes.COMBO_CANJES,
                    selection,
                    selectionArgs);
        }else if (ContractComboCanjes.uriMatcher.match(uri)== ContractComboCanjes.SINGLE_ROW) {
            long idStock = ContentUris.parseId(uri);
            affected = db.delete(ContractComboCanjes.COMBO_CANJES,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
            // Notificar cambio asociado a la uri
            resolver.
                    notifyChange(uri, null, false);
        }

        else if (ContractCausalesMCI.uriMatcher.match(uri)== ContractCausalesMCI.ALLROWS) {
            affected = db.delete(ContractCausalesMCI.CAUSALES_MCI,
                    selection,
                    selectionArgs);
        }else if (ContractCausalesMCI.uriMatcher.match(uri)== ContractCausalesMCI.SINGLE_ROW) {
            long idStock = ContentUris.parseId(uri);
            affected = db.delete(ContractCausalesMCI.CAUSALES_MCI,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
            // Notificar cambio asociado a la uri
            resolver.
                    notifyChange(uri, null, false);
        }

        else if (ContractRangosPrecios.uriMatcher.match(uri) == ContractRangosPrecios.ALLROWS) {
            affected = db.delete(ContractRangosPrecios.RANGOS_PRECIOS, selection, selectionArgs);
        } else if (ContractRangosPrecios.uriMatcher.match(uri) == ContractRangosPrecios.SINGLE_ROW) {
            long id = ContentUris.parseId(uri);
            affected = db.delete(ContractRangosPrecios.RANGOS_PRECIOS,
                    ContractRangosPrecios.Columnas._ID + "=" + id
                            + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""),
                    selectionArgs);
            resolver.notifyChange(uri, null, false);
        } else if (ContractRangosPrecios.uriMatcher.match(uri) == ContractRangosPrecios.BY_PRESENTACION) {
            String presentacion = uri.getLastPathSegment();
            affected = db.delete(ContractRangosPrecios.RANGOS_PRECIOS,
                    ContractRangosPrecios.Columnas.PRESENTACION + "='" + presentacion + "'"
                            + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""),
                    selectionArgs);
            resolver.notifyChange(uri, null, false);
        }

        else if (ContractRangosPreciosSku.uriMatcher.match(uri) == ContractRangosPreciosSku.ALLROWS) {
            affected = db.delete(ContractRangosPreciosSku.RANGOS_PRECIOS_SKU, selection, selectionArgs);
        } else if (ContractRangosPreciosSku.uriMatcher.match(uri) == ContractRangosPreciosSku.SINGLE_ROW) {
            long id = ContentUris.parseId(uri);
            affected = db.delete(ContractRangosPreciosSku.RANGOS_PRECIOS_SKU,
                    ContractRangosPreciosSku.Columnas._ID + "=" + id
                            + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""),
                    selectionArgs);
            resolver.notifyChange(uri, null, false);
        } else if (ContractRangosPreciosSku.uriMatcher.match(uri) == ContractRangosPreciosSku.BY_SKU) {
            String sku = uri.getLastPathSegment();
            affected = db.delete(ContractRangosPreciosSku.RANGOS_PRECIOS_SKU,
                    ContractRangosPreciosSku.Columnas.SKU + "='" + sku + "'"
                            + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""),
                    selectionArgs);
            resolver.notifyChange(uri, null, false);
        }

        else if (ContractCausalesOSA.uriMatcher.match(uri)== ContractCausalesOSA.ALLROWS) {
            affected = db.delete(ContractCausalesOSA.CAUSALES_OSA,
                    selection,
                    selectionArgs);
        }else if (ContractCausalesOSA.uriMatcher.match(uri)== ContractCausalesOSA.SINGLE_ROW) {
            long idStock = ContentUris.parseId(uri);
            affected = db.delete(ContractCausalesOSA.CAUSALES_OSA,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
            // Notificar cambio asociado a la uri
            resolver.
                    notifyChange(uri, null, false);
        }

        else if (ContractAlertas.uriMatcher.match(uri)== ContractAlertas.ALLROWS) {
            affected = db.delete(ContractAlertas.ALERTAS,
                    selection,
                    selectionArgs);
        }else if (ContractAlertas.uriMatcher.match(uri)== ContractAlertas.SINGLE_ROW) {
            long idStock = ContentUris.parseId(uri);
            affected = db.delete(ContractAlertas.ALERTAS,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
            // Notificar cambio asociado a la uri
            resolver.
                    notifyChange(uri, null, false);
        }


        else if (ContractTiempoAlmuerzo.uriMatcher.match(uri)== ContractTiempoAlmuerzo.ALLROWS) {
            affected = db.delete(ContractTiempoAlmuerzo.REPO_TIEMPO_ALMUERZO,
                    selection,
                    selectionArgs);
        }else if (ContractTiempoAlmuerzo.uriMatcher.match(uri)== ContractTiempoAlmuerzo.SINGLE_ROW) {
            long idStock = ContentUris.parseId(uri);
            affected = db.delete(ContractTiempoAlmuerzo.REPO_TIEMPO_ALMUERZO,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
            // Notificar cambio asociado a la uri
            resolver.
                    notifyChange(uri, null, false);
        }


        else if (ContractPDI.uriMatcher.match(uri)== ContractPDI.ALLROWS) {
            affected = db.delete(ContractPDI.PDI,
                    selection,
                    selectionArgs);
        }else if (ContractPDI.uriMatcher.match(uri)== ContractPDI.SINGLE_ROW) {
            long idStock = ContentUris.parseId(uri);
            affected = db.delete(ContractPDI.PDI,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
            // Notificar cambio asociado a la uri
            resolver.
                    notifyChange(uri, null, false);
        }

        else if (ContractLog.uriMatcher.match(uri)== ContractLog.ALLROWS) {
            affected = db.delete(ContractLog.LOG,
                    selection,
                    selectionArgs);
        }else if (ContractLog.uriMatcher.match(uri)== ContractLog.SINGLE_ROW) {
            long idStock = ContentUris.parseId(uri);
            affected = db.delete(ContractLog.LOG,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
            // Notificar cambio asociado a la uri
            resolver.
                    notifyChange(uri, null, false);
        }

        else if (ContractPrioritario.uriMatcher.match(uri)== ContractPrioritario.ALLROWS) {
            affected = db.delete(ContractPrioritario.PRIORITARIO,
                    selection,
                    selectionArgs);
        }else if (ContractPrioritario.uriMatcher.match(uri)== ContractPrioritario.SINGLE_ROW) {
            long idStock = ContentUris.parseId(uri);
            affected = db.delete(ContractPrioritario.PRIORITARIO,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
            // Notificar cambio asociado a la uri
            resolver.
                    notifyChange(uri, null, false);
        }

        else if (ContractTareas.uriMatcher.match(uri)== ContractTareas.ALLROWS) {
            affected = db.delete(ContractTareas.TAREA,
                    selection,
                    selectionArgs);
        }else if (ContractTareas.uriMatcher.match(uri)== ContractTareas.SINGLE_ROW) {
            long idStock = ContentUris.parseId(uri);
            affected = db.delete(ContractTareas.TAREA,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
            // Notificar cambio asociado a la uri
            resolver.
                    notifyChange(uri, null, false);
        }


        else if (ContractPortafolioProductos.uriMatcher.match(uri)== ContractPortafolioProductos.ALLROWS) {
            affected = db.delete(ContractPortafolioProductos.PORTAFOLIOPRODUCTOS,
                    selection,
                    selectionArgs);
        }else if (ContractPortafolioProductos.uriMatcher.match(uri)== ContractPortafolioProductos.SINGLE_ROW) {
            long idStock = ContentUris.parseId(uri);
            affected = db.delete(ContractPortafolioProductos.PORTAFOLIOPRODUCTOS,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
            // Notificar cambio asociado a la uri
            resolver.
                    notifyChange(uri, null, false);
        }

        else if (ContractPortafolioProductosAASS.uriMatcher.match(uri)== ContractPortafolioProductosAASS.ALLROWS) {
            affected = db.delete(ContractPortafolioProductosAASS.PORTAFOLIOPRODUCTOS_AASS,
                    selection,
                    selectionArgs);
        }else if (ContractPortafolioProductosAASS.uriMatcher.match(uri)== ContractPortafolioProductosAASS.SINGLE_ROW) {
            long idStock = ContentUris.parseId(uri);
            affected = db.delete(ContractPortafolioProductosAASS.PORTAFOLIOPRODUCTOS_AASS,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
            // Notificar cambio asociado a la uri
            resolver.
                    notifyChange(uri, null, false);
        }

        else if (ContractPortafolioProductosMAYO.uriMatcher.match(uri)== ContractPortafolioProductosMAYO.ALLROWS) {
            affected = db.delete(ContractPortafolioProductosMAYO.PORTAFOLIOPRODUCTOS_MAYO,
                    selection,
                    selectionArgs);
        }else if (ContractPortafolioProductosMAYO.uriMatcher.match(uri)== ContractPortafolioProductosMAYO.SINGLE_ROW) {
            long idStock = ContentUris.parseId(uri);
            affected = db.delete(ContractPortafolioProductosMAYO.PORTAFOLIOPRODUCTOS_MAYO,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
            // Notificar cambio asociado a la uri
            resolver.
                    notifyChange(uri, null, false);
        }

        else if (ContractInsertPrecios.uriMatcher.match(uri)==ContractInsertPrecios.ALLROWS) {
            affected = db.delete(ContractInsertPrecios.INSERT_PRECIOS,
                    selection,
                    selectionArgs);
        }else if (ContractInsertPrecios.uriMatcher.match(uri)==ContractInsertPrecios.SINGLE_ROW) {
            long idStock = ContentUris.parseId(uri);
            affected = db.delete(ContractInsertPrecios.INSERT_PRECIOS,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
            // Notificar cambio asociado a la uri
            resolver.
                    notifyChange(uri, null, false);
        }

        else if (ContractInsertPacks.uriMatcher.match(uri)==ContractInsertPacks.ALLROWS) {
            affected = db.delete(ContractInsertPacks.INSERT_PACKS,
                    selection,
                    selectionArgs);
        }else if (ContractInsertPacks.uriMatcher.match(uri)==ContractInsertPacks.SINGLE_ROW) {
            long idStock = ContentUris.parseId(uri);
            affected = db.delete(ContractInsertPacks.INSERT_PACKS,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
            // Notificar cambio asociado a la uri
            resolver.
                    notifyChange(uri, null, false);
        }


        else if (ContractInsertPropensosYProdMalEst.uriMatcher.match(uri)==ContractInsertPropensosYProdMalEst.ALLROWS) {
            affected = db.delete(ContractInsertPropensosYProdMalEst.INSERT_PROP_MAL_EST,
                    selection,
                    selectionArgs);
        }else if (ContractInsertPropensosYProdMalEst.uriMatcher.match(uri)==ContractInsertPropensosYProdMalEst.SINGLE_ROW) {
            long idStock = ContentUris.parseId(uri);
            affected = db.delete(ContractInsertPropensosYProdMalEst.INSERT_PROP_MAL_EST,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
            // Notificar cambio asociado a la uri
            resolver.
                    notifyChange(uri, null, false);
        }


        else if (ContractInsertAlmuerzo.uriMatcher.match(uri)==ContractInsertAlmuerzo.ALLROWS) {
            affected = db.delete(ContractInsertAlmuerzo.INSERT_ALMUERZO,
                    selection,
                    selectionArgs);
        }else if (ContractInsertAlmuerzo.uriMatcher.match(uri)==ContractInsertAlmuerzo.SINGLE_ROW) {
            long idStock = ContentUris.parseId(uri);
            affected = db.delete(ContractInsertAlmuerzo.INSERT_ALMUERZO,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
            // Notificar cambio asociado a la uri
            resolver.
                    notifyChange(uri, null, false);
        }

        else if (ContractInsertProyectosContacto.uriMatcher.match(uri)==ContractInsertProyectosContacto.ALLROWS) {
            affected = db.delete(ContractInsertProyectosContacto.INSERT_PROYECTOS_CONTACTO,
                    selection,
                    selectionArgs);
        }else if (ContractInsertProyectosContacto.uriMatcher.match(uri)==ContractInsertProyectosContacto.SINGLE_ROW) {
            long idStock = ContentUris.parseId(uri);
            affected = db.delete(ContractInsertProyectosContacto.INSERT_PROYECTOS_CONTACTO,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
            // Notificar cambio asociado a la uri
            resolver.
                    notifyChange(uri, null, false);
        }

        else if (ContractInsertProforma.uriMatcher.match(uri)==ContractInsertProforma.ALLROWS) {
            affected = db.delete(ContractInsertProforma.INSERT_PROFORMA,
                    selection,
                    selectionArgs);
        }else if (ContractInsertProforma.uriMatcher.match(uri)==ContractInsertProforma.SINGLE_ROW) {
            long idProforma = ContentUris.parseId(uri);
            affected = db.delete(ContractInsertProforma.INSERT_PROFORMA,
                    Constantes.ID_REMOTA + "=" + idProforma
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
            resolver.
                    notifyChange(uri, null, false);
        }

        else if (ContractInsertPagoFactura.uriMatcher.match(uri)==ContractInsertPagoFactura.ALLROWS) {
            affected = db.delete(ContractInsertPagoFactura.INSERT_PAGO_FACTURA,
                    selection,
                    selectionArgs);
        }else if (ContractInsertPagoFactura.uriMatcher.match(uri)==ContractInsertPagoFactura.SINGLE_ROW) {
            long idPagoFactura = ContentUris.parseId(uri);
            affected = db.delete(ContractInsertPagoFactura.INSERT_PAGO_FACTURA,
                    Constantes.ID_REMOTA + "=" + idPagoFactura
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
            resolver.
                    notifyChange(uri, null, false);
        }



        else if (ContractInsertPdvFotografico.uriMatcher.match(uri)==ContractInsertPdvFotografico.ALLROWS) {
            affected = db.delete(ContractInsertPdvFotografico.INSERT_PVD_FOTOGRAFICO,
                    selection,
                    selectionArgs);
        }else if (ContractInsertPdvFotografico.uriMatcher.match(uri)==ContractInsertPdvFotografico.SINGLE_ROW) {
            long idStock = ContentUris.parseId(uri);
            affected = db.delete(ContractInsertPdvFotografico.INSERT_PVD_FOTOGRAFICO,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
            // Notificar cambio asociado a la uri
            resolver.
                    notifyChange(uri, null, false);
        }



        else if (ContractInsertVentas.uriMatcher.match(uri)==ContractInsertVentas.ALLROWS) {
            affected = db.delete(ContractInsertVentas.INSERT_VENTAS,
                    selection,
                    selectionArgs);
        }else if (ContractInsertVentas.uriMatcher.match(uri)==ContractInsertVentas.SINGLE_ROW) {
            long idStock = ContentUris.parseId(uri);
            affected = db.delete(ContractInsertVentas.INSERT_VENTAS,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
            // Notificar cambio asociado a la uri
            resolver.
                    notifyChange(uri, null, false);
        }


        else if (ContractInsertProdCaducar.uriMatcher.match(uri)==ContractInsertProdCaducar.ALLROWS) {
            affected = db.delete(ContractInsertProdCaducar.INSERT_PROD_CADUCAR,
                    selection,
                    selectionArgs);
        }else if (ContractInsertProdCaducar.uriMatcher.match(uri)==ContractInsertProdCaducar.SINGLE_ROW) {
            long idStock = ContentUris.parseId(uri);
            affected = db.delete(ContractInsertProdCaducar.INSERT_PROD_CADUCAR,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
            // Notificar cambio asociado a la uri
            resolver.
                    notifyChange(uri, null, false);
        }

        else if (ContractInsertSugeridos.uriMatcher.match(uri)==ContractInsertSugeridos.ALLROWS) {
            affected = db.delete(ContractInsertSugeridos.INSERT_SUGERIDOS,
                    selection,
                    selectionArgs);
        }else if (ContractInsertSugeridos.uriMatcher.match(uri)==ContractInsertSugeridos.SINGLE_ROW) {
            long idStock = ContentUris.parseId(uri);
            affected = db.delete(ContractInsertSugeridos.INSERT_SUGERIDOS,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
            // Notificar cambio asociado a la uri
            resolver.
                    notifyChange(uri, null, false);
        }

        else if (ContractInsertCanjes.uriMatcher.match(uri)==ContractInsertCanjes.ALLROWS) {
            affected = db.delete(ContractInsertCanjes.INSERT_CANJES,
                    selection,
                    selectionArgs);
        }else if (ContractInsertCanjes.uriMatcher.match(uri)==ContractInsertCanjes.SINGLE_ROW) {
            long idStock = ContentUris.parseId(uri);
            affected = db.delete(ContractInsertCanjes.INSERT_CANJES,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
            // Notificar cambio asociado a la uri
            resolver.
                    notifyChange(uri, null, false);
        }

        else if (ContractInsertMaterialesRecibidos.uriMatcher.match(uri)==ContractInsertMaterialesRecibidos.ALLROWS) {
            affected = db.delete(ContractInsertMaterialesRecibidos.INSERT_MATERIALES_RECIBIDOS,
                    selection,
                    selectionArgs);
        }else if (ContractInsertMaterialesRecibidos.uriMatcher.match(uri)==ContractInsertMaterialesRecibidos.SINGLE_ROW) {
            long idStock = ContentUris.parseId(uri);
            affected = db.delete(ContractInsertMaterialesRecibidos.INSERT_MATERIALES_RECIBIDOS,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
            // Notificar cambio asociado a la uri
            resolver.
                    notifyChange(uri, null, false);
        }

        else if (ContractInsertEjecucionMateriales.uriMatcher.match(uri)==ContractInsertEjecucionMateriales.ALLROWS) {
            affected = db.delete(ContractInsertEjecucionMateriales.INSERT_EJECUCION_MATERIALES,
                    selection,
                    selectionArgs);
        }else if (ContractInsertEjecucionMateriales.uriMatcher.match(uri)==ContractInsertEjecucionMateriales.SINGLE_ROW) {
            long idStock = ContentUris.parseId(uri);
            affected = db.delete(ContractInsertEjecucionMateriales.INSERT_EJECUCION_MATERIALES,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
            // Notificar cambio asociado a la uri
            resolver.
                    notifyChange(uri, null, false);
        }

        else if (ContractInsertMCIPdv.uriMatcher.match(uri)==ContractInsertMCIPdv.ALLROWS) {
            affected = db.delete(ContractInsertMCIPdv.INSERT_MCI,
                    selection,
                    selectionArgs);
        }else if (ContractInsertMCIPdv.uriMatcher.match(uri)==ContractInsertMCIPdv.SINGLE_ROW) {
            long idStock = ContentUris.parseId(uri);
            affected = db.delete(ContractInsertMCIPdv.INSERT_MCI,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
            // Notificar cambio asociado a la uri
            resolver.
                    notifyChange(uri, null, false);
        }

        else if (ContractInsertMallaCodificados.uriMatcher.match(uri)==ContractInsertMallaCodificados.ALLROWS) {
            affected = db.delete(ContractInsertMallaCodificados.INSERT_MALLA_CODIFICADOS,
                    selection,
                    selectionArgs);
        }else if (ContractInsertMallaCodificados.uriMatcher.match(uri)==ContractInsertMallaCodificados.SINGLE_ROW) {
            long idStock = ContentUris.parseId(uri);
            affected = db.delete(ContractInsertMallaCodificados.INSERT_MALLA_CODIFICADOS,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
            // Notificar cambio asociado a la uri
            resolver.
                    notifyChange(uri, null, false);
        }

        else if (ContractInsertTareas.uriMatcher.match(uri)==ContractInsertTareas.ALLROWS) {
            affected = db.delete(ContractInsertTareas.INSERT_TAREAS,
                    selection,
                    selectionArgs);
        }else if (ContractInsertTareas.uriMatcher.match(uri)==ContractInsertTareas.SINGLE_ROW) {
            long idStock = ContentUris.parseId(uri);
            affected = db.delete(ContractInsertTareas.INSERT_TAREAS,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
            // Notificar cambio asociado a la uri
            resolver.
                    notifyChange(uri, null, false);
        }

        else if (ContractInsertRotacion.uriMatcher.match(uri)==ContractInsertRotacion.ALLROWS) {
            affected = db.delete(ContractInsertRotacion.INSERT_ROTACION,
                    selection,
                    selectionArgs);
        }else if (ContractInsertRotacion.uriMatcher.match(uri)==ContractInsertRotacion.SINGLE_ROW) {
            long idStock = ContentUris.parseId(uri);
            affected = db.delete(ContractInsertRotacion.INSERT_ROTACION,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
            // Notificar cambio asociado a la uri
            resolver.
                    notifyChange(uri, null, false);
        }

        else if (ContractInsertPreguntas.uriMatcher.match(uri)==ContractInsertPreguntas.ALLROWS) {
            affected = db.delete(ContractInsertPreguntas.INSERT_PREGUNTAS,
                    selection,
                    selectionArgs);
        }else if (ContractInsertPreguntas.uriMatcher.match(uri)==ContractInsertPreguntas.SINGLE_ROW) {
            long idStock = ContentUris.parseId(uri);
            affected = db.delete(ContractInsertPreguntas.INSERT_PREGUNTAS,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
            // Notificar cambio asociado a la uri
            resolver.
                    notifyChange(uri, null, false);
        }

        else if (ContractInsertResultadoPreguntas.uriMatcher.match(uri)==ContractInsertResultadoPreguntas.ALLROWS) {
            affected = db.delete(ContractInsertResultadoPreguntas.INSERT_RESULTADO_PREGUNTAS,
                    selection,
                    selectionArgs);
        }else if (ContractInsertResultadoPreguntas.uriMatcher.match(uri)==ContractInsertResultadoPreguntas.SINGLE_ROW) {
            long idStock = ContentUris.parseId(uri);
            affected = db.delete(ContractInsertResultadoPreguntas.INSERT_RESULTADO_PREGUNTAS,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
            // Notificar cambio asociado a la uri
            resolver.
                    notifyChange(uri, null, false);
        }

        else if (ContractInsertPreciosSesion.uriMatcher.match(uri)==ContractInsertPreciosSesion.ALLROWS) {
            affected = db.delete(ContractInsertPreciosSesion.INSERT_PRECIOS_SESION,
                    selection,
                    selectionArgs);
        }else if (ContractInsertPreciosSesion.uriMatcher.match(uri)==ContractInsertPreciosSesion.SINGLE_ROW) {
            long idStock = ContentUris.parseId(uri);
            affected = db.delete(ContractInsertPreciosSesion.INSERT_PRECIOS_SESION,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
            // Notificar cambio asociado a la uri
            resolver.
                    notifyChange(uri, null, false);
        }

        else if (InsertFlooring.uriMatcher.match(uri)==InsertFlooring.ALLROWS) {
            affected = db.delete(InsertFlooring.INSERT_FLOORING,
                    selection,
                    selectionArgs);
        }else if (InsertFlooring.uriMatcher.match(uri)==InsertFlooring.SINGLE_ROW) {
            long idStock = ContentUris.parseId(uri);
            affected = db.delete(InsertFlooring.INSERT_FLOORING,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
            // Notificar cambio asociado a la uri
            resolver.
                    notifyChange(uri, null, false);
        }

        else if (ContractInsertImpulso.uriMatcher.match(uri)==ContractInsertImpulso.ALLROWS) {
            affected = db.delete(ContractInsertImpulso.INSERT_IMPUSLO,
                    selection,
                    selectionArgs);
        }else if (ContractInsertImpulso.uriMatcher.match(uri)==ContractInsertImpulso.SINGLE_ROW) {
            long idStock = ContentUris.parseId(uri);
            affected = db.delete(ContractInsertImpulso.INSERT_IMPUSLO,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
            // Notificar cambio asociado a la uri
            resolver.
                    notifyChange(uri, null, false);
        }

        else if (ContractInsertExh.uriMatcher.match(uri)==ContractInsertExh.ALLROWS) {
            affected = db.delete(ContractInsertExh.INSERT_EXH,
                    selection,
                    selectionArgs);
        }else if (ContractInsertExh.uriMatcher.match(uri)==ContractInsertExh.SINGLE_ROW) {
            long idStock = ContentUris.parseId(uri);
            affected = db.delete(ContractInsertExh.INSERT_EXH,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
            // Notificar cambio asociado a la uri
            resolver.
                    notifyChange(uri, null, false);
        }

        else if (ContractInsertExhSesion.uriMatcher.match(uri)==ContractInsertExhSesion.ALLROWS) {
            affected = db.delete(ContractInsertExhSesion.INSERT_EXH_SESION,
                    selection,
                    selectionArgs);
        }else if (ContractInsertExhSesion.uriMatcher.match(uri)==ContractInsertExhSesion.SINGLE_ROW) {
            long idStock = ContentUris.parseId(uri);
            affected = db.delete(ContractInsertExhSesion.INSERT_EXH_SESION,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
            // Notificar cambio asociado a la uri
            resolver.
                    notifyChange(uri, null, false);
        }

        else if (ContractInsertFotografico.uriMatcher.match(uri)==ContractInsertFotografico.ALLROWS) {
            affected = db.delete(ContractInsertFotografico.INSERT_FOTOGRAFICO,
                    selection,
                    selectionArgs);
        }else if (ContractInsertFotografico.uriMatcher.match(uri)==ContractInsertFotografico.SINGLE_ROW) {
            long idStock = ContentUris.parseId(uri);
            affected = db.delete(ContractInsertFotografico.INSERT_FOTOGRAFICO,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
            // Notificar cambio asociado a la uri
            resolver.
                    notifyChange(uri, null, false);
        }

        else if (ContractInsertAsistencia.uriMatcher.match(uri)==ContractInsertAsistencia.ALLROWS) {
            affected = db.delete(ContractInsertAsistencia.INSERT_ASISTENCIA,
                    selection,
                    selectionArgs);
        }else if (ContractInsertAsistencia.uriMatcher.match(uri)==ContractInsertAsistencia.SINGLE_ROW) {
            long idStock = ContentUris.parseId(uri);
            affected = db.delete(ContractInsertAsistencia.INSERT_ASISTENCIA,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
            // Notificar cambio asociado a la uri
            resolver.
                    notifyChange(uri, null, false);
        }

        else if (ContractAsistenciasLocal.uriMatcher.match(uri)==ContractAsistenciasLocal.ALLROWS) {
            affected = db.delete(ContractAsistenciasLocal.TABLE_NAME,
                    selection,
                    selectionArgs);
        }else if (ContractAsistenciasLocal.uriMatcher.match(uri)==ContractAsistenciasLocal.SINGLE_ROW) {
            long idStock = ContentUris.parseId(uri);
            affected = db.delete(ContractAsistenciasLocal.TABLE_NAME,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
            // Notificar cambio asociado a la uri
            resolver.
                    notifyChange(uri, null, false);
        }

        else if (ContractHoraMarcacion.uriMatcher.match(uri)==ContractHoraMarcacion.ALLROWS) {
            affected = db.delete(ContractHoraMarcacion.TABLE_NAME,
                    selection,
                    selectionArgs);
        }else if (ContractHoraMarcacion.uriMatcher.match(uri)==ContractHoraMarcacion.SINGLE_ROW) {
            long idStock = ContentUris.parseId(uri);
            affected = db.delete(ContractHoraMarcacion.TABLE_NAME,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
            // Notificar cambio asociado a la uri
            resolver.
                    notifyChange(uri, null, false);
        }

        else if (ContractInsertEvidencias.uriMatcher.match(uri)==ContractInsertEvidencias.ALLROWS) {
            affected = db.delete(ContractInsertEvidencias.INSERT_EVIDENCIAS,
                    selection,
                    selectionArgs);
        }else if (ContractInsertEvidencias.uriMatcher.match(uri)==ContractInsertEvidencias.SINGLE_ROW) {
            long idStock = ContentUris.parseId(uri);
            affected = db.delete(ContractInsertEvidencias.INSERT_EVIDENCIAS,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
            // Notificar cambio asociado a la uri
            resolver.
                    notifyChange(uri, null, false);
        }

        else if (ContractInsertGps.uriMatcher.match(uri)==ContractInsertGps.ALLROWS) {
            affected = db.delete(ContractInsertGps.INSERT_GPS,
                    selection,
                    selectionArgs);
        }else if (ContractInsertGps.uriMatcher.match(uri)==ContractInsertGps.SINGLE_ROW) {
            long idStock = ContentUris.parseId(uri);
            affected = db.delete(ContractInsertGps.INSERT_GPS,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
            // Notificar cambio asociado a la uri
            resolver.
                    notifyChange(uri, null, false);
        }

        else if (ContractInsertRastreo.uriMatcher.match(uri)==ContractInsertRastreo.ALLROWS) {
            affected = db.delete(ContractInsertRastreo.INSERT_GEO,
                    selection,
                    selectionArgs);
        }else if (ContractInsertRastreo.uriMatcher.match(uri)==ContractInsertRastreo.SINGLE_ROW) {
            long idStock = ContentUris.parseId(uri);
            affected = db.delete(ContractInsertRastreo.INSERT_GEO,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
            // Notificar cambio asociado a la uri
            resolver.
                    notifyChange(uri, null, false);
        }

        else if (ContractNotificacion.uriMatcher.match(uri)==ContractNotificacion.ALLROWS) {
            affected = db.delete(ContractNotificacion.NOTIFICACION,
                    selection,
                    selectionArgs);
        }else if (ContractNotificacion.uriMatcher.match(uri)==ContractNotificacion.SINGLE_ROW) {
            long idStock = ContentUris.parseId(uri);
            affected = db.delete(ContractNotificacion.NOTIFICACION,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
            // Notificar cambio asociado a la uri
            resolver.
                    notifyChange(uri, null, false);
        }

        else if (ContractInsertInicial.uriMatcher.match(uri)==ContractInsertInicial.ALLROWS) {
            affected = db.delete(ContractInsertInicial.INSERT_INICIAL,
                    selection,
                    selectionArgs);
        }else if (ContractInsertInicial.uriMatcher.match(uri)==ContractInsertInicial.SINGLE_ROW) {
            long idStock = ContentUris.parseId(uri);
            affected = db.delete(ContractInsertInicial.INSERT_INICIAL,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
            // Notificar cambio asociado a la uri
            resolver.
                    notifyChange(uri, null, false);
        }

        else if (ContractInsertPromocion.uriMatcher.match(uri)==ContractInsertPromocion.ALLROWS) {
            affected = db.delete(ContractInsertPromocion.INSERT_PROMO,
                    selection,
                    selectionArgs);
        }else if (ContractInsertPromocion.uriMatcher.match(uri)==ContractInsertPromocion.SINGLE_ROW) {
            long idStock = ContentUris.parseId(uri);
            affected = db.delete(ContractInsertPromocion.INSERT_PROMO,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
            // Notificar cambio asociado a la uri
            resolver.
                    notifyChange(uri, null, false);
        }


        else if (ContractInsertImplementacion.uriMatcher.match(uri)==ContractInsertImplementacion.ALLROWS) {
            affected = db.delete(ContractInsertImplementacion.INSERT_IMPLEM,
                    selection,
                    selectionArgs);
        }else if (ContractInsertImplementacion.uriMatcher.match(uri)==ContractInsertImplementacion.SINGLE_ROW) {
            long idStock = ContentUris.parseId(uri);
            affected = db.delete(ContractInsertImplementacion.INSERT_IMPLEM,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
            // Notificar cambio asociado a la uri
            resolver.
                    notifyChange(uri, null, false);
        }

        else if (ContractInsertValores.uriMatcher.match(uri)==ContractInsertValores.ALLROWS) {
            affected = db.delete(ContractInsertValores.INSERT_VALORES,
                    selection,
                    selectionArgs);
        }else if (ContractInsertValores.uriMatcher.match(uri)==ContractInsertValores.SINGLE_ROW) {
            long idStock = ContentUris.parseId(uri);
            affected = db.delete(ContractInsertValores.INSERT_VALORES,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
            // Notificar cambio asociado a la uri
            resolver.
                    notifyChange(uri, null, false);
        }

        else if (ContractInsertValoresSesion.uriMatcher.match(uri)==ContractInsertValoresSesion.ALLROWS) {
            affected = db.delete(ContractInsertValoresSesion.INSERT_VALORES_SESION,
                    selection,
                    selectionArgs);
        }else if (ContractInsertValoresSesion.uriMatcher.match(uri)==ContractInsertValoresSesion.SINGLE_ROW) {
            long idStock = ContentUris.parseId(uri);
            affected = db.delete(ContractInsertValoresSesion.INSERT_VALORES_SESION,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
            // Notificar cambio asociado a la uri
            resolver.
                    notifyChange(uri, null, false);
        }

        else if (ContractInsertPdv.uriMatcher.match(uri)==ContractInsertPdv.ALLROWS) {
            affected = db.delete(ContractInsertPdv.INSERT_PDV,
                    selection,
                    selectionArgs);
        }else if (ContractInsertPdv.uriMatcher.match(uri)==ContractInsertPdv.SINGLE_ROW) {
            long idStock = ContentUris.parseId(uri);
            affected = db.delete(ContractInsertPdv.INSERT_PDV,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
            // Notificar cambio asociado a la uri
            resolver.
                    notifyChange(uri, null, false);
        }

        else if (ContractInsertShare.uriMatcher.match(uri)==ContractInsertShare.ALLROWS) {
            affected = db.delete(ContractInsertShare.INSERT_SHARE,
                    selection,
                    selectionArgs);
        }else if (ContractInsertShare.uriMatcher.match(uri)==ContractInsertShare.SINGLE_ROW) {
            long idStock = ContentUris.parseId(uri);
            affected = db.delete(ContractInsertShare.INSERT_SHARE,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
            // Notificar cambio asociado a la uri
            resolver.
                    notifyChange(uri, null, false);
        }

        else if (ContractInsertPDI.uriMatcher.match(uri)==ContractInsertPDI.ALLROWS) {
            affected = db.delete(ContractInsertPDI.INSERT_PDI,
                    selection,
                    selectionArgs);
        }else if (ContractInsertPDI.uriMatcher.match(uri)==ContractInsertPDI.SINGLE_ROW) {
            long idStock = ContentUris.parseId(uri);
            affected = db.delete(ContractInsertPDI.INSERT_PDI,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
            // Notificar cambio asociado a la uri
            resolver.
                    notifyChange(uri, null, false);
        }

        else if (ContractInsertAgotados.uriMatcher.match(uri)==ContractInsertAgotados.ALLROWS) {
            affected = db.delete(ContractInsertAgotados.INSERT_AGOTADOS,
                    selection,
                    selectionArgs);
        }else if (ContractInsertAgotados.uriMatcher.match(uri)==ContractInsertAgotados.SINGLE_ROW) {
            long idStock = ContentUris.parseId(uri);
            affected = db.delete(ContractInsertAgotados.INSERT_AGOTADOS,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
            // Notificar cambio asociado a la uri
            resolver.
                    notifyChange(uri, null, false);
        }

        else if (ContractInsertVenta.uriMatcher.match(uri)==ContractInsertVenta.ALLROWS) {
            affected = db.delete(ContractInsertVenta.INSERT_VENTA,
                    selection,
                    selectionArgs);
        }else if (ContractInsertVenta.uriMatcher.match(uri)==ContractInsertVenta.SINGLE_ROW) {
            long idStock = ContentUris.parseId(uri);
            affected = db.delete(ContractInsertVenta.INSERT_VENTA,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
            // Notificar cambio asociado a la uri
            resolver.
                    notifyChange(uri, null, false);
        }

        else if (ContractInsertLogisticoRelevo.uriMatcher.match(uri)==ContractInsertLogisticoRelevo.ALLROWS) {
            affected = db.delete(ContractInsertLogisticoRelevo.INSERT_LOGISTICO_RELEVO,
                    selection,
                    selectionArgs);
        }else if (ContractInsertLogisticoRelevo.uriMatcher.match(uri)==ContractInsertLogisticoRelevo.SINGLE_ROW) {
            long idStock = ContentUris.parseId(uri);
            affected = db.delete(ContractInsertLogisticoRelevo.INSERT_LOGISTICO_RELEVO,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
            // Notificar cambio asociado a la uri
            resolver.
                    notifyChange(uri, null, false);
        }

        else if (ContractInsertLogisticoRelevo.uriMatcher.match(uri)==ContractInsertLogisticoRelevo.ALLROWS) {
            affected = db.delete(ContractInsertLogisticoRelevo.INSERT_LOGISTICO_RELEVO,
                    selection,
                    selectionArgs);
        }else if (ContractInsertLogisticoRelevo.uriMatcher.match(uri)==ContractInsertLogisticoRelevo.SINGLE_ROW) {
            long idStock = ContentUris.parseId(uri);
            affected = db.delete(ContractInsertLogisticoRelevo.INSERT_LOGISTICO_RELEVO,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
            // Notificar cambio asociado a la uri
            resolver.
                    notifyChange(uri, null, false);
        }

        else if (ContractCausalesAsistenciaAtraso.uriMatcher.match(uri) == ContractCausalesAsistenciaAtraso.ALLROWS) {
            affected = db.delete(ContractCausalesAsistenciaAtraso.CAUSALES_ASISTENCIA_ATRASO,
                    selection, selectionArgs);
        } else if (ContractCausalesAsistenciaAtraso.uriMatcher.match(uri) == ContractCausalesAsistenciaAtraso.SINGLE_ROW) {
            long idStock = ContentUris.parseId(uri);
            affected = db.delete(ContractCausalesAsistenciaAtraso.CAUSALES_ASISTENCIA_ATRASO,
                    ContractCausalesAsistenciaAtraso.Columnas.ID + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
            resolver.notifyChange(uri, null, false);
        }

        else if (ContractCausalesAsistencia.uriMatcher.match(uri) == ContractCausalesAsistencia.ALLROWS) {
            affected = db.delete(ContractCausalesAsistencia.CAUSALES_ASISTENCIA,
                    selection,
                    selectionArgs);
        } else if (ContractCausalesAsistencia.uriMatcher.match(uri) == ContractCausalesAsistencia.SINGLE_ROW) {
            long idStock = ContentUris.parseId(uri);
            affected = db.delete(ContractCausalesAsistencia.CAUSALES_ASISTENCIA,
                    ContractCausalesAsistencia.Columnas.ID + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
            // Notificar cambio asociado a la uri
            resolver.
                    notifyChange(uri, null, false);
        }


        else{
            throw new IllegalArgumentException("Elemento local desconocido: " + uri);
        }

        return affected;
    }

    //Aqui es importante el ELSE
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        int affected;

        if (ContractPharmaValue.uriMatcher.match(uri)== ContractPharmaValue.ALLROWS) {
            System.out.println("UPDATE");
            affected = db.update(ContractPharmaValue.POS, values,
                    selection, selectionArgs);
        }else if (ContractPharmaValue.uriMatcher.match(uri)== ContractPharmaValue.SINGLE_ROW) {
            String idStock = uri.getPathSegments().get(1);
            affected = db.update(ContractPharmaValue.POS, values,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
        }

        else
        if (ContractPrecios.uriMatcher.match(uri)==ContractPrecios.ALLROWS) {
            System.out.println("UPDATE");
            affected = db.update(ContractPrecios.PRECIOS, values,
                    selection, selectionArgs);
        }else if (ContractPrecios.uriMatcher.match(uri)==ContractPrecios.SINGLE_ROW) {
            String idStock = uri.getPathSegments().get(1);
            affected = db.update(ContractPrecios.PRECIOS, values,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
        }


        else
        if (ContractPreciosPvc.uriMatcher.match(uri)==ContractPreciosPvc.ALLROWS) {
            System.out.println("UPDATE");
            affected = db.update(ContractPreciosPvc.PRECIOS_PVC, values,
                    selection, selectionArgs);
        }else if (ContractPreciosPvc.uriMatcher.match(uri)==ContractPreciosPvc.SINGLE_ROW) {
            String idStock = uri.getPathSegments().get(1);
            affected = db.update(ContractPreciosPvc.PRECIOS_PVC, values,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
        }

        else
        if (ContractPreguntas.uriMatcher.match(uri)==ContractPreguntas.ALLROWS) {
            System.out.println("UPDATE");
            affected = db.update(ContractPreguntas.TABLE_QUEST, values,
                    selection, selectionArgs);
        }else if (ContractPreguntas.uriMatcher.match(uri)==ContractPreguntas.SINGLE_ROW) {
            String idStock = uri.getPathSegments().get(1);
            affected = db.update(ContractPreguntas.TABLE_QUEST, values,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
        }

        else
        if(ContractJustificacion.uriMatcher.match(uri)==ContractJustificacion.ALLROWS){
            System.out.println("UPDATE");
            affected = db.update(ContractJustificacion.JUSTIFICACIONES, values,
                    selection, selectionArgs);
        }else if(ContractJustificacion.uriMatcher.match(uri)==ContractJustificacion.SINGLE_ROW){
            String idStock = uri.getPathSegments().get(1);
            affected = db.update(ContractJustificacion.JUSTIFICACIONES, values,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
        }

        else
        if(ContractTipoPrecios.uriMatcher.match(uri)==ContractTipoPrecios.ALLROWS){
            System.out.println("UPDATE");
            affected = db.update(ContractTipoPrecios.TIPO_PRECIOS, values,
                    selection, selectionArgs);
        }else if(ContractTipoPrecios.uriMatcher.match(uri)==ContractTipoPrecios.SINGLE_ROW){
            String idStock = uri.getPathSegments().get(1);
            affected = db.update(ContractTipoPrecios.TIPO_PRECIOS, values,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
        }

        else
        if(ContractMarcasBlancas.uriMatcher.match(uri)==ContractMarcasBlancas.ALLROWS){
            System.out.println("UPDATE");
            affected = db.update(ContractMarcasBlancas.MARCAS_BLANCAS, values,
                    selection, selectionArgs);
        }else if(ContractMarcasBlancas.uriMatcher.match(uri)==ContractMarcasBlancas.SINGLE_ROW){
            String idStock = uri.getPathSegments().get(1);
            affected = db.update(ContractMarcasBlancas.MARCAS_BLANCAS, values,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
        }



        else
        if (ContractTests.uriMatcher.match(uri)==ContractTests.ALLROWS) {
            System.out.println("UPDATE");
            affected = db.update(ContractTests.TABLE_TEST, values,
                    selection, selectionArgs);
        }else if (ContractTests.uriMatcher.match(uri)==ContractTests.SINGLE_ROW) {
            String idStock = uri.getPathSegments().get(1);
            affected = db.update(ContractTests.TABLE_TEST, values,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
        }


        else
        if (ContractTipoExh.uriMatcher.match(uri)==ContractTipoExh.ALLROWS) {
            System.out.println("UPDATE");
            affected = db.update(ContractTipoExh.TABLE_NAME, values,
                    selection, selectionArgs);
        }else if (ContractTipoExh.uriMatcher.match(uri)==ContractTipoExh.SINGLE_ROW) {
            String idStock = uri.getPathSegments().get(1);
            affected = db.update(ContractTipoExh.TABLE_NAME, values,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
        }




        else
        if (ContractPromociones.uriMatcher.match(uri)== ContractPromociones.ALLROWS) {
            System.out.println("UPDATE");
            affected = db.update(ContractPromociones.PROMOCIONES, values,
                    selection, selectionArgs);
        }else if (ContractPromociones.uriMatcher.match(uri)== ContractPromociones.SINGLE_ROW) {
            String idStock = uri.getPathSegments().get(1);
            affected = db.update(ContractPromociones.PROMOCIONES, values,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
        }

        else
        if (ContractRotacion.uriMatcher.match(uri)== ContractRotacion.ALLROWS) {
            System.out.println("UPDATE");
            affected = db.update(ContractRotacion.ROTACION, values,
                    selection, selectionArgs);
        }else if (ContractRotacion.uriMatcher.match(uri)== ContractRotacion.SINGLE_ROW) {
            String idStock = uri.getPathSegments().get(1);
            affected = db.update(ContractRotacion.ROTACION, values,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
        }

        else
        if (ContractPopSugerido.uriMatcher.match(uri)== ContractPopSugerido.ALLROWS) {
            System.out.println("UPDATE");
            affected = db.update(ContractPopSugerido.POPSUGERIDO, values,
                    selection, selectionArgs);
        }else if (ContractPopSugerido.uriMatcher.match(uri)== ContractPopSugerido.SINGLE_ROW) {
            String idStock = uri.getPathSegments().get(1);
            affected = db.update(ContractPopSugerido.POPSUGERIDO, values,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
        }

        //Causales Atraso
        else if (ContractCausalesAsistencia.uriMatcher.match(uri) == ContractCausalesAsistencia.ALLROWS) {
            System.out.println("UPDATE");
            affected = db.update(ContractCausalesAsistencia.CAUSALES_ASISTENCIA, values,
                    selection, selectionArgs);
        } else if (ContractCausalesAsistencia.uriMatcher.match(uri) == ContractCausalesAsistencia.SINGLE_ROW) {
            String idStock = uri.getPathSegments().get(1);
            affected = db.update(ContractCausalesAsistencia.CAUSALES_ASISTENCIA, values,
                    ContractCausalesAsistencia.Columnas.ID + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
        }
        //Causales Atraso
        else if (ContractCausalesAsistenciaAtraso.uriMatcher.match(uri) == ContractCausalesAsistenciaAtraso.ALLROWS) {
            System.out.println("UPDATE");
            affected = db.update(ContractCausalesAsistenciaAtraso.CAUSALES_ASISTENCIA_ATRASO, values,
                    selection, selectionArgs);
        } else if (ContractCausalesAsistenciaAtraso.uriMatcher.match(uri) == ContractCausalesAsistenciaAtraso.SINGLE_ROW) {
            String idStock = uri.getPathSegments().get(1);
            affected = db.update(ContractCausalesAsistenciaAtraso.CAUSALES_ASISTENCIA_ATRASO, values,
                    ContractCausalesAsistenciaAtraso.Columnas.ID + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
        }

        else if (ContractCausalesProdMalEst.uriMatcher.match(uri)== ContractCausalesProdMalEst.ALLROWS) {
            System.out.println("UPDATE");
            affected = db.update(ContractCausalesProdMalEst.CAUSALES_PROD_MAL_EST, values,
                    selection, selectionArgs);
        } else if (ContractCausalesProdMalEst.uriMatcher.match(uri)== ContractCausalesProdMalEst.SINGLE_ROW) {
            String idStock = uri.getPathSegments().get(1);
            affected = db.update(ContractCausalesProdMalEst.CAUSALES_PROD_MAL_EST, values,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
        }


        else
        if (ContractVersiones.uriMatcher.match(uri)== ContractVersiones.ALLROWS) {
            System.out.println("UPDATE");
            affected = db.update(ContractVersiones.VERSIONES, values,
                    selection, selectionArgs);
        }else if (ContractVersiones.uriMatcher.match(uri)== ContractVersiones.SINGLE_ROW) {
            String idStock = uri.getPathSegments().get(1);
            affected = db.update(ContractVersiones.VERSIONES, values,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
        }



        else
        if (ContractComboCanjes.uriMatcher.match(uri)== ContractComboCanjes.ALLROWS) {
            System.out.println("UPDATE");
            affected = db.update(ContractComboCanjes.COMBO_CANJES, values,
                    selection, selectionArgs);
        }else if (ContractComboCanjes.uriMatcher.match(uri)== ContractComboCanjes.SINGLE_ROW) {
            String idStock = uri.getPathSegments().get(1);
            affected = db.update(ContractComboCanjes.COMBO_CANJES, values,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
        }

        else
        if (ContractCausalesMCI.uriMatcher.match(uri)== ContractCausalesMCI.ALLROWS) {
            System.out.println("UPDATE");
            affected = db.update(ContractCausalesMCI.CAUSALES_MCI, values,
                    selection, selectionArgs);
        }else if (ContractCausalesMCI.uriMatcher.match(uri)== ContractCausalesMCI.SINGLE_ROW) {
            String idStock = uri.getPathSegments().get(1);
            affected = db.update(ContractCausalesMCI.CAUSALES_MCI, values,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
        }

        else if (ContractRangosPrecios.uriMatcher.match(uri) == ContractRangosPrecios.ALLROWS) {
            affected = db.update(ContractRangosPrecios.RANGOS_PRECIOS, values, selection, selectionArgs);
        } else if (ContractRangosPrecios.uriMatcher.match(uri) == ContractRangosPrecios.SINGLE_ROW) {
            long id = ContentUris.parseId(uri);
            affected = db.update(ContractRangosPrecios.RANGOS_PRECIOS, values,
                    ContractRangosPrecios.Columnas._ID + "=" + id
                            + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""),
                    selectionArgs);
        } else if (ContractRangosPrecios.uriMatcher.match(uri) == ContractRangosPrecios.BY_PRESENTACION) {
            String presentacion = uri.getLastPathSegment();
            affected = db.update(ContractRangosPrecios.RANGOS_PRECIOS, values,
                    ContractRangosPrecios.Columnas.PRESENTACION + "='" + presentacion + "'"
                            + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""),
                    selectionArgs);
        }

        else if (ContractRangosPreciosSku.uriMatcher.match(uri) == ContractRangosPreciosSku.ALLROWS) {
            affected = db.update(ContractRangosPreciosSku.RANGOS_PRECIOS_SKU, values, selection, selectionArgs);
        } else if (ContractRangosPreciosSku.uriMatcher.match(uri) == ContractRangosPreciosSku.SINGLE_ROW) {
            long id = ContentUris.parseId(uri);
            affected = db.update(ContractRangosPreciosSku.RANGOS_PRECIOS_SKU, values,
                    ContractRangosPreciosSku.Columnas._ID + "=" + id
                            + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""),
                    selectionArgs);
        } else if (ContractRangosPreciosSku.uriMatcher.match(uri) == ContractRangosPreciosSku.BY_SKU) {
            String sku = uri.getLastPathSegment();
            affected = db.update(ContractRangosPreciosSku.RANGOS_PRECIOS_SKU, values,
                    ContractRangosPreciosSku.Columnas.SKU + "='" + sku + "'"
                            + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""),
                    selectionArgs);
        }

        else
        if (ContractCausalesOSA.uriMatcher.match(uri)== ContractCausalesOSA.ALLROWS) {
            System.out.println("UPDATE");
            affected = db.update(ContractCausalesOSA.CAUSALES_OSA, values,
                    selection, selectionArgs);
        }else if (ContractCausalesOSA.uriMatcher.match(uri)== ContractCausalesOSA.SINGLE_ROW) {
            String idStock = uri.getPathSegments().get(1);
            affected = db.update(ContractCausalesOSA.CAUSALES_OSA, values,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
        }

        else
        if (ContractAlertas.uriMatcher.match(uri)== ContractAlertas.ALLROWS) {
            System.out.println("UPDATE");
            affected = db.update(ContractAlertas.ALERTAS, values,
                    selection, selectionArgs);
        }else if (ContractAlertas.uriMatcher.match(uri)== ContractAlertas.SINGLE_ROW) {
            String idStock = uri.getPathSegments().get(1);
            affected = db.update(ContractAlertas.ALERTAS, values,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
        }

        else
        if (ContractTiempoAlmuerzo.uriMatcher.match(uri)== ContractTiempoAlmuerzo.ALLROWS) {
            System.out.println("UPDATE");
            affected = db.update(ContractTiempoAlmuerzo.REPO_TIEMPO_ALMUERZO, values,
                    selection, selectionArgs);
        }else if (ContractTiempoAlmuerzo.uriMatcher.match(uri)== ContractTiempoAlmuerzo.SINGLE_ROW) {
            String idStock = uri.getPathSegments().get(1);
            affected = db.update(ContractTiempoAlmuerzo.REPO_TIEMPO_ALMUERZO, values,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
        }

        else
        if (ContractPDI.uriMatcher.match(uri)== ContractPDI.ALLROWS) {
            System.out.println("UPDATE");
            affected = db.update(ContractPDI.PDI, values,
                    selection, selectionArgs);
        }else if (ContractPDI.uriMatcher.match(uri)== ContractPDI.SINGLE_ROW) {
            String idStock = uri.getPathSegments().get(1);
            affected = db.update(ContractPDI.PDI, values,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
        }

        else
        if (ContractLog.uriMatcher.match(uri)== ContractLog.ALLROWS) {
            System.out.println("UPDATE");
            affected = db.update(ContractLog.LOG, values,
                    selection, selectionArgs);
        }else if (ContractLog.uriMatcher.match(uri)== ContractLog.SINGLE_ROW) {
            String idStock = uri.getPathSegments().get(1);
            affected = db.update(ContractLog.LOG, values,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
        }

        else
        if (ContractPrioritario.uriMatcher.match(uri)== ContractPrioritario.ALLROWS) {
            System.out.println("UPDATE");
            affected = db.update(ContractPrioritario.PRIORITARIO, values,
                    selection, selectionArgs);
        }else if (ContractPrioritario.uriMatcher.match(uri)== ContractPrioritario.SINGLE_ROW) {
            String idStock = uri.getPathSegments().get(1);
            affected = db.update(ContractPrioritario.PRIORITARIO, values,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
        }

        else
        if (ContractTareas.uriMatcher.match(uri)== ContractTareas.ALLROWS) {
            System.out.println("UPDATE");
            affected = db.update(ContractTareas.TAREA, values,
                    selection, selectionArgs);
        }else if (ContractTareas.uriMatcher.match(uri)== ContractTareas.SINGLE_ROW) {
            String idStock = uri.getPathSegments().get(1);
            affected = db.update(ContractTareas.TAREA, values,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
        }


        else
        if (ContractPortafolioProductos.uriMatcher.match(uri)== ContractPortafolioProductos.ALLROWS) {
            System.out.println("UPDATE");
            affected = db.update(ContractPortafolioProductos.PORTAFOLIOPRODUCTOS, values,
                    selection, selectionArgs);
        }else if (ContractPortafolioProductos.uriMatcher.match(uri)== ContractPortafolioProductos.SINGLE_ROW) {
            String idStock = uri.getPathSegments().get(1);
            affected = db.update(ContractPortafolioProductos.PORTAFOLIOPRODUCTOS, values,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
        }

        else
        if (ContractPortafolioProductosAASS.uriMatcher.match(uri)== ContractPortafolioProductosAASS.ALLROWS) {
            System.out.println("UPDATE");
            affected = db.update(ContractPortafolioProductosAASS.PORTAFOLIOPRODUCTOS_AASS, values,
                    selection, selectionArgs);
        }else if (ContractPortafolioProductosAASS.uriMatcher.match(uri)== ContractPortafolioProductosAASS.SINGLE_ROW) {
            String idStock = uri.getPathSegments().get(1);
            affected = db.update(ContractPortafolioProductosAASS.PORTAFOLIOPRODUCTOS_AASS, values,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
        }

        else
        if (ContractPortafolioProductosMAYO.uriMatcher.match(uri)== ContractPortafolioProductosMAYO.ALLROWS) {
            System.out.println("UPDATE");
            affected = db.update(ContractPortafolioProductosMAYO.PORTAFOLIOPRODUCTOS_MAYO, values,
                    selection, selectionArgs);
        }else if (ContractPortafolioProductosMAYO.uriMatcher.match(uri)== ContractPortafolioProductosMAYO.SINGLE_ROW) {
            String idStock = uri.getPathSegments().get(1);
            affected = db.update(ContractPortafolioProductosMAYO.PORTAFOLIOPRODUCTOS_MAYO, values,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
        }

        else
        if (ContractInsertPrecios.uriMatcher.match(uri)==ContractInsertPrecios.ALLROWS) {
            System.out.println("UPDATE");
            affected = db.update(ContractInsertPrecios.INSERT_PRECIOS, values,
                    selection, selectionArgs);
        }else if (ContractInsertPrecios.uriMatcher.match(uri)==ContractInsertPrecios.SINGLE_ROW) {
            String idStock = uri.getPathSegments().get(1);
            affected = db.update(ContractInsertPrecios.INSERT_PRECIOS, values,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
        }

        else
        if (ContractInsertPacks.uriMatcher.match(uri)==ContractInsertPacks.ALLROWS) {
            System.out.println("UPDATE");
            affected = db.update(ContractInsertPacks.INSERT_PACKS, values,
                    selection, selectionArgs);
        }else if (ContractInsertPacks.uriMatcher.match(uri)==ContractInsertPacks.SINGLE_ROW) {
            String idStock = uri.getPathSegments().get(1);
            affected = db.update(ContractInsertPacks.INSERT_PACKS, values,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
        }


        else
        if (ContractInsertPropensosYProdMalEst.uriMatcher.match(uri)==ContractInsertPropensosYProdMalEst.ALLROWS) {
            System.out.println("UPDATE");
            affected = db.update(ContractInsertPropensosYProdMalEst.INSERT_PROP_MAL_EST, values,
                    selection, selectionArgs);
        }else if (ContractInsertPropensosYProdMalEst.uriMatcher.match(uri)==ContractInsertPropensosYProdMalEst.SINGLE_ROW) {
            String idStock = uri.getPathSegments().get(1);
            affected = db.update(ContractInsertPropensosYProdMalEst.INSERT_PROP_MAL_EST, values,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
        }



        else
        if (ContractInsertAlmuerzo.uriMatcher.match(uri)==ContractInsertAlmuerzo.ALLROWS) {
            System.out.println("UPDATE");
            affected = db.update(ContractInsertAlmuerzo.INSERT_ALMUERZO, values,
                    selection, selectionArgs);
        }else if (ContractInsertAlmuerzo.uriMatcher.match(uri)==ContractInsertAlmuerzo.SINGLE_ROW) {
            String idStock = uri.getPathSegments().get(1);
            affected = db.update(ContractInsertAlmuerzo.INSERT_ALMUERZO, values,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
        }

        else
        if (ContractInsertProyectosContacto.uriMatcher.match(uri)==ContractInsertProyectosContacto.ALLROWS) {
            affected = db.update(ContractInsertProyectosContacto.INSERT_PROYECTOS_CONTACTO, values,
                    selection, selectionArgs);
        }else if (ContractInsertProyectosContacto.uriMatcher.match(uri)==ContractInsertProyectosContacto.SINGLE_ROW) {
            String idStock = uri.getPathSegments().get(1);
            affected = db.update(ContractInsertProyectosContacto.INSERT_PROYECTOS_CONTACTO, values,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
        }

        else if (ContractInsertProforma.uriMatcher.match(uri)==ContractInsertProforma.ALLROWS) {
            affected = db.update(ContractInsertProforma.INSERT_PROFORMA, values,
                    selection, selectionArgs);
        }else if (ContractInsertProforma.uriMatcher.match(uri)==ContractInsertProforma.SINGLE_ROW) {
            String idProforma = uri.getPathSegments().get(1);
            affected = db.update(ContractInsertProforma.INSERT_PROFORMA, values,
                    Constantes.ID_REMOTA + "=" + idProforma
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
        }

        else if (ContractInsertPagoFactura.uriMatcher.match(uri)==ContractInsertPagoFactura.ALLROWS) {
            affected = db.update(ContractInsertPagoFactura.INSERT_PAGO_FACTURA, values,
                    selection, selectionArgs);
        }else if (ContractInsertPagoFactura.uriMatcher.match(uri)==ContractInsertPagoFactura.SINGLE_ROW) {
            String idPagoFactura = uri.getPathSegments().get(1);
            affected = db.update(ContractInsertPagoFactura.INSERT_PAGO_FACTURA, values,
                    Constantes.ID_REMOTA + "=" + idPagoFactura
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
        }



        else
        if (ContractInsertPdvFotografico.uriMatcher.match(uri)==ContractInsertPdvFotografico.ALLROWS) {
            System.out.println("UPDATE");
            affected = db.update(ContractInsertPdvFotografico.INSERT_PVD_FOTOGRAFICO, values,
                    selection, selectionArgs);
        }else if (ContractInsertPdvFotografico.uriMatcher.match(uri)==ContractInsertPdvFotografico.SINGLE_ROW) {
            String idStock = uri.getPathSegments().get(1);
            affected = db.update(ContractInsertPdvFotografico.INSERT_PVD_FOTOGRAFICO, values,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
        }




        else
        if (ContractInsertVentas.uriMatcher.match(uri)==ContractInsertVentas.ALLROWS) {
            System.out.println("UPDATE");
            affected = db.update(ContractInsertVentas.INSERT_VENTAS, values,
                    selection, selectionArgs);
        }else if (ContractInsertVentas.uriMatcher.match(uri)==ContractInsertVentas.SINGLE_ROW) {
            String idStock = uri.getPathSegments().get(1);
            affected = db.update(ContractInsertVentas.INSERT_VENTAS, values,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
        }





        else
        if (ContractInsertProdCaducar.uriMatcher.match(uri)==ContractInsertProdCaducar.ALLROWS) {
            System.out.println("UPDATE");
            affected = db.update(ContractInsertProdCaducar.INSERT_PROD_CADUCAR, values,
                    selection, selectionArgs);
        }else if (ContractInsertProdCaducar.uriMatcher.match(uri)==ContractInsertProdCaducar.SINGLE_ROW) {
            String idStock = uri.getPathSegments().get(1);
            affected = db.update(ContractInsertProdCaducar.INSERT_PROD_CADUCAR, values,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
        }

        else
        if (ContractInsertSugeridos.uriMatcher.match(uri)==ContractInsertSugeridos.ALLROWS) {
            System.out.println("UPDATE");
            affected = db.update(ContractInsertSugeridos.INSERT_SUGERIDOS, values,
                    selection, selectionArgs);
        }else if (ContractInsertSugeridos.uriMatcher.match(uri)==ContractInsertSugeridos.SINGLE_ROW) {
            String idStock = uri.getPathSegments().get(1);
            affected = db.update(ContractInsertSugeridos.INSERT_SUGERIDOS, values,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
        }

        else
        if (ContractInsertCanjes.uriMatcher.match(uri)==ContractInsertCanjes.ALLROWS) {
            System.out.println("UPDATE");
            affected = db.update(ContractInsertCanjes.INSERT_CANJES, values,
                    selection, selectionArgs);
        }else if (ContractInsertCanjes.uriMatcher.match(uri)==ContractInsertCanjes.SINGLE_ROW) {
            String idStock = uri.getPathSegments().get(1);
            affected = db.update(ContractInsertCanjes.INSERT_CANJES, values,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
        }

        else
        if (ContractInsertMaterialesRecibidos.uriMatcher.match(uri)==ContractInsertMaterialesRecibidos.ALLROWS) {
            System.out.println("UPDATE");
            affected = db.update(ContractInsertMaterialesRecibidos.INSERT_MATERIALES_RECIBIDOS, values,
                    selection, selectionArgs);
        }else if (ContractInsertMaterialesRecibidos.uriMatcher.match(uri)==ContractInsertMaterialesRecibidos.SINGLE_ROW) {
            String idStock = uri.getPathSegments().get(1);
            affected = db.update(ContractInsertMaterialesRecibidos.INSERT_MATERIALES_RECIBIDOS, values,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
        }

        else
        if (ContractInsertEjecucionMateriales.uriMatcher.match(uri)==ContractInsertEjecucionMateriales.ALLROWS) {
            System.out.println("UPDATE");
            affected = db.update(ContractInsertEjecucionMateriales.INSERT_EJECUCION_MATERIALES, values,
                    selection, selectionArgs);
        }else if (ContractInsertEjecucionMateriales.uriMatcher.match(uri)==ContractInsertEjecucionMateriales.SINGLE_ROW) {
            String idStock = uri.getPathSegments().get(1);
            affected = db.update(ContractInsertEjecucionMateriales.INSERT_EJECUCION_MATERIALES, values,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
        }

        else
        if (ContractInsertMCIPdv.uriMatcher.match(uri)==ContractInsertMCIPdv.ALLROWS) {
            System.out.println("UPDATE");
            affected = db.update(ContractInsertMCIPdv.INSERT_MCI, values,
                    selection, selectionArgs);
        }else if (ContractInsertMCIPdv.uriMatcher.match(uri)==ContractInsertMCIPdv.SINGLE_ROW) {
            String idStock = uri.getPathSegments().get(1);
            affected = db.update(ContractInsertMCIPdv.INSERT_MCI, values,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
        }

        else
        if (ContractInsertMallaCodificados.uriMatcher.match(uri)==ContractInsertMallaCodificados.ALLROWS) {
            System.out.println("UPDATE");
            affected = db.update(ContractInsertMallaCodificados.INSERT_MALLA_CODIFICADOS, values,
                    selection, selectionArgs);
        }else if (ContractInsertMallaCodificados.uriMatcher.match(uri)==ContractInsertMallaCodificados.SINGLE_ROW) {
            String idStock = uri.getPathSegments().get(1);
            affected = db.update(ContractInsertMallaCodificados.INSERT_MALLA_CODIFICADOS, values,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
        }

        else
        if (ContractInsertTareas.uriMatcher.match(uri)==ContractInsertTareas.ALLROWS) {
            System.out.println("UPDATE");
            affected = db.update(ContractInsertTareas.INSERT_TAREAS, values,
                    selection, selectionArgs);
        }else if (ContractInsertTareas.uriMatcher.match(uri)==ContractInsertTareas.SINGLE_ROW) {
            String idStock = uri.getPathSegments().get(1);
            affected = db.update(ContractInsertTareas.INSERT_TAREAS, values,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
        }

        else
        if (ContractInsertRotacion.uriMatcher.match(uri)==ContractInsertRotacion.ALLROWS) {
            System.out.println("UPDATE");
            affected = db.update(ContractInsertRotacion.INSERT_ROTACION, values,
                    selection, selectionArgs);
        }else if (ContractInsertRotacion.uriMatcher.match(uri)==ContractInsertRotacion.SINGLE_ROW) {
            String idStock = uri.getPathSegments().get(1);
            affected = db.update(ContractInsertRotacion.INSERT_ROTACION, values,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
        }

        else
        if (ContractInsertPreguntas.uriMatcher.match(uri)==ContractInsertPreguntas.ALLROWS) {
            System.out.println("UPDATE");
            affected = db.update(ContractInsertPreguntas.INSERT_PREGUNTAS, values,
                    selection, selectionArgs);
        }else if (ContractInsertPreguntas.uriMatcher.match(uri)==ContractInsertPreguntas.SINGLE_ROW) {
            String idStock = uri.getPathSegments().get(1);
            affected = db.update(ContractInsertPreguntas.INSERT_PREGUNTAS, values,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
        }


        else
        if (ContractInsertResultadoPreguntas.uriMatcher.match(uri)==ContractInsertResultadoPreguntas.ALLROWS) {
            System.out.println("UPDATE");
            affected = db.update(ContractInsertResultadoPreguntas.INSERT_RESULTADO_PREGUNTAS, values,
                    selection, selectionArgs);
        }else if (ContractInsertResultadoPreguntas.uriMatcher.match(uri)==ContractInsertResultadoPreguntas.SINGLE_ROW) {
            String idStock = uri.getPathSegments().get(1);
            affected = db.update(ContractInsertResultadoPreguntas.INSERT_RESULTADO_PREGUNTAS, values,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
        }



        else
        if (ContractInsertPreciosSesion.uriMatcher.match(uri)==ContractInsertPreciosSesion.ALLROWS) {
            System.out.println("UPDATE");
            affected = db.update(ContractInsertPreciosSesion.INSERT_PRECIOS_SESION, values,
                    selection, selectionArgs);
        }else if (ContractInsertPreciosSesion.uriMatcher.match(uri)==ContractInsertPreciosSesion.SINGLE_ROW) {
            String idStock = uri.getPathSegments().get(1);
            affected = db.update(ContractInsertPreciosSesion.INSERT_PRECIOS_SESION, values,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
        }

        else
        if (InsertFlooring.uriMatcher.match(uri)==InsertFlooring.ALLROWS) {
            System.out.println("UPDATE");
            affected = db.update(InsertFlooring.INSERT_FLOORING, values,
                    selection, selectionArgs);
        }else if (InsertFlooring.uriMatcher.match(uri)==InsertFlooring.SINGLE_ROW) {
            String idStock = uri.getPathSegments().get(1);
            affected = db.update(InsertFlooring.INSERT_FLOORING, values,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
        }

        else
        if (ContractInsertImpulso.uriMatcher.match(uri)==ContractInsertImpulso.ALLROWS) {
            System.out.println("UPDATE");
            affected = db.update(ContractInsertImpulso.INSERT_IMPUSLO, values,
                    selection, selectionArgs);
        }else if (ContractInsertImpulso.uriMatcher.match(uri)==ContractInsertImpulso.SINGLE_ROW) {
            String idStock = uri.getPathSegments().get(1);
            affected = db.update(ContractInsertImpulso.INSERT_IMPUSLO, values,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
        }

        else
        if (ContractInsertExh.uriMatcher.match(uri)==ContractInsertExh.ALLROWS) {
            System.out.println("UPDATE");
            affected = db.update(ContractInsertExh.INSERT_EXH, values,
                    selection, selectionArgs);
        }else if (ContractInsertExh.uriMatcher.match(uri)==ContractInsertExh.SINGLE_ROW) {
            String idStock = uri.getPathSegments().get(1);
            affected = db.update(ContractInsertExh.INSERT_EXH, values,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
        }

        else
        if (ContractInsertExhSesion.uriMatcher.match(uri)==ContractInsertExhSesion.ALLROWS) {
            System.out.println("UPDATE");
            affected = db.update(ContractInsertExhSesion.INSERT_EXH_SESION, values,
                    selection, selectionArgs);
        }else if (ContractInsertExhSesion.uriMatcher.match(uri)==ContractInsertExhSesion.SINGLE_ROW) {
            String idStock = uri.getPathSegments().get(1);
            affected = db.update(ContractInsertExhSesion.INSERT_EXH_SESION, values,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
        }

        else
        if (ContractInsertFotografico.uriMatcher.match(uri)==ContractInsertFotografico.ALLROWS) {
            System.out.println("UPDATE");
            affected = db.update(ContractInsertFotografico.INSERT_FOTOGRAFICO, values,
                    selection, selectionArgs);
        }else if (ContractInsertFotografico.uriMatcher.match(uri)==ContractInsertFotografico.SINGLE_ROW) {
            String idStock = uri.getPathSegments().get(1);
            affected = db.update(ContractInsertFotografico.INSERT_FOTOGRAFICO, values,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
        }

        else
        if (ContractInsertAsistencia.uriMatcher.match(uri)==ContractInsertAsistencia.ALLROWS) {
            System.out.println("UPDATE");
            affected = db.update(ContractInsertAsistencia.INSERT_ASISTENCIA, values,
                    selection, selectionArgs);
        }else if (ContractInsertAsistencia.uriMatcher.match(uri)==ContractInsertAsistencia.SINGLE_ROW) {
            String idStock = uri.getPathSegments().get(1);
            affected = db.update(ContractInsertAsistencia.INSERT_ASISTENCIA, values,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
        }

        else
        if (ContractAsistenciasLocal.uriMatcher.match(uri)==ContractAsistenciasLocal.ALLROWS) {
            System.out.println("UPDATE");
            affected = db.update(ContractAsistenciasLocal.TABLE_NAME, values,
                    selection, selectionArgs);
        }else if (ContractAsistenciasLocal.uriMatcher.match(uri)==ContractAsistenciasLocal.SINGLE_ROW) {
            String idStock = uri.getPathSegments().get(1);
            affected = db.update(ContractAsistenciasLocal.TABLE_NAME, values,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
        }

        else
        if (ContractHoraMarcacion.uriMatcher.match(uri)==ContractHoraMarcacion.ALLROWS) {
            System.out.println("UPDATE");
            affected = db.update(ContractHoraMarcacion.TABLE_NAME, values,
                    selection, selectionArgs);
        }else if (ContractHoraMarcacion.uriMatcher.match(uri)==ContractHoraMarcacion.SINGLE_ROW) {
            String idStock = uri.getPathSegments().get(1);
            affected = db.update(ContractHoraMarcacion.TABLE_NAME, values,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
        }

        else
        if (ContractInsertEvidencias.uriMatcher.match(uri)==ContractInsertEvidencias.ALLROWS) {
            System.out.println("UPDATE");
            affected = db.update(ContractInsertEvidencias.INSERT_EVIDENCIAS, values,
                    selection, selectionArgs);
        }else if (ContractInsertEvidencias.uriMatcher.match(uri)==ContractInsertEvidencias.SINGLE_ROW) {
            String idStock = uri.getPathSegments().get(1);
            affected = db.update(ContractInsertEvidencias.INSERT_EVIDENCIAS, values,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
        }


        else
        if (ContractInsertGps.uriMatcher.match(uri)==ContractInsertGps.ALLROWS) {
            System.out.println("UPDATE");
            affected = db.update(ContractInsertGps.INSERT_GPS, values,
                    selection, selectionArgs);
        }else if (ContractInsertGps.uriMatcher.match(uri)==ContractInsertGps.SINGLE_ROW) {
            String idStock = uri.getPathSegments().get(1);
            affected = db.update(ContractInsertGps.INSERT_GPS, values,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
        }


        else
        if (ContractInsertRastreo.uriMatcher.match(uri)==ContractInsertRastreo.ALLROWS) {
            System.out.println("UPDATE");
            affected = db.update(ContractInsertRastreo.INSERT_GEO, values,
                    selection, selectionArgs);
        }else if (ContractInsertRastreo.uriMatcher.match(uri)==ContractInsertRastreo.SINGLE_ROW) {
            String idStock = uri.getPathSegments().get(1);
            affected = db.update(ContractInsertRastreo.INSERT_GEO, values,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
        }


        else
        if (ContractNotificacion.uriMatcher.match(uri)==ContractNotificacion.ALLROWS) {
            System.out.println("UPDATE NOTIFICACION");
            affected = db.update(ContractNotificacion.NOTIFICACION, values,
                    selection, selectionArgs);
        }else if (ContractNotificacion.uriMatcher.match(uri)==ContractNotificacion.SINGLE_ROW) {
            String idStock = uri.getPathSegments().get(1);
            affected = db.update(ContractNotificacion.NOTIFICACION, values,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
        }


        else
        if (ContractInsertInicial.uriMatcher.match(uri)==ContractInsertInicial.ALLROWS) {
            System.out.println("UPDATE");
            affected = db.update(ContractInsertInicial.INSERT_INICIAL, values,
                    selection, selectionArgs);
        }else if (ContractInsertInicial.uriMatcher.match(uri)==ContractInsertInicial.SINGLE_ROW) {
            String idStock = uri.getPathSegments().get(1);
            affected = db.update(ContractInsertInicial.INSERT_INICIAL, values,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
        }

        else
        if (ContractInsertPromocion.uriMatcher.match(uri)==ContractInsertPromocion.ALLROWS) {
            System.out.println("UPDATE");
            affected = db.update(ContractInsertPromocion.INSERT_PROMO, values,
                    selection, selectionArgs);
        }else if (ContractInsertPromocion.uriMatcher.match(uri)==ContractInsertPromocion.SINGLE_ROW) {
            String idStock = uri.getPathSegments().get(1);
            affected = db.update(ContractInsertPromocion.INSERT_PROMO, values,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
        }

        else
        if (ContractInsertImplementacion.uriMatcher.match(uri)==ContractInsertImplementacion.ALLROWS) {
            System.out.println("UPDATE");
            affected = db.update(ContractInsertImplementacion.INSERT_IMPLEM, values,
                    selection, selectionArgs);
        }else if (ContractInsertImplementacion.uriMatcher.match(uri)==ContractInsertImplementacion.SINGLE_ROW) {
            String idStock = uri.getPathSegments().get(1);
            affected = db.update(ContractInsertImplementacion.INSERT_IMPLEM, values,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
        }

        else
        if (ContractInsertValores.uriMatcher.match(uri)==ContractInsertValores.ALLROWS) {
            System.out.println("UPDATE");
            affected = db.update(ContractInsertValores.INSERT_VALORES, values,
                    selection, selectionArgs);
        }else if (ContractInsertValores.uriMatcher.match(uri)==ContractInsertValores.SINGLE_ROW) {
            String idStock = uri.getPathSegments().get(1);
            affected = db.update(ContractInsertValores.INSERT_VALORES, values,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
        }

        else
        if (ContractInsertValoresSesion.uriMatcher.match(uri)==ContractInsertValoresSesion.ALLROWS) {
            System.out.println("UPDATE");
            affected = db.update(ContractInsertValoresSesion.INSERT_VALORES_SESION, values,
                    selection, selectionArgs);
        }else if (ContractInsertValoresSesion.uriMatcher.match(uri)==ContractInsertValoresSesion.SINGLE_ROW) {
            String idStock = uri.getPathSegments().get(1);
            affected = db.update(ContractInsertValoresSesion.INSERT_VALORES_SESION, values,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
        }

        else
        if (ContractInsertPdv.uriMatcher.match(uri)==ContractInsertPdv.ALLROWS) {
            System.out.println("UPDATE");
            affected = db.update(ContractInsertPdv.INSERT_PDV, values,
                    selection, selectionArgs);
        }else if (ContractInsertPdv.uriMatcher.match(uri)==ContractInsertPdv.SINGLE_ROW) {
            String idStock = uri.getPathSegments().get(1);
            affected = db.update(ContractInsertPdv.INSERT_PDV, values,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
        }

        else
        if (ContractInsertShare.uriMatcher.match(uri)==ContractInsertShare.ALLROWS) {
            System.out.println("UPDATE");
            affected = db.update(ContractInsertShare.INSERT_SHARE, values,
                    selection, selectionArgs);
        }else if (ContractInsertShare.uriMatcher.match(uri)==ContractInsertShare.SINGLE_ROW) {
            String idStock = uri.getPathSegments().get(1);
            affected = db.update(ContractInsertShare.INSERT_SHARE, values,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
        }

        else
        if (ContractInsertPDI.uriMatcher.match(uri)==ContractInsertPDI.ALLROWS) {
            System.out.println("UPDATE");
            affected = db.update(ContractInsertPDI.INSERT_PDI, values,
                    selection, selectionArgs);
        }else if (ContractInsertPDI.uriMatcher.match(uri)==ContractInsertPDI.SINGLE_ROW) {
            String idStock = uri.getPathSegments().get(1);
            affected = db.update(ContractInsertPDI.INSERT_PDI, values,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
        }

        else
        if (ContractInsertAgotados.uriMatcher.match(uri)==ContractInsertAgotados.ALLROWS) {
            System.out.println("UPDATE");
            affected = db.update(ContractInsertAgotados.INSERT_AGOTADOS, values,
                    selection, selectionArgs);
        }else if (ContractInsertAgotados.uriMatcher.match(uri)==ContractInsertAgotados.SINGLE_ROW) {
            String idStock = uri.getPathSegments().get(1);
            affected = db.update(ContractInsertAgotados.INSERT_AGOTADOS, values,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
        }

        else
        if (ContractInsertVenta.uriMatcher.match(uri)==ContractInsertVenta.ALLROWS) {
            System.out.println("UPDATE");
            affected = db.update(ContractInsertVenta.INSERT_VENTA, values,
                    selection, selectionArgs);
        }else if (ContractInsertVenta.uriMatcher.match(uri)==ContractInsertVenta.SINGLE_ROW) {
            String idStock = uri.getPathSegments().get(1);
            affected = db.update(ContractInsertVenta.INSERT_VENTA, values,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
        }

        else
        if (ContractInsertLogisticoRelevo.uriMatcher.match(uri)==ContractInsertLogisticoRelevo.ALLROWS) {
            System.out.println("UPDATE");
            affected = db.update(ContractInsertLogisticoRelevo.INSERT_LOGISTICO_RELEVO, values,
                    selection, selectionArgs);
        }else if (ContractInsertLogisticoRelevo.uriMatcher.match(uri)==ContractInsertLogisticoRelevo.SINGLE_ROW) {
            String idStock = uri.getPathSegments().get(1);
            affected = db.update(ContractInsertLogisticoRelevo.INSERT_LOGISTICO_RELEVO, values,
                    Constantes.ID_REMOTA + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
        }

        else if (ContractCausalesAsistencia.uriMatcher.match(uri) == ContractCausalesAsistencia.ALLROWS) {
            System.out.println("UPDATE");
            affected = db.update(ContractCausalesAsistencia.CAUSALES_ASISTENCIA, values,
                    selection, selectionArgs);
        } else if (ContractCausalesAsistencia.uriMatcher.match(uri) == ContractCausalesAsistencia.SINGLE_ROW) {
            String idStock = uri.getPathSegments().get(1);
            affected = db.update(ContractCausalesAsistencia.CAUSALES_ASISTENCIA, values,
                    ContractCausalesAsistencia.Columnas.ID + "=" + idStock
                            + (!TextUtils.isEmpty(selection) ?
                            " AND (" + selection + ')' : ""),
                    selectionArgs);
        }

        else{
            throw new IllegalArgumentException("URI desconocida: " + uri);
        }

        resolver.notifyChange(uri, null, true);
        return affected;
    }
}
