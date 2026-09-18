package com.luckyecuador.app.PintucoAPP.Clase;

public class Base_Promociones {

    public String id;//YA
    public String canal;//YA
    public String tipo;//YA
    public String descripcion;//YA

    public Base_Promociones() {}

    public Base_Promociones(String id, String canal, String tipo, String descripcion) {
        this.id = id;
        this.canal = canal;
        this.tipo = tipo;
        this.descripcion = descripcion;
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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
