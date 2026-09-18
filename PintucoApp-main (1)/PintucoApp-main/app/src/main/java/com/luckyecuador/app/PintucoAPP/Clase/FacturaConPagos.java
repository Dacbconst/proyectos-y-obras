package com.luckyecuador.app.PintucoAPP.Clase;

import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;

import java.util.ArrayList;
import java.util.List;

/**
 * Una factura (fila de insert_proforma con foto_factura) — es el modelo del
 * módulo "Facturas" (item_factura_card.xml), distinto de
 * BaseProforma/RegistroProforma que arman la pestaña "Gestión de Visitas".
 *
 * Dos tipos, según {@code plazoMeses} (ver esAPlazos()):
 * - Directa ("Factura"): evento único — {@code fotoFactura}/{@code montoTotalFactura}
 *   son la foto/monto reales que el promotor capturó, sin más. Sin lista, sin
 *   botón para agregar más.
 * - A plazos ("Factura Plazos", sin límite de cantidad — ver
 *   AdapterFacturas.onAdjuntarPago): acá {@code fotoFactura}/{@code montoTotalFactura}
 *   son el cotizado (última evidencia/monto_validado de Proforma), copiados
 *   automáticamente al confirmar (ver ProformaFragment.guardarFotoFactura()).
 *   El comprobante real que el promotor capturó vive aparte en {@code pagos}
 *   (insert_pago_factura) — la primera la sube Proforma sola al confirmar.
 */
public class FacturaConPagos {

    public String idAgendamiento;
    /** id_remota de la fila de insert_proforma que es la factura — usado como id_proforma al subir un pago (legado). */
    public String idRemotaFactura;
    /** _ID local del cursor — clave de acordeón estable incluso antes de sincronizar (aún sin idRemotaFactura). */
    public Integer idLocal;
    public String fechaAgenda;
    // "SI" = el agendamiento nunca pasó por Agenda (no se le asignó fecha/técnico)
    // — la tarjeta muestra "No requirió" en vez de fechaAgenda. No afecta el
    // filtro de mes (ese usa fechaFactura/fecha_proforma, ver estaEnMesFiltrado).
    public boolean noRequiereVisita;
    public String empresa;
    public String lugar;
    // PDV elegido en "Seleccione PDV" y su ciudad — usados solo para la marca de
    // agua de la foto de pago, no para el encabezado (ese sigue usando "lugar").
    public String pdv;
    public String ciudadPdv;
    public String codigoPdv;
    public String usuario;
    public String fotoFactura;
    public String fechaFactura;
    public Double montoTotalFactura;
    public Integer plazoMeses;
    public String estadoPago;
    /** Cierre manual e irreversible (ver ProformaFragment/FacturasFragment "Cerrar Proceso"). */
    public String motivoCierrePago;
    public List<RegistroPagoFactura> pagos = new ArrayList<>();

    /**
     * true = cerrada a mano desde "Cierre Factura" (irreversible), sin importar si llegó al
     * total cotizado — desde este celular (motivo_cierre_pago local) o desde la web (bajó
     * estado_pago=cerrado en el sync, sin que este celular haya escrito el motivo).
     */
    public boolean estaCerradaManualmente() {
        return (motivoCierrePago != null && !motivoCierrePago.trim().isEmpty())
                || Constantes.ESTADO_PAGO_CERRADO.equalsIgnoreCase(estadoPago);
    }

    /** true = la suma de comprobantes llegó sola al total cotizado, SIN haberse cerrado a mano. */
    public boolean estaCompletadaAutomaticamente() {
        return !estaCerradaManualmente() && Constantes.ESTADO_PAGO_COMPLETADO.equalsIgnoreCase(estadoPago);
    }

    /** true = ya no admite más cuotas: cerrada a mano, o la suma de pagos ya alcanzó el total cotizado. */
    public boolean estaCerrada() {
        return estaCerradaManualmente() || estaCompletadaAutomaticamente();
    }

    public double getMontoPagado() {
        double total = 0;
        for (RegistroPagoFactura p : pagos) {
            if (p.montoPago != null) {
                total += p.montoPago;
            }
        }
        return total;
    }

    public boolean esAPlazos() {
        return plazoMeses != null && plazoMeses > 0;
    }

    /**
     * Clave estable para el acordeón de FacturasFragment: un mismo agendamiento
     * puede tener varias facturas ahora, así que ya no sirve idAgendamiento solo.
     * Prioriza idLocal (existe siempre, incluso antes de sincronizar) sobre
     * idRemotaFactura (llega recién tras el primer sync).
     */
    public String claveUnica() {
        if (idLocal != null) {
            return "L" + idLocal;
        }
        if (idRemotaFactura != null) {
            return "R" + idRemotaFactura;
        }
        return idAgendamiento;
    }

    /** Número de orden de la próxima factura a subir — sin límite ni relación con plazoMeses. */
    public int siguienteNumeroCuota() {
        return pagos.size() + 1;
    }
}
