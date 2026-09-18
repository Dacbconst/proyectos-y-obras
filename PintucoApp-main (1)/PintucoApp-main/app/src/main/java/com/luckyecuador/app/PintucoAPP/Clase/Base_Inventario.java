package com.luckyecuador.app.PintucoAPP.Clase;

public class Base_Inventario {

    private String sku;
    private String stock_percha;
    private String stock_bodega;
    private String sugerido;
    private int seleccionSugerido;
    private String cant_sugerido;
    private String tipo_sugerido;
    private int seleccionTipoSugerido;
    private Boolean tieneDatos = false;


    public Base_Inventario(){}

    public Base_Inventario(String sku, String stock_percha, String stock_bodega, String sugerido,int seleccionSugerido, String cant_sugerido, String tipo_sugerido, int seleccionTipoSugerido) {
        this.sku = sku;
        this.stock_percha = stock_percha;
        this.stock_bodega = stock_bodega;
        this.sugerido = sugerido;
        this.cant_sugerido = cant_sugerido;
        this.tipo_sugerido = tipo_sugerido;
        this.seleccionSugerido = seleccionSugerido;
        this.seleccionTipoSugerido = seleccionTipoSugerido;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getStock_percha() {
        return stock_percha;
    }

    public void setStock_percha(String stock_percha) {
        this.stock_percha = stock_percha;
    }

    public String getStock_bodega() {
        return stock_bodega;
    }

    public void setStock_bodega(String stock_bodega) {
        this.stock_bodega = stock_bodega;
    }

    public String getSugerido() {
        return sugerido;
    }

    public void setSugerido(String sugerido) {
        this.sugerido = sugerido;
    }

    public int getSeleccionSugerido() {
        return seleccionSugerido;
    }

    public void setSeleccionSugerido(int seleccionSugerido) {
        this.seleccionSugerido = seleccionSugerido;
    }

    public String getCant_sugerido() {
        return cant_sugerido;
    }

    public void setCant_sugerido(String cant_sugerido) {
        this.cant_sugerido = cant_sugerido;
    }

    public String getTipo_sugerido() {
        return tipo_sugerido;
    }

    public void setTipo_sugerido(String tipo_sugerido) {
        this.tipo_sugerido = tipo_sugerido;
    }

    public int getSeleccionTipoSugerido() {
        return seleccionTipoSugerido;
    }

    public void setSeleccionTipoSugerido(int seleccionTipoSugerido) {
        this.seleccionTipoSugerido = seleccionTipoSugerido;
    }

    public Boolean getTieneDatos() {
        return tieneDatos;
    }

    public void setTieneDatos(Boolean tieneDatos) {
        this.tieneDatos = tieneDatos;
    }




}
