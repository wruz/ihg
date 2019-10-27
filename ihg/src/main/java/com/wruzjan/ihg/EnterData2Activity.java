package com.wruzjan.ihg;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wruzjan.ihg.utils.AlertUtils;
import com.wruzjan.ihg.utils.NetworkStateChecker;
import com.wruzjan.ihg.utils.Utils;
import com.wruzjan.ihg.utils.dao.AddressDataSource;
import com.wruzjan.ihg.utils.dao.AwaitingProtocolDataSource;
import com.wruzjan.ihg.utils.dao.ProtocolPaderewskiegoDataSource;
import com.wruzjan.ihg.utils.model.Address;
import com.wruzjan.ihg.utils.model.AwaitingProtocol;
import com.wruzjan.ihg.utils.model.ProtocolPaderewskiego;
import com.wruzjan.ihg.utils.pdf.GeneratePDFPaderewskiego;
import com.wruzjan.ihg.utils.printer.BluetoothConnectionPaderewskiego;
import com.zebra.android.discovery.BluetoothDiscoverer;
import com.zebra.android.discovery.DiscoveredPrinter;
import com.zebra.android.discovery.DiscoveryHandler;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.FileProvider;

public class EnterData2Activity extends Activity {

    private AddressDataSource addressDataSource;
    private ProtocolPaderewskiegoDataSource protocolPaderewskiegoDataSource;
    private AwaitingProtocolDataSource awaitingProtocolDataSource;
    private NetworkStateChecker networkStateChecker;
    private Address address;
    private ProtocolPaderewskiego PROTOCOL;
    private String pdfFilePath;
    private int windows_no_micro;
    private int windows_micro;
    private int windows_all;
    private boolean protocolSaved = false;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_data_2);

        addressDataSource = new AddressDataSource(this);
        addressDataSource.open();

        protocolPaderewskiegoDataSource = new ProtocolPaderewskiegoDataSource(this);
        protocolPaderewskiegoDataSource.open();

        awaitingProtocolDataSource = new AwaitingProtocolDataSource(this);
        awaitingProtocolDataSource.open();

        Intent intent = getIntent();

        SwitchCompat gasMeterSwitchCompat = findViewById(R.id.eq_gas_meter_value);
        TextView gasMeterSwitchCompatText = findViewById(R.id.eq_gas_meter_value_text);
        setTextOnOffLabelChangeListener(gasMeterSwitchCompat, gasMeterSwitchCompatText);

        SwitchCompat stoveSwitchCompat = findViewById(R.id.eq_stove_value);
        TextView stoveSwitchCompatText = findViewById(R.id.eq_stove_value_text);
        setTextOnOffLabelChangeListener(stoveSwitchCompat, stoveSwitchCompatText);
        SwitchCompat bakeSwitchCompat = findViewById(R.id.eq_bake_value);
        TextView bakeSwitchCompatText = findViewById(R.id.eq_bake_value_text);
        setTextOnOffLabelChangeListener(bakeSwitchCompat, bakeSwitchCompatText);
        SwitchCompat combiOvenSwitchCompat = findViewById(R.id.eq_combi_oven_value);
        TextView combiOvenSwitchCompatText = findViewById(R.id.eq_combi_oven_value_text);
        setTextOnOffLabelChangeListener(combiOvenSwitchCompat, combiOvenSwitchCompatText);
        SwitchCompat kitchenTermSwitchCompat = findViewById(R.id.eq_kitchen_term_value);
        TextView kitchenTermSwitchCompatText = findViewById(R.id.eq_kitchen_term_value_text);
        setTextOnOffLabelChangeListener(kitchenTermSwitchCompat, kitchenTermSwitchCompatText);

        networkStateChecker = new NetworkStateChecker(getApplication());

        if(intent.hasExtra(Utils.WINDOWS_NO_MICRO)){
            if(!intent.getStringExtra(Utils.WINDOWS_NO_MICRO).equals(AlertUtils.BLANK))
                windows_no_micro = Integer.parseInt(intent.getStringExtra(Utils.WINDOWS_NO_MICRO));
            if(intent.getStringExtra(Utils.WINDOWS_NO_MICRO).equals(AlertUtils.BLANK))
                windows_no_micro = 0;
        }

        if(intent.hasExtra(Utils.WINDOWS_MICRO)){
            if(!intent.getStringExtra(Utils.WINDOWS_MICRO).equals(AlertUtils.BLANK))
                windows_micro = Integer.parseInt(intent.getStringExtra(Utils.WINDOWS_MICRO));
            if(intent.getStringExtra(Utils.WINDOWS_MICRO).equals(AlertUtils.BLANK))
                windows_micro = 0;
        }

        if(intent.hasExtra(Utils.WINDOWS_ALL)){
            if(!intent.getStringExtra(Utils.WINDOWS_ALL).equals(AlertUtils.BLANK))
                windows_all = Integer.parseInt(intent.getStringExtra(Utils.WINDOWS_ALL));
            if(intent.getStringExtra(Utils.WINDOWS_ALL).equals(AlertUtils.BLANK))
                windows_all = 0;
        }

        EditText kitchenMicro = (EditText) findViewById(R.id.kitchen_airflow_microventilation);
        if(windows_micro==0 || windows_no_micro==windows_all){
            kitchenMicro.setHint(AlertUtils.BLANK);
        } else {
            kitchenMicro.setHint(AlertUtils.FIELD_REQUIRED);
        }

        final SwitchCompat kitchenAvailableSwitchCompat = findViewById(R.id.kitchen_availability);
        final TextView kitchenAvailableSwitchCompatText = findViewById(R.id.kitchen_availability_text);
        kitchenAvailableSwitchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    EditText kitchenMicro = (EditText) findViewById(R.id.kitchen_airflow_microventilation);
                    if(windows_micro==0 || windows_no_micro==windows_all){
                        kitchenMicro.setHint(AlertUtils.BLANK);
                    } else {
                        kitchenMicro.setHint(AlertUtils.FIELD_REQUIRED);
                    }
                    EditText kitchenClosed = (EditText) findViewById(R.id.kitchen_airflow_windows_closed);
                    kitchenClosed.setHint(AlertUtils.FIELD_REQUIRED);
                    EditText kitchenOpen = (EditText) findViewById(R.id.kitchen_airflow_open);
                    kitchenOpen.setHint(AlertUtils.FIELD_REQUIRED);
                    kitchenAvailableSwitchCompatText.setText(kitchenAvailableSwitchCompat.getTextOn());
                } else {
                    // The toggle is disabled
                    EditText kitchenMicro = (EditText) findViewById(R.id.kitchen_airflow_microventilation);
                    kitchenMicro.setHint(AlertUtils.BLANK);
                    EditText kitchenClosed = (EditText) findViewById(R.id.kitchen_airflow_windows_closed);
                    kitchenClosed.setHint(AlertUtils.BLANK);
                    EditText kitchenOpen = (EditText) findViewById(R.id.kitchen_airflow_open);
                    kitchenOpen.setHint(AlertUtils.BLANK);
                    kitchenAvailableSwitchCompatText.setText(kitchenAvailableSwitchCompat.getTextOff());
                }
            }
        });

        EditText toiletMicro = (EditText) findViewById(R.id.toilet_airflow_microventilation);
        if(windows_micro==0 || windows_no_micro==windows_all){
            toiletMicro.setHint(AlertUtils.BLANK);
        } else {
            toiletMicro.setHint(AlertUtils.FIELD_REQUIRED);
        }

        final SwitchCompat toiletAvailableSwitchCompat = findViewById(R.id.toilet_availability);
        final TextView toiletAvailableSwitchCompatText = findViewById(R.id.toilet_availability_text);
        toiletAvailableSwitchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    EditText toiletMicro = (EditText) findViewById(R.id.toilet_airflow_microventilation);
                    if(windows_micro==0 || windows_no_micro==windows_all){
                        toiletMicro.setHint(AlertUtils.BLANK);
                    } else {
                        toiletMicro.setHint(AlertUtils.FIELD_REQUIRED);
                    }
                    EditText toiletClosed = (EditText) findViewById(R.id.toilet_airflow_windows_closed);
                    toiletClosed.setHint(AlertUtils.FIELD_REQUIRED);
                    EditText toiletOpen = (EditText) findViewById(R.id.toilet_airflow_open);
                    toiletOpen.setHint(AlertUtils.FIELD_REQUIRED);
                    CheckBox checkToiletOthers = (CheckBox) findViewById(R.id.toilet_checkbox_others);
                    checkToiletOthers.setChecked(false);
                    TextView toiletOthersTextView = (TextView) findViewById(R.id.toilet_others);
                    toiletOthersTextView.setText(AlertUtils.BLANK);
                    toiletAvailableSwitchCompatText.setText(toiletAvailableSwitchCompat.getTextOn());
                } else {
                    // The toggle is disabled
                    EditText toiletMicro = (EditText) findViewById(R.id.toilet_airflow_microventilation);
                    toiletMicro.setHint(AlertUtils.BLANK);
                    EditText toiletClosed = (EditText) findViewById(R.id.toilet_airflow_windows_closed);
                    toiletClosed.setHint(AlertUtils.BLANK);
                    EditText toiletOpen = (EditText) findViewById(R.id.toilet_airflow_open);
                    toiletOpen.setHint(AlertUtils.BLANK);
                    CheckBox checkToiletOthers = (CheckBox) findViewById(R.id.toilet_checkbox_others);
                    checkToiletOthers.setChecked(true);
                    TextView toiletOthersTextView = (TextView) findViewById(R.id.toilet_others);
                    toiletOthersTextView.setText(AlertUtils.UNAVAILABLE);
                    toiletAvailableSwitchCompatText.setText(toiletAvailableSwitchCompat.getTextOff());
                }
            }
        });

        EditText bathMicro = (EditText) findViewById(R.id.bath_airflow_microventilation);
        if(windows_micro==0 || windows_no_micro==windows_all){
            bathMicro.setHint(AlertUtils.BLANK);
        } else {
            bathMicro.setHint(AlertUtils.FIELD_REQUIRED);
        }

        final SwitchCompat bathAvailableSwitchCompat = findViewById(R.id.bath_availability);
        final TextView bathAvailableSwitchCompatText = findViewById(R.id.bath_availability_text);
        bathAvailableSwitchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    EditText bathMicro = (EditText) findViewById(R.id.bath_airflow_microventilation);
                    if(windows_micro==0 || windows_no_micro==windows_all){
                        bathMicro.setHint(AlertUtils.BLANK);
                    } else {
                        bathMicro.setHint(AlertUtils.FIELD_REQUIRED);
                    }
                    EditText bathClosed = (EditText) findViewById(R.id.bath_airflow_windows_closed);
                    bathClosed.setHint(AlertUtils.FIELD_REQUIRED);
                    EditText bathOpen = (EditText) findViewById(R.id.bath_airflow_open);
                    bathOpen.setHint(AlertUtils.FIELD_REQUIRED);
                    bathAvailableSwitchCompatText.setText(bathAvailableSwitchCompat.getTextOn());
                } else {
                    // The toggle is disabled
                    EditText bathMicro = (EditText) findViewById(R.id.bath_airflow_microventilation);
                    bathMicro.setHint(AlertUtils.BLANK);
                    EditText bathClosed = (EditText) findViewById(R.id.bath_airflow_windows_closed);
                    bathClosed.setHint(AlertUtils.BLANK);
                    EditText bathOpen = (EditText) findViewById(R.id.bath_airflow_open);
                    bathOpen.setHint(AlertUtils.BLANK);
                    bathAvailableSwitchCompatText.setText(bathAvailableSwitchCompat.getTextOff());
                }
            }
        });

        EditText flueMicro = (EditText) findViewById(R.id.flue_airflow_microventilation);
        if(windows_micro==0 || windows_no_micro==windows_all){
            flueMicro.setHint(AlertUtils.BLANK);
        } else {
            flueMicro.setHint(AlertUtils.FIELD_REQUIRED);
        }

        final SwitchCompat flueAvailableSwitchCompat = findViewById(R.id.flue_availability);
        final TextView flueAvailableSwitchCompatText = findViewById(R.id.flue_availability_text);
        flueAvailableSwitchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    EditText flueMicro = (EditText) findViewById(R.id.flue_airflow_microventilation);
                    if(windows_micro==0 || windows_no_micro==windows_all){
                        flueMicro.setHint(AlertUtils.BLANK);
                    } else {
                        flueMicro.setHint(AlertUtils.FIELD_REQUIRED);
                    }
                    EditText flueClosed = (EditText) findViewById(R.id.flue_airflow_windows_closed);
                    flueClosed.setHint(AlertUtils.FIELD_REQUIRED);
                    EditText flueOpen = (EditText) findViewById(R.id.flue_airflow_open);
                    flueOpen.setHint(AlertUtils.FIELD_REQUIRED);
                    flueAvailableSwitchCompatText.setText(flueAvailableSwitchCompat.getTextOn());
                } else {
                    // The toggle is disabled
                    EditText flueMicro = (EditText) findViewById(R.id.flue_airflow_microventilation);
                    flueMicro.setHint(AlertUtils.BLANK);
                    EditText flueClosed = (EditText) findViewById(R.id.flue_airflow_windows_closed);
                    flueClosed.setHint(AlertUtils.BLANK);
                    EditText flueOpen = (EditText) findViewById(R.id.flue_airflow_open);
                    flueOpen.setHint(AlertUtils.BLANK);
                    flueAvailableSwitchCompatText.setText(flueAvailableSwitchCompat.getTextOff());
                }
            }
        });

        //fill comments for user
        TextView userCommentsTextView = (TextView) findViewById(R.id.comments_for_user);
        userCommentsTextView.setText(AlertUtils.USER_COMMENTS_TEMPLATE);

        //kitchen autocomplete
        CheckBox checkKitchenInaccessible = (CheckBox) findViewById(R.id.kitchen_checkbox_inaccessible);
        checkKitchenInaccessible.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                TextView userCommentsTextView = (TextView) findViewById(R.id.comments_for_user);
                String userComments = userCommentsTextView.getText().toString();
                if(isChecked){
                    userCommentsTextView.setText(addCommentsForUser(AlertUtils.ENABLE_KITCHEN_ACCESS, userComments));
                } else {
                    userCommentsTextView.setText(removeCommentsForUser(AlertUtils.ENABLE_KITCHEN_ACCESS, userComments));
                }
            }
        });
        CheckBox checkKitchenSteady = (CheckBox) findViewById(R.id.kitchen_checkbox_steady);
        checkKitchenSteady.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                TextView userCommentsTextView = (TextView) findViewById(R.id.comments_for_user);
                String userComments = userCommentsTextView.getText().toString();
                if(isChecked){
                    userCommentsTextView.setText(addCommentsForUser(AlertUtils.ENABLE_KITCHEN_ACCESS, userComments));
                } else {
                    userCommentsTextView.setText(removeCommentsForUser(AlertUtils.ENABLE_KITCHEN_ACCESS, userComments));
                }
            }
        });
        CheckBox checkKitchenHood = (CheckBox) findViewById(R.id.kitchen_checkbox_hood);
        checkKitchenHood.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                TextView userCommentsTextView = (TextView) findViewById(R.id.comments_for_user);
                String userComments = userCommentsTextView.getText().toString();
                if(isChecked){
                    userCommentsTextView.setText(addCommentsForUser(AlertUtils.REMOVE_KITCHEN_LIFT, userComments));
                } else {
                    userCommentsTextView.setText(removeCommentsForUser(AlertUtils.REMOVE_KITCHEN_LIFT, userComments));
                }
            }
        });
        CheckBox checkKitchenVent = (CheckBox) findViewById(R.id.kitchen_checkbox_vent);
        checkKitchenVent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                TextView userCommentsTextView = (TextView) findViewById(R.id.comments_for_user);
                String userComments = userCommentsTextView.getText().toString();
                if(isChecked){
                    userCommentsTextView.setText(addCommentsForUser(AlertUtils.REMOVE_KITCHEN_LIFT, userComments));
                } else {
                    userCommentsTextView.setText(removeCommentsForUser(AlertUtils.REMOVE_KITCHEN_LIFT, userComments));
                }
            }
        });

        //toilet autocomplete
        CheckBox checkToiletInaccessible = (CheckBox) findViewById(R.id.toilet_checkbox_inaccessible);
        checkToiletInaccessible.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                TextView userCommentsTextView = (TextView) findViewById(R.id.comments_for_user);
                String userComments = userCommentsTextView.getText().toString();
                if(isChecked){
                    userCommentsTextView.setText(addCommentsForUser(AlertUtils.ENABLE_TOILET_ACCESS, userComments));
                } else {
                    userCommentsTextView.setText(removeCommentsForUser(AlertUtils.ENABLE_TOILET_ACCESS, userComments));
                }
            }
        });
        CheckBox checkToiletSteady = (CheckBox) findViewById(R.id.toilet_checkbox_steady);
        checkToiletSteady.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                TextView userCommentsTextView = (TextView) findViewById(R.id.comments_for_user);
                String userComments = userCommentsTextView.getText().toString();
                if(isChecked){
                    userCommentsTextView.setText(addCommentsForUser(AlertUtils.ENABLE_TOILET_ACCESS, userComments));
                } else {
                    userCommentsTextView.setText(removeCommentsForUser(AlertUtils.ENABLE_TOILET_ACCESS, userComments));
                }
            }
        });
        CheckBox checkToiletVent = (CheckBox) findViewById(R.id.toilet_checkbox_vent);
        checkToiletVent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                TextView userCommentsTextView = (TextView) findViewById(R.id.comments_for_user);
                String userComments = userCommentsTextView.getText().toString();
                if(isChecked){
                    userCommentsTextView.setText(addCommentsForUser(AlertUtils.REMOVE_TOILET_LIFT, userComments));
                } else {
                    userCommentsTextView.setText(removeCommentsForUser(AlertUtils.REMOVE_TOILET_LIFT, userComments));
                }
            }
        });

        //bath autocomplete
        CheckBox checkBathInaccessible = (CheckBox) findViewById(R.id.bath_checkbox_inaccessible);
        checkBathInaccessible.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                TextView userCommentsTextView = (TextView) findViewById(R.id.comments_for_user);
                String userComments = userCommentsTextView.getText().toString();
                if(isChecked){
                    userCommentsTextView.setText(addCommentsForUser(AlertUtils.ENABLE_BATH_ACCESS, userComments));
                } else {
                    userCommentsTextView.setText(removeCommentsForUser(AlertUtils.ENABLE_BATH_ACCESS, userComments));
                }
            }
        });
        CheckBox checkBathSteady = (CheckBox) findViewById(R.id.bath_checkbox_steady);
        checkBathSteady.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                TextView userCommentsTextView = (TextView) findViewById(R.id.comments_for_user);
                String userComments = userCommentsTextView.getText().toString();
                if(isChecked){
                    userCommentsTextView.setText(addCommentsForUser(AlertUtils.ENABLE_BATH_ACCESS, userComments));
                } else {
                    userCommentsTextView.setText(removeCommentsForUser(AlertUtils.ENABLE_BATH_ACCESS, userComments));
                }
            }
        });
        CheckBox checkBathVent = (CheckBox) findViewById(R.id.bath_checkbox_vent);
        checkBathVent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                TextView userCommentsTextView = (TextView) findViewById(R.id.comments_for_user);
                String userComments = userCommentsTextView.getText().toString();
                if(isChecked){
                    userCommentsTextView.setText(addCommentsForUser(AlertUtils.REMOVE_BATH_LIFT, userComments));
                } else {
                    userCommentsTextView.setText(removeCommentsForUser(AlertUtils.REMOVE_BATH_LIFT, userComments));
                }
            }
        });

        //flue autocomplete
        CheckBox checkFlueInaccessible = (CheckBox) findViewById(R.id.flue_checkbox_inaccessible);
        checkFlueInaccessible.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                TextView userCommentsTextView = (TextView) findViewById(R.id.comments_for_user);
                String userComments = userCommentsTextView.getText().toString();
                if(isChecked){
                    userCommentsTextView.setText(addCommentsForUser(AlertUtils.ENABLE_FLUE_ACCESS, userComments));
                } else {
                    userCommentsTextView.setText(removeCommentsForUser(AlertUtils.ENABLE_FLUE_ACCESS, userComments));
                }
            }
        });
        CheckBox checkFlueSteady = (CheckBox) findViewById(R.id.flue_checkbox_rigid);
        checkFlueSteady.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                TextView userCommentsTextView = (TextView) findViewById(R.id.comments_for_user);
                String userComments = userCommentsTextView.getText().toString();
                if(isChecked){
                    userCommentsTextView.setText(addCommentsForUser(AlertUtils.ENABLE_FLUE_ACCESS, userComments));
                } else {
                    userCommentsTextView.setText(removeCommentsForUser(AlertUtils.ENABLE_FLUE_ACCESS, userComments));
                }
            }
        });

        //co2 autocomplete
        TextView co2TextView = (TextView)findViewById(R.id.co2);
        co2TextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                String co2 = s.toString();
                if(!co2.isEmpty()){
                    int number = Integer.parseInt(co2);
                    TextView userCommentsTextView = (TextView) findViewById(R.id.comments_for_user);
                    String userComments = userCommentsTextView.getText().toString();
                    if(number>=300 && number<500){
                        userCommentsTextView.setText(addCommentsForUser(AlertUtils.BAKE_SERVICE, userComments));
                    } else {
                        userCommentsTextView.setText(removeCommentsForUser(AlertUtils.BAKE_SERVICE, userComments));
                    }
                }
            }
        });

        co2TextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                String co2 = s.toString();
                if(!co2.isEmpty()){
                    int number = Integer.parseInt(co2);
                    TextView userCommentsTextView = (TextView) findViewById(R.id.comments_for_user);
                    String userComments = userCommentsTextView.getText().toString();
                    if (number>=500){
                        userCommentsTextView.setText(addCommentsForUser(AlertUtils.BAKE_REPAIR, userComments));
                    } else {
                        userCommentsTextView.setText(removeCommentsForUser(AlertUtils.BAKE_REPAIR, userComments));
                    }
                }
            }
        });

        //get edit info
        if(intent.hasExtra(Utils.EDIT_FLAG)){
            boolean editFlag = intent.getBooleanExtra(Utils.EDIT_FLAG, false);
            if(editFlag){
                int addressId = intent.getIntExtra(Utils.ADDRESS_ID, -1);
                int protocolId = intent.getIntExtra(Utils.PROTOCOL_ID, -1);
                ProtocolPaderewskiego protocolEdited = protocolPaderewskiegoDataSource.getPaderewskiegoProtocolsById(protocolId);

                //fill form from saved protocol
                kitchenAvailableSwitchCompat.setChecked(protocolEdited.is_kitchen_enabled());
                if(protocolEdited.is_kitchen_enabled()){
                    if(Double.compare(protocolEdited.get_kitchen_grid_dimension_x(), new Double("0.0"))!=0){
                        TextView kitchenGridXTextView = (TextView) findViewById(R.id.kitchen_grid_dimension_1);
                        kitchenGridXTextView.setText(Double.toString(protocolEdited.get_kitchen_grid_dimension_x()));
                    }
                    if(Double.compare(protocolEdited.get_kitchen_grid_dimension_y(), new Double("0.0"))!=0){
                        TextView kitchenGridYTextView = (TextView) findViewById(R.id.kitchen_grid_dimension_2);
                        kitchenGridYTextView.setText(Double.toString(protocolEdited.get_kitchen_grid_dimension_y()));
                    }
                    if(Double.compare(protocolEdited.get_kitchen_grid_dimension_round(), new Double("0.0"))!=0){
                        TextView kitchenGridRoundTextView = (TextView) findViewById(R.id.kitchen_grid_dimension_round);
                        kitchenGridRoundTextView.setText(Double.toString(protocolEdited.get_kitchen_grid_dimension_round()));
                    }
                    TextView kitchenAirflowClosedTextView = (TextView) findViewById(R.id.kitchen_airflow_windows_closed);
                    kitchenAirflowClosedTextView.setText(Double.toString(protocolEdited.get_kitchen_airflow_windows_closed()));
                    TextView kitchenAirflowMicroTextView = (TextView) findViewById(R.id.kitchen_airflow_microventilation);
                    kitchenAirflowMicroTextView.setText(Double.toString(protocolEdited.get_kitchen_airflow_microventilation()));
                    TextView kitchenAirflowOpenTextView = (TextView) findViewById(R.id.kitchen_airflow_open);
                    kitchenAirflowOpenTextView.setText(Double.toString(protocolEdited.get_kitchen_airflow_windows_open()));
                }
                CheckBox checkKitchenCleaned = (CheckBox) findViewById(R.id.kitchen_checkbox_cleaned);
                checkKitchenCleaned.setChecked(protocolEdited.is_kitchen_cleaned());
                checkKitchenHood.setChecked(protocolEdited.is_kitchen_hood());
                checkKitchenVent.setChecked(protocolEdited.is_kitchen_vent());
                checkKitchenInaccessible.setChecked(protocolEdited.is_kitchen_inaccessible());
                checkKitchenSteady.setChecked(protocolEdited.is_kitchen_steady());
                CheckBox checkKitchenNotCleaned = (CheckBox) findViewById(R.id.kitchen_checkbox_not_cleaned);
                checkKitchenNotCleaned.setChecked(protocolEdited.is_kitchen_not_cleaned());
                CheckBox checkKitchenOthers = (CheckBox) findViewById(R.id.kitchen_checkbox_others);
                checkKitchenOthers.setChecked(protocolEdited.is_kitchen_others());
                TextView kitchenOthersTextView = (TextView) findViewById(R.id.kitchen_others);
                kitchenOthersTextView.setText(protocolEdited.get_kitchen_others_comments());


                toiletAvailableSwitchCompat.setChecked(protocolEdited.is_toilet_enabled());
                if(protocolEdited.is_toilet_enabled()){
                    if(Double.compare(protocolEdited.get_toilet_grid_dimension_x(), new Double("0.0"))!=0){
                        TextView toiletGridXTextView = (TextView) findViewById(R.id.toilet_grid_dimension_1);
                        toiletGridXTextView.setText(Double.toString(protocolEdited.get_toilet_grid_dimension_x()));
                    }
                    if(Double.compare(protocolEdited.get_toilet_grid_dimension_y(), new Double("0.0"))!=0){
                        TextView toiletGridYTextView = (TextView) findViewById(R.id.toilet_grid_dimension_2);
                        toiletGridYTextView.setText(Double.toString(protocolEdited.get_toilet_grid_dimension_y()));
                    }
                    if(Double.compare(protocolEdited.get_toilet_grid_dimension_round(), new Double("0.0"))!=0){
                        TextView toiletGridRoundTextView = (TextView) findViewById(R.id.toilet_grid_dimension_round);
                        toiletGridRoundTextView.setText(Double.toString(protocolEdited.get_toilet_grid_dimension_round()));
                    }
                    TextView toiletAirflowClosedTextView = (TextView) findViewById(R.id.toilet_airflow_windows_closed);
                    toiletAirflowClosedTextView.setText(Double.toString(protocolEdited.get_toilet_airflow_windows_closed()));
                    TextView toiletAirflowMicroTextView = (TextView) findViewById(R.id.toilet_airflow_microventilation);
                    toiletAirflowMicroTextView.setText(Double.toString(protocolEdited.get_toilet_airflow_microventilation()));
                    TextView toiletAirflowOpenTextView = (TextView) findViewById(R.id.toilet_airflow_open);
                    toiletAirflowOpenTextView.setText(Double.toString(protocolEdited.get_toilet_airflow_windows_open()));
                }
                CheckBox checkToiletCleaned = (CheckBox) findViewById(R.id.toilet_checkbox_cleaned);
                checkToiletCleaned.setChecked(protocolEdited.is_toilet_cleaned());
                checkToiletVent.setChecked(protocolEdited.is_toilet_vent());
                checkToiletInaccessible.setChecked(protocolEdited.is_toilet_inaccessible());
                checkToiletSteady.setChecked(protocolEdited.is_toilet_steady());
                CheckBox checkToiletNotCleaned = (CheckBox) findViewById(R.id.toilet_checkbox_not_cleaned);
                checkToiletNotCleaned.setChecked(protocolEdited.is_toilet_not_cleaned());
                CheckBox checkToiletOthers = (CheckBox) findViewById(R.id.toilet_checkbox_others);
                checkToiletOthers.setChecked(protocolEdited.is_toilet_others());
                TextView toiletOthersTextView = (TextView) findViewById(R.id.toilet_others);
                toiletOthersTextView.setText(protocolEdited.get_toilet_others_comments());

                bathAvailableSwitchCompat.setChecked(protocolEdited.is_bath_enabled());
                if(protocolEdited.is_bath_enabled()){
                    if(Double.compare(protocolEdited.get_bath_grid_dimension_x(), new Double("0.0"))!=0){
                        TextView bathGridXTextView = (TextView) findViewById(R.id.bath_grid_dimension_1);
                        bathGridXTextView.setText(Double.toString(protocolEdited.get_bath_grid_dimension_x()));
                    }
                    if(Double.compare(protocolEdited.get_bath_grid_dimension_y(), new Double("0.0"))!=0){
                        TextView bathGridYTextView = (TextView) findViewById(R.id.bath_grid_dimension_2);
                        bathGridYTextView.setText(Double.toString(protocolEdited.get_bath_grid_dimension_y()));
                    }
                    if(Double.compare(protocolEdited.get_bath_grid_dimension_round(), new Double("0.0"))!=0){
                        TextView bathGridRoundTextView = (TextView) findViewById(R.id.bath_grid_dimension_round);
                        bathGridRoundTextView.setText(Double.toString(protocolEdited.get_bath_grid_dimension_round()));
                    }
                    TextView bathAirflowClosedTextView = (TextView) findViewById(R.id.bath_airflow_windows_closed);
                    bathAirflowClosedTextView.setText(Double.toString(protocolEdited.get_bath_airflow_windows_closed()));
                    TextView bathAirflowMicroTextView = (TextView) findViewById(R.id.bath_airflow_microventilation);
                    bathAirflowMicroTextView.setText(Double.toString(protocolEdited.get_bath_airflow_microventilation()));
                    TextView bathAirflowOpenTextView = (TextView) findViewById(R.id.bath_airflow_open);
                    bathAirflowOpenTextView.setText(Double.toString(protocolEdited.get_bath_airflow_windows_open()));
                }
                CheckBox checkBathCleaned = (CheckBox) findViewById(R.id.bath_checkbox_cleaned);
                checkBathCleaned.setChecked(protocolEdited.is_bath_cleaned());
                checkBathVent.setChecked(protocolEdited.is_bath_vent());
                checkBathInaccessible.setChecked(protocolEdited.is_bath_inaccessible());
                checkBathSteady.setChecked(protocolEdited.is_bath_steady());
                CheckBox checkBathNotCleaned = (CheckBox) findViewById(R.id.bath_checkbox_not_cleaned);
                checkBathNotCleaned.setChecked(protocolEdited.is_bath_not_cleaned());
                CheckBox checkBathOthers = (CheckBox) findViewById(R.id.bath_checkbox_others);
                checkBathOthers.setChecked(protocolEdited.is_bath_others());
                TextView bathOthersTextView = (TextView) findViewById(R.id.bath_others);
                bathOthersTextView.setText(protocolEdited.get_bath_others_comments());

                flueAvailableSwitchCompat.setChecked(protocolEdited.is_flue_enabled());
                if(protocolEdited.is_flue_enabled()){
                    TextView flueAirflowClosedTextView = (TextView) findViewById(R.id.flue_airflow_windows_closed);
                    flueAirflowClosedTextView.setText(Double.toString(protocolEdited.get_flue_airflow_windows_closed()));
                    TextView flueAirflowMicroTextView = (TextView) findViewById(R.id.flue_airflow_microventilation);
                    flueAirflowMicroTextView.setText(Double.toString(protocolEdited.get_flue_airflow_microventilation()));
                    TextView flueAirflowOpenTextView = (TextView) findViewById(R.id.flue_airflow_open);
                    flueAirflowOpenTextView.setText(Double.toString(protocolEdited.get_flue_airflow_windows_open()));
                }
                CheckBox checkFlueCleaned = (CheckBox) findViewById(R.id.flue_checkbox_cleaned);
                checkFlueCleaned.setChecked(protocolEdited.is_flue_cleaned());
                CheckBox checkflueBoiler = (CheckBox) findViewById(R.id.flue_checkbox_boiler);
                checkflueBoiler.setChecked(protocolEdited.is_flue_boiler());
                checkFlueInaccessible.setChecked(protocolEdited.is_flue_inaccessible());
                CheckBox checkflueRigid = (CheckBox) findViewById(R.id.flue_checkbox_rigid);
                checkflueRigid.setChecked(protocolEdited.is_flue_rigid());
                CheckBox checkflueAlufol = (CheckBox) findViewById(R.id.flue_checkbox_alufol);
                checkflueAlufol.setChecked(protocolEdited.is_flue_alufol());
                CheckBox checkFlueNotCleaned = (CheckBox) findViewById(R.id.flue_checkbox_not_cleaned);
                checkFlueNotCleaned.setChecked(protocolEdited.is_flue_not_cleaned());
                CheckBox checkFlueOthers = (CheckBox) findViewById(R.id.flue_checkbox_others);
                checkFlueOthers.setChecked(protocolEdited.is_flue_others());
                TextView flueOthersTextView = (TextView) findViewById(R.id.flue_others);
                flueOthersTextView.setText(protocolEdited.get_flue_others_comments());

                co2TextView.setText(Integer.toString((int)protocolEdited.get_co2()));
                TextView eqCommentsTextView = (TextView)findViewById(R.id.equipment_comments);
                eqCommentsTextView.setText(protocolEdited.get_comments());


                CheckBox gasMeterCheck = (CheckBox) findViewById(R.id.eq_gas_meter);
                gasMeterCheck.setChecked(protocolEdited.is_eq_ch_gas_meter_working());
                if(protocolEdited.is_eq_ch_gas_meter_working()){
                    gasMeterSwitchCompat.setChecked(protocolEdited.is_eq_gas_meter_working());
                }

                CheckBox stoveCheck = (CheckBox) findViewById(R.id.eq_stove);
                stoveCheck.setChecked(protocolEdited.is_eq_ch_stove_working());
                if(protocolEdited.is_eq_ch_stove_working()){
                    stoveSwitchCompat.setChecked(protocolEdited.is_eq_stove_working());
                }

                CheckBox bakeCheck = (CheckBox) findViewById(R.id.eq_bake);
                bakeCheck.setChecked(protocolEdited.is_eq_ch_bake_working());
                if(protocolEdited.is_eq_ch_bake_working()){
                    bakeSwitchCompat.setChecked(protocolEdited.is_eq_bake_working());
                }

                CheckBox combiOvenCheck = (CheckBox) findViewById(R.id.eq_combi_oven);
                combiOvenCheck.setChecked(protocolEdited.is_eq_ch_combi_oven_working());
                if(protocolEdited.is_eq_ch_combi_oven_working()){
                    combiOvenSwitchCompat.setChecked(protocolEdited.is_eq_combi_oven_working());
                }

                CheckBox kitchenTermCheck = (CheckBox) findViewById(R.id.eq_kitchen_term);
                kitchenTermCheck.setChecked(protocolEdited.is_eq_ch_kitchen_term_working());
                if(protocolEdited.is_eq_ch_kitchen_term_working()){
                    kitchenTermSwitchCompat.setChecked(protocolEdited.is_eq_kitchen_term_working());
                }

                TextView telephoneTextView = (TextView) findViewById(R.id.telephone);
                telephoneTextView.setText(protocolEdited.get_telephone());

                CheckBox othersCheck = (CheckBox) findViewById(R.id.eq_others);
                othersCheck.setChecked(protocolEdited.is_eq_ch_others());
                if(protocolEdited.is_eq_ch_others()){
                    TextView eqOthersTextView = (TextView)findViewById(R.id.eq_others_value);
                    eqOthersTextView.setText(protocolEdited.get_eq_other());
                }

                userCommentsTextView.setText(protocolEdited.get_comments_for_user());

            }
        }

    }

    @Override
    protected void onResume() {
        addressDataSource.open();
        protocolPaderewskiegoDataSource.open();
        awaitingProtocolDataSource.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        addressDataSource.close();
        protocolPaderewskiegoDataSource.close();
        awaitingProtocolDataSource.close();
        super.onPause();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.select_printer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        Intent intent = new Intent(this, PrinterSettingActivity.class);
        startActivity(intent);
        return true;
    }

    public void getSignature(View view) {
        Intent intent = new Intent(this, CaptureSignatureActivity.class);
        startActivity(intent);
    }

    public void printData(View view) {

        Intent intent = getIntent();
        ProtocolPaderewskiego protocol = new ProtocolPaderewskiego();

        //get data from intent
        if(intent.hasExtra(Utils.WORKER_NAME)){
            protocol.set_worker_name(intent.getStringExtra(Utils.WORKER_NAME));
        }
        if(intent.hasExtra(Utils.TEMP_INSIDE)){
            if(!intent.getStringExtra(Utils.TEMP_INSIDE).equals(""))
                protocol.set_temp_inside(Double.parseDouble(intent.getStringExtra(Utils.TEMP_INSIDE)));
        }
        if(intent.hasExtra(Utils.WORKER_NAME)){
            if(!intent.getStringExtra(Utils.TEMP_OUTSIDE).equals(""))
                protocol.set_temp_outside(Double.parseDouble(intent.getStringExtra(Utils.TEMP_OUTSIDE)));
        }
        if(intent.hasExtra(Utils.WIND_SPEED)){
            if(!intent.getStringExtra(Utils.WIND_SPEED).equals(""))
                protocol.set_wind_speed(Double.parseDouble(intent.getStringExtra(Utils.WIND_SPEED)));
        }
        if(intent.hasExtra(Utils.WIND_DIRECTION)){
            protocol.set_wind_direction(intent.getStringExtra(Utils.WIND_DIRECTION));
        }
        if(intent.hasExtra(Utils.PRESSURE)){
            if(!intent.getStringExtra(Utils.PRESSURE).equals(""))
                protocol.set_pressure(Double.parseDouble(intent.getStringExtra(Utils.PRESSURE)));
        }
        if(intent.hasExtra(Utils.WINDOWS_ALL)){
            if(!intent.getStringExtra(Utils.WINDOWS_ALL).equals(""))
                protocol.set_windows_all(Integer.parseInt(intent.getStringExtra(Utils.WINDOWS_ALL)));
        }
        if(intent.hasExtra(Utils.WINDOWS_MICRO)){
            if(!intent.getStringExtra(Utils.WINDOWS_MICRO).equals(""))
                protocol.set_windows_micro(Integer.parseInt(intent.getStringExtra(Utils.WINDOWS_MICRO)));
        }
        if(intent.hasExtra(Utils.WINDOWS_VENT)){
            if(!intent.getStringExtra(Utils.WINDOWS_VENT).equals(""))
                protocol.set_windows_vent(Integer.parseInt(intent.getStringExtra(Utils.WINDOWS_VENT)));
        }
        if(intent.hasExtra(Utils.WINDOWS_NO_MICRO)){
            if(!intent.getStringExtra(Utils.WINDOWS_NO_MICRO).equals(""))
                protocol.set_windows_no_micro(Integer.parseInt(intent.getStringExtra(Utils.WINDOWS_NO_MICRO)));
        }

        //get data from form
        SwitchCompat kitchenAvailableSwitchCompat = (SwitchCompat) findViewById(R.id.kitchen_availability);
        boolean kitchenChecked = kitchenAvailableSwitchCompat.isChecked();
        TextView kitchenGridXTextView = (TextView) findViewById(R.id.kitchen_grid_dimension_1);
        String kitchenGridX = kitchenGridXTextView.getText().toString();
        TextView kitchenGridYTextView = (TextView) findViewById(R.id.kitchen_grid_dimension_2);
        String kitchenGridY = kitchenGridYTextView.getText().toString();
        TextView kitchenGridRoundTextView = (TextView) findViewById(R.id.kitchen_grid_dimension_round);
        String kitchenGridRound = kitchenGridRoundTextView.getText().toString();
        TextView kitchenAirflowClosedTextView = (TextView) findViewById(R.id.kitchen_airflow_windows_closed);
        String kitchenAirflowClosed = kitchenAirflowClosedTextView.getText().toString();
        TextView kitchenAirflowMicroTextView = (TextView) findViewById(R.id.kitchen_airflow_microventilation);
        String kitchenAirflowMicro = kitchenAirflowMicroTextView.getText().toString();
        TextView kitchenAirflowOpenTextView = (TextView) findViewById(R.id.kitchen_airflow_open);
        String kitchenAirflowOpen = kitchenAirflowOpenTextView.getText().toString();

        CheckBox checkKitchenCleaned = (CheckBox) findViewById(R.id.kitchen_checkbox_cleaned);
        boolean kitchenCleaned= checkKitchenCleaned.isChecked();
        CheckBox checkKitchenHood = (CheckBox) findViewById(R.id.kitchen_checkbox_hood);
        boolean kitchenHood = checkKitchenHood.isChecked();
        CheckBox checkKitchenVent = (CheckBox) findViewById(R.id.kitchen_checkbox_vent);
        boolean kitchenVent = checkKitchenVent.isChecked();
        CheckBox checkKitchenInaccessible = (CheckBox) findViewById(R.id.kitchen_checkbox_inaccessible);
        boolean kitchenInaccessible = checkKitchenInaccessible.isChecked();
        CheckBox checkKitchenSteady = (CheckBox) findViewById(R.id.kitchen_checkbox_steady);
        boolean kitchenSteady = checkKitchenSteady.isChecked();
        CheckBox checkKitchenNotCleaned = (CheckBox) findViewById(R.id.kitchen_checkbox_not_cleaned);
        boolean kitchenNotCleaned= checkKitchenNotCleaned.isChecked();
        CheckBox checkKitchenOthers = (CheckBox) findViewById(R.id.kitchen_checkbox_others);
        boolean kitchenOthers= checkKitchenOthers.isChecked();
        TextView kitchenOthersTextView = (TextView) findViewById(R.id.kitchen_others);
        String kitchenOthersComment= kitchenOthersTextView.getText().toString();

        SwitchCompat toiletAvailableSwitchCompat = (SwitchCompat) findViewById(R.id.toilet_availability);
        boolean toiletChecked = toiletAvailableSwitchCompat.isChecked();
        TextView toiletGridXTextView = (TextView) findViewById(R.id.toilet_grid_dimension_1);
        String toiletGridX = toiletGridXTextView.getText().toString();
        TextView toiletGridYTextView = (TextView) findViewById(R.id.toilet_grid_dimension_2);
        String toiletGridY = toiletGridYTextView.getText().toString();
        TextView toiletGridRoundTextView = (TextView) findViewById(R.id.toilet_grid_dimension_round);
        String toiletGridRound = toiletGridRoundTextView.getText().toString();
        TextView toiletAirflowClosedTextView = (TextView) findViewById(R.id.toilet_airflow_windows_closed);
        String toiletAirflowClosed = toiletAirflowClosedTextView.getText().toString();
        TextView toiletAirflowMicroTextView = (TextView) findViewById(R.id.toilet_airflow_microventilation);
        String toiletAirflowMicro = toiletAirflowMicroTextView.getText().toString();
        TextView toiletAirflowOpenTextView = (TextView) findViewById(R.id.toilet_airflow_open);
        String toiletAirflowOpen = toiletAirflowOpenTextView.getText().toString();

        CheckBox checktoiletCleaned = (CheckBox) findViewById(R.id.toilet_checkbox_cleaned);
        boolean toiletCleaned= checktoiletCleaned.isChecked();
        CheckBox checktoiletVent = (CheckBox) findViewById(R.id.toilet_checkbox_vent);
        boolean toiletVent = checktoiletVent.isChecked();
        CheckBox checktoiletInaccessible = (CheckBox) findViewById(R.id.toilet_checkbox_inaccessible);
        boolean toiletInaccessible = checktoiletInaccessible.isChecked();
        CheckBox checktoiletSteady = (CheckBox) findViewById(R.id.toilet_checkbox_steady);
        boolean toiletSteady = checktoiletSteady.isChecked();
        CheckBox checktoiletNotCleaned = (CheckBox) findViewById(R.id.toilet_checkbox_not_cleaned);
        boolean toiletNotCleaned= checktoiletNotCleaned.isChecked();
        CheckBox checkToiletOthers = (CheckBox) findViewById(R.id.toilet_checkbox_others);
        boolean toiletOthers= checkToiletOthers.isChecked();
        TextView toiletOthersTextView = (TextView) findViewById(R.id.toilet_others);
        String toiletOthersComment= toiletOthersTextView.getText().toString();

        SwitchCompat bathAvailableSwitchCompat = (SwitchCompat) findViewById(R.id.bath_availability);
        boolean bathChecked = bathAvailableSwitchCompat.isChecked();
        TextView bathGridXTextView = (TextView) findViewById(R.id.bath_grid_dimension_1);
        String bathGridX = bathGridXTextView.getText().toString();
        TextView bathGridYTextView = (TextView) findViewById(R.id.bath_grid_dimension_2);
        String bathGridY = bathGridYTextView.getText().toString();
        TextView bathGridRoundTextView = (TextView) findViewById(R.id.bath_grid_dimension_round);
        String bathGridRound = bathGridRoundTextView.getText().toString();
        TextView bathAirflowClosedTextView = (TextView) findViewById(R.id.bath_airflow_windows_closed);
        String bathAirflowClosed = bathAirflowClosedTextView.getText().toString();
        TextView bathAirflowMicroTextView = (TextView) findViewById(R.id.bath_airflow_microventilation);
        String bathAirflowMicro = bathAirflowMicroTextView.getText().toString();
        TextView bathAirflowOpenTextView = (TextView) findViewById(R.id.bath_airflow_open);
        String bathAirflowOpen = bathAirflowOpenTextView.getText().toString();

        CheckBox checkbathCleaned = (CheckBox) findViewById(R.id.bath_checkbox_cleaned);
        boolean bathCleaned= checkbathCleaned.isChecked();
        CheckBox checkbathVent = (CheckBox) findViewById(R.id.bath_checkbox_vent);
        boolean bathVent = checkbathVent.isChecked();
        CheckBox checkbathInaccessible = (CheckBox) findViewById(R.id.bath_checkbox_inaccessible);
        boolean bathInaccessible = checkbathInaccessible.isChecked();
        CheckBox checkbathSteady = (CheckBox) findViewById(R.id.bath_checkbox_steady);
        boolean bathSteady = checkbathSteady.isChecked();
        CheckBox checkbathNotCleaned = (CheckBox) findViewById(R.id.bath_checkbox_not_cleaned);
        boolean bathNotCleaned= checkbathNotCleaned.isChecked();
        CheckBox checkbathOthers = (CheckBox) findViewById(R.id.bath_checkbox_others);
        boolean bathOthers= checkbathOthers.isChecked();
        TextView bathOthersTextView = (TextView) findViewById(R.id.bath_others);
        String bathOthersComment= bathOthersTextView.getText().toString();

        SwitchCompat flueAvailableSwitchCompat = (SwitchCompat) findViewById(R.id.flue_availability);
        boolean flueChecked = flueAvailableSwitchCompat.isChecked();
        TextView flueAirflowClosedTextView = (TextView) findViewById(R.id.flue_airflow_windows_closed);
        String flueAirflowClosed = flueAirflowClosedTextView.getText().toString();
        TextView flueAirflowMicroTextView = (TextView) findViewById(R.id.flue_airflow_microventilation);
        String flueAirflowMicro = flueAirflowMicroTextView.getText().toString();
        TextView flueAirflowOpenTextView = (TextView) findViewById(R.id.flue_airflow_open);
        String flueAirflowOpen = flueAirflowOpenTextView.getText().toString();

        CheckBox checkflueCleaned = (CheckBox) findViewById(R.id.flue_checkbox_cleaned);
        boolean flueCleaned= checkflueCleaned.isChecked();
        CheckBox checkflueBoiler = (CheckBox) findViewById(R.id.flue_checkbox_boiler);
        boolean flueBoiler = checkflueBoiler.isChecked();
        CheckBox checkflueInaccessible = (CheckBox) findViewById(R.id.flue_checkbox_inaccessible);
        boolean flueInaccessible = checkflueInaccessible.isChecked();
        CheckBox checkflueRigid = (CheckBox) findViewById(R.id.flue_checkbox_rigid);
        boolean flueRigid = checkflueRigid.isChecked();
        CheckBox checkflueAlufol = (CheckBox) findViewById(R.id.flue_checkbox_alufol);
        boolean flueAlufol = checkflueAlufol.isChecked();
        CheckBox checkflueNotCleaned = (CheckBox) findViewById(R.id.flue_checkbox_not_cleaned);
        boolean flueNotCleaned= checkflueNotCleaned.isChecked();
        CheckBox checkFlueOthers = (CheckBox) findViewById(R.id.flue_checkbox_others);
        boolean flueOthers= checkFlueOthers.isChecked();
        TextView flueOthersTextView = (TextView) findViewById(R.id.flue_others);
        String flueOthersComments= flueOthersTextView.getText().toString();

        TextView co2TextView = (TextView)findViewById(R.id.co2);
        String co2 = co2TextView.getText().toString();

        TextView eqCommentsTextView = (TextView)findViewById(R.id.equipment_comments);
        String eqComments = eqCommentsTextView.getText().toString();

        CheckBox gasMeterCheck = (CheckBox) findViewById(R.id.eq_gas_meter);
        boolean eqGasMeterChecked = gasMeterCheck.isChecked();
        SwitchCompat gasMeterSwitchCompat = (SwitchCompat) findViewById(R.id.eq_gas_meter_value);
        boolean eqGasMeterWorking = gasMeterSwitchCompat.isChecked();

        CheckBox stoveCheck = (CheckBox) findViewById(R.id.eq_stove);
        boolean eqStoveChecked = stoveCheck.isChecked();
        SwitchCompat stoveSwitchCompat = (SwitchCompat) findViewById(R.id.eq_stove_value);
        boolean eqStoveWorking = stoveSwitchCompat.isChecked();

        CheckBox bakeCheck = (CheckBox) findViewById(R.id.eq_bake);
        boolean eqBakeChecked = bakeCheck.isChecked();
        SwitchCompat bakeSwitchCompat = (SwitchCompat) findViewById(R.id.eq_bake_value);
        boolean eqBakeworking = bakeSwitchCompat.isChecked();

        CheckBox combiOvenCheck = (CheckBox) findViewById(R.id.eq_combi_oven);
        boolean eqCombiOvenChecked = combiOvenCheck.isChecked();
        SwitchCompat combiOvenSwitchCompat = (SwitchCompat) findViewById(R.id.eq_combi_oven_value);
        boolean eqCombiOvenWorking = combiOvenSwitchCompat.isChecked();

        CheckBox kitchenTermCheck = (CheckBox) findViewById(R.id.eq_kitchen_term);
        boolean eqKitchenTermChecked = kitchenTermCheck.isChecked();
        SwitchCompat kitchenTermSwitchCompat = (SwitchCompat) findViewById(R.id.eq_kitchen_term_value);
        boolean eqkitchenTermWorking = kitchenTermSwitchCompat.isChecked();

        CheckBox othersCheck = (CheckBox) findViewById(R.id.eq_others);
        boolean eqOthersChecked = othersCheck.isChecked();
        TextView eqOthersTextView = (TextView)findViewById(R.id.eq_others_value);
        String eqOthersWorking = eqOthersTextView.getText().toString();

        TextView telephoneTextView = (TextView) findViewById(R.id.telephone);
        String telephone = telephoneTextView.getText().toString();

        TextView userCommentsTextView = (TextView) findViewById(R.id.comments_for_user);
        String userComments = userCommentsTextView.getText().toString();

        if(!co2.equals("")){
            double co2Num = Double.parseDouble(co2);
            if(300<co2Num && co2Num<500){
                userComments = addCommentsForUser(AlertUtils.BAKE_SERVICE, userComments);
            }
        }

        //validate required fields
        if(windows_micro!=0 || windows_no_micro!=windows_all){
            if(kitchenChecked){
                if(((kitchenGridX.isEmpty() || kitchenGridY.isEmpty()) && kitchenGridRound.isEmpty()) || kitchenAirflowClosed.isEmpty() || kitchenAirflowMicro.isEmpty() || kitchenAirflowOpen.isEmpty()){
                    displayValidationError();
                    return;
                }
            }
        } else {
            if(kitchenChecked){
                if(((kitchenGridX.isEmpty() || kitchenGridY.isEmpty()) && kitchenGridRound.isEmpty()) || kitchenAirflowClosed.isEmpty() || kitchenAirflowOpen.isEmpty()){
                    displayValidationError();
                    return;
                }
            }
        }
        if(kitchenOthers){
            if(kitchenOthersComment.isEmpty()){
                displayValidationError();
                return;
            }
        }
        if(windows_micro!=0 || windows_no_micro!=windows_all){
            if(toiletChecked){
                if(((toiletGridX.isEmpty() || toiletGridY.isEmpty()) && toiletGridRound.isEmpty()) || toiletAirflowClosed.isEmpty() || toiletAirflowMicro.isEmpty() || toiletAirflowOpen.isEmpty()){
                    displayValidationError();
                    return;
                }
            }
        } else {
            if(toiletChecked){
                if(((toiletGridX.isEmpty() || toiletGridY.isEmpty()) && toiletGridRound.isEmpty()) || toiletAirflowClosed.isEmpty() || toiletAirflowOpen.isEmpty()){
                    displayValidationError();
                    return;
                }
            }
        }
        if(windows_micro!=0 || windows_no_micro!=windows_all){
            if(toiletChecked){
                if(((toiletGridX.isEmpty() || toiletGridY.isEmpty()) && toiletGridRound.isEmpty()) || toiletAirflowClosed.isEmpty() || toiletAirflowMicro.isEmpty() || toiletAirflowOpen.isEmpty()){
                    displayValidationError();
                    return;
                }
            }
        } else {
            if(toiletChecked){
                if(((toiletGridX.isEmpty() || toiletGridY.isEmpty()) && toiletGridRound.isEmpty()) || toiletAirflowClosed.isEmpty() || toiletAirflowOpen.isEmpty()){
                    displayValidationError();
                    return;
                }
            }
        }
        if(toiletOthers){
            if(toiletOthersComment.isEmpty()){
                displayValidationError();
                return;
            }
        }
        if(windows_micro!=0 || windows_no_micro!=windows_all){
            if(bathChecked){
                if(((bathGridX.isEmpty() || bathGridY.isEmpty()) && bathGridRound.isEmpty()) || bathAirflowClosed.isEmpty() || bathAirflowMicro.isEmpty() || bathAirflowOpen.isEmpty()){
                    displayValidationError();
                    return;
                }
            }
        } else {
            if(bathChecked){
                if(((bathGridX.isEmpty() || bathGridY.isEmpty()) && bathGridRound.isEmpty()) || bathAirflowClosed.isEmpty() || bathAirflowOpen.isEmpty()){
                    displayValidationError();
                    return;
                }
            }
        }
        if(bathOthers){
            if(bathOthersComment.isEmpty()){
                displayValidationError();
                return;
            }
        }
        if(windows_micro!=0 || windows_no_micro!=windows_all){
            if(flueChecked){
                if(flueAirflowClosed.isEmpty() || flueAirflowMicro.isEmpty() || flueAirflowOpen.isEmpty()){
                    displayValidationError();
                    return;
                }
            }
        } else {
            if(flueChecked){
                if(flueAirflowClosed.isEmpty() || flueAirflowOpen.isEmpty()){
                    displayValidationError();
                    return;
                }
            }
        }
        if(flueOthers){
            if(flueOthersComments.isEmpty()){
                displayValidationError();
                return;
            }
        }

        //get address
        if(intent.hasExtra(Utils.ADDRESS_ID)){
            int addressId = intent.getIntExtra(Utils.ADDRESS_ID, -1);
            address = addressDataSource.getAddressById(addressId);
        } else {
            Context context = getApplicationContext();
            CharSequence text = AlertUtils.SELECT_ADDRESSES_PROTOCOLS_ADD;
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }

        //fill protocol object
        //kitchen
        protocol.set_kitchen_enabled(kitchenChecked);
        if(kitchenChecked){
            if(kitchenGridRound.equals("")){
                protocol.set_kitchen_grid_dimension_x(Double.parseDouble(kitchenGridX));
                protocol.set_kitchen_grid_dimension_y(Double.parseDouble(kitchenGridY));
            } else {
                protocol.set_kitchen_grid_dimension_round(Double.parseDouble(kitchenGridRound));
            }
            if(windows_micro!=0 || windows_no_micro!=windows_all){
                protocol.set_kitchen_airflow_microventilation(Double.parseDouble(kitchenAirflowMicro));
            }
            protocol.set_kitchen_airflow_windows_closed(Double.parseDouble(kitchenAirflowClosed));
            protocol.set_kitchen_airflow_windows_open(Double.parseDouble(kitchenAirflowOpen));
        }
        protocol.set_kitchen_cleaned(kitchenCleaned);
        protocol.set_kitchen_hood(kitchenHood);
        protocol.set_kitchen_vent(kitchenVent);
        protocol.set_kitchen_inaccessible(kitchenInaccessible);
        protocol.set_kitchen_steady(kitchenSteady);
        protocol.set_kitchen_not_cleaned(kitchenNotCleaned);
        protocol.set_kitchen_others(kitchenOthers);
        if(kitchenOthers)
            protocol.set_kitchen_others_comments(kitchenOthersComment);

        //toilet
        protocol.set_toilet_enabled(toiletChecked);
        if(toiletChecked){
            if(toiletGridRound.equals("")){
                protocol.set_toilet_grid_dimension_x(Double.parseDouble(toiletGridX));
                protocol.set_toilet_grid_dimension_y(Double.parseDouble(toiletGridY));
            } else {
                protocol.set_toilet_grid_dimension_round(Double.parseDouble(toiletGridRound));
            }
            if(windows_micro!=0 || windows_no_micro!=windows_all){
                protocol.set_toilet_airflow_microventilation(Double.parseDouble(toiletAirflowMicro));
            }
            protocol.set_toilet_airflow_windows_closed(Double.parseDouble(toiletAirflowClosed));
            protocol.set_toilet_airflow_windows_open(Double.parseDouble(toiletAirflowOpen));
        }
        protocol.set_toilet_cleaned(toiletCleaned);
        protocol.set_toilet_vent(toiletVent);
        protocol.set_toilet_inaccessible(toiletInaccessible);
        protocol.set_toilet_steady(toiletSteady);
        protocol.set_toilet_not_cleaned(toiletNotCleaned);
        protocol.set_toilet_others(toiletOthers);
        if(toiletOthers)
            protocol.set_toilet_others_comments(toiletOthersComment);

        //bathroom
        protocol.set_bath_enabled(bathChecked);
        if(bathChecked){
            if(bathGridRound.equals("")){
                protocol.set_bath_grid_dimension_x(Double.parseDouble(bathGridX));
                protocol.set_bath_grid_dimension_y(Double.parseDouble(bathGridY));
            } else {
                protocol.set_bath_grid_dimension_round(Double.parseDouble(bathGridRound));
            }
            if(windows_micro!=0 || windows_no_micro!=windows_all){
                protocol.set_bath_airflow_microventilation(Double.parseDouble(bathAirflowMicro));
            }
            protocol.set_bath_airflow_windows_closed(Double.parseDouble(bathAirflowClosed));
            protocol.set_bath_airflow_windows_open(Double.parseDouble(bathAirflowOpen));
        }
        protocol.set_bath_cleaned(bathCleaned);
        protocol.set_bath_vent(bathVent);
        protocol.set_bath_inaccessible(bathInaccessible);
        protocol.set_bath_steady(bathSteady);
        protocol.set_bath_not_cleaned(bathNotCleaned);
        protocol.set_bath_others(bathOthers);
        if(bathOthers)
            protocol.set_bath_others_comments(bathOthersComment);

        //flue
        protocol.set_flue_enabled(flueChecked);
        if(flueChecked){
            if(windows_micro!=0 || windows_no_micro!=windows_all){
                if(!flueAirflowMicro.equals(""))
                    protocol.set_flue_airflow_microventilation(Double.parseDouble(flueAirflowMicro));
            }
            if(!flueAirflowClosed.equals(""))
                protocol.set_flue_airflow_windows_closed(Double.parseDouble(flueAirflowClosed));
            if(!flueAirflowOpen.equals(""))
                protocol.set_flue_airflow_windows_open(Double.parseDouble(flueAirflowOpen));
        }
        protocol.set_flue_cleaned(flueCleaned);
        protocol.set_flue_boiler(flueBoiler);
        protocol.set_flue_inaccessible(flueInaccessible);
        protocol.set_flue_rigid(flueRigid);
        protocol.set_flue_alufol(flueAlufol);
        protocol.set_flue_not_cleaned(flueNotCleaned);
        protocol.set_flue_others(flueOthers);
        if(flueOthers)
            protocol.set_flue_others_comments(flueOthersComments);

        //equipment&others
        if(!co2.equals("")){
            protocol.set_co2(Double.parseDouble(co2));
        }
        protocol.set_comments(eqComments);

        protocol.set_eq_ch_gas_meter_working(eqGasMeterChecked);
        if(eqGasMeterChecked)
            protocol.set_eq_gas_meter_working(eqGasMeterWorking);

        protocol.set_eq_ch_stove_working(eqStoveChecked);
        if(eqStoveChecked)
            protocol.set_eq_stove_working(eqStoveWorking);

        protocol.set_eq_ch_bake_working(eqBakeChecked);
        if(eqBakeChecked)
            protocol.set_eq_bake_working(eqBakeworking);

        protocol.set_eq_ch_combi_oven_working(eqCombiOvenChecked);
        if(eqCombiOvenChecked)
            protocol.set_eq_combi_oven_working(eqCombiOvenWorking);

        protocol.set_eq_ch_kitchen_term_working(eqKitchenTermChecked);
        if(eqKitchenTermChecked)
            protocol.set_eq_kitchen_term_working(eqkitchenTermWorking);

        protocol.set_telephone(telephone);

        protocol.set_comments_for_user(userComments);

        protocol.set_eq_ch_others(eqOthersChecked);
        if(eqOthersChecked)
            protocol.set_eq_other(eqOthersWorking);

        PROTOCOL = protocol;

        try {
        BluetoothDiscoverer.findPrinters(getApplicationContext(), new DiscoveryHandler() {
            @Override
            public void foundPrinter(DiscoveredPrinter discoveredPrinter) {
            }

            @Override
            public void discoveryFinished() {
                SharedPreferences settings = getSharedPreferences(Utils.PREFS_NAME, 0);
                BluetoothConnectionPaderewskiego bluetoothConnection = new BluetoothConnectionPaderewskiego();
                bluetoothConnection.sendCpclOverBluetooth(settings.getString(Utils.PRINTER_MAC, ""), address, PROTOCOL);
            }

            @Override
            public void discoveryError(String s) {
                Context context = getApplicationContext();
                CharSequence text = "błąd: " + s;
                int duration = Toast.LENGTH_LONG;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });
        } catch(Exception e){
            Context context = getApplicationContext();
            CharSequence text = AlertUtils.PRINTER_CONNECTION_ERROR;
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }

    }

    public void saveData(View view) {

        Intent intent = getIntent();
        ProtocolPaderewskiego protocol = new ProtocolPaderewskiego();

        //get data from intent
        if(intent.hasExtra(Utils.WORKER_NAME)){
            protocol.set_worker_name(intent.getStringExtra(Utils.WORKER_NAME));
        }
        if(intent.hasExtra(Utils.TEMP_INSIDE)){
            if(!intent.getStringExtra(Utils.TEMP_INSIDE).equals(""))
                protocol.set_temp_inside(Double.parseDouble(intent.getStringExtra(Utils.TEMP_INSIDE)));
        }
        if(intent.hasExtra(Utils.WORKER_NAME)){
            if(!intent.getStringExtra(Utils.TEMP_OUTSIDE).equals(""))
                protocol.set_temp_outside(Double.parseDouble(intent.getStringExtra(Utils.TEMP_OUTSIDE)));
        }
        if(intent.hasExtra(Utils.WIND_SPEED)){
            if(!intent.getStringExtra(Utils.WIND_SPEED).equals(""))
                protocol.set_wind_speed(Double.parseDouble(intent.getStringExtra(Utils.WIND_SPEED)));
        }
        if(intent.hasExtra(Utils.WIND_DIRECTION)){
            protocol.set_wind_direction(intent.getStringExtra(Utils.WIND_DIRECTION));
        }
        if(intent.hasExtra(Utils.PRESSURE)){
            if(!intent.getStringExtra(Utils.PRESSURE).equals(""))
                protocol.set_pressure(Double.parseDouble(intent.getStringExtra(Utils.PRESSURE)));
        }
        if(intent.hasExtra(Utils.WINDOWS_ALL)){
            if(!intent.getStringExtra(Utils.WINDOWS_ALL).equals(""))
                protocol.set_windows_all(Integer.parseInt(intent.getStringExtra(Utils.WINDOWS_ALL)));
        }
        if(intent.hasExtra(Utils.WINDOWS_MICRO)){
            if(!intent.getStringExtra(Utils.WINDOWS_MICRO).equals(""))
                protocol.set_windows_micro(Integer.parseInt(intent.getStringExtra(Utils.WINDOWS_MICRO)));
        }
        if(intent.hasExtra(Utils.WINDOWS_VENT)){
            if(!intent.getStringExtra(Utils.WINDOWS_VENT).equals(""))
                protocol.set_windows_vent(Integer.parseInt(intent.getStringExtra(Utils.WINDOWS_VENT)));
        }
        if(intent.hasExtra(Utils.WINDOWS_NO_MICRO)){
            if(!intent.getStringExtra(Utils.WINDOWS_NO_MICRO).equals(""))
                protocol.set_windows_no_micro(Integer.parseInt(intent.getStringExtra(Utils.WINDOWS_NO_MICRO)));
        }

        //get data from form
        SwitchCompat kitchenAvailableSwitchCompat = (SwitchCompat) findViewById(R.id.kitchen_availability);
        boolean kitchenChecked = kitchenAvailableSwitchCompat.isChecked();
        TextView kitchenGridXTextView = (TextView) findViewById(R.id.kitchen_grid_dimension_1);
        String kitchenGridX = kitchenGridXTextView.getText().toString();
        TextView kitchenGridYTextView = (TextView) findViewById(R.id.kitchen_grid_dimension_2);
        String kitchenGridY = kitchenGridYTextView.getText().toString();
        TextView kitchenGridRoundTextView = (TextView) findViewById(R.id.kitchen_grid_dimension_round);
        String kitchenGridRound = kitchenGridRoundTextView.getText().toString();
        TextView kitchenAirflowClosedTextView = (TextView) findViewById(R.id.kitchen_airflow_windows_closed);
        String kitchenAirflowClosed = kitchenAirflowClosedTextView.getText().toString();
        TextView kitchenAirflowMicroTextView = (TextView) findViewById(R.id.kitchen_airflow_microventilation);
        String kitchenAirflowMicro = kitchenAirflowMicroTextView.getText().toString();
        TextView kitchenAirflowOpenTextView = (TextView) findViewById(R.id.kitchen_airflow_open);
        String kitchenAirflowOpen = kitchenAirflowOpenTextView.getText().toString();

        CheckBox checkKitchenCleaned = (CheckBox) findViewById(R.id.kitchen_checkbox_cleaned);
        boolean kitchenCleaned= checkKitchenCleaned.isChecked();
        CheckBox checkKitchenHood = (CheckBox) findViewById(R.id.kitchen_checkbox_hood);
        boolean kitchenHood = checkKitchenHood.isChecked();
        CheckBox checkKitchenVent = (CheckBox) findViewById(R.id.kitchen_checkbox_vent);
        boolean kitchenVent = checkKitchenVent.isChecked();
        CheckBox checkKitchenInaccessible = (CheckBox) findViewById(R.id.kitchen_checkbox_inaccessible);
        boolean kitchenInaccessible = checkKitchenInaccessible.isChecked();
        CheckBox checkKitchenSteady = (CheckBox) findViewById(R.id.kitchen_checkbox_steady);
        boolean kitchenSteady = checkKitchenSteady.isChecked();
        CheckBox checkKitchenNotCleaned = (CheckBox) findViewById(R.id.kitchen_checkbox_not_cleaned);
        boolean kitchenNotCleaned= checkKitchenNotCleaned.isChecked();
        CheckBox checkKitchenOthers = (CheckBox) findViewById(R.id.kitchen_checkbox_others);
        boolean kitchenOthers= checkKitchenOthers.isChecked();
        TextView kitchenOthersTextView = (TextView) findViewById(R.id.kitchen_others);
        String kitchenOthersComment= kitchenOthersTextView.getText().toString();

        SwitchCompat toiletAvailableSwitchCompat = (SwitchCompat) findViewById(R.id.toilet_availability);
        boolean toiletChecked = toiletAvailableSwitchCompat.isChecked();
        TextView toiletGridXTextView = (TextView) findViewById(R.id.toilet_grid_dimension_1);
        String toiletGridX = toiletGridXTextView.getText().toString();
        TextView toiletGridYTextView = (TextView) findViewById(R.id.toilet_grid_dimension_2);
        String toiletGridY = toiletGridYTextView.getText().toString();
        TextView toiletGridRoundTextView = (TextView) findViewById(R.id.toilet_grid_dimension_round);
        String toiletGridRound = toiletGridRoundTextView.getText().toString();
        TextView toiletAirflowClosedTextView = (TextView) findViewById(R.id.toilet_airflow_windows_closed);
        String toiletAirflowClosed = toiletAirflowClosedTextView.getText().toString();
        TextView toiletAirflowMicroTextView = (TextView) findViewById(R.id.toilet_airflow_microventilation);
        String toiletAirflowMicro = toiletAirflowMicroTextView.getText().toString();
        TextView toiletAirflowOpenTextView = (TextView) findViewById(R.id.toilet_airflow_open);
        String toiletAirflowOpen = toiletAirflowOpenTextView.getText().toString();

        CheckBox checktoiletCleaned = (CheckBox) findViewById(R.id.toilet_checkbox_cleaned);
        boolean toiletCleaned= checktoiletCleaned.isChecked();
        CheckBox checktoiletVent = (CheckBox) findViewById(R.id.toilet_checkbox_vent);
        boolean toiletVent = checktoiletVent.isChecked();
        CheckBox checktoiletInaccessible = (CheckBox) findViewById(R.id.toilet_checkbox_inaccessible);
        boolean toiletInaccessible = checktoiletInaccessible.isChecked();
        CheckBox checktoiletSteady = (CheckBox) findViewById(R.id.toilet_checkbox_steady);
        boolean toiletSteady = checktoiletSteady.isChecked();
        CheckBox checktoiletNotCleaned = (CheckBox) findViewById(R.id.toilet_checkbox_not_cleaned);
        boolean toiletNotCleaned= checktoiletNotCleaned.isChecked();
        CheckBox checkToiletOthers = (CheckBox) findViewById(R.id.toilet_checkbox_others);
        boolean toiletOthers= checkToiletOthers.isChecked();
        TextView toiletOthersTextView = (TextView) findViewById(R.id.toilet_others);
        String toiletOthersComment= toiletOthersTextView.getText().toString();

        SwitchCompat bathAvailableSwitchCompat = (SwitchCompat) findViewById(R.id.bath_availability);
        boolean bathChecked = bathAvailableSwitchCompat.isChecked();
        TextView bathGridXTextView = (TextView) findViewById(R.id.bath_grid_dimension_1);
        String bathGridX = bathGridXTextView.getText().toString();
        TextView bathGridYTextView = (TextView) findViewById(R.id.bath_grid_dimension_2);
        String bathGridY = bathGridYTextView.getText().toString();
        TextView bathGridRoundTextView = (TextView) findViewById(R.id.bath_grid_dimension_round);
        String bathGridRound = bathGridRoundTextView.getText().toString();
        TextView bathAirflowClosedTextView = (TextView) findViewById(R.id.bath_airflow_windows_closed);
        String bathAirflowClosed = bathAirflowClosedTextView.getText().toString();
        TextView bathAirflowMicroTextView = (TextView) findViewById(R.id.bath_airflow_microventilation);
        String bathAirflowMicro = bathAirflowMicroTextView.getText().toString();
        TextView bathAirflowOpenTextView = (TextView) findViewById(R.id.bath_airflow_open);
        String bathAirflowOpen = bathAirflowOpenTextView.getText().toString();

        CheckBox checkbathCleaned = (CheckBox) findViewById(R.id.bath_checkbox_cleaned);
        boolean bathCleaned= checkbathCleaned.isChecked();
        CheckBox checkbathVent = (CheckBox) findViewById(R.id.bath_checkbox_vent);
        boolean bathVent = checkbathVent.isChecked();
        CheckBox checkbathInaccessible = (CheckBox) findViewById(R.id.bath_checkbox_inaccessible);
        boolean bathInaccessible = checkbathInaccessible.isChecked();
        CheckBox checkbathSteady = (CheckBox) findViewById(R.id.bath_checkbox_steady);
        boolean bathSteady = checkbathSteady.isChecked();
        CheckBox checkbathNotCleaned = (CheckBox) findViewById(R.id.bath_checkbox_not_cleaned);
        boolean bathNotCleaned= checkbathNotCleaned.isChecked();
        CheckBox checkbathOthers = (CheckBox) findViewById(R.id.bath_checkbox_others);
        boolean bathOthers= checkbathOthers.isChecked();
        TextView bathOthersTextView = (TextView) findViewById(R.id.bath_others);
        String bathOthersComment= bathOthersTextView.getText().toString();

        SwitchCompat flueAvailableSwitchCompat = (SwitchCompat) findViewById(R.id.flue_availability);
        boolean flueChecked = flueAvailableSwitchCompat.isChecked();
        TextView flueAirflowClosedTextView = (TextView) findViewById(R.id.flue_airflow_windows_closed);
        String flueAirflowClosed = flueAirflowClosedTextView.getText().toString();
        TextView flueAirflowMicroTextView = (TextView) findViewById(R.id.flue_airflow_microventilation);
        String flueAirflowMicro = flueAirflowMicroTextView.getText().toString();
        TextView flueAirflowOpenTextView = (TextView) findViewById(R.id.flue_airflow_open);
        String flueAirflowOpen = flueAirflowOpenTextView.getText().toString();

        CheckBox checkflueCleaned = (CheckBox) findViewById(R.id.flue_checkbox_cleaned);
        boolean flueCleaned= checkflueCleaned.isChecked();
        CheckBox checkflueBoiler = (CheckBox) findViewById(R.id.flue_checkbox_boiler);
        boolean flueBoiler = checkflueBoiler.isChecked();
        CheckBox checkflueInaccessible = (CheckBox) findViewById(R.id.flue_checkbox_inaccessible);
        boolean flueInaccessible = checkflueInaccessible.isChecked();
        CheckBox checkflueRigid = (CheckBox) findViewById(R.id.flue_checkbox_rigid);
        boolean flueRigid = checkflueRigid.isChecked();
        CheckBox checkflueAlufol = (CheckBox) findViewById(R.id.flue_checkbox_alufol);
        boolean flueAlufol = checkflueAlufol.isChecked();
        CheckBox checkflueNotCleaned = (CheckBox) findViewById(R.id.flue_checkbox_not_cleaned);
        boolean flueNotCleaned= checkflueNotCleaned.isChecked();
        CheckBox checkFlueOthers = (CheckBox) findViewById(R.id.flue_checkbox_others);
        boolean flueOthers= checkFlueOthers.isChecked();
        TextView flueOthersTextView = (TextView) findViewById(R.id.flue_others);
        String flueOthersComments= flueOthersTextView.getText().toString();

        TextView co2TextView = (TextView)findViewById(R.id.co2);
        String co2 = co2TextView.getText().toString();
        TextView eqCommentsTextView = (TextView)findViewById(R.id.equipment_comments);
        String eqComments = eqCommentsTextView.getText().toString();

        CheckBox gasMeterCheck = (CheckBox) findViewById(R.id.eq_gas_meter);
        boolean eqGasMeterChecked = gasMeterCheck.isChecked();
        SwitchCompat gasMeterSwitchCompat = (SwitchCompat) findViewById(R.id.eq_gas_meter_value);
        boolean eqGasMeterWorking = gasMeterSwitchCompat.isChecked();

        CheckBox stoveCheck = (CheckBox) findViewById(R.id.eq_stove);
        boolean eqStoveChecked = stoveCheck.isChecked();
        SwitchCompat stoveSwitchCompat = (SwitchCompat) findViewById(R.id.eq_stove_value);
        boolean eqStoveWorking = stoveSwitchCompat.isChecked();

        CheckBox bakeCheck = (CheckBox) findViewById(R.id.eq_bake);
        boolean eqBakeChecked = bakeCheck.isChecked();
        SwitchCompat bakeSwitchCompat = (SwitchCompat) findViewById(R.id.eq_bake_value);
        boolean eqBakeworking = bakeSwitchCompat.isChecked();

        CheckBox combiOvenCheck = (CheckBox) findViewById(R.id.eq_combi_oven);
        boolean eqCombiOvenChecked = combiOvenCheck.isChecked();
        SwitchCompat combiOvenSwitchCompat = (SwitchCompat) findViewById(R.id.eq_combi_oven_value);
        boolean eqCombiOvenWorking = combiOvenSwitchCompat.isChecked();

        CheckBox kitchenTermCheck = (CheckBox) findViewById(R.id.eq_kitchen_term);
        boolean eqKitchenTermChecked = kitchenTermCheck.isChecked();
        SwitchCompat kitchenTermSwitchCompat = (SwitchCompat) findViewById(R.id.eq_kitchen_term_value);
        boolean eqkitchenTermWorking = kitchenTermSwitchCompat.isChecked();

        TextView telephoneTextView = (TextView) findViewById(R.id.telephone);
        String telephone = telephoneTextView.getText().toString();

        CheckBox othersCheck = (CheckBox) findViewById(R.id.eq_others);
        boolean eqOthersChecked = othersCheck.isChecked();
        TextView eqOthersTextView = (TextView)findViewById(R.id.eq_others_value);
        String eqOthersWorking = eqOthersTextView.getText().toString();

        TextView userCommentsTextView = (TextView) findViewById(R.id.comments_for_user);
        String userComments = userCommentsTextView.getText().toString();

        if(!co2.equals("")){
            double co2Num = Double.parseDouble(co2);
            if(300<co2Num && co2Num<500){
                userComments = addCommentsForUser(AlertUtils.BAKE_SERVICE, userComments);
            }
        }

        //validate required fields
        if(windows_micro!=0 || windows_no_micro!=windows_all){
            if(kitchenChecked){
                if(((kitchenGridX.isEmpty() || kitchenGridY.isEmpty()) && kitchenGridRound.isEmpty()) || kitchenAirflowClosed.isEmpty() || kitchenAirflowMicro.isEmpty() || kitchenAirflowOpen.isEmpty()){
                    displayValidationError();
                    return;
                }
            }
        } else {
            if(kitchenChecked){
                if(((kitchenGridX.isEmpty() || kitchenGridY.isEmpty()) && kitchenGridRound.isEmpty()) || kitchenAirflowClosed.isEmpty() || kitchenAirflowOpen.isEmpty()){
                    displayValidationError();
                    return;
                }
            }
        }
        if(kitchenOthers){
            if(kitchenOthersComment.isEmpty()){
                displayValidationError();
                return;
            }
        }
        if(windows_micro!=0 || windows_no_micro!=windows_all){
            if(toiletChecked){
                if(((toiletGridX.isEmpty() || toiletGridY.isEmpty()) && toiletGridRound.isEmpty()) || toiletAirflowClosed.isEmpty() || toiletAirflowMicro.isEmpty() || toiletAirflowOpen.isEmpty()){
                    displayValidationError();
                    return;
                }
            }
        } else {
            if(toiletChecked){
                if(((toiletGridX.isEmpty() || toiletGridY.isEmpty()) && toiletGridRound.isEmpty()) || toiletAirflowClosed.isEmpty() || toiletAirflowOpen.isEmpty()){
                    displayValidationError();
                    return;
                }
            }
        }
        if(windows_micro!=0 || windows_no_micro!=windows_all){
            if(toiletChecked){
                if(((toiletGridX.isEmpty() || toiletGridY.isEmpty()) && toiletGridRound.isEmpty()) || toiletAirflowClosed.isEmpty() || toiletAirflowMicro.isEmpty() || toiletAirflowOpen.isEmpty()){
                    displayValidationError();
                    return;
                }
            }
        } else {
            if(toiletChecked){
                if(((toiletGridX.isEmpty() || toiletGridY.isEmpty()) && toiletGridRound.isEmpty()) || toiletAirflowClosed.isEmpty() || toiletAirflowOpen.isEmpty()){
                    displayValidationError();
                    return;
                }
            }
        }
        if(toiletOthers){
            if(toiletOthersComment.isEmpty()){
                displayValidationError();
                return;
            }
        }
        if(windows_micro!=0 || windows_no_micro!=windows_all){
            if(bathChecked){
                if(((bathGridX.isEmpty() || bathGridY.isEmpty()) && bathGridRound.isEmpty()) || bathAirflowClosed.isEmpty() || bathAirflowMicro.isEmpty() || bathAirflowOpen.isEmpty()){
                    displayValidationError();
                    return;
                }
            }
        } else {
            if(bathChecked){
                if(((bathGridX.isEmpty() || bathGridY.isEmpty()) && bathGridRound.isEmpty()) || bathAirflowClosed.isEmpty() || bathAirflowOpen.isEmpty()){
                    displayValidationError();
                    return;
                }
            }
        }
        if(bathOthers){
            if(bathOthersComment.isEmpty()){
                displayValidationError();
                return;
            }
        }
        if(windows_micro!=0 || windows_no_micro!=windows_all){
            if(flueChecked){
                if(flueAirflowClosed.isEmpty() || flueAirflowMicro.isEmpty() || flueAirflowOpen.isEmpty()){
                    displayValidationError();
                    return;
                }
            }
        } else {
            if(flueChecked){
                if(flueAirflowClosed.isEmpty() || flueAirflowOpen.isEmpty()){
                    displayValidationError();
                    return;
                }
            }
        }
        if(flueOthers){
            if(flueOthersComments.isEmpty()){
                displayValidationError();
                return;
            }
        }

        //get address
        if(intent.hasExtra(Utils.ADDRESS_ID)){
            int addressId = intent.getIntExtra(Utils.ADDRESS_ID, -1);
            protocol.set_address_id(addressId);
            address = addressDataSource.getAddressById(addressId);
        } else {
            Context context = getApplicationContext();
            CharSequence text = AlertUtils.SELECT_ADDRESSES_PROTOCOLS_ADD;
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }

        //fill protocol object
        //kitchen
        protocol.set_kitchen_enabled(kitchenChecked);
        if(kitchenChecked){
            if(kitchenGridRound.equals("")){
                protocol.set_kitchen_grid_dimension_x(Double.parseDouble(kitchenGridX));
                protocol.set_kitchen_grid_dimension_y(Double.parseDouble(kitchenGridY));
            } else {
                protocol.set_kitchen_grid_dimension_round(Double.parseDouble(kitchenGridRound));
            }
            if(windows_micro!=0 || windows_no_micro!=windows_all){
                protocol.set_kitchen_airflow_microventilation(Double.parseDouble(kitchenAirflowMicro));
            }
            protocol.set_kitchen_airflow_windows_closed(Double.parseDouble(kitchenAirflowClosed));
            protocol.set_kitchen_airflow_windows_open(Double.parseDouble(kitchenAirflowOpen));
        }
        protocol.set_kitchen_cleaned(kitchenCleaned);
        protocol.set_kitchen_hood(kitchenHood);
        protocol.set_kitchen_vent(kitchenVent);
        protocol.set_kitchen_inaccessible(kitchenInaccessible);
        protocol.set_kitchen_steady(kitchenSteady);
        protocol.set_kitchen_not_cleaned(kitchenNotCleaned);
        protocol.set_kitchen_others(kitchenOthers);
        if(kitchenOthers)
            protocol.set_kitchen_others_comments(kitchenOthersComment);

        //toilet
        protocol.set_toilet_enabled(toiletChecked);
        if(toiletChecked){
            if(toiletGridRound.equals("")){
                protocol.set_toilet_grid_dimension_x(Double.parseDouble(toiletGridX));
                protocol.set_toilet_grid_dimension_y(Double.parseDouble(toiletGridY));
            } else {
                protocol.set_toilet_grid_dimension_round(Double.parseDouble(toiletGridRound));
            }
            if(windows_micro!=0 || windows_no_micro!=windows_all){
                protocol.set_toilet_airflow_microventilation(Double.parseDouble(toiletAirflowMicro));
            }
            protocol.set_toilet_airflow_windows_closed(Double.parseDouble(toiletAirflowClosed));
            protocol.set_toilet_airflow_windows_open(Double.parseDouble(toiletAirflowOpen));
        }
        protocol.set_toilet_cleaned(toiletCleaned);
        protocol.set_toilet_vent(toiletVent);
        protocol.set_toilet_inaccessible(toiletInaccessible);
        protocol.set_toilet_steady(toiletSteady);
        protocol.set_toilet_not_cleaned(toiletNotCleaned);
        protocol.set_toilet_others(toiletOthers);
        if(toiletOthers)
            protocol.set_toilet_others_comments(toiletOthersComment);

        //bathroom
        protocol.set_bath_enabled(bathChecked);
        if(bathChecked){
            if(bathGridRound.equals("")){
                protocol.set_bath_grid_dimension_x(Double.parseDouble(bathGridX));
                protocol.set_bath_grid_dimension_y(Double.parseDouble(bathGridY));
            } else {
                protocol.set_bath_grid_dimension_round(Double.parseDouble(bathGridRound));
            }
            if(windows_micro!=0 || windows_no_micro!=windows_all){
                protocol.set_bath_airflow_microventilation(Double.parseDouble(bathAirflowMicro));
            }
            protocol.set_bath_airflow_windows_closed(Double.parseDouble(bathAirflowClosed));
            protocol.set_bath_airflow_windows_open(Double.parseDouble(bathAirflowOpen));
        }
        protocol.set_bath_cleaned(bathCleaned);
        protocol.set_bath_vent(bathVent);
        protocol.set_bath_inaccessible(bathInaccessible);
        protocol.set_bath_steady(bathSteady);
        protocol.set_bath_not_cleaned(bathNotCleaned);
        protocol.set_bath_others(bathOthers);
        if(bathOthers)
            protocol.set_bath_others_comments(bathOthersComment);

        //flue
        protocol.set_flue_enabled(flueChecked);
        if(flueChecked){
            if(windows_micro!=0 || windows_no_micro!=windows_all){
                if(!flueAirflowMicro.equals(""))
                    protocol.set_flue_airflow_microventilation(Double.parseDouble(flueAirflowMicro));
            }
            if(!flueAirflowClosed.equals(""))
                protocol.set_flue_airflow_windows_closed(Double.parseDouble(flueAirflowClosed));
            if(!flueAirflowOpen.equals(""))
                protocol.set_flue_airflow_windows_open(Double.parseDouble(flueAirflowOpen));
        }
        protocol.set_flue_cleaned(flueCleaned);
        protocol.set_flue_boiler(flueBoiler);
        protocol.set_flue_inaccessible(flueInaccessible);
        protocol.set_flue_rigid(flueRigid);
        protocol.set_flue_alufol(flueAlufol);
        protocol.set_flue_not_cleaned(flueNotCleaned);
        protocol.set_flue_others(flueOthers);
        if(flueOthers)
            protocol.set_flue_others_comments(flueOthersComments);

        //equipment&others
        if(!co2.equals("")){
            protocol.set_co2(Double.parseDouble(co2));
        }
        protocol.set_comments(eqComments);

        protocol.set_eq_ch_gas_meter_working(eqGasMeterChecked);
        if(eqGasMeterChecked)
            protocol.set_eq_gas_meter_working(eqGasMeterWorking);

        protocol.set_eq_ch_stove_working(eqStoveChecked);
        if(eqStoveChecked)
            protocol.set_eq_stove_working(eqStoveWorking);

        protocol.set_eq_ch_bake_working(eqBakeChecked);
        if(eqBakeChecked)
            protocol.set_eq_bake_working(eqBakeworking);

        protocol.set_eq_ch_combi_oven_working(eqCombiOvenChecked);
        if(eqCombiOvenChecked)
            protocol.set_eq_combi_oven_working(eqCombiOvenWorking);

        protocol.set_eq_ch_kitchen_term_working(eqKitchenTermChecked);
        if(eqKitchenTermChecked)
            protocol.set_eq_kitchen_term_working(eqkitchenTermWorking);

        protocol.set_telephone(telephone);
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        protocol.set_created(dateFormat.format(new Date()));
        protocol.set_comments_for_user(userComments);

        protocol.set_eq_ch_others(eqOthersChecked);
        if(eqOthersChecked)
            protocol.set_eq_other(eqOthersWorking);

//      generate files
        GeneratePDFPaderewskiego pdfGenerator = new GeneratePDFPaderewskiego(getResources());

        try {
            PROTOCOL = protocol;
            pdfFilePath = pdfGenerator.generatePdf(address, protocol);

            //      save in database
            protocolPaderewskiegoDataSource.insertProtocolPaderewskiego(protocol);
            protocolSaved = true;

            if (networkStateChecker.isOnline()) {
                Context context = getApplicationContext();
                CharSequence text = "Plik został poprawnie zapisany w pamięci urządzenia";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

                Button sendButton = findViewById(R.id.send_button);
                sendButton.setEnabled(true);
                openDropboxApp();
            } else {
                Toast.makeText(this, "Brak połączenia z internetem. Protokół został zapisany do późniejszej synchronizacji", Toast.LENGTH_SHORT).show();
                awaitingProtocolDataSource.addAwaitingProtocol(new AwaitingProtocol(pdfFilePath));
            }
        } catch (Exception e) {
            Context context = getApplicationContext();
            e.printStackTrace();
            CharSequence text = String.format("Zapis pliku się nie udał: %s", e.getMessage());
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
    }

    public void sendMail(View view) {
        Uri uri = FileProvider.getUriForFile(this, "com.ihg.fileprovider", new File(pdfFilePath));

        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL, "");
        i.putExtra(Intent.EXTRA_SUBJECT, "Pomiar: " + address.getDistrinct() + ", " + address.getStreet() + " " + address.getBuilding() + "/" + address.getFlat());
        i.putExtra(Intent.EXTRA_TEXT   , "Protokół PDF w załączniku");

        File file = new File(pdfFilePath);
        if (!file.exists() || !file.canRead()) {
            Log.d(Utils.DEBUG_TAG, "Błąd dodawania załącznika");
            Toast.makeText(this, "Błąd dodawania załącznika.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        i.putExtra(Intent.EXTRA_STREAM, uri);

        try {
            startActivity(Intent.createChooser(i, "Wybierz aplikację Gmail"));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(EnterData2Activity.this, "Brak klienta email na urządzeniu.", Toast.LENGTH_SHORT).show();
        }

    }

    private void openDropboxApp() {
        Uri uri = FileProvider.getUriForFile(this, "com.ihg.fileprovider", new File(pdfFilePath));

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_STREAM, uri);

        try {
            startActivity(Intent.createChooser(intent, "Wybierz aplikację Dropbox"));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(EnterData2Activity.this, "Brak klienta Dropbox na urządzeniu.", Toast.LENGTH_SHORT).show();
        }
    }

    public void close(View view) {
        if(!protocolSaved){
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    EnterData2Activity.this);
            alertDialogBuilder.setTitle("Protokół nie zosał zapisany");

            alertDialogBuilder
                    .setCancelable(false)
                    .setNegativeButton("anuluj", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // cancel dialog
                            dialog.cancel();
                        }
                    })
                    .setPositiveButton("zamknij mimo to", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Context context = getApplicationContext();
                            Intent adressesIntent = new Intent(context, BrowseAddressesActivity.class);
                            startActivity(adressesIntent);
                        }
                    });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();

        } else {
            Intent adressesIntent = new Intent(this, BrowseAddressesActivity.class);
            startActivity(adressesIntent);
        }
    }

    private void displayValidationError(){
        Context context = getApplicationContext();
        CharSequence text = "nie wszystkie wymagane pola zostały uzupełnione";
        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    // max length 225 characters
    private String addCommentsForUser(String comment, String userComments){
        if(!userComments.contains(comment)){
            if(userComments.length()+comment.length()+2<=Utils.USER_COMMENTS_LENGTH){
                if(userComments.length()==0){
                    userComments = comment;
                } else {
                    userComments = userComments + ", " + comment;
                }
            } else {
                Context context = getApplicationContext();
                CharSequence text = "nie można dodać zalecenia, tekst za długi";
                int duration = Toast.LENGTH_LONG;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        }
        return userComments;
    }

    private String removeCommentsForUser(String comment, String userComments){
        if(userComments.contains(comment)){
            userComments = userComments.replace(", " + comment, "");
            userComments = userComments.replace(comment, "");
        }
        return userComments;
    }

    private void setTextOnOffLabelChangeListener(final SwitchCompat switchCompat, final TextView switchText) {
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    switchText.setText(switchCompat.getTextOn());
                } else {
                    switchText.setText(switchCompat.getTextOff());
                }
            }
        });
    }
}