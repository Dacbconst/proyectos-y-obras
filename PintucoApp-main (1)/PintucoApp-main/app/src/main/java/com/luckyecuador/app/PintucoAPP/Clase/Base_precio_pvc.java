package com.luckyecuador.app.PintucoAPP.Clase;

/**
 * Created by Lucky Ecuador on 24/04/2017.
 */

public class Base_precio_pvc {

    public String id;
    public String usuario;
    public String fecha;
    public String codigo_pdv;
    public String canal;
    public String cadena;
    public String categoria;
    public String subcategoria;
    public String marca;
    public String sku;
    public String pvc;

    public Base_precio_pvc(String id, String usuario, String fecha, String codigo_pdv,String canal,String cadena, String categoria, String subcategoria, String marca,String sku,String pvc) {
        this.id = id;
        this.usuario = usuario;
        this.fecha = fecha;
        this.codigo_pdv = codigo_pdv;
        this.cadena = cadena;
        this.canal = canal;
        this.categoria = categoria;
        this.subcategoria = subcategoria;
        this.marca = marca;
        this.sku = sku;
        this.pvc = pvc;
    }

    public Base_precio_pvc() {

    }

    public String getCanal() {
        return canal;
    }

    public void setCanal(String canal) {
        this.canal = canal;
    }

    public String getCadena() {
        return cadena;
    }

    public void setCadena(String cadena) {
        this.cadena = cadena;
    }

    public String getPvc() {
        return pvc;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public void setPvc(String pvc) {
        this.pvc = pvc;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
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
}
