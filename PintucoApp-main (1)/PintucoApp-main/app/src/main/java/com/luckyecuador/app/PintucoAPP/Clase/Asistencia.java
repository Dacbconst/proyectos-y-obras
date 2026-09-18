package com.luckyecuador.app.PintucoAPP.Clase;

public class Asistencia {
    public String hora;
    public String registrado;
    public String estado;

    public Asistencia(String hora, String registrado, String estado) {
        this.hora = hora;
        this.registrado = registrado;
        this.estado = estado;
    }

    public Asistencia() {
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getRegistrado() {
        return registrado;
    }

    public void setRegistrado(String registrado) {
        this.registrado = registrado;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Asistencia{" +
                "hora='" + hora + '\'' +
                ", registrado='" + registrado + '\'' +
                ", estado='" + estado + '\'' +
                '}';
    }
}
