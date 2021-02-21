package com.wruzjan.ihg;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.wruzjan.ihg.utils.AdapterUtils;
import com.wruzjan.ihg.utils.AlertUtils;
import com.wruzjan.ihg.utils.LocatorIdGenerator;
import com.wruzjan.ihg.utils.StringUtils;
import com.wruzjan.ihg.utils.Utils;
import com.wruzjan.ihg.utils.dao.AddressDataSource;
import com.wruzjan.ihg.utils.dao.ProtocolNewPaderewskiegoDataSource;
import com.wruzjan.ihg.utils.dao.StreetAndIdentifierDataSource;
import com.wruzjan.ihg.utils.model.Address;
import com.wruzjan.ihg.utils.model.ProtocolNewPaderewskiego;
import com.wruzjan.ihg.utils.model.StreetAndIdentifier;
import com.wruzjan.ihg.utils.threading.BaseAsyncTask;
import com.wruzjan.ihg.utils.threading.GetNewPaderewskiegoProtocolsByIdAsyncTask;
import com.wruzjan.ihg.utils.view.InstantAutoCompleteTextView;
import com.wruzjan.ihg.utils.view.ProgressLayout;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import androidx.annotation.NonNull;

/**
 * Created by jantelega on 12.10.2016.
 */
public class ChooseWorkerNewPaderewskiegoActivity extends Activity {

    private AddressDataSource datasource;
    private StreetAndIdentifierDataSource streetAndIdentifierDataSource;
    private Address address;
    //edit info
    private int addressId;
    private int protocolId;
    private boolean editFlag;

    private ProtocolNewPaderewskiegoDataSource protocolDataSource;

    private TextView tempInsideTextView;
    private TextView tempOutsideTextView;
    private ProgressLayout progressLayout;
    private InstantAutoCompleteTextView companyAddressTextView;
    private InstantAutoCompleteTextView protocolTypeTextView;

    private GetNewPaderewskiegoProtocolsByIdAsyncTask getNewPaderewskiegoProtocolsByIdAsyncTask;

