package com.luckyecuador.app.PintucoAPP.Clase;

public class Base_tipoPrecios {

    public String id;//YA
    public String canal;//YA
    public String subcanal;//YA
    public String tipo;//YA


    public Base_tipoPrecios() {}

    public Base_tipoPrecios(String id, String canal, String subcanal, String tipo) {
        this.id = id;
        this.canal = canal;
        this.subcanal = subcanal;
        this.tipo = tipo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCanal() {
        return canal;
    }

    public void setCanal(String canal) {
        this.canal = canal;
    }

    public String getSubcanal() {
        return subcanal;
    }

    public void setSubcanal(String subcanal) {
        this.subcanal = subcanal;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
