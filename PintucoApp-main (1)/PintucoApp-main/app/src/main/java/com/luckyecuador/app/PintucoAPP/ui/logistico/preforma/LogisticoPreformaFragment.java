package com.luckyecuador.app.PintucoAPP.ui.logistico.preforma;

import static android.app.Activity.RESULT_OK;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
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
import android.util.Base64;
import android.util.Log;
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

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.luckyecuador.app.PintucoAPP.CameraActivity;
import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;
import com.luckyecuador.app.PintucoAPP.Conexion.MarshMallowPermission;
import com.luckyecuador.app.PintucoAPP.Conexion.Mensajes;
import com.luckyecuador.app.PintucoAPP.Conexion.VerificarNet;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertLogisticoRelevo;
import com.luckyecuador.app.PintucoAPP.DataBase.DatabaseHelper;
import com.luckyecuador.app.PintucoAPP.DataBase.Provider;
import com.luckyecuador.app.PintucoAPP.R;
import com.luckyecuador.app.PintucoAPP.Sync.SyncAdapter;
import com.luckyecuador.app.PintucoAPP.Utils.DeveloperOptions;
import com.luckyecuador.app.PintucoAPP.Utils.GuardarLog;
import com.luckyecuador.app.PintucoAPP.Utils.ImageMark;
import com.luckyecuador.app.PintucoAPP.Utils.RequestPermissions;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class LogisticoPreformaFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    ProgressDialog progressDialog;
    android.app.AlertDialog alertDialog;

    //Views
    private Spinner spCategoria;
    private EditText txtComentario; // Agregado desde el XML

    private Button btnPropia;
    private Button btnCompetencia;

    DatabaseHelper handler;

    //Photo Camera
    public ImageButton btnCamera;
    public static ImageView imageView;
    Bitmap bitmap;
    private Bitmap bitmapfinal;
    final int COD_SELECCIONA = 10;
    final int COD_FOTO = 20;

    private Button btnGuardar;

    private final String CARPETA_RAIZ = "PintucoApp/";
    private final String RUTA_IMAGEN = CARPETA_RAIZ + "LogisticoPreforma";
    String path;
    String image = "";

    MarshMallowPermission marshMallowPermission;

    String categoria = "";
    String comentario = "";

    private String id_pdv, user, codigo_pdv, punto_venta, fecha, hora, format;
    private String canal, subcanal;
    private final String modulo = "LOGISTICA";

    private String tipo = "PREFORMA";
    private final String fabricante = "AkzoNobel S.A";
    private String tipo_logistico_guardado = "PREFORMA";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_preforma_logistico, container, false);


        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        handler = new DatabaseHelper(getContext(), Provider.DATABASE_NAME, null, 1);
        new DeveloperOptions().modalDevOptions(getActivity());
        RequestPermissions requestPermissions = new RequestPermissions(getContext(), getActivity());
        requestPermissions.showPermissionDialog();
        LoadData();

        marshMallowPermission = new MarshMallowPermission(getActivity());


        spCategoria = rootView.findViewById(R.id.spCategoria);
        txtComentario = rootView.findViewById(R.id.txtComentario);
        imageView = rootView.findViewById(R.id.ivFotoPreforma);
        btnCamera = rootView.findViewById(R.id.ibCargarFotoPreforma);
        btnPropia = rootView.findViewById(R.id.btnPropia);
        btnGuardar = rootView.findViewById(R.id.btnGuardar);
        btnCompetencia = rootView.findViewById(R.id.btnCompetencia);


        if (txtComentario != null) {
            txtComentario.setText("");
            txtComentario.setHint("Comentario");
        }

        btnPropia.setOnClickListener(this);
        btnCompetencia.setOnClickListener(this);
        btnCamera.setOnClickListener(this);
        btnGuardar.setOnClickListener(this);


        tipo = "PREFORMA";
        btnPropia.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.button_background_selected));
        btnPropia.setPadding(15, 15, 15, 15);
        btnCompetencia.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.button_background_unselected));
        btnCompetencia.setPadding(15, 15, 15, 15);


        filtrarCategoria(tipo, fabricante);

        new GuardarLog(getContext()).saveLog(user, "", "Ingreso al Módulo Logístico - Preforma");
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void filtrarCategoria(String tipo, String fabricante) {
        List<String> categorias = handler.getCategoriaLogisticoPreforma(tipo, fabricante, modulo);

        if (categorias == null || categorias.isEmpty()) {
            categorias = new ArrayList<>();
            categorias.add("Seleccione");
        } else if (categorias.size() >= 2) {

            if (categorias.get(0).equals("Seleccione") && categorias.get(1).equals("Seleccione")) {
                categorias.remove(0);
            }
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, categorias);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategoria.setAdapter(dataAdapter);
        spCategoria.setOnItemSelectedListener(this);
        spCategoria.post(() -> spCategoria.setSelection(0));
    }

    public void onResume() {
        super.onResume();
        Log.i("ACTIVITY", "ON RESUME");
        new DeveloperOptions().modalDevOptions(getActivity());
    }

    public void LoadData() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        id_pdv = sharedPreferences.getString(Constantes.IDPDV, Constantes.NODATA);
        user = sharedPreferences.getString(Constantes.USER, Constantes.NODATA);
        canal = sharedPreferences.getString(Constantes.TIPO, Constantes.NODATA);
        subcanal = sharedPreferences.getString(Constantes.SUBCANAL, Constantes.NODATA);
        codigo_pdv = sharedPreferences.getString(Constantes.CODIGO, Constantes.NODATA);
        punto_venta = sharedPreferences.getString(Constantes.PDV, Constantes.NODATA);
        fecha = sharedPreferences.getString(Constantes.FECHA, Constantes.NODATA);
        hora = sharedPreferences.getString(Constantes.HORA, Constantes.NODATA);
        format = sharedPreferences.getString(Constantes.FORMAT, Constantes.NODATA);
    }

    //************ METODOS PARA TAKE-PHOTO Y UPLOAD ************
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case COD_SELECCIONA:
                    Uri filePath = data.getData();
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), filePath);
                        scaleImage(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Error al cargar imagen", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case COD_FOTO:
                    MediaScannerConnection.scanFile(getContext(), new String[]{path}, null,
                            (path, uri) -> Log.i("Ruta de almacenamiento", "Path: " + path));
                    Bitmap bitmap = BitmapFactory.decodeFile(path);
                    scaleImage(bitmap);
                    break;
            }
        }
    }


    public void scaleImage(Bitmap bitmap) {
        try {
            if (bitmap == null) return;

            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-5"));
            Date currentLocalTime = cal.getTime();
            DateFormat hour = new SimpleDateFormat("HH:mm:ss");
            hour.setTimeZone(TimeZone.getTimeZone("GMT-5"));
            hora = hour.format(currentLocalTime);

            int mheight = (int) (bitmap.getHeight() * (1024.0 / bitmap.getWidth()));
            Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 1024, mheight, true);
            imageView.setImageBitmap(scaled);
            bitmapfinal = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

        } catch (Exception e) {
            AlertDialog alertDialog1;
            alertDialog1 = new AlertDialog.Builder(getContext()).create();
            alertDialog1.setTitle("Message");
            alertDialog1.setMessage("Notificar \t "+e.toString());
            alertDialog1.show();
            Log.e("compressBitmap", "Error on compress file");
        }
    }

    private void cargarImagen() {
        final CharSequence[] opciones = {"Tomar Foto", "Cargar Imagen", "Cancelar"};
        final AlertDialog.Builder alertOpciones = new AlertDialog.Builder(getContext());
        alertOpciones.setTitle("Seleccione una Opción");
        alertOpciones.setItems(opciones, (dialogInterface, i) -> {
            if (opciones[i].equals("Tomar Foto")) {
                Intent n = new Intent(getContext(), CameraActivity.class);
                n.putExtra("activity", "logis_prefor");
                startActivity(n);
            } else if (opciones[i].equals("Cargar Imagen")) {
                openGallery();
            } else {
                dialogInterface.dismiss();
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
        File fileImagen = new File(Environment.getExternalStorageDirectory(), RUTA_IMAGEN);
        boolean isCreada = fileImagen.exists();
        String nombreImagen = "";
        if (!isCreada) {
            isCreada = fileImagen.mkdirs();
        }

        if (isCreada) {
            nombreImagen = (System.currentTimeMillis() / 1000) + ".jpg";
        }

        path = Environment.getExternalStorageDirectory() +
                File.separator + RUTA_IMAGEN + File.separator + nombreImagen;

        File imagen = new File(path);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            String authorities = getContext().getPackageName() + ".provider";
            Uri imageUri = FileProvider.getUriForFile(getContext(), authorities, imagen);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        } else {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imagen));
        }
        startActivityForResult(intent, COD_FOTO);
    }


    public String getStringImage(Bitmap bmp) {
        if (bmp == null) return "";

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 35, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                // Permisos concedidos
            } else {
                solicitarPermisosManual();
            }
        }
    }

    private void solicitarPermisosManual() {
        final CharSequence[] opciones = {"si", "no"};
        final AlertDialog.Builder alertOpciones = new AlertDialog.Builder(getContext());
        alertOpciones.setTitle("¿Desea configurar los permisos de forma manual?");
        alertOpciones.setItems(opciones, (dialogInterface, i) -> {
            if (opciones[i].equals("si")) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getContext().getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            } else {
                Toast.makeText(getContext(), "Los permisos no fueron aceptados", Toast.LENGTH_SHORT).show();
                dialogInterface.dismiss();
            }
        });
        alertOpciones.show();
    }

