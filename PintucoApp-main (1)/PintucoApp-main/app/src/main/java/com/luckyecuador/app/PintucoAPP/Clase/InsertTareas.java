package com.luckyecuador.app.PintucoAPP.Clase;

public class InsertTareas {

    public String id;
    public String pos_id;
    public String user;
    public String supervisor;
    public String fecha;
    public String hora;
    public String channel;
    public String codigo_pdv;
    public String mercaderista;
    public String tareas;
    public String realizado;
    public String foto;

    public InsertTareas() {
    }

    public InsertTareas(String id, String pos_id, String user, String supervisor, String fecha,
                        String hora, String channel, String codigo_pdv, String mercaderista,
                        String tareas, String realizado, String foto) {
        this.id = id;
        this.pos_id = pos_id;
        this.user = user;
        this.supervisor = supervisor;
        this.fecha = fecha;
        this.hora = hora;
        this.channel = channel;
        this.codigo_pdv = codigo_pdv;
        this.mercaderista = mercaderista;
        this.tareas = tareas;
        this.realizado = realizado;
        this.foto = foto;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPos_id() {
        return pos_id;
    }

    public void setPos_id(String pos_id) {
        this.pos_id = pos_id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(String supervisor) {
        this.supervisor = supervisor;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
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

    public String getRealizado() {
        return realizado;
    }

    public void setRealizado(String realizado) {
        this.realizado = realizado;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}
