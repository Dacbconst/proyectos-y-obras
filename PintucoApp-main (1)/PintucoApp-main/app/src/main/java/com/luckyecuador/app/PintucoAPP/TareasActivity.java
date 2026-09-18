package com.luckyecuador.app.PintucoAPP;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import android.text.InputFilter;
import android.text.Spanned;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.luckyecuador.app.PintucoAPP.Clase.InsertTareas;
import com.luckyecuador.app.PintucoAPP.Clase.BaseTareas;
import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;
import com.luckyecuador.app.PintucoAPP.Conexion.MarshMallowPermission;
import com.luckyecuador.app.PintucoAPP.Conexion.Mensajes;
import com.luckyecuador.app.PintucoAPP.Conexion.VerificarNet;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertTareas;
import com.luckyecuador.app.PintucoAPP.DataBase.DatabaseHelper;
import com.luckyecuador.app.PintucoAPP.DataBase.Provider;
import com.luckyecuador.app.PintucoAPP.Sync.SyncAdapter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Pattern;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


public class TareasActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {

    ArrayList<BaseTareas> type_name_copy = new ArrayList<BaseTareas>();
    ArrayList<InsertTareas> sesion = new ArrayList<InsertTareas>();

    private String categoria, subcategoria, segmento1, segmento2, brand, tamano, cantidad, codigo, descripcion;
    private String id_pdv,user,codigo_pdv, punto_venta,fecha,hora;

    DatabaseHelper handler;

    EditText txtsearch;
    TextView txtfechav, lSeleccioneProducto;
    ImageButton btnFecha;
    TextView empty;
    ListView listview;

    public ArrayList<BaseTareas> listProductos;
    List<String> listTarget;
    List<String> filterProducts;
    CustomAdapterTareas dataAdapter;

    private EditText txtventas, txtpregular, txtppromocion, txtcuotas, txtvcuotas;
    private String fechaventas, producto, pregular, ppromocion, poferta, aprobado, sku, realizado, cuotas, vcuotas, format, presentacion;
    private String ciudad,retail,sucursal;
    private String canal, subcanal;

    LinearLayout layout_skuName;
    LinearLayout layout_skuDescripcion;

    //Photo Camera
    private ImageView imageView;
    private Bitmap bitmap;
    private Bitmap bitmapfinal;
    final int COD_SELECCIONA=10;
    final int COD_FOTO=20;

    private final String CARPETA_RAIZ="PintucoApp/";
    private final String RUTA_IMAGEN=CARPETA_RAIZ+"Tareas";
    String path;
    Button btnCamera;

    String image = "";
    MarshMallowPermission marshMallowPermission;

    private final String manufacturer = "Alicorp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tareas);
        setToolbar();

        handler = new DatabaseHelper(this, Provider.DATABASE_NAME,null,1);
        LoadData();
        marshMallowPermission = new MarshMallowPermission(this);

        handler = new DatabaseHelper(this, Provider.DATABASE_NAME,null,1);

        listview = (ListView)findViewById(R.id.lvSKUCode);
        View headerView = (View) this.getLayoutInflater().inflate(R.layout.list_row_tareas_title,null,false);
        listview.addHeaderView(headerView,null,false);

