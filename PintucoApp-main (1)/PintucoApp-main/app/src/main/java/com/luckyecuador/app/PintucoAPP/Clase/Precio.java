package com.luckyecuador.app.PintucoAPP.Clase;

public class Precio {

    private String sku_code;
    private String pvp;
    private String pvc;
    private String p_oferta;
    private String contenido;

    public Precio() {}

    public Precio(String sku_code) {
        this.sku_code = sku_code;
    }

    public Precio(String sku_code, String pvp, String pvc, String p_oferta) {
        this.sku_code = sku_code;
        this.pvp = pvp;
        this.pvc = pvc;
        this.p_oferta = p_oferta;
    }

    public Precio(String sku_code, String pvp, String pvc, String p_oferta, String contenido) {
        this.sku_code = sku_code;
        this.pvp = pvp;
        this.pvc = pvc;
        this.p_oferta = p_oferta;
        this.contenido = contenido;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public String getSku_code() {
        return sku_code;
    }

    public void setSku_code(String sku_code) {
        this.sku_code = sku_code;
    }

    public String getPvp() {
        return pvp;
    }

    public void setPvp(String pvp) {
        this.pvp = pvp;
    }

    public String getPvc() {
        return pvc;
    }

    public void setPvc(String pvc) {
        this.pvc = pvc;
    }

    public String getP_oferta() {
        return p_oferta;
    }

    public void setP_oferta(String p_oferta) {
        this.p_oferta = p_oferta;
    }
}
