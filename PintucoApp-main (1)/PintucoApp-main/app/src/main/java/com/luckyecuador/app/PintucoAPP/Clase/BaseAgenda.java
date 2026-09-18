package com.luckyecuador.app.PintucoAPP.Clase;

public class BaseAgenda {

    public String id;
    public String fecha;
    public String hora;
    public String titulo;
    public String empresa;
    public String descripcion;
    public String estado;
    public String activar;
    public String reagendado;

    // Campos adicionales solo para la mini ventana de detalle (al tocar la
    // tarjeta del evento) — no se usan en la fila resumida de la lista.
    public String codigoPdv;
    public String pdv;
    public String contacto;
    public String mail;
    public String telefono;
    public String telefonoConvencional;
    public String tecnico;
    public String latitud;
    public String longitud;
    public String fechaRegistro;
    public String lugar;

    // Local, no viene de ninguna columna: true si ya se subió evidencia de proforma
    // para este agendamiento (ver AgendaFragment.obtenerIdsAgendamientoConEvidenciaProforma).
    public boolean visitado;

    public BaseAgenda() {
    }

    public BaseAgenda(String id, String fecha, String hora, String titulo, String empresa, String descripcion,
                       String estado, String activar, String reagendado) {
        this.id = id;
        this.fecha = fecha;
        this.hora = hora;
        this.titulo = titulo;
        this.empresa = empresa;
        this.descripcion = descripcion;
        this.estado = estado;
        this.activar = activar;
        this.reagendado = reagendado;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
