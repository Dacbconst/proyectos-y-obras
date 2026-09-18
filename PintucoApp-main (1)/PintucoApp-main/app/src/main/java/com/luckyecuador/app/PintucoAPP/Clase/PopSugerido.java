package com.luckyecuador.app.PintucoAPP.Clase;

public class PopSugerido {

    public String id;
    public String canal;
    public String codigo_pdv;
    public String pop_sugerido;

    public PopSugerido() {}

 /*   public PopSugerido(String pop_sugerido) {
        this.pop_sugerido = pop_sugerido;
    }*/

    public PopSugerido(String id, String canal, String codigo_pdv, String pop_sugerido) {
        this.id = id;
        this.canal = canal;
        this.codigo_pdv = codigo_pdv;
        this.pop_sugerido = pop_sugerido;
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

    public String getCodigo_pdv() {
        return codigo_pdv;
    }

    public void setCodigo_pdv(String codigo_pdv) {
        this.codigo_pdv = codigo_pdv;
    }

    public String getPop_sugerido() {
        return pop_sugerido;
    }

    public void setPop_sugerido(String pop_sugerido) {
        this.pop_sugerido = pop_sugerido;
    }
}
