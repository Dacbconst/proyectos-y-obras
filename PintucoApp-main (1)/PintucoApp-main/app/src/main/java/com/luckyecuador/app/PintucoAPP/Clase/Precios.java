package com.luckyecuador.app.PintucoAPP.Clase;

/**
 * Created by Lucky Ecuador on 24/04/2017.
 */

public class Precios {

    public String id;
    public String producto;
    public String segmento;
    public String marca;
    public String categoria;
    public String subcategoria;

    public Precios(String id, String producto, String segmento, String marca,
                   String categoria, String subcategoria) {
        this.id = id;
        this.producto = producto;
        this.segmento = segmento;
        this.marca = marca;
        this.categoria = categoria;
        this.subcategoria=subcategoria;
    }
}
