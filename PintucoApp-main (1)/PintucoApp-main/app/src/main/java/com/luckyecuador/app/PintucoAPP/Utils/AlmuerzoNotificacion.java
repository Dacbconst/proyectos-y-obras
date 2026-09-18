package com.luckyecuador.app.PintucoAPP.Utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;
import com.luckyecuador.app.PintucoAPP.Conexion.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class AlmuerzoNotificacion {

    Context context;

    public interface LoginCallback {
        void onLoginResponse(boolean login);
    }

    public AlmuerzoNotificacion(Context context) {
        this.context = context;
    }

    public void enviarNotificacion(String usuario, String tipo,String supervisor) {
        try{
            HashMap<String, String> map = new HashMap<>();
            map.put("usuario", usuario);
            map.put("tipo", tipo);
            map.put("supervisor", supervisor);

            Log.i("supervisor",""+supervisor);

            JSONObject jobject = new JSONObject(map);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constantes.SEND_NOTIFICATION, jobject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    Log.i("response",""+response.toString());
                    procesarRespuestaNotificacion(response);
                    /*
                    if (response != null) {
                        try {
                            int estado = response.getInt("estado");
                            String mensaje = response.getString("mensaje");

                            if (estado == 1) {
                                Log.i("Notf", mensaje);
                            } else if(estado == 0) {
                                Log.i("Notf", mensaje);
                                // callback.onLoginResponse(false);
                            }

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    */
                }

                private void procesarRespuestaNotificacion(JSONObject response) {

                    if (response!=null){
                        try{
                            String mensaje = response.getString("mensaje");
                            Log.i("notf resp: ", mensaje);
                        }catch (JSONException e){

                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(context,"VACIO",Toast.LENGTH_SHORT).show();
                    }


                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Manejo de errores
                            Log.i("error noti",""+error.getMessage());
                        }
                    });

            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(60000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            VolleySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
        }catch (Exception e){
            Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }



}
