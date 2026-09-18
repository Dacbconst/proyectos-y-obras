package com.luckyecuador.app.PintucoAPP.Contracts;

import android.content.UriMatcher;
import android.net.Uri;
import android.provider.BaseColumns;

import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;

public class ContractInsertProyectosContacto {
    /**
     * Representación de la tabla a consultar
     */
    public static final String INSERT_PROYECTOS_CONTACTO = "insert_proyectos_contacto";
    /**
     * Tipo MIME que retorna la consulta de una sola fila
     */
    public final static String SINGLE_MIME =
            "vnd.android.cursor.item/vnd." + Constantes.AUTHORITY + INSERT_PROYECTOS_CONTACTO;
    /**
     * Tipo MIME que retorna la consulta de {@link //CONTENT_URI}
     */
    public final static String MULTIPLE_MIME =
            "vnd.android.cursor.dir/vnd." + Constantes.AUTHORITY + INSERT_PROYECTOS_CONTACTO;
    /**
     * URI de contenido principal
     */
    public final static Uri CONTENT_URI = Uri.parse("content://" + Constantes.AUTHORITY + "/" + INSERT_PROYECTOS_CONTACTO);
    /**
     * Comparador de URIs de contenido
     */
    public static final UriMatcher uriMatcher;
    /**
     * Código para URIs de multiples registros
     */
    public static final int ALLROWS = 1;
    /**
     * Código para URIS de un solo registro
     */
    public static final int SINGLE_ROW = 2;


    // Asignación de URIs
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(Constantes.AUTHORITY, INSERT_PROYECTOS_CONTACTO, ALLROWS);
        uriMatcher.addURI(Constantes.AUTHORITY, INSERT_PROYECTOS_CONTACTO + "/#", SINGLE_ROW);
    }

    // Valores para la columna ESTADO
    public static final int ESTADO_OK = 0;
    public static final int ESTADO_SYNC = 1;


    /**
     * Estructura de la tabla
     */
    public static class Columnas implements BaseColumns {

        private Columnas() {
            // Sin instancias
        }

        public final static String CODIGO_PDV = "codigo_pdv";
        public final static String PDV = "pdv";
        // Ciudad del PDV elegido en "Seleccione PDV" (get_pdvs_contacto.php ya
        // consulta lvi_rutero por ese PDV; solo se le agregó "city" al SELECT).
        // Se guarda en el registro para no depender de conexión ni de la tabla
        // local "pharmavalue" (esa es la ruta de Pharma/mercaderista, no la de
        // Proyectos y Obras/KYWI) al momento de poner la marca de agua en las
        // fotos de Proforma/Factura.
        public final static String CIUDAD_PDV = "ciudad_pdv";
        public final static String USUARIO = "usuario";
        public final static String FECHA = "fecha";
        public final static String CONTACTO = "contacto";
        public final static String EMPRESA = "empresa";
        public final static String MAIL = "mail";
        public final static String DIRECCION = "direccion";
        public final static String TELEFONO = "telefono";
        public final static String TELEFONO_CONVENCIONAL = "telefono_convencional";
        public final static String FECHA_AGENDAMIENTO = "fecha_agendamiento";

        // Campos de agenda: vacíos al crear, los completa la futura web (o el APK más adelante)
        public final static String TITULO = "titulo";
        // El promotor puede sugerirla al agendar; el analista la deja o la cambia
        // desde la web y asigna técnico — es el mismo campo para ambos.
        public final static String HORA = "hora";
        public final static String LUGAR = "lugar";
        public final static String LATITUD = "latitud";
        public final static String LONGITUD = "longitud";
        public final static String TECNICO = "tecnico";
        public final static String ESTADO_AGENDA = "estado_agenda";
        public final static String ACTIVAR = "activar";
        // "SI" cuando la web cambia la fecha/hora de una cita que ya estaba agendada
        public final static String REAGENDADO = "reagendado";
        // "SI" cuando el promotor marca el check "No requiere visita" en Contacto:
        // el PDV pasa directo a Proforma sin esperar hora/técnico asignados por el
        // analista (se salta esa validación en ProformaFragment.cargarVisitasDesdeRegistro).
        public final static String NO_REQUIERE_VISITA = "no_requiere_visita";
        // Timestamp que pone MySQL solo (no lo manda la app); se usa para mostrar
        // "Registrado: dd/MM/yyyy HH:mm" en el detalle de la visita.
        public final static String FECHA_REGISTRO = "fecha_registro";

    }
}
