package com.wruzjan.ihg.reports;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;
import java.util.Date;

public class ReportDateDialog extends DialogFragment {

    @Nullable
    private Listener listener;

    public static ReportDateDialog newInstance() {
        ReportDateDialog dialog = new ReportDateDialog();
        Bundle bundle = new Bundle();
        dialog.setArguments(bundle);
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Calendar currentDateCalendar = Calendar.getInstance();
        currentDateCalendar.setTime(new Date());

        DatePickerDialog pickerDialog = new DatePickerDialog(
                requireContext(),
                0,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(view.getYear(), view.getMonth(), view.getDayOfMonth());
                        if (listener != null) {
                            listener.onDateSelected(calendar.getTime());
                        }
                    }
                }, currentDateCalendar.get(Calendar.YEAR), currentDateCalendar.get(Calendar.MONTH), currentDateCalendar.get(Calendar.DAY_OF_MONTH));
        pickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        return pickerDialog;
    }

    @Override
    public void onPause() {
        listener = null;
        super.onPause();
    }

    public void setListener(@Nullable Listener listener) {
        this.listener = listener;
    }

    public interface Listener {

        void onDateSelected(@NonNull Date reportDate);
    }
}
