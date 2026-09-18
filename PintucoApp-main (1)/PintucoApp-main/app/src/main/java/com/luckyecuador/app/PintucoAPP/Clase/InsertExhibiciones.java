package com.luckyecuador.app.PintucoAPP.Clase;

public class InsertExhibiciones {

    private String marca;
    private String tipo_exh;
    private String zona;
    private String contratada;
    private String condicion;
    private String foto;

    public InsertExhibiciones() {}

    public InsertExhibiciones(String marca, String tipo_exh, String zona, String contratada, String condicion, String foto) {
        this.marca = marca;
        this.tipo_exh = tipo_exh;
        this.zona = zona;
        this.contratada = contratada;
        this.condicion = condicion;
        this.foto = foto;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getTipo_exh() {
        return tipo_exh;
    }

    public void setTipo_exh(String tipo_exh) {
        this.tipo_exh = tipo_exh;
    }

    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }

    public String getContratada() {
        return contratada;
    }

    public void setContratada(String contratada) {
        this.contratada = contratada;
    }

    public String getCondicion() {
        return condicion;
    }

    public void setCondicion(String condicion) {
        this.condicion = condicion;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}
