package com.luckyecuador.app.PintucoAPP;


import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;


import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;
import com.luckyecuador.app.PintucoAPP.Conexion.Mensajes;
import com.luckyecuador.app.PintucoAPP.Conexion.VerificarNet;
import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertPreguntas;
import com.luckyecuador.app.PintucoAPP.Sync.SyncAdapter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class ResultQuizActivity extends AppCompatActivity {

    private String nomTest;
    private int score,tiempo;
    private int totalQuestions;
    private String resultado;
    private String mensajeDeAprobacion = "FELICITACIONES!!, HAS APROBADO\n" +
            "SATISFACTORIAMENTE EL TEST DE CONOCIMIENTO EN:\n";
    private String mensajeDeNoAprobacion = "NO HA PODIDO SUPERAR EL TEST DE CONOCIMIENTO EN:";
    private String id_pdv,test_id,user,codigo_pdv, punto_venta,fecha,hora, format
            ,FILE_NAME,DIRECTORIO,USUARIO, total_erroneas, porcentaje, cronometro;
    private double minutos;



    private TextView nombreTest;
    private TextView mensaje,mensajeCertificado;
    private ImageView certificadoImg;
    private Button btnRegresar,btnGenerarPDF,btnCompartir,btnVisualizar;
    private LinearLayout botones;
//    private PDFView pdfView;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_quiz);

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},PackageManager.PERMISSION_GRANTED);


        nombreTest = findViewById(R.id.nombre_test);
        mensaje = findViewById(R.id.tv_mensaje);
        mensajeCertificado = findViewById(R.id.tv_mensaje_certificado);
        certificadoImg = findViewById(R.id.certificadoImg);
        btnGenerarPDF = findViewById(R.id.btn_generarPDF);

//        pdfView = findViewById(R.id.pdfView);
        btnRegresar = findViewById(R.id.btn_back);
        btnRegresar = findViewById(R.id.btn_back);
        btnCompartir = findViewById(R.id.btn_compartir);
        btnVisualizar = findViewById(R.id.btn_viewer);
        botones = findViewById(R.id.botones);

        LoadData();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            test_id = extras.getString("test_id","noData");
            nomTest = extras.getString("nombreTest","noData");
            score = extras.getInt("score", 0);
            totalQuestions = extras.getInt("totalPreguntas",0);
            cronometro = extras.getString("cronometro","noData");

        }

        int correctas = score;
        int incorrectas = totalQuestions - score;


        DecimalFormat df = new DecimalFormat("0");
        resultado = df.format(((float) score/totalQuestions)*100);




        Log.i("SCORE", score + "");
        Log.i("TOTAL PREG", totalQuestions + "");
        Log.i("RESULTADO", resultado+"");


        if (Integer.parseInt(resultado) >=70) {
            mensaje.setText(mensajeDeAprobacion);
            nombreTest.setText(nomTest);
            certificadoImg.setImageResource(R.drawable.icono_certificado);
        } else{

            mensaje.setVisibility(View.GONE);
            nombreTest.setVisibility(View.GONE);
            mensajeCertificado.setVisibility(View.GONE);
            btnGenerarPDF.setVisibility(View.GONE);
            certificadoImg.setImageResource(R.drawable.post_lucky);
            certificadoImg.getLayoutParams().height = 600;
            certificadoImg.getLayoutParams().width = 600;

        }



        btnGenerarPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                crearCertificado();
                botones.setVisibility(View.VISIBLE);
            }
        });

        btnVisualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(ResultQuizActivity.this, VisualizarCertificadoActivity.class);
                i.putExtra("ruta",FILE_NAME);
                startActivity(i);

            }
        });

        btnCompartir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                File file = new File(FILE_NAME);

                if (file.exists()) {
                    final Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("application/pdf");
                    Uri outputPdfUri = FileProvider.getUriForFile(getApplicationContext(), ResultQuizActivity.this.getPackageName() + ".provider", file);
                    shareIntent.putExtra(Intent.EXTRA_STREAM, outputPdfUri);
                    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(Intent.createChooser(shareIntent, "Share PDF using.."));
                }


            }
        });



        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ResultQuizActivity.this,PuntosListActivity.class);
                startActivity(i);
            }
        });

        insertData(correctas,incorrectas,Integer.parseInt(resultado),nomTest);



    }





    private void insertData(int correctas,int incorrectas,int calificacion,String nombreTest)
    {
        try{
            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-5"));
            Date currentLocalTime = cal.getTime();
            DateFormat date = new SimpleDateFormat("dd/MM/yyy");
            date.setTimeZone(TimeZone.getTimeZone("GMT-5"));
            String fechaser = date.format(currentLocalTime);

            DateFormat hour = new SimpleDateFormat("HH:mm:ss");
            hour.setTimeZone(TimeZone.getTimeZone("GMT-5"));
            String horaser = hour.format(currentLocalTime);
            int estado_test = 1;

            ContentValues values = new ContentValues();

            values.put(ContractInsertPreguntas.Columnas.USUARIO, user);
            values.put(ContractInsertPreguntas.Columnas.TEST_ID, test_id);
            values.put(ContractInsertPreguntas.Columnas.TEST, nombreTest);
            values.put(ContractInsertPreguntas.Columnas.CORRECTAS, correctas);
            values.put(ContractInsertPreguntas.Columnas.INCORRECTAS, incorrectas);
            values.put(ContractInsertPreguntas.Columnas.CALIFICACION, calificacion);
            values.put(ContractInsertPreguntas.Columnas.ESTADO_TEST, estado_test);



            /*for(int i = 0; i < seleccionados.size(); i++){
                Log.i("SELECCIONADOS", seleccionados.get(i).toString() + " (INDICE: " + i + ")");
                Log.i("CUMPLIMIENTO", seleccionados.size()+"");
                if(seleccionados.size()==0){
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
                if(seleccionados.size()==1){
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
                if(seleccionados.size()==2){
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
                if(seleccionados.size()==3){
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
                if(seleccionados.size()==4){
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
                if(seleccionados.size()==5){
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
                if(seleccionados.size()==6){
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
                if(seleccionados.size()==7){
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
                if(seleccionados.size()==8){
                    seleccionados.add(8,"N/A");
                    seleccionados.add(9,"N/A");
                    seleccionados.add(10,"N/A");
                    seleccionados.add(11,"N/A");
                    seleccionados.add(12,"N/A");
                    seleccionados.add(13,"N/A");
                    seleccionados.add(14,"N/A");
                    seleccionados.add(15,"N/A");
                }
                if(seleccionados.size()==9){
                    seleccionados.add(9,"N/A");
                    seleccionados.add(10,"N/A");
                    seleccionados.add(11,"N/A");
                    seleccionados.add(12,"N/A");
                    seleccionados.add(13,"N/A");
                    seleccionados.add(14,"N/A");
                    seleccionados.add(15,"N/A");
                }
                if(seleccionados.size()==10){
                    seleccionados.add(10,"N/A");
                    seleccionados.add(11,"N/A");
                    seleccionados.add(12,"N/A");
                    seleccionados.add(13,"N/A");
                    seleccionados.add(14,"N/A");
                    seleccionados.add(15,"N/A");
                }
                if(seleccionados.size()==11){
                    seleccionados.add(11,"N/A");
                    seleccionados.add(12,"N/A");
                    seleccionados.add(13,"N/A");
                    seleccionados.add(14,"N/A");
                    seleccionados.add(15,"N/A");
                }
                if(seleccionados.size()==12){
                    seleccionados.add(12,"N/A");
                    seleccionados.add(13,"N/A");
                    seleccionados.add(14,"N/A");
                    seleccionados.add(15,"N/A");
                }
                if(seleccionados.size()==13){
                    seleccionados.add(13,"N/A");
                    seleccionados.add(14,"N/A");
                    seleccionados.add(15,"N/A");
                }
                if(seleccionados.size()==14){
                    seleccionados.add(14,"N/A");
                    seleccionados.add(15,"N/A");
                }
                if(seleccionados.size()==15){
                    seleccionados.add(15,"N/A");
                }
                /*values.put(ContractInsertPreguntas.Columnas.P1, seleccionados.get(0).toString());
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
            //}
            values.put(ContractInsertPreguntas.Columnas.FECHA, fechaser);
            values.put(ContractInsertPreguntas.Columnas.HORA, horaser);
            values.put(ContractInsertPreguntas.Columnas.CRONOMETO,cronometro);
            values.put(Constantes.PENDIENTE_INSERCION, 1);

            getContentResolver().insert(ContractInsertPreguntas.CONTENT_URI, values);

            if (VerificarNet.hayConexion(getApplicationContext())) {
                SyncAdapter.sincronizarAhora(this, true, Constantes.insertPreguntas, null);
                Toast.makeText(getApplicationContext(), Mensajes.ON_SYNC_SERVER, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), Mensajes.ON_SYNC_DEVICE, Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }



    private void crearCertificado() {

        Bitmap bmp,scaledBmp,bmp_logo,scaledBmp_logo;
        int pageWidth = 1200;


        bmp = BitmapFactory.decodeResource(getResources(),R.drawable.certificado_xplora);
     //   bmp_logo = BitmapFactory.decodeResource(getResources(),R.drawable.logo_danec_slogan);
        scaledBmp = Bitmap.createScaledBitmap(bmp,1123,794,false);
      //  scaledBmp_logo = Bitmap.createScaledBitmap(bmp_logo,200,80,false);


        int annio;
        String mes;
        int dia;
        String MES[] = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
        String Fecha;
        Calendar obtenerFecha = Calendar.getInstance();

        annio = obtenerFecha.get(Calendar.YEAR);
        mes = MES[obtenerFecha.get(Calendar.MONTH)];
        dia = obtenerFecha.get(Calendar.DATE);

        fecha = dia + " de "+ mes + " del " + annio;



        PdfDocument pdfDocument = new PdfDocument();
        Paint paint = new Paint();
        Paint mensajeAprobado = new Paint();
        Paint mensajeFelicitaciones = new Paint();
        Paint nombreTest = new Paint();
        Paint fechaHoy = new Paint();
        Paint mensajeConcedemosCertificado = new Paint();
        Paint nombreUsuario = new Paint();
        Paint puntaje = new Paint();



        PdfDocument.PageInfo myPageInfo = new PdfDocument.PageInfo.Builder(1123, 794, 1).create();
        PdfDocument.Page myPage1 = pdfDocument.startPage(myPageInfo);
        Canvas canvas = myPage1.getCanvas();

        /* Ocupa Todo  el Fondo la Imagen */
        canvas.drawBitmap(scaledBmp, 0, 0, paint);

        mensajeAprobado.setColor(getResources().getColor(R.color.text_color_cert));
        mensajeAprobado.setTextAlign(Paint.Align.CENTER);
        mensajeAprobado.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        mensajeAprobado.setTextSize(50);
        canvas.drawText("APROBADO", 570, 245, mensajeAprobado);

        mensajeFelicitaciones.setColor(getResources().getColor(R.color.text_color_cert));
        mensajeFelicitaciones.setTextAlign(Paint.Align.CENTER);
        mensajeFelicitaciones.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        mensajeFelicitaciones.setTextSize(15);
        canvas.drawText("Felicitaciones has aprobado el test de conocimiento", 570, 270, mensajeFelicitaciones);


        nombreTest.setColor(getResources().getColor(R.color.text_color_cert));
        nombreTest.setTextAlign(Paint.Align.CENTER);
        nombreTest.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        nombreTest.setTextSize(30);
        canvas.drawText(""+nomTest, 570, 310, nombreTest);


        fechaHoy.setColor(getResources().getColor(R.color.text_color_cert));
        fechaHoy.setTextAlign(Paint.Align.CENTER);
        fechaHoy.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        fechaHoy.setTextSize(17);
        canvas.drawText(""+fecha, 570, 345, fechaHoy);


        mensajeConcedemosCertificado.setColor(getResources().getColor(R.color.text_color_cert));
        mensajeConcedemosCertificado.setTextAlign(Paint.Align.CENTER);
        mensajeConcedemosCertificado.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        mensajeConcedemosCertificado.setTextSize(17);
        canvas.drawText("concedemos el certificado de aprobación a : ", 570, 365, mensajeConcedemosCertificado);


        nombreUsuario.setColor(getResources().getColor(R.color.text_color_cert));
        nombreUsuario.setTextAlign(Paint.Align.CENTER);
        nombreUsuario.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD));
        nombreUsuario.setTextSize(50);
        canvas.drawText(""+user,570,430,nombreUsuario);

        nombreUsuario.setColor(getResources().getColor(R.color.text_color_cert));
        nombreUsuario.setTextAlign(Paint.Align.CENTER);
        nombreUsuario.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.NORMAL));
        nombreUsuario.setTextSize(30);
        canvas.drawText("Con un puntaje de:",570,500,nombreUsuario);


        nombreUsuario.setColor(getResources().getColor(R.color.text_color_cert));
        nombreUsuario.setTextAlign(Paint.Align.CENTER);
        nombreUsuario.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD));
        nombreUsuario.setTextSize(60);
        canvas.drawText(""+resultado,550,600,nombreUsuario);

        nombreUsuario.setTextAlign(Paint.Align.CENTER);
        nombreUsuario.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.NORMAL));
        nombreUsuario.setTextSize(40);
        canvas.drawText("pts",630,600,nombreUsuario);

      //  canvas.drawBitmap(scaledBmp_logo, 450, 675, paint);


        pdfDocument.finishPage(myPage1);

        String ubicacion = Environment.getExternalStorageDirectory().getPath();
        File carpCertificado = new File(ubicacion + "/Certificados/");

        if (!carpCertificado.exists()){
            carpCertificado.mkdir();
        }

        DIRECTORIO = Environment.getExternalStorageDirectory()+"/Certificados/";
        USUARIO = "CERT_"+nomTest+"_"+user;
        FILE_NAME = DIRECTORIO + USUARIO + ".pdf";


        File file = new File(FILE_NAME);

        if (!file.exists()) {
            Toast.makeText(getApplicationContext(),"Se Guardo en :"+FILE_NAME,Toast.LENGTH_LONG).show();
//                    pdfView.fromFile(file).load();
            try {
                pdfDocument.writeTo(new FileOutputStream(file));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            Toast.makeText(getApplicationContext(),"El Certificado ya se ha Descargado.",Toast.LENGTH_LONG).show();
        }


        pdfDocument.close();

    }


    @Override
    public void onBackPressed() {

    }

    public void LoadData(){
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








}
