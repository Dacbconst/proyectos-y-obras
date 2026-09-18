package com.luckyecuador.app.PintucoAPP;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

/**
 * Created by Lucky Ecuador on 10/01/2017.
 * Fragmento con un diálogo de elección de fechas
 */

public class DialogoFecha extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        int anio = calendar.get(Calendar.YEAR);
        int mes = calendar.get(Calendar.MONTH);
        int dia = calendar.get(Calendar.DAY_OF_MONTH);

        //Retornar la instancia creada del dialogo
        return new DatePickerDialog(
                getActivity(),
                (DatePickerDialog.OnDateSetListener) getActivity(),
                anio,
                mes,
                dia
        );
    }
}
