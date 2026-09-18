package com.luckyecuador.app.PintucoAPP.Clase;

public class InsertPdi {

    public String pos_id;
    public String user;
    public String supervisor;
    public String fecha;
    public String hora;
    public String categoria;
    public String marca_seleccionada;
    public String cumplimiento;
    public String verso;
    public String caras;
    public String otros;
    public String obj_categoria;
    public String part_categoria;
    public String image;
    public String canal;
    public String pos_name;
    public String plataforma;

    public InsertPdi(){}

    public InsertPdi(String pos_id, String user, String supervisor, String fecha, String hora, String categoria, String marca_seleccionada, String cumplimiento, String verso, String caras, String otros, String obj_categoria, String part_categoria, String image, String canal, String pos_name, String plataforma) {
        this.pos_id = pos_id;
        this.user = user;
        this.supervisor = supervisor;
        this.fecha = fecha;
        this.hora = hora;
        this.categoria = categoria;
        this.marca_seleccionada = marca_seleccionada;
        this.cumplimiento = cumplimiento;
        this.verso = verso;
        this.caras = caras;
        this.otros = otros;
        this.obj_categoria = obj_categoria;
        this.part_categoria = part_categoria;
        this.image = image;
        this.canal = canal;
        this.pos_name = pos_name;
        this.plataforma = plataforma;
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

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getMarca_seleccionada() {
        return marca_seleccionada;
    }

    public void setMarca_seleccionada(String marca_seleccionada) {
        this.marca_seleccionada = marca_seleccionada;
    }

    public String getCumplimiento() {
        return cumplimiento;
    }

    public void setCumplimiento(String cumplimiento) {
        this.cumplimiento = cumplimiento;
    }

    public String getVerso() {
        return verso;
    }

    public void setVerso(String verso) {
        this.verso = verso;
    }

    public String getCaras() {
        return caras;
    }

    public void setCaras(String caras) {
        this.caras = caras;
    }

    public String getOtros() {
        return otros;
    }

    public void setOtros(String otros) {
        this.otros = otros;
    }

    public String getObj_categoria() {
        return obj_categoria;
    }

    public void setObj_categoria(String obj_categoria) {
        this.obj_categoria = obj_categoria;
    }

    public String getPart_categoria() {
        return part_categoria;
    }

    public void setPart_categoria(String part_categoria) {
        this.part_categoria = part_categoria;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCanal() {
        return canal;
    }

    public void setCanal(String canal) {
        this.canal = canal;
    }

    public String getPos_name() {
        return pos_name;
    }

    public void setPos_name(String pos_name) {
        this.pos_name = pos_name;
    }

    public String getPlataforma() {
        return plataforma;
    }

    public void setPlataforma(String plataforma) {
        this.plataforma = plataforma;
    }
}

