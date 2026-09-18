package com.luckyecuador.app.PintucoAPP.DataBase;

import android.database.Cursor;

/**
 * Created by Lucky Ecuador on 01/03/2017.
 */

public class Consultas {

    public static String obtenerString(Cursor cursor, String columna) {
        return cursor.getString(cursor.getColumnIndex(columna));
    }
}
