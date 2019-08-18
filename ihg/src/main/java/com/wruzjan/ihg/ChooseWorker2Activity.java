package com.wruzjan.ihg;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.wruzjan.ihg.utils.AlertUtils;
import com.wruzjan.ihg.utils.Utils;
import com.wruzjan.ihg.utils.dao.AddressDataSource;
import com.wruzjan.ihg.utils.dao.ProtocolPaderewskiegoDataSource;
import com.wruzjan.ihg.utils.model.Address;
import com.wruzjan.ihg.utils.model.ProtocolPaderewskiego;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by jantelega on 24.11.2013.
 */
public class ChooseWorker2Activity extends Activity {

    private AddressDataSource datasource;
    private ProtocolPaderewskiegoDataSource protocolDataSource;
    private Address address;
    //edit info
    private int addressId;
    private int protocolId;
    private boolean editFlag;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_worker_2);

        datasource = new AddressDataSource(this);
        datasource.open();

        protocolDataSource = new ProtocolPaderewskiegoDataSource(this);
        protocolDataSource.open();

        Spinner spinner = (Spinner) findViewById(R.id.workers_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.workers, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        //get data from last entry and fill form
        SharedPreferences settings = getSharedPreferences(Utils.PREFS_NAME, 0);
        spinner.setSelection(settings.getInt("workerPosition", 0));

        EditText tempInsideField = (EditText) findViewById(R.id.temp_inside);
        tempInsideField.setText(settings.getString("tempInside", null), TextView.BufferType.EDITABLE);

        EditText tempOutsideField = (EditText) findViewById(R.id.temp_outside);
        tempOutsideField.setText(settings.getString("tempOutside", null), TextView.BufferType.EDITABLE);

        EditText windSpeedField = (EditText) findViewById(R.id.wind_speed);
        windSpeedField.setText(settings.getString("windSpeed", null), TextView.BufferType.EDITABLE);


        Spinner windDirectionSpinner = (Spinner) findViewById(R.id.wind_direction_spinner);
        ArrayAdapter<CharSequence> windDirectionAdapter = ArrayAdapter.createFromResource(this,
                R.array.wind_directions, android.R.layout.simple_spinner_item);
        windDirectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        windDirectionSpinner.setAdapter(windDirectionAdapter);
        windDirectionSpinner.setSelection(settings.getInt("windDirectionPosition", 0));

        EditText pressureField = (EditText) findViewById(R.id.pressure);
        pressureField.setText(settings.getString("pressure", null), TextView.BufferType.EDITABLE);


        Intent intent = getIntent();

        //get edit info
        if(intent.hasExtra(Utils.EDIT_FLAG)&&intent.hasExtra(Utils.ADDRESS_ID)&&intent.hasExtra(Utils.PROTOCOL_ID)){
            addressId = intent.getIntExtra(Utils.ADDRESS_ID, -1);
            address = datasource.getAddressById(addressId);
            protocolId = intent.getIntExtra(Utils.PROTOCOL_ID, -1);
            editFlag = intent.getBooleanExtra(Utils.EDIT_FLAG, false);
        } else {
            editFlag = false;
            //get address
            if(intent.hasExtra(Utils.ADDRESS_ID)){
                addressId = intent.getIntExtra(Utils.ADDRESS_ID, -1);
                address = datasource.getAddressById(addressId);

                //override check
                String str_path = Environment.getExternalStorageDirectory().toString() + "/IHG/" + address.getCity() + "/";
                if (address.getDistrinct().isEmpty()) {
                    str_path = str_path + "inne";
                } else {
                    str_path = str_path + address.getDistrinct().trim();
                }
                str_path = str_path + "/" + address.getStreet().trim() + "/" + new SimpleDateFormat("yyyy").format(Calendar.getInstance().getTime());
                boolean success = (new File(str_path).mkdirs());
                str_path = str_path + "/" + address.getStreet().trim() + "_" + address.getBuilding().trim() + "_" + address.getFlat().trim() + "_" + new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime()) + ".pdf";

                //display override info
                    if(new File(str_path).exists()){
                        Context context = getApplicationContext();
                        CharSequence text = AlertUtils.PROTOCOL_ALREADY_ENTERED_PREFIX
                                +address.getCity()+", "
                                +address.getStreet()+" "
                                +address.getBuilding()+"/"
                                +address.getFlat()
                                +AlertUtils.PROTOCOL_ALREADY_ENTERED_POSTFIX;
                        int duration = Toast.LENGTH_LONG;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }

            } else {
                Context context = getApplicationContext();
                CharSequence text = AlertUtils.SELECT_ADDRESSES_PROTOCOLS_ADD;
                int duration = Toast.LENGTH_LONG;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        }
        if(editFlag){
            ProtocolPaderewskiego protocolEdited = protocolDataSource.getPaderewskiegoProtocolsById(protocolId);
            EditText windowsAllTextView = (EditText) findViewById(R.id.windows_all);
            windowsAllTextView.setText(Integer.toString(protocolEdited.get_windows_all()));
            EditText windowsMicroTextView = (EditText) findViewById(R.id.windows_micro);
            windowsMicroTextView.setText(Integer.toString(protocolEdited.get_windows_micro()));
            EditText windowsVentTextView = (EditText) findViewById(R.id.windows_vent);
            windowsVentTextView.setText(Integer.toString(protocolEdited.get_windows_vent()));
            EditText windowsNoMicroTextView = (EditText) findViewById(R.id.windows_no_micro);
            windowsNoMicroTextView.setText(Integer.toString(protocolEdited.get_windows_no_micro()));
        }
    }

    @Override
    protected void onPause() {
        datasource.close();
        protocolDataSource.close();
        super.onPause();

    }

    @Override
    protected void onResume() {
        datasource.open();
        protocolDataSource.open();
        super.onResume();
    }

    public void save(View view) {
        //get selected worker
        Spinner workersSpinner = (Spinner) findViewById(R.id.workers_spinner);
        String worker = (String) workersSpinner.getSelectedItem();
        //get data from form
        TextView tempInsideTextView = (TextView) findViewById(R.id.temp_inside);
        String tempInside = tempInsideTextView.getText().toString();
        TextView tempOutsideTextView = (TextView) findViewById(R.id.temp_outside);
        String tempOutside = tempOutsideTextView.getText().toString();
        TextView windSpeedTextView = (TextView) findViewById(R.id.wind_speed);
        String windSpeed = windSpeedTextView.getText().toString();
        Spinner windDirectionSpinner = (Spinner) findViewById(R.id.wind_direction_spinner);
        String windDirection = (String) windDirectionSpinner.getSelectedItem();
        TextView pressureTextView = (TextView) findViewById(R.id.pressure);
        String pressure = pressureTextView.getText().toString();
        TextView windowsAllTextView = (TextView) findViewById(R.id.windows_all);
        String windowsAll = windowsAllTextView.getText().toString();
        TextView windowsMicroTextView = (TextView) findViewById(R.id.windows_micro);
        String windowsMicro = windowsMicroTextView.getText().toString();
        TextView windowsVentTextView = (TextView) findViewById(R.id.windows_vent);
        String windowsVent = windowsVentTextView.getText().toString();
        TextView windowsNoMicroTextView = (TextView) findViewById(R.id.windows_no_micro);
        String windowsNoMicro = windowsNoMicroTextView.getText().toString();

        //keep data and start EnterDataActivity
        if(tempInside.isEmpty() || tempOutside.isEmpty()){
            Context context = getApplicationContext();
            CharSequence text = AlertUtils.TEMP_MISSING;
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        } else {
            //save data for further entries
            SharedPreferences settings = getSharedPreferences(Utils.PREFS_NAME, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt("workerPosition", workersSpinner.getSelectedItemPosition());
            editor.putInt("windDirectionPosition", windDirectionSpinner.getSelectedItemPosition());
            editor.putString("tempInside", tempInside);
            editor.putString("tempOutside", tempOutside);
            editor.putString("windSpeed", windSpeed);
            editor.putString("pressure", pressure);
            editor.commit();

            Intent intent = new Intent(this, EnterData2Activity.class);
            if(editFlag){
                intent.putExtra(Utils.PROTOCOL_ID, protocolId);
            }
            intent.putExtra(Utils.EDIT_FLAG, editFlag);
            intent.putExtra(Utils.ADDRESS_ID, address.getId());
            intent.putExtra(Utils.WORKER_NAME, worker);
            intent.putExtra(Utils.TEMP_INSIDE, tempInside);
            intent.putExtra(Utils.TEMP_OUTSIDE, tempOutside);
            intent.putExtra(Utils.WIND_SPEED, windSpeed);
            intent.putExtra(Utils.WIND_DIRECTION, windDirection);
            intent.putExtra(Utils.PRESSURE, pressure);
            intent.putExtra(Utils.WINDOWS_ALL, windowsAll);
            intent.putExtra(Utils.WINDOWS_MICRO, windowsMicro);
            intent.putExtra(Utils.WINDOWS_VENT, windowsVent);
            intent.putExtra(Utils.WINDOWS_NO_MICRO, windowsNoMicro);
            startActivity(intent);
        }
    }
}