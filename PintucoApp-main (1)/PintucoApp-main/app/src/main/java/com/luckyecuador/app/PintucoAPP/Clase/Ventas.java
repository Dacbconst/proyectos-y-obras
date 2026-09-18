package com.luckyecuador.app.PintucoAPP.Clase;

public class Ventas {

    private String sku;
    private String tipo_venta;
    private String cantidad;
    private String precio_unitario;
    private String valor_total;
    private String contenido;

    public Ventas() {}

    public Ventas(String sku) {
        this.sku = sku;
    }

    public Ventas(String sku, String tipo_venta, String cantidad, String precio_unitario, String valor_total) {
        this.sku = sku;
        this.tipo_venta = tipo_venta;
        this.cantidad = cantidad;
        this.precio_unitario = precio_unitario;
        this.valor_total = valor_total;
    }

    public Ventas(String sku, String tipo_venta, String cantidad, String precio_unitario, String valor_total, String contenido) {
        this.sku = sku;
        this.tipo_venta = tipo_venta;
        this.cantidad = cantidad;
        this.precio_unitario = precio_unitario;
        this.valor_total = valor_total;
        this.contenido = contenido;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getTipo_venta() {
        return tipo_venta;
    }

    public void setTipo_venta(String tipo_venta) {
        this.tipo_venta = tipo_venta;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getPrecio_unitario() {
        return precio_unitario;
    }

    public void setPrecio_unitario(String precio_unitario) {
        this.precio_unitario = precio_unitario;
    }

    public String getValor_total() {
        return valor_total;
    }

    public void setValor_total(String valor_total) {
        this.valor_total = valor_total;
    }
}
