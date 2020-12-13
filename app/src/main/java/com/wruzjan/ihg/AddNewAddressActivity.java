package com.wruzjan.ihg;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wruzjan.ihg.utils.AlertUtils;
import com.wruzjan.ihg.utils.Utils;
import com.wruzjan.ihg.utils.ValidationUtils;
import com.wruzjan.ihg.utils.dao.AddressDataSource;
import com.wruzjan.ihg.utils.model.Address;

public class AddNewAddressActivity extends Activity {

    private AddressDataSource datasource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_adress);

        //get data from last entry and fill form
        SharedPreferences settings = getSharedPreferences(Utils.PREFS_NAME, 0);

        EditText streetField = findViewById(R.id.street);
        streetField.setText(settings.getString(Utils.STREET, null), TextView.BufferType.EDITABLE);

        EditText cityField = findViewById(R.id.city);
        cityField.setText(settings.getString(Utils.CITY, null), TextView.BufferType.EDITABLE);

        EditText districtField = findViewById(R.id.district);
        districtField.setText(settings.getString(Utils.DISTRICT, null), TextView.BufferType.EDITABLE);

        datasource = new AddressDataSource(this);
        datasource.open();
    }

    public void addAddress(View view) {

//        required fields
        EditText nameField = findViewById(R.id.name);
        String name = nameField.getText().toString();

        EditText streetField = findViewById(R.id.street);
        String street = streetField.getText().toString();

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
        Address address = new Address(name, street, building, flat, district, city);
//        validate address
        if (address.getName().isEmpty() || address.getStreet().isEmpty() ||
                address.getBuilding().isEmpty() || address.getCity().isEmpty()) {
            Context context = getApplicationContext();
            CharSequence text = AlertUtils.VALIDATION_FAILED_FIELDS;
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        } else if (!ValidationUtils.isMatchingBuildingNumberPattern(address.getBuilding())) {
            Toast.makeText(this, R.string.address_validation_error_invalid_building_number, Toast.LENGTH_LONG).show();
        } else if (!address.getFlat().isEmpty() && !ValidationUtils.isMatchingBuildingNumberPattern(address.getFlat())) {
            Toast.makeText(this, R.string.address_validation_error_invalid_flat_number, Toast.LENGTH_LONG).show();
        } else {
            datasource.insertAddress(address);

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

    public void addAddressAndProtocolSiemianowice(View view) {

//        required fields
        EditText nameField = findViewById(R.id.name);
        String name = nameField.getText().toString();

        EditText streetField = findViewById(R.id.street);
        String street = streetField.getText().toString();

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
        Address address = new Address(name, street, building, flat, district, city);
//        validate address
        if (address.getName().isEmpty() || address.getStreet().isEmpty() ||
                address.getBuilding().isEmpty() || address.getCity().isEmpty()) {
            Context context = getApplicationContext();
            CharSequence text = AlertUtils.VALIDATION_FAILED_FIELDS;
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        } else if (!ValidationUtils.isMatchingBuildingNumberPattern(address.getBuilding())) {
            Toast.makeText(this, R.string.address_validation_error_invalid_building_number, Toast.LENGTH_LONG).show();
        } else if (!address.getFlat().isEmpty() && !ValidationUtils.isMatchingBuildingNumberPattern(address.getFlat())) {
            Toast.makeText(this, R.string.address_validation_error_invalid_flat_number, Toast.LENGTH_LONG).show();
        } else {
            address = datasource.insertAddress(address);

            //save data for further entries
            SharedPreferences settings = getSharedPreferences(Utils.PREFS_NAME, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(Utils.STREET, address.getStreet());
            editor.putString(Utils.CITY, address.getCity());
            editor.putString(Utils.DISTRICT, address.getDistrinct());
            editor.commit();

            Intent intent = new Intent(this, ChooseWorkerActivity.class);
            intent.putExtra(Utils.ADDRESS_ID, address.getId());
            startActivity(intent);
        }

    }

    public void addAddressAndProtocolNewPaderewskiego(View view) {

//        required fields
        EditText nameField = findViewById(R.id.name);
        String name = nameField.getText().toString();

        EditText streetField = findViewById(R.id.street);
        String street = streetField.getText().toString();

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
        Address address = new Address(name, street, building, flat, district, city);
//        validate address
        if (address.getName().isEmpty() || address.getStreet().isEmpty() ||
                address.getBuilding().isEmpty() || address.getCity().isEmpty()) {
            Context context = getApplicationContext();
            CharSequence text = AlertUtils.VALIDATION_FAILED_FIELDS;
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        } else if (!ValidationUtils.isMatchingBuildingNumberPattern(address.getBuilding())) {
            Toast.makeText(this, R.string.address_validation_error_invalid_building_number, Toast.LENGTH_LONG).show();
        } else if (!address.getFlat().isEmpty() && !ValidationUtils.isMatchingBuildingNumberPattern(address.getFlat())) {
            Toast.makeText(this, R.string.address_validation_error_invalid_flat_number, Toast.LENGTH_LONG).show();
        } else {
            address = datasource.insertAddress(address);

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

    public void addAddressAndProtocolPadarewskiego(View view) {

//        required fields
        EditText nameField = findViewById(R.id.name);
        String name = nameField.getText().toString();

        EditText streetField = findViewById(R.id.street);
        String street = streetField.getText().toString();

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
        Address address = new Address(name, street, building, flat, district, city);
//        validate address
        if (address.getName().isEmpty() || address.getStreet().isEmpty() ||
                address.getBuilding().isEmpty() || address.getCity().isEmpty()) {
            Context context = getApplicationContext();
            CharSequence text = AlertUtils.VALIDATION_FAILED_FIELDS;
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        } else if (!ValidationUtils.isMatchingBuildingNumberPattern(address.getBuilding())) {
            Toast.makeText(this, R.string.address_validation_error_invalid_building_number, Toast.LENGTH_LONG).show();
        } else if (!address.getFlat().isEmpty() && !ValidationUtils.isMatchingBuildingNumberPattern(address.getFlat())) {
            Toast.makeText(this, R.string.address_validation_error_invalid_flat_number, Toast.LENGTH_LONG).show();
        } else {
            address = datasource.insertAddress(address);

            //save data for further entries
            SharedPreferences settings = getSharedPreferences(Utils.PREFS_NAME, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(Utils.STREET, address.getStreet());
            editor.putString(Utils.CITY, address.getCity());
            editor.putString(Utils.DISTRICT, address.getDistrinct());
            editor.commit();

            Intent intent = new Intent(this, ChooseWorker2Activity.class);
            intent.putExtra(Utils.ADDRESS_ID, address.getId());
            startActivity(intent);
        }

    }

    @Override
    protected void onResume() {
        datasource.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        datasource.close();
        super.onPause();
    }

}
