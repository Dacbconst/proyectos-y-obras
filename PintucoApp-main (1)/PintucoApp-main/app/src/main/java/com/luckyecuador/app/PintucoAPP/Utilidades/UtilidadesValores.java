package com.luckyecuador.app.PintucoAPP.Utilidades;

import android.database.Cursor;
import android.os.Build;
import android.util.Log;

import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertValores;

import org.json.JSONException;
import org.json.JSONObject;

public class UtilidadesValores {

    public static final int COLUMNA_PHARMA_ID = 2;
    public static final int COLUMNA_CODIGO = 3;
    public static final int COLUMNA_USUARIO = 4;
    public static final int COLUMNA_SUPERVISOR = 5;
    public static final int COLUMNA_FECHA = 6;
    public static final int COLUMNA_HORA = 7;
    public static final int COLUMNA_SECTOR = 8;
    public static final int COLUMNA_CATEGORIA = 9;
    public static final int COLUMNA_SEGMENTO1 = 10;
    //public static final int COLUMNA_POFERTA = 11;
    public static final int COLUMNA_BRAND = 11;
    /*public static final int COLUMNA_TAMANO = 13;
    public static final int COLUMNA_CANTIDAD = 14;*/
    public static final int COLUMNA_SKU_CODE = 12;
    public static final int COLUMNA_CODIFICA = 13;
    public static final int COLUMNA_AUSENCIA = 14;
    public static final int COLUMNA_DISPONIBLE = 15;
    public static final int COLUMNA_RESPONSABLE = 16;
    public static final int COLUMNA_RAZONES = 17;
    public static final int COLUMNA_SUGERIDO = 18;
    public static final int COLUMNA_TIPO_SUGERIDO = 19;
    public static final int COLUMNA_PVP = 20;
    public static final int COLUMNA_PVC = 21;
    public static final int COLUMNA_POFERTA = 22;
    public static final int COLUMNA_MANUFACTURER = 23;
    public static final int COLUMNA_QUIEBRE_PERCHA = 24;
    public static final int COLUMNA_QUIEBRE_BODEGA = 25;
    public static final int COLUMNA_POS_NAME = 26;
    public static final int COLUMNA_PLATAFORMA = 27;

