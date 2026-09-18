package com.luckyecuador.app.PintucoAPP.Clase;

/** Una cuota/pago ya realizado y subido, dentro de una factura a plazos. */
public class RegistroPagoFactura {

    public Integer idLocal;
    public String idRemota;
    public String idProforma;
    public String idAgendamiento;
    public String codigoPdv;
    public String usuario;
    public Integer numeroCuota;
    public Double montoPago;
    public String fotoPago;
    public String fechaPago;
    public String observacion;
    public boolean pendienteSync;
}
