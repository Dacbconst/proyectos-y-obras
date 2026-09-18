package com.luckyecuador.app.PintucoAPP.Utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.widget.ImageView;


public class ImageMarkAlmuerzo {

    /**
     * METODO PARA AÑADIR LA MARCA DE AGUA
     * @param fecha
     * @param fecha
     * @param color
     * @param alpha
     * @param size
     * @param underline
     * @return
     */

    public Bitmap mark(Bitmap src,String tipo ,String ciudad, String local,String categoria, String fecha, String coordenadas, String direccion, int color, int alpha, int size, boolean underline) {
        int w = src.getWidth();
        int h = src.getHeight();

        Paint bgPaint=new Paint();
        bgPaint.setColor(Color.parseColor("#80000000"));  //transparent black,change opacity by changing hex value "AA" between "00" and "FF"

        Bitmap result = Bitmap.createBitmap(w, h, src.getConfig());
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(src, 0, 0, null);



        Paint paint = new Paint();
        paint.setColor(color);
        paint.setTextSize(size);
        paint.setTypeface(Typeface.SERIF);
        paint.setAntiAlias(false);
        paint.setUnderlineText(underline);

        //should draw background first,order is important
        float left = 0;
        float right = w;
        float bottom = h;
        float top = 0;
        if (w>h) {
            top = (float) (bottom-(h*.2));//TAMAÑO DEL CONTENEDOR DE INFO
        } else {
            top = (float) (bottom-(h*.28));//TAMAÑO DEL CONTENEDOR DE INFO
        }

        canvas.drawRect(left, top,right,bottom,bgPaint);
        canvas.drawText(tipo, 10, h-(size*8), paint);
        canvas.drawText(local, 10, h-(size*6), paint);
        //canvas.drawText(ciudad, 10, h-(size*4), paint);
        canvas.drawText(fecha, 10, h-size*5, paint);
        canvas.drawText(categoria, 10, h-(size*4), paint);
        canvas.drawText(coordenadas, 10, h-(size*3), paint);
        canvas.drawText("Direccion:", 10, h-(size*2), paint);
        canvas.drawText(direccion, 10, h-(size), paint);

        //canvas.drawBitmap(getGoogleMapThumbnail(-2.177968, -79.911970),10,h-(size),paint);





        //  canvas.drawText(user, 10, h-(size*2), paint);


        return result;
    }

    public Bitmap markSinMapa(Bitmap src, String fecha,String hora,String usuario,String coordenadas, int color, int alpha, int size, boolean underline) {
        int w = src.getWidth();
        int h = src.getHeight();

        Paint bgPaint=new Paint();
        bgPaint.setColor(Color.parseColor("#80000000"));  //transparent black,change opacity by changing hex value "AA" between "00" and "FF"

        Bitmap result = Bitmap.createBitmap(w, h, src.getConfig());
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(src, 0, 0, null);



        Paint paint = new Paint();
        paint.setColor(color);
        paint.setTextSize(size);
        paint.setTypeface(Typeface.SERIF);
        paint.setAntiAlias(false);
        paint.setUnderlineText(underline);

        //should draw background first,order is important
        float left = 0;
        float right = w;
        float bottom = h;
        float top = 0;
        if (w>h) {
            top = (float) (bottom-(h*.2));//TAMAÑO DEL CONTENEDOR DE INFO
        } else {
            top = (float) (bottom-(h*.15));//TAMAÑO DEL CONTENEDOR DE INFO
        }

        canvas.drawRect(left, top,right,bottom,bgPaint);
        canvas.drawText(fecha, 10, h-(size*4), paint);
        canvas.drawText(hora, 10, h-(size*3), paint);
        canvas.drawText(usuario, 10, h-(size*2), paint);
        canvas.drawText(coordenadas, 10, h-(size), paint);

        return result;
    }

    public Bitmap mark2(Bitmap src,String tipo ,String ciudad, String local,String categoria, String fecha, String coordenadas, String direccion, int color, int alpha, int size, boolean underline, ImageView imageView) {
        int w = src.getWidth();
        int h = src.getHeight();

        Paint bgPaint=new Paint();
        bgPaint.setColor(Color.parseColor("#80000000"));  //transparent black,change opacity by changing hex value "AA" between "00" and "FF"

        Bitmap result = Bitmap.createBitmap(w, h, src.getConfig());
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(src, 0, 0, null);



        Paint paint = new Paint();
        paint.setColor(color);
        paint.setTextSize(size);
        paint.setTypeface(Typeface.SERIF);
        paint.setAntiAlias(false);
        paint.setUnderlineText(underline);

        //should draw background first,order is important
        float left = 0;
        float right = w;
        float bottom = h;
        float top = 0;
        if (w>h) {
            top = (float) (bottom-(h*.2));//TAMAÑO DEL CONTENEDOR DE INFO
        } else {
            top = (float) (bottom-(h*.28));//TAMAÑO DEL CONTENEDOR DE INFO
        }

        canvas.drawRect(left, top,right,bottom,bgPaint);
        canvas.drawText(tipo, 10, h-(size*8), paint);
        canvas.drawText(local, 10, h-(size*7), paint);
        //canvas.drawText(ciudad, 10, h-(size*4), paint);
        canvas.drawText(fecha, 10, h-size*6, paint);
        canvas.drawText(categoria, 10, h-(size*5), paint);
        canvas.drawText(coordenadas, 10, h-(size*4), paint);
        canvas.drawText("Direccion:", 10, h-(size*3), paint);
        canvas.drawText(direccion, 10, h-(size*2), paint);




        //Bitmap bitmap = imageViewToBitmap(imageView);



        Log.i("bitbit",""+ imageView.getDrawable());
        //canvas.drawBitmap(bitmap,2055,h-(size*10),null);



        //  canvas.drawText(user, 10, h-(size*2), paint);


        return result;
    }


    public static Bitmap imageViewToBitmap(ImageView imageView) {
        // Obtén el drawable del ImageView
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();

        if (drawable != null) {
            // Convierte el drawable a Bitmap y devuélvelo
            return drawable.getBitmap();
        } else {
            // En caso de que el ImageView no tenga un drawable válido, devuelve null
            return null;
        }
    }


}


