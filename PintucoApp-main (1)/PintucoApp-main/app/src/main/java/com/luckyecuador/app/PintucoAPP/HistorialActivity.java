package com.luckyecuador.app.PintucoAPP;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.luckyecuador.app.PintucoAPP.Adaptadores.AdapterAgotados;
import com.luckyecuador.app.PintucoAPP.Adaptadores.AdapterAlmuerzo;
import com.luckyecuador.app.PintucoAPP.Adaptadores.AdapterAsistencias;
import com.luckyecuador.app.PintucoAPP.Adaptadores.AdapterCanjes;
import com.luckyecuador.app.PintucoAPP.Adaptadores.AdapterEjecucionMateriales;
import com.luckyecuador.app.PintucoAPP.Adaptadores.AdapterEvidencias;
import com.luckyecuador.app.PintucoAPP.Adaptadores.AdapterExh;
import com.luckyecuador.app.PintucoAPP.Adaptadores.AdapterFlooring;
import com.luckyecuador.app.PintucoAPP.Adaptadores.AdapterFotografico;
import com.luckyecuador.app.PintucoAPP.Adaptadores.AdapterFotograficoPdv;
import com.luckyecuador.app.PintucoAPP.Adaptadores.AdapterGps;
import com.luckyecuador.app.PintucoAPP.Adaptadores.AdapterImplementacion;
import com.luckyecuador.app.PintucoAPP.Adaptadores.AdapterImpulso;
import com.luckyecuador.app.PintucoAPP.Adaptadores.AdapterLogisticoRelevo;
import com.luckyecuador.app.PintucoAPP.Adaptadores.AdapterLogisticoPreforma;
import com.luckyecuador.app.PintucoAPP.Adaptadores.AdapterMCI;
import com.luckyecuador.app.PintucoAPP.Adaptadores.AdapterMallaCodificados;
import com.luckyecuador.app.PintucoAPP.Adaptadores.AdapterMaterialesRecibidos;
import com.luckyecuador.app.PintucoAPP.Adaptadores.AdapterPDI;
import com.luckyecuador.app.PintucoAPP.Adaptadores.AdapterPDV;
import com.luckyecuador.app.PintucoAPP.Adaptadores.AdapterPacks;
import com.luckyecuador.app.PintucoAPP.Adaptadores.AdapterPrecios;
import com.luckyecuador.app.PintucoAPP.Adaptadores.AdapterPreguntas;
import com.luckyecuador.app.PintucoAPP.Adaptadores.AdapterProdCad;
import com.luckyecuador.app.PintucoAPP.Adaptadores.AdapterProductosEnMalEstado;
import com.luckyecuador.app.PintucoAPP.Adaptadores.AdapterPromo;
import com.luckyecuador.app.PintucoAPP.Adaptadores.AdapterPropensos;
import com.luckyecuador.app.PintucoAPP.Adaptadores.AdapterRotacion;
import com.luckyecuador.app.PintucoAPP.Adaptadores.AdapterShare;
import com.luckyecuador.app.PintucoAPP.Adaptadores.AdapterTareas;
import com.luckyecuador.app.PintucoAPP.Adaptadores.AdapterValores;
import com.luckyecuador.app.PintucoAPP.Adaptadores.AdapterVenta;
import com.luckyecuador.app.PintucoAPP.Adaptadores.AdapterVentas;
import com.luckyecuador.app.PintucoAPP.Adaptadores.SugeridosAdapter;
import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertAgotados;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertAlmuerzo;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertAsistencia;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertCanjes;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertEjecucionMateriales;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertEvidencias;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertExh;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertFotografico;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertGps;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertImplementacion;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertImpulso;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertLogisticoRelevo;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertMCIPdv;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertMallaCodificados;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertMaterialesRecibidos;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertPDI;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertPacks;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertPdvFotografico;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertPrecios;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertPreguntas;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertProdCaducar;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertPromocion;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertPropensosYProdMalEst;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertRotacion;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertShare;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertSugeridos;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertTareas;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertValores;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertVenta;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertVentas;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractNotificacion;
import com.luckyecuador.app.PintucoAPP.Contracts.InsertFlooring;
import com.luckyecuador.app.PintucoAPP.DataBase.DatabaseHelper;
import com.luckyecuador.app.PintucoAPP.DataBase.Provider;
import com.luckyecuador.app.PintucoAPP.Sync.SyncAdapter;
import com.luckyecuador.app.PintucoAPP.Utils.GuardarLog;
import com.luckyecuador.app.PintucoAPP.Utils.SignalQuality;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HistorialActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener, LoaderManager.LoaderCallbacks<Cursor>, SwipeRefreshLayout.OnRefreshListener , View.OnClickListener {

    DatabaseHelper handler;
    Spinner spMod, spPdv, spCategoria, spMarca, spStatus;
    LinearLayout llCategoria, llMarca, llCategorias, llPdv;
    String basedatos;
    ImageButton btnFinicio;
    ImageButton btnFfin;
    TextView txtFinicio;
    TextView txtFfin;
    String tablaInsert, columnaPdv, columnaCategoria, columnaMarca;
    SwipeRefreshLayout swipeRefresh;

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private AdapterPrecios adapterPrecio;
    private AdapterExh adapterExh;
    private AdapterGps adapterGps;
    private AdapterAsistencias adapterAsistencias;

    private AdapterFotograficoPdv adapterFotograficoPdv;
    private AdapterFlooring adapterFlooring;
    private AdapterPromo adapterPromo;
    private AdapterValores adapterValores;
    private AdapterImplementacion adapterImplementacion;
    private AdapterPDV adapterPDV;
    private AdapterShare adapterShare;
    private AdapterAgotados adapterAgotados;
    private AdapterVenta adapterVenta;
    private AdapterFotografico adapterFotografico;
    private AdapterPreguntas adapterPreguntas;
    private AdapterPacks adapterPacks;
    private AdapterProdCad adapterProdCad;
    private AdapterImpulso adapterImpulso;
    private AdapterRotacion adapterRotacion;
    private SugeridosAdapter adapterSugeridos;
    private AdapterTareas adapterTareas;
    private AdapterCanjes adapterCanjes;
    private AdapterMCI adapterMCI;
    private AdapterMaterialesRecibidos adapterMaterialesRecibidos;
    private AdapterEjecucionMateriales adapterEjecucionMateriales;
    private AdapterPDI adapterPDI;
    private AdapterMallaCodificados adapterMallaCodificados;
    private AdapterEvidencias adapterEvidencias;
    private AdapterVentas adapterVentas;
    private AdapterPropensos adapterPropensos;
    private AdapterProductosEnMalEstado adapterProductosEnMalEstado;
    private AdapterAlmuerzo adapterAlmuerzo;
    private AdapterLogisticoRelevo adapterLogisticoRelevo;
    private AdapterLogisticoPreforma adapterLogisticoPreforma;
    private Spinner spTipoLogistico;
    private LinearLayout layoutTipoLogistico;
    private String tipoLogistico = "RELEVO";

    String tipo,user, modulo ="", pdv="", categoria="", marca="", status="",campo = "";
    String fDesde = "", fHasta = "";

    private TextView emptyView;

    private static int idPrecio = 0;
    private static int idExh = 1;
    private static int idGps = 2;
    private static int idFlooring = 3;
    private static int idPromo = 4;
    private static int idValores = 5;
    private static int idImple = 6;
    private static int idNotificacion = 7;
    private static int idShare = 8;
    private static int idAgotados = 9;
    private static int idVenta = 10;
    private static int idExhAntDes = 11;
    private static int idPreg = 12;
    private static int idPacks = 13;
    private static int idProdCad = 14;
    private static int idImpul = 15;
    private static int idRotacion = 16;
    private static int idSugeridos = 17;
    private static int idTareas = 18;
    private static int idCanjes = 19;
    private static int idMCI = 20;
    private static int idMaterialesRecibidos = 21;
    private static int idEjecMateriales = 22;
    private static int idPDI = 23;
    private static int idMallaCodificados = 24;
    private static int idEvidencias = 25;

    private static int idAsistencias = 26;
    private static int idAdapterFotograficoPdv = 27;
    private static int idVentas = 28;
    private static int idPropensos = 29;
    private static int idProductosEnMalEstado = 30;
    private static int idAlmuerzo = 31;
    private static int idLogisticoRelevo = 32;
    private static int idLogisticoPreforma = 33;

    public static final String ACTION_NOTIFICATION_CLICKED = "com.ejemplo.app.NOTIFICATION_CLICKED";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);
        setToolbar();

        LoadData();
        handler = new DatabaseHelper(this, Provider.DATABASE_NAME,null,1);

        new GuardarLog(HistorialActivity.this).saveLog(user, "", "Ingreso al Módulo Status(Enviado / No Enviado)");
        //startService(new Intent(getApplicationContext(), MyService.class));

        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        spMod = (Spinner) findViewById(R.id.spModulo);

        spMod = (Spinner) findViewById(R.id.spModulo);
        spPdv = (Spinner) findViewById(R.id.spPdv);
        spCategoria = (Spinner) findViewById(R.id.spCategoria);
        spMarca = (Spinner) findViewById(R.id.spMarca);
        spStatus = (Spinner) findViewById(R.id.spStatus);
        llCategoria = (LinearLayout) findViewById(R.id.layout_categoria);
        llMarca = (LinearLayout) findViewById(R.id.layout_marca);
        llCategorias = (LinearLayout) findViewById(R.id.layout_categorias);
        llPdv = (LinearLayout) findViewById(R.id.layout_pdv);
        btnFinicio = (ImageButton) findViewById(R.id.btnFechaInicio);
        btnFfin = (ImageButton) findViewById(R.id.btnFechaFin);
        txtFinicio = (TextView) findViewById((R.id.txtFechaI));
        txtFfin = (TextView) findViewById((R.id.txtFechaF));


        swipeRefresh.setOnRefreshListener(this);
        btnFinicio.setOnClickListener(this);
        btnFfin.setOnClickListener(this);

        ArrayAdapter adaptadorKeyword2 = ArrayAdapter.createFromResource(this,R.array.modR,android.R.layout.simple_spinner_item);
        adaptadorKeyword2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMod.setAdapter(adaptadorKeyword2);
        spMod.setOnItemSelectedListener(this);
        spStatus.setOnItemSelectedListener(this);

        spTipoLogistico = findViewById(R.id.spTipoLogistico);
        layoutTipoLogistico = findViewById(R.id.layout_tipo_logistico);


        ArrayAdapter<String> tipoAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                new String[]{"RELEVO", "PREFORMA"});
        tipoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTipoLogistico.setAdapter(tipoAdapter);
        spTipoLogistico.setOnItemSelectedListener(this);

        subirInformacionRetenida();


