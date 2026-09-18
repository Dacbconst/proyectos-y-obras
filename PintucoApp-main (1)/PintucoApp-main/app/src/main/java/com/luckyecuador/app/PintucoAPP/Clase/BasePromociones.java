package com.luckyecuador.app.PintucoAPP.Clase;

public class BasePromociones {

    public String id;//YA
    public String canal;//YA
    public String tipo;//YA
    public String descripcion;//YA
    public String mecanica;//YA


    public BasePromociones() {}

    public BasePromociones(String id, String canal, String tipo, String descripcion, String mecanica) {
        this.id = id;
        this.canal = canal;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.mecanica = mecanica;
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

    public String getMecanica() {
        return mecanica;
    }

    public void setMecanica(String mecanica) {
        this.mecanica = mecanica;
    }
}