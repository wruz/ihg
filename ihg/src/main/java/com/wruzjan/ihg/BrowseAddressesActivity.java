package com.wruzjan.ihg;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.wruzjan.ihg.utils.AlertUtils;
import com.wruzjan.ihg.utils.Utils;
import com.wruzjan.ihg.utils.dao.AddressDataSource;
import com.wruzjan.ihg.utils.dao.ProtocolDataSource;
import com.wruzjan.ihg.utils.dao.ProtocolNewPaderewskiegoDataSource;
import com.wruzjan.ihg.utils.dao.ProtocolPaderewskiegoDataSource;
import com.wruzjan.ihg.utils.model.Address;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class BrowseAddressesActivity extends AppCompatActivity {

    public static final String DAILY_REPORT_SIEMANOWICE_FRAGMENT = "DAILY_REPORT_SIEMANOWICE_FRAGMENT";
    private AddressDataSource datasource;
    private ProtocolDataSource protocolDataSource;
    private ProtocolPaderewskiegoDataSource protocolPaderewskiegoDataSource;
    private ProtocolNewPaderewskiegoDataSource protocolNewPaderewskiegoDataSource;
    private ListView addressesList;

    private int selectedPosition = 0;
    private ArrayAdapter<Address> adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_browse_addresses);

        datasource = new AddressDataSource(this);
        datasource.open();

        protocolDataSource = new ProtocolDataSource(this);
        protocolDataSource.open();

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
    public boolean onOptionsItemSelected(MenuItem item){
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
//        get selected address
        Address address =  (Address) addressesList.getItemAtPosition(selectedPosition);
//        delete address
        datasource.deleteAddress(address);
        //TODO delete corresponding protocols also, but ask user about it first
//        protocolDataSource.deleteProtocols(address);
//        refresh list
        this.recreate();
//        display confirmation
        Context context = getApplicationContext();
        CharSequence text = AlertUtils.ADDRESS_DELETED;
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    /**
     * add form to address button
     */
    public void addNewFormToAddress(View view) {

        Intent intent = new Intent(this, ChooseWorkerActivity.class);
        if(addressesList.getCount() != 0){
            Address address =  (Address) addressesList.getItemAtPosition(selectedPosition);
            intent.putExtra(Utils.ADDRESS_ID, address.getId());
            startActivity(intent);
        } else {
            Context context = getApplicationContext();
            CharSequence text = AlertUtils.ENTER_ADDRESSES_FORM;
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }

    }

    public void addNewForm2ToAddress(View view) {
        Intent intent = new Intent(this, ChooseWorker2Activity.class);
        if(addressesList.getCount() != 0){
            Address address =  (Address) addressesList.getItemAtPosition(selectedPosition);
            intent.putExtra(Utils.ADDRESS_ID, address.getId());
            startActivity(intent);
        } else {
            Context context = getApplicationContext();
            CharSequence text = AlertUtils.ENTER_ADDRESSES_FORM;
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }

    }

    /**
     * Redirect to choose worker activity for new Paderewskiego.
     *
     * @param view the view.
     */
    public void addNewFormNewPaderewskiegoToAddress(View view) {
        Intent intent = new Intent(this, ChooseWorkerNewPaderewskiegoActivity.class);
        if(addressesList.getCount() != 0){
            Address address =  (Address) addressesList.getItemAtPosition(selectedPosition);
            intent.putExtra(Utils.ADDRESS_ID, address.getId());
            startActivity(intent);
        } else {
            Context context = getApplicationContext();
            CharSequence text = AlertUtils.ENTER_ADDRESSES_FORM;
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
    }

    public void deleteAllProtocols(View view){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                BrowseAddressesActivity.this);
        alertDialogBuilder.setTitle("zostaną usunięte wszystkie protokoły zapisane w urządzeniu");

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
                        try{
                            protocolDataSource.deleteAllProtocols();
                            Context context = getApplicationContext();
                            CharSequence text = "Protokoły zostały poprawnie usunięte";
                            int duration = Toast.LENGTH_LONG;

                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();

                        } catch (Exception e){
                            Context context = getApplicationContext();
                            e.printStackTrace();
                            CharSequence text = String.format("Usunięcie plików się nie udało: %s", e.getMessage());
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

    public void deleteAllAddresses(View view){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                BrowseAddressesActivity.this);
        alertDialogBuilder.setTitle("zostaną usunięte wszystkie adresy i protokoły zapisane w urządzeniu");

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
                        try{
                            protocolDataSource.deleteAllProtocols();
                            datasource.deleteAllAddresses();
                            Context context = getApplicationContext();
                            CharSequence text = "Protokoły zostały poprawnie usunięte";
                            int duration = Toast.LENGTH_LONG;

                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();

                            // reload activity
                            finish();
                            startActivity(getIntent());

                        } catch (Exception e){
                            Context context = getApplicationContext();
                            e.printStackTrace();
                            CharSequence text = String.format("Usunięcie plików się nie udało: %s", e.getMessage());
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
        if(addressesList.getCount() != 0){
            Address address =  (Address) addressesList.getItemAtPosition(selectedPosition);
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
        addressesList.setItemChecked(0, true);
        super.onResume();
    }

    @Override
    protected void onPause() {
        datasource.close();
        protocolDataSource.close();
        protocolPaderewskiegoDataSource.close();
        protocolNewPaderewskiegoDataSource.close();
        super.onPause();
    }

    public void generateDailyReportSiemianowice(@NonNull View view) {
        GenerateDailyReportDialog dialog = (GenerateDailyReportDialog) getSupportFragmentManager().findFragmentByTag(DAILY_REPORT_SIEMANOWICE_FRAGMENT);
        if (dialog == null) {
            dialog = GenerateDailyReportDialog.newInstance(GenerateDailyReportDialog.City.SIEMANOWICE);
        }
        dialog.show(getSupportFragmentManager(), DAILY_REPORT_SIEMANOWICE_FRAGMENT);
    }

    public void generateDailyReportNewPaderewskiego(@NonNull View view) {

    }

    private boolean isExternalStorageAccessGranted() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }
}
