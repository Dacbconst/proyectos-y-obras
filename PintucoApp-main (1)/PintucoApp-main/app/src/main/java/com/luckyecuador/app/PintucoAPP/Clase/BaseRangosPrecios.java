package com.luckyecuador.app.PintucoAPP.Clase;

public class BaseRangosPrecios {
    public String id;
    public String presentacion;
    public double minimo;
    public double maximo;

    public BaseRangosPrecios() {
    }

    public BaseRangosPrecios(String presentacion, double minimo, double maximo) {
        this.presentacion = presentacion;
        this.minimo = minimo;
        this.maximo = maximo;
    }

    public String getPresentacion() {
        return presentacion;
    }

    public void setPresentacion(String presentacion) {
        this.presentacion = presentacion;
    }

    public double getMinimo() {
        return minimo;
    }

    public void setMinimo(double minimo) {
        this.minimo = minimo;
    }

    public double getMaximo() {
        return maximo;
    }

    public void setMaximo(double maximo) {
        this.maximo = maximo;
    }
}