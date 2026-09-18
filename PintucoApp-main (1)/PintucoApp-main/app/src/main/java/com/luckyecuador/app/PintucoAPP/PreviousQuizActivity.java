package com.luckyecuador.app.PintucoAPP;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.luckyecuador.app.PintucoAPP.Clase.Base_preguntas;

import com.luckyecuador.app.PintucoAPP.DataBase.DatabaseHelper;
import com.luckyecuador.app.PintucoAPP.DataBase.Provider;


import java.util.List;

public class PreviousQuizActivity extends AppCompatActivity {

    private String test_id,nomTest,f_inicio,h_inicio,f_limite,h_limite,descripcion,estado;



    private TextView textView_f_inicio;
    private TextView textView_f_limite;
    private TextView textView_descripcion;
    private int questionCountTotal;
    private TextView textView_totalQuestions;
    private Button btnStart;

    // Lista de Preguntas
    private List<Base_preguntas> questionList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previous);



        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            test_id = extras.getString("test_id", "NoData");
            nomTest = extras.getString("nomTest", "NoData");
            f_inicio = extras.getString("F_inicio","NoData");
            h_inicio = extras.getString("H_inicio","NoData");
            f_limite = extras.getString("F_limite","NoData");
            h_limite = extras.getString("H_limite","NoData");
            descripcion = extras.getString("descripcion","Nodata");
            estado = extras.getString("estado","Nodata");

        }

        this.setTitle(nomTest);


        Toolbar toolbar =  findViewById(R.id.toolbar);
        TextView textView_nomTest = findViewById(R.id.tv_nomTest);
        textView_descripcion = findViewById(R.id.tv_descripcion);
        textView_f_inicio = findViewById(R.id.tv_fecha_inicio);
        textView_f_limite = findViewById(R.id.tv_fecha_limite);
        textView_totalQuestions = findViewById(R.id.tv_totalQuestions);
        btnStart = findViewById(R.id.btn_start_quiz);


        // Cambia el Titulo del Toolbar por el nombre del test
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(nomTest);

        textView_nomTest.setText("Evaluacion de " + nomTest);
        textView_descripcion.setText(descripcion);
        textView_f_inicio.setText("FECHA DE INICIO : " + f_inicio + " - " + h_inicio );
        textView_f_limite.setText("FECHA LIMITE : "+ f_limite + " - " + h_limite);



        DatabaseHelper db = new DatabaseHelper(this, Provider.DATABASE_NAME, null, 1);

        questionList = db.getAllQuestionsById(test_id);

        // El Valor total de preguntas
        questionCountTotal = questionList.size();

        textView_totalQuestions.setText(questionCountTotal + " Preguntas con varias opciones");

            btnStart.setOnClickListener(v -> quiz());



    }


    private void quiz(){
        Intent i = new Intent(this, QuizMainActivity.class);
        i.putExtra("test_id",test_id);
        i.putExtra("nomTest",nomTest);
        startActivity(i);
    }


}
