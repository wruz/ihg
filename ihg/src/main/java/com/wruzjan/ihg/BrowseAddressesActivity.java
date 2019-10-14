package com.wruzjan.ihg;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.DropBoxManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.wruzjan.ihg.utils.AlertUtils;
import com.wruzjan.ihg.utils.NavigationUtils;
import com.wruzjan.ihg.utils.Utils;
import com.wruzjan.ihg.utils.dao.AddressDataSource;
import com.wruzjan.ihg.utils.dao.AwaitingProtocolDataSource;
import com.wruzjan.ihg.utils.dao.ProtocolDataSource;
import com.wruzjan.ihg.utils.dao.ProtocolNewPaderewskiegoDataSource;
import com.wruzjan.ihg.utils.dao.ProtocolPaderewskiegoDataSource;
import com.wruzjan.ihg.utils.model.Address;
import com.wruzjan.ihg.utils.model.AwaitingProtocol;
import com.wruzjan.ihg.utils.threading.BaseAsyncTask;
import com.wruzjan.ihg.utils.threading.GenerateNewPaderewskiegoDailyReportAsyncTask;
import com.wruzjan.ihg.utils.threading.GenerateSiemanowiceDailyReportAsyncTask;
import com.wruzjan.ihg.utils.view.ProgressLayout;

import java.io.File;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

public class BrowseAddressesActivity extends AppCompatActivity implements GenerateDailyReportDialog.Listener, BaseAsyncTask.PreExecuteUiListener, BaseAsyncTask.PostExecuteUiListener<String> {

    public static final String DAILY_REPORT_SIEMANOWICE_FRAGMENT = "DAILY_REPORT_SIEMANOWICE_FRAGMENT";
    public static final String DAILY_REPORT_NEW_PADERWSKIEGO_FRAGMENT = "DAILY_REPORT_NEW_PADERWSKIEGO_FRAGMENT";
    private AddressDataSource datasource;
    private ProtocolDataSource protocolDataSource;
    private ProtocolPaderewskiegoDataSource protocolPaderewskiegoDataSource;
    private ProtocolNewPaderewskiegoDataSource protocolNewPaderewskiegoDataSource;
    private AwaitingProtocolDataSource awaitingProtocolDataSource;
    private ListView addressesList;

    private int selectedPosition = 0;
    private ArrayAdapter<Address> adapter;

    @Nullable private GenerateSiemanowiceDailyReportAsyncTask generateSiemanowiceDailyReportAsyncTask;
    @Nullable private GenerateNewPaderewskiegoDailyReportAsyncTask generateNewPaderewskiegoDailyReportAsyncTask;

    private ProgressLayout progressLayout;
    private Button synchronizeProtocolsButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_browse_addresses);

        progressLayout = findViewById(R.id.progress);
        synchronizeProtocolsButton = findViewById(R.id.synchronize_all_protocols_button);
        synchronizeProtocolsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<AwaitingProtocol> awaitingProtocols = awaitingProtocolDataSource.getAwaitingProtocols();
                NavigationUtils.openDropBoxApp(BrowseAddressesActivity.this, awaitingProtocols);
            }
        });

        datasource = new AddressDataSource(this);
        datasource.open();

        protocolDataSource = new ProtocolDataSource(this);
        protocolDataSource.open();

        awaitingProtocolDataSource = new AwaitingProtocolDataSource(this);
        awaitingProtocolDataSource.open();
        protocolPaderewskiegoDataSource = new ProtocolPaderewskiegoDataSource(this);
        protocolPaderewskiegoDataSource.open();

        protocolNewPaderewskiegoDataSource = new ProtocolNewPaderewskiegoDataSource(this);
        protocolNewPaderewskiegoDataSource.open();

        addressesList = findViewById(R.id.addresses_list);
        EditText inputSearch = findViewById(R.id.inputSearch);

        List<Address> values = datasource.getAllAddresses();

        // Use the built-in layout for showing a list item with a single
        // line of text whose background is changes when activated.
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_activated_1, values);

        addressesList.setAdapter(adapter);

        if (adapter.isEmpty()) {
            Context context = getApplicationContext();
            CharSequence text = AlertUtils.NO_ADDRESSES;
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }

        addressesList.setTextFilterEnabled(true);

        // Tell the list view to show one checked/activated item at a time.
        addressesList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        // Start with first item activated.
        // Make the newly clicked item the currently selected one.
        addressesList.setItemChecked(0, true);

        addressesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedPosition = i;
                addressesList.setSelection(i);
                addressesList.setItemChecked(i, true);
            }
        });

        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                BrowseAddressesActivity.this.adapter.getFilter().filter(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!isExternalStorageAccessGranted()) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    0);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.select_printer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(this, PrinterSettingActivity.class);
        startActivity(intent);
        return true;
    }

    /**
     * add new address button
     */
    public void addNewAddress(View view) {
        Intent intent = new Intent(this, AddNewAddressActivity.class);
        startActivity(intent);
    }

    /**
     * delete address button
     */
    public void deleteAddress(View view) {
        if (addressesList.getCount() == 0) {
            Toast.makeText(this, R.string.toast_empty_adress_list_to_delete_message, Toast.LENGTH_SHORT).show();
            return;
        }

        new AlertDialog.Builder(this)
                .setCancelable(false)
                .setMessage(R.string.delete_address_warning)
                .setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setPositiveButton("Usuń", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // get selected address
                        Address address = (Address) addressesList.getItemAtPosition(selectedPosition);
                        // delete address
                        protocolDataSource.deleteProtocols(address);
                        datasource.deleteAddress(address);
                        // refresh list
                        BrowseAddressesActivity.this.recreate();
                        // display confirmation
                        Context context = getApplicationContext();
                        CharSequence text = AlertUtils.ADDRESS_DELETED;
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    }
                })
                .show();
    }

    public void deleteAllAddresses(View view) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                BrowseAddressesActivity.this);
        alertDialogBuilder.setTitle(R.string.delete_all_addresses_warning);

        alertDialogBuilder
                .setCancelable(false)
                .setNegativeButton("anuluj", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // cancel dialog
                        dialog.cancel();
                    }
                })
                .setPositiveButton("usuń", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // delete all protocols
                        try {
                            protocolDataSource.deleteAllProtocols();
                            datasource.deleteAllAddresses();
                            Context context = getApplicationContext();
                            CharSequence text = "Adresy zostały poprawnie usunięte";
                            int duration = Toast.LENGTH_LONG;

                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();

                            // reload activity
                            finish();
                            startActivity(getIntent());

                        } catch (Exception e) {
                            Context context = getApplicationContext();
                            e.printStackTrace();
                            CharSequence text = String.format("Usunięcie adresów się nie udało: %s", e.getMessage());
                            int duration = Toast.LENGTH_LONG;

                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();
                        }

                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    public void showOldProtocols(View view) {
        Intent intent = new Intent(this, BrowseProtocolsActivity.class);
        if (addressesList.getCount() != 0) {
            Address address = (Address) addressesList.getItemAtPosition(selectedPosition);
            intent.putExtra(Utils.ADDRESS_ID, address.getId());
            startActivity(intent);
        } else {
            Context context = getApplicationContext();
            CharSequence text = AlertUtils.ENTER_ADDRESSES_PROTOCOLS;
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }

    }

    @Override
    protected void onResume() {
        datasource.open();
        protocolDataSource.open();
        protocolPaderewskiegoDataSource.open();
        protocolNewPaderewskiegoDataSource.open();
        awaitingProtocolDataSource.open();
        addressesList.setItemChecked(0, true);

        int awaitingProtocolCount = awaitingProtocolDataSource.getAwaitincProtocolCount();
        if (awaitingProtocolCount > 0) {
            synchronizeProtocolsButton.setEnabled(true);
            synchronizeProtocolsButton.setText(getString(R.string.synchronize_protocols, awaitingProtocolCount));
        } else {
            synchronizeProtocolsButton.setEnabled(false);
            synchronizeProtocolsButton.setText(getString(R.string.no_synchronization));
        }

        super.onResume();
    }

    @Override
    protected void onPause() {
        if (generateSiemanowiceDailyReportAsyncTask != null) {
            generateSiemanowiceDailyReportAsyncTask.setPreExecuteUiListener(null);
            generateSiemanowiceDailyReportAsyncTask.setPostExecuteUiListener(null);
        }

        if (generateNewPaderewskiegoDailyReportAsyncTask != null) {
            generateNewPaderewskiegoDailyReportAsyncTask.setPreExecuteUiListener(null);
            generateNewPaderewskiegoDailyReportAsyncTask.setPostExecuteUiListener(null);
        }

        datasource.close();
        protocolDataSource.close();
        protocolPaderewskiegoDataSource.close();
        protocolNewPaderewskiegoDataSource.close();
        awaitingProtocolDataSource.close();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (generateSiemanowiceDailyReportAsyncTask != null) {
            generateSiemanowiceDailyReportAsyncTask.cancel(true);
        }
        if (generateNewPaderewskiegoDailyReportAsyncTask != null) {
            generateNewPaderewskiegoDailyReportAsyncTask.cancel(true);
        }
        super.onDestroy();
    }

    @Override
    public void onReportGenerate(@NonNull GenerateDailyReportDialog.City city, @NonNull Date reportDate) {
        switch (city) {
            case SIEMANOWICE:
                if (generateSiemanowiceDailyReportAsyncTask != null) {
                    generateSiemanowiceDailyReportAsyncTask.cancel(true);
                }
                generateSiemanowiceDailyReportAsyncTask = new GenerateSiemanowiceDailyReportAsyncTask(datasource, protocolDataSource);
                generateSiemanowiceDailyReportAsyncTask.setPreExecuteUiListener(this);
                generateSiemanowiceDailyReportAsyncTask.setPostExecuteUiListener(this);
                generateSiemanowiceDailyReportAsyncTask.execute(reportDate);
                break;
            case NOWY_PADERWSKIEGO:
                if (generateNewPaderewskiegoDailyReportAsyncTask != null) {
                    generateNewPaderewskiegoDailyReportAsyncTask.cancel(true);
                }
                generateNewPaderewskiegoDailyReportAsyncTask = new GenerateNewPaderewskiegoDailyReportAsyncTask(datasource, protocolNewPaderewskiegoDataSource);
                generateNewPaderewskiegoDailyReportAsyncTask.setPreExecuteUiListener(this);
                generateNewPaderewskiegoDailyReportAsyncTask.setPostExecuteUiListener(this);
                generateNewPaderewskiegoDailyReportAsyncTask.execute(reportDate);
                break;
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
            Uri uri =  FileProvider.getUriForFile(this, "com.ihg.fileprovider", new File(reportFilePath));

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

    public void generateDailyReportSiemianowice(@NonNull View view) {
        GenerateDailyReportDialog dialog = (GenerateDailyReportDialog) getSupportFragmentManager().findFragmentByTag(DAILY_REPORT_SIEMANOWICE_FRAGMENT);
        if (dialog == null) {
            dialog = GenerateDailyReportDialog.newInstance(GenerateDailyReportDialog.City.SIEMANOWICE);
        }
        dialog.setListener(this);
        dialog.show(getSupportFragmentManager(), DAILY_REPORT_SIEMANOWICE_FRAGMENT);
    }

    public void generateDailyReportNewPaderewskiego(@NonNull View view) {
        GenerateDailyReportDialog dialog = (GenerateDailyReportDialog) getSupportFragmentManager().findFragmentByTag(DAILY_REPORT_NEW_PADERWSKIEGO_FRAGMENT);
        if (dialog == null) {
            dialog = GenerateDailyReportDialog.newInstance(GenerateDailyReportDialog.City.NOWY_PADERWSKIEGO);
        }
        dialog.setListener(this);
        dialog.show(getSupportFragmentManager(), DAILY_REPORT_NEW_PADERWSKIEGO_FRAGMENT);
    }

    private boolean isExternalStorageAccessGranted() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }
}
