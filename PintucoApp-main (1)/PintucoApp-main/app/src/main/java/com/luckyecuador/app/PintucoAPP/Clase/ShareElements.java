package com.luckyecuador.app.PintucoAPP.Clase;

public class ShareElements {

    private String marca;
    private String caras;
    private String razon;

    public ShareElements() {}

    public ShareElements(String marca, String caras, String razon) {
        this.marca = marca;
        this.caras = caras;
        this.razon = razon;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getCaras() {
        return caras;
    }

    public void setCaras(String caras) {
        this.caras = caras;
    }

    public String getRazon() {
        return razon;
    }

    public void setRazon(String razon) {
        this.razon = razon;
    }
}
