package com.luckyecuador.app.PintucoAPP.Utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;

import com.luckyecuador.app.PintucoAPP.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class EvidenciaMark {

    public interface BitmapCallback {
        void onBitmapReady(Bitmap bitmap);
    }

    public Bitmap mark(Bitmap src,String tipo ,String ciudad, String local,String categoria, String fecha, String coordenadas, String direccion, int color, int alpha, int size, boolean underline) {
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
        paint.setTextSize(text_size);
        paint.setTypeface(Typeface.SERIF);
        paint.setAntiAlias(false);
        paint.setUnderlineText(underline);

        //should draw background first,order is important
        float left = 0;
        float right = w;
        float bottom = h;
        float top = (float) (bottom-(h*.15));//TAMAÑO DEL CONTENEDOR DE INFO

        /*if (w>h) {
            top = (float) (bottom-(h*.2));//TAMAÑO DEL CONTENEDOR DE INFO
        } else {
            top = (float) (bottom-(h*.28));//TAMAÑO DEL CONTENEDOR DE INFO
        }*/

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

    public Bitmap markSinMapa(Bitmap src,String ciudad, String local,String usuario, String coordenadas, String direccion, String fecha_hora, int color, int alpha, int size, boolean underline) {
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
        float top = (float) (bottom-(h*.2));//TAMAÑO DEL CONTENEDOR DE INFO

        /*if (w>h) {
            top = (float) (bottom-(h*.2));//TAMAÑO DEL CONTENEDOR DE INFO
        } else {
            top = (float) (bottom-(h*.28));//TAMAÑO DEL CONTENEDOR DE INFO
        }*/

        canvas.drawRect(left, top,right,bottom,bgPaint);
        canvas.drawText(ciudad, 20, h-(text_size*7), paint);
        //canvas.drawText(ciudad, 10, h-(size*4), paint);
        canvas.drawText(local, 20, h-(text_size*6), paint);
        canvas.drawText(usuario, 20, h-(text_size*5), paint);
        canvas.drawText(coordenadas, 20, h-(text_size*4), paint);
        canvas.drawText("Direccion:", 20, h-(text_size*3), paint);
        canvas.drawText(direccion, 20, h-(text_size*2), paint);
        canvas.drawText(fecha_hora, 20, h-(text_size), paint);

        //canvas.drawBitmap(getGoogleMapThumbnail(-2.177968, -79.911970),10,h-(size),paint);





        //  canvas.drawText(user, 10, h-(size*2), paint);
        return result;
    }

    public void mark2(Double latitud, Double longitud, Bitmap src, String ciudad, String local, String categoria, String fecha, String coordenadas, String direccion,
                      int color, int alpha, int size, boolean underline, ImageView imageView, Drawable drawable, BitmapCallback callback) {
        //dimensiones de la foto
        int w = src.getWidth();
        int h = src.getHeight();
        int text_size = h/45;

        Log.i("dimensiones foto", w + " " +h);

        Paint bgPaint=new Paint();
        bgPaint.setColor(Color.parseColor("#80000000"));  //transparent black,change opacity by changing hex value "AA" between "00" and "FF"
        Bitmap result = Bitmap.createBitmap(w, h, src.getConfig());
        Log.i("bitbit 1",""+ result);
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(src, 0, 0, null);

        Log.i("canvas",""+ canvas);

        Paint paint = new Paint();
        paint.setColor(color);
        paint.setTextSize(text_size);
        paint.setTypeface(Typeface.SERIF);
        paint.setAntiAlias(false);
        paint.setUnderlineText(underline);

        //should draw background first,order is important
        float left = 0;
        float right = w;
        float bottom = h;
        float top = (float) (bottom-(h*.2));//TAMAÑO DEL CONTENEDOR DE INFO
        float limite = (float) (w-(w/2.5));

        /*if (w>h) {
            top = (float) (bottom-(h*.2));//TAMAÑO DEL CONTENEDOR DE INFO
        } else {
            top = (float) (bottom-(h*.28));//TAMAÑO DEL CONTENEDOR DE INFO
        }*/

        /*char[] chars = direccion.toCharArray();
        paint.breakText(chars, 0, chars.length, w-400, null);*/

        char[] chars = direccion.toCharArray();
        int nextPos = paint.breakText(chars, 0, chars.length, limite , null);
        String direccion1 = direccion.substring(0, nextPos);

        canvas.drawRect(left, top,right,bottom,bgPaint);
        //canvas.drawText(tipo, 10, h-(text_size*8), paint);
        canvas.drawText(local, 10, h-(text_size*8), paint);
        //canvas.drawText(ciudad, 10, h-(size*4), paint);
        canvas.drawText(fecha, 10, h-text_size*7, paint);
        canvas.drawText(categoria, 10, h-(text_size*6), paint);
        canvas.drawText("Coordenadas:", 10, h-(text_size*5), paint);
        canvas.drawText(coordenadas, 10, h-(text_size*4), paint);
        canvas.drawText("Direccion:", 10, h-(text_size*3), paint);
        canvas.drawText(direccion1, 10, h-(text_size*2), paint);

        if(chars.length > nextPos){
            String direccion2 = direccion.substring(nextPos,chars.length);
            canvas.drawText(direccion2, 10, h-(text_size), paint);
        }


        String lat = String.valueOf(latitud);
        String lon = String.valueOf(longitud);

        String tamano = "400";
        String imageUrl22 = "https://maps.googleapis.com/maps/api/staticmap?"+
                "center="+ lat +","+ lon +
                "&zoom=19"+
                "&size="+tamano+"x"+tamano+
                "&scale=2"+
                "&markers=size:big|color:red|"+ lat +","+ lon +
                "&key=AIzaSyD4PXR4R1B8xlEwqKMOID8CLtMDkXJpPGE";

        Log.i("url mapstatic", imageUrl22);




        Picasso.get().load(imageUrl22).resize((int) (w/(2.5)), (int) (h/3.45)).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                //imageView.setImageBitmap(bitmap);
                canvas.drawBitmap(bitmap,limite,h-(text_size*13),null);
                Log.i("onBitmapLoaded", bitmap + " cv: "+canvas);

                // Llama a la devolución de llamada cuando el bitmap esté listo
                callback.onBitmapReady(result);
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                Log.i("onBitmapFailed", "entro");
                Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                canvas.drawBitmap(bitmap,limite,h-(text_size*13),null);
                callback.onBitmapReady(result);
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                Log.i("onPrepareLoad", "entro");
            }
        });



        /*Picasso.get().load(imageUrl22).resize(400, 400).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream .toByteArray();

                String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
                Log.i("Encodec", encoded);
                parseBase64(encoded);

        Picasso.get().load(imageUrl22).resize((int) (w/(2.5)), h/3).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                imageView.setImageBitmap(bitmap);
                canvas.drawBitmap(bitmap,limite,h-(text_size*13),null);
                Log.i("onBitmapLoaded", bitmap + " cv: "+canvas);

                // Llama a la devolución de llamada cuando el bitmap esté listo
                callback.onBitmapReady(result);
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });*/

    }


    public static String parseBase64(String base64) {

        try {
            Pattern pattern = Pattern.compile("((?<=base64,).*\\s*)",Pattern.DOTALL|Pattern.MULTILINE);
            Matcher matcher = pattern.matcher(base64);
            if (matcher.find()) {
                return matcher.group().toString();
            } else {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return "";
    }


    public Bitmap mark3(Bitmap src,String ciudad, String local,String categoria, String fecha, String coordenadas, String direccion, int color, int alpha, int size, boolean underline, ImageView imageView) {
        //dimensiones de la foto
        int w = src.getWidth();
        int h = src.getHeight();
        int text_size = h/45;

        Paint bgPaint=new Paint();
        bgPaint.setColor(Color.parseColor("#80000000"));  //transparent black,change opacity by changing hex value "AA" between "00" and "FF"
        Bitmap result = Bitmap.createBitmap(w, h, src.getConfig());
        Log.i("bitbit 1",""+ result);
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(src, 0, 0, null);

        Log.i("canvas",""+ canvas);

        Paint paint = new Paint();
        paint.setColor(color);
        paint.setTextSize(text_size);
        paint.setTypeface(Typeface.SERIF);
        paint.setAntiAlias(false);
        paint.setUnderlineText(underline);

        //should draw background first,order is important
        float left = 0;
        float right = w;
        float bottom = h;
        float top = (float) (bottom-(h*.2));//TAMAÑO DEL CONTENEDOR DE INFO
        float limite = (float) (w-(w/2.5));

        /*if (w>h) {
            top = (float) (bottom-(h*.2));//TAMAÑO DEL CONTENEDOR DE INFO
        } else {
            top = (float) (bottom-(h*.28));//TAMAÑO DEL CONTENEDOR DE INFO
        }*/

        /*char[] chars = direccion.toCharArray();
        paint.breakText(chars, 0, chars.length, w-400, null);*/

        /*char[] chars = direccion.toCharArray();
        int nextPos = paint.breakText(chars, 0, chars.length, limite , null);
        String direccion1 = direccion.substring(0, nextPos);*/

        /*canvas.drawRect(left, top,right,bottom,bgPaint);
        //canvas.drawText(tipo, 10, h-(text_size*8), paint);
        canvas.drawText(local, 10, h-(text_size*8), paint);
        //canvas.drawText(ciudad, 10, h-(size*4), paint);
        canvas.drawText(fecha, 10, h-text_size*7, paint);
        canvas.drawText(categoria, 10, h-(text_size*6), paint);
        canvas.drawText("Coordenadas:", 10, h-(text_size*5), paint);
        canvas.drawText(coordenadas, 10, h-(text_size*4), paint);
        canvas.drawText("Direccion:", 10, h-(text_size*3), paint);
        canvas.drawText(direccion1, 10, h-(text_size*2), paint);*/

        Bitmap bitmap = imageViewToBitmap(imageView);
        canvas.drawBitmap(bitmap,limite,h-(text_size*13),null);

        //canvas.drawText(user, 10, h-(size*2), paint);

        return result;
    }


    public static Bitmap imageViewToBitmap(ImageView imageView) {
        Log.i("imageViewToBitmap","imageView: "+ imageView);
        // Obtén el drawable del ImageView
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();

        Log.i("imageViewToBitmap","drawable: "+ drawable);
        if (drawable != null) {
            Log.i("imageViewToBitmap","drawable is not null "+ drawable.getBitmap());
            // Convierte el drawable a Bitmap y devuélvelo
            return drawable.getBitmap();
        } else {
            Log.i("imageViewToBitmap","drawable is null ");
            // En caso de que el ImageView no tenga un drawable válido, devuelve null
            return null;
        }
    }


}


