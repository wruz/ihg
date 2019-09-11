package com.wruzjan.ihg;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.wruzjan.ihg.utils.dao.AddressDataSource;
import com.wruzjan.ihg.utils.dao.ProtocolDataSource;
import com.wruzjan.ihg.utils.threading.BaseAsyncTask;
import com.wruzjan.ihg.utils.threading.GenerateSiemanowiceDailyReportAsyncTask;

import java.io.File;
import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;

public class GenerateDailyReportDialog extends DialogFragment implements BaseAsyncTask.UiListener<String> {

    private static final String ARG_CITY = "ARG_CITY";

    private DatePicker datePicker;
    private GenerateSiemanowiceDailyReportAsyncTask generateSiemanowiceDailyReportAsyncTask;
    private AddressDataSource addressDataSource;
    private ProtocolDataSource protocolDataSource;

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

        AlertDialog alertDialog = new AlertDialog.Builder(requireActivity())
                .setTitle(getArguments().getSerializable(ARG_CITY).toString())
                .setView(rootView)
                .setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("Generuj raport dzienny", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
                        generateSiemanowiceDailyReportAsyncTask.execute(calendar.getTime());
                    }
                })
                .create();
        datePicker = rootView.findViewById(R.id.date_picker);
        datePicker.setMaxDate(System.currentTimeMillis());

        addressDataSource = new AddressDataSource(getContext());
        addressDataSource.open();
        protocolDataSource = new ProtocolDataSource(getContext());
        protocolDataSource.open();
        generateSiemanowiceDailyReportAsyncTask = new GenerateSiemanowiceDailyReportAsyncTask(addressDataSource, protocolDataSource);
        generateSiemanowiceDailyReportAsyncTask.setUiListener(this);
        return alertDialog;
    }

    @Override
    public void onResume() {
        super.onResume();
        addressDataSource.open();
        protocolDataSource.open();
        generateSiemanowiceDailyReportAsyncTask.setUiListener(this);
    }

    @Override
    public void onPause() {
        generateSiemanowiceDailyReportAsyncTask.setUiListener(null);
        addressDataSource.close();
        protocolDataSource.close();
        super.onPause();
    }

    @Override
    public void onPostExecute(@NonNull String reportFilePath) {
        Uri uri =  FileProvider.getUriForFile(requireActivity(), "com.ihg.fileprovider", new File(reportFilePath));

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_STREAM, uri);

        try {
            startActivity(Intent.createChooser(intent, "Wybierz aplikację Dropbox"));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getActivity(), "Brak klienta Dropbox na urządzeniu.", Toast.LENGTH_SHORT).show();
        }
    }

    enum City {
        SIEMANOWICE,
        NOWY_PADERWSKIEGO
    }
}
