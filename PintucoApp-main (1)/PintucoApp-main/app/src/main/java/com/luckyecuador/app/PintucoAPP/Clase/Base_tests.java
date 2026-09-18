package com.luckyecuador.app.PintucoAPP.Clase;

public class Base_tests {
    public String id;
    public String test;
    public String descripcion;
    public String f_inicio;
    public String h_inicio;
    public String f_limite;
    public String h_limite;
    public String active;

    public Base_tests() {}

    public Base_tests(String id, String test, String descripcion, String f_inicio, String h_inicio, String f_limite, String h_limite, String active) {
        this.id = id;
        this.test = test;
        this.descripcion = descripcion;
        this.f_inicio = f_inicio;
        this.h_inicio = h_inicio;
        this.f_limite = f_limite;
        this.h_limite = h_limite;
        this.active = active;
    }

    public String getH_inicio() {
        return h_inicio;
    }

    public void setH_inicio(String h_inicio) {
        this.h_inicio = h_inicio;
    }

    public String getH_limite() {
        return h_limite;
    }

    public void setH_limite(String h_limite) {
        this.h_limite = h_limite;
    }

    public String getId() {
        return id;
    }

    public void setTest_id(String id) {
        this.id = id;
    }

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getF_inicio() {
        return f_inicio;
    }

    public void setF_inicio(String f_inicio) {
        this.f_inicio = f_inicio;
    }

    public String getF_limite() {
        return f_limite;
    }

    public void setF_limite(String f_limite) {
        this.f_limite = f_limite;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }
}
