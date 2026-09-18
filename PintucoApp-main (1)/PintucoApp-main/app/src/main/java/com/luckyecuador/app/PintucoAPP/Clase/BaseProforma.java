package com.luckyecuador.app.PintucoAPP.Clase;

import com.luckyecuador.app.PintucoAPP.Conexion.Constantes;

import java.util.ArrayList;
import java.util.List;

/**
 * Tarjeta de "Gestión de Visitas" del módulo Proforma: un agendamiento de
 * insert_proyectos_contacto (ya confirmado por el analista, con hora y técnico)
 * y su lista de seguimiento comercial (una o varias filas de insert_proforma,
 * cada una representando una visita/contacto distinto a lo largo del tiempo).
 */
public class BaseProforma {

    // Datos del agendamiento (insert_proyectos_contacto)
    public String idAgendamiento;
    public String codigoPdv;
    public String usuario;
    public String fechaContactoCreacion;
    public String fechaAgenda;
    public String empresa;
    public String lugar;
    public String direccion;
    public String latitud;
    public String longitud;
    // PDV elegido en "Seleccione PDV" y su ciudad — usados solo para la marca de
    // agua de las fotos (evidencia/factura), no para el encabezado de la tarjeta
    // (ese sigue usando "lugar", que es el campo que edita la web).
    public String pdv;
    public String ciudadPdv;
    // estado_agenda (pendiente/vencida/...), para vencidaSinSubir()
    public String estadoAgenda;
    // "SI" = nunca pasó por Agenda (no se le asignó fecha/técnico) — el
    // encabezado muestra "No requirió" en vez de fecha_agendamiento.
    public boolean noRequiereVisita;

    // Seguimiento comercial: ordenado de la visita más antigua a la más reciente.
    public List<RegistroProforma> registros = new ArrayList<>();

    public String getIdAgendamiento() {
        return idAgendamiento;
    }

    public String getEmpresa() {
        return empresa;
    }

    public String getLugar() {
        return lugar;
    }

    /**
     * "Lugar" a mostrar en la tarjeta: el que edita la web (lugar) si ya lo
     * llenó, si no la dirección que el promotor tipeó al registrar el contacto
     * (direccion). Sin este respaldo, un contacto "sin requerir visita" (que
     * nunca pasa por Agenda, el único lugar donde alguien más edita "lugar")
     * se veía con el encabezado vacío aunque la dirección sí se había
     * guardado — solo estaba en la columna equivocada para lo que se muestra.
     */
    public String getLugarMostrado() {
        return (lugar != null && !lugar.trim().isEmpty()) ? lugar : direccion;
    }

    public String getFechaAgenda() {
        return fechaAgenda;
    }

    public boolean isNoRequiereVisita() {
        return noRequiereVisita;
    }

    public List<RegistroProforma> getRegistros() {
        return registros;
    }

    /** Última fila guardada (los borradores no cuentan) — null si aún no hay ninguna. */
    public RegistroProforma getUltimoRegistroGuardado() {
        RegistroProforma ultimo = null;
        for (RegistroProforma r : registros) {
            if (!r.esBorrador()) {
                ultimo = r;
            }
        }
        return ultimo;
    }

    /**
     * Fase actual de la tarjeta = la del último registro YA GUARDADO (los
     * borradores no cuentan). Se usa para el filtro Activos/Completados y
     * para el diálogo de info de fase del encabezado.
     */
    public int getFaseActual() {
        RegistroProforma ultimo = getUltimoRegistroGuardado();
        return ultimo != null ? ultimo.faseActual : 3;
    }

    /**
     * true = la última fila guardada (los borradores no cuentan) es la
     * factura recién enviada — pill "Completado" en el header y en el filtro
     * de pills. No es un cierre permanente: en cuanto se guarda una ronda
     * nueva sobre este mismo agendamiento (para facturarle otra vez en un mes
     * futuro), la última fila deja de ser la factura y la tarjeta vuelve a
     * "Activos" — ver AdapterProforma.bind() para cómo se habilita esa ronda
     * nueva incluso con el ciclo ya "completado".
     */
    public boolean estaCompletado() {
        RegistroProforma ultimo = getUltimoRegistroGuardado();
        return ultimo != null && ultimo.esFilaFactura();
    }

    /**
     * La última ronda que de verdad está cotizada (con evidencia Y
     * monto_validado, las dos) — se resetea a null en cada fila de factura,
     * esa cotización ya se gastó. A propósito exige monto_validado acá (a
     * diferencia de AdapterProforma.ultimaFilaVisible, que solo mira si
     * necesitaFoto() para habilitar el botón "Enviar factura"): si mientras el
     * panel de "Confirmar factura" está abierto llega de fondo una ronda nueva
     * ya con foto pero TODAVÍA sin validar por oficina, esa ronda sin monto no
     * debe tapar la cotización anterior — sin este chequeo extra, guardarFotoFactura
     * terminaba copiando una ronda sin monto y caía al respaldo (foto/monto
     * tecleados a mano en vez del cotizado real).
     */
    public RegistroProforma getUltimaRondaCotizada() {
        RegistroProforma ultimaCotizada = null;
        for (RegistroProforma r : registros) {
            if (r.esBorrador()) {
                continue;
            }
            if (r.esFilaFactura()) {
                ultimaCotizada = null;
                continue;
            }
            if (r.evidencia != null && r.montoValidado != null) {
                ultimaCotizada = r;
            }
        }
        return ultimaCotizada;
    }

    /**
     * true = alguna fila de este agendamiento tiene "Cierre Proforma" (motivo_cierre)
     * — la negociación se dio por cerrada sin llegar a facturar. Revisa todas las
     * filas (no solo la última) porque el cierre queda escrito en la última fila
     * guardada AL MOMENTO de cerrar, que no es necesariamente la misma que
     * getUltimoRegistroGuardado() una vez que el sync trae filas nuevas después.
     */
    public boolean proformaCerrada() {
        for (RegistroProforma r : registros) {
            if (r.motivoCierre != null && !r.motivoCierre.trim().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    /** true = venció en Agenda y nunca se subió foto acá — pill "Vencidas". */
    public boolean vencidaSinSubir() {
        if (!Constantes.ESTADO_AGENDA_VENCIDA.equalsIgnoreCase(estadoAgenda)) {
            return false;
        }
        for (RegistroProforma r : registros) {
            if (!r.esBorrador() && r.evidencia != null && !r.evidencia.trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }
}
