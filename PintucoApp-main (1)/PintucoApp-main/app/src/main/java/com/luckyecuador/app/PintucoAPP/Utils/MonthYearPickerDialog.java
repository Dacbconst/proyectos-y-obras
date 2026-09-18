package com.luckyecuador.app.PintucoAPP.Utils;


import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.DatePicker;
import java.util.Calendar;

public class MonthYearPickerDialog {
    private DatePickerDialog datePickerDialog;
    private DatePickerDialog.OnDateSetListener dateSetListener;


    public MonthYearPickerDialog(Context context, DatePickerDialog.OnDateSetListener listener) {
        this.dateSetListener = listener;
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        datePickerDialog = new DatePickerDialog(context, listener, year, month, 1);
        datePickerDialog.getDatePicker().setCalendarViewShown(false);

        View vistaDia = datePickerDialog.getDatePicker()
                .findViewById(context.getResources().getIdentifier("day", "id", "android"));
        if (vistaDia != null) {
            vistaDia.setVisibility(View.GONE);
        }
    }

    public void show() {
        datePickerDialog.show();
    }

}
