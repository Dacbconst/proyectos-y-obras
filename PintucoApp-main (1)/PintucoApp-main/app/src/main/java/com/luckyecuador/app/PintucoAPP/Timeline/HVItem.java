package com.luckyecuador.app.PintucoAPP.Timeline;

public class HVItem {

    private String hora;
    private String descripcion;

    public HVItem(String hora, String descripcion) {
        this.hora = hora;
        this.descripcion = descripcion;
    }

    public HVItem() {

    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }


}
