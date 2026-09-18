package com.luckyecuador.app.PintucoAPP.Clase;

public class Base_tipo_exh {
    public String id;
    public String canal;
    public String exhibicion;
    public String tipo;
    public String foto;
    public Boolean isSelected = false;


    public Base_tipo_exh() {}

    public Base_tipo_exh(String id, String canal, String exhibicion, String tipo, String foto, Boolean isSelected) {
        this.id = id;
        this.canal = canal;
        this.exhibicion = exhibicion;
        this.tipo = tipo;
        this.foto = foto;
        this.isSelected = isSelected;
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

    public String getExhibicion() {
        return exhibicion;
    }

    public void setExhibicion(String exhibicion) {
        this.exhibicion = exhibicion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }
}
