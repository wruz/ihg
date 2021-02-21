package com.wruzjan.ihg.reports;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.wruzjan.ihg.R;
import com.wruzjan.ihg.utils.DateUtils;
import com.wruzjan.ihg.utils.FileUtils;
import com.wruzjan.ihg.utils.dao.AddressDataSource;
import com.wruzjan.ihg.utils.dao.ProtocolDataSource;
import com.wruzjan.ihg.utils.dao.ProtocolNewPaderewskiegoDataSource;
import com.wruzjan.ihg.utils.dao.StreetAndIdentifierDataSource;
import com.wruzjan.ihg.utils.threading.BaseAsyncTask;
import com.wruzjan.ihg.utils.threading.GenerateNewPaderewskiegoDailyReportAsyncTask;
import com.wruzjan.ihg.utils.threading.GenerateSiemanowiceDailyReportAsyncTask;
import com.wruzjan.ihg.utils.view.ProgressLayout;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class GenerateReportActivity extends AppCompatActivity implements BaseAsyncTask.PreExecuteUiListener, BaseAsyncTask.PostExecuteUiListener<String> {

    private static final String DATE_PICKER_FRAGMENT = "DATE_PICKER_FRAGMENT";
    private static final String EXTRA_CITY = "EXTRA_CITY";
    private static final String EXTRA_START_DATE = "EXTRA_START_DATE";
    private static final String EXTRA_END_DATE = "EXTRA_END_DATE";

    private static final int MAX_DATE_RANGE_IN_DAYS = 31;

    private ProgressLayout progressLayout;

    private AddressDataSource addressDataSource;
    private StreetAndIdentifierDataSource streetAndIdentifierDataSource;
    private ProtocolDataSource protocolDataSource;
    private ProtocolNewPaderewskiegoDataSource protocolNewPaderewskiegoDataSource;

    @Nullable
    private GenerateSiemanowiceDailyReportAsyncTask generateSiemanowiceDailyReportAsyncTask;
    @Nullable
    private GenerateNewPaderewskiegoDailyReportAsyncTask generateNewPaderewskiegoDailyReportAsyncTask;

    private Date startDate;
    private Date endDate;

    private Button startDateButton;
    private Button endDateButton;

    public static void start(
            @NonNull Context context,
            @NonNull City city,
            @NonNull Date startDate,
            @NonNull Date endDate
    ) {
        Intent intent = new Intent(context, GenerateReportActivity.class);
        intent.putExtra(EXTRA_CITY, city);
        intent.putExtra(EXTRA_START_DATE, startDate);
        intent.putExtra(EXTRA_END_DATE, endDate);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_report);

        startDate = (Date) getIntent().getSerializableExtra(EXTRA_START_DATE);
        endDate = (Date) getIntent().getSerializableExtra(EXTRA_END_DATE);

        progressLayout = findViewById(R.id.progress);
        startDateButton = findViewById(R.id.start_date_button);
        endDateButton = findViewById(R.id.end_date_button);

        assignStartDate(startDate);
        assignEndDate(endDate);

        addressDataSource = new AddressDataSource(this);
        addressDataSource.open();

        streetAndIdentifierDataSource = new StreetAndIdentifierDataSource(this);
        streetAndIdentifierDataSource.open();

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
        addressDataSource.close();
        streetAndIdentifierDataSource.close();
        protocolDataSource.close();
        protocolNewPaderewskiegoDataSource.close();
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

    public void selectStartDate(@NonNull View view) {
        ReportDateDialog dialog = (ReportDateDialog) getSupportFragmentManager().findFragmentByTag(DATE_PICKER_FRAGMENT);
        if (dialog == null) {
            dialog = ReportDateDialog.newInstance(startDate);
        }
        dialog.setListener(new ReportDateDialog.Listener() {
            @Override
            public void onDateSelected(@NonNull Date reportDate) {
                assignStartDate(reportDate);
            }
        });
        dialog.show(getSupportFragmentManager(), DATE_PICKER_FRAGMENT);
    }

    public void selectEndDate(@NonNull View view) {
        ReportDateDialog dialog = (ReportDateDialog) getSupportFragmentManager().findFragmentByTag(DATE_PICKER_FRAGMENT);
        if (dialog == null) {
            dialog = ReportDateDialog.newInstance(endDate);
        }
        dialog.setListener(new ReportDateDialog.Listener() {
            @Override
            public void onDateSelected(@NonNull Date reportDate) {
                assignEndDate(reportDate);
            }
        });
        dialog.show(getSupportFragmentManager(), DATE_PICKER_FRAGMENT);
    }

    public void generateReportFromDateRange(@NonNull View view) {
        if (startDate.after(endDate)) {
            Toast.makeText(this, getString(R.string.start_date_greater_than_end_date_error_message), Toast.LENGTH_SHORT).show();
            return;
        } else if (isGreaterThanMaxDateRange()) {
            Toast.makeText(this, getString(R.string.range_freater_than_max_range_error_message), Toast.LENGTH_SHORT).show();
            return;
        }

        City city = (City) getIntent().getSerializableExtra(EXTRA_CITY);
        if (city != null) {
            switch (city) {
                case SIEMANOWICE:
                    if (generateSiemanowiceDailyReportAsyncTask != null) {
                        generateSiemanowiceDailyReportAsyncTask.cancel(true);
                    }
                    generateSiemanowiceDailyReportAsyncTask = new GenerateSiemanowiceDailyReportAsyncTask(getApplication(), addressDataSource, streetAndIdentifierDataSource, protocolDataSource);
                    generateSiemanowiceDailyReportAsyncTask.setPreExecuteUiListener(this);
                    generateSiemanowiceDailyReportAsyncTask.setPostExecuteUiListener(this);
                    generateSiemanowiceDailyReportAsyncTask.execute(startDate, endDate);
                    break;
                case NOWY_PADERWSKIEGO:
                    if (generateNewPaderewskiegoDailyReportAsyncTask != null) {
                        generateNewPaderewskiegoDailyReportAsyncTask.cancel(true);
                    }
                    generateNewPaderewskiegoDailyReportAsyncTask = new GenerateNewPaderewskiegoDailyReportAsyncTask(getApplication(), addressDataSource, streetAndIdentifierDataSource, protocolNewPaderewskiegoDataSource);
                    generateNewPaderewskiegoDailyReportAsyncTask.setPreExecuteUiListener(this);
                    generateNewPaderewskiegoDailyReportAsyncTask.setPostExecuteUiListener(this);
                    generateNewPaderewskiegoDailyReportAsyncTask.execute(startDate, endDate);
                    break;
            }
        }
    }

    private void assignStartDate(@NonNull Date reporStarttDate) {
        startDateButton.setText(getString(R.string.generate_report_from_date, DateUtils.DATABASE_DATE_FORMAT.format(reporStarttDate)));
        startDate = DateUtils.getDateOnlyCalendar(reporStarttDate).getTime();
    }

    private void assignEndDate(@NonNull Date reportEndDate) {
        endDateButton.setText(getString(R.string.generate_report_to_date, DateUtils.DATABASE_DATE_FORMAT.format(reportEndDate)));
        endDate = DateUtils.getDateOnlyCalendar(reportEndDate).getTime();
    }

    private boolean isGreaterThanMaxDateRange() {
        long rangeMillis = endDate.getTime() - startDate.getTime();
        return TimeUnit.DAYS.convert(rangeMillis, TimeUnit.MILLISECONDS) > MAX_DATE_RANGE_IN_DAYS;
    }

    public enum City {
        SIEMANOWICE,
        NOWY_PADERWSKIEGO
    }
}
