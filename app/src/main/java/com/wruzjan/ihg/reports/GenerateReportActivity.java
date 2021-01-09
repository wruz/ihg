package com.wruzjan.ihg.reports;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.wruzjan.ihg.R;
import com.wruzjan.ihg.utils.FileUtils;
import com.wruzjan.ihg.utils.dao.AddressDataSource;
import com.wruzjan.ihg.utils.dao.ProtocolDataSource;
import com.wruzjan.ihg.utils.dao.ProtocolNewPaderewskiegoDataSource;
import com.wruzjan.ihg.utils.threading.BaseAsyncTask;
import com.wruzjan.ihg.utils.threading.GenerateNewPaderewskiegoDailyReportAsyncTask;
import com.wruzjan.ihg.utils.threading.GenerateSiemanowiceDailyReportAsyncTask;
import com.wruzjan.ihg.utils.view.ProgressLayout;

import java.util.Date;

public class GenerateReportActivity extends AppCompatActivity implements GenerateDailyReportDialog.Listener, BaseAsyncTask.PreExecuteUiListener, BaseAsyncTask.PostExecuteUiListener<String> {

    private static final String DATE_PICKER_FRAGMENT = "DATE_PICKER_FRAGMENT";
    private static final String EXTRA_CITY = "EXTRA_CITY";

    private ProgressLayout progressLayout;

    private AddressDataSource addressDataSource;
    private ProtocolDataSource protocolDataSource;
    private ProtocolNewPaderewskiegoDataSource protocolNewPaderewskiegoDataSource;

    @Nullable
    private GenerateSiemanowiceDailyReportAsyncTask generateSiemanowiceDailyReportAsyncTask;
    @Nullable
    private GenerateNewPaderewskiegoDailyReportAsyncTask generateNewPaderewskiegoDailyReportAsyncTask;

    public static void start(@NonNull Context context, @NonNull City city) {
        Intent intent = new Intent(context, GenerateReportActivity.class);
        intent.putExtra(EXTRA_CITY, city);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_report);

        progressLayout = findViewById(R.id.progress);


        addressDataSource = new AddressDataSource(this);
        addressDataSource.open();

        protocolNewPaderewskiegoDataSource = new ProtocolNewPaderewskiegoDataSource(this);
        protocolNewPaderewskiegoDataSource.open();

        protocolDataSource = new ProtocolDataSource(this);
        protocolDataSource.open();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (generateSiemanowiceDailyReportAsyncTask != null) {
            generateSiemanowiceDailyReportAsyncTask.setPreExecuteUiListener(null);
            generateSiemanowiceDailyReportAsyncTask.setPostExecuteUiListener(null);
        }

        if (generateNewPaderewskiegoDailyReportAsyncTask != null) {
            generateNewPaderewskiegoDailyReportAsyncTask.setPreExecuteUiListener(null);
            generateNewPaderewskiegoDailyReportAsyncTask.setPostExecuteUiListener(null);
        }
        addressDataSource.close();
        protocolDataSource.close();
        protocolNewPaderewskiegoDataSource.close();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (generateSiemanowiceDailyReportAsyncTask != null) {
            generateSiemanowiceDailyReportAsyncTask.cancel(true);
        }
        if (generateNewPaderewskiegoDailyReportAsyncTask != null) {
            generateNewPaderewskiegoDailyReportAsyncTask.cancel(true);
        }
    }

    @Override
    public void onReportGenerate(@NonNull Date reportDate) {
        City city = (City) getIntent().getSerializableExtra(EXTRA_CITY);
        if (city != null) {
            switch (city) {
                case SIEMANOWICE:
                    if (generateSiemanowiceDailyReportAsyncTask != null) {
                        generateSiemanowiceDailyReportAsyncTask.cancel(true);
                    }
                    generateSiemanowiceDailyReportAsyncTask = new GenerateSiemanowiceDailyReportAsyncTask(addressDataSource, protocolDataSource);
                    generateSiemanowiceDailyReportAsyncTask.setPreExecuteUiListener(this);
                    generateSiemanowiceDailyReportAsyncTask.setPostExecuteUiListener(this);
                    generateSiemanowiceDailyReportAsyncTask.execute(reportDate);
                    break;
                case NOWY_PADERWSKIEGO:
                    if (generateNewPaderewskiegoDailyReportAsyncTask != null) {
                        generateNewPaderewskiegoDailyReportAsyncTask.cancel(true);
                    }
                    generateNewPaderewskiegoDailyReportAsyncTask = new GenerateNewPaderewskiegoDailyReportAsyncTask(addressDataSource, protocolNewPaderewskiegoDataSource);
                    generateNewPaderewskiegoDailyReportAsyncTask.setPreExecuteUiListener(this);
                    generateNewPaderewskiegoDailyReportAsyncTask.setPostExecuteUiListener(this);
                    generateNewPaderewskiegoDailyReportAsyncTask.execute(reportDate);
                    break;
            }
        }
    }

    @Override
    public void onPreExecute() {
        progressLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPostExecute(@NonNull String reportFilePath) {
        progressLayout.setVisibility(View.GONE);

        if (!reportFilePath.isEmpty()) {
            Uri uri = FileUtils.getUriFromFile(this, reportFilePath);

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_STREAM, uri);

            try {
                startActivity(Intent.createChooser(intent, "Wybierz aplikację Dropbox"));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(this, "Brak klienta Dropbox na urządzeniu.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Brak protokołów z tego dnia", Toast.LENGTH_SHORT).show();
        }
    }

    public void generateReportFromDateRange(@NonNull View view) {
        GenerateDailyReportDialog dialog = (GenerateDailyReportDialog) getSupportFragmentManager().findFragmentByTag(DATE_PICKER_FRAGMENT);
        if (dialog == null) {
            dialog = GenerateDailyReportDialog.newInstance();
        }
        dialog.setListener(this);
        dialog.show(getSupportFragmentManager(), DATE_PICKER_FRAGMENT);
    }

    public enum City {
        SIEMANOWICE,
        NOWY_PADERWSKIEGO
    }
}
