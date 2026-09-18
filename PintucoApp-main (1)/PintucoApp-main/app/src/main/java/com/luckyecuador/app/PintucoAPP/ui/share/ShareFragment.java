package com.luckyecuador.app.PintucoAPP.ui.share;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_OK;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.github.barteksc.pdfviewer.PDFView;
import com.luckyecuador.app.PintucoAPP.Clase.BasePortafolioProductos;
import com.luckyecuador.app.PintucoAPP.Clase.ShareElements;
import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;
import com.luckyecuador.app.PintucoAPP.Conexion.Mensajes;
import com.luckyecuador.app.PintucoAPP.Conexion.VerificarNet;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertShare;
import com.luckyecuador.app.PintucoAPP.DataBase.DatabaseHelper;
import com.luckyecuador.app.PintucoAPP.DataBase.Provider;
import com.luckyecuador.app.PintucoAPP.R;
import com.luckyecuador.app.PintucoAPP.Sync.SyncAdapter;
import com.luckyecuador.app.PintucoAPP.Utils.DeveloperOptions;
import com.luckyecuador.app.PintucoAPP.Utils.GuardarLog;
import com.luckyecuador.app.PintucoAPP.Utils.RequestPermissions;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Pattern;

public class ShareFragment extends Fragment implements  AdapterView.OnItemSelectedListener{

    private EditText txtTotalCaras;

   private EditText txtCaras;

    public android.app.AlertDialog ad;

    private TextView top3;

    private EditText txtMarca1;
    private EditText txtMarca2;
    private EditText txtMarca3;

    private EditText txtCaras1;
    private EditText txtCaras2;
    private EditText txtCaras3;
    private EditText txtComentario;

    private String subc;

    private Boolean topOtros;

    private String platafroma;

    public android.app.AlertDialog adMsj;

    private LinearLayout linearLayoutOtros1;
    private LinearLayout linearLayoutOtros2;
    private LinearLayout linearLayoutOtros3;
    private LinearLayout llComentario;

    private TextView lblOtros;
    private Spinner spCategoria;
    private Spinner spSubcategoria;
    private Spinner spRazones;
    private Spinner spSegmento;

    private Spinner spCausal;
    private Button btnGuardar;
    //private Spinner spBrand;
    String[] valueOfEditText;
    String[] valueOfTextView;
    View view;

    ArrayList<ShareElements> shareElements;

    ArrayList<ShareElements> otrasMarcas;

    CustomAdapterShare dataAdapter;
    TextView empty;
    ListView listview;

    String user,
    codigo_pdv,
    punto_venta,
    fecha,
    hora,
    sku_list;

    ArrayList<BasePortafolioProductos> listProductos;
    String id_pdv, subcategoria, presentacion, venta, categoria, sku, canal, subcanal;
    String brand;
    DatabaseHelper handler;
    private int resta;
    ImageView btnCalculo;
    ImageButton btnCamera;

    String otros;

    Boolean esDiferenteCaras = false;

    private ImageView imageView;
    private Bitmap bitmapfinal;
    Bitmap bitmap;
    final int COD_SELECCIONA=10;
    final int COD_FOTO=20;

    LinearLayout ly;

    String modulo = "SHARE";
    private final String CARPETA_RAIZ="PintucoApp/";
    private final String RUTA_IMAGEN=CARPETA_RAIZ+"Share";
    String path;
    String image = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_share, container, false);

        // Evita que el teclado no cubra los sku
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        // Establece la orientacion
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        handler = new DatabaseHelper(getContext(), Provider.DATABASE_NAME, null, 1);

        setHasOptionsMenu(true);

        LoadData();
        new DeveloperOptions().modalDevOptions(getActivity());
        RequestPermissions requestPermissions = new RequestPermissions(getContext(), getActivity());
        requestPermissions.showPermissionDialog();

        txtTotalCaras = (EditText) rootView.findViewById(R.id.txtTotalCaras);
        lblOtros = (TextView) rootView.findViewById(R.id.lblOtros);
        spCategoria = (Spinner) rootView.findViewById(R.id.spSector);
        spSubcategoria = (Spinner) rootView.findViewById(R.id.spCategoria);
        txtComentario = (EditText) rootView.findViewById(R.id.txtComentario);
      //  spRazones = (Spinner) rootView.findViewById(R.id.spRazon);
        spSegmento = (Spinner) rootView.findViewById(R.id.spSegmento);
        spCausal = (Spinner) rootView.findViewById(R.id.spCausal);
        btnGuardar = (Button) rootView.findViewById(R.id.btnGuardar);
        //spBrand =(Spinner) findViewById(R.id.spCategoria);
        empty = (TextView) rootView.findViewById(R.id.recyclerview_data_empty);
        btnCalculo = (ImageView) rootView.findViewById(R.id.btnCalculo);
        imageView = (ImageView) rootView.findViewById(R.id.ivFotoShare);
        btnCamera = (ImageButton) rootView.findViewById(R.id.ibCargarFotoShare);
        top3 = (TextView) rootView.findViewById(R.id.tvTop3);
        linearLayoutOtros1 = (LinearLayout) rootView.findViewById(R.id.linearLayoutOtros1);
        linearLayoutOtros2 = (LinearLayout) rootView.findViewById(R.id.linearLayoutOtros2);
        linearLayoutOtros3 = (LinearLayout) rootView.findViewById(R.id.linearLayoutOtros3);
        llComentario = (LinearLayout) rootView.findViewById(R.id.llComentario);
        txtCaras1 = (EditText) rootView.findViewById(R.id.txtCaras1);
        txtCaras2 = (EditText) rootView.findViewById(R.id.txtCaras2);
        txtCaras3 = (EditText) rootView.findViewById(R.id.txtCaras3);
        txtMarca1 = (EditText) rootView.findViewById(R.id.txtMarca1);
        txtMarca2 = (EditText) rootView.findViewById(R.id.txtMarca2);
        txtMarca3 = (EditText) rootView.findViewById(R.id.txtMarca3);

        ly = (LinearLayout) rootView.findViewById(R.id.linearLayoutComentario);

        //startService(new Intent(getContext(), MyService.class));




        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                for (int i = start; i < end; ++i) {
                    if (!Pattern.compile("[ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890.]*").matcher(String.valueOf(source.charAt(i))).matches()) {
                        return "";
                    }
                }
                return null;
            }
        };

        txtTotalCaras.setFilters(new InputFilter[]{new InputFilter() {
            DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                int indexPoint = dest.toString().indexOf(decimalFormatSymbols.getDecimalSeparator());
                if (indexPoint == -1)
                    return source;

                int decimals = dend - (indexPoint+1);
                return decimals < 2 ? source : "";
            }
        }
        });

        txtTotalCaras.setFilters(new InputFilter[]{filter,new InputFilter.LengthFilter(6)});

        btnCalculo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    esDiferenteCaras = true;
                    int sum = 0;
                    for (int i = 0; i < listview.getChildCount(); i++) {
                        View v = listview.getChildAt(i);
                        EditText edt = (EditText) v.findViewById(R.id.txtventa);
//
                        if (!edt.getText().toString().equals(""))
                            sum+=Integer.parseInt(String.valueOf(edt.getText()));
                    }
                    if (!txtTotalCaras.getText().toString().equals("")) {
                        int cmTotal = Integer.parseInt(String.valueOf(txtTotalCaras.getText()));
                        resta = cmTotal - sum;
                        Log.i("RESTA", resta + "");
                        resta(resta);
                    }
                }catch (Exception e) {
                    Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (view == btnCamera) {
                    cargarImagen();
                }


            }
        });


        filtrarCategoria();
        spCausal.setOnItemSelectedListener(this);

        listview = (ListView) rootView.findViewById(R.id.list);
//        listview.setHasFixedSize(true);
        listview.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        txtTotalCaras.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(Editable s) {
                txtTotalCaras.setTextColor(getResources().getColor(R.color.negro));
            }
        });

        lblOtros.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(Editable s) {
                lblOtros.setTextColor(getResources().getColor(R.color.negro));
            }
        });


        new GuardarLog(getContext()).saveLog(user, "", "Ingreso al Módulo Share of Display");

        return rootView;
    }

    public static boolean StringDuplicado(String str) {
        return (str.matches(".*(.)\\1.*"));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_visualizacion, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_view) {
            ver_ejemplo_sod();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        binding = null;
    }

    public void ver_ejemplo_sod(){
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alertdialog_como_revelar_sod, null);

        PDFView pdfView = dialogView.findViewById(R.id.pdfview);


        pdfView.fromAsset("MANUAL_SHARE.pdf")
                .load();


        builder.setPositiveButton(R.string.cancel,null);
        builder.setView(dialogView);
        ad = builder.create();
        ad.show();
    }



    private boolean validaPermisos() {
        if (Build.VERSION.SDK_INT<Build.VERSION_CODES.M) {
            return true;
        }

        if ((getContext().checkSelfPermission(CAMERA)== PackageManager.PERMISSION_GRANTED) && 
            (getContext().checkSelfPermission(WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED)) {
            return true;
        }

        if ((shouldShowRequestPermissionRationale(CAMERA)) ||
                (shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE))) {
            cargarDialogoRecomendacion();
        }else{
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE,CAMERA},100);
        }

        return false;
    }

    public void onResume() {
        super.onResume();
        Log.i("ACTIVITY", "ON RESUME");
        new DeveloperOptions().modalDevOptions(getActivity());
    }

    public void resultado() {
        int resta = 0;
        try{
            if (!txtTotalCaras.getText().toString().equals("")) {
                int cmTotal = Integer.parseInt(String.valueOf(txtTotalCaras.getText()));
                resta = cmTotal - sum();
                resta(resta);
            }
        }catch (Exception e) {
            Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    public int verificacion_resultado() {
        int resta = 0;
        try{
            if (!txtTotalCaras.getText().toString().equals("")) {
                int cmTotal = Integer.parseInt(String.valueOf(txtTotalCaras.getText()));
                resta = cmTotal - sum();
            }
        }catch (Exception e) {
            Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
        return resta;
    }

    public int sum() {
        int sum = 0;
        try {
            for(int i = 0; i < listview.getChildCount(); i++) {
                View v = listview.getChildAt(i);
                EditText edt = (EditText) v.findViewById(R.id.txtventa);
                Log.i("FOR", edt.getText().toString());
                if (!edt.getText().toString().equals(""))
                    sum += Integer.parseInt(String.valueOf(edt.getText()));
            }

        }catch (Exception e) {
            Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
        return sum;
    }

    public void LoadData() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        id_pdv = sharedPreferences.getString(Constantes.IDPDV,Constantes.NODATA);
        user = sharedPreferences.getString(Constantes.USER, Constantes.NODATA);
        canal = sharedPreferences.getString(Constantes.TIPO,Constantes.NODATA);
        subcanal = sharedPreferences.getString(Constantes.SUBCANAL,Constantes.NODATA);
        id_pdv = sharedPreferences.getString(Constantes.PHARMA_ID, Constantes.NODATA);
        codigo_pdv = sharedPreferences.getString(Constantes.CODIGO, Constantes.NODATA);
        punto_venta = sharedPreferences.getString(Constantes.PDV, Constantes.NODATA);
        fecha = sharedPreferences.getString(Constantes.FECHA, Constantes.NODATA);
        hora = sharedPreferences.getString(Constantes.HORA, Constantes.NODATA);
    }

    public void resta(int total) {
        try{
            if (total<0) {
               // lblOtros.setBackgroundColor(Color.RED);
                lblOtros.setBackground(getResources().getDrawable(R.drawable.spinner_background_red));
                lblOtros.setTextColor(Color.BLACK);
                Toast.makeText(getContext(),"Valor de celdas menor al Total",Toast.LENGTH_SHORT).show();
            } else {

                // Descomentar cuando se habilite el top 3 otros
/*

                lblOtros.setBackgroundColor(Color.TRANSPARENT);
                top3.setVisibility(View.VISIBLE);
                linearLayoutOtros1.setVisibility(View.VISIBLE);
                linearLayoutOtros2.setVisibility(View.VISIBLE);
                linearLayoutOtros3.setVisibility(View.VISIBLE);

                topOtros = true;

                txtMarca1.setTextColor(Color.BLACK);
                txtMarca1.setText("");
                txtMarca2.setTextColor(Color.BLACK);
                txtMarca2.setText("");
                txtMarca3.setTextColor(Color.BLACK);
                txtMarca3.setText("");

                txtCaras1.setTextColor(Color.BLACK);
                txtCaras1.setText("");
                txtCaras2.setTextColor(Color.BLACK);
                txtCaras2.setText("");
                txtCaras3.setTextColor(Color.BLACK);
                txtCaras3.setText("");
*/

                lblOtros.setBackgroundColor(Color.TRANSPARENT);
                llComentario.setVisibility(View.VISIBLE);

            }

//            if (total == 0){
//                top3.setVisibility(View.GONE);
//                linearLayoutOtros1.setVisibility(View.GONE);
//                linearLayoutOtros2.setVisibility(View.GONE);
//                linearLayoutOtros3.setVisibility(View.GONE);
//                topOtros = false;
//            }

            lblOtros.setText(String.valueOf(total));

        }catch (Exception e) {
            Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    public void cargarTotal(String codigo, String categoria, String subcategoria) {
        //Log.i("CODIGO", codigo_pdv);

        String totalUniverso = handler.getTotalUniverso(codigo, categoria, subcategoria);
        txtTotalCaras.setText(totalUniverso);
        txtTotalCaras.setTextColor(getResources().getColor(R.color.color_revelado));


        /*
        int totalUniversoMesAnterior = handler.getTotalUniversoMesAnterior(codigo,categoria,subcategoria);

        if (!totalUniverso.isEmpty()){
            if (totalUniversoMesAnterior > Integer.parseInt(totalUniverso)){
                alertDialogCarasReveladas();
            }
        }*/


    }


    public void cargarOtros(String codigo, String categoria, String subcategoria) {
        //Log.i("CODIGO", codigo_pdv);
        otros = handler.getOtros(codigo, categoria, subcategoria);
        String hora = handler.getHora(codigo, categoria, subcategoria);



        lblOtros.setText(otros);
        lblOtros.setTextColor(getResources().getColor(R.color.color_revelado));

        // Descomentar cuando se habilite el top 3 otros

//        if (!otros.isEmpty() && Integer.parseInt(otros) > 0){
//            top3.setVisibility(View.VISIBLE);
//            linearLayoutOtros1.setVisibility(View.VISIBLE);
//            linearLayoutOtros2.setVisibility(View.VISIBLE);
//            linearLayoutOtros3.setVisibility(View.VISIBLE);
//        }

//        if (!otros.isEmpty()){
//            if (Integer.parseInt(otros) > 0){
//
//                String razones = "Otros";
//                otrasMarcas = handler.getOtrasMarcas(codigo,categoria,subcategoria,hora);
//
//                if (otrasMarcas.size() > 0){
//                    top3.setVisibility(View.VISIBLE);
//                    linearLayoutOtros1.setVisibility(View.VISIBLE);
//                    linearLayoutOtros2.setVisibility(View.VISIBLE);
//                    linearLayoutOtros3.setVisibility(View.VISIBLE);
//                }
//
//                if(otrasMarcas.size() == 1){
//
//                    txtMarca1.setText(otrasMarcas.get(0).getMarca().replace("OTROS 1:",""));
//                    txtMarca1.setTextColor(getResources().getColor(R.color.color_revelado));
//                    txtCaras1.setText(otrasMarcas.get(0).getCaras());
//                    txtCaras1.setTextColor(getResources().getColor(R.color.color_revelado));
//
//                    txtMarca2.setText("");
//                    txtCaras2.setText("");
//
//                    txtMarca3.setText("");
//                    txtCaras3.setText("");
//
//
//                }else if(otrasMarcas.size() == 2){
//
//                    txtMarca1.setText(otrasMarcas.get(0).getMarca().replace("OTROS 1:",""));
//                    txtMarca1.setTextColor(getResources().getColor(R.color.color_revelado));
//                    txtCaras1.setText(otrasMarcas.get(0).getCaras());
//                    txtCaras1.setTextColor(getResources().getColor(R.color.color_revelado));
//
//                    txtMarca2.setText(otrasMarcas.get(1).getMarca().replace("OTROS 2:",""));
//                    txtMarca2.setTextColor(getResources().getColor(R.color.color_revelado));
//                    txtCaras2.setText(otrasMarcas.get(1).getCaras());
//                    txtCaras2.setTextColor(getResources().getColor(R.color.color_revelado));
//
//                    txtMarca3.setText("");
//                    txtCaras3.setText("");
//
//                }else if(otrasMarcas.size() == 3){
//
//                    txtMarca1.setText(otrasMarcas.get(0).getMarca().replace("OTROS 1:",""));
//                    txtMarca1.setTextColor(getResources().getColor(R.color.color_revelado));
//                    txtCaras1.setText(otrasMarcas.get(0).getCaras());
//                    txtCaras1.setTextColor(getResources().getColor(R.color.color_revelado));
//
//                    txtMarca2.setText(otrasMarcas.get(1).getMarca().replace("OTROS 2:",""));
//                    txtMarca2.setTextColor(getResources().getColor(R.color.color_revelado));
//                    txtCaras2.setText(otrasMarcas.get(1).getCaras());
//                    txtCaras2.setTextColor(getResources().getColor(R.color.color_revelado));
//
//                    txtMarca3.setText(otrasMarcas.get(2).getMarca().replace("OTROS 3:",""));
//                    txtMarca3.setTextColor(getResources().getColor(R.color.color_revelado));
//                    txtCaras3.setText(otrasMarcas.get(2).getCaras());
//                    txtCaras3.setTextColor(getResources().getColor(R.color.color_revelado));
//
//                }
//            }
//        }
    }

    public String cargarCaras(String codigo, String categoria, String subcategoria, String marca) {
        //Log.i("CODIGO", codigo_pdv);
        String totalCaras = handler.getTotalCaras(codigo, categoria, subcategoria, marca);
        return totalCaras;
    }

    public void filtrarCategoria() {
        List<String> operadores = handler.getSectorShare(modulo);
        if (operadores.size()==2) {
            operadores.remove(0);
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, operadores);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategoria.setAdapter(dataAdapter);
        spCategoria.setOnItemSelectedListener(this);
        spCategoria.post(() -> spCategoria.setSelection(0));
    }

    public void filtrarSubcategoria(String sector) {
        List<String> operadores = handler.getCategoriaShare(sector);
        if (operadores.size()==2) {
            operadores.remove(0);
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, operadores);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSubcategoria.setAdapter(dataAdapter);
        spSubcategoria.setOnItemSelectedListener(this);
    }

    public void filtrarSegmento(String sector, String categoria) {

        List<String> operadores = handler.getSegmentoShare(sector, categoria);
        /*
        if (operadores.size()==2) {
            operadores.remove(0);
        }*/
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, operadores);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSegmento.setAdapter(dataAdapter);
        spSegmento.setOnItemSelectedListener(this);
    }

    /*public void filtrarMarca(String logro, String subcategoria) {
        List<String> operadores = handler.getBrandShare(subcategoria,logro);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, operadores);
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spBrand.setAdapter(dataAdapter);
        spBrand.setOnItemSelectedListener(this);
    }*/

    //************METODOS PARA TAKE-PHOTO Y UPLOAD
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK) {

            switch (requestCode) {
                case COD_SELECCIONA:
                   /* Uri miPath=data.getData();
                    imageView.setImageURI(miPath);*/
                    Uri filePath = data.getData();
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), filePath);
                        //Setear el ImageView con el Bitmap
                        scaleImage(bitmap);
                    }catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case COD_FOTO:
                    MediaScannerConnection.scanFile(getContext(), new String[]{path}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String path, Uri uri) {
                                    Log.i("Ruta de almacenamiento","Path: "+path);
                                }
                            });
                    Bitmap bitmap= BitmapFactory.decodeFile(path);
                    scaleImage(bitmap);
                    break;
            }
        }
    }

    //Permite hacer la imagen mas pequeña para mostrarla en el ImageView
    public void scaleImage(Bitmap bitmap) {
        try{
            int mheight = (int) ( bitmap.getHeight() * (1024.0 / bitmap.getWidth()) );
            Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 1024, mheight, true);
//            imageView.setImageBitmap(scaled);

            try {
                for(int i = 0; i < listview.getChildCount(); i++) {
                    View v = listview.getChildAt(i);
                    EditText edt = (EditText) v.findViewById(R.id.txtventa);
                    ImageView iv = (ImageView) v.findViewById(R.id.ivFotoExhibiciones);
                    Log.i("FOR", edt.getText().toString());
                    listview.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
                    if (edt.getText().toString().equals(sku_list)) {
//                        sum += Integer.parseInt(String.valueOf(edt.getText()));
                        iv.setImageBitmap(scaled);
                        bitmapfinal = ((BitmapDrawable)iv.getDrawable()).getBitmap();
                        Log.i("BITMAP:", bitmapfinal.toString());
                    }
                }//
            }catch (Exception e) {
                Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
            }

//            bitmapfinal = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
        } catch (Exception e) {
            AlertDialog alertDialog1;
            alertDialog1 = new AlertDialog.Builder(getContext()).create();
            alertDialog1.setTitle("Message");
            alertDialog1.setMessage("Notificar \t "+e.toString());
            alertDialog1.show();
            Log.e("compressBitmap", "Error on compress file");
        }
    }


    private void alertDialogCarasReveladas() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
        LayoutInflater inflater2 = getLayoutInflater();
        View dialogView = inflater2.inflate(R.layout.alertdialog_caras_reveladas, null);
        builder.setView(dialogView);
        Button btn = (Button) dialogView.findViewById(R.id.boton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adMsj.dismiss();
                adMsj = null;
            }
        });
        adMsj = builder.create();
        adMsj.show();
        adMsj.setCancelable(false);
    }




    private void cargarImagen() {
        final CharSequence[] opciones={"Tomar Foto","Cargar Imagen","Cancelar"};
        final AlertDialog.Builder alertOpciones=new AlertDialog.Builder(getContext());
        alertOpciones.setTitle("Seleccione una Opción");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("Tomar Foto")) {
                    tomarFotografia();
                }else{
                    if (opciones[i].equals("Cargar Imagen")) {
                        /* Intent intent=new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/");
                        startActivityForResult(intent.createChooser(intent,"Seleccione la Aplicación"),COD_SELECCIONA);*/
                        openGallery();
                    }else{
                        dialogInterface.dismiss();
                    }
                }
            }
        });
        alertOpciones.show();
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Seleccionar una imagen"), COD_SELECCIONA);
    }

    private void tomarFotografia() {
        File fileImagen=new File(Environment.getExternalStorageDirectory(),RUTA_IMAGEN);
        boolean isCreada=fileImagen.exists();
        String nombreImagen="";
        if (isCreada==false) {
            isCreada=fileImagen.mkdirs();
        }

        if (isCreada==true) {
            nombreImagen=(System.currentTimeMillis()/1000)+".jpg";
        }


        path=Environment.getExternalStorageDirectory()+
                File.separator+RUTA_IMAGEN+File.separator+nombreImagen;

        File imagen=new File(path);

        Intent intent=null;
        intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.N)
        {
            String authorities=getContext().getPackageName()+".provider";
            Uri imageUri= FileProvider.getUriForFile(getContext(),authorities,imagen);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        }else
        {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imagen));
        }
        startActivityForResult(intent,COD_FOTO);

        //
    }

    //Metodo que sube la imagen al servidor
    public String getStringImage(Bitmap bmp) {
        String encodedImage;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //Comprime la Imagen tipo, calidad y outputstream
        bmp.compress(Bitmap.CompressFormat.JPEG, 35, baos);
        byte[] imageBytes = baos.toByteArray();
        encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    //String to bitmap
//    public Bitmap StringToBitMap(String encodedString) {
//        try{
//            byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);
//            Bitmap bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
//            return bitmap;
//        }catch(Exception e) {
//            e.getMessage();
//            return null;
//        }
//    }
    //Tomar Foto

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode==100) {
            if (grantResults.length==2 && grantResults[0]== PackageManager.PERMISSION_GRANTED
                    && grantResults[1]==PackageManager.PERMISSION_GRANTED) {
                //botonCargar.setEnabled(true);
            }else{
                solicitarPermisosManual();
            }
        }

    }

    private void solicitarPermisosManual() {
        final CharSequence[] opciones={"si","no"};
        final AlertDialog.Builder alertOpciones=new AlertDialog.Builder(getContext());
        alertOpciones.setTitle("¿Desea configurar los permisos de forma manual?");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("si")) {
                    Intent intent=new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri=Uri.fromParts("package", getContext().getPackageName(),null);
                    intent.setData(uri);
                    startActivity(intent);
                }else{
                    Toast.makeText(getContext(),"Los permisos no fueron aceptados",Toast.LENGTH_SHORT).show();
                    dialogInterface.dismiss();
                }
            }
        });
        alertOpciones.show();
    }

    private void cargarDialogoRecomendacion() {
        AlertDialog.Builder dialogo=new AlertDialog.Builder(getContext());
        dialogo.setTitle("Permisos Desactivados");
        dialogo.setMessage("Debe aceptar los permisos para el correcto funcionamiento de la App");

        dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE,CAMERA},100);
            }
        });
        dialogo.show();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        /*
        if (adapterView== spCategoria) {
            try{
                categoria = adapterView.getItemAtPosition(i).toString();
                if (categoria.equalsIgnoreCase("SELECCIONE")) {
                    listview.setAdapter(null);

                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
                    listview.setLayoutParams(layoutParams);
//                    listview.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 10));

                    top3.setVisibility(View.GONE);
                    linearLayoutOtros1.setVisibility(View.GONE);
                    linearLayoutOtros2.setVisibility(View.GONE);
                    linearLayoutOtros3.setVisibility(View.GONE);

                }
                filtrarSubcategoria(categoria);
            }catch (Exception e) {
                Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
        */




        if (adapterView== spCategoria)
        {
            try {
                categoria = adapterView.getItemAtPosition(i).toString();

                if (categoria.equalsIgnoreCase("SELECCIONE")) {
                    listview.setAdapter(null);

                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
                    listview.setLayoutParams(layoutParams);
//                    listview.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 10));
                }
                //filtrarMarca(logro,subcategoria);
                //filtrarSegmento(logro, subcategoria);

                cargarTotal(codigo_pdv, categoria, subcategoria);
                cargarOtros(codigo_pdv, categoria, subcategoria);

                // Descomentar cuando se habilite el top 3 otros

//                if (lblOtros.getText().toString().isEmpty()){
//
//                    top3.setVisibility(View.GONE);
//                    linearLayoutOtros1.setVisibility(View.GONE);
//                    linearLayoutOtros2.setVisibility(View.GONE);
//                    linearLayoutOtros3.setVisibility(View.GONE);
//
//                }

                esDiferenteCaras = false;
                lblOtros.setBackgroundColor(Color.TRANSPARENT);
                llComentario.setVisibility(View.GONE);
                spCausal.setSelection(0);
                txtComentario.setText("");
                showListView(categoria, subcategoria, canal, subcanal);

            }catch (Exception e) {
                Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }








        /*
        if (adapterView== spSubcategoria)
        {
            try {
                subcategoria = adapterView.getItemAtPosition(i).toString();

                if (subcategoria.equalsIgnoreCase("SELECCIONE")) {
                    listview.setAdapter(null);
//                    listview.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 10));
                }
                //filtrarMarca(logro,subcategoria);
                //filtrarSegmento(logro, subcategoria);

                cargarTotal(codigo_pdv, categoria, subcategoria);
                cargarOtros(codigo_pdv, categoria, subcategoria);

                if (lblOtros.getText().toString().isEmpty()){

                    top3.setVisibility(View.GONE);
                    linearLayoutOtros1.setVisibility(View.GONE);
                    linearLayoutOtros2.setVisibility(View.GONE);
                    linearLayoutOtros3.setVisibility(View.GONE);

                }

                esDiferenteCaras = false;

                showListView(categoria, subcategoria, canal, subcanal);

            }catch (Exception e) {
                Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        } */

        if(adapterView == spCausal){
            if (adapterView.getItemAtPosition(i).toString().equals("Otros")){
                if (ly.getVisibility() == View.GONE){
                    ly.setVisibility(View.VISIBLE);
                }
            }else{
                if (ly.getVisibility() == View.VISIBLE){
                    ly.setVisibility(View.GONE);
                }
                txtComentario.setText("");
            }
        }

        /*if (adapterView== spSegmento)
        {
            try{
                presentacion =adapterView.getItemAtPosition(i).toString();
                //filtrarMarca(logro,subcategoria);
                showListView(logro, subcategoria, presentacion);
            }catch (Exception e) {
                Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }*/

        /*if (adapterView==spBrand) {
            contenido = spBrand.getSelectedItem().toString();
            showListView(logro, subcategoria);
        }*/

    }

    /*public void showListView(String sector ,String logro, String segmento) {
        listProductos = handler.filtrarListProductos2Share(sector,logro, segmento);

        dataAdapter = new CustomAdapterPresenciaMinima(getContext(),listProductos);
        listview.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, listProductos.size()*85));
        listview.setAdapter(dataAdapter);

        if (!dataAdapter.isEmpty()) {
            listview.setAdapter(dataAdapter);
        }else{
            empty.setVisibility(View.VISIBLE);
        }
    }*/
//    public void showListView(String sector ,String categoria, String canal, String subcanal) {
//        listProductos = handler.filtrarListProductos2Share(sector,categoria, canal, subcanal);
//
//        dataAdapter = new CustomAdapterPresenciaMinima(getContext(),listProductos);
//        listview.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, listProductos.size()*100));
//        listview.setAdapter(dataAdapter);
//
//        if (!dataAdapter.isEmpty()) {
//            listview.setAdapter(dataAdapter);
//        }else{
//            empty.setVisibility(View.VISIBLE);
//        }
//    }

//    public void showListView(String sector ,String categoria, String canal, String subcanal) {
//        listProductos = handler.filtrarListProductos2Share(sector,categoria, canal, subcanal);
//
//        dataAdapter = new CustomAdapterShare(getContext(), listProductos);
//        if (dataAdapter.getItemCount() != 0) {
//            empty.setVisibility(View.GONE);
//            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
//
//            listview.setLayoutManager(linearLayoutManager);
//            listview.setHasFixedSize(true);
//            CustomAdapterShare customAdapter = new CustomAdapterShare(this.getContext(),listProductos);
//            listview.setAdapter(customAdapter);
//
//            dataAdapter = (CustomAdapterShare) listview.getAdapter();
//        }else{
//            empty.setVisibility(View.VISIBLE);
//        }
//    }

    public void showListView(String sector ,String categoria, String canal, String subcanal) {


        List<String> marcas_blancas = handler.getMarcasBlancas();
        Log.i("valores share",""+marcas_blancas);
        if (!canal.equals("MAYORISTA")){
            marcas_blancas.clear();
            marcas_blancas.add("-");
        }

        listProductos = handler.filtrarListProductos2Share(sector,categoria, canal, subcanal,  marcas_blancas,modulo );
        Log.i("LIST-SIZE", listProductos.size()+"");
        if(listProductos.size()>0){
            dataAdapter = new CustomAdapterShare(getContext(),listProductos);
            listview.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, listProductos.size()*120));
            listview.setAdapter(dataAdapter);
            listview.setVisibility(View.VISIBLE);
            //empty.setVisibility(View.GONE);
            //listview.setVisibility(View.VISIBLE);
        }else{
            if(!spCategoria.getSelectedItem().toString().equals("Seleccione") /* && !spSubcategoria.getSelectedItem().toString().equals("Seleccione") */){
                Toast.makeText(getContext(), "No hay elementos para mostrar", Toast.LENGTH_SHORT).show();
                listview.setVisibility(View.GONE);
            }
            //empty.setVisibility(View.VISIBLE);
            //listview.setVisibility(View.GONE);
        }
    }

