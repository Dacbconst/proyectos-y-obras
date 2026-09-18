package com.luckyecuador.app.PintucoAPP;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.luckyecuador.app.PintucoAPP.Clase.BasePreguntas;
import com.luckyecuador.app.PintucoAPP.DataBase.DatabaseHelper;

public class QuizActivity extends AppCompatActivity {

    List<BasePreguntas> quesList;
    int score = 0;
    int qid = 0;
    BasePreguntas currentQ;
    TextView txtQuestion;
    RadioButton rda, rdb, rdc;
    Button butNext;
    DatabaseHelper db;
    ArrayList<String> seleccionados = new ArrayList<String>();
    ;

    private String canal, user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        /*
        db = new DatabaseHelper(this, Provider.DATABASE_NAME, null, 1);
      //  LoadData();

        canal = getIntent().getStringExtra("canal");

        quesList = db.getAllQuestions(canal);
        currentQ = quesList.get(qid);
     //   txtQuestion=(TextView)findViewById(R.id.textView1);
    //    rda=(RadioButton)findViewById(R.id.radio0);
    //    rdb=(RadioButton)findViewById(R.id.radio1);
    //    rdc=(RadioButton)findViewById(R.id.radio2);
    //    butNext=(Button)findViewById(R.id.button1);

        //startService(new Intent(getApplicationContext(), MyService.class));

      //  setQuestionView();

        butNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               RadioGroup grp=(RadioGroup)findViewById(R.id.radioGroup1);
                if (grp.getCheckedRadioButtonId() != -1) {
                    RadioButton answer = (RadioButton) findViewById(grp.getCheckedRadioButtonId());
                    grp.clearCheck();
                    Log.i("yourans", currentQ.getAnswer() + " " + answer.getText());
                    if (currentQ.getAnswer().equals(answer.getText())) {
                        seleccionados.add(answer.getText().toString().concat(" (C)"));
                        score++;
                        Log.i("score", "Your score" + score);
                    }else{
                        seleccionados.add(answer.getText().toString().concat(" (I)"));
                    }
                    if (qid < db.rowcount(canal)) {
                        Log.i("QID", qid+"");
                        Log.i("ROW COUNT", db.rowcount(canal)+"");
                        currentQ = quesList.get(qid);
                        setQuestionView();
                    } else {
                        Intent intent = new Intent(QuizActivity.this, ResultActivity.class);
                        Bundle b = new Bundle();
                        b.putInt("score", score); //Your score
                        b.putInt("total_preguntas", db.rowcount(canal));
                        b.putStringArrayList("seleccionados",seleccionados);
                        intent.putExtras(b); //Put your score to your next Intent
                        startActivity(intent);
                        finish();
                    }
                }else{
                    Toast.makeText(getApplicationContext(),"Debe seleccionar una respuesta para continuar",Toast.LENGTH_LONG).show();
                }
            }
        });


    }
    public void LoadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        user = sharedPreferences.getString(Constantes.USER,Constantes.NODATA);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_quiz, menu);
        return true;
    }
    private void setQuestionView() {
        txtQuestion.setText(currentQ.getQuestion());
        rda.setText(currentQ.getOpta());
        rdb.setText(currentQ.getOptb());
        rdc.setText(currentQ.getOptc());

        qid++;
    }
    */
    }
}

