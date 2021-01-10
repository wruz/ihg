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

    private static final String ARG_SELECTED_DATE = "ARG_SELECTED_DATE";

    @Nullable
    private Listener listener;

    public static ReportDateDialog newInstance(@NonNull Date selectedDate) {
        ReportDateDialog dialog = new ReportDateDialog();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_SELECTED_DATE, selectedDate);
        dialog.setArguments(bundle);
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Calendar selectedDateCalendar = Calendar.getInstance();
        selectedDateCalendar.setTime((Date) requireArguments().getSerializable(ARG_SELECTED_DATE));

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
                }, selectedDateCalendar.get(Calendar.YEAR), selectedDateCalendar.get(Calendar.MONTH), selectedDateCalendar.get(Calendar.DAY_OF_MONTH));
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
