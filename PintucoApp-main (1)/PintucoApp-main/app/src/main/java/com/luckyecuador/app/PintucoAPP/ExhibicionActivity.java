package com.luckyecuador.app.PintucoAPP;

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
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.luckyecuador.app.PintucoAPP.Clase.BasePortafolioProductos;
import com.luckyecuador.app.PintucoAPP.Clase.InsertExhibiciones;
import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;
import com.luckyecuador.app.PintucoAPP.Conexion.MarshMallowPermission;
import com.luckyecuador.app.PintucoAPP.Conexion.Mensajes;
import com.luckyecuador.app.PintucoAPP.Conexion.VerificarNet;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertExh;
import com.luckyecuador.app.PintucoAPP.DataBase.DatabaseHelper;
import com.luckyecuador.app.PintucoAPP.DataBase.Provider;
import com.luckyecuador.app.PintucoAPP.Sync.SyncAdapter;
import com.luckyecuador.app.PintucoAPP.Utils.SpinnerAdapter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class ExhibicionActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener{

    private static final String TAG = ExhibicionActivity.class.getSimpleName();
    ArrayList<InsertExhibiciones> sesion = new ArrayList<InsertExhibiciones>();

    //Views
    Spinner spCategoria;
    Spinner spSubcategoria;
    Spinner spBrand;

    ArrayList<BasePortafolioProductos> listProductos;
    CustomAdapterExhibicion dataAdapter;

    DatabaseHelper handler;

    //Photo Camera
    public ImageButton btnCamera;
    public ImageView imageView;
    Bitmap bitmap;
    private Bitmap bitmapfinal;
    final int COD_SELECCIONA=10;
    final int COD_FOTO=20;

    private final String CARPETA_RAIZ="PintucoApp/";
    private final String RUTA_IMAGEN=CARPETA_RAIZ+"Exhibicion";
    String path;
    String image = "";

    MarshMallowPermission marshMallowPermission;

    String categoria, tipo, marca;

    private String subcategoria, canal, subcanal;
    private String id_pdv, user, codigo_pdv, punto_venta, fecha, hora, format;

    TextView empty;
    RecyclerView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exhibicion);
        setToolbar();
        handler = new DatabaseHelper(this, Provider.DATABASE_NAME,null,1);

        LoadData();

        marshMallowPermission = new MarshMallowPermission(this);
        spCategoria = (Spinner) findViewById(R.id.spCategoria);
        spSubcategoria = (Spinner) findViewById(R.id.spSubcategoria);
        spBrand = (Spinner) findViewById(R.id.spMarca);

        empty = (TextView) findViewById(R.id.recyclerview_data_empty);
        listview = (RecyclerView) findViewById(R.id.lvSKUCode);

        imageView = (ImageView) findViewById(R.id.ivFotoExhibiciones);

        filtrarCategoria();
    }

    private boolean validaPermisos() {
        if (Build.VERSION.SDK_INT<Build.VERSION_CODES.M) {
            return true;
        }

        if ((checkSelfPermission(CAMERA)== PackageManager.PERMISSION_GRANTED)&&
                (checkSelfPermission(WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED)) {
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

    public void filtrarCategoria() {
//        List<String> operadores = handler.getCategoriaExhibicion();
//        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_spinner_item, operadores);
//        dataAdapter
//                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spCategoria.setAdapter(dataAdapter);
//        spCategoria.setOnItemSelectedListener(this);
    }

    public void filtrarSubcategoriaExhibicion(String categoria) {
//        List<String> operadores = handler.getSubcategoriaExhibicion(categoria);
//        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_spinner_item, operadores);
//        dataAdapter
//                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spSubcategoria.setAdapter(dataAdapter);
//        spSubcategoria.setOnItemSelectedListener(this);
    }

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    public void LoadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        id_pdv = sharedPreferences.getString(Constantes.IDPDV,Constantes.NODATA);
        user=sharedPreferences.getString(Constantes.USER,Constantes.NODATA);

        user = sharedPreferences.getString(Constantes.USER, Constantes.NODATA);
        id_pdv = sharedPreferences.getString(Constantes.PHARMA_ID, Constantes.NODATA);
        canal =sharedPreferences.getString(Constantes.TIPO,Constantes.NODATA);
        subcanal =sharedPreferences.getString(Constantes.SUBCANAL,Constantes.NODATA);
        codigo_pdv = sharedPreferences.getString(Constantes.CODIGO, Constantes.NODATA);
        punto_venta = sharedPreferences.getString(Constantes.PDV, Constantes.NODATA);
        fecha = sharedPreferences.getString(Constantes.FECHA, Constantes.NODATA);
        hora = sharedPreferences.getString(Constantes.HORA, Constantes.NODATA);
        format = sharedPreferences.getString(Constantes.FORMAT, Constantes.NODATA);
    }


    //************METODOS PARA TAKE-PHOTO Y UPLOAD
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK) {

            switch (requestCode) {
                case COD_SELECCIONA:
                    Uri filePath = data.getData();
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                        scaleImage(bitmap);
                    }catch (IOException e) {
                        e.printStackTrace();
                    }
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

    //Permite hacer la imagen mas pequeña para mostrarla en el ImageView
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

    private void cargarImagen() {
        final CharSequence[] opciones={"Tomar Foto","Cargar Imagen","Cancelar"};
        final AlertDialog.Builder alertOpciones=new AlertDialog.Builder(ExhibicionActivity.this);
        alertOpciones.setTitle("Seleccione una Opción");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("Tomar Foto")) {
                    tomarFotografia();
                }else{
                    if (opciones[i].equals("Cargar Imagen")) {
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
        startActivityForResult(Intent.createChooser(intent,  "Seleccionar una imagen"), COD_SELECCIONA);
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
            String authorities=getApplicationContext().getPackageName()+".provider";
            Uri imageUri= FileProvider.getUriForFile(this,authorities,imagen);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        }else
        {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imagen));
        }
        startActivityForResult(intent,COD_FOTO);
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

    //String to bitmap
    public Bitmap StringToBitMap(String encodedString) {
        try{
            byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }catch(Exception e) {
            e.getMessage();
            return null;
        }
    }
    //Tomar Foto

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
        final AlertDialog.Builder alertOpciones=new AlertDialog.Builder(ExhibicionActivity.this);
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
        AlertDialog.Builder dialogo=new AlertDialog.Builder(ExhibicionActivity.this);
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
        if (adapterView==spCategoria) {
            try{
                categoria = adapterView.getItemAtPosition(i).toString();
                filtrarSubcategoriaExhibicion(categoria);
            }catch (Exception e) {
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
        if (adapterView== spSubcategoria) {
            try{
                subcategoria = adapterView.getItemAtPosition(i).toString();
                filtrarListView(categoria, subcategoria, canal, subcanal);
            }catch (Exception e) {
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }

    }

    public void filtrarListView(String categoria, String subcategoria, String canal, String subcanal) {
//        listProductos = handler.filtrarListProductos3Exhibicion(categoria,subcategoria, canal, subcanal);
//        Log.i("List Original",listProductos.size()+"");
//        dataAdapter = new CustomAdapterExhibicion(this, listProductos);
//        if (dataAdapter.getItemCount() != 0) {
//            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ExhibicionActivity.this);
//            listview.setLayoutManager(linearLayoutManager);
//            listview.setHasFixedSize(true);
//            CustomAdapterExhibicion customAdapter = new CustomAdapterExhibicion(this.getApplicationContext(),listProductos);
//            listview.setAdapter(customAdapter);
//        }else{
//            empty.setVisibility(View.VISIBLE);
//        }
    }

    public void insertData(String Categoria, String subcategoria, String Brand,
                           String TipoExh, String Zona, String Contratada, String Condicion) {
        if (id_pdv!=null) {
            try{
                if (imageView != null && imageView.getDrawable() != null) { //ImageView no vacio
                    image = getStringImage(bitmapfinal);
                } else {
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
                    //Almacenar Datos
                    ContentValues values = new ContentValues();

                    values.put(ContractInsertExh.Columnas.PHARMA_ID,id_pdv);
                    values.put(ContractInsertExh.Columnas.CODIGO,codigo_pdv);
                    values.put(ContractInsertExh.Columnas.USUARIO,user);
                    values.put(ContractInsertExh.Columnas.SUPERVISOR,punto_venta);
                    values.put(ContractInsertExh.Columnas.FECHA,fechaser);
                    values.put(ContractInsertExh.Columnas.HORA,horaser);
                    values.put(ContractInsertExh.Columnas.SECTOR,"");
                    values.put(ContractInsertExh.Columnas.CATEGORIA,Categoria);
                    values.put(ContractInsertExh.Columnas.SUBCATEGORIA,subcategoria);
                    values.put(ContractInsertExh.Columnas.SEGMENTO,"");
                    values.put(ContractInsertExh.Columnas.BRAND,Brand);
                    values.put(ContractInsertExh.Columnas.TIPO_EXH,TipoExh);
                    values.put(ContractInsertExh.Columnas.ZONA_EX,Zona);
                    values.put(ContractInsertExh.Columnas.CONTRATADA,Contratada);
                    values.put(ContractInsertExh.Columnas.CONDICION,Condicion);
                    values.put(ContractInsertExh.Columnas.FOTO, image);
                    values.put(Constantes.PENDIENTE_INSERCION, 1);

                    getContentResolver().insert(ContractInsertExh.CONTENT_URI, values);

                    if (VerificarNet.hayConexion(getApplicationContext())) {
                        SyncAdapter.sincronizarAhora(this, true, Constantes.insertExh, null);
                        Toast.makeText(getApplicationContext(), Mensajes.ON_SYNC_SERVER, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), Mensajes.ON_SYNC_DEVICE, Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "Por favor tomar una foto para poder ser enviado.", Toast.LENGTH_SHORT).show();
                }
            }catch (Exception e) {
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }else{
            Toast.makeText(getApplicationContext(), Mensajes.ERROR,Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {}

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {}

    public class CustomAdapterExhibicion extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        private final String TAG = CustomAdapterExhibicion.class.getSimpleName();
        private static final int TYPE_HEADER = 0;
        private static final int TYPE_ITEM = 1;
        private Context context;
        public ArrayList<BasePortafolioProductos> values;

        public CustomAdapterExhibicion(Context context, ArrayList<BasePortafolioProductos> values) {
            this.context = context;
            this.values = values;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == TYPE_HEADER) {
                View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_exhibicion_title, parent, false);
                return new HeaderViewHolder(layoutView);
            } else if (viewType == TYPE_ITEM) {
                View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_exhibicion, parent, false);
                return new ItemViewHolder(layoutView);
            }
            throw new RuntimeException("No match for " + viewType + ".");
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
            BasePortafolioProductos mObject = values.get(position);
            if (holder instanceof HeaderViewHolder) {
                //((HeaderViewHolder) holder).headerTitle.setText(mObject.getSku());
                ((HeaderViewHolder) holder).headerTitle.setText("MARCA");
                ((HeaderViewHolder) holder).headerTitle1.setText("TIPO EXH.");
                ((HeaderViewHolder) holder).headerTitle2.setText("ZONA");
                ((HeaderViewHolder) holder).headerTitle3.setText("CONTRATADA");
                ((HeaderViewHolder) holder).headerTitle4.setText("CONDICION");
                ((HeaderViewHolder) holder).headerTitle5.setText("FOTO");
                //((HeaderViewHolder) holder).headerTitle6.setText("VISTA");
            }else if (holder instanceof ItemViewHolder) {
                ((ItemViewHolder) holder).txt_sku.setText(mObject.getSku());
                sesion = handler.getListGuardadoExh(codigo_pdv);
                for(int i = 0; i < sesion.size(); i++) {
                    Log.i("EXH",sesion.get(i).getTipo_exh() + " - " + sesion.get(i).getZona() + " - " + sesion.get(i).getContratada() + " - " + sesion.get(i).getCondicion());

                    if (mObject.getSku().equalsIgnoreCase(sesion.get(i).getMarca())) {
                        ((ItemViewHolder) holder).spTipoExh.setSelection(Integer.parseInt(sesion.get(i).getTipo_exh()));
                        final Handler handler = new Handler();
                        final int finalI = i;
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ((ItemViewHolder) holder).spZona.setSelection(Integer.parseInt(sesion.get(finalI).getZona()));
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        ((ItemViewHolder) holder).spContratada.setSelection(Integer.parseInt(sesion.get(finalI).getContratada()));
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                ((ItemViewHolder) holder).spCondicion.setSelection(Integer.parseInt(sesion.get(finalI).getCondicion()));
                                                /*handler.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        ((ItemViewHolder) holder).imageView.setImageBitmap(StringToBitMap(sesion.get(finalI).getFoto()));
                                                    }
                                                }, 1000);*/
                                            }
                                        }, 1000);
                                    }
                                }, 1000);
                            }
                        }, 1000);
                    }
                }

            }
        }

        private BasePortafolioProductos getItem(int position) {
            return values.get(position);
        }
        @Override
        public int getItemCount() {
            return values.size();
        }
        @Override
        public int getItemViewType(int position) {
            if (isPositionHeader(position))
                return TYPE_HEADER;
            return TYPE_ITEM;
        }
        private boolean isPositionHeader(int position) {
            return position == 0;
        }

        public void filterList(ArrayList<BasePortafolioProductos> filteredList) {
            listProductos = filteredList;
            Log.i("LIST",listProductos.size()+"");
            ExhibicionActivity.this.dataAdapter.notifyDataSetChanged();
        }

        public class HeaderViewHolder extends RecyclerView.ViewHolder{

            public TextView headerTitle, headerTitle1, headerTitle2, headerTitle3, headerTitle4, headerTitle5, headerTitle6;

            public HeaderViewHolder(View itemView) {
                super(itemView);
                headerTitle = (TextView)itemView.findViewById(R.id.lblSku);
                headerTitle1 = (TextView)itemView.findViewById(R.id.lblTipoexh);
                headerTitle2 = (TextView)itemView.findViewById(R.id.lblZona);
                headerTitle3 = (TextView)itemView.findViewById(R.id.lblContratada);
                headerTitle4 = (TextView)itemView.findViewById(R.id.lblCondicion);
                headerTitle5 = (TextView)itemView.findViewById(R.id.lblFoto);
                //headerTitle6 = (TextView)itemView.findViewById(R.id.lvlVista);
            }
        }

        public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            public TextView txt_sku;
            public Spinner spTipoExh;
            public Spinner spZona;
            public Spinner spContratada;
            public Spinner spCondicion;
            public ImageButton btnCamera;
            //public ImageView imageView;
            public CheckBox chkGuardar;

            public ItemViewHolder(View itemView) {
                super(itemView);
                txt_sku = (TextView)itemView.findViewById(R.id.lblSku);
                spTipoExh = (Spinner)itemView.findViewById(R.id.spTipoExh);
                spZona = (Spinner)itemView.findViewById(R.id.spZona);
                spContratada = (Spinner)itemView.findViewById(R.id.spContratada);
                spCondicion = (Spinner)itemView.findViewById(R.id.spCondicion);
                chkGuardar = (CheckBox)itemView.findViewById(R.id.checkGuardar);
                //imageView = (ImageView)itemView.findViewById(R.id.ivFotoExhibiciones);
                btnCamera = (ImageButton) itemView.findViewById(R.id.ibCargarFotoExhibiciones);

                btnCamera.setOnClickListener(this);
                if (validaPermisos()) {
                    btnCamera.setEnabled(true);
                }else{
                    btnCamera.setEnabled(false);
                }

                final ArrayList<String> NA = new ArrayList<>();
                NA.add("N/A");

                spTipoExh.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (spTipoExh.getSelectedItem().toString().trim().equalsIgnoreCase("No posee exhibición")) {
                            spZona.setAdapter(new SpinnerAdapter(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item, NA));
                        }else{
                            spZona.setAdapter(new SpinnerAdapter(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item, Arrays.asList(getResources().getStringArray(R.array.comboZona))));
                            spContratada.setAdapter(new SpinnerAdapter(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item, Arrays.asList(getResources().getStringArray(R.array.combo_si_no))));
                            spCondicion.setAdapter(new SpinnerAdapter(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item, Arrays.asList(getResources().getStringArray(R.array.combo_condicion))));
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {}
                });

                spZona.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (spZona.getSelectedItem().toString().trim().equalsIgnoreCase("N/A")) {
                            spContratada.setAdapter(new SpinnerAdapter(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item, NA));
                        }else{
                            spContratada.setAdapter(new SpinnerAdapter(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item, Arrays.asList(getResources().getStringArray(R.array.combo_si_no))));
                            spCondicion.setAdapter(new SpinnerAdapter(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item, Arrays.asList(getResources().getStringArray(R.array.combo_condicion))));
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {}
                });

                spContratada.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (spContratada.getSelectedItem().toString().trim().equalsIgnoreCase("N/A")) {
                            spCondicion.setAdapter(new SpinnerAdapter(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item, NA));
                        }else{
                            spCondicion.setAdapter(new SpinnerAdapter(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item, Arrays.asList(getResources().getStringArray(R.array.combo_condicion))));
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {}
                });

                chkGuardar.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        if (((CheckBox)v).isChecked()) {
                            final String marca = txt_sku.getText().toString();
                            final String tipo_exh = spTipoExh.getSelectedItem().toString().trim();
                            final String zona = spZona.getSelectedItem().toString().trim();
                            final String contratada = spContratada.getSelectedItem().toString().trim();
                            final String condicion = spCondicion.getSelectedItem().toString().trim();

                            if (!tipo_exh.equalsIgnoreCase("Seleccione") &&
                                !zona.equalsIgnoreCase("Seleccione") &&
                                !contratada.equalsIgnoreCase("Seleccione") &&
                                !condicion.equalsIgnoreCase("Seleccione")) {

                                if (imageView != null && imageView.getDrawable() != null) {//ImageView no vacio
                                    image = getStringImage(bitmapfinal);
                                } else {
                                    Toast.makeText(ExhibicionActivity.this,"No has tomado la foto",Toast.LENGTH_LONG).show();
                                    chkGuardar.setChecked(false);
                                }

                                if (image != null && !image.equals("")) {
                                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ExhibicionActivity.this);
                                    builder.setIcon(android.R.drawable.ic_dialog_alert);
                                    builder.setTitle("Confirmación");
                                    builder.setMessage("¿Desea guardar la información?");
                                    builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            //insertData(skuSelected, codifica, codifica, canal, descripcion);
                                            //String Sector = spSector.getSelectedItem().toString();
                                            String Categoria = spCategoria.getSelectedItem().toString();
                                            String subcategoria = spSubcategoria.getSelectedItem().toString();
                                            //String segmento = spSegmento.getSelectedItem().toString();
                                            insertData(Categoria, subcategoria, marca, tipo_exh, zona, contratada, condicion);
                                            //limpiarRecycler();
                                        }
                                    });

                                    builder.setNeutralButton("NO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            chkGuardar.setChecked(false);
                                        }
                                    });

                                    android.app.AlertDialog ad = builder.create();
                                    ad.show();

                                    Button pButton = ad.getButton(DialogInterface.BUTTON_POSITIVE);
                                    //pButton.setTextColor(Color.rgb(79, 195, 247));
                                    Button cButton = ad.getButton(DialogInterface.BUTTON_NEUTRAL);
                                    //cButton.setTextColor(Color.rgb(79, 195, 247));
                                }else {
                                    Toast.makeText(getApplicationContext(), "Por favor tomar una foto para poder ser enviado.", Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                Toast.makeText(ExhibicionActivity.this,"Debe seleccionar todos los campos",Toast.LENGTH_LONG).show();
                                chkGuardar.setChecked(false);
                            }

                        }
                    }
                });
            }

            public void limpiarRecycler() {
                spTipoExh.setSelection(0);
                spZona.setSelection(0);
                spContratada.setSelection(0);
                spCondicion.setSelection(0);
                imageView.setImageResource(android.R.color.transparent);
                chkGuardar.setChecked(false);
                image = "";
            }

            @Override
            public void onClick(View v) {
                if (v == btnCamera) {
                    cargarImagen();
                }
            }
        }
    }
}