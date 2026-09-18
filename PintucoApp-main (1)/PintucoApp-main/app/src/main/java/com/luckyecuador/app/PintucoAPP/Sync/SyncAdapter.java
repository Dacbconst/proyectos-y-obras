package com.luckyecuador.app.PintucoAPP.Sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.content.SharedPreferences;
import android.content.SyncResult;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.luckyecuador.app.PintucoAPP.Clase.BaseAlertas;
import com.luckyecuador.app.PintucoAPP.Clase.BaseCausalesAsistencia;
import com.luckyecuador.app.PintucoAPP.Clase.BaseCausalesAsistenciaAtraso;
import com.luckyecuador.app.PintucoAPP.Clase.BaseCausalesMCI;
import com.luckyecuador.app.PintucoAPP.Clase.BaseCausalesOSA;
import com.luckyecuador.app.PintucoAPP.Clase.BasePDI;
import com.luckyecuador.app.PintucoAPP.Clase.BasePharmaValue;
import com.luckyecuador.app.PintucoAPP.Clase.BasePopSugerido;
import com.luckyecuador.app.PintucoAPP.Clase.BasePortafolioProductos;
import com.luckyecuador.app.PintucoAPP.Clase.BasePromociones;
import com.luckyecuador.app.PintucoAPP.Clase.BaseCombosCanjes;
import com.luckyecuador.app.PintucoAPP.Clase.BasePortafolioPrioritario;
import com.luckyecuador.app.PintucoAPP.Clase.BaseRangosPrecios;
import com.luckyecuador.app.PintucoAPP.Clase.BaseRangosPreciosSku;
import com.luckyecuador.app.PintucoAPP.Clase.BaseRotacion;
import com.luckyecuador.app.PintucoAPP.Clase.BaseTareas;
import com.luckyecuador.app.PintucoAPP.Clase.Base_causales_prod_mal_est;
import com.luckyecuador.app.PintucoAPP.Clase.Base_justificacion;
import com.luckyecuador.app.PintucoAPP.Clase.Base_marcasBlancas;
import com.luckyecuador.app.PintucoAPP.Clase.Base_precio_pvc;
import com.luckyecuador.app.PintucoAPP.Clase.Base_preguntas;
import com.luckyecuador.app.PintucoAPP.Clase.Base_tests;
import com.luckyecuador.app.PintucoAPP.Clase.Base_tiempo_almuerzo;
import com.luckyecuador.app.PintucoAPP.Clase.Base_tipoPrecios;
import com.luckyecuador.app.PintucoAPP.Clase.Base_tipo_exh;
import com.luckyecuador.app.PintucoAPP.Clase.Base_versiones;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractAlertas;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractCausalesAsistencia;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractCausalesAsistenciaAtraso;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractCausalesMCI;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractCausalesOSA;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractCausalesProdMalEst;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractComboCanjes;
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
import com.luckyecuador.app.PintucoAPP.Contracts.ContractPDI;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractPopSugerido;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractPreciosPvc;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractPrioritario;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractPromociones;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractRangosPrecios;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractRangosPreciosSku;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractRotacion;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertAgotados;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertFotografico;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertImplementacion;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertImpulso;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertInicial;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertPacks;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertPdv;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertPreguntas;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertProdCaducar;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertRotacion;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertPromocion;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertShare;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertValores;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertVenta;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractPharmaValue;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractPortafolioProductos;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractPreguntas;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractTareas;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractTests;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractTiempoAlmuerzo;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractTipoExh;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractTipoPrecios;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractVersiones;
import com.luckyecuador.app.PintucoAPP.DataBase.DatabaseHelper;
import com.luckyecuador.app.PintucoAPP.Utilidades.UtilidadesAgotados;
import com.luckyecuador.app.PintucoAPP.Utilidades.UtilidadesAlmuerzo;
import com.luckyecuador.app.PintucoAPP.Utilidades.UtilidadesProyectosContacto;
import com.luckyecuador.app.PintucoAPP.Utilidades.UtilidadesPagoFactura;
import com.luckyecuador.app.PintucoAPP.Utilidades.UtilidadesProforma;
import com.luckyecuador.app.PintucoAPP.Utilidades.UtilidadesAsistencia;
import com.luckyecuador.app.PintucoAPP.Utilidades.UtilidadesCanjes;
import com.luckyecuador.app.PintucoAPP.Utilidades.UtilidadesEjecucionMateriales;
import com.luckyecuador.app.PintucoAPP.Utilidades.UtilidadesEvidencias;
import com.luckyecuador.app.PintucoAPP.Utilidades.UtilidadesFotografico;
import com.luckyecuador.app.PintucoAPP.Utilidades.UtilidadesImplementacion;
import com.luckyecuador.app.PintucoAPP.Utilidades.UtilidadesImpulso;
import com.luckyecuador.app.PintucoAPP.Utilidades.UtilidadesInicial;
import com.luckyecuador.app.PintucoAPP.Utilidades.UtilidadesLog;
import com.luckyecuador.app.PintucoAPP.Utilidades.UtilidadesLogisticoRelevo;
import com.luckyecuador.app.PintucoAPP.Utilidades.UtilidadesMCI;
import com.luckyecuador.app.PintucoAPP.Utilidades.UtilidadesMallaCodificados;
import com.luckyecuador.app.PintucoAPP.Utilidades.UtilidadesMaterialesRecibidos;
import com.luckyecuador.app.PintucoAPP.Utilidades.UtilidadesPDI;
import com.luckyecuador.app.PintucoAPP.Utilidades.UtilidadesPacks;
import com.luckyecuador.app.PintucoAPP.Utilidades.UtilidadesPdv;
import com.luckyecuador.app.PintucoAPP.Utilidades.UtilidadesPdvFotografico;
import com.luckyecuador.app.PintucoAPP.Utilidades.UtilidadesPreguntas;
import com.luckyecuador.app.PintucoAPP.Utilidades.UtilidadesProdCad;
import com.luckyecuador.app.PintucoAPP.Utilidades.UtilidadesPropensosYProdMalEst;
import com.luckyecuador.app.PintucoAPP.Utilidades.UtilidadesResultadoPreguntas;
import com.luckyecuador.app.PintucoAPP.Utilidades.UtilidadesRotacion;
import com.luckyecuador.app.PintucoAPP.Utilidades.UtilidadesPromocion;
import com.luckyecuador.app.PintucoAPP.Utilidades.UtilidadesShare;
import com.luckyecuador.app.PintucoAPP.Utilidades.UtilidadesSugeridos;
import com.luckyecuador.app.PintucoAPP.Utilidades.UtilidadesTareas;
import com.luckyecuador.app.PintucoAPP.Utilidades.UtilidadesValores;
import com.google.gson.Gson;
import com.luckyecuador.app.PintucoAPP.Clase.Precios;
import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;
import com.luckyecuador.app.PintucoAPP.Conexion.Mensajes;
import com.luckyecuador.app.PintucoAPP.Conexion.VolleySingleton;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertExh;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertGps;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertPrecios;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertRastreo;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractNotificacion;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractPrecios;
import com.luckyecuador.app.PintucoAPP.Contracts.InsertFlooring;
import com.luckyecuador.app.PintucoAPP.DataBase.Projection;
import com.luckyecuador.app.PintucoAPP.R;
import com.luckyecuador.app.PintucoAPP.Utilidades.UtilidadesExh;
import com.luckyecuador.app.PintucoAPP.Utilidades.UtilidadesFlooring;
import com.luckyecuador.app.PintucoAPP.Utilidades.UtilidadesGps;
import com.luckyecuador.app.PintucoAPP.Utilidades.UtilidadesNotificacion;
import com.luckyecuador.app.PintucoAPP.Utilidades.UtilidadesPrecios;
import com.luckyecuador.app.PintucoAPP.Utilidades.UtilidadesRastreo;
import com.luckyecuador.app.PintucoAPP.Utilidades.UtilidadesVenta;
import com.luckyecuador.app.PintucoAPP.Utilidades.UtilidadesVentas;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Lucky Ecuador on 15/07/2016.
 */
//Maneja la transferencia de datos entre el servidor y el cliente
public class SyncAdapter extends AbstractThreadedSyncAdapter {
    private static final String TAG = SyncAdapter.class.getSimpleName();

    public static final int MY_DEFAULT_TIMEOUT = 60000;
    // Claves para viajar valueActivity/operator dentro del Bundle de cada petición de sync.
    // No usar los campos static de abajo como fuente de verdad en onPerformSync: dos llamadas
    // a sincronizarAhora() seguidas (ej. guardar una cita dispara una subida, y casi cualquier
    // onResume dispara una bajada de contactos) pueden pisarlos antes de que el onPerformSync
    // de la primera petición llegue a leerlos, hacienda que una sincronización se ejecute con
    // los datos de la otra (p.ej. bajar_contactos con operator=null, que el servidor responde
    // con una lista vacía/incorrecta y dispara el borrado local de contactos "que ya no vinieron").
    private static final String EXTRA_VALUE_ACTIVITY = "syncAdapter.valueActivity";
    private static final String EXTRA_OPERATOR = "syncAdapter.operator";
    private static Context mContext;
    private static String valueActivity;
    ContentResolver resolver;
    private Gson gson = new Gson();
    private static String operator;


    SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        resolver = context.getContentResolver();
    }

    /**
     * Constructor para mantener compatibilidad en versiones inferiores a 3.0
     */
    public SyncAdapter(
            Context context,
            boolean autoInitialize,
            boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        resolver = context.getContentResolver();
    }

    public static void inicializarSyncAdapter(Context context) {
        obtenerCuentaASincronizar(context);
    }

    @Override
    public void onPerformSync(Account account,
                              Bundle extras,
                              String authority,
                              ContentProviderClient provider,
                              final SyncResult syncResult) {
        try {
            // Se leen del Bundle propio de ESTA petición (no de los campos static, que otra
            // llamada a sincronizarAhora() pudo haber pisado mientras esta petición esperaba
            // turno en la cola del SyncManager). Se usa el static como respaldo únicamente
            // por si alguna vez el sistema dispara un sync sin pasar por sincronizarAhora().
            String valueActivity = extras.getString(EXTRA_VALUE_ACTIVITY, SyncAdapter.valueActivity);
            String operator = extras.getString(EXTRA_OPERATOR, SyncAdapter.operator);

            Log.i(TAG, "onPerformSync() iniciado. Actividad: " + valueActivity + ", Operador: " + operator);

            boolean soloSubida = extras.getBoolean(ContentResolver.SYNC_EXTRAS_UPLOAD, false);

            if (!soloSubida) {//Bajando
                Log.i(TAG, "Iniciando descarga de datos para: " + valueActivity);
                //Llena la Base/Datos Local con los datos del Servidor
                switch (valueActivity) {
                    case Constantes.bajar_Oper: //PDV
                        Log.i(TAG, "Ejecutando: realizarSincronizacionLocalPDV");
                        realizarSincronizacionLocalPDV(syncResult, operator);
                        break;
                    case Constantes.bajar_floo: //PRODUCTOS
                        Log.i(TAG, "Ejecutando: realizarSincronizacionLocalFlooring2");
                        realizarSincronizacionLocalFlooring2(syncResult, operator);
                    //    realizarSincronizacionLocalMarcasBlancas(syncResult, operator);
                        break;
                    case Constantes.bajar_promociones: //PROMOCIONES
                        Log.i(TAG, "Ejecutando: realizarSincronizacionLocalPromociones");
                        realizarSincronizacionLocalPromociones(syncResult, operator);
                        break;
                    case Constantes.bajar_tipo_exhibicion: //TIPO EXH
                        Log.i(TAG, "Ejecutando: realizarSincronizacionLocalTipoExh");
                        realizarSincronizacionLocalTipoExh(syncResult, operator);
                        break;
                    case Constantes.bajar_precios_pvc: //PRECIOS PVC
                        Log.i(TAG, "Ejecutando: realizarSincronizacionLocalPreciosPvc");
                        realizarSincronizacionLocalPreciosPvc(syncResult, operator);
                        break;
                    case Constantes.bajar_tipo_precios: //TIPO PRECIOS
                        Log.i(TAG, "Ejecutando: realizarSincronizacionLocalTipoPrecios");
                        realizarSincronizacionLocalTipoPrecios(syncResult, operator);
                        break;
                    case Constantes.bajar_justificaciones: //JUSTIFICACIONES
                        Log.i(TAG, "Ejecutando: realizarSincronizacionLocalJustificacion");
                        realizarSincronizacionLocalJustificacion(syncResult, operator);
                        break;
                    case Constantes.bajar_causales_prod_mal_est: //PRODUCTOS EN MAL ESTADO
                        Log.i(TAG, "Ejecutando: realizarSincronizacionLocalCausalesProdMalEst");
                        realizarSincronizacionLocalCausalesProdMalEst(syncResult, operator);
                        break;
                    case Constantes.bajar_versiones: // VERSIONES APP
                        Log.i(TAG, "Ejecutando: realizarSincronizacionLocalVersiones");
                        realizarSincronizacionLocalVersiones(syncResult, operator);
                        break;
                    case Constantes.bajar_tiempo_almuerzo: //TIEMPO ALMUERZO
                        Log.i(TAG, "Ejecutando: realizarSincronizacionLocalTiempoAlmuerzo");
                        realizarSincronizacionLocalTiempoAlmuerzo(syncResult, operator);
                        break;
                    case Constantes.bajar_causales_mci: //CAUSALES MCI
                        Log.i(TAG, "Ejecutando: realizarSincronizacionLocalCausalesMCI");
                        realizarSincronizacionLocalCausalesMCI(syncResult, operator);
                        break;
                    case Constantes.bajar_causales_asistencia:
                        Log.i(TAG, "Ejecutando: realizarSincronizacionLocalCausalesAsistencia");
                        realizarSincronizacionLocalCausalesAsistencia(syncResult, operator);
                        break;
                    case Constantes.bajar_rangos_precios: //RAnGo PreZio WAAAZAAAAA
                        Log.i(TAG, "Ejecutando: realizarSincronizacionLocalRangosPrecios");
                        realizarSincronizacionLocalRangosPrecios(syncResult, operator);
                        break;
                    case Constantes.bajar_rangos_precios_sku:
                        Log.i(TAG, "Ejecutando: realizarSincronizacionLocalRangosPreciosSku");
                        realizarSincronizacionLocalRangosPreciosSku(syncResult, operator);
                        break;
                    case Constantes.bajar_causales_asistencia_atraso:
                        Log.i(TAG, "Ejecutando: realizarSincronizacionLocalCausalesAsistenciaAtraso");
                        realizarSincronizacionLocalCausalesAsistenciaAtraso(syncResult, operator);
                        break;
                    case Constantes.bajar_contactos:
                        Log.i(TAG, "Ejecutando: realizarSincronizacionLocalContactos");
                        realizarSincronizacionLocalContactos(syncResult, operator);
                        break;
                    case Constantes.bajar_proforma:
                        Log.i(TAG, "Ejecutando: realizarSincronizacionLocalProforma");
                        realizarSincronizacionLocalProforma(syncResult, operator);
                        break;
                    case Constantes.bajar_pago_factura:
                        Log.i(TAG, "Ejecutando: realizarSincronizacionLocalPagoFactura");
                        realizarSincronizacionLocalPagoFactura(syncResult, operator);
                        break;

                    default:
                        Log.w(TAG, "Actividad de bajada no reconocida: " + valueActivity);
                        enviarBroadcast(false, "Error: Tarea de sincronización desconocida.");
                        break;
                }
            } else {
                Log.i(TAG, "Iniciando subida de datos para: " + valueActivity);
                switch (valueActivity) {
                    case Constantes.insertPrecio:
                        realizarSincronizacionRemotaInsertPrecio();
                        realizarSincronizacionRemotaInsertGeo();
                        realizarSincronizacionRemotaInsertNot();
                        realizarSincronizacionRemotaInsertLog();
                        break;
                    case Constantes.insertCanjes:
                        realizarSincronizacionRemotaInsertCanjes();
                        realizarSincronizacionRemotaInsertGeo();
                        realizarSincronizacionRemotaInsertNot();
                        realizarSincronizacionRemotaInsertLog();
                        break;
                    case Constantes.insertAlmuerzo:
                        realizarSincronizacionRemotaInsertAlmuerzo();
                        realizarSincronizacionRemotaInsertGeo();
                        realizarSincronizacionRemotaInsertNot();
                        realizarSincronizacionRemotaInsertLog();
                        break;
                    case Constantes.insertProyectosContacto:
                        realizarSincronizacionRemotaInsertProyectosContacto();
                        realizarSincronizacionRemotaInsertGeo();
                        realizarSincronizacionRemotaInsertNot();
                        realizarSincronizacionRemotaInsertLog();
                        break;
                    case Constantes.insertProforma:
                        realizarSincronizacionRemotaInsertProforma();
                        realizarSincronizacionRemotaInsertGeo();
                        realizarSincronizacionRemotaInsertNot();
                        realizarSincronizacionRemotaInsertLog();
                        break;
                    case Constantes.insertPagoFactura:
                        realizarSincronizacionRemotaInsertPagoFactura();
                        realizarSincronizacionRemotaInsertGeo();
                        realizarSincronizacionRemotaInsertNot();
                        realizarSincronizacionRemotaInsertLog();
                        break;
                    case Constantes.insertMaterialesRecibidos:
                        realizarSincronizacionRemotaInsertMaterialesRecibidos();
                        realizarSincronizacionRemotaInsertGeo();
                        realizarSincronizacionRemotaInsertNot();
                        realizarSincronizacionRemotaInsertLog();
                        break;
                    case Constantes.insertEjecucionMateriales:
                        realizarSincronizacionRemotaInsertEjecucionMateriales();
                        realizarSincronizacionRemotaInsertGeo();
                        realizarSincronizacionRemotaInsertNot();
                        realizarSincronizacionRemotaInsertLog();
                        break;
                    case Constantes.insertMci:
                        realizarSincronizacionRemotaInsertMCI();
                        realizarSincronizacionRemotaInsertGeo();
                        realizarSincronizacionRemotaInsertNot();
                        realizarSincronizacionRemotaInsertLog();
                        break;
                    case Constantes.insertExh:
                        realizarSincronizacionRemotaInsertExh();
                        realizarSincronizacionRemotaInsertGeo();
                        realizarSincronizacionRemotaInsertNot();
                        realizarSincronizacionRemotaInsertLog();
                        break;
                    case Constantes.insertGps:
                        realizarSincronizacionRemotaInsertGps();
                        realizarSincronizacionRemotaInsertGeo();
                        realizarSincronizacionRemotaInsertNot();
                        realizarSincronizacionRemotaInsertLog();
                        break;
                    case Constantes.insertAsistencia:
                        realizarSincronizacionRemotaInsertAsistencia();
                        realizarSincronizacionRemotaInsertGeo();
                        realizarSincronizacionRemotaInsertNot();
                        realizarSincronizacionRemotaInsertLog();
                        break;
                    case Constantes.insertGeo:
                        realizarSincronizacionRemotaInsertGeo();
                        realizarSincronizacionRemotaInsertNot();
                        realizarSincronizacionRemotaInsertLog();
                        break;
                    case Constantes.insertFlooring:
                        realizarSincronizacionRemotaInsertFlooring();
                        realizarSincronizacionRemotaInsertGeo();
                        realizarSincronizacionRemotaInsertNot();
                        realizarSincronizacionRemotaInsertLog();
                        break;
                    case Constantes.insertPromocion:
                        realizarSincronizacionRemotaInsertPromo();
                        realizarSincronizacionRemotaInsertGeo();
                        realizarSincronizacionRemotaInsertNot();
                        realizarSincronizacionRemotaInsertLog();
                        break;
                    case Constantes.insertPdvFotografico:
                        realizarSincronizacionRemotaInsertPdvFotografico();
                        realizarSincronizacionRemotaInsertGeo();
                        realizarSincronizacionRemotaInsertNot();
                        realizarSincronizacionRemotaInsertLog();
                        break;
                    case Constantes.insertImplementacion:
                        realizarSincronizacionRemotaInsertImplementacion();
                        realizarSincronizacionRemotaInsertGeo();
                        realizarSincronizacionRemotaInsertNot();
                        realizarSincronizacionRemotaInsertLog();
                        break;
                    case Constantes.insertvalores:
                        realizarSincronizacionRemotaInsertValores();
                        realizarSincronizacionRemotaInsertGeo();
                        realizarSincronizacionRemotaInsertNot();
                        realizarSincronizacionRemotaInsertLog();
                        break;
                    case Constantes.insertpdv:
                        realizarSincronizacionRemotaInsertNot();
                        realizarSincronizacionRemotaInsertGeo();
                        realizarSincronizacionRemotaInsertLog();
                        break;
                    case Constantes.insertShare:
                        realizarSincronizacionRemotaInsertShare();
                        realizarSincronizacionRemotaInsertGeo();
                        realizarSincronizacionRemotaInsertNot();
                        realizarSincronizacionRemotaInsertLog();
                        break;
                    case Constantes.insertarPdI:
                        realizarSincronizacionRemotaInsertPDI();
                        realizarSincronizacionRemotaInsertGeo();
                        realizarSincronizacionRemotaInsertNot();
                        realizarSincronizacionRemotaInsertLog();
                        break;
                    case Constantes.insertAgotados:
                        realizarSincronizacionRemotaInsertAgotados();
                        realizarSincronizacionRemotaInsertGeo();
                        realizarSincronizacionRemotaInsertNot();
                        realizarSincronizacionRemotaInsertLog();
                        break;
                    case Constantes.insertVenta:
                        realizarSincronizacionRemotaInsertVenta();
                        realizarSincronizacionRemotaInsertGeo();
                        realizarSincronizacionRemotaInsertNot();
                        realizarSincronizacionRemotaInsertLog();
                        break;
                    case Constantes.insertFoto:
                        realizarSincronizacionRemotaInsertFotografico();
                        realizarSincronizacionRemotaInsertGeo();
                        realizarSincronizacionRemotaInsertNot();
                        realizarSincronizacionRemotaInsertLog();
                        break;
                    case Constantes.insertPreguntas:
                        realizarSincronizacionRemotaInsertPreguntas();
                        realizarSincronizacionRemotaInsertGeo();
                        realizarSincronizacionRemotaInsertNot();
                        realizarSincronizacionRemotaInsertLog();
                        break;
                    case Constantes.insertResultadoPreguntas:
                        realizarSincronizacionRemotaInsertResultadoPreguntas();
                        realizarSincronizacionRemotaInsertGeo();
                        realizarSincronizacionRemotaInsertNot();
                        realizarSincronizacionRemotaInsertLog();
                        break;
                    case Constantes.insertProdCad:
                        realizarSincronizacionRemotaInsertProdCad();
                        realizarSincronizacionRemotaInsertGeo();
                        realizarSincronizacionRemotaInsertNot();
                        realizarSincronizacionRemotaInsertLog();
                        break;
                    case Constantes.insertSugeridos:
                        realizarSincronizacionRemotaInsertSugeridos();
                        realizarSincronizacionRemotaInsertGeo();
                        realizarSincronizacionRemotaInsertNot();
                        realizarSincronizacionRemotaInsertLog();
                        break;
                    case Constantes.insertRotacion:
                        realizarSincronizacionRemotaInsertRotacion();
                        realizarSincronizacionRemotaInsertGeo();
                        realizarSincronizacionRemotaInsertNot();
                        realizarSincronizacionRemotaInsertLog();
                        break;
                    case Constantes.insertPacks:
                        realizarSincronizacionRemotaInsertPacks();
                        realizarSincronizacionRemotaInsertGeo();
                        realizarSincronizacionRemotaInsertNot();
                        realizarSincronizacionRemotaInsertLog();
                        break;
                    case Constantes.insertTareas:
                        realizarSincronizacionRemotaInsertTareas();
                        realizarSincronizacionRemotaInsertGeo();
                        realizarSincronizacionRemotaInsertNot();
                        realizarSincronizacionRemotaInsertLog();
                        break;
                    case Constantes.insertImpulso:
                        realizarSincronizacionRemotaInsertImpulso();
                        realizarSincronizacionRemotaInsertGeo();
                        realizarSincronizacionRemotaInsertNot();
                        realizarSincronizacionRemotaInsertLog();
                        break;
                    case Constantes.insertMallaCodificados:
                        realizarSincronizacionRemotaInsertMallaCodificados();
                        realizarSincronizacionRemotaInsertGeo();
                        realizarSincronizacionRemotaInsertNot();
                        realizarSincronizacionRemotaInsertLog();
                        break;
                    case Constantes.insertEvidencias:
                        realizarSincronizacionRemotaInsertEvidencias();
                        realizarSincronizacionRemotaInsertGeo();
                        realizarSincronizacionRemotaInsertNot();
                        realizarSincronizacionRemotaInsertLog();
                        break;
                    case Constantes.insertNot:
                        realizarSincronizacionRemotaInsertNot();
                        realizarSincronizacionRemotaInsertGeo();
                        realizarSincronizacionRemotaInsertLog();
                        break;
                    case Constantes.insertVentas:
                        realizarSincronizacionRemotaInsertVentas();
                        realizarSincronizacionRemotaInsertGeo();
                        realizarSincronizacionRemotaInsertLog();
                        break;
                    case Constantes.insertPropensosYProdMalEst:
                        realizarSincronizacionRemotaInsertPropensosYProdMalEst();
                        realizarSincronizacionRemotaInsertGeo();
                        realizarSincronizacionRemotaInsertLog();
                        break;
                    case Constantes.insertLogisticoRelevo:
                        realizarSincronizacionRemotaInsertLogisticoRelevo();
                        realizarSincronizacionRemotaInsertGeo();
//                        realizarSincronizacionRemotaInsertNot();
                        realizarSincronizacionRemotaInsertLog();
                        break;
                    case Constantes.SUBIR_TODO:
                        realizarSincronizacionRemotaInsertPrecio();
                        realizarSincronizacionRemotaInsertFlooring();
                        realizarSincronizacionRemotaInsertExh();
                        realizarSincronizacionRemotaInsertGps();
                        realizarSincronizacionRemotaInsertPromo();
                        realizarSincronizacionRemotaInsertImplementacion();
                        realizarSincronizacionRemotaInsertValores();
                        realizarSincronizacionRemotaInsertInicial();
                        realizarSincronizacionRemotaInsertNot();
                        realizarSincronizacionRemotaInsertAgotados();
                        realizarSincronizacionRemotaInsertVenta();
                        realizarSincronizacionRemotaInsertShare();
                        realizarSincronizacionRemotaInsertGeo();
                        realizarSincronizacionRemotaInsertFotografico();
                        realizarSincronizacionRemotaInsertPreguntas();
                        realizarSincronizacionRemotaInsertProdCad();
                        realizarSincronizacionRemotaInsertPacks();
                        realizarSincronizacionRemotaInsertVentas();
                        realizarSincronizacionRemotaInsertTareas();
                        realizarSincronizacionRemotaInsertImpulso();
                        realizarSincronizacionRemotaInsertSugeridos();
                        realizarSincronizacionRemotaInsertCanjes();
                        realizarSincronizacionRemotaInsertMCI();
                        realizarSincronizacionRemotaInsertMaterialesRecibidos();
                        realizarSincronizacionRemotaInsertPDI();
                        realizarSincronizacionRemotaInsertMallaCodificados();
                        realizarSincronizacionRemotaInsertLog();
                        realizarSincronizacionRemotaInsertEvidencias();
                        realizarSincronizacionRemotaInsertPdvFotografico();
                        realizarSincronizacionRemotaInsertAsistencia();
                        realizarSincronizacionRemotaInsertAlmuerzo();
                        realizarSincronizacionRemotaInsertPropensosYProdMalEst();
                        realizarSincronizacionRemotaInsertLogisticoRelevo();
                        realizarSincronizacionRemotaInsertProyectosContacto();
                        realizarSincronizacionRemotaInsertProforma();
                        realizarSincronizacionRemotaInsertPagoFactura();
                        break;
                    default:
                        try {
                            final Activity activity = (Activity) mContext;
                            activity.runOnUiThread(new Runnable() {
                                public void run() {
                                    Log.i("SYNC", Mensajes.MOD_ERROR);
    //                                Toast.makeText(activity, Mensajes.MOD_ERROR, Toast.LENGTH_SHORT).show();
                                }
                            });
                        } catch (Exception e) {
                            Log.i("SYNC", e.getMessage());
                        }
                        break;
                }
            }
        } catch (Exception e) {
            Log.i("SYNC", e.getMessage());
        }
    }

    /**
     * Inicia manualmente la sincronización
     *
     * @param context    Contexto para crear la petición de sincronización
     * @param onlyUpload Usa true para sincronizar el servidor o false para sincronizar el cliente
     */
    public static void sincronizarAhora(Context context, boolean onlyUpload, String valueActivity1, String userName) {
        mContext = context;
        // Se mantienen estos static solo como respaldo de onPerformSync (ver comentario junto a
        // su declaración); la fuente de verdad real para esta petición viaja en el Bundle.
        valueActivity = valueActivity1;
        operator = userName;
        //Toast.makeText(context,"Realizando petición de sincronización remota manual.",Toast.LENGTH_LONG).show();
        Log.i(TAG, "Realizando petición de sincronización remota manual.");
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        if (onlyUpload)
            bundle.putBoolean(ContentResolver.SYNC_EXTRAS_UPLOAD, true);
        bundle.putString(EXTRA_VALUE_ACTIVITY, valueActivity1);
        if (userName != null)
            bundle.putString(EXTRA_OPERATOR, userName);
        ContentResolver.requestSync(obtenerCuentaASincronizar(context),
                context.getString(R.string.provider_authority), bundle);
    }

    /**
     * Crea u obtiene una cuenta existente
     *
     * @param context Contexto para acceder al administrador de cuentas
     * @return cuenta auxiliar.
     */
    private static Account obtenerCuentaASincronizar(Context context) {
        // Obtener instancia del administrador de cuentas
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        // Crear cuenta por defecto
        Account newAccount = new Account(
                context.getString(R.string.app_name), Constantes.ACCOUNT_TYPE);

        // Comprobar existencia de la cuenta
        if (null == accountManager.getPassword(newAccount)) {

            // Añadir la cuenta al account manager sin password y sin datos de user
            if (!accountManager.addAccountExplicitly(newAccount, "", null))
                return null;

        }
        Log.i(TAG, "SyncAdapter: Cuenta de user obtenida.");
        return newAccount;
    }


    /*
     *  SINCRONIZAR TABLA LOCAL
     */

    private void realizarSincronizacionLocalPDV(final SyncResult syncResult, String mercaderista) {
        Log.i(TAG, "Realizando Sincronizacion Local de BasePharmaValue." + mercaderista);
        HashMap<String, String> map = new HashMap<>();// Mapeo previo

        map.put("operator", mercaderista);

        // Crear nuevo objeto Json basado en el mapa
        JSONObject jobject = new JSONObject(map);
        //GET METHOD
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, Constantes.GET_PUNTOVENTAS, jobject, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        procesarRespuestaGetPDV(response, syncResult);
                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                if (error instanceof TimeoutError) {
                                    enviarBroadcast(true, Mensajes.TIME_OUT);
                                } else if (error instanceof NoConnectionError) {
                                    //TODO
                                    enviarBroadcast(true, Mensajes.NO_RED);
                                } else if (error instanceof AuthFailureError) {
                                    //TODO
                                } else if (error instanceof ServerError) {
                                    //TODO
                                    enviarBroadcast(true, Mensajes.SEVER_ERROR);
                                } else if (error instanceof NetworkError) {
                                    //TODO
                                    enviarBroadcast(true, Mensajes.RED_ERROR);
                                } else if (error instanceof ParseError) {
                                    //TODO
                                }
                            }
                        });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(MY_DEFAULT_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);
    }

    private void procesarRespuestaGetPDV(JSONObject response, SyncResult syncResult) {
        try {
            // Obtener atributo "estado"
            String estado = response.getString(Constantes.ESTADO);

            switch (estado) {
                case Constantes.SUCCESS: // EXITO
                    actualizarDatosLocalesPos(response, syncResult);
                    break;
                case Constantes.FAILED: // FALLIDO
                    String mensaje = response.getString(Constantes.MENSAJE);
                    enviarBroadcast(true, mensaje);
                    Log.i(TAG, mensaje);
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void actualizarDatosLocalesPos(JSONObject response, SyncResult syncResult) {
        JSONArray loginResult = null;

        try {
            // Obtener array "gastos"
            loginResult = response.getJSONArray(Constantes.PDV_RESULT);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Parsear con Gson
        BasePharmaValue[] res = gson.fromJson(loginResult != null ? loginResult.toString() : null, BasePharmaValue[].class);
        List<BasePharmaValue> data = Arrays.asList(res);

        // Lista para recolección de operaciones pendientes
        ArrayList<ContentProviderOperation> ops = new ArrayList<>();

        // Tabla hash para recibir los datos entrantes
        HashMap<String, BasePharmaValue> expenseMap = new HashMap<>();
        for (BasePharmaValue e : data) {
            expenseMap.put(e.id, e);
        }

        // Consultar registros remotos actuales
        Uri uri = ContractPharmaValue.CONTENT_URI;
        String select = Constantes.ID_REMOTA + " IS NOT NULL";
        Cursor c = resolver.query(uri, Projection.PROJECTION_PUNTOS, select, null, null);
        assert c != null;

        Log.i(TAG, "Se encontraron " + c.getCount() + " registros locales.");

        // Encontrar datos obsoletos

        String id;
        String channel;
        String subchannel;
        String channel_segment;
        String format;
        String customer_owner;
        String pos_id;
        String pos_name;
        String pos_name_dpsm;
        String zone;
        String region;
        String province;
        String city;
        String address;
        String kam;
        String sales_executive;
        String merchandising;
        String supervisor;
        String mercaderista;
        String user;
        String dpsm;
        String status;
        String tipo;
        String latitud;
        String longitud;
        String foto;
        String segmentacion;
        String compras;
        String pass;
        String numero_controller;
        String fecha_visita;
        String device_id;
        String perimetro;
        String distancia;
        String termometro;
        String hora_inicio;
        String hora_fin;

        while (c.moveToNext()) {
            syncResult.stats.numEntries++;
            id = c.getString(Projection.COLUMNA_ID_REMOTA);
            channel = c.getString(Projection.CHANNEL);
            subchannel = c.getString(Projection.SUBCHANNEL);
            channel_segment = c.getString(Projection.CHANNEL_SEGMENT);
            format = c.getString(Projection.FORMAT);
            customer_owner = c.getString(Projection.CUSTOMER_OWNER);
            pos_id = c.getString(Projection.POS_ID);
            pos_name = c.getString(Projection.POS_NAME);
            pos_name_dpsm = c.getString(Projection.POS_NAME_DPSM);
            zone = c.getString(Projection.ZONA);
            region = c.getString(Projection.REGION);
            province = c.getString(Projection.SECTOR);
            city = c.getString(Projection.CIUDAD);
            address = c.getString(Projection.DIRECCION);
            kam = c.getString(Projection.KAM);
            sales_executive = c.getString(Projection.SALES_EXECUTIVE);
            merchandising = c.getString(Projection.MERCHANDISING);
            supervisor = c.getString(Projection.SUPERVISOR);
            mercaderista = c.getString(Projection.MERCADERISTA);
            user = c.getString(Projection.USUARIO);
            dpsm = c.getString(Projection.DPSM);
            status = c.getString(Projection.STATUS);
            tipo = c.getString(Projection.TIPO_PDV);
            latitud = c.getString(Projection.LATITUD);
            longitud = c.getString(Projection.LONGITUD);
            foto = c.getString(Projection.FOTO);
            segmentacion = c.getString(Projection.SEGMENTACION);
            compras = c.getString(Projection.COMPRAS);
            pass = c.getString(Projection.PASS);
            numero_controller = c.getString(Projection.NUMERO_CONTROLLER);
            fecha_visita = c.getString(Projection.FECHA_VISITA);
            device_id = c.getString(Projection.DEVICE_ID);
            perimetro = c.getString(Projection.PERIMETRO);
            distancia = c.getString(Projection.DISTANCIA);
            termometro = c.getString(Projection.TERMOMETRO);
            hora_inicio = c.getString(Projection.HORA_INICIO);
            hora_fin = c.getString(Projection.HORA_FIN);

            BasePharmaValue match = expenseMap.get(id);

            if (match != null) {
                // Esta entrada existe, por lo que se remueve del mapeado
                expenseMap.remove(id);

                Uri existingUri = ContractPharmaValue.CONTENT_URI.buildUpon()
                        .appendPath(id).build();

                // Comprobar si el gasto necesita ser actualizado
                boolean b1 = match.channel != null && !match.channel.equals(channel);
                boolean b2 = match.subchannel != null && !match.subchannel.equals(subchannel);
                boolean b3 = match.channel_segment != null && !match.channel_segment.equals(channel_segment);
                boolean b4 = match.format != null && !match.format.equals(format);
                boolean b5 = match.customer_owner != null && !match.customer_owner.equals(customer_owner);
                boolean b6 = match.pos_id != null && !match.pos_id.equals(pos_id);
                boolean b7 = match.pos_name != null && !match.pos_name.equals(pos_name);
                boolean b8 = match.pos_name_dpsm != null && !match.pos_name_dpsm.equals(pos_name_dpsm);
                boolean b9 = match.zone != null && !match.zone.equals(zone);
                boolean b10 = match.region != null && !match.region.equals(region);
                boolean b11 = match.province != null && !match.province.equals(province);
                boolean b12 = match.city != null && !match.city.equals(city);
                boolean b13 = match.address != null && !match.address.equals(address);
                boolean b14 = match.kam != null && !match.kam.equals(kam);
                boolean b15 = match.sales_executive != null && !match.sales_executive.equals(sales_executive);
                boolean b16 = match.merchandising != null && !match.merchandising.equals(merchandising);
                boolean b17 = match.supervisor != null && !match.supervisor.equals(supervisor);
                boolean b18 = match.mercaderista != null && !match.supervisor.equals(mercaderista);
                boolean b19 = match.user != null && !match.user.equals(user);
                boolean b20 = match.dpsm != null && !match.dpsm.equals(dpsm);
                boolean b21 = match.status != null && !match.status.equals(status);
                boolean b22 = match.tipo != null && !match.tipo.equals(tipo);
                boolean b23 = match.latitud != null && !match.latitud.equals(latitud);
                boolean b24 = match.longitud != null && !match.longitud.equals(longitud);
                boolean b25 = match.foto != null && !match.foto.equals(foto);
                boolean b26 = match.segmentacion != null && !match.segmentacion.equals(segmentacion);
                boolean b27 = match.compras != null && !match.compras.equals(compras);
                boolean b28 = match.pass != null && !match.pass.equals(pass);
                boolean b29 = match.numero_controller != null && !match.numero_controller.equals(numero_controller);
                boolean b30 = match.fecha_visita != null && !match.fecha_visita.equals(fecha_visita);
                boolean b31 = match.device_id != null && !match.device_id.equals(device_id);
                boolean b32 = match.perimetro != null && !match.perimetro.equals(perimetro);
                boolean b33 = match.distancia != null && !match.distancia.equals(distancia);
                boolean b34 = match.termometro != null && !match.termometro.equals(termometro);
                boolean b35 = match.hora_inicio != null && !match.hora_inicio.equals(hora_inicio);
                boolean b36 = match.hora_fin != null && !match.hora_fin.equals(hora_fin);


                if (b1 || b2 || b3 || b4 || b5 || b6 || b7 || b8 || b9 || b10 || b11 || b12 || b13 ||
                    b14 || b15 || b16 || b17 || b18 || b19 || b20 || b21 || b22 || b23 || b24 || b25 ||
                    b26 || b27 || b28 || b29 || b30 || b31 || b32 || b33 || b34 || b35 || b36) {

                    Log.i(TAG, "Programando actualización de: " + existingUri);

                    ops.add(ContentProviderOperation.newUpdate(existingUri)
                            .withValue(ContractPharmaValue.Columnas.CHANNEL, match.channel)
                            .withValue(ContractPharmaValue.Columnas.SUBCHANNEL, match.subchannel)
                            .withValue(ContractPharmaValue.Columnas.CHANNEL_SEGMENT, match.channel_segment)
                            .withValue(ContractPharmaValue.Columnas.FORMAT, match.format)
                            .withValue(ContractPharmaValue.Columnas.CUSTOMER_OWNER, match.customer_owner)
                            .withValue(ContractPharmaValue.Columnas.POS_ID, match.pos_id)
                            .withValue(ContractPharmaValue.Columnas.POS_NAME, match.pos_name)
                            .withValue(ContractPharmaValue.Columnas.POS_NAME_DPSM, match.pos_name_dpsm)
                            .withValue(ContractPharmaValue.Columnas.ZONA, match.zone)
                            .withValue(ContractPharmaValue.Columnas.REGION, match.region)
                            .withValue(ContractPharmaValue.Columnas.PROVINCIA, match.province)
                            .withValue(ContractPharmaValue.Columnas.CIUDAD, match.city)
                            .withValue(ContractPharmaValue.Columnas.DIRECCION, match.address)
                            .withValue(ContractPharmaValue.Columnas.KAM, match.kam)
                            .withValue(ContractPharmaValue.Columnas.SALES_EXECUTIVE, match.sales_executive)
                            .withValue(ContractPharmaValue.Columnas.MERCHANDISING, match.merchandising)
                            .withValue(ContractPharmaValue.Columnas.SUPERVISOR, match.supervisor)
                            .withValue(ContractPharmaValue.Columnas.MERCADERISTA, match.mercaderista)
                            .withValue(ContractPharmaValue.Columnas.USER, match.user)
                            .withValue(ContractPharmaValue.Columnas.DPSM, match.dpsm)
                            .withValue(ContractPharmaValue.Columnas.STATUS, match.status)
                            .withValue(ContractPharmaValue.Columnas.TIPO, match.tipo)
                            .withValue(ContractPharmaValue.Columnas.LATITUD, match.latitud)
                            .withValue(ContractPharmaValue.Columnas.LONGITUD, match.longitud)
                            .withValue(ContractPharmaValue.Columnas.FOTO, match.foto)
                            .withValue(ContractPharmaValue.Columnas.SEGMENTACION, match.segmentacion)
                            .withValue(ContractPharmaValue.Columnas.COMPRAS, match.compras)
                            .withValue(ContractPharmaValue.Columnas.PASS, match.pass)
                            .withValue(ContractPharmaValue.Columnas.NUMERO_CONTROLLER, match.numero_controller)
                            .withValue(ContractPharmaValue.Columnas.FECHA_VISITA, match.fecha_visita)
                            .withValue(ContractPharmaValue.Columnas.DEVICE_ID, match.device_id)
                            .withValue(ContractPharmaValue.Columnas.PERIMETRO, match.perimetro)
                            .withValue(ContractPharmaValue.Columnas.DISTANCIA, match.distancia)
                            .withValue(ContractPharmaValue.Columnas.TERMOMETRO, match.termometro)
                            .withValue(ContractPharmaValue.Columnas.HORA_INICIO, match.hora_inicio)
                            .withValue(ContractPharmaValue.Columnas.HORA_FIN, match.hora_fin)
                            .build());
                    syncResult.stats.numUpdates++;
                } else {
                    Log.i(TAG, "No hay acciones para este registro: " + existingUri);
                }
            } else {
                // Debido a que la entrada no existe, es removida de la base de datos
                Uri deleteUri = ContractPharmaValue.CONTENT_URI.buildUpon()
                        .appendPath(id).build();
                Log.i(TAG, "Programando eliminación de: " + deleteUri);
                ops.add(ContentProviderOperation.newDelete(deleteUri).build());
                syncResult.stats.numDeletes++;
            }
        }
        c.close();

        // Insertar items resultantes — marcar PDVs reasignados para pedir nueva entrada
        SharedPreferences spSync = getContext().getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editorSync = spSync.edit();
        for (BasePharmaValue e : expenseMap.values()) {
            Log.i(TAG, "Programando inserción de Tabla BasePharmaValue en Base Local: " + e.id);
            if (e.pos_id != null && !e.pos_id.isEmpty()) {
                editorSync.putBoolean("reasignado_" + e.pos_id, true);
            }
            ops.add(ContentProviderOperation.newInsert(ContractPharmaValue.CONTENT_URI)
                    .withValue(Constantes.ID_REMOTA, e.id) //Esta clave importante!!! ForeignKey del servidor
                    .withValue(ContractPharmaValue.Columnas.CHANNEL, e.channel)
                    .withValue(ContractPharmaValue.Columnas.SUBCHANNEL, e.subchannel)
                    .withValue(ContractPharmaValue.Columnas.CHANNEL_SEGMENT, e.channel_segment)
                    .withValue(ContractPharmaValue.Columnas.FORMAT, e.format)
                    .withValue(ContractPharmaValue.Columnas.CUSTOMER_OWNER, e.customer_owner)
                    .withValue(ContractPharmaValue.Columnas.POS_ID, e.pos_id)
                    .withValue(ContractPharmaValue.Columnas.POS_NAME, e.pos_name)
                    .withValue(ContractPharmaValue.Columnas.POS_NAME_DPSM, e.pos_name_dpsm)
                    .withValue(ContractPharmaValue.Columnas.ZONA, e.zone)
                    .withValue(ContractPharmaValue.Columnas.REGION, e.region)
                    .withValue(ContractPharmaValue.Columnas.PROVINCIA, e.province)
                    .withValue(ContractPharmaValue.Columnas.CIUDAD, e.city)
                    .withValue(ContractPharmaValue.Columnas.DIRECCION, e.address)
                    .withValue(ContractPharmaValue.Columnas.KAM, e.kam)
                    .withValue(ContractPharmaValue.Columnas.SALES_EXECUTIVE, e.sales_executive)
                    .withValue(ContractPharmaValue.Columnas.MERCHANDISING, e.merchandising)
                    .withValue(ContractPharmaValue.Columnas.SUPERVISOR, e.supervisor)
                    .withValue(ContractPharmaValue.Columnas.MERCADERISTA, e.mercaderista)
                    .withValue(ContractPharmaValue.Columnas.USER, e.user)
                    .withValue(ContractPharmaValue.Columnas.DPSM, e.dpsm)
                    .withValue(ContractPharmaValue.Columnas.STATUS, e.status)
                    .withValue(ContractPharmaValue.Columnas.TIPO, e.tipo)
                    .withValue(ContractPharmaValue.Columnas.LATITUD, e.latitud)
                    .withValue(ContractPharmaValue.Columnas.LONGITUD, e.longitud)
                    .withValue(ContractPharmaValue.Columnas.FOTO, e.foto)
                    .withValue(ContractPharmaValue.Columnas.SEGMENTACION, e.segmentacion)
                    .withValue(ContractPharmaValue.Columnas.COMPRAS, e.compras)
                    .withValue(ContractPharmaValue.Columnas.PASS, e.pass)
                    .withValue(ContractPharmaValue.Columnas.NUMERO_CONTROLLER, e.numero_controller)
                    .withValue(ContractPharmaValue.Columnas.FECHA_VISITA, e.fecha_visita)
                    .withValue(ContractPharmaValue.Columnas.DEVICE_ID, e.device_id)
                    .withValue(ContractPharmaValue.Columnas.PERIMETRO, e.perimetro)
                    .withValue(ContractPharmaValue.Columnas.DISTANCIA, e.distancia)
                    .withValue(ContractPharmaValue.Columnas.TERMOMETRO, e.termometro)
                    .withValue(ContractPharmaValue.Columnas.HORA_INICIO, e.hora_inicio)
                    .withValue(ContractPharmaValue.Columnas.HORA_FIN, e.hora_fin)
                    .build());
            syncResult.stats.numInserts++;
        }

        if (syncResult.stats.numInserts > 0 ||
                syncResult.stats.numUpdates > 0 ||
                syncResult.stats.numDeletes > 0) {
            Log.i(TAG, "Aplicando operaciones...");
            try {
                resolver.applyBatch(Constantes.AUTHORITY, ops);
            } catch (RemoteException | OperationApplicationException e) {
                e.printStackTrace();
            }
            editorSync.apply();
            resolver.notifyChange(
                    ContractPharmaValue.CONTENT_URI,
                    null,
                    false);
//            Toast.makeText(getContext(),"Sincronización de BasePharmaValue finalizada.",Toast.LENGTH_SHORT).show();
            Log.i(TAG, "Sincronización de tabla BasePharmaValue finalizada.");
            enviarBroadcast(true, Mensajes.SYNC_FINALIZADA_PDV);

        } else {
            Log.i(TAG, "No se requiere sincronización de tabla BasePharmaValue");
            enviarBroadcast(true, Mensajes.SYNC_NOREQUERIDA + "BasePharmaValue");
        }
    }

    /**
     * PRODUCTOS
     *
     * @param syncResult
     * @param operatorName
     */
    public void realizarSincronizacionLocalFlooring2(final SyncResult syncResult, String operatorName) {
        Log.i(TAG, "Realizando Sincronizacion Local de Flooring." + operatorName);
        String operators = operatorName;
        HashMap<String, String> map = new HashMap<>();// Mapeo previo

        map.put("operators", operators);

        // Crear nuevo objeto Json basado en el mapa
        JSONObject jobject = new JSONObject(map);
        //GET METHOD
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, Constantes.GET_FLOORING, jobject, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        procesarRespuestaGetFlooring2(response, syncResult);
                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                if (error instanceof TimeoutError) {
                                    enviarBroadcast(true, Mensajes.TIME_OUT);
                                } else if (error instanceof NoConnectionError) {
                                    //TODO
                                    enviarBroadcast(true, Mensajes.NO_RED);
                                } else if (error instanceof AuthFailureError) {
                                    //TODO
                                } else if (error instanceof ServerError) {
                                    //TODO
                                    enviarBroadcast(true, Mensajes.SEVER_ERROR);
                                } else if (error instanceof NetworkError) {
                                    //TODO
                                    enviarBroadcast(true, Mensajes.RED_ERROR);
                                } else if (error instanceof ParseError) {
                                    //TODO
                                }
                            }
                        });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(MY_DEFAULT_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);
    }

    private void procesarRespuestaGetFlooring2(JSONObject response, SyncResult syncResult) {
        try {
            // Obtener atributo "estado"
            String estado = response.getString(Constantes.ESTADO);

            switch (estado) {
                case Constantes.SUCCESS: // EXITO
                    actualizarDatosLocalesFlooring2(response, syncResult);
                    break;
                case Constantes.FAILED: // FALLIDO
                    String mensaje = response.getString(Constantes.MENSAJE);
                    enviarBroadcast(true, mensaje);
                    Log.i(TAG, mensaje);
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void actualizarDatosLocalesFlooring2(JSONObject response, SyncResult syncResult) {

        JSONArray loginResult = null;

        try {
            // Obtener array "gastos"
            loginResult = response.getJSONArray(Constantes.FLOORING_RESULT);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Parsear con Gson
        BasePortafolioProductos[] res = gson.fromJson(loginResult != null ? loginResult.toString() : null, BasePortafolioProductos[].class);
        List<BasePortafolioProductos> data = Arrays.asList(res);

        // Lista para recolección de operaciones pendientes
        ArrayList<ContentProviderOperation> ops = new ArrayList<>();

        // Tabla hash para recibir los datos entrantes
        HashMap<String, BasePortafolioProductos> expenseMap = new HashMap<>();
        for (BasePortafolioProductos e : data) {
            expenseMap.put(e.id, e);
        }

        // Consultar registros remotos actuales
        Uri uri = ContractPortafolioProductos.CONTENT_URI;
        String select = Constantes.ID_REMOTA + " IS NOT NULL";
        Cursor c = resolver.query(uri, Projection.PROJECTION_FLOORING, select, null, null);
        assert c != null;

        Log.i(TAG, "Se encontraron " + c.getCount() + " registros locales.");

        // Encontrar datos obsoletos
        String id;
        //String codigo_producto;
        String sector;
        String categoria;
        String subcategoria;
        String segmento;
        String presentacion;
        String variante1;
        String variante2;
        String contenido;
        String sku;
        String marca;
        String fabricante;
        String pvp;
        String cadenas;
        String foto;
        String plataforma;
        String locales;
        String dolar;
        /*String manufacturer;
        String format;*/

        while (c.moveToNext()) {
            syncResult.stats.numEntries++;


            id = c.getString(Projection.COLUMNA_ID_REMOTA);
            //codigo_producto = c.getString(Projection.CODIGO_PRODUCTO);
            sector = c.getString(Projection.SECTOR);
            categoria = c.getString(Projection.CATEGORY);
            subcategoria = c.getString(Projection.SUBCATEGORIA);
            segmento = c.getString(Projection.SEGMENTO);
            presentacion = c.getString(Projection.PRESENTACION);
            variante1 = c.getString(Projection.VARIANTE1);
            variante2 = c.getString(Projection.VARIANTE2);
            contenido = c.getString(Projection.CONTENIDO);
            sku = c.getString(Projection.SKU);
            marca = c.getString(Projection.MARCA);
            fabricante = c.getString(Projection.FABRICANTE);
            pvp = c.getString(Projection.PVP);
            cadenas = c.getString(Projection.CADENAS);
            foto = c.getString(Projection.FOTO);
            plataforma = c.getString(Projection.PLATAFORMA);
            locales = c.getString(Projection.LOCALES);
            dolar = c.getString(Projection.DOLAR);
            /*manufacturer = c.getString(Projection.MANUFACTURER);
            format = c.getString(Projection.FORMAT_PRODUCTO);*/

            BasePortafolioProductos match = expenseMap.get(id);

            if (match != null) {
                // Esta entrada existe, por lo que se remueve del mapeado
                expenseMap.remove(id);

                Uri existingUri = ContractPortafolioProductos.CONTENT_URI.buildUpon()
                        .appendPath(id).build();

                // Comprobar si el gasto necesita ser actualizado
                //boolean b=match.codigo_producto!=null && !match.codigo_producto.equals(codigo_producto);
                boolean b1 = match.sector != null && !match.sector.equals(sector);
                boolean b2 = match.categoria != null && !match.categoria.equals(categoria);
                boolean b3 = match.subcategoria != null && !match.subcategoria.equals(subcategoria);
                boolean b4 = match.segmento != null && !match.segmento.equals(segmento);
                boolean b5 = match.presentacion != null && !match.presentacion.equals(presentacion);
                boolean b6 = match.variante1 != null && !match.variante1.equals(variante1);
                boolean b7 = match.variante2 != null && !match.variante2.equals(variante2);
                boolean b9 = match.contenido != null && !match.contenido.equals(contenido);
                boolean b8 = match.sku != null && !match.sku.equals(sku);
                boolean b10 = match.marca != null && !match.marca.equals(marca);
                boolean b11 = match.fabricante != null && !match.fabricante.equals(fabricante);
                boolean b12 = match.pvp != null && !match.pvp.equals(pvp);
                boolean b13 = match.cadenas != null && !match.cadenas.equals(cadenas);
                boolean b14 = match.foto != null && !match.foto.equals(foto);
                boolean b15 = match.plataforma != null && !match.plataforma.equals(plataforma);
                boolean b16 = match.locales != null && !match.locales.equals(locales);
                boolean b17 = match.dolar != null && !match.dolar.equals(dolar);
                /*boolean b12 = match.manufacturer != null && !match.manufacturer.equals(manufacturer);
                boolean b13 = match.format != null && !match.format.equals(format);*/

                if (b1 || b2 || b3 || b4 || b5 || b6 || b7 || b8 || b9 || b10 || b11 || b12 || b13 || b14 || b15 || b16 || b17)   {// || b12 || b13) {

                    Log.i(TAG, "Programando actualización de: " + existingUri);

                    ops.add(ContentProviderOperation.newUpdate(existingUri)
                            //.withValue(ContractPortafolioProductos.Columnas.CODIGO_PRODUCTO,match.codigo_producto)
                            .withValue(ContractPortafolioProductos.Columnas.SECTOR, match.sector)
                            .withValue(ContractPortafolioProductos.Columnas.CATEGORY, match.categoria)
                            .withValue(ContractPortafolioProductos.Columnas.SUBCATEGORIA, match.subcategoria)
                            .withValue(ContractPortafolioProductos.Columnas.SEGMENTO, match.segmento)
                            .withValue(ContractPortafolioProductos.Columnas.PRESENTACION, match.presentacion)
                            .withValue(ContractPortafolioProductos.Columnas.VARIANTE1, match.variante1)
                            .withValue(ContractPortafolioProductos.Columnas.VARIANTE2, match.variante2)
                            .withValue(ContractPortafolioProductos.Columnas.CONTENIDO, match.contenido)
                            .withValue(ContractPortafolioProductos.Columnas.SKU, match.sku)
                            .withValue(ContractPortafolioProductos.Columnas.MARCA, match.marca)
                            .withValue(ContractPortafolioProductos.Columnas.FABRICANTE, match.fabricante)
                            .withValue(ContractPortafolioProductos.Columnas.PVP, match.pvp)
                            .withValue(ContractPortafolioProductos.Columnas.CADENAS, match.cadenas)
                            .withValue(ContractPortafolioProductos.Columnas.FOTO, match.foto)
                            .withValue(ContractPortafolioProductos.Columnas.PLATAFORMA, match.plataforma)
                            .withValue(ContractPortafolioProductos.Columnas.LOCALES, match.locales)
                            .withValue(ContractPortafolioProductos.Columnas.DOLAR, match.dolar)
                            /*.withValue(ContractPortafolioProductos.Columnas.MANUFACTURER, match.manufacturer)
                            .withValue(ContractPortafolioProductos.Columnas.FORMAT,match.format)*/
                            .build());
                    syncResult.stats.numUpdates++;
                } else {
                    Log.i(TAG, "No hay acciones para este registro: " + existingUri);
                }
            } else {
                // Debido a que la entrada no existe, es removida de la base de datos
                Uri deleteUri = ContractPortafolioProductos.CONTENT_URI.buildUpon()
                        .appendPath(id).build();
                Log.i(TAG, "Programando eliminación de: " + deleteUri);
                ops.add(ContentProviderOperation.newDelete(deleteUri).build());
                syncResult.stats.numDeletes++;
            }
        }
        c.close();

        // Insertar items resultantes
        for (BasePortafolioProductos e : expenseMap.values()) {
            Log.i(TAG, "Programando inserción de Tabla Base_Portafolio_Productos en Base Local: " + e.id);
            ops.add(ContentProviderOperation.newInsert(ContractPortafolioProductos.CONTENT_URI)
                    .withValue(Constantes.ID_REMOTA, e.id) //Esta clave importante!!! ForeignKey del servidor
                    //.withValue(ContractPortafolioProductos.Columnas.CODIGO_PRODUCTO,e.codigo_producto)
                    .withValue(ContractPortafolioProductos.Columnas.SECTOR, e.sector)
                    .withValue(ContractPortafolioProductos.Columnas.CATEGORY, e.categoria)
                    .withValue(ContractPortafolioProductos.Columnas.SUBCATEGORIA, e.subcategoria)
                    .withValue(ContractPortafolioProductos.Columnas.SEGMENTO, e.segmento)
                    .withValue(ContractPortafolioProductos.Columnas.PRESENTACION, e.presentacion)
                    .withValue(ContractPortafolioProductos.Columnas.VARIANTE1, e.variante1)
                    .withValue(ContractPortafolioProductos.Columnas.VARIANTE2, e.variante2)
                    .withValue(ContractPortafolioProductos.Columnas.CONTENIDO, e.contenido)
                    .withValue(ContractPortafolioProductos.Columnas.SKU, e.sku)
                    .withValue(ContractPortafolioProductos.Columnas.MARCA, e.marca)
                    .withValue(ContractPortafolioProductos.Columnas.FABRICANTE, e.fabricante)
                    .withValue(ContractPortafolioProductos.Columnas.PVP, e.pvp)
                    .withValue(ContractPortafolioProductos.Columnas.CADENAS, e.cadenas)
                    .withValue(ContractPortafolioProductos.Columnas.FOTO, e.foto)
                    .withValue(ContractPortafolioProductos.Columnas.PLATAFORMA, e.plataforma)
                    .withValue(ContractPortafolioProductos.Columnas.LOCALES, e.locales)
                    .withValue(ContractPortafolioProductos.Columnas.DOLAR, e.dolar)
                    /*.withValue(ContractPortafolioProductos.Columnas.MANUFACTURER, e.manufacturer)
                    .withValue(ContractPortafolioProductos.Columnas.FORMAT,e.format)*/
                    .build());
            syncResult.stats.numInserts++;
        }

        if (syncResult.stats.numInserts > 0 ||
                syncResult.stats.numUpdates > 0 ||
                syncResult.stats.numDeletes > 0) {
            Log.i(TAG, "Aplicando operaciones...");
            try {
                resolver.applyBatch(Constantes.AUTHORITY, ops);
            } catch (RemoteException | OperationApplicationException e) {
                e.printStackTrace();
            }
            resolver.notifyChange(
                    ContractPortafolioProductos.CONTENT_URI,
                    null,
                    false);
//            Toast.makeText(getContext(),"Sincronización de BasePharmaValue finalizada.",Toast.LENGTH_SHORT).show();
            Log.i(TAG, "Sincronización de tabla Base_Portafolio_Productos finalizada.");
            enviarBroadcast(true, Mensajes.SYNC_FINALIZADA_PROD);

        } else {
            Log.i(TAG, "No se requiere sincronización de tabla Productos");
            enviarBroadcast(true, Mensajes.SYNC_NOREQUERIDA + "Productos");
        }

    }




    /*CAUSALES PRODUCTOS EN MAL ESTADO*/
    public void realizarSincronizacionLocalCausalesProdMalEst(final SyncResult syncResult, String operatorName){
        Log.i(TAG, "Realizando Sincronizacion Local de Causales Productos en mal Estado." + operatorName);
        String operators = operatorName;
        HashMap<String, String> map = new HashMap<>();// Mapeo previo

        map.put("operators", operators);

        // Crear nuevo objeto Json basado en el mapa
        JSONObject jobject = new JSONObject(map);
        //GET METHOD
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, Constantes.GET_CAUSALES_PROD_MAL_EST, jobject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        procesarRespuestaGetCausalesProdMalEst(response, syncResult);
                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                if (error instanceof TimeoutError) {
                                    enviarBroadcast(true, Mensajes.TIME_OUT);
                                } else if (error instanceof NoConnectionError) {
                                    //TODO
                                    enviarBroadcast(true, Mensajes.NO_RED);
                                }else if (error instanceof AuthFailureError) {
                                    //TODO
                                } else if (error instanceof ServerError) {
                                    //TODO
                                    enviarBroadcast(true, Mensajes.SEVER_ERROR);
                                } else if (error instanceof NetworkError) {
                                    //TODO
                                    enviarBroadcast(true, Mensajes.RED_ERROR);
                                } else if (error instanceof ParseError) {
                                    //TODO
                                }
                            }
                        });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);
    }

    private void procesarRespuestaGetCausalesProdMalEst(JSONObject response, SyncResult syncResult) {
        try {
            // Obtener atributo "estado"
            String estado = response.getString(Constantes.ESTADO);

            switch (estado) {
                case Constantes.SUCCESS: // EXITO
                    actualizarDatosLocalesCausalesProdMalEst(response, syncResult);
                    break;
                case Constantes.FAILED: // FALLIDO
                    String mensaje = response.getString(Constantes.MENSAJE);
                    enviarBroadcast(true, mensaje);
                    Log.i(TAG, mensaje);
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void actualizarDatosLocalesCausalesProdMalEst(JSONObject response, SyncResult syncResult) {
        JSONArray loginResult = null;

        try {
            // Obtener array "gastos"
            loginResult = response.getJSONArray(Constantes.CAUSALES_PROD_MAL_EST_RESULT);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Parsear con Gson
        Base_causales_prod_mal_est[] res = gson.fromJson(loginResult != null ? loginResult.toString() : null, Base_causales_prod_mal_est[].class);
        List<Base_causales_prod_mal_est> data = Arrays.asList(res);

        // Lista para recolección de operaciones pendientes
        ArrayList<ContentProviderOperation> ops = new ArrayList<>();

        // Tabla hash para recibir los datos entrantes
        HashMap<String, Base_causales_prod_mal_est> expenseMap = new HashMap<>();
        for (Base_causales_prod_mal_est e : data) {
            expenseMap.put(e.id, e);
        }

        // Consultar registros remotos actuales
        Uri uri = ContractCausalesProdMalEst.CONTENT_URI;
        String select = Constantes.ID_REMOTA + " IS NOT NULL";
        Cursor c = resolver.query(uri, Projection.PROJECTION_CAUSALES_PROD_MAL_EST, select, null, null);
        assert c != null;

        Log.i(TAG, "Se encontraron " + c.getCount() + " registros locales.");

        // Encontrar datos obsoletos
        String id;
        String causal;
        String cantidad_defectuosa;

        while (c.moveToNext()) {
            syncResult.stats.numEntries++;
            id = c.getString(Projection.COLUMNA_ID_REMOTA);
            causal = c.getString(Projection.P_CAUSAL);
            cantidad_defectuosa = c.getString(Projection.P_CANT_DEFECTUOSA);

            Base_causales_prod_mal_est match = expenseMap.get(id);

            if (match != null) {
                // Esta entrada existe, por lo que se remueve del mapeado
                expenseMap.remove(id);

                Uri existingUri = ContractCausalesProdMalEst.CONTENT_URI.buildUpon()
                        .appendPath(id).build();

                // Comprobar si el gasto necesita ser actualizado
                boolean b1=match.causal!=null && !match.causal.equals(causal);
                boolean b2=match.cantidad_defectuosa!=null && !match.cantidad_defectuosa.equals(cantidad_defectuosa);

                if (b1 || b2) {
                    Log.i(TAG, "Programando actualización de: " + existingUri);
                    ops.add(ContentProviderOperation.newUpdate(existingUri)
                            .withValue(ContractCausalesProdMalEst.Columnas.CAUSAL,match.causal)
                            .withValue(ContractCausalesProdMalEst.Columnas.CANTIDAD_DEFECTUOSA,match.cantidad_defectuosa)
                            .build());
                    syncResult.stats.numUpdates++;
                } else {
                    Log.i(TAG, "No hay acciones para este registro: " + existingUri);
                }
            } else {
                // Debido a que la entrada no existe, es removida de la base de datos
                Uri deleteUri = ContractCausalesProdMalEst.CONTENT_URI.buildUpon()
                        .appendPath(id).build();
                Log.i(TAG, "Programando eliminación de: " + deleteUri);
                ops.add(ContentProviderOperation.newDelete(deleteUri).build());
                syncResult.stats.numDeletes++;
            }
        }
        c.close();

        // Insertar items resultantes
        for (Base_causales_prod_mal_est e : expenseMap.values()) {
            Log.i(TAG, "Programando inserción de Tabla Base_causales_prod_mal_est en Base Local: " + e.id);
            ops.add(ContentProviderOperation.newInsert(ContractCausalesProdMalEst.CONTENT_URI)
                    .withValue(Constantes.ID_REMOTA , e.id) //Esta clave importante!!! ForeignKey del servidor
                    .withValue(ContractCausalesProdMalEst.Columnas.CAUSAL,e.causal)
                    .withValue(ContractCausalesProdMalEst.Columnas.CANTIDAD_DEFECTUOSA,e.cantidad_defectuosa)
                    .build());
            syncResult.stats.numInserts++;
        }

        if (syncResult.stats.numInserts > 0 ||
                syncResult.stats.numUpdates > 0 ||
                syncResult.stats.numDeletes > 0) {
            Log.i(TAG, "Aplicando operaciones...");
            try {
                resolver.applyBatch(Constantes.AUTHORITY, ops);
            } catch (RemoteException | OperationApplicationException e) {
                e.printStackTrace();
            }
            resolver.notifyChange(
                    ContractCausalesProdMalEst.CONTENT_URI,
                    null,
                    false);
//            Toast.makeText(getContext(),"Sincronización de Base_pharma_value finalizada.",Toast.LENGTH_SHORT).show();
            Log.i(TAG, "Sincronización de tabla Base_causales_prod_mal_est finalizada.");
            enviarBroadcast(true, Mensajes.SYNC_FINALIZADA_CAUSALES_PROD_MAL_EST);

        } else {
            Log.i(TAG, "No se requiere sincronización de tabla CausalesProdMalEst");
            enviarBroadcast(true, Mensajes.SYNC_NOREQUERIDA + "CausalesProdMalEst");
        }

    }




    /* VERSIONES */
    public void realizarSincronizacionLocalVersiones(final SyncResult syncResult, String operatorName){
        Log.i(TAG, "Realizando Sincronizacion Local de Versiones." + operatorName);
        String operator = operatorName;
        HashMap<String, String> map = new HashMap<>();// Mapeo previo

        map.put("operator", operator);

        // Crear nuevo objeto Json basado en el mapa
        JSONObject jobject = new JSONObject(map);
        //GET METHOD
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, Constantes.GET_VERSIONES, jobject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        procesarRespuestaGetVersiones(response, syncResult);
                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                if (error instanceof TimeoutError) {
                                    enviarBroadcast(true, Mensajes.TIME_OUT);
                                } else if (error instanceof NoConnectionError) {
                                    //TODO
                                    enviarBroadcast(true, Mensajes.NO_RED);
                                }else if (error instanceof AuthFailureError) {
                                    //TODO
                                } else if (error instanceof ServerError) {
                                    //TODO
                                    enviarBroadcast(true, Mensajes.SEVER_ERROR);
                                } else if (error instanceof NetworkError) {
                                    //TODO
                                    enviarBroadcast(true, Mensajes.RED_ERROR);
                                } else if (error instanceof ParseError) {
                                    //TODO
                                }
                            }
                        });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);
    }

    private void procesarRespuestaGetVersiones(JSONObject response, SyncResult syncResult) {
        try {
            // Obtener atributo "estado"
            String estado = response.getString(Constantes.ESTADO);

            switch (estado) {
                case Constantes.SUCCESS: // EXITO
                    actualizarDatosLocalesVersiones(response, syncResult);
                    break;
                case Constantes.FAILED: // FALLIDO
                    String mensaje = response.getString(Constantes.MENSAJE);
                    enviarBroadcast(true, mensaje);
                    Log.i(TAG, mensaje);
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void actualizarDatosLocalesVersiones(JSONObject response, SyncResult syncResult) {
        JSONArray loginResult = null;

        try {
            // Obtener array "gastos"
            loginResult = response.getJSONArray(Constantes.VERSIONES_RESULT);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Parsear con Gson
        Base_versiones[] res = gson.fromJson(loginResult != null ? loginResult.toString() : null, Base_versiones[].class);
        List<Base_versiones> data = Arrays.asList(res);

        // Lista para recolección de operaciones pendientes
        ArrayList<ContentProviderOperation> ops = new ArrayList<>();

        // Tabla hash para recibir los datos entrantes
        HashMap<String, Base_versiones> expenseMap = new HashMap<>();
        for (Base_versiones e : data) {
            expenseMap.put(e.id, e);
        }

        // Consultar registros remotos actuales
        Uri uri = ContractVersiones.CONTENT_URI;
        String select = Constantes.ID_REMOTA + " IS NOT NULL";
        Cursor c = resolver.query(uri, Projection.PROJECTION_VERSIONES, select, null, null);
        assert c != null;

        Log.i(TAG, "Se encontraron " + c.getCount() + " registros locales.");

        // Encontrar datos obsoletos
        String id;
        String fecha_version;
        String link_aplicativo;
        String tamaño;

        while (c.moveToNext()) {
            syncResult.stats.numEntries++;
            id = c.getString(Projection.COLUMNA_ID_REMOTA);
            fecha_version = c.getString(Projection.FECHA_VERSION);
            link_aplicativo = c.getString(Projection.LINK_APLICATIVO);
            tamaño = c.getString(Projection.TAMAÑO);

            Base_versiones match = expenseMap.get(id);

            if (match != null) {
                // Esta entrada existe, por lo que se remueve del mapeado
                expenseMap.remove(id);

                Uri existingUri = ContractVersiones.CONTENT_URI.buildUpon()
                        .appendPath(id).build();

                // Comprobar si el gasto necesita ser actualizado
                boolean b1=match.fecha_version!=null && !match.fecha_version.equals(fecha_version);
                boolean b2=match.link_aplicativo!=null && !match.link_aplicativo.equals(link_aplicativo);
                boolean b3=match.tamaño!=null && !match.tamaño.equals(tamaño);

                if (b1 || b2 || b3) {
                    Log.i(TAG, "Programando actualización de: " + existingUri);
                    ops.add(ContentProviderOperation.newUpdate(existingUri)
                            .withValue(ContractVersiones.Columnas.FECHA_VERSION,match.fecha_version)
                            .withValue(ContractVersiones.Columnas.LINK_APLICATIVO,match.link_aplicativo)
                            .withValue(ContractVersiones.Columnas.TAMAÑO,match.tamaño)
                            .build());
                    syncResult.stats.numUpdates++;
                } else {
                    Log.i(TAG, "No hay acciones para este registro: " + existingUri);
                }
            } else {
                // Debido a que la entrada no existe, es removida de la base de datos
                Uri deleteUri = ContractVersiones.CONTENT_URI.buildUpon()
                        .appendPath(id).build();
                Log.i(TAG, "Programando eliminación de: " + deleteUri);
                ops.add(ContentProviderOperation.newDelete(deleteUri).build());
                syncResult.stats.numDeletes++;
            }
        }
        c.close();

        // Insertar items resultantes
        for (Base_versiones e : expenseMap.values()) {
            Log.i(TAG, "Programando inserción de Tabla Base_versiones en Base Local: " + e.id);
            ops.add(ContentProviderOperation.newInsert(ContractVersiones.CONTENT_URI)
                    .withValue(Constantes.ID_REMOTA , e.id) //Esta clave importante!!! ForeignKey del servidor
                    .withValue(ContractVersiones.Columnas.FECHA_VERSION,e.fecha_version)
                    .withValue(ContractVersiones.Columnas.LINK_APLICATIVO,e.link_aplicativo)
                    .withValue(ContractVersiones.Columnas.TAMAÑO,e.tamaño)
                    .build());
            syncResult.stats.numInserts++;
        }

        if (syncResult.stats.numInserts > 0 ||
                syncResult.stats.numUpdates > 0 ||
                syncResult.stats.numDeletes > 0) {
            Log.i(TAG, "Aplicando operaciones...");
            try {
                resolver.applyBatch(Constantes.AUTHORITY, ops);
            } catch (RemoteException | OperationApplicationException e) {
                e.printStackTrace();
            }
            resolver.notifyChange(
                    ContractVersiones.CONTENT_URI,
                    null,
                    false);
//            Toast.makeText(getContext(),"Sincronización de Base_pharma_value finalizada.",Toast.LENGTH_SHORT).show();
            Log.i(TAG, "Sincronización de tabla Base_versiones finalizada.");
            enviarBroadcast(true, Mensajes.SYNC_FINALIZADA_VERSIONES);

        } else {
            Log.i(TAG, "No se requiere sincronización de tabla Versiones");
            enviarBroadcast(true, Mensajes.SYNC_NOREQUERIDA + "Versiones");
        }

    }





    /* TIEMPO ALMUERZO */

    public void realizarSincronizacionLocalTiempoAlmuerzo(final SyncResult syncResult, String operatorName){
        Log.i(TAG, "Realizando Sincronizacion Local de Tiempo Almuerzo." + operatorName);
        String operator = operatorName;
        HashMap<String, String> map = new HashMap<>();// Mapeo previo

        map.put("operator", operator);

        // Crear nuevo objeto Json basado en el mapa
        JSONObject jobject = new JSONObject(map);
        //GET METHOD
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, Constantes.GET_TIEMPO_ALMUERZO, jobject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        procesarRespuestaGetTiempoAlmuerzo(response, syncResult);
                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                if (error instanceof TimeoutError) {
                                    enviarBroadcast(true, Mensajes.TIME_OUT);
                                } else if (error instanceof NoConnectionError) {
                                    //TODO
                                    enviarBroadcast(true, Mensajes.NO_RED);
                                }else if (error instanceof AuthFailureError) {
                                    //TODO
                                } else if (error instanceof ServerError) {
                                    //TODO
                                    enviarBroadcast(true, Mensajes.SEVER_ERROR);
                                } else if (error instanceof NetworkError) {
                                    //TODO
                                    enviarBroadcast(true, Mensajes.RED_ERROR);
                                } else if (error instanceof ParseError) {
                                    //TODO
                                }
                            }
                        });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);
    }

    private void procesarRespuestaGetTiempoAlmuerzo(JSONObject response, SyncResult syncResult) {
        try {
            // Obtener atributo "estado"
            String estado = response.getString(Constantes.ESTADO);

            switch (estado) {
                case Constantes.SUCCESS: // EXITO
                    actualizarDatosLocalesTiempoAlmuerzo(response, syncResult);
                    break;
                case Constantes.FAILED: // FALLIDO
                    String mensaje = response.getString(Constantes.MENSAJE);
                    enviarBroadcast(true, mensaje);
                    Log.i(TAG, mensaje);
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void actualizarDatosLocalesTiempoAlmuerzo(JSONObject response, SyncResult syncResult) {
        JSONArray loginResult = null;

        try {
            // Obtener array "gastos"
            loginResult = response.getJSONArray(Constantes.TIEMPO_ALMUERZO_RESULT);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Parsear con Gson
        Base_tiempo_almuerzo[] res = gson.fromJson(loginResult != null ? loginResult.toString() : null, Base_tiempo_almuerzo[].class);
        List<Base_tiempo_almuerzo> data = Arrays.asList(res);

        // Lista para recolección de operaciones pendientes
        ArrayList<ContentProviderOperation> ops = new ArrayList<>();

        // Tabla hash para recibir los datos entrantes
        HashMap<String, Base_tiempo_almuerzo> expenseMap = new HashMap<>();
        for (Base_tiempo_almuerzo e : data) {
            expenseMap.put(e.id, e);
        }

        // Consultar registros remotos actuales
        Uri uri = ContractTiempoAlmuerzo.CONTENT_URI;
        String select = Constantes.ID_REMOTA + " IS NOT NULL";
        Cursor c = resolver.query(uri, Projection.PROJECTION_TIEMPO_ALMUERZO, select, null, null);
        assert c != null;

        Log.i(TAG, "Se encontraron " + c.getCount() + " registros locales.");

        // Encontrar datos obsoletos
        String id;
        String tiempo_almuerzo;

        while (c.moveToNext()) {
            syncResult.stats.numEntries++;
            id = c.getString(Projection.COLUMNA_ID_REMOTA);
            tiempo_almuerzo = c.getString(Projection.TIEMPO_ALMUERZO);


            Base_tiempo_almuerzo match = expenseMap.get(id);

            if (match != null) {
                // Esta entrada existe, por lo que se remueve del mapeado
                expenseMap.remove(id);

                Uri existingUri = ContractTiempoAlmuerzo.CONTENT_URI.buildUpon()
                        .appendPath(id).build();

                // Comprobar si el gasto necesita ser actualizado
                boolean b1 = match.tiempo_almuerzo!=null && !match.tiempo_almuerzo.equals(tiempo_almuerzo);

                if (b1) {
                    Log.i(TAG, "Programando actualización de: " + existingUri);
                    ops.add(ContentProviderOperation.newUpdate(existingUri)
                            .withValue(ContractTiempoAlmuerzo.Columnas.TIEMPO_ALMUERZO, match.tiempo_almuerzo)
                            .build());
                    syncResult.stats.numUpdates++;
                } else {
                    Log.i(TAG, "No hay acciones para este registro: " + existingUri);
                }
            } else {
                // Debido a que la entrada no existe, es removida de la base de datos
                Uri deleteUri = ContractTiempoAlmuerzo.CONTENT_URI.buildUpon()
                        .appendPath(id).build();
                Log.i(TAG, "Programando eliminación de: " + deleteUri);
                ops.add(ContentProviderOperation.newDelete(deleteUri).build());
                syncResult.stats.numDeletes++;
            }
        }
        c.close();

        // Insertar items resultantes
        for (Base_tiempo_almuerzo e : expenseMap.values()) {
            Log.i(TAG, "Programando inserción de Tabla Base_tipo_almuerzo en Base Local: " + e.id);
            ops.add(ContentProviderOperation.newInsert(ContractTiempoAlmuerzo.CONTENT_URI)
                    .withValue(Constantes.ID_REMOTA , e.id) //Esta clave importante!!! ForeignKey del servidor
                    .withValue(ContractTiempoAlmuerzo.Columnas.TIEMPO_ALMUERZO, e.tiempo_almuerzo)
                    .build());
            syncResult.stats.numInserts++;
        }

        if (syncResult.stats.numInserts > 0 ||
                syncResult.stats.numUpdates > 0 ||
                syncResult.stats.numDeletes > 0) {
            Log.i(TAG, "Aplicando operaciones...");
            try {
                resolver.applyBatch(Constantes.AUTHORITY, ops);
            } catch (RemoteException | OperationApplicationException e) {
                e.printStackTrace();
            }
            resolver.notifyChange(
                    ContractTiempoAlmuerzo.CONTENT_URI,
                    null,
                    false);
//            Toast.makeText(getContext(),"Sincronización de Base_pharma_value finalizada.",Toast.LENGTH_SHORT).show();
            Log.i(TAG, "Sincronización de tabla Tiempo Almuerzo finalizada.");
            enviarBroadcast(true, Mensajes.SYNC_FINALIZADA_TIEMPO_ALMUERZO);

        } else {
            Log.i(TAG, "No se requiere sincronización de tabla Tiempo Almuerzo");
            enviarBroadcast(true, Mensajes.SYNC_NOREQUERIDA + "Tiempo Almuerzo");
        }
    }







    /**
     * PRECIOS PVC
     *
     * @param syncResult
     * @param operatorName
     */




    /**
     * PREGUNTAS
     *
     * @param syncResult
     * @param operatorName
     */
    public void realizarSincronizacionLocalPreguntas(final SyncResult syncResult, String operatorName) {
        Log.i(TAG, "Realizando Sincronizacion Local de Preguntas." + operatorName);
        String operators = operatorName;
        HashMap<String, String> map = new HashMap<>();// Mapeo previo

        map.put("operators", operators);

        // Crear nuevo objeto Json basado en el mapa
        JSONObject jobject = new JSONObject(map);
        //GET METHOD
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, Constantes.GET_PREGUNTAS, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        procesarRespuestaGetPreguntas(response, syncResult);
                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                if (error instanceof TimeoutError) {
                                    enviarBroadcast(true, Mensajes.TIME_OUT);
                                } else if (error instanceof NoConnectionError) {
                                    //TODO
                                    enviarBroadcast(true, Mensajes.NO_RED);
                                } else if (error instanceof AuthFailureError) {
                                    //TODO
                                } else if (error instanceof ServerError) {
                                    //TODO
                                    enviarBroadcast(true, Mensajes.SEVER_ERROR);
                                } else if (error instanceof NetworkError) {
                                    //TODO
                                    enviarBroadcast(true, Mensajes.RED_ERROR);
                                } else if (error instanceof ParseError) {
                                    //TODO
                                }

                                Log.i("error",""+error.getMessage());
                            }
                        });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(MY_DEFAULT_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);
    }

    private void procesarRespuestaGetPreguntas(JSONObject response, SyncResult syncResult) {
        try {
            // Obtener atributo "estado"
            String estado = response.getString(Constantes.ESTADO);

            switch (estado) {
                case Constantes.SUCCESS: // EXITO
                    actualizarDatosLocalesPreguntas(response, syncResult);
                    break;
                case Constantes.FAILED: // FALLIDO
                    String mensaje = response.getString(Constantes.MENSAJE);
                    enviarBroadcast(true, mensaje);
                    Log.i(TAG, mensaje);
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void actualizarDatosLocalesPreguntas(JSONObject response, SyncResult syncResult) {
        JSONArray loginResult = null;

        try {
            // Obtener array "gastos"
            loginResult = response.getJSONArray(Constantes.PREGUNTAS_RESULT);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Parsear con Gson
        Base_preguntas[] res = gson.fromJson(loginResult != null ? loginResult.toString() : null, Base_preguntas[].class);
        List<Base_preguntas> data = Arrays.asList(res);

        // Lista para recolección de operaciones pendientes
        ArrayList<ContentProviderOperation> ops = new ArrayList<>();

        // Tabla hash para recibir los datos entrantes
        HashMap<String, Base_preguntas> expenseMap = new HashMap<>();
        for (Base_preguntas e : data) {
            expenseMap.put(e.id, e);
        }

        // Consultar registros remotos actuales
        Uri uri = ContractPreguntas.CONTENT_URI;
        String select = Constantes.ID_REMOTA + " IS NOT NULL";
        Cursor c = resolver.query(uri, Projection.PROJECTION_PREGUNTAS, select, null, null);
        assert c != null;

        Log.i(TAG, "Se encontraron " + c.getCount() + " registros locales.");

        // Encontrar datos obsoletos
        String id;
        //String codigo_producto;
        String question;
        String answer;
        String opta;
        String optb;
        String optc;
        String canal;
        String tiempo;
        String test_id;


        while (c.moveToNext()) {

            syncResult.stats.numEntries++;
            id = c.getString(Projection.COLUMNA_ID_REMOTA);
            //codigo_producto = c.getString(Projection.CODIGO_PRODUCTO);
            question = c.getString(Projection.QUESTION);
            answer = c.getString(Projection.ANSWER);
            opta = c.getString(Projection.OPTA);
            optb = c.getString(Projection.OPTB);
            optc = c.getString(Projection.OPTC);
            canal = c.getString(Projection.QCANAL);
            tiempo = c.getString(Projection.QTIEMPO);
            test_id = c.getString(Projection.QTEST_ID);


            Base_preguntas match = expenseMap.get(id);

            if (match != null) {
                // Esta entrada existe, por lo que se remueve del mapeado
                expenseMap.remove(id);

                Uri existingUri = ContractPreguntas.CONTENT_URI.buildUpon()
                        .appendPath(id).build();

                // Comprobar si el gasto necesita ser actualizado
                //boolean b=match.codigo_producto!=null && !match.codigo_producto.equals(codigo_producto);
                boolean b2=  match.question!=null && !match.question.equals(question);
                boolean b3=  match.answer!=null && !match.answer.equals(answer);
                boolean b4=  match.opta!=null && !match.opta.equals(opta);
                boolean b5=  match.optb!=null && !match.optb.equals(optb);
                boolean b6 = match.optc != null && !match.optc.equals(optc);
                boolean b7 = match.canal != null && !match.canal.equals(canal);
                boolean b8 = match.tiempo != null && !match.tiempo.equals(tiempo);
                boolean b9 = match.test_id != null && !match.test_id.equals(test_id);

                if (b2 || b3 || b4 || b5 || b6 || b7 || b8 || b9) {

                    Log.i(TAG, "Programando actualización de: " + existingUri);

                    ops.add(ContentProviderOperation.newUpdate(existingUri)
                            //.withValue(ContractPortafolioProductos.Columnas.CODIGO_PRODUCTO,match.codigo_producto)
                            .withValue(ContractPreguntas.Columnas.KEY_QUES,match.question)
                            .withValue(ContractPreguntas.Columnas.KEY_ANSWER,match.answer)
                            .withValue(ContractPreguntas.Columnas.KEY_OPTA,match.opta)
                            .withValue(ContractPreguntas.Columnas.KEY_OPTB,match.optb)
                            .withValue(ContractPreguntas.Columnas.KEY_OPTC, match.optc)
                            .withValue(ContractPreguntas.Columnas.KEY_CANAL, match.canal)
                            .withValue(ContractPreguntas.Columnas.KEY_TIEMPO, match.tiempo)
                            .withValue(ContractPreguntas.Columnas.KEY_TEST_ID, match.test_id)
                            .build());
                    syncResult.stats.numUpdates++;
                } else {
                    Log.i(TAG, "No hay acciones para este registro: " + existingUri);
                }
            } else {
                // Debido a que la entrada no existe, es removida de la base de datos
                Uri deleteUri = ContractPreguntas.CONTENT_URI.buildUpon()
                        .appendPath(id).build();
                Log.i(TAG, "Programando eliminación de: " + deleteUri);
                ops.add(ContentProviderOperation.newDelete(deleteUri).build());
                syncResult.stats.numDeletes++;
            }
        }
        c.close();

        // Insertar items resultantes
        for (Base_preguntas e : expenseMap.values()) {
            Log.i(TAG, "Programando inserción de Tabla Base_Preguntas en Base Local: " + e.id);
            ops.add(ContentProviderOperation.newInsert(ContractPreguntas.CONTENT_URI)
                    .withValue(Constantes.ID_REMOTA , e.id) //Esta clave importante!!! ForeignKey del servidor
                    //.withValue(ContractPortafolioProductos.Columnas.CODIGO_PRODUCTO,e.codigo_producto)
                    .withValue(ContractPreguntas.Columnas.KEY_QUES,e.question)
                    .withValue(ContractPreguntas.Columnas.KEY_ANSWER,e.answer)
                    .withValue(ContractPreguntas.Columnas.KEY_OPTA,e.opta)
                    .withValue(ContractPreguntas.Columnas.KEY_OPTB,e.optb)
                    .withValue(ContractPreguntas.Columnas.KEY_OPTC, e.optc)
                    .withValue(ContractPreguntas.Columnas.KEY_CANAL, e.canal)
                    .withValue(ContractPreguntas.Columnas.KEY_TIEMPO, e.tiempo)
                    .withValue(ContractPreguntas.Columnas.KEY_TEST_ID, e.test_id)
                    .build());
            syncResult.stats.numInserts++;
        }

        if (syncResult.stats.numInserts > 0 ||
                syncResult.stats.numUpdates > 0 ||
                syncResult.stats.numDeletes > 0) {
            Log.i(TAG, "Aplicando operaciones...");
            try {
                resolver.applyBatch(Constantes.AUTHORITY, ops);
            } catch (RemoteException | OperationApplicationException e) {
                e.printStackTrace();
            }
            resolver.notifyChange(
                    ContractPreguntas.CONTENT_URI,
                    null,
                    false);
//            Toast.makeText(getContext(),"Sincronización de Base_pharma_value finalizada.",Toast.LENGTH_SHORT).show();
            Log.i(TAG, "Sincronización de tabla Base_Preguntas finalizada.");
            enviarBroadcast(true, Mensajes.SYNC_FINALIZADA_PREG);

        } else {
            Log.i(TAG, "No se requiere sincronización de tabla Preguntas");
            enviarBroadcast(true, Mensajes.SYNC_NOREQUERIDA + "Preguntas");
        }

    }



    /* JUSTIFICACIONES */
    public void realizarSincronizacionLocalJustificacion(final SyncResult syncResult, String operatorName){
        Log.i(TAG, "Realizando Sincronizacion Local de Justificacion." + operatorName);
        String operators = operatorName;
        HashMap<String, String> map = new HashMap<>();// Mapeo previo

        map.put("operators", operators);

        // Crear nuevo objeto Json basado en el mapa
        JSONObject jobject = new JSONObject(map);
        //GET METHOD
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, Constantes.GET_JUSTIFICACION, jobject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        procesarRespuestaGetJustificacion(response, syncResult);
                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                if (error instanceof TimeoutError) {
                                    enviarBroadcast(true, Mensajes.TIME_OUT);
                                } else if (error instanceof NoConnectionError) {
                                    //TODO
                                    enviarBroadcast(true, Mensajes.NO_RED);
                                }else if (error instanceof AuthFailureError) {
                                    //TODO
                                } else if (error instanceof ServerError) {
                                    //TODO
                                    enviarBroadcast(true, Mensajes.SEVER_ERROR);
                                } else if (error instanceof NetworkError) {
                                    //TODO
                                    enviarBroadcast(true, Mensajes.RED_ERROR);
                                } else if (error instanceof ParseError) {
                                    //TODO
                                }
                            }
                        });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);
    }

    private void procesarRespuestaGetJustificacion(JSONObject response, SyncResult syncResult) {
        try {
            // Obtener atributo "estado"
            String estado = response.getString(Constantes.ESTADO);

            switch (estado) {
                case Constantes.SUCCESS: // EXITO
                    actualizarDatosLocalesJustificacion(response, syncResult);
                    break;
                case Constantes.FAILED: // FALLIDO
                    String mensaje = response.getString(Constantes.MENSAJE);
                    enviarBroadcast(true, mensaje);
                    Log.i(TAG, mensaje);
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void actualizarDatosLocalesJustificacion(JSONObject response, SyncResult syncResult) {
        JSONArray loginResult = null;

        try {
            // Obtener array "gastos"
            loginResult = response.getJSONArray(Constantes.JUSTIFICACIONESRESULT);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Parsear con Gson
        Base_justificacion[] res = gson.fromJson(loginResult != null ? loginResult.toString() : null, Base_justificacion[].class);
        List<Base_justificacion> data = Arrays.asList(res);

        // Lista para recolección de operaciones pendientes
        ArrayList<ContentProviderOperation> ops = new ArrayList<>();

        // Tabla hash para recibir los datos entrantes
        HashMap<String, Base_justificacion> expenseMap = new HashMap<>();
        for (Base_justificacion e : data) {
            expenseMap.put(e.id, e);
        }

        // Consultar registros remotos actuales
        Uri uri = ContractJustificacion.CONTENT_URI;
        String select = Constantes.ID_REMOTA + " IS NOT NULL";
        Cursor c = resolver.query(uri, Projection.PROJECTION_JUSTIFICACION, select, null, null);
        assert c != null;

        Log.i(TAG, "Se encontraron " + c.getCount() + " registros locales.");

        // Encontrar datos obsoletos
        String id;
        //String codigo_producto;
        String justificacion;


        while (c.moveToNext()) {
            syncResult.stats.numEntries++;
            id = c.getString(Projection.COLUMNA_ID_REMOTA);
            //codigo_producto = c.getString(Projection.CODIGO_PRODUCTO);
            justificacion = c.getString(Projection.JUSTIFICACION);


            Base_justificacion match = expenseMap.get(id);

            if (match != null) {
                // Esta entrada existe, por lo que se remueve del mapeado
                expenseMap.remove(id);

                Uri existingUri = ContractJustificacion.CONTENT_URI.buildUpon()
                        .appendPath(id).build();

                // Comprobar si el gasto necesita ser actualizado
                //boolean b=match.codigo_producto!=null && !match.codigo_producto.equals(codigo_producto);
                boolean b1=match.justificacion!=null && !match.justificacion.equals(justificacion);

                if (b1) {

                    Log.i(TAG, "Programando actualización de: " + existingUri);

                    ops.add(ContentProviderOperation.newUpdate(existingUri)
                            //.withValue(ContractPortafolioProductos.Columnas.CODIGO_PRODUCTO,match.codigo_producto)
                            .withValue(ContractJustificacion.Columnas.JUSTIFICACION,match.justificacion)
                            .build());
                    syncResult.stats.numUpdates++;
                } else {
                    Log.i(TAG, "No hay acciones para este registro: " + existingUri);
                }
            } else {
                // Debido a que la entrada no existe, es removida de la base de datos
                Uri deleteUri = ContractJustificacion.CONTENT_URI.buildUpon()
                        .appendPath(id).build();
                Log.i(TAG, "Programando eliminación de: " + deleteUri);
                ops.add(ContentProviderOperation.newDelete(deleteUri).build());
                syncResult.stats.numDeletes++;
            }
        }
        c.close();

        // Insertar items resultantes
        for (Base_justificacion e : expenseMap.values()) {
            Log.i(TAG, "Programando inserción de Tabla Base_justificacion en Base Local: " + e.id);
            ops.add(ContentProviderOperation.newInsert(ContractJustificacion.CONTENT_URI)
                    .withValue(Constantes.ID_REMOTA , e.id) //Esta clave importante!!! ForeignKey del servidor
                    //.withValue(ContractPortafolioProductos.Columnas.CODIGO_PRODUCTO,e.codigo_producto)
                    .withValue(ContractJustificacion.Columnas.JUSTIFICACION,e.justificacion)
                    .build());
            syncResult.stats.numInserts++;
        }

        if (syncResult.stats.numInserts > 0 ||
                syncResult.stats.numUpdates > 0 ||
                syncResult.stats.numDeletes > 0) {
            Log.i(TAG, "Aplicando operaciones...");
            try {
                resolver.applyBatch(Constantes.AUTHORITY, ops);
            } catch (RemoteException | OperationApplicationException e) {
                e.printStackTrace();
            }
            resolver.notifyChange(
                    ContractJustificacion.CONTENT_URI,
                    null,
                    false);
//            Toast.makeText(getContext(),"Sincronización de Base_pharma_value finalizada.",Toast.LENGTH_SHORT).show();
            Log.i(TAG, "Sincronización de tabla Base_Justificacion finalizada.");
            enviarBroadcast(true, Mensajes.SYNC_FINALIZADA_JUST);

        } else {
            Log.i(TAG, "No se requiere sincronización de tabla Justificación");
            enviarBroadcast(true, Mensajes.SYNC_NOREQUERIDA + "Justificación");
        }

    }


    /* MARCAS BLANCAS */

    public void realizarSincronizacionLocalMarcasBlancas(final SyncResult syncResult, String operatorName){
        Log.i(TAG, "Realizando Sincronizacion Local de Marcas_Blancas." + operatorName);
        String operators = operatorName;
        HashMap<String, String> map = new HashMap<>();// Mapeo previo

        map.put("operators", operators);

        // Crear nuevo objeto Json basado en el mapa
        JSONObject jobject = new JSONObject(map);
        //GET METHOD
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, Constantes.GET_MARCAS_BLANCAS, jobject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        procesarRespuestaGetMarcasBlancas(response, syncResult);
                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                if (error instanceof TimeoutError) {
                                    enviarBroadcast(true, Mensajes.TIME_OUT);
                                } else if (error instanceof NoConnectionError) {
                                    //TODO
                                    enviarBroadcast(true, Mensajes.NO_RED);
                                }else if (error instanceof AuthFailureError) {
                                    //TODO
                                } else if (error instanceof ServerError) {
                                    //TODO
                                    enviarBroadcast(true, Mensajes.SEVER_ERROR);
                                } else if (error instanceof NetworkError) {
                                    //TODO
                                    enviarBroadcast(true, Mensajes.RED_ERROR);
                                } else if (error instanceof ParseError) {
                                    //TODO
                                }
                            }
                        });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);
    }

    private void procesarRespuestaGetMarcasBlancas(JSONObject response, SyncResult syncResult) {
        try {
            // Obtener atributo "estado"
            String estado = response.getString(Constantes.ESTADO);

            switch (estado) {
                case Constantes.SUCCESS: // EXITO
                    actualizarDatosLocalesMarcasBlancas(response, syncResult);
                    break;
                case Constantes.FAILED: // FALLIDO
                    String mensaje = response.getString(Constantes.MENSAJE);
                    enviarBroadcast(true, mensaje);
                    Log.i(TAG, mensaje);
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void actualizarDatosLocalesMarcasBlancas(JSONObject response, SyncResult syncResult) {
        JSONArray loginResult = null;

        try {
            // Obtener array "gastos"
            loginResult = response.getJSONArray(Constantes.TIPO_MARCAS_RESULT);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Parsear con Gson
        Base_marcasBlancas[] res = gson.fromJson(loginResult != null ? loginResult.toString() : null, Base_marcasBlancas[].class);
        List<Base_marcasBlancas> data = Arrays.asList(res);

        // Lista para recolección de operaciones pendientes
        ArrayList<ContentProviderOperation> ops = new ArrayList<>();

        // Tabla hash para recibir los datos entrantes
        HashMap<String, Base_marcasBlancas> expenseMap = new HashMap<>();
        for (Base_marcasBlancas e : data) {
            expenseMap.put(e.id, e);
        }

        // Consultar registros remotos actuales
        Uri uri = ContractMarcasBlancas.CONTENT_URI;
        String select = Constantes.ID_REMOTA + " IS NOT NULL";
        Cursor c = resolver.query(uri, Projection.PROJECTION_MARCAS_BLANCAS, select, null, null);
        assert c != null;

        Log.i(TAG, "Se encontraron " + c.getCount() + " registros locales.");

        // Encontrar datos obsoletos
        String id;
        //String codigo_producto;
        String marcas;


        while (c.moveToNext()) {
            syncResult.stats.numEntries++;
            id = c.getString(Projection.COLUMNA_ID_REMOTA);
            //codigo_producto = c.getString(Projection.CODIGO_PRODUCTO);
            marcas = c.getString(Projection.MARCAS);


            Base_marcasBlancas match = expenseMap.get(id);

            if (match != null) {
                // Esta entrada existe, por lo que se remueve del mapeado
                expenseMap.remove(id);

                Uri existingUri = ContractMarcasBlancas.CONTENT_URI.buildUpon()
                        .appendPath(id).build();

                // Comprobar si el gasto necesita ser actualizado
                //boolean b=match.codigo_producto!=null && !match.codigo_producto.equals(codigo_producto);
                boolean b1=match.marcas !=null && !match.marcas.equals(marcas);

                if (b1) {

                    Log.i(TAG, "Programando actualización de: " + existingUri);

                    ops.add(ContentProviderOperation.newUpdate(existingUri)
                            //.withValue(ContractPortafolioProductos.Columnas.CODIGO_PRODUCTO,match.codigo_producto)
                            .withValue(ContractMarcasBlancas.Columnas.MARCAS,match.marcas)
                            .build());
                    syncResult.stats.numUpdates++;
                } else {
                    Log.i(TAG, "No hay acciones para este registro: " + existingUri);
                }
            } else {
                // Debido a que la entrada no existe, es removida de la base de datos
                Uri deleteUri = ContractMarcasBlancas.CONTENT_URI.buildUpon()
                        .appendPath(id).build();
                Log.i(TAG, "Programando eliminación de: " + deleteUri);
                ops.add(ContentProviderOperation.newDelete(deleteUri).build());
                syncResult.stats.numDeletes++;
            }
        }
        c.close();

        // Insertar items resultantes
        for (Base_marcasBlancas e : expenseMap.values()) {
            Log.i(TAG, "Programando inserción de Tabla Base_marcasBlancas en Base Local: " + e.id);
            ops.add(ContentProviderOperation.newInsert(ContractMarcasBlancas.CONTENT_URI)
                    .withValue(Constantes.ID_REMOTA , e.id) //Esta clave importante!!! ForeignKey del servidor
                    //.withValue(ContractPortafolioProductos.Columnas.CODIGO_PRODUCTO,e.codigo_producto)
                    .withValue(ContractMarcasBlancas.Columnas.MARCAS,e.marcas)
                    .build());
            syncResult.stats.numInserts++;
        }

        if (syncResult.stats.numInserts > 0 ||
                syncResult.stats.numUpdates > 0 ||
                syncResult.stats.numDeletes > 0) {
            Log.i(TAG, "Aplicando operaciones...");
            try {
                resolver.applyBatch(Constantes.AUTHORITY, ops);
            } catch (RemoteException | OperationApplicationException e) {
                e.printStackTrace();
            }
            resolver.notifyChange(
                    ContractMarcasBlancas.CONTENT_URI,
                    null,
                    false);
//            Toast.makeText(getContext(),"Sincronización de Base_pharma_value finalizada.",Toast.LENGTH_SHORT).show();
            Log.i(TAG, "Sincronización de tabla Base_marcasBlancas finalizada.");
            enviarBroadcast(true, Mensajes.SYNC_FINALIZADA_MARCAS_BLANCAS);

        } else {
            Log.i(TAG, "No se requiere sincronización de tabla Marcas Blancas");
            enviarBroadcast(true, Mensajes.SYNC_NOREQUERIDA + "Marcas_blancas");
        }

    }



    /* TIPO PRECIOS */

    public void realizarSincronizacionLocalTipoPrecios(final SyncResult syncResult, String operatorName){
        Log.i(TAG, "Realizando Sincronizacion Local de Tipo_precios." + operatorName);
        String operators = operatorName;
        HashMap<String, String> map = new HashMap<>();// Mapeo previo

        map.put("operators", operators);

        // Crear nuevo objeto Json basado en el mapa
        JSONObject jobject = new JSONObject(map);
        //GET METHOD
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, Constantes.GET_TIPO_PRECIOS, jobject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        procesarRespuestaGetTipoPrecios(response, syncResult);
                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                if (error instanceof TimeoutError) {
                                    enviarBroadcast(true, Mensajes.TIME_OUT);
                                } else if (error instanceof NoConnectionError) {
                                    //TODO
                                    enviarBroadcast(true, Mensajes.NO_RED);
                                }else if (error instanceof AuthFailureError) {
                                    //TODO
                                } else if (error instanceof ServerError) {
                                    //TODO
                                    enviarBroadcast(true, Mensajes.SEVER_ERROR);
                                } else if (error instanceof NetworkError) {
                                    //TODO
                                    enviarBroadcast(true, Mensajes.RED_ERROR);
                                } else if (error instanceof ParseError) {
                                    //TODO
                                }
                            }
                        });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);
    }

    private void procesarRespuestaGetTipoPrecios(JSONObject response, SyncResult syncResult) {
        try {
            // Obtener atributo "estado"
            String estado = response.getString(Constantes.ESTADO);

            switch (estado) {
                case Constantes.SUCCESS: // EXITO
                    actualizarDatosLocalesTipoPrecios(response, syncResult);
                    break;
                case Constantes.FAILED: // FALLIDO
                    String mensaje = response.getString(Constantes.MENSAJE);
                    enviarBroadcast(true, mensaje);
                    Log.i(TAG, mensaje);
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void actualizarDatosLocalesTipoPrecios(JSONObject response, SyncResult syncResult) {
        JSONArray loginResult = null;

        try {
            // Obtener array "gastos"
            loginResult = response.getJSONArray(Constantes.TIPO_PRECIOS_RESULT);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Parsear con Gson
        Base_tipoPrecios[] res = gson.fromJson(loginResult != null ? loginResult.toString() : null, Base_tipoPrecios[].class);
        List<Base_tipoPrecios> data = Arrays.asList(res);

        // Lista para recolección de operaciones pendientes
        ArrayList<ContentProviderOperation> ops = new ArrayList<>();

        // Tabla hash para recibir los datos entrantes
        HashMap<String, Base_tipoPrecios> expenseMap = new HashMap<>();
        for (Base_tipoPrecios e : data) {
            expenseMap.put(e.id, e);
        }

        // Consultar registros remotos actuales
        Uri uri = ContractTipoPrecios.CONTENT_URI;
        String select = Constantes.ID_REMOTA + " IS NOT NULL";
        Cursor c = resolver.query(uri, Projection.PROJECTION_TIPO_PRECIOS, select, null, null);
        assert c != null;

        Log.i(TAG, "Se encontraron " + c.getCount() + " registros locales.");

        // Encontrar datos obsoletos
        String id;
        //String codigo_producto;
        String canal;
        String subcanal;
        String tipo_precio;


        while (c.moveToNext()) {
            syncResult.stats.numEntries++;
            id = c.getString(Projection.COLUMNA_ID_REMOTA);
            //codigo_producto = c.getString(Projection.CODIGO_PRODUCTO);
            canal = c.getString(Projection.TIPO_PRECIO);
            subcanal = c.getString(Projection.TIPO_PRECIO);
            tipo_precio = c.getString(Projection.TIPO_PRECIO);


            Base_tipoPrecios match = expenseMap.get(id);

            if (match != null) {
                // Esta entrada existe, por lo que se remueve del mapeado
                expenseMap.remove(id);

                Uri existingUri = ContractTipoPrecios.CONTENT_URI.buildUpon()
                        .appendPath(id).build();

                // Comprobar si el gasto necesita ser actualizado
                //boolean b=match.codigo_producto!=null && !match.codigo_producto.equals(codigo_producto);
                boolean b1=match.canal !=null && !match.canal.equals(canal);
                boolean b2=match.subcanal !=null && !match.subcanal.equals(subcanal);
                boolean b3=match.tipo !=null && !match.tipo.equals(tipo_precio);

                if (b1) {

                    Log.i(TAG, "Programando actualización de: " + existingUri);

                    ops.add(ContentProviderOperation.newUpdate(existingUri)
                            //.withValue(ContractPortafolioProductos.Columnas.CODIGO_PRODUCTO,match.codigo_producto)
                            .withValue(ContractTipoPrecios.Columnas.CANAL,match.canal)
                            .withValue(ContractTipoPrecios.Columnas.SUBCANAL,match.subcanal)
                            .withValue(ContractTipoPrecios.Columnas.TIPO_PRECIO,match.tipo)
                            .build());
                    syncResult.stats.numUpdates++;
                } else {
                    Log.i(TAG, "No hay acciones para este registro: " + existingUri);
                }
            } else {
                // Debido a que la entrada no existe, es removida de la base de datos
                Uri deleteUri = ContractTipoPrecios.CONTENT_URI.buildUpon()
                        .appendPath(id).build();
                Log.i(TAG, "Programando eliminación de: " + deleteUri);
                ops.add(ContentProviderOperation.newDelete(deleteUri).build());
                syncResult.stats.numDeletes++;
            }
        }
        c.close();

        // Insertar items resultantes
        for (Base_tipoPrecios e : expenseMap.values()) {
            Log.i(TAG, "Programando inserción de Tabla Base_tipoPrecios en Base Local: " + e.id);
            ops.add(ContentProviderOperation.newInsert(ContractTipoPrecios.CONTENT_URI)
                    .withValue(Constantes.ID_REMOTA , e.id) //Esta clave importante!!! ForeignKey del servidor
                    //.withValue(ContractPortafolioProductos.Columnas.CODIGO_PRODUCTO,e.codigo_producto)
                    .withValue(ContractTipoPrecios.Columnas.CANAL,e.canal)
                    .withValue(ContractTipoPrecios.Columnas.SUBCANAL,e.subcanal)
                    .withValue(ContractTipoPrecios.Columnas.TIPO_PRECIO,e.tipo)
                    .build());
            syncResult.stats.numInserts++;
        }

        if (syncResult.stats.numInserts > 0 ||
                syncResult.stats.numUpdates > 0 ||
                syncResult.stats.numDeletes > 0) {
            Log.i(TAG, "Aplicando operaciones...");
            try {
                resolver.applyBatch(Constantes.AUTHORITY, ops);
            } catch (RemoteException | OperationApplicationException e) {
                e.printStackTrace();
            }
            resolver.notifyChange(
                    ContractTipoPrecios.CONTENT_URI,
                    null,
                    false);
//            Toast.makeText(getContext(),"Sincronización de Base_pharma_value finalizada.",Toast.LENGTH_SHORT).show();
            Log.i(TAG, "Sincronización de tabla Base_tipoPrecios finalizada.");
            enviarBroadcast(true, Mensajes.SYNC_FINALIZADA_TIPO_PRECIO);

        } else {
            Log.i(TAG, "No se requiere sincronización de tabla Tipo Precios");
            enviarBroadcast(true, Mensajes.SYNC_NOREQUERIDA + "Tipo_precios");
        }

    }



    public void realizarSincronizacionLocalPreciosPvc(final SyncResult syncResult, String operatorName) {
        Log.i(TAG, "Realizando Sincronizacion Local de Precios PVC." + operatorName);
        String operator = operatorName;
        HashMap<String, String> map = new HashMap<>();// Mapeo previo

        map.put("usuario", operator);



        // Crear nuevo objeto Json basado en el mapa
        JSONObject jobject = new JSONObject(map);
        //GET METHOD
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST,Constantes.GET_PRODUCTOS_PVC, jobject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        procesarRespuestaGetPreciosPvc(response, syncResult);
                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                if (error instanceof TimeoutError) {
                                    enviarBroadcast(true, Mensajes.TIME_OUT);
                                } else if (error instanceof NoConnectionError) {
                                    //TODO
                                    enviarBroadcast(true, Mensajes.NO_RED);
                                } else if (error instanceof AuthFailureError) {
                                    //TODO
                                } else if (error instanceof ServerError) {
                                    //TODO
                                    enviarBroadcast(true, Mensajes.SEVER_ERROR);
                                } else if (error instanceof NetworkError) {
                                    //TODO
                                    enviarBroadcast(true, Mensajes.RED_ERROR);
                                } else if (error instanceof ParseError) {
                                    //TODO
                                }

                                Log.i("error",""+error.getMessage());
                            }
                        });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(1000000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);
    }
    //Rango Preciossssssssssssss
    public void realizarSincronizacionLocalRangosPrecios(final SyncResult syncResult, String operatorName) {
        Log.i(TAG, ">>> RANGOS_PRECIOS: Iniciando sincronización. Operador: " + operatorName);

        if (operatorName == null || operatorName.isEmpty()) {
            Log.e(TAG, ">>> RANGOS_PRECIOS: ERROR - operatorName es nulo o vacío");
            enviarBroadcast(false, "Error: Usuario no definido para sincronización de precios.");
            return;
        }

        HashMap<String, String> map = new HashMap<>();
        map.put("operators", operatorName);
        JSONObject jobject = new JSONObject(map);

        Log.i(TAG, ">>> RANGOS_PRECIOS: JSON enviado al servidor: " + jobject.toString());
        Log.i(TAG, ">>> RANGOS_PRECIOS: URL destino: " + Constantes.GET_RANGOS_PRECIOS);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, Constantes.GET_RANGOS_PRECIOS, jobject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, ">>> RANGOS_PRECIOS: Respuesta recibida del servidor: " + response.toString());
                        procesarRespuestaGetRangosPrecios(response, syncResult);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String errorMsg;
                        if (error instanceof TimeoutError) {
                            errorMsg = Mensajes.TIME_OUT;
                            Log.e(TAG, ">>> RANGOS_PRECIOS: ERROR - TimeoutError");
                        } else if (error instanceof NoConnectionError) {
                            errorMsg = Mensajes.NO_RED;
                            Log.e(TAG, ">>> RANGOS_PRECIOS: ERROR - NoConnectionError");
                        } else if (error instanceof ServerError) {
                            errorMsg = Mensajes.SEVER_ERROR;
                            Log.e(TAG, ">>> RANGOS_PRECIOS: ERROR - ServerError. Código: " +
                                    (error.networkResponse != null ? error.networkResponse.statusCode : "sin código"));
                            if (error.networkResponse != null) {
                                Log.e(TAG, ">>> RANGOS_PRECIOS: Respuesta del servidor: " +
                                        new String(error.networkResponse.data));
                            }
                        } else if (error instanceof NetworkError) {
                            errorMsg = Mensajes.RED_ERROR;
                            Log.e(TAG, ">>> RANGOS_PRECIOS: ERROR - NetworkError");
                        } else if (error instanceof AuthFailureError) {
                            errorMsg = "Error de autenticación";
                            Log.e(TAG, ">>> RANGOS_PRECIOS: ERROR - AuthFailureError");
                        } else {
                            errorMsg = "Error desconocido: " + (error.getMessage() != null ? error.getMessage() : "null");
                            Log.e(TAG, ">>> RANGOS_PRECIOS: ERROR - Desconocido: " + error.getMessage());
                        }
                        // *** CRÍTICO: siempre enviar broadcast en caso de error ***
                        enviarBroadcast(true, errorMsg);
                    }
                });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_DEFAULT_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Log.i(TAG, ">>> RANGOS_PRECIOS: Petición añadida a la cola de Volley");
        VolleySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);
    }

    private void procesarRespuestaGetRangosPrecios(JSONObject response, SyncResult syncResult) {
        Log.i(TAG, ">>> RANGOS_PRECIOS: Procesando respuesta...");
        try {
            String estado = response.getString(Constantes.ESTADO);
            Log.i(TAG, ">>> RANGOS_PRECIOS: Estado recibido: " + estado);

            switch (estado) {
                case Constantes.SUCCESS:
                    Log.i(TAG, ">>> RANGOS_PRECIOS: Estado SUCCESS, procediendo a actualizar datos locales");
                    actualizarDatosLocalesRangosPrecios(response, syncResult);
                    break;
                case Constantes.FAILED:
                    String mensaje = response.getString(Constantes.MENSAJE);
                    Log.e(TAG, ">>> RANGOS_PRECIOS: Estado FAILED. Mensaje: " + mensaje);
                    // *** CRÍTICO: enviar broadcast aunque sea FAILED ***
                    enviarBroadcast(true, mensaje);
                    break;
                default:
                    Log.e(TAG, ">>> RANGOS_PRECIOS: Estado desconocido: " + estado);
                    enviarBroadcast(true, "Estado desconocido del servidor: " + estado);
                    break;
            }
        } catch (JSONException e) {
            Log.e(TAG, ">>> RANGOS_PRECIOS: JSONException al procesar respuesta: " + e.getMessage());
            e.printStackTrace();
            // *** CRÍTICO: enviar broadcast en caso de excepción ***
            enviarBroadcast(true, "Error al procesar respuesta del servidor.");
        }
    }

    private void actualizarDatosLocalesRangosPrecios(JSONObject response, SyncResult syncResult) {
        Log.i(TAG, ">>> RANGOS_PRECIOS: Iniciando actualización de datos locales...");
        JSONArray loginResult = null;

        try {
            loginResult = response.getJSONArray(Constantes.RANGOS_PRECIOS_RESULT);
            Log.i(TAG, ">>> RANGOS_PRECIOS: Registros recibidos del servidor: " + loginResult.length());
        } catch (JSONException e) {
            Log.e(TAG, ">>> RANGOS_PRECIOS: ERROR al obtener array '" + Constantes.RANGOS_PRECIOS_RESULT +
                    "' del JSON. Claves disponibles: " + response.toString());
            e.printStackTrace();
            // *** CRÍTICO: enviar broadcast para salir del loading ***
            enviarBroadcast(true, "Error: clave '" + Constantes.RANGOS_PRECIOS_RESULT + "' no encontrada en respuesta.");
            return;
        }

        try {
            BaseRangosPrecios[] res = gson.fromJson(
                    loginResult.toString(),
                    BaseRangosPrecios[].class);
            List<BaseRangosPrecios> data = Arrays.asList(res);
            Log.i(TAG, ">>> RANGOS_PRECIOS: Objetos parseados con Gson: " + data.size());

            ArrayList<ContentProviderOperation> ops = new ArrayList<>();
            HashMap<String, BaseRangosPrecios> expenseMap = new HashMap<>();

            for (BaseRangosPrecios e : data) {
                if (e.presentacion != null) {
                    expenseMap.put(e.presentacion, e);
                    Log.d(TAG, ">>> RANGOS_PRECIOS: Mapeado - presentacion: " + e.presentacion +
                            ", min: " + e.getMinimo() + ", max: " + e.getMaximo());
                } else {
                    Log.w(TAG, ">>> RANGOS_PRECIOS: Registro con presentacion null, ignorado");
                }
            }

            Log.i(TAG, ">>> RANGOS_PRECIOS: Total en mapa: " + expenseMap.size());

            Uri uri = ContractRangosPrecios.CONTENT_URI;
            String select = ContractRangosPrecios.Columnas.PRESENTACION + " IS NOT NULL";
            Cursor c = resolver.query(uri, Projection.PROJECTION_RANGOS_PRECIOS, select, null, null);

            if (c == null) {
                Log.e(TAG, ">>> RANGOS_PRECIOS: Cursor nulo, posible error en ContentProvider");
                enviarBroadcast(true, "Error al acceder a la base de datos local.");
                return;
            }

            Log.i(TAG, ">>> RANGOS_PRECIOS: Registros locales encontrados: " + c.getCount());

            while (c.moveToNext()) {
                syncResult.stats.numEntries++;
                String presentacion = c.getString(Projection.R_PRESENTACION);
                double minimo = c.getDouble(Projection.R_MINIMO);
                double maximo = c.getDouble(Projection.R_MAXIMO);

                Log.d(TAG, ">>> RANGOS_PRECIOS: Revisando local - presentacion: " + presentacion);

                BaseRangosPrecios match = expenseMap.get(presentacion);
                if (match != null) {
                    expenseMap.remove(presentacion);
                    Uri existingUri = ContractRangosPrecios.CONTENT_URI.buildUpon()
                            .appendPath(presentacion).build();

                    if (match.getMinimo() != minimo || match.getMaximo() != maximo) {
                        Log.i(TAG, ">>> RANGOS_PRECIOS: Actualizando - " + presentacion);
                        ops.add(ContentProviderOperation.newUpdate(existingUri)
                                .withValue(ContractRangosPrecios.Columnas.MINIMO, match.getMinimo())
                                .withValue(ContractRangosPrecios.Columnas.MAXIMO, match.getMaximo())
                                .build());
                        syncResult.stats.numUpdates++;
                    } else {
                        Log.d(TAG, ">>> RANGOS_PRECIOS: Sin cambios para - " + presentacion);
                    }
                } else {
                    Log.i(TAG, ">>> RANGOS_PRECIOS: Eliminando local (no existe en servidor) - " + presentacion);
                    Uri deleteUri = ContractRangosPrecios.CONTENT_URI.buildUpon()
                            .appendPath(presentacion).build();
                    ops.add(ContentProviderOperation.newDelete(deleteUri).build());
                    syncResult.stats.numDeletes++;
                }
            }
            c.close();

            for (BaseRangosPrecios e : expenseMap.values()) {
                Log.i(TAG, ">>> RANGOS_PRECIOS: Insertando nuevo - presentacion: " + e.getPresentacion());
                ops.add(ContentProviderOperation.newInsert(ContractRangosPrecios.CONTENT_URI)
                        .withValue(ContractRangosPrecios.Columnas.PRESENTACION, e.getPresentacion())
                        .withValue(ContractRangosPrecios.Columnas.MINIMO, e.getMinimo())
                        .withValue(ContractRangosPrecios.Columnas.MAXIMO, e.getMaximo())
                        .build());
                syncResult.stats.numInserts++;
            }

            Log.i(TAG, ">>> RANGOS_PRECIOS: Resumen - Inserts: " + syncResult.stats.numInserts +
                    ", Updates: " + syncResult.stats.numUpdates +
                    ", Deletes: " + syncResult.stats.numDeletes);

            if (!ops.isEmpty()) {
                Log.i(TAG, ">>> RANGOS_PRECIOS: Aplicando " + ops.size() + " operaciones en batch...");
                resolver.applyBatch(Constantes.AUTHORITY, ops);
                resolver.notifyChange(ContractRangosPrecios.CONTENT_URI, null, false);
                Log.i(TAG, ">>> RANGOS_PRECIOS: Batch aplicado correctamente");
                enviarBroadcast(true, Mensajes.SYNC_FINALIZADA_RANGOS_PRECIOS);
            } else {
                Log.i(TAG, ">>> RANGOS_PRECIOS: No hay operaciones pendientes, datos ya sincronizados");
                enviarBroadcast(true, Mensajes.SYNC_NOREQUERIDA + "RangosPrecios");
            }

        } catch (RemoteException | OperationApplicationException e) {
            Log.e(TAG, ">>> RANGOS_PRECIOS: ERROR al aplicar batch: " + e.getMessage());
            e.printStackTrace();
            enviarBroadcast(true, "Error al guardar datos localmente: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, ">>> RANGOS_PRECIOS: ERROR inesperado: " + e.getMessage());
            e.printStackTrace();
            enviarBroadcast(true, "Error inesperado: " + e.getMessage());
        }
    }

    //Rango Precios por SKUU waazaaaaa
    
    public void realizarSincronizacionLocalRangosPreciosSku(final SyncResult syncResult, String operatorName) {
        Log.i(TAG, ">>> RANGOS_PRECIOS_SKU: Iniciando sincronización. Operador: " + operatorName);

        if (operatorName == null || operatorName.isEmpty()) {
            Log.e(TAG, ">>> RANGOS_PRECIOS_SKU: ERROR - operatorName es nulo o vacío");
            enviarBroadcast(false, "Error: Usuario no definido para sincronización de precios por SKU.");
            return;
        }

        HashMap<String, String> map = new HashMap<>();
        map.put("operators", operatorName);
        JSONObject jobject = new JSONObject(map);

        Log.i(TAG, ">>> RANGOS_PRECIOS_SKU: JSON enviado al servidor: " + jobject.toString());
        Log.i(TAG, ">>> RANGOS_PRECIOS_SKU: URL destino: " + Constantes.GET_RANGOS_PRECIOS_SKU);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, Constantes.GET_RANGOS_PRECIOS_SKU, jobject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, ">>> RANGOS_PRECIOS_SKU: Respuesta recibida del servidor: " + response.toString());
                        procesarRespuestaGetRangosPreciosSku(response, syncResult);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String errorMsg;
                        if (error instanceof TimeoutError) {
                            errorMsg = Mensajes.TIME_OUT;
                            Log.e(TAG, ">>> RANGOS_PRECIOS_SKU: ERROR - TimeoutError");
                        } else if (error instanceof NoConnectionError) {
                            errorMsg = Mensajes.NO_RED;
                            Log.e(TAG, ">>> RANGOS_PRECIOS_SKU: ERROR - NoConnectionError");
                        } else if (error instanceof ServerError) {
                            errorMsg = Mensajes.SEVER_ERROR;
                            Log.e(TAG, ">>> RANGOS_PRECIOS_SKU: ERROR - ServerError. Código: " +
                                    (error.networkResponse != null ? error.networkResponse.statusCode : "sin código"));
                            if (error.networkResponse != null) {
                                Log.e(TAG, ">>> RANGOS_PRECIOS_SKU: Respuesta del servidor: " +
                                        new String(error.networkResponse.data));
                            }
                        } else if (error instanceof NetworkError) {
                            errorMsg = Mensajes.RED_ERROR;
                            Log.e(TAG, ">>> RANGOS_PRECIOS_SKU: ERROR - NetworkError");
                        } else if (error instanceof AuthFailureError) {
                            errorMsg = "Error de autenticación";
                            Log.e(TAG, ">>> RANGOS_PRECIOS_SKU: ERROR - AuthFailureError");
                        } else {
                            errorMsg = "Error desconocido: " + (error.getMessage() != null ? error.getMessage() : "null");
                            Log.e(TAG, ">>> RANGOS_PRECIOS_SKU: ERROR - Desconocido: " + error.getMessage());
                        }
                        // *** CRÍTICO: siempre enviar broadcast en caso de error ***
                        enviarBroadcast(true, errorMsg);
                    }
                });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_DEFAULT_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Log.i(TAG, ">>> RANGOS_PRECIOS_SKU: Petición añadida a la cola de Volley");
        VolleySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);
    }

    private void procesarRespuestaGetRangosPreciosSku(JSONObject response, SyncResult syncResult) {
        Log.i(TAG, ">>> RANGOS_PRECIOS_SKU: Procesando respuesta...");
        try {
            String estado = response.getString(Constantes.ESTADO);
            Log.i(TAG, ">>> RANGOS_PRECIOS_SKU: Estado recibido: " + estado);

            switch (estado) {
                case Constantes.SUCCESS:
                    Log.i(TAG, ">>> RANGOS_PRECIOS_SKU: Estado SUCCESS, procediendo a actualizar datos locales");
                    actualizarDatosLocalesRangosPreciosSku(response, syncResult);
                    break;
                case Constantes.FAILED:
                    String mensaje = response.getString(Constantes.MENSAJE);
                    Log.e(TAG, ">>> RANGOS_PRECIOS_SKU: Estado FAILED. Mensaje: " + mensaje);
                    // *** CRÍTICO: enviar broadcast aunque sea FAILED ***
                    enviarBroadcast(true, mensaje);
                    break;
                default:
                    Log.e(TAG, ">>> RANGOS_PRECIOS_SKU: Estado desconocido: " + estado);
                    enviarBroadcast(true, "Estado desconocido del servidor: " + estado);
                    break;
            }
        } catch (JSONException e) {
            Log.e(TAG, ">>> RANGOS_PRECIOS_SKU: JSONException al procesar respuesta: " + e.getMessage());
            e.printStackTrace();
            // *** CRÍTICO: enviar broadcast en caso de excepción ***
            enviarBroadcast(true, "Error al procesar respuesta del servidor.");
        }
    }

    private void actualizarDatosLocalesRangosPreciosSku(JSONObject response, SyncResult syncResult) {
        Log.i(TAG, ">>> RANGOS_PRECIOS_SKU: Iniciando actualización de datos locales...");
        JSONArray loginResult = null;

        try {
            loginResult = response.getJSONArray(Constantes.RANGOS_PRECIOS_SKU_RESULT);
            Log.i(TAG, ">>> RANGOS_PRECIOS_SKU: Registros recibidos del servidor: " + loginResult.length());
        } catch (JSONException e) {
            Log.e(TAG, ">>> RANGOS_PRECIOS_SKU: ERROR al obtener array '" + Constantes.RANGOS_PRECIOS_SKU_RESULT +
                    "' del JSON. Claves disponibles: " + response.toString());
            e.printStackTrace();
            // *** CRÍTICO: enviar broadcast para salir del loading ***
            enviarBroadcast(true, "Error: clave '" + Constantes.RANGOS_PRECIOS_SKU_RESULT + "' no encontrada en respuesta.");
            return;
        }

        try {
            BaseRangosPreciosSku[] res = gson.fromJson(
                    loginResult.toString(),
                    BaseRangosPreciosSku[].class);
            List<BaseRangosPreciosSku> data = Arrays.asList(res);
            Log.i(TAG, ">>> RANGOS_PRECIOS_SKU: Objetos parseados con Gson: " + data.size());

            ArrayList<ContentProviderOperation> ops = new ArrayList<>();
            HashMap<String, BaseRangosPreciosSku> expenseMap = new HashMap<>();

            for (BaseRangosPreciosSku e : data) {
                if (e.sku != null) {
                    expenseMap.put(e.sku, e);
                    Log.d(TAG, ">>> RANGOS_PRECIOS_SKU: Mapeado - sku: " + e.sku +
                            ", min: " + e.getMinimo() + ", max: " + e.getMaximo());
                } else {
                    Log.w(TAG, ">>> RANGOS_PRECIOS_SKU: Registro con sku null, ignorado");
                }
            }

            Log.i(TAG, ">>> RANGOS_PRECIOS_SKU: Total en mapa: " + expenseMap.size());

            Uri uri = ContractRangosPreciosSku.CONTENT_URI;
            String select = ContractRangosPreciosSku.Columnas.SKU + " IS NOT NULL";
            Cursor c = resolver.query(uri, Projection.PROJECTION_RANGOS_PRECIOS_SKU, select, null, null);

            if (c == null) {
                Log.e(TAG, ">>> RANGOS_PRECIOS_SKU: Cursor nulo, posible error en ContentProvider");
                enviarBroadcast(true, "Error al acceder a la base de datos local.");
                return;
            }

            Log.i(TAG, ">>> RANGOS_PRECIOS_SKU: Registros locales encontrados: " + c.getCount());

            while (c.moveToNext()) {
                syncResult.stats.numEntries++;
                String sku = c.getString(Projection.RS_SKU);
                double minimo = c.getDouble(Projection.RS_MINIMO);
                double maximo = c.getDouble(Projection.RS_MAXIMO);

                Log.d(TAG, ">>> RANGOS_PRECIOS_SKU: Revisando local - sku: " + sku);

                BaseRangosPreciosSku match = expenseMap.get(sku);
                if (match != null) {
                    expenseMap.remove(sku);
                    Uri existingUri = ContractRangosPreciosSku.CONTENT_URI.buildUpon()
                            .appendPath(sku).build();

                    if (match.getMinimo() != minimo || match.getMaximo() != maximo) {
                        Log.i(TAG, ">>> RANGOS_PRECIOS_SKU: Actualizando - " + sku);
                        ops.add(ContentProviderOperation.newUpdate(existingUri)
                                .withValue(ContractRangosPreciosSku.Columnas.MINIMO, match.getMinimo())
                                .withValue(ContractRangosPreciosSku.Columnas.MAXIMO, match.getMaximo())
                                .build());
                        syncResult.stats.numUpdates++;
                    } else {
                        Log.d(TAG, ">>> RANGOS_PRECIOS_SKU: Sin cambios para - " + sku);
                    }
                } else {
                    Log.i(TAG, ">>> RANGOS_PRECIOS_SKU: Eliminando local (no existe en servidor) - " + sku);
                    Uri deleteUri = ContractRangosPreciosSku.CONTENT_URI.buildUpon()
                            .appendPath(sku).build();
                    ops.add(ContentProviderOperation.newDelete(deleteUri).build());
                    syncResult.stats.numDeletes++;
                }
            }
            c.close();

            for (BaseRangosPreciosSku e : expenseMap.values()) {
                Log.i(TAG, ">>> RANGOS_PRECIOS_SKU: Insertando nuevo - sku: " + e.getSku());
                ops.add(ContentProviderOperation.newInsert(ContractRangosPreciosSku.CONTENT_URI)
                        .withValue(ContractRangosPreciosSku.Columnas.SKU, e.getSku())
                        .withValue(ContractRangosPreciosSku.Columnas.MINIMO, e.getMinimo())
                        .withValue(ContractRangosPreciosSku.Columnas.MAXIMO, e.getMaximo())
                        .build());
                syncResult.stats.numInserts++;
            }

            Log.i(TAG, ">>> RANGOS_PRECIOS_SKU: Resumen - Inserts: " + syncResult.stats.numInserts +
                    ", Updates: " + syncResult.stats.numUpdates +
                    ", Deletes: " + syncResult.stats.numDeletes);

            if (!ops.isEmpty()) {
                Log.i(TAG, ">>> RANGOS_PRECIOS_SKU: Aplicando " + ops.size() + " operaciones en batch...");
                resolver.applyBatch(Constantes.AUTHORITY, ops);
                resolver.notifyChange(ContractRangosPreciosSku.CONTENT_URI, null, false);
                Log.i(TAG, ">>> RANGOS_PRECIOS_SKU: Batch aplicado correctamente");
                enviarBroadcast(true, Mensajes.SYNC_FINALIZADA_RANGOS_PRECIOS_SKU);
            } else {
                Log.i(TAG, ">>> RANGOS_PRECIOS_SKU: No hay operaciones pendientes, datos ya sincronizados");
                enviarBroadcast(true, Mensajes.SYNC_NOREQUERIDA + "RangosPreciosSku");
            }

        } catch (RemoteException | OperationApplicationException e) {
            Log.e(TAG, ">>> RANGOS_PRECIOS_SKU: ERROR al aplicar batch: " + e.getMessage());
            e.printStackTrace();
            enviarBroadcast(true, "Error al guardar datos localmente: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, ">>> RANGOS_PRECIOS_SKU: ERROR inesperado: " + e.getMessage());
            e.printStackTrace();
            enviarBroadcast(true, "Error inesperado: " + e.getMessage());
        }
    }
    //Fin de rango precios por SKU waazaaaaa



    private void procesarRespuestaGetPreciosPvc(JSONObject response, SyncResult syncResult) {
        try {
            // Obtener atributo "estado"
            String estado = response.getString(Constantes.ESTADO);
            Log.i("estado",""+estado);

            switch (estado) {
                case Constantes.SUCCESS: // EXITO
                    actualizarDatosLocalesPreciosPvc(response, syncResult);
                    break;
                case Constantes.FAILED: // FALLIDO
                    String mensaje = response.getString(Constantes.MENSAJE);
                    enviarBroadcast(true, mensaje);
                    Log.i(TAG, mensaje);
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void actualizarDatosLocalesPreciosPvc(JSONObject response, SyncResult syncResult) {
        JSONArray loginResult = null;


        try {
            // Obtener array "gastos"
            loginResult = response.getJSONArray(Constantes.PRECIOS_PVC_RESULT);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Parsear con Gson
        Base_precio_pvc[] res = gson.fromJson(loginResult != null ? loginResult.toString() : null, Base_precio_pvc[].class);
        List<Base_precio_pvc> data = Arrays.asList(res);

        // Lista para recolección de operaciones pendientes
        ArrayList<ContentProviderOperation> ops = new ArrayList<>();

        // Tabla hash para recibir los datos entrantes
        HashMap<String, Base_precio_pvc> expenseMap = new HashMap<>();
        for (Base_precio_pvc e : data) {
            expenseMap.put(e.id, e);
        }

        // Consultar registros remotos actuales
        Uri uri = ContractPreciosPvc.CONTENT_URI;
        String select = Constantes.ID_REMOTA + " IS NOT NULL";
        Cursor c = resolver.query(uri, Projection.PROJECTION_PRECIOS_PVC, select, null, null);
        assert c != null;

        Log.i(TAG, "Se encontraron " + c.getCount() + " registros locales.");

        // Encontrar datos obsoletos
        String id;
        //String codigo_producto;
        String usuario;
        String fecha;
        String codigo_pdv;
        String canal;
        String cadena;
        String categoria;
        String subcategoria;
        String marca;
        String sku;
        String pvc;



        while (c.moveToNext()) {

            syncResult.stats.numEntries++;
            id = c.getString(Projection.COLUMNA_ID_REMOTA);
            //codigo_producto = c.getString(Projection.CODIGO_PRODUCTO);
            usuario = c.getString(Projection.COLUMNA_USUARIO_PVC);
            fecha = c.getString(Projection.COLUMNA_FECHA_PVC);
            codigo_pdv = c.getString(Projection.COLUMNA_CODIGO_PVC);
            canal = c.getString(Projection.COLUMNA_CANAL);
            cadena = c.getString(Projection.COLUMNA_CADENA);
            categoria = c.getString(Projection.COLUMNA_CATEGORIA_PVC);
            subcategoria = c.getString(Projection.COLUMNA_SUBCATEGORIA_PVC);
            marca = c.getString(Projection.COLUMNA_MARCA_PVC);
            sku = c.getString(Projection.COLUMNA_SKU_PVC);
            pvc = c.getString(Projection.COLUMNA_PVC);



            Base_precio_pvc match = expenseMap.get(id);

            if (match != null) {
                // Esta entrada existe, por lo que se remueve del mapeado
                expenseMap.remove(id);

                Uri existingUri = ContractPreciosPvc.CONTENT_URI.buildUpon()
                        .appendPath(id).build();

                // Comprobar si el gasto necesita ser actualizado
                //boolean b=match.codigo_producto!=null && !match.codigo_producto.equals(codigo_producto);
                boolean b2=  match.usuario!=null && !match.usuario.equals(usuario);
                boolean b3=  match.fecha!=null && !match.fecha.equals(fecha);
                boolean b4=  match.codigo_pdv!=null && !match.codigo_pdv.equals(codigo_pdv);
                boolean b5=  match.canal!=null && !match.canal.equals(canal);
                boolean b6=  match.cadena!=null && !match.cadena.equals(cadena);
                boolean b7=  match.categoria!=null && !match.categoria.equals(categoria);
                boolean b8 = match.subcategoria != null && !match.subcategoria.equals(subcategoria);
                boolean b9 = match.marca != null && !match.marca.equals(marca);
                boolean b10 = match.sku != null && !match.sku.equals(sku);
                boolean b11 = match.pvc != null && !match.pvc.equals(pvc);

                if (b2 || b3 || b4 || b5 || b6 || b7 || b8 || b9 || b10 || b11) {

                    Log.i(TAG, "Programando actualización de: " + existingUri);

                    ops.add(ContentProviderOperation.newUpdate(existingUri)
                            //.withValue(ContractPortafolioProductos.Columnas.CODIGO_PRODUCTO,match.codigo_producto)
                            .withValue(ContractPreciosPvc.Columnas.USUARIO,match.usuario)
                            .withValue(ContractPreciosPvc.Columnas.FECHA,match.fecha)
                            .withValue(ContractPreciosPvc.Columnas.CODIGO,match.codigo_pdv)
                            .withValue(ContractPreciosPvc.Columnas.CANAL,match.canal)
                            .withValue(ContractPreciosPvc.Columnas.CADENA,match.cadena)
                            .withValue(ContractPreciosPvc.Columnas.CATEGORIA,match.categoria)
                            .withValue(ContractPreciosPvc.Columnas.SUBCATEGORIA, match.subcategoria)
                            .withValue(ContractPreciosPvc.Columnas.MARCA, match.marca)
                            .withValue(ContractPreciosPvc.Columnas.SKU, match.sku)
                            .withValue(ContractPreciosPvc.Columnas.PVC, match.pvc)
                            .build());
                    syncResult.stats.numUpdates++;
                } else {
                    Log.i(TAG, "No hay acciones para este registro: " + existingUri);
                }
            } else {
                // Debido a que la entrada no existe, es removida de la base de datos
                Uri deleteUri = ContractPreciosPvc.CONTENT_URI.buildUpon()
                        .appendPath(id).build();
                Log.i(TAG, "Programando eliminación de: " + deleteUri);
                ops.add(ContentProviderOperation.newDelete(deleteUri).build());
                syncResult.stats.numDeletes++;
            }
        }
        c.close();

        // Insertar items resultantes
        for (Base_precio_pvc e : expenseMap.values()) {
            Log.i(TAG, "Programando inserción de Tabla Base Precios Pvc en Base Local: " + e.id);
            ops.add(ContentProviderOperation.newInsert(ContractPreciosPvc.CONTENT_URI)
                    .withValue(Constantes.ID_REMOTA , e.id) //Esta clave importante!!! ForeignKey del servidor
                    //.withValue(ContractPortafolioProductos.Columnas.CODIGO_PRODUCTO,e.codigo_producto)
                    .withValue(ContractPreciosPvc.Columnas.USUARIO,e.usuario)
                    .withValue(ContractPreciosPvc.Columnas.FECHA,e.fecha)
                    .withValue(ContractPreciosPvc.Columnas.CODIGO,e.codigo_pdv)
                    .withValue(ContractPreciosPvc.Columnas.CANAL,e.canal)
                    .withValue(ContractPreciosPvc.Columnas.CADENA,e.cadena)
                    .withValue(ContractPreciosPvc.Columnas.CATEGORIA,e.categoria)
                    .withValue(ContractPreciosPvc.Columnas.SUBCATEGORIA, e.subcategoria)
                    .withValue(ContractPreciosPvc.Columnas.MARCA, e.marca)
                    .withValue(ContractPreciosPvc.Columnas.SKU, e.sku)
                    .withValue(ContractPreciosPvc.Columnas.PVC, e.pvc)
                    .build());
            syncResult.stats.numInserts++;
        }

        if (syncResult.stats.numInserts > 0 ||
                syncResult.stats.numUpdates > 0 ||
                syncResult.stats.numDeletes > 0) {
            Log.i(TAG, "Aplicando operaciones...");
            try {
                resolver.applyBatch(Constantes.AUTHORITY, ops);
            } catch (RemoteException | OperationApplicationException e) {
                e.printStackTrace();
            }
            resolver.notifyChange(
                    ContractPreciosPvc.CONTENT_URI,
                    null,
                    false);
//            Toast.makeText(getContext(),"Sincronización de Base_pharma_value finalizada.",Toast.LENGTH_SHORT).show();
            Log.i(TAG, "Sincronización de tabla Base Precios Pvc finalizada.");
            enviarBroadcast(true, Mensajes.SYNC_FINALIZADA_PRECIOS_PVC);

        } else {
            Log.i(TAG, "No se requiere sincronización de tabla Base Precios Pvc");
            enviarBroadcast(true, Mensajes.SYNC_NOREQUERIDA + "Precios Pvc");
        }

    }




    /**
     * TESTS
     *
     * @param syncResult
     * @param operatorName
     */
    public void realizarSincronizacionLocalTests(final SyncResult syncResult, String operatorName) {
        Log.i(TAG, "Realizando Sincronizacion Local de Tests." + operatorName);
        String operators = operatorName;
        HashMap<String, String> map = new HashMap<>();// Mapeo previo

        map.put("operators", operators);

        // Crear nuevo objeto Json basado en el mapa
        JSONObject jobject = new JSONObject(map);
        //GET METHOD
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, Constantes.GET_TESTS, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        procesarRespuestaGetTests(response, syncResult);
                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                if (error instanceof TimeoutError) {
                                    enviarBroadcast(true, Mensajes.TIME_OUT);
                                } else if (error instanceof NoConnectionError) {
                                    //TODO
                                    enviarBroadcast(true, Mensajes.NO_RED);
                                } else if (error instanceof AuthFailureError) {
                                    //TODO
                                } else if (error instanceof ServerError) {
                                    //TODO
                                    enviarBroadcast(true, Mensajes.SEVER_ERROR);
                                } else if (error instanceof NetworkError) {
                                    //TODO
                                    enviarBroadcast(true, Mensajes.RED_ERROR);
                                } else if (error instanceof ParseError) {
                                    //TODO
                                }
                            }
                        });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(MY_DEFAULT_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);
    }

    private void procesarRespuestaGetTests(JSONObject response, SyncResult syncResult) {
        try {
            // Obtener atributo "estado"
            String estado = response.getString(Constantes.ESTADO);

            switch (estado) {
                case Constantes.SUCCESS: // EXITO
                    actualizarDatosLocalesTests(response, syncResult);
                    break;
                case Constantes.FAILED: // FALLIDO
                    String mensaje = response.getString(Constantes.MENSAJE);
                    enviarBroadcast(true, mensaje);
                    Log.i(TAG, mensaje);
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void actualizarDatosLocalesTests(JSONObject response, SyncResult syncResult) {
        JSONArray loginResult = null;

        try {
            // Obtener array "gastos"
            loginResult = response.getJSONArray(Constantes.TESTS_RESULT);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Parsear con Gson
        Base_tests[] res = gson.fromJson(loginResult != null ? loginResult.toString() : null, Base_tests[].class);
        List<Base_tests> data = Arrays.asList(res);

        // Lista para recolección de operaciones pendientes
        ArrayList<ContentProviderOperation> ops = new ArrayList<>();

        // Tabla hash para recibir los datos entrantes
        HashMap<String, Base_tests> expenseMap = new HashMap<>();
        for (Base_tests e : data) {
            expenseMap.put(e.id, e);
        }

        // Consultar registros remotos actuales
        Uri uri = ContractTests.CONTENT_URI;
        String select = Constantes.ID_REMOTA + " IS NOT NULL";
        Cursor c = resolver.query(uri, Projection.PROJECTION_TESTS, select, null, null);
        assert c != null;

        Log.i(TAG, "Se encontraron " + c.getCount() + " registros locales.");

        // Encontrar datos obsoletos
        String id;


        String test;
        String descripcion;
        String f_inicio;
        String h_inicio;
        String f_limite;
        String h_limite;
        String active;


        while (c.moveToNext()) {

            syncResult.stats.numEntries++;
            id = c.getString(Projection.COLUMNA_ID_REMOTA);
            //codigo_producto = c.getString(Projection.CODIGO_PRODUCTO);
            test = c.getString(Projection.TEST);
            descripcion = c.getString(Projection.DESCRIPCION);
            f_inicio = c.getString(Projection.F_INICIO);
            h_inicio = c.getString(Projection.H_INICIO);
            f_limite = c.getString(Projection.F_LIMITE);
            h_limite = c.getString(Projection.H_LIMITE);
            active = c.getString(Projection.ACTIVE);




            Base_tests match = expenseMap.get(id);

            if (match != null) {
                // Esta entrada existe, por lo que se remueve del mapeado
                expenseMap.remove(id);

                Uri existingUri = ContractTests.CONTENT_URI.buildUpon()
                        .appendPath(id).build();

                // Comprobar si el gasto necesita ser actualizado
                //boolean b=match.codigo_producto!=null && !match.codigo_producto.equals(codigo_producto);

                boolean b2=match.test!=null && !match.test.equals(test);
                boolean b3=match.descripcion!=null && !match.descripcion.equals(descripcion);
                boolean b4=match.f_inicio!=null && !match.f_inicio.equals(f_inicio);
                boolean b5=match.h_inicio!=null && !match.h_inicio.equals(h_inicio);
                boolean b6 = match.f_limite != null && !match.f_limite.equals(f_limite);
                boolean b7 = match.h_limite != null && !match.h_limite.equals(h_limite);
                boolean b8 = match.active != null && !match.active.equals(active);



                if (b2 || b3 || b4 || b5 || b6 || b7 || b8 ) {

                    Log.i(TAG, "Programando actualización de: " + existingUri);

                    ops.add(ContentProviderOperation.newUpdate(existingUri)
                            //.withValue(ContractPortafolioProductos.Columnas.CODIGO_PRODUCTO,match.codigo_producto)
                            .withValue(ContractTests.Columnas.KEY_TEST,match.test)
                            .withValue(ContractTests.Columnas.KEY_DESCRIPTION,match.descripcion)
                            .withValue(ContractTests.Columnas.KEY_DATE_START,match.f_inicio)
                            .withValue(ContractTests.Columnas.KEY_HOUR_START,match.h_inicio)
                            .withValue(ContractTests.Columnas.KEY_DATE_LIMIT,match.f_limite)
                            .withValue(ContractTests.Columnas.KEY_HOUR_LIMIT,match.h_limite)
                            .withValue(ContractTests.Columnas.KEY_ACTIVE, match.active)
                            .build());
                    syncResult.stats.numUpdates++;

                } else {
                    Log.i(TAG, "No hay acciones para este registro: " + existingUri);
                }
            } else {
                // Debido a que la entrada no existe, es removida de la base de datos
                Uri deleteUri = ContractTests.CONTENT_URI.buildUpon()
                        .appendPath(id).build();
                Log.i(TAG, "Programando eliminación de: " + deleteUri);
                ops.add(ContentProviderOperation.newDelete(deleteUri).build());
                syncResult.stats.numDeletes++;
            }
        }
        c.close();

        // Insertar items resultantes
        for (Base_tests e : expenseMap.values()) {
            Log.i(TAG, "Programando inserción de Tabla Base_Test en Base Local: " + e.id);
            ops.add(ContentProviderOperation.newInsert(ContractTests.CONTENT_URI)
                    .withValue(Constantes.ID_REMOTA , e.id) //Esta clave importante!!! ForeignKey del servidor
                    //.withValue(ContractPortafolioProductos.Columnas.CODIGO_PRODUCTO,e.codigo_producto)
                    .withValue(ContractTests.Columnas.KEY_TEST,e.test)
                    .withValue(ContractTests.Columnas.KEY_DESCRIPTION,e.descripcion)
                    .withValue(ContractTests.Columnas.KEY_DATE_START,e.f_inicio)
                    .withValue(ContractTests.Columnas.KEY_HOUR_START,e.h_inicio)
                    .withValue(ContractTests.Columnas.KEY_DATE_LIMIT,e.f_limite)
                    .withValue(ContractTests.Columnas.KEY_HOUR_LIMIT,e.h_limite)
                    .withValue(ContractTests.Columnas.KEY_ACTIVE, e.active)
                    .build());
            syncResult.stats.numInserts++;
        }

        if (syncResult.stats.numInserts > 0 ||
                syncResult.stats.numUpdates > 0 ||
                syncResult.stats.numDeletes > 0) {
            Log.i(TAG, "Aplicando operaciones...");
            try {
                resolver.applyBatch(Constantes.AUTHORITY, ops);
            } catch (RemoteException | OperationApplicationException e) {
                e.printStackTrace();
            }
            resolver.notifyChange(
                    ContractTests.CONTENT_URI,
                    null,
                    false);
//            Toast.makeText(getContext(),"Sincronización de Base_pharma_value finalizada.",Toast.LENGTH_SHORT).show();
            Toast.makeText(getContext(),"Sincronizacion finalizada",Toast.LENGTH_SHORT).show();
            Log.i(TAG, "Sincronización de tabla Base_Test finalizada.");
            enviarBroadcast(true, Mensajes.SYNC_FINALIZADA_TESTS);

        } else {
            Log.i(TAG, "No se requiere sincronización de tabla Test");
            enviarBroadcast(true, Mensajes.SYNC_NOREQUERIDA + "Tests");
        }

    }




    /**
     * TIPO DE EXHIBICION
     *
     * @param syncResult
     * @param operatorName
     */
    public void realizarSincronizacionLocalTipoExh(final SyncResult syncResult, String operatorName) {
        Log.i(TAG, "Realizando Sincronizacion Local de Tipo de Exhibicion." + operatorName);
        String operators = operatorName;
        HashMap<String, String> map = new HashMap<>();// Mapeo previo


        map.put("operators", operators);

        // Crear nuevo objeto Json basado en el mapa
        JSONObject jobject = new JSONObject(map);
        //GET METHOD
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, Constantes.GET_TIPO_EXH, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        procesarRespuestaGetTipoExh(response, syncResult);
                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                if (error instanceof TimeoutError) {
                                    enviarBroadcast(true, Mensajes.TIME_OUT);
                                } else if (error instanceof NoConnectionError) {
                                    //TODO
                                    enviarBroadcast(true, Mensajes.NO_RED);
                                } else if (error instanceof AuthFailureError) {
                                    //TODO
                                } else if (error instanceof ServerError) {
                                    //TODO
                                    enviarBroadcast(true, Mensajes.SEVER_ERROR);
                                } else if (error instanceof NetworkError) {
                                    //TODO
                                    enviarBroadcast(true, Mensajes.RED_ERROR);
                                } else if (error instanceof ParseError) {
                                    //TODO
                                }
                                Log.i("prueba",""+ error.getMessage());
                            }
                        });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(MY_DEFAULT_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);
    }

    private void procesarRespuestaGetTipoExh(JSONObject response, SyncResult syncResult) {
        try {
            // Obtener atributo "estado"
            String estado = response.getString(Constantes.ESTADO);

            switch (estado) {
                case Constantes.SUCCESS: // EXITO
                    actualizarDatosLocalesTipoExh(response, syncResult);
                    break;
                case Constantes.FAILED: // FALLIDO
                    String mensaje = response.getString(Constantes.MENSAJE);
                    enviarBroadcast(true, mensaje);
                    Log.i(TAG, mensaje);
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void actualizarDatosLocalesTipoExh(JSONObject response, SyncResult syncResult) {
        JSONArray loginResult = null;

        try {
            // Obtener array "gastos"

            loginResult = response.getJSONArray(Constantes.TIPO_EXH_RESULT);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Parsear con Gson
        Base_tipo_exh[] res = gson.fromJson(loginResult != null ? loginResult.toString() : null, Base_tipo_exh[].class);
        List<Base_tipo_exh> data = Arrays.asList(res);

        // Lista para recolección de operaciones pendientes
        ArrayList<ContentProviderOperation> ops = new ArrayList<>();

        // Tabla hash para recibir los datos entrantes
        HashMap<String, Base_tipo_exh> expenseMap = new HashMap<>();
        for (Base_tipo_exh e : data) {
            expenseMap.put(e.id, e);
        }

        // Consultar registros remotos actuales
        Uri uri = ContractTipoExh.CONTENT_URI;
        String select = Constantes.ID_REMOTA + " IS NOT NULL";
        Cursor c = resolver.query(uri, Projection.PROJECTION_TIPO_EXH, select, null, null);
        assert c != null;

        Log.i(TAG, "Se encontraron " + c.getCount() + " registros locales.");

        // Encontrar datos obsoletos
        String id;


        String canal;
        String exhibicion;
        String tipo;
        String foto;



        while (c.moveToNext()) {

            syncResult.stats.numEntries++;
            id = c.getString(Projection.COLUMNA_ID_REMOTA);
            //codigo_producto = c.getString(Projection.CODIGO_PRODUCTO);
            canal = c.getString(Projection.CANAL_E);
            exhibicion = c.getString(Projection.EXHIBICION);
            tipo = c.getString(Projection.TIPO_E);
            foto = c.getString(Projection.FOTO_E);





            Base_tipo_exh match = expenseMap.get(id);

            if (match != null) {
                // Esta entrada existe, por lo que se remueve del mapeado
                expenseMap.remove(id);

                Uri existingUri = ContractTipoExh.CONTENT_URI.buildUpon()
                        .appendPath(id).build();

                // Comprobar si el gasto necesita ser actualizado
                //boolean b=match.codigo_producto!=null && !match.codigo_producto.equals(codigo_producto);

                boolean b2=match.canal!=null && !match.canal.equals(canal);
                boolean b3=match.exhibicion!=null && !match.exhibicion.equals(exhibicion);
                boolean b4=match.tipo!=null && !match.tipo.equals(tipo);
                boolean b5=match.foto!=null && !match.foto.equals(foto);




                if (b2 || b3 || b4 || b5) {

                    Log.i(TAG, "Programando actualización de: " + existingUri);

                    ops.add(ContentProviderOperation.newUpdate(existingUri)
                            //.withValue(ContractPortafolioProductos.Columnas.CODIGO_PRODUCTO,match.codigo_producto)
                            .withValue(Constantes.ID_REMOTA,match.id)
                            .withValue(ContractTipoExh.Columnas.CANAL,match.canal)
                            .withValue(ContractTipoExh.Columnas.EXHIBICION,match.exhibicion)
                            .withValue(ContractTipoExh.Columnas.TIPO,match.tipo)
                            .withValue(ContractTipoExh.Columnas.FOTO,match.foto)
                            .build());
                    syncResult.stats.numUpdates++;

                } else {
                    Log.i(TAG, "No hay acciones para este registro: " + existingUri);
                }
            } else {
                // Debido a que la entrada no existe, es removida de la base de datos
                Uri deleteUri = ContractTipoExh.CONTENT_URI.buildUpon()
                        .appendPath(id).build();
                Log.i(TAG, "Programando eliminación de: " + deleteUri);
                ops.add(ContentProviderOperation.newDelete(deleteUri).build());
                syncResult.stats.numDeletes++;
            }
        }
        c.close();

        // Insertar items resultantes
        for (Base_tipo_exh e : expenseMap.values()) {
            Log.i(TAG, "Programando inserción de Tabla Base_Tipo_Exhibicion en Base Local: " + e.id);
            ops.add(ContentProviderOperation.newInsert(ContractTipoExh.CONTENT_URI)
                    .withValue(Constantes.ID_REMOTA , e.id) //Esta clave importante!!! ForeignKey del servidor
                    //.withValue(ContractPortafolioProductos.Columnas.CODIGO_PRODUCTO,e.codigo_producto)
                    .withValue(ContractTipoExh.Columnas.CANAL,e.canal)
                    .withValue(ContractTipoExh.Columnas.EXHIBICION,e.exhibicion)
                    .withValue(ContractTipoExh.Columnas.TIPO,e.tipo)
                    .withValue(ContractTipoExh.Columnas.FOTO,e.foto)
                    .build());
            syncResult.stats.numInserts++;
        }

        if (syncResult.stats.numInserts > 0 ||
                syncResult.stats.numUpdates > 0 ||
                syncResult.stats.numDeletes > 0) {
            Log.i(TAG, "Aplicando operaciones...");
            try {
                resolver.applyBatch(Constantes.AUTHORITY, ops);
            } catch (RemoteException | OperationApplicationException e) {
                e.printStackTrace();
            }
            resolver.notifyChange(
                    ContractTipoExh.CONTENT_URI,
                    null,
                    false);
//            Toast.makeText(getContext(),"Sincronización de Base_pharma_value finalizada.",Toast.LENGTH_SHORT).show();
            Toast.makeText(getContext(),"Sincronizacion finalizada",Toast.LENGTH_SHORT).show();
            Log.i(TAG, "Sincronización de tabla Base Tipo Exhibicion finalizada.");
            enviarBroadcast(true, Mensajes.SYNC_FINALIZADA_TIPO_EXH);

        } else {
            Log.i(TAG, "No se requiere sincronización de tabla Tipo Exhibicion  ");
            enviarBroadcast(true, Mensajes.SYNC_NOREQUERIDA + "Tipo Exhibicion");
        }

    }








    /**
     * PROMOCIONES
     *
     * @param syncResult
     * @param operatorName
     */
    public void realizarSincronizacionLocalPromociones(final SyncResult syncResult, String operatorName) {
        Log.i(TAG, "Realizando Sincronizacion Local de Promociones." + operatorName);
        String operators = operatorName;
        HashMap<String, String> map = new HashMap<>();// Mapeo previo

        map.put("operators", operators);

        // Crear nuevo objeto Json basado en el mapa
        JSONObject jobject = new JSONObject(map);
        //GET METHOD
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, Constantes.GET_PROMOCIONES, jobject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        procesarRespuestaGetPromociones(response, syncResult);
                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                if (error instanceof TimeoutError) {
                                    enviarBroadcast(true, Mensajes.TIME_OUT);
                                } else if (error instanceof NoConnectionError) {
                                    //TODO
                                    enviarBroadcast(true, Mensajes.NO_RED);
                                } else if (error instanceof AuthFailureError) {
                                    //TODO
                                } else if (error instanceof ServerError) {
                                    //TODO
                                    enviarBroadcast(true, Mensajes.SEVER_ERROR);
                                } else if (error instanceof NetworkError) {
                                    //TODO
                                    enviarBroadcast(true, Mensajes.RED_ERROR);
                                } else if (error instanceof ParseError) {
                                    //TODO
                                }
                            }
                        });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(MY_DEFAULT_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);
    }

    private void procesarRespuestaGetPromociones(JSONObject response, SyncResult syncResult) {
        try {
            // Obtener atributo "estado"
            String estado = response.getString(Constantes.ESTADO);

            switch (estado) {
                case Constantes.SUCCESS: // EXITO
                    actualizarDatosLocalesPromociones(response, syncResult);
                    break;
                case Constantes.FAILED: // FALLIDO
                    String mensaje = response.getString(Constantes.MENSAJE);
                    enviarBroadcast(true, mensaje);
                    Log.i(TAG, mensaje);
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void actualizarDatosLocalesPromociones(JSONObject response, SyncResult syncResult) {
        JSONArray loginResult = null;

        try {
            // Obtener array "gastos"
            loginResult = response.getJSONArray(Constantes.PROMOCIONES_RESULT);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Parsear con Gson
        BasePromociones[] res = gson.fromJson(loginResult != null ? loginResult.toString() : null, BasePromociones[].class);
        List<BasePromociones> data = Arrays.asList(res);

        // Lista para recolección de operaciones pendientes
        ArrayList<ContentProviderOperation> ops = new ArrayList<>();

        // Tabla hash para recibir los datos entrantes
        HashMap<String, BasePromociones> expenseMap = new HashMap<>();
        for (BasePromociones e : data) {
            expenseMap.put(e.id, e);
        }

        // Consultar registros remotos actuales
        Uri uri = ContractPromociones.CONTENT_URI;
        String select = Constantes.ID_REMOTA + " IS NOT NULL";
        Cursor c = resolver.query(uri, Projection.PROJECTION_PROMOCIONES, select, null, null);
        assert c != null;

        Log.i(TAG, "Se encontraron " + c.getCount() + " registros locales.");

        // Encontrar datos obsoletos
        String id;
        //String codigo_producto;
        String canal;
        String tipo;
        String descripcion;
        String mecanica;

        while (c.moveToNext()) {
            syncResult.stats.numEntries++;
            id = c.getString(Projection.COLUMNA_ID_REMOTA);
            //codigo_producto = c.getString(Projection.CODIGO_PRODUCTO);
            canal = c.getString(Projection.CANAL);
            tipo = c.getString(Projection.TIPO);
            descripcion = c.getString(Projection.DESCRIP);
            mecanica = c.getString(Projection.MECANICA);

            BasePromociones match = expenseMap.get(id);

            if (match != null) {
                // Esta entrada existe, por lo que se remueve del mapeado
                expenseMap.remove(id);

                Uri existingUri = ContractPromociones.CONTENT_URI.buildUpon()
                        .appendPath(id).build();

                // Comprobar si el gasto necesita ser actualizado
                //boolean b=match.codigo_producto!=null && !match.codigo_producto.equals(codigo_producto);
                boolean b1 = match.canal != null && !match.canal.equals(canal);
                boolean b2 = match.tipo != null && !match.tipo.equals(tipo);
                boolean b3 = match.descripcion != null && !match.descripcion.equals(descripcion);
                boolean b4 = match.mecanica != null && !match.mecanica.equals(mecanica);

                if (b1 || b2 || b3 || b4) {

                    Log.i(TAG, "Programando actualización de: " + existingUri);

                    ops.add(ContentProviderOperation.newUpdate(existingUri)
                            //.withValue(ContractPortafolioProductos.Columnas.CODIGO_PRODUCTO,match.codigo_producto)
                            .withValue(ContractPromociones.Columnas.CANAL, match.canal)
                            .withValue(ContractPromociones.Columnas.TIPO, match.tipo)
                            .withValue(ContractPromociones.Columnas.DESCRIPCION, match.descripcion)
                            .withValue(ContractPromociones.Columnas.MECANICA, match.mecanica)
                            .build());
                    syncResult.stats.numUpdates++;
                } else {
                    Log.i(TAG, "No hay acciones para este registro: " + existingUri);
                }
            } else {
                // Debido a que la entrada no existe, es removida de la base de datos
                Uri deleteUri = ContractPromociones.CONTENT_URI.buildUpon()
                        .appendPath(id).build();
                Log.i(TAG, "Programando eliminación de: " + deleteUri);
                ops.add(ContentProviderOperation.newDelete(deleteUri).build());
                syncResult.stats.numDeletes++;
            }
        }
        c.close();

        // Insertar items resultantes
        for (BasePromociones e : expenseMap.values()) {
            Log.i(TAG, "Programando inserción de Tabla BasePromociones en Base Local: " + e.id);
            ops.add(ContentProviderOperation.newInsert(ContractPromociones.CONTENT_URI)
                    .withValue(Constantes.ID_REMOTA, e.id) //Esta clave importante!!! ForeignKey del servidor
                    //.withValue(ContractPortafolioProductos.Columnas.CODIGO_PRODUCTO,e.codigo_producto)
                    .withValue(ContractPromociones.Columnas.CANAL, e.canal)
                    .withValue(ContractPromociones.Columnas.TIPO, e.tipo)
                    .withValue(ContractPromociones.Columnas.DESCRIPCION, e.descripcion)
                    .withValue(ContractPromociones.Columnas.MECANICA, e.mecanica)
                    .build());
            syncResult.stats.numInserts++;
        }

        if (syncResult.stats.numInserts > 0 ||
                syncResult.stats.numUpdates > 0 ||
                syncResult.stats.numDeletes > 0) {
            Log.i(TAG, "Aplicando operaciones...");
            try {
                resolver.applyBatch(Constantes.AUTHORITY, ops);
            } catch (RemoteException | OperationApplicationException e) {
                e.printStackTrace();
            }
            resolver.notifyChange(
                    ContractPromociones.CONTENT_URI,
                    null,
                    false);
//            Toast.makeText(getContext(),"Sincronización de BasePharmaValue finalizada.",Toast.LENGTH_SHORT).show();
            Log.i(TAG, "Sincronización de tabla BasePromociones finalizada");
            enviarBroadcast(true, Mensajes.SYNC_FINALIZADA_PROMO);

        } else {
            Log.i(TAG, "No se requiere sincronización de tabla Promociones");
            enviarBroadcast(true, Mensajes.SYNC_NOREQUERIDA + "Promociones");
        }

    }

    /**
     * ROTACION
     *
     * @param syncResult
     * @param operatorName
     */
    public void realizarSincronizacionLocalRotacion(final SyncResult syncResult, String operatorName) {
        Log.i(TAG, "Realizando Sincronizacion Local de Rotacion." + operatorName);
        String operators = operatorName;
        HashMap<String, String> map = new HashMap<>();// Mapeo previo

        map.put("operators", operators);

        // Crear nuevo objeto Json basado en el mapa
        JSONObject jobject = new JSONObject(map);
        //GET METHOD
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, Constantes.GET_ROTACION, jobject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        procesarRespuestaGetRotacion(response, syncResult);
                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                if (error instanceof TimeoutError) {
                                    enviarBroadcast(true, Mensajes.TIME_OUT);
                                } else if (error instanceof NoConnectionError) {
                                    //TODO
                                    enviarBroadcast(true, Mensajes.NO_RED);
                                } else if (error instanceof AuthFailureError) {
                                    //TODO
                                } else if (error instanceof ServerError) {
                                    //TODO
                                    enviarBroadcast(true, Mensajes.SEVER_ERROR);
                                } else if (error instanceof NetworkError) {
                                    //TODO
                                    enviarBroadcast(true, Mensajes.RED_ERROR);
                                } else if (error instanceof ParseError) {
                                    //TODO
                                }
                            }
                        });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(MY_DEFAULT_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);
    }

    private void procesarRespuestaGetRotacion(JSONObject response, SyncResult syncResult) {
        try {
            // Obtener atributo "estado"
            String estado = response.getString(Constantes.ESTADO);

            switch (estado) {
                case Constantes.SUCCESS: // EXITO
                    actualizarDatosLocalesRotacion(response, syncResult);
                    break;
                case Constantes.FAILED: // FALLIDO
                    String mensaje = response.getString(Constantes.MENSAJE);
                    enviarBroadcast(true, mensaje);
                    Log.i(TAG, mensaje);
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void actualizarDatosLocalesRotacion(JSONObject response, SyncResult syncResult) {
        JSONArray loginResult = null;

        try {
            // Obtener array "gastos"
            loginResult = response.getJSONArray(Constantes.ROTACION_RESULT);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Parsear con Gson
        BaseRotacion[] res = gson.fromJson(loginResult != null ? loginResult.toString() : null, BaseRotacion[].class);
        List<BaseRotacion> data = Arrays.asList(res);

        // Lista para recolección de operaciones pendientes
        ArrayList<ContentProviderOperation> ops = new ArrayList<>();

        // Tabla hash para recibir los datos entrantes
        HashMap<String, BaseRotacion> expenseMap = new HashMap<>();
        for (BaseRotacion e : data) {
            expenseMap.put(e.id, e);
        }

        // Consultar registros remotos actuales
        Uri uri = ContractRotacion.CONTENT_URI;
        String select = Constantes.ID_REMOTA + " IS NOT NULL";
        Cursor c = resolver.query(uri, Projection.PROJECTION_ROTACION, select, null, null);
        assert c != null;

        Log.i(TAG, "Se encontraron " + c.getCount() + " registros locales.");

        // Encontrar datos obsoletos
        String id;
        //String codigo_producto;
        String categoria;
        String subcategoria;
        String marca;
        String producto;
        String promocional;
        String mecanica;
        String peso;
        String tipo;
        String plataforma;

        while (c.moveToNext()) {
            syncResult.stats.numEntries++;
            id = c.getString(Projection.COLUMNA_ID_REMOTA);
            //codigo_producto = c.getString(Projection.CODIGO_PRODUCTO);
            categoria = c.getString(Projection.ROT_CATEGORIA);
            subcategoria = c.getString(Projection.ROT_SUBCATEGORIA);
            marca = c.getString(Projection.ROT_MARCA);
            producto = c.getString(Projection.ROT_PRODUCTO);
            promocional = c.getString(Projection.ROT_PROMOCIONAL);
            mecanica = c.getString(Projection.ROT_MECANICA);
            peso = c.getString(Projection.ROT_PESO);
            tipo = c.getString(Projection.ROT_TIPO);
            plataforma = c.getString(Projection.ROT_PLATAFORMA);

            BaseRotacion match = expenseMap.get(id);

            if (match != null) {
                // Esta entrada existe, por lo que se remueve del mapeado
                expenseMap.remove(id);

                Uri existingUri = ContractRotacion.CONTENT_URI.buildUpon()
                        .appendPath(id).build();

                // Comprobar si el gasto necesita ser actualizado
                //boolean b=match.codigo_producto!=null && !match.codigo_producto.equals(codigo_producto);
                boolean b1 = match.categoria != null && !match.categoria.equals(categoria);
                boolean b2 = match.subcategoria != null && !match.subcategoria.equals(subcategoria);
                boolean b3 = match.marca != null && !match.marca.equals(marca);
                boolean b4 = match.producto != null && !match.producto.equals(producto);
                boolean b5 = match.promocional != null && !match.promocional.equals(promocional);
                boolean b6 = match.mecanica != null && !match.mecanica.equals(mecanica);
                boolean b7 = match.peso != null && !match.peso.equals(peso);
                boolean b8 = match.tipo != null && !match.tipo.equals(tipo);
                boolean b9 = match.plataforma != null && !match.plataforma.equals(plataforma);

                if (b1 || b2 || b3 || b4 || b5 || b6 || b7 || b8 || b9) {

                    Log.i(TAG, "Programando actualización de: " + existingUri);

                    ops.add(ContentProviderOperation.newUpdate(existingUri)
                            //.withValue(ContractPortafolioProductos.Columnas.CODIGO_PRODUCTO,match.codigo_producto)
                            .withValue(ContractRotacion.Columnas.CATEGORIA, match.categoria)
                            .withValue(ContractRotacion.Columnas.SUBCATEGORIA, match.subcategoria)
                            .withValue(ContractRotacion.Columnas.MARCA, match.marca)
                            .withValue(ContractRotacion.Columnas.PRODUCTO, match.producto)
                            .withValue(ContractRotacion.Columnas.PROMOCIONAL, match.promocional)
                            .withValue(ContractRotacion.Columnas.MECANICA, match.mecanica)
                            .withValue(ContractRotacion.Columnas.PESO, match.peso)
                            .withValue(ContractRotacion.Columnas.TIPO, match.tipo)
                            .withValue(ContractRotacion.Columnas.PLATAFORMA, match.plataforma)
                            .build());
                    syncResult.stats.numUpdates++;
                } else {
                    Log.i(TAG, "No hay acciones para este registro: " + existingUri);
                }
            } else {
                // Debido a que la entrada no existe, es removida de la base de datos
                Uri deleteUri = ContractRotacion.CONTENT_URI.buildUpon()
                        .appendPath(id).build();
                Log.i(TAG, "Programando eliminación de: " + deleteUri);
                ops.add(ContentProviderOperation.newDelete(deleteUri).build());
                syncResult.stats.numDeletes++;
            }
        }
        c.close();

        // Insertar items resultantes
        for (BaseRotacion e : expenseMap.values()) {
            Log.i(TAG, "Programando inserción de Tabla Rotacion en Base Local: " + e.id);
            ops.add(ContentProviderOperation.newInsert(ContractRotacion.CONTENT_URI)
                    .withValue(Constantes.ID_REMOTA, e.id) //Esta clave importante!!! ForeignKey del servidor
                    //.withValue(ContractPortafolioProductos.Columnas.CODIGO_PRODUCTO,e.codigo_producto)
                    .withValue(ContractRotacion.Columnas.CATEGORIA, e.categoria)
                    .withValue(ContractRotacion.Columnas.SUBCATEGORIA, e.subcategoria)
                    .withValue(ContractRotacion.Columnas.MARCA, e.marca)
                    .withValue(ContractRotacion.Columnas.PRODUCTO, e.producto)
                    .withValue(ContractRotacion.Columnas.PROMOCIONAL, e.promocional)
                    .withValue(ContractRotacion.Columnas.MECANICA, e.mecanica)
                    .withValue(ContractRotacion.Columnas.PESO, e.peso)
                    .withValue(ContractRotacion.Columnas.TIPO, e.tipo)
                    .withValue(ContractRotacion.Columnas.PLATAFORMA, e.plataforma)
                    .build());
            syncResult.stats.numInserts++;
        }

        if (syncResult.stats.numInserts > 0 ||
                syncResult.stats.numUpdates > 0 ||
                syncResult.stats.numDeletes > 0) {
            Log.i(TAG, "Aplicando operaciones...");
            try {
                resolver.applyBatch(Constantes.AUTHORITY, ops);
            } catch (RemoteException | OperationApplicationException e) {
                e.printStackTrace();
            }
            resolver.notifyChange(
                    ContractRotacion.CONTENT_URI,
                    null,
                    false);
//            Toast.makeText(getContext(),"Sincronización de BasePharmaValue finalizada.",Toast.LENGTH_SHORT).show();
            Log.i(TAG, "Sincronización de tabla Rotacion finalizada");
            enviarBroadcast(true, Mensajes.SYNC_FINALIZADA_ROTACION);

        } else {
            Log.i(TAG, "No se requiere sincronización de tabla Rotacion");
            enviarBroadcast(true, Mensajes.SYNC_NOREQUERIDA + "Rotacion");
        }

    }

    /*POP SUGERIDO*/
  /*  public void realizarSincronizacionLocalPopSugerido(final SyncResult syncResult, String operatorName) {
        Log.i(TAG, "Realizando Sincronizacion Local de Pop Sugerido." + operatorName);
        String operators = operatorName;
        HashMap<String, String> map = new HashMap<>();// Mapeo previo

        map.put("operators", operators);

        // Crear nuevo objeto Json basado en el mapa
        JSONObject jobject = new JSONObject(map);
        //GET METHOD
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, Constantes.GET_POPSUGERIDO, jobject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        procesarRespuestaGetPopSugerido(response, syncResult);
                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                if (error instanceof TimeoutError) {
                                    enviarBroadcast(true, Mensajes.TIME_OUT);
                                } else if (error instanceof NoConnectionError) {
                                    //TODO
                                    enviarBroadcast(true, Mensajes.NO_RED);
                                }else if (error instanceof AuthFailureError) {
                                    //TODO
                                } else if (error instanceof ServerError) {
                                    //TODO
                                    enviarBroadcast(true, Mensajes.SEVER_ERROR);
                                } else if (error instanceof NetworkError) {
                                    //TODO
                                    enviarBroadcast(true, Mensajes.RED_ERROR);
                                } else if (error instanceof ParseError) {
                                    //TODO
                                }
                            }
                        });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(MY_DEFAULT_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);
    }

    private void procesarRespuestaGetPopSugerido(JSONObject response, SyncResult syncResult) {
        try {
            // Obtener atributo "estado"
            String estado = response.getString(Constantes.ESTADO);

            switch (estado) {
                case Constantes.SUCCESS: // EXITO
                    actualizarDatosLocalesPopSugerido(response, syncResult);
                    break;
                case Constantes.FAILED: // FALLIDO
                    String mensaje = response.getString(Constantes.MENSAJE);
                    enviarBroadcast(true, mensaje);
                    Log.i(TAG, mensaje);
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void actualizarDatosLocalesPopSugerido(JSONObject response, SyncResult syncResult) {
        JSONArray loginResult = null;

        try {
            // Obtener array "gastos"
            loginResult = response.getJSONArray(Constantes.POPSUGERIDORESULT);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Parsear con Gson
        BasePopSugerido[] res = gson.fromJson(loginResult != null ? loginResult.toString() : null, BasePopSugerido[].class);
        List<BasePopSugerido> data = Arrays.asList(res);

        // Lista para recolección de operaciones pendientes
        ArrayList<ContentProviderOperation> ops = new ArrayList<>();

        // Tabla hash para recibir los datos entrantes
        HashMap<String, BasePopSugerido> expenseMap = new HashMap<>();
        for (BasePopSugerido e : data) {
            expenseMap.put(e.id, e);
        }

        // Consultar registros remotos actuales
        Uri uri = ContractPopSugerido.CONTENT_URI;
        String select = Constantes.ID_REMOTA + " IS NOT NULL";
        Cursor c = resolver.query(uri, Projection.PROJECTION_POPSUGERIDO, select, null, null);
        assert c != null;

        Log.i(TAG, "Se encontraron " + c.getCount() + " registros locales.");

        // Encontrar datos obsoletos
        String id;
        //String codigo_producto;
        String canal;
        String pop_sugerido;

        while (c.moveToNext()) {
            syncResult.stats.numEntries++;
            id = c.getString(Projection.COLUMNA_ID_REMOTA);
            //codigo_producto = c.getString(Projection.CODIGO_PRODUCTO);
            canal = c.getString(Projection.COLUMNA_CANAL_POP);
            pop_sugerido = c.getString(Projection.COLUMNA_POP_SUGERIDO);

            BasePopSugerido match = expenseMap.get(id);

            if (match != null) {
                // Esta entrada existe, por lo que se remueve del mapeado
                expenseMap.remove(id);

                Uri existingUri = ContractPopSugerido.CONTENT_URI.buildUpon()
                        .appendPath(id).build();

                // Comprobar si el gasto necesita ser actualizado
                //boolean b=match.codigo_producto!=null && !match.codigo_producto.equals(codigo_producto);
                boolean b1=match.canal !=null && !match.canal.equals(canal);
                boolean b2=match.pop_sugerido !=null && !match.pop_sugerido.equals(pop_sugerido);

                if (b1 || b2 ) {

                    Log.i(TAG, "Programando actualización de: " + existingUri);

                    ops.add(ContentProviderOperation.newUpdate(existingUri)
                            //.withValue(ContractPortafolioProductos.Columnas.CODIGO_PRODUCTO,match.codigo_producto)
                            .withValue(ContractPopSugerido.Columnas.CANAL,match.canal)
                            .withValue(ContractPopSugerido.Columnas.POP_SUGERIDO,match.pop_sugerido)
                            .build());
                    syncResult.stats.numUpdates++;
                } else {
                    Log.i(TAG, "No hay acciones para este registro: " + existingUri);
                }
            } else {
                // Debido a que la entrada no existe, es removida de la base de datos
                Uri deleteUri = ContractPopSugerido.CONTENT_URI.buildUpon()
                        .appendPath(id).build();
                Log.i(TAG, "Programando eliminación de: " + deleteUri);
                ops.add(ContentProviderOperation.newDelete(deleteUri).build());
                syncResult.stats.numDeletes++;
            }
        }
        c.close();

        // Insertar items resultantes
        for (BasePopSugerido e : expenseMap.values()) {
            Log.i(TAG, "Programando inserción de Tabla Pop Sugerido en Base Local: " + e.id);
            ops.add(ContentProviderOperation.newInsert(ContractPopSugerido.CONTENT_URI)
                    .withValue(Constantes.ID_REMOTA , e.id) //Esta clave importante!!! ForeignKey del servidor
                    //.withValue(ContractPortafolioProductos.Columnas.CODIGO_PRODUCTO,e.codigo_producto)
                    .withValue(ContractPopSugerido.Columnas.CANAL,e.canal)
                    .withValue(ContractPopSugerido.Columnas.POP_SUGERIDO,e.pop_sugerido)
                    .build());
            syncResult.stats.numInserts++;
        }

        if (syncResult.stats.numInserts > 0 ||
                syncResult.stats.numUpdates > 0 ||
                syncResult.stats.numDeletes > 0) {
            Log.i(TAG, "Aplicando operaciones...");
            try {
                resolver.applyBatch(Constantes.AUTHORITY, ops);
            } catch (RemoteException | OperationApplicationException e) {
                e.printStackTrace();
            }
            resolver.notifyChange(
                    ContractPopSugerido.CONTENT_URI,
                    null,
                    false);
//            Toast.makeText(getContext(),"Sincronización de BasePharmaValue finalizada.",Toast.LENGTH_SHORT).show();
            Log.i(TAG, "Sincronización de tabla pop_sugerido finalizada");
            enviarBroadcast(true, Mensajes.SYNC_FINALIZADA_POPSUGERIDO);

        } else {
           // Log.i(TAG, "No se requiere sincronización de tabla pop_sugerido");
            enviarBroadcast(true, Mensajes.SYNC_NOREQUERIDA + "pop_sugerido");
        }

    }*/

    /**
     * POPSUGERIDO
     *
     * @param syncResult
     * @param operatorName
     */
    public void realizarSincronizacionLocalPopSugerido(final SyncResult syncResult, String operatorName) {
        Log.i(TAG, "Realizando Sincronizacion Local de Pop_sugerido." + operatorName);
        String operators = operatorName;
        HashMap<String, String> map = new HashMap<>();// Mapeo previo

        map.put("operators", operators);

        // Crear nuevo objeto Json basado en el mapa
        JSONObject jobject = new JSONObject(map);
        //GET METHOD
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, Constantes.GET_POPSUGERIDO, jobject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        procesarRespuestaGetPopSugeridos(response, syncResult);
                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                if (error instanceof TimeoutError) {
                                    enviarBroadcast(true, Mensajes.TIME_OUT);
                                } else if (error instanceof NoConnectionError) {
                                    //TODO
                                    enviarBroadcast(true, Mensajes.NO_RED);
                                } else if (error instanceof AuthFailureError) {
                                    //TODO
                                } else if (error instanceof ServerError) {
                                    //TODO
                                    enviarBroadcast(true, Mensajes.SEVER_ERROR);
                                } else if (error instanceof NetworkError) {
                                    //TODO
                                    enviarBroadcast(true, Mensajes.RED_ERROR);
                                } else if (error instanceof ParseError) {
                                    //TODO
                                }
                            }
                        });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(MY_DEFAULT_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);
    }

    private void procesarRespuestaGetPopSugeridos(JSONObject response, SyncResult syncResult) {
        try {
            // Obtener atributo "estado"
            String estado = response.getString(Constantes.ESTADO);

            switch (estado) {
                case Constantes.SUCCESS: // EXITO
                    actualizarDatosLocalesPopSugeridos(response, syncResult);
                    break;
                case Constantes.FAILED: // FALLIDO
                    String mensaje = response.getString(Constantes.MENSAJE);
                    enviarBroadcast(true, mensaje);
                    Log.i(TAG, mensaje);
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void actualizarDatosLocalesPopSugeridos(JSONObject response, SyncResult syncResult) {
        JSONArray loginResult = null;

        try {
            // Obtener array "gastos"
            loginResult = response.getJSONArray(Constantes.POPSUGERIDO_RESULT);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Parsear con Gson
        BasePopSugerido[] res = gson.fromJson(loginResult != null ? loginResult.toString() : null, BasePopSugerido[].class);
        List<BasePopSugerido> data = Arrays.asList(res);

        // Lista para recolección de operaciones pendientes
        ArrayList<ContentProviderOperation> ops = new ArrayList<>();

        // Tabla hash para recibir los datos entrantes
        HashMap<String, BasePopSugerido> expenseMap = new HashMap<>();
        for (BasePopSugerido e : data) {
            expenseMap.put(e.id, e);
        }

        // Consultar registros remotos actuales
        Uri uri = ContractPopSugerido.CONTENT_URI;
        String select = Constantes.ID_REMOTA + " IS NOT NULL";
        Cursor c = resolver.query(uri, Projection.PROJECTION_POPSUGERIDO, select, null, null);
        assert c != null;

        Log.i(TAG, "Se encontraron " + c.getCount() + " registros locales.");

        // Encontrar datos obsoletos
        String id;
        //String codigo_producto;
        String canal;
        String codigo_pdv;
        String pop_sugerido;

        while (c.moveToNext()) {
            syncResult.stats.numEntries++;
            id = c.getString(Projection.COLUMNA_ID_REMOTA);
            //codigo_producto = c.getString(Projection.CODIGO_PRODUCTO);
            canal = c.getString(Projection.COLUMNA_CANAL_POP);
            codigo_pdv = c.getString(Projection.COLUMNA_CODIGO_PDV_POP);
            pop_sugerido = c.getString(Projection.COLUMNA_POP_SUGERIDO);

            BasePopSugerido match = expenseMap.get(id);

            if (match != null) {
                // Esta entrada existe, por lo que se remueve del mapeado
                expenseMap.remove(id);

                Uri existingUri = ContractPopSugerido.CONTENT_URI.buildUpon()
                        .appendPath(id).build();

                // Comprobar si el gasto necesita ser actualizado
                //boolean b=match.codigo_producto!=null && !match.codigo_producto.equals(codigo_producto);
                boolean b1 = match.canal != null && !match.canal.equals(canal);
                boolean b2 = match.codigo_pdv != null && !match.codigo_pdv.equals(codigo_pdv);
                boolean b3 = match.pop_sugerido != null && !match.pop_sugerido.equals(pop_sugerido);

                if (b1 || b2 || b3) {

                    Log.i(TAG, "Programando actualización de: " + existingUri);

                    ops.add(ContentProviderOperation.newUpdate(existingUri)
                            //.withValue(ContractPortafolioProductos.Columnas.CODIGO_PRODUCTO,match.codigo_producto)
                            .withValue(ContractPopSugerido.Columnas.CANAL, match.canal)
                            .withValue(ContractPopSugerido.Columnas.CODIGO_PDV, match.codigo_pdv)
                            .withValue(ContractPopSugerido.Columnas.POP_SUGERIDO, match.pop_sugerido)
                            .build());
                    syncResult.stats.numUpdates++;
                } else {
                    Log.i(TAG, "No hay acciones para este registro: " + existingUri);
                }
            } else {
                // Debido a que la entrada no existe, es removida de la base de datos
                Uri deleteUri = ContractPopSugerido.CONTENT_URI.buildUpon()
                        .appendPath(id).build();
                Log.i(TAG, "Programando eliminación de: " + deleteUri);
                ops.add(ContentProviderOperation.newDelete(deleteUri).build());
                syncResult.stats.numDeletes++;
            }
        }
        c.close();

        // Insertar items resultantes
        for (BasePopSugerido e : expenseMap.values()) {
            Log.i(TAG, "Programando inserción de Tabla Pop_sugerido en Base Local: " + e.id);
            ops.add(ContentProviderOperation.newInsert(ContractPopSugerido.CONTENT_URI)
                    .withValue(Constantes.ID_REMOTA, e.id) //Esta clave importante!!! ForeignKey del servidor
                    //.withValue(ContractPortafolioProductos.Columnas.CODIGO_PRODUCTO,e.codigo_producto)
                    .withValue(ContractPopSugerido.Columnas.CANAL, e.canal)
                    .withValue(ContractPopSugerido.Columnas.CODIGO_PDV, e.codigo_pdv)
                    .withValue(ContractPopSugerido.Columnas.POP_SUGERIDO, e.pop_sugerido)
                    .build());
            syncResult.stats.numInserts++;
        }

        if (syncResult.stats.numInserts > 0 ||
                syncResult.stats.numUpdates > 0 ||
                syncResult.stats.numDeletes > 0) {
            Log.i(TAG, "Aplicando operaciones...");
            try {
                resolver.applyBatch(Constantes.AUTHORITY, ops);
            } catch (RemoteException | OperationApplicationException e) {
                e.printStackTrace();
            }
            resolver.notifyChange(
                    ContractPopSugerido.CONTENT_URI,
                    null,
                    false);
//            Toast.makeText(getContext(),"Sincronización de BasePharmaValue finalizada.",Toast.LENGTH_SHORT).show();
            Log.i(TAG, "Sincronización de tabla Pop_sugerido finalizada");
            enviarBroadcast(true, Mensajes.SYNC_FINALIZADA_POPSUGERIDO);

        } else {
            Log.i(TAG, "No se requiere sincronización de tabla Pop_sugerido");
            enviarBroadcast(true, Mensajes.SYNC_NOREQUERIDA + "Pop_sugerido");
        }

    }

    /**
     * COMBO CANJES
     *
     * @param syncResult
     * @param operatorName
     */
    public void realizarSincronizacionLocalComboCanjes(final SyncResult syncResult, String operatorName) {
        Log.i(TAG, "Realizando Sincronizacion Local de Combo Canjes." + operatorName);
        String operators = operatorName;
        HashMap<String, String> map = new HashMap<>();// Mapeo previo

        map.put("operators", operators);

        // Crear nuevo objeto Json basado en el mapa
        JSONObject jobject = new JSONObject(map);
        //GET METHOD
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, Constantes.GET_COMBO_CANJES, jobject,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                procesarRespuestaGetComboCanjes(response, syncResult);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                if (error instanceof TimeoutError) {
                                    enviarBroadcast(true, Mensajes.TIME_OUT);
                                } else if (error instanceof NoConnectionError) {
                                    //TODO
                                    enviarBroadcast(true, Mensajes.NO_RED);
                                } else if (error instanceof AuthFailureError) {
                                    //TODO
                                } else if (error instanceof ServerError) {
                                    //TODO
                                    enviarBroadcast(true, Mensajes.SEVER_ERROR);
                                } else if (error instanceof NetworkError) {
                                    //TODO
                                    enviarBroadcast(true, Mensajes.RED_ERROR);
                                } else if (error instanceof ParseError) {
                                    //TODO
                                }
                            }
                        });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(MY_DEFAULT_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);
    }

    private void procesarRespuestaGetComboCanjes(JSONObject response, SyncResult syncResult) {
        try {
            // Obtener atributo "estado"
            String estado = response.getString(Constantes.ESTADO);

            switch (estado) {
                case Constantes.SUCCESS: // EXITO
                    actualizarDatosLocalesComboCanjes(response, syncResult);
                    break;
                case Constantes.FAILED: // FALLIDO
                    String mensaje = response.getString(Constantes.MENSAJE);
                    enviarBroadcast(true, mensaje);
                    Log.i(TAG, mensaje);
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void actualizarDatosLocalesComboCanjes(JSONObject response, SyncResult syncResult) {
        JSONArray loginResult = null;

        try {
            // Obtener array "gastos"
            loginResult = response.getJSONArray(Constantes.COMBO_CANJES_RESULT);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Parsear con Gson
        BaseCombosCanjes[] res = gson.fromJson(loginResult != null ? loginResult.toString() : null,
                BaseCombosCanjes[].class);
        List<BaseCombosCanjes> data = Arrays.asList(res);

        // Lista para recolección de operaciones pendientes
        ArrayList<ContentProviderOperation> ops = new ArrayList<>();

        // Tabla hash para recibir los datos entrantes
        HashMap<String, BaseCombosCanjes> expenseMap = new HashMap<>();
        for (BaseCombosCanjes e : data) {
            expenseMap.put(e.id, e);
        }

        // Consultar registros remotos actuales
        Uri uri = ContractComboCanjes.CONTENT_URI;
        String select = Constantes.ID_REMOTA + " IS NOT NULL";
        Cursor c = resolver.query(uri, Projection.PROJECTION_COMBO_CANJES, select, null, null);
        assert c != null;

        Log.i(TAG, "Se encontraron " + c.getCount() + " registros locales.");

        // Encontrar datos obsoletos
        String id;
        String tipo_combo;
        String mecanica;

        while (c.moveToNext()) {
            syncResult.stats.numEntries++;
            id = c.getString(Projection.COLUMNA_ID_REMOTA);
            tipo_combo = c.getString(Projection.TIPO_COMBO);
            mecanica = c.getString(Projection.MECANICA);

            BaseCombosCanjes match = expenseMap.get(id);

            if (match != null) {
                // Esta entrada existe, por lo que se remueve del mapeado
                expenseMap.remove(id);

                Uri existingUri = ContractComboCanjes.CONTENT_URI.buildUpon()
                        .appendPath(id).build();

                // Comprobar si el gasto necesita ser actualizado
                //boolean b=match.codigo_producto!=null && !match.codigo_producto.equals(codigo_producto);
                boolean b1 = match.tipo_combo != null && !match.tipo_combo.equals(tipo_combo);
                boolean b2 = match.mecanica != null && !match.mecanica.equals(mecanica);

                if (b1 || b2) {

                    Log.i(TAG, "Programando actualización de: " + existingUri);

                    ops.add(ContentProviderOperation.newUpdate(existingUri)
                            //.withValue(ContractPortafolioProductos.Columnas.CODIGO_PRODUCTO,match.codigo_producto)
                            .withValue(ContractComboCanjes.Columnas.TIPO_COMBO, match.tipo_combo)
                            .withValue(ContractComboCanjes.Columnas.MECANICA, match.mecanica)
                            .build());
                    syncResult.stats.numUpdates++;
                } else {
                    Log.i(TAG, "No hay acciones para este registro: " + existingUri);
                }
            } else {
                // Debido a que la entrada no existe, es removida de la base de datos
                Uri deleteUri = ContractComboCanjes.CONTENT_URI.buildUpon()
                        .appendPath(id).build();
                Log.i(TAG, "Programando eliminación de: " + deleteUri);
                ops.add(ContentProviderOperation.newDelete(deleteUri).build());
                syncResult.stats.numDeletes++;
            }
        }
        c.close();

        // Insertar items resultantes
        for (BaseCombosCanjes e : expenseMap.values()) {
            Log.i(TAG, "Programando inserción de Tabla Combo Canjes en Base Local: " + e.id);
            ops.add(ContentProviderOperation.newInsert(ContractComboCanjes.CONTENT_URI)
                    .withValue(Constantes.ID_REMOTA, e.id) //Esta clave importante!!! ForeignKey del servidor
                    //.withValue(ContractPortafolioProductos.Columnas.CODIGO_PRODUCTO,e.codigo_producto)
                    .withValue(ContractComboCanjes.Columnas.TIPO_COMBO, e.tipo_combo)
                    .withValue(ContractComboCanjes.Columnas.MECANICA, e.mecanica)
                    .build());
            syncResult.stats.numInserts++;
        }

        if (syncResult.stats.numInserts > 0 ||
                syncResult.stats.numUpdates > 0 ||
                syncResult.stats.numDeletes > 0) {
            Log.i(TAG, "Aplicando operaciones...");
            try {
                resolver.applyBatch(Constantes.AUTHORITY, ops);
            } catch (RemoteException | OperationApplicationException e) {
                e.printStackTrace();
            }
            resolver.notifyChange(
                    ContractComboCanjes.CONTENT_URI,
                    null,
                    false);
//            Toast.makeText(getContext(),"Sincronización de BasePharmaValue finalizada.",Toast.LENGTH_SHORT).show();
            Log.i(TAG, "Sincronización de tabla Combo Canjes finalizada");
            enviarBroadcast(true, Mensajes.SYNC_FINALIZADA_COMBO_CANJES);

        } else {
            Log.i(TAG, "No se requiere sincronización de tabla Combo Canjes");
            enviarBroadcast(true, Mensajes.SYNC_NOREQUERIDA + "Combo Canjes");
        }

    }

    /**
     * CAUSALES MCI
     *
     * @param syncResult
     * @param operatorName
     */
    public void realizarSincronizacionLocalCausalesMCI(final SyncResult syncResult, String operatorName) {
        Log.i(TAG, "Realizando Sincronizacion Local de Causales MCI." + operatorName);
        String operators = operatorName;
        HashMap<String, String> map = new HashMap<>();// Mapeo previo

        map.put("operators", operators);

        // Crear nuevo objeto Json basado en el mapa
        JSONObject jobject = new JSONObject(map);
        //GET METHOD
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, Constantes.GET_CAUSALES_MCI, jobject,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                procesarRespuestaGetCausalesMCI(response, syncResult);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                if (error instanceof TimeoutError) {
                                    enviarBroadcast(true, Mensajes.TIME_OUT);
                                } else if (error instanceof NoConnectionError) {
                                    //TODO
                                    enviarBroadcast(true, Mensajes.NO_RED);
                                } else if (error instanceof AuthFailureError) {
                                    //TODO
                                } else if (error instanceof ServerError) {
                                    //TODO
                                    enviarBroadcast(true, Mensajes.SEVER_ERROR);
                                } else if (error instanceof NetworkError) {
                                    //TODO
                                    enviarBroadcast(true, Mensajes.RED_ERROR);
                                } else if (error instanceof ParseError) {
                                    //TODO
                                }
                            }
                        });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(MY_DEFAULT_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);
    }

    private void procesarRespuestaGetCausalesMCI(JSONObject response, SyncResult syncResult) {
        try {
            // Obtener atributo "estado"
            String estado = response.getString(Constantes.ESTADO);

            switch (estado) {
                case Constantes.SUCCESS: // EXITO
                    actualizarDatosLocalesCausalesMCI(response, syncResult);
                    break;
                case Constantes.FAILED: // FALLIDO
                    String mensaje = response.getString(Constantes.MENSAJE);
                    enviarBroadcast(true, mensaje);
                    Log.i(TAG, mensaje);
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void actualizarDatosLocalesCausalesMCI(JSONObject response, SyncResult syncResult) {
        JSONArray loginResult = null;

        try {
            // Obtener array "gastos"
            loginResult = response.getJSONArray(Constantes.CAUSALES_MCI_RESULT);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Parsear con Gson
        BaseCausalesMCI[] res = gson.fromJson(loginResult != null ? loginResult.toString() : null, BaseCausalesMCI[].class);
        List<BaseCausalesMCI> data = Arrays.asList(res);

        // Lista para recolección de operaciones pendientes
        ArrayList<ContentProviderOperation> ops = new ArrayList<>();

        // Tabla hash para recibir los datos entrantes
        HashMap<String, BaseCausalesMCI> expenseMap = new HashMap<>();
        for (BaseCausalesMCI e : data) {
            expenseMap.put(e.id, e);
        }

        // Consultar registros remotos actuales
        Uri uri = ContractCausalesMCI.CONTENT_URI;
        String select = Constantes.ID_REMOTA + " IS NOT NULL";
        Cursor c = resolver.query(uri, Projection.PROJECTION_CAUSALES_MCI, select, null, null);
        assert c != null;

        Log.i(TAG, "Se encontraron " + c.getCount() + " registros locales.");

        // Encontrar datos obsoletos
        String id;
        String causal;

        while (c.moveToNext()) {
            syncResult.stats.numEntries++;
            id = c.getString(Projection.COLUMNA_ID_REMOTA);
            causal = c.getString(Projection.CAUSAL_MCI);

            BaseCausalesMCI match = expenseMap.get(id);

            if (match != null) {
                // Esta entrada existe, por lo que se remueve del mapeado
                expenseMap.remove(id);

                Uri existingUri = ContractCausalesMCI.CONTENT_URI.buildUpon()
                        .appendPath(id).build();

                // Comprobar si el gasto necesita ser actualizado
                boolean b1 = match.causal != null && !match.causal.equals(causal);

                if (b1) {

                    Log.i(TAG, "Programando actualización de: " + existingUri);

                    ops.add(ContentProviderOperation.newUpdate(existingUri)
                            //.withValue(ContractPortafolioProductos.Columnas.CODIGO_PRODUCTO,match.codigo_producto)
                            .withValue(ContractCausalesMCI.Columnas.CAUSAL, match.causal)
                            .build());
                    syncResult.stats.numUpdates++;
                } else {
                    Log.i(TAG, "No hay acciones para este registro: " + existingUri);
                }
            } else {
                // Debido a que la entrada no existe, es removida de la base de datos
                Uri deleteUri = ContractCausalesMCI.CONTENT_URI.buildUpon()
                        .appendPath(id).build();
                Log.i(TAG, "Programando eliminación de: " + deleteUri);
                ops.add(ContentProviderOperation.newDelete(deleteUri).build());
                syncResult.stats.numDeletes++;
            }
        }
        c.close();

        // Insertar items resultantes
        for (BaseCausalesMCI e : expenseMap.values()) {
            Log.i(TAG, "Programando inserción de Tabla Causales MCI en Base Local: " + e.id);
            ops.add(ContentProviderOperation.newInsert(ContractCausalesMCI.CONTENT_URI)
                    .withValue(Constantes.ID_REMOTA, e.id) //Esta clave importante!!! ForeignKey del servidor
                    //.withValue(ContractPortafolioProductos.Columnas.CODIGO_PRODUCTO,e.codigo_producto)
                    .withValue(ContractCausalesMCI.Columnas.CAUSAL, e.causal)
                    .build());
            syncResult.stats.numInserts++;
        }

        if (syncResult.stats.numInserts > 0 ||
                syncResult.stats.numUpdates > 0 ||
                syncResult.stats.numDeletes > 0) {
            Log.i(TAG, "Aplicando operaciones...");
            try {
                resolver.applyBatch(Constantes.AUTHORITY, ops);
            } catch (RemoteException | OperationApplicationException e) {
                e.printStackTrace();
            }
            resolver.notifyChange(
                    ContractCausalesMCI.CONTENT_URI,
                    null,
                    false);
//            Toast.makeText(getContext(),"Sincronización de BasePharmaValue finalizada.",Toast.LENGTH_SHORT).show();
            Log.i(TAG, "Sincronización de tabla Causales MCI finalizada");
            enviarBroadcast(true, Mensajes.SYNC_FINALIZADA_CAUSALES_MCI);

        } else {
            Log.i(TAG, "No se requiere sincronización de tabla Causales MCI");
            enviarBroadcast(true, Mensajes.SYNC_NOREQUERIDA + "Causales MCI");
        }

    }

    /**
     * CAUSALES OSA
     *
     * @param syncResult
     * @param operatorName
     */
    public void realizarSincronizacionLocalCausalesOSA(final SyncResult syncResult, String operatorName) {
        Log.i(TAG, "Realizando Sincronizacion Local de Causales OSA." + operatorName);
        String operators = operatorName;
        HashMap<String, String> map = new HashMap<>();// Mapeo previo

        map.put("operators", operators);

        // Crear nuevo objeto Json basado en el mapa
        JSONObject jobject = new JSONObject(map);
        //GET METHOD
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, Constantes.GET_CAUSALES_OSA, jobject,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                procesarRespuestaGetCausalesOSA(response, syncResult);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                if (error instanceof TimeoutError) {
                                    enviarBroadcast(true, Mensajes.TIME_OUT);
                                } else if (error instanceof NoConnectionError) {
                                    //TODO
                                    enviarBroadcast(true, Mensajes.NO_RED);
                                } else if (error instanceof AuthFailureError) {
                                    //TODO
                                } else if (error instanceof ServerError) {
                                    //TODO
                                    enviarBroadcast(true, Mensajes.SEVER_ERROR);
                                } else if (error instanceof NetworkError) {
                                    //TODO
                                    enviarBroadcast(true, Mensajes.RED_ERROR);
                                } else if (error instanceof ParseError) {
                                    //TODO
                                }
                            }
                        });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(MY_DEFAULT_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);
    }

    private void procesarRespuestaGetCausalesOSA(JSONObject response, SyncResult syncResult) {
        try {
            // Obtener atributo "estado"
            String estado = response.getString(Constantes.ESTADO);

            switch (estado) {
                case Constantes.SUCCESS: // EXITO
                    actualizarDatosLocalesCausalesOSA(response, syncResult);
                    break;
                case Constantes.FAILED: // FALLIDO
                    String mensaje = response.getString(Constantes.MENSAJE);
                    enviarBroadcast(true, mensaje);
                    Log.i(TAG, mensaje);
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void actualizarDatosLocalesCausalesOSA(JSONObject response, SyncResult syncResult) {
        JSONArray loginResult = null;

        try {
            // Obtener array "gastos"
            loginResult = response.getJSONArray(Constantes.CAUSALES_OSA_RESULT);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Parsear con Gson
        BaseCausalesOSA[] res = gson.fromJson(loginResult != null ? loginResult.toString() : null, BaseCausalesOSA[].class);
        List<BaseCausalesOSA> data = Arrays.asList(res);

        // Lista para recolección de operaciones pendientes
        ArrayList<ContentProviderOperation> ops = new ArrayList<>();

        // Tabla hash para recibir los datos entrantes
        HashMap<String, BaseCausalesOSA> expenseMap = new HashMap<>();
        for (BaseCausalesOSA e : data) {
            expenseMap.put(e.id, e);
        }

        // Consultar registros remotos actuales
        Uri uri = ContractCausalesOSA.CONTENT_URI;
        String select = Constantes.ID_REMOTA + " IS NOT NULL";
        Cursor c = resolver.query(uri, Projection.PROJECTION_CAUSALES_OSA, select, null, null);
        assert c != null;

        Log.i(TAG, "Se encontraron " + c.getCount() + " registros locales.");

        // Encontrar datos obsoletos
        String id;
        String canal;
        String responsable;
        String causal;

        while (c.moveToNext()) {
            syncResult.stats.numEntries++;
            id = c.getString(Projection.COLUMNA_ID_REMOTA);
            canal = c.getString(Projection.CANAL_OSA);
            responsable = c.getString(Projection.RESPONSABLE_OSA);
            causal = c.getString(Projection.CAUSAL_OSA);

            BaseCausalesOSA match = expenseMap.get(id);

            if (match != null) {
                // Esta entrada existe, por lo que se remueve del mapeado
                expenseMap.remove(id);

                Uri existingUri = ContractCausalesOSA.CONTENT_URI.buildUpon()
                        .appendPath(id).build();

                // Comprobar si el gasto necesita ser actualizado
                boolean b1 = match.canal != null && !match.canal.equals(canal);
                boolean b2 = match.responsable != null && !match.responsable.equals(causal);
                boolean b3 = match.causal != null && !match.causal.equals(causal);

                if (b1 || b2 || b3) {

                    Log.i(TAG, "Programando actualización de: " + existingUri);

                    ops.add(ContentProviderOperation.newUpdate(existingUri)
                            //.withValue(ContractPortafolioProductos.Columnas.CODIGO_PRODUCTO,match.codigo_producto)
                            .withValue(ContractCausalesOSA.Columnas.CANAL, match.canal)
                            .withValue(ContractCausalesOSA.Columnas.RESPONSABLE, match.responsable)
                            .withValue(ContractCausalesOSA.Columnas.CAUSAL, match.causal)
                            .build());
                    syncResult.stats.numUpdates++;
                } else {
                    Log.i(TAG, "No hay acciones para este registro: " + existingUri);
                }
            } else {
                // Debido a que la entrada no existe, es removida de la base de datos
                Uri deleteUri = ContractCausalesOSA.CONTENT_URI.buildUpon()
                        .appendPath(id).build();
                Log.i(TAG, "Programando eliminación de: " + deleteUri);
                ops.add(ContentProviderOperation.newDelete(deleteUri).build());
                syncResult.stats.numDeletes++;
            }
        }
        c.close();

        // Insertar items resultantes
        for (BaseCausalesOSA e : expenseMap.values()) {
            Log.i(TAG, "Programando inserción de Tabla Causales OSA en Base Local: " + e.id);
            ops.add(ContentProviderOperation.newInsert(ContractCausalesOSA.CONTENT_URI)
                    .withValue(Constantes.ID_REMOTA, e.id) //Esta clave importante!!! ForeignKey del servidor
                    //.withValue(ContractPortafolioProductos.Columnas.CODIGO_PRODUCTO,e.codigo_producto)
                    .withValue(ContractCausalesOSA.Columnas.CANAL, e.canal)
                    .withValue(ContractCausalesOSA.Columnas.RESPONSABLE, e.responsable)
                    .withValue(ContractCausalesOSA.Columnas.CAUSAL, e.causal)
                    .build());
            syncResult.stats.numInserts++;
        }

        if (syncResult.stats.numInserts > 0 ||
                syncResult.stats.numUpdates > 0 ||
                syncResult.stats.numDeletes > 0) {
            Log.i(TAG, "Aplicando operaciones...");
            try {
                resolver.applyBatch(Constantes.AUTHORITY, ops);
            } catch (RemoteException | OperationApplicationException e) {
                e.printStackTrace();
            }
            resolver.notifyChange(
                    ContractCausalesOSA.CONTENT_URI,
                    null,
                    false);
//            Toast.makeText(getContext(),"Sincronización de BasePharmaValue finalizada.",Toast.LENGTH_SHORT).show();
            Log.i(TAG, "Sincronización de tabla Causales OSA finalizada");
            enviarBroadcast(true, Mensajes.SYNC_FINALIZADA_CAUSALES_OSA);

        } else {
            Log.i(TAG, "No se requiere sincronización de tabla Causales OSA");
            enviarBroadcast(true, Mensajes.SYNC_NOREQUERIDA + "Causales OSA");
        }

    }

    /**
     * MATERIALES ALERTAS
     *
     * @param syncResult
     * @param operatorName
     */
    public void realizarSincronizacionLocalMaterialesAlertas(final SyncResult syncResult, String operatorName) {
        Log.i(TAG, "Realizando Sincronizacion Local de Materiales Alertas." + operatorName);
        String operators = operatorName;
        HashMap<String, String> map = new HashMap<>();// Mapeo previo

        map.put("operators", operators);

        // Crear nuevo objeto Json basado en el mapa
        JSONObject jobject = new JSONObject(map);
        //GET METHOD
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, Constantes.GET_MATERIALES_ALERTAS, jobject,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                procesarRespuestaGetMaterialesAlertas(response, syncResult);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                if (error instanceof TimeoutError) {
                                    enviarBroadcast(true, Mensajes.TIME_OUT);
                                } else if (error instanceof NoConnectionError) {
                                    //TODO
                                    enviarBroadcast(true, Mensajes.NO_RED);
                                } else if (error instanceof AuthFailureError) {
                                    //TODO
                                } else if (error instanceof ServerError) {
                                    //TODO
                                    enviarBroadcast(true, Mensajes.SEVER_ERROR);
                                } else if (error instanceof NetworkError) {
                                    //TODO
                                    enviarBroadcast(true, Mensajes.RED_ERROR);
                                } else if (error instanceof ParseError) {
                                    //TODO
                                }
                            }
                        });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(MY_DEFAULT_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);
    }

    private void procesarRespuestaGetMaterialesAlertas(JSONObject response, SyncResult syncResult) {
        try {
            // Obtener atributo "estado"
            String estado = response.getString(Constantes.ESTADO);

            switch (estado) {
                case Constantes.SUCCESS: // EXITO
                    actualizarDatosLocalesMaterialesAlertas(response, syncResult);
                    break;
                case Constantes.FAILED: // FALLIDO
                    String mensaje = response.getString(Constantes.MENSAJE);
                    enviarBroadcast(true, mensaje);
                    Log.i(TAG, mensaje);
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void actualizarDatosLocalesMaterialesAlertas(JSONObject response, SyncResult syncResult) {
        JSONArray loginResult = null;

        try {
            // Obtener array "gastos"
            loginResult = response.getJSONArray(Constantes.MATERIALES_ALERTAS_RESULT);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Parsear con Gson
        BaseAlertas[] res = gson.fromJson(loginResult != null ? loginResult.toString() : null, BaseAlertas[].class);
        List<BaseAlertas> data = Arrays.asList(res);

        // Lista para recolección de operaciones pendientes
        ArrayList<ContentProviderOperation> ops = new ArrayList<>();

        // Tabla hash para recibir los datos entrantes
        HashMap<String, BaseAlertas> expenseMap = new HashMap<>();
        for (BaseAlertas e : data) {
            expenseMap.put(e.id, e);
        }

        // Consultar registros remotos actuales
        Uri uri = ContractAlertas.CONTENT_URI;
        String select = Constantes.ID_REMOTA + " IS NOT NULL";
        Cursor c = resolver.query(uri, Projection.PROJECTION_MATERIALES_ALERTAS, select, null, null);
        assert c != null;

        Log.i(TAG, "Se encontraron " + c.getCount() + " registros locales.");

        // Encontrar datos obsoletos
        String id;
        String tipo_alerta;
        String categoria_material;
        String material;

        while (c.moveToNext()) {
            syncResult.stats.numEntries++;
            id = c.getString(Projection.COLUMNA_ID_REMOTA);
            tipo_alerta = c.getString(Projection.TIPO_ALERTA);
            categoria_material = c.getString(Projection.CATEGORIA_MATERIAL);
            material = c.getString(Projection.MATERIALES);

            BaseAlertas match = expenseMap.get(id);

            if (match != null) {
                // Esta entrada existe, por lo que se remueve del mapeado
                expenseMap.remove(id);

                Uri existingUri = ContractAlertas.CONTENT_URI.buildUpon()
                        .appendPath(id).build();

                // Comprobar si el gasto necesita ser actualizado
                boolean b1 = match.tipo_alerta != null && !match.tipo_alerta.equals(tipo_alerta);
                boolean b2 = match.categoria != null && !match.categoria.equals(categoria_material);
                boolean b3 = match.material != null && !match.material.equals(material);

                if (b1 || b2 || b3) {

                    Log.i(TAG, "Programando actualización de: " + existingUri);

                    ops.add(ContentProviderOperation.newUpdate(existingUri)
                            //.withValue(ContractPortafolioProductos.Columnas.CODIGO_PRODUCTO,match.codigo_producto)
                            .withValue(ContractAlertas.Columnas.TIPO_ALERTA, match.tipo_alerta)
                            .withValue(ContractAlertas.Columnas.CATEGORIA, match.categoria)
                            .withValue(ContractAlertas.Columnas.MATERIAL, match.material)
                            .build());
                    syncResult.stats.numUpdates++;
                } else {
                    Log.i(TAG, "No hay acciones para este registro: " + existingUri);
                }
            } else {
                // Debido a que la entrada no existe, es removida de la base de datos
                Uri deleteUri = ContractAlertas.CONTENT_URI.buildUpon()
                        .appendPath(id).build();
                Log.i(TAG, "Programando eliminación de: " + deleteUri);
                ops.add(ContentProviderOperation.newDelete(deleteUri).build());
                syncResult.stats.numDeletes++;
            }
        }
        c.close();

        // Insertar items resultantes
        for (BaseAlertas e : expenseMap.values()) {
            Log.i(TAG, "Programando inserción de Tabla Materiales Alertas en Base Local: " + e.id);
            ops.add(ContentProviderOperation.newInsert(ContractAlertas.CONTENT_URI)
                    .withValue(Constantes.ID_REMOTA, e.id) //Esta clave importante!!! ForeignKey del servidor
                    //.withValue(ContractPortafolioProductos.Columnas.CODIGO_PRODUCTO,e.codigo_producto)
                    .withValue(ContractAlertas.Columnas.TIPO_ALERTA, e.tipo_alerta)
                    .withValue(ContractAlertas.Columnas.CATEGORIA, e.categoria)
                    .withValue(ContractAlertas.Columnas.MATERIAL, e.material)
                    .build());
            syncResult.stats.numInserts++;
        }

        if (syncResult.stats.numInserts > 0 ||
                syncResult.stats.numUpdates > 0 ||
                syncResult.stats.numDeletes > 0) {
            Log.i(TAG, "Aplicando operaciones...");
            try {
                resolver.applyBatch(Constantes.AUTHORITY, ops);
            } catch (RemoteException | OperationApplicationException e) {
                e.printStackTrace();
            }
            resolver.notifyChange(
                    ContractAlertas.CONTENT_URI,
                    null,
                    false);
//            Toast.makeText(getContext(),"Sincronización de BasePharmaValue finalizada.",Toast.LENGTH_SHORT).show();
            Log.i(TAG, "Sincronización de tabla Materiales Alertas finalizada");
            enviarBroadcast(true, Mensajes.SYNC_FINALIZADA_MATERIALES_ALERTAS);

        } else {
            Log.i(TAG, "No se requiere sincronización de tabla Materiales Alertas");
            enviarBroadcast(true, Mensajes.SYNC_NOREQUERIDA + "Materiales Alertas");
        }

    }

    /**
     * PDI
     *
     * @param syncResult
     * @param operatorName
     */
    public void realizarSincronizacionLocalPDI(final SyncResult syncResult, String operatorName) {
        Log.i(TAG, "Realizando Sincronizacion Local de PDI." + operatorName);
        String operators = operatorName;
        HashMap<String, String> map = new HashMap<>();// Mapeo previo

        map.put("operators", operators);

        // Crear nuevo objeto Json basado en el mapa
        JSONObject jobject = new JSONObject(map);
        System.out.println("SYNC PDI: " + jobject);
        //GET METHOD
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, Constantes.GET_PDI, jobject,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                procesarRespuestaGetPDI(response, syncResult);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                if (error instanceof TimeoutError) {
                                    enviarBroadcast(true, Mensajes.TIME_OUT);
                                } else if (error instanceof NoConnectionError) {
                                    //TODO
                                    enviarBroadcast(true, Mensajes.NO_RED);
                                } else if (error instanceof AuthFailureError) {
                                    //TODO
                                } else if (error instanceof ServerError) {
                                    //TODO
                                    enviarBroadcast(true, Mensajes.SEVER_ERROR);
                                } else if (error instanceof NetworkError) {
                                    //TODO
                                    enviarBroadcast(true, Mensajes.RED_ERROR);
                                } else if (error instanceof ParseError) {
                                    //TODO
                                }
                            }
                        });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(MY_DEFAULT_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);
    }

    private void procesarRespuestaGetPDI(JSONObject response, SyncResult syncResult) {
        try {
            // Obtener atributo "estado"
            String estado = response.getString(Constantes.ESTADO);

            switch (estado) {
                case Constantes.SUCCESS: // EXITO
                    actualizarDatosLocalesPDI(response, syncResult);
                    break;
                case Constantes.FAILED: // FALLIDO
                    String mensaje = response.getString(Constantes.MENSAJE);
                    enviarBroadcast(true, mensaje);
                    Log.i(TAG, mensaje);
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void actualizarDatosLocalesPDI(JSONObject response, SyncResult syncResult) {
        JSONArray loginResult = null;

        try {
            // Obtener array "gastos"
            loginResult = response.getJSONArray(Constantes.PDI_RESULT);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Parsear con Gson
        BasePDI[] res = gson.fromJson(loginResult != null ? loginResult.toString() : null, BasePDI[].class);
        List<BasePDI> data = Arrays.asList(res);

        // Lista para recolección de operaciones pendientes
        ArrayList<ContentProviderOperation> ops = new ArrayList<>();

        // Tabla hash para recibir los datos entrantes
        HashMap<String, BasePDI> expenseMap = new HashMap<>();
        for (BasePDI e : data) {
            expenseMap.put(e.id, e);
        }

        // Consultar registros remotos actuales
        Uri uri = ContractPDI.CONTENT_URI;
        String select = Constantes.ID_REMOTA + " IS NOT NULL";
        Cursor c = resolver.query(uri, Projection.PROJECTION_PDI, select, null, null);
        assert c != null;

        Log.i(TAG, "Se encontraron " + c.getCount() + " registros locales.");

        // Encontrar datos obsoletos
        String id;
        String canal;
        String categoria;
        String subcategoria;
        String marca;
        String objetivo;
        String plataforma;

        while (c.moveToNext()) {
            syncResult.stats.numEntries++;
            id = c.getString(Projection.COLUMNA_ID_REMOTA);
            canal = c.getString(Projection.CANAL_PDI);
            categoria = c.getString(Projection.CATEGORIA_PDI);
            subcategoria = c.getString(Projection.SUBCATEGORIA_PDI);
            marca = c.getString(Projection.MARCA_PDI);
            objetivo = c.getString(Projection.OBJETIVO_PDI);
            plataforma = c.getString(Projection.PLATAFORMA_PDI);

            BasePDI match = expenseMap.get(id);

            if (match != null) {
                // Esta entrada existe, por lo que se remueve del mapeado
                expenseMap.remove(id);

                Uri existingUri = ContractPDI.CONTENT_URI.buildUpon()
                        .appendPath(id).build();

                // Comprobar si el gasto necesita ser actualizado
                boolean b1 = match.canal != null && !match.canal.equals(canal);
                boolean b2 = match.categoria != null && !match.categoria.equals(categoria);
                boolean b3 = match.subcategoria != null && !match.subcategoria.equals(subcategoria);
                boolean b4 = match.marca != null && !match.marca.equals(marca);
                boolean b5 = match.objetivo != null && !match.objetivo.equals(objetivo);
                boolean b6 = match.plataforma != null && !match.plataforma.equals(plataforma);

                if (b1 || b2 || b3 || b4 || b5 || b6) {

                    Log.i(TAG, "Programando actualización de: " + existingUri);

                    ops.add(ContentProviderOperation.newUpdate(existingUri)
                            //.withValue(ContractPortafolioProductos.Columnas.CODIGO_PRODUCTO,match.codigo_producto)
                            .withValue(ContractPDI.Columnas.CANAL, match.canal)
                            .withValue(ContractPDI.Columnas.CATEGORIA, match.categoria)
                            .withValue(ContractPDI.Columnas.SUBCATEGORIA, match.subcategoria)
                            .withValue(ContractPDI.Columnas.MARCA, match.marca)
                            .withValue(ContractPDI.Columnas.OBJETIVO, match.objetivo)
                            .withValue(ContractPDI.Columnas.PLATAFORMA, match.plataforma)
                            .build());
                    syncResult.stats.numUpdates++;
                } else {
                    Log.i(TAG, "No hay acciones para este registro: " + existingUri);
                }
            } else {
                // Debido a que la entrada no existe, es removida de la base de datos
                Uri deleteUri = ContractPDI.CONTENT_URI.buildUpon()
                        .appendPath(id).build();
                Log.i(TAG, "Programando eliminación de: " + deleteUri);
                ops.add(ContentProviderOperation.newDelete(deleteUri).build());
                syncResult.stats.numDeletes++;
            }
        }
        c.close();

        // Insertar items resultantes
        for (BasePDI e : expenseMap.values()) {
            Log.i(TAG, "Programando inserción de Tabla PDI en Base Local: " + e.id);
            ops.add(ContentProviderOperation.newInsert(ContractPDI.CONTENT_URI)
                    .withValue(Constantes.ID_REMOTA, e.id) //Esta clave importante!!! ForeignKey del servidor
                    //.withValue(ContractPortafolioProductos.Columnas.CODIGO_PRODUCTO,e.codigo_producto)
                    .withValue(ContractPDI.Columnas.CANAL, e.canal)
                    .withValue(ContractPDI.Columnas.CATEGORIA, e.categoria)
                    .withValue(ContractPDI.Columnas.SUBCATEGORIA, e.subcategoria)
                    .withValue(ContractPDI.Columnas.MARCA, e.marca)
                    .withValue(ContractPDI.Columnas.OBJETIVO, e.objetivo)
                    .withValue(ContractPDI.Columnas.PLATAFORMA, e.plataforma)
                    .build());
            syncResult.stats.numInserts++;
        }

        if (syncResult.stats.numInserts > 0 ||
                syncResult.stats.numUpdates > 0 ||
                syncResult.stats.numDeletes > 0) {
            Log.i(TAG, "Aplicando operaciones...");
            try {
                resolver.applyBatch(Constantes.AUTHORITY, ops);
            } catch (RemoteException | OperationApplicationException e) {
                e.printStackTrace();
            }
            resolver.notifyChange(
                    ContractPDI.CONTENT_URI,
                    null,
                    false);
//            Toast.makeText(getContext(),"Sincronización de BasePharmaValue finalizada.",Toast.LENGTH_SHORT).show();
            Log.i(TAG, "Sincronización de tabla PDI finalizada");
            enviarBroadcast(true, Mensajes.SYNC_FINALIZADA_PDI);

        } else {
            Log.i(TAG, "No se requiere sincronización de tabla PDI");
            enviarBroadcast(true, Mensajes.SYNC_NOREQUERIDA + "PDI");
        }

    }

    /**
     * PORTAFOLIO PRIORITARIO
     *
     * @param syncResult
     * @param operatorName
     */
    public void realizarSincronizacionLocalPrioritario(final SyncResult syncResult, String operatorName) {
        Log.i(TAG, "Realizando Sincronizacion Local de Prioritario." + operatorName);
        String operators = operatorName;
        HashMap<String, String> map = new HashMap<>();// Mapeo previo

        map.put("operators", operators);

        // Crear nuevo objeto Json basado en el mapa
        JSONObject jobject = new JSONObject(map);
        //GET METHOD
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, Constantes.GET_PRIORITARIO, jobject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        procesarRespuestaGetPrioritario(response, syncResult);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof TimeoutError) {
                            enviarBroadcast(true, Mensajes.TIME_OUT);
                        } else if (error instanceof NoConnectionError) {
                            //TODO
                            enviarBroadcast(true, Mensajes.NO_RED);
                        } else if (error instanceof AuthFailureError) {
                            //TODO
                        } else if (error instanceof ServerError) {
                            //TODO
                            enviarBroadcast(true, Mensajes.SEVER_ERROR);
                        } else if (error instanceof NetworkError) {
                            //TODO
                            enviarBroadcast(true, Mensajes.RED_ERROR);
                        } else if (error instanceof ParseError) {
                            //TODO
                        }
                    }
                });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(MY_DEFAULT_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);
    }

    private void procesarRespuestaGetPrioritario(JSONObject response, SyncResult syncResult) {
        try {
            // Obtener atributo "estado"
            String estado = response.getString(Constantes.ESTADO);

            switch (estado) {
                case Constantes.SUCCESS: // EXITO
                    actualizarDatosLocalesPrioritario(response, syncResult);
                    break;
                case Constantes.FAILED: // FALLIDO
                    String mensaje = response.getString(Constantes.MENSAJE);
                    enviarBroadcast(true, mensaje);
                    Log.i(TAG, mensaje);
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void actualizarDatosLocalesPrioritario(JSONObject response, SyncResult syncResult) {
        JSONArray loginResult = null;

        try {
            // Obtener array "gastos"
            loginResult = response.getJSONArray(Constantes.PRIORITARIO_RESULT);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Parsear con Gson
        BasePortafolioPrioritario[] res = gson.fromJson(loginResult != null ? loginResult.toString() : null, BasePortafolioPrioritario[].class);
        List<BasePortafolioPrioritario> data = Arrays.asList(res);

        // Lista para recolección de operaciones pendientes
        ArrayList<ContentProviderOperation> ops = new ArrayList<>();

        // Tabla hash para recibir los datos entrantes
        HashMap<String, BasePortafolioPrioritario> expenseMap = new HashMap<>();
        for (BasePortafolioPrioritario e : data) {
            expenseMap.put(e.id, e);
        }

        // Consultar registros remotos actuales
        Uri uri = ContractPrioritario.CONTENT_URI;
        String select = Constantes.ID_REMOTA + " IS NOT NULL";
        Cursor c = resolver.query(uri, Projection.PROJECTION_PRIORITARIO, select, null, null);
        assert c != null;

        Log.i(TAG, "Se encontraron " + c.getCount() + " registros locales.");

        // Encontrar datos obsoletos
        String id;
        //String codigo_producto;
        String canal;
        String codigo_pdv;
        String categoria;
        String subcategoria;
        String marca;
        String contenido;
        String sku;

        while (c.moveToNext()) {
            syncResult.stats.numEntries++;
            id = c.getString(Projection.COLUMNA_ID_REMOTA);
            //codigo_producto = c.getString(Projection.CODIGO_PRODUCTO);
            canal = c.getString(Projection.COLUMNA_CANAL_PRIORITARIOS);
            codigo_pdv = c.getString(Projection.COLUMNA_CODIGO_PDV_PRIORITARIOS);
            categoria = c.getString(Projection.COLUMNA_CATEGORIA_PRIORITARIOS);
            subcategoria = c.getString(Projection.COLUMNA_SUBCATEGORIA_PRIORITARIOS);
            marca = c.getString(Projection.COLUMNA_MARCA_PRIORITARIOS);
            contenido = c.getString(Projection.COLUMNA_CONTENIDO_PRIORITARIOS);
            sku = c.getString(Projection.COLUMNA_SKU_PRIORITARIOS);

            BasePortafolioPrioritario match = expenseMap.get(id);

            if (match != null) {
                // Esta entrada existe, por lo que se remueve del mapeado
                expenseMap.remove(id);

                Uri existingUri = ContractPrioritario.CONTENT_URI.buildUpon()
                        .appendPath(id).build();

                // Comprobar si el gasto necesita ser actualizado
                //boolean b=match.codigo_producto!=null && !match.codigo_producto.equals(codigo_producto);
                boolean b1 = match.canal != null && !match.canal.equals(canal);
                boolean b2 = match.codigo_pdv != null && !match.codigo_pdv.equals(codigo_pdv);
                boolean b3 = match.categoria != null && !match.categoria.equals(categoria);
                boolean b4 = match.subcategoria != null && !match.subcategoria.equals(subcategoria);
                boolean b5 = match.marca != null && !match.marca.equals(marca);
                boolean b6 = match.contenido != null && !match.contenido.equals(contenido);
                boolean b7 = match.sku != null && !match.sku.equals(sku);

                if (b1 || b2 || b3 || b4 || b5 || b6 || b7) {

                    Log.i(TAG, "Programando actualización de: " + existingUri);

                    ops.add(ContentProviderOperation.newUpdate(existingUri)
                            //.withValue(ContractPortafolioProductos.Columnas.CODIGO_PRODUCTO,match.codigo_producto)
                            .withValue(ContractPrioritario.Columnas.CANAL, match.canal)
                            .withValue(ContractPrioritario.Columnas.CODIGO_PDV, match.codigo_pdv)
                            .withValue(ContractPrioritario.Columnas.CATEGORIA, match.categoria)
                            .withValue(ContractPrioritario.Columnas.SUBCATEGORIA, match.subcategoria)
                            .withValue(ContractPrioritario.Columnas.MARCA, match.marca)
                            .withValue(ContractPrioritario.Columnas.CONTENIDO, match.contenido)
                            .withValue(ContractPrioritario.Columnas.SKU, match.sku)
                            .build());
                    syncResult.stats.numUpdates++;
                } else {
                    Log.i(TAG, "No hay acciones para este registro: " + existingUri);
                }
            } else {
                // Debido a que la entrada no existe, es removida de la base de datos
                Uri deleteUri = ContractPrioritario.CONTENT_URI.buildUpon()
                        .appendPath(id).build();
                Log.i(TAG, "Programando eliminación de: " + deleteUri);
                ops.add(ContentProviderOperation.newDelete(deleteUri).build());
                syncResult.stats.numDeletes++;
            }
        }
        c.close();

        // Insertar items resultantes
        for (BasePortafolioPrioritario e : expenseMap.values()) {
            Log.i(TAG, "Programando inserción de Tabla Portafolio Prioritario en Base Local: " + e.id);
            ops.add(ContentProviderOperation.newInsert(ContractPrioritario.CONTENT_URI)
                    .withValue(Constantes.ID_REMOTA, e.id) //Esta clave importante!!! ForeignKey del servidor
                    //.withValue(ContractPortafolioProductos.Columnas.CODIGO_PRODUCTO,e.codigo_producto)
                    .withValue(ContractPrioritario.Columnas.CANAL, e.canal)
                    .withValue(ContractPrioritario.Columnas.CODIGO_PDV, e.codigo_pdv)
                    .withValue(ContractPrioritario.Columnas.CATEGORIA, e.categoria)
                    .withValue(ContractPrioritario.Columnas.SUBCATEGORIA, e.subcategoria)
                    .withValue(ContractPrioritario.Columnas.MARCA, e.marca)
                    .withValue(ContractPrioritario.Columnas.CONTENIDO, e.contenido)
                    .withValue(ContractPrioritario.Columnas.SKU, e.sku)
                    .build());
            syncResult.stats.numInserts++;
        }

        if (syncResult.stats.numInserts > 0 ||
                syncResult.stats.numUpdates > 0 ||
                syncResult.stats.numDeletes > 0) {
            Log.i(TAG, "Aplicando operaciones...");
            try {
                resolver.applyBatch(Constantes.AUTHORITY, ops);
            } catch (RemoteException | OperationApplicationException e) {
                e.printStackTrace();
            }
            resolver.notifyChange(
                    ContractPrioritario.CONTENT_URI,
                    null,
                    false);
//            Toast.makeText(getContext(),"Sincronización de BasePharmaValue finalizada.",Toast.LENGTH_SHORT).show();
            Log.i(TAG, "Sincronización de tabla Portafolio Prioritario finalizada");
            enviarBroadcast(true, Mensajes.SYNC_FINALIZADA_PRIORITARIO);

        } else {
            Log.i(TAG, "No se requiere sincronización de tabla Portafolio Prioritario");
            enviarBroadcast(true, Mensajes.SYNC_NOREQUERIDA + "Portafolio Prioritario");
        }

    }

    /**
     * TAREAS
     *
     * @param syncResult
     * @param operatorName
     */
    public void realizarSincronizacionLocalTareas(final SyncResult syncResult, String operatorName) {
        Log.i(TAG, "Realizando Sincronizacion Local de Tareas." + operatorName);
        String operators = operatorName;
        HashMap<String, String> map = new HashMap<>();// Mapeo previo

        map.put("operators", operators);

        // Crear nuevo objeto Json basado en el mapa
        JSONObject jobject = new JSONObject(map);
        //GET METHOD
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, Constantes.GET_TAREAS, jobject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        procesarRespuestaGetTareas(response, syncResult);
                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                if (error instanceof TimeoutError) {
                                    enviarBroadcast(true, Mensajes.TIME_OUT);
                                } else if (error instanceof NoConnectionError) {
                                    //TODO
                                    enviarBroadcast(true, Mensajes.NO_RED);
                                } else if (error instanceof AuthFailureError) {
                                    //TODO
                                } else if (error instanceof ServerError) {
                                    //TODO
                                    enviarBroadcast(true, Mensajes.SEVER_ERROR);
                                } else if (error instanceof NetworkError) {
                                    //TODO
                                    enviarBroadcast(true, Mensajes.RED_ERROR);
                                } else if (error instanceof ParseError) {
                                    //TODO
                                }
                            }
                        });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(MY_DEFAULT_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);
    }

    private void procesarRespuestaGetTareas(JSONObject response, SyncResult syncResult) {
        try {
            // Obtener atributo "estado"
            String estado = response.getString(Constantes.ESTADO);

            switch (estado) {
                case Constantes.SUCCESS: // EXITO
                    actualizarDatosLocalesTareas(response, syncResult);
                    break;
                case Constantes.FAILED: // FALLIDO
                    String mensaje = response.getString(Constantes.MENSAJE);
                    enviarBroadcast(true, mensaje);
                    Log.i(TAG, mensaje);
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void actualizarDatosLocalesTareas(JSONObject response, SyncResult syncResult) {
        JSONArray loginResult = null;

        try {
            // Obtener array "gastos"
            loginResult = response.getJSONArray(Constantes.TAREAS_RESULT);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Parsear con Gson
        BaseTareas[] res = gson.fromJson(loginResult != null ? loginResult.toString() : null, BaseTareas[].class);
        List<BaseTareas> data = Arrays.asList(res);

        // Lista para recolección de operaciones pendientes
        ArrayList<ContentProviderOperation> ops = new ArrayList<>();

        // Tabla hash para recibir los datos entrantes
        HashMap<String, BaseTareas> expenseMap = new HashMap<>();
        for (BaseTareas e : data) {
            expenseMap.put(e.id, e);
        }

        // Consultar registros remotos actuales
        Uri uri = ContractTareas.CONTENT_URI;
        String select = Constantes.ID_REMOTA + " IS NOT NULL";
        Cursor c = resolver.query(uri, Projection.PROJECTION_TAREAS, select, null, null);
        assert c != null;

        Log.i(TAG, "Se encontraron " + c.getCount() + " registros locales.");

        // Encontrar datos obsoletos
        String id;
        //String codigo_producto;
        String canal;
        String codigopdv;
        String mercaderista;
        String tareas;
        String periodo;
        String fecha_ingreso;

        while (c.moveToNext()) {
            syncResult.stats.numEntries++;
            id = c.getString(Projection.COLUMNA_ID_REMOTA);
            //codigo_producto = c.getString(Projection.CODIGO_PRODUCTO);
            canal = c.getString(Projection.CANALTAR);
            codigopdv = c.getString(Projection.CODIGOPDVTAR);
            mercaderista = c.getString(Projection.MERCADERISTATAR);
            tareas = c.getString(Projection.TAREAS);
            periodo = c.getString(Projection.PERIODO);
            fecha_ingreso = c.getString(Projection.FECHA_INGRESOTAR);

            BaseTareas match = expenseMap.get(id);

            if (match != null) {
                // Esta entrada existe, por lo que se remueve del mapeado
                expenseMap.remove(id);

                Uri existingUri = ContractTareas.CONTENT_URI.buildUpon()
                        .appendPath(id).build();

                // Comprobar si el gasto necesita ser actualizado
                //boolean b=match.codigo_producto!=null && !match.codigo_producto.equals(codigo_producto);
                boolean b1 = match.channel != null && !match.channel.equals(canal);
                boolean b2 = match.codigo_pdv != null && !match.codigo_pdv.equals(codigopdv);
                boolean b3 = match.mercaderista != null && !match.mercaderista.equals(mercaderista);
                boolean b4 = match.tareas != null && !match.tareas.equals(tareas);
                boolean b5 = match.periodo != null && !match.periodo.equals(periodo);
                boolean b6 = match.fecha_ingreso != null && !match.fecha_ingreso.equals(fecha_ingreso);

                if (b1 || b2 || b3 || b4 || b5 || b6) {

                    Log.i(TAG, "Programando actualización de: " + existingUri);

                    ops.add(ContentProviderOperation.newUpdate(existingUri)
                            //.withValue(ContractPortafolioProductos.Columnas.CODIGO_PRODUCTO,match.codigo_producto)
                            .withValue(ContractTareas.Columnas.CANAL, match.channel)
                            .withValue(ContractTareas.Columnas.CODIGOPDV, match.codigo_pdv)
                            .withValue(ContractTareas.Columnas.MERCADERISTA, match.mercaderista)
                            .withValue(ContractTareas.Columnas.TAREAS, match.tareas)
                            .withValue(ContractTareas.Columnas.PERIODO, match.periodo)
                            .withValue(ContractTareas.Columnas.FECHA_INGRESO, match.fecha_ingreso)
                            .build());
                    syncResult.stats.numUpdates++;
                } else {
                    Log.i(TAG, "No hay acciones para este registro: " + existingUri);
                }
            } else {
                // Debido a que la entrada no existe, es removida de la base de datos
                Uri deleteUri = ContractTareas.CONTENT_URI.buildUpon()
                        .appendPath(id).build();
                Log.i(TAG, "Programando eliminación de: " + deleteUri);
                ops.add(ContentProviderOperation.newDelete(deleteUri).build());
                syncResult.stats.numDeletes++;
            }
        }
        c.close();

        // Insertar items resultantes
        for (BaseTareas e : expenseMap.values()) {
            Log.i(TAG, "Programando inserción de Tabla Tareas en Base Local: " + e.id);
            ops.add(ContentProviderOperation.newInsert(ContractTareas.CONTENT_URI)
                    .withValue(Constantes.ID_REMOTA, e.id) //Esta clave importante!!! ForeignKey del servidor
                    //.withValue(ContractPortafolioProductos.Columnas.CODIGO_PRODUCTO,e.codigo_producto)
                    .withValue(ContractTareas.Columnas.CANAL, e.channel)
                    .withValue(ContractTareas.Columnas.CODIGOPDV, e.codigo_pdv)
                    .withValue(ContractTareas.Columnas.MERCADERISTA, e.mercaderista)
                    .withValue(ContractTareas.Columnas.TAREAS, e.tareas)
                    .withValue(ContractTareas.Columnas.PERIODO, e.periodo)
                    .withValue(ContractTareas.Columnas.FECHA_INGRESO, e.fecha_ingreso)
                    .build());
            syncResult.stats.numInserts++;
        }

        if (syncResult.stats.numInserts > 0 ||
                syncResult.stats.numUpdates > 0 ||
                syncResult.stats.numDeletes > 0) {
            Log.i(TAG, "Aplicando operaciones...");
            try {
                resolver.applyBatch(Constantes.AUTHORITY, ops);
            } catch (RemoteException | OperationApplicationException e) {
                e.printStackTrace();
            }
            resolver.notifyChange(
                    ContractTareas.CONTENT_URI,
                    null,
                    false);
//            Toast.makeText(getContext(),"Sincronización de BasePharmaValue finalizada.",Toast.LENGTH_SHORT).show();
            Log.i(TAG, "Sincronización de tabla Tareas finalizada");
            enviarBroadcast(true, Mensajes.SYNC_FINALIZADA_TAREAS);

        } else {
            Log.i(TAG, "No se requiere sincronización de tabla Tareas");
            enviarBroadcast(true, Mensajes.SYNC_NOREQUERIDA + "Tarea");
        }

    }

    /*
     *  SINCRONIZAR TABLA LOCAL
     */
    public void realizarSincronizacionLocalPrecios(final SyncResult syncResult) {
        Log.i(TAG, "Realizando Sincronizacion Local de Precios.");

        //GET METHOD
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, Constantes.GET_PRECIOS, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        procesarRespuestaGetPrecios(response, syncResult);
                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                if (error instanceof TimeoutError) {
                                    enviarBroadcast(true, Mensajes.TIME_OUT);
                                } else if (error instanceof NoConnectionError) {
                                    //TODO
                                    enviarBroadcast(true, Mensajes.NO_RED);
                                } else if (error instanceof AuthFailureError) {
                                    //TODO
                                } else if (error instanceof ServerError) {
                                    //TODO
                                    enviarBroadcast(true, Mensajes.SEVER_ERROR);
                                } else if (error instanceof NetworkError) {
                                    //TODO
                                    enviarBroadcast(true, Mensajes.RED_ERROR);
                                } else if (error instanceof ParseError) {
                                    //TODO
                                }
                            }
                        });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(MY_DEFAULT_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);
    }

    /**
     * Procesa la respuesta del servidor al pedir que se retornen todos los productos.
     *
     * @param response   Respuesta en subchannel Json
     * @param syncResult Registro de resultados de sincronización
     */
    private void procesarRespuestaGetPrecios(JSONObject response, SyncResult syncResult) {
        try {
            // Obtener atributo "estado"
            String estado = response.getString(Constantes.ESTADO);

            switch (estado) {
                case Constantes.SUCCESS: // EXITO
                    actualizarDatosLocalesPrecios(response, syncResult);
                    break;
                case Constantes.FAILED: // FALLIDO
                    String mensaje = response.getString(Constantes.MENSAJE);
                    enviarBroadcast(true, mensaje);
                    Log.i(TAG, mensaje);
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * Actualiza los registros locales a través de una comparación con los datos
     * del servidor
     *
     * @param response   Respuesta en subchannel Json obtenida del servidor
     * @param syncResult Registros de la sincronización
     */
    private void actualizarDatosLocalesPrecios(JSONObject response, SyncResult syncResult) {

        JSONArray loginResult = null;

        try {
            // Obtener array "gastos"
            loginResult = response.getJSONArray(Constantes.PRECIOS_RESULT);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Parsear con Gson
        Precios[] res = gson.fromJson(loginResult != null ? loginResult.toString() : null, Precios[].class);
        List<Precios> data = Arrays.asList(res);

        // Lista para recolección de operaciones pendientes
        ArrayList<ContentProviderOperation> ops = new ArrayList<>();

        // Tabla hash para recibir los datos entrantes
        HashMap<String, Precios> expenseMap = new HashMap<>();
        for (Precios e : data) {
            expenseMap.put(e.id, e);
        }

        // Consultar registros remotos actuales
        Uri uri = ContractPrecios.CONTENT_URI;
        String select = Constantes.ID_REMOTA + " IS NOT NULL";
        Cursor c = resolver.query(uri, Projection.PROJECTION_PRECIOS, select, null, null);
        assert c != null;

        Log.i(TAG, "Se encontraron " + c.getCount() + " registros locales.");

        // Encontrar datos obsoletos
        String id;
        String producto;
        String segmento;
        String marca;
        String categoria;
        String subcategoria;

        while (c.moveToNext()) {
            syncResult.stats.numEntries++;

            id = c.getString(Projection.COLUMNA_ID_REMOTA);
            producto = c.getString(Projection.COLUMNA_PRODUCTO);
            segmento = c.getString(Projection.COLUMNA_SEGMENTO);
            marca = c.getString(Projection.COLUMNA_MARCA);
            categoria = c.getString(Projection.COLUMNA_CATEGORIA);
            subcategoria = c.getString(Projection.COLUMNA_SUBCATEGORIA);

            Precios match = expenseMap.get(id);

            if (match != null) {
                // Esta entrada existe, por lo que se remueve del mapeado
                expenseMap.remove(id);

                Uri existingUri = ContractPrecios.CONTENT_URI.buildUpon()
                        .appendPath(id).build();

                // Comprobar si el gasto necesita ser actualizado
                boolean b = match.producto != null && !match.producto.equals(producto);
                boolean b1 = match.segmento != null && !match.segmento.equals(segmento);
                boolean b2 = match.marca != null && !match.marca.equals(marca);
                boolean b3 = match.categoria != null && !match.categoria.equals(categoria);
                boolean b4 = match.subcategoria != null && !match.subcategoria.equals(subcategoria);

                if (b || b1 || b2 || b3 || b4) {

                    Log.i(TAG, "Programando actualización de: " + existingUri);

                    ops.add(ContentProviderOperation.newUpdate(existingUri)
                            .withValue(ContractPrecios.Columnas.PRODUCTO, match.producto)
                            .withValue(ContractPrecios.Columnas.SEGMENTO, match.segmento)
                            .withValue(ContractPrecios.Columnas.MARCA, match.marca)
                            .withValue(ContractPrecios.Columnas.CATEGORIA, match.categoria)
                            .withValue(ContractPrecios.Columnas.SUBCATEGORIA, match.subcategoria)
                            .build());
                    syncResult.stats.numUpdates++;
                } else {
                    Log.i(TAG, "No hay acciones para este registro: " + existingUri);
                }
            } else {
                // Debido a que la entrada no existe, es removida de la base de datos
                Uri deleteUri = ContractPrecios.CONTENT_URI.buildUpon()
                        .appendPath(id).build();
                Log.i(TAG, "Programando eliminación de: " + deleteUri);
                ops.add(ContentProviderOperation.newDelete(deleteUri).build());
                syncResult.stats.numDeletes++;
            }
        }
        c.close();

        // Insertar items resultantes
        for (Precios e : expenseMap.values()) {
            Log.i(TAG, "Programando inserción de Tabla Precios en Base Local: " + e.id);
            ops.add(ContentProviderOperation.newInsert(ContractPrecios.CONTENT_URI)
                    .withValue(Constantes.ID_REMOTA, e.id) //Esta clave importante!!! ForeignKey del servidor
                    .withValue(ContractPrecios.Columnas.PRODUCTO, e.producto)
                    .withValue(ContractPrecios.Columnas.SEGMENTO, e.segmento)
                    .withValue(ContractPrecios.Columnas.MARCA, e.marca)
                    .withValue(ContractPrecios.Columnas.CATEGORIA, e.categoria)
                    .withValue(ContractPrecios.Columnas.SUBCATEGORIA, e.subcategoria)
                    .build());
            syncResult.stats.numInserts++;
        }

        if (syncResult.stats.numInserts > 0 ||
                syncResult.stats.numUpdates > 0 ||
                syncResult.stats.numDeletes > 0) {
            Log.i(TAG, "Aplicando operaciones...");
            try {
                resolver.applyBatch(Constantes.AUTHORITY, ops);
            } catch (RemoteException | OperationApplicationException e) {
                e.printStackTrace();
            }
            resolver.notifyChange(
                    ContractPrecios.CONTENT_URI,
                    null,
                    false);
            Log.i(TAG, "Sincronización de tabla Precios finalizada.");
            enviarBroadcast(true, Mensajes.SYNC_FINALIZADA);

        } else {
            Log.i(TAG, "No se requiere sincronización de tabla Precios");
            enviarBroadcast(true, Mensajes.SYNC_NOREQUERIDA + "Precios");
        }

    }



    /*
     *  INSERTS
     */

    private void realizarSincronizacionRemotaInsertFlooring() {
        Log.i(TAG, "SyncAdapterSubida: Preparando datos a ser insertados en el servidor...");

        iniciarActualizacion(InsertFlooring.CONTENT_URI);

        Cursor c = obtenerRegistrosSucios(InsertFlooring.CONTENT_URI, Projection.PROJECTION_INSERTFLOORING);

        Log.i(TAG, "SyncAdapterSubida: Se encontraron " + c.getCount() + " registros sucios.");


        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                final int idLocal = c.getInt(Projection.COLUMNA_ID);

                Log.i(TAG, "SyncAdapterSubida: Insertando datos de Precios en el servidor...");

                VolleySingleton.getInstance(getContext()).addToRequestQueue(
                        new JsonObjectRequest(
                                Request.Method.POST,
                                Constantes.INSERTAR_FLOORING,
                                UtilidadesFlooring.deCursorAJSONObject(c),
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        procesarRespuestaInsertFlooring(response, idLocal);
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
//                                            Toast.makeText(getContext(), "Datos no enviados, almacenados en el teléfono.", Toast.LENGTH_SHORT).show();
                                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Network Timeout");
                                        } else if (error instanceof AuthFailureError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error AuthFailure");
                                        } else if (error instanceof ServerError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Server");
                                        } else if (error instanceof NetworkError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Network");
                                        } else if (error instanceof ParseError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Parse");
                                        }
                                    }
                                }
                        ) {
                            @Override
                            public Map<String, String> getHeaders() {
                                Map<String, String> headers = new HashMap<String, String>();
                                headers.put("Content-Type", "application/json; charset=utf-8");
                                headers.put("Accept", "application/json");
                                return headers;
                            }

                            @Override
                            public String getBodyContentType() {
                                return "application/json; charset=utf-8";//+ getParamsEncoding();
                            }
                        }
                );
            }
        } else {
            //Toast.makeText(getContext(),"No hay datos en cola de sincronizacion",Toast.LENGTH_SHORT).show();
//            StockActivity stockActivity = new StockActivity();
//            stockActivity.showToastFromBackground("No se requiere sincronización");
            Log.i(TAG, "SyncAdapterSubida: No se requiere sincronización: No hay datos en cola de sincronizacion");
            enviarBroadcast(true, Mensajes.SYNC_NODATOSCOLA);
        }
        c.close();
    }

    public void procesarRespuestaInsertFlooring(JSONObject response, int idLocal) {

        try {
            // Obtener estado
            String estado = response.getString(Constantes.ESTADO);
            // Obtener mensaje
            String mensaje = response.getString(Constantes.MENSAJE);

            switch (estado) {
                case Constantes.SUCCESS:
                    // Obtener identificador del nuevo registro creado en el servidor
                    String idRemota = response.getString(Constantes.LAST_ID);
                    Log.i(TAG, mensaje);
                    //Borrar datos sucios:
                    finalizarActualizacion(InsertFlooring.CONTENT_URI, InsertFlooring.Columnas._ID, idRemota, idLocal);
                    enviarBroadcast(true, Mensajes.ON_SYNC_TRUE);
                    Toast.makeText(getContext(), "Datos almacenados en el servidor", Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "SyncAdapterSubida: Registros Insertados en el Servidor y en la Base Local " + idRemota);
                    break;

                case Constantes.FAILED:
                    Log.i(TAG, mensaje);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private void realizarSincronizacionRemotaInsertInicial() {
        Log.i(TAG, "SyncAdapterSubida: Preparando datos a ser insertados en el servidor...");

        iniciarActualizacion(ContractInsertInicial.CONTENT_URI);

        Cursor c = obtenerRegistrosSucios(ContractInsertInicial.CONTENT_URI, Projection.PROJECTION_INSERTINICIAL);

        Log.i(TAG, "SyncAdapterSubida: Se encontraron " + c.getCount() + " registros sucios.");


        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                final int idLocal = c.getInt(Projection.COLUMNA_ID);

                Log.i(TAG, "SyncAdapterSubida: Insertando datos de Precios en el servidor...");

                VolleySingleton.getInstance(getContext()).addToRequestQueue(
                        new JsonObjectRequest(
                                Request.Method.POST,
                                Constantes.INSERTAR_INICIAL,
                                UtilidadesInicial.deCursorAJSONObject(c),
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        procesarRespuestaInsertInicial(response, idLocal);
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
//                                            Toast.makeText(getContext(), "Datos no enviados, almacenados en el teléfono.", Toast.LENGTH_SHORT).show();
                                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Network Timeout");
                                        } else if (error instanceof AuthFailureError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error AuthFailure");
                                        } else if (error instanceof ServerError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Server");
                                        } else if (error instanceof NetworkError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Network");
                                        } else if (error instanceof ParseError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Parse");
                                        }
                                    }
                                }
                        ) {
                            @Override
                            public Map<String, String> getHeaders() {
                                Map<String, String> headers = new HashMap<String, String>();
                                headers.put("Content-Type", "application/json; charset=utf-8");
                                headers.put("Accept", "application/json");
                                return headers;
                            }

                            @Override
                            public String getBodyContentType() {
                                return "application/json; charset=utf-8";//+ getParamsEncoding();
                            }
                        }
                );
            }
        } else {
            //Toast.makeText(getContext(),"No hay datos en cola de sincronizacion",Toast.LENGTH_SHORT).show();
//            StockActivity stockActivity = new StockActivity();
//            stockActivity.showToastFromBackground("No se requiere sincronización");
            Log.i(TAG, "SyncAdapterSubida: No se requiere sincronización: No hay datos en cola de sincronizacion");
            enviarBroadcast(true, Mensajes.SYNC_NODATOSCOLA);
        }
        c.close();
    }

    public void procesarRespuestaInsertInicial(JSONObject response, int idLocal) {

        try {
            // Obtener estado
            String estado = response.getString(Constantes.ESTADO);
            // Obtener mensaje
            String mensaje = response.getString(Constantes.MENSAJE);

            switch (estado) {
                case Constantes.SUCCESS:
                    // Obtener identificador del nuevo registro creado en el servidor
                    String idRemota = response.getString(Constantes.LAST_ID);
                    Log.i(TAG, mensaje);
                    //Borrar datos sucios:
                    finalizarActualizacion(ContractInsertInicial.CONTENT_URI, ContractInsertInicial.Columnas._ID, idRemota, idLocal);
                    enviarBroadcast(true, Mensajes.ON_SYNC_TRUE);
                    Toast.makeText(getContext(), "Datos almacenados en el servidor", Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "SyncAdapterSubida: Registros Insertados en el Servidor y en la Base Local " + idRemota);
                    break;

                case Constantes.FAILED:
                    Log.i(TAG, mensaje);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void realizarSincronizacionRemotaInsertPrecio() {
        Log.i(TAG, "SyncAdapterSubida: Preparando datos a ser insertados en el servidor...");

        iniciarActualizacion(ContractInsertPrecios.CONTENT_URI);

        Cursor c = obtenerRegistrosSucios(ContractInsertPrecios.CONTENT_URI, Projection.PROJECTION_INSERTPRECIO);

        Log.i(TAG, "SyncAdapterSubida: Se encontraron " + c.getCount() + " registros sucios.");


        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                final int idLocal = c.getInt(Projection.COLUMNA_ID);

                Log.i(TAG, "SyncAdapterSubida: Insertando datos de Precios en el servidor...");

                VolleySingleton.getInstance(getContext()).addToRequestQueue(
                        new JsonObjectRequest(
                                Request.Method.POST,
                                Constantes.INSERTAR_PRECIO,
                                UtilidadesPrecios.deCursorAJSONObject(c),
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        procesarRespuestaInsertPrecios(response, idLocal);
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
//                                            Toast.makeText(getContext(), "Datos no enviados, almacenados en el teléfono.", Toast.LENGTH_SHORT).show();
                                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Network Timeout");
                                        } else if (error instanceof AuthFailureError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error AuthFailure");
                                        } else if (error instanceof ServerError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Server");
                                        } else if (error instanceof NetworkError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Network");
                                        } else if (error instanceof ParseError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Parse");
                                        }
                                    }
                                }
                        ) {
                            @Override
                            public Map<String, String> getHeaders() {
                                Map<String, String> headers = new HashMap<String, String>();
                                headers.put("Content-Type", "application/json; charset=utf-8");
                                headers.put("Accept", "application/json");
                                return headers;
                            }

                            @Override
                            public String getBodyContentType() {
                                return "application/json; charset=utf-8";//+ getParamsEncoding();
                            }
                        }
                );
            }
        } else {
            //Toast.makeText(getContext(),"No hay datos en cola de sincronizacion",Toast.LENGTH_SHORT).show();
//            StockActivity stockActivity = new StockActivity();
//            stockActivity.showToastFromBackground("No se requiere sincronización");
            Log.i(TAG, "SyncAdapterSubida: No se requiere sincronización: No hay datos en cola de sincronizacion");
            enviarBroadcast(true, Mensajes.SYNC_NODATOSCOLA);
        }
        c.close();
    }

    public void procesarRespuestaInsertPrecios(JSONObject response, int idLocal) {
        try {
            // Obtener estado
            String estado = response.getString(Constantes.ESTADO);
            // Obtener mensaje
            String mensaje = response.getString(Constantes.MENSAJE);

            switch (estado) {
                case Constantes.SUCCESS:
                    // Obtener identificador del nuevo registro creado en el servidor
                    String idRemota = response.getString(Constantes.LAST_ID);
                    Log.i(TAG, mensaje);
                    //Borrar datos sucios:
                    finalizarActualizacion(ContractInsertPrecios.CONTENT_URI, ContractInsertPrecios.Columnas._ID, idRemota, idLocal);
                    enviarBroadcast(true, Mensajes.ON_SYNC_TRUE);
                    Toast.makeText(getContext(), "Datos almacenados en el servidor", Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "SyncAdapterSubida: Registros Insertados en el Servidor y en la Base Local " + idRemota);
                    break;

                case Constantes.FAILED:
                    Log.i(TAG, mensaje);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void realizarSincronizacionRemotaInsertCanjes() {
        Log.i(TAG, "SyncAdapterSubida: Preparando datos a ser insertados en el servidor...");

        iniciarActualizacion(ContractInsertCanjes.CONTENT_URI);

        Cursor c = obtenerRegistrosSucios(ContractInsertCanjes.CONTENT_URI, Projection.PROJECTION_INSERT_CANJES);

        Log.i(TAG, "SyncAdapterSubida: Se encontraron " + c.getCount() + " registros sucios.");


        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                final int idLocal = c.getInt(Projection.COLUMNA_ID);

                Log.i(TAG, "SyncAdapterSubida: ando datos de Canjes en el servidor...");

                VolleySingleton.getInstance(getContext()).addToRequestQueue(
                        new JsonObjectRequest(
                                Request.Method.POST,
                                Constantes.INSERTAR_CANJES,
                                UtilidadesCanjes.deCursorAJSONObject(c),
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        procesarRespuestaInsertCanjes(response, idLocal);
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
//                                            Toast.makeText(getContext(), "Datos no enviados, almacenados en el teléfono.", Toast.LENGTH_SHORT).show();
                                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Network Timeout");
                                        } else if (error instanceof AuthFailureError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error AuthFailure");
                                        } else if (error instanceof ServerError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Server");
                                        } else if (error instanceof NetworkError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Network");
                                        } else if (error instanceof ParseError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Parse");
                                        }
                                    }
                                }
                        ) {
                            @Override
                            public Map<String, String> getHeaders() {
                                Map<String, String> headers = new HashMap<String, String>();
                                headers.put("Content-Type", "application/json; charset=utf-8");
                                headers.put("Accept", "application/json");
                                return headers;
                            }

                            @Override
                            public String getBodyContentType() {
                                return "application/json; charset=utf-8" ;//+ getParamsEncoding();
                            }
                        }
                );
            }
        } else {
            //Toast.makeText(getContext(),"No hay datos en cola de sincronizacion",Toast.LENGTH_SHORT).show();
//            StockActivity stockActivity = new StockActivity();
//            stockActivity.showToastFromBackground("No se requiere sincronización");
            Log.i(TAG, "SyncAdapterSubida: No se requiere sincronización: No hay datos en cola de sincronizacion");
            enviarBroadcast(true, Mensajes.SYNC_NODATOSCOLA);
        }
        c.close();
    }

    public void procesarRespuestaInsertCanjes(JSONObject response, int idLocal) {
        try {
            // Obtener estado
            String estado = response.getString(Constantes.ESTADO);
            // Obtener mensaje
            String mensaje = response.getString(Constantes.MENSAJE);

            switch (estado) {
                case Constantes.SUCCESS:
                    // Obtener identificador del nuevo registro creado en el servidor
                    String idRemota = response.getString(Constantes.LAST_ID);
                    Log.i(TAG, mensaje);
                    //Borrar datos sucios:
                    finalizarActualizacion(ContractInsertCanjes.CONTENT_URI, ContractInsertCanjes.Columnas._ID, idRemota, idLocal);
                    enviarBroadcast(true, Mensajes.ON_SYNC_TRUE);
                    Toast.makeText(getContext(),"Datos almacenados en el servidor",Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "SyncAdapterSubida: Registros Insertados en el Servidor y en la Base Local "+idRemota);
                    break;

                case Constantes.FAILED:
                    Log.i(TAG, mensaje);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * INSERT MATERIALES RECIBIDOS
     */
    private void realizarSincronizacionRemotaInsertMaterialesRecibidos() {
        Log.i(TAG, "SyncAdapterSubida: Preparando datos a ser insertados en el servidor...");

        iniciarActualizacion(ContractInsertMaterialesRecibidos.CONTENT_URI);

        Cursor c = obtenerRegistrosSucios(ContractInsertMaterialesRecibidos.CONTENT_URI, Projection.PROJECTION_INSERT_MATERIALES_RECIBIDOS);

        Log.i(TAG, "SyncAdapterSubida: Se encontraron " + c.getCount() + " registros sucios.");


        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                final int idLocal = c.getInt(Projection.COLUMNA_ID);

                Log.i(TAG, "SyncAdapterSubida: Insertando datos de Materiales Recibidos en el servidor...");

                VolleySingleton.getInstance(getContext()).addToRequestQueue(
                        new JsonObjectRequest(
                                Request.Method.POST,
                                Constantes.INSERTAR_MATERIALES_RECIBIDOS,
                                UtilidadesMaterialesRecibidos.deCursorAJSONObject(c),
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        procesarRespuestaInsertMaterialesRecibidos(response, idLocal);
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
//                                            Toast.makeText(getContext(), "Datos no enviados, almacenados en el teléfono.", Toast.LENGTH_SHORT).show();
                                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Network Timeout");
                                        } else if (error instanceof AuthFailureError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error AuthFailure");
                                        } else if (error instanceof ServerError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Server");
                                        } else if (error instanceof NetworkError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Network");
                                        } else if (error instanceof ParseError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Parse");
                                        }
                                    }
                                }
                        ) {
                            @Override
                            public Map<String, String> getHeaders() {
                                Map<String, String> headers = new HashMap<String, String>();
                                headers.put("Content-Type", "application/json; charset=utf-8");
                                headers.put("Accept", "application/json");
                                return headers;
                            }

                            @Override
                            public String getBodyContentType() {
                                return "application/json; charset=utf-8" ;//+ getParamsEncoding();
                            }
                        }
                );
            }
        } else {
            //Toast.makeText(getContext(),"No hay datos en cola de sincronizacion",Toast.LENGTH_SHORT).show();
//            StockActivity stockActivity = new StockActivity();
//            stockActivity.showToastFromBackground("No se requiere sincronización");
            Log.i(TAG, "SyncAdapterSubida: No se requiere sincronización: No hay datos en cola de sincronizacion");
            enviarBroadcast(true, Mensajes.SYNC_NODATOSCOLA);
        }
        c.close();
    }

    public void procesarRespuestaInsertMaterialesRecibidos(JSONObject response, int idLocal) {
        try {
            // Obtener estado
            String estado = response.getString(Constantes.ESTADO);
            // Obtener mensaje
            String mensaje = response.getString(Constantes.MENSAJE);

            switch (estado) {
                case Constantes.SUCCESS:
                    // Obtener identificador del nuevo registro creado en el servidor
                    String idRemota = response.getString(Constantes.LAST_ID);
                    Log.i(TAG, mensaje);
                    //Borrar datos sucios:
                    finalizarActualizacion(ContractInsertMaterialesRecibidos.CONTENT_URI, ContractInsertMaterialesRecibidos.Columnas._ID, idRemota, idLocal);
                    enviarBroadcast(true, Mensajes.ON_SYNC_TRUE);
                    Toast.makeText(getContext(),"Datos almacenados en el servidor",Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "SyncAdapterSubida: Registros Insertados en el Servidor y en la Base Local "+idRemota);
                    break;

                case Constantes.FAILED:
                    Log.i(TAG, mensaje);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * INSERT EJECUCION MATERIALES
     */
    private void realizarSincronizacionRemotaInsertEjecucionMateriales() {
        Log.i(TAG, "SyncAdapterSubida: Preparando datos a ser insertados en el servidor...");

        iniciarActualizacion(ContractInsertEjecucionMateriales.CONTENT_URI);

        Cursor c = obtenerRegistrosSucios(ContractInsertEjecucionMateriales.CONTENT_URI, Projection.PROJECTION_INSERT_EJECUCION_MATERIALES);

        Log.i(TAG, "SyncAdapterSubida: Se encontraron " + c.getCount() + " registros sucios.");


        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                final int idLocal = c.getInt(Projection.COLUMNA_ID);

                Log.i(TAG, "SyncAdapterSubida: Insertando datos de Ejecucion de Materiales en el servidor...");

                VolleySingleton.getInstance(getContext()).addToRequestQueue(
                        new JsonObjectRequest(
                                Request.Method.POST,
                                Constantes.INSERTAR_EJECUCION_MATERIALES,
                                UtilidadesEjecucionMateriales.deCursorAJSONObject(c),
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        procesarRespuestaInsertEjecucionMateriales(response, idLocal);
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
//                                            Toast.makeText(getContext(), "Datos no enviados, almacenados en el teléfono.", Toast.LENGTH_SHORT).show();
                                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Network Timeout");
                                        } else if (error instanceof AuthFailureError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error AuthFailure");
                                        } else if (error instanceof ServerError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Server");
                                        } else if (error instanceof NetworkError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Network");
                                        } else if (error instanceof ParseError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Parse");
                                        }
                                    }
                                }
                        ) {
                            @Override
                            public Map<String, String> getHeaders() {
                                Map<String, String> headers = new HashMap<String, String>();
                                headers.put("Content-Type", "application/json; charset=utf-8");
                                headers.put("Accept", "application/json");
                                return headers;
                            }

                            @Override
                            public String getBodyContentType() {
                                return "application/json; charset=utf-8" ;//+ getParamsEncoding();
                            }
                        }
                );
            }
        } else {
            //Toast.makeText(getContext(),"No hay datos en cola de sincronizacion",Toast.LENGTH_SHORT).show();
//            StockActivity stockActivity = new StockActivity();
//            stockActivity.showToastFromBackground("No se requiere sincronización");
            Log.i(TAG, "SyncAdapterSubida: No se requiere sincronización: No hay datos en cola de sincronizacion");
            enviarBroadcast(true, Mensajes.SYNC_NODATOSCOLA);
        }
        c.close();
    }

    public void procesarRespuestaInsertEjecucionMateriales(JSONObject response, int idLocal) {
        try {
            // Obtener estado
            String estado = response.getString(Constantes.ESTADO);
            // Obtener mensaje
            String mensaje = response.getString(Constantes.MENSAJE);

            switch (estado) {
                case Constantes.SUCCESS:
                    // Obtener identificador del nuevo registro creado en el servidor
                    String idRemota = response.getString(Constantes.LAST_ID);
                    Log.i(TAG, mensaje);
                    //Borrar datos sucios:
                    finalizarActualizacion(ContractInsertEjecucionMateriales.CONTENT_URI, ContractInsertEjecucionMateriales.Columnas._ID, idRemota, idLocal);
                    enviarBroadcast(true, Mensajes.ON_SYNC_TRUE);
                    Toast.makeText(getContext(),"Datos almacenados en el servidor",Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "SyncAdapterSubida: Registros Insertados en el Servidor y en la Base Local "+idRemota);
                    break;

                case Constantes.FAILED:
                    Log.i(TAG, mensaje);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * INSERT PROPENSOS Y PRODUCTOS MAL ESTADO
     */
    private void realizarSincronizacionRemotaInsertPropensosYProdMalEst() {
        Log.i(TAG, "SyncAdapterSubida: Preparando datos a ser insertados en el servidor...");

        iniciarActualizacion(ContractInsertPropensosYProdMalEst.CONTENT_URI);

        Cursor c = obtenerRegistrosSucios(ContractInsertPropensosYProdMalEst.CONTENT_URI, Projection.PROJECTION_INSERT_PROPENSOS_Y_PROD_MAL_EST);

        Log.i(TAG, "SyncAdapterSubida: Se encontraron " + c.getCount() + " registros sucios.");


        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                final int idLocal = c.getInt(Projection.COLUMNA_ID);

                Log.i(TAG, "SyncAdapterSubida: Insertando datos de MCI en el servidor...");

                VolleySingleton.getInstance(getContext()).addToRequestQueue(
                        new JsonObjectRequest(
                                Request.Method.POST,
                                Constantes.INSERTAR_PROPENSOS_Y_PROD_MAL_ESTADO,
                                UtilidadesPropensosYProdMalEst.deCursorAJSONObject(c),
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        procesarRespuestaInsertPropensosYProdMalEst(response, idLocal);
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
//                                            Toast.makeText(getContext(), "Datos no enviados, almacenados en el teléfono.", Toast.LENGTH_SHORT).show();
                                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Network Timeout");
                                        } else if (error instanceof AuthFailureError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error AuthFailure");
                                        } else if (error instanceof ServerError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Server");
                                        } else if (error instanceof NetworkError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Network");
                                        } else if (error instanceof ParseError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Parse");
                                        }
                                    }
                                }
                        ) {
                            @Override
                            public Map<String, String> getHeaders() {
                                Map<String, String> headers = new HashMap<String, String>();
                                headers.put("Content-Type", "application/json; charset=utf-8");
                                headers.put("Accept", "application/json");
                                return headers;
                            }

                            @Override
                            public String getBodyContentType() {
                                return "application/json; charset=utf-8" ;//+ getParamsEncoding();
                            }
                        }
                );
            }
        } else {
            //Toast.makeText(getContext(),"No hay datos en cola de sincronizacion",Toast.LENGTH_SHORT).show();
//            StockActivity stockActivity = new StockActivity();
//            stockActivity.showToastFromBackground("No se requiere sincronización");
            Log.i(TAG, "SyncAdapterSubida: No se requiere sincronización: No hay datos en cola de sincronizacion");
            enviarBroadcast(true, Mensajes.SYNC_NODATOSCOLA);
        }
        c.close();
    }

    public void procesarRespuestaInsertPropensosYProdMalEst(JSONObject response, int idLocal) {
        try {
            // Obtener estado
            String estado = response.getString(Constantes.ESTADO);
            // Obtener mensaje
            String mensaje = response.getString(Constantes.MENSAJE);

            switch (estado) {
                case Constantes.SUCCESS:
                    // Obtener identificador del nuevo registro creado en el servidor
                    String idRemota = response.getString(Constantes.LAST_ID);
                    Log.i(TAG, mensaje);
                    //Borrar datos sucios:
                    finalizarActualizacion(ContractInsertPropensosYProdMalEst.CONTENT_URI, ContractInsertPropensosYProdMalEst.Columnas._ID, idRemota, idLocal);
                    enviarBroadcast(true, Mensajes.ON_SYNC_TRUE);
                    Toast.makeText(getContext(),"Datos almacenados en el servidor",Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "SyncAdapterSubida: Registros Insertados en el Servidor y en la Base Local "+idRemota);
                    break;

                case Constantes.FAILED:
                    Log.i(TAG, mensaje);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * CAUSALES ASISTENCIA
     */
    public void realizarSincronizacionLocalCausalesAsistencia(final SyncResult syncResult, String operatorName) {
        Log.i(TAG, "Realizando Sincronizacion Local de Causales Asistencia." + operatorName);
        Log.i(TAG, ">>> CAUSALES_ASISTENCIA: URL destino: " + Constantes.GET_CAUSALES_ASISTENCIA);
        HashMap<String, String> map = new HashMap<>();
        map.put("operators", operatorName);
        JSONObject jobject = new JSONObject(map);
        Log.i(TAG, ">>> CAUSALES_ASISTENCIA: JSON enviado: " + jobject.toString());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, Constantes.GET_CAUSALES_ASISTENCIA, jobject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, ">>> CAUSALES_ASISTENCIA: Respuesta recibida: " + response.toString());
                        procesarRespuestaGetCausalesAsistencia(response, syncResult);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, ">>> CAUSALES_ASISTENCIA: ERROR Volley: " + error.toString());
                        if (error instanceof TimeoutError) {
                            enviarBroadcast(true, Mensajes.TIME_OUT);
                        } else if (error instanceof NoConnectionError) {
                            enviarBroadcast(true, Mensajes.NO_RED);
                        } else if (error instanceof AuthFailureError) {
                            //TODO
                        } else if (error instanceof ServerError) {
                            enviarBroadcast(true, Mensajes.SEVER_ERROR);
                        } else if (error instanceof NetworkError) {
                            enviarBroadcast(true, Mensajes.RED_ERROR);
                        } else if (error instanceof ParseError) {
                            //TODO
                        }
                    }
                });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(MY_DEFAULT_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);
        Log.i(TAG, ">>> CAUSALES_ASISTENCIA: Petición añadida a la cola de Volley");
    }
    private void procesarRespuestaGetCausalesAsistencia(JSONObject response, SyncResult syncResult) {
        try {
            String estado = response.getString(Constantes.ESTADO);
            switch (estado) {
                case Constantes.SUCCESS:
                    actualizarDatosLocalesCausalesAsistencia(response, syncResult);
                    break;
                case Constantes.FAILED:
                    String mensaje = response.getString(Constantes.MENSAJE);
                    enviarBroadcast(true, mensaje);
                    Log.i(TAG, mensaje);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void actualizarDatosLocalesCausalesAsistencia(JSONObject response, SyncResult syncResult) {
        JSONArray loginResult = null;
        try {
            loginResult = response.getJSONArray(Constantes.CAUSALES_ASISTENCIA_RESULT);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        BaseCausalesAsistencia[] res = gson.fromJson(
                loginResult != null ? loginResult.toString() : null,
                BaseCausalesAsistencia[].class);
        List<BaseCausalesAsistencia> data = Arrays.asList(res);

        ArrayList<ContentProviderOperation> ops = new ArrayList<>();
        HashMap<String, BaseCausalesAsistencia> expenseMap = new HashMap<>();
        for (BaseCausalesAsistencia e : data) {
            expenseMap.put(e.id, e);
        }

        Uri uri = ContractCausalesAsistencia.CONTENT_URI;
        String select = Constantes.ID_REMOTA + " IS NOT NULL";
        Cursor c = resolver.query(uri, Projection.PROJECTION_CAUSALES_ASISTENCIA, select, null, null);
        assert c != null;

        Log.i(TAG, "Se encontraron " + c.getCount() + " registros locales.");

        String id;
        String descripcion;

        while (c.moveToNext()) {
            syncResult.stats.numEntries++;
            id = c.getString(Projection.COLUMNA_ID_REMOTA);
            descripcion = c.getString(Projection.CAUSAL_ASISTENCIA_DESCRIPCION);

            BaseCausalesAsistencia match = expenseMap.get(id);

            if (match != null) {
                expenseMap.remove(id);
                Uri existingUri = ContractCausalesAsistencia.CONTENT_URI.buildUpon()
                        .appendPath(id).build();

                boolean b1 = match.descripcion != null && !match.descripcion.equals(descripcion);

                if (b1) {
                    Log.i(TAG, "Programando actualización de: " + existingUri);
                    ops.add(ContentProviderOperation.newUpdate(existingUri)
                            .withValue(ContractCausalesAsistencia.Columnas.DESCRIPCION, match.descripcion)
                            .build());
                    syncResult.stats.numUpdates++;
                } else {
                    Log.i(TAG, "No hay acciones para este registro: " + existingUri);
                }
            } else {
                Uri deleteUri = ContractCausalesAsistencia.CONTENT_URI.buildUpon()
                        .appendPath(id).build();
                Log.i(TAG, "Programando eliminación de: " + deleteUri);
                ops.add(ContentProviderOperation.newDelete(deleteUri).build());
                syncResult.stats.numDeletes++;
            }
        }
        c.close();

        for (BaseCausalesAsistencia e : expenseMap.values()) {
            Log.i(TAG, "Programando inserción de Tabla Causales Asistencia en Base Local: " + e.id);
            ops.add(ContentProviderOperation.newInsert(ContractCausalesAsistencia.CONTENT_URI)
                    .withValue(Constantes.ID_REMOTA, e.id)
                    .withValue(ContractCausalesAsistencia.Columnas.DESCRIPCION, e.descripcion)
                    .build());
            syncResult.stats.numInserts++;
        }

        if (syncResult.stats.numInserts > 0 ||
                syncResult.stats.numUpdates > 0 ||
                syncResult.stats.numDeletes > 0) {
            Log.i(TAG, "Aplicando operaciones...");
            try {
                resolver.applyBatch(Constantes.AUTHORITY, ops);
            } catch (RemoteException | OperationApplicationException e) {
                e.printStackTrace();
            }
            resolver.notifyChange(ContractCausalesAsistencia.CONTENT_URI, null, false);
            Log.i(TAG, "Sincronización de tabla Causales Asistencia finalizada");
            enviarBroadcast(true, Mensajes.SYNC_FINALIZADA_CAUSALES_ASISTENCIA);
        } else {
            Log.i(TAG, "No se requiere sincronización de tabla Causales Asistencia");
            enviarBroadcast(true, Mensajes.SYNC_NOREQUERIDA + "Causales Asistencia");
        }
    }
    /**
     * CAUSALES ASISTENCIA ATRASO
     */
    public void realizarSincronizacionLocalCausalesAsistenciaAtraso(final SyncResult syncResult, String operatorName) {
        Log.i(TAG, "Realizando Sincronizacion Local de Causales Asistencia Atraso." + operatorName);
        HashMap<String, String> map = new HashMap<>();
        map.put("operators", operatorName);
        JSONObject jobject = new JSONObject(map);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, Constantes.GET_CAUSALES_ASISTENCIA_ATRASO, jobject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        procesarRespuestaGetCausalesAsistenciaAtraso(response, syncResult);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof TimeoutError) {
                            enviarBroadcast(true, Mensajes.TIME_OUT);
                        } else if (error instanceof NoConnectionError) {
                            enviarBroadcast(true, Mensajes.NO_RED);
                        } else if (error instanceof AuthFailureError) {
                            //TODO
                        } else if (error instanceof ServerError) {
                            enviarBroadcast(true, Mensajes.SEVER_ERROR);
                        } else if (error instanceof NetworkError) {
                            enviarBroadcast(true, Mensajes.RED_ERROR);
                        } else if (error instanceof ParseError) {
                            //TODO
                        }
                    }
                });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(MY_DEFAULT_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);
    }

    private void procesarRespuestaGetCausalesAsistenciaAtraso(JSONObject response, SyncResult syncResult) {
        try {
            String estado = response.getString(Constantes.ESTADO);
            switch (estado) {
                case Constantes.SUCCESS:
                    actualizarDatosLocalesCausalesAsistenciaAtraso(response, syncResult);
                    break;
                case Constantes.FAILED:
                    String mensaje = response.getString(Constantes.MENSAJE);
                    enviarBroadcast(true, mensaje);
                    Log.i(TAG, mensaje);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void actualizarDatosLocalesCausalesAsistenciaAtraso(JSONObject response, SyncResult syncResult) {
        JSONArray loginResult = null;
        try {
            loginResult = response.getJSONArray(Constantes.CAUSALES_ASISTENCIA_ATRASO_RESULT);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        BaseCausalesAsistenciaAtraso[] res = gson.fromJson(
                loginResult != null ? loginResult.toString() : null,
                BaseCausalesAsistenciaAtraso[].class);
        List<BaseCausalesAsistenciaAtraso> data = Arrays.asList(res);

        ArrayList<ContentProviderOperation> ops = new ArrayList<>();
        HashMap<String, BaseCausalesAsistenciaAtraso> expenseMap = new HashMap<>();
        for (BaseCausalesAsistenciaAtraso e : data) {
            expenseMap.put(e.id, e);
        }

        Uri uri = ContractCausalesAsistenciaAtraso.CONTENT_URI;
        String select = Constantes.ID_REMOTA + " IS NOT NULL";
        Cursor c = resolver.query(uri, Projection.PROJECTION_CAUSALES_ASISTENCIA_ATRASO, select, null, null);
        assert c != null;

        Log.i(TAG, "Se encontraron " + c.getCount() + " registros locales.");

        String id;
        String descripcion;

        while (c.moveToNext()) {
            syncResult.stats.numEntries++;
            id = c.getString(Projection.COLUMNA_ID_REMOTA);
            descripcion = c.getString(Projection.CAUSAL_ASISTENCIA_ATRASO_DESCRIPCION);

            BaseCausalesAsistenciaAtraso match = expenseMap.get(id);

            if (match != null) {
                expenseMap.remove(id);
                Uri existingUri = ContractCausalesAsistenciaAtraso.CONTENT_URI.buildUpon()
                        .appendPath(id).build();

                boolean b1 = match.descripcion != null && !match.descripcion.equals(descripcion);

                if (b1) {
                    Log.i(TAG, "Programando actualización de: " + existingUri);
                    ops.add(ContentProviderOperation.newUpdate(existingUri)
                            .withValue(ContractCausalesAsistenciaAtraso.Columnas.DESCRIPCION, match.descripcion)
                            .build());
                    syncResult.stats.numUpdates++;
                } else {
                    Log.i(TAG, "No hay acciones para este registro: " + existingUri);
                }
            } else {
                Uri deleteUri = ContractCausalesAsistenciaAtraso.CONTENT_URI.buildUpon()
                        .appendPath(id).build();
                Log.i(TAG, "Programando eliminación de: " + deleteUri);
                ops.add(ContentProviderOperation.newDelete(deleteUri).build());
                syncResult.stats.numDeletes++;
            }
        }
        c.close();

        for (BaseCausalesAsistenciaAtraso e : expenseMap.values()) {
            Log.i(TAG, "Programando inserción de Tabla Causales Asistencia Atraso en Base Local: " + e.id);
            ops.add(ContentProviderOperation.newInsert(ContractCausalesAsistenciaAtraso.CONTENT_URI)
                    .withValue(Constantes.ID_REMOTA, e.id)
                    .withValue(ContractCausalesAsistenciaAtraso.Columnas.DESCRIPCION, e.descripcion)
                    .build());
            syncResult.stats.numInserts++;
        }

        if (syncResult.stats.numInserts > 0 ||
                syncResult.stats.numUpdates > 0 ||
                syncResult.stats.numDeletes > 0) {
            Log.i(TAG, "Aplicando operaciones...");
            try {
                resolver.applyBatch(Constantes.AUTHORITY, ops);
            } catch (RemoteException | OperationApplicationException e) {
                e.printStackTrace();
            }
            resolver.notifyChange(ContractCausalesAsistenciaAtraso.CONTENT_URI, null, false);
            Log.i(TAG, "Sincronización de tabla Causales Asistencia Atraso finalizada");
            enviarBroadcast(true, Mensajes.SYNC_FINALIZADA_CAUSALES_ASISTENCIA_ATRASO);
        } else {
            Log.i(TAG, "No se requiere sincronización de tabla Causales Asistencia Atraso");
            enviarBroadcast(true, Mensajes.SYNC_NOREQUERIDA + "Causales Asistencia Atraso");
        }
    }

    // MCI
    private void realizarSincronizacionRemotaInsertMCI() {
        Log.i(TAG, "SyncAdapterSubida: Preparando datos a ser insertados en el servidor...");

        iniciarActualizacion(ContractInsertMCIPdv.CONTENT_URI);

        Cursor c = obtenerRegistrosSucios(ContractInsertMCIPdv.CONTENT_URI, Projection.PROJECTION_INSERT_MCI);

        Log.i(TAG, "SyncAdapterSubida: Se encontraron " + c.getCount() + " registros sucios.");


        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                final int idLocal = c.getInt(Projection.COLUMNA_ID);

                Log.i(TAG, "SyncAdapterSubida: Insertando datos de MCI en el servidor...");

                VolleySingleton.getInstance(getContext()).addToRequestQueue(
                        new JsonObjectRequest(
                                Request.Method.POST,
                                Constantes.INSERTAR_MCI,
                                UtilidadesMCI.deCursorAJSONObject(c),
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        procesarRespuestaInsertMCI(response, idLocal);
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
//                                            Toast.makeText(getContext(), "Datos no enviados, almacenados en el teléfono.", Toast.LENGTH_SHORT).show();
                                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Network Timeout");
                                        } else if (error instanceof AuthFailureError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error AuthFailure");
                                        } else if (error instanceof ServerError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Server");
                                        } else if (error instanceof NetworkError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Network");
                                        } else if (error instanceof ParseError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Parse");
                                        }
                                    }
                                }
                        ) {
                            @Override
                            public Map<String, String> getHeaders() {
                                Map<String, String> headers = new HashMap<String, String>();
                                headers.put("Content-Type", "application/json; charset=utf-8");
                                headers.put("Accept", "application/json");
                                return headers;
                            }

                            @Override
                            public String getBodyContentType() {
                                return "application/json; charset=utf-8" ;//+ getParamsEncoding();
                            }
                        }
                );
            }
        } else {
            //Toast.makeText(getContext(),"No hay datos en cola de sincronizacion",Toast.LENGTH_SHORT).show();
//            StockActivity stockActivity = new StockActivity();
//            stockActivity.showToastFromBackground("No se requiere sincronización");
            Log.i(TAG, "SyncAdapterSubida: No se requiere sincronización: No hay datos en cola de sincronizacion");
            enviarBroadcast(true, Mensajes.SYNC_NODATOSCOLA);
        }
        c.close();
    }

    public void procesarRespuestaInsertMCI(JSONObject response, int idLocal) {
        try {
            // Obtener estado
            String estado = response.getString(Constantes.ESTADO);
            // Obtener mensaje
            String mensaje = response.getString(Constantes.MENSAJE);

            switch (estado) {
                case Constantes.SUCCESS:
                    // Obtener identificador del nuevo registro creado en el servidor
                    String idRemota = response.getString(Constantes.LAST_ID);
                    Log.i(TAG, mensaje);
                    //Borrar datos sucios:
                    finalizarActualizacion(ContractInsertMCIPdv.CONTENT_URI, ContractInsertMCIPdv.Columnas._ID, idRemota, idLocal);
                    enviarBroadcast(true, Mensajes.ON_SYNC_TRUE);
                    Toast.makeText(getContext(),"Datos almacenados en el servidor",Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "SyncAdapterSubida: Registros Insertados en el Servidor y en la Base Local "+idRemota);
                    break;

                case Constantes.FAILED:
                    Log.i(TAG, mensaje);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    //PRODUCTOS A CADUCAR
    private void realizarSincronizacionRemotaInsertProdCad() {
        Log.i(TAG, "SyncAdapterSubida: Preparando datos a ser insertados en el servidor...");

        iniciarActualizacion(ContractInsertProdCaducar.CONTENT_URI);

        Cursor c = obtenerRegistrosSucios(ContractInsertProdCaducar.CONTENT_URI, Projection.PROJECTION_INSERT_PROD_CADUCAR);

        Log.i(TAG, "SyncAdapterSubida: Se encontraron " + c.getCount() + " registros sucios.");


        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                final int idLocal = c.getInt(Projection.COLUMNA_ID);

                Log.i(TAG, "SyncAdapterSubida: Insertando datos de Precios en el servidor...");

                VolleySingleton.getInstance(getContext()).addToRequestQueue(
                        new JsonObjectRequest(
                                Request.Method.POST,
                                Constantes.INSERTAR_PROD_CAD,
                                UtilidadesProdCad.deCursorAJSONObject(c),
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        procesarRespuestaInsertProdCad(response, idLocal);
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
//                                            Toast.makeText(getContext(), "Datos no enviados, almacenados en el teléfono.", Toast.LENGTH_SHORT).show();
                                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Network Timeout");
                                        } else if (error instanceof AuthFailureError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error AuthFailure");
                                        } else if (error instanceof ServerError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Server");
                                        } else if (error instanceof NetworkError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Network");
                                        } else if (error instanceof ParseError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Parse");
                                        }
                                    }
                                }
                        ) {
                            @Override
                            public Map<String, String> getHeaders() {
                                Map<String, String> headers = new HashMap<String, String>();
                                headers.put("Content-Type", "application/json; charset=utf-8");
                                headers.put("Accept", "application/json");
                                return headers;
                            }

                            @Override
                            public String getBodyContentType() {
                                return "application/json; charset=utf-8" ;//+ getParamsEncoding();
                            }
                        }
                );
            }
        } else {
            //Toast.makeText(getContext(),"No hay datos en cola de sincronizacion",Toast.LENGTH_SHORT).show();
//            StockActivity stockActivity = new StockActivity();
//            stockActivity.showToastFromBackground("No se requiere sincronización");
            Log.i(TAG, "SyncAdapterSubida: No se requiere sincronización: No hay datos en cola de sincronizacion");
            enviarBroadcast(true, Mensajes.SYNC_NODATOSCOLA);
        }
        c.close();
    }

    public void procesarRespuestaInsertProdCad(JSONObject response, int idLocal) {
        try {
            // Obtener estado
            String estado = response.getString(Constantes.ESTADO);
            // Obtener mensaje
            String mensaje = response.getString(Constantes.MENSAJE);

            switch (estado) {
                case Constantes.SUCCESS:
                    // Obtener identificador del nuevo registro creado en el servidor
                    String idRemota = response.getString(Constantes.LAST_ID);
                    Log.i(TAG, mensaje);
                    //Borrar datos sucios:
                    finalizarActualizacion(ContractInsertProdCaducar.CONTENT_URI, ContractInsertProdCaducar.Columnas._ID, idRemota, idLocal);
                    enviarBroadcast(true, Mensajes.ON_SYNC_TRUE);
                    Toast.makeText(getContext(),"Datos almacenados en el servidor",Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "SyncAdapterSubida: Registros Insertados en el Servidor y en la Base Local "+idRemota);
                    break;

                case Constantes.FAILED:
                    Log.i(TAG, mensaje);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    //SUGERIDOS

    //PRODUCTOS A CADUCAR
    private void realizarSincronizacionRemotaInsertSugeridos() {
        Log.i(TAG, "SyncAdapterSubida: Preparando datos a ser insertados en el servidor...");

        iniciarActualizacion(ContractInsertSugeridos.CONTENT_URI);

        Cursor c = obtenerRegistrosSucios(ContractInsertSugeridos.CONTENT_URI, Projection.PROJECTION_INSERT_SUGERIDOS);

        Log.i(TAG, "SyncAdapterSubida: Se encontraron " + c.getCount() + " registros sucios.");


        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                final int idLocal = c.getInt(Projection.COLUMNA_ID);

                Log.i(TAG, "SyncAdapterSubida: Insertando datos de Sugeridos en el servidor...");

                VolleySingleton.getInstance(getContext()).addToRequestQueue(
                        new JsonObjectRequest(
                                Request.Method.POST,
                                Constantes.INSERTAR_SUGERIDOS,
                                UtilidadesSugeridos.deCursorAJSONObject(c),
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        procesarRespuestaInsertSugeridos(response, idLocal);
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
//                                            Toast.makeText(getContext(), "Datos no enviados, almacenados en el teléfono.", Toast.LENGTH_SHORT).show();
                                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Network Timeout");
                                        } else if (error instanceof AuthFailureError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error AuthFailure");
                                        } else if (error instanceof ServerError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Server");
                                        } else if (error instanceof NetworkError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Network");
                                        } else if (error instanceof ParseError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Parse");
                                        }
                                    }
                                }
                        ) {
                            @Override
                            public Map<String, String> getHeaders() {
                                Map<String, String> headers = new HashMap<String, String>();
                                headers.put("Content-Type", "application/json; charset=utf-8");
                                headers.put("Accept", "application/json");
                                return headers;
                            }

                            @Override
                            public String getBodyContentType() {
                                return "application/json; charset=utf-8" ;//+ getParamsEncoding();
                            }
                        }
                );
            }
        } else {
            //Toast.makeText(getContext(),"No hay datos en cola de sincronizacion",Toast.LENGTH_SHORT).show();
//            StockActivity stockActivity = new StockActivity();
//            stockActivity.showToastFromBackground("No se requiere sincronización");
            Log.i(TAG, "SyncAdapterSubida: No se requiere sincronización: No hay datos en cola de sincronizacion");
            enviarBroadcast(true, Mensajes.SYNC_NODATOSCOLA);
        }
        c.close();
    }

    public void procesarRespuestaInsertSugeridos(JSONObject response, int idLocal) {
        try {
            // Obtener estado
            String estado = response.getString(Constantes.ESTADO);
            // Obtener mensaje
            String mensaje = response.getString(Constantes.MENSAJE);

            switch (estado) {
                case Constantes.SUCCESS:
                    // Obtener identificador del nuevo registro creado en el servidor
                    String idRemota = response.getString(Constantes.LAST_ID);
                    Log.i(TAG, mensaje);
                    //Borrar datos sucios:
                    finalizarActualizacion(ContractInsertSugeridos.CONTENT_URI, ContractInsertSugeridos.Columnas._ID, idRemota, idLocal);
                    enviarBroadcast(true, Mensajes.ON_SYNC_TRUE);
                    Toast.makeText(getContext(),"Datos almacenados en el servidor",Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "SyncAdapterSubida: Registros Insertados en el Servidor y en la Base Local "+idRemota);
                    break;

                case Constantes.FAILED:
                    Log.i(TAG, mensaje);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }




    /*------------------------------------------------------------------------------------------------------------------------*/


    //ROTACION

    private void realizarSincronizacionRemotaInsertRotacion() {
        Log.i(TAG, "SyncAdapterSubida: Preparando datos a ser insertados en el servidor...");

        iniciarActualizacion(ContractInsertRotacion.CONTENT_URI);

        Cursor c = obtenerRegistrosSucios(ContractInsertRotacion.CONTENT_URI, Projection.PROJECTION_INSERT_ROTACION);

        Log.i(TAG, "SyncAdapterSubida: Se encontraron " + c.getCount() + " registros sucios.");


        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                final int idLocal = c.getInt(Projection.COLUMNA_ID);

                Log.i(TAG, "SyncAdapterSubida: Insertando datos de Rotacion en el servidor...");

                VolleySingleton.getInstance(getContext()).addToRequestQueue(
                        new JsonObjectRequest(
                                Request.Method.POST,
                                Constantes.INSERTAR_ROTACION,
                                UtilidadesRotacion.deCursorAJSONObject(c),
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        procesarRespuestaInsertRotacion(response, idLocal);
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
//                                            Toast.makeText(getContext(), "Datos no enviados, almacenados en el teléfono.", Toast.LENGTH_SHORT).show();
                                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Network Timeout");
                                        } else if (error instanceof AuthFailureError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error AuthFailure");
                                        } else if (error instanceof ServerError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Server");
                                        } else if (error instanceof NetworkError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Network");
                                        } else if (error instanceof ParseError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Parse");
                                        }
                                    }
                                }
                        ) {
                            @Override
                            public Map<String, String> getHeaders() {
                                Map<String, String> headers = new HashMap<String, String>();
                                headers.put("Content-Type", "application/json; charset=utf-8");
                                headers.put("Accept", "application/json");
                                return headers;
                            }

                            @Override
                            public String getBodyContentType() {
                                return "application/json; charset=utf-8" ;//+ getParamsEncoding();
                            }
                        }
                );
            }
        } else {
            //Toast.makeText(getContext(),"No hay datos en cola de sincronizacion",Toast.LENGTH_SHORT).show();
//            StockActivity stockActivity = new StockActivity();
//            stockActivity.showToastFromBackground("No se requiere sincronización");
            Log.i(TAG, "SyncAdapterSubida: No se requiere sincronización: No hay datos en cola de sincronizacion");
            enviarBroadcast(true, Mensajes.SYNC_NODATOSCOLA);
        }
        c.close();
    }

    public void procesarRespuestaInsertRotacion(JSONObject response, int idLocal) {
        try {
            // Obtener estado
            String estado = response.getString(Constantes.ESTADO);
            // Obtener mensaje
            String mensaje = response.getString(Constantes.MENSAJE);

            switch (estado) {
                case Constantes.SUCCESS:
                    // Obtener identificador del nuevo registro creado en el servidor
                    String idRemota = response.getString(Constantes.LAST_ID);
                    Log.i(TAG, mensaje);
                    //Borrar datos sucios:
                    finalizarActualizacion(ContractInsertRotacion.CONTENT_URI, ContractInsertRotacion.Columnas._ID, idRemota, idLocal);
                    enviarBroadcast(true, Mensajes.ON_SYNC_TRUE);
                    Toast.makeText(getContext(),"Datos almacenados en el servidor",Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "SyncAdapterSubida: Registros Insertados en el Servidor y en la Base Local "+idRemota);
                    break;

                case Constantes.FAILED:
                    Log.i(TAG, mensaje);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }




    private void realizarSincronizacionRemotaInsertPacks() {
        Log.i(TAG, "SyncAdapterSubida: Preparando datos a ser insertados en el servidor...");

        iniciarActualizacion(ContractInsertPacks.CONTENT_URI);

        Cursor c = obtenerRegistrosSucios(ContractInsertPacks.CONTENT_URI, Projection.PROJECTION_INSERT_PACKS);

        Log.i(TAG, "SyncAdapterSubida: Se encontraron " + c.getCount() + " registros sucios.");


        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                final int idLocal = c.getInt(Projection.COLUMNA_ID);

                Log.i(TAG, "SyncAdapterSubida: Insertando datos de Precios en el servidor...");

                VolleySingleton.getInstance(getContext()).addToRequestQueue(
                        new JsonObjectRequest(
                                Request.Method.POST,
                                Constantes.INSERTAR_PACKS,
                                UtilidadesPacks.deCursorAJSONObject(c),
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        procesarRespuestaInsertPacks(response, idLocal);
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
//                                            Toast.makeText(getContext(), "Datos no enviados, almacenados en el teléfono.", Toast.LENGTH_SHORT).show();
                                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Network Timeout");
                                        } else if (error instanceof AuthFailureError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error AuthFailure");
                                        } else if (error instanceof ServerError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Server");
                                        } else if (error instanceof NetworkError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Network");
                                        } else if (error instanceof ParseError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Parse");
                                        }
                                    }
                                }
                        ) {
                            @Override
                            public Map<String, String> getHeaders() {
                                Map<String, String> headers = new HashMap<String, String>();
                                headers.put("Content-Type", "application/json; charset=utf-8");
                                headers.put("Accept", "application/json");
                                return headers;
                            }

                            @Override
                            public String getBodyContentType() {
                                return "application/json; charset=utf-8" ;//+ getParamsEncoding();
                            }
                        }
                );
            }
        } else {
            //Toast.makeText(getContext(),"No hay datos en cola de sincronizacion",Toast.LENGTH_SHORT).show();
//            StockActivity stockActivity = new StockActivity();
//            stockActivity.showToastFromBackground("No se requiere sincronización");
            Log.i(TAG, "SyncAdapterSubida: No se requiere sincronización: No hay datos en cola de sincronizacion");
            enviarBroadcast(true, Mensajes.SYNC_NODATOSCOLA);
        }
        c.close();
    }

    public void procesarRespuestaInsertPacks(JSONObject response, int idLocal) {
        try {
            // Obtener estado
            String estado = response.getString(Constantes.ESTADO);
            // Obtener mensaje
            String mensaje = response.getString(Constantes.MENSAJE);

            switch (estado) {
                case Constantes.SUCCESS:
                    // Obtener identificador del nuevo registro creado en el servidor
                    String idRemota = response.getString(Constantes.LAST_ID);
                    Log.i(TAG, mensaje);
                    //Borrar datos sucios:
                    finalizarActualizacion(ContractInsertPacks.CONTENT_URI, ContractInsertPacks.Columnas._ID, idRemota, idLocal);
                    enviarBroadcast(true, Mensajes.ON_SYNC_TRUE);
                    Toast.makeText(getContext(),"Datos almacenados en el servidor",Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "SyncAdapterSubida: Registros Insertados en el Servidor y en la Base Local "+idRemota);
                    break;

                case Constantes.FAILED:
                    Log.i(TAG, mensaje);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /*INSERT TAREAS*/
    private void realizarSincronizacionRemotaInsertTareas() {
        Log.i(TAG, "SyncAdapterSubida: Preparando datos a ser insertados en el servidor...");

        iniciarActualizacion(ContractInsertTareas.CONTENT_URI);

        Cursor c = obtenerRegistrosSucios(ContractInsertTareas.CONTENT_URI, Projection.PROJECTION_INSERT_TAREAS);

        Log.i(TAG, "SyncAdapterSubida: Se encontraron " + c.getCount() + " registros sucios.");


        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                final int idLocal = c.getInt(Projection.COLUMNA_ID);

                Log.i(TAG, "SyncAdapterSubida: Insertando datos de Tareas en el servidor...");

                VolleySingleton.getInstance(getContext()).addToRequestQueue(
                        new JsonObjectRequest(
                                Request.Method.POST,
                                Constantes.INSERTAR_TAREAS,
                                UtilidadesTareas.deCursorAJSONObject(c),
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        procesarRespuestaInsertTareas(response, idLocal);
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
//                                            Toast.makeText(getContext(), "Datos no enviados, almacenados en el teléfono.", Toast.LENGTH_SHORT).show();
                                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Network Timeout");
                                        } else if (error instanceof AuthFailureError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error AuthFailure");
                                        } else if (error instanceof ServerError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Server");
                                        } else if (error instanceof NetworkError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Network");
                                        } else if (error instanceof ParseError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Parse");
                                        }
                                    }
                                }
                        ) {
                            @Override
                            public Map<String, String> getHeaders() {
                                Map<String, String> headers = new HashMap<String, String>();
                                headers.put("Content-Type", "application/json; charset=utf-8");
                                headers.put("Accept", "application/json");
                                return headers;
                            }

                            @Override
                            public String getBodyContentType() {
                                return "application/json; charset=utf-8" ;//+ getParamsEncoding();
                            }
                        }
                );
            }
        } else {
            //Toast.makeText(getContext(),"No hay datos en cola de sincronizacion",Toast.LENGTH_SHORT).show();
//            StockActivity stockActivity = new StockActivity();
//            stockActivity.showToastFromBackground("No se requiere sincronización");
            Log.i(TAG, "SyncAdapterSubida: No se requiere sincronización: No hay datos en cola de sincronizacion");
            enviarBroadcast(true, Mensajes.SYNC_NODATOSCOLA);
        }
        c.close();
    }

    public void procesarRespuestaInsertTareas(JSONObject response, int idLocal) {
        try {
            // Obtener estado
            String estado = response.getString(Constantes.ESTADO);
            // Obtener mensaje
            String mensaje = response.getString(Constantes.MENSAJE);

            switch (estado) {
                case Constantes.SUCCESS:
                    // Obtener identificador del nuevo registro creado en el servidor
                    String idRemota = response.getString(Constantes.LAST_ID);
                    Log.i(TAG, mensaje);
                    //Borrar datos sucios:
                    finalizarActualizacion(ContractInsertTareas.CONTENT_URI, ContractInsertTareas.Columnas._ID, idRemota, idLocal);
                    enviarBroadcast(true, Mensajes.ON_SYNC_TRUE);
                    Toast.makeText(getContext(),"Datos almacenados en el servidor",Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "SyncAdapterSubida: Registros Insertados en el Servidor y en la Base Local "+idRemota);
                    break;

                case Constantes.FAILED:
                    Log.i(TAG, mensaje);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /*--------------------------------------------------------------------------*/
    private void realizarSincronizacionRemotaInsertImpulso() {
        Log.i(TAG, "SyncAdapterSubida: Preparando datos a ser insertados en el servidor...");

        iniciarActualizacion(ContractInsertImpulso.CONTENT_URI);

        Cursor c = obtenerRegistrosSucios(ContractInsertImpulso.CONTENT_URI, Projection.PROJECTION_INSERT_IMPULSO);

        Log.i(TAG, "SyncAdapterSubida: Se encontraron " + c.getCount() + " registros sucios.");


        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                final int idLocal = c.getInt(Projection.COLUMNA_ID);

                Log.i(TAG, "SyncAdapterSubida: Insertando datos de Precios en el servidor...");

                VolleySingleton.getInstance(getContext()).addToRequestQueue(
                        new JsonObjectRequest(
                                Request.Method.POST,
                                Constantes.INSERTAR_IMPULSO,
                                UtilidadesImpulso.deCursorAJSONObject(c),
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        procesarRespuestaInsertImpulso(response, idLocal);
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
//                                            Toast.makeText(getContext(), "Datos no enviados, almacenados en el teléfono.", Toast.LENGTH_SHORT).show();
                                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Network Timeout");
                                        } else if (error instanceof AuthFailureError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error AuthFailure");
                                        } else if (error instanceof ServerError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Server");
                                        } else if (error instanceof NetworkError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Network");
                                        } else if (error instanceof ParseError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Parse");
                                        }
                                    }
                                }
                        ) {
                            @Override
                            public Map<String, String> getHeaders() {
                                Map<String, String> headers = new HashMap<String, String>();
                                headers.put("Content-Type", "application/json; charset=utf-8");
                                headers.put("Accept", "application/json");
                                return headers;
                            }

                            @Override
                            public String getBodyContentType() {
                                return "application/json; charset=utf-8" ;//+ getParamsEncoding();
                            }
                        }
                );
            }
        } else {
            //Toast.makeText(getContext(),"No hay datos en cola de sincronizacion",Toast.LENGTH_SHORT).show();
//            StockActivity stockActivity = new StockActivity();
//            stockActivity.showToastFromBackground("No se requiere sincronización");
            Log.i(TAG, "SyncAdapterSubida: No se requiere sincronización: No hay datos en cola de sincronizacion");
            enviarBroadcast(true, Mensajes.SYNC_NODATOSCOLA);
        }
        c.close();
    }

    public void procesarRespuestaInsertImpulso(JSONObject response, int idLocal) {
        try {
            // Obtener estado
            String estado = response.getString(Constantes.ESTADO);
            // Obtener mensaje
            String mensaje = response.getString(Constantes.MENSAJE);

            switch (estado) {
                case Constantes.SUCCESS:
                    // Obtener identificador del nuevo registro creado en el servidor
                    String idRemota = response.getString(Constantes.LAST_ID);
                    Log.i(TAG, mensaje);
                    //Borrar datos sucios:
                    finalizarActualizacion(ContractInsertImpulso.CONTENT_URI, ContractInsertImpulso.Columnas._ID, idRemota, idLocal);
                    enviarBroadcast(true, Mensajes.ON_SYNC_TRUE);
                    Toast.makeText(getContext(),"Datos almacenados en el servidor",Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "SyncAdapterSubida: Registros Insertados en el Servidor y en la Base Local "+idRemota);
                    break;

                case Constantes.FAILED:
                    Log.i(TAG, mensaje);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void realizarSincronizacionRemotaInsertMallaCodificados() {
        Log.i(TAG, "SyncAdapterSubida: Preparando datos a ser insertados en el servidor...");

        iniciarActualizacion(ContractInsertMallaCodificados.CONTENT_URI);

        Cursor c = obtenerRegistrosSucios(ContractInsertMallaCodificados.CONTENT_URI, Projection.PROJECTION_INSERT_MALLA_CODIFICADOS);

        Log.i(TAG, "SyncAdapterSubida: Se encontraron " + c.getCount() + " registros sucios.");


        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                final int idLocal = c.getInt(Projection.COLUMNA_ID);

                Log.i(TAG, "SyncAdapterSubida: Insertando datos de Precios en el servidor...");

                VolleySingleton.getInstance(getContext()).addToRequestQueue(
                        new JsonObjectRequest(
                                Request.Method.POST,
                                Constantes.INSERTAR_MALLA_CODIFICADOS,
                                UtilidadesMallaCodificados.deCursorAJSONObject(c),
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        procesarRespuestaInsertMallaCodificados(response, idLocal);
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
//                                            Toast.makeText(getContext(), "Datos no enviados, almacenados en el teléfono.", Toast.LENGTH_SHORT).show();
                                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Network Timeout");
                                        } else if (error instanceof AuthFailureError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error AuthFailure");
                                        } else if (error instanceof ServerError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Server");
                                        } else if (error instanceof NetworkError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Network");
                                        } else if (error instanceof ParseError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Parse");
                                        }
                                    }
                                }
                        ) {
                            @Override
                            public Map<String, String> getHeaders() {
                                Map<String, String> headers = new HashMap<String, String>();
                                headers.put("Content-Type", "application/json; charset=utf-8");
                                headers.put("Accept", "application/json");
                                return headers;
                            }

                            @Override
                            public String getBodyContentType() {
                                return "application/json; charset=utf-8" ;//+ getParamsEncoding();
                            }
                        }
                );
            }
        } else {
            //Toast.makeText(getContext(),"No hay datos en cola de sincronizacion",Toast.LENGTH_SHORT).show();
//            StockActivity stockActivity = new StockActivity();
//            stockActivity.showToastFromBackground("No se requiere sincronización");
            Log.i(TAG, "SyncAdapterSubida: No se requiere sincronización: No hay datos en cola de sincronizacion");
            enviarBroadcast(true, Mensajes.SYNC_NODATOSCOLA);
        }
        c.close();
    }

    public void procesarRespuestaInsertMallaCodificados(JSONObject response, int idLocal) {
        try {
            // Obtener estado
            String estado = response.getString(Constantes.ESTADO);
            // Obtener mensaje
            String mensaje = response.getString(Constantes.MENSAJE);

            switch (estado) {
                case Constantes.SUCCESS:
                    // Obtener identificador del nuevo registro creado en el servidor
                    String idRemota = response.getString(Constantes.LAST_ID);
                    Log.i(TAG, mensaje);
                    //Borrar datos sucios:
                    finalizarActualizacion(ContractInsertMallaCodificados.CONTENT_URI, ContractInsertMallaCodificados.Columnas._ID, idRemota, idLocal);
                    enviarBroadcast(true, Mensajes.ON_SYNC_TRUE);
                    Toast.makeText(getContext(),"Datos almacenados en el servidor",Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "SyncAdapterSubida: Registros Insertados en el Servidor y en la Base Local "+idRemota);
                    break;

                case Constantes.FAILED:
                    Log.i(TAG, mensaje);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void realizarSincronizacionRemotaInsertEvidencias() {
        Log.i(TAG, "SyncAdapterSubida: Preparando datos a ser insertados en el servidor...");

        iniciarActualizacion(ContractInsertEvidencias.CONTENT_URI);

        Cursor c = obtenerRegistrosSucios(ContractInsertEvidencias.CONTENT_URI, Projection.PROJECTION_INSERT_EVIDENCIAS);

        Log.i(TAG, "SyncAdapterSubida: Se encontraron " + c.getCount() + " registros sucios.");


        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                final int idLocal = c.getInt(Projection.COLUMNA_ID);

                Log.i(TAG, "SyncAdapterSubida: Insertando datos de Fotografico en el servidor...");

                VolleySingleton.getInstance(getContext()).addToRequestQueue(
                        new JsonObjectRequest(
                                Request.Method.POST,
                                Constantes.INSERTAR_EVIDENCIA,
                                UtilidadesEvidencias.deCursorAJSONObject(c),
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        procesarRespuestaInsertEvidencias(response, idLocal);
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
//                                            Toast.makeText(getContext(), "Datos no enviados, almacenados en el teléfono.", Toast.LENGTH_SHORT).show();
                                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Network Timeout");
                                        } else if (error instanceof AuthFailureError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error AuthFailure");
                                        } else if (error instanceof ServerError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Server");
                                        } else if (error instanceof NetworkError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Network");
                                        } else if (error instanceof ParseError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Parse");
                                        }
                                    }
                                }
                        ) {
                            @Override
                            public Map<String, String> getHeaders() {
                                Map<String, String> headers = new HashMap<String, String>();
                                headers.put("Content-Type", "application/json; charset=utf-8");
                                headers.put("Accept", "application/json");
                                return headers;
                            }

                            @Override
                            public String getBodyContentType() {
                                return "application/json; charset=utf-8";// + getParamsEncoding();
                            }
                        }
                );
            }
        } else {
            //Toast.makeText(getContext(),"No hay datos en cola de sincronizacion",Toast.LENGTH_SHORT).show();
//            StockActivity stockActivity = new StockActivity();
//            stockActivity.showToastFromBackground("No se requiere sincronización");
            Log.i(TAG, "SyncAdapterSubida: No se requiere sincronización: No hay datos en cola de sincronizacion");
            enviarBroadcast(true, Mensajes.SYNC_NODATOSCOLA);
        }
        c.close();
    }

    /**
     * Procesa los diferentes tipos de respuesta obtenidos del servidor
     *
     * @param response Respuesta en subchannel Json
     */
    public void procesarRespuestaInsertEvidencias(JSONObject response, int idLocal) {

        try {
            // Obtener estado
            String estado = response.getString(Constantes.ESTADO);
            // Obtener mensaje
            String mensaje = response.getString(Constantes.MENSAJE);

            switch (estado) {
                case Constantes.SUCCESS:
                    // Obtener identificador del nuevo registro creado en el servidor
                    String idRemota = response.getString(Constantes.LAST_ID);
                    Log.i(TAG, mensaje);
                    //Borrar datos sucios:
                    finalizarActualizacion(ContractInsertEvidencias.CONTENT_URI, ContractInsertEvidencias.Columnas._ID, idRemota, idLocal);
                    enviarBroadcast(true, Mensajes.ON_SYNC_TRUE);
                    Toast.makeText(getContext(),"Datos almacenados en el servidor",Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "SyncAdapterSubida: Registros Insertados en el Servidor y en la Base Local "+idRemota);
                    break;

                case Constantes.FAILED:
                    Log.i(TAG, mensaje);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /*PREGUNTAS*/
    private void realizarSincronizacionRemotaInsertPreguntas() {
        Log.i(TAG, "SyncAdapterSubida: Preparando datos a ser insertados en el servidor...");

        iniciarActualizacion(ContractInsertPreguntas.CONTENT_URI);

        Cursor c = obtenerRegistrosSucios(ContractInsertPreguntas.CONTENT_URI, Projection.PROJECTION_INSERT_PREGUNTAS);

        Log.i(TAG, "SyncAdapterSubida: Se encontraron " + c.getCount() + " registros sucios.");


        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                final int idLocal = c.getInt(Projection.COLUMNA_ID);

                Log.i(TAG, "SyncAdapterSubida: Insertando datos de Precios en el servidor...");

                VolleySingleton.getInstance(getContext()).addToRequestQueue(
                        new JsonObjectRequest(
                                Request.Method.POST,
                                Constantes.INSERTAR_PREGUNTAS,
                                UtilidadesPreguntas.deCursorAJSONObject(c),
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        procesarRespuestaInsertPreguntas(response, idLocal);
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
//                                            Toast.makeText(getContext(), "Datos no enviados, almacenados en el teléfono.", Toast.LENGTH_SHORT).show();
                                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Network Timeout");
                                        } else if (error instanceof AuthFailureError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error AuthFailure");
                                        } else if (error instanceof ServerError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Server");
                                        } else if (error instanceof NetworkError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Network");
                                        } else if (error instanceof ParseError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Parse");
                                        }
                                    }
                                }
                        ) {
                            @Override
                            public Map<String, String> getHeaders() {
                                Map<String, String> headers = new HashMap<String, String>();
                                headers.put("Content-Type", "application/json; charset=utf-8");
                                headers.put("Accept", "application/json");
                                return headers;
                            }

                            @Override
                            public String getBodyContentType() {
                                return "application/json; charset=utf-8" ;//+ getParamsEncoding();
                            }
                        }
                );
            }
        } else {
            //Toast.makeText(getContext(),"No hay datos en cola de sincronizacion",Toast.LENGTH_SHORT).show();
//            StockActivity stockActivity = new StockActivity();
//            stockActivity.showToastFromBackground("No se requiere sincronización");
            Log.i(TAG, "SyncAdapterSubida: No se requiere sincronización: No hay datos en cola de sincronizacion");
            enviarBroadcast(true, Mensajes.SYNC_NODATOSCOLA);
        }
        c.close();
    }

    public void procesarRespuestaInsertPreguntas(JSONObject response, int idLocal) {
        try {
            // Obtener estado
            String estado = response.getString(Constantes.ESTADO);
            // Obtener mensaje
            String mensaje = response.getString(Constantes.MENSAJE);

            switch (estado) {
                case Constantes.SUCCESS:
                    // Obtener identificador del nuevo registro creado en el servidor
                    String idRemota = response.getString(Constantes.LAST_ID);
                    Log.i(TAG, mensaje);
                    //Borrar datos sucios:
                    finalizarActualizacion(ContractInsertPreguntas.CONTENT_URI, ContractInsertPreguntas.Columnas._ID, idRemota, idLocal);
                    enviarBroadcast(true, Mensajes.ON_SYNC_TRUE);
                    Toast.makeText(getContext(),"Datos almacenados en el servidor",Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "SyncAdapterSubida: Registros Insertados en el Servidor y en la Base Local "+idRemota);
                    break;

                case Constantes.FAILED:
                    Log.i(TAG, mensaje);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /* RESULTADO PREGUNTAS */

    private void realizarSincronizacionRemotaInsertResultadoPreguntas() {
        Log.i(TAG, "SyncAdapterSubida: Preparando datos a ser insertados en el servidor...");

        iniciarActualizacion(ContractInsertResultadoPreguntas.CONTENT_URI);

        Cursor c = obtenerRegistrosSucios(ContractInsertResultadoPreguntas.CONTENT_URI, Projection.PROJECTION_RESULTADO_PREGUNTAS);

        Log.i(TAG, "SyncAdapterSubida: Se encontraron " + c.getCount() + " registros sucios.");


        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                final int idLocal = c.getInt(Projection.COLUMNA_ID);

                Log.i(TAG, "SyncAdapterSubida: Insertando datos de Precios en el servidor...");

                VolleySingleton.getInstance(getContext()).addToRequestQueue(
                        new JsonObjectRequest(
                                Request.Method.POST,
                                Constantes.INSERTAR_RESULTADO_PREGUNTAS,
                                UtilidadesResultadoPreguntas.deCursorAJSONObject(c),
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        procesarRespuestaInsertResultadoPreguntas(response, idLocal);
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
//                                            Toast.makeText(getContext(), "Datos no enviados, almacenados en el teléfono.", Toast.LENGTH_SHORT).show();
                                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Network Timeout");
                                        } else if (error instanceof AuthFailureError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error AuthFailure");
                                        } else if (error instanceof ServerError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Server");
                                        } else if (error instanceof NetworkError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Network");
                                        } else if (error instanceof ParseError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Parse");
                                        }
                                    }
                                }
                        ) {
                            @Override
                            public Map<String, String> getHeaders() {
                                Map<String, String> headers = new HashMap<String, String>();
                                headers.put("Content-Type", "application/json; charset=utf-8");
                                headers.put("Accept", "application/json");
                                return headers;
                            }

                            @Override
                            public String getBodyContentType() {
                                return "application/json; charset=utf-8" ;//+ getParamsEncoding();
                            }
                        }
                );
            }
        } else {
            //Toast.makeText(getContext(),"No hay datos en cola de sincronizacion",Toast.LENGTH_SHORT).show();
//            StockActivity stockActivity = new StockActivity();
//            stockActivity.showToastFromBackground("No se requiere sincronización");
            Log.i(TAG, "SyncAdapterSubida: No se requiere sincronización: No hay datos en cola de sincronizacion");
            enviarBroadcast(true, Mensajes.SYNC_NODATOSCOLA);
        }
        c.close();
    }

    public void procesarRespuestaInsertResultadoPreguntas(JSONObject response, int idLocal) {
        try {
            // Obtener estado
            String estado = response.getString(Constantes.ESTADO);
            // Obtener mensaje
            String mensaje = response.getString(Constantes.MENSAJE);

            switch (estado) {
                case Constantes.SUCCESS:
                    // Obtener identificador del nuevo registro creado en el servidor
                    String idRemota = response.getString(Constantes.LAST_ID);
                    Log.i(TAG, mensaje);
                    //Borrar datos sucios:
                    finalizarActualizacion(ContractInsertResultadoPreguntas.CONTENT_URI, ContractInsertResultadoPreguntas.Columnas._ID, idRemota, idLocal);
                    enviarBroadcast(true, Mensajes.ON_SYNC_TRUE);
                    Toast.makeText(getContext(),"Datos almacenados en el servidor",Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "SyncAdapterSubida: Registros Insertados en el Servidor y en la Base Local "+idRemota);
                    break;

                case Constantes.FAILED:
                    Log.i(TAG, mensaje);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

/*

    insert_pdv_fotografico

*/

    private void realizarSincronizacionRemotaInsertPdvFotografico() {
        Log.i(TAG, "SyncAdapterSubida: Preparando datos a ser insertados en el servidor...");

        iniciarActualizacion(ContractInsertPdvFotografico.CONTENT_URI);

        Cursor c = obtenerRegistrosSucios(ContractInsertPdvFotografico.CONTENT_URI, Projection.PROJECTION_INSERT_PDV_FOTOGRAFICO);

        Log.i(TAG, "SyncAdapterSubida: Se encontraron " + c.getCount() + " registros sucios.");

        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                final int idLocal = c.getInt(Projection.COLUMNA_ID);

                Log.i(TAG, "SyncAdapterSubida: Insertando datos de PdvFotografico en el servidor...");

                VolleySingleton.getInstance(getContext()).addToRequestQueue(
                        new JsonObjectRequest(
                                Request.Method.POST,
                                Constantes.INSERTAR_PDV_FOTOGRAFICO,
                                UtilidadesPdvFotografico.deCursorAJSONObject(c),
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        procesarRespuestaInsertPdvFotografico(response, idLocal);
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
//                                            Toast.makeText(getContext(), "Datos no enviados, almacenados en el teléfono.", Toast.LENGTH_SHORT).show();
                                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Network Timeout");
                                        } else if (error instanceof AuthFailureError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error AuthFailure");
                                        } else if (error instanceof ServerError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Server");
                                        } else if (error instanceof NetworkError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Network");
                                        } else if (error instanceof ParseError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Parse");
                                        }
                                    }
                                }
                        ) {
                            @Override
                            public Map<String, String> getHeaders() {
                                Map<String, String> headers = new HashMap<String, String>();
                                headers.put("Content-Type", "application/json; charset=utf-8");
                                headers.put("Accept", "application/json");
                                return headers;
                            }

                            @Override
                            public String getBodyContentType() {
                                return "application/json; charset=utf-8";// + getParamsEncoding();
                            }
                        }
                );
            }
        } else {
            //Toast.makeText(getContext(),"No hay datos en cola de sincronizacion",Toast.LENGTH_SHORT).show();
//            StockActivity stockActivity = new StockActivity();
//            stockActivity.showToastFromBackground("No se requiere sincronización");
            Log.i(TAG, "SyncAdapterSubida: No se requiere sincronización: No hay datos en cola de sincronizacion");
            enviarBroadcast(true, Mensajes.SYNC_NODATOSCOLA);
        }
        c.close();
    }


    public void procesarRespuestaInsertPdvFotografico(JSONObject response, int idLocal) {
        try {
            // Obtener estado
            String estado = response.getString(Constantes.ESTADO);
            // Obtener mensaje
            String mensaje = response.getString(Constantes.MENSAJE);

            switch (estado) {
                case Constantes.SUCCESS:
                    // Obtener identificador del nuevo registro creado en el servidor
                    String idRemota = response.getString(Constantes.LAST_ID);
                    Log.i(TAG, mensaje);
                    //Borrar datos sucios:
                    finalizarActualizacion(ContractInsertPdvFotografico.CONTENT_URI, ContractInsertPdvFotografico.Columnas._ID, idRemota, idLocal);
                    enviarBroadcast(true, Mensajes.ON_SYNC_TRUE);
                    Toast.makeText(getContext(),"Datos almacenados en el servidor",Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "SyncAdapterSubida: Registros Insertados en el Servidor y en la Base Local "+idRemota);
                    break;

                case Constantes.FAILED:
                    Log.i(TAG, mensaje);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**/
    private void realizarSincronizacionRemotaInsertPromo() {
        Log.i(TAG, "SyncAdapterSubida: Preparando datos a ser insertados en el servidor...");

        iniciarActualizacion(ContractInsertPromocion.CONTENT_URI);

        Cursor c = obtenerRegistrosSucios(ContractInsertPromocion.CONTENT_URI, Projection.PROJECTION_INSERTPROMO);

        Log.i(TAG, "SyncAdapterSubida: Se encontraron " + c.getCount() + " registros sucios.");

        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                final int idLocal = c.getInt(Projection.COLUMNA_ID);

                Log.i(TAG, "SyncAdapterSubida: Insertando datos de Precios en el servidor...");

                VolleySingleton.getInstance(getContext()).addToRequestQueue(
                        new JsonObjectRequest(
                                Request.Method.POST,
                                Constantes.INSERTAR_PROMO,
                                UtilidadesPromocion.deCursorAJSONObject(c),
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        procesarRespuestaInsertPromo(response, idLocal);
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
//                                            Toast.makeText(getContext(), "Datos no enviados, almacenados en el teléfono.", Toast.LENGTH_SHORT).show();
                                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Network Timeout");
                                        } else if (error instanceof AuthFailureError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error AuthFailure");
                                        } else if (error instanceof ServerError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Server");
                                        } else if (error instanceof NetworkError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Network");
                                        } else if (error instanceof ParseError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Parse");
                                        }
                                    }
                                }
                        ) {
                            @Override
                            public Map<String, String> getHeaders() {
                                Map<String, String> headers = new HashMap<String, String>();
                                headers.put("Content-Type", "application/json; charset=utf-8");
                                headers.put("Accept", "application/json");
                                return headers;
                            }

                            @Override
                            public String getBodyContentType() {
                                return "application/json; charset=utf-8";// + getParamsEncoding();
                            }
                        }
                );
            }
        } else {
            //Toast.makeText(getContext(),"No hay datos en cola de sincronizacion",Toast.LENGTH_SHORT).show();
//            StockActivity stockActivity = new StockActivity();
//            stockActivity.showToastFromBackground("No se requiere sincronización");
            Log.i(TAG, "SyncAdapterSubida: No se requiere sincronización: No hay datos en cola de sincronizacion");
            enviarBroadcast(true, Mensajes.SYNC_NODATOSCOLA);
        }
        c.close();
    }


    public void procesarRespuestaInsertPromo(JSONObject response, int idLocal) {
        try {
            // Obtener estado
            String estado = response.getString(Constantes.ESTADO);
            // Obtener mensaje
            String mensaje = response.getString(Constantes.MENSAJE);

            switch (estado) {
                case Constantes.SUCCESS:
                    // Obtener identificador del nuevo registro creado en el servidor
                    String idRemota = response.getString(Constantes.LAST_ID);
                    Log.i(TAG, mensaje);
                    //Borrar datos sucios:
                    finalizarActualizacion(ContractInsertPromocion.CONTENT_URI, ContractInsertPromocion.Columnas._ID, idRemota, idLocal);
                    enviarBroadcast(true, Mensajes.ON_SYNC_TRUE);
                    Toast.makeText(getContext(),"Datos almacenados en el servidor",Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "SyncAdapterSubida: Registros Insertados en el Servidor y en la Base Local "+idRemota);
                    break;

                case Constantes.FAILED:
                    Log.i(TAG, mensaje);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void realizarSincronizacionRemotaInsertImplementacion() {
        Log.i(TAG, "SyncAdapterSubida: Preparando datos a ser insertados en el servidor...");

        iniciarActualizacion(ContractInsertImplementacion.CONTENT_URI);

        Cursor c = obtenerRegistrosSucios(ContractInsertImplementacion.CONTENT_URI, Projection.PROJECTION_INSERTIMPLEM);

        Log.i(TAG, "SyncAdapterSubida: Se encontraron " + c.getCount() + " registros sucios.");


        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                final int idLocal = c.getInt(Projection.COLUMNA_ID);

                Log.i(TAG, "SyncAdapterSubida: Insertando datos de Precios en el servidor...");

                VolleySingleton.getInstance(getContext()).addToRequestQueue(
                        new JsonObjectRequest(
                                Request.Method.POST,
                                Constantes.INSERTAR_IMPLEM,
                                UtilidadesImplementacion.deCursorAJSONObject(c),
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        procesarRespuestaInsertImplementacion(response, idLocal);
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
//                                            Toast.makeText(getContext(), "Datos no enviados, almacenados en el teléfono.", Toast.LENGTH_SHORT).show();
                                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Network Timeout");
                                        } else if (error instanceof AuthFailureError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error AuthFailure");
                                        } else if (error instanceof ServerError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Server");
                                        } else if (error instanceof NetworkError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Network");
                                        } else if (error instanceof ParseError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Parse");
                                        }
                                    }
                                }
                        ) {
                            @Override
                            public Map<String, String> getHeaders() {
                                Map<String, String> headers = new HashMap<String, String>();
                                headers.put("Content-Type", "application/json; charset=utf-8");
                                headers.put("Accept", "application/json");
                                return headers;
                            }

                            @Override
                            public String getBodyContentType() {
                                return "application/json; charset=utf-8";// + getParamsEncoding();
                            }
                        }
                );
            }
        } else {
            //Toast.makeText(getContext(),"No hay datos en cola de sincronizacion",Toast.LENGTH_SHORT).show();
//            StockActivity stockActivity = new StockActivity();
//            stockActivity.showToastFromBackground("No se requiere sincronización");
            Log.i(TAG, "SyncAdapterSubida: No se requiere sincronización: No hay datos en cola de sincronizacion");
            enviarBroadcast(true, Mensajes.SYNC_NODATOSCOLA);
        }
        c.close();
    }


    public void procesarRespuestaInsertImplementacion(JSONObject response, int idLocal) {

        try {
            // Obtener estado
            String estado = response.getString(Constantes.ESTADO);
            // Obtener mensaje
            String mensaje = response.getString(Constantes.MENSAJE);

            switch (estado) {
                case Constantes.SUCCESS:
                    // Obtener identificador del nuevo registro creado en el servidor
                    String idRemota = response.getString(Constantes.LAST_ID);
                    Log.i(TAG, mensaje);
                    //Borrar datos sucios:
                    finalizarActualizacion(ContractInsertImplementacion.CONTENT_URI, ContractInsertImplementacion.Columnas._ID, idRemota, idLocal);
                    enviarBroadcast(true, Mensajes.ON_SYNC_TRUE);
                    Toast.makeText(getContext(),"Datos almacenados en el servidor",Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "SyncAdapterSubida: Registros Insertados en el Servidor y en la Base Local "+idRemota);
                    break;

                case Constantes.FAILED:
                    Log.i(TAG, mensaje);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void realizarSincronizacionRemotaInsertValores() {
        Log.i(TAG, "SyncAdapterSubida: Preparando datos a ser insertados en el servidor...");

        iniciarActualizacion(ContractInsertValores.CONTENT_URI);

        Cursor c = obtenerRegistrosSucios(ContractInsertValores.CONTENT_URI, Projection.PROJECTION_INSERTVALORES);

        Log.i(TAG, "SyncAdapterSubida: Se encontraron " + c.getCount() + " registros sucios.");


        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                final int idLocal = c.getInt(Projection.COLUMNA_ID);

                Log.i(TAG, "SyncAdapterSubida: Insertando datos de Precios en el servidor...");

                VolleySingleton.getInstance(getContext()).addToRequestQueue(
                        new JsonObjectRequest(
                                Request.Method.POST,
                                Constantes.INSERTAR_VALORES,
                                UtilidadesValores.deCursorAJSONObject(c),
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        procesarRespuestaInsertValores(response, idLocal);
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
//                                            Toast.makeText(getContext(), "Datos no enviados, almacenados en el teléfono.", Toast.LENGTH_SHORT).show();
                                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Network Timeout");
                                        } else if (error instanceof AuthFailureError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error AuthFailure");
                                        } else if (error instanceof ServerError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Server");
                                        } else if (error instanceof NetworkError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Network");
                                        } else if (error instanceof ParseError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Parse");
                                        }
                                    }
                                }
                        ) {
                            @Override
                            public Map<String, String> getHeaders() {
                                Map<String, String> headers = new HashMap<String, String>();
                                headers.put("Content-Type", "application/json; charset=utf-8");
                                headers.put("Accept", "application/json");
                                return headers;
                            }

                            @Override
                            public String getBodyContentType() {
                                return "application/json; charset=utf-8";// + getParamsEncoding();
                            }
                        }
                );
            }
        } else {
            //Toast.makeText(getContext(),"No hay datos en cola de sincronizacion",Toast.LENGTH_SHORT).show();
//            StockActivity stockActivity = new StockActivity();
//            stockActivity.showToastFromBackground("No se requiere sincronización");
            Log.i(TAG, "SyncAdapterSubida: No se requiere sincronización: No hay datos en cola de sincronizacion");
            enviarBroadcast(true, Mensajes.SYNC_NODATOSCOLA);
        }
        c.close();
    }


    public void procesarRespuestaInsertValores(JSONObject response, int idLocal) {

        try {
            // Obtener estado
            String estado = response.getString(Constantes.ESTADO);
            // Obtener mensaje
            String mensaje = response.getString(Constantes.MENSAJE);

            switch (estado) {
                case Constantes.SUCCESS:
                    // Obtener identificador del nuevo registro creado en el servidor
                    String idRemota = response.getString(Constantes.LAST_ID);
                    Log.i(TAG, mensaje);
                    //Borrar datos sucios:
                    finalizarActualizacion(ContractInsertValores.CONTENT_URI, ContractInsertValores.Columnas._ID, idRemota, idLocal);
                    enviarBroadcast(true, Mensajes.ON_SYNC_TRUE);
                    Toast.makeText(getContext(),"Datos almacenados en el servidor",Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "SyncAdapterSubida: Registros Insertados en el Servidor y en la Base Local "+idRemota);
                    break;

                case Constantes.FAILED:
                    Log.i(TAG, mensaje);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }



    private void realizarSincronizacionRemotaInsertPDV() {
        Log.i(TAG, "SyncAdapterSubida: Preparando datos a ser insertados en el servidor...");

        iniciarActualizacion(ContractInsertPdv.CONTENT_URI);

        Cursor c = obtenerRegistrosSucios(ContractInsertPdv.CONTENT_URI, Projection.PROJECTION_INSERTPDV);

        Log.i(TAG, "SyncAdapterSubida: Se encontraron " + c.getCount() + " registros sucios.");


        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                final int idLocal = c.getInt(Projection.COLUMNA_ID);

                Log.i(TAG, "SyncAdapterSubida: Insertando datos de PDV en el servidor...");

                VolleySingleton.getInstance(getContext()).addToRequestQueue(
                        new JsonObjectRequest(
                                Request.Method.POST,
                                Constantes.INSERTAR_PDV,
                                UtilidadesPdv.deCursorAJSONObject(c),
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        procesarRespuestaInsertPDV(response, idLocal);
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
//                                            Toast.makeText(getContext(), "Datos no enviados, almacenados en el teléfono.", Toast.LENGTH_SHORT).show();
                                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Network Timeout");
                                        } else if (error instanceof AuthFailureError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error AuthFailure");
                                        } else if (error instanceof ServerError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Server");
                                        } else if (error instanceof NetworkError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Network");
                                        } else if (error instanceof ParseError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Parse");
                                        }
                                    }
                                }
                        ) {
                            @Override
                            public Map<String, String> getHeaders() {
                                Map<String, String> headers = new HashMap<String, String>();
                                headers.put("Content-Type", "application/json; charset=utf-8");
                                headers.put("Accept", "application/json");
                                return headers;
                            }

                            @Override
                            public String getBodyContentType() {
                                return "application/json; charset=utf-8" + getParamsEncoding();
                            }
                        }
                );
            }
        } else {
            //Toast.makeText(getContext(),"No hay datos en cola de sincronizacion",Toast.LENGTH_SHORT).show();
//            StockActivity stockActivity = new StockActivity();
//            stockActivity.showToastFromBackground("No se requiere sincronización");
            Log.i(TAG, "SyncAdapterSubida: No se requiere sincronización: No hay datos en cola de sincronizacion");
            enviarBroadcast(true, Mensajes.SYNC_NODATOSCOLA);
        }
        c.close();
    }

    public void procesarRespuestaInsertPDV(JSONObject response, int idLocal) {

        try {
            // Obtener estado
            String estado = response.getString(Constantes.ESTADO);
            // Obtener mensaje
            String mensaje = response.getString(Constantes.MENSAJE);

            switch (estado) {
                case Constantes.SUCCESS:
                    // Obtener identificador del nuevo registro creado en el servidor
                    String idRemota = response.getString(Constantes.LAST_ID);
                    Log.i(TAG, mensaje);
                    //Borrar datos sucios:
                    finalizarActualizacion(ContractInsertPdv.CONTENT_URI, ContractInsertPdv.Columnas._ID, idRemota, idLocal);
                    enviarBroadcast(true, Mensajes.ON_SYNC_TRUE);
                    Toast.makeText(getContext(),"Datos almacenados en el servidor",Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "SyncAdapterSubida: Registros Insertados en el Servidor y en la Base Local "+idRemota);
                    break;

                case Constantes.FAILED:
                    Log.i(TAG, mensaje);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }



    private void realizarSincronizacionRemotaInsertAgotados() {
        Log.i(TAG, "SyncAdapterSubida: Preparando datos a ser insertados en el servidor...");

        iniciarActualizacion(ContractInsertAgotados.CONTENT_URI);

        Cursor c = obtenerRegistrosSucios(ContractInsertAgotados.CONTENT_URI, Projection.PROJECTION_INSERTAGOTADOS);

        Log.i(TAG, "SyncAdapterSubida: Se encontraron " + c.getCount() + " registros sucios.");


        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                final int idLocal = c.getInt(Projection.COLUMNA_ID);

                Log.i(TAG, "SyncAdapterSubida: Insertando datos de Agotados en el servidor...");

                VolleySingleton.getInstance(getContext()).addToRequestQueue(
                        new JsonObjectRequest(
                                Request.Method.POST,
                                Constantes.INSERTAR_AGOTADOS,
                                UtilidadesAgotados.deCursorAJSONObject(c),
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        procesarRespuestaInsertAgotados(response, idLocal);
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
//                                            Toast.makeText(getContext(), "Datos no enviados, almacenados en el teléfono.", Toast.LENGTH_SHORT).show();
                                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Network Timeout");
                                        } else if (error instanceof AuthFailureError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error AuthFailure");
                                        } else if (error instanceof ServerError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Server");
                                        } else if (error instanceof NetworkError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Network");
                                        } else if (error instanceof ParseError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Parse");
                                        }
                                    }
                                }
                        ) {
                            @Override
                            public Map<String, String> getHeaders() {
                                Map<String, String> headers = new HashMap<String, String>();
                                headers.put("Content-Type", "application/json; charset=utf-8");
                                headers.put("Accept", "application/json");
                                return headers;
                            }

                            @Override
                            public String getBodyContentType() {
                                return "application/json; charset=utf-8";// + getParamsEncoding();
                            }
                        }
                );
            }
        } else {
            //Toast.makeText(getContext(),"No hay datos en cola de sincronizacion",Toast.LENGTH_SHORT).show();
//            StockActivity stockActivity = new StockActivity();
//            stockActivity.showToastFromBackground("No se requiere sincronización");
            Log.i(TAG, "SyncAdapterSubida: No se requiere sincronización: No hay datos en cola de sincronizacion");
            enviarBroadcast(true, Mensajes.SYNC_NODATOSCOLA);
        }
        c.close();
    }

    public void procesarRespuestaInsertAgotados(JSONObject response, int idLocal) {

        try {
            // Obtener estado
            String estado = response.getString(Constantes.ESTADO);
            // Obtener mensaje
            String mensaje = response.getString(Constantes.MENSAJE);

            switch (estado) {
                case Constantes.SUCCESS:
                    // Obtener identificador del nuevo registro creado en el servidor
                    String idRemota = response.getString(Constantes.LAST_ID);
                    Log.i(TAG, mensaje);
                    //Borrar datos sucios:
                    finalizarActualizacion(ContractInsertAgotados.CONTENT_URI, ContractInsertAgotados.Columnas._ID, idRemota, idLocal);
                    enviarBroadcast(true, Mensajes.ON_SYNC_TRUE);
                    Toast.makeText(getContext(),"Datos almacenados en el servidor",Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "SyncAdapterSubida: Registros Insertados en el Servidor y en la Base Local "+idRemota);
                    break;

                case Constantes.FAILED:
                    Log.i(TAG, mensaje);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void realizarSincronizacionRemotaInsertVenta() {
        Log.i(TAG, "SyncAdapterSubida: Preparando datos a ser insertados en el servidor...");

        iniciarActualizacion(ContractInsertVenta.CONTENT_URI);

        Cursor c = obtenerRegistrosSucios(ContractInsertVenta.CONTENT_URI, Projection.PROJECTION_INSERTVENTA);

        Log.i(TAG, "SyncAdapterSubida: Se encontraron " + c.getCount() + " registros sucios.");


        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                final int idLocal = c.getInt(Projection.COLUMNA_ID);

                Log.i(TAG, "SyncAdapterSubida: Insertando datos de Agotados en el servidor...");

                VolleySingleton.getInstance(getContext()).addToRequestQueue(
                        new JsonObjectRequest(
                                Request.Method.POST,
                                Constantes.INSERTAR_VENTA,
                                UtilidadesVenta.deCursorAJSONObject(c),
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        procesarRespuestaInsertVenta(response, idLocal);
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
//                                            Toast.makeText(getContext(), "Datos no enviados, almacenados en el teléfono.", Toast.LENGTH_SHORT).show();
                                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Network Timeout");
                                        } else if (error instanceof AuthFailureError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error AuthFailure");
                                        } else if (error instanceof ServerError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Server");
                                        } else if (error instanceof NetworkError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Network");
                                        } else if (error instanceof ParseError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Parse");
                                        }
                                    }
                                }
                        ) {
                            @Override
                            public Map<String, String> getHeaders() {
                                Map<String, String> headers = new HashMap<String, String>();
                                headers.put("Content-Type", "application/json; charset=utf-8");
                                headers.put("Accept", "application/json");
                                return headers;
                            }

                            @Override
                            public String getBodyContentType() {
                                return "application/json; charset=utf-8";// + getParamsEncoding();
                            }
                        }
                );
            }
        } else {
            //Toast.makeText(getContext(),"No hay datos en cola de sincronizacion",Toast.LENGTH_SHORT).show();
//            StockActivity stockActivity = new StockActivity();
//            stockActivity.showToastFromBackground("No se requiere sincronización");
            Log.i(TAG, "SyncAdapterSubida: No se requiere sincronización: No hay datos en cola de sincronizacion");
            enviarBroadcast(true, Mensajes.SYNC_NODATOSCOLA);
        }
        c.close();
    }

    public void procesarRespuestaInsertVenta(JSONObject response, int idLocal) {

        try {
            // Obtener estado
            String estado = response.getString(Constantes.ESTADO);
            // Obtener mensaje
            String mensaje = response.getString(Constantes.MENSAJE);

            switch (estado) {
                case Constantes.SUCCESS:
                    // Obtener identificador del nuevo registro creado en el servidor
                    String idRemota = response.getString(Constantes.LAST_ID);
                    Log.i(TAG, mensaje);
                    //Borrar datos sucios:
                    finalizarActualizacion(ContractInsertVenta.CONTENT_URI, ContractInsertVenta.Columnas._ID, idRemota, idLocal);
                    enviarBroadcast(true, Mensajes.ON_SYNC_TRUE);
                    Toast.makeText(getContext(),"Datos almacenados en el servidor",Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "SyncAdapterSubida: Registros Insertados en el Servidor y en la Base Local "+idRemota);
                    break;

                case Constantes.FAILED:
                    Log.i(TAG, mensaje);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void realizarSincronizacionRemotaInsertExh() {
        Log.i(TAG, "SyncAdapterSubida: Preparando datos a ser insertados en el servidor...");

        iniciarActualizacion(ContractInsertExh.CONTENT_URI);

        Cursor c = obtenerRegistrosSucios(ContractInsertExh.CONTENT_URI, Projection.PROJECTION_INSERTEXH);

        Log.i(TAG, "SyncAdapterSubida: Se encontraron " + c.getCount() + " registros sucios.");

        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                final int idLocal = c.getInt(Projection.COLUMNA_ID);

                Log.i(TAG, "SyncAdapterSubida: Insertando datos de Exh en el servidor...");

                VolleySingleton.getInstance(getContext()).addToRequestQueue(
                        new JsonObjectRequest(
                                Request.Method.POST,
                                Constantes.INSERTAR_EXH,
                                UtilidadesExh.deCursorAJSONObject(c),
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        procesarRespuestaInsertExh(response, idLocal);
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
//                                            Toast.makeText(getContext(), "Datos no enviados, almacenados en el teléfono.", Toast.LENGTH_SHORT).show();
                                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Network Timeout");
                                        } else if (error instanceof AuthFailureError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error AuthFailure");
                                        } else if (error instanceof ServerError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Server");
                                        } else if (error instanceof NetworkError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Network");
                                        } else if (error instanceof ParseError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Parse");
                                        }
                                    }
                                }
                        ) {
                            @Override
                            public Map<String, String> getHeaders() {
                                Map<String, String> headers = new HashMap<String, String>();
                                headers.put("Content-Type", "application/json; charset=utf-8");
                                headers.put("Accept", "application/json");
                                return headers;
                            }

                            @Override
                            public String getBodyContentType() {
                                return "application/json; charset=utf-8";// + getParamsEncoding();
                            }
                        }
                );
            }
        } else {
            //Toast.makeText(getContext(),"No hay datos en cola de sincronizacion",Toast.LENGTH_SHORT).show();
//            StockActivity stockActivity = new StockActivity();
//            stockActivity.showToastFromBackground("No se requiere sincronización");
            Log.i(TAG, "SyncAdapterSubida: No se requiere sincronización: No hay datos en cola de sincronizacion");
            enviarBroadcast(true, Mensajes.SYNC_NODATOSCOLA);
        }
        c.close();
    }

    /**
     * INSERT LOGISTICO RELEVO
     */

    private void realizarSincronizacionRemotaInsertLogisticoRelevo() {
        Log.i(TAG, "SyncAdapterSubida: Preparando datos a ser insertados en el servidor...");

        iniciarActualizacion(ContractInsertLogisticoRelevo.CONTENT_URI);

        Cursor c = obtenerRegistrosSucios(ContractInsertLogisticoRelevo.CONTENT_URI, Projection.PROJECTION_INSERT_LOGISTICO_RELEVO);

        Log.i(TAG, "SyncAdapterSubida: Se encontraron " + c.getCount() + " registros sucios.");


        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                final int idLocal = c.getInt(Projection.COLUMNA_ID);

                Log.i(TAG, "SyncAdapterSubida: Insertando datos de Logístico en el servidor...");

                VolleySingleton.getInstance(getContext()).addToRequestQueue(
                        new JsonObjectRequest(
                                Request.Method.POST,
                                Constantes.INSERTAR_LOGISTICO_RELEVO,
                                UtilidadesLogisticoRelevo.deCursorAJSONObject(c),
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        procesarRespuestaInsertLogisticoRelevo(response, idLocal);
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                            Toast.makeText(getContext(), "Datos no enviados, almacenados en el teléfono.", Toast.LENGTH_SHORT).show();
                                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Network Timeout");
                                        } else if (error instanceof AuthFailureError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error AuthFailure");
                                        } else if (error instanceof ServerError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Server");
                                        } else if (error instanceof NetworkError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Network");
                                        } else if (error instanceof ParseError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Parse");
                                        }
                                    }
                                }
                        ) {
                            @Override
                            public Map<String, String> getHeaders() {
                                Map<String, String> headers = new HashMap<String, String>();
                                headers.put("Content-Type", "application/json; charset=utf-8");
                                headers.put("Accept", "application/json");
                                return headers;
                            }

                            @Override
                            public String getBodyContentType() {
                                return "application/json; charset=utf-8";//+ getParamsEncoding();
                            }
                        }
                );
            }
        } else {
            //Toast.makeText(getContext(),"No hay datos en cola de sincronizacion",Toast.LENGTH_SHORT).show();
//            StockActivity stockActivity = new StockActivity();
//            stockActivity.showToastFromBackground("No se requiere sincronización");
            Log.i(TAG, "SyncAdapterSubida: No se requiere sincronización: No hay datos en cola de sincronizacion");
            enviarBroadcast(true, Mensajes.SYNC_NODATOSCOLA);
        }
        c.close();
    }

    public void procesarRespuestaInsertLogisticoRelevo(JSONObject response, int idLocal) {
        try {
            // Obtener estado
            String estado = response.getString(Constantes.ESTADO);
            // Obtener mensaje
            String mensaje = response.getString(Constantes.MENSAJE);

            switch (estado) {
                case Constantes.SUCCESS:
                    // Obtener identificador del nuevo registro creado en el servidor
                    String idRemota = response.getString(Constantes.LAST_ID);
                    Log.i(TAG, mensaje);
                    //Borrar datos sucios:
                    finalizarActualizacion(ContractInsertLogisticoRelevo.CONTENT_URI, ContractInsertLogisticoRelevo.Columnas._ID, idRemota, idLocal);
                    enviarBroadcast(true, Mensajes.ON_SYNC_TRUE);
                    Toast.makeText(getContext(), "Datos almacenados en el servidor", Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "SyncAdapterSubida: Registros Insertados en el Servidor y en la Base Local " + idRemota);
                    break;

                case Constantes.FAILED:
                    Log.i(TAG, mensaje);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     *  FIN
     */

    /**
     * Procesa los diferentes tipos de respuesta obtenidos del servidor
     *
     * @param response Respuesta en subchannel Json
     */
    public void procesarRespuestaInsertExh(JSONObject response, int idLocal) {

        try {
            // Obtener estado
            String estado = response.getString(Constantes.ESTADO);
            // Obtener mensaje
            String mensaje = response.getString(Constantes.MENSAJE);

            switch (estado) {
                case Constantes.SUCCESS:
                    // Obtener identificador del nuevo registro creado en el servidor
                    String idRemota = response.getString(Constantes.LAST_ID);
                    Log.i(TAG, mensaje);
                    //Borrar datos sucios:
                    finalizarActualizacion(ContractInsertExh.CONTENT_URI, ContractInsertExh.Columnas._ID, idRemota, idLocal);
                    enviarBroadcast(true, Mensajes.ON_SYNC_TRUE);
                    Toast.makeText(getContext(),"Datos almacenados en el servidor",Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "SyncAdapterSubida: Registros Insertados en el Servidor y en la Base Local "+idRemota);
                    break;

                case Constantes.FAILED:
                    Log.i(TAG, mensaje);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void realizarSincronizacionRemotaInsertFotografico() {
        Log.i(TAG, "SyncAdapterSubida: Preparando datos a ser insertados en el servidor...");

        iniciarActualizacion(ContractInsertFotografico.CONTENT_URI);

        Cursor c = obtenerRegistrosSucios(ContractInsertFotografico.CONTENT_URI, Projection.PROJECTION_INSERTFOT);

        Log.i(TAG, "SyncAdapterSubida: Se encontraron " + c.getCount() + " registros sucios.");


        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                final int idLocal = c.getInt(Projection.COLUMNA_ID);

                Log.i(TAG, "SyncAdapterSubida: Insertando datos de Fotografico en el servidor...");

                VolleySingleton.getInstance(getContext()).addToRequestQueue(
                        new JsonObjectRequest(
                                Request.Method.POST,
                                Constantes.INSERTAR_FOTOGRAFICO,
                                UtilidadesFotografico.deCursorAJSONObject(c),
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        procesarRespuestaInsertFotografico(response, idLocal);
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Network Timeout");
                                        } else if (error instanceof AuthFailureError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error AuthFailure");
                                        } else if (error instanceof ServerError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Server");
                                        } else if (error instanceof NetworkError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Network");
                                        } else if (error instanceof ParseError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Parse");
                                        }
                                    }
                                }
                        ) {
                            @Override
                            public Map<String, String> getHeaders() {
                                Map<String, String> headers = new HashMap<String, String>();
                                headers.put("Content-Type", "application/json; charset=utf-8");
                                headers.put("Accept", "application/json");
                                return headers;
                            }

                            @Override
                            public String getBodyContentType() {
                                return "application/json; charset=utf-8";// + getParamsEncoding();
                            }
                        }
                );
            }
        } else {
            //Toast.makeText(getContext(),"No hay datos en cola de sincronizacion",Toast.LENGTH_SHORT).show();
//            StockActivity stockActivity = new StockActivity();
//            stockActivity.showToastFromBackground("No se requiere sincronización");
            Log.i(TAG, "SyncAdapterSubida: No se requiere sincronización: No hay datos en cola de sincronizacion");
            enviarBroadcast(true, Mensajes.SYNC_NODATOSCOLA);
        }
        c.close();
    }



    /**
     * Procesa los diferentes tipos de respuesta obtenidos del servidor
     *
     * @param response Respuesta en subchannel Json
     */
    public void procesarRespuestaInsertFotografico(JSONObject response, int idLocal) {

        try {
            // Obtener estado
            String estado = response.getString(Constantes.ESTADO);
            // Obtener mensaje
            String mensaje = response.getString(Constantes.MENSAJE);

            switch (estado) {
                case Constantes.SUCCESS:
                    // Obtener identificador del nuevo registro creado en el servidor
                    String idRemota = response.getString(Constantes.LAST_ID);
                    Log.i(TAG, mensaje);
                    //Borrar datos sucios:
                    finalizarActualizacion(ContractInsertFotografico.CONTENT_URI, ContractInsertFotografico.Columnas._ID, idRemota, idLocal);
                    enviarBroadcast(true, Mensajes.ON_SYNC_TRUE);
                    Toast.makeText(getContext(),"Datos almacenados en el servidor",Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "SyncAdapterSubida: Registros Insertados en el Servidor y en la Base Local "+idRemota);
                    break;

                case Constantes.FAILED:
                    Log.i(TAG, mensaje);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /*SHARE*/
    private void realizarSincronizacionRemotaInsertShare() {
        Log.i(TAG, "SyncAdapterSubida: Preparando datos a ser insertados en el servidor...");

        iniciarActualizacion(ContractInsertShare.CONTENT_URI);

        Cursor c = obtenerRegistrosSucios(ContractInsertShare.CONTENT_URI, Projection.PROJECTION_INSERTSHARE);

        Log.i(TAG, "SyncAdapterSubida: Se encontraron " + c.getCount() + " registros sucios.");


        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                final int idLocal = c.getInt(Projection.COLUMNA_ID);

                Log.i(TAG, "SyncAdapterSubida: Insertando datos de Share en el servidor...");

                VolleySingleton.getInstance(getContext()).addToRequestQueue(
                        new JsonObjectRequest(
                                Request.Method.POST,
                                Constantes.INSERTAR_SHARE,
                                UtilidadesShare.deCursorAJSONObject(c),
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        procesarRespuestaInsertShare(response, idLocal);
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
//                                            Toast.makeText(getContext(), "Datos no enviados, almacenados en el teléfono.", Toast.LENGTH_SHORT).show();
                                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Network Timeout");
                                        } else if (error instanceof AuthFailureError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error AuthFailure");
                                        } else if (error instanceof ServerError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Server");
                                        } else if (error instanceof NetworkError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Network");
                                        } else if (error instanceof ParseError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Parse");
                                        }
                                    }
                                }
                        ) {
                            @Override
                            public Map<String, String> getHeaders() {
                                Map<String, String> headers = new HashMap<String, String>();
                                headers.put("Content-Type", "application/json; charset=utf-8");
                                headers.put("Accept", "application/json");
                                return headers;
                            }

                            @Override
                            public String getBodyContentType() {
                                return "application/json; charset=utf-8";// + getParamsEncoding();
                            }
                        }
                );
            }
        } else {
            //Toast.makeText(getContext(),"No hay datos en cola de sincronizacion",Toast.LENGTH_SHORT).show();
//            StockActivity stockActivity = new StockActivity();
//            stockActivity.showToastFromBackground("No se requiere sincronización");
            Log.i(TAG, "SyncAdapterSubida: No se requiere sincronización: No hay datos en cola de sincronizacion");
            enviarBroadcast(true, Mensajes.SYNC_NODATOSCOLA);
        }
        c.close();
    }

    public void procesarRespuestaInsertShare(JSONObject response, int idLocal) {

        try {
            // Obtener estado
            String estado = response.getString(Constantes.ESTADO);
            // Obtener mensaje
            String mensaje = response.getString(Constantes.MENSAJE);

            switch (estado) {
                case Constantes.SUCCESS:
                    // Obtener identificador del nuevo registro creado en el servidor
                    String idRemota = response.getString(Constantes.LAST_ID);
                    Log.i(TAG, mensaje);
                    //Borrar datos sucios:
                    finalizarActualizacion(ContractInsertShare.CONTENT_URI, ContractInsertShare.Columnas._ID, idRemota, idLocal);
                    enviarBroadcast(true, Mensajes.ON_SYNC_TRUE);
                    Toast.makeText(getContext(),"Datos almacenados en el servidor",Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "SyncAdapterSubida: Registros Insertados en el Servidor y en la Base Local "+idRemota);
                    break;

                case Constantes.FAILED:
                    Log.i(TAG, mensaje);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    /* TIEMPO ALMUERZO*/

    private void realizarSincronizacionRemotaInsertAlmuerzo() {
        Log.i(TAG, "SyncAdapterSubida: Preparando datos a ser insertados en el servidor...");

        iniciarActualizacion(ContractInsertAlmuerzo.CONTENT_URI);

        Cursor c = obtenerRegistrosSucios(ContractInsertAlmuerzo.CONTENT_URI, Projection.PROJECTION_INSERTALMUERZO);

        Log.i(TAG, "SyncAdapterSubida: Se encontraron " + c.getCount() + " registros sucios.");


        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                final int idLocal = c.getInt(Projection.COLUMNA_ID);

                Log.i(TAG, "SyncAdapterSubida: Insertando datos de Precios en el servidor...");

                VolleySingleton.getInstance(getContext()).addToRequestQueue(
                        new JsonObjectRequest(
                                Request.Method.POST,
                                Constantes.INSERTAR_ALMUERZO,
                                UtilidadesAlmuerzo.deCursorAJSONObject(c),
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        procesarRespuestaInsertAlmuerzo(response, idLocal);
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
//                                            Toast.makeText(getContext(), "Datos no enviados, almacenados en el teléfono.", Toast.LENGTH_SHORT).show();
                                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Network Timeout");
                                        } else if (error instanceof AuthFailureError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error AuthFailure");
                                        } else if (error instanceof ServerError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Server");
                                        } else if (error instanceof NetworkError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Network");
                                        } else if (error instanceof ParseError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Parse");
                                        }
                                    }
                                }
                        ) {
                            @Override
                            public Map<String, String> getHeaders() {
                                Map<String, String> headers = new HashMap<String, String>();
                                headers.put("Content-Type", "application/json; charset=utf-8");
                                headers.put("Accept", "application/json");
                                return headers;
                            }

                            @Override
                            public String getBodyContentType() {
                                return "application/json; charset=utf-8" ;//+ getParamsEncoding();
                            }
                        }
                );
            }
        } else {
            //Toast.makeText(getContext(),"No hay datos en cola de sincronizacion",Toast.LENGTH_SHORT).show();
//            StockActivity stockActivity = new StockActivity();
//            stockActivity.showToastFromBackground("No se requiere sincronización");
            Log.i(TAG, "SyncAdapterSubida: No se requiere sincronización: No hay datos en cola de sincronizacion");
            enviarBroadcast(true, Mensajes.SYNC_NODATOSCOLA);
        }
        c.close();
    }

    public void procesarRespuestaInsertAlmuerzo(JSONObject response, int idLocal) {

        try {
            // Obtener estado
            String estado = response.getString(Constantes.ESTADO);
            // Obtener mensaje
            String mensaje = response.getString(Constantes.MENSAJE);

            switch (estado) {
                case Constantes.SUCCESS:
                    // Obtener identificador del nuevo registro creado en el servidor
                    String idRemota = response.getString(Constantes.LAST_ID);
                    Log.i(TAG, mensaje);
                    //Borrar datos sucios:
                    finalizarActualizacion(ContractInsertAlmuerzo.CONTENT_URI, ContractInsertAlmuerzo.Columnas._ID, idRemota, idLocal);
                    enviarBroadcast(true, Mensajes.ON_SYNC_TRUE);
                    Toast.makeText(getContext(),"Datos almacenados en el servidor",Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "SyncAdapterSubida: Registros Insertados en el Servidor y en la Base Local "+idRemota);
                    break;

                case Constantes.FAILED:
                    Log.i(TAG, mensaje);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /* PROYECTOS Y OBRAS - CONTACTO */

    private void realizarSincronizacionRemotaInsertProyectosContacto() {
        Log.i(TAG, "SyncAdapterSubida: Preparando datos de Contacto a ser insertados en el servidor...");

        iniciarActualizacion(ContractInsertProyectosContacto.CONTENT_URI);

        Cursor c = obtenerRegistrosSucios(ContractInsertProyectosContacto.CONTENT_URI, Projection.PROJECTION_INSERTPROYECTOSCONTACTO);

        Log.i(TAG, "SyncAdapterSubida: Se encontraron " + c.getCount() + " registros sucios de Contacto.");

        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                final int idLocal = c.getInt(Projection.COLUMNA_ID);

                Log.i(TAG, "SyncAdapterSubida: Insertando datos de Contacto en el servidor...");

                VolleySingleton.getInstance(getContext()).addToRequestQueue(
                        new JsonObjectRequest(
                                Request.Method.POST,
                                Constantes.INSERTAR_CONTACTO,
                                UtilidadesProyectosContacto.deCursorAJSONObject(c),
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        procesarRespuestaInsertProyectosContacto(response, idLocal);
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Network Timeout");
                                        } else if (error instanceof AuthFailureError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error AuthFailure");
                                        } else if (error instanceof ServerError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Server");
                                        } else if (error instanceof NetworkError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Network");
                                        } else if (error instanceof ParseError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Parse");
                                        }
                                    }
                                }
                        ) {
                            @Override
                            public Map<String, String> getHeaders() {
                                Map<String, String> headers = new HashMap<String, String>();
                                headers.put("Content-Type", "application/json; charset=utf-8");
                                headers.put("Accept", "application/json");
                                return headers;
                            }

                            @Override
                            public String getBodyContentType() {
                                return "application/json; charset=utf-8";
                            }
                        }
                );
            }
        } else {
            Log.i(TAG, "SyncAdapterSubida: No se requiere sincronización: No hay datos en cola de sincronizacion");
            enviarBroadcast(true, Mensajes.SYNC_NODATOSCOLA);
        }
        c.close();
    }

    public void procesarRespuestaInsertProyectosContacto(JSONObject response, int idLocal) {

        try {
            // Obtener estado
            String estado = response.getString(Constantes.ESTADO);
            // Obtener mensaje
            String mensaje = response.getString(Constantes.MENSAJE);

            switch (estado) {
                case Constantes.SUCCESS:
                    // Obtener identificador del nuevo registro creado en el servidor
                    String idRemota = response.getString(Constantes.LAST_ID);
                    Log.i(TAG, mensaje);
                    //Borrar datos sucios:
                    finalizarActualizacion(ContractInsertProyectosContacto.CONTENT_URI, ContractInsertProyectosContacto.Columnas._ID, idRemota, idLocal);
                    enviarBroadcast(true, Mensajes.ON_SYNC_TRUE);
                    Toast.makeText(getContext(),"Datos almacenados en el servidor",Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "SyncAdapterSubida: Registros Insertados en el Servidor y en la Base Local "+idRemota);
                    break;

                case Constantes.FAILED:
                    Log.i(TAG, mensaje);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void realizarSincronizacionLocalContactos(final SyncResult syncResult, String operatorName) {
        Log.i(TAG, "Realizando Sincronizacion Local de Contactos. " + operatorName);
        HashMap<String, String> map = new HashMap<>();
        map.put("usuario", operatorName);

        JSONObject jobject = new JSONObject(map);

        Log.i(TAG, "ContactosDebug: URL=" + Constantes.GET_CONTACTOS + " body=" + jobject.toString());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, Constantes.GET_CONTACTOS, jobject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, "ContactosDebug: respuesta recibida = " + response.toString());
                        procesarRespuestaGetContactos(response, syncResult);
                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e(TAG, "ContactosDebug: error " + error.getClass().getSimpleName()
                                        + " - " + error.getMessage(), error);

                                if (error.networkResponse != null) {
                                    Log.e(TAG, "ContactosDebug: statusCode=" + error.networkResponse.statusCode
                                            + " body=" + new String(error.networkResponse.data));
                                }

                                if (error instanceof TimeoutError) {
                                    enviarBroadcast(true, Mensajes.TIME_OUT);
                                } else if (error instanceof NoConnectionError) {
                                    enviarBroadcast(true, Mensajes.NO_RED);
                                } else if (error instanceof AuthFailureError) {
                                    Log.e(TAG, "ContactosDebug: AuthFailureError");
                                } else if (error instanceof ServerError) {
                                    enviarBroadcast(true, Mensajes.SEVER_ERROR);
                                } else if (error instanceof NetworkError) {
                                    enviarBroadcast(true, Mensajes.RED_ERROR);
                                } else if (error instanceof ParseError) {
                                    Log.e(TAG, "ContactosDebug: ParseError");
                                }
                            }
                        });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);
    }

    private void procesarRespuestaGetContactos(JSONObject response, SyncResult syncResult) {
        try {
            String estado = response.getString(Constantes.ESTADO);

            switch (estado) {
                case Constantes.SUCCESS:
                    actualizarDatosLocalesContactos(response, syncResult);
                    break;
                case Constantes.FAILED:
                    String mensaje = response.getString(Constantes.MENSAJE);
                    Log.i(TAG, mensaje);
                    // No reenviamos el mensaje crudo del PHP: ese texto genérico ("No se encontraron
                    // registros en el servidor.") lo comparten TODAS las etapas de la cadena de sync,
                    // y retransmitirlo dispara en cascada los "if" de las demás etapas en las Activities.
                    enviarBroadcast(true, Mensajes.SYNC_NOREQUERIDA + "Contactos");
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void actualizarDatosLocalesContactos(JSONObject response, SyncResult syncResult) {
        JSONArray contactosJson = null;

        try {
            contactosJson = response.getJSONArray(Constantes.CONTACTOS_RESULT);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Tabla hash con los datos entrantes del servidor, usando el id remoto como llave
        HashMap<String, JSONObject> mapaServidor = new HashMap<>();
        if (contactosJson != null) {
            for (int i = 0; i < contactosJson.length(); i++) {
                try {
                    JSONObject item = contactosJson.getJSONObject(i);
                    mapaServidor.put(item.getString("id"), item);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        ArrayList<ContentProviderOperation> ops = new ArrayList<>();

        Uri uri = ContractInsertProyectosContacto.CONTENT_URI;
        String select = Constantes.ID_REMOTA + " IS NOT NULL";
        Cursor c = resolver.query(uri, Projection.PROJECTION_INSERTPROYECTOSCONTACTO, select, null, null);
        assert c != null;

        Log.i(TAG, "Se encontraron " + c.getCount() + " registros locales de Contactos.");

        while (c.moveToNext()) {
            syncResult.stats.numEntries++;
            String idRemota = c.getString(Projection.COLUMNA_ID_REMOTA);

            JSONObject match = mapaServidor.get(idRemota);

            if (match != null) {
                // Esta entrada existe, por lo que se remueve del mapeado
                mapaServidor.remove(idRemota);

                Uri existingUri = ContractInsertProyectosContacto.CONTENT_URI.buildUpon()
                        .appendPath(idRemota).build();

                String contactoLocal = c.getString(UtilidadesProyectosContacto.COLUMNA_CONTACTO);
                String empresaLocal = c.getString(UtilidadesProyectosContacto.COLUMNA_EMPRESA);
                String mailLocal = c.getString(UtilidadesProyectosContacto.COLUMNA_MAIL);
                String direccionLocal = c.getString(UtilidadesProyectosContacto.COLUMNA_DIRECCION);
                String telefonoLocal = c.getString(UtilidadesProyectosContacto.COLUMNA_TELEFONO);
                String telefonoConvencionalLocal = c.getString(UtilidadesProyectosContacto.COLUMNA_TELEFONO_CONVENCIONAL);
                String fechaAgendamientoLocal = c.getString(UtilidadesProyectosContacto.COLUMNA_FECHA_AGENDAMIENTO);
                String tituloLocal = c.getString(UtilidadesProyectosContacto.COLUMNA_TITULO);
                String horaLocal = c.getString(UtilidadesProyectosContacto.COLUMNA_HORA);
                String lugarLocal = c.getString(UtilidadesProyectosContacto.COLUMNA_LUGAR);
                String latitudLocal = c.getString(UtilidadesProyectosContacto.COLUMNA_LATITUD);
                String longitudLocal = c.getString(UtilidadesProyectosContacto.COLUMNA_LONGITUD);
                String tecnicoLocal = c.getString(UtilidadesProyectosContacto.COLUMNA_TECNICO);
                String estadoAgendaLocal = c.getString(UtilidadesProyectosContacto.COLUMNA_ESTADO_AGENDA);
                String activarLocal = c.getString(UtilidadesProyectosContacto.COLUMNA_ACTIVAR);
                String reagendadoLocal = c.getString(UtilidadesProyectosContacto.COLUMNA_REAGENDADO);
                String ciudadPdvLocal = c.getString(UtilidadesProyectosContacto.COLUMNA_CIUDAD_PDV);
                String noRequiereVisitaLocal = c.getString(UtilidadesProyectosContacto.COLUMNA_NO_REQUIERE_VISITA);

                // fecha_registro no entra en esta comparación: es metadata que MySQL pone
                // una sola vez al crear la fila y nunca cambia, así que no hace falta
                // disparar un UPDATE completo solo por ella — el insert nuevo (más abajo,
                // para filas que no existían localmente) sí la trae desde el inicio.
                boolean cambio = !UtilidadesProyectosContacto.optStringSeguro(match, "contacto").equals(contactoLocal)
                        || !UtilidadesProyectosContacto.optStringSeguro(match, "empresa").equals(empresaLocal)
                        || !UtilidadesProyectosContacto.optStringSeguro(match, "mail").equals(mailLocal)
                        || !UtilidadesProyectosContacto.optStringSeguro(match, "direccion").equals(direccionLocal)
                        || !UtilidadesProyectosContacto.optStringSeguro(match, "telefono").equals(telefonoLocal)
                        || !UtilidadesProyectosContacto.optStringSeguro(match, "telefono_convencional").equals(telefonoConvencionalLocal == null ? "" : telefonoConvencionalLocal)
                        || !UtilidadesProyectosContacto.deFechaSql(UtilidadesProyectosContacto.optStringSeguro(match, "fecha_agendamiento")).equals(fechaAgendamientoLocal)
                        || !UtilidadesProyectosContacto.optStringSeguro(match, "titulo").equals(tituloLocal == null ? "" : tituloLocal)
                        // "hora" viene del servidor en HH:mm:ss (columna TIME) — se convierte
                        // a hh:mm a antes de comparar, porque así es como queda guardada
                        // localmente (la ponga el promotor desde la app o el analista desde
                        // la web); si no, cualquier hora ya puesta dispararía un "cambio" en
                        // cada sync solo por diferir el formato.
                        || !UtilidadesProyectosContacto.deHoraSql(UtilidadesProyectosContacto.optStringSeguro(match, "hora")).equals(horaLocal == null ? "" : horaLocal)
                        || !UtilidadesProyectosContacto.optStringSeguro(match, "lugar").equals(lugarLocal == null ? "" : lugarLocal)
                        || !UtilidadesProyectosContacto.optStringSeguro(match, "latitud").equals(latitudLocal == null ? "" : latitudLocal)
                        || !UtilidadesProyectosContacto.optStringSeguro(match, "longitud").equals(longitudLocal == null ? "" : longitudLocal)
                        || !UtilidadesProyectosContacto.optStringSeguro(match, "tecnico").equals(tecnicoLocal == null ? "" : tecnicoLocal)
                        || !UtilidadesProyectosContacto.optStringSeguro(match, "estado_agenda", Constantes.ESTADO_AGENDA_PENDIENTE).equals(estadoAgendaLocal)
                        || !UtilidadesProyectosContacto.optStringSeguro(match, "activar", "SI").equals(activarLocal == null ? "SI" : activarLocal)
                        || !UtilidadesProyectosContacto.optStringSeguro(match, "reagendado", "NO").equals(reagendadoLocal == null ? "NO" : reagendadoLocal)
                        || !UtilidadesProyectosContacto.optStringSeguro(match, "ciudad_pdv").equals(ciudadPdvLocal == null ? "" : ciudadPdvLocal)
                        || !UtilidadesProyectosContacto.optStringSeguro(match, "no_requiere_visita", "NO").equals(noRequiereVisitaLocal == null ? "NO" : noRequiereVisitaLocal);

                if (cambio) {
                    Log.i(TAG, "Programando actualización de: " + existingUri);
                    ops.add(ContentProviderOperation.newUpdate(existingUri)
                            .withValue(ContractInsertProyectosContacto.Columnas.CONTACTO, UtilidadesProyectosContacto.optStringSeguro(match, "contacto"))
                            .withValue(ContractInsertProyectosContacto.Columnas.EMPRESA, UtilidadesProyectosContacto.optStringSeguro(match, "empresa"))
                            .withValue(ContractInsertProyectosContacto.Columnas.MAIL, UtilidadesProyectosContacto.optStringSeguro(match, "mail"))
                            .withValue(ContractInsertProyectosContacto.Columnas.DIRECCION, UtilidadesProyectosContacto.optStringSeguro(match, "direccion"))
                            .withValue(ContractInsertProyectosContacto.Columnas.TELEFONO, UtilidadesProyectosContacto.optStringSeguro(match, "telefono"))
                            .withValue(ContractInsertProyectosContacto.Columnas.TELEFONO_CONVENCIONAL, UtilidadesProyectosContacto.optStringSeguro(match, "telefono_convencional"))
                            .withValue(ContractInsertProyectosContacto.Columnas.FECHA_AGENDAMIENTO, UtilidadesProyectosContacto.deFechaSql(UtilidadesProyectosContacto.optStringSeguro(match, "fecha_agendamiento")))
                            .withValue(ContractInsertProyectosContacto.Columnas.TITULO, UtilidadesProyectosContacto.optStringSeguro(match, "titulo"))
                            .withValue(ContractInsertProyectosContacto.Columnas.HORA, UtilidadesProyectosContacto.deHoraSql(UtilidadesProyectosContacto.optStringSeguro(match, "hora")))
                            .withValue(ContractInsertProyectosContacto.Columnas.LUGAR, UtilidadesProyectosContacto.optStringSeguro(match, "lugar"))
                            .withValue(ContractInsertProyectosContacto.Columnas.LATITUD, UtilidadesProyectosContacto.optStringSeguro(match, "latitud"))
                            .withValue(ContractInsertProyectosContacto.Columnas.LONGITUD, UtilidadesProyectosContacto.optStringSeguro(match, "longitud"))
                            .withValue(ContractInsertProyectosContacto.Columnas.TECNICO, UtilidadesProyectosContacto.optStringSeguro(match, "tecnico"))
                            .withValue(ContractInsertProyectosContacto.Columnas.ESTADO_AGENDA,
                                    UtilidadesProyectosContacto.optStringSeguro(match, "estado_agenda", Constantes.ESTADO_AGENDA_PENDIENTE))
                            .withValue(ContractInsertProyectosContacto.Columnas.ACTIVAR, UtilidadesProyectosContacto.optStringSeguro(match, "activar", "SI"))
                            .withValue(ContractInsertProyectosContacto.Columnas.REAGENDADO, UtilidadesProyectosContacto.optStringSeguro(match, "reagendado", "NO"))
                            .withValue(ContractInsertProyectosContacto.Columnas.FECHA_REGISTRO, UtilidadesProyectosContacto.optStringSeguro(match, "fecha_registro"))
                            .withValue(ContractInsertProyectosContacto.Columnas.CIUDAD_PDV, UtilidadesProyectosContacto.optStringSeguro(match, "ciudad_pdv"))
                            .withValue(ContractInsertProyectosContacto.Columnas.NO_REQUIERE_VISITA, UtilidadesProyectosContacto.optStringSeguro(match, "no_requiere_visita", "NO"))
                            .build());
                    syncResult.stats.numUpdates++;
                } else {
                    Log.i(TAG, "No hay acciones para este registro: " + existingUri);
                }
            } else {
                // A esta función solo se llega con una respuesta SUCCESS real del servidor
                // (timeouts/errores de red se manejan aparte y nunca disparan esto), y la
                // consulta de getContactos trae siempre el listado completo del usuario sin
                // paginar — así que si el registro no vino, es porque ya no existe en el
                // servidor. Se borra localmente.
                Uri deleteUri = ContractInsertProyectosContacto.CONTENT_URI.buildUpon()
                        .appendPath(idRemota).build();
                Log.i(TAG, "Programando eliminación de: " + deleteUri);
                ops.add(ContentProviderOperation.newDelete(deleteUri).build());
                syncResult.stats.numDeletes++;
            }
        }
        c.close();

        // Insertar los registros nuevos que quedaron en el mapa
        for (JSONObject item : mapaServidor.values()) {
            try {
                String idRemota = item.getString("id");
                Log.i(TAG, "Programando inserción de Contacto en Base Local: " + idRemota);
                ops.add(ContentProviderOperation.newInsert(ContractInsertProyectosContacto.CONTENT_URI)
                        .withValue(Constantes.ID_REMOTA, idRemota)
                        .withValue(ContractInsertProyectosContacto.Columnas.CODIGO_PDV, UtilidadesProyectosContacto.optStringSeguro(item, "codigo_pdv"))
                        .withValue(ContractInsertProyectosContacto.Columnas.PDV, UtilidadesProyectosContacto.optStringSeguro(item, "pdv"))
                        .withValue(ContractInsertProyectosContacto.Columnas.USUARIO, UtilidadesProyectosContacto.optStringSeguro(item, "usuario"))
                        .withValue(ContractInsertProyectosContacto.Columnas.FECHA, UtilidadesProyectosContacto.deFechaSql(UtilidadesProyectosContacto.optStringSeguro(item, "fecha")))
                        .withValue(ContractInsertProyectosContacto.Columnas.CONTACTO, UtilidadesProyectosContacto.optStringSeguro(item, "contacto"))
                        .withValue(ContractInsertProyectosContacto.Columnas.EMPRESA, UtilidadesProyectosContacto.optStringSeguro(item, "empresa"))
                        .withValue(ContractInsertProyectosContacto.Columnas.MAIL, UtilidadesProyectosContacto.optStringSeguro(item, "mail"))
                        .withValue(ContractInsertProyectosContacto.Columnas.DIRECCION, UtilidadesProyectosContacto.optStringSeguro(item, "direccion"))
                        .withValue(ContractInsertProyectosContacto.Columnas.TELEFONO, UtilidadesProyectosContacto.optStringSeguro(item, "telefono"))
                        .withValue(ContractInsertProyectosContacto.Columnas.TELEFONO_CONVENCIONAL, UtilidadesProyectosContacto.optStringSeguro(item, "telefono_convencional"))
                        .withValue(ContractInsertProyectosContacto.Columnas.FECHA_AGENDAMIENTO, UtilidadesProyectosContacto.deFechaSql(UtilidadesProyectosContacto.optStringSeguro(item, "fecha_agendamiento")))
                        .withValue(ContractInsertProyectosContacto.Columnas.TITULO, UtilidadesProyectosContacto.optStringSeguro(item, "titulo"))
                        .withValue(ContractInsertProyectosContacto.Columnas.HORA, UtilidadesProyectosContacto.deHoraSql(UtilidadesProyectosContacto.optStringSeguro(item, "hora")))
                        .withValue(ContractInsertProyectosContacto.Columnas.LUGAR, UtilidadesProyectosContacto.optStringSeguro(item, "lugar"))
                        .withValue(ContractInsertProyectosContacto.Columnas.LATITUD, UtilidadesProyectosContacto.optStringSeguro(item, "latitud"))
                        .withValue(ContractInsertProyectosContacto.Columnas.LONGITUD, UtilidadesProyectosContacto.optStringSeguro(item, "longitud"))
                        .withValue(ContractInsertProyectosContacto.Columnas.TECNICO, UtilidadesProyectosContacto.optStringSeguro(item, "tecnico"))
                        .withValue(ContractInsertProyectosContacto.Columnas.ESTADO_AGENDA,
                                UtilidadesProyectosContacto.optStringSeguro(item, "estado_agenda", Constantes.ESTADO_AGENDA_PENDIENTE))
                        .withValue(ContractInsertProyectosContacto.Columnas.ACTIVAR, UtilidadesProyectosContacto.optStringSeguro(item, "activar", "SI"))
                        .withValue(ContractInsertProyectosContacto.Columnas.REAGENDADO, UtilidadesProyectosContacto.optStringSeguro(item, "reagendado", "NO"))
                        .withValue(ContractInsertProyectosContacto.Columnas.FECHA_REGISTRO, UtilidadesProyectosContacto.optStringSeguro(item, "fecha_registro"))
                        .withValue(ContractInsertProyectosContacto.Columnas.CIUDAD_PDV, UtilidadesProyectosContacto.optStringSeguro(item, "ciudad_pdv"))
                        .withValue(ContractInsertProyectosContacto.Columnas.NO_REQUIERE_VISITA, UtilidadesProyectosContacto.optStringSeguro(item, "no_requiere_visita", "NO"))
                        .withValue(Constantes.PENDIENTE_INSERCION, 0)
                        .build());
                syncResult.stats.numInserts++;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (syncResult.stats.numInserts > 0 ||
                syncResult.stats.numUpdates > 0 ||
                syncResult.stats.numDeletes > 0) {
            Log.i(TAG, "Aplicando operaciones de Contactos...");
            try {
                resolver.applyBatch(Constantes.AUTHORITY, ops);
            } catch (RemoteException | OperationApplicationException e) {
                e.printStackTrace();
            }
            resolver.notifyChange(ContractInsertProyectosContacto.CONTENT_URI, null, false);
            Log.i(TAG, "Sincronización de tabla Contactos finalizada.");
            enviarBroadcast(true, Mensajes.SYNC_FINALIZADA_CONTACTOS);
        } else {
            Log.i(TAG, "No se requiere sincronización de tabla Contactos");
            enviarBroadcast(true, Mensajes.SYNC_NOREQUERIDA + "Contactos");
        }
    }


    /* PROFORMA (Gestión de Visitas) */

    private void realizarSincronizacionRemotaInsertProforma() {
        Log.i(TAG, "SyncAdapterSubida: Preparando datos de Proforma a ser insertados en el servidor...");

        iniciarActualizacion(ContractInsertProforma.CONTENT_URI);

        Cursor c = obtenerRegistrosSucios(ContractInsertProforma.CONTENT_URI, Projection.PROJECTION_INSERTPROFORMA);

        Log.i(TAG, "SyncAdapterSubida: Se encontraron " + c.getCount() + " registros sucios de Proforma.");

        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                final int idLocal = c.getInt(Projection.COLUMNA_ID);

                Log.i(TAG, "SyncAdapterSubida: Insertando datos de Proforma en el servidor...");

                VolleySingleton.getInstance(getContext()).addToRequestQueue(
                        new JsonObjectRequest(
                                Request.Method.POST,
                                Constantes.INSERTAR_PROFORMA,
                                UtilidadesProforma.deCursorAJSONObject(c),
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        procesarRespuestaInsertProforma(response, idLocal);
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Network Timeout");
                                        } else if (error instanceof AuthFailureError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error AuthFailure");
                                        } else if (error instanceof ServerError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Server");
                                        } else if (error instanceof NetworkError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Network");
                                        } else if (error instanceof ParseError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Parse");
                                        }
                                    }
                                }
                        ) {
                            @Override
                            public Map<String, String> getHeaders() {
                                Map<String, String> headers = new HashMap<String, String>();
                                headers.put("Content-Type", "application/json; charset=utf-8");
                                headers.put("Accept", "application/json");
                                return headers;
                            }

                            @Override
                            public String getBodyContentType() {
                                return "application/json; charset=utf-8";
                            }
                        }
                );
            }
        } else {
            Log.i(TAG, "SyncAdapterSubida: No se requiere sincronización: No hay datos en cola de sincronizacion");
            enviarBroadcast(true, Mensajes.SYNC_NODATOSCOLA);
        }
        c.close();
    }

    public void procesarRespuestaInsertProforma(JSONObject response, int idLocal) {
        try {
            String estado = response.getString(Constantes.ESTADO);
            String mensaje = response.getString(Constantes.MENSAJE);

            switch (estado) {
                case Constantes.SUCCESS:
                    String idRemota = response.getString(Constantes.LAST_ID);
                    Log.i(TAG, mensaje);
                    finalizarActualizacion(ContractInsertProforma.CONTENT_URI, ContractInsertProforma.Columnas._ID, idRemota, idLocal);
                    enviarBroadcast(true, Mensajes.ON_SYNC_TRUE);
                    Toast.makeText(getContext(),"Datos almacenados en el servidor",Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "SyncAdapterSubida: Registros de Proforma Insertados en el Servidor y en la Base Local " + idRemota);
                    break;

                case Constantes.FAILED:
                    Log.i(TAG, mensaje);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Borra un envío de Proforma también en el servidor (además del borrado
     * local que ya se hizo en ProformaFragment.onEliminarRegistro) — necesario
     * porque la fila ya tiene id_remota, así que get_proforma.php la volvería a
     * bajar si no se borra también allá. Depende del endpoint eliminar_proforma.php
     * (todavía no existe del lado servidor — ver plan/gap de backend); mientras
     * tanto esta llamada simplemente fallará en silencio y la fila puede
     * reaparecer en el próximo sync de bajada.
     */
    public static void eliminarProformaRemota(Context context, String idRemota, String usuario) {
        HashMap<String, String> map = new HashMap<>();
        map.put("id", idRemota);
        map.put("usuario", usuario);
        JSONObject jobject = new JSONObject(map);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, Constantes.ELIMINAR_PROFORMA, jobject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, "Eliminar Proforma remota: " + response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i(TAG, "Eliminar Proforma remota: error (¿existe eliminar_proforma.php?) " + error);
                    }
                });

        VolleySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    public void realizarSincronizacionLocalProforma(final SyncResult syncResult, String operatorName) {
        Log.i(TAG, "Realizando Sincronizacion Local de Proforma. " + operatorName);
        HashMap<String, String> map = new HashMap<>();
        map.put("usuario", operatorName);

        JSONObject jobject = new JSONObject(map);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, Constantes.GET_PROFORMA, jobject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        procesarRespuestaGetProforma(response, syncResult);
                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                if (error instanceof TimeoutError) {
                                    enviarBroadcast(true, Mensajes.TIME_OUT);
                                } else if (error instanceof NoConnectionError) {
                                    enviarBroadcast(true, Mensajes.NO_RED);
                                } else if (error instanceof ServerError) {
                                    enviarBroadcast(true, Mensajes.SEVER_ERROR);
                                } else if (error instanceof NetworkError) {
                                    enviarBroadcast(true, Mensajes.RED_ERROR);
                                }
                            }
                        });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);
    }

    private void procesarRespuestaGetProforma(JSONObject response, SyncResult syncResult) {
        try {
            String estado = response.getString(Constantes.ESTADO);

            switch (estado) {
                case Constantes.SUCCESS:
                    actualizarDatosLocalesProforma(response, syncResult);
                    break;
                case Constantes.FAILED:
                    String mensaje = response.getString(Constantes.MENSAJE);
                    Log.i(TAG, mensaje);
                    enviarBroadcast(true, Mensajes.SYNC_NOREQUERIDA + "Proforma");
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void actualizarDatosLocalesProforma(JSONObject response, SyncResult syncResult) {
        JSONArray proformaJson = null;

        try {
            proformaJson = response.getJSONArray(Constantes.PROFORMA_RESULT);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HashMap<String, JSONObject> mapaServidor = new HashMap<>();
        if (proformaJson != null) {
            for (int i = 0; i < proformaJson.length(); i++) {
                try {
                    JSONObject item = proformaJson.getJSONObject(i);
                    mapaServidor.put(item.getString("id"), item);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        ArrayList<ContentProviderOperation> ops = new ArrayList<>();

        Uri uri = ContractInsertProforma.CONTENT_URI;
        String select = Constantes.ID_REMOTA + " IS NOT NULL";
        Cursor c = resolver.query(uri, Projection.PROJECTION_INSERTPROFORMA, select, null, null);
        assert c != null;

        Log.i(TAG, "Se encontraron " + c.getCount() + " registros locales de Proforma.");

        while (c.moveToNext()) {
            syncResult.stats.numEntries++;
            String idRemota = c.getString(Projection.COLUMNA_ID_REMOTA);

            JSONObject match = mapaServidor.get(idRemota);

            if (match != null) {
                mapaServidor.remove(idRemota);

                Uri existingUri = ContractInsertProforma.CONTENT_URI.buildUpon()
                        .appendPath(idRemota).build();

                String estadoProformaLocal = c.getString(UtilidadesProforma.COLUMNA_ESTADO_PROFORMA);
                String evidenciaLocal = c.getString(UtilidadesProforma.COLUMNA_EVIDENCIA);
                String caracteristicaVisitaLocal = c.getString(UtilidadesProforma.COLUMNA_CARACTERISTICA_VISITA);
                String acompanamientoTecnicoLocal = c.getString(UtilidadesProforma.COLUMNA_ACOMPANAMIENTO_TECNICO);
                String faseActualLocal = c.getString(UtilidadesProforma.COLUMNA_FASE_ACTUAL);
                String fotoFacturaLocal = c.getString(UtilidadesProforma.COLUMNA_FOTO_FACTURA);
                String fechaFacturaLocal = c.getString(UtilidadesProforma.COLUMNA_FECHA_FACTURA);
                String montoValidadoLocal = c.getString(UtilidadesProforma.COLUMNA_MONTO_VALIDADO);
                String observacionesAuditoriaLocal = c.getString(UtilidadesProforma.COLUMNA_OBSERVACIONES_AUDITORIA);
                String fechaAuditoriaLocal = c.getString(UtilidadesProforma.COLUMNA_FECHA_AUDITORIA);
                String montoTotalFacturaLocal = c.getString(UtilidadesProforma.COLUMNA_MONTO_TOTAL_FACTURA);
                String plazoMesesLocal = c.getString(UtilidadesProforma.COLUMNA_PLAZO_MESES);
                String estadoPagoLocal = c.getString(UtilidadesProforma.COLUMNA_ESTADO_PAGO);
                String motivoCierreLocal = c.getString(UtilidadesProforma.COLUMNA_MOTIVO_CIERRE);
                String motivoCierrePagoLocal = c.getString(UtilidadesProforma.COLUMNA_MOTIVO_CIERRE_PAGO);

                boolean cambio = !UtilidadesProforma.optStringSeguro(match, "estado_proforma", Constantes.ESTADO_PROFORMA_PENDIENTE).equals(estadoProformaLocal)
                        || !UtilidadesProforma.optStringSeguro(match, "evidencia").equals(evidenciaLocal == null ? "" : evidenciaLocal)
                        || !UtilidadesProforma.optStringSeguro(match, "caracteristica_visita").equals(caracteristicaVisitaLocal == null ? "" : caracteristicaVisitaLocal)
                        || !UtilidadesProforma.optStringSeguro(match, "acompanamiento_tecnico").equals(acompanamientoTecnicoLocal == null ? "" : acompanamientoTecnicoLocal)
                        || !UtilidadesProforma.optStringSeguro(match, "fase_actual", "3").equals(faseActualLocal == null ? "3" : faseActualLocal)
                        || !UtilidadesProforma.optStringSeguro(match, "foto_factura").equals(fotoFacturaLocal == null ? "" : fotoFacturaLocal)
                        || !UtilidadesProforma.optStringSeguro(match, "fecha_factura").equals(fechaFacturaLocal == null ? "" : fechaFacturaLocal)
                        || !UtilidadesProforma.optStringSeguro(match, "monto_validado").equals(montoValidadoLocal == null ? "" : montoValidadoLocal)
                        || !UtilidadesProforma.optStringSeguro(match, "observaciones_auditoria").equals(observacionesAuditoriaLocal == null ? "" : observacionesAuditoriaLocal)
                        || !UtilidadesProforma.optStringSeguro(match, "fecha_auditoria").equals(fechaAuditoriaLocal == null ? "" : fechaAuditoriaLocal)
                        || !UtilidadesProforma.optStringSeguro(match, "monto_total_factura").equals(montoTotalFacturaLocal == null ? "" : montoTotalFacturaLocal)
                        || !UtilidadesProforma.optStringSeguro(match, "plazo_meses").equals(plazoMesesLocal == null ? "" : plazoMesesLocal)
                        || !UtilidadesProforma.optStringSeguro(match, "estado_pago").equals(estadoPagoLocal == null ? "" : estadoPagoLocal)
                        || !UtilidadesProforma.optStringSeguro(match, "motivo_cierre").equals(motivoCierreLocal == null ? "" : motivoCierreLocal)
                        || !UtilidadesProforma.optStringSeguro(match, "motivo_cierre_pago").equals(motivoCierrePagoLocal == null ? "" : motivoCierrePagoLocal);

                if (cambio) {
                    Log.i(TAG, "Programando actualización de: " + existingUri);
                    ops.add(ContentProviderOperation.newUpdate(existingUri)
                            .withValue(ContractInsertProforma.Columnas.ESTADO_PROFORMA,
                                    UtilidadesProforma.optStringSeguro(match, "estado_proforma", Constantes.ESTADO_PROFORMA_PENDIENTE))
                            .withValue(ContractInsertProforma.Columnas.EVIDENCIA, UtilidadesProforma.optStringSeguro(match, "evidencia"))
                            .withValue(ContractInsertProforma.Columnas.CARACTERISTICA_VISITA, UtilidadesProforma.optStringSeguro(match, "caracteristica_visita"))
                            .withValue(ContractInsertProforma.Columnas.ACOMPANAMIENTO_TECNICO, UtilidadesProforma.optStringSeguro(match, "acompanamiento_tecnico"))
                            .withValue(ContractInsertProforma.Columnas.FASE_ACTUAL, UtilidadesProforma.optStringSeguro(match, "fase_actual", "3"))
                            .withValue(ContractInsertProforma.Columnas.FOTO_FACTURA, UtilidadesProforma.optStringSeguro(match, "foto_factura"))
                            .withValue(ContractInsertProforma.Columnas.FECHA_FACTURA, UtilidadesProforma.optStringSeguro(match, "fecha_factura"))
                            .withValue(ContractInsertProforma.Columnas.MONTO_VALIDADO, UtilidadesProforma.optStringSeguro(match, "monto_validado"))
                            .withValue(ContractInsertProforma.Columnas.OBSERVACIONES_AUDITORIA, UtilidadesProforma.optStringSeguro(match, "observaciones_auditoria"))
                            .withValue(ContractInsertProforma.Columnas.FECHA_AUDITORIA, UtilidadesProforma.optStringSeguro(match, "fecha_auditoria"))
                            .withValue(ContractInsertProforma.Columnas.MONTO_TOTAL_FACTURA, UtilidadesProforma.optStringSeguro(match, "monto_total_factura"))
                            .withValue(ContractInsertProforma.Columnas.PLAZO_MESES, UtilidadesProforma.optStringSeguro(match, "plazo_meses"))
                            .withValue(ContractInsertProforma.Columnas.ESTADO_PAGO, UtilidadesProforma.optStringSeguro(match, "estado_pago"))
                            .withValue(ContractInsertProforma.Columnas.MOTIVO_CIERRE, UtilidadesProforma.optStringSeguro(match, "motivo_cierre"))
                            .withValue(ContractInsertProforma.Columnas.MOTIVO_CIERRE_PAGO, UtilidadesProforma.optStringSeguro(match, "motivo_cierre_pago"))
                            .build());
                    syncResult.stats.numUpdates++;
                } else {
                    Log.i(TAG, "No hay acciones para este registro: " + existingUri);
                }
            } else {
                Uri deleteUri = ContractInsertProforma.CONTENT_URI.buildUpon()
                        .appendPath(idRemota).build();
                Log.i(TAG, "Programando eliminación de: " + deleteUri);
                ops.add(ContentProviderOperation.newDelete(deleteUri).build());
                syncResult.stats.numDeletes++;
            }
        }
        c.close();

        for (JSONObject item : mapaServidor.values()) {
            try {
                String idRemota = item.getString("id");
                Log.i(TAG, "Programando inserción de Proforma en Base Local: " + idRemota);
                ops.add(ContentProviderOperation.newInsert(ContractInsertProforma.CONTENT_URI)
                        .withValue(Constantes.ID_REMOTA, idRemota)
                        .withValue(ContractInsertProforma.Columnas.ID_AGENDAMIENTO, UtilidadesProforma.optStringSeguro(item, "id_agendamiento"))
                        .withValue(ContractInsertProforma.Columnas.CODIGO_PDV, UtilidadesProforma.optStringSeguro(item, "codigo_pdv"))
                        .withValue(ContractInsertProforma.Columnas.USUARIO, UtilidadesProforma.optStringSeguro(item, "usuario"))
                        .withValue(ContractInsertProforma.Columnas.FECHA_PROFORMA, UtilidadesProforma.optStringSeguro(item, "fecha_proforma"))
                        .withValue(ContractInsertProforma.Columnas.ESTADO_PROFORMA,
                                UtilidadesProforma.optStringSeguro(item, "estado_proforma", Constantes.ESTADO_PROFORMA_PENDIENTE))
                        .withValue(ContractInsertProforma.Columnas.EVIDENCIA, UtilidadesProforma.optStringSeguro(item, "evidencia"))
                        .withValue(ContractInsertProforma.Columnas.CARACTERISTICA_VISITA, UtilidadesProforma.optStringSeguro(item, "caracteristica_visita"))
                        .withValue(ContractInsertProforma.Columnas.ACOMPANAMIENTO_TECNICO, UtilidadesProforma.optStringSeguro(item, "acompanamiento_tecnico"))
                        .withValue(ContractInsertProforma.Columnas.FASE_ACTUAL, UtilidadesProforma.optStringSeguro(item, "fase_actual", "3"))
                        .withValue(ContractInsertProforma.Columnas.FOTO_FACTURA, UtilidadesProforma.optStringSeguro(item, "foto_factura"))
                        .withValue(ContractInsertProforma.Columnas.FECHA_FACTURA, UtilidadesProforma.optStringSeguro(item, "fecha_factura"))
                        .withValue(ContractInsertProforma.Columnas.MONTO_VALIDADO, UtilidadesProforma.optStringSeguro(item, "monto_validado"))
                        .withValue(ContractInsertProforma.Columnas.OBSERVACIONES_AUDITORIA, UtilidadesProforma.optStringSeguro(item, "observaciones_auditoria"))
                        .withValue(ContractInsertProforma.Columnas.FECHA_AUDITORIA, UtilidadesProforma.optStringSeguro(item, "fecha_auditoria"))
                        .withValue(ContractInsertProforma.Columnas.MONTO_TOTAL_FACTURA, UtilidadesProforma.optStringSeguro(item, "monto_total_factura"))
                        .withValue(ContractInsertProforma.Columnas.PLAZO_MESES, UtilidadesProforma.optStringSeguro(item, "plazo_meses"))
                        .withValue(ContractInsertProforma.Columnas.ESTADO_PAGO, UtilidadesProforma.optStringSeguro(item, "estado_pago"))
                        .withValue(ContractInsertProforma.Columnas.MOTIVO_CIERRE, UtilidadesProforma.optStringSeguro(item, "motivo_cierre"))
                        .withValue(ContractInsertProforma.Columnas.MOTIVO_CIERRE_PAGO, UtilidadesProforma.optStringSeguro(item, "motivo_cierre_pago"))
                        .withValue(Constantes.PENDIENTE_INSERCION, 0)
                        .build());
                syncResult.stats.numInserts++;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (syncResult.stats.numInserts > 0 ||
                syncResult.stats.numUpdates > 0 ||
                syncResult.stats.numDeletes > 0) {
            Log.i(TAG, "Aplicando operaciones de Proforma...");
            try {
                resolver.applyBatch(Constantes.AUTHORITY, ops);
            } catch (RemoteException | OperationApplicationException e) {
                e.printStackTrace();
            }
            resolver.notifyChange(ContractInsertProforma.CONTENT_URI, null, false);
            Log.i(TAG, "Sincronización de tabla Proforma finalizada.");
            enviarBroadcast(true, Mensajes.SYNC_FINALIZADA_PROFORMA);
        } else {
            Log.i(TAG, "No se requiere sincronización de tabla Proforma");
            enviarBroadcast(true, Mensajes.SYNC_NOREQUERIDA + "Proforma");
        }
    }


    /* PAGO FACTURA (cuotas de una factura a plazos) */

    private void realizarSincronizacionRemotaInsertPagoFactura() {
        Log.i(TAG, "SyncAdapterSubida: Preparando datos de Pago de Factura a ser insertados en el servidor...");

        iniciarActualizacion(ContractInsertPagoFactura.CONTENT_URI);

        Cursor c = obtenerRegistrosSucios(ContractInsertPagoFactura.CONTENT_URI, Projection.PROJECTION_INSERT_PAGO_FACTURA);

        Log.i(TAG, "SyncAdapterSubida: Se encontraron " + c.getCount() + " registros sucios de Pago de Factura.");

        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                final int idLocal = c.getInt(Projection.COLUMNA_ID);

                Log.i(TAG, "SyncAdapterSubida: Insertando datos de Pago de Factura en el servidor...");

                VolleySingleton.getInstance(getContext()).addToRequestQueue(
                        new JsonObjectRequest(
                                Request.Method.POST,
                                Constantes.INSERTAR_PAGO_FACTURA,
                                UtilidadesPagoFactura.deCursorAJSONObject(c),
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        procesarRespuestaInsertPagoFactura(response, idLocal);
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Network Timeout");
                                        } else if (error instanceof AuthFailureError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error AuthFailure");
                                        } else if (error instanceof ServerError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Server");
                                        } else if (error instanceof NetworkError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Network");
                                        } else if (error instanceof ParseError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Parse");
                                        }
                                    }
                                }
                        ) {
                            @Override
                            public Map<String, String> getHeaders() {
                                Map<String, String> headers = new HashMap<String, String>();
                                headers.put("Content-Type", "application/json; charset=utf-8");
                                headers.put("Accept", "application/json");
                                return headers;
                            }

                            @Override
                            public String getBodyContentType() {
                                return "application/json; charset=utf-8";
                            }
                        }
                );
            }
        } else {
            Log.i(TAG, "SyncAdapterSubida: No se requiere sincronización: No hay datos en cola de sincronizacion");
            enviarBroadcast(true, Mensajes.SYNC_NODATOSCOLA);
        }
        c.close();
    }

    public void procesarRespuestaInsertPagoFactura(JSONObject response, int idLocal) {
        try {
            String estado = response.getString(Constantes.ESTADO);
            String mensaje = response.getString(Constantes.MENSAJE);

            switch (estado) {
                case Constantes.SUCCESS:
                    String idRemota = response.getString(Constantes.LAST_ID);
                    Log.i(TAG, mensaje);
                    finalizarActualizacion(ContractInsertPagoFactura.CONTENT_URI, ContractInsertPagoFactura.Columnas._ID, idRemota, idLocal);
                    enviarBroadcast(true, Mensajes.ON_SYNC_TRUE);
                    Log.i(TAG, "SyncAdapterSubida: Registros de Pago de Factura Insertados en el Servidor y en la Base Local " + idRemota);
                    break;

                case Constantes.FAILED:
                    Log.i(TAG, mensaje);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void realizarSincronizacionLocalPagoFactura(final SyncResult syncResult, String operatorName) {
        Log.i(TAG, "Realizando Sincronizacion Local de Pago de Factura. " + operatorName);
        HashMap<String, String> map = new HashMap<>();
        map.put("usuario", operatorName);

        JSONObject jobject = new JSONObject(map);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, Constantes.GET_PAGO_FACTURA, jobject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        procesarRespuestaGetPagoFactura(response, syncResult);
                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                if (error instanceof TimeoutError) {
                                    enviarBroadcast(true, Mensajes.TIME_OUT);
                                } else if (error instanceof NoConnectionError) {
                                    enviarBroadcast(true, Mensajes.NO_RED);
                                } else if (error instanceof ServerError) {
                                    enviarBroadcast(true, Mensajes.SEVER_ERROR);
                                } else if (error instanceof NetworkError) {
                                    enviarBroadcast(true, Mensajes.RED_ERROR);
                                }
                            }
                        });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);
    }

    private void procesarRespuestaGetPagoFactura(JSONObject response, SyncResult syncResult) {
        try {
            String estado = response.getString(Constantes.ESTADO);

            switch (estado) {
                case Constantes.SUCCESS:
                    actualizarDatosLocalesPagoFactura(response, syncResult);
                    break;
                case Constantes.FAILED:
                    String mensaje = response.getString(Constantes.MENSAJE);
                    Log.i(TAG, mensaje);
                    enviarBroadcast(true, Mensajes.SYNC_NOREQUERIDA + "PagoFactura");
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void actualizarDatosLocalesPagoFactura(JSONObject response, SyncResult syncResult) {
        JSONArray pagosJson = null;

        try {
            pagosJson = response.getJSONArray(Constantes.PAGO_FACTURA_RESULT);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HashMap<String, JSONObject> mapaServidor = new HashMap<>();
        if (pagosJson != null) {
            for (int i = 0; i < pagosJson.length(); i++) {
                try {
                    JSONObject item = pagosJson.getJSONObject(i);
                    mapaServidor.put(item.getString("id"), item);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        ArrayList<ContentProviderOperation> ops = new ArrayList<>();

        Uri uri = ContractInsertPagoFactura.CONTENT_URI;
        String select = Constantes.ID_REMOTA + " IS NOT NULL";
        Cursor c = resolver.query(uri, Projection.PROJECTION_INSERT_PAGO_FACTURA, select, null, null);
        assert c != null;

        Log.i(TAG, "Se encontraron " + c.getCount() + " registros locales de Pago de Factura.");

        while (c.moveToNext()) {
            syncResult.stats.numEntries++;
            String idRemota = c.getString(Projection.COLUMNA_ID_REMOTA);

            JSONObject match = mapaServidor.get(idRemota);

            if (match != null) {
                // Los pagos son inmutables una vez subidos (una foto de un mes no
                // se vuelve a editar) — solo hace falta revisar altas/bajas, no
                // comparar campo por campo como en Proforma.
                mapaServidor.remove(idRemota);
            } else {
                Uri deleteUri = ContractInsertPagoFactura.CONTENT_URI.buildUpon()
                        .appendPath(idRemota).build();
                Log.i(TAG, "Programando eliminación de: " + deleteUri);
                ops.add(ContentProviderOperation.newDelete(deleteUri).build());
                syncResult.stats.numDeletes++;
            }
        }
        c.close();

        for (JSONObject item : mapaServidor.values()) {
            try {
                String idRemota = item.getString("id");
                Log.i(TAG, "Programando inserción de Pago de Factura en Base Local: " + idRemota);
                ops.add(ContentProviderOperation.newInsert(ContractInsertPagoFactura.CONTENT_URI)
                        .withValue(Constantes.ID_REMOTA, idRemota)
                        .withValue(ContractInsertPagoFactura.Columnas.ID_PROFORMA, UtilidadesPagoFactura.optStringSeguro(item, "id_proforma"))
                        .withValue(ContractInsertPagoFactura.Columnas.ID_AGENDAMIENTO, UtilidadesPagoFactura.optStringSeguro(item, "id_agendamiento"))
                        .withValue(ContractInsertPagoFactura.Columnas.CODIGO_PDV, UtilidadesPagoFactura.optStringSeguro(item, "codigo_pdv"))
                        .withValue(ContractInsertPagoFactura.Columnas.USUARIO, UtilidadesPagoFactura.optStringSeguro(item, "usuario"))
                        .withValue(ContractInsertPagoFactura.Columnas.NUMERO_CUOTA, UtilidadesPagoFactura.optStringSeguro(item, "numero_cuota"))
                        .withValue(ContractInsertPagoFactura.Columnas.MONTO_PAGO, UtilidadesPagoFactura.optStringSeguro(item, "monto_pago"))
                        .withValue(ContractInsertPagoFactura.Columnas.FOTO_PAGO, UtilidadesPagoFactura.optStringSeguro(item, "foto_pago"))
                        .withValue(ContractInsertPagoFactura.Columnas.FECHA_PAGO, UtilidadesPagoFactura.optStringSeguro(item, "fecha_pago"))
                        .withValue(ContractInsertPagoFactura.Columnas.OBSERVACION, UtilidadesPagoFactura.optStringSeguro(item, "observacion"))
                        .withValue(Constantes.PENDIENTE_INSERCION, 0)
                        .build());
                syncResult.stats.numInserts++;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (syncResult.stats.numInserts > 0 ||
                syncResult.stats.numDeletes > 0) {
            Log.i(TAG, "Aplicando operaciones de Pago de Factura...");
            try {
                resolver.applyBatch(Constantes.AUTHORITY, ops);
            } catch (RemoteException | OperationApplicationException e) {
                e.printStackTrace();
            }
            resolver.notifyChange(ContractInsertPagoFactura.CONTENT_URI, null, false);
            Log.i(TAG, "Sincronización de tabla Pago de Factura finalizada.");
            enviarBroadcast(true, Mensajes.SYNC_FINALIZADA_PAGO_FACTURA);
        } else {
            Log.i(TAG, "No se requiere sincronización de tabla Pago de Factura");
            enviarBroadcast(true, Mensajes.SYNC_NOREQUERIDA + "PagoFactura");
        }
    }


    /*SHARE*/
    private void realizarSincronizacionRemotaInsertPDI() {
        Log.i(TAG, "SyncAdapterSubida: Preparando datos a ser insertados en el servidor...");

        iniciarActualizacion(ContractInsertPDI.CONTENT_URI);

        Cursor c = obtenerRegistrosSucios(ContractInsertPDI.CONTENT_URI, Projection.PROJECTION_INSERT_PDI);

        Log.i(TAG, "SyncAdapterSubida: Se encontraron " + c.getCount() + " registros sucios.");


        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                final int idLocal = c.getInt(Projection.COLUMNA_ID);

                Log.i(TAG, "SyncAdapterSubida: Insertando datos de Share en el servidor...");

                VolleySingleton.getInstance(getContext()).addToRequestQueue(
                        new JsonObjectRequest(
                                Request.Method.POST,
                                Constantes.INSERTAR_PDI,
                                UtilidadesPDI.deCursorAJSONObject(c),
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        procesarRespuestaInsertPDI(response, idLocal);
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
//                                            Toast.makeText(getContext(), "Datos no enviados, almacenados en el teléfono.", Toast.LENGTH_SHORT).show();
                                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Network Timeout");
                                        } else if (error instanceof AuthFailureError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error AuthFailure");
                                        } else if (error instanceof ServerError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Server");
                                        } else if (error instanceof NetworkError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Network");
                                        } else if (error instanceof ParseError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Parse");
                                        }
                                    }
                                }
                        ) {
                            @Override
                            public Map<String, String> getHeaders() {
                                Map<String, String> headers = new HashMap<String, String>();
                                headers.put("Content-Type", "application/json; charset=utf-8");
                                headers.put("Accept", "application/json");
                                return headers;
                            }

                            @Override
                            public String getBodyContentType() {
                                return "application/json; charset=utf-8";// + getParamsEncoding();
                            }
                        }
                );
            }
        } else {
            //Toast.makeText(getContext(),"No hay datos en cola de sincronizacion",Toast.LENGTH_SHORT).show();
//            StockActivity stockActivity = new StockActivity();
//            stockActivity.showToastFromBackground("No se requiere sincronización");
            Log.i(TAG, "SyncAdapterSubida: No se requiere sincronización: No hay datos en cola de sincronizacion");
            enviarBroadcast(true, Mensajes.SYNC_NODATOSCOLA);
        }
        c.close();
    }

    public void procesarRespuestaInsertPDI(JSONObject response, int idLocal) {
        try {
            // Obtener estado
            String estado = response.getString(Constantes.ESTADO);
            // Obtener mensaje
            String mensaje = response.getString(Constantes.MENSAJE);

            switch (estado) {
                case Constantes.SUCCESS:
                    // Obtener identificador del nuevo registro creado en el servidor
                    String idRemota = response.getString(Constantes.LAST_ID);
                    Log.i(TAG, mensaje);
                    //Borrar datos sucios:
                    finalizarActualizacion(ContractInsertPDI.CONTENT_URI, ContractInsertPDI.Columnas._ID, idRemota, idLocal);
                    enviarBroadcast(true, Mensajes.ON_SYNC_TRUE);
                    Toast.makeText(getContext(),"Datos almacenados en el servidor",Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "SyncAdapterSubida: Registros Insertados en el Servidor y en la Base Local "+idRemota);
                    break;

                case Constantes.FAILED:
                    Log.i(TAG, mensaje);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /*GPS*/
    private void realizarSincronizacionRemotaInsertGps() {
        Log.i(TAG, "SyncAdapterSubida: Preparando datos a ser insertados en el servidor...");

        iniciarActualizacion(ContractInsertGps.CONTENT_URI);

        Cursor c = obtenerRegistrosSucios(ContractInsertGps.CONTENT_URI, Projection.PROJECTION_INSERT_GPS);

        Log.i(TAG, "SyncAdapterSubida: Se encontraron " + c.getCount() + " registros sucios.");


        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                final int idLocal = c.getInt(Projection.COLUMNA_ID);

                Log.i(TAG, "SyncAdapterSubida: Insertando datos de Gps en el servidor...");

                VolleySingleton.getInstance(getContext()).addToRequestQueue(
                        new JsonObjectRequest(
                                Request.Method.POST,
                                Constantes.INSERTAR_GPS,
                                UtilidadesGps.deCursorAJSONObject(c),
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        procesarRespuestaInsertGps(response, idLocal);
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
//                                            Toast.makeText(getContext(), "Datos no enviados, almacenados en el teléfono.", Toast.LENGTH_SHORT).show();
                                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Network Timeout");
                                        } else if (error instanceof AuthFailureError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error AuthFailure");
                                        } else if (error instanceof ServerError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Server");
                                        } else if (error instanceof NetworkError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Network");
                                        } else if (error instanceof ParseError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Parse");
                                        }

                                        Log.i("error",""+error.getMessage());
                                    }
                                }
                        ) {
                            @Override
                            public Map<String, String> getHeaders() {
                                Map<String, String> headers = new HashMap<String, String>();
                                headers.put("Content-Type", "application/json; charset=utf-8");
                                headers.put("Accept", "application/json");
                                return headers;
                            }

                            @Override
                            public String getBodyContentType() {
                                return "application/json; charset=utf-8";// + getParamsEncoding();
                            }
                        }
                );
            }
        } else {
            //Toast.makeText(getContext(),"No hay datos en cola de sincronizacion",Toast.LENGTH_SHORT).show();
//            StockActivity stockActivity = new StockActivity();
//            stockActivity.showToastFromBackground("No se requiere sincronización");
            Log.i(TAG, "SyncAdapterSubida: No se requiere sincronización: No hay datos en cola de sincronizacion");
            enviarBroadcast(true, Mensajes.SYNC_NODATOSCOLA);
        }
        c.close();
    }



    /**
     * Procesa los diferentes tipos de respuesta obtenidos del servidor
     *
     * @param response Respuesta en subchannel Json
     */
    public void procesarRespuestaInsertGps(JSONObject response, int idLocal) {

        try {
            // Obtener estado
            String estado = response.getString(Constantes.ESTADO);
            // Obtener mensaje
            String mensaje = response.getString(Constantes.MENSAJE);

            switch (estado) {
                case Constantes.SUCCESS:
                    // Obtener identificador del nuevo registro creado en el servidor
                    String idRemota = response.getString(Constantes.LAST_ID);
                    Log.i(TAG, mensaje);
                    //Borrar datos sucios:
                    finalizarActualizacion(ContractInsertGps.CONTENT_URI, ContractInsertGps.Columnas._ID, idRemota, idLocal);
                    enviarBroadcast(true, Mensajes.ON_SYNC_TRUE);
                    Toast.makeText(getContext(),"Datos almacenados en el servidor",Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "SyncAdapterSubida: Registros Insertados en el Servidor y en la Base Local "+idRemota);
                    break;

                case Constantes.FAILED:
                    Log.i(TAG, mensaje);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /* ASISTENCIAS */

    private void realizarSincronizacionRemotaInsertAsistencia() {
        Log.i(TAG, "SyncAdapterSubida asis: Preparando datos a ser insertados en el servidor...");

        iniciarActualizacion(ContractInsertAsistencia.CONTENT_URI);

        Cursor c = obtenerRegistrosSucios(ContractInsertAsistencia.CONTENT_URI, Projection.PROJECTION_INSERT_ASISTENCIA);

        Log.i(TAG, "SyncAdapterSubida: Se encontraron " + c.getCount() + " registros sucios.");


        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                final int idLocal = c.getInt(Projection.COLUMNA_ID);

                Log.i(TAG, "SyncAdapterSubida: Insertando datos de Asistencia en el servidor...");

                VolleySingleton.getInstance(getContext()).addToRequestQueue(
                        new JsonObjectRequest(
                                Request.Method.POST,
                                Constantes.INSERTAR_ASISTENCIA,
                                UtilidadesAsistencia.deCursorAJSONObject(c),
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        procesarRespuestaInsertAsistencia(response, idLocal);
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
//                                            Toast.makeText(getContext(), "Datos no enviados, almacenados en el teléfono.", Toast.LENGTH_SHORT).show();
                                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Network Timeout");
                                        } else if (error instanceof AuthFailureError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error AuthFailure");
                                        } else if (error instanceof ServerError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Server");
                                        } else if (error instanceof NetworkError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Network");
                                        } else if (error instanceof ParseError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Parse");
                                        }

                                        Log.i("error",""+error.getMessage());
                                    }
                                }
                        ) {
                            @Override
                            public Map<String, String> getHeaders() {
                                Map<String, String> headers = new HashMap<String, String>();
                                headers.put("Content-Type", "application/json; charset=utf-8");
                                headers.put("Accept", "application/json");
                                return headers;
                            }

                            @Override
                            public String getBodyContentType() {
                                return "application/json; charset=utf-8";// + getParamsEncoding();
                            }
                        }
                );
            }
        } else {
            //Toast.makeText(getContext(),"No hay datos en cola de sincronizacion",Toast.LENGTH_SHORT).show();
//            StockActivity stockActivity = new StockActivity();
//            stockActivity.showToastFromBackground("No se requiere sincronización");
            Log.i(TAG, "SyncAdapterSubida: No se requiere sincronización: No hay datos en cola de sincronizacion");
            enviarBroadcast(true, Mensajes.SYNC_NODATOSCOLA);
        }
        c.close();
    }


    public void procesarRespuestaInsertAsistencia(JSONObject response, int idLocal) {

        try {
            // Obtener estado
            String estado = response.getString(Constantes.ESTADO);
            // Obtener mensaje
            String mensaje = response.getString(Constantes.MENSAJE);

            switch (estado) {
                case Constantes.SUCCESS:
                    // Obtener identificador del nuevo registro creado en el servidor
                    String idRemota = response.getString(Constantes.LAST_ID);
                    Log.i(TAG, mensaje);
                    //Borrar datos sucios:
                    finalizarActualizacion(ContractInsertAsistencia.CONTENT_URI, ContractInsertAsistencia.Columnas._ID, idRemota, idLocal);
                    enviarBroadcast(true, Mensajes.ON_SYNC_TRUE);
                    Toast.makeText(getContext(),"Datos almacenados en el servidor",Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "SyncAdapterSubida: Registros Insertados en el Servidor y en la Base Local "+idRemota);
                    break;

                case Constantes.FAILED:
                    Log.i(TAG, mensaje);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    private void realizarSincronizacionRemotaInsertGeo() {
        Log.i(TAG, "GEOREFERENCIA: Preparando datos a ser insertados en el servidor...");

        iniciarActualizacion(ContractInsertRastreo.CONTENT_URI);

        Cursor c = obtenerRegistrosSucios(ContractInsertRastreo.CONTENT_URI, Projection.PROJECTION_INSERT_RASTREO);

        Log.i(TAG, "GEOREFERENCIA: Se encontraron " + c.getCount() + " registros sucios.");

        if (c.getCount() > 0) {
            try{
                while (c.moveToNext()) {
                    final int idLocal = c.getInt(Projection.COLUMNA_ID);

                    Log.i(TAG, "GEOREFERENCIA: Insertando datos en el servidor...");

                    VolleySingleton.getInstance(getContext()).addToRequestQueue(
                            new JsonObjectRequest(
                                    Request.Method.POST,
                                    Constantes.INSERTAR_GEO,
                                    UtilidadesRastreo.deCursorAJSONObject(c),
                                    new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            procesarRespuestaInsertGeo(response, idLocal);
                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
//                                        Toast.makeText(getContext(),"Problema en el servidor: Datos almacenados localmente",Toast.LENGTH_SHORT).show();
                                            if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                                Log.i(TAG, "GEOREFERENCIA: Error Network Timeout");
                                                Log.i(TAG, "GEOREFERENCIA: Problema en respuesta del servidor...");
                                                Log.d(TAG, "Error Volley: " + error.getMessage());
                                            } else if (error instanceof AuthFailureError) {
                                                Log.i(TAG, "GEOREFERENCIA: Error AuthFailure");
                                                Log.i(TAG, "GEOREFERENCIA: Problema en respuesta del servidor...");
                                                Log.d(TAG, "Error Volley: " + error.getMessage());
                                            } else if (error instanceof ServerError) {
                                                Log.i(TAG, "GEOREFERENCIA: Error Server");
                                                Log.i(TAG, "GEOREFERENCIA: Problema en respuesta del servidor...");
                                                Log.d(TAG, "Error Volley: " + error.getMessage());
                                            } else if (error instanceof NetworkError) {
                                                Log.i(TAG, "GEOREFERENCIA: Error Network");
                                                Log.i(TAG, "GEOREFERENCIA: Problema en respuesta del servidor...");
                                                Log.d(TAG, "Error Volley: " + error.getMessage());
                                            } else if (error instanceof ParseError) {
                                                Log.i(TAG, "GEOREFERENCIA: Error Parse");
                                                Log.i(TAG, "GEOREFERENCIA: Problema en respuesta del servidor...");
                                                Log.d(TAG, "Error Volley: " + error.getMessage());
                                            }
                                        }
                                    }
                            ) {
                                @Override
                                public Map<String, String> getHeaders() {
                                    Map<String, String> headers = new HashMap<String, String>();
                                    headers.put("Content-Type", "application/json; charset=utf-8");
                                    headers.put("Accept", "application/json");
                                    return headers;
                                }

                                @Override
                                public String getBodyContentType() {
                                    return "application/json; charset=utf-8";// + getParamsEncoding();
                                }
                            }
                    );
                }
            }catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            //Toast.makeText(getContext(),"No hay datos en cola de sincronizacion",Toast.LENGTH_SHORT).show();
//            StockActivity stockActivity = new StockActivity();
//            stockActivity.showToastFromBackground("No se requiere sincronización");
            Log.i(TAG, "GEOREFERENCIA: No se requiere sincronización: No hay datos en cola de sincronizacion");
        }
        c.close();
    }

    /**
     * Procesa los diferentes tipos de respuesta obtenidos del servidor
     * @param response Respuesta en subchannel Json
     */
    public void procesarRespuestaInsertGeo(JSONObject response, int idLocal) {

        try {
            // Obtener estado
            String estado = response.getString(Constantes.ESTADO);
            // Obtener mensaje
            String mensaje = response.getString(Constantes.MENSAJE);

            switch (estado) {
                case Constantes.SUCCESS:
                    // Obtener identificador del nuevo registro creado en el servidor
                    String idRemota = response.getString(Constantes.LAST_ID);
                    Log.i(TAG, mensaje);
                    //Borrar datos sucios:
                    finalizarActualizacion(ContractInsertRastreo.CONTENT_URI, ContractInsertRastreo.Columnas._ID, idRemota, idLocal);
                    //Toast.makeText(getContext(),"Datos almacenados en el servidor",Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "GEOREFERENCIA: Registros Insertados en el Servidor - LastId: "+idRemota);
                    break;

                case Constantes.FAILED:
                    //onResponse(false);
                    Log.i(TAG, mensaje);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void realizarSincronizacionRemotaInsertNot() {
        Log.i(TAG, "SyncAdapterSubida: Preparando datos a ser insertados en el servidor...");

        iniciarActualizacion(ContractNotificacion.CONTENT_URI);

        Cursor c = obtenerRegistrosSucios(ContractNotificacion.CONTENT_URI, Projection.PROJECTION_NOTIFICACION);

        Log.i(TAG, "SyncAdapterSubida: Se encontraron " + c.getCount() + " registros sucios.");


        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                final int idLocal = c.getInt(Projection.COLUMNA_ID);

                Log.i(TAG, "SyncAdapterSubida: Insertando datos de Gps en el servidor...");

                VolleySingleton.getInstance(getContext()).addToRequestQueue(
                        new JsonObjectRequest(
                                Request.Method.POST,
                                Constantes.INSERTAR_NOTIFICACION,
                                UtilidadesNotificacion.deCursorAJSONObject(c),
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        procesarRespuestaInsertNot(response, idLocal);
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
//                                            Toast.makeText(getContext(), "Datos no enviados, almacenados en el teléfono.", Toast.LENGTH_SHORT).show();
                                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Network Timeout");
                                        } else if (error instanceof AuthFailureError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error AuthFailure");
                                        } else if (error instanceof ServerError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Server");
                                        } else if (error instanceof NetworkError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Network");
                                        } else if (error instanceof ParseError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Parse");
                                        }
                                    }
                                }
                        ) {
                            @Override
                            public Map<String, String> getHeaders() {
                                Map<String, String> headers = new HashMap<String, String>();
                                headers.put("Content-Type", "application/json; charset=utf-8");
                                headers.put("Accept", "application/json");
                                return headers;
                            }

                            @Override
                            public String getBodyContentType() {
                                return "application/json; charset=utf-8";// + getParamsEncoding();
                            }
                        }
                );
            }
        } else {
            //Toast.makeText(getContext(),"No hay datos en cola de sincronizacion",Toast.LENGTH_SHORT).show();
//            StockActivity stockActivity = new StockActivity();
//            stockActivity.showToastFromBackground("No se requiere sincronización");
            Log.i(TAG, "SyncAdapterSubida: No se requiere sincronización: No hay datos en cola de sincronizacion");
            enviarBroadcast(true, Mensajes.SYNC_NODATOSCOLA);
        }
        c.close();
    }

    /**
     * Procesa los diferentes tipos de respuesta obtenidos del servidor
     *
     * @param response Respuesta en subchannel Json
     */
    public void procesarRespuestaInsertNot(JSONObject response, int idLocal) {

        try {
            // Obtener estado
            String estado = response.getString(Constantes.ESTADO);
            // Obtener mensaje
            String mensaje = response.getString(Constantes.MENSAJE);

            switch (estado) {
                case Constantes.SUCCESS:
                    // Obtener identificador del nuevo registro creado en el servidor
                    String idRemota = response.getString(Constantes.LAST_ID);
                    Log.i(TAG, mensaje);
                    //Borrar datos sucios:
                    finalizarActualizacion(ContractNotificacion.CONTENT_URI, ContractNotificacion.Columnas._ID, idRemota, idLocal);
                    Log.i(TAG, "SyncAdapterSubida: Registros Insertados en el Servidor y en la Base Local "+idRemota);
                    break;

                case Constantes.FAILED:
                    Log.i(TAG, mensaje);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }




    private void realizarSincronizacionRemotaInsertVentas() {
        Log.i(TAG, "SyncAdapterSubida: Preparando datos a ser insertados en el servidor...");

        iniciarActualizacion(ContractInsertVentas.CONTENT_URI);

        Cursor c = obtenerRegistrosSucios(ContractInsertVentas.CONTENT_URI, Projection.PROJECTION_INSERT_VENTAS);

        Log.i(TAG, "SyncAdapterSubida: Se encontraron " + c.getCount() + " registros sucios.");


        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                final int idLocal = c.getInt(Projection.COLUMNA_ID);

                Log.i(TAG, "SyncAdapterSubida: Insertando datos de Gps en el servidor...");

                VolleySingleton.getInstance(getContext()).addToRequestQueue(
                        new JsonObjectRequest(
                                Request.Method.POST,
                                Constantes.INSERTAR_VENTAS,
                                UtilidadesVentas.deCursorAJSONObject(c),
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        procesarRespuestaInsertVentas(response, idLocal);
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
//                                            Toast.makeText(getContext(), "Datos no enviados, almacenados en el teléfono.", Toast.LENGTH_SHORT).show();
                                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Network Timeout");
                                        } else if (error instanceof AuthFailureError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error AuthFailure");
                                        } else if (error instanceof ServerError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Server");
                                        } else if (error instanceof NetworkError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Network");
                                        } else if (error instanceof ParseError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Parse");
                                        }
                                    }
                                }
                        ) {
                            @Override
                            public Map<String, String> getHeaders() {
                                Map<String, String> headers = new HashMap<String, String>();
                                headers.put("Content-Type", "application/json; charset=utf-8");
                                headers.put("Accept", "application/json");
                                return headers;
                            }

                            @Override
                            public String getBodyContentType() {
                                return "application/json; charset=utf-8";// + getParamsEncoding();
                            }
                        }
                );
            }
        } else {
            //Toast.makeText(getContext(),"No hay datos en cola de sincronizacion",Toast.LENGTH_SHORT).show();
//            StockActivity stockActivity = new StockActivity();
//            stockActivity.showToastFromBackground("No se requiere sincronización");
            Log.i(TAG, "SyncAdapterSubida: No se requiere sincronización: No hay datos en cola de sincronizacion");
            enviarBroadcast(true, Mensajes.SYNC_NODATOSCOLA);
        }
        c.close();
    }

    /**
     * Procesa los diferentes tipos de respuesta obtenidos del servidor
     *
     * @param response Respuesta en subchannel Json
     */
    public void procesarRespuestaInsertVentas(JSONObject response, int idLocal) {

        try {
            // Obtener estado
            String estado = response.getString(Constantes.ESTADO);
            // Obtener mensaje
            String mensaje = response.getString(Constantes.MENSAJE);

            switch (estado) {
                case Constantes.SUCCESS:
                    // Obtener identificador del nuevo registro creado en el servidor
                    String idRemota = response.getString(Constantes.LAST_ID);
                    Log.i(TAG, mensaje);
                    //Borrar datos sucios:
                    finalizarActualizacion(ContractInsertVentas.CONTENT_URI, ContractInsertVentas.Columnas._ID, idRemota, idLocal);
                    Toast.makeText(getContext(),"Datos almacenados en el servidor",Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "SyncAdapterSubida: Registros Insertados en el Servidor y en la Base Local "+idRemota);
                    break;

                case Constantes.FAILED:
                    Log.i(TAG, mensaje);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }





    private void realizarSincronizacionRemotaInsertLog() {
        Log.i(TAG, "SyncAdapterSubida: Preparando datos de Log a ser insertados en el servidor...");

        iniciarActualizacion(ContractLog.CONTENT_URI);

        Cursor c = obtenerRegistrosSucios(ContractLog.CONTENT_URI, Projection.PROJECTION_LOG);

        Log.i(TAG, "SyncAdapterSubida: Se encontraron " + c.getCount() + " registros sucios.");


        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                final int idLocal = c.getInt(Projection.COLUMNA_ID);

                Log.i(TAG, "SyncAdapterSubida: Insertando datos de log en el servidor...");

                VolleySingleton.getInstance(getContext()).addToRequestQueue(
                        new JsonObjectRequest(
                                Request.Method.POST,
                                Constantes.INSERTAR_LOG,
                                UtilidadesLog.deCursorAJSONObject(c),
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        procesarRespuestaInsertLog(response, idLocal);
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
//                                            Toast.makeText(getContext(), "Datos no enviados, almacenados en el teléfono.", Toast.LENGTH_SHORT).show();
                                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Network Timeout");
                                        } else if (error instanceof AuthFailureError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error AuthFailure");
                                        } else if (error instanceof ServerError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Server");
                                        } else if (error instanceof NetworkError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Network");
                                        } else if (error instanceof ParseError) {
                                            Log.i(TAG, "SyncAdapterSubida: Error Parse");
                                        }
                                    }
                                }
                        ) {
                            @Override
                            public Map<String, String> getHeaders() {
                                Map<String, String> headers = new HashMap<String, String>();
                                headers.put("Content-Type", "application/json; charset=utf-8");
                                headers.put("Accept", "application/json");
                                return headers;
                            }

                            @Override
                            public String getBodyContentType() {
                                return "application/json; charset=utf-8";// + getParamsEncoding();
                            }
                        }
                );
            }
        } else {
            //Toast.makeText(getContext(),"No hay datos en cola de sincronizacion",Toast.LENGTH_SHORT).show();
//            StockActivity stockActivity = new StockActivity();
//            stockActivity.showToastFromBackground("No se requiere sincronización");
            Log.i(TAG, "SyncAdapterSubida: No se requiere sincronización: No hay datos en cola de sincronizacion");
            enviarBroadcast(true, Mensajes.SYNC_NODATOSCOLA);
        }
        c.close();
    }

    /**
     * Procesa los diferentes tipos de respuesta obtenidos del servidor
     *
     * @param response Respuesta en subchannel Json
     */
    public void procesarRespuestaInsertLog(JSONObject response, int idLocal) {
        try {
            // Obtener estado
            String estado = response.getString(Constantes.ESTADO);
            // Obtener mensaje
            String mensaje = response.getString(Constantes.MENSAJE);

            switch (estado) {
                case Constantes.SUCCESS:
                    // Obtener identificador del nuevo registro creado en el servidor
                    String idRemota = response.getString(Constantes.LAST_ID);
                    Log.i(TAG, mensaje);
                    //Borrar datos sucios:
                    finalizarActualizacion(ContractLog.CONTENT_URI, ContractLog.Columnas._ID, idRemota, idLocal);
                    Log.i(TAG, "SyncAdapterSubida: Registros Insertados en el Servidor y en la Base Local "+idRemota);
                    break;

                case Constantes.FAILED:
                    Log.i(TAG, mensaje);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /*
     * ***********************************************************************************************************************
     */


    /**
     * Cambia a estado "de sincronización" el registro que se acaba de insertar localmente
     */
    private void iniciarActualizacion(Uri ContentUri) {//Uri
        Uri uri = ContentUri;
        String selection = Constantes.PENDIENTE_INSERCION + "=? AND "
                + Constantes.ESTADO + "=?";
        String[] selectionArgs = new String[]{"1", Constantes.ESTADO_OK + ""};

        ContentValues v = new ContentValues();
        v.put(Constantes.ESTADO, Constantes.ESTADO_SYNC);

        int results = resolver.update(uri, v, selection, selectionArgs);
        Log.i(TAG, "SyncAdapterSubida: Registros puestos en cola de inserción: " + results);
    }

    /**
     * Limpia el registro que se sincronizó y le asigna la nueva id remota proveida
     * por el servidor
     *
     * @param idRemota id remota
     */
    private void finalizarActualizacion(Uri ContentUri, String ID, String idRemota, int idLocal) {
        Log.i(TAG, "SyncAdapterSubida:finalizando actualizacion");
        Uri uri = ContentUri;
        String selection =  ID + "=?";
        String[] selectionArgs = new String[]{String.valueOf(idLocal)};

        ContentValues v = new ContentValues();
        v.put(Constantes.PENDIENTE_INSERCION, "0");
        v.put(Constantes.ESTADO, Constantes.ESTADO_OK);
        v.put(Constantes.ID_REMOTA, idRemota);

        resolver.update(uri, v, selection, selectionArgs);
    }

    /**
     * Obtiene el registro que se acaba de marcar como "pendiente por sincronizar" y
     * con "estado de sincronización"
     * @return Cursor con el registro.
     */
    private Cursor obtenerRegistrosSucios(Uri ContentUri, String[] projection) {//Uri, projection
        Uri uri = ContentUri;
        String selection = Constantes.PENDIENTE_INSERCION + "=? AND "
                + Constantes.ESTADO + "=?";
        String[] selectionArgs = new String[]{"1", Constantes.ESTADO_SYNC + ""};

        return resolver.query(uri, projection, selection, selectionArgs, null);
    }

    private void enviarBroadcast(boolean estado, String mensaje) {
        Intent intentLocal = new Intent(Intent.ACTION_SYNC);
        intentLocal.putExtra(Mensajes.EXTRA_RESULTADO, estado);
        intentLocal.putExtra(Mensajes.EXTRA_MENSAJE, mensaje);
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intentLocal);
    }

}