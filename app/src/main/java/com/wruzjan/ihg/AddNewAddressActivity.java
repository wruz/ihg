package com.wruzjan.ihg;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.wruzjan.ihg.utils.AlertUtils;
import com.wruzjan.ihg.utils.Utils;
import com.wruzjan.ihg.utils.ValidationUtils;
import com.wruzjan.ihg.utils.dao.AddressDataSource;
import com.wruzjan.ihg.utils.dao.StreetAndIdentifierDataSource;
import com.wruzjan.ihg.utils.model.Address;
import com.wruzjan.ihg.utils.model.StreetAndIdentifier;

import java.util.Arrays;
import java.util.List;

public class AddNewAddressActivity extends Activity {

    private static final String UNSPECIFIED_STREET = "5";

    private AddressDataSource addressDataSource;
    private StreetAndIdentifierDataSource streetAndIdentifierDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_adress);

        //get data from last entry and fill form
        SharedPreferences settings = getSharedPreferences(Utils.PREFS_NAME, 0);
        String[] availableStreets = getResources().getStringArray(R.array.available_street_names);
        List<String> availableStreetIds = Arrays.asList(getResources().getStringArray(R.array.available_street_identifiers));

        Spinner streetField = findViewById(R.id.street);
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, availableStreets);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        streetField.setAdapter(adapter);

        String lastStreet = settings.getString(Utils.STREET, null);
        int streetIdIndex = availableStreetIds.indexOf(lastStreet);
        if (streetIdIndex != -1) {
            streetField.setSelection(streetIdIndex);
        } else {
            settings.edit().remove(Utils.STREET).commit();
            streetField.setSelection(0);
        }

        EditText cityField = findViewById(R.id.city);
        cityField.setText(settings.getString(Utils.CITY, null), TextView.BufferType.EDITABLE);

        EditText districtField = findViewById(R.id.district);
        districtField.setText(settings.getString(Utils.DISTRICT, null), TextView.BufferType.EDITABLE);

        addressDataSource = new AddressDataSource(this);
        addressDataSource.open();

        streetAndIdentifierDataSource = new StreetAndIdentifierDataSource(this);
        streetAndIdentifierDataSource.open();
    }

    public void addAddress(View view) {

//        required fields
        EditText nameField = findViewById(R.id.name);
        String name = nameField.getText().toString();

        List<String> availableStreetIds = Arrays.asList(getResources().getStringArray(R.array.available_street_identifiers));
        Spinner streetField = findViewById(R.id.street);
        String localStreetIdentifier = availableStreetIds.get((int) streetField.getSelectedItemId());

        EditText buildingField = findViewById(R.id.building);
        String building = buildingField.getText().toString();

        EditText cityField = findViewById(R.id.city);
        String city = cityField.getText().toString();

//        optional fields
        EditText flatField = findViewById(R.id.flat);
        String flat = flatField.getText().toString();

        EditText districtField = findViewById(R.id.district);
        String district = districtField.getText().toString();

//        create Address
        StreetAndIdentifier streetAndIdentifier = streetAndIdentifierDataSource.getByStreetIdentifier(Integer.parseInt(localStreetIdentifier));
        Integer streetIdentifier = streetAndIdentifier != null ? streetAndIdentifier.getStreetIdentifier() : -1;

        Address address = new Address(name, null, building, flat, district, city, streetIdentifier);
//        validate address
        if (address.getName().isEmpty() || address.getBuilding().isEmpty() || address.getCity().isEmpty()) {
            Context context = getApplicationContext();
            CharSequence text = AlertUtils.VALIDATION_FAILED_FIELDS;
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        } else if (!isBuildingNumberValid(address.getBuilding())) {
            Toast.makeText(this, R.string.address_validation_error_invalid_building_number, Toast.LENGTH_LONG).show();
        } else if (!address.getFlat().isEmpty() && !isBuildingNumberValid(address.getFlat())) {
            Toast.makeText(this, R.string.address_validation_error_invalid_flat_number, Toast.LENGTH_LONG).show();
        } else {
            addressDataSource.insertAddress(address);

            //save data for further entries
            SharedPreferences settings = getSharedPreferences(Utils.PREFS_NAME, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(Utils.STREET, address.getStreet());
            editor.putString(Utils.CITY, address.getCity());
            editor.putString(Utils.DISTRICT, address.getDistrinct());
            editor.commit();

            Intent intent = new Intent(this, BrowseAddressesActivity.class);
            startActivity(intent);
        }

    }

    public void addAddressAndProtocol(View view) {

//        required fields
        EditText nameField = findViewById(R.id.name);
        String name = nameField.getText().toString();

        List<String> availableStreetIds = Arrays.asList(getResources().getStringArray(R.array.available_street_identifiers));
        Spinner streetField = findViewById(R.id.street);
        String localStreetIdentifier = availableStreetIds.get((int) streetField.getSelectedItemId());

        EditText buildingField = findViewById(R.id.building);
        String building = buildingField.getText().toString();

        EditText cityField = findViewById(R.id.city);
        String city = cityField.getText().toString();

//        optional fields
        EditText flatField = findViewById(R.id.flat);
        String flat = flatField.getText().toString();

        EditText districtField = findViewById(R.id.district);
        String district = districtField.getText().toString();

//        create Address
        StreetAndIdentifier streetAndIdentifier = streetAndIdentifierDataSource.getByStreetIdentifier(Integer.parseInt(localStreetIdentifier));
        Integer streetIdentifier = streetAndIdentifier != null ? streetAndIdentifier.getStreetIdentifier() : -1;

        Address address = new Address(name, null, building, flat, district, city, streetIdentifier);
//        validate address
        if (address.getName().isEmpty() || address.getBuilding().isEmpty() || address.getCity().isEmpty()) {
            Context context = getApplicationContext();
            CharSequence text = AlertUtils.VALIDATION_FAILED_FIELDS;
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        } else if (!isBuildingNumberValid(address.getBuilding())) {
            Toast.makeText(this, R.string.address_validation_error_invalid_building_number, Toast.LENGTH_LONG).show();
        } else if (!address.getFlat().isEmpty() && !isBuildingNumberValid(address.getFlat())) {
            Toast.makeText(this, R.string.address_validation_error_invalid_flat_number, Toast.LENGTH_LONG).show();
        } else {
            address = addressDataSource.insertAddress(address);

            //save data for further entries
            SharedPreferences settings = getSharedPreferences(Utils.PREFS_NAME, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(Utils.STREET, address.getStreet());
            editor.putString(Utils.CITY, address.getCity());
            editor.putString(Utils.DISTRICT, address.getDistrinct());
            editor.commit();

            Intent intent = new Intent(this, ChooseWorkerNewPaderewskiegoActivity.class);
            intent.putExtra(Utils.ADDRESS_ID, address.getId());
            startActivity(intent);
        }

    }

    @Override
    protected void onResume() {
        addressDataSource.open();
        streetAndIdentifierDataSource.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        addressDataSource.close();
        streetAndIdentifierDataSource.close();
        super.onPause();
    }

    private boolean isBuildingNumberValid(String buildingNumber) {
        Spinner streetField = findViewById(R.id.street);
        List<String> availableStreetIds = Arrays.asList(getResources().getStringArray(R.array.available_street_identifiers));
        if (availableStreetIds.get((int) streetField.getSelectedItemId()).equals(UNSPECIFIED_STREET)) {
            return true;
        } else {
            return ValidationUtils.isMatchingBuildingNumberPattern(buildingNumber);
        }
    }
}
