package com.luckyecuador.app.PintucoAPP.Clase;

public class BasePDI {

    public String id;
    public String canal;
    public String categoria;
    public String subcategoria;
    public String marca;
    public String objetivo;
    public String plataforma;

    public BasePDI() {}

    public BasePDI(String categoria, String objetivo) {
        this.categoria = categoria;
        this.objetivo = objetivo;
    }

    public BasePDI(String id, String canal, String categoria, String subcategoria, String marca, String objetivo, String plataforma) {
        this.id = id;
        this.canal = canal;
        this.categoria = categoria;
        this.subcategoria = subcategoria;
        this.marca = marca;
        this.objetivo = objetivo;
        this.plataforma = plataforma;
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

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getSubcategoria() {
        return subcategoria;
    }

    public void setSubcategoria(String subcategoria) {
        this.subcategoria = subcategoria;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getObjetivo() {
        return objetivo;
    }

    public void setObjetivo(String objetivo) {
        this.objetivo = objetivo;
    }

    public String getPlataforma() {
        return plataforma;
    }

    public void setPlataforma(String plataforma) {
        this.plataforma = plataforma;
    }
}
