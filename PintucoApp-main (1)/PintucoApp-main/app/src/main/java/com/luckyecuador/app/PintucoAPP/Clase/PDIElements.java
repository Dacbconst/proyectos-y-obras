package com.luckyecuador.app.PintucoAPP.Clase;

public class PDIElements {

    public String sku;
    public String universo;
    public String caras;
    public String cumplimiento;

    public PDIElements() {}

    public PDIElements(String sku, String universo, String caras, String cumplimiento) {
        this.sku = sku;
        this.universo = universo;
        this.caras = caras;
        this.cumplimiento = cumplimiento;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getUniverso() {
        return universo;
    }

    public void setUniverso(String universo) {
        this.universo = universo;
    }

    public String getCaras() {
        return caras;
    }

    public void setCaras(String caras) {
        this.caras = caras;
    }

    public String getCumplimiento() {
        return cumplimiento;
    }

    public void setCumplimiento(String cumplimiento) {
        this.cumplimiento = cumplimiento;
    }
}