package com.luckyecuador.app.PintucoAPP.Clase;

public class BaseCausalesOSA {

    public String id;
    public String canal;
    public String responsable;
    public String causal;

    public BaseCausalesOSA() {}

    public BaseCausalesOSA(String id, String canal, String responsable, String causal) {
        this.id = id;
        this.canal = canal;
        this.responsable = responsable;
        this.causal = causal;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCanal() {
        return canal;
    }

    public void setCanal(String canal) {
        this.canal = canal;
    }

    public String getResponsable() {
        return responsable;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }

    public String getCausal() {
        return causal;
    }

    public void setCausal(String causal) {
        this.causal = causal;
    }
}
