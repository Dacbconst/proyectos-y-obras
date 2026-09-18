package com.luckyecuador.app.PintucoAPP.Clase;

/**
 * Created by Lucky Ecuador on 27/02/2018.
 * Modificado por Joffre Herrera 18/02/2020
 */

public class Base_portafolio_productos_mayo {

    public String id;
    public String codigo;
    public String usuario;
    public String categoria;
    public String subcategoria;
    public String marca;
    public String fabricante;
    public String sku;
    public String status;

    public Base_portafolio_productos_mayo() {}

    public Base_portafolio_productos_mayo(String id, String codigo, String usuario, String categoria, String subcategoria, String marca, String fabricante, String sku, String status) {
        this.id = id;
        this.codigo = codigo;
        this.usuario = usuario;
        this.categoria = categoria;
        this.subcategoria = subcategoria;
        this.marca = marca;
        this.fabricante = fabricante;
        this.sku = sku;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
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

    public String getFabricante() {
        return fabricante;
    }

    public void setFabricante(String fabricante) {
        this.fabricante = fabricante;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
