package com.luckyecuador.app.PintucoAPP.Clase;

public class CanjesElements {

    private String producto;
    private String tipoCombo;
    private String mecanica;
    private String armados;
    private String stock;
    private String pvcCombo;
    private String pvcUnitario;
    private String visita;
    private String mes;
    private String observacion;
    private String foto;

    public CanjesElements() {}

    public CanjesElements(String producto, String tipoCombo, String mecanica, String armados, String stock,
                          String pvcCombo, String pvcUnitario, String visita, String mes, String observacion, String foto) {
        this.producto = producto;
        this.tipoCombo = tipoCombo;
        this.mecanica = mecanica;
        this.armados = armados;
        this.stock = stock;
        this.pvcCombo = pvcCombo;
        this.pvcUnitario = pvcUnitario;
        this.visita = visita;
        this.mes = mes;
        this.observacion = observacion;
        this.foto = foto;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public String getTipoCombo() {
        return tipoCombo;
    }

    public void setTipoCombo(String tipoCombo) {
        this.tipoCombo = tipoCombo;
    }

    public String getMecanica() {
        return mecanica;
    }

    public void setMecanica(String mecanica) {
        this.mecanica = mecanica;
    }

    public String getArmados() {
        return armados;
    }

    public void setArmados(String armados) {
        this.armados = armados;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getPvcCombo() {
        return pvcCombo;
    }

    public void setPvcCombo(String pvcCombo) {
        this.pvcCombo = pvcCombo;
    }

    public String getPvcUnitario() {
        return pvcUnitario;
    }

    public void setPvcUnitario(String pvcUnitario) {
        this.pvcUnitario = pvcUnitario;
    }

    public String getVisita() {
        return visita;
    }

    public void setVisita(String visita) {
        this.visita = visita;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}