    private final LocatorIdGenerator locatorIdGenerator = new LocatorIdGenerator();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_worker_new_paderewskiego);

        datasource = new AddressDataSource(this);
        datasource.open();

        streetAndIdentifierDataSource = new StreetAndIdentifierDataSource(this);
        streetAndIdentifierDataSource.open();

        protocolDataSource = new ProtocolNewPaderewskiegoDataSource(this);
        protocolDataSource.open();

        getNewPaderewskiegoProtocolsByIdAsyncTask = new GetNewPaderewskiegoProtocolsByIdAsyncTask(protocolDataSource);
        getNewPaderewskiegoProtocolsByIdAsyncTask.setPostExecuteUiListener(updateTemperatureListener());

        tempInsideTextView = findViewById(R.id.temp_inside);
        tempOutsideTextView = findViewById(R.id.temp_outside);
        progressLayout = findViewById(R.id.progress);

        companyAddressTextView = findViewById(R.id.company_address_text_view);
        companyAddressTextView.setThreshold(0);

        String[] newPaderewskiegoCompanyAddresses = getResources().getStringArray(R.array.new_paderewskiego_company_headers);
        companyAddressTextView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, newPaderewskiegoCompanyAddresses));
        companyAddressTextView.setText(PreferenceManager.getDefaultSharedPreferences(this).getString(Utils.PREF_NEW_PADEREWSKIEGO_COMPANY_ADDRESS, newPaderewskiegoCompanyAddresses[0]));

        protocolTypeTextView = findViewById(R.id.protocol_type_text_view);
        protocolTypeTextView.setThreshold(0);

        String[] newPaderewskiegoProtocolType = getResources().getStringArray(R.array.new_paderewskiego_protocol_type);
        protocolTypeTextView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, newPaderewskiegoProtocolType));
        protocolTypeTextView.setText(PreferenceManager.getDefaultSharedPreferences(this).getString(Utils.PREF_NEW_PADEREWSKIEGO_PROTOCOL_TYPE, newPaderewskiegoProtocolType[0]));

        // workers dropdown list
        Spinner workersSpinner = findViewById(R.id.workers_spinner);
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        workersSpinner.setAdapter(adapter);

        //get worker data from last entry and fill form
        SharedPreferences settings = getSharedPreferences(Utils.PREFS_NAME, 0);
        tempOutsideTextView.setText(settings.getString(Utils.OUTSIDE_TEMPERATURE_NOWY_PADEREWSKIEGO, ""));

        Intent intent = getIntent();

        //get edit info
        if(intent.hasExtra(Utils.EDIT_FLAG)&&intent.hasExtra(Utils.ADDRESS_ID)&&intent.hasExtra(Utils.PROTOCOL_ID)){
            addressId = intent.getIntExtra(Utils.ADDRESS_ID, -1);
            protocolId = intent.getIntExtra(Utils.PROTOCOL_ID, -1);
            editFlag = intent.getBooleanExtra(Utils.EDIT_FLAG, false);

            progressLayout.setVisibility(View.VISIBLE);
            getNewPaderewskiegoProtocolsByIdAsyncTask.execute(protocolId);
        } else {
            adapter.addAll(getResources().getStringArray(R.array.workers));
            workersSpinner.setSelection(settings.getInt(Utils.WORKER_POSITION, 0));

            //get address
            if(intent.hasExtra(Utils.ADDRESS_ID)){
                int addressId = intent.getIntExtra(Utils.ADDRESS_ID, -1);
                address = datasource.getAddressById(addressId);
                StreetAndIdentifier streetAndIdentifier = streetAndIdentifierDataSource.getByStreetIdentifier(address.getStreetAndIdentifierId());
                String streetName = streetAndIdentifier != null && address.getStreetAndIdentifierId() != -1 ? streetAndIdentifier.getStreetName() : address.getStreet();

                //override check
                String str_path = Environment.getExternalStorageDirectory().toString() + "/IHG/" + address.getCity() + "/";
                if (address.getDistrinct().isEmpty()) {
                    str_path = str_path + "inne";
                } else {
                    str_path = str_path + address.getDistrinct().trim();
                }
                str_path = str_path + "/" + streetName.trim() + "/" + new SimpleDateFormat("yyyy").format(Calendar.getInstance().getTime());
                boolean success = (new File(str_path).mkdirs());

                if (streetAndIdentifier != null && address.getStreetAndIdentifierId() != -1) {
                    str_path = str_path + "/" + locatorIdGenerator.generate(address, streetAndIdentifier) + "_" + new SimpleDateFormat("dd.MM.yyyy").format(Calendar.getInstance().getTime()) + ".pdf";
                } else {
                    str_path = str_path + "/" + streetName.trim() + "_" + address.getBuilding().trim() + "_" + address.getFlat().trim() + "_" + new SimpleDateFormat("dd.MM.yyyy").format(Calendar.getInstance().getTime()) + ".pdf";
                }

                //display override info
                if(new File(str_path).exists()){
                    Context context = getApplicationContext();
                    CharSequence text = AlertUtils.PROTOCOL_ALREADY_ENTERED_PREFIX
                            +address.getCity()+", "
                            +streetName+" "
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
    }

    @Override
    protected void onPause() {
        datasource.close();
        streetAndIdentifierDataSource.close();
        protocolDataSource.close();
        getNewPaderewskiegoProtocolsByIdAsyncTask.setPostExecuteUiListener(null);
        getNewPaderewskiegoProtocolsByIdAsyncTask.cancel(true);
        super.onPause();

    }

    @Override
    protected void onResume() {
        datasource.open();
        streetAndIdentifierDataSource.open();
        protocolDataSource.open();
        super.onResume();
    }

    /**
     * select worker button
     */
    public void selectWorker(View view) {
        //get selected worker
        Spinner workersSpinner = findViewById(R.id.workers_spinner);
        String worker = (String) workersSpinner.getSelectedItem();

        if (worker.equalsIgnoreCase(getApplication().getString(R.string.no_inspector_chosen_item))) {
            Toast.makeText(this, getApplication().getString(R.string.no_inspector_chosen_message), Toast.LENGTH_SHORT).show();
            return;
        }

        //get temperatures
        String tempInside = tempInsideTextView.getText().toString();
        String tempOutside = tempOutsideTextView.getText().toString();
        //keep worker name and start EnterKitchenDataActivity

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
            editor.putString(Utils.OUTSIDE_TEMPERATURE_NOWY_PADEREWSKIEGO, tempOutside);
            editor.putInt(Utils.WORKER_POSITION, workersSpinner.getSelectedItemPosition());
            editor.commit();

            Intent intent = new Intent(this, EnterDataNewPaderewskiegoActivity.class);
            if(editFlag){
                intent.putExtra(Utils.ADDRESS_ID, addressId);
                intent.putExtra(Utils.PROTOCOL_ID, protocolId);
                intent.putExtra(Utils.EDIT_FLAG, true);
            } else {
                intent.putExtra(Utils.ADDRESS_ID, address.getId());
                intent.putExtra(Utils.EDIT_FLAG, false);
            }
            intent.putExtra(EnterDataActivity.EXTRA_COMPANY_ADDRESS, companyAddressTextView.getText().toString());
            intent.putExtra(EnterDataActivity.EXTRA_PROTOCOL_TYPE, protocolTypeTextView.getText().toString());
            intent.putExtra(Utils.WORKER_NAME, worker);
            intent.putExtra(Utils.TEMP_INSIDE, tempInside);
            intent.putExtra(Utils.TEMP_OUTSIDE, tempOutside);
            startActivity(intent);
        }
    }

    private BaseAsyncTask.PostExecuteUiListener<ProtocolNewPaderewskiego> updateTemperatureListener() {
        return new BaseAsyncTask.PostExecuteUiListener<ProtocolNewPaderewskiego>() {
            @Override
            public void onPostExecute(@NonNull ProtocolNewPaderewskiego protocol) {
                progressLayout.setVisibility(View.GONE);
                tempInsideTextView.setText(StringUtils.formatFloatOneDecimal(protocol.get_temp_inside()));
                tempOutsideTextView.setText(StringUtils.formatFloatOneDecimal(protocol.get_temp_outside()));
                companyAddressTextView.setText(protocol.getCompanyAddress());
                protocolTypeTextView.setText(protocol.getProtocolType());

                Spinner workersSpinner = findViewById(R.id.workers_spinner);
                String[] workers = getResources().getStringArray(R.array.workers);

                boolean workerExists = false;

                for (String worker : workers) {
                    if (worker.equalsIgnoreCase(protocol.get_worker_name())) {
                        workerExists = true;
                        break;
                    }
                }

                ArrayAdapter<CharSequence> workersAdapter = (ArrayAdapter<CharSequence>) workersSpinner.getAdapter();

                if (workerExists) {
                    workersAdapter.addAll(workers);
                    AdapterUtils.setItemToSpinner(workersSpinner, protocol.get_worker_name());
                } else {
                    workersAdapter.add(getApplication().getString(R.string.no_inspector_chosen_item));
                    workersAdapter.addAll(workers);
                    workersSpinner.setSelection(0);
                }

            }
        };
    }
}