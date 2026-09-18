package com.luckyecuador.app.PintucoAPP;


import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.luckyecuador.app.PintucoAPP.Clase.Base_preguntas;
import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;
import com.luckyecuador.app.PintucoAPP.Conexion.VerificarNet;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertResultadoPreguntas;
import com.luckyecuador.app.PintucoAPP.DataBase.DatabaseHelper;
import com.luckyecuador.app.PintucoAPP.DataBase.Provider;
import com.luckyecuador.app.PintucoAPP.Sync.SyncAdapter;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;


public class QuizMainActivity extends AppCompatActivity {


    private TextView textViewQuestion;
    private TextView textViewQuestionCount;
    private TextView textViewCountDown;
    private RadioGroup rbGroup;
    private RadioButton rb_opta, rb_optb, rb_optc;
    private Button btnNext;
    private ProgressBar progressBar;
    private String hours,fecha,hora;
    private Chronometer crono;



    private String test_id;
    private String nomTest;
    private int score = 0;
    private int questionCounter = 0;
    private int questionCountTotal;
    private Base_preguntas currentQuestion;
    private boolean answered;
    // Tiempo en Milisegundos
    private  int COUNTDOWN_IN_MILLIS = 0;
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis;
    private int incremento = 0;



    // Lista de Preguntas
    private List<Base_preguntas> questionList;
    private ArrayList<String> opciones = new ArrayList<>();

    // Insert Preguntas
    private String user,preguntaActual,opcionA,opcionB,
            opcionC,resp,respUsuario,resultado,tiempo,cronometro;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        LoadData();

        // Obtiene los Componentes del xml
        getXmlComponents();

        // Obtener las preguntas
        fetchDB();


    }



    private void getXmlComponents(){

        // Obtenemos los componentes de la parte visual
        textViewQuestion = findViewById(R.id.tv_mensaje);
        textViewQuestionCount = findViewById(R.id.tv_question_counter);
        textViewCountDown = findViewById(R.id.tv_countdown);
        rbGroup = findViewById(R.id.options);
        rb_opta = findViewById(R.id.rb_opta);
        rb_optb = findViewById(R.id.rb_optb);
        rb_optc = findViewById(R.id.rb_optc);
        crono = findViewById(R.id.crono);
        progressBar= findViewById(R.id.tv_progressBarQuestions);
        btnNext = findViewById(R.id.btn_back);


    }



    public void insertarPregunta(String usuario,String test_id,String test, String preguntaActual, String opcionA, String opcionB, String opcionC, String resp, String respUsuario, String resultado) {


        obtenerFecha();


        DatabaseHelper dHelper = new DatabaseHelper(QuizMainActivity.this, Provider.DATABASE_NAME, null, 1);
        SQLiteDatabase db = dHelper.getWritableDatabase();


        // Guardando Datos de manera Local
        ContentValues values = new ContentValues();
     //   values.put(ContractInsertResultadoPreguntas.Columnas.KEY_USUARIO_ID, usuario_id);
        values.put(ContractInsertResultadoPreguntas.Columnas.KEY_USUARIO,usuario );
        values.put(ContractInsertResultadoPreguntas.Columnas.KEY_TEST_ID,test_id );
        values.put(ContractInsertResultadoPreguntas.Columnas.KEY_TEST,test );
        values.put(ContractInsertResultadoPreguntas.Columnas.KEY_QUES,preguntaActual );
        values.put(ContractInsertResultadoPreguntas.Columnas.KEY_OPTA,opcionA );
        values.put(ContractInsertResultadoPreguntas.Columnas.KEY_OPTB,opcionB );
        values.put(ContractInsertResultadoPreguntas.Columnas.KEY_OPTC,opcionC );
        values.put(ContractInsertResultadoPreguntas.Columnas.KEY_ANSWER,resp );
        values.put(ContractInsertResultadoPreguntas.Columnas.KEY_ANSWER_USER,respUsuario );
        values.put(ContractInsertResultadoPreguntas.Columnas.KEY_RESULT,resultado);
        values.put(ContractInsertResultadoPreguntas.Columnas.KEY_FECHA,fecha);
        values.put(ContractInsertResultadoPreguntas.Columnas.KEY_HORA,hora);
        values.put(Constantes.PENDIENTE_INSERCION, 1);


      //  db.insert(ContractInsertResultadoPreguntas.INSERT_RESULTADO_PREGUNTAS, null,values);

     //   getContentResolver().insert(ContractInsertResultadoPreguntas.CONTENT_URI, values);

        getContentResolver().insert(ContractInsertResultadoPreguntas.CONTENT_URI, values);

        if (VerificarNet.hayConexion(getApplicationContext())) {
            SyncAdapter.sincronizarAhora(this, true, Constantes.insertResultadoPreguntas, null);
         //   Toast.makeText(getApplicationContext(), Mensajes.ON_SYNC_SERVER, Toast.LENGTH_SHORT).show();
        } else {
         //   Toast.makeText(getApplicationContext(), Mensajes.ON_SYNC_DEVICE, Toast.LENGTH_SHORT).show();
        }

    }






    private void fetchDB(){

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            test_id = extras.getString("test_id", "NoData");
            nomTest = extras.getString("nomTest","noData");
        }

        DatabaseHelper db = new DatabaseHelper(this, Provider.DATABASE_NAME, null, 1);

        questionList = db.getAllQuestionsById(test_id);

        incremento = 0;

        startQuiz();
    }


    private void startQuiz(){

        // Inicia el Cronometro
        long startTime = SystemClock.elapsedRealtime();
        crono.setBase(startTime);
        crono.start();

        // El Valor total de preguntas
        questionCountTotal = questionList.size();

        // Reordenar Preguntas
        Collections.shuffle(questionList);


        // Muestra la siguiente pregunta
        showNextQuestion();


        // Cuando se da click en el boton siguiente
        btnNext.setOnClickListener(v -> {

            // Incremento de ProgressBar
            double porcentaje = (100 / questionCountTotal );
            incremento = (int) Math.ceil(porcentaje);

            rb_opta.setClickable(true);
            rb_optb.setClickable(true);
            rb_optc.setClickable(true);

            rb_opta.setVisibility(View.VISIBLE);
            rb_optb.setVisibility(View.VISIBLE);
            rb_optc.setVisibility(View.VISIBLE);

            // Verifica si se ha seleccionado una respuesta
            if(!answered)
            {
                // Si se ha seleccionado una respuesta
                if(rbGroup.getCheckedRadioButtonId() != -1)
                {
                    // Verifica la respuesta del usuario
                    checkAnswer();
                    // Muestra la siguiente pregunta
                    showNextQuestion();

                }
                // Conometro llega a 0
                else if(timeLeftInMillis == 0) {
                    // Incrementa el progressbar
                    progressBar.incrementProgressBy(incremento);
                    // Muestra la siguiente pregunta
                    showNextQuestion();
                }
                else
                {
                    Toast.makeText(QuizMainActivity.this,"Por favor, Selecione una respuesta",Toast.LENGTH_LONG).show();
                }
            }
        });


    }


    private void checkAnswer(){
        answered = true;

        countDownTimer.cancel();

        int rbSelected = rbGroup.getCheckedRadioButtonId();
        RadioButton selectedRadioButton = (RadioButton)findViewById(rbSelected);
        String respuestaUser = selectedRadioButton.getText().toString();
        String respuestaCorrecta = currentQuestion.getAnswer();

        if(respuestaUser.equals(respuestaCorrecta))
        {
            score+=1;
        }

        // Guardar Preguntas y respuesta del usuario
        preguntaActual =  currentQuestion.getQuestion();
        resp = currentQuestion.getAnswer();
        opcionA = currentQuestion.getOpta();
        opcionB = currentQuestion.getOptb();
        opcionC = currentQuestion.getOptc();
        respUsuario = respuestaUser;
        resultado = respuestaUser.equals(respuestaCorrecta) ? "CORRECTA" : "INCORRECTA";


        obtenerFecha();


        DatabaseHelper dHelper = new DatabaseHelper(QuizMainActivity.this, Provider.DATABASE_NAME, null, 1);
        SQLiteDatabase db = dHelper.getWritableDatabase();


        // Guardando Datos de manera Local
        ContentValues values = new ContentValues();
        //   values.put(ContractInsertResultadoPreguntas.Columnas.KEY_USUARIO_ID, usuario_id);
        values.put(ContractInsertResultadoPreguntas.Columnas.KEY_USUARIO,user );
        values.put(ContractInsertResultadoPreguntas.Columnas.KEY_TEST_ID,test_id );
        values.put(ContractInsertResultadoPreguntas.Columnas.KEY_TEST,nomTest );
        values.put(ContractInsertResultadoPreguntas.Columnas.KEY_QUES,preguntaActual );
        values.put(ContractInsertResultadoPreguntas.Columnas.KEY_OPTA,opcionA );
        values.put(ContractInsertResultadoPreguntas.Columnas.KEY_OPTB,opcionB );
        values.put(ContractInsertResultadoPreguntas.Columnas.KEY_OPTC,opcionC );
        values.put(ContractInsertResultadoPreguntas.Columnas.KEY_ANSWER,resp );
        values.put(ContractInsertResultadoPreguntas.Columnas.KEY_ANSWER_USER,respUsuario );
        values.put(ContractInsertResultadoPreguntas.Columnas.KEY_RESULT,resultado);
        values.put(ContractInsertResultadoPreguntas.Columnas.KEY_FECHA,fecha);
        values.put(ContractInsertResultadoPreguntas.Columnas.KEY_HORA,hora);
        values.put(Constantes.PENDIENTE_INSERCION, 1);


        //  db.insert(ContractInsertResultadoPreguntas.INSERT_RESULTADO_PREGUNTAS, null,values);

        //   getContentResolver().insert(ContractInsertResultadoPreguntas.CONTENT_URI, values);

        getContentResolver().insert(ContractInsertResultadoPreguntas.CONTENT_URI, values);

        if (VerificarNet.hayConexion(getApplicationContext())) {
            SyncAdapter.sincronizarAhora(this, true, Constantes.insertResultadoPreguntas, null);
            //   Toast.makeText(getApplicationContext(), Mensajes.ON_SYNC_SERVER, Toast.LENGTH_SHORT).show();
        } else {
            //   Toast.makeText(getApplicationContext(), Mensajes.ON_SYNC_DEVICE, Toast.LENGTH_SHORT).show();
        }




     //   insertarPregunta(user,test_id,nomTest,preguntaActual,opcionA,opcionB,opcionC,resp,respUsuario,resultado);

    }

    private void showNextQuestion(){


        progressBar.incrementProgressBy(incremento);


        // Limpia los Radiobuttons
        rbGroup.clearCheck();

        if(questionCounter < questionCountTotal){


            currentQuestion = questionList.get(questionCounter);
            textViewQuestion.setText(currentQuestion.getQuestion());

            if (opciones.size() > 0){
                opciones.clear();
            }

            opciones.add(currentQuestion.getOpta());
            opciones.add(currentQuestion.getOptb());
            opciones.add(currentQuestion.getOptc());

            Collections.shuffle(opciones);



            rb_opta.setText(opciones.get(0));
            rb_optb.setText(opciones.get(1));
            rb_optc.setText(opciones.get(2));


         //   rb_opta.setText(currentQuestion.getOpta());
         //   rb_optb.setText(currentQuestion.getOptb());
         //   rb_optc.setText(currentQuestion.getOptc());
        //    tiempo += Integer.parseInt(questionList.get(questionCounter).getTiempo());

            questionCounter ++;
            textViewQuestionCount.setText("Pregunta: "+ questionCounter + "/" + questionCountTotal);
            answered = false;


            COUNTDOWN_IN_MILLIS = Integer.parseInt(currentQuestion.getTiempo()) * 1000;

            timeLeftInMillis = COUNTDOWN_IN_MILLIS;

            //     Toast.makeText(getApplicationContext(), currentQuestion.getTiempo() * 1000,Toast.LENGTH_LONG).show();


            startCountDown();






        }else
        {

            finishQuiz();
        }

    }



    private void startCountDown(){
        countDownTimer = new CountDownTimer(timeLeftInMillis,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
            //    progressBar.incrementProgressBy(incremento);
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                timeLeftInMillis = 0;
                //   showNextQuestion();
                updateCountDownText();
            }
        }.start();
    }

    private void updateCountDownText(){
        int minutes = (int) (timeLeftInMillis / 1000) / 60 ;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;

        String timeFormatted = String.format(Locale.getDefault(),"%01d:%02d",minutes,seconds);
        textViewCountDown.setText(timeFormatted + " Seg");


        if(timeLeftInMillis == 0){

          //  progressBar.incrementProgressBy(incremento);

            if(rbGroup.getCheckedRadioButtonId() != -1 ) {
                checkAnswer();
                showNextQuestion();
            }
            else
            {
                rb_opta.setClickable(false);
                rb_optb.setClickable(false);
                rb_optc.setClickable(false);

                textViewCountDown.setText("Tiempo Finalizado");

                preguntaActual = currentQuestion.getQuestion();
                resp = currentQuestion.getAnswer();
                opcionA = currentQuestion.getOpta();
                opcionB = currentQuestion.getOptb();
                opcionC = currentQuestion.getOptc();
                respUsuario =  "NO RESPONDIO";
                resultado =  "INCORRECTA";

             //   progressBar.incrementProgressBy(incremento);

                obtenerFecha();


                DatabaseHelper dHelper = new DatabaseHelper(QuizMainActivity.this, Provider.DATABASE_NAME, null, 1);
                SQLiteDatabase db = dHelper.getWritableDatabase();


                // Guardando Datos de manera Local
                ContentValues values = new ContentValues();
                //   values.put(ContractInsertResultadoPreguntas.Columnas.KEY_USUARIO_ID, usuario_id);
                values.put(ContractInsertResultadoPreguntas.Columnas.KEY_USUARIO,user );
                values.put(ContractInsertResultadoPreguntas.Columnas.KEY_TEST_ID,test_id );
                values.put(ContractInsertResultadoPreguntas.Columnas.KEY_TEST,nomTest );
                values.put(ContractInsertResultadoPreguntas.Columnas.KEY_QUES,preguntaActual );
                values.put(ContractInsertResultadoPreguntas.Columnas.KEY_OPTA,opcionA );
                values.put(ContractInsertResultadoPreguntas.Columnas.KEY_OPTB,opcionB );
                values.put(ContractInsertResultadoPreguntas.Columnas.KEY_OPTC,opcionC );
                values.put(ContractInsertResultadoPreguntas.Columnas.KEY_ANSWER,resp );
                values.put(ContractInsertResultadoPreguntas.Columnas.KEY_ANSWER_USER,respUsuario );
                values.put(ContractInsertResultadoPreguntas.Columnas.KEY_RESULT,resultado);
                values.put(ContractInsertResultadoPreguntas.Columnas.KEY_FECHA,fecha);
                values.put(ContractInsertResultadoPreguntas.Columnas.KEY_HORA,hora);
                values.put(Constantes.PENDIENTE_INSERCION, 1);


                //  db.insert(ContractInsertResultadoPreguntas.INSERT_RESULTADO_PREGUNTAS, null,values);

                //   getContentResolver().insert(ContractInsertResultadoPreguntas.CONTENT_URI, values);

                getContentResolver().insert(ContractInsertResultadoPreguntas.CONTENT_URI, values);

                if (VerificarNet.hayConexion(getApplicationContext())) {
                    SyncAdapter.sincronizarAhora(this, true, Constantes.insertResultadoPreguntas, null);
                    //   Toast.makeText(getApplicationContext(), Mensajes.ON_SYNC_SERVER, Toast.LENGTH_SHORT).show();
                } else {
                    //   Toast.makeText(getApplicationContext(), Mensajes.ON_SYNC_DEVICE, Toast.LENGTH_SHORT).show();
                }

                //  insertarPregunta(user,test_id,nomTest,preguntaActual,opcionA,opcionB,opcionC,resp,respUsuario,resultado);

            }

        }


    }

    private void finishQuiz(){

        crono.stop();
        // Obtener el Tiempo de la Prueba
        cronometro = crono.getText().toString();
      //  Toast.makeText(this,""+cronometro,Toast.LENGTH_LONG).show();



        btnNext.setText("Finalizar");

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setProgress(0);
                resultQuiz();
            }
        });



    }

    @Override
    public void onBackPressed() {

    }

    public void LoadData(){
        SharedPreferences sharedPreferences = getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        user = sharedPreferences.getString(Constantes.USER,Constantes.NODATA);
    }

    private void resultQuiz(){
        //  int resultado = (int) Math.floor(score/questionCountTotal) * 100 ;

        Intent i = new Intent(QuizMainActivity.this,ResultQuizActivity.class);
        // Toast.makeText(getApplicationContext(),nomTest + " " +puntuacion,Toast.LENGTH_LONG).show();
        i.putExtra("test_id",test_id);
        i.putExtra("nombreTest",nomTest);
        i.putExtra("score",score);
        i.putExtra("totalPreguntas",questionCountTotal);
        i.putExtra("cronometro",cronometro);
       // i.putExtra("tiempo",tiempo);
        startActivity(i);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(countDownTimer != null){
            countDownTimer.cancel();
        }
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




