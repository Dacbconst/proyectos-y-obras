package com.luckyecuador.app.PintucoAPP.Clase;

public class InsertOsa {

    public String sku;
    public String sugerido;

    public InsertOsa() {}

    public InsertOsa(String sku, String sugerido) {
        this.sku = sku;
        this.sugerido = sugerido;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getSugerido() {
        return sugerido;
    }

    public void setSugerido(String sugerido) {
        this.sugerido = sugerido;
    }
}
