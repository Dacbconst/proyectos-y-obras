package com.luckyecuador.app.PintucoAPP.Clase;

public class BaseCausalesAsistenciaAtraso {
    public String id;
    public String descripcion;

    public BaseCausalesAsistenciaAtraso() {}

    public BaseCausalesAsistenciaAtraso(String id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}