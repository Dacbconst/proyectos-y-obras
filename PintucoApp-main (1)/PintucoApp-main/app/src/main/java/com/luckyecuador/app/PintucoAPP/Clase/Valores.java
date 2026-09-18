package com.luckyecuador.app.PintucoAPP.Clase;

public class Valores {

    private String sku_code;
    private String ausencia;
    private String codifica;
    private String responsable;
    private String razones;

    public Valores() {}

    public Valores(String sku_code, String ausencia, String codifica, String responsable, String razones) {
        this.sku_code = sku_code;
        this.ausencia = ausencia;
        this.codifica = codifica;
        this.responsable = responsable;
        this.razones = razones;
    }

    public String getSku_code() {
        return sku_code;
    }

    public void setSku_code(String sku_code) {
        this.sku_code = sku_code;
    }

    public String getAusencia() {
        return ausencia;
    }

    public void setAusencia(String ausencia) {
        this.ausencia = ausencia;
    }

    public String getCodifica() {
        return codifica;
    }

    public void setCodifica(String codifica) {
        this.codifica = codifica;
    }

    public String getResponsable() {
        return responsable;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }

    public String getRazones() {
        return razones;
    }

    public void setRazones(String razones) {
        this.razones = razones;
    }
}
