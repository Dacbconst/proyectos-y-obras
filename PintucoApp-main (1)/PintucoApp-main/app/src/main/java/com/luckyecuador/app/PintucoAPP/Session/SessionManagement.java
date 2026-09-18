package com.luckyecuador.app.PintucoAPP.Session;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManagement {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String SHARED_PREF_NAME = "session";
    String SESSION_KEY = "session_user";

    public SessionManagement(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

//    public void saveSession(User user) {
//        int id = user.getId();
//        editor.putInt(SESSION_KEY, id).commit();
//    }
//
//    public int gerSession() {
//        return sharedPreferences.getInt(SESSION_KEY, -1);
//    }
//
//    public void removeSession() {
//        editor.putInt(SESSION_KEY, -1).commit();
//    }

    public void saveSession(User user) {
        String usuario = user.getName();
        editor.putString(SESSION_KEY, usuario).commit();
    }

    public String gerSession() {
        return sharedPreferences.getString(SESSION_KEY, "");
    }

    public void removeSession() {
        editor.putString(SESSION_KEY, "").commit();
    }
}