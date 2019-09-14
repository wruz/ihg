package com.wruzjan.ihg;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class GenerateDailyReportDialog extends DialogFragment {

    private static final String ARG_CITY = "ARG_CITY";

    private DatePicker datePicker;
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
        View rootView = View.inflate(requireContext(), R.layout.fragment_generate_daily_report, null);

        city = (City) getArguments().getSerializable(ARG_CITY);

        AlertDialog alertDialog = new AlertDialog.Builder(requireActivity())
                .setTitle(city.toString())
                .setView(rootView)
                .setNegativeButton(R.string.generate_daily_report_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(R.string.generate_daily_report, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
                        if (listener != null) {
                            listener.onReportGenerate(city, calendar.getTime());
                        }
                    }
                })
                .create();
        datePicker = rootView.findViewById(R.id.date_picker);
        datePicker.setMaxDate(System.currentTimeMillis());
        return alertDialog;
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
