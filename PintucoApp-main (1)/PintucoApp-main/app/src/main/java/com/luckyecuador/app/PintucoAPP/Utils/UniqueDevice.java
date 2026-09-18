package com.luckyecuador.app.PintucoAPP.Utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.Settings;
import android.util.Log;

import com.luckyecuador.app.PintucoAPP.Contracts.ContractPharmaValue;
import com.luckyecuador.app.PintucoAPP.DataBase.DatabaseHelper;
import com.luckyecuador.app.PintucoAPP.DataBase.Provider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UniqueDevice {

    public String user = null;
    DatabaseHelper handler;

    @SuppressLint("HardwareIds")
    public static String getDeviceId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public void modalUniqueDevice(Activity activity, String user) {
        this.user = user;

        Log.i("USER_DEVICE", user);
        handler = new DatabaseHelper(activity.getApplicationContext(), Provider.DATABASE_NAME, null, 1);
        String device_id = getDeviceId(activity.getApplicationContext());
        Log.i("DEVICE_ID", device_id);
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setCancelable(false);
        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.setTitle("Sesión única");
        builder.setMessage("Este dispositivo no está atado a su usuario, por favor, contáctese con su supervisor");
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                activity.moveTaskToBack(true);
            }
        });
        AlertDialog ad = builder.create();

//        boolean isUserDevice = handler.esDispositivoDelUsuario(device_id);

//        if (!isUserDevice) {
//            if (VerificarNet.hayConexion(activity.getApplicationContext()) && (user!=null || !user.trim().isEmpty())) {
//                getUserDevice(activity, user);
//            }
//            ad.show();
//        } else {
//            if (ad.isShowing()) {
//                ad.dismiss();
//            }
//        }
    }

    public void getUserDevice(Activity activity, String user){
//        try{
//            HashMap<String, String> map = new HashMap<>();
//            map.put("user", user);
//
//            // Crear nuevo objeto Json basado en el mapa
//            JSONObject jobject = new JSONObject(map);
//
//            //GET METHOD
//            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constantes.GET_USER_DEVICE, jobject, new Response.Listener<JSONObject>() {
//                @Override
//                public void onResponse(JSONObject response) {
//                    procesarRespuestaGetUserDevice(response, activity);
//                }
//            }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    if (error instanceof TimeoutError) {
//                        Log.i("ERROR JSON USER DEVICE", Mensajes.TIME_OUT);
//                    } else if (error instanceof NoConnectionError) {
//                        Log.i("ERROR JSON USER DEVICE", Mensajes.NO_RED);
//                    }else if (error instanceof AuthFailureError) {
//                        //TODO
//                    } else if (error instanceof ServerError) {
//                        Log.i("ERROR JSON USER DEVICE", Mensajes.SEVER_ERROR);
//                    } else if (error instanceof NetworkError) {
//                        Log.i("ERROR JSON USER DEVICE", Mensajes.RED_ERROR);
//                    } else if (error instanceof ParseError) {
//                        //TODO
//                    }
//                }
//            });
//
//            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(7000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//            VolleySingleton.getInstance(activity.getApplicationContext()).addToRequestQueue(jsonObjectRequest);
//        }catch (Exception e){
//            Log.i("ERROR REQUEST USER DEVICE", e.getMessage());
//            e.printStackTrace();
//        }
    }

    public void procesarRespuestaGetUserDevice(JSONObject response, Activity activity){
        if (response!=null){
//            List<String> mensajeList = new ArrayList<>();
            try{
                String estado = response.getString("estado");
                switch (estado){
                    case "1": //EXITO
                        JSONArray mensaje = response.getJSONArray("datos");
                        for(int i = 0; i < mensaje.length(); i++){
                            JSONObject jb1 = mensaje.getJSONObject(i);
//                            mensajeList.add(jb1.getString("device_id"));
                            String id = jb1.getString("device_id");
                            Log.i("RESPONSE", id);
                            ContentValues values = new ContentValues();
                            values.put(ContractPharmaValue.Columnas.DEVICE_ID, id);
                            new TareaEditarDeviceId(activity.getApplicationContext().getContentResolver(), values, user).execute(ContractPharmaValue.CONTENT_URI);
                        }
                        break;
                    case "2": //FALLIDO
                        String mensaje2 = response.getString("mensaje");
                        Log.i("FALLO RESPUESTA JSON", mensaje2);
                        break;
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

    static class TareaEditarDeviceId extends AsyncTask<Uri, Void, Void> {
        private final ContentResolver resolver;
        private final ContentValues valores;
        private final String user;

        public TareaEditarDeviceId(ContentResolver resolver, ContentValues valores, String user) {
            this.resolver = resolver;
            this.valores = valores;
            this.user = user;
        }

        @Override
        protected Void doInBackground(Uri... args) {
            Uri uri = args[0];
            if (null != uri) {
                String selectQuery = ContractPharmaValue.Columnas.USER + "=?";
                String[] val = new String[]{user};
                resolver.update(uri, valores, selectQuery, val);
            }
            return null;
        }

    }

}
