package com.luckyecuador.app.PintucoAPP.data;

/**
 * Created by emon on 11/30/2017.
 */

import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.luckyecuador.app.PintucoAPP.Question;

import static com.luckyecuador.app.PintucoAPP.data.QuizContract.MovieEntry.KEY_ANSWER;
import static com.luckyecuador.app.PintucoAPP.data.QuizContract.MovieEntry.KEY_ID;
import static com.luckyecuador.app.PintucoAPP.data.QuizContract.MovieEntry.KEY_OPTA;
import static com.luckyecuador.app.PintucoAPP.data.QuizContract.MovieEntry.KEY_OPTB;
import static com.luckyecuador.app.PintucoAPP.data.QuizContract.MovieEntry.KEY_OPTC;
import static com.luckyecuador.app.PintucoAPP.data.QuizContract.MovieEntry.KEY_QUES;
import static com.luckyecuador.app.PintucoAPP.data.QuizContract.MovieEntry.TABLE_QUEST;

public class DbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "triviaQuiz";
    // tasks table name

    private SQLiteDatabase dbase;
    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        dbase=db;
        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_QUEST + " ( "
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_QUES
                + " TEXT, " + KEY_ANSWER+ " TEXT, "+KEY_OPTA +" TEXT, "
                +KEY_OPTB +" TEXT, "+KEY_OPTC+" TEXT)";
        db.execSQL(sql);
        addQuestions();
        //db.close();
    }

    private void addQuestions() {
        Question q1=new Question("1. ¿Cuántas Categorias maneja Kimberly Clark Ecuador?","5", "6 ", "9", "9");
        this.addQuestion(q1);
        Question q2=new Question("2. ¿Cuántas Marcas maneja Kimberly Clark Ecuador?", "2", "4", "6", "6");
        this.addQuestion(q2);
        Question q3=new Question("3. ¿Cada cuánto se releva información de PRECIOS?","Semanal", "Quincenal","Mensual", "Quincenal" );
        this.addQuestion(q3);
        Question q4=new Question("4. ¿Cada cuánto se releva información de SHARE?", "Semanal", "Quincenal","Mensual", "Quincenal" );
        this.addQuestion(q4);
        Question q5=new Question("5. Si mi producto no se encuentra visible en percha pero si se encuentra en bodega, ¿Este Codifica en el PDV?","SI","NO","NO ESTOY SEGURO(A)","SI");
        this.addQuestion(q5);
        Question q6=new Question("6. ¿Nombre del status de PEQUEÑIN?","Otelo & Fabell S.A","Productos Familia Sancela Del Ecuador S.","Zaimella Del Ecuador","Productos Familia Sancela Del Ecuador S.");
        this.addQuestion(q6);
        Question q7=new Question("7. ¿Cuántos módulos (reportes) que debe relevar estan en el menú principal del aplicatvo?","7","8","6","8");
        this.addQuestion(q7);
        Question q8=new Question("8. ¿Debe marcar su entrada y salida por cada punto de venta que visita?","SI","NO","NO ESTOY SEGURO(A)","SI");
        this.addQuestion(q8);
        Question q9=new Question("9. Para el reporte de PRECIOS, ¿Se releva información tanto de status propia como la de competencia?","SI","NO","NO ESTOY SEGURO(A)","SI");
        this.addQuestion(q9);
        Question q10=new Question("10. ¿Cada cuánto debo debo realizar la sincronización de mi usuario del app KC?","Todos los dias","Solo cuando exista una actualización tanto de productos como de PDV","Una vez por semana","Solo cuando exista una actualización tanto de productos como de PDV");
        this.addQuestion(q10);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUEST);
        // Create tables again
        onCreate(db);
    }
    // Adding new question
    public void addQuestion(Question quest) {
        //SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_QUES, quest.getQUESTION());
        values.put(KEY_ANSWER, quest.getANSWER());
        values.put(KEY_OPTA, quest.getOPTA());
        values.put(KEY_OPTB, quest.getOPTB());
        values.put(KEY_OPTC, quest.getOPTC());
        // Inserting Row
        dbase.insert(TABLE_QUEST, null, values);
    }
    public List<Question> getAllQuestions() {
        List<Question> quesList = new ArrayList<Question>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_QUEST;
        dbase=this.getReadableDatabase();
        Cursor cursor = dbase.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Question quest = new Question();
                quest.setID(cursor.getInt(0));
                quest.setQUESTION(cursor.getString(1));
                quest.setANSWER(cursor.getString(2));
                quest.setOPTA(cursor.getString(3));
                quest.setOPTB(cursor.getString(4));
                quest.setOPTC(cursor.getString(5));
                quesList.add(quest);
            } while (cursor.moveToNext());
        }
        // return quest list
        return quesList;
    }
    public int rowcount()
    {
        int row=0;
        String selectQuery = "SELECT  * FROM " + TABLE_QUEST;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        row=cursor.getCount();
        return row;
    }
}