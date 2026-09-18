package com.luckyecuador.app.PintucoAPP.Clase;

public class LogUser {

    private String usuario;
    private String fecha;
    private String hora;
    private String accion;

    public LogUser() {}

    public LogUser(String usuario, String fecha, String hora, String accion) {
        this.usuario = usuario;
        this.fecha = fecha;
        this.hora = hora;
        this.accion = accion;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
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

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }
}
