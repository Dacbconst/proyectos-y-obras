package com.luckyecuador.app.PintucoAPP;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.common.util.concurrent.ListenableFuture;
import com.luckyecuador.app.PintucoAPP.Adaptadores.PostRecyclerAdapter;
import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;
import com.luckyecuador.app.PintucoAPP.ui.almuerzo.AlmuerzoFragment;
import com.luckyecuador.app.PintucoAPP.ui.canjes.CanjesFragment;
import com.luckyecuador.app.PintucoAPP.ui.evidencias.EvidenciasFragment;
import com.luckyecuador.app.PintucoAPP.ui.exhibiciones.ExhibicionFragment;
import com.luckyecuador.app.PintucoAPP.ui.logistico.preforma.LogisticoPreformaFragment;
import com.luckyecuador.app.PintucoAPP.ui.productos_mal_estado.ProductosMalEstadoFragment;
import com.luckyecuador.app.PintucoAPP.ui.promociones.PromoFragment;
import com.luckyecuador.app.PintucoAPP.ui.ventas.VentasFragment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

public class CameraActivity extends AppCompatActivity{

    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;

    int cameraSelectors = CameraSelector.LENS_FACING_BACK;

    PreviewView previewView;
    private ImageCapture imageCapture;
    private Button bCapture;
    OrientationEventListener oel;
    FloatingActionButton fab;
    FloatingActionButton fab2;
    View overlayRotar;
    ImageView ivRotar;
    Animation rotateAnim;
    boolean avisoRotarVisible = false;
    boolean sensorOrientacionDisponible = false;

    private String activity = null;
    private String tipo = null;
    private  String USUARIO;
    private  String FILE_NAME;
    private  String fechaser;
    private  String horaser;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        activity = getIntent().getStringExtra("activity");
        tipo = getIntent().getStringExtra("tipo");

        previewView = findViewById(R.id.previewView);
        //bCapture = findViewById(R.id.bCapture);
        overlayRotar = findViewById(R.id.overlayRotar);
        ivRotar = findViewById(R.id.ivRotar);

