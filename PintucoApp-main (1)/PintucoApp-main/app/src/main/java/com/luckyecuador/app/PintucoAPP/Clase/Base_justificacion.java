package com.luckyecuador.app.PintucoAPP.Clase;

public class Base_justificacion {

    public String id;//YA
    public String justificacion;//YA


    public Base_justificacion() {}

    public Base_justificacion(String id, String justificacion) {
        this.id = id;
        this.justificacion = justificacion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJustificacion() {
        return justificacion;
    }

    public void setJustificacion(String justificacion) {
        this.justificacion = justificacion;
    }
}
