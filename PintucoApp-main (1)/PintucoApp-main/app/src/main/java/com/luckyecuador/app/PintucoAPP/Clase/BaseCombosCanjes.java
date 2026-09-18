package com.luckyecuador.app.PintucoAPP.Clase;

public class BaseCombosCanjes {

    public String id;
    public String tipo_combo;
    public String mecanica;

    public BaseCombosCanjes() {}

    public BaseCombosCanjes(String id, String tipo_combo, String mecanica) {
        this.id = id;
        this.tipo_combo = tipo_combo;
        this.mecanica = mecanica;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTipo_combo() {
        return tipo_combo;
    }

    public void setTipo_combo(String tipo_combo) {
        this.tipo_combo = tipo_combo;
    }

    public String getMecanica() {
        return mecanica;
    }

    public void setMecanica(String mecanica) {
        this.mecanica = mecanica;
    }
}