//    public class CustomAdapterPresenciaMinima extends ArrayAdapter<String> {
//
//        public List<String> values;
//        public Context context;
//        boolean[] checkBoxState;
//
//        public CustomAdapterPresenciaMinima(Context context, List<String> values) {
//            super(context, 0, values);
//            this.values = values;
//            checkBoxState=new boolean[values.size()];
//        }
//
//        public class ViewHolder{
//            TextView lblSku;
//            CheckBox check; // agregado GT
//            EditText txtventa;
//        }
//
//        @Override
//        public int getViewTypeCount() {
//            // TODO Auto-generated method stub
//            return values.size();
//        }
//
//        @Override
//        public int getItemViewType(int position) {
//            // TODO Auto-generated method stub
//            return position;
//        }
//
//        @Override
//        public View getView(final int position, View convertView, ViewGroup parent) {
//            //Obtener Instancia Inflater
//            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            ViewHolder vHolder = null;
//            //Comprobar si el View existe
//            //Si no existe inflarlo
//            if (null == convertView) {
//                convertView = inflater.inflate(R.layout.list_row_option_share, parent, false); // Modificacion (list_row_option) GT
//
//                //Obtener instancias de los elementos
//                vHolder = new ViewHolder();
//                vHolder.lblSku = (TextView) convertView.findViewById(R.id.lblSku);
//                vHolder.check = (CheckBox) convertView.findViewById(R.id.checkPresencia);
//                vHolder.check.setVisibility(View.GONE);
//                //   vHolder.txtunidad = (EditText) convertView.findViewById(R.id.txtunidad);
//                vHolder.txtventa = (EditText) convertView.findViewById(R.id.txtventa);
//
//                vHolder.txtventa.setHint("Caras/Percha");
//                //cargarMotivos(vHolder);
//                convertView.setTag(vHolder);
//
//                //checkGuardar.setEnabled(true);
//            } else { vHolder = (ViewHolder) convertView.getTag(); }
//
//            if (values.size() > 0) {
//                //set the data to be displayed
//                vHolder.lblSku.setText(values.get(position));
//
//                vHolder.txtventa.setText(cargarCaras(codigo_pdv, categoria, subcategoria, values.get(position)));
//                vHolder.txtventa.setTextColor(getResources().getColor(R.color.verde));
//                // sku = vHolder.lblSku.getText().toString();
//                // checkGuardar.setEnabled(false);
//                final ViewHolder finalv =vHolder;
//
//                ViewHolder finalVHolder = vHolder;
//                vHolder.txtventa.addTextChangedListener(new TextWatcher() {
//                    @Override
//                    public void onTextChanged(CharSequence s, int start, int before, int count) {}
//
//                    @Override
//                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
//
//                    @Override
//                    public void afterTextChanged(Editable s) {
//                        finalVHolder.txtventa.setTextColor(getResources().getColor(R.color.negro));
//                    }
//                });
//
//                btnGuardar.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        View view;
//                        EditText et = null;
//                        TextView tv;
//                        int listLength = listview.getChildCount();
//                        valueOfEditText = new String[listLength+1];
//                        valueOfTextView = new String[listLength+1];
//                        for (int i = 0; i < listLength; i++) {
//                            String et_otros = lblOtros.getText().toString();
//                            view = listview.getChildAt(i);
//                            et = (EditText) view.findViewById(R.id.txtventa);
//                            tv = (TextView) view.findViewById(R.id.lblSku);
//                            valueOfEditText[i] = et.getText().toString();
//                            valueOfTextView[i] = tv.getText().toString();
//
//                            valueOfEditText[listLength] = et_otros;
//                            valueOfTextView[listLength] = "Otros";
//                        }
//
//                        venta = finalv.txtventa.getText().toString();
//                        sku = finalv.lblSku.getText().toString();
//
//                        int contador_llenos = 0;
//
//                        if (txtCaras!=null && !txtCaras.getText().toString().equals("") && !lblOtros.getText().toString().equals("")) {
//                            if (verificacion_resultado()==Integer.parseInt(lblOtros.getText().toString())) {
//                                for (int i = 0; i < listLength; i++) {
//                                    view = listview.getChildAt(i);
//                                    et = (EditText) view.findViewById(R.id.txtventa);
//                                    if (!et.getText().toString().trim().isEmpty()) {
//                                        contador_llenos++;
//                                    }
//                                }
//
//                                Log.i("CONTADOR_LLENOS",contador_llenos+"");
//                                Log.i("LISTLENGTH",listLength+"");
//
//                                if (contador_llenos==listLength) {
//                                    insertData();
//                                    for (int i = 0; i < listLength; i++) {
//                                        view = listview.getChildAt(i);
//                                        et = (EditText) view.findViewById(R.id.txtventa);
//                                        et.setText("");
//                                    }
//                                    txtCaras.setText("");
//                                    lblOtros.setText("");
//                                }else{
//                                    Toast.makeText(getContext(), "Ingresar cantidad de caras", Toast.LENGTH_SHORT).show();
//                                }
//                            }else{
//                                Toast.makeText(getContext(), "El valor de Otros es incorrecto, se volvera a calcular automaticamente", Toast.LENGTH_SHORT).show();
//                                resultado();
//                            }
//                        }else{
//                            Toast.makeText(getContext(),"Ingresar el total de caras o calcule otros antes de almacenar los datos.",Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//            }
//            //Devolver al ListView la fila creada
//            return convertView;
//        }
//    }

    public void insertData() {
        try {
            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-5"));
            Date currentLocalTime = cal.getTime();
            DateFormat date = new SimpleDateFormat("dd/MM/yyy");
            date.setTimeZone(TimeZone.getTimeZone("GMT-5"));
            String fechaser = date.format(currentLocalTime);

            DateFormat hour = new SimpleDateFormat("HH:mm:ss");
            hour.setTimeZone(TimeZone.getTimeZone("GMT-5"));
            String horaser = hour.format(currentLocalTime);

            String categoria = spCategoria.getSelectedItem().toString();
        //    String subcategoria = spSubcategoria.getSelectedItem().toString();
            String comentario = txtComentario.getText().toString();
           // String razones = spRazones.getSelectedItem().toString();
            //String presentacion = spSegmento.getSelectedItem().toString();
            //String contenido= spBrand.getSelectedItem().toString();

            //Toast.makeText(getContext(), ""+hasConsecutiveDuplicateCharacter(txtComentario.getText().toString()), Toast.LENGTH_SHORT).show();

            String ctms_percha = txtTotalCaras.getText().toString().trim();
            String otros = lblOtros.getText().toString().trim();

            String image = "";

            if (imageView != null && imageView.getDrawable() != null) { //ImageView no vacio
                image = getStringImage(bitmapfinal);
            }else{
                image = "NO_FOTO";
            }

            if(!spCausal.getSelectedItem().toString().equals("Otros")){
                comentario = "Sin Novedad";
            }

            if (!otros.substring(0,1).equals("-")) {
                // Descomentar cuando se habilite el top 3 otros
            //    ShareElements shareElement = new ShareElements();
            //    shareElement.setMarca("Otros");
            //    shareElement.setCaras(otros);
            //    shareElements.add(shareElement);

                for (int i=0; i < shareElements.size(); i++) {

                    String skuSelected = shareElements.get(i).getMarca();
                    String cantidad = shareElements.get(i).getCaras();
                    String razon = shareElements.get(i).getRazon();

                    String manufacturer = handler.getManufacturerShare(skuSelected);
                    String plataforma = handler.getPlataformaByMarca(categoria, subcategoria, skuSelected);


                    // Descomentar cuando se habilite el top 3 otros
                    /*
                    // Para las otra(s) marca(s) del top 3
                    if (plataforma.isEmpty() || plataforma == ""){
                        plataforma = handler.getPlataformaForOtros(categoria, subcategoria);
                    }
                   */

                    ContentValues values = new ContentValues();

                    values.put(ContractInsertShare.Columnas.PHARMA_ID, id_pdv);
                    values.put(ContractInsertShare.Columnas.CODIGO, codigo_pdv);
                    values.put(ContractInsertShare.Columnas.USUARIO, user);
                    values.put(ContractInsertShare.Columnas.SUPERVISOR, punto_venta);
                    values.put(ContractInsertShare.Columnas.FECHA, fechaser);
                    values.put(ContractInsertShare.Columnas.HORA, horaser);
                    values.put(ContractInsertShare.Columnas.SECTOR, categoria);
                    values.put(ContractInsertShare.Columnas.CATEGORIA, "N/A");
                    values.put(ContractInsertShare.Columnas.RAZONES, comentario);
                    values.put(ContractInsertShare.Columnas.SEGMENTO, "");
                    values.put(ContractInsertShare.Columnas.MARCA_SELECCIONADA, skuSelected);
                    values.put(ContractInsertShare.Columnas.BRAND, skuSelected);
                    values.put(ContractInsertShare.Columnas.CTMS_PERCHA, ctms_percha);
                    values.put(ContractInsertShare.Columnas.CTMS_MARCA, cantidad);
                    values.put(ContractInsertShare.Columnas.OTROS, otros);
                    values.put(ContractInsertShare.Columnas.FOTO, image);
                    values.put(ContractInsertShare.Columnas.MANUFACTURER, manufacturer);
                    values.put(ContractInsertShare.Columnas.POS_NAME, punto_venta);
                    values.put(ContractInsertShare.Columnas.PLATAFORMA, plataforma);
                 //   values.put(ContractInsertShare.Columnas.COMENTARIO, comentario);

                    values.put(Constantes.PENDIENTE_INSERCION, 1);

                    getContext().getContentResolver().insert(ContractInsertShare.CONTENT_URI, values);

                    if (VerificarNet.hayConexion(getContext())) {
                        SyncAdapter.sincronizarAhora(getContext(), true, Constantes.insertShare, null);
                        Toast.makeText(getContext(), Mensajes.ON_SYNC_SERVER, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), Mensajes.ON_SYNC_DEVICE, Toast.LENGTH_SHORT).show();
                    }
                }
//                listview.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 10));

//                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
//                listview.setLayoutParams(layoutParams);

                limpiarDatos();
            }else{
                Toast.makeText(getContext(), "El valor de otros no puede ser negativo", Toast.LENGTH_SHORT).show();
                lblOtros.setBackgroundColor(Color.TRANSPARENT);
            }
        }catch (Exception e) {
            Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void limpiarDatos(){

        for (int i = 0; i < listview.getChildCount(); i++) {
            view = listview.getChildAt(i);
            txtCaras = (EditText) view.findViewById(R.id.txtventa);
            txtCaras.setText("");
            txtCaras.getText().clear();
            //       spRazon.setSelection(0);
        }

        Log.i("Entra limpiarDatos","Entra limpiarDatos");
        ShareFragment.this.txtTotalCaras.setText("");
        ShareFragment.this.spCategoria.setSelection(0);

        lblOtros.setText("");
        // Descomentar cuando se habilite el top 3 otros

        // ShareFragment.this.spSubcategoria.setSelection(0);
//        linearLayoutOtros1.setVisibility(View.GONE);
//        txtMarca1.setText("");
//        txtCaras1.setText("");
//        linearLayoutOtros2.setVisibility(View.GONE);
//        txtMarca2.setText("");
//        txtCaras2.setText("");
//        linearLayoutOtros3.setVisibility(View.GONE);
//        txtMarca3.setText("");
//        txtCaras3.setText("");
//        txtComentario.setText("");
        esDiferenteCaras = false;
        ShareFragment.this.spCausal.setSelection(0);


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

//    public class CustomAdapterShare extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
//
//        private final String TAG = CustomAdapterShare.class.getSimpleName();
//        private static final int TYPE_HEADER = 0;
//        private static final int TYPE_ITEM = 1;
//        private Context context;
//        public ArrayList<BasePortafolioProductos> values;
//
//        public CustomAdapterShare(Context context, ArrayList<BasePortafolioProductos> values) {
//            this.context = context;
//            this.values = values;
//        }
//
//        @Override
//        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
////            if (viewType == TYPE_HEADER) {
////                View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_inv_title, parent, false);
////                return new HeaderViewHolder(layoutView);
////            } else if (viewType == TYPE_ITEM) {
//                View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_option_share, parent, false);
//                return new ItemViewHolder(layoutView);
////            }
////            throw new RuntimeException("No match for " + viewType + ".");
//        }
//
//        @Override
//        public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
//            BasePortafolioProductos mObject = values.get(position);
//            /*if (holder instanceof HeaderViewHolder) {
//                //((HeaderViewHolder) holder).headerTitle.setText(mObject.getSku());
//                ((FlooringActivity.CustomAdapterInventario.HeaderViewHolder) holder).headerTitle.setText("SKU");
//                ((FlooringActivity.CustomAdapterInventario.HeaderViewHolder) holder).headerTitle1.setText("STOCK ACTUAL");
//                ((FlooringActivity.CustomAdapterInventario.HeaderViewHolder) holder).headerTitle2.setText("SUGERIDO");
////                ((HeaderViewHolder) holder).headerTitle3.setText("CAUSAL SUGERIDO");
//                ((FlooringActivity.CustomAdapterInventario.HeaderViewHolder) holder).headerTitle4.setText("OBSERVACION");
//                ((FlooringActivity.CustomAdapterInventario.HeaderViewHolder) holder).headerTitle5.setText("CADUCIDAD");
//            }else */if (holder instanceof ItemViewHolder) {
//                ((ItemViewHolder) holder).lblSku.setText(mObject.getMarca());
//                ((ItemViewHolder) holder).txtventa.setText(cargarCaras(codigo_pdv, categoria, subcategoria, mObject.getMarca()));
//                ((ItemViewHolder) holder).txtventa.setTextColor(getResources().getColor(R.color.verde));
//
//            }
//        }
//
//        private BasePortafolioProductos getItem(int position) {
//            return values.get(position);
//        }
//        @Override
//        public int getItemCount() {
//            return values.size();
//        }
//        @Override
//        public int getItemViewType(int position) {
//            if (isPositionHeader(position))
//                return TYPE_HEADER;
//            return TYPE_ITEM;
//        }
//        private boolean isPositionHeader(int position) {
//            return position == 0;
//        }
//
////        public void filterList(ArrayList<BasePortafolioProductos> filteredList) {
////            listProductos = filteredList;
////            Log.i("LIST",listProductos.status()+"");
////            FlooringActivity.this.dataAdapter.notifyDataSetChanged();
////        }
//
//        public class HeaderViewHolder extends RecyclerView.ViewHolder{
//
//            public TextView headerTitle, headerTitle1, headerTitle2, /*headerTitle3,*/ headerTitle4, headerTitle5, headerTitle6;
//
//            public HeaderViewHolder(View itemView) {
//                super(itemView);
//                headerTitle = (TextView)itemView.findViewById(R.id.lblSku);
//                headerTitle1 = (TextView)itemView.findViewById(R.id.lblStock);
//                headerTitle2 = (TextView)itemView.findViewById(R.id.lblSugerido);
////                headerTitle3 = (TextView)itemView.findViewById(R.id.lblCausal);
//                headerTitle4 = (TextView)itemView.findViewById(R.id.lblDetalle);
//                headerTitle5 = (TextView)itemView.findViewById(R.id.lblCaducidad);
//            }
//        }
//
//        public class ItemViewHolder extends RecyclerView.ViewHolder {
//
//            public TextView lblSku;
//            public CheckBox chkGuardar;
//            public EditText txtventa;
//            public EditText txtSugerido;
//            public EditText txtObservacion;
//            public ImageButton btnCamera;
//            public TextView txtFecha;
//            public ImageButton btnFechaProd;
//
//            public ItemViewHolder(View itemView) {
//                super(itemView);
//
//                lblSku = (TextView)itemView.findViewById(R.id.lblSku);
//                txtventa = (EditText) itemView.findViewById(R.id.txtventa);
//                btnCamera = (ImageButton) itemView.findViewById(R.id.ibCargarFotoShare);
//
//                if (validaPermisos()) {
//                    btnCamera.setEnabled(true);
//                }else{
//                    btnCamera.setEnabled(false);
//                }
//
//                txtventa.addTextChangedListener(new TextWatcher() {
//                    @Override
//                    public void onTextChanged(CharSequence s, int start, int before, int count) {}
//
//                    @Override
//                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
//
//                    @Override
//                    public void afterTextChanged(Editable s) {
//                        txtventa.setTextColor(getResources().getColor(R.color.negro));
//                    }
//                });
//
//                btnGuardar.setOnClickListener(new View.OnClickListener() {
//
//                    @Override
//                    public void onClick(View v) {
//
//                        View view;
//                        EditText et = null;
//                        TextView tv;
//                        int listLength = listview.getChildCount();
//                        valueOfEditText = new String[listLength+1];
//                        valueOfTextView = new String[listLength+1];
//                        for (int i = 0; i < listLength; i++) {
//                            String et_otros = lblOtros.getText().toString();
//                            view = listview.getChildAt(i);
//                            et = (EditText) view.findViewById(R.id.txtventa);
//                            tv = (TextView) view.findViewById(R.id.lblSku);
//                            valueOfEditText[i] = et.getText().toString();
//                            valueOfTextView[i] = tv.getText().toString();
//
//                            valueOfEditText[listLength] = et_otros;
//                            valueOfTextView[listLength] = "Otros";
//                        }
//
//                        venta = txtventa.getText().toString();
//                        sku = lblSku.getText().toString();
//                        String razones = spRazones.getSelectedItem().toString();
//
////                        image = getStringImage(bitmapfinal);
//
//                        int contador_llenos = 0;
//                        if (!razones.equalsIgnoreCase("SELECCIONE")) {
//
//                        if (txtCaras!=null && !txtCaras.getText().toString().equals("") && !lblOtros.getText().toString().equals("")) {
//                            if (verificacion_resultado()==Integer.parseInt(lblOtros.getText().toString())) {
//                                for (int i = 0; i < listLength; i++) {
//                                    view = listview.getChildAt(i);
//                                    et = (EditText) view.findViewById(R.id.txtventa);
//                                    if (!et.getText().toString().trim().isEmpty()) {
//                                        contador_llenos++;
//                                    }
//                                }
//
//                                Log.i("CONTADOR_LLENOS",contador_llenos+"");
//                                Log.i("LISTLENGTH",listLength+"");
//
//                                if (contador_llenos==listLength) {
//                                    insertData(razones);
//                                    for (int i = 0; i < listLength; i++) {
//                                        view = listview.getChildAt(i);
//                                        et = (EditText) view.findViewById(R.id.txtventa);
//                                        et.setText("");
//                                    }
//                                    txtCaras.setText("");
//                                    lblOtros.setText("");
//                                }else{
//                                    Toast.makeText(getContext(), "Ingresar cantidad de caras", Toast.LENGTH_SHORT).show();
//                                }
//                            }else{
//                                Toast.makeText(getContext(), "El valor de Otros es incorrecto, se volvera a calcular automaticamente", Toast.LENGTH_SHORT).show();
//                                resultado();
//                            }
//                        }else{
//                            Toast.makeText(getContext(),"Ingresar el total de caras o calcule otros antes de almacenar los datos.",Toast.LENGTH_SHORT).show();
//                        }
//                        }else{
//                            Toast.makeText(getContext(), "Debe llenar todo el formulario", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//
//                btnCamera.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        sku_list = lblSku.getText().toString();
//                        cargarImagen();
////                        Toast.makeText(getContext(), lblSku.getText().toString(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//            }
//        }
//    }

    public class CustomAdapterShare extends ArrayAdapter<BasePortafolioProductos> {

        public List<BasePortafolioProductos> values;
        public Context context;
        boolean[] checkBoxState;

        public CustomAdapterShare(Context context, List<BasePortafolioProductos> values) {
            super(context, 0, values);
            this.values = values;
            checkBoxState=new boolean[values.size()];
        }

        public class ViewHolder{
            TextView lblSku;
            CheckBox check; // agregado GT
            EditText txtventa;
            Spinner spRazon;

            LinearLayout lOtraRazon;

            EditText otraRazon;
        }

        @Override
        public int getViewTypeCount() {
            // TODO Auto-generated method stub
            return values.size();
        }

        @Override
        public int getItemViewType(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            //Obtener Instancia Inflater
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            ViewHolder vHolder = null;
            //Comprobar si el View existe
            //Si no existe inflarlo
            if (null == convertView) {
                convertView = inflater.inflate(R.layout.list_row_option_share, parent, false); // Modificacion (list_row_option) GT

                //Obtener instancias de los elementos
                vHolder = new ViewHolder();
                vHolder.lblSku = (TextView) convertView.findViewById(R.id.lblSku);
                vHolder.check = (CheckBox) convertView.findViewById(R.id.checkPresencia);
                vHolder.check.setVisibility(View.GONE);
            //    vHolder.otraRazon = (EditText) convertView.findViewById(R.id.txtOtraRazon);
            //    vHolder.lOtraRazon = (LinearLayout) convertView.findViewById(R.id.lOtraRazon);
                //   vHolder.txtunidad = (EditText) convertView.findViewById(R.id.txtunidad);
                vHolder.txtventa = (EditText) convertView.findViewById(R.id.txtventa);
              //  vHolder.spRazon = (Spinner) convertView.findViewById(R.id.spRazon);
                //cargarMotivos(vHolder);
                convertView.setTag(vHolder);

                //checkGuardar.setEnabled(true);
            } else { vHolder = (ViewHolder) convertView.getTag(); }

            if (values.size() > 0) {
                //set the data to be displayed
                String marca = values.get(position).getMarca();

                vHolder.lblSku.setText(marca);
                String caras = cargarCaras(codigo_pdv, categoria, subcategoria, marca);

                // En caso que no se encuentren registros
                if (caras == null || caras == "") caras = "0";

                if (Integer.parseInt(caras) > 0){

                    vHolder.txtventa.setText(caras);
                    vHolder.txtventa.setTextColor(getResources().getColor(R.color.color_revelado));
                    vHolder.lblSku.setTextColor(getResources().getColor(R.color.color_revelado));

                }

/*
                ViewHolder finalVHolder1 = vHolder;
                vHolder.spRazon.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        String razon = adapterView.getSelectedItem().toString();

                        if (razon.equals("Otra Razon")){
                            finalVHolder1.lOtraRazon.setVisibility(View.VISIBLE);
                        }else{
                            finalVHolder1.lOtraRazon.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
*/


                //    vHolder.txtventa.setTextColor(getResources().getColor(R.color.colorBlack));
                 //   vHolder.lblSku.setTextColor(getResources().getColor(R.color.colorBlack));






                /*
                if (Integer.parseInt(caras) > 0){
                    vHolder.lblSku.setTextColor(getResources().getColor(R.color.color_revelado));
                }*/

                // sku = vHolder.lblSku.getText().toString();
                // checkGuardar.setEnabled(false);
                final ViewHolder finalv =vHolder;

                ViewHolder finalVHolder = vHolder;


                btnGuardar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String categoria = spCategoria.getSelectedItem().toString();
                    //    String subcategoria = spSubcategoria.getSelectedItem().toString();
                        String otros = lblOtros.getText().toString();
                        shareElements = new ArrayList<ShareElements>();
//                        String razon = spRazones.getSelectedItem().toString();

                        if (esFormularioValido(categoria, subcategoria, otros) && listadoEsValido()) {
                            View view;
                            EditText txtCaras = null;
                            TextView lblMarca = null;
                            Spinner spRazon = null;
                            int listLength = listview.getChildCount();

                            for (int i = 0; i < listLength; i++) {
                                view = listview.getChildAt(i);
                                txtCaras = (EditText) view.findViewById(R.id.txtventa);
                                lblMarca = (TextView) view.findViewById(R.id.lblSku);
                            //    spRazon = (Spinner) view.findViewById(R.id.spRazon);

                                ShareElements shareElement = new ShareElements();
                                shareElement.setMarca(lblMarca.getText().toString());
                                shareElement.setCaras(txtCaras.getText().toString());
                            //    shareElement.setRazon(spRazon.getSelectedItem().toString());
                                shareElements.add(shareElement);
                            }

                            int contador_llenos = 0;

                            if (verificacion_resultado() == Integer.parseInt(lblOtros.getText().toString())) {
                                String et_otros = lblOtros.getText().toString();

                                ShareElements shareElement = new ShareElements();
                                shareElement.setMarca("Otros");
                                shareElement.setCaras(et_otros);
                                shareElement.setRazon("Otros");
                                shareElements.add(shareElement);

                                insertData();
                                for (int i = 0; i < listLength; i++) {
                                    view = listview.getChildAt(i);
                                    txtCaras = (EditText) view.findViewById(R.id.txtventa);
                                    txtCaras.setText("");
                                }
                                ShareFragment.this.txtCaras.setText("");
                                ShareFragment.this.spCategoria.setSelection(0);
                            //    ShareFragment.this.spSubcategoria.setSelection(0);
                                lblOtros.setText("");
                            } else {
                                Toast.makeText(getContext(), "El valor de Otros es incorrecto, se volvera a calcular automaticamente", Toast.LENGTH_SHORT).show();
                                resultado();
                            }
                        }
                    }
                });













//
//                btnGuardar.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        String categoria = spCategoria.getSelectedItem().toString();
//                    //    String subcategoria = spSubcategoria.getSelectedItem().toString();
//                        String subcategoria = "";
//                        String otros = lblOtros.getText().toString();
//                        shareElements = new ArrayList<ShareElements>();
////                        String razon = spRazones.getSelectedItem().toString();
//
//                        if(listview.getVisibility() == View.VISIBLE){
//                            //if(!lblOtros.getText().toString().equals("")){
//         //                   if(!otros.trim().isEmpty()) {
//                                if (esFormularioValido(categoria, subcategoria, otros) && listadoEsValido()) {
//                                    if(verificacion_resultado() == Integer.parseInt(lblOtros.getText().toString())) {
//                                        EditText txtCaras = null;
//                                        TextView lblMarca = null;
//                                        Spinner spRazon = null;
//                                        EditText txtOtraRazon = null;
//                                        int listLength = listview.getChildCount();
//
//                                        for (int i = 0; i < listLength; i++) {
//                                            view = listview.getChildAt(i);
//                                            String et_otros = lblOtros.getText().toString();
//                                            txtCaras = (EditText) view.findViewById(R.id.txtventa);
//                                            lblMarca = (TextView) view.findViewById(R.id.lblSku);
//                                            //     spRazon = (Spinner) view.findViewById(R.id.spRazon);
//                                            //     txtOtraRazon = (EditText) view.findViewById(R.id.txtOtraRazon);
//
//                                            ShareElements shareElement = new ShareElements();
//                                            shareElement.setMarca(lblMarca.getText().toString());
//                                            shareElement.setCaras(txtCaras.getText().toString());
//
//                                        /*
//                                        if (spRazon.getSelectedItem().toString().equals("Otra Razon")){
//                                            shareElement.setRazon(txtOtraRazon.getText().toString());
//                                        }else{
//                                            shareElement.setRazon(spRazon.getSelectedItem().toString());
//                                        }
//                                        */
//
//                                            shareElements.add(shareElement);
//                                        }
//
//                                        int contador_llenos = 0;
//
//
//                                        if (verificacion_resultado() == Integer.parseInt(lblOtros.getText().toString())) {
//                                            String et_otros = lblOtros.getText().toString();
//
//                                            ShareElements shareElement = new ShareElements();
//                                            shareElement.setMarca("Otros");
//                                            shareElement.setCaras(et_otros);
//                                            shareElement.setRazon("Otros");
//                                            shareElements.add(shareElement);
//
//                                            insertData();
//                                            for (int i = 0; i < listLength; i++) {
//                                                view = listview.getChildAt(i);
//                                                txtCaras = (EditText) view.findViewById(R.id.txtventa);
//                                                txtCaras.setText("");
//                                                //spRazon.setSelection(0);
//                                            }
//                                            ShareFragment.this.txtCaras.setText("");
//                                            ShareFragment.this.spCategoria.setSelection(0);
//                                            lblOtros.setText("");
//                                        } else {
//                                            Toast.makeText(getContext(), "El valor de Otros es incorrecto, se volvera a calcular automaticamente", Toast.LENGTH_SHORT).show();
//                                            resultado();
//                                        }
//
//                                        // Descomentar cuando se habilite el top 3 otros y comentar el if de arriba
////                                        if (Integer.parseInt(lblOtros.getText().toString()) != 0) {
////
////                                            if(verificacion_resultado()==Integer.parseInt(lblOtros.getText().toString())) {
////                                                String et_otros = lblOtros.getText().toString();
////
////                                                int totalOtrosTotal = 0;
////                                                int totalOtros1 = 0;
////                                                int totalOtros2 = 0;
////                                                int totalOtros3 = 0;
////
////                                                String txt_Marca1 = txtMarca1.getText().toString();
////                                                String txt_Marca2 = txtMarca2.getText().toString();
////                                                String txt_Marca3 = txtMarca3.getText().toString();
////
////                                                String txt_Caras1 = txtCaras1.getText().toString();
////                                                String txt_Caras2 = txtCaras2.getText().toString();
////                                                String txt_Caras3 = txtCaras3.getText().toString();
////
////                                                if (Integer.parseInt(et_otros) > 0) {
////                                                    if (topOtrosValidar()) {
////                                                        if(verificacion_resultado()==Integer.parseInt(lblOtros.getText().toString())) {
////                                                            if (!txt_Marca1.isEmpty() && !txt_Caras1.isEmpty()) {
////                                                                ShareElements shareElement = new ShareElements();
////                                                                shareElement.setMarca("OTROS 1: "+txt_Marca1);
////                                                                shareElement.setCaras(txt_Caras1);
////                                                                shareElement.setRazon("Otros");
////                                                                shareElements.add(shareElement);
////
////                                                                totalOtros1 = Integer.parseInt(txt_Caras1);
////                                                                totalOtrosTotal += totalOtros1;
////                                                            }
////
////                                                            if (!txt_Marca2.isEmpty() && !txt_Caras2.isEmpty()) {
////                                                                ShareElements shareElement = new ShareElements();
////                                                                shareElement.setMarca("OTROS 2: "+txt_Marca2);
////                                                                shareElement.setCaras(txt_Caras2);
////                                                                shareElement.setRazon("Otros");
////                                                                shareElements.add(shareElement);
////
////                                                                totalOtros2 = Integer.parseInt(txt_Caras2);
////                                                                totalOtrosTotal += totalOtros2;
////                                                            }
////
////                                                            if (!txt_Marca3.isEmpty() && !txt_Caras3.isEmpty()) {
////                                                                ShareElements shareElement = new ShareElements();
////                                                                shareElement.setMarca("OTROS 3: "+txt_Marca3);
////                                                                shareElement.setCaras(txt_Caras3);
////                                                                shareElement.setRazon("Otros");
////                                                                shareElements.add(shareElement);
////
////                                                                totalOtros3 = Integer.parseInt(txt_Caras3);
////                                                                totalOtrosTotal += totalOtros3;
////                                                            }
////
////                                                            if(verificacion_resultado()==Integer.parseInt(lblOtros.getText().toString())) {
////                                                                int valorCarasMarca1 = (otrasMarcas != null && otrasMarcas.size() > 0 && otrasMarcas.get(0) != null && otrasMarcas.get(0).getCaras() != null) ? Integer.parseInt(otrasMarcas.get(0).getCaras()) : 0;
////                                                                int valorCarasMarca2 = (otrasMarcas != null && otrasMarcas.size() > 1 && otrasMarcas.get(1) != null && otrasMarcas.get(1).getCaras() != null) ? Integer.parseInt(otrasMarcas.get(1).getCaras()) : 0;
////                                                                int valorCarasMarca3 = (otrasMarcas != null && otrasMarcas.size() > 2 && otrasMarcas.get(2) != null && otrasMarcas.get(2).getCaras() != null) ? Integer.parseInt(otrasMarcas.get(2).getCaras()) : 0;
////
////                                                                esDiferenteCaras = totalOtros1 != valorCarasMarca1 ||
////                                                                        totalOtros2 != valorCarasMarca2 ||
////                                                                        totalOtros3 != valorCarasMarca3;
////
////                                                                if (esDiferenteCaras) {
////                                                                    if (totalOtros1 !=0 && totalOtros2 ==0 && totalOtros3 ==0) {
////                                                                        if (totalOtrosTotal == Integer.parseInt(et_otros)){
////                                                                            insertData();
////                                                                        }else{
////                                                                            Toast.makeText(getContext(), "El Otros 1 deben ser igual al Total Otros", Toast.LENGTH_LONG).show();
////                                                                        }
////                                                                    }
////
////                                                                    if (totalOtros1 !=0 && totalOtros2 !=0 && totalOtros3 ==0 ) {
////                                                                        if (totalOtrosTotal == Integer.parseInt(et_otros)) {
////                                                                            insertData();
////                                                                        } else {
////                                                                            Toast.makeText(getContext(), "El Otros 1 y 2 deben ser igual al Total Otros", Toast.LENGTH_LONG).show();
////                                                                        }
////                                                                    }
////
////                                                                    if (totalOtros1 !=0 && totalOtros2 !=0 && totalOtros3 !=0 ) {
////                                                                        if (totalOtrosTotal <= Integer.parseInt(et_otros)) {
////                                                                            insertData();
////                                                                        } else {
////                                                                            Toast.makeText(getContext(), "El Top Otros 3 deben ser menor o igual al Total Otros", Toast.LENGTH_LONG).show();
////                                                                        }
////                                                                    }
////                                                                }else{
////                                                                    alertDialogRevelo();
////                                                                }
////                                                            } else {
////                                                                Toast.makeText(getContext(), "El valor de Otros es incorrecto, se volvera a calcular automaticamente", Toast.LENGTH_LONG).show();
////                                                                resultado();
////                                                            }
////                                                        } else {
////                                                            Toast.makeText(getContext(), "El valor de Otros es incorrecto, se volvera a calcular automaticamente", Toast.LENGTH_LONG).show();
////                                                            resultado();
////                                                        }
////                                                    } else {
////                                                        Toast.makeText(getContext(), "Deben llenar al menos 1 marca otros y sus caras. ", Toast.LENGTH_LONG).show();
////                                                    }
////                                                } else {
////                                                    Toast.makeText(getContext(), "El valor de Otros es incorrecto, se volvera a calcular automaticamente", Toast.LENGTH_LONG).show();
////                                                    resultado();
////                                                }
////                                            }else{
////                                                Toast.makeText(getContext(), "El valor de Otros es incorrecto, se volvera a calcular automaticamente", Toast.LENGTH_SHORT).show();
////                                                resultado();
////                                            }
////                                        }else{
////                                            if (Integer.parseInt(lblOtros.getText().toString()) == 0){
////                                                insertData();
////                                            }
////                                        }
//
//                                    }else{
//                                        Toast.makeText(getContext(), "El valor de Otros es incorrecto, se volvera a calcular automaticamente", Toast.LENGTH_SHORT).show();
//                                        resultado();
//                                    }
//
//                                } //    ui
////                            } else {
////                                Toast.makeText(getContext(),"Ingresar el total de caras o calcule otros antes de almacenar los datos.",Toast.LENGTH_SHORT).show();
////                            }
//                        }
//                    }
//
//                });
//
            }
            //Devolver al ListView la fila creada y si me amas? seguro? del 1 al 10? tengo que esforzarme más! para llegar a 10, por que no?
            return convertView;
        }
    }

    private void alertDialogRevelo() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
        LayoutInflater inflater2 = getLayoutInflater();
        View dialogView = inflater2.inflate(R.layout.alertdialog_revelo, null);
        builder.setView(dialogView);

        Button btn = (Button) dialogView.findViewById(R.id.boton);
        Button btnCancelar = (Button) dialogView.findViewById(R.id.btnCancelar);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertData();
                limpiarDatos();
                adMsj.dismiss();
                adMsj = null;

            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  if (adMsj != null) {
                    adMsj.dismiss();
                    adMsj = null;
               // }
            }
        });

        adMsj = builder.create();
        adMsj.show();
    }




    public static boolean contieneUnaPalabraValida(String str) {
        return str.matches("^[a-zA-Z\\sáÁéÉíÍóÓúÚüÜñÑ]{2,}$");
    }
    private boolean esFormularioValido(String categoria, String subcategoria, String otros) {
        Log.i("FV:", otros + " - " + contieneUnaPalabraValida(otros));
        if (categoria.equalsIgnoreCase("SELECCIONE")) {
            Toast.makeText(getContext(), "Debe seleccionar una categoria", Toast.LENGTH_SHORT).show();
            return false;
        }
        /*
        if (subcategoria.equalsIgnoreCase("SELECCIONE")) {
            Toast.makeText(getContext(), "Debe seleccionar una categoria", Toast.LENGTH_SHORT).show();
            return false;
        }*/

        if (txtTotalCaras.getText().toString().equals("")) {
            Toast.makeText(getContext(), "Debe ingresar un total de caras", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!txtTotalCaras.getText().toString().equals("")) {
            double n = Double.parseDouble(txtTotalCaras.getText().toString());
            if (n<1) {
                Toast.makeText(getContext(), "Total de caras no puede ser cero", Toast.LENGTH_SHORT).show();
                return false;
            }
        }




/*
        if (lblOtros.getText().toString().equals("")) {
            Toast.makeText(getContext(), "Debe calcular el valor de otros", Toast.LENGTH_SHORT).show();
            lblOtros.setBackgroundColor(Color.TRANSPARENT);
            return false;
        }*/

        if (otros.contains("-")) {
            Toast.makeText(getContext(), "El valor de otros no puede ser negativo", Toast.LENGTH_SHORT).show();
            lblOtros.setBackgroundColor(Color.TRANSPARENT);
            return false;
        }



    if (llComentario.getVisibility() == View.VISIBLE) {
        if (spCausal.getSelectedItem().toString().equals("Seleccione")) {
            Toast.makeText(getContext(), "Debe seleccionar un comentario.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (spCausal.getSelectedItem().toString().equals("Otros")) {
            if (txtComentario.getText().toString().equals("")) {
                Toast.makeText(getContext(), "Debe ingresar un comentario en 'Otros'.", Toast.LENGTH_SHORT).show();
                return false;
            } else {
                if (!contieneUnaPalabraValida(txtComentario.getText().toString())) {
                    Toast.makeText(getContext(), "El texto ingresado no es valido", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        }
    }


        return true;
    }

    private boolean topOtrosValidar(){
        return (!txtMarca1.getText().toString().isEmpty() && !txtCaras1.getText().toString().isEmpty()) ||
                (!txtMarca2.getText().toString().isEmpty() && !txtCaras2.getText().toString().isEmpty()) ||
                (!txtMarca3.getText().toString().isEmpty() && !txtCaras3.getText().toString().isEmpty());
    }



    private boolean listadoEsValido() {
        View view;
        TextView lblMarca = null;
        EditText txtCaras = null;
        EditText otraRazon = null;
        Spinner spRazon = null;

        int listLength = listview.getChildCount();
        for (int i = 0; i < listLength; i++) {
            view = listview.getChildAt(i);
            lblMarca = (TextView) view.findViewById(R.id.lblSku);
            txtCaras = (EditText) view.findViewById(R.id.txtventa);
            spRazon = (Spinner) view.findViewById(R.id.spRazon);
            otraRazon = (EditText) view.findViewById(R.id.txtOtraRazon);
            String producto = lblMarca.getText().toString();




            if (txtCaras.getText().toString().trim().isEmpty()) {
                Toast.makeText(getContext(), "Debe ingresar la cara en el producto: " + producto, Toast.LENGTH_SHORT).show();
                return false;
            }


            if (lblOtros.getText().toString().equals("")) {
                Toast.makeText(getContext(), "Debe calcular el valor de otros", Toast.LENGTH_SHORT).show();
                lblOtros.setBackgroundColor(Color.TRANSPARENT);
                return false;
            }


            /*
            if (spRazon.getSelectedItem().toString().equalsIgnoreCase("SELECCIONE")) {
                Toast.makeText(getContext(), "Debe seleccionar la razón en el producto: " + producto, Toast.LENGTH_SHORT).show();
                return false;
            }

            if(spRazon.getSelectedItem().toString().equalsIgnoreCase("Otra Razon")){
                if (otraRazon.getText().toString().isEmpty()){
                    Toast.makeText(getContext(), "No has ingresado la razón en el producto: "+producto, Toast.LENGTH_SHORT).show();
                    return false;
                }
            }*/


        }
        return true;
    }
}