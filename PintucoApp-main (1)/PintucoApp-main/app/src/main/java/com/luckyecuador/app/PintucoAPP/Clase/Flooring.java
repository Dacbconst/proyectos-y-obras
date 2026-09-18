package com.luckyecuador.app.PintucoAPP.Clase;

public class Flooring {

    private String sku_code;

    private String semana;
    private String stockActual;
    private String sugerido;
    private String observaciones;
    private String inventario;
    private String fecha_caducidad;

    public Flooring() {}

    public Flooring(String sku_code) {
        this.sku_code = sku_code;
    }


    public Flooring(String sku_code, String semana, String stockActual, String sugerido, String observaciones, String inventario, String fecha_caducidad) {
        this.sku_code = sku_code;
        this.semana = semana;
        this.stockActual = stockActual;
        this.sugerido = sugerido;
        this.observaciones = observaciones;
        this.inventario = inventario;
        this.fecha_caducidad = fecha_caducidad;
    }

    public String getStockActual() {
        return stockActual;
    }

    public String getSemana() {
        return semana;
    }

    public void setSemana(String semana) {
        this.semana = semana;
    }

    public void setStockActual(String stockActual) {
        this.stockActual = stockActual;
    }

    public String getSugerido() {
        return sugerido;
    }

    public void setSugerido(String sugerido) {
        this.sugerido = sugerido;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getSku_code() {
        return sku_code;
    }

    public void setSku_code(String sku_code) {
        this.sku_code = sku_code;
    }

    public String getInventario() {
        return inventario;
    }

    public void setInventario(String inventario) {
        this.inventario = inventario;
    }

    public String getFecha_caducidad() {
        return fecha_caducidad;
    }

    public void setFecha_caducidad(String fecha_caducidad) {
        this.fecha_caducidad = fecha_caducidad;
    }
}
