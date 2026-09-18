package com.luckyecuador.app.PintucoAPP.Clase;
/**
 * Created by Lucky Ecuador on 27/02/2018.
 * Modificado por Joffre Herrera 10/02/2019
 * Modificado por Samantha Flores 12/02/2021
 */
public class BaseRotacion {

    public String id;
    public String categoria;
    public String subcategoria;
    public String marca;
    public String producto;
    public String promocional;
    public String mecanica;
    public String peso;
    public String tipo;
    public String plataforma;

    public BaseRotacion() {}

    public BaseRotacion(String producto) {
        this.producto = producto;
    }

    public BaseRotacion(String id, String categoria, String subcategoria, String marca, String producto,
                        String promocional, String mecanica, String peso, String tipo, String plataforma) {
        this.id = id;
        this.categoria = categoria;
        this.subcategoria = subcategoria;
        this.marca = marca;
        this.producto = producto;
        this.promocional = promocional;
        this.mecanica = mecanica;
        this.peso = peso;
        this.tipo = tipo;
        this.plataforma = plataforma;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public String getPromocional() {
        return promocional;
    }

    public void setPromocional(String promocional) {
        this.promocional = promocional;
    }

    public String getMecanica() {
        return mecanica;
    }

    public void setMecanica(String mecanica) {
        this.mecanica = mecanica;
    }

    public String getPeso() {
        return peso;
    }

    public void setPeso(String peso) {
        this.peso = peso;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getPlataforma() {
        return plataforma;
    }

    public void setPlataforma(String plataforma) {
        this.plataforma = plataforma;
    }
}
