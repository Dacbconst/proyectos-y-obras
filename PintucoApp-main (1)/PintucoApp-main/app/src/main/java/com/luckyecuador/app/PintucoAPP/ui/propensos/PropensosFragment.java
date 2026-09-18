package com.luckyecuador.app.PintucoAPP.ui.propensos;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import android.widget.ArrayAdapter;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
//
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
//import android.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.luckyecuador.app.PintucoAPP.CameraActivity;
import com.luckyecuador.app.PintucoAPP.Clase.Base_portafolio_productos;
import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;
import com.luckyecuador.app.PintucoAPP.Conexion.Mensajes;
import com.luckyecuador.app.PintucoAPP.Conexion.VerificarNet;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertPropensosYProdMalEst;
import com.luckyecuador.app.PintucoAPP.DataBase.DatabaseHelper;
import com.luckyecuador.app.PintucoAPP.DataBase.Provider;
import com.luckyecuador.app.PintucoAPP.R;
import com.luckyecuador.app.PintucoAPP.Sync.SyncAdapter;
import com.luckyecuador.app.PintucoAPP.Utils.DeveloperOptions;
import com.luckyecuador.app.PintucoAPP.Utils.GuardarLog;
//ESTA IMPORTANCIION SIRVEPARA MANEJAR MARCAS DE AGUA PARA FOTOS
import com.luckyecuador.app.PintucoAPP.Utils.ImageMark;
import com.luckyecuador.app.PintucoAPP.Utils.RequestPermissions;
//CLASES DE UTILIDADES DE JAVA
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.net.URLEncoder;

//DECLARAMOS LA CLASE PUBLICA y como extiende significa que una parte de ella se la
//´puede reutilizar para una actividade del android
public class PropensosFragment extends Fragment implements DatePickerDialog.OnDateSetListener, AdapterView.OnItemSelectedListener {


    private static final int COD_SELECCIONA = 10; //codigo de solicitud
    private static final int COD_FOTO =20 ;
    ArrayList<Base_portafolio_productos> type_name_copy = new ArrayList<Base_portafolio_productos>();

    //Elementos de la interfaz de usuario
    TextView txtfechav;
    ImageButton btnFecha;
    private TextView empty;
    private RecyclerView recyclerView;
    private Spinner spCategoria;
    private Spinner spSubcategoria;
    public TextView lblEstadoFoto; // se cambiaron a variables de instancia
    public ImageView imageView; // se cambiaron a variables de instancia
    private Spinner spPresentacion;
    private Spinner spMarca;
    private Spinner spVariante;
    private EditText txtSKUCode;
    private EditText txtDescripcion;
    // estas variables sirven para almacenar lo que son valores seleccionados de valores de entrada
    private String categoria, subcategoria, segmento1, segmento2, brand, variante, tamano, cantidad, codigo, descripcion, format, presentacion;
    private String id_pdv, user, codigo_pdv, punto_venta, fecha, hora, celular, zone, sku_cliente; // se agg la variable para usar con el celular
    //private  Integer valorizado;
    private Long dias_restantes; // Changed to Long as it's a numeric calculation

    private double litros, valorizado;
    private final String manufacturer = "AkzoNobel S.A";
    //instancia para interactuar con la base de datos
    DatabaseHelper handler;

    ArrayList<Base_portafolio_productos> listProductos;

    List<String> filterProducts;
    List<String> filterTargets;

    String venta, souv;
    private SharedPreferences sharedPref;

    CustomAdapterPropensos dataAdapter;


    LinearLayout layout_skuName;
    LinearLayout layout_skuDescripcion;

    //Photo Camera - these should ideally not be static if you're handling multiple photos for multiple items.
    //For now, I'll adapt them to be set just before calling the camera activity.
    //A better solution for photo handling with RecyclerView is to use ActivityResultLauncher
    //and pass the item's position or unique ID to the camera activity.
    private ImageView currentImageViewForPhoto;
    private TextView currentLblEstadoFotoForPhoto;
    private int currentPhotoItemPosition = -1; // To track which item requested the photo

    String modulo = "PROPENSOS";
    String cadena = "";

    private final String CARPETA_RAIZ = "DanecApp/";
    private final String RUTA_IMAGEN = CARPETA_RAIZ + "Inventario";

    String path;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = (View) inflater.inflate(R.layout.fragment_propensos, container, false);
        Button btnGuardarGlobal = rootView.findViewById(R.id.checkGuardar);


        //configurar el listener
        btnGuardarGlobal.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                guardarTodosLosDatos(); // cuando se le de click al boton llama a la funcion
            }
        });


        ///
        // Evita que el teclado no cubra los sku
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        // Establece la orientacion
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        LoadData();

//permisos de desarrollador
        new DeveloperOptions().modalDevOptions(getActivity());
        RequestPermissions requestPermissions = new RequestPermissions(getContext(), getActivity());
        requestPermissions.showPermissionDialog();

//aqui se crea la interaccion de base de datos
        handler = new DatabaseHelper(getContext(), Provider.DATABASE_NAME, null, 1);
        /// inicializacion de elementos
        spCategoria = (Spinner) rootView.findViewById(R.id.spCategoria);
        spSubcategoria = (Spinner) rootView.findViewById(R.id.spSubcategoria);
        spPresentacion = (Spinner) rootView.findViewById(R.id.spPresentacion);
        spMarca = (Spinner) rootView.findViewById(R.id.spMarca);
        spVariante = (Spinner) rootView.findViewById(R.id.spVariante);
        txtSKUCode = (EditText) rootView.findViewById(R.id.txtSKUCode);
        txtDescripcion = (EditText) rootView.findViewById(R.id.txtDescripcionSKU);

        //configuracion del RECYCLE
        recyclerView = (RecyclerView) rootView.findViewById(R.id.lvSKUCode);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        layout_skuName = (LinearLayout) rootView.findViewById(R.id.layout_skuName);
        layout_skuDescripcion = (LinearLayout) rootView.findViewById(R.id.layout_skuDescripcion);

        empty = (TextView) rootView.findViewById(R.id.recyclerview_data_empty);

        layout_skuName.setVisibility(View.INVISIBLE);
        layout_skuDescripcion.setVisibility(View.INVISIBLE);

        filtrarCategoria(manufacturer);

        new GuardarLog(getContext()).saveLog(user, "", "Ingreso al Módulo Propensos");

        return rootView;
    }
// se cambio aqui el getcategoria.

    public void filtrarCategoria(String manufacturer) {
        // handler.getCategoriaPropensosYMalEstado ahora puede aceptar una cadena de filtro para 'cadena'

        List<String> operadores = handler.getCategoriaPropensosYMalEstado(manufacturer, modulo);
        if (operadores.size() == 2) {
            operadores.remove(0);
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, operadores);
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategoria.setAdapter(dataAdapter);
        spCategoria.setOnItemSelectedListener(this);
        spCategoria.post(() -> spCategoria.setSelection(0));
    }

    /// MPIN
    // ▼▼▼  METODO PARA GUARDAR TODOS LOS DATOS DEL LISTVIEW ▼▼▼
    private void guardarTodosLosDatos() {
        //VALIDACION para comprobar si esta nulo o vacio
        if (dataAdapter == null || dataAdapter.getItemCount() == 0) {
            Toast.makeText(requireContext(), "No hay datos para guardar", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean hayCambios = false;
        // First pass: validate all items in the adapter's data list
        //Para cada posición, comprueba si se ha introducido algún dato
        for (int i = 0; i < dataAdapter.productos.size(); i++) {
            Base_portafolio_productos item = dataAdapter.productos.get(i);

            // Verificar si este ítem tiene datos
            boolean tieneDatos = (item.getTotal() != null && !item.getTotal().isEmpty()) ||
                    (item.getFechaCaducidad() != null && !item.getFechaCaducidad().isEmpty()) ||
                    (item.getFotoBitmap() != null); // Check if a photo bitmap is associated

            // verificar si hace falta campos, si es cadena KYWI-MPIN
            String ToastCorrespondiente;

            if (cadena.contains("KYWI")) {
                ToastCorrespondiente = item.getSkuCliente();
                            }
            else {
                ToastCorrespondiente = item.getSku();
            }

            if (tieneDatos) {
                hayCambios = true;

                // Validaciones -  para el toast correcto
                if (item.getTotal() == null || item.getTotal().trim().isEmpty()) {
                    Toast.makeText(requireContext(), "Ingrese el total para: " + ToastCorrespondiente, Toast.LENGTH_SHORT).show();
                    return;
                }

//                // VALIDACION PARA QUE NO PERMITA GUARDAR EL TOTAL SI ES IGUAL A A "0":
//                try {
//                    int totalEntered = Integer.parseInt(item.getTotal());
//                    if (totalEntered == 0) {
//                        Toast.makeText(requireContext(), "El total no puede ser 0 para: " + ToastCorrespondiente, Toast.LENGTH_SHORT).show();
//                        return; // Stop the saving process
//                    }
//                } catch (NumberFormatException e) {
//                    // Este caso debería idealmente ser prevenido por inputType="number" en XML
//                    // o manejado por una comprobación previa de `isEmpty()` si el campo está vacío,
//                    // pero es bueno capturarlo si de alguna manera se filtra una entrada no numérica.
//                    Toast.makeText(requireContext(), "Formato de total inválido para: " + ToastCorrespondiente, Toast.LENGTH_SHORT).show();
//                    return;
//                }

                if (item.getFechaCaducidad() == null || item.getFechaCaducidad().trim().isEmpty()) {
                    Toast.makeText(requireContext(), "Ingrese fecha de caducidad para: " + ToastCorrespondiente, android.widget.Toast.LENGTH_SHORT).show();
                    return;
                }

                if (item.getFotoBitmap() == null) {
                    Toast.makeText(requireContext(), "Tome foto para: " + ToastCorrespondiente, android.widget.Toast.LENGTH_SHORT).show();
                    return;
                }

//                if (item.getTipoUnidad() == null || item.getTipoUnidad().isEmpty() || item.getTipoUnidad().equals("SELECCIONE")) {
//                    Toast.makeText(requireContext(), "Seleccione tipo de unidad para " + item.getSku(), Toast.LENGTH_SHORT).show();
//                    return;
//                }
            }
        }

        if (!hayCambios) {
            Toast.makeText(requireContext(), "No hay datos para guardar", Toast.LENGTH_SHORT).show();
            return;
        }

        // Diálogo de confirmación
        new AlertDialog.Builder(requireContext())
                .setTitle("Confirmar")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setMessage("¿Esta seguro de guardar la informacion?, No podra editar la informacion guardada")
                .setPositiveButton("Sí", (dialog, which) -> guardarDatosValidados())
                .setNegativeButton("No", null)
                .show();
    }

    private void guardarDatosValidados() {
        boolean todosGuardados = true; //bandera
        StringBuilder errores = new StringBuilder();
        // Assuming 'mensaje' is built up during the loop for WhatsApp
        // You might need to adjust how 'mensaje' is constructed to include all saved items.
        StringBuilder whatsappMessageBuilder = new StringBuilder(); // para manipular las cadenas de texto en este caso , se la usa cuando se necesitara muchas veces esa variable
        whatsappMessageBuilder.append(" \uD83D\uDC4B ¡HOLA!").append("\rTe saluda\r").append("*")
                .append(user).append("*").append("\r\nPromotor de GRUPO LUCKY.\n")

        // Add the top border line
        //.append("\n---------------------------------------------------")
        // Add the original text line that you want to emphasize
                .append("\r*").append(codigo_pdv).append("*").append("\r-\r").append("*").append(punto_venta).append("*").append("\r-\r").append("*").append(zone.trim()).append("*")
        // Add the bottom border line
       // .append("\n---------------------------------------------------")
       .append("\r\n*\uD83D\uDC47 Link de descarga:* ")
                .append("\r\n https://www.xplora.ec/App/XploraEcuador/mantenimiento_descarga_bases/Pintuco/includes/link_descarga.php?usuario=" + user + "&fecha=" + fecha + "")
                .append("\n");


        // Segundo pasO para guardar datos de la lista de datos del adaptador
        for (int i = 0; i < dataAdapter.productos.size(); i++) { //iterar sobre cada punto de la clase base....
            Base_portafolio_productos item = dataAdapter.productos.get(i);

            // Solo procesar elementos con datos
            if ((item.getTotal() != null && !item.getTotal().isEmpty()) ||
                    (item.getFechaCaducidad() != null && !item.getFechaCaducidad().isEmpty()) ||
                    (item.getFotoBitmap() != null)) {

                try {
                    String sku = item.getSku();
                    String sku_cliente = item.getSkuCliente();
                    String producto = "";
                    String total = item.getTotal();
                    String caducidad = item.getFechaCaducidad(); // aqui obtengo la fecha de caducidad que introduce el usuario
                    String tipoUnidad = item.getTipoUnidad();
                    // No necesitas obtener pvp directamente de item, lo obtendrás de la DB
                    // String pvp = item.getPvp();

                    int totalConvertido = Integer.parseInt(total);

                    if(cadena.contains("KYWI"))
                    {
                        producto = sku_cliente;
                    }
                    else {
                        producto = sku;
                    }

                    // Procesar foto
                    String foto = "NO_FOTO";
                    if (item.getFotoBitmap() != null) {
                        foto = procesarFoto(item.getFotoBitmap(), producto);
                    }

                    ///
                    litros = obtenerLitros(totalConvertido, producto);


                    valorizado = obtenerValorizado(totalConvertido, producto);


                    dias_restantes = calcularDiasRestantes(caducidad, fecha);





                    // Append data for WhatsApp message
                    whatsappMessageBuilder.append("\r\n *SKU:* ").append(producto)
                            .append("\r\n \uD83E\uDD0F *Cantidad:* ").append(total).append("\r").append(tipoUnidad)
                            .append("\r\n \uD83D\uDCA7 *Total Litros:* ").append(litros)
                            .append("\r\n \uD83D\uDDD3\uFE0F *Fecha Caducidad:* ").append(caducidad)
                            .append("\r\n ⌛ *Dias restantes:* ").append(dias_restantes) // Use the calculated dias_restantes
                        //    .append("\r\n*Tipo Unidad:* ").append(tipoUnidad)
                            .append("\r\n \uD83D\uDCB8 *Valorizado:* ").append("\r*$").append(valorizado).append("*")

                            .append("\r\n");

                    // Insertar en BD
                    insertData(sku, tipoUnidad, total, caducidad, foto, litros, dias_restantes, valorizado, sku_cliente);

                    // Opcionalmente, borra los datos del ítem después de guardarlo si ya no debería ser editable
                    // O ACTUALIZAR su "estado" dentro del ítem mismo
                    item.setTotal(""); // Clear data after saving
                    item.setFechaCaducidad("");
                    item.setTipoUnidad(""); // Reset spinner selection
                    item.setFotoBitmap(null); // Clear bitmap
                    item.setEstado("REALIZADO"); // Update status in data model




                    //Notifica al adaptador que un elemento específico en la posición ha cambiado, lo que
                    // //hace que vuelva a vincular el contenedor de vista y actualice la interfaz de usuario
                    dataAdapter.notifyItemChanged(i);

                } catch (Exception e) {
                    todosGuardados = false;
                    errores.append("Error en ").append(item.getSku())
                            .append(": ").append(e.getMessage()).append("\n");
                }
            }
        }


        // Mostrar resultados
        if (!todosGuardados) {
            new AlertDialog.Builder(requireContext())
                    .setTitle("Errores")
                    .setMessage(errores.toString())
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton("Aceptar", null)
                    .show();
        } else {
            Toast.makeText(requireContext(), "Datos guardados correctamente", Toast.LENGTH_SHORT).show();
            // Refresh the list to reflect cleared data and updated statuses
            dataAdapter.notifyDataSetChanged();

            // *** LLAME A LA FUNCIÓN DE ENVÍO DE WHATSAPP AQUÍ ***
            //  variable  'celular' esté correctamente poblada con el número de teléfono del destinatario.
            // Ya lo está cargando en LoadData().
            enviarMensajeWhatsapp(whatsappMessageBuilder.toString());
        }
    }

    private double obtenerValorizado(Integer cantidad, String sku) { //
        Double pvpUnitario;
        // obtengo el  dolar de la database  usando el conector handler
        String pvpStringFromDB = handler.getDolarPropensos(sku, cadena);
        String cleanedPvpString = pvpStringFromDB.trim();
        Log.d("ValorizadoDebug", "SKU: " + sku + ", Raw PVP from DB: '" + pvpStringFromDB + "'");

        try {

            pvpUnitario = Double.parseDouble(cleanedPvpString);
            // Calculate the total valorizado
            Double valorizadoResult = (pvpUnitario * cantidad);
            valorizadoResult = formatearDecimales(valorizadoResult,8);

            return valorizadoResult;
        } catch (NumberFormatException e) {
            System.err.println("Error al convertir a decimal. La cadena de PVP desde DB '" + cleanedPvpString + "' no es un número válido.");

            Log.e("ValorizadoError", "NumberFormatException for SKU: " + sku + ", PVP String: '" + cleanedPvpString + "'", e);
            return 0.0; // Return 0 or handle the error appropriately
        }
    }

    // FUNCION PARA CALCULAR LOS DIAS RESTANTES PARA EL DIALOG OF WHATSAAP
    private Long calcularDiasRestantes(String fechaCaducidadTotal, String fechaActual) {
        try {
            // Parse fechaCaducidadTotal (MM/YYYY) to the last day of the month
            SimpleDateFormat monthYearFormat = new SimpleDateFormat("MM/yyyy", Locale.getDefault());
            Date caducidadDate = monthYearFormat.parse(fechaCaducidadTotal);

            Calendar calCaducidad = Calendar.getInstance();
            calCaducidad.setTime(caducidadDate);
            // Set to the last day of the month
            calCaducidad.set(Calendar.DAY_OF_MONTH, calCaducidad.getActualMaximum(Calendar.DAY_OF_MONTH));
            caducidadDate = calCaducidad.getTime();

            // Parse fechaActual (DD/MM/YYYY)
            SimpleDateFormat dayMonthYearFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Date actualDate = dayMonthYearFormat.parse(fechaActual);

            // Calculate the difference in milliseconds
            long diff = caducidadDate.getTime() - actualDate.getTime();

            // Convert milliseconds to days
            return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

        } catch (ParseException e) {
            Log.e("DiasRestantesError", "Error parsing dates: " + e.getMessage());
            return 0L; // Return 0 or handle the error appropriately
        }
    }

    //funcion para redondear decimales jc
    public static Double formatearDecimales(Double numero, Integer numeroDecimales) {
        return Math.round(numero * Math.pow(10, numeroDecimales)) / Math.pow(10, numeroDecimales);
    }
    private double obtenerLitros(Integer total, String sku) {
        Double totalLitros;

        String contenido = handler.getContenidoPropensos(sku, cadena);
        //como String es inmutable y se lo quiere parar a double se asgina otra variable
        String contenidoPropensos = contenido.replace("L", "").trim();

        //contenido.replace("L", "").trim();//  para eliminar espacios  al principio y al final porsicaso
// PARA CONVERITIR ESE CONTENIDO STRING A DOUBLE:
        try {
            double valorDouble = Double.parseDouble(contenidoPropensos);



            totalLitros = (valorDouble * total);
            totalLitros = formatearDecimales(totalLitros,8);




            return totalLitros;
        } catch (NumberFormatException e) {
            // Manejo de error: Si la cadena limpia no es un número válido
            System.err.println("Error al convertir a double. La cadena '" + contenidoPropensos + "' no es un número válido.");
            return 0.0; // O un valor que indique un error, como -1.0 si es apropiado
        }





        //return total;
    }

    private String procesarFoto(Bitmap temporal, String producto) {
        int n = Resources.getSystem().getDisplayMetrics().widthPixels;
        int size = n < 1080 ? 35 : 90;
//MARCA DE AGUA
        String ciudad = "Ciudad: " + handler.getCityPdv(codigo_pdv);
        String local = "Local: " + punto_venta;
        String usuario = "Usuario: " + user;
        String fechaHora = "Fecha y hora: " + fecha + " " + hora;

        ImageMark im = new ImageMark();
        Bitmap watermark = im.mark(temporal, ciudad, local, usuario, fechaHora, Color.YELLOW, 100, size, false);
        int mheight = (int) (watermark.getHeight() * (1024.0 / watermark.getWidth()));
        Bitmap scaled = Bitmap.createScaledBitmap(watermark, 1024, mheight, true);
        //SE LLAMA PARA CONVERTIR EL MAPA DE BITS EN UNA CADENA EN BASE 64
        return getStringImage(scaled);
    }

    // This method is no longer directly used for bulk save validation, as it now validates the data model.
    private boolean esFormularioValido(Base_portafolio_productos item) {
        boolean valido = true;

        if (item.getTipoUnidad() == null || item.getTipoUnidad().isEmpty() || item.getTipoUnidad().equals("SELECCIONE")) {
            Toast.makeText(getContext(), "Debe seleccionar el tipo de conteo para " + item.getSku(),
                    Toast.LENGTH_SHORT).show();
            valido = false;
        }

        if (item.getTotal() == null || item.getTotal().trim().isEmpty()) {
            Toast.makeText(getContext(), "Debe ingresar el total para " + item.getSku(),
                    Toast.LENGTH_SHORT).show();
            valido = false;
        }

        if (item.getFechaCaducidad() == null || item.getFechaCaducidad().trim().isEmpty()) {
            Toast.makeText(getContext(), "Debe ingresar la fecha de caducidad para " + item.getSku(),
                    Toast.LENGTH_SHORT).show();
            valido = false;
        }

        if (item.getFotoBitmap() == null) {
            Toast.makeText(getContext(), "Debe tomar una foto para " + item.getSku(),
                    Toast.LENGTH_SHORT).show();
            valido = false;
        }

        return valido;
    }
    // FIN DEL PASO 3 ▲▲▲


    /// /

    public void filtrarSubcategoria(String categoria, String manufacturer) {
        List<String> operadores = handler.getSubcategoriaFlooring(categoria, manufacturer);
        if (operadores.size() == 2) {
            operadores.remove(0);
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, operadores);
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSubcategoria.setAdapter(dataAdapter);
        spSubcategoria.setOnItemSelectedListener(this);
    }

    public void filtrarBrand(String categoria, String manufacturer) {
        List<String> operadores = handler.getMarcaPropensosYMalEstado(categoria, manufacturer, modulo);
        if (operadores.size() == 2) {
            operadores.remove(0);
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, operadores);
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMarca.setAdapter(dataAdapter);
        spMarca.setOnItemSelectedListener(this);
    }

    public void LoadData() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        id_pdv = sharedPreferences.getString(Constantes.IDPDV, Constantes.NODATA);
        user = sharedPreferences.getString(Constantes.USER, Constantes.NODATA);

        user = sharedPreferences.getString(Constantes.USER, Constantes.NODATA);
        id_pdv = sharedPreferences.getString(Constantes.PHARMA_ID, Constantes.NODATA);
        codigo_pdv = sharedPreferences.getString(Constantes.CODIGO, Constantes.NODATA);
        punto_venta = sharedPreferences.getString(Constantes.PDV, Constantes.NODATA);
        fecha = sharedPreferences.getString(Constantes.FECHA, Constantes.NODATA);
        hora = sharedPreferences.getString(Constantes.HORA, Constantes.NODATA);
        format = sharedPreferences.getString(Constantes.FORMAT, Constantes.NODATA);
        celular =sharedPreferences.getString(Constantes.CELULAR, Constantes.NODATA);
        cadena = sharedPreferences.getString(Constantes.SUBCANAL, Constantes.NODATA);
        zone = sharedPreferences.getString(Constantes.ZONE, Constantes.NODATA);
        //  litros =sharedPreferences.getString(Constantes.LITROS, Constantes.NODATA);
        // dias_restantes =sharedPreferences.getString(Constantes.DIAS_RESTANTES, Constantes.NODATA);
        //valorizado =sharedPreferences.getString(Constantes.VALORIZADO, Constantes.NODATA);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        try {
            date = dateFormat.parse(i2 + "/" + (i1 + 1) + "/" + i);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String outDate = dateFormat.format(date);

        txtfechav.setText(outDate);

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView == spCategoria) {
            try {
                categoria = adapterView.getItemAtPosition(i).toString();
                filtrarBrand(categoria, manufacturer);
                recyclerView.setAdapter(null);
            } catch (Exception e) {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }

        if (adapterView == spMarca) {
            try {
                brand = adapterView.getItemAtPosition(i).toString();
                showRecyclerView(categoria, brand, manufacturer);
            } catch (Exception e) {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
        if (adapterView == spVariante) {
            try {
                variante = adapterView.getItemAtPosition(i).toString();
            } catch (Exception e) {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }

    public void showRecyclerView(String categoria, String brand, String manufacturer) {
        listProductos = handler.filtrarListProductosPropensos(categoria, brand, manufacturer, modulo,cadena);
        dataAdapter = new CustomAdapterPropensos(getContext(), listProductos);
        if (!dataAdapter.productos.isEmpty()) {
            empty.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.setAdapter(dataAdapter);

            txtSKUCode.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    dataAdapter.getFilter().filter(txtSKUCode.getText().toString());
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
        } else {
            recyclerView.setVisibility(View.GONE);
            empty.setVisibility(View.VISIBLE);
        }
    }

    public void listUpdate(ArrayList<Base_portafolio_productos> data) {
        if (!data.isEmpty()) {
            recyclerView.setVisibility(View.VISIBLE);
            empty.setVisibility(View.INVISIBLE);
            dataAdapter = new CustomAdapterPropensos(getContext(), data);
            recyclerView.setAdapter(dataAdapter);
        } else {
            recyclerView.setVisibility(View.GONE);
            empty.setVisibility(View.VISIBLE);
        }
    }

    public void onResume() {
        super.onResume();
        Log.i("ACTIVITY", "ON RESUME");
        new DeveloperOptions().modalDevOptions(getActivity());
    }


    // Refactored CustomAdapterPropensos to properly extend RecyclerView.Adapter and implement Filterable
    public class CustomAdapterPropensos extends RecyclerView.Adapter<CustomAdapterPropensos.ViewHolder> implements Filterable {
        private List<Base_portafolio_productos> productos;
        private List<Base_portafolio_productos> productosFull; // Full list for filtering
        private Context context;
        private DatabaseHelper handler; // Added to access handler methods
        private String codigo_pdv, user, modulo;

        // Constructor actualizado
        public CustomAdapterPropensos(Context context, List<Base_portafolio_productos> productos) {
            this.context = context;
            this.productos = productos;
            this.productosFull = new ArrayList<>(productos); // Initialize full list for filtering
            this.handler = new DatabaseHelper(context, Provider.DATABASE_NAME, null, 1); // Initialize handler
            SharedPreferences sharedPreferences = context.getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
            this.codigo_pdv = sharedPreferences.getString(Constantes.CODIGO, Constantes.NODATA);
            this.user = sharedPreferences.getString(Constantes.USER, Constantes.NODATA);
            this.modulo = "PROPENSOS"; // Or pass it in constructor if it can vary
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView lblSku;
            public TextView lblsku_Cliente;


            public TextView lblEstado;
            public TextView lblEstadoFoto;
            public Spinner spTipoUnidades;
            public EditText txtTotal;
            public TextView lblFechaCaducidadTotal;
            public ImageButton btnFechaCaducidadTotal;
            public ImageView ivFoto;
            public ImageButton btnCamera;
            public ImageButton ibPreview;

            public ViewHolder(View itemView) {
                super(itemView);
                lblSku = itemView.findViewById(R.id.lblSku);
                lblsku_Cliente = itemView.findViewById(R.id.lblSkuCliente);
                lblEstado = itemView.findViewById(R.id.lblEstado);
                lblEstadoFoto = itemView.findViewById(R.id.lblEstadoFoto);
                spTipoUnidades = itemView.findViewById(R.id.spTipoUnidades);
                txtTotal = itemView.findViewById(R.id.txtTotal);
                lblFechaCaducidadTotal = itemView.findViewById(R.id.lblFechaCaducidadTotal);
                btnFechaCaducidadTotal = itemView.findViewById(R.id.btnFechaCaducidadTotal);
                ivFoto = itemView.findViewById(R.id.ivFoto);
                btnCamera = itemView.findViewById(R.id.ibCargarFoto);
                ibPreview = itemView.findViewById(R.id.ibPreview);

                // Initialize spinner adapter for each item
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                        R.array.opciones_spinner, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spTipoUnidades.setAdapter(adapter);


                if(cadena.contains("KYWI")){
                    lblSku.setVisibility(View.GONE);
                    lblsku_Cliente.setVisibility(View.VISIBLE);
                    }
                else{
                    lblSku.setVisibility(View.VISIBLE);
                    lblsku_Cliente.setVisibility(View.GONE);

                }

                // Add listeners to update the data model directly
                txtTotal.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        // Update the data model as text changes
                        if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                            productos.get(getAdapterPosition()).setTotal(s.toString());
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });
//listener para el spiner tipounidades
                spTipoUnidades.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) { // este es cuando elusuario si elige alguna opccion del spinner
                        if (getBindingAdapterPosition() != RecyclerView.NO_POSITION) {
                            productos.get(getBindingAdapterPosition()).setTipoUnidad(parent.getItemAtPosition(position).toString()); //cuando si hay una seleccion
                        }
                    }
                  //  @Override
//                    public void onNothingSelected(AdapterView<?> parent) { //Este metodo se llama cuando la selección actual desaparece por alguna razón
//                      /*  if (getAdapterPosition() != RecyclerView.NO_POSITION) {
//                            productos.get(getAdapterPosition()).setTipoUnidad(""); // Or a default value
//                        }*/
//                    }
                  public void onNothingSelected(AdapterView<?> parent) { // sirve como un plan de respaldo cuando no hya nada seleccionado..
                      // Este metodo se llama cuando el elemento seleccionado actualmente se elimina o la selección queda vacía.
                      // Es una buena práctica asegurarse de que se establezca un valor predeterminado aquí si el campo no puede ser nulo.
                      if (getBindingAdapterPosition() != RecyclerView.NO_POSITION) {
                         // Establezca un valor predeterminado si no hay nada seleccionado.
                          // O si el primer elemento de la matriz es el valor predeterminado deseado.
                          productos.get(getBindingAdapterPosition()).setTipoUnidad(parent.getItemAtPosition(0).toString()); // lo cambia por el primer elemento de la lista
                      }
                  }
                });
            }
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.list_row_propensos, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Base_portafolio_productos item = productos.get(position);

            String sku = item.getSku();
            String sku_cliente = item.getSkuCliente();
            holder.lblSku.setText(sku);
            holder.lblsku_Cliente.setText(sku_cliente);
            holder.txtTotal.setText(item.getTotal());
            holder.lblFechaCaducidadTotal.setText(item.getFechaCaducidad());

            String estado = handler.getEstadoPropensosYProductosMalEstado(codigo_pdv, user, sku, modulo);
            item.setEstado(estado); // Update the item's state in the data model
            holder.lblEstado.setText(estado);
            if (estado.equalsIgnoreCase("REALIZADO")) {
                holder.lblEstado.setBackgroundColor(ContextCompat.getColor(context, android.R.color.holo_green_light));
            } else {
                holder.lblEstado.setBackgroundColor(ContextCompat.getColor(context, android.R.color.holo_red_dark)); // Default for non-REALIZADO
            }

            // Restore state from the data model

            // Set spinner selection
            ArrayAdapter<CharSequence> adapter = (ArrayAdapter<CharSequence>) holder.spTipoUnidades.getAdapter();
            if (item.getTipoUnidad() != null && !item.getTipoUnidad().isEmpty()) {
                int spinnerPosition = adapter.getPosition(item.getTipoUnidad());
                holder.spTipoUnidades.setSelection(spinnerPosition);
            } else {
                holder.spTipoUnidades.setSelection(0); // por defecto devuelve al primer elemento del spinner
                // IMPORTANT: Also update the data model if it's null, to match the UI's default.
                item.setTipoUnidad(adapter.getItem(0).toString()); // Assuming the first item is the desired default
            }
            // Restore image
            if (item.getFotoBitmap() != null) {
                holder.ivFoto.setImageBitmap(item.getFotoBitmap());
                holder.lblEstadoFoto.setText("FOTO OK");
                holder.lblEstadoFoto.setBackgroundColor(ContextCompat.getColor(context, android.R.color.holo_green_light));
            } else {
                holder.ivFoto.setImageDrawable(null); // Clear image
                holder.lblEstadoFoto.setText("NO FOTO");
                holder.lblEstadoFoto.setBackgroundColor(ContextCompat.getColor(context, android.R.color.holo_red_dark));
            }


            holder.btnCamera.setOnClickListener(v -> {
                // Set the fragment's references to the current item's ImageView and TextView
                // and store the position of the item requesting the photo.
                currentImageViewForPhoto = holder.ivFoto;
                currentLblEstadoFotoForPhoto = holder.lblEstadoFoto;
                currentPhotoItemPosition = holder.getAdapterPosition();
                cargarImagen(); // Call the fragment's method to handle camera intent
            });

            holder.ibPreview.setOnClickListener(v -> {
                if (holder.ivFoto.getDrawable() != null) {
                    mostrarVistaPrevia(holder.ivFoto.getDrawable());
                } else {
                    Toast.makeText(context, "No hay foto cargada", Toast.LENGTH_SHORT).show();
                }
            });

            holder.btnFechaCaducidadTotal.setOnClickListener(v -> {
                // Pass the TextView and the item to the date picker method
                cargarFechaCaducidad2(holder.lblFechaCaducidadTotal, item, holder.getAdapterPosition());
            });
        }

        @Override
        public int getItemCount() {
            return productos.size();
        }

        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    List<Base_portafolio_productos> filteredList = new ArrayList<>();

                    if (constraint == null || constraint.length() == 0) {
                        filteredList.addAll(productosFull);
                    } else {
                        String filterPattern = constraint.toString().toLowerCase().trim();

                        for (Base_portafolio_productos item : productosFull) {
                            if (item.getSku().toLowerCase().contains(filterPattern)) {
                                filteredList.add(item);
                            }
                        }
                    }

                    FilterResults results = new FilterResults();
                    results.values = filteredList;
                    return results;
                }

                @SuppressWarnings("unchecked")
                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    productos.clear();
                    productos.addAll((List<Base_portafolio_productos>) results.values);
                    notifyDataSetChanged();
                }
            };
        }

        // Helper methods from the original CustomAdapterPropensos, now moved or adapted
        private void mostrarVistaPrevia(Drawable drawable) {
            AlertDialog.Builder alertadd = new AlertDialog.Builder(context);
            alertadd.setTitle("Vista Previa");
            LayoutInflater factory = LayoutInflater.from(context);
            final View view = factory.inflate(R.layout.vista_previa, null);
            ImageView dialog_imageview = view.findViewById(R.id.dialog_imageview);
            dialog_imageview.setImageDrawable(drawable);
            alertadd.setView(view);
            alertadd.setNeutralButton("Cerrar", (dlg, sumthin) -> {
            });
            alertadd.show();
        }

        // This fn is for month and year only
        private void cargarFechaCaducidad2(TextView lblFechaCaducidad, Base_portafolio_productos item, int position) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.dialog_month_year_picker, null);

            NumberPicker mesPicker = view.findViewById(R.id.monthPicker);
            NumberPicker añoPicker = view.findViewById(R.id.yearPicker);
            Button btnAceptar = view.findViewById(R.id.btnAceptar);
            Button btnCancelar = view.findViewById(R.id.btnCancelar);

            mesPicker.setMinValue(1);
            mesPicker.setMaxValue(12);
            mesPicker.setDisplayedValues(new String[]{
                    "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
                    "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
            });

            Calendar calendar = Calendar.getInstance();
            int currentYear = calendar.get(Calendar.YEAR);
            añoPicker.setMinValue(1900);
            añoPicker.setMaxValue(currentYear + 50);
            añoPicker.setValue(currentYear);

            // If an existing date is set, pre-fill the pickers
            if (item.getFechaCaducidad() != null && !item.getFechaCaducidad().isEmpty()) {
                try {
                    String[] parts = item.getFechaCaducidad().split("/");
                    if (parts.length == 2) {
                        mesPicker.setValue(Integer.parseInt(parts[0]));
                        añoPicker.setValue(Integer.parseInt(parts[1]));
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }


            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setView(view);

            AlertDialog ad = builder.create();

            btnAceptar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int mesSelecionado = mesPicker.getValue();
                    int añoSeleccionado = añoPicker.getValue();

                    String formatoFecha = String.format(Locale.getDefault(), "%02d/%d", mesSelecionado, añoSeleccionado);
                    lblFechaCaducidad.setText(formatoFecha);
                    item.setFechaCaducidad(formatoFecha); // Update data model
                    notifyItemChanged(position); // Notify adapter of change
                    ad.dismiss();
                }
            });

            btnCancelar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ad.dismiss();
                }
            });
            ad.show();
        }
    }


    //************METODOS PARA TAKE-PHOTO Y UPLOAD
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Ensure currentImageViewForPhoto, currentLblEstadoFotoForPhoto, and currentPhotoItemPosition are set before calling CameraActivity
        // This part is crucial for linking the result back to the correct RecyclerView item.
        if (resultCode == RESULT_OK && currentImageViewForPhoto != null && currentLblEstadoFotoForPhoto != null && currentPhotoItemPosition != -1) {
            Bitmap takenBitmap = null;
            switch (requestCode) {
                case COD_SELECCIONA: // For gallery selection
                    Uri filePath = data.getData();
                    try {
                        takenBitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), filePath);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case COD_FOTO: // For camera capture
                    Uri cameraImageUri = data.getData(); // Get the URI from the intent
                    if (cameraImageUri != null) {
                        try {
                            takenBitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), cameraImageUri);
                            // No need for MediaScannerConnection here if CameraActivity handles saving and you get the URI
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }

            if (takenBitmap != null) {
                scaleImage(currentImageViewForPhoto, currentLblEstadoFotoForPhoto, takenBitmap, currentPhotoItemPosition);
            }
            // Reset for the next photo capture
            currentImageViewForPhoto = null;
            currentLblEstadoFotoForPhoto = null;
            currentPhotoItemPosition = -1;
        }
        // Handle cases where the camera activity was canceled or failed (resultCode != RESULT_OK)
        else if (resultCode == RESULT_CANCELED) {
            Toast.makeText(getContext(), "Captura de foto cancelada.", Toast.LENGTH_SHORT).show();
            // Reset if canceled to prevent issues with next photo attempt
            currentImageViewForPhoto = null;
            currentLblEstadoFotoForPhoto = null;
            currentPhotoItemPosition = -1;
        }
    }

    //Permite hacer la imagen mas pequeña para mostrarla en el ImageView
    public void scaleImage(ImageView imageView, TextView lblEstadoFoto, Bitmap bitmap, int position) {
        try {
            int mheight = (int) (bitmap.getHeight() * (1024.0 / bitmap.getWidth()));
            Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 1024, mheight, true);
            imageView.setImageBitmap(scaled);

            // Update the data model with the new bitmap
            if (position != RecyclerView.NO_POSITION && dataAdapter != null) {
                dataAdapter.productos.get(position).setFotoBitmap(scaled);
                dataAdapter.notifyItemChanged(position); // Notify adapter to refresh the item
            }

            lblEstadoFoto.setText("FOTO OK");
            lblEstadoFoto.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
        } catch (Exception e) {
            androidx.appcompat.app.AlertDialog alertDialog1;
            alertDialog1 = new androidx.appcompat.app.AlertDialog.Builder(getContext()).create();
            alertDialog1.setTitle("Message");
            alertDialog1.setMessage("Notificar \t " + e.toString());
            alertDialog1.show();
            Log.e("compressBitmap", "Error on compress file");
        }
    }

    private void cargarImagen() {
        final CharSequence[] opciones = {"Tomar Foto", "Cargar Imagen", "Cancelar"};
        final androidx.appcompat.app.AlertDialog.Builder alertOpciones = new androidx.appcompat.app.AlertDialog.Builder(getContext());
        alertOpciones.setTitle("Seleccione una Opción");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("Tomar Foto")) {
                    //  tomarFotografia();
                    Intent n = new Intent(getContext(), CameraActivity.class);
                    n.putExtra("activity", "propensos");
                    startActivityForResult(n, COD_FOTO);
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
        startActivityForResult(Intent.createChooser(intent, "Seleccionar una imagen"), COD_SELECCIONA);
    }

    private void tomarFotografia() {
        File fileImagen = new File(Environment.getExternalStorageDirectory(), RUTA_IMAGEN);
        boolean isCreada = fileImagen.exists();
        String nombreImagen = "";
        if (isCreada == false) {
            isCreada = fileImagen.mkdirs();
        }

        if (isCreada == true) {
            nombreImagen = (System.currentTimeMillis() / 1000) + ".jpg";
        }

        path = Environment.getExternalStorageDirectory() +
                File.separator + RUTA_IMAGEN + File.separator + nombreImagen;

        File imagen = new File(path);

        Intent intent = null;
        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            String authorities = getContext().getPackageName() + ".provider";
            Uri imageUri = FileProvider.getUriForFile(getContext(), authorities, imagen);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        } else {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imagen));
        }
        startActivityForResult(intent, COD_FOTO);
    }

    //Metodo que sube la imagen al servidor
    public String getStringImage(Bitmap bmp) {
        String encodedImage;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 35, baos);
        byte[] imageBytes = baos.toByteArray();
        encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    //String to bitmap
    public Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 100) {
            if (grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                //botonCargar.setEnabled(true);
            } else {
                solicitarPermisosManual();
            }
        }

    }

    private void solicitarPermisosManual() {
        final CharSequence[] opciones = {"si", "no"};
        final androidx.appcompat.app.AlertDialog.Builder alertOpciones = new androidx.appcompat.app.AlertDialog.Builder(getContext());
        alertOpciones.setTitle("¿Desea configurar los permisos de forma manual?");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
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
            }
        });
        alertOpciones.show();
    }

    private void cargarDialogoRecomendacion() {
        androidx.appcompat.app.AlertDialog.Builder dialogo = new androidx.appcompat.app.AlertDialog.Builder(getContext());
        dialogo.setTitle("Permisos Desactivados");
        dialogo.setMessage("Debe aceptar los permisos para el correcto funcionamiento de la App");

        dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, 100);
            }
        });
        dialogo.show();
    }



    // This method is now used internally by the saving logic, not directly by UI clearing for a single item.
    // The individual item state is reset in `guardarDatosValidados()`.
    public void limpiarFormulario(Spinner spTipoUnidades, EditText txtTotal, ImageView ivFoto, TextView lblFechaCaducidadTotal, TextView lblEstadoFoto) {
        spTipoUnidades.setSelection(0);
        txtTotal.setText("");
        lblFechaCaducidadTotal.setText("");
        ivFoto.setImageResource(0);
        lblEstadoFoto.setText("NO FOTO");
        lblEstadoFoto.setBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));
    }

    public void insertData(String producto, String conteoTotal, String total, String caducidadTotal, String foto, Double litros, Long diasRestante, Double valorizado, String sku_cliente) {

        //Almacenar Datos
        ContentValues values = new ContentValues();
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-5"));
        Date currentLocalTime = cal.getTime();
        DateFormat date = new SimpleDateFormat("dd/MM/yyy");
        date.setTimeZone(TimeZone.getTimeZone("GMT-5"));
        String fechaser = date.format(currentLocalTime);
        DateFormat hour = new SimpleDateFormat("HH:mm:ss");
        hour.setTimeZone(TimeZone.getTimeZone("GMT-5"));
        String horaser = hour.format(currentLocalTime);

        String categoria = spCategoria.getSelectedItem().toString();
        String brand = spMarca.getSelectedItem().toString();

        String presentacion = handler.getPresentacionPropensos(producto);
        String contenido = handler.getContenidoPrecios(producto);
        String sector = handler.getSectorPrecios(producto);

        String plataforma = handler.getPlataformaBySku(producto);

        int contador = 0;
        String mensaje = "";


        values.put(ContractInsertPropensosYProdMalEst.Columnas.PHARMA_ID, id_pdv);
        values.put(ContractInsertPropensosYProdMalEst.Columnas.CODIGO, codigo_pdv);
        values.put(ContractInsertPropensosYProdMalEst.Columnas.USUARIO, user);
        values.put(ContractInsertPropensosYProdMalEst.Columnas.SUPERVISOR, punto_venta);
        values.put(ContractInsertPropensosYProdMalEst.Columnas.FECHA, fechaser);
        values.put(ContractInsertPropensosYProdMalEst.Columnas.HORA, horaser);
        values.put(ContractInsertPropensosYProdMalEst.Columnas.SECTOR, sector);
        values.put(ContractInsertPropensosYProdMalEst.Columnas.CATEGORIA, categoria);
        values.put(ContractInsertPropensosYProdMalEst.Columnas.SUBCATEGORIA, "N/A");
        values.put(ContractInsertPropensosYProdMalEst.Columnas.PRESENTACION, presentacion);
        values.put(ContractInsertPropensosYProdMalEst.Columnas.BRAND, brand);
        values.put(ContractInsertPropensosYProdMalEst.Columnas.CONTENIDO, contenido);
        values.put(ContractInsertPropensosYProdMalEst.Columnas.SKU_CODE, producto);
        values.put(ContractInsertPropensosYProdMalEst.Columnas.INVENTARIOS, "");
        values.put(ContractInsertPropensosYProdMalEst.Columnas.SOUVENIRS, "");
        values.put(ContractInsertPropensosYProdMalEst.Columnas.TIPO_CONTEO_TOTAL, conteoTotal);
        values.put(ContractInsertPropensosYProdMalEst.Columnas.TOTAL, total);
        values.put(ContractInsertPropensosYProdMalEst.Columnas.TIPO_CONTEO_DEFECTUOSAS, "N/A");
        values.put(ContractInsertPropensosYProdMalEst.Columnas.CANT_DEFECTUOSAS, "N/A");
        values.put(ContractInsertPropensosYProdMalEst.Columnas.FECHA_CADUCIDAD_TOTAL, caducidadTotal);
        values.put(ContractInsertPropensosYProdMalEst.Columnas.FECHA_CADUCIDAD, "N/A");
        values.put(ContractInsertPropensosYProdMalEst.Columnas.CAUSAL, "N/A");
        values.put(ContractInsertPropensosYProdMalEst.Columnas.FOTO, foto);
        values.put(ContractInsertPropensosYProdMalEst.Columnas.PLATAFORMA, plataforma);
        values.put(ContractInsertPropensosYProdMalEst.Columnas.MODULO, modulo);
        values.put(ContractInsertPropensosYProdMalEst.Columnas.LITROS, litros);
        values.put(ContractInsertPropensosYProdMalEst.Columnas.DIAS_RESTANTES, diasRestante); // Use the calculated value
        values.put(ContractInsertPropensosYProdMalEst.Columnas.VALORIZADO, valorizado);
        values.put(ContractInsertPropensosYProdMalEst.Columnas.SKU_CLIENTE, sku_cliente);
        values.put(Constantes.PENDIENTE_INSERCION, 1);

        getContext().getContentResolver().insert(ContractInsertPropensosYProdMalEst.CONTENT_URI, values);

        if (VerificarNet.hayConexion(getContext())) {
            SyncAdapter.sincronizarAhora(getContext(), true, Constantes.insertPropensosYProdMalEst, null);
            Toast.makeText(getContext(), Mensajes.ON_SYNC_SERVER, Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(getContext(), Mensajes.ON_SYNC_DEVICE, Toast.LENGTH_SHORT).show();
        }




        //CONFIGURACION DEL IF PARA EL CONTADOR

//        if (contador == 0) {
//            mensaje = "_*XPLORA*_ de _*Grupo Lucky*_ te envía la siguiente información. *Cliente:* " + punto_venta + "\r\n";
//        }
//        Log.i("MENSAJE ANTES", mensaje + " " + ientrega);
//        if (ientrega.equalsIgnoreCase("SI")){
//            //istock, icantidad, ioTipoSug;
//            mensaje = mensaje + "\r\n*Sugerido:* " + sku.replace("&", "-").replace("#", "") + "\r\n*Cantidad Inventario:* "+ istock +"\r\n*Cantidad Sugerido:* " + icantidad + "\r\n" +"*Tipo Sugerido:* " + ioTipoSug + "\r\n";
//            contador++;
//                /*if (iobservaciones.equalsIgnoreCase("")) {
//                    mensaje = mensaje + "\r\n*Sugerido:* " + sku.replace("&"l, "-").replace("#", "") + "\r\n*Cantidad:* "+ icantidad +" "+ itipo +"\r\n*Despacho:* " + ientrega + "\r\n";
//                    //enviarMensaje(celular, mensaje);
//                }else{
//                    mensaje = mensaje + "\r\n*Sugerido:* " + sku.replace("&"l, "-").replace("#", "") + "\r\n*Cantidad:* "+ icantidad +" "+ itipo +"\r\n*Despacho:* " + ientrega + "\r\n*Observaciones:* " + iobservaciones +"\r\n";
//                }
//                contador++;*/
//        }

    }
    /// /SECCION DE WHATSAAP
    //Funcion para enviar mensajes, copiado de florringFragment mpin
    // se modifico la funcion de enviarmensajes para que permita codiifcar las variables dentro del url mediante la URI.
    private void enviarMensajeWhatsapp(String messagestr) {
        String phonestr = celular; // Asumimos que 'celular' es donde obtienes el número de la base de datos.

        // 1. Limpiar el número de teléfono: Eliminar '+' y espacios
        if (phonestr != null && !phonestr.isEmpty()) { // Añadir un null check para evitar NullPointerException
            phonestr = phonestr.replace("+", ""); // Elimina el signo '+'
            phonestr = phonestr.replace(" ", "");  // Elimina los espacios
        }

        // Verifica que el mensaje y el número no estén vacíos después de la limpieza
        if (!messagestr.isEmpty() && phonestr != null && !phonestr.isEmpty()) { // Añadir null check para phonestr
            if (isWhatappInstalled() || isWhatappBusinessInstalled()) {
                // Ya no necesitas la condición !phonestr.contains("+") porque ya lo eliminamos.
                // La condición !phonestr.equals("N/A") && !phonestr.equals("NA") se mantiene si esos valores son posibles.
                if (!phonestr.equals("N/A") && !phonestr.equals("NA")) {
                    try {
                        // URL-encode el mensaje completo
                        String encodedMessage = URLEncoder.encode(messagestr, "UTF-8");// esto sirve para la url que se envia se codifique ya que no permite ciertos caracteres

                        // Construye la URL final de la API de WhatsApp con el número limpio
                        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=" + phonestr + "&text=" + encodedMessage));
                        startActivity(i);
                    } catch (UnsupportedEncodingException e) {
                        Log.e("WhatsAppSend", "Error al codificar el mensaje para WhatsApp: " + e.getMessage());
                        Toast.makeText(getContext(), "Error al preparar el mensaje de WhatsApp.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Este Toast se mostrará si el número es "N/A" o "NA"
                    Toast.makeText(getContext(),"Numero de telefono no puede ser: " + celular, Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(),"Whatsapp no esta instalado",Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "Por favor, verificar el número de teléfono o el mensaje, podrían estar vacios", Toast.LENGTH_LONG).show();
        }
    }
    //VERIFICA SI ESTA INSTALADO
    private boolean isWhatappInstalled() {
        PackageManager packageManager = getContext().getPackageManager();
        boolean whatsappInstalled;
        try {
            packageManager.getPackageInfo("com.whatsapp",PackageManager.GET_ACTIVITIES);
            whatsappInstalled = true;
        }catch (PackageManager.NameNotFoundException e) {
            whatsappInstalled = false;
        }
        return whatsappInstalled;
    }

    private boolean isWhatappBusinessInstalled() {
        PackageManager packageManager = getContext().getPackageManager();
        boolean whatsappInstalled;
        try {
            packageManager.getPackageInfo("com.whatsapp.w4b",PackageManager.GET_ACTIVITIES);
            whatsappInstalled = true;
        }catch (PackageManager.NameNotFoundException e) {
            whatsappInstalled = false;
        }
        return whatsappInstalled;
    }




    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}