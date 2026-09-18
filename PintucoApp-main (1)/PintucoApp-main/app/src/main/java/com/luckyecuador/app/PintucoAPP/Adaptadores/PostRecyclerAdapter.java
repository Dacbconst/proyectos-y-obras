package com.luckyecuador.app.PintucoAPP.Adaptadores;

import static android.content.Context.LOCATION_SERVICE;
import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;
import static com.luckyecuador.app.PintucoAPP.ServiceRastreo.LocationService.LOCATION_DISTANCE;
import static com.luckyecuador.app.PintucoAPP.ServiceRastreo.LocationService.LOCATION_INTERVAL;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.SphericalUtil;
import com.luckyecuador.app.PintucoAPP.CameraActivity;
import com.luckyecuador.app.PintucoAPP.Clase.BasePharmaValue;
import com.luckyecuador.app.PintucoAPP.Clase.Base_pharma_value;
import com.luckyecuador.app.PintucoAPP.Clase.Base_versiones;
import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;
import com.luckyecuador.app.PintucoAPP.Conexion.Mensajes;
import com.luckyecuador.app.PintucoAPP.Conexion.VerificarNet;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertGps;
import com.luckyecuador.app.PintucoAPP.DataBase.DatabaseHelper;
import com.luckyecuador.app.PintucoAPP.DataBase.Provider;
import com.luckyecuador.app.PintucoAPP.MenuNavigationActivity;
import com.luckyecuador.app.PintucoAPP.PuntosListActivity;
import com.luckyecuador.app.PintucoAPP.R;
import com.luckyecuador.app.PintucoAPP.Sync.SyncAdapter;
import com.luckyecuador.app.PintucoAPP.Utils.GuardarLog;
import com.luckyecuador.app.PintucoAPP.Utils.ImageMark;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class PostRecyclerAdapter extends RecyclerView.Adapter<BaseViewHolder>  {

    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_NORMAL = 1;
    private boolean isLoaderVisible = false;

    BasePharmaValue pdv = new BasePharmaValue();

    private double latitud_pdv, longitud_pdv, distance_pdv;

    String path, hora;


    Bitmap bitmap;
    final int COD_SELECCIONA = 10;
    final int COD_FOTO = 20;
    private final String CARPETA_RAIZ = "Alicorp/";
    private final String RUTA_IMAGEN = CARPETA_RAIZ + "Justificacion";
    private static final int REQUEST_PICK_IMAGE = 1;
    private Uri imageUri;
    public static ImageView imageView = null;
    public static Bitmap bitmapfinal;
    private Context mContext;

    private String modulo_actual;
    ArrayList<Base_versiones> nuevaVersionList;



    private boolean falta_salida,almuerzo_en_curso;

    private List<BasePharmaValue> mPuntoVentas;

    private FusedLocationProviderClient fusedLocationClient;

    private AlertDialog adJustificacion;

    private ImageButton ibLocalizacion;

    private LocationManager locationManager;
    private LocationListener locationListener;
    private Double latitud = 0.0, longitud = 0.0,distance;


    private TextView txtlatitud;
    private TextView txtlongitud;

    public PostRecyclerAdapter(Context context, List<BasePharmaValue> postItems) {
        this.mContext = context;
        this.mPuntoVentas = postItems;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new ViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false));
            case VIEW_TYPE_LOADING:
                return new FooterHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false));
            default:
                return null;
        }
    }



    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.onBind(position);

        locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                try {


                    if (latitud != null && longitud != null) {

                        //Setear los EditText
                        if (!latitud.equals(location.getLatitude()) && !longitud.equals(location.getLongitude())) {
                            latitud = location.getLatitude();
                            longitud = location.getLongitude();

                        //    txtlatitud.setText(String.valueOf(latitud));
                        //    txtlongitud.setText(String.valueOf(longitud));
                        }

                    }

                    locationManager.removeUpdates(locationListener);
//                locationManager = null;
                } catch (Exception e) {
                    Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
                //Si el GPS esta deshabilitado, abrir la ventana de activacion del GPS en el dispositivo.
                Intent newIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(newIntent);
            }
        };


    }

    @Override
    public int getItemViewType(int position) {
        if (isLoaderVisible) {
            return position == mPuntoVentas.size() - 1 ? VIEW_TYPE_LOADING : VIEW_TYPE_NORMAL;
        } else {
            return VIEW_TYPE_NORMAL;
        }
    }

    @Override
    public int getItemCount() {
        return mPuntoVentas == null ? 0 : mPuntoVentas.size();
    }

    public void add(BasePharmaValue response) {
        mPuntoVentas.add(response);
        notifyItemInserted(mPuntoVentas.size() - 1);
        //notifyItemInserted(mPuntoVentas.size());
    }

    public void addAll(List<BasePharmaValue> postItems) {
        for (BasePharmaValue response : postItems) {
            Log.i("ADD PDV", response.getPos_id());
            add(response);
        }
    }

    private void remove(BasePharmaValue postItems) {
        int position = mPuntoVentas.indexOf(postItems);
        if (position >= 0) {
            mPuntoVentas.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void addLoading() {
        isLoaderVisible = true;
        add(new BasePharmaValue());
    }

    public void removeLoading() {
        isLoaderVisible = false;
        int position = mPuntoVentas.size() - 1;
        BasePharmaValue item = getItem(position);
        if (item != null) {
            mPuntoVentas.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    BasePharmaValue getItem(int position) {
        return mPuntoVentas.get(position);
    }



    public class ViewHolder extends BaseViewHolder  {

        private final Context context;
        private String reSelected, localSelected, id_ruta, codigo, city, channel, modulo, id_servidor, fecha, hora, bateria, usuarioCursor, idRuta, coordCursor, justificacion,justificacionNoPDV, causalJustificacion, descJustificacion, pos_name;
        private String codigo_pdv_pendiente;
        DatabaseHelper handler;

        private int radioButtonSelected = -1;

        private int radioButtonSelected2 = -1;

        private String tipo_relevo = "";

        CardView card_view;
        TextView id;
        TextView textViewTitle;
        TextView textViewTitle2;
        TextView textViewDescription;
        TextView textViewFormato;
        TextView textViewNombreComercial;
        TextView textViewProvincia;
        Button btnJustificar,btnIngresar;
        int currentPosition;

        RadioGroup radioGroup;

        RadioGroup radioGroupJustNoPDV;

        List<String> arrayJustificaciones;
        EditText txtotraJustificacion;

        ImageView imageViewFotoJustifiacion;

        ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
//            ButterKnife.bind(this, itemView);

            card_view = itemView.findViewById(R.id.card_view);
            id = itemView.findViewById(R.id.id);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewTitle2 = itemView.findViewById(R.id.textViewTitle2);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
            textViewFormato = itemView.findViewById(R.id.textViewFormato);
            textViewNombreComercial = itemView.findViewById(R.id.textViewNombreComercial);
            textViewProvincia = itemView.findViewById(R.id.textViewProvincia);
            btnJustificar = itemView.findViewById(R.id.btnJustificar);
            btnIngresar = itemView.findViewById(R.id.btnIngresar);

        }

        protected void clear() {
        }

        public void onBind(int position) {
            super.onBind(position);
            currentPosition = position;
            final BasePharmaValue item = mPuntoVentas.get(position);


            idRuta = item.getId();
            codigo = item.getPos_id();
            pos_name = item.getPos_name();
            city = item.getCity();
            textViewTitle.setText(item.getPos_id());
            id.setText(item.getId());
            textViewDescription.setText("Local: " + item.getPos_name());
            textViewFormato.setText("Canal: " + item.getChannel());
            textViewNombreComercial.setText("Dirección: " + item.getAddress());
            textViewProvincia.setText("Ciudad: " + item.getCity());
            handler = new DatabaseHelper(context, Provider.DATABASE_NAME, null, 1);
            LoadData();

//            textViewTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            textViewTitle2.setVisibility(View.VISIBLE);
            if (item.getTermometro().equals("0")) {
                textViewTitle2.setVisibility(View.GONE);
//                textViewTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_termometro, 0);
            }

            boolean tieneMarcaciones = handler.tieneMarcacionesHoy(codigo, usuarioCursor);
            boolean tieneJustificacionPDVNoVisitdado = handler.tieneJustificacion(codigo, usuarioCursor);

            // Si ya registro su justificacion
            if (tieneMarcaciones || tieneJustificacionPDVNoVisitdado) {
                btnJustificar.setEnabled(false);
                btnJustificar.setBackgroundResource(R.drawable.boton_desactivado);

            } else {
                btnJustificar.setEnabled(true);
            }






            if (modulo_actual.equalsIgnoreCase(Constantes.MODULO_PUNTOS_TARDIO)){
                btnJustificar.setVisibility(View.GONE);
            }


            btnJustificar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    obtenerCoordenadas();
                    alertDialogJustificar(modulo_actual);
                }
            });


            /*
            card_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!item.getPos_id().trim().isEmpty() && !item.getPos_id().equals("")) {
                        Log.i("PDV SELECTED", item.getPos_id());
                        id_ruta = item.getId();
                        codigo = item.getPos_id();
                      //  city = item.getCity();
                        channel = item.getChannel();
                        modulo = item.getModulo();
                        Log.i("CHANNEL", channel);
                        LoadData();
                        String client = handler.getPosNamePdv(codigo);
                        alertDialog(codigo, client, channel, id_ruta, modulo);
                    } else {
                        Toast.makeText(context,"CODIGO NO VÁLIDO", Toast.LENGTH_LONG).show();
                    }
                }
            });
            */

            btnIngresar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!item.getPos_id().trim().isEmpty() && !item.getPos_id().equals("")) {
                        Log.i("PDV SELECTED", item.getPos_id());
                        id_ruta = item.getId();
                        codigo = item.getPos_id();
                        //  city = item.getCity();
                        channel = item.getChannel();
                        modulo = item.getModulo();
                        Log.i("CHANNEL", channel);
                        LoadData();
                        String client = handler.getPosNamePdv(codigo);

                        if (falta_salida && !codigo_pdv_pendiente.equals(codigo)) {
                            String clientePendiente = handler.getPosNamePdv(codigo_pdv_pendiente);
                            Toast.makeText(context, "Debe marcar la salida del PDV " + clientePendiente + " antes de ingresar a otro PDV", Toast.LENGTH_LONG).show();
                        } else if (!almuerzo_en_curso && modulo_actual.equalsIgnoreCase(Constantes.MODULO_PUNTOS_PRINCIPAL)){
                            alertDialog(codigo, client, channel, id_ruta, modulo);
                        } else if(modulo_actual.equalsIgnoreCase(Constantes.MODULO_PUNTOS_TARDIO)){
                            alertDialog(codigo, client, channel, id_ruta, modulo);
                        } else {
                            Toast.makeText(context,"Debe finalizar su almuerzo",Toast.LENGTH_LONG).show();
                        }

                    } else {
                        Toast.makeText(context,"CODIGO NO VÁLIDO", Toast.LENGTH_LONG).show();
                    }
                }
            });





        }





        public void alertDialog(String codigo, String client, String channel, String id_ruta, String modulo) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setIcon(R.drawable.icon_info);
            builder.setTitle("Ingreso al PDV");

            String msg = "¿Vas ingresar al PDV " + client + " a relevar información?";

            if (modulo_actual.equalsIgnoreCase(Constantes.MODULO_PUNTOS_TARDIO)){
                msg = "¿Estas ingresando al PDV " + client + " para el relevo tardio de información?";
            }


            // Obtenemos la version actual
            String fechaVersionActual = context.getString(R.string.version).split(": ")[1];

            nuevaVersionList = handler.getNuevaVersion(fechaVersionActual);

            builder.setMessage(msg);