//        if (tipo.equals("PROMOTOR R"))
//        {
//            ArrayAdapter adaptadorKeyword1 = ArrayAdapter.createFromResource(this,R.array.modR,android.R.layout.simple_spinner_item);
//            adaptadorKeyword1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//            spMod.setAdapter(adaptadorKeyword1);
//            spMod.setOnItemSelectedListener(this);
//        }else if (tipo.equals("PROMOTOR C"))
//        {
//            ArrayAdapter adaptadorKeyword1 = ArrayAdapter.createFromResource(this,R.array.modC,android.R.layout.simple_spinner_item);
//            adaptadorKeyword1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//            spMod.setAdapter(adaptadorKeyword1);
//            spMod.setOnItemSelectedListener(this);
//        }else if (tipo.equals("MERCADERISTA"))
//        {
//            ArrayAdapter adaptadorKeyword1 = ArrayAdapter.createFromResource(this,R.array.merca,android.R.layout.simple_spinner_item);
//            adaptadorKeyword1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//            spMod.setAdapter(adaptadorKeyword1);
//            spMod.setOnItemSelectedListener(this);
//        }

        recyclerView = (RecyclerView) findViewById(R.id.reciclador);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        emptyView = (TextView) findViewById(R.id.recyclerview_data_empty);
    }

    private void subirInformacionRetenida() {
        // Obtenemos la calidad de señal de red sea por Wifi o Datos
        String señal = new SignalQuality(getApplicationContext()).getSignalQuality();
        Log.i("Señal","Calidad de la señal de Internet: "+ señal);

        if (señal.equalsIgnoreCase("Buena") || señal.equalsIgnoreCase("Excelente")){
            //subir informacion retenida automaticamente
            SyncAdapter.sincronizarAhora(HistorialActivity.this, true, Constantes.SUBIR_TODO, null);
            Log.i("Noti","Se envio la informacion retenida");
        }
    }


    public void LoadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        tipo = sharedPreferences.getString(Constantes.TIPO,Constantes.NODATA);
        user = sharedPreferences.getString(Constantes.USER,Constantes.NODATA);
    }

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//        if (adapterView == spMod) {
//            basedatos = adapterView.getItemAtPosition(i).toString();
//            if (basedatos!=null) {
//                switch (basedatos) {
//                    case "PRECIOS":
//                        getPrecios();
//                        break;
//                    case "EXHIBICIONES":
//                        getExh();
//                        break;
//                    case "REGISTRO":
//                        getGps();
//                        break;
//                    case "INVENTARIO + AGOTADOS":
//                        getFlooring();
//                        break;
//                    case "PROMOCIONES":
//                        getPromocion();
//                        break;
//                    case "QUIEBRES":
//                        getValores();
//                        break;
//                    case "NOTIFICACION":
//                        getNotificacion();
//                        break;
//                    case "NUEVO PDV":
//                        getImplementacion();
//                        break;
//
//                    case "SOD":
//                        getShare();
//                        break;
//
//                    case "TIEMPO GESTION":
//                        getAgotados();
//                        break;
//
//                    case "VENTAS":
//                        getVentas();
//                        break;
//                    case "VENTA - FACTURA":
//                        getVenta();
//                        break;
//                    case "LOGROS":
//                        getExhibicionAntesDespues();
//                        break;
//                    case "TEST":
//                        getTest();
//                        break;
//                    case "ON PACKS":
//                        getOnPacks();
//                        break;
//                    case "PRODUCTOS A CADUCAR":
//                        getProdCad();
//                        break;
//                    case "IMPULSO":
//                        getImpulso();
//                        break;
//                    case "ROTACION":
//                        getRotacion();
//                        break;
//                    case "SMS":
//                        getSugeridos();
//                        break;
//                    case "TAREAS":
//                        getTareas();
//                        break;
//                    case "CANJES":
//                        getCanjes();
//                        break;
//                    case "MCI":
//                        getMCI();
//                        break;
//                    case "MATERIALES RECIBIDOS":
//                        getMaterialesRecibidos();
//                        break;
//                    case "EJECUCION DE MATERIALES":
//                        getEjecucionMateriales();
//                        break;
//                    case "PDI":
//                        getPDI();
//                        break;
//                    case "MALLA CODIFICADOS":
//                        getMalla();
//                        break;
//                    case "ANTES Y DESPUÉS":
//                        getAntesDespues();
//                        break;
//                    case "ASISTENCIAS":
//                        getAsistencias();
//                        break;
//                    case "FOTOGRAFICO PDV":
//                        getFotograficoPdv();
//                        break;
//                    case "PROPENSOS":
//                        getPropensos();
//                        break;
//                    case "PRODUCTOS M.E.":
//                        getProductosEnMalEstado();
//                        break;
//                }
//            }
////            cargarModulos(basedatos);
//        }

        if(adapterView == spMod){
            modulo = adapterView.getItemAtPosition(i).toString();
            tipo = adapterView.getItemAtPosition(i).toString();

            limpiarFiltros();


            setContractByModulo(modulo);
            hideItems(modulo, "");
            filtrarPdv();
            spStatus.setSelection(0);

            if (modulo.equals("LOGISTICO")) {
                layoutTipoLogistico.setVisibility(View.VISIBLE);
                spTipoLogistico.setSelection(0);
                tipoLogistico = "RELEVO";
            } else {
                layoutTipoLogistico.setVisibility(View.GONE);
            }
        }

        if(adapterView == spPdv){
            pdv = adapterView.getItemAtPosition(i).toString();
            filtrarCategoria(pdv);
            spStatus.setSelection(0);
        }

        if(adapterView == spCategoria){
            categoria = adapterView.getItemAtPosition(i).toString();

            if (modulo.equals("LOGISTICO") && tipoLogistico.equals("RELEVO")) {
                filtrarMarca(pdv, categoria);
            } else if (!modulo.equals("LOGISTICO")) {
                filtrarMarca(pdv, categoria);
            }

            spStatus.setSelection(0);
        }

        if (adapterView == spTipoLogistico) {
            tipoLogistico = adapterView.getItemAtPosition(i).toString();

            if (modulo.equals("LOGISTICO")) {
                if (tipoLogistico.equals("RELEVO")) {

                    llCategorias.setVisibility(View.VISIBLE);
                    llMarca.setVisibility(View.VISIBLE);


                    if (!pdv.equals("PDV")) {
                        filtrarCategoria(pdv);
                    }
                    if (!categoria.equals("CATEGORIA") && !pdv.equals("PDV")) {
                        filtrarMarca(pdv, categoria);
                    }  else if (!pdv.equals("PDV")) {

                        List<String> marcas = new ArrayList<>();
                        marcas.add("MARCA");
                        ArrayAdapter<String> marcaAdapter = new ArrayAdapter<>(this,
                                android.R.layout.simple_spinner_item, marcas);
                        spMarca.setAdapter(marcaAdapter);
                        marca = "MARCA";
                    }

                } else if (tipoLogistico.equals("PREFORMA")) {

                    llCategorias.setVisibility(View.VISIBLE);
                    llMarca.setVisibility(View.GONE);


                    marca = "MARCA";
                    List<String> marcas = new ArrayList<>();
                    marcas.add("MARCA");
                    ArrayAdapter<String> marcaAdapter = new ArrayAdapter<>(this,
                            android.R.layout.simple_spinner_item, marcas);
                    marcaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spMarca.setAdapter(marcaAdapter);

                    if (!pdv.equals("PDV")) {
                        filtrarCategoria(pdv);
                    }
                }


//                if (!pdv.equals("PDV") && !categoria.equals("CATEGORIA")) {
//                    if (camposValidados()) {
//                        filtrarHistorial(modulo);
//                    }
//                }
                if (!pdv.equals("PDV") && !categoria.equals("CATEGORIA") && !fDesde.isEmpty() && !fHasta.isEmpty()) {
                    if (tipoLogistico.equals("RELEVO")) {
                        getLogisticoRelevo();
                    } else {
                        getLogisticoPreforma();
                    }
                }
            }
        }

        if(adapterView == spMarca){
            marca = adapterView.getItemAtPosition(i).toString();
            spStatus.setSelection(0);

            if (camposValidados()) {
                filtrarHistorial(modulo);
            }
        }

        if(adapterView == spStatus) {
            status = adapterView.getItemAtPosition(i).toString();
            if(status.equals("NO ENVIADO")){
                status = "1";
            } else if(status.equals("ENVIADO")) {
                status = "0";
            } else if (status.equals("TODOS")){
                status = ".";
            }

            if(camposValidados()){
                filtrarHistorial(modulo);
            }
        }
    }



    public void filtrarHistorial(String modulo){
        Log.i("entra FA", "filtrar historia");
        switch (modulo) {
            case "PRECIOS":
                getPrecios();
                break;
            case "EXHIBICIONES":
                getExh();
                break;
            case "REGISTRO":
                getGps();
                break;
            case "INVENTARIO + AGOTADOS":
                getFlooring();
                break;
            case "PROMOCIONES":
                getPromocion();
                break;
            /*
            case "OSA":
                getValores();
                break;
            */
            case "NOTIFICACION":
                getNotificacion();
                break;
            /*
            case "NUEVO PDV":
                getImplementacion();
                break;
            */
            case "SHARE OF SHELF":
                getShare();
                break;

            case "VENTAS":
                getVentas();
                break;
            case "PROPENSOS":
                getPropensos();
                break;

            case "PRODUCTOS M.E.":
                getProductosEnMalEstado();
                break;

            case "ALMUERZO":
                getAlmuerzo();
                break;

            case "LOGISTICO":
                if (tipoLogistico.equals("RELEVO")) {
                    getLogisticoRelevo();
                } else {
                    getLogisticoPreforma();
                }
                break;

            /*
            case "TIEMPO GESTION":
                getAgotados();
                break;
            case "VENTA - FACTURA":
                getVenta();
                break;
            case "LOGROS":
                getLogros();
                break;
            case "TEST":
                getTest();
                break;
            case "ON PACKS":
                getOnPacks();
                break;
            case "PRODUCTOS A CADUCAR":
                getProdCad();
                break;
            case "IMPULSO":
                getImpulso();
                break;
            case "ROTACION":
                getRotacion();
                break;
            case "SMS":
                getSugeridos();
                break;
            case "TAREAS":
                getTareas();
                break;
            case "CANJES":
                getCanjes();
                break;
            case "MCI":
                getMCI();
                break;
            case "MATERIALES RECIBIDOS":
                getMaterialesRecibidos();
                break;
            case "EJECUCION DE MATERIALES":
                getEjecucionMateriales();
                break;
            case "PDI":
                getPDI();
                break;
            case "CODIFICADOS":
                getCodificados();
                break;
            case "ANTES Y DESPUÉS":
                getAntesDespues();
                break;
            case "TRACKING":
                getTracking();
                break;
            case "CONVENIOS":
                getConvenios();
                break;
             */
        }
    }






    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {}

    public void getPrecios() {
        adapterPrecio = new AdapterPrecios(this);
        recyclerView.setAdapter(adapterPrecio);
        // getSupportLoaderManager().initLoader(idPrecio, null,this);
        getSupportLoaderManager().restartLoader(idPrecio, null, this);
    }

    /*
    public void getFotograficoPdv() {
        adapterFotograficoPdv = new AdapterFotograficoPdv(this);
        recyclerView.setAdapter(adapterFotograficoPdv);
        getSupportLoaderManager().initLoader(idAdapterFotograficoPdv, null,this);
    }

    public void getAsistencias() {
        adapterAsistencias = new AdapterAsistencias(this);
        recyclerView.setAdapter(adapterAsistencias);
        getSupportLoaderManager().initLoader(idAsistencias, null,this);
    }*/

    public void getExh() {
        adapterExh = new AdapterExh(this);
        recyclerView.setAdapter(adapterExh);
        //  getSupportLoaderManager().initLoader(idExh, null,this);
        getSupportLoaderManager().restartLoader(idExh, null,this);
    }

    public void getGps() {
        adapterGps = new AdapterGps(this);
        recyclerView.setAdapter(adapterGps);
        //   getSupportLoaderManager().initLoader(idGps, null,this);
        getSupportLoaderManager().restartLoader(idGps, null,this);
    }

    public void getFlooring() {
        adapterFlooring= new AdapterFlooring(this);
        recyclerView.setAdapter(adapterFlooring);
        //  getSupportLoaderManager().initLoader(idFlooring,null,this);
        getSupportLoaderManager().restartLoader(idFlooring,null,this);
    }

    public void getPromocion() {
        adapterPromo= new AdapterPromo(this);
        recyclerView.setAdapter(adapterPromo);
        //  getSupportLoaderManager().initLoader(idPromo,null,this);
        getSupportLoaderManager().restartLoader(idPromo,null,this);
    }

    /*
    public void getValores()
    {
        adapterValores= new AdapterValores(this);
        recyclerView.setAdapter(adapterValores);
        getSupportLoaderManager().initLoader(idValores,null,this);
    }

    public void getImplementacion() {
        adapterImplementacion= new AdapterImplementacion(this);
        recyclerView.setAdapter(adapterImplementacion);
        getSupportLoaderManager().initLoader(idImple,null,this);
    }
*/
    public void getNotificacion() {
        adapterPDV= new AdapterPDV(this);
        recyclerView.setAdapter(adapterPDV);
        // getSupportLoaderManager().initLoader(idNotificacion,null,this);
        getSupportLoaderManager().restartLoader(idNotificacion,null,this);
    }

    public void getShare() {
        adapterShare= new AdapterShare(this);
        recyclerView.setAdapter(adapterShare);
        //   getSupportLoaderManager().initLoader(idShare,null,this);
        getSupportLoaderManager().restartLoader(idShare,null,this);
    }

    /*
       public void getAgotados() {
           adapterAgotados= new AdapterAgotados(this);
           recyclerView.setAdapter(adapterAgotados);
           getSupportLoaderManager().initLoader(idAgotados,null,this);
       }

       public void getVenta() {
           adapterVenta = new AdapterVenta(this);
           recyclerView.setAdapter(adapterVenta);
           getSupportLoaderManager().initLoader(idVenta, null,this);
       }
   */
    public void getVentas() {
        adapterVentas = new AdapterVentas(this);
        recyclerView.setAdapter(adapterVentas);
        //  getSupportLoaderManager().initLoader(idVentas, null,this);
        getSupportLoaderManager().restartLoader(idVentas, null,this);
    }
    /*
        public void getExhibicionAntesDespues() {
            adapterFotografico = new AdapterFotografico(this);
            recyclerView.setAdapter(adapterFotografico);
            getSupportLoaderManager().initLoader(idExhAntDes, null,this);
        }

        public void getTest() {
            adapterPreguntas = new AdapterPreguntas(this);
            recyclerView.setAdapter(adapterPreguntas);
            getSupportLoaderManager().initLoader(idPreg, null,this);
        }

        public void getOnPacks() {
            adapterPacks = new AdapterPacks(this);
            recyclerView.setAdapter(adapterPacks);
            getSupportLoaderManager().initLoader(idPacks, null,this);
        }

        public void getProdCad() {
            adapterProdCad = new AdapterProdCad(this);
            recyclerView.setAdapter(adapterProdCad);
            getSupportLoaderManager().initLoader(idProdCad, null,this);
        }

        public void getImpulso() {
            adapterImpulso = new AdapterImpulso(this);
            recyclerView.setAdapter(adapterImpulso);
            getSupportLoaderManager().initLoader(idImpul, null,this);
        }
        public void getRotacion() {
            adapterRotacion = new AdapterRotacion(this);
            recyclerView.setAdapter(adapterRotacion);
            getSupportLoaderManager().initLoader(idRotacion, null,this);
        }

        public void getSugeridos() {
            adapterSugeridos = new SugeridosAdapter(this);
            recyclerView.setAdapter(adapterSugeridos);
            getSupportLoaderManager().initLoader(idSugeridos, null,this);
        }

        public void getTareas() {
            adapterTareas = new AdapterTareas(this);
            recyclerView.setAdapter(adapterTareas);
            getSupportLoaderManager().initLoader(idTareas, null,this);
        }

        public void getCanjes() {
            adapterCanjes = new AdapterCanjes(this);
            recyclerView.setAdapter(adapterCanjes);
            getSupportLoaderManager().initLoader(idCanjes, null,this);
        }

        public void getMCI() {
            adapterMCI = new AdapterMCI(this);
            recyclerView.setAdapter(adapterMCI);
            getSupportLoaderManager().initLoader(idMCI, null,this);
        }

        public void getMaterialesRecibidos() {
            adapterMaterialesRecibidos = new AdapterMaterialesRecibidos(this);
            recyclerView.setAdapter(adapterMaterialesRecibidos);
            getSupportLoaderManager().initLoader(idMaterialesRecibidos, null,this);
        }

        public void getEjecucionMateriales() {
            adapterEjecucionMateriales = new AdapterEjecucionMateriales(this);
            recyclerView.setAdapter(adapterEjecucionMateriales);
            getSupportLoaderManager().initLoader(idEjecMateriales, null,this);
        }

        public void getPDI() {
            adapterPDI = new AdapterPDI(this);
            recyclerView.setAdapter(adapterPDI);
            getSupportLoaderManager().initLoader(idPDI, null,this);
        }

        public void getMalla() {
            adapterMallaCodificados = new AdapterMallaCodificados(this);
            recyclerView.setAdapter(adapterMallaCodificados);
            getSupportLoaderManager().initLoader(idMallaCodificados, null,this);
        }

        public void getAntesDespues() {
            adapterEvidencias = new AdapterEvidencias(this);
            recyclerView.setAdapter(adapterEvidencias);
            getSupportLoaderManager().initLoader(idEvidencias, null,this);
        }
    */
    public void getPropensos() {
        adapterPropensos = new AdapterPropensos(this);
        recyclerView.setAdapter(adapterPropensos);
        // getSupportLoaderManager().initLoader(idPropensos, null,this);
        getSupportLoaderManager().restartLoader(idPropensos, null,this);
    }

    public void getProductosEnMalEstado() {
        adapterProductosEnMalEstado = new AdapterProductosEnMalEstado(this);
        recyclerView.setAdapter(adapterProductosEnMalEstado);
        // getSupportLoaderManager().initLoader(idProductosEnMalEstado, null,this);
        getSupportLoaderManager().restartLoader(idProductosEnMalEstado, null,this);
    }

    public void getAlmuerzo() {
        adapterAlmuerzo = new AdapterAlmuerzo(this);
        recyclerView.setAdapter(adapterAlmuerzo);
        // getSupportLoaderManager().initLoader(idProductosEnMalEstado, null,this);
        getSupportLoaderManager().restartLoader(idAlmuerzo, null,this);
    }

    public void getLogisticoRelevo() {
        adapterLogisticoRelevo = new AdapterLogisticoRelevo(this);
        recyclerView.setAdapter(adapterLogisticoRelevo);
        // getSupportLoaderManager().initLoader(idAlmuerzo, null,this);
        getSupportLoaderManager().restartLoader(idLogisticoRelevo, null,this);
    }

    public void getLogisticoPreforma() {
        adapterLogisticoPreforma = new AdapterLogisticoPreforma(this);
        recyclerView.setAdapter(adapterLogisticoPreforma);
        // getSupportLoaderManager().initLoader(idAlmuerzo, null,this);
        getSupportLoaderManager().restartLoader(idLogisticoPreforma, null,this);
    }

    public void setContractByModulo(String modulo){
        //aqui se colocan los filtros que aplican para cada modulo y las respectivas tablas y columnas del contract
        switch (modulo) {
            case "PRECIOS":
                Log.i("Entra HA case", "precios");
                tablaInsert = ContractInsertPrecios.INSERT_PRECIOS;
                columnaPdv = ContractInsertPrecios.Columnas.POS_NAME;
                columnaCategoria = ContractInsertPrecios.Columnas.CATEGORIA;
                columnaMarca = ContractInsertPrecios.Columnas.BRAND;
                campo = "";
                break;
            case "SHARE OF SHELF":
                tablaInsert = ContractInsertShare.INSERT_SHARE;
                columnaPdv = ContractInsertShare.Columnas.POS_NAME;
                columnaCategoria = ContractInsertShare.Columnas.SECTOR;
                columnaMarca = ContractInsertShare.Columnas.BRAND;
                campo = "";
                break;
            case "EXHIBICIONES":
                tablaInsert = ContractInsertExh.INSERT_EXH;
                columnaPdv = ContractInsertExh.Columnas.POS_NAME;
                columnaCategoria = ContractInsertExh.Columnas.CATEGORIA;
                columnaMarca = ContractInsertExh.Columnas.BRAND;
                campo = "";
                break;
            case "PROMOCIONES":
                tablaInsert = ContractInsertPromocion.INSERT_PROMO;
                columnaPdv = ContractInsertPromocion.Columnas.POS_NAME;
                columnaCategoria = ContractInsertPromocion.Columnas.CATEGORIA;
                //    columnaMarca = ContractInsertPromocion.Columnas.MARCA;
                campo = "";
                break;
            case "INVENTARIO + AGOTADOS":
                tablaInsert = InsertFlooring.INSERT_FLOORING;
                columnaPdv = InsertFlooring.Columnas.POS_NAME;
                columnaCategoria = InsertFlooring.Columnas.CATEGORIA;
                columnaMarca = InsertFlooring.Columnas.BRAND;
                campo = "";
                break;
            case "VENTAS":
                tablaInsert = ContractInsertVentas.INSERT_VENTAS;
                columnaPdv = ContractInsertVentas.Columnas.POS_NAME;
                columnaCategoria = ContractInsertVentas.Columnas.CATEGORIA;
                columnaMarca = ContractInsertVentas.Columnas.MARCA;
                campo = "";
                break;
            case "PROPENSOS":
                tablaInsert = ContractInsertPropensosYProdMalEst.INSERT_PROP_MAL_EST;
                columnaPdv = ContractInsertPropensosYProdMalEst.Columnas.SUPERVISOR; // PDV
                columnaCategoria = ContractInsertPropensosYProdMalEst.Columnas.CATEGORIA;
                columnaMarca = ContractInsertPropensosYProdMalEst.Columnas.BRAND;
                campo = "PROPENSOS";
                break;
            case "PRODUCTOS M.E.":
                tablaInsert = ContractInsertPropensosYProdMalEst.INSERT_PROP_MAL_EST;
                columnaPdv = ContractInsertPropensosYProdMalEst.Columnas.SUPERVISOR; // PDV
                columnaCategoria = ContractInsertPropensosYProdMalEst.Columnas.CATEGORIA;
                columnaMarca = ContractInsertPropensosYProdMalEst.Columnas.BRAND;
                campo = "PRODUCTOS EN MAL ESTADO";
                break;
            case "REGISTRO":
                tablaInsert = ContractInsertGps.INSERT_GPS;
                columnaPdv = ContractInsertGps.Columnas.POS_NAME;
                columnaCategoria = "";
                columnaMarca = "";
                campo = "";
                break;
            case "ALMUERZO":
                tablaInsert = ContractInsertAlmuerzo.INSERT_ALMUERZO;
                columnaPdv = "";
                columnaCategoria = "";
                columnaMarca = "";
                campo = "";
                break;
            case "NOTIFICACION": //no
                tablaInsert = ContractNotificacion.NOTIFICACION;
                campo = "";
                break;
            /*
            case "NUEVO PDV":
                tablaInsert = ContractInsertImplementacion.INSERT_IMPLEM;
                columnaPdv = ContractInsertImplementacion.Columnas.PDV;
                break;
            */

            case "LOGISTICO":
                tablaInsert = ContractInsertLogisticoRelevo.INSERT_LOGISTICO_RELEVO;
                columnaPdv = ContractInsertLogisticoRelevo.Columnas.SUPERVISOR;
                columnaCategoria = ContractInsertLogisticoRelevo.Columnas.CATEGORIA;
                columnaMarca = ContractInsertLogisticoRelevo.Columnas.BRAND;
                campo = "";
                // NOTA: El layout_tipo_logistico ya se maneja en onItemSelected
                break;

            default:
                tablaInsert = "";
                columnaPdv = "";
                columnaCategoria = "";
                columnaMarca = "";
                campo = "";
        }
    }





    public void hideItems(String modulo, String tipo){

        if (modulo.equalsIgnoreCase("LOGISTICO")) {
            // Mostrar PDV siempre
            llPdv.setVisibility(View.VISIBLE);

            // La categoría siempre visible para LOGISTICO
            llCategorias.setVisibility(View.VISIBLE);

            // Marca visible solo para RELEVO
            if (tipoLogistico.equals("RELEVO")) {
                llMarca.setVisibility(View.VISIBLE);
            } else {
                llMarca.setVisibility(View.GONE);
            }
        } else {

            if(modulo.equalsIgnoreCase("REGISTRO")){
                llCategorias.setVisibility(View.GONE);
                llPdv.setVisibility(View.VISIBLE);
                llMarca.setVisibility(View.GONE);
            }else if (modulo.equalsIgnoreCase("NUEVO PDV")){
                llCategorias.setVisibility(View.GONE);
                llPdv.setVisibility(View.GONE);
                llMarca.setVisibility(View.GONE);
            }else if (modulo.equalsIgnoreCase("PROMOCIONES")){
                llCategorias.setVisibility(View.VISIBLE);
                llCategoria.setGravity(Gravity.CENTER);
                llPdv.setVisibility(View.VISIBLE);
                llMarca.setVisibility(View.GONE);
            } else if (modulo.equalsIgnoreCase("ALMUERZO")){
                llPdv.setVisibility(View.GONE);
                llCategorias.setVisibility(View.GONE);
                llMarca.setVisibility(View.GONE);
            } else {
                llCategorias.setVisibility(View.VISIBLE);
                llPdv.setVisibility(View.VISIBLE);
                llMarca.setVisibility(View.VISIBLE);
            }
        }
    }

    public boolean camposValidados(){

        if (!modulo.equalsIgnoreCase("MÓDULO")){
            if(fDesde.isEmpty() || fHasta.isEmpty()) {
                Toast.makeText(this, "Elija fecha inicio y fin", Toast.LENGTH_SHORT).show();
                spStatus.setSelection(0);
                return false;
            }
        }

        if(pdv.equalsIgnoreCase("PDV") && !modulo.equalsIgnoreCase("ALMUERZO")){
            Toast.makeText(this, "Elija un PDV", Toast.LENGTH_SHORT).show();
            spStatus.setSelection(0);
            return false;
        }

        if (!modulo.equalsIgnoreCase("REGISTRO") && !modulo.equalsIgnoreCase("NUEVO PDV") &&
                !modulo.equalsIgnoreCase("ALMUERZO") ){
            if (modulo.equalsIgnoreCase("LOGISTICO")) {
                if (categoria.equalsIgnoreCase("CATEGORIA")) {
                    Toast.makeText(this, "Elija una categoría", Toast.LENGTH_SHORT).show();
                    spStatus.setSelection(0);
                    return false;
                }

                // Validar marca solo si es RELEVO
                if (tipoLogistico.equals("RELEVO") && marca.equalsIgnoreCase("MARCA")) {
                    Toast.makeText(this, "Elija una marca", Toast.LENGTH_SHORT).show();
                    spStatus.setSelection(0);
                    return false;
                }

            } else {
                if(categoria.equalsIgnoreCase("CATEGORIA")){
                    Toast.makeText(this, "Elija una categoría", Toast.LENGTH_SHORT).show();
                    spStatus.setSelection(0);
                    return false;
                }
            }
        }

        if (!modulo.equalsIgnoreCase("REGISTRO") && !modulo.equalsIgnoreCase("NUEVO PDV") &&
                !modulo.equalsIgnoreCase("PROMOCIONES") && !modulo.equalsIgnoreCase("ALMUERZO") &&
                !modulo.equalsIgnoreCase("LOGISTICO")){

            if(marca.equalsIgnoreCase("MARCA")){
                Toast.makeText(this, "Elija una marca", Toast.LENGTH_SHORT).show();
                spStatus.setSelection(0);
                return false;
            }

        }

        return true;
    }



    public void filtrarPdv(){

        List<String> operadores = handler.filtrarPdvsHistorial(tablaInsert, columnaPdv,campo);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, operadores);
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPdv.setAdapter(dataAdapter);
        spPdv.setOnItemSelectedListener(this);

    }
    public void filtrarCategoria(String pdv) {
        List<String> operadores = handler.filtrarCategoriasHistorial(tablaInsert, columnaPdv, columnaCategoria, pdv, campo, tipoLogistico);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, operadores);
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategoria.setAdapter(dataAdapter);
        spCategoria.setOnItemSelectedListener(this);
    }

    public void filtrarMarca(String pdv, String categoria) {
        // Si es LOGISTICO y PREFORMA, no mostrar marcas reales
        if (modulo.equals("LOGISTICO") && tipoLogistico.equals("PREFORMA")) {
            List<String> marcas = new ArrayList<>();
            marcas.add("MARCA");

            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, marcas);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spMarca.setAdapter(dataAdapter);
            spMarca.setOnItemSelectedListener(this);
            return;
        }
        List<String> operadores = handler.filtrarMarcaHistorial(tablaInsert, columnaPdv, columnaCategoria, columnaMarca, pdv, categoria, campo, tipoLogistico);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, operadores);
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMarca.setAdapter(dataAdapter);
        spMarca.setOnItemSelectedListener(this);
    }




    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        emptyView.setText("Cargando datos...");
        CursorLoader loader = null;
        // Consultar todos los registros
        if (id==idPrecio) {
            String selection = columnaPdv + "=? AND " + columnaCategoria + "=? AND " +
                    columnaMarca + "=? AND " + Constantes.PENDIENTE_INSERCION + " REGEXP ? AND " +
                    "fecha BETWEEN ? AND ?; ";
            String[] selectArgs = {pdv, categoria, marca, status, fDesde, fHasta};
            loader= new CursorLoader(
                    this,
                    ContractInsertPrecios.CONTENT_URI,
                    null, selection, selectArgs, null);
        }else
        if (id==idExh) {
            String selection = columnaPdv + "=? AND " + columnaCategoria + "=? AND " +
                    columnaMarca + "=? AND " + Constantes.PENDIENTE_INSERCION + " REGEXP ? AND " +
                    "fecha BETWEEN ? AND ?; ";
            String[] selectArgs = {pdv, categoria, marca, status, fDesde, fHasta};
            loader= new CursorLoader(
                    this,
                    ContractInsertExh.CONTENT_URI,
                    null, selection, selectArgs, null);
        }else
        if (id==idGps) {
            String selection = columnaPdv + "=? AND " + Constantes.PENDIENTE_INSERCION + " REGEXP ? AND " +
                    "fecha BETWEEN ? AND ?; ";
            String[] selectArgs = {pdv, status, fDesde, fHasta};
            loader = new CursorLoader(
                    this,
                    ContractInsertGps.CONTENT_URI,
                    null, selection, selectArgs, null);
        }else
        if (id==idFlooring)
        {

            String selection = columnaPdv + "=? AND " + columnaCategoria + "=? AND " +
                    columnaMarca + "=? AND " + Constantes.PENDIENTE_INSERCION + " REGEXP ? AND " +
                    "fecha BETWEEN ? AND ?; ";
            String[] selectArgs = {pdv, categoria, marca, status, fDesde, fHasta};
            loader=new CursorLoader(
                    this,
                    InsertFlooring.CONTENT_URI,
                    null,selection,selectArgs,null);


        }else
        if (id==idPromo) {
            String selection = columnaPdv + "=? AND " + columnaCategoria + "=? AND " +
                    Constantes.PENDIENTE_INSERCION + " REGEXP ? AND "+
                    "fecha BETWEEN ? AND ?; ";
            String[] selectArgs = {pdv, categoria, status, fDesde, fHasta};
            loader=new CursorLoader(
                    this,
                    ContractInsertPromocion.CONTENT_URI,
                    null,selection,selectArgs,null);
        }else
        if (id==idValores) {
            loader=new CursorLoader(
                    this,
                    ContractInsertValores.CONTENT_URI,
                    null,null,null,null);
        }else
        if (id==idImple) {
            loader=new CursorLoader(
                    this,
                    ContractInsertImplementacion.CONTENT_URI,
                    null,null,null,null);
        }else
        if (id==idNotificacion) {
            String selection = Constantes.PENDIENTE_INSERCION + " REGEXP ? AND "+
                    "fecha BETWEEN ? AND ?; ";
            String[] selectArgs = {status, fDesde, fHasta};
            loader=new CursorLoader(
                    this,
                    ContractNotificacion.CONTENT_URI,
                    null,selection,selectArgs,null);
        }else
        if (id==idShare) {
            String selection = columnaPdv + "=? AND " + columnaCategoria + "=? AND " +
                    columnaMarca + "=? AND " + Constantes.PENDIENTE_INSERCION + " REGEXP ? AND " +
                    "fecha BETWEEN ? AND ?; ";
            String[] selectArgs = {pdv, categoria, marca, status, fDesde, fHasta};
            loader=new CursorLoader(
                    this,
                    ContractInsertShare.CONTENT_URI,
                    null,selection,selectArgs,null);
        }else
        if (id==idAgotados) {
            loader=new CursorLoader(
                    this,
                    ContractInsertAgotados.CONTENT_URI,
                    null,null,null,null);
        }else
        if (id==idVenta) {
            loader=new CursorLoader(
                    this,
                    ContractInsertVenta.CONTENT_URI,
                    null,null,null,null);
        }else
        if (id==idExhAntDes) {
            loader=new CursorLoader(
                    this,
                    ContractInsertFotografico.CONTENT_URI,
                    null,null,null,null);
        }else
        if (id==idPreg) {
            loader=new CursorLoader(
                    this,
                    ContractInsertPreguntas.CONTENT_URI,
                    null,null,null,null);
        }else
        if (id==idPacks) {
            loader=new CursorLoader(
                    this,
                    ContractInsertPacks.CONTENT_URI,
                    null,null,null,null);
        }else
        if (id==idProdCad) {
            loader=new CursorLoader(
                    this,
                    ContractInsertProdCaducar.CONTENT_URI,
                    null,null,null,null);
        }else
        if (id==idImpul) {
            loader=new CursorLoader(
                    this,
                    ContractInsertImpulso.CONTENT_URI,
                    null,null,null,null);
        }else
        if (id==idRotacion) {
            loader=new CursorLoader(
                    this,
                    ContractInsertRotacion.CONTENT_URI,
                    null,null,null,null);
        }else
        if (id==idSugeridos) {
            loader=new CursorLoader(
                    this,
                    ContractInsertSugeridos.CONTENT_URI,
                    null,null,null,null);
        }else
        if (id==idTareas) {
            loader=new CursorLoader(
                    this,
                    ContractInsertTareas.CONTENT_URI,
                    null,null,null,null);
        }else
        if (id==idCanjes) {
            loader=new CursorLoader(
                    this,
                    ContractInsertCanjes.CONTENT_URI,
                    null,null,null,null);
        }else
        if (id==idMCI) {
            loader=new CursorLoader(
                    this,
                    ContractInsertMCIPdv.CONTENT_URI,
                    null,null,null,null);
        }else
        if (id==idMaterialesRecibidos) {
            loader=new CursorLoader(
                    this,
                    ContractInsertMaterialesRecibidos.CONTENT_URI,
                    null,null,null,null);
        }else
        if (id==idEjecMateriales) {
            loader=new CursorLoader(
                    this,
                    ContractInsertEjecucionMateriales.CONTENT_URI,
                    null,null,null,null);
        }else
        if (id==idPDI) {
            loader=new CursorLoader(
                    this,
                    ContractInsertPDI.CONTENT_URI,
                    null,null,null,null);
        }else
        if (id==idMallaCodificados) {
            loader=new CursorLoader(
                    this,
                    ContractInsertMallaCodificados.CONTENT_URI,
                    null,null,null,null);
        }else
        if (id==idEvidencias) {
            loader=new CursorLoader(
                    this,
                    ContractInsertEvidencias.CONTENT_URI,
                    null,null,null,null);
        }else
        if (id==idAsistencias) {
            loader=new CursorLoader(
                    this,
                    ContractInsertAsistencia.CONTENT_URI,
                    null,null,null,null);
        }else
        if (id==idAdapterFotograficoPdv) {
            loader=new CursorLoader(
                    this,
                    ContractInsertPdvFotografico.CONTENT_URI,
                    null,null,null,null);
        }else
        if (id==idVentas) {
            String selection = columnaPdv + "=? AND " + columnaCategoria + "=? AND " +
                    columnaMarca + "=? AND " + Constantes.PENDIENTE_INSERCION + " REGEXP ? AND " +
                    "fecha BETWEEN ? AND ?; ";
            String[] selectArgs = {pdv, categoria, marca, status, fDesde, fHasta};
            loader=new CursorLoader(
                    this,
                    ContractInsertVentas.CONTENT_URI,
                    null,selection,selectArgs,null);
        }else
        if (id==idPropensos) {
            String selection = columnaPdv + "=? AND " + columnaCategoria + "=? AND " +
                    columnaMarca + "=? AND " + Constantes.PENDIENTE_INSERCION + " REGEXP ? AND " +
                    "fecha BETWEEN ? AND ? AND modulo = 'PROPENSOS'; ";
            String[] selectArgs = {pdv, categoria, marca, status, fDesde, fHasta};
            loader=new CursorLoader(
                    this,
                    ContractInsertPropensosYProdMalEst.CONTENT_URI,
                    null,selection,selectArgs,null);
        } else
        if (id==idProductosEnMalEstado) {
            String selection = columnaPdv + "=? AND " + columnaCategoria + "=? AND " +
                    columnaMarca + "=? AND " + Constantes.PENDIENTE_INSERCION + " REGEXP ? AND " +
                    "fecha BETWEEN ? AND ? AND modulo = 'PRODUCTOS EN MAL ESTADO'; ";
            String[] selectArgs = {pdv, categoria, marca, status, fDesde, fHasta};
            loader=new CursorLoader(
                    this,
                    ContractInsertPropensosYProdMalEst.CONTENT_URI,
                    null,selection,selectArgs,null);
        } else
        if (id==idAlmuerzo) {
            String selection = Constantes.PENDIENTE_INSERCION + " REGEXP ? AND " +
                    "fecha BETWEEN ? AND ?; ";
            String[] selectArgs = {status, fDesde, fHasta};

            loader=new CursorLoader(
                    this,
                    ContractInsertAlmuerzo.CONTENT_URI,
                    null,selection,selectArgs,null);
        } else
//
//
//        if (id == idLogisticoRelevo) {
//            String selection;
//            String[] selectArgs;
//
//            if (tipoLogistico.equals("RELEVO")) {
//                // Para RELEVO: filtrar por pdv, categoria, marca, status, fechas y tipo_logistico
//                selection = columnaPdv + "=? AND " + columnaCategoria + "=? AND " +
//                        columnaMarca + "=? AND " + Constantes.PENDIENTE_INSERCION + " REGEXP ? AND " +
//                        "fecha BETWEEN ? AND ? AND tipo_logistico = ?";
//                selectArgs = new String[]{pdv, categoria, marca, status, fDesde, fHasta, tipoLogistico};
//            } else {
//                // Para PREFORMA: NO incluir marca en el filtro
//                selection = columnaPdv + "=? AND " + columnaCategoria + "=? AND " +
//                        Constantes.PENDIENTE_INSERCION + " REGEXP ? AND " +
//                        "fecha BETWEEN ? AND ? AND tipo_logistico = ?";
//                selectArgs = new String[]{pdv, categoria, status, fDesde, fHasta, tipoLogistico};
//            }
//
//            loader = new CursorLoader(
//                    this,
//                    ContractInsertLogisticoRelevo.CONTENT_URI,
//                    null, selection, selectArgs, null);
//        }


            if (id==idLogisticoRelevo) {
                if (tipoLogistico.equals("RELEVO")) {
                    String selection = columnaPdv + "=? AND " + columnaCategoria + "=? AND " +
                            columnaMarca + "=? AND " + Constantes.PENDIENTE_INSERCION + " REGEXP ? AND " +
                            "fecha BETWEEN ? AND ? AND tipo_logistico = ?; ";
                    String[] selectArgs = {pdv, categoria, marca, status, fDesde, fHasta, "RELEVO"};
                    loader = new CursorLoader(
                            this,
                            ContractInsertLogisticoRelevo.CONTENT_URI,
                            null, selection, selectArgs, null);
                }
            }
            else
            if (id==idLogisticoPreforma){
                if (tipoLogistico.equals("PREFORMA")) {
                    String selection = columnaPdv + "=? AND " + columnaCategoria + "=? AND " +
                            Constantes.PENDIENTE_INSERCION + " REGEXP ? AND " +
                            "fecha BETWEEN ? AND ? AND tipo_logistico = ?";
                    String[] selectArgs = new String[]{pdv, categoria, status, fDesde, fHasta, "PREFORMA"};
                    loader = new CursorLoader(
                            this,
                            ContractInsertLogisticoRelevo.CONTENT_URI,
                            null, selection, selectArgs, null);
                }
            }

        return loader ;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        Log.i("onLoadFinished",columnaPdv + " colCat: " + columnaCategoria + "colMar " + columnaMarca + "vars: " + pdv +","+categoria+","+marca+","+status);
        data.moveToFirst(); // Mueve al primer registro
        while (!data.isAfterLast()) {
            // Obtén el valor de la columna 'columna_nombre'
            String columnaValor = data.getString(5);
            String columnaValor2 = data.getString(6);
            Log.i("data onloadfinished","Fecha: " + columnaValor+" hora: "+columnaValor2+"");

            data.moveToNext(); // Mueve al siguiente registro
        }



        switch (loader.getId()) {
            case 0:
                if (loader!=null && adapterPrecio != null) {
                    Log.d("TAG" , "||onLoadFinished called||");
                    adapterPrecio.swapCursor(data);
                    emptyView.setText("");
                }
            case 1:
                if (loader != null && adapterExh != null) {
                    Log.d("TAG" , "||onLoadFinished called||");
                    adapterExh.swapCursor(data);
                    emptyView.setText("");
                }
            case 2:
                if (loader != null && adapterGps != null) {
                    Log.d("TAG" , "||onLoadFinished called||");
                    adapterGps.swapCursor(data);
                    emptyView.setText("");
                }
            case 3:
                if (loader != null && adapterFlooring != null) {
                    Log.d("TAG" , "||onLoadFinished called||");
                    adapterFlooring.swapCursor(data);
                    emptyView.setText("");
                }
            case 4:
                if (loader != null && adapterPromo != null) {
                    Log.d("TAG" , "||onLoadFinished called||");
                    adapterPromo.swapCursor(data);
                    emptyView.setText("");
                }
            case 5:
                if (loader != null && adapterValores != null) {
                    Log.d("TAG" , "||onLoadFinished called||");
                    adapterValores.swapCursor(data);
                    emptyView.setText("");
                }
            case 6:
                if (loader != null && adapterImplementacion != null) {
                    Log.d("TAG" , "||onLoadFinished called||");
                    adapterImplementacion.swapCursor(data);
                    emptyView.setText("");
                }
            case 7:
                if (loader != null && adapterPDV != null) {
                    Log.d("TAG" , "||onLoadFinished called||");
                    adapterPDV.swapCursor(data);
                    emptyView.setText("");
                }
            case 8:
                if (loader != null && adapterShare != null) {
                    Log.d("TAG" , "||onLoadFinished called||");
                    adapterShare.swapCursor(data);
                    emptyView.setText("");
                }
            case 9:
                if (loader != null && adapterAgotados != null) {
                    Log.d("TAG" , "||onLoadFinished called||");
                    adapterAgotados.swapCursor(data);
                    emptyView.setText("");
                }
            case 10:
                if (loader != null && adapterVenta != null) {
                    Log.d("TAG" , "||onLoadFinished called||");
                    adapterVenta.swapCursor(data);
                    emptyView.setText("");
                }
            case 11:
                if (loader != null && adapterFotografico != null) {
                    Log.d("TAG" , "||onLoadFinished called||");
                    adapterFotografico.swapCursor(data);
                    emptyView.setText("");
                }
            case 12:
                if (loader != null && adapterPreguntas != null) {
                    Log.d("TAG" , "||onLoadFinished called||");
                    adapterPreguntas.swapCursor(data);
                    emptyView.setText("");
                }
            case 13:
                if (loader != null && adapterPacks != null) {
                    Log.d("TAG" , "||onLoadFinished called||");
                    adapterPacks.swapCursor(data);
                    emptyView.setText("");
                }
            case 14:
                if (loader != null && adapterProdCad != null) {
                    Log.d("TAG" , "||onLoadFinished called||");
                    adapterProdCad.swapCursor(data);
                    emptyView.setText("");
                }
            case 15:
                if (loader != null && adapterImpulso != null) {
                    Log.d("TAG" , "||onLoadFinished called||");
                    adapterImpulso.swapCursor(data);
                    emptyView.setText("");
                }
            case 16:
                if (loader != null && adapterRotacion != null) {
                    Log.d("TAG" , "||onLoadFinished called||");
                    adapterRotacion.swapCursor(data);
                    emptyView.setText("");
                }
            case 17:
                if (loader != null && adapterSugeridos != null) {
                    Log.d("TAG" , "||onLoadFinished called||");
                    adapterSugeridos.swapCursor(data);
                    emptyView.setText("");
                }
            case 18:
                if (loader != null && adapterTareas != null) {
                    Log.d("TAG" , "||onLoadFinished called||");
                    adapterTareas.swapCursor(data);
                    emptyView.setText("");
                }
            case 19:
                if (loader != null && adapterCanjes != null) {
                    Log.d("TAG" , "||onLoadFinished called||");
                    adapterCanjes.swapCursor(data);
                    emptyView.setText("");
                }
            case 20:
                if (loader != null && adapterMCI != null) {
                    Log.d("TAG" , "||onLoadFinished called||");
                    adapterMCI.swapCursor(data);
                    emptyView.setText("");
                }
            case 21:
                if (loader != null && adapterMaterialesRecibidos != null) {
                    Log.d("TAG" , "||onLoadFinished called||");
                    adapterMaterialesRecibidos.swapCursor(data);
                    emptyView.setText("");
                }
            case 22:
                if (loader != null && adapterEjecucionMateriales != null) {
                    Log.d("TAG" , "||onLoadFinished called||");
                    adapterEjecucionMateriales.swapCursor(data);
                    emptyView.setText("");
                }
            case 23:
                if (loader != null && adapterPDI != null) {
                    Log.d("TAG" , "||onLoadFinished called||");
                    adapterPDI.swapCursor(data);
                    emptyView.setText("");
                }
            case 24:
                if (loader != null && adapterMallaCodificados != null) {
                    Log.d("TAG" , "||onLoadFinished called||");
                    adapterMallaCodificados.swapCursor(data);
                    emptyView.setText("");
                }
            case 25:
                if (loader != null && adapterEvidencias != null) {
                    Log.d("TAG" , "||onLoadFinished called||");
                    adapterEvidencias.swapCursor(data);
                    emptyView.setText("");
                }
            case 26:
                if (loader != null && adapterAsistencias != null) {
                    Log.d("TAG" , "||onLoadFinished called||");
                    adapterAsistencias.swapCursor(data);
                    emptyView.setText("");
                }
            case 27:
                if (loader != null && adapterFotograficoPdv != null) {
                    Log.d("TAG" , "||onLoadFinished called||");
                    adapterFotograficoPdv.swapCursor(data);
                    emptyView.setText("");
                }

            case 28:
                if (loader != null && adapterVentas != null) {
                    Log.d("TAG" , "||onLoadFinished called||");
                    adapterVentas.swapCursor(data);
                    emptyView.setText("");
                }

            case 29:
                if (loader != null && adapterPropensos != null) {
                    Log.d("TAG" , "||onLoadFinished called||");
                    adapterPropensos.swapCursor(data);
                    emptyView.setText("");
                }

            case 30:
                if (loader != null && adapterProductosEnMalEstado != null) {
                    Log.d("TAG" , "||onLoadFinished called||");
                    adapterProductosEnMalEstado.swapCursor(data);
                    emptyView.setText("");
                }

            case 31:
                if (loader != null && adapterAlmuerzo != null) {
                    Log.d("TAG" , "||onLoadFinished called||");
                    adapterAlmuerzo.swapCursor(data);
                    emptyView.setText("");
                }

            case 32:
                if (loader != null && adapterLogisticoRelevo != null) {
                    Log.d("TAG" , "||onLoadFinished called||");
                    adapterLogisticoRelevo.swapCursor(data);
                    emptyView.setText("");
                }
            case 33:
                if (loader != null && adapterLogisticoPreforma != null) {
                    Log.d("TAG" , "||onLoadFinished called||");
                    adapterLogisticoPreforma.swapCursor(data);
                    emptyView.setText("");
                }

        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // adapterPrecio.swapCursor(null);

    }

    public void vaciarHistorial() {
        try{
            //Reportes: Eliminar todos los registros que han sido enviados (Pendienteinsercion=0)
            String selection =  Constantes.PENDIENTE_INSERCION + "=?";
            String[] selectionArgs = new String[]{"0"};
            getContentResolver().delete(ContractInsertPrecios.CONTENT_URI,selection,selectionArgs);
            getContentResolver().delete(ContractInsertExh.CONTENT_URI,selection,selectionArgs);
            getContentResolver().delete(ContractInsertGps.CONTENT_URI,selection,selectionArgs);
            getContentResolver().delete(InsertFlooring.CONTENT_URI,selection,selectionArgs);
            getContentResolver().delete(ContractInsertPromocion.CONTENT_URI,selection,selectionArgs);
            getContentResolver().delete(ContractInsertValores.CONTENT_URI,selection,selectionArgs);
            getContentResolver().delete(ContractInsertImplementacion.CONTENT_URI,selection,selectionArgs);
            getContentResolver().delete(ContractNotificacion.CONTENT_URI,selection,selectionArgs);
            getContentResolver().delete(ContractInsertShare.CONTENT_URI,selection,selectionArgs);
            getContentResolver().delete(ContractInsertAgotados.CONTENT_URI,selection,selectionArgs);
            getContentResolver().delete(ContractInsertVenta.CONTENT_URI,selection,selectionArgs);
            getContentResolver().delete(ContractInsertFotografico.CONTENT_URI,selection,selectionArgs);
            getContentResolver().delete(ContractInsertPreguntas.CONTENT_URI,selection,selectionArgs);
            getContentResolver().delete(ContractInsertProdCaducar.CONTENT_URI,selection,selectionArgs);
            getContentResolver().delete(ContractInsertPacks.CONTENT_URI,selection,selectionArgs);
            getContentResolver().delete(ContractInsertImpulso.CONTENT_URI,selection,selectionArgs);
            getContentResolver().delete(ContractInsertRotacion.CONTENT_URI,selection,selectionArgs);
            getContentResolver().delete(ContractInsertCanjes.CONTENT_URI,selection,selectionArgs);
            getContentResolver().delete(ContractInsertMCIPdv.CONTENT_URI,selection,selectionArgs);
            getContentResolver().delete(ContractInsertMaterialesRecibidos.CONTENT_URI,selection,selectionArgs);
            getContentResolver().delete(ContractInsertEjecucionMateriales.CONTENT_URI,selection,selectionArgs);
            getContentResolver().delete(ContractInsertPDI.CONTENT_URI,selection,selectionArgs);
            getContentResolver().delete(ContractInsertMallaCodificados.CONTENT_URI,selection,selectionArgs);
            getContentResolver().delete(ContractInsertEvidencias.CONTENT_URI,selection,selectionArgs);
            getContentResolver().delete(ContractInsertAsistencia.CONTENT_URI,selection,selectionArgs);
            getContentResolver().delete(ContractInsertPdvFotografico.CONTENT_URI,selection,selectionArgs);
            getContentResolver().delete(ContractInsertVentas.CONTENT_URI,selection,selectionArgs);
            getContentResolver().delete(ContractInsertPropensosYProdMalEst.CONTENT_URI,selection,selectionArgs);
            getContentResolver().delete(ContractInsertAlmuerzo.CONTENT_URI,selection,selectionArgs);
            getContentResolver().delete(ContractInsertLogisticoRelevo.CONTENT_URI,selection,selectionArgs);
//            getContentResolver().delete(ContractLog.CONTENT_URI,selection,selectionArgs);

            //Reload la actividad
            finish();
            startActivity(getIntent());
        }catch (Exception e) {
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_historial, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_hist) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setIcon(R.drawable.icon_info);


            builder.setTitle("Vaciar Historial");

            TextView message = new TextView(getApplicationContext());
            message.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            message.setTextColor(getResources().getColor(R.color.negro));
            message.setText("\nEstás a punto de eliminar el historial de la información relevada\n\n¿Desea continuar?");
            builder.setView(message);

            //    builder.setMessage("¿Desea eliminar registros?");
            builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    vaciarHistorial();
                }
            });

            builder.setNeutralButton("NO",null);

            AlertDialog ad = builder.create();
            ad.show();
            Button pButton = ad.getButton(DialogInterface.BUTTON_POSITIVE);
            //pButton.setTextColor(Color.rgb(79, 195, 247));
            Button cButton = ad.getButton(DialogInterface.BUTTON_NEUTRAL);
            //cButton.setTextColor(Color.rgb(79, 195, 247));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        finish();
        startActivity(getIntent());
    }

    @Override
    public void onClick(View v) {
        if (v == btnFinicio) {
            inicioPromo();
        }
        if (v == btnFfin) {
            String fechaInicio = txtFinicio.getText().toString();
            if (!fechaInicio.trim().isEmpty()) {
                finPromo();
            } else {
                Toast.makeText(this, "Primero seleccione fecha incio", Toast.LENGTH_SHORT).show();
            }
        }
    }



    public void inicioPromo() {
        try {
            final Calendar calendar = Calendar.getInstance();
            calendar.setFirstDayOfWeek(Calendar.MONDAY);
            int anio = calendar.get(Calendar.YEAR);
            int mes = calendar.get(Calendar.MONTH);
            int dia = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog from_dateListener = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    Date date = null;
                    try {
                        date = dateFormat.parse(dayOfMonth + "/" + (month + 1) + "/" + year);
                        String outDate = dateFormat.format(date);
                        txtFinicio.setText(outDate);

                        fDesde = outDate;

                        if (!txtFfin.getText().toString().trim().isEmpty()) {
                            txtFfin.setText("Fecha fin");
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }, anio, mes, dia);
            from_dateListener.show();
        } catch (Exception e) {
            System.out.println("INI PROMO: " + e.getMessage());
        }
    }

    public void finPromo() {
        try {
            final Calendar calendar = Calendar.getInstance();
            calendar.setFirstDayOfWeek(Calendar.MONDAY);
            int anio = calendar.get(Calendar.YEAR);
            int mes = calendar.get(Calendar.MONTH);
            int dia = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog from_dateListener = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    Date date = null;
                    try {
                        date = dateFormat.parse(dayOfMonth + "/" + (month+1) + "/" + year);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    String outDate = dateFormat.format(date);
                    txtFfin.setText(outDate);

                    fHasta = outDate;
                }
            },anio,mes,dia);

            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            formatter.setLenient(false);

            String oldTime = txtFinicio.getText().toString();
            Date oldDate = formatter.parse(oldTime);

            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(oldDate);
            calendar1.add(Calendar.DAY_OF_MONTH, 6); // Suma 6 días
            Date newDate = calendar1.getTime();

            long newMillis = newDate.getTime();
            long oldMillis = oldDate.getTime();

            // set maximum date to be selected as today
            from_dateListener.getDatePicker().setMinDate(oldMillis);
            from_dateListener.getDatePicker().setMaxDate(newMillis);
            from_dateListener.show();
        } catch (Exception e) {
            System.out.println("FIN PROMO: " + e.getMessage());
        }
    }

    private void limpiarFiltros() {
        pdv = "PDV";
        categoria = "CATEGORIA";
        marca = "MARCA";
        status = "Status";
        fDesde = "";
        fHasta = "";
        txtFinicio.setText("Fecha inicio");
        txtFfin.setText("Fecha fin");

        // Limpiar spinners
        spPdv.setSelection(0);
        spCategoria.setSelection(0);
        spMarca.setSelection(0);
        spStatus.setSelection(0);

        // Limpiar RecyclerView
        recyclerView.setAdapter(null);
    }


    /*public void generatePDF(RecyclerView view) {

        RecyclerView.Adapter adapter = view.getAdapter();
        Bitmap bigBitmap = null;
        if (adapter != null) {
            int size = adapter.getItemCount();
            int height = 0;
            Paint paint = new Paint();
            int iHeight = 0;
            final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

            // Use 1/8th of the available memory for this memory cache.
            final int cacheSize = maxMemory / 8;
            LruCache<String, Bitmap> bitmaCache = new LruCache<>(cacheSize);
            for (int i = 0; i < size; i++) {
                RecyclerView.ViewHolder holder = adapter.createViewHolder(view, adapter.getItemViewType(i));
                adapter.onBindViewHolder(holder, i);
                holder.itemView.measure(View.MeasureSpec.makeMeasureSpec(view.getWidth(), View.MeasureSpec.EXACTLY),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                holder.itemView.layout(0, 0, holder.itemView.getMeasuredWidth(), holder.itemView.getMeasuredHeight());
                holder.itemView.setDrawingCacheEnabled(true);
                holder.itemView.buildDrawingCache();
                Bitmap drawingCache = holder.itemView.getDrawingCache();
                if (drawingCache != null) {

                    bitmaCache.put(String.valueOf(i), drawingCache);
                }

                height += holder.itemView.getMeasuredHeight();
            }

            bigBitmap = Bitmap.createBitmap(view.getMeasuredWidth(), height, Bitmap.Config.ARGB_8888);
            Canvas bigCanvas = new Canvas(bigBitmap);
            bigCanvas.drawColor(Color.WHITE);

            Document document = new Document(PageSize.A4);
            final File file = new File(getStorageDir("PDF"), "print.pdf");
            try {
                PdfWriter.getInstance(document, new FileOutputStream(file));
            } catch (DocumentException | FileNotFoundException e) {
                e.printStackTrace();
            }

            for (int i = 0; i < size; i++) {

                try {
                    //Adding the content to the document
                    Bitmap bmp = bitmaCache.get(String.valueOf(i));
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    Image image = Image.getInstance(stream.toByteArray());
                    float scaler = ((document.getPageSize().getWidth() - document.leftMargin()
                            - document.rightMargin() - 0) / image.getWidth()) * 100; // 0 means you have no indentation. If you have any, change it.
                    image.scalePercent(scaler);
                    image.setAlignment(com.itextpdf.text.Image.ALIGN_CENTER | com.itextpdf.text.Image.ALIGN_TOP);
                    if (!document.isOpen()) {
                        document.open();
                    }
                    document.add(image);

                } catch (Exception ex) {
                    Log.e("TAG-ORDER PRINT ERROR", ex.getMessage());
                }
            }

            if (document.isOpen()) {
                document.close();
            }
            // Set on UI Thread
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    AlertDialog.Builder builder = new AlertDialog.Builder(HistorialActivity.this);
                    builder.setTitle("Success")
                            .setMessage("PDF File Generated Successfully.")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                    intent.setDataAndType(Uri.fromFile(file), "application/pdf");
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                    startActivity(intent);
                                }

                            }).show();
                }
            });

        }

    }*/

}
