package com.luckyecuador.app.PintucoAPP.Clase;

public class Producto {
    private String id;
    private String sku;
    private String pvc;

    public Producto(String id, String sku, String pvc) {
        this.id = id;
        this.sku = sku;
        this.pvc = pvc;
    }

    public Producto() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getPvc() {
        return pvc;
    }

    public void setPvc(String pvc) {
        this.pvc = pvc;
    }

    @Override
    public String toString() {
        return "Producto{" +
                "id='" + id + '\'' +
                ", sku='" + sku + '\'' +
                ", pvc='" + pvc + '\'' +
                '}';
    }
}
