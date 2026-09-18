package com.luckyecuador.app.PintucoAPP.Clase;

public class BaseCausalesMCI {

    public String id;
    public String causal;

    public BaseCausalesMCI() {}

    public BaseCausalesMCI(String id, String causal) {
        this.id = id;
        this.causal = causal;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCausal() {
        return causal;
    }

    public void setCausal(String causal) {
        this.causal = causal;
    }
}
