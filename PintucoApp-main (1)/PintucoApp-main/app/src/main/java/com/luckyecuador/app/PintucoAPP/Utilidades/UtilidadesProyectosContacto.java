package com.luckyecuador.app.PintucoAPP.Utilidades;

import android.database.Cursor;
import android.util.Log;

import com.luckyecuador.app.PintucoAPP.Contracts.ContractInsertProyectosContacto;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class UtilidadesProyectosContacto {

    // Indices para las columnas indicadas en la proyección
    public static final int COLUMNA_CODIGO_PDV = 2;
    public static final int COLUMNA_PDV = 3;
    public static final int COLUMNA_USUARIO = 4;
    public static final int COLUMNA_FECHA = 5;
    public static final int COLUMNA_CONTACTO = 6;
    public static final int COLUMNA_EMPRESA = 7;
    public static final int COLUMNA_MAIL = 8;
    public static final int COLUMNA_DIRECCION = 9;
    public static final int COLUMNA_TELEFONO = 10;
    public static final int COLUMNA_FECHA_AGENDAMIENTO = 11;
    public static final int COLUMNA_TITULO = 12;
    public static final int COLUMNA_HORA = 13;
    public static final int COLUMNA_LUGAR = 14;
    public static final int COLUMNA_LATITUD = 15;
    public static final int COLUMNA_LONGITUD = 16;
    public static final int COLUMNA_TECNICO = 17;
    public static final int COLUMNA_ESTADO_AGENDA = 18;
    public static final int COLUMNA_ACTIVAR = 19;
    public static final int COLUMNA_TELEFONO_CONVENCIONAL = 20;
    public static final int COLUMNA_REAGENDADO = 21;
    public static final int COLUMNA_CIUDAD_PDV = 22;
    public static final int COLUMNA_NO_REQUIERE_VISITA = 23;

    /**
     * Copia los datos de un registro de Contacto almacenados en un cursor hacia un
     * JSONObject
     *
     * @param c cursor
     * @return objeto json
     */
    public static JSONObject deCursorAJSONObject(Cursor c) {
        JSONObject jObject = new JSONObject();

        String codigoPdv;
        String pdv;
        String usuario;
        String fecha;
        String contacto;
        String empresa;
        String mail;
        String direccion;
        String latitud;
        String longitud;
        String telefono;
        String telefonoConvencional;
        String fechaAgendamiento;
        String titulo;
        String activar;
        String hora;
        String ciudadPdv;
        String noRequiereVisita;

        codigoPdv = c.getString(COLUMNA_CODIGO_PDV);
        pdv = c.getString(COLUMNA_PDV);
        usuario = c.getString(COLUMNA_USUARIO);
        fecha = c.getString(COLUMNA_FECHA);
        contacto = c.getString(COLUMNA_CONTACTO);
        empresa = c.getString(COLUMNA_EMPRESA);
        mail = c.getString(COLUMNA_MAIL);
        direccion = c.getString(COLUMNA_DIRECCION);
        latitud = c.getString(COLUMNA_LATITUD);
        longitud = c.getString(COLUMNA_LONGITUD);
        telefono = c.getString(COLUMNA_TELEFONO);
        telefonoConvencional = c.getString(COLUMNA_TELEFONO_CONVENCIONAL);
        fechaAgendamiento = c.getString(COLUMNA_FECHA_AGENDAMIENTO);
        titulo = c.getString(COLUMNA_TITULO);
        activar = c.getString(COLUMNA_ACTIVAR);
        hora = c.getString(COLUMNA_HORA);
        ciudadPdv = c.getString(COLUMNA_CIUDAD_PDV);
        noRequiereVisita = c.getString(COLUMNA_NO_REQUIERE_VISITA);

        try {
            jObject.put(ContractInsertProyectosContacto.Columnas.CODIGO_PDV, codigoPdv);
            jObject.put(ContractInsertProyectosContacto.Columnas.PDV, pdv);
            jObject.put(ContractInsertProyectosContacto.Columnas.USUARIO, usuario);
            jObject.put(ContractInsertProyectosContacto.Columnas.FECHA, aFechaSql(fecha));
            jObject.put(ContractInsertProyectosContacto.Columnas.CONTACTO, contacto);
            jObject.put(ContractInsertProyectosContacto.Columnas.EMPRESA, empresa);
            jObject.put(ContractInsertProyectosContacto.Columnas.MAIL, mail);
            jObject.put(ContractInsertProyectosContacto.Columnas.DIRECCION, direccion);
            jObject.put(ContractInsertProyectosContacto.Columnas.LATITUD, latitud);
            jObject.put(ContractInsertProyectosContacto.Columnas.LONGITUD, longitud);
            jObject.put(ContractInsertProyectosContacto.Columnas.TELEFONO, telefono);
            jObject.put(ContractInsertProyectosContacto.Columnas.TELEFONO_CONVENCIONAL, telefonoConvencional);
            jObject.put(ContractInsertProyectosContacto.Columnas.FECHA_AGENDAMIENTO, aFechaSql(fechaAgendamiento));
            jObject.put(ContractInsertProyectosContacto.Columnas.TITULO, titulo);
            // NOTA: el servidor (insert_contacto.php, fuera de este repo) hoy solo sabe
            // INSERTAR filas nuevas — no recibe id_remota en este payload, así que no
            // tiene forma de saber a qué fila existente aplicarle este activar="NO".
            // Se manda igual porque es aditivo (no rompe nada si el backend lo ignora),
            // pero para que el borrado se refleje server-side hace falta un cambio en
            // ese endpoint (ver detalle en la conversación/plan).
            jObject.put(ContractInsertProyectosContacto.Columnas.ACTIVAR, activar != null ? activar : "SI");
            // NOTA: igual que "activar" arriba, insert_contacto.php (fuera de este repo)
            // todavía no tiene esta columna — se manda igual porque es aditivo (no
            // rompe nada si el backend la ignora). Hace falta agregar la columna
            // no_requiere_visita en MySQL para que el flag sobreviva a un sync/reinstalo
            // y para que el analista la vea desde la web.
            jObject.put(ContractInsertProyectosContacto.Columnas.NO_REQUIERE_VISITA, noRequiereVisita != null ? noRequiereVisita : "NO");
            // El promotor puede sugerir la hora al agendar; el analista la deja o la
            // cambia desde la web. Se sube solo si hay algo escrito (columna TIME en
            // el servidor: hay que convertir de "hh:mm a" a "HH:mm:ss").
            if (hora != null && !hora.isEmpty()) {
                jObject.put(ContractInsertProyectosContacto.Columnas.HORA, aHoraSql(hora));
            }
            // Ciudad del PDV elegido en "Seleccione PDV", capturada al registrar —
            // ver nota en ContractInsertProyectosContacto.Columnas.CIUDAD_PDV.
            if (ciudadPdv != null && !ciudadPdv.isEmpty()) {
                jObject.put(ContractInsertProyectosContacto.Columnas.CIUDAD_PDV, ciudadPdv);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("Cursor a JSONObject-", String.valueOf(jObject));

        return jObject;
    }

    /**
     * Convierte una fecha en formato dd/MM/yyyy (usado en pantalla) al formato
     * yyyy-MM-dd que espera la columna DATE en el servidor.
     */
    private static String aFechaSql(String fechaDDMMYYYY) {
        try {
            SimpleDateFormat formatoOrigen = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Date fecha = formatoOrigen.parse(fechaDDMMYYYY);
            SimpleDateFormat formatoDestino = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            return formatoDestino.format(fecha);
        } catch (ParseException | NullPointerException e) {
            return fechaDDMMYYYY;
        }
    }

    /**
     * Inverso de {@link #aFechaSql}: convierte la fecha que devuelve el servidor
     * (yyyy-MM-dd) al formato dd/MM/yyyy que usa la pantalla y que espera
     * AgendaFragment para poder ubicar el evento en el calendario. Sin esto, la
     * fecha que baja del servidor queda en yyyy-MM-dd y AgendaFragment nunca la
     * reconoce, así que el agendamiento desaparece de la vista de Agenda.
     */
    public static String deFechaSql(String fechaYYYYMMDD) {
        // "0000-00-00" es el default típico de una columna DATE NOT NULL en MySQL
        // cuando nunca se agendó (ej. contactos "no requiere visita"). SimpleDateFormat
        // es lenient por defecto y NO lanza ParseException con mes/día "00" — en vez
        // de eso "rueda" hacia atrás y calcula una fecha real inválida (30/11/0002),
        // así que hay que descartar este caso ANTES de intentar parsear.
        if (fechaYYYYMMDD == null || fechaYYYYMMDD.trim().isEmpty() || "0000-00-00".equals(fechaYYYYMMDD.trim())) {
            return "";
        }
        try {
            SimpleDateFormat formatoOrigen = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date fecha = formatoOrigen.parse(fechaYYYYMMDD);
            SimpleDateFormat formatoDestino = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            return formatoDestino.format(fecha);
        } catch (ParseException | NullPointerException e) {
            return fechaYYYYMMDD;
        }
    }

    /**
     * Convierte la hora en formato hh:mm a (con AM/PM, usada en pantalla) al
     * formato HH:mm:ss que espera la columna TIME en el servidor.
     */
    private static String aHoraSql(String hora12h) {
        try {
            SimpleDateFormat formatoOrigen = new SimpleDateFormat("hh:mm a", Locale.getDefault());
            Date hora = formatoOrigen.parse(hora12h);
            SimpleDateFormat formatoDestino = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
            return formatoDestino.format(hora);
        } catch (ParseException | NullPointerException e) {
            return hora12h;
        }
    }

    /**
     * Inverso de {@link #aHoraSql}: convierte la hora que devuelve el servidor
     * (HH:mm:ss) al formato hh:mm a (con AM/PM) que usa la pantalla — para que la
     * hora quede en el mismo formato sin importar si la puso el promotor desde la
     * app o el analista desde la web.
     */
    public static String deHoraSql(String hora24h) {
        try {
            SimpleDateFormat formatoOrigen = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
            Date hora = formatoOrigen.parse(hora24h);
            SimpleDateFormat formatoDestino = new SimpleDateFormat("hh:mm a", Locale.getDefault());
            return formatoDestino.format(hora);
        } catch (ParseException | NullPointerException e) {
            return hora24h;
        }
    }

    /**
     * Reemplazo seguro de {@code JSONObject.optString(key)} para columnas que sí
     * pueden venir NULL del servidor (titulo, hora, lugar, latitud, longitud,
     * tecnico, telefono_convencional, etc.). {@code optString} de Android trata el
     * JSON null como el objeto JSONObject.NULL (no como null de Java), y termina
     * devolviendo literalmente el texto "null" en vez de vacío — eso es lo que se
     * veía escrito como "null" en las cards de Agenda. {@code JSONObject.isNull}
     * sí detecta ese caso correctamente.
     */
    public static String optStringSeguro(JSONObject json, String key) {
        return optStringSeguro(json, key, "");
    }

    public static String optStringSeguro(JSONObject json, String key, String valorPorDefecto) {
        if (json == null || !json.has(key) || json.isNull(key)) {
            return valorPorDefecto;
        }
        return json.optString(key, valorPorDefecto);
    }

}
