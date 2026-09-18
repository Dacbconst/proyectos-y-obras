package com.luckyecuador.app.PintucoAPP.Clase;

import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;

/**
 * Una sola visita/contacto comercial dentro del seguimiento de un agendamiento
 * (fila de insert_proforma). Un mismo agendamiento puede tener varios de estos
 * a lo largo del tiempo — igual que Exhibiciones permite varias fotos por PDV.
 */
public class RegistroProforma {

    public Integer idLocal;
    public String idRemota;
    public String fechaContacto;
    public String estadoProforma;
    public String evidencia;
    public String caracteristicaVisita;
    public String acompanamientoTecnico;
    public boolean pendienteSync;
    public int faseActual = 3;
    public String fotoFactura;
    public String fechaFactura;

    /**
     * Monto de la proforma — la misma columna que usa la oficina/web
     * (monto_validado): el promotor también puede escribirla directo desde el
     * diálogo "Detalle de la visita" (get_proforma.php ya la trae en el SELECT *).
     */
    public Double montoValidado;
    public String observacionesAuditoria;
    public String fechaAuditoria;

    /** Solo en la fila de factura: total acordado y plazo en meses (null/0 = pago único). */
    public Double montoTotalFactura;
    public Integer plazoMeses;
    public String estadoPago;

    /** Cierre manual e irreversible (ver ProformaFragment "Cerrar Proceso"): observación
     *  obligatoria que queda de la negociación de proforma sin llegar a facturar. */
    public String motivoCierre;
    /** Igual, pero para cortar el cobro de una factura a plazos antes del total cotizado. */
    public String motivoCierrePago;

    /**
     * Cambios elegidos por el usuario para una fila YA guardada, todavía sin
     * confirmar con "Guardar" (solo existen en memoria — no son el valor
     * persistido en insert_proforma todavía). Null = sin cambio pendiente en
     * ese campo. No aplican a filas borrador: ahí los campos normales
     * (estadoProforma, evidencia, etc.) cumplen ese mismo rol de "todavía sin
     * guardar", porque la fila completa todavía no existe en la base.
     */
    public String estadoPendiente;
    public String evidenciaPendiente;
    public String caracteristicaPendiente;
    public String acompanamientoPendiente;
    public String fotoFacturaPendiente;
    public String montoValidadoPendiente;

    public boolean tieneIdRemota() {
        return idRemota != null && !idRemota.trim().isEmpty();
    }

    /** true = todavía no existe en insert_proforma (fila "lista para usar", aún sin guardar). */
    public boolean esBorrador() {
        return idLocal == null;
    }

    /** true = esta fila del historial ES la factura enviada (no una ronda de proforma). */
    public boolean esFilaFactura() {
        return fotoFactura != null && !fotoFactura.trim().isEmpty();
    }

    /** true = fila ya guardada, pero con al menos un cambio elegido y sin confirmar. */
    public boolean tieneCambiosPendientes() {
        return !esBorrador() && (estadoPendiente != null || evidenciaPendiente != null
                || caracteristicaPendiente != null || acompanamientoPendiente != null
                || fotoFacturaPendiente != null || montoValidadoPendiente != null);
    }

    /**
     * true = la fila borrador tiene lo mínimo listo para guardarse: evidencia +
     * detalle (característica de la visita) + monto. El estado se auto-asigna
     * al guardar (no hay selector de estado para una ronda nueva todavía sin
     * guardar), así que no forma parte de este chequeo.
     */
    public boolean tieneLasTresSeccionesCompletas() {
        if (esFilaFactura()) {
            return true;
        }
        return evidencia != null
                && getCaracteristicaMostrado() != null && !getCaracteristicaMostrado().trim().isEmpty()
                && getMontoValidadoMostrado() != null;
    }

    public boolean esFaseRechazada() {
        return "rechazado".equalsIgnoreCase(estadoProforma) && faseActual == 3;
    }

    /** true = el analista rechazó la foto (mala calidad, no corresponde) y pide una nueva. */
    public boolean esCorreccionSolicitada() {
        return Constantes.ESTADO_PROFORMA_CORRECCION_SOLICITADA.equalsIgnoreCase(estadoProforma);
    }

    /**
     * true = fila YA guardada (tiene idLocal) pero todavía necesita una foto: o
     * es una ronda vacía recién creada por el analista tras aprobar la anterior
     * (sin evidencia todavía), o es la ronda actual rechazada esperando un
     * reenvío. Es la fila "activa" del agendamiento — no un borrador nuevo.
     * Una fila de factura (fotoFactura ya presente) nunca cuenta como "necesita
     * foto": es su propia entrada del historial, completa en sí misma.
     */
    public boolean necesitaFoto() {
        return !esBorrador() && !esFilaFactura() && (evidencia == null || esCorreccionSolicitada());
    }

    /** true = esta fila acepta cambios en memoria (foto/estado/detalle) antes de guardar. */
    public boolean esEditable() {
        return esBorrador() || necesitaFoto();
    }

    public boolean requiereFotoFactura() {
        return faseActual == 5 && (fotoFactura == null || fotoFactura.isEmpty());
    }

    public boolean tieneFotoFacturaPendiente() {
        return !esBorrador() && fotoFacturaPendiente != null;
    }

    /** true = esta factura se acordó pagar a plazos (no de una sola vez). */
    public boolean esFacturaAPlazos() {
        return plazoMeses != null && plazoMeses > 0;
    }

    /**
     * true = la auditoría/web todavía no tocó esta fila (monto_validado y
     * observaciones_auditoria siguen vacíos). Cubre el caso de subir una foto
     * al agendamiento equivocado por error: solo se puede corregir mientras
     * nadie de oficina ya validó/observó el registro.
     */
    public boolean puedeEliminarse() {
        return !esBorrador()
                && montoValidado == null
                && (observacionesAuditoria == null || observacionesAuditoria.trim().isEmpty());
    }

    public Integer getIdLocal() {
        return idLocal;
    }

    public String getFechaContacto() {
        return fechaContacto;
    }

    public String getEstadoProforma() {
        return estadoProforma;
    }

    /** El estado a mostrar en pantalla: el pendiente sin guardar si hay uno, si no el persistido. */
    public String getEstadoMostrado() {
        return estadoPendiente != null ? estadoPendiente : estadoProforma;
    }

    public String getEvidencia() {
        return evidencia;
    }

    public String getCaracteristicaVisita() {
        return caracteristicaVisita;
    }

    public String getAcompanamientoTecnico() {
        return acompanamientoTecnico;
    }

    /** La foto a mostrar: la pendiente sin guardar si hay una, si no la persistida. */
    public String getEvidenciaMostrada() {
        return evidenciaPendiente != null ? evidenciaPendiente : evidencia;
    }

    /** La característica a mostrar: la pendiente sin guardar si hay una, si no la persistida. */
    public String getCaracteristicaMostrado() {
        return caracteristicaPendiente != null ? caracteristicaPendiente : caracteristicaVisita;
    }

    /** El acompañamiento a mostrar: el pendiente sin guardar si hay uno, si no el persistido. */
    public String getAcompanamientoMostrado() {
        return acompanamientoPendiente != null ? acompanamientoPendiente : acompanamientoTecnico;
    }

    /** El monto de la proforma a mostrar: el pendiente sin guardar si hay uno, si no el persistido. */
    public String getMontoValidadoMostrado() {
        if (montoValidadoPendiente != null) {
            return montoValidadoPendiente;
        }
        return montoValidado != null ? String.valueOf(montoValidado) : null;
    }

    /** La foto de factura a mostrar: la pendiente sin guardar si hay una, si no la persistida. */
    public String getFotoFacturaMostrada() {
        return fotoFacturaPendiente != null ? fotoFacturaPendiente : fotoFactura;
    }

    public boolean isPendienteSync() {
        return pendienteSync;
    }
}
