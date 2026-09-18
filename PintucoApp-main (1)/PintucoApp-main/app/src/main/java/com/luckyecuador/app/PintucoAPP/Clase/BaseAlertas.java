package com.luckyecuador.app.PintucoAPP.Clase;

public class BaseAlertas {

    public String id;
    public String tipo_alerta;
    public String categoria;
    public String material;

    public BaseAlertas() {}

    public BaseAlertas(String id, String tipo_alerta, String categoria, String material) {
        this.id = id;
        this.tipo_alerta = tipo_alerta;
        this.categoria = categoria;
        this.material = material;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTipo_alerta() {
        return tipo_alerta;
    }

    public void setTipo_alerta(String tipo_alerta) {
        this.tipo_alerta = tipo_alerta;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }
}