    /**
     * Determina si la aplicación corre en versiones superiores o iguales
     * a Android LOLLIPOP
     *
     * @return booleano de confirmación
     */
    public static boolean materialDesign() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    /**
     * Copia los datos de un gasto almacenados en un cursor hacia un
     * JSONObject
     *
     * @param c cursor
     * @return objeto jason
     */
    public static JSONObject deCursorAJSONObject(Cursor c) {
        JSONObject jObject = new JSONObject();

        String pharma_id;
        String codigo;
        String usuario;
        String supervisor;
        String fecha;
        String hora;
        String sector;
        String categoria;
        String segment1;
        String segment2;
        String brand;
        String tamano;
        String cantidad;
        String sku_code;
        String codifica;
        String ausencia;
        String disponible;
        String responsable;
        String razones;
        String sugerido;
        String tipo_sugerido;
        String pvp;
        String pvc;
        String poferta;
        String manufacturer;
        String quiebre_percha;
        String quiebre_bodega;
        String pos_name;
        String plataforma;

        pharma_id = c.getString(COLUMNA_PHARMA_ID);
        codigo = c.getString(COLUMNA_CODIGO);
        usuario = c.getString(COLUMNA_USUARIO);
        supervisor = c.getString(COLUMNA_SUPERVISOR);
        fecha = c.getString(COLUMNA_FECHA);
        hora = c.getString(COLUMNA_HORA);
        sector = c.getString(COLUMNA_SECTOR);
        categoria = c.getString(COLUMNA_CATEGORIA);
        segment1 = c.getString(COLUMNA_SEGMENTO1);
        //segment2 = c.getString(COLUMNA_POFERTA);
        brand = c.getString(COLUMNA_BRAND);
        //tamano = c.getString(COLUMNA_TAMANO);
        //cantidad = c.getString(COLUMNA_CANTIDAD);
        sku_code = c.getString(COLUMNA_SKU_CODE);
        codifica = c.getString(COLUMNA_CODIFICA);
        ausencia = c.getString(COLUMNA_AUSENCIA);
        disponible = c.getString(COLUMNA_DISPONIBLE);
        responsable = c.getString(COLUMNA_RESPONSABLE);
        razones = c.getString(COLUMNA_RAZONES);
        sugerido = c.getString(COLUMNA_SUGERIDO);
        tipo_sugerido = c.getString(COLUMNA_TIPO_SUGERIDO);
        pvp = c.getString(COLUMNA_PVP);
        pvc = c.getString(COLUMNA_PVC);
        poferta = c.getString(COLUMNA_POFERTA);
        manufacturer = c.getString(COLUMNA_MANUFACTURER);
        quiebre_percha = c.getString(COLUMNA_QUIEBRE_PERCHA);
        quiebre_bodega = c.getString(COLUMNA_QUIEBRE_BODEGA);
        pos_name = c.getString(COLUMNA_POS_NAME);
        plataforma = c.getString(COLUMNA_PLATAFORMA);

        try {
            jObject.put(ContractInsertValores.Columnas.PHARMA_ID, pharma_id);
            jObject.put(ContractInsertValores.Columnas.CODIGO, codigo);
            jObject.put(ContractInsertValores.Columnas.USUARIO, usuario);
            jObject.put(ContractInsertValores.Columnas.SUPERVISOR, supervisor);
            jObject.put(ContractInsertValores.Columnas.FECHA, fecha);
            jObject.put(ContractInsertValores.Columnas.HORA, hora);
            jObject.put(ContractInsertValores.Columnas.SECTOR, sector);
            jObject.put(ContractInsertValores.Columnas.CATEGORIA, categoria);
            jObject.put(ContractInsertValores.Columnas.SEGMENTO1, segment1);
            //jObject.put(ContractInsertValores.Columnas.POFERTA, segment2);
            jObject.put(ContractInsertValores.Columnas.BRAND, brand);
            /*jObject.put(ContractInsertValores.Columnas.TAMANO, tamano);
            jObject.put(ContractInsertValores.Columnas.CANTIDAD, cantidad);*/
            jObject.put(ContractInsertValores.Columnas.SKU_CODE, sku_code);
            jObject.put(ContractInsertValores.Columnas.CODIFICA, codifica);
            jObject.put(ContractInsertValores.Columnas.AUSENCIA, ausencia);
            jObject.put(ContractInsertValores.Columnas.DISPONIBLE, disponible);
            jObject.put(ContractInsertValores.Columnas.RESPONSABLE, responsable);
            jObject.put(ContractInsertValores.Columnas.RAZONES, razones);
            jObject.put(ContractInsertValores.Columnas.SUGERIDO, sugerido);
            jObject.put(ContractInsertValores.Columnas.TIPO_SUGERIDO, tipo_sugerido);
            jObject.put(ContractInsertValores.Columnas.PVP, pvp);
            jObject.put(ContractInsertValores.Columnas.PVC, pvc);
            jObject.put(ContractInsertValores.Columnas.POFERTA, poferta);
            jObject.put(ContractInsertValores.Columnas.MANUFACTURER, manufacturer);
            jObject.put(ContractInsertValores.Columnas.QUIEBRE_PERCHA, quiebre_percha);
            jObject.put(ContractInsertValores.Columnas.QUIEBRE_BODEGA, quiebre_bodega);
            jObject.put(ContractInsertValores.Columnas.POS_NAME, pos_name);
            jObject.put(ContractInsertValores.Columnas.PLATAFORMA, plataforma);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("Cursor a JSONObject-", String.valueOf(jObject));

        return jObject;
    }

}