//            builder.setPositiveButton("Interno", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    openMenu("INTERNO", channel, id_ruta);
//                }
//            });
//
//            builder.setNegativeButton("Externo",new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    openMenu("EXTERNO", channel, id_ruta);
//                }
//            });


            builder.setPositiveButton("Ingresar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    if (modulo_actual.equalsIgnoreCase(Constantes.MODULO_PUNTOS_PRINCIPAL)) {

                        // Si existe una nueva version
                        if (nuevaVersionList.size() > 0) {
                            alertDialogNuevaVersion(nuevaVersionList, client);
                        } else {
                            openMenu("", channel, id_ruta, modulo);
                            new GuardarLog(context).saveLog(usuarioCursor, "", "Ingreso al PDV: " + client);
                        }

                    } else if (modulo_actual.equalsIgnoreCase(Constantes.MODULO_PUNTOS_TARDIO)) {
                        openMenu("", channel, id_ruta, modulo);
                    }

                }
            });

            builder.setNeutralButton("Cancelar",null);

            AlertDialog ad = builder.create();
            ad.show();
        }

        public void openMenu(String tipo_relevo, String channel, String id_ruta, String modulo) {
//            boolean interno = false;
//            if (tipo_relevo.equalsIgnoreCase("INTERNO")) {
//                interno = true;
//            }

            String idpdv = handler.getIdPdv(codigo);
            String tipo = handler.getChannelSegmentPdv(codigo);
            String puntoventa = handler.getZonePdv(codigo);
            String subcanal = handler.getSubcanalPdv(codigo);
            String formato = handler.getFormatPdv(codigo);
            String direccion = handler.getDireccionPdv(codigo);
            String cliente = handler.getPosNamePdv(codigo);
            String zona = handler.getZonePdv(codigo);
            String format = handler.getFormat(codigo); //Código FABRIL
            String vendedor = handler.getNombreComercial(codigo); //Vendedor Asignado
            String celular = handler.getCelularPDV(codigo); //Celular Asignado (del Vendedor para el SMS)
            String supervisor = handler.getSupervisorByCodigo(codigo); //Celular Asignado (del Vendedor para el SMS)

            //se cae sin pos_id
            SaveData(idpdv, codigo, usuarioCursor, cliente, format, tipo, subcanal, vendedor, celular, channel, id_ruta, supervisor, zona);
            insertData(idpdv, codigo, tipo, cliente, formato, direccion, cliente, zona);

            /*if(!isServiceRunning(AsistenciaService.class)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    // Si la versión del SDK es Oreo o superior, utiliza startForegroundService()
                    Intent serviceIntent = new Intent(context, AsistenciaService.class);
                    ContextCompat.startForegroundService(context, serviceIntent);
                } else {
                    // Si la versión del SDK es anterior a Oreo, utiliza startService() normal
                    Intent serviceIntent = new Intent(context, AsistenciaService.class);
                    context.startService(serviceIntent);
                }
            }*/



            /*if (!areFloatingNotificationsEnabled()) {
                // Las notificaciones flotantes no están habilitadas, mostrar un diálogo para redirigir al usuario a la configuración
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Notificaciones flotantes desactivadas");
                builder.setMessage("Para utilizar esta función, habilite las notificaciones flotantes en la configuración de la aplicación.");
                builder.setPositiveButton("Configuración", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                        context.startActivity(intent);
                    }
                });
                builder.setNegativeButton("Cancelar", null);
                builder.show();
            }else{

            }*/




           Intent in = new Intent(context, MenuNavigationActivity.class);
//            in.putExtra(Constantes.INTERNO, interno);
            in.putExtra(Constantes.CANAL, channel);
            in.putExtra(Constantes.SHOW_PRIORITARIOS, true);
            in.putExtra(Constantes.MODULO, modulo);
            context.startActivity(in);
            Activity activity = (Activity) context;
            activity.finish();


        }

        private void openFloatingNotificationsSettings() {
            Intent intent = new Intent();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // Para Android 10 y posteriores, abrir la configuración de notificaciones flotantes directamente
                intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.getPackageName());
            } else {
                // Para versiones anteriores a Android 10, abrir la configuración general de la aplicación
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", context.getPackageName(), null);
                intent.setData(uri);
            }

            context.startActivity(intent);
        }

        private boolean areFloatingNotificationsEnabled() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // Verificar si las notificaciones flotantes están habilitadas en Android 10 y posteriores
                return Settings.canDrawOverlays(context);
            } else {
                // Para versiones anteriores a Android 10, siempre devolver true porque no hay una configuración específica
                return true;
            }
        }

        private boolean isServiceRunning(Class<?> serviceClass) {
            ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
                if (serviceClass.getName().equals(service.service.getClassName())) {
                    return true;
                }
            }
            return false;
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


        public boolean validarFormulario(){

            if (imageView.getDrawable() == null){
                Toast.makeText(context,"No tomó una foto",Toast.LENGTH_SHORT).show();
                return false;
            }

            // Comprobamos que se haya marcado alguna de las opciones
            if (radioButtonSelected != -1) {

                if (justificacion.equalsIgnoreCase("JUSTIFICAR NO VISITA")){

                    if (radioButtonSelected2 != -1){

                        if (justificacionNoPDV.equalsIgnoreCase("OTRAS")
                                || justificacionNoPDV.equalsIgnoreCase("OTROS")
                                || justificacionNoPDV.equalsIgnoreCase("OTRO")){

                            if (txtotraJustificacion.getText().toString().equalsIgnoreCase("") || txtotraJustificacion.getText().toString().isEmpty() || txtotraJustificacion.getText().toString() == null) {
                                Toast.makeText(context, "Debe escribir su justificación.", Toast.LENGTH_SHORT).show();
                                return false;
                            } else {
                                descJustificacion = txtotraJustificacion.getText().toString();
                                return true;
                            }


                        }else{
                            descJustificacion = justificacionNoPDV;
                        }

                    }else{
                        Toast.makeText(context,"Debe escoger una justificación",Toast.LENGTH_SHORT).show();
                        return false;
                    }

                }

                // En caso que se haya escogido la opcion "OTRAS" , se habilitara el campo para escribir la justificacion
                if (justificacion.equalsIgnoreCase("VERIFICAR ENTRADA") || justificacion.equalsIgnoreCase("VERIFICAR SALIDA")) {

                    if (txtotraJustificacion.getText().toString().equalsIgnoreCase("") || txtotraJustificacion.getText().toString().isEmpty() || txtotraJustificacion.getText().toString() == null) {
                        Toast.makeText(context, "Debe escribir su justificación.", Toast.LENGTH_SHORT).show();
                        return false;
                    } else {
                        descJustificacion = txtotraJustificacion.getText().toString();
                        return true;
                    }
                }else {
                  //  descJustificacion = justificacion;
                    return true;
                }


                // En caso que se haya escogido la opcion "OTRAS" , se habilitara el campo para escribir la justificacion

            }else{
                Toast.makeText(context, "Debe escoger una justificación.", Toast.LENGTH_SHORT).show();
            }





            /*
                if (radioButtonSelected2 != -1){
                    if (justificacionNoPDV.equalsIgnoreCase("OTRAS")){
                        if (txtotraJustificacion.getText().toString().equalsIgnoreCase("") || txtotraJustificacion.getText().toString().isEmpty() || txtotraJustificacion.getText().toString() == null) {
                            Toast.makeText(context, "Debe escribir su justificación.", Toast.LENGTH_SHORT).show();
                            return false;
                        } else {
                            descJustificacion = txtotraJustificacion.getText().toString();
                            return true;
                        }
                    }else{
                        descJustificacion = justificacionNoPDV;
                    }
                }else{
                    Toast.makeText(context, "Debe escoger una justificación.", Toast.LENGTH_SHORT).show();
                }*/


            return false;
        }



        public void alertDialogConfirmar(String modulo){


            LayoutInflater myLayout = LayoutInflater.from(context);
            View dialogView = myLayout.inflate(R.layout.alertdialog_confirmacion_justificacion, null);
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setView(dialogView);
            builder.setIcon(R.drawable.icon_warning);


            TextView tvPDV = dialogView.findViewById(R.id.tvPDV);
            Button btnConfirmar = dialogView.findViewById(R.id.btnConfirmar);
            Button btnCancelar = dialogView.findViewById(R.id.btnCancelar);

            AlertDialog ad = builder.create();

            tvPDV.setText(pos_name);

            btnConfirmar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

               //     if (latitud != 0.0 && longitud != 0.0){

                   //     if(insertDataRegistro(latitud,longitud)){

                            /*
                            if (justificacion.equalsIgnoreCase("VERIFICAR ENTRADA")){

                                openMenu("",channel,id_ruta,modulo);

                                boolean tiene_justificacion_entrada = handler.tieneJustificacionEntrada(codigo,usuarioCursor,fecha);

                                if (tiene_justificacion_entrada) {
                                    faltaSalida(true, fecha);
                                } else {
                                    faltaSalida(false, fecha);
                                }
                            }*/

                            insertDataRegistro(latitud,longitud);
                            ad.dismiss();
                            adJustificacion.dismiss();
                            notifyDataSetChanged();
                            txtotraJustificacion.setText("");
                 //       }

                //    }else{

                //        Toast.makeText(context, "Generando las Coordenadas. , Vuelva a intentarlo.", Toast.LENGTH_SHORT).show();
                //    }


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






        public void alertDialogJustificar(String modulo){

            obtenerFecha();


            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setCancelable(false);
            LayoutInflater myLayout = LayoutInflater.from(context);
            View dialogView = myLayout.inflate(R.layout.alertdialog_justificacion, null);

          //  builder.setView(dialogView);
            builder.setIcon(R.drawable.icon_warning);
            builder.setTitle("Justificación");
            builder.setView(LayoutInflater.from(context).inflate(R.layout.alertdialog_causales,null));


            radioGroup = dialogView.findViewById(R.id.radioGroup);
            radioGroupJustNoPDV = dialogView.findViewById(R.id.radioGroupNoVisitaPDV);
            txtotraJustificacion = dialogView.findViewById(R.id.txtOtraJustificacion);
            ImageButton btnCamera = dialogView.findViewById(R.id.btnCamera);
            imageView = dialogView.findViewById(R.id.ivFotoRegistro);


            List<String> justificaciones = new ArrayList<>();
         //    justificaciones.add("VERIFICAR ENTRADA");
         //   justificaciones.add("VERIFICAR SALIDA");
            justificaciones.add("JUSTIFICAR NO VISITA");

            int contadorId = 1;

            for (String item : justificaciones) {
                // Creamos el radiobutton y le damos estilos
                RadioButton radioButton = new RadioButton(context);
                radioButton.setText(item);
                //Soporta todas las versiones
                Typeface typeface = ResourcesCompat.getFont(context, R.font.roboto_light_italic);
                radioButton.setTypeface(typeface);
                radioButton.setId(contadorId);
                radioGroup.addView(radioButton);
                contadorId++;
            }

            // Obtenemos las Justificaciones para la no visita PDV
            arrayJustificaciones = handler.getJustificacion();
            // Recorremos las justificaciones
            for (String item : arrayJustificaciones) {
                // Creamos el radiobutton y le damos estilos
                RadioButton radioButton = new RadioButton(context);
                radioButton.setText(item);
                //Soporta todas las versiones
                Typeface typeface = ResourcesCompat.getFont(context, R.font.roboto_light_italic);
                radioButton.setTypeface(typeface);
                radioButton.setId(contadorId);
                if (item.equals("VERIFICAR ENTRADA") || item.equals("VERIFICAR SALIDA") || item.equals("JUSTIFICAR NO VISITA")) {
                    radioButton.setVisibility(View.GONE);
                }
                radioGroupJustNoPDV.addView(radioButton);
                contadorId++;
            }

            radioGroupJustNoPDV.setVisibility(View.GONE);


            btnCamera.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    cargarImagen();
                }
            });


            //cuando se halla seleccionado
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {

                    radioButtonSelected = checkedId;
                    // Verificar que este seleccionado un radiobutton
                    if (radioButtonSelected != -1){
                        // Obtenemos el radiobbutton seleccionado
                        RadioButton radioButtonSelected = dialogView.findViewById(checkedId);
                        //Obtenemos la justificacion
                        justificacion = radioButtonSelected.getText().toString();
                        // En caso , que se seleccione "OTRAS" se activara el editext para agregar una justificacion
                        /*
                        if (justificacion.equalsIgnoreCase("VERIFICAR ENTRADA") || justificacion.equalsIgnoreCase("VERIFICAR SALIDA")){
                            tipo_relevo = "Interno";
                            txtotraJustificacion.setVisibility(View.VISIBLE);
                        }else{
                            txtotraJustificacion.setVisibility(View.GONE);
                        }*/


                        if (justificacion.equalsIgnoreCase("JUSTIFICAR NO VISITA")){
                            tipo_relevo = "Externo";
                            radioGroupJustNoPDV.setVisibility(View.VISIBLE);


                            radioGroupJustNoPDV.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(RadioGroup radioGroup, int checkedId2) {

                                    radioButtonSelected2 = checkedId2;

                                    // Verificar que este seleccionado un radiobutton
                                    if (radioButtonSelected2 != -1) {
                                        // Obtenemos el radiobbutton seleccionado
                                        RadioButton radioButtonSelected2 = dialogView.findViewById(checkedId2);
                                        //Obtenemos la justificacion
                                        justificacionNoPDV = radioButtonSelected2.getText().toString();

                                        descJustificacion = justificacionNoPDV;


                                        if (justificacionNoPDV.equalsIgnoreCase("OTRAS") ||
                                                justificacionNoPDV.equalsIgnoreCase("OTROS") ||
                                                justificacionNoPDV.equalsIgnoreCase("OTRA")){
                                            txtotraJustificacion.setVisibility(View.VISIBLE);
                                        }else{
                                            txtotraJustificacion.setVisibility(View.GONE);
                                        }
                                    }

                                }
                            });

                        }else{
                            radioGroupJustNoPDV.setVisibility(View.GONE);
                        }

                    }

                }


            });


            /*
            ibLocalizacion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    obtenerCoordenadas();
                }
            });*/

            builder.setPositiveButton("Guardar",null);


            builder.setNegativeButton("Cancelar",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    radioButtonSelected = -1;
                }
            });

            builder.setView(dialogView);
            adJustificacion = builder.create();
            adJustificacion.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialogInterface) {

                    Button btnGuardar = ((AlertDialog)adJustificacion).getButton(AlertDialog.BUTTON_POSITIVE);
                    btnGuardar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(validarFormulario()){
                                alertDialogConfirmar(modulo);
                            }
                        }
                    });

                }
            });


            adJustificacion.show();

        }


