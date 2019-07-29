package com.wruzjan.ihg;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.wruzjan.ihg.utils.AlertUtils;
import com.wruzjan.ihg.utils.Utils;
import com.wruzjan.ihg.utils.dao.AddressDataSource;
import com.wruzjan.ihg.utils.dao.ProtocolDataSource;
import com.wruzjan.ihg.utils.dao.ProtocolNewPaderewskiegoDataSource;
import com.wruzjan.ihg.utils.dao.ProtocolPaderewskiegoDataSource;
import com.wruzjan.ihg.utils.excel.GenerateExcel;
import com.wruzjan.ihg.utils.model.Address;
import com.wruzjan.ihg.utils.model.Protocol;
import com.wruzjan.ihg.utils.model.ProtocolNewPaderewskiego;
import com.wruzjan.ihg.utils.model.ProtocolPaderewskiego;
import com.wruzjan.ihg.utils.pdf.GeneratePDF;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class BrowseProtocolsActivity extends Activity {

    private AddressDataSource addressDataSource;
    private ProtocolDataSource protocolDataSource;
    private ProtocolPaderewskiegoDataSource protocolPaderewskiegoDataSource;
    private ProtocolNewPaderewskiegoDataSource protocolNewPaderewskiegoDataSource;
    private Address address;
    private ListView protocolsSiemianowiceList;
    private ListView protocolsPaderewskiegoList;
    private ListView protocolsNewPaderewskiegoList;
    private int siemianowiceSelectedPosition = 0;
    private int paderewskiegoSelectedPosition = 0;
    private int newPaderewskiegoSelectedPosition = 0;
    private ArrayAdapter<Protocol> adapterSiemianowice;
    private ArrayAdapter<ProtocolPaderewskiego> adapterPaderewskiego;
    private ArrayAdapter<ProtocolNewPaderewskiego> adapterNewPaderewskiego;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean hasSiemianowiceProrocols = true;
        boolean hasPaderewskiegoProrocols = true;
        boolean hasNewPaderewskiegoProrocols = true;

        setContentView(R.layout.activity_browse_protocols);

        protocolDataSource = new ProtocolDataSource(this);
        protocolDataSource.open();

        protocolPaderewskiegoDataSource = new ProtocolPaderewskiegoDataSource(this);
        protocolPaderewskiegoDataSource.open();

        protocolNewPaderewskiegoDataSource = new ProtocolNewPaderewskiegoDataSource(this);
        protocolNewPaderewskiegoDataSource.open();

        addressDataSource = new AddressDataSource(this);
        addressDataSource.open();

        Intent intent = getIntent();

        //get address details
        if(intent.hasExtra(Utils.ADDRESS_ID)){
            int addressId = intent.getIntExtra(Utils.ADDRESS_ID, -1);
            address = addressDataSource.getAddressById(addressId);
        } else {
            Context context = getApplicationContext();
            CharSequence text = AlertUtils.SELECT_ADDRESSES_PROTOCOLS;
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }

        //fill address details
        TextView addressDetails = (TextView) findViewById(R.id.address_details);
        addressDetails.setText(address.getName() + " " + address.getStreet() + " " + address.getBuilding() + "/" + address.getFlat() + ", " + address.getCity());

        //get protocols
        List<Protocol> protocolsSiemianowice =
                protocolDataSource.getAllSiemianowiceProtocolsByAddressId(address.getId());
        List<ProtocolPaderewskiego> protocolsPaderewskiego =
                protocolPaderewskiegoDataSource.getAllPaderewskiegoProtocolsByAddressId(address.getId());
        List<ProtocolNewPaderewskiego> protocolsNewPaderewskiego =
                protocolNewPaderewskiegoDataSource.getAllNewPaderewskiegoProtocolsByAddressId(address.getId());

        //fill protocols details
        protocolsSiemianowiceList = (ListView) findViewById(R.id.protocols_siemianowice_list);
        protocolsPaderewskiegoList = (ListView) findViewById(R.id.protocols_paderewskiego_list);
        protocolsNewPaderewskiegoList = (ListView) findViewById(R.id.protocols_new_paderewskiego_list);

        adapterSiemianowice = new ArrayAdapter<Protocol>(this,
                android.R.layout.simple_list_item_activated_1, protocolsSiemianowice);

        if(adapterSiemianowice.isEmpty()){
            TextView protocolsSiemianowiceTitle = (TextView) findViewById(R.id.protocols_siemianowice_title);
            protocolsSiemianowiceTitle.setVisibility(View.GONE);
            Button openSiemianowiceButton = (Button) findViewById(R.id.open_siemianowice_button);
            openSiemianowiceButton.setVisibility(View.GONE);
            protocolsSiemianowiceList.setVisibility(View.GONE);
            hasSiemianowiceProrocols = false;
        } else {
            protocolsSiemianowiceList.setAdapter(adapterSiemianowice);
            protocolsSiemianowiceList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            protocolsSiemianowiceList.setItemChecked(0, true);
            protocolsSiemianowiceList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    siemianowiceSelectedPosition = i;
                    protocolsSiemianowiceList.setSelection(i);
                    protocolsSiemianowiceList.setItemChecked(i, true);
                }
            });
        }

        adapterPaderewskiego = new ArrayAdapter<ProtocolPaderewskiego>(this,
                android.R.layout.simple_list_item_activated_1, protocolsPaderewskiego);

        if(adapterPaderewskiego.isEmpty()){
            TextView protocolsPaderewskiegoTitle = (TextView) findViewById(R.id.protocols_paderewskiego_title);
            protocolsPaderewskiegoTitle.setVisibility(View.GONE);
            //TODO wczytywanie paderewskiego
            Button openPaderewkiegoButton = (Button) findViewById(R.id.open_paderewskiego_button);
            openPaderewkiegoButton.setVisibility(View.GONE);
            protocolsPaderewskiegoList.setVisibility(View.GONE);
            hasPaderewskiegoProrocols = false;
        } else {
            protocolsPaderewskiegoList.setAdapter(adapterPaderewskiego);
            protocolsPaderewskiegoList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            protocolsPaderewskiegoList.setItemChecked(0, true);
            protocolsPaderewskiegoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    paderewskiegoSelectedPosition = i;
                    protocolsPaderewskiegoList.setSelection(i);
                    protocolsPaderewskiegoList.setItemChecked(i, true);
                }
            });
        }

        adapterNewPaderewskiego = new ArrayAdapter<ProtocolNewPaderewskiego>(this,
                android.R.layout.simple_list_item_activated_1, protocolsNewPaderewskiego);

        if(adapterNewPaderewskiego.isEmpty()){
            TextView protocolsNewPaderewskiegoTitle = (TextView) findViewById(R.id.protocols_new_paderewskiego_title);
            protocolsNewPaderewskiegoTitle.setVisibility(View.GONE);
            //TODO wczytywanie new paderewskiego
            Button openNewPaderewskiegoButton = (Button) findViewById(R.id.open_new_paderewskiego_button);
            openNewPaderewskiegoButton.setVisibility(View.GONE);
            protocolsNewPaderewskiegoList.setVisibility(View.GONE);
            hasNewPaderewskiegoProrocols = false;
        } else {
            protocolsNewPaderewskiegoList.setAdapter(adapterNewPaderewskiego);
            protocolsNewPaderewskiegoList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            protocolsNewPaderewskiegoList.setItemChecked(0, true);
            protocolsNewPaderewskiegoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    newPaderewskiegoSelectedPosition= i;
                    protocolsNewPaderewskiegoList.setSelection(i);
                    protocolsNewPaderewskiegoList.setItemChecked(i, true);
                }
            });
        }

        if(!hasSiemianowiceProrocols && !hasPaderewskiegoProrocols && !hasNewPaderewskiegoProrocols){

            Button deleteButton =(Button)findViewById(R.id.detele_protocols_button);
            deleteButton.setEnabled(false);

            Context context = getApplicationContext();
            CharSequence text = AlertUtils.NO_PROTOCOLS;
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }

    }

    public void openSiemianowice(View view) {
        if(protocolsSiemianowiceList.getCount() != 0){
            Intent intent = new Intent(this, ChooseWorkerActivity.class);
            Protocol protocol =  (Protocol) protocolsSiemianowiceList.getItemAtPosition(siemianowiceSelectedPosition);
            intent.putExtra(Utils.PROTOCOL_ID, protocol.get_id());
            intent.putExtra(Utils.ADDRESS_ID, address.getId());
            intent.putExtra(Utils.EDIT_FLAG, true);
            startActivity(intent);

        } else {
            Context context = getApplicationContext();
            CharSequence text = AlertUtils.NO_PROTOCOLS;
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
    }

    public void openPaderewskiego(View view) {
        if(protocolsPaderewskiegoList.getCount() != 0){
            Intent intent = new Intent(this, ChooseWorker2Activity.class);
            ProtocolPaderewskiego protocol =  (ProtocolPaderewskiego) protocolsPaderewskiegoList.getItemAtPosition(paderewskiegoSelectedPosition);
            intent.putExtra(Utils.PROTOCOL_ID, protocol.get_id());
            intent.putExtra(Utils.ADDRESS_ID, address.getId());
            intent.putExtra(Utils.EDIT_FLAG, true);
            startActivity(intent);

        } else {
            Context context = getApplicationContext();
            CharSequence text = AlertUtils.NO_PROTOCOLS;
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
    }

    public void openNewPaderewskiego(View view) {
        if(protocolsNewPaderewskiegoList.getCount() != 0){
            Intent intent = new Intent(this, ChooseWorkerNewPaderewskiegoActivity.class);
            ProtocolNewPaderewskiego protocol =  (ProtocolNewPaderewskiego) protocolsNewPaderewskiegoList.
                    getItemAtPosition(newPaderewskiegoSelectedPosition);
            intent.putExtra(Utils.PROTOCOL_ID, protocol.get_id());
            intent.putExtra(Utils.ADDRESS_ID, address.getId());
            intent.putExtra(Utils.EDIT_FLAG, true);
            startActivity(intent);
        } else {
            Context context = getApplicationContext();
            CharSequence text = AlertUtils.NO_PROTOCOLS;
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
    }

    public void deleteProtocols(View view) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                BrowseProtocolsActivity.this);
        alertDialogBuilder.setTitle("zostaną usunięte wszystkie protokoły dla adresu: "+address.getCity()+", "
                +address.getStreet()+" "
                +address.getBuilding()+"/"
                +address.getFlat());

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
                        // delete protocols
                        try{
                            protocolDataSource.deleteProtocols(address);
                            close(null);
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

    public void close(View view) {
        Intent adressesIntent = new Intent(this, BrowseAddressesActivity.class);
        startActivity(adressesIntent);
    }

    @Override
    protected void onResume() {
        protocolDataSource.open();
        protocolPaderewskiegoDataSource.open();
        protocolNewPaderewskiegoDataSource.open();
        addressDataSource.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        protocolDataSource.close();
        protocolPaderewskiegoDataSource.close();
        protocolNewPaderewskiegoDataSource.close();
        addressDataSource.close();
        super.onPause();

    }

}