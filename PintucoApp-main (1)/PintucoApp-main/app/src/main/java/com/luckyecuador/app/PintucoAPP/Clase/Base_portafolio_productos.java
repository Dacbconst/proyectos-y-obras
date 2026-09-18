package com.luckyecuador.app.PintucoAPP.Clase;

import android.graphics.Bitmap;

/**
 * Created by Lucky Ecuador on 27/02/2018.
 * Modificado por Joffre Herrera 10/02/2019
 */

public class Base_portafolio_productos {

    public String id;
    public String sector;
    public String categoria;
    public String subcategoria;
    public String segmento;
    public String presentacion;
    public String variante1;
    public String variante2;
    public String contenido;
    public String sku;
    public String marca;
    public String fabricante;
    public String pvp;
    public String cadenas;
    public String foto;
    public String skuCliente;
    //
    private String sku2;
    private String total;
    private String fechaCaducidad;
    private String tipoUnidad;
    private Bitmap fotoBitmap;
    private String estado; // To hold the "REALIZADO" or other state

    public String getSku2() {
        return sku2;
    }

    public void setSku2(String sku2) {
        this.sku2 = sku2;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getFechaCaducidad() {
        return fechaCaducidad;
    }

    public void setFechaCaducidad(String fechaCaducidad) {
        this.fechaCaducidad = fechaCaducidad;
    }

    public String getTipoUnidad() {
        return tipoUnidad;
    }

    public void setTipoUnidad(String tipoUnidad) {
        this.tipoUnidad = tipoUnidad;
    }

    public Bitmap getFotoBitmap() {
        return fotoBitmap;
    }

    public void setFotoBitmap(Bitmap fotoBitmap) {
        this.fotoBitmap = fotoBitmap;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }











    public Base_portafolio_productos() {}

//    public Base_portafolio_productos(String id, String sector, /*...,*/ String skuCliente) {
//        // ... muchos campos públicos se inicializan ...
//        this.skuCliente = skuCliente;
//    }

    public Base_portafolio_productos(String sku) {
        this.sku = sku;
    }

    public String getSkuCliente() {
        return skuCliente;
    }

    public void setSkuCliente(String skuCliente) {
        this.skuCliente = skuCliente;
    }

    public Base_portafolio_productos(String id, String sector, String categoria, String subcategoria,
                                     String segmento, String presentacion, String variante1, String variante2,
                                     String contenido, String sku, String marca, String fabricante, String pvp, String cadenas, String foto, String skuCliente) {
        this.id = id;
        this.sector = sector;
        this.categoria = categoria;
        this.subcategoria = subcategoria;
        this.segmento = segmento;
        this.presentacion = presentacion;
        this.variante1 = variante1;
        this.variante2 = variante2;
        this.contenido = contenido;
        this.sku = sku;
        this.marca = marca;
        this.fabricante = fabricante;
        this.pvp = pvp;
        this.cadenas = cadenas;
        this.foto = foto;
        this.skuCliente = skuCliente;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
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

    public String getSegmento() {
        return segmento;
    }

    public void setSegmento(String segmento) {
        this.segmento = segmento;
    }

    public String getPresentacion() {
        return presentacion;
    }

    public void setPresentacion(String presentacion) {
        this.presentacion = presentacion;
    }

    public String getVariante1() {
        return variante1;
    }

    public void setVariante1(String variante1) {
        this.variante1 = variante1;
    }

    public String getVariante2() {
        return variante2;
    }

    public void setVariante2(String variante2) {
        this.variante2 = variante2;
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

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getFabricante() {
        return fabricante;
    }

    public void setFabricante(String fabricante) {
        this.fabricante = fabricante;
    }

    public String getPvp() {
        return pvp;
    }

    public void setPvp(String pvp) {
        this.pvp = pvp;
    }

    public String getCadenas() {
        return cadenas;
    }

    public void setCadenas(String cadenas) {
        this.cadenas = cadenas;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}