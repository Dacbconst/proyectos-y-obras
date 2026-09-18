package com.luckyecuador.app.PintucoAPP.Utilidades;

import android.text.InputFilter;
import android.text.Spanned;

public class DecimalDigitsInputFilter implements InputFilter {

    private final int maxDigitsBeforeDecimalPoint;
    private final int maxDigitsAfterDecimalPoint;

    public DecimalDigitsInputFilter(int maxDigitsBeforeDecimalPoint, int maxDigitsAfterDecimalPoint) {
        this.maxDigitsBeforeDecimalPoint = maxDigitsBeforeDecimalPoint;
        this.maxDigitsAfterDecimalPoint = maxDigitsAfterDecimalPoint;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        StringBuilder builder = new StringBuilder(dest);
        builder.replace(dstart, dend, source.subSequence(start, end).toString());
        if (!builder.toString().matches(
                "(\\d{0," + maxDigitsBeforeDecimalPoint + "})(\\.\\d{0," + maxDigitsAfterDecimalPoint + "})?"
        )) {
            if (source.length() == 0)
                return dest.subSequence(dstart, dend);
            return "";
        }
        return null;
    }
}