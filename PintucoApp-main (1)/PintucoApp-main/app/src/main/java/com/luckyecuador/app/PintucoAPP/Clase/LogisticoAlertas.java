package com.luckyecuador.app.PintucoAPP.Clase;


public class LogisticoAlertas {
    private String skuCode;
    private String cantidad; // cantidad del día anterior
    private String causal;
    private String tipoLogistico;
    private String fechaCaducado;
    private String fechaPropenso;
    private String fecha;

    // Constructor vacío
    public LogisticoAlertas() {
    }

    // Constructor con parámetros (opcional)
    public LogisticoAlertas(String skuCode, String cantidad, String causal,
                            String tipoLogistico, String fechaCaducado,
                            String fechaPropenso, String fecha) {
        this.skuCode = skuCode;
        this.cantidad = cantidad;
        this.causal = causal;
        this.tipoLogistico = tipoLogistico;
        this.fechaCaducado = fechaCaducado;
        this.fechaPropenso = fechaPropenso;
        this.fecha = fecha;
    }

    // Getters y Setters
    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getCausal() {
        return causal;
    }

    public void setCausal(String causal) {
        this.causal = causal;
    }

    public String getTipoLogistico() {
        return tipoLogistico;
    }

    public void setTipoLogistico(String tipoLogistico) {
        this.tipoLogistico = tipoLogistico;
    }

    public String getFechaCaducado() {
        return fechaCaducado;
    }

    public void setFechaCaducado(String fechaCaducado) {
        this.fechaCaducado = fechaCaducado;
    }

    public String getFechaPropenso() {
        return fechaPropenso;
    }

    public void setFechaPropenso(String fechaPropenso) {
        this.fechaPropenso = fechaPropenso;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    @Override
    public String toString() {
        return "LogisticoRegistro{" +
                "skuCode='" + skuCode + '\'' +
                ", cantidad='" + cantidad + '\'' +
                ", causal='" + causal + '\'' +
                ", tipoLogistico='" + tipoLogistico + '\'' +
                ", fechaCaducado='" + fechaCaducado + '\'' +
                ", fechaPropenso='" + fechaPropenso + '\'' +
                ", fecha='" + fecha + '\'' +
                '}';
    }

}