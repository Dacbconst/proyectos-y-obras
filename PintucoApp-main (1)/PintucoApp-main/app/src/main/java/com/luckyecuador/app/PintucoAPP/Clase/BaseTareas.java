package com.luckyecuador.app.PintucoAPP.Clase;

public class BaseTareas {

    public String id;
    public String channel;
    public String codigo_pdv;
    public String mercaderista;
    public String tareas;
    public String periodo;
    public String fecha_ingreso;

    public BaseTareas() {
    }

    public BaseTareas(String id, String channel, String codigo_pdv, String mercaderista, String tareas,
                      String periodo, String fecha_ingreso) {
        this.id = id;
        this.channel = channel;
        this.codigo_pdv = codigo_pdv;
        this.mercaderista = mercaderista;
        this.tareas = tareas;
        this.periodo = periodo;
        this.fecha_ingreso = fecha_ingreso;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getCodigo_pdv() {
        return codigo_pdv;
    }

    public void setCodigo_pdv(String codigo_pdv) {
        this.codigo_pdv = codigo_pdv;
    }

    public String getMercaderista() {
        return mercaderista;
    }

    public void setMercaderista(String mercaderista) {
        this.mercaderista = mercaderista;
    }

    public String getTareas() {
        return tareas;
    }

    public void setTareas(String tareas) {
        this.tareas = tareas;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public String getFecha_ingreso() {
        return fecha_ingreso;
    }

    public void setFecha_ingreso(String fecha_ingreso) {
        this.fecha_ingreso = fecha_ingreso;
    }
}
