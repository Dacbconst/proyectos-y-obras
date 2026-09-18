package com.luckyecuador.app.PintucoAPP.Clase;

public class InsertSms {

    private String pos_id;
    private String user;
    private String supervisor;
    private String fecha;
    private String hora;
    private String local;
    private String codigo;
    private String vendedor_fabril;
    private String categoria;
    private String subcategoria;
    private String brand;
    private String sku_code;
    private String quiebre;
    private String unidad_disponible;
    private String sugerido;
    private String cantidad;
    private String observaciones;

    public InsertSms(){}

    public InsertSms(String pos_id, String user, String supervisor, String fecha, String hora, String local, String codigo, String vendedor_fabril, String categoria, String subcategoria, String brand, String sku_code, String quiebre, String unidad_disponible, String sugerido, String cantidad, String observaciones) {
        this.pos_id = pos_id;
        this.user = user;
        this.supervisor = supervisor;
        this.fecha = fecha;
        this.hora = hora;
        this.local = local;
        this.codigo = codigo;
        this.vendedor_fabril = vendedor_fabril;
        this.categoria = categoria;
        this.subcategoria = subcategoria;
        this.brand = brand;
        this.sku_code = sku_code;
        this.quiebre = quiebre;
        this.unidad_disponible = unidad_disponible;
        this.sugerido = sugerido;
        this.cantidad = cantidad;
        this.observaciones = observaciones;
    }

    public String getPos_id() {
        return pos_id;
    }

    public void setPos_id(String pos_id) {
        this.pos_id = pos_id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(String supervisor) {
        this.supervisor = supervisor;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getVendedor_fabril() {
        return vendedor_fabril;
    }

    public void setVendedor_fabril(String vendedor_fabril) {
        this.vendedor_fabril = vendedor_fabril;
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

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getSku_code() {
        return sku_code;
    }

    public void setSku_code(String sku_code) {
        this.sku_code = sku_code;
    }

    public String getQuiebre() {
        return quiebre;
    }

    public void setQuiebre(String quiebre) {
        this.quiebre = quiebre;
    }

    public String getUnidad_disponible() {
        return unidad_disponible;
    }

    public void setUnidad_disponible(String unidad_disponible) {
        this.unidad_disponible = unidad_disponible;
    }

    public String getSugerido() {
        return sugerido;
    }

    public void setSugerido(String sugerido) {
        this.sugerido = sugerido;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}
