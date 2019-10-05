package com.wruzjan.ihg;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.wruzjan.ihg.utils.AlertUtils;
import com.wruzjan.ihg.utils.StringUtils;
import com.wruzjan.ihg.utils.Utils;
import com.wruzjan.ihg.utils.dao.AddressDataSource;
import com.wruzjan.ihg.utils.dao.ProtocolDataSource;
import com.wruzjan.ihg.utils.model.Address;
import com.wruzjan.ihg.utils.model.Protocol;
import com.wruzjan.ihg.utils.threading.BaseAsyncTask;
import com.wruzjan.ihg.utils.threading.GetSiemanowiceByProtocolIdAsyncTask;
import com.wruzjan.ihg.utils.view.ProgressLayout;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import androidx.annotation.NonNull;

public class ChooseWorkerActivity extends Activity {

    private AddressDataSource datasource;
    private Address address;
    //edit info
    private int addressId;
    private int protocolId;
    private boolean editFlag;

    private ProtocolDataSource protocolDataSource;

    private TextView tempInsideTextView;
    private TextView tempOutsideTextView;
    private ProgressLayout progressLayout;

    private GetSiemanowiceByProtocolIdAsyncTask getSiemanowiceByProtocolIdAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_worker);

        datasource = new AddressDataSource(this);
        datasource.open();

        protocolDataSource = new ProtocolDataSource(this);
        protocolDataSource.open();

        getSiemanowiceByProtocolIdAsyncTask = new GetSiemanowiceByProtocolIdAsyncTask(protocolDataSource);
        getSiemanowiceByProtocolIdAsyncTask.setPostExecuteUiListener(updateTemperatureListener());

        tempInsideTextView = findViewById(R.id.temp_inside);
        tempOutsideTextView = findViewById(R.id.temp_outside);
        progressLayout = findViewById(R.id.progress);

        Spinner spinner = (Spinner) findViewById(R.id.workers_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.workers, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        //get data from last entry and fill form
        SharedPreferences settings = getSharedPreferences(Utils.PREFS_NAME, 0);
        tempOutsideTextView.setText(settings.getString("tempOutside", ""));
        spinner.setSelection(settings.getInt("workerPosition", 0));

        Intent intent = getIntent();

        //get edit info
        if(intent.hasExtra(Utils.EDIT_FLAG)&&intent.hasExtra(Utils.ADDRESS_ID)&&intent.hasExtra(Utils.PROTOCOL_ID)){
            addressId = intent.getIntExtra(Utils.ADDRESS_ID, -1);
            protocolId = intent.getIntExtra(Utils.PROTOCOL_ID, -1);
            editFlag = intent.getBooleanExtra(Utils.EDIT_FLAG, false);

            progressLayout.setVisibility(View.VISIBLE);
            getSiemanowiceByProtocolIdAsyncTask.execute(protocolId);
        } else {
            //get address
            if(intent.hasExtra(Utils.ADDRESS_ID)){
                int addressId = intent.getIntExtra(Utils.ADDRESS_ID, -1);
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
    }

    /**
     * select worker button
     */
    public void selectWorker(View view) {
        //get selected worker
        Spinner workersSpinner = (Spinner) findViewById(R.id.workers_spinner);
        String worker = (String) workersSpinner.getSelectedItem();
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
            editor.putString("tempOutside", tempOutside);
            editor.putInt("workerPosition", workersSpinner.getSelectedItemPosition());
            editor.commit();

            Intent intent = new Intent(this, EnterDataActivity.class);
            if(editFlag){
                intent.putExtra(Utils.ADDRESS_ID, addressId);
                intent.putExtra(Utils.PROTOCOL_ID, protocolId);
                intent.putExtra(Utils.EDIT_FLAG, true);
            } else {
                intent.putExtra(Utils.ADDRESS_ID, address.getId());
                intent.putExtra(Utils.EDIT_FLAG, false);
            }
            intent.putExtra(Utils.WORKER_NAME, worker);
            intent.putExtra(Utils.TEMP_INSIDE, tempInside);
            intent.putExtra(Utils.TEMP_OUTSIDE, tempOutside);
            startActivity(intent);
        }
    }

    @Override
    protected void onPause() {
        datasource.close();
        protocolDataSource.close();
        getSiemanowiceByProtocolIdAsyncTask.setPostExecuteUiListener(null);
        getSiemanowiceByProtocolIdAsyncTask.cancel(true);
        super.onPause();
    }

    @Override
    protected void onResume() {
        datasource.open();
        protocolDataSource.open();
        super.onResume();
    }

    private BaseAsyncTask.PostExecuteUiListener<Protocol> updateTemperatureListener() {
        return new BaseAsyncTask.PostExecuteUiListener<Protocol>() {
            @Override
            public void onPostExecute(@NonNull Protocol protocol) {
                progressLayout.setVisibility(View.GONE);
                tempInsideTextView.setText(StringUtils.formatFloatOneDecimal(protocol.get_temp_inside()));
                tempOutsideTextView.setText(StringUtils.formatFloatOneDecimal(protocol.get_temp_outside()));
            }
        };
    }
}