//        private void verificarNuevaVersion() {
//
//            // Obtenemos la version actual
//            String fechaVersionActual = context.getString(R.string.version).split(": ")[1];
//
//            nuevaVersionList = handler.getNuevaVersion(fechaVersionActual);
//
//            // Si existe una nueva version
//            if (nuevaVersionList.size() > 0){
//
//                // Crear y configurar el ProgressDialog
//                ProgressDialog progressDialog = new ProgressDialog(context);
//                progressDialog.setTitle("Cargando");
//                progressDialog.setMessage("Por favor espera...");
//                progressDialog.setCancelable(false); // Evitar que el usuario lo cierre manualmente
//                progressDialog.show(); // Mostrar el ProgressDialog
//
//                // Subimos toda la informacion que tenga retenida
//                SyncAdapter.sincronizarAhora(context, true, Constantes.SUBIR_TODO, null);
//
//
//                Handler handler = new Handler();
//
//                // Programar el cierre del ProgressDialog después de 5 segundos
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        // Cerrar el ProgressDialog
//                        if (progressDialog.isShowing() && progressDialog != null) {
//                            progressDialog.dismiss();
//                        }
//                        alertDialogNuevaVersion(nuevaVersionList);
//                    }
//                }, 5000); // 5 segundos
//
//            } else {
//
//
//
//
//            }
//
//
//
//        }

        public void alertDialogNuevaVersion(ArrayList<Base_versiones> nuevaVersionList,String client){

            LayoutInflater myLayout = LayoutInflater.from(context);
            View dialogView = myLayout.inflate(R.layout.alertdialog_nueva_version, null);
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setView(dialogView);

            // Obtenemos informacion de la version
            String fecha_nueva_version = nuevaVersionList.get(0).getFecha_version();
            String parts[] = fecha_nueva_version.split("-");
            // Convertimos a dd/mm/yyyy para mostrarlo en msg
            String formatoddmmyyyy = parts[2] + "/" + parts[1] + "/" + parts[0];
            String link_aplicativo = nuevaVersionList.get(0).getLink_aplicativo();
            String tamañoApk = nuevaVersionList.get(0).getTamaño();

            if (tamañoApk == null){
                tamañoApk = "No Disponible";
            }

            Button btnDescargar = dialogView.findViewById(R.id.btnDescargar);
            Button btnCancelar = dialogView.findViewById(R.id.btnCancelar);
            Button btnIngresar = dialogView.findViewById(R.id.btnIngresar);
            TextView tv_nueva_version = dialogView.findViewById(R.id.tv_nueva_version);
            TextView tv_tamaño = dialogView.findViewById(R.id.tv_tamanio);
            btnCancelar.setVisibility(View.GONE);
            btnIngresar.setVisibility(View.VISIBLE);


            tv_nueva_version.setText(formatoddmmyyyy);
            tv_tamaño.setText(""+tamañoApk);


            AlertDialog ad = builder.create();
            ad.show();


            btnDescargar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialogConfirmNuevaVersion(link_aplicativo);
                }

            });

//            btnCancelar.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                  ad.dismiss();
//                }
//            });


            btnIngresar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openMenu("", channel, id_ruta, modulo);
                    new GuardarLog(context).saveLog(usuarioCursor, "", "Ingreso al PDV: " + client);
                }
            });

        }

        private void alertDialogConfirmNuevaVersion(String link_aplicativo) {
            LayoutInflater myLayout = LayoutInflater.from(context);
            View dialogView = myLayout.inflate(R.layout.alertdialog_conf_nueva_version, null);
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setView(dialogView);


            Button btnConfirmar = dialogView.findViewById(R.id.btnConfirm);
            Button btnCancelar = dialogView.findViewById(R.id.btnCancel);

            AlertDialog ad2 = builder.create();
            ad2.show();

            btnConfirmar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Uri uriUrl = Uri.parse(link_aplicativo);
                    Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                  context.startActivity(launchBrowser);
                    ad2.dismiss();
                }
            });

            btnCancelar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ad2.dismiss();
                }
            });

        }



        public boolean insertDataRegistro(double latitud, double longitud) {
            try {

                Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-5"));
                Date currentLocalTime = cal.getTime();
                DateFormat date = new SimpleDateFormat("dd/MM/yyy");
                date.setTimeZone(TimeZone.getTimeZone("GMT-5"));
                String fechaser = date.format(currentLocalTime);

                DateFormat hour = new SimpleDateFormat("HH:mm:ss");
                hour.setTimeZone(TimeZone.getTimeZone("GMT-5"));
                String horaser = hour.format(currentLocalTime);


                if (latitud != 0.0 && longitud != 0.0){

                    pdv = handler.getPdv(codigo);
                    distance_pdv = Double.parseDouble(pdv.getDistancia());
                    latitud_pdv = Double.parseDouble(pdv.getLatitud());
                    longitud_pdv = Double.parseDouble(pdv.getLongitud());

                    MarkerOptions place1 = new MarkerOptions().position(new LatLng(latitud_pdv, longitud_pdv)).title("Posicion actual");
                    MarkerOptions place2 = new MarkerOptions().position(new LatLng(latitud, longitud)).title("Posicion PDV");

                    distance = SphericalUtil.computeDistanceBetween(place1.getPosition(), place2.getPosition());
                    distance = Double.valueOf(Math.round(distance));

                }else{
                    distance = 0.0;
                }


                String ciudad = "Ciudad: " + city;
                String local = "Local: " + pos_name;
                String usuario = "Usuario: " + usuarioCursor;
                String fechaHora = "Fecha y hora: " + fechaser + " " + horaser;

                Bitmap temporal = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                ImageMark im = new ImageMark();
                Bitmap watermark = im.mark(temporal, ciudad, local, usuario, fechaHora, Color.YELLOW, 100, 85, false);
                int mheight = (int) (watermark.getHeight() * (1024.0 / watermark.getWidth()) );
                Bitmap scaled = Bitmap.createScaledBitmap(watermark, 1024, mheight, true);

                String image = getStringImage(scaled);



                handler = new DatabaseHelper(context, Provider.DATABASE_NAME, null, 1);

                String registro = "JUSTIFICACION";

                if(justificacion.equalsIgnoreCase("JUSTIFICAR NO VISITA")){
                    causalJustificacion = justificacion;
                }

                /*
                if(justificacion.equalsIgnoreCase("VERIFICAR ENTRADA") || justificacion.equalsIgnoreCase("VERIFICAR SALIDA") || justificacion.equalsIgnoreCase("JUSTIFICAR NO VISITA")){
                    causalJustificacion = justificacion;
                }*/

                /*
                if(justificacion.equalsIgnoreCase("JUSTIFICAR NO VISITA")){
                    tipo_relevo = "Externo";
                }else{
                    tipo_relevo = "Interno";
                }*/


                /*
                if(justificacionNoPDV.equalsIgnoreCase("OTRAS")){
                    causalJustificacion = "JUSTIFICAR NO VISITA";
                }else {
                    causalJustificacion = "JUSTIFICAR NO VISITA";
                }*/


/*
                if (imageView != null && imageView.getDrawable() != null){

                    bitmapfinal = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
                    image = getStringImage(bitmapfinal);



                }else{
                    image = "NO FOTO";
                }
                */

                Base_pharma_value bpv = handler.getInfoPDV(codigo);

                ContentValues values = new ContentValues();

                values.put(ContractInsertGps.Columnas.IDPDV, codigo);
                values.put(ContractInsertGps.Columnas.USUARIO, usuarioCursor);
                values.put(ContractInsertGps.Columnas.TIPO, registro);
                values.put(ContractInsertGps.Columnas.VERSION,context.getResources().getString(R.string.version));
                values.put(ContractInsertGps.Columnas.LATITUDE, latitud + "");
                values.put(ContractInsertGps.Columnas.LONGITUDE, longitud + "");
                values.put(ContractInsertGps.Columnas.FOTO, image);
                values.put(ContractInsertGps.Columnas.FECHA, fechaser);
                values.put(ContractInsertGps.Columnas.HORA, horaser);
                values.put(ContractInsertGps.Columnas.CAUSAL, causalJustificacion + ": " + descJustificacion);
                values.put(ContractInsertGps.Columnas.DISTANCIA, distance);
                values.put(ContractInsertGps.Columnas.TIPO_RELEVO, tipo_relevo);
                values.put(ContractInsertGps.Columnas.POS_NAME, bpv.getPos_name());
                values.put(Constantes.ID_REMOTA_RUTA, idRuta);
                values.put(Constantes.PENDIENTE_INSERCION, 1);
                context.getContentResolver().insert(ContractInsertGps.CONTENT_URI, values);

                Toast.makeText(context, "Registrada su "+registro+" : " + horaser + "; Coordenadas: " + latitud + ", " + longitud, Toast.LENGTH_SHORT).show();

                if (VerificarNet.hayConexion(context)) {
                    SyncAdapter.sincronizarAhora(context, true, Constantes.insertGps, null);
                    Toast.makeText(context, Mensajes.ON_SYNC_SERVER, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, Mensajes.ON_SYNC_DEVICE, Toast.LENGTH_SHORT).show();
                }







                return true;
            } catch (Exception e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
                return false;
            }
        }


        private void sharedPreferencesAsistenciaServices(){

        }


        private void faltaSalida(boolean salida, String fecha_falta_salida) {
            falta_salida = salida;
            SharedPreferences sharedPref = context.getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean(Constantes.FALTA_SALIDA, salida);
            editor.putString(Constantes.FECHA_FALTA_SALIDA, fecha_falta_salida);
            editor.commit();
        }

        public String getStringImage(Bitmap bmp){
            String encodedImage;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            //Comprime la Imagen tipo, calidad y outputstream
            bmp.compress(Bitmap.CompressFormat.JPEG, 30, baos);
            byte[] imageBytes = baos.toByteArray();
            encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            return encodedImage;

        }






        private void obtenerCoordenadas() {


//            txtlatitud.setHint("Buscando..");
//            txtlongitud.setHint("Buscando..");

            try {
                locationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                        locationListener);
            } catch (SecurityException ex) {
                Log.i(TAG, "fail to request location update, ignore", ex);
            } catch (IllegalArgumentException ex) {
                Log.d(TAG, "network provider does not exist, " + ex.getMessage());
            }
            try {
                locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                        locationListener);
            } catch (SecurityException ex) {
                Log.i(TAG, "fail to request location update, ignore", ex);
            } catch (IllegalArgumentException ex) {
                Log.d(TAG, "gps provider does not exist " + ex.getMessage());
            }
        }

        private void SaveData(String idpdv, String codigo, String user, String cliente, String format,
                              String tipo, String subcanal, String vendedor, String celular, String canal,
                              String id_ruta, String supervisor, String zona) {
            try {
                obtenerFecha();
                String pharma_id = idpdv;

                SharedPreferences sharedPref = context.getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString(Constantes.USER, user);
                    editor.putString(Constantes.SUPERVISOR, supervisor);
                    editor.putString(Constantes.PHARMA_ID, pharma_id != null ? pharma_id : "");
                    editor.putString(Constantes.CODIGO, codigo);
                    editor.putString(Constantes.PDV, cliente);
                    editor.putString(Constantes.CANAL, canal);
                    editor.putString(Constantes.SUBCANAL, subcanal);
                    editor.putString(Constantes.FECHA, fecha);
                    editor.putString(Constantes.HORA, hora);
                    editor.putString(Constantes.FORMAT, format);
                    editor.putString(Constantes.IDPDV, idpdv != null ? idpdv : "");
                    editor.putString(Constantes.TIPO, tipo);
                    editor.putString(Constantes.VENDEDOR, vendedor);
                    editor.putString(Constantes.CELULAR, celular);
                    editor.putString(Constantes.ID_RUTA, id_ruta);
                    editor.putString(Constantes.DISTANCIA, String.valueOf(distance));
                    editor.putBoolean(Constantes.FALTA_SALIDA, false);
                    editor.putString(Constantes.ZONE, zona);
                    editor.commit();

                if (pharma_id == null || pharma_id.equals("")) {
                    Toast.makeText(context, Mensajes.DATA_NULL, Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }


        private void cargarImagen() {

            final CharSequence[] opciones={"Tomar Foto","Cancelar"};
//        final CharSequence[] opciones={"Tomar Foto","Cancelar"};
            final androidx.appcompat.app.AlertDialog.Builder alertOpciones=new androidx.appcompat.app.AlertDialog.Builder(context);
            alertOpciones.setTitle("Seleccione una Opción");
            alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (opciones[i].equals("Tomar Foto")){
                        Intent n = new Intent(context, CameraActivity.class);
                        n.putExtra("activity", "justificar");
                        context.startActivity(n);

                    } else {
                        if (opciones[i].equals("Cargar Imagen")) {
                        /* Intent intent=new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/");
                        startActivityForResult(intent.createChooser(intent,"Seleccione la Aplicación"),COD_SELECCIONA);*/
                            //  openGallery();
                          //  Intent n = new Intent(context, GaleriaActivity.class);
                         //   context.startActivity(n);
                        } else {
                            dialogInterface.dismiss();
                        }
                    }
                }
            });
            alertOpciones.show();
        }


        public void insertData(String id_pdv, String codigo, String tipo, String cliente, String ubicacion, String correo, String lati, String longi) {
            if (id_pdv != null) {
                Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-5"));
                Date currentLocalTime = cal.getTime();
                DateFormat date = new SimpleDateFormat("dd/MM/yyy");
                date.setTimeZone(TimeZone.getTimeZone("GMT-5"));
                String fechaser = date.format(currentLocalTime);

                DateFormat hour = new SimpleDateFormat("HH:mm:ss");
                hour.setTimeZone(TimeZone.getTimeZone("GMT-5"));
                String horaser = hour.format(currentLocalTime);

    /*            MyService x = new MyService();
                MyService.MyLocationListener y = x.new MyLocationListener();

                String latitude = y.getLatitud();
                String longitude = y.getLongitud();

                //Almacenar Datos
                ContentValues values = new ContentValues();
                values.put(ContractInsertInicial.Columnas.IDPDV, id_pdv);//SharedPreference
                values.put(ContractInsertInicial.Columnas.CODIGO, codigo);//SharedPreference
                values.put(ContractInsertInicial.Columnas.TIPO, tipo);//Spinner
                values.put(ContractInsertInicial.Columnas.DEALER, cliente);//List
                values.put(ContractInsertInicial.Columnas.UBICACION, ubicacion);//Spinner
                values.put(ContractInsertInicial.Columnas.CORREO, correo);//Spinner
                values.put(ContractInsertInicial.Columnas.LATITUD, latitude);//Spinner
                values.put(ContractInsertInicial.Columnas.LONGITUD, longitude);
                values.put(ContractInsertInicial.Columnas.FECHA, fechaser);//Extra
                values.put(ContractInsertInicial.Columnas.HORA, horaser);//Extra
                values.put(Constantes.PENDIENTE_INSERCION, 1);

                context.getContentResolver().insert(ContractInsertInicial.CONTENT_URI, values);
*/
//                if (VerificarNet.hayConexion(context)) {
//                    SyncAdapter.sincronizarAhora(context, true, Constantes.insertinicial, null);
//                    Toast.makeText(context, Mensajes.ON_SYNC_SERVER, Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(context, Mensajes.ON_SYNC_DEVICE, Toast.LENGTH_SHORT).show();
//                }
            } else {
                Toast.makeText(context, "No se esta obteniendo el id", Toast.LENGTH_SHORT).show();
            }
        }

        public void LoadData() {
            SharedPreferences sharedPreferences = context.getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
            usuarioCursor = sharedPreferences.getString(Constantes.USER,Constantes.NODATA);
            modulo_actual = sharedPreferences.getString(Constantes.MODULO_ACTUAL, Constantes.NODATA).toUpperCase();
            almuerzo_en_curso = sharedPreferences.getBoolean(Constantes.ALMUERZO_EN_CURSO,false);

            codigo_pdv_pendiente = sharedPreferences.getString(Constantes.CODIGO, Constantes.NODATA);
            String fecha_pendiente = sharedPreferences.getString(Constantes.FECHA, Constantes.NODATA);
            falta_salida = sharedPreferences.getBoolean(Constantes.FALTA_SALIDA, false);
            if (falta_salida && !codigo_pdv_pendiente.equals(Constantes.NODATA)) {
                falta_salida = handler.visitaEnCurso(codigo_pdv_pendiente, usuarioCursor, fecha_pendiente)
                        && handler.faltaJustificacionSalida(codigo_pdv_pendiente, usuarioCursor, fecha_pendiente);
            }

            //idRuta =  sharedPreferences.getString(Constantes.ID_RUTA,Constantes.NODATA);
            //bateria = sharedPreferences.getString(Constantes.BATERIA, Constantes.NO_DATA);
        }

    }

    public class FooterHolder extends BaseViewHolder {

        ProgressBar mProgressBar;

        FooterHolder(View itemView) {
            super(itemView);
//            ButterKnife.bind(this, itemView);
            mProgressBar = itemView.findViewById(R.id.progressBar);
        }

        @Override
        protected void clear() {

        }

    }

}
