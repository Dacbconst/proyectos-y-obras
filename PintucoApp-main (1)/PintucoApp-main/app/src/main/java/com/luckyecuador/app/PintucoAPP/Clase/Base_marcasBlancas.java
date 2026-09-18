package com.luckyecuador.app.PintucoAPP.Clase;

public class Base_marcasBlancas {

    public String id;//YA
    public String marcas;//YA


    public Base_marcasBlancas() {}

    public Base_marcasBlancas(String id, String marcas) {
        this.id = id;
        this.marcas = marcas;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMarcas() {
        return marcas;
    }

    public void setMarcas(String marcas) {
        this.marcas = marcas;
    }
}