        ListView listView = (ListView) findViewById(R.id.lvSKUCode);
        empty = (TextView) findViewById(R.id.recyclerview_data_empty);
        imageView = (ImageView) findViewById(R.id.ivFoto);
        listView.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        showListView();
    }
    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void LoadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        id_pdv = sharedPreferences.getString(Constantes.IDPDV,Constantes.NODATA);
        user = sharedPreferences.getString(Constantes.USER,Constantes.NODATA);

        id_pdv = sharedPreferences.getString(Constantes.PHARMA_ID, Constantes.NODATA);
        codigo_pdv = sharedPreferences.getString(Constantes.CODIGO, Constantes.NODATA);
        punto_venta = sharedPreferences.getString(Constantes.PDV, Constantes.NODATA);
        fecha = sharedPreferences.getString(Constantes.FECHA, Constantes.NODATA);
        hora = sharedPreferences.getString(Constantes.HORA, Constantes.NODATA);
        format = sharedPreferences.getString(Constantes.FORMAT, Constantes.NODATA);
        canal = sharedPreferences.getString(Constantes.TIPO,Constantes.NODATA);
        subcanal = sharedPreferences.getString(Constantes.SUBCANAL,Constantes.NODATA);
    }

    public void showListView() {
        listProductos = handler.filtrarListProductosTareas(codigo_pdv, user);
        dataAdapter = new CustomAdapterTareas(this, listProductos);
        if (!dataAdapter.isEmpty()) {
            empty.setVisibility(View.INVISIBLE);
            listview.setVisibility(View.VISIBLE);
            listview.setAdapter(dataAdapter);


        }else{
            listview.setVisibility(View.GONE);
            empty.setVisibility(View.VISIBLE);
        }
    }

    public void listUpdate(ArrayList<BaseTareas> data) {
        if (!data.isEmpty()) {
            listview.setVisibility(View.VISIBLE);
            empty.setVisibility(View.INVISIBLE);
            listview.setAdapter(new CustomAdapterTareas(this, data));
        }else{
            listview.setVisibility(View.GONE);
            empty.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {}

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode==100) {
            if (grantResults.length==2 && grantResults[0]==PackageManager.PERMISSION_GRANTED
                    && grantResults[1]==PackageManager.PERMISSION_GRANTED) {
                //botonCargar.setEnabled(true);
            }else{
                solicitarPermisosManual();
            }
        }

    }

    private void solicitarPermisosManual() {
        final CharSequence[] opciones={"si","no"};
        final AlertDialog.Builder alertOpciones=new AlertDialog.Builder(TareasActivity.this);
        alertOpciones.setTitle("¿Desea configurar los permisos de forma manual?");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("si")) {
                    Intent intent=new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri=Uri.fromParts("package",getPackageName(),null);
                    intent.setData(uri);
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(),"Los permisos no fueron aceptados",Toast.LENGTH_SHORT).show();
                    dialogInterface.dismiss();
                }
            }
        });
        alertOpciones.show();
    }

    private void cargarDialogoRecomendacion() {
        AlertDialog.Builder dialogo=new AlertDialog.Builder(TareasActivity.this);
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

    private void cargarImagen() {
        final CharSequence[] opciones={"Tomar Foto","Cargar Imagen","Cancelar"};
        final AlertDialog.Builder alertOpciones=new AlertDialog.Builder(TareasActivity.this);
        alertOpciones.setTitle("Seleccione una Opción");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("Tomar Foto")) {
                    tomarFotografia();
                }else{
                    if (opciones[i].equals("Cargar Imagen")) {
                        Intent intent=new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/");
                        startActivityForResult(intent.createChooser(intent,"Seleccione la Aplicación"),COD_SELECCIONA);
                    }else{
                        dialogInterface.dismiss();
                    }
                }
            }
        });
        alertOpciones.show();
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
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.N) {
            String authorities=getApplicationContext().getPackageName()+".provider";
            Uri imageUri= FileProvider.getUriForFile(this,authorities,imagen);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        }else{
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imagen));
        }
        startActivityForResult(intent,COD_FOTO);
        //
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //OutOfMemoryError
        if (resultCode==RESULT_OK) {
            switch (requestCode) {
                case COD_SELECCIONA:
                    Uri miPath=data.getData();
                    imageView.setImageURI(miPath);
                    break;
                case COD_FOTO:
                    MediaScannerConnection.scanFile(this, new String[]{path}, null,
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

    //Permite hacer la imagen mas pequeña
    public void scaleImage(Bitmap bitmap) {
        try{
            int mheight = (int) ( bitmap.getHeight() * (1024.0 / bitmap.getWidth()) );
            Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 1024, mheight, true);
            imageView.setImageBitmap(scaled);
            bitmapfinal = ((BitmapDrawable)imageView.getDrawable()).getBitmap();

        } catch (Exception e) {
            AlertDialog alertDialog1;
            alertDialog1 = new AlertDialog.Builder(getApplicationContext()).create();
            alertDialog1.setTitle("Message");
            alertDialog1.setMessage("Notificar \t "+e.toString());
            alertDialog1.show();
            Log.e("compressBitmap", "Error on compress file");
        }
    }

    //Metodo que sube la imagen al servidor
    public String getStringImage(Bitmap bmp) {
        String encodedImage;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //Comprime la Imagen canal, calidad y outputstream
        bmp.compress(Bitmap.CompressFormat.JPEG, 35, baos);
        byte[] imageBytes = baos.toByteArray();
        encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    public void insertData(String sku, String realizado) {
        try{
            if (imageView != null && imageView.getDrawable() != null) { //ImageView no vacio
                image = getStringImage(bitmapfinal);
            }else{
                Toast.makeText(this,"No has tomado la foto",Toast.LENGTH_LONG).show();
            }
            if (image != null && !image.equals("")) {
                Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-5"));
                Date currentLocalTime = cal.getTime();
                DateFormat date = new SimpleDateFormat("dd/MM/yyy");
                date.setTimeZone(TimeZone.getTimeZone("GMT-5"));
                String fechaser = date.format(currentLocalTime);

                DateFormat hour = new SimpleDateFormat("HH:mm:ss");
                hour.setTimeZone(TimeZone.getTimeZone("GMT-5"));
                String horaser = hour.format(currentLocalTime);

                ContentValues values = new ContentValues();

                values.put(ContractInsertTareas.Columnas.PHARMA_ID, id_pdv);
                values.put(ContractInsertTareas.Columnas.CODIGO, codigo_pdv);
                values.put(ContractInsertTareas.Columnas.USUARIO, user);
                values.put(ContractInsertTareas.Columnas.SUPERVISOR, punto_venta);
                values.put(ContractInsertTareas.Columnas.FECHA, fechaser);
                values.put(ContractInsertTareas.Columnas.HORA, horaser);
                values.put(ContractInsertTareas.Columnas.CHANNEL, canal);
                values.put(ContractInsertTareas.Columnas.CODIGOPDV, codigo_pdv);
                values.put(ContractInsertTareas.Columnas.MERCADERISTA, user);
                values.put(ContractInsertTareas.Columnas.TAREAS, sku);
                values.put(ContractInsertTareas.Columnas.REALIZADO, realizado);
                values.put(ContractInsertTareas.Columnas.FOTO, image);
                values.put(Constantes.PENDIENTE_INSERCION, 1);

                getContentResolver().insert(ContractInsertTareas.CONTENT_URI, values);

                if (VerificarNet.hayConexion(getApplicationContext())) {
                    SyncAdapter.sincronizarAhora(this, true, Constantes.insertTareas, null);
                    Toast.makeText(getApplicationContext(), Mensajes.ON_SYNC_SERVER, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), Mensajes.ON_SYNC_DEVICE, Toast.LENGTH_SHORT).show();
                }
                limpiarFormulario();
                showListView();
            }else {
                Toast.makeText(getApplicationContext(), "Por favor tomar una foto para poder ser enviado.", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e) {
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void limpiarFormulario() {
        imageView.setImageResource(android.R.color.transparent);
        image = "";
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {}

    public class CustomAdapterTareas extends ArrayAdapter<BaseTareas> implements Filterable {

        public ArrayList<BaseTareas> values;
        public Context context;
        boolean[] checkBoxState;
//        String realizado = "NO";
        String realizado = "SI";

        public CustomAdapterTareas(Context context, ArrayList<BaseTareas> values) {
            super(context, 0, values);
            this.values = values;
            checkBoxState = new boolean[values.size()];
        }

        public class ViewHolder {
            TextView lblSku;
            TextView lblEstado;
            Switch switch1;
            ImageButton ibCargarFotoTareas;
            CheckBox checkGuardar; //agregado GT
            //EditText txtunidad;
            EditText txt_precio_regular;
            EditText txt_precio_promocion;
            EditText txt_precio_oferta;
            // Spinner spMotivo;
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

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            //Obtener Instancia Inflater
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            CustomAdapterTareas.ViewHolder vHolder = null;
            //Comprobar si el View existe
            //Si no existe inflarlo
            if (null == convertView) {
                convertView = inflater.inflate(R.layout.list_row_tareas, parent, false); // Modificacion (list_row_option) GT
                //Obtener instancias de los elementos
                vHolder = new CustomAdapterTareas.ViewHolder();
                vHolder.lblSku = (TextView) convertView.findViewById(R.id.lblSku);
                vHolder.lblEstado = (TextView) convertView.findViewById(R.id.lblEstado);
                vHolder.switch1 = (Switch) convertView.findViewById(R.id.switch1);
                vHolder.ibCargarFotoTareas = (ImageButton) convertView.findViewById(R.id.ibCargarFotoTareas);
                vHolder.checkGuardar = (CheckBox) convertView.findViewById(R.id.checkGuardar);
               /* vHolder.txt_precio_regular = (EditText) convertView.findViewById(R.id.txt_precio_regular);
                vHolder.txt_precio_promocion = (EditText) convertView.findViewById(R.id.txt_precio_promocion);
                vHolder.txt_precio_oferta = (EditText) convertView.findViewById(R.id.txt_precio_oferta);*/

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

                vHolder.ibCargarFotoTareas.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cargarImagen();
                    }
                });

                if (values.size() > 0) {
                    String tarea = values.get(position).getTareas();
                    String periodo = values.get(position).getPeriodo();
                    String estado = handler.getEstadoTarea(codigo_pdv, user, periodo, tarea);
                    vHolder.lblSku.setText(tarea);
                    vHolder.lblEstado.setText(estado);
                    if (estado.equalsIgnoreCase("REALIZADO")) {
                        vHolder.lblEstado.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
                    }
                    // vHolder.txt_precio_regular.setText(values.get(position).getPvp().trim());
                    // vHolder.txt_precio_promocion.setText(values.get(position).getPvp().trim());

//                    String fecha = "";
//                    sesion = handler.getListGuardadoTareas(codigo_pdv, user, periodo);
//                    Log.i("CODIGO",codigo_pdv);
//                    Log.i("SESION SIZE",sesion.size()+"");
//                    for(int i = 0; i < sesion.size(); i++) {
//                        Log.i("ENTRA SESION","ENTRA SESION");
//                        if (values.get(position).getTareas().equals(sesion.get(i).getTareas())) {
//                            vHolder.lblSku.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.rojo_alicorp));
//                            vHolder.lblSku.setTypeface(null, Typeface.BOLD);
//                        }else{
//                            Log.i("NO ENTRA","NO ENTRA");
//                        }
//                    }

                    final CustomAdapterTareas.ViewHolder finalv = vHolder;

                    vHolder.checkGuardar.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            if (finalv.switch1.isChecked()) {
                                realizado =  "SI";
                            }
                            sku = finalv.lblSku.getText().toString();
                            aprobado = finalv.switch1.getText().toString();

//                            if (((CheckBox) v).isChecked()) {
                                if (imageView != null && imageView.getDrawable() != null) {//ImageView no vacio
                                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(TareasActivity.this);
                                    builder.setIcon(android.R.drawable.ic_dialog_alert);
                                    builder.setTitle("Confirmación");
                                    builder.setMessage("¿Desea guardar la tarea " + sku + "?");
                                    builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            image = getStringImage(bitmapfinal);
                                            insertData(sku, realizado);
                                        }
                                    });

                                    builder.setNeutralButton("NO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            finalv.checkGuardar.setChecked(false);
                                        }
                                    });

                                    android.app.AlertDialog ad = builder.create();
                                    ad.show();

                                    Button pButton = ad.getButton(DialogInterface.BUTTON_POSITIVE);
                                    //pButton.setTextColor(Color.rgb(79, 195, 247));
                                    Button cButton = ad.getButton(DialogInterface.BUTTON_NEUTRAL);
                                    //cButton.setTextColor(Color.rgb(79, 195, 247));
                                } else {
                                    Toast.makeText(getApplicationContext(), "No puede guardar sin tomar una foto", Toast.LENGTH_LONG).show();
                                    finalv.checkGuardar.setChecked(false);
                                }

//                            } else {
//                                Toast.makeText(getApplicationContext(), "No ingresaste el precio regular o promocion", Toast.LENGTH_LONG).show();
//                            }
                        }
                    });
                }
            }
            //Devolver al ListView la fila creada
            return convertView;
        }
    }
}