package com.luckyecuador.app.PintucoAPP;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;
import com.luckyecuador.app.PintucoAPP.Conexion.Mensajes;
import com.luckyecuador.app.PintucoAPP.Conexion.VerificarNet;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertPreguntas;
import com.luckyecuador.app.PintucoAPP.Sync.SyncAdapter;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class ResultActivity extends AppCompatActivity {

    TextView t;
    TextView encabezado;
    private String id_pdv,user,codigo_pdv, punto_venta,fecha,hora, format, total_erroneas, porcentaje;
    ArrayList<String> seleccionados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        LoadData();
        //get rating bar object
        RatingBar bar=(RatingBar)findViewById(R.id.ratingBar1);

        bar.setNumStars(5);
        bar.setStepSize(0.5f);
        //get text view
        t = (TextView)findViewById(R.id.textResult);
        encabezado = (TextView)findViewById(R.id.textEncabezado);
        //get score
        Bundle b = getIntent().getExtras();

        DecimalFormat df = new DecimalFormat("#.00");

        int score = b.getInt("score");
        int total_preguntas = b.getInt("total_preguntas");
        int incorrectas = total_preguntas-score;
        double calificacion = (score*100)/total_preguntas;
        seleccionados = b.getStringArrayList("seleccionados");

        //display score
        bar.setRating(score);
        total_erroneas = "Total Respuestas Erróneas: " + incorrectas + "/" + total_preguntas;
        porcentaje = "Porcentaje Calificación: " + calificacion + "%";

        insertData(score+"",incorrectas+"",df.format(calificacion)+"",puntuaciones(total_preguntas,score), seleccionados);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_result, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, QuizActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void LoadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        id_pdv = sharedPreferences.getString(Constantes.IDPDV,Constantes.NODATA);
        user=sharedPreferences.getString(Constantes.USER,Constantes.NODATA);
        user = sharedPreferences.getString(Constantes.USER, Constantes.NODATA);
        id_pdv = sharedPreferences.getString(Constantes.PHARMA_ID, Constantes.NODATA);
        codigo_pdv = sharedPreferences.getString(Constantes.CODIGO, Constantes.NODATA);
        punto_venta = sharedPreferences.getString(Constantes.PDV, Constantes.NODATA);
        fecha = sharedPreferences.getString(Constantes.FECHA, Constantes.NODATA);
        hora = sharedPreferences.getString(Constantes.HORA, Constantes.NODATA);
        format = sharedPreferences.getString(Constantes.FORMAT, Constantes.NODATA);
    }

    public void insertData(String correctas, String incorrectas, String calificacion, String observacion, ArrayList seleccionados) {
        try{
            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-5"));
            Date currentLocalTime = cal.getTime();
            DateFormat date = new SimpleDateFormat("dd/MM/yyy");
            date.setTimeZone(TimeZone.getTimeZone("GMT-5"));
            String fechaser = date.format(currentLocalTime);

            DateFormat hour = new SimpleDateFormat("HH:mm:ss");
            hour.setTimeZone(TimeZone.getTimeZone("GMT-5"));
            String horaser = hour.format(currentLocalTime);

            ContentValues values = new ContentValues();

            values.put(ContractInsertPreguntas.Columnas.USUARIO, user);
            values.put(ContractInsertPreguntas.Columnas.CORRECTAS, correctas);
            values.put(ContractInsertPreguntas.Columnas.INCORRECTAS, incorrectas);
            values.put(ContractInsertPreguntas.Columnas.CALIFICACION, calificacion);
            values.put(ContractInsertPreguntas.Columnas.OBSERVACION, observacion);

            for(int i = 0; i < seleccionados.size(); i++) {
                Log.i("SELECCIONADOS", seleccionados.get(i).toString() + " (INDICE: " + i + ")");
                Log.i("CUMPLIMIENTO", seleccionados.size()+"");
                if (seleccionados.size()==0) {
                    seleccionados.add(0,"N/A");
                    seleccionados.add(1,"N/A");
                    seleccionados.add(2,"N/A");
                    seleccionados.add(3,"N/A");
                    seleccionados.add(4,"N/A");
                    seleccionados.add(5,"N/A");
                    seleccionados.add(6,"N/A");
                    seleccionados.add(7,"N/A");
                    seleccionados.add(8,"N/A");
                    seleccionados.add(9,"N/A");
                    seleccionados.add(10,"N/A");
                    seleccionados.add(11,"N/A");
                    seleccionados.add(12,"N/A");
                    seleccionados.add(13,"N/A");
                    seleccionados.add(14,"N/A");
                    seleccionados.add(15,"N/A");
                }
                if (seleccionados.size()==1) {
                    seleccionados.add(1,"N/A");
                    seleccionados.add(2,"N/A");
                    seleccionados.add(3,"N/A");
                    seleccionados.add(4,"N/A");
                    seleccionados.add(5,"N/A");
                    seleccionados.add(6,"N/A");
                    seleccionados.add(7,"N/A");
                    seleccionados.add(8,"N/A");
                    seleccionados.add(9,"N/A");
                    seleccionados.add(10,"N/A");
                    seleccionados.add(11,"N/A");
                    seleccionados.add(12,"N/A");
                    seleccionados.add(13,"N/A");
                    seleccionados.add(14,"N/A");
                    seleccionados.add(15,"N/A");
                }
                if (seleccionados.size()==2) {
                    seleccionados.add(2,"N/A");
                    seleccionados.add(3,"N/A");
                    seleccionados.add(4,"N/A");
                    seleccionados.add(5,"N/A");
                    seleccionados.add(6,"N/A");
                    seleccionados.add(7,"N/A");
                    seleccionados.add(8,"N/A");
                    seleccionados.add(9,"N/A");
                    seleccionados.add(10,"N/A");
                    seleccionados.add(11,"N/A");
                    seleccionados.add(12,"N/A");
                    seleccionados.add(13,"N/A");
                    seleccionados.add(14,"N/A");
                    seleccionados.add(15,"N/A");
                }
                if (seleccionados.size()==3) {
                    seleccionados.add(3,"N/A");
                    seleccionados.add(4,"N/A");
                    seleccionados.add(5,"N/A");
                    seleccionados.add(6,"N/A");
                    seleccionados.add(7,"N/A");
                    seleccionados.add(8,"N/A");
                    seleccionados.add(9,"N/A");
                    seleccionados.add(10,"N/A");
                    seleccionados.add(11,"N/A");
                    seleccionados.add(12,"N/A");
                    seleccionados.add(13,"N/A");
                    seleccionados.add(14,"N/A");
                    seleccionados.add(15,"N/A");
                }
                if (seleccionados.size()==4) {
                    seleccionados.add(4,"N/A");
                    seleccionados.add(5,"N/A");
                    seleccionados.add(6,"N/A");
                    seleccionados.add(7,"N/A");
                    seleccionados.add(8,"N/A");
                    seleccionados.add(9,"N/A");
                    seleccionados.add(10,"N/A");
                    seleccionados.add(11,"N/A");
                    seleccionados.add(12,"N/A");
                    seleccionados.add(13,"N/A");
                    seleccionados.add(14,"N/A");
                    seleccionados.add(15,"N/A");
                }
                if (seleccionados.size()==5) {
                    seleccionados.add(5,"N/A");
                    seleccionados.add(6,"N/A");
                    seleccionados.add(7,"N/A");
                    seleccionados.add(8,"N/A");
                    seleccionados.add(9,"N/A");
                    seleccionados.add(10,"N/A");
                    seleccionados.add(11,"N/A");
                    seleccionados.add(12,"N/A");
                    seleccionados.add(13,"N/A");
                    seleccionados.add(14,"N/A");
                    seleccionados.add(15,"N/A");
                }
                if (seleccionados.size()==6) {
                    seleccionados.add(6,"N/A");
                    seleccionados.add(7,"N/A");
                    seleccionados.add(8,"N/A");
                    seleccionados.add(9,"N/A");
                    seleccionados.add(10,"N/A");
                    seleccionados.add(11,"N/A");
                    seleccionados.add(12,"N/A");
                    seleccionados.add(13,"N/A");
                    seleccionados.add(14,"N/A");
                    seleccionados.add(15,"N/A");
                }
                if (seleccionados.size()==7) {
                    seleccionados.add(7,"N/A");
                    seleccionados.add(8,"N/A");
                    seleccionados.add(9,"N/A");
                    seleccionados.add(10,"N/A");
                    seleccionados.add(11,"N/A");
                    seleccionados.add(12,"N/A");
                    seleccionados.add(13,"N/A");
                    seleccionados.add(14,"N/A");
                    seleccionados.add(15,"N/A");
                }
                if (seleccionados.size()==8) {
                    seleccionados.add(8,"N/A");
                    seleccionados.add(9,"N/A");
                    seleccionados.add(10,"N/A");
                    seleccionados.add(11,"N/A");
                    seleccionados.add(12,"N/A");
                    seleccionados.add(13,"N/A");
                    seleccionados.add(14,"N/A");
                    seleccionados.add(15,"N/A");
                }
                if (seleccionados.size()==9) {
                    seleccionados.add(9,"N/A");
                    seleccionados.add(10,"N/A");
                    seleccionados.add(11,"N/A");
                    seleccionados.add(12,"N/A");
                    seleccionados.add(13,"N/A");
                    seleccionados.add(14,"N/A");
                    seleccionados.add(15,"N/A");
                }
                if (seleccionados.size()==10) {
                    seleccionados.add(10,"N/A");
                    seleccionados.add(11,"N/A");
                    seleccionados.add(12,"N/A");
                    seleccionados.add(13,"N/A");
                    seleccionados.add(14,"N/A");
                    seleccionados.add(15,"N/A");
                }
                if (seleccionados.size()==11) {
                    seleccionados.add(11,"N/A");
                    seleccionados.add(12,"N/A");
                    seleccionados.add(13,"N/A");
                    seleccionados.add(14,"N/A");
                    seleccionados.add(15,"N/A");
                }
                if (seleccionados.size()==12) {
                    seleccionados.add(12,"N/A");
                    seleccionados.add(13,"N/A");
                    seleccionados.add(14,"N/A");
                    seleccionados.add(15,"N/A");
                }
                if (seleccionados.size()==13) {
                    seleccionados.add(13,"N/A");
                    seleccionados.add(14,"N/A");
                    seleccionados.add(15,"N/A");
                }
                if (seleccionados.size()==14) {
                    seleccionados.add(14,"N/A");
                    seleccionados.add(15,"N/A");
                }
                if (seleccionados.size()==15) {
                    seleccionados.add(15,"N/A");
                }
                values.put(ContractInsertPreguntas.Columnas.P1, seleccionados.get(0).toString());
                values.put(ContractInsertPreguntas.Columnas.P2, seleccionados.get(1).toString());
                values.put(ContractInsertPreguntas.Columnas.P3, seleccionados.get(2).toString());
                values.put(ContractInsertPreguntas.Columnas.P4, seleccionados.get(3).toString());
                values.put(ContractInsertPreguntas.Columnas.P5, seleccionados.get(4).toString());
                values.put(ContractInsertPreguntas.Columnas.P6, seleccionados.get(5).toString());
                values.put(ContractInsertPreguntas.Columnas.P7, seleccionados.get(6).toString());
                values.put(ContractInsertPreguntas.Columnas.P8, seleccionados.get(7).toString());
                values.put(ContractInsertPreguntas.Columnas.P9, seleccionados.get(8).toString());
                values.put(ContractInsertPreguntas.Columnas.P10, seleccionados.get(9).toString());
                values.put(ContractInsertPreguntas.Columnas.P11, seleccionados.get(10).toString());
                values.put(ContractInsertPreguntas.Columnas.P12, seleccionados.get(11).toString());
                values.put(ContractInsertPreguntas.Columnas.P13, seleccionados.get(12).toString());
                values.put(ContractInsertPreguntas.Columnas.P14, seleccionados.get(13).toString());
                values.put(ContractInsertPreguntas.Columnas.P15, seleccionados.get(14).toString());
                /*values.put(ContractInsertPreguntas.Columnas.P1, "");
                values.put(ContractInsertPreguntas.Columnas.P2, "");
                values.put(ContractInsertPreguntas.Columnas.P3, "");
                values.put(ContractInsertPreguntas.Columnas.P4, "");
                values.put(ContractInsertPreguntas.Columnas.P5, "");
                values.put(ContractInsertPreguntas.Columnas.P6, "");
                values.put(ContractInsertPreguntas.Columnas.P7, "");
                values.put(ContractInsertPreguntas.Columnas.P8, "");
                values.put(ContractInsertPreguntas.Columnas.P9, "");
                values.put(ContractInsertPreguntas.Columnas.P10, "");
                values.put(ContractInsertPreguntas.Columnas.P11, "");
                values.put(ContractInsertPreguntas.Columnas.P12, "");
                values.put(ContractInsertPreguntas.Columnas.P13, "");
                values.put(ContractInsertPreguntas.Columnas.P14, "");
                values.put(ContractInsertPreguntas.Columnas.P15, "");*/
            }

            values.put(ContractInsertPreguntas.Columnas.FECHA, fechaser);
            values.put(ContractInsertPreguntas.Columnas.HORA, horaser);
            values.put(Constantes.PENDIENTE_INSERCION, 1);

            getContentResolver().insert(ContractInsertPreguntas.CONTENT_URI, values);

            if (VerificarNet.hayConexion(getApplicationContext())) {
                SyncAdapter.sincronizarAhora(this, true, Constantes.insertPreguntas, null);
                Toast.makeText(getApplicationContext(), Mensajes.ON_SYNC_SERVER, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), Mensajes.ON_SYNC_DEVICE, Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e) {
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public String puntuaciones(int total_preguntas, int score) {
        String puntuacion1 = "Espera la llamada de Dennisse Coronel";
        String puntuacion2 = "Ponte a ESTUDIAR YA!!";
        String puntuacion3 = "Debes estudiar más";
        String puntuacion4 = "Buen intento, tú puedes";
        String puntuacion5 = "Vamos que si se puede...";
        String puntuacion6 = "Felicidades, Dennisse invita la PIZZA";

        String mensaje = "";
        if (total_preguntas==15) {
            switch (score) {
                case 0:
                    mensaje = puntuacion1;
                    encabezado.setText(mensaje);
                    t.setText(total_erroneas + "\n\n" + porcentaje);
                    break;
                case 1:
                    mensaje = puntuacion1;
                    encabezado.setText(mensaje);
                    t.setText(total_erroneas + "\n\n" + porcentaje);
                    break;
                case 2:
                    mensaje = puntuacion1;
                    encabezado.setText(mensaje);
                    t.setText(total_erroneas + "\n\n" + porcentaje);
                    break;
                case 3:
                    mensaje = puntuacion2;
                    encabezado.setText(mensaje);
                    t.setText(total_erroneas + "\n\n" + porcentaje);
                    break;
                case 4:
                    mensaje = puntuacion2;
                    encabezado.setText(mensaje);
                    t.setText(total_erroneas + "\n\n" + porcentaje);
                    break;
                case 5:
                    mensaje = puntuacion2;
                    encabezado.setText(mensaje);
                    t.setText(total_erroneas + "\n\n" + porcentaje);
                    break;
                case 6:
                    mensaje = puntuacion2;
                    encabezado.setText(mensaje);
                    t.setText(total_erroneas + "\n\n" + porcentaje);
                    break;
                case 7:
                    mensaje = puntuacion3;
                    encabezado.setText(mensaje);
                    t.setText(total_erroneas + "\n\n" + porcentaje);
                    break;
                case 8:
                    mensaje = puntuacion3;
                    encabezado.setText(mensaje);
                    t.setText(total_erroneas + "\n\n" + porcentaje);
                    break;
                case 9:
                    mensaje = puntuacion3;
                    encabezado.setText(mensaje);
                    t.setText(total_erroneas + "\n\n" + porcentaje);
                    break;
                case 10:
                    mensaje = puntuacion3;
                    encabezado.setText(mensaje);
                    t.setText(total_erroneas + "\n\n" + porcentaje);
                    break;
                case 11:
                    mensaje = puntuacion4;
                    encabezado.setText(mensaje);
                    t.setText(total_erroneas + "\n\n" + porcentaje);
                    break;
                case 12:
                    mensaje = puntuacion4;
                    encabezado.setText(mensaje);
                    t.setText(total_erroneas + "\n\n" + porcentaje);
                    break;
                case 13:
                    mensaje = puntuacion5;
                    encabezado.setText(mensaje);
                    t.setText(total_erroneas + "\n\n" + porcentaje);
                    break;
                case 14:
                    mensaje = puntuacion5;
                    encabezado.setText(mensaje);
                    t.setText(total_erroneas + "\n\n" + porcentaje);
                    break;
                case 15:
                    mensaje = puntuacion6;
                    encabezado.setText(mensaje);
                    t.setText(total_erroneas + "\n\n" + porcentaje);
                    break;
                default:
                    Log.i("FUERA DE RANGO","FUERA DE RANGO");
                    break;
            }
        }else if (total_preguntas==14) {
            switch (score) {
                case 0:
                    mensaje = puntuacion1;
                    encabezado.setText(mensaje);
                    t.setText(total_erroneas + "\n\n" + porcentaje);
                    break;
                case 1:
                    mensaje = puntuacion1;
                    encabezado.setText(mensaje);
                    t.setText(total_erroneas + "\n\n" + porcentaje);
                    break;
                case 2:
                    mensaje = puntuacion1;
                    encabezado.setText(mensaje);
                    t.setText(total_erroneas + "\n\n" + porcentaje);
                    break;
                case 3:
                    mensaje = puntuacion2;
                    encabezado.setText(mensaje);
                    t.setText(total_erroneas + "\n\n" + porcentaje);
                    break;
                case 4:
                    mensaje = puntuacion2;
                    encabezado.setText(mensaje);
                    t.setText(total_erroneas + "\n\n" + porcentaje);
                    break;
                case 5:
                    mensaje = puntuacion2;
                    encabezado.setText(mensaje);
                    t.setText(total_erroneas + "\n\n" + porcentaje);
                    break;
                case 6:
                    mensaje = puntuacion2;
                    encabezado.setText(mensaje);
                    t.setText(total_erroneas + "\n\n" + porcentaje);
                    break;
                case 7:
                    mensaje = puntuacion3;
                    encabezado.setText(mensaje);
                    t.setText(total_erroneas + "\n\n" + porcentaje);
                    break;
                case 8:
                    mensaje = puntuacion3;
                    encabezado.setText(mensaje);
                    t.setText(total_erroneas + "\n\n" + porcentaje);
                    break;
                case 9:
                    mensaje = puntuacion3;
                    encabezado.setText(mensaje);
                    t.setText(total_erroneas + "\n\n" + porcentaje);
                    break;
                case 10:
                    mensaje = puntuacion4;
                    encabezado.setText(mensaje);
                    t.setText(total_erroneas + "\n\n" + porcentaje);
                    break;
                case 11:
                    mensaje = puntuacion4;
                    encabezado.setText(mensaje);
                    t.setText(total_erroneas + "\n\n" + porcentaje);
                    break;
                case 12:
                    mensaje = puntuacion5;
                    encabezado.setText(mensaje);
                    t.setText(total_erroneas + "\n\n" + porcentaje);
                    break;
                case 13:
                    mensaje = puntuacion5;
                    encabezado.setText(mensaje);
                    t.setText(total_erroneas + "\n\n" + porcentaje);
                    break;
                case 14:
                    mensaje = puntuacion6;
                    encabezado.setText(mensaje);
                    t.setText(total_erroneas + "\n\n" + porcentaje);
                    break;
                default:
                    Log.i("FUERA DE RANGO","FUERA DE RANGO");
                    break;
            }
        }else if (total_preguntas==13) {
            switch (score) {
                case 0:
                    mensaje = puntuacion1;
                    encabezado.setText(mensaje);
                    t.setText(total_erroneas + "\n\n" + porcentaje);
                    break;
                case 1:
                    mensaje = puntuacion1;
                    encabezado.setText(mensaje);
                    t.setText(total_erroneas + "\n\n" + porcentaje);
                    break;
                case 2:
                    mensaje = puntuacion1;
                    encabezado.setText(mensaje);
                    t.setText(total_erroneas + "\n\n" + porcentaje);
                    break;
                case 3:
                    mensaje = puntuacion2;
                    encabezado.setText(mensaje);
                    t.setText(total_erroneas + "\n\n" + porcentaje);
                    break;
                case 4:
                    mensaje = puntuacion2;
                    encabezado.setText(mensaje);
                    t.setText(total_erroneas + "\n\n" + porcentaje);
                    break;
                case 5:
                    mensaje = puntuacion2;
                    encabezado.setText(mensaje);
                    t.setText(total_erroneas + "\n\n" + porcentaje);
                    break;
                case 6:
                    mensaje = puntuacion2;
                    encabezado.setText(mensaje);
                    t.setText(total_erroneas + "\n\n" + porcentaje);
                    break;
                case 7:
                    mensaje = puntuacion3;
                    encabezado.setText(mensaje);
                    t.setText(total_erroneas + "\n\n" + porcentaje);
                    break;
                case 8:
                    mensaje = puntuacion3;
                    encabezado.setText(mensaje);
                    t.setText(total_erroneas + "\n\n" + porcentaje);
                    break;
                case 9:
                    mensaje = puntuacion3;
                    encabezado.setText(mensaje);
                    t.setText(total_erroneas + "\n\n" + porcentaje);
                    break;
                case 10:
                    mensaje = puntuacion4;
                    encabezado.setText(mensaje);
                    t.setText(total_erroneas + "\n\n" + porcentaje);
                    break;
                case 11:
                    mensaje = puntuacion4;
                    encabezado.setText(mensaje);
                    t.setText(total_erroneas + "\n\n" + porcentaje);
                    break;
                case 12:
                    mensaje = puntuacion5;
                    encabezado.setText(mensaje);
                    t.setText(total_erroneas + "\n\n" + porcentaje);
                    break;
                case 13:
                    mensaje = puntuacion6;
                    encabezado.setText(mensaje);
                    t.setText(total_erroneas + "\n\n" + porcentaje);
                    break;
                default:
                    Log.i("FUERA DE RANGO","FUERA DE RANGO");
                    break;
            }
        }else if (total_preguntas==12) {
            switch (score) {
                case 0:
                    mensaje = puntuacion1;
                    encabezado.setText(mensaje);
                    t.setText(total_erroneas + "\n\n" + porcentaje);
                    break;
                case 1:
                    mensaje = puntuacion1;
                    encabezado.setText(mensaje);
                    t.setText(total_erroneas + "\n\n" + porcentaje);
                    break;
                case 2:
                    mensaje = puntuacion1;
                    encabezado.setText(mensaje);
                    t.setText(total_erroneas + "\n\n" + porcentaje);
                    break;
                case 3:
                    mensaje = puntuacion2;
                    encabezado.setText(mensaje);
                    t.setText(total_erroneas + "\n\n" + porcentaje);
                    break;
                case 4:
                    mensaje = puntuacion2;
                    encabezado.setText(mensaje);
                    t.setText(total_erroneas + "\n\n" + porcentaje);
                    break;
                case 5:
                    mensaje = puntuacion2;
                    encabezado.setText(mensaje);
                    t.setText(total_erroneas + "\n\n" + porcentaje);
                    break;
                case 6:
                    mensaje = puntuacion2;
                    encabezado.setText(mensaje);
                    t.setText(total_erroneas + "\n\n" + porcentaje);
                    break;
                case 7:
                    mensaje = puntuacion3;
                    encabezado.setText(mensaje);
                    t.setText(total_erroneas + "\n\n" + porcentaje);
                    break;
                case 8:
                    mensaje = puntuacion3;
                    encabezado.setText(mensaje);
                    t.setText(total_erroneas + "\n\n" + porcentaje);
                    break;
                case 9:
                    mensaje = puntuacion3;
                    encabezado.setText(mensaje);
                    t.setText(total_erroneas + "\n\n" + porcentaje);
                    break;
                case 10:
                    mensaje = puntuacion4;
                    encabezado.setText(mensaje);
                    t.setText(total_erroneas + "\n\n" + porcentaje);
                    break;
                case 11:
                    mensaje = puntuacion5;
                    encabezado.setText(mensaje);
                    t.setText(total_erroneas + "\n\n" + porcentaje);
                    break;
                case 12:
                    mensaje = puntuacion6;
                    encabezado.setText(mensaje);
                    t.setText(total_erroneas + "\n\n" + porcentaje);
                    break;
                default:
                    Log.i("FUERA DE RANGO","FUERA DE RANGO");
                    break;
            }
        }else if (total_preguntas==11) {
            switch (score) {
                case 0:
                    mensaje = puntuacion1;
                    encabezado.setText(mensaje);
                    t.setText(total_erroneas + "\n\n" + porcentaje);
                    break;
                case 1:
                    mensaje = puntuacion1;
                    encabezado.setText(mensaje);
                    t.setText(total_erroneas + "\n\n" + porcentaje);
                    break;
                case 2:
                    mensaje = puntuacion1;
                    encabezado.setText(mensaje);
                    t.setText(total_erroneas + "\n\n" + porcentaje);
                    break;
                case 3:
                    mensaje = puntuacion2;
                    encabezado.setText(mensaje);
                    t.setText(total_erroneas + "\n\n" + porcentaje);
                    break;
                case 4:
                    mensaje = puntuacion2;
                    encabezado.setText(mensaje);
                    t.setText(total_erroneas + "\n\n" + porcentaje);
                    break;
                case 5:
                    mensaje = puntuacion2;
                    encabezado.setText(mensaje);
                    t.setText(total_erroneas + "\n\n" + porcentaje);
                    break;
                case 6:
                    mensaje = puntuacion2;
                    encabezado.setText(mensaje);
                    t.setText(total_erroneas + "\n\n" + porcentaje);
                    break;
                case 7:
                    mensaje = puntuacion3;
                    encabezado.setText(mensaje);
                    t.setText(total_erroneas + "\n\n" + porcentaje);
                    break;
                case 8:
                    mensaje = puntuacion3;
                    encabezado.setText(mensaje);
                    t.setText(total_erroneas + "\n\n" + porcentaje);
                    break;
                case 9:
                    mensaje = puntuacion4;
                    encabezado.setText(mensaje);
                    t.setText(total_erroneas + "\n\n" + porcentaje);
                    break;
                case 10:
                    mensaje = puntuacion5;
                    encabezado.setText(mensaje);
                    t.setText(total_erroneas + "\n\n" + porcentaje);
                    break;
                case 11:
                    mensaje = puntuacion6;
                    encabezado.setText(mensaje);
                    t.setText(total_erroneas + "\n\n" + porcentaje);
                    break;
                default:
                    Log.i("FUERA DE RANGO","FUERA DE RANGO");
                    break;
            }
        }else if (total_preguntas==10) {
            switch (score) {
                case 0:
                    mensaje = puntuacion1;
                    encabezado.setText(mensaje);
                    t.setText(total_erroneas + "\n\n" + porcentaje);
                    break;
                case 1:
                    mensaje = puntuacion1;
                    encabezado.setText(mensaje);
                    t.setText(total_erroneas + "\n\n" + porcentaje);
                    break;
                case 2:
                    mensaje = puntuacion1;
                    encabezado.setText(mensaje);
                    t.setText(total_erroneas + "\n\n" + porcentaje);
                    break;
                case 3:
                    mensaje = puntuacion2;
                    encabezado.setText(mensaje);
                    t.setText(total_erroneas + "\n\n" + porcentaje);
                    break;
                case 4:
                    mensaje = puntuacion2;
                    encabezado.setText(mensaje);
                    t.setText(total_erroneas + "\n\n" + porcentaje);
                    break;
                case 5:
                    mensaje = puntuacion2;
                    encabezado.setText(mensaje);
                    t.setText(total_erroneas + "\n\n" + porcentaje);
                    break;
                case 6:
                    mensaje = puntuacion3;
                    encabezado.setText(mensaje);
                    t.setText(total_erroneas + "\n\n" + porcentaje);
                    break;
                case 7:
                    mensaje = puntuacion3;
                    encabezado.setText(mensaje);
                    t.setText(total_erroneas + "\n\n" + porcentaje);
                    break;
                case 8:
                    mensaje = puntuacion4;
                    encabezado.setText(mensaje);
                    t.setText(total_erroneas + "\n\n" + porcentaje);
                    break;
                case 9:
                    mensaje = puntuacion5;
                    encabezado.setText(mensaje);
                    t.setText(total_erroneas + "\n\n" + porcentaje);
                    break;
                case 10:
                    mensaje = puntuacion6;
                    encabezado.setText(mensaje);
                    t.setText(total_erroneas + "\n\n" + porcentaje);
                    break;
                default:
                    Log.i("FUERA DE RANGO","FUERA DE RANGO");
                    break;
            }
        }
        return mensaje;
    }
}