package com.luckyecuador.app.PintucoAPP.Clase;

/**
 * Estado en memoria de una factura que se está armando dentro del panel
 * "Confirmar factura" (foto + monto de lo que se cobra esta vez) antes de
 * guardarla de verdad — vive en el Fragment (no en el ViewHolder) para
 * sobrevivir a un recargue de la lista, igual que {@link RegistroProforma}
 * usa un mapa de "pendientes" aparte para lo mismo.
 */
public class FacturaEnProgreso {
    public String fotoBase64;
    public String montoTotalFactura;
    // null = el promotor todavía no eligió directo/a plazos — no se asume ninguno por defecto.
    public Boolean pagoAPlazos;
    public Integer plazoMeses;
    // Si el monto que se ve ahora mismo en el campo es la propuesta automática
    // (último monto validado de la proforma) y no algo que el promotor ya haya
    // tecleado — controla si se pinta en gris (sugerencia) o en el color normal.
    public boolean montoTotalFacturaEsSugerido;
    // Se marca en true la primera vez que se propone un monto, para no volver a
    // insistir con la sugerencia si el promotor la borra a propósito.
    public boolean montoSugeridoYaPropuesto;
}