//    public void mostrar_alert(String url, String tipoExh) {
//        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
//        LayoutInflater inflater = getActivity().getLayoutInflater();
//        View dialogView = inflater.inflate(R.layout.alertdialog_tipo_exhibicion, null);
//        View titleView = inflater.inflate(R.layout.alertdialog_title_tipo_exh, null);
//
//        TextView title = titleView.findViewById(R.id.titulo_tipo_exh);
//        title.setText(tipoExh);
//        builder.setCustomTitle(titleView);
//        builder.setCancelable(false);
//
//        WebView wv = dialogView.findViewById(R.id.wvTipoExh);
//        wv.getSettings().setJavaScriptEnabled(true);
//        wv.getSettings().setBuiltInZoomControls(true);
//        wv.getSettings().setDisplayZoomControls(false);
//        wv.getSettings().setUseWideViewPort(true);
//        wv.setInitialScale(1);
//        wv.setWebViewClient(new WebViewClient());
//
//        if (VerificarNet.hayConexion(getContext())) {
//            wv.loadUrl(url);
//        } else {
//            wv.loadUrl("file:///android_asset/html/pagina-error.html");
//        }
//
//        builder.setNegativeButton("Aceptar", (dialog, id) -> dialog.dismiss());
//        builder.setView(dialogView);
//        alertDialog = builder.create();
//        alertDialog.show();
//    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView == spCategoria) {
            if (i == 0) {
                categoria = "";
            } else {
                try {
                    categoria = adapterView.getItemAtPosition(i).toString();
                } catch (Exception e) {
                    categoria = "";
                }
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        categoria = "";
    }

    public void insertData(String categoria) {
        if (id_pdv != null ) {
            try {
                // Obtener comentario
                comentario = txtComentario != null ? txtComentario.getText().toString().trim() : "";
//                if (comentario.isEmpty() || comentario.equals("Comentario")) {
//                    comentario = "N/A";
//                }

                String ciudad = "Ciudad: " + handler.getCityPdv(codigo_pdv);
                String local = "Local: " + punto_venta;
                String usuario = "Usuario: " + user;
                String fechaHora = "Fecha y hora: " + fecha + " " + hora;

                Bitmap temporal = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                ImageMark im = new ImageMark();
                Bitmap watermark = im.mark(temporal, ciudad, local, usuario, fechaHora, Color.YELLOW, 100, 85, false);
                int mheight = (int) (watermark.getHeight() * (1024.0 / watermark.getWidth()));
                Bitmap scaled = Bitmap.createScaledBitmap(watermark, 1024, mheight, true);

                image = getStringImage(scaled);

                if (image == null || image.isEmpty()) {
                    Toast.makeText(getContext(), "Por favor tomar una foto", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Fecha y hora actual
                Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-5"));
                Date currentLocalTime = cal.getTime();
                DateFormat date = new SimpleDateFormat("dd/MM/yyyy");
                date.setTimeZone(TimeZone.getTimeZone("GMT-5"));
                String fechaser = date.format(currentLocalTime);

                DateFormat hour = new SimpleDateFormat("HH:mm:ss");
                hour.setTimeZone(TimeZone.getTimeZone("GMT-5"));
                String horaser = hour.format(currentLocalTime);

                // Insertar datos - SOLO columnas que existen en el Contract
                ContentValues values = new ContentValues();
                values.put(ContractInsertLogisticoRelevo.Columnas.PHARMA_ID, id_pdv);
                values.put(ContractInsertLogisticoRelevo.Columnas.CODIGO, codigo_pdv);
                values.put(ContractInsertLogisticoRelevo.Columnas.USUARIO, user);
                values.put(ContractInsertLogisticoRelevo.Columnas.SUPERVISOR, punto_venta);
                values.put(ContractInsertLogisticoRelevo.Columnas.FECHA, fechaser);
                values.put(ContractInsertLogisticoRelevo.Columnas.HORA, horaser);
                values.put(ContractInsertLogisticoRelevo.Columnas.CATEGORIA, categoria);
                values.put(ContractInsertLogisticoRelevo.Columnas.TIPO_LOGISTICO, tipo_logistico_guardado);
                values.put(ContractInsertLogisticoRelevo.Columnas.FOTO, image);
                values.put(ContractInsertLogisticoRelevo.Columnas.COMENTARIO, comentario);
                values.put(Constantes.PENDIENTE_INSERCION, 1);

                // Insertar usando el ContentResolver con el URI correcto
                getContext().getContentResolver().insert(ContractInsertLogisticoRelevo.CONTENT_URI, values);

                // Sincronizar
                if (VerificarNet.hayConexion(getContext())) {
                    SyncAdapter.sincronizarAhora(getContext(), true, Constantes.insertLogisticoRelevo, null);
                    Toast.makeText(getContext(), Mensajes.ON_SYNC_SERVER, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), Mensajes.ON_SYNC_DEVICE, Toast.LENGTH_SHORT).show();
                }

                limpiarDatos();

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(getContext(), Mensajes.ERROR,Toast.LENGTH_SHORT).show();
        }
    }

    private void limpiarDatos() {
        if (spCategoria != null) {
            spCategoria.setSelection(0);
        }
        if (imageView != null) {
            imageView.setImageDrawable(null);
            imageView.setImageResource(android.R.color.transparent);
        }
        if (txtComentario != null) {
            txtComentario.setText("");
        }
        image = "";
        bitmapfinal = null;
    }

    private boolean esFormularioValido() {
//        if (spCategoria == null || spCategoria.getSelectedItem() == null) {
//            Toast.makeText(getContext(), "Error en el formulario", Toast.LENGTH_SHORT).show();
//            return false;
//        }

        String categoriaSelected = spCategoria.getSelectedItem().toString();
        if (categoriaSelected.equalsIgnoreCase("Seleccione") || categoriaSelected.isEmpty()) {
            Toast.makeText(getContext(), "Debe seleccionar una categoría", Toast.LENGTH_SHORT).show();
            return false;
        }

        String comentarioText = txtComentario.getText().toString().trim();
        if (comentarioText.isEmpty()) {
            Toast.makeText(getContext(), "Debe ingresar un comentario", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (imageView.getDrawable() == null) {
            Toast.makeText(getContext(), "Debe tomar una foto", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btnPropia) {
            tipo = "PREFORMA";
            tipo_logistico_guardado = "PREFORMA";
            btnPropia.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.button_background_selected));
            btnPropia.setPadding(15, 15, 15, 15);
            btnCompetencia.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.button_background_unselected));
            btnCompetencia.setPadding(15, 15, 15, 15);
            filtrarCategoria(tipo, fabricante);

        } else if (id == R.id.btnCompetencia) {
            tipo = "COMPETENCIA";
            tipo_logistico_guardado = "COMPETENCIA";
            btnPropia.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.button_background_unselected));
            btnPropia.setPadding(15, 15, 15, 15);
            btnCompetencia.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.button_background_selected));
            btnCompetencia.setPadding(15, 15, 15, 15);
            filtrarCategoria(tipo, fabricante);

        } else if(v == btnCamera){
            cargarImagen();
        }

        else if (id == R.id.btnGuardar) {
            if (esFormularioValido()) {
                insertData(categoria);
            }

        } /*else if (id == R.id.ibCargarFotoPreforma) {
            cargarImagen();
        }*/
    }
}