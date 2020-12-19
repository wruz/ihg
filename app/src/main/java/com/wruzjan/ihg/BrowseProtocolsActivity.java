package com.wruzjan.ihg;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.wruzjan.ihg.utils.AlertUtils;
import com.wruzjan.ihg.utils.Utils;
import com.wruzjan.ihg.utils.dao.AddressDataSource;
import com.wruzjan.ihg.utils.dao.ProtocolDataSource;
import com.wruzjan.ihg.utils.dao.ProtocolNewPaderewskiegoDataSource;
import com.wruzjan.ihg.utils.dao.ProtocolPaderewskiegoDataSource;
import com.wruzjan.ihg.utils.dao.StreetAndIdentifierDataSource;
import com.wruzjan.ihg.utils.model.Address;
import com.wruzjan.ihg.utils.model.Protocol;
import com.wruzjan.ihg.utils.model.ProtocolNewPaderewskiego;
import com.wruzjan.ihg.utils.model.ProtocolPaderewskiego;
import com.wruzjan.ihg.utils.model.StreetAndIdentifier;

import java.util.List;

public class BrowseProtocolsActivity extends Activity {

    private AddressDataSource addressDataSource;
    private StreetAndIdentifierDataSource streetAndIdentifierDataSource;
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

        streetAndIdentifierDataSource = new StreetAndIdentifierDataSource(this);
        streetAndIdentifierDataSource.open();

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
        TextView addressDetails = findViewById(R.id.address_details);
        addressDetails.setText(address.getName() + " " + address.getStreet() + " " + address.getBuilding() + "/" + address.getFlat() + ", " + address.getCity());

        //get protocols
        List<Protocol> protocolsSiemianowice =
                protocolDataSource.getAllSiemianowiceProtocolsByAddressId(address.getId());
        List<ProtocolPaderewskiego> protocolsPaderewskiego =
                protocolPaderewskiegoDataSource.getAllPaderewskiegoProtocolsByAddressId(address.getId());
        List<ProtocolNewPaderewskiego> protocolsNewPaderewskiego =
                protocolNewPaderewskiegoDataSource.getAllNewPaderewskiegoProtocolsByAddressId(address.getId());

        //fill protocols details
        protocolsSiemianowiceList = findViewById(R.id.protocols_siemianowice_list);
        protocolsPaderewskiegoList = findViewById(R.id.protocols_paderewskiego_list);
        protocolsNewPaderewskiegoList = findViewById(R.id.protocols_new_paderewskiego_list);

        ArrayAdapter<Protocol> adapterSiemianowice = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_activated_1, protocolsSiemianowice);

        if(adapterSiemianowice.isEmpty()){
            TextView protocolsSiemianowiceTitle = findViewById(R.id.protocols_siemianowice_title);
            protocolsSiemianowiceTitle.setVisibility(View.GONE);
            Button openSiemianowiceButton = findViewById(R.id.open_siemianowice_button);
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

        ArrayAdapter<ProtocolPaderewskiego> adapterPaderewskiego = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_activated_1, protocolsPaderewskiego);

        if(adapterPaderewskiego.isEmpty()){
            TextView protocolsPaderewskiegoTitle = findViewById(R.id.protocols_paderewskiego_title);
            protocolsPaderewskiegoTitle.setVisibility(View.GONE);
            //TODO wczytywanie paderewskiego
            Button openPaderewkiegoButton = findViewById(R.id.open_paderewskiego_button);
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

        ArrayAdapter<ProtocolNewPaderewskiego> adapterNewPaderewskiego = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_activated_1, protocolsNewPaderewskiego);

        if(adapterNewPaderewskiego.isEmpty()){
            TextView protocolsNewPaderewskiegoTitle = findViewById(R.id.protocols_new_paderewskiego_title);
            protocolsNewPaderewskiegoTitle.setVisibility(View.GONE);
            //TODO wczytywanie new paderewskiego
            Button openNewPaderewskiegoButton = findViewById(R.id.open_new_paderewskiego_button);
            openNewPaderewskiegoButton.setVisibility(View.GONE);
            protocolsNewPaderewskiegoList.setVisibility(View.GONE);
            hasNewPaderewskiegoProrocols = false;
        } else {
            TextView protocolsNewPaderewskiegoTitle = findViewById(R.id.protocols_new_paderewskiego_title);
            StreetAndIdentifier streetAndIdentifier = streetAndIdentifierDataSource.getByStreetIdentifier(address.getStreetAndIdentifierId());

            if (streetAndIdentifier != null) {
                protocolsNewPaderewskiegoTitle.setText(getString(R.string.protocols_header, streetAndIdentifier.getStreetName()));
            }

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
        streetAndIdentifierDataSource.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        protocolDataSource.close();
        protocolPaderewskiegoDataSource.close();
        protocolNewPaderewskiegoDataSource.close();
        addressDataSource.close();
        streetAndIdentifierDataSource.close();
        super.onPause();
    }
}