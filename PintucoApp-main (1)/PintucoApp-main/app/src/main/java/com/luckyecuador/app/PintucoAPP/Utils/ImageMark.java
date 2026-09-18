package com.luckyecuador.app.PintucoAPP.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStream;

public class ImageMark {

    /**
     * METODO PARA AÑADIR LA MARCA DE AGUA
     * @param src
     * @param ciudad
     * @param local
     * @param user
     * @param fecha
     * @param color
     * @param alpha
     * @param size
     * @param underline
     * @return
     */
    public Bitmap mark(Bitmap src, String ciudad, String local, String user, String fecha, int color, int alpha, int size, boolean underline) {
        int w = src.getWidth();
        int h = src.getHeight();
        int text_size = h/45;

        Paint bgPaint=new Paint();
        bgPaint.setColor(Color.parseColor("#80000000"));  //transparent black,change opacity by changing hex value "AA" between "00" and "FF"

        Bitmap result = Bitmap.createBitmap(w, h, src.getConfig());
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(src, 0, 0, null);
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setTypeface(Typeface.MONOSPACE);
        paint.setTextSize(text_size);
        paint.setAntiAlias(true);
        paint.setUnderlineText(underline);

        //should draw background first,order is important
        float left = 0;
        float right = w;
        float bottom = h;
        float top = (float) (bottom-(h*.15));//TAMAÑO DEL CONTENEDOR DE INFO
        canvas.drawRect(left, top, right, bottom,bgPaint);

        canvas.drawText(ciudad, 10, h-(text_size*4), paint);
        canvas.drawText(local, 10, h-(text_size*3), paint);
        canvas.drawText(user, 10, h-(text_size*2), paint);
        canvas.drawText(fecha, 10, h-(text_size), paint);

        return result;
    }

    public Bitmap markOld(Bitmap src, String ciudad, String local, String user, String fecha, int color, int alpha, int size, boolean underline) {
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
        paint.setAntiAlias(true);
        paint.setUnderlineText(underline);

        //should draw background first,order is important
        float left = 0;
        float right = w;
        float bottom = h;
        float top = (float) (bottom-(h*.10));//TAMAÑO DEL CONTENEDOR DE INFO
        canvas.drawRect(left, top, right, bottom,bgPaint);

        canvas.drawText(ciudad, 10, h-270, paint);
        canvas.drawText(local, 10, h-185, paint);
        canvas.drawText(user, 10, h-100, paint);
        canvas.drawText(fecha, 10, h-15, paint);


        return result;
    }


    public static Bitmap getGoogleMapThumbnail(double lati, double longi){
        String URL = "http://maps.google.com/maps/api/staticmap?center=" +lati + "," + longi + "&zoom=15&size=200x200&sensor=false";
        Bitmap bmp = null;
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet request = new HttpGet(URL);

        InputStream in = null;
        try {
            in = httpclient.execute(request).getEntity().getContent();
            bmp = BitmapFactory.decodeStream(in);
            in.close();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return bmp;
    }

}
