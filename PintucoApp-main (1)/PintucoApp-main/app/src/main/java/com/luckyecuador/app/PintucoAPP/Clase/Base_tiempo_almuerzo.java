package com.luckyecuador.app.PintucoAPP.Clase;

public class Base_tiempo_almuerzo {

    public String id;
    public String tiempo_almuerzo;

    public Base_tiempo_almuerzo() {}

    public Base_tiempo_almuerzo(String id, String tiempo_almuerzo) {
        this.id = id;
        this.tiempo_almuerzo = tiempo_almuerzo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {this.id = id;}

    public String getTiempo_almuerzo() {
        return tiempo_almuerzo;
    }

    public void setTiempo_almuerzo(String tiempo_almuerzo) {
        this.tiempo_almuerzo = tiempo_almuerzo;
    }
}
