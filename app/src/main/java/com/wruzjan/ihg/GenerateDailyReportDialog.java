package com.wruzjan.ihg;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Message;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;
import java.util.Date;

public class GenerateDailyReportDialog extends DialogFragment {

    private static final String ARG_CITY = "ARG_CITY";

    private City city;

    @Nullable
    private Listener listener;

    public static GenerateDailyReportDialog newInstance(@NonNull City city) {
        GenerateDailyReportDialog dialog = new GenerateDailyReportDialog();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_CITY, city);
        dialog.setArguments(bundle);
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        city = (City) getArguments().getSerializable(ARG_CITY);

        DatePickerDialog pickerDialog = new DatePickerDialog(
                requireContext(),
                0,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(view.getYear(), view.getMonth(), view.getDayOfMonth());
                        if (listener != null) {
                            listener.onReportGenerate(city, calendar.getTime());
                        }
                    }
                }, -1, -1, -1);

        pickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.generate_daily_report_cancel), Message.obtain());
        pickerDialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.generate_daily_report), Message.obtain());
        pickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

//        AlertDialog alertDialog = new AlertDialog.Builder(requireActivity())
//                .setTitle(city.toString())
//                .setView(rootView)
//                .setNegativeButton(R.string.generate_daily_report_cancel, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                })
//                .setPositiveButton(R.string.generate_daily_report, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        Calendar calendar = Calendar.getInstance();
//                        calendar.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
//                        if (listener != null) {
//                            listener.onReportGenerate(city, calendar.getTime());
//                        }
//                    }
//                })
//                .create();
//        datePicker = rootView.findViewById(R.id.date_picker);
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

    enum City {
        SIEMANOWICE,
        NOWY_PADERWSKIEGO
    }

    interface Listener {

        void onReportGenerate(@NonNull City city, @NonNull Date reportDate);
    }
}
