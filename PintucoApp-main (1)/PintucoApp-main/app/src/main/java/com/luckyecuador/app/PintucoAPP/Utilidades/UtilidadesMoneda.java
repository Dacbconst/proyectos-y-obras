package com.luckyecuador.app.PintucoAPP.Utilidades;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.function.Consumer;

/**
 * Formato de moneda compartido por los campos de monto de Proforma (precio
 * propuesto, monto total de factura) — mismo criterio en la tarjeta
 * (AdapterProforma) y en los diálogos (ProformaFragment).
 */
public class UtilidadesMoneda {

    private UtilidadesMoneda() {
        // Sin instancias
    }

    /** Extrae el valor numérico de un texto que puede venir con "$"/"," (ej. "$1,234.56"). */
    public static Double parsearMonedaSegura(String texto) {
        if (texto == null) {
            return null;
        }
        String limpio = texto.replaceAll("[^\\d.]", "");
        if (limpio.isEmpty()) {
            return null;
        }
        try {
            return Double.parseDouble(limpio);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /** "1234.56"/"$1,234.56"/null → "$1,234.56", o "" si no hay un monto válido — solo para MOSTRAR (histórico, totales, no editable). */
    public static String formatearComoMoneda(String textoOMonto) {
        Double valor = parsearMonedaSegura(textoOMonto);
        return valor == null ? "" : NumberFormat.getCurrencyInstance(Locale.US).format(valor);
    }

    public static String formatearComoMoneda(double valor) {
        return NumberFormat.getCurrencyInstance(Locale.US).format(valor);
    }

    /**
     * Igual que {@link #formatearComoMoneda(String)} pero sin separador de miles
     * ni forzar 2 decimales — para PRECARGAR un campo editable (el usuario sigue
     * escribiendo después, y no tiene sentido que le aparezcan comas que él
     * nunca tecleó).
     */
    public static String formatearParaEditar(String textoOMonto) {
        Double valor = parsearMonedaSegura(textoOMonto);
        return valor == null ? "" : formatearParaEditar(valor.doubleValue());
    }

    public static String formatearParaEditar(double valor) {
        String numero = valor == Math.floor(valor) ? String.valueOf((long) valor) : String.valueOf(valor);
        return "$" + numero;
    }

    /**
     * Redondeo a 3 decimales — precisión interna para cálculos (repartir un
     * total entre cuotas, sumar pagos, etc.). La pantalla siempre muestra 2
     * decimales; este tercero solo existe para que esas cuentas no arrastren
     * más ruido de punto flotante del necesario.
     */
    public static Double redondearInterno(Double valor) {
        return valor == null ? null : Math.round(valor * 1000.0) / 1000.0;
    }

    /** "$258.4" → "$258.40" — para dejar siempre 2 decimales visibles al terminar de escribir. */
    private static String formatearDosDecimales(double valor) {
        return String.format(Locale.US, "$%.2f", valor);
    }

    /**
     * Solo antepone "$" a lo que el usuario va escribiendo — respeta el punto
     * decimal tal como lo tipea (ej. escribir "2450.2" queda "$2450.2", no lo
     * reinterpreta como centavos). Al perder el foco, completa a 2 decimales
     * (escribir "258" queda "$258.00" apenas se sale del campo).
     * {@code onCambio} recibe el valor numérico ya extraído, redondeado a 3
     * decimales de precisión interna (null si el campo quedó vacío).
     */
    public static void aplicarFormatoMoneda(EditText et, Consumer<Double> onCambio) {
        et.addTextChangedListener(new TextWatcher() {
            private boolean actualizando = false;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (actualizando) {
                    return;
                }
                actualizando = true;

                String actual = s.toString();
                String numero = actual.replace("$", "");
                String nuevoTexto = numero.isEmpty() ? "" : "$" + numero;
                if (!nuevoTexto.equals(actual)) {
                    et.setText(nuevoTexto);
                    et.setSelection(nuevoTexto.length());
                }

                actualizando = false;
                onCambio.accept(redondearInterno(parsearMonedaSegura(numero)));
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        et.setOnFocusChangeListener((v, tieneFoco) -> {
            if (!tieneFoco) {
                Double valor = parsearMonedaSegura(et.getText().toString());
                et.setText(valor == null ? "" : formatearDosDecimales(valor));
            }
        });
    }
}
