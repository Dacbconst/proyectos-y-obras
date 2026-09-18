package com.luckyecuador.app.PintucoAPP;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.luckyecuador.app.PintucoAPP.Adaptadores.QuizListAdapter;
import com.luckyecuador.app.PintucoAPP.Clase.Base_tests;
import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;
import com.luckyecuador.app.PintucoAPP.DataBase.DatabaseHelper;
import com.luckyecuador.app.PintucoAPP.DataBase.Provider;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class StartQuizActivity extends AppCompatActivity {

    // Recycler donde se listaran los test
    ImageView imagenTest;
    TextView nombreTest , f_inicio , f_limite;
    RecyclerView listaTest;
    ArrayList<Base_tests> listArrayQuiz;
    String user , canal , fecha , hora;
    SwipeRefreshLayout swipeRefreshLayout;


    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_quiz);

        listaTest = findViewById(R.id.listaTest);
        imagenTest = findViewById(R.id.imgTest);
        nombreTest = findViewById(R.id.tv_nombre_test);
        f_inicio = findViewById(R.id.tv_fecha_inicio);
        swipeRefreshLayout = findViewById(R.id.swipeRefresh);

        listaTest.setLayoutManager(new LinearLayoutManager(this));

        DatabaseHelper db = new DatabaseHelper(this, Provider.DATABASE_NAME, null, 1);

        LoadData();

      //  canal = db.getChannelSegmentPreguntas(user);

     //   Log.i("USER", user);
     //   Log.i("CANAL", canal);


      //  canal = "AUTOSERVICIO";


     //   listArrayQuiz = (ArrayList<Base_tests>) db.getAllQuizByCanal(canal,user);

        ArrayList<String> canales = (ArrayList<String>) db.getCanal();

        listArrayQuiz = (ArrayList<Base_tests>) db.getAllQuizByCanal(canales,user);

     //  Toast.makeText(this, ""+listArrayQuiz.size(), Toast.LENGTH_SHORT).show();






        ArrayList <Base_tests> listE = new ArrayList<>();
        listE.addAll(listArrayQuiz);


        obtenerFecha();



        for (int i = listArrayQuiz.size() - 1 ; i >= 0 ; i--) {



            // Obteneos los datos
            String f_inicio_test = listArrayQuiz.get(i).getF_inicio();
            String f_limite_test = listArrayQuiz.get(i).getF_limite();
            String h_inicio = listArrayQuiz.get(i).getH_inicio();
            String h_limite = listArrayQuiz.get(i).getH_limite();



            // Formateamos las fechas
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date strf_inicio = null;
            Date strf_limite = null;
            Date strf_actual = null;
            try {
                strf_actual = sdf.parse(fecha);
                strf_inicio = sdf.parse(f_inicio_test);
                strf_limite = sdf.parse(f_limite_test);
            } catch (ParseException e) {
                e.printStackTrace();
            }




                // Realiza una comparacion si la hora actual se encuentra en el rango
                // entre fecha inicio y fecha limite

                boolean r_fecha_inicio = strf_actual.compareTo(strf_inicio) >= 0;
                boolean r_fecha_limite =  strf_actual.compareTo(strf_limite) <= 0;
                boolean r_h_inicio = hora.compareTo(h_inicio) >= 0;
                boolean r_h_limite = hora.compareTo(h_limite) <= 0;








                if (r_fecha_inicio == true && r_fecha_limite == true) {

                    // Verificamos si la fecha actual es igual a la fecha de inicio test
                    if (fecha.equals(f_inicio_test)) {

                        // Si la hora actual es inferior a la hora inicio del test , se lo elimina.
                        if (r_h_inicio == false) {
                            listE.remove(i);

                         //   Toast.makeText(this,("Entra Aqui "+i),Toast.LENGTH_SHORT).show();
                        }
                    }

                    // Verificamos si la fecha actual es igual a la fecha de limite test
                    if (fecha.equals(f_limite_test)) {

                        // Si la hora actual es superior a la hora limite del test , se lo elimina.
                        if (r_h_limite == false) {
                            listE.remove(i);
                        }
                    }

                }
                else {

                    listE.remove(i);
                }



        }






        if(listE.size() ==0 ){
            Toast.makeText(this,"No se encuentran Tests Disponibles",Toast.LENGTH_LONG).show();
        }

        QuizListAdapter adapter = new QuizListAdapter(listE);

        adapter.setOnClickListenner(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String test_id = listE.get(listaTest.getChildAdapterPosition(v)).getId();
                String test = listE.get(listaTest.getChildAdapterPosition(v)).getTest();
                String f_inicio = listE.get(listaTest.getChildAdapterPosition(v)).getF_inicio();
                String h_inicio = listE.get(listaTest.getChildAdapterPosition(v)).getH_inicio();
                String f_limite = listE.get(listaTest.getChildAdapterPosition(v)).getF_limite();
                String h_limite = listE.get(listaTest.getChildAdapterPosition(v)).getH_limite();
                String descripcion  = listE.get(listaTest.getChildAdapterPosition(v)).getDescripcion();
                String  activo =  listE.get(listaTest.getChildAdapterPosition(v)).getActive();

                if(activo.equals("NO")){
                    Toast.makeText(getApplicationContext(),"El test se encuentra Inactivo",Toast.LENGTH_LONG).show();
                }else{
                    previousQuiz(test_id,test,f_inicio,h_inicio,f_limite,h_limite,descripcion,activo);
                }
            }
        });
        listaTest.setAdapter(adapter);


    }

    private void obtenerTests(ArrayList<String> canales) {

        DatabaseHelper db = new DatabaseHelper(this, Provider.DATABASE_NAME, null, 1);

       // ArrayList<Base_tests> quiz =;

        // quiz =  db.getAllQuizByCanal("AUTOSERVICIO",user);

      //  Toast.makeText(this, ""+quiz.size(), Toast.LENGTH_SHORT).show();

       // ArrayList <Base_tests> quiz2 = (ArrayList<Base_tests>) db.getAllQuizByCanal("MAYORISTA",user);

      // listArrayQuiz.addAll(quiz);




        for (int i =0 ; i < canales.size() ; i++){

            String canal = canales.get(i).toString();
          //   listArrayQuiz.addAll(db.getAllQuizByCanal(canal,user));
        }


    }

    /*
        private void llenarBase() {

        }
    */
    public void LoadData(){
        SharedPreferences sharedPreferences = getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        user = sharedPreferences.getString(Constantes.USER,Constantes.NODATA);
    }

    private void previousQuiz(String test_id,String quizName,String f_inicio,String h_inicio,String f_limite,String h_limite,String descripcion,String estado){
        Intent i = new Intent(this, PreviousQuizActivity.class);
        i.putExtra("test_id", test_id);
        i.putExtra("nomTest", quizName);
        i.putExtra("F_inicio",f_inicio);
        i.putExtra("H_inicio",h_inicio);
        i.putExtra("F_limite",f_limite);
        i.putExtra("H_limite",h_limite);
        i.putExtra("descripcion",descripcion);
        i.putExtra("estado",estado);
        startActivity(i);
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


}