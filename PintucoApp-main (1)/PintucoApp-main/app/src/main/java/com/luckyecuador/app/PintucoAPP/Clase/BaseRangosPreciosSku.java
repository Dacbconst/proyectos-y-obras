package com.luckyecuador.app.PintucoAPP.Clase;

public class BaseRangosPreciosSku {
    public String id;
    public String sku;
    public double minimo;
    public double maximo;

    public BaseRangosPreciosSku() {
    }

    public BaseRangosPreciosSku(String sku, double minimo, double maximo) {
        this.sku = sku;
        this.minimo = minimo;
        this.maximo = maximo;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public double getMinimo() {
        return minimo;
    }

    public void setMinimo(double minimo) {
        this.minimo = minimo;
    }

    public double getMaximo() {
        return maximo;
    }

    public void setMaximo(double maximo) {
        this.maximo = maximo;
    }
}
