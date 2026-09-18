package com.luckyecuador.app.PintucoAPP.ui.logros;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.INTERNET;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_OK;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;
import com.luckyecuador.app.PintucoAPP.Conexion.VerificarNet;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertPdvFotografico;
import com.luckyecuador.app.PintucoAPP.DataBase.DatabaseHelper;
import com.luckyecuador.app.PintucoAPP.DataBase.Provider;
import com.luckyecuador.app.PintucoAPP.R;
import com.luckyecuador.app.PintucoAPP.Sync.SyncAdapter;
import com.luckyecuador.app.PintucoAPP.Utils.DeveloperOptions;
import com.luckyecuador.app.PintucoAPP.Utils.RequestPermissions;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class FotograficoFragment extends Fragment implements View.OnClickListener, TextWatcher, AdapterView.OnItemSelectedListener {

    //BASE SQLITE
    DatabaseHelper handler;

    //Photo Camera
    private ImageView imageView;
    private Bitmap bitmap;
    private Bitmap bitmapfinal;
    private long lengthbmp;
    //Photo Camera
    final int COD_SELECCIONA=10;
    final int COD_FOTO=20;

    private final String CARPETA_RAIZ="PintucoApp/";
    private final String RUTA_IMAGEN=CARPETA_RAIZ+"ExhibicionAntDesp";
    String path;

    private EditText txtComentario;
    private ImageButton btnCamera;
    private Button btnGallery;
    private Button btnSave;

    private String fecha, hora, user, id_pdv, pos_name;

    String codigo, supervisor, categoria, subcategoria, marca, logro;

    private Spinner spSector;
    private Spinner spCategoria;
    private Spinner spBrand;

    private String fabricante = "Alicorp";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //este modulo es el mismo FotograficoActivity, solo que dentro del pdv
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_fotografico, container, false);

        // Evita que el teclado no cubra los sku
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        // Establece la orientacion
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        handler = new DatabaseHelper(getContext(), Provider.DATABASE_NAME,null,1);
        LoadData();
        new DeveloperOptions().modalDevOptions(getActivity());
        RequestPermissions requestPermissions = new RequestPermissions(getContext(), getActivity());
        requestPermissions.showPermissionDialog();

        imageView = (ImageView) rootView.findViewById(R.id.imageView);
        btnCamera = (ImageButton) rootView.findViewById(R.id.btnCameraLocal);
        btnGallery= (Button) rootView.findViewById(R.id.btnGallery);
        btnSave= (Button) rootView.findViewById(R.id.btnSave);

        //txtCodigo = (EditText) findViewById(R.id.txtCodigo);
        txtComentario = (EditText) rootView.findViewById(R.id.txtComentario);

        txtComentario.setSingleLine(false);  //add this
        txtComentario.setLines(4);
        txtComentario.setMaxLines(5);
        txtComentario.setGravity(Gravity.LEFT | Gravity.TOP);
        txtComentario.setHorizontalScrollBarEnabled(false); //this

        //txtCodigo.addTextChangedListener(this);

        //spExhibicion = (Spinner) findViewById(R.id.spExhibicion);
        //spLocal = (Spinner) findViewById(R.id.spLocal);
        spSector = (Spinner) rootView.findViewById(R.id.spSector);
        spCategoria = (Spinner) rootView.findViewById(R.id.spCategoria);
        spBrand = (Spinner) rootView.findViewById(R.id.spMarca);

        btnCamera.setOnClickListener(this);
        btnGallery.setOnClickListener(this);
        btnSave.setOnClickListener(this);

        //filtrarLocal("5001470");

        validaPermisos();

        //filtrarLocal(codigo);
        filtrarSector(fabricante);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        binding = null;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView== spSector) {
            try{
                categoria = adapterView.getItemAtPosition(i).toString();
                filtrarCategoria(categoria, fabricante);
            }catch (Exception e) {
                Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
        if (adapterView==spCategoria)
        {
            try{
                subcategoria=adapterView.getItemAtPosition(i).toString();
                //filtrarMarca(logro,subcategoria);
                filtrarBrand(categoria, subcategoria, fabricante);
            }catch (Exception e) {
                Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }

    public void onResume() {
        super.onResume();
        Log.i("ACTIVITY", "ON RESUME");
        new DeveloperOptions().modalDevOptions(getActivity());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    private boolean validaPermisos() {

        if (Build.VERSION.SDK_INT<Build.VERSION_CODES.M) {
            return true;
        }

        if ((getContext().checkSelfPermission(CAMERA)== PackageManager.PERMISSION_GRANTED)&&
            (getContext().checkSelfPermission(WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED) &&
            (getContext().checkSelfPermission(ACCESS_COARSE_LOCATION)==PackageManager.PERMISSION_GRANTED) &&
            (getContext().checkSelfPermission(ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED) &&
            (getContext().checkSelfPermission(INTERNET)==PackageManager.PERMISSION_GRANTED)) {
            return true;
        }

        if ((shouldShowRequestPermissionRationale(CAMERA)) ||
                (shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)) ||
                (shouldShowRequestPermissionRationale(ACCESS_COARSE_LOCATION)) ||
                (shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)) ||
                (shouldShowRequestPermissionRationale(INTERNET))) {
            cargarDialogoRecomendacion();
        }else{
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE,CAMERA,ACCESS_COARSE_LOCATION,ACCESS_FINE_LOCATION,INTERNET},100);
        }

        return false;
    }

    private void cargarDialogoRecomendacion() {
        AlertDialog.Builder dialogo = new AlertDialog.Builder(getContext());
        dialogo.setTitle("Permisos Desactivados");
        dialogo.setMessage("Debe aceptar los permisos para el correcto funcionamiento de la App");

        dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE,CAMERA,ACCESS_COARSE_LOCATION,ACCESS_FINE_LOCATION,INTERNET},100);
            }
        });
        dialogo.show();
    }

    @Override
    public void onClick(View view) {
        /*if (view == btnGallery) {
            showFileChooser();
        }*/
        if (view == btnCamera) {
            cargarImagen();
        }
        if (view == btnSave) {
            enviarDatos();
        }
    }

    public void filtrarSector(String fabricante) {
        List<String> operadores = handler.getSectorLogros(fabricante);
        if (operadores.size()==2) {
            operadores.remove(0);
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, operadores);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSector.setAdapter(dataAdapter);
        spSector.setOnItemSelectedListener(this);
    }

    public void filtrarCategoria(String sector, String fabricante) {
        List<String> operadores = handler.getCategoriaLogros(sector, fabricante);
        if (operadores.size()==2) {
            operadores.remove(0);
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, operadores);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategoria.setAdapter(dataAdapter);
        spCategoria.setOnItemSelectedListener(this);
    }

    public void filtrarBrand(String sector, String categoria, String fabricante) {
        List<String> operadores = handler.getBrandLogros(categoria,sector, fabricante);
        if (operadores.size()==2) {
            operadores.remove(0);
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, operadores);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spBrand.setAdapter(dataAdapter);
        spBrand.setOnItemSelectedListener(this);
    }

    /**/
    public void enviarDatos() {
        obtenerFecha();
        if (imageView != null && imageView.getDrawable() != null) {
            //if (esFormularioValido()) {
                String image = getStringImage(bitmapfinal);

                //obtenervaloresseleccinados();
                String canal = handler.getCanalPDV(id_pdv);

                ContentValues values = new ContentValues();
                values.put(ContractInsertPdvFotografico.Columnas.PHARMA_ID, id_pdv);
                values.put(ContractInsertPdvFotografico.Columnas.CODIGO, pos_name ); //pos name
                values.put(ContractInsertPdvFotografico.Columnas.USUARIO, user);
                values.put(ContractInsertPdvFotografico.Columnas.SUPERVISOR, supervisor);
                values.put(ContractInsertPdvFotografico.Columnas.FECHA, fecha);
                values.put(ContractInsertPdvFotografico.Columnas.HORA, hora);
                values.put(ContractInsertPdvFotografico.Columnas.CANAL, canal);
                values.put(ContractInsertPdvFotografico.Columnas.CODIGO_PDV, id_pdv);
                values.put(ContractInsertPdvFotografico.Columnas.MERCADERISTA, user);
                values.put(ContractInsertPdvFotografico.Columnas.FOTO, image);
                values.put(Constantes.PENDIENTE_INSERCION, 1);

                getContext().getContentResolver().insert(ContractInsertPdvFotografico.CONTENT_URI, values);
                if (VerificarNet.hayConexion(getContext())) {
                    SyncAdapter.sincronizarAhora(getContext(), true, Constantes.insertPdvFotografico, null);
                    Toast.makeText(getContext(), "Guardando la información en el servidor.", Toast.LENGTH_SHORT).show();
                    vaciarCampos();
                } else {
                    Toast.makeText(getContext(), "Guardando la información en el teléfono. \n Deberá ser sincronizada", Toast.LENGTH_SHORT).show();
                    vaciarCampos();
                }
            //}
        } else {
            Toast.makeText(getContext(), "Por favor tomar una foto para poder ser enviado.", Toast.LENGTH_SHORT).show();
        }

    }

    private boolean esFormularioValido() {
        if (txtComentario.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(getContext(),"Debe llenar el formulario",Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    /**/
    public void vaciarCampos() {
        txtComentario.setText("");
        imageView.setImageResource(android.R.color.transparent);
    }


    public void obtenervaloresseleccinados() {
        subcategoria = spCategoria.getSelectedItem().toString();
        categoria = spSector.getSelectedItem().toString();
        marca = spBrand.getSelectedItem().toString();
        logro = txtComentario.getText().toString();
        //codigo = txtCodigo.getText().toString();
    }

    //************METODOS PARA TAKE-PHOTO Y UPLOAD
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

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
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
            imageView.setImageBitmap(scaled);
            bitmapfinal = ((BitmapDrawable)imageView.getDrawable()).getBitmap();

        } catch (Exception e) {
            AlertDialog alertDialog1;
            alertDialog1 = new AlertDialog.Builder(getContext()).create();
            alertDialog1.setTitle("Message");
            alertDialog1.setMessage("Notificar \t "+e.toString());
            alertDialog1.show();
            Log.e("compressBitmap", "Error on compress file");
        }
    }


    //Metodo que sube la imagen al servidor
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public String getStringImage(Bitmap bmp) {
        String encodedImage;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //Comprime la Imagen canal, calidad y outputstream
        bmp.compress(Bitmap.CompressFormat.JPEG, 35, baos);
        byte[] imageBytes = baos.toByteArray();
        encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    public void LoadData() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        id_pdv = sharedPreferences.getString(Constantes.IDPDV,Constantes.NODATA);
        pos_name = sharedPreferences.getString(Constantes.PDV,Constantes.NODATA);
        user = sharedPreferences.getString(Constantes.USER,Constantes.NODATA);
        codigo = sharedPreferences.getString(Constantes.CODIGO, Constantes.NODATA);
        supervisor = sharedPreferences.getString(Constantes.SUPERVISOR,Constantes.NODATA);

        Log.i("USER FOTO ACTIVITY:",user);
    }

    public void obtenerFecha() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-5"));
        Date currentLocalTime = cal.getTime();
        DateFormat date = new SimpleDateFormat("dd/MM/yyy");
        date.setTimeZone(TimeZone.getTimeZone("GMT-5"));
        fecha = date.format(currentLocalTime);

        DateFormat hour = new SimpleDateFormat("HH:mm:ss");
        hour.setTimeZone(TimeZone.getTimeZone("GMT-5"));
        hora = hour.format(currentLocalTime);
//            return localTime;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

    @Override
    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
        //codigo = txtCodigo.getText().toString().toUpperCase().trim();
        //filtrarLocal(codigo);
    }

    @Override
    public void afterTextChanged(Editable editable) {}

    /*public void filtrarLocal(String codigo) {
        handler = new DatabaseHelper(getContext(), Provider.DATABASE_NAME,null,1);
        List<String> operadores = handler.getNombreCom(codigo);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item, operadores);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spLocal.setAdapter(dataAdapter);
        spLocal.setOnItemSelectedListener(this);
    }*/
}