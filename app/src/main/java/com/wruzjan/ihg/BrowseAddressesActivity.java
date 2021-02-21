package com.wruzjan.ihg;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.wruzjan.ihg.reports.GenerateReportActivity;
import com.wruzjan.ihg.utils.AlertUtils;
import com.wruzjan.ihg.utils.DateUtils;
import com.wruzjan.ihg.utils.FileUtils;
import com.wruzjan.ihg.utils.NavigationUtils;
import com.wruzjan.ihg.utils.NetworkStateChecker;
import com.wruzjan.ihg.utils.Utils;
import com.wruzjan.ihg.utils.dao.AddressDataSource;
import com.wruzjan.ihg.utils.dao.AwaitingProtocolDataSource;
import com.wruzjan.ihg.utils.dao.ProtocolDataSource;
import com.wruzjan.ihg.utils.dao.ProtocolNewPaderewskiegoDataSource;
import com.wruzjan.ihg.utils.dao.ProtocolPaderewskiegoDataSource;
import com.wruzjan.ihg.utils.dao.StreetAndIdentifierDataSource;
import com.wruzjan.ihg.utils.model.Address;
import com.wruzjan.ihg.utils.model.AwaitingProtocol;
import com.wruzjan.ihg.utils.model.Protocol;
import com.wruzjan.ihg.utils.model.ProtocolNewPaderewskiego;
import com.wruzjan.ihg.utils.model.StreetAndIdentifier;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class BrowseAddressesActivity extends AppCompatActivity {

    public static final String EXTRA_PROTOCOL_PDF_TO_SHARE = "EXTRA_PROTOCOL_PDF_TO_SHARE";

    private AddressDataSource datasource;
    private ProtocolDataSource protocolDataSource;
    private StreetAndIdentifierDataSource streetAndIdentifierDataSource;
    private ProtocolPaderewskiegoDataSource protocolPaderewskiegoDataSource;
    private ProtocolNewPaderewskiegoDataSource protocolNewPaderewskiegoDataSource;
    private AwaitingProtocolDataSource awaitingProtocolDataSource;
    private ListView addressesList;

    private int selectedPosition = 0;
    private ArrayAdapter<Address> adapter;

    private Button synchronizeProtocolsButton;

    private NetworkStateChecker networkStateChecker;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == NavigationUtils.DROPBOX_SHARE_REQUEST_CODE) {
            if (data != null && data.getComponent() != null && !TextUtils.isEmpty(data.getComponent().flattenToShortString())) {
                awaitingProtocolDataSource.open();
                List<AwaitingProtocol> awaitingProtocols = awaitingProtocolDataSource.getAwaitingProtocols();
                ArrayList<Uri> uris = new ArrayList<>(awaitingProtocols.size());
                for (AwaitingProtocol awaitingProtocol : awaitingProtocols) {
                    Uri uri = FileUtils.getUriFromFile(this, awaitingProtocol.getProtocolPdfUrl());
                    uris.add(uri);
                }
                data.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);

                // Start the selected activity
                startActivity(data);
                awaitingProtocolDataSource.deleteAllAwaitingProtocols();
                synchronizeProtocolsButton.setEnabled(false);
                synchronizeProtocolsButton.setText(getString(R.string.no_synchronization));
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_browse_addresses);

        networkStateChecker = new NetworkStateChecker(getApplication());

        synchronizeProtocolsButton = findViewById(R.id.synchronize_all_protocols_button);
        synchronizeProtocolsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (networkStateChecker.isOnline()) {
                    NavigationUtils.openDropBoxApp(BrowseAddressesActivity.this);
                } else {
                    Toast.makeText(BrowseAddressesActivity.this, "Brak połączenia z internetem do dokonania synchronizacji", Toast.LENGTH_SHORT).show();
                }
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

        streetAndIdentifierDataSource = new StreetAndIdentifierDataSource(this);
        streetAndIdentifierDataSource.open();

        addressesList = findViewById(R.id.addresses_list);
        EditText inputSearch = findViewById(R.id.inputSearch);

        List<Address> values = datasource.getAllAddresses();

        for (Address value : values) {
            StreetAndIdentifier streetAndIdentifier = streetAndIdentifierDataSource.getByStreetIdentifier(value.getStreetAndIdentifierId());
            String streetName = streetAndIdentifier != null && value.getStreetAndIdentifierId() != -1 ? streetAndIdentifier.getStreetName() : value.getStreet();
            value.setStreet(streetName);
        }

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

        String pdfFilePath = getIntent().getStringExtra(EXTRA_PROTOCOL_PDF_TO_SHARE);
        if (pdfFilePath != null) {
            NavigationUtils.openDropBoxApp(this, pdfFilePath);
        }
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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
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
        streetAndIdentifierDataSource.open();
        addressesList.setItemChecked(0, true);

        int awaitingProtocolCount = awaitingProtocolDataSource.getAwaitingProtocolCount();
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
        datasource.close();
        protocolDataSource.close();
        protocolPaderewskiegoDataSource.close();
        protocolNewPaderewskiegoDataSource.close();
        awaitingProtocolDataSource.close();
        streetAndIdentifierDataSource.close();
        super.onPause();
    }

    public void generateDailyReportSiemianowice(@NonNull View view) {
        Protocol latestProtocol = protocolDataSource.getLatestProtocol();
        if (latestProtocol == null) {
            Toast.makeText(this, getString(R.string.no_created_protocols_error_message), Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            Date startDate = DateUtils.DATABASE_DATE_FORMAT.parse(latestProtocol.get_created());
            Calendar startDateCalendar = Calendar.getInstance();
            startDateCalendar.setTime(startDate);
            startDateCalendar.add(Calendar.DATE, 1);

            Calendar endDateCalendar = DateUtils.getDateOnlyCalendar(getYesterdayDate());
            if (startDateCalendar.after(endDateCalendar)) {
                startDateCalendar.setTime(endDateCalendar.getTime());
            }

            GenerateReportActivity.start(this, GenerateReportActivity.City.SIEMANOWICE, startDateCalendar.getTime(), endDateCalendar.getTime());
        } catch (ParseException exception) {
            Toast.makeText(this, getString(R.string.corrupted_protocol_data_error_message), Toast.LENGTH_SHORT).show();
        }
    }

    public void generateDailyReportNewPaderewskiego(@NonNull View view) {
        ProtocolNewPaderewskiego latestProtocol = protocolNewPaderewskiegoDataSource.getLatestProtocol();
        if (latestProtocol == null) {
            Toast.makeText(this, getString(R.string.no_created_protocols_error_message), Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            Date startDate = DateUtils.DATABASE_DATE_FORMAT.parse(latestProtocol.get_created());
            Calendar startDateCalendar = Calendar.getInstance();
            startDateCalendar.setTime(startDate);
            startDateCalendar.add(Calendar.DATE, 1);

            Calendar endDateCalendar = DateUtils.getDateOnlyCalendar(getYesterdayDate());
            if (startDateCalendar.after(endDateCalendar)) {
                startDateCalendar.setTime(endDateCalendar.getTime());
            }

            GenerateReportActivity.start(this, GenerateReportActivity.City.NOWY_PADERWSKIEGO, startDateCalendar.getTime(), endDateCalendar.getTime());
        } catch (ParseException exception) {
            Toast.makeText(this, getString(R.string.corrupted_protocol_data_error_message), Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isExternalStorageAccessGranted() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    @NonNull
    private Date getYesterdayDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        return calendar.getTime();
    }
}
