package com.luckyecuador.app.PintucoAPP.Clase;

public class Base_causales_prod_mal_est {

    public String id;//YA
    public String causal;//YA
    public String cantidad_defectuosa;//YA

    public Base_causales_prod_mal_est() {}

    public Base_causales_prod_mal_est(String id, String causal, String cantidad_defectuosa) {
        this.id = id;
        this.causal = causal;
        this.cantidad_defectuosa = cantidad_defectuosa;
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

    public String getCantidad_defectuosa() {
        return cantidad_defectuosa;
    }

    public void setCantidad_defectuosa(String cantidad_defectuosa) {
        this.cantidad_defectuosa = cantidad_defectuosa;
    }
}