        rotateAnim = new RotateAnimation(-25f, 25f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnim.setDuration(600);
        rotateAnim.setRepeatCount(Animation.INFINITE);
        rotateAnim.setRepeatMode(Animation.REVERSE);
        rotateAnim.setInterpolator(new AccelerateDecelerateInterpolator());

        context = this;

        fab = findViewById(R.id.fab_camera);
        fab2 = findViewById(R.id.fab_switch_camera);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                capturePhoto();
            }
        });
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cameraSelectors == CameraSelector.LENS_FACING_BACK) {
                    cameraSelectors = CameraSelector.LENS_FACING_FRONT;
                } else {
                    cameraSelectors = CameraSelector.LENS_FACING_BACK;
                }
                openCamera();
            }
        });
        //bCapture.setOnClickListener(this);
        if (activity.equals("asistencia")){
            fab2.setVisibility(View.GONE);
            cameraSelectors = CameraSelector.LENS_FACING_FRONT;
        }
        openCamera();

    }

    private void mostrarAvisoRotar() {
        // Si el sensor de orientación no está disponible (hardware viejo/defectuoso),
        // nunca bloqueamos: es preferible avisar sin frenar a dejar al usuario sin poder fotografiar.
        if (!sensorOrientacionDisponible || avisoRotarVisible) return;
        avisoRotarVisible = true;
        overlayRotar.setVisibility(View.VISIBLE);
        ivRotar.startAnimation(rotateAnim);
        fab.setEnabled(false);
        fab.setAlpha(0.4f);
    }

    private void ocultarAvisoRotar() {
        // El "release" nunca se condiciona a nada: cualquier lectura ambigua o de portrait
        // siempre libera el bloqueo, para que jamás quede el botón trabado.
        if (!avisoRotarVisible) return;
        avisoRotarVisible = false;
        overlayRotar.setVisibility(View.GONE);
        ivRotar.clearAnimation();
        fab.setEnabled(true);
        fab.setAlpha(1f);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (oel != null) {
            oel.disable();
        }
    }

    private void openCamera(){
        cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                startCameraX(cameraProvider);
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }, getExecutor());
    }

    Executor getExecutor() {
        return ContextCompat.getMainExecutor(this);
    }

    @SuppressLint("RestrictedApi")
    private void startCameraX(ProcessCameraProvider cameraProvider) {
        cameraProvider.unbindAll();
        CameraSelector cameraSelector = new CameraSelector.Builder()
                //.requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .requireLensFacing(cameraSelectors)
                .build();
        Preview preview = new Preview.Builder()
                .build();
        preview.setSurfaceProvider(previewView.getSurfaceProvider());

        // Image capture use case
        imageCapture = new ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .build();
        if (oel != null) {
            oel.disable();
        }
        oel = new OrientationEventListener(getApplicationContext(),SensorManager.SENSOR_DELAY_NORMAL) {
            @Override
            public void onOrientationChanged(int i) {
                if (i >= 225 && i < 315) {
                    imageCapture.setTargetRotation(Surface.ROTATION_90);
                    // Desactivado a pedido: aviso de rotar + bloqueo del botón. Descomentar para reactivar.
                    // mostrarAvisoRotar();
                } else if(i >= 135 && i < 225) {
                    imageCapture.setTargetRotation(Surface.ROTATION_180);
                    // ocultarAvisoRotar();
                } else if (i >= 45 && i < 135) {
                    imageCapture.setTargetRotation(Surface.ROTATION_270);
                    // mostrarAvisoRotar();
                } else {
                    imageCapture.setTargetRotation(Surface.ROTATION_0);
                    // ocultarAvisoRotar();
                }
            }
        };
        oel.enable();
        sensorOrientacionDisponible = oel.canDetectOrientation();

        // Image analysis use case
        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build();

        //imageAnalysis.setAnalyzer(getExecutor(), this);

        //bind to lifecycle:
        cameraProvider.bindToLifecycle((LifecycleOwner) this, cameraSelector, preview, imageCapture);
    }

    private void capturePhoto() {
        long timestamp = System.currentTimeMillis();

        // Para "asistencia" y "marcacion" (entrada/salida) la foto cruda nunca se hace
        // pública en la galería: se guarda en un archivo privado de caché (se sobreescribe
        // en cada toma), así jamás queda un duplicado suelto junto a la versión con marca de agua.
        boolean esCapturaPrivada = activity.equals("asistencia") || activity.equals("marcacion");
        File archivoTemporalAsistencia = esCapturaPrivada ? new File(getCacheDir(), activity + "_temp.jpg") : null;

        ImageCapture.OutputFileOptions outputFileOptions;
        if (esCapturaPrivada) {
            outputFileOptions = new ImageCapture.OutputFileOptions.Builder(archivoTemporalAsistencia).build();
        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, timestamp);
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");
            //contentValues.put(MediaStore.MediaColumns.WIDTH,1440);
            //contentValues.put(MediaStore.MediaColumns.HEIGHT,1920);
            //imageCapture.setTargetRotation(Surface.ROTATION_90);
            outputFileOptions = new ImageCapture.OutputFileOptions.Builder(
                    getContentResolver(),
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    contentValues
            ).build();
        }

        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.sin_conexion);

        imageCapture.takePicture(
                outputFileOptions,
                getExecutor(),
                new ImageCapture.OnImageSavedCallback() {
                    @Override
                    public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                        // Para archivos (captura privada) getSavedUri() siempre es null; la armamos a mano.
                        Uri savedUri = esCapturaPrivada ? Uri.fromFile(archivoTemporalAsistencia) : outputFileResults.getSavedUri();

                        // Primero, verifica si la URI es nula. Si lo es, algo salió mal y debemos cancelar.
                        if (savedUri == null) {
                            setResult(RESULT_CANCELED);
                            Toast.makeText(getBaseContext(), "Error: No se pudo obtener la URI de la imagen guardada.", Toast.LENGTH_SHORT).show();
                            finish();
                            return; // Sal de el mEtodo si el URI es nulo
                        }

                        // Handle each activity type
                        if (activity.equals("menu")) {
                            MenuNavigationActivity.imageView.setImageURI(savedUri);
                            setResult(RESULT_OK); // Assuming you want a RESULT_OK for this case too
                        } else if (activity.equals("marcacion")) {
                            // Foto de marcación de entrada/salida (con causales/justificación).
                            MenuNavigationActivity.imageView.setImageURI(savedUri);
                            setResult(RESULT_OK);
                        } else if (activity.equals("nuevo_pdv")) {
                            ImplementacionActivity.ivFoto.setImageURI(savedUri);
                            setResult(RESULT_OK);
                        } else if (activity.equals("promo")) {
                            PromoFragment.imageView.setImageURI(savedUri);
                            setResult(RESULT_OK);
                        } else if (activity.equals("almuerzo")) {
                            AlmuerzoFragment.iv_foto.setImageURI(savedUri);
                            String foto_uri = savedUri.toString();
                            SharedPreferences sharedPref = getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString(Constantes.FOTO_ALMUERZO, foto_uri);
                           // editor.apply(); // Use apply() for async saving, commit() for synchronous
                          //  setResult(RESULT_OK);
                        } else if (activity.equals("ventas")) {
                            VentasFragment.imageView.setImageURI(savedUri);
                            //setResult(RESULT_OK);
                        } else if (activity.equals("foto_pdv")) {
                            // Here you're calling a method that likely handles the image directly
                            // Ensure 'drawable' is accessible here if needed.
                            FotograficoPdvActivity.scaleImageUri(savedUri, drawable);
                           // setResult(RESULT_OK);
                        } else if (activity.equals("exh")) {
                            ExhibicionFragment.imageView.setImageURI(savedUri);
                            //setResult(RESULT_OK);
                        } else if (activity.equals("asistencia")) {
                            AsistenciaActivity.img.setImageURI(savedUri);
                            AsistenciaActivity.valor = false; // Be careful with static variables like this
                           // setResult(RESULT_OK);
                        } else if (activity.equals("canjes")) {
                            CanjesFragment.imageView.setImageURI(savedUri);
                           // setResult(RESULT_OK);
                        } else if (activity.equals("propensos")) {
                            // This is the correct way to return the URI to PropensosFragment
                            Intent resultIntent = new Intent();
                            resultIntent.setData(savedUri);
                            setResult(RESULT_OK, resultIntent); // Set RESULT_OK with the URI
                        } else if (activity.equals("proforma")) {
                            // ProformaFragment vive dentro de un RecyclerView (no hay un ImageView
                            // estático al que apuntar), así que devolvemos la URI igual que "propensos".
                            Intent resultIntent = new Intent();
                            resultIntent.setData(savedUri);
                            setResult(RESULT_OK, resultIntent);
                        } else if (activity.equals("productos_mal_estado")) {
                            ProductosMalEstadoFragment.imageView.setImageURI(savedUri);
                            ProductosMalEstadoFragment.lblEstadoFoto.setText("FOTO OK");
                            ProductosMalEstadoFragment.lblEstadoFoto.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
                            //setResult(RESULT_OK);
                        } else if (activity.equals("justificar")) {
                            PostRecyclerAdapter.imageView.setImageURI(savedUri);
                           // setResult(RESULT_OK);
                        } else if (activity.equals("justificar_salida")) {
                            MenuNavigationActivity.imageViewJust.setImageURI(savedUri);
                           // setResult(RESULT_OK);
                        } else if (activity.equals("logis_prefor")) {
                            LogisticoPreformaFragment.imageView.setImageURI(savedUri);
                            //setResult(RESULT_OK);
                        } else if (activity.equals("evidencias")) {
                            if (tipo.equalsIgnoreCase("antes")) {
                                EvidenciasFragment.imageViewAntes.setImageURI(savedUri);
                            } else if (tipo.equalsIgnoreCase("despues")) {
                                EvidenciasFragment.imageViewDespues.setImageURI(savedUri);
                            }
                           setResult(RESULT_OK);
                        } else {
                             //If none of the above activities match, it's an unhandled case or an error
                            setResult(RESULT_CANCELED);
                           Toast.makeText(getBaseContext(), "Actividad no reconocida o no manejada para la imagen.", Toast.LENGTH_SHORT).show();
                        }

                        // Finally, finish the activity after all logic is executed and result is set
                        finish();
                    }

                    @Override
                    public void onError(@NonNull ImageCaptureException exception) {
                        Toast.makeText(CameraActivity.this, "Error saving photo: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

        );


    }

    public static String getStringImage(Bitmap bmp){
        String encodedImage;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //Comprime la Imagen tipo, calidad y outputstream
        bmp.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] imageBytes = baos.toByteArray();
        encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    public void guardarFotoEnGaleria(Bitmap scaled){
        //String user2 = user.replace(" ","_");
        USUARIO = "FOTOGRAFICO_"+ "user2" + "_" + fechaser + "_" + horaser;
        FILE_NAME = USUARIO + ".jpeg";

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DISPLAY_NAME, FILE_NAME);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            values.put(MediaStore.MediaColumns.RELATIVE_PATH, "Fotografico/");
            //values.put(MediaStore.MediaColumns.IS_PENDING, 1);
        } else {
            File directory = Environment.getExternalStoragePublicDirectory("Fotografico");
            File file = new File(directory, FILE_NAME);
            values.put(MediaStore.MediaColumns.DATA, file.getAbsolutePath());
        }

        Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        try(OutputStream output = getContentResolver().openOutputStream(uri)) {
            scaled.compress(Bitmap.CompressFormat.JPEG, 100,output);
            Toast.makeText(getApplicationContext(),"Se Guardó en Galería: Fotografico",Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public class ImageLoader {

        private Context context;

        public ImageLoader(Context context) {
            this.context = context;
        }

        public void loadImageFromUrl(String imageUrl, ImageView imageView) {
            Glide.with(context)
                    .load(imageUrl)
                    .into(imageView);
        }
    }

}