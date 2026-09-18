package com.luckyecuador.app.PintucoAPP.Clase;

public class Portafolio_prioritario {

    public String id;
    public String canal;
    public String codigo_pdv;
    public String categoria;
    public String subcategoria;
    public String marca;
    public String contenido;
    public String sku;

    public Portafolio_prioritario() {}

    public Portafolio_prioritario(String sku) {
        this.sku = sku;
    }

    public Portafolio_prioritario(String id, String canal, String codigo_pdv, String categoria, String subcategoria, String marca, String contenido, String sku) {
        this.id = id;
        this.canal = canal;
        this.codigo_pdv = codigo_pdv;
        this.categoria = categoria;
        this.subcategoria = subcategoria;
        this.marca = marca;
        this.contenido = contenido;
        this.sku = sku;
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

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }
}
