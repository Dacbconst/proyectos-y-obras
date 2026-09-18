package com.luckyecuador.app.PintucoAPP.Clase;
/**
 * Created by Lucky Ecuador on 27/02/2018.
 * Modificado por Joffre Herrera 10/02/2019
 * Modificado por Samantha Flores 12/02/2021
 */
public class Rotacion {
    public String id;
    public String categoria;
    public String producto;
    public String promocional;
    public String mecanica;
    public String peso;

    public Rotacion() {}

    public Rotacion(String producto) {
        this.producto = producto;
    }

    public Rotacion(String id, String categoria, String producto,
                                     String promocional, String mecanica, String peso) {
        this.id = id;
        this.categoria = categoria;
        this.producto = producto;
        this.promocional = promocional;
        this.mecanica = mecanica;
        this.peso = peso;
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

}
