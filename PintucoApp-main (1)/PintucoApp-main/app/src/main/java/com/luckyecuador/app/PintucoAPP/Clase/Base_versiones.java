package com.luckyecuador.app.PintucoAPP.Clase;

/**
 * Created by Lucky Ecuador on 24/04/2017.
 */

public class Base_versiones {

    public String id;
    public String fecha_version;
    public String link_aplicativo;
    public String tamaño;

    public Base_versiones(){}

    public Base_versiones(String id, String fecha_version, String link_aplicativo, String tamaño) {
        this.id = id;
        this.fecha_version = fecha_version;
        this.link_aplicativo = link_aplicativo;
        this.tamaño = tamaño;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFecha_version() {
        return fecha_version;
    }

    public void setFecha_version(String fecha_version) {
        this.fecha_version = fecha_version;
    }

    public String getLink_aplicativo() {
        return link_aplicativo;
    }

    public void setLink_aplicativo(String link_aplicativo) {
        this.link_aplicativo = link_aplicativo;
    }

    public String getTamaño() {
        return tamaño;
    }

    public void setTamaño(String tamaño) {
        this.tamaño = tamaño;
    }
}
