package com.wruzjan.ihg;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.wruzjan.ihg.utils.AdapterUtils;
import com.wruzjan.ihg.utils.AlertUtils;
import com.wruzjan.ihg.utils.Utils;
import com.wruzjan.ihg.utils.dao.AddressDataSource;
import com.wruzjan.ihg.utils.dao.ProtocolDataSource;
import com.wruzjan.ihg.utils.model.Address;
import com.wruzjan.ihg.utils.model.Protocol;
import com.wruzjan.ihg.utils.pdf.GeneratePDF;
import com.wruzjan.ihg.utils.printer.BluetoothConnection;
import com.wruzjan.ihg.utils.view.MultiSelectionViewHelper;
import com.zebra.android.comm.ZebraPrinterConnectionException;
import com.zebra.android.discovery.BluetoothDiscoverer;
import com.zebra.android.discovery.DiscoveredPrinter;
import com.zebra.android.discovery.DiscoveryHandler;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.FileProvider;

public class EnterDataActivity extends Activity {

    public static final String EXTRA_COMPANY_ADDRESS = "EXTRA_COMPANY_ADDRESS";

    private static ArrayList<String> PRINTER_MACS = new ArrayList<String>();

    private AddressDataSource addressDataSource;
    private ProtocolDataSource protocolDataSource;
    private Address address;
    private Protocol PROTOCOL;
    private String pdfFilePath;
    private boolean protocolSaved = false;

    private Spinner kitchenClosedSpinner;
    private ArrayAdapter<String> kitchenClosedSpinnerAdapter;

    private Spinner bathroomClosedSpinner;
    private ArrayAdapter<String> bathroomClosedSpinnerAdapter;

    private Spinner toiletClosedSpinner;
    private ArrayAdapter<String> toiletClosedSpinnerAdapter;

    private Spinner flueClosedSpinner;
    private ArrayAdapter<String> flueClosedSpinnerAdapter;

    private Spinner kitchenMicroventSpinner;
    private ArrayAdapter<String> kitchenMicroventSpinnerAdapter;

    private Spinner bathroomMicroventSpinner;
    private ArrayAdapter<String> bathroomMicroventSpinnerAdapter;

    private Spinner toiletMicroventSpinner;
    private ArrayAdapter<String> toiletMicroventSpinnerAdapter;

    private Spinner flueMicroventSpinner;
    private ArrayAdapter<String> flueMicroventSpinnerAdapter;

    private EditText managerCommentsTextView;
    private MultiSelectionViewHelper managerCommentsMultiSelectionViewHelper;

    private EditText userCommentsTextView;
    private MultiSelectionViewHelper userCommentsMultiSelectionViewHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_data);

        SwitchCompat gasFittingsSwitch = findViewById(R.id.gas_fittings);
        TextView gasFittingsSwitchText = findViewById(R.id.gas_fittings_text);
        setTextOnOffLabelChangeListener(gasFittingsSwitch, gasFittingsSwitchText);
        SwitchCompat gasCookerSwitch = findViewById(R.id.gas_cooker);
        TextView gasCookerSwitchText = findViewById(R.id.gas_cooker_text);
        setTextOnOffLabelChangeListener(gasCookerSwitch, gasCookerSwitchText);
        SwitchCompat bathroomBakeSwitch = findViewById(R.id.bathroom_bake);
        TextView bathroomBakeSwitchText = findViewById(R.id.bathroom_bake_text);
        setTextOnOffLabelChangeListener(bathroomBakeSwitch, bathroomBakeSwitchText);

        addressDataSource = new AddressDataSource(this);
        addressDataSource.open();

        protocolDataSource = new ProtocolDataSource(this);
        protocolDataSource.open();

        managerCommentsTextView = findViewById(R.id.comments_for_manager);
        managerCommentsMultiSelectionViewHelper = new MultiSelectionViewHelper(
                managerCommentsTextView,
                getResources().getStringArray(R.array.general_comments));

        userCommentsTextView = findViewById(R.id.comments_for_user);
        userCommentsMultiSelectionViewHelper = new MultiSelectionViewHelper(
                userCommentsTextView,
                getResources().getStringArray(R.array.general_comments));

        CheckBox gasFittingsCheck = (CheckBox) findViewById(R.id.is_gas_fittings);
        gasFittingsCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    TextView gasFittingsCommentsTextView = (TextView) findViewById(R.id.gas_fittings_comments);
                    gasFittingsCommentsTextView.setText("");
                } else {
                    TextView gasFittingsCommentsTextView = (TextView) findViewById(R.id.gas_fittings_comments);
                    gasFittingsCommentsTextView.setText(AlertUtils.NO_GAS);
                }
            }
        });

        kitchenClosedSpinner = findViewById(R.id.kitchen_airflow_windows_closed);
        kitchenClosedSpinnerAdapter = AdapterUtils.createAdapterAndAssignToSpinner(kitchenClosedSpinner, "0.0");

        bathroomClosedSpinner = findViewById(R.id.bathroom_airflow_windows_closed);
        bathroomClosedSpinnerAdapter = AdapterUtils.createAdapterAndAssignToSpinner(bathroomClosedSpinner, "0.0");

        toiletClosedSpinner = findViewById(R.id.toilet_airflow_windows_closed);
        toiletClosedSpinnerAdapter = AdapterUtils.createAdapterAndAssignToSpinner(toiletClosedSpinner, "0.0");

        flueClosedSpinner = findViewById(R.id.flue_airflow_windows_closed);
        flueClosedSpinnerAdapter = AdapterUtils.createAdapterAndAssignToSpinner(flueClosedSpinner, "0.0");

        kitchenMicroventSpinner = findViewById(R.id.kitchen_airflow_microventilation);
        kitchenMicroventSpinnerAdapter = AdapterUtils.createAdapterAndAssignToSpinner(kitchenMicroventSpinner, "1.0");

        bathroomMicroventSpinner = findViewById(R.id.bathroom_airflow_microventilation);
        bathroomMicroventSpinnerAdapter = AdapterUtils.createAdapterAndAssignToSpinner(bathroomMicroventSpinner, "1.0");

        toiletMicroventSpinner = findViewById(R.id.toilet_airflow_microventilation);
        toiletMicroventSpinnerAdapter = AdapterUtils.createAdapterAndAssignToSpinner(toiletMicroventSpinner, "1.0");

        flueMicroventSpinner = findViewById(R.id.flue_airflow_microventilation);
        flueMicroventSpinnerAdapter = AdapterUtils.createAdapterAndAssignToSpinner(flueMicroventSpinner, "1.0");

        final SwitchCompat kitchenAvailableSwitch = findViewById(R.id.kitchen_availability);
        final TextView kitchenAvailableSwitchText = findViewById(R.id.kitchen_availability_text);
        kitchenAvailableSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    EditText kitchenComments = (EditText) findViewById(R.id.kitchen_comments);
                    kitchenComments.setHint(null);
                    kitchenAvailableSwitchText.setText(kitchenAvailableSwitch.getTextOn());
                } else {
                    EditText kitchenComments = (EditText) findViewById(R.id.kitchen_comments);
                    kitchenComments.setHint("pole wymagane");
                    kitchenComments.requestFocus();
                    kitchenAvailableSwitchText.setText(kitchenAvailableSwitch.getTextOff());
                }
            }
        });

        final SwitchCompat bathroomAvailableSwitch = findViewById(R.id.bathroom_availability);
        final TextView bathroomAvailableSwitchText = findViewById(R.id.bathroom_availabilityy_text);
        bathroomAvailableSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    EditText bathComments = (EditText) findViewById(R.id.bathroom_comments);
                    bathComments.setHint(null);
                    bathroomAvailableSwitchText.setText(bathroomAvailableSwitch.getTextOn());
                } else {
                    EditText bathComments = (EditText) findViewById(R.id.bathroom_comments);
                    bathComments.setHint("pole wymagane");
                    bathComments.requestFocus();
                    bathroomAvailableSwitchText.setText(bathroomAvailableSwitch.getTextOff());
                }
            }
        });

        final SwitchCompat toiletAvailableSwitch = findViewById(R.id.toilet_availability);
        final TextView toiletAvailableSwitchText = findViewById(R.id.toilet_availability_text);
        toiletAvailableSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    EditText toiletDimX = (EditText) findViewById(R.id.toilet_grid_dimension_1);
//                    toiletDimX.setHint("pole wymagane");
                    toiletDimX.requestFocus();
//                    EditText toiletDimY = (EditText) findViewById(R.id.toilet_grid_dimension_2);
//                    toiletDimY.setHint("pole wymagane");
                    EditText toiletComments = (EditText) findViewById(R.id.toilet_comments);
                    toiletComments.setText(AlertUtils.BLANK);
                    toiletAvailableSwitchText.setText(toiletAvailableSwitch.getTextOn());
                } else {
                    // The toggle is disabled
                    EditText toiletDimX = (EditText) findViewById(R.id.toilet_grid_dimension_1);
//                    toiletDimX.setHint(null);
                    toiletDimX.requestFocus();
//                    EditText toiletDimY = (EditText) findViewById(R.id.toilet_grid_dimension_2);
//                    toiletDimY.setHint(null);
                    EditText toiletComments = (EditText) findViewById(R.id.toilet_comments);
                    toiletComments.setText(AlertUtils.LACK);
                    toiletComments.requestFocus();
                    toiletAvailableSwitchText.setText(toiletAvailableSwitch.getTextOff());
                }
            }
        });
        final SwitchCompat flueAvailableSwitch = findViewById(R.id.flue_availability);
        final TextView flueAvailableSwitchText = findViewById(R.id.flue_availability_text);
        flueAvailableSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    flueClosedSpinner.requestFocus();
                    EditText flueComments = (EditText) findViewById(R.id.flue_comments);
                    flueComments.setHint(null);
                    flueAvailableSwitchText.setText(flueAvailableSwitch.getTextOn());
                } else {
                    // The toggle is disabled
                    flueMicroventSpinner.requestFocus();
                    EditText flueComments = (EditText) findViewById(R.id.flue_comments);
                    flueComments.setHint("pole wymagane");
                    flueComments.requestFocus();
                    flueAvailableSwitchText.setText(flueAvailableSwitch.getTextOff());
                }
            }
        });

        //kitchen autocomplete
        EditText kitchenComments = (EditText) findViewById(R.id.kitchen_comments);
        kitchenComments.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String comment = s.toString();
                if (comment.contains("wentylator") || comment.contains("okap elektryczny")) {
                    String userComments = userCommentsMultiSelectionViewHelper.getPreAppendedText();
                    userCommentsMultiSelectionViewHelper.setPreAppendedText(addCommentsForUser("usunąć wyciąg mechaniczny z przewodu w kuchni", userComments));
                    enableOptionIfPreAppendedTextDoesNotContainEntry("usunąć wyciąg mechaniczny z przewodu w kuchni", false);
                }
                if (!comment.contains("wentylator") && !comment.contains("okap elektryczny")) {
                    String userComments = userCommentsMultiSelectionViewHelper.getPreAppendedText();
                    userCommentsMultiSelectionViewHelper.setPreAppendedText(removeCommentsForUser("usunąć wyciąg mechaniczny z przewodu w kuchni", userComments));
                    enableOptionIfPreAppendedTextDoesNotContainEntry("usunąć wyciąg mechaniczny z przewodu w kuchni", true);
                }
                if (comment.contains("kratka stała") || comment.contains("zabudowa, brak dostępu") || comment.contains("sztywna rura")) {
                    String userComments = userCommentsMultiSelectionViewHelper.getPreAppendedText();
                    userCommentsMultiSelectionViewHelper.setPreAppendedText(addCommentsForUser("umożliwić dostęp do przewodu w kuchni", userComments));
                    enableOptionIfPreAppendedTextDoesNotContainEntry("umożliwić dostęp do przewodu w kuchni", false);
                }
                if (!comment.contains("kratka stała") && !comment.contains("zabudowa, brak dostępu") && !comment.contains("sztywna rura")) {
                    String userComments = userCommentsMultiSelectionViewHelper.getPreAppendedText();
                    userCommentsMultiSelectionViewHelper.setPreAppendedText(removeCommentsForUser("umożliwić dostęp do przewodu w kuchni", userComments));
                    enableOptionIfPreAppendedTextDoesNotContainEntry("umożliwić dostęp do przewodu w kuchni", true);
                }
            }
        });

        //bath autocomplete
        EditText bathComments = (EditText) findViewById(R.id.bathroom_comments);
        bathComments.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String comment = s.toString();
                if (comment.contains("zbyt mały otwór w drzwiach")) {
                    String userComments = userCommentsMultiSelectionViewHelper.getPreAppendedText();
                    userCommentsMultiSelectionViewHelper.setPreAppendedText(addCommentsForUser("powiększyć otwór w drzwiach łazienkowych do 220cm2", userComments));
                    enableOptionIfPreAppendedTextDoesNotContainEntry("powiększyć otwór w drzwiach łazienkowych do 220cm2", false);
                }
                if (!comment.contains("zbyt mały otwór w drzwiach")) {
                    String userComments = userCommentsMultiSelectionViewHelper.getPreAppendedText();
                    userCommentsMultiSelectionViewHelper.setPreAppendedText(removeCommentsForUser("powiększyć otwór w drzwiach łazienkowych do 220cm2", userComments));
                    enableOptionIfPreAppendedTextDoesNotContainEntry("powiększyć otwór w drzwiach łazienkowych do 220cm2", true);
                }
                if (comment.contains("wentylator") || comment.contains("okap elektryczny")) {
                    String userComments = userCommentsMultiSelectionViewHelper.getPreAppendedText();
                    userCommentsMultiSelectionViewHelper.setPreAppendedText(addCommentsForUser("usunąć wyciąg mechaniczny z przewodu w łazience", userComments));
                    enableOptionIfPreAppendedTextDoesNotContainEntry("usunąć wyciąg mechaniczny z przewodu w łazience", false);
                }
                if (!comment.contains("wentylator") && !comment.contains("okap elektryczny")) {
                    String userComments = userCommentsMultiSelectionViewHelper.getPreAppendedText();
                    userCommentsMultiSelectionViewHelper.setPreAppendedText(removeCommentsForUser("usunąć wyciąg mechaniczny z przewodu w łazience", userComments));
                    enableOptionIfPreAppendedTextDoesNotContainEntry("usunąć wyciąg mechaniczny z przewodu w łazience", true);
                }
                if (comment.contains("kratka stała") || comment.contains("zabudowa, brak dostępu") || comment.contains("sztywna rura")) {
                    String userComments = userCommentsMultiSelectionViewHelper.getPreAppendedText();
                    userCommentsMultiSelectionViewHelper.setPreAppendedText(addCommentsForUser("umożliwić dostęp do przewodu w łazience", userComments));
                    enableOptionIfPreAppendedTextDoesNotContainEntry("umożliwić dostęp do przewodu w łazience", false);
                }
                if (!comment.contains("kratka stała") && !comment.contains("zabudowa, brak dostępu") && !comment.contains("sztywna rura")) {
                    String userComments = userCommentsMultiSelectionViewHelper.getPreAppendedText();
                    userCommentsMultiSelectionViewHelper.setPreAppendedText(removeCommentsForUser("umożliwić dostęp do przewodu w łazience", userComments));
                    enableOptionIfPreAppendedTextDoesNotContainEntry("umożliwić dostęp do przewodu w łazience", true);
                }
            }
        });

        //toilet autocomplete
        EditText toiletComments = (EditText) findViewById(R.id.toilet_comments);
        toiletComments.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String comment = s.toString();
                if (comment.contains("wentylator") || comment.contains("okap elektryczny")) {
                    String userComments = userCommentsMultiSelectionViewHelper.getPreAppendedText();
                    userCommentsMultiSelectionViewHelper.setPreAppendedText(addCommentsForUser("usunąć wyciąg mechaniczny z przewodu w WC", userComments));
                    enableOptionIfPreAppendedTextDoesNotContainEntry("usunąć wyciąg mechaniczny z przewodu w WC", false);
                }
                if (!comment.contains("wentylator") && !comment.contains("okap elektryczny")) {
                    String userComments = userCommentsMultiSelectionViewHelper.getPreAppendedText();
                    userCommentsMultiSelectionViewHelper.setPreAppendedText(removeCommentsForUser("usunąć wyciąg mechaniczny z przewodu w WC", userComments));
                    enableOptionIfPreAppendedTextDoesNotContainEntry("usunąć wyciąg mechaniczny z przewodu w WC", true);
                }
                if (comment.contains("kratka stała") || comment.contains("zabudowa, brak dostępu") || comment.contains("sztywna rura")) {
                    String userComments = userCommentsMultiSelectionViewHelper.getPreAppendedText();
                    userCommentsMultiSelectionViewHelper.setPreAppendedText(addCommentsForUser("umożliwić dostęp do przewodu w WC", userComments));
                    enableOptionIfPreAppendedTextDoesNotContainEntry("umożliwić dostęp do przewodu w WC", false);
                }
                if (!comment.contains("kratka stała") && !comment.contains("zabudowa, brak dostępu") && !comment.contains("sztywna rura")) {
                    String userComments = userCommentsMultiSelectionViewHelper.getPreAppendedText();
                    userCommentsMultiSelectionViewHelper.setPreAppendedText(removeCommentsForUser("umożliwić dostęp do przewodu w WC", userComments));
                    enableOptionIfPreAppendedTextDoesNotContainEntry("umożliwić dostęp do przewodu w WC", true);
                }
            }
        });

        //flue autocomplete
        EditText flueComments = (EditText) findViewById(R.id.flue_comments);
        flueComments.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String comment = s.toString();
                if (comment.contains("wentylator") || comment.contains("okap elektryczny")) {
                    String userComments = userCommentsMultiSelectionViewHelper.getPreAppendedText();
                    userCommentsMultiSelectionViewHelper.setPreAppendedText(addCommentsForUser("usunąć wyciąg mechaniczny z przewodu spalinowego", userComments));
                    enableOptionIfPreAppendedTextDoesNotContainEntry("usunąć wyciąg mechaniczny z przewodu spalinowego", false);
                }
                if (!comment.contains("wentylator") && !comment.contains("okap elektryczny")) {
                    String userComments = userCommentsMultiSelectionViewHelper.getPreAppendedText();
                    userCommentsMultiSelectionViewHelper.setPreAppendedText(removeCommentsForUser("usunąć wyciąg mechaniczny z przewodu spalinowego", userComments));
                    enableOptionIfPreAppendedTextDoesNotContainEntry("usunąć wyciąg mechaniczny z przewodu spalinowego", true);
                }
                if (comment.contains("kratka stała") || comment.contains("zabudowa, brak dostępu") || comment.contains("sztywna rura")) {
                    String userComments = userCommentsMultiSelectionViewHelper.getPreAppendedText();
                    userCommentsMultiSelectionViewHelper.setPreAppendedText(addCommentsForUser("umożliwić dostęp do przewodu spalinowego", userComments));
                    enableOptionIfPreAppendedTextDoesNotContainEntry("umożliwić dostęp do przewodu spalinowego", false);
                }
                if (!comment.contains("kratka stała") && !comment.contains("zabudowa, brak dostępu") && !comment.contains("sztywna rura")) {
                    String userComments = userCommentsMultiSelectionViewHelper.getPreAppendedText();
                    userCommentsMultiSelectionViewHelper.setPreAppendedText(removeCommentsForUser("umożliwić dostęp do przewodu spalinowego", userComments));
                    enableOptionIfPreAppendedTextDoesNotContainEntry("umożliwić dostęp do przewodu spalinowego", true);
                }
            }
        });

        //co2 autocomplete
        TextView co2TextView = (TextView) findViewById(R.id.co2);
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
                if (!co2.isEmpty()) {
                    int number = Integer.parseInt(co2);
                    String userComments = userCommentsTextView.getText().toString();
                    if (number >= 300 && number < 500) {
                        userCommentsMultiSelectionViewHelper.setPreAppendedText(addCommentsForUser("zalecana konserwacja pieca gazowego", userComments));
                        enableOptionIfPreAppendedTextDoesNotContainEntry("zalecana konserwacja pieca gazowego", false);
                    } else {
                        userCommentsMultiSelectionViewHelper.setPreAppendedText(removeCommentsForUser("zalecana konserwacja pieca gazowego", userComments));
                        enableOptionIfPreAppendedTextDoesNotContainEntry("zalecana konserwacja pieca gazowego", true);
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
                if (!co2.isEmpty()) {
                    int number = Integer.parseInt(co2);
                    String userComments = userCommentsTextView.getText().toString();
                    if (number >= 500) {
                        userCommentsMultiSelectionViewHelper.setPreAppendedText(addCommentsForUser("konieczna konserwacja pieca gazowego", userComments));
                        enableOptionIfPreAppendedTextDoesNotContainEntry("konieczna konserwacja pieca gazowego", false);
                    } else {
                        userCommentsMultiSelectionViewHelper.setPreAppendedText(removeCommentsForUser("konieczna konserwacja pieca gazowego", userComments));
                        enableOptionIfPreAppendedTextDoesNotContainEntry("konieczna konserwacja pieca gazowego", true);
                    }
                }
            }
        });

        //comments autocomplete
        AutoCompleteTextView kitchenCommentsTextView = (AutoCompleteTextView) findViewById(R.id.kitchen_comments);
        AutoCompleteTextView bathCommentsTextView = (AutoCompleteTextView) findViewById(R.id.bathroom_comments);
        AutoCompleteTextView toiletCommentsTextView = (AutoCompleteTextView) findViewById(R.id.toilet_comments);
        AutoCompleteTextView flueCommentsTextView = (AutoCompleteTextView) findViewById(R.id.flue_comments);
        String[] countries = getResources().getStringArray(R.array.comments);
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, countries);
        kitchenCommentsTextView.setAdapter(adapter);
        bathCommentsTextView.setAdapter(adapter);
        toiletCommentsTextView.setAdapter(adapter);
        flueCommentsTextView.setAdapter(adapter);

        Protocol latestProtocol = protocolDataSource.getLatestProtocol();
        TextView equipmentCommentsTextView = findViewById(R.id.equipment_comments);
        if (latestProtocol != null) {
            equipmentCommentsTextView.setText(latestProtocol.get_equipment_comments());
        } else {
            equipmentCommentsTextView.setText(AlertUtils.USER_COMMENTS_TEMPLATE);
        }

        //get edit info
        Intent intent = getIntent();
        if (intent.hasExtra(Utils.EDIT_FLAG)) {
            boolean editFlag = intent.getBooleanExtra(Utils.EDIT_FLAG, false);
            if (editFlag) {
                int addressId = intent.getIntExtra(Utils.ADDRESS_ID, -1);
                int protocolId = intent.getIntExtra(Utils.PROTOCOL_ID, -1);

                //fill data from previous protocol
                Protocol protocolEdited = protocolDataSource.getSiemianowiceProtocolsById(protocolId);

                //kitchen
                kitchenAvailableSwitch.setChecked(protocolEdited.is_kitchen_enabled());
                if (Float.compare(protocolEdited.get_kitchen_grid_dimension_x(), 0.0f) != 0) {
                    TextView kitchenGridXTextView = (TextView) findViewById(R.id.kitchen_grid_dimension_1);
                    kitchenGridXTextView.setText(Float.toString(protocolEdited.get_kitchen_grid_dimension_x()));
                }
                if (Float.compare(protocolEdited.get_kitchen_grid_dimension_y(), 0.0f) != 0) {
                    TextView kitchenGridYTextView = (TextView) findViewById(R.id.kitchen_grid_dimension_2);
                    kitchenGridYTextView.setText(Float.toString(protocolEdited.get_kitchen_grid_dimension_y()));
                }
                if (protocolEdited.get_kitchen_grid_dimension_round() != 0.0) {
                    TextView kitchenGridRoundTextView = (TextView) findViewById(R.id.kitchen_grid_dimension_round);
                    kitchenGridRoundTextView.setText(Double.toString(protocolEdited.get_kitchen_grid_dimension_round()));
                }
                AdapterUtils.setItemToSpinner(kitchenClosedSpinner, kitchenClosedSpinnerAdapter, protocolEdited.get_kitchen_airflow_windows_closed());
                AdapterUtils.setItemToSpinner(kitchenMicroventSpinner, kitchenMicroventSpinnerAdapter, protocolEdited.get_kitchen_airflow_microventilation());
                kitchenComments.setText(protocolEdited.get_kitchen_comments());

                //toilet
                toiletAvailableSwitch.setChecked(protocolEdited.is_toilet_enabled());
                if (Float.compare(protocolEdited.get_toilet_grid_dimension_x(), 0.0f) != 0) {
                    TextView toiletGridXTextView = (TextView) findViewById(R.id.toilet_grid_dimension_1);
                    toiletGridXTextView.setText(Float.toString(protocolEdited.get_toilet_grid_dimension_x()));
                }
                if (Float.compare(protocolEdited.get_toilet_grid_dimension_y(), 0.0f) != 0) {
                    TextView toiletGridYTextView = (TextView) findViewById(R.id.toilet_grid_dimension_2);
                    toiletGridYTextView.setText(Float.toString(protocolEdited.get_toilet_grid_dimension_y()));
                }
                if (protocolEdited.get_toilet_grid_dimension_round() != 0.0) {
                    TextView toiletGridRoundTextView = (TextView) findViewById(R.id.toilet_grid_dimension_round);
                    toiletGridRoundTextView.setText(Double.toString(protocolEdited.get_toilet_grid_dimension_round()));
                }
                AdapterUtils.setItemToSpinner(toiletClosedSpinner, toiletClosedSpinnerAdapter, protocolEdited.get_toilet_airflow_windows_closed());
                AdapterUtils.setItemToSpinner(toiletMicroventSpinner, toiletMicroventSpinnerAdapter, protocolEdited.get_toilet_airflow_microventilation());
                toiletComments.setText(protocolEdited.get_toilet_comments());

                //bath
                bathroomAvailableSwitch.setChecked(protocolEdited.is_bathroom_enabled());
                if (Float.compare(protocolEdited.get_bathroom_grid_dimension_x(), 0.0f) != 0) {
                    TextView bathGridXTextView = (TextView) findViewById(R.id.bathroom_grid_dimension_1);
                    bathGridXTextView.setText(Float.toString(protocolEdited.get_bathroom_grid_dimension_x()));
                }
                if (Float.compare(protocolEdited.get_bathroom_grid_dimension_y(), 0.0f) != 0) {
                    TextView bathGridYTextView = (TextView) findViewById(R.id.bathroom_grid_dimension_2);
                    bathGridYTextView.setText(Float.toString(protocolEdited.get_bathroom_grid_dimension_y()));
                }
                if (protocolEdited.get_bathroom_grid_dimension_round() != 0.0) {
                    TextView bathGridRoundTextView = (TextView) findViewById(R.id.bathroom_grid_dimension_round);
                    bathGridRoundTextView.setText(Double.toString(protocolEdited.get_bathroom_grid_dimension_round()));
                }
                AdapterUtils.setItemToSpinner(bathroomClosedSpinner, bathroomClosedSpinnerAdapter, protocolEdited.get_bathroom_airflow_windows_closed());
                AdapterUtils.setItemToSpinner(bathroomMicroventSpinner, bathroomMicroventSpinnerAdapter, protocolEdited.get_bathroom_airflow_microventilation());
                bathComments.setText(protocolEdited.get_bathroom_comments());

                //flue
                flueAvailableSwitch.setChecked(protocolEdited.is_flue_enabled());
                AdapterUtils.setItemToSpinner(flueClosedSpinner, flueClosedSpinnerAdapter, protocolEdited.get_flue_airflow_windows_closed());
                AdapterUtils.setItemToSpinner(flueMicroventSpinner, flueMicroventSpinnerAdapter, protocolEdited.get_flue_airflow_microventilation());
                flueComments.setText(protocolEdited.get_flue_comments());

                //others
                gasFittingsCheck.setChecked(protocolEdited.is_gas_fittings_present());
                gasFittingsSwitch.setChecked(protocolEdited.is_gas_fittings_working());
                TextView gasFittingsCommentsTextView = (TextView) findViewById(R.id.gas_fittings_comments);
                gasFittingsCommentsTextView.setText(protocolEdited.get_gas_fittings_comments());
                CheckBox gasCookerCheck = (CheckBox) findViewById(R.id.is_gas_cooker);
                gasCookerCheck.setChecked(protocolEdited.is_gas_cooker_present());
                gasCookerSwitch.setChecked(protocolEdited.is_gas_cooker_working());
                CheckBox bathroomBakeCheck = (CheckBox) findViewById(R.id.is_bathroom_bake);
                bathroomBakeCheck.setChecked(protocolEdited.is_bathroom_bake_present());
                bathroomBakeSwitch.setChecked(protocolEdited.is_bathroom_bake_working());
                equipmentCommentsTextView.setText(protocolEdited.get_equipment_comments());
                if (Float.compare(protocolEdited.get_co2(), 0.0f) != 0) {
                    co2TextView.setText(Integer.toString((int) (Math.round(protocolEdited.get_co2()))));
                }
                if (!TextUtils.isEmpty(protocolEdited.get_comments_for_user())) {
                    userCommentsMultiSelectionViewHelper.setSelection(protocolEdited.get_comments_for_user());
                }

                if (!TextUtils.isEmpty(protocolEdited.get_comments_for_manager())) {
                    managerCommentsMultiSelectionViewHelper.setSelection(protocolEdited.get_comments_for_manager());
                }
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.select_printer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(this, PrinterSettingActivity.class);
        startActivity(intent);
        return true;
    }

    /**
     * save button
     */
    public void saveData(View view) {

        Intent intent = getIntent();
        Protocol protocol = new Protocol();

        String companyAddress = getIntent().getStringExtra(EXTRA_COMPANY_ADDRESS);
        PreferenceManager.getDefaultSharedPreferences(this).edit().putString(Utils.PREF_SIEMANOWICE_COMPANY_ADDRESS, companyAddress).apply();
        protocol.setCompanyAddress(companyAddress);

        //get worker
        if (intent.hasExtra(Utils.WORKER_NAME)) {
            protocol.set_worker_name(intent.getStringExtra(Utils.WORKER_NAME));
        }
        if (intent.hasExtra(Utils.TEMP_INSIDE)) {
            protocol.set_temp_inside(Float.parseFloat(intent.getStringExtra(Utils.TEMP_INSIDE)));
        }
        if (intent.hasExtra(Utils.WORKER_NAME)) {
            protocol.set_temp_outside(Float.parseFloat(intent.getStringExtra(Utils.TEMP_OUTSIDE)));
        }

//        get fields from form
        SwitchCompat kitchenAvailableSwitch = findViewById(R.id.kitchen_availability);
        boolean kitchenChecked = kitchenAvailableSwitch.isChecked();
        TextView kitchenGridXTextView = (TextView) findViewById(R.id.kitchen_grid_dimension_1);
        String kitchenGridX = kitchenGridXTextView.getText().toString();
        TextView kitchenGridYTextView = (TextView) findViewById(R.id.kitchen_grid_dimension_2);
        String kitchenGridY = kitchenGridYTextView.getText().toString();
        TextView kitchenGridRoundTextView = (TextView) findViewById(R.id.kitchen_grid_dimension_round);
        String kitchenGridRound = kitchenGridRoundTextView.getText().toString();
        String kitchenAirflowClosed = kitchenClosedSpinner.getSelectedItem().toString();
        String kitchenAirflowMicro = kitchenMicroventSpinner.getSelectedItem().toString();
        TextView kitchenCommentsTextView = (TextView) findViewById(R.id.kitchen_comments);
        String kitchenComments = kitchenCommentsTextView.getText().toString();

        SwitchCompat bathAvailableSwitch = findViewById(R.id.bathroom_availability);
        boolean bathChecked = bathAvailableSwitch.isChecked();
        TextView bathGridXTextView = (TextView) findViewById(R.id.bathroom_grid_dimension_1);
        String bathGridX = bathGridXTextView.getText().toString();
        TextView bathGridYTextView = (TextView) findViewById(R.id.bathroom_grid_dimension_2);
        String bathGridY = bathGridYTextView.getText().toString();
        TextView bathGridRoundTextView = (TextView) findViewById(R.id.bathroom_grid_dimension_round);
        String bathGridRound = bathGridRoundTextView.getText().toString();
        String bathAirflowClosed = bathroomClosedSpinner.getSelectedItem().toString();
        String bathAirflowMicro = bathroomMicroventSpinner.getSelectedItem().toString();
        TextView bathroomCommentsTextView = (TextView) findViewById(R.id.bathroom_comments);
        String bathroomComments = bathroomCommentsTextView.getText().toString();

        SwitchCompat toiletAvailableSwitch = findViewById(R.id.toilet_availability);
        boolean toiletChecked = toiletAvailableSwitch.isChecked();
        TextView toiletGridXTextView = (TextView) findViewById(R.id.toilet_grid_dimension_1);
        String toiletGridX = toiletGridXTextView.getText().toString();
        TextView toiletGridYTextView = (TextView) findViewById(R.id.toilet_grid_dimension_2);
        String toiletGridY = toiletGridYTextView.getText().toString();
        TextView toiletGridRoundTextView = (TextView) findViewById(R.id.toilet_grid_dimension_round);
        String toiletGridRound = toiletGridRoundTextView.getText().toString();
        String toiletAirflowClosed = toiletClosedSpinner.getSelectedItem().toString();
        String toiletAirflowMicro = toiletMicroventSpinner.getSelectedItem().toString();
        TextView toiletCommentsTextView = (TextView) findViewById(R.id.toilet_comments);
        String toiletComments = toiletCommentsTextView.getText().toString();

        SwitchCompat flueAvailableSwitch = findViewById(R.id.flue_availability);
        boolean flueChecked = flueAvailableSwitch.isChecked();
        String flueAirflowClosed = flueClosedSpinner.getSelectedItem().toString();
        String flueAirflowMicro = flueMicroventSpinner.getSelectedItem().toString();
        TextView flueCommentsTextView = (TextView) findViewById(R.id.flue_comments);
        String flueComments = flueCommentsTextView.getText().toString();

        CheckBox gasFittingsCheck = (CheckBox) findViewById(R.id.is_gas_fittings);
        boolean gasFittingsPresent = gasFittingsCheck.isChecked();
        SwitchCompat gasFittingsSwitch = findViewById(R.id.gas_fittings);
        boolean gasFittingsChecked = gasFittingsSwitch.isChecked();
        TextView gasFittingsCommentsTextView = (TextView) findViewById(R.id.gas_fittings_comments);
        String gasFittingsComments = gasFittingsCommentsTextView.getText().toString();
        CheckBox gasCookerCheck = (CheckBox) findViewById(R.id.is_gas_cooker);
        boolean gasCookerPresent = gasCookerCheck.isChecked();
        SwitchCompat gasCookerSwitch = findViewById(R.id.gas_cooker);
        boolean gasCookerChecked = gasCookerSwitch.isChecked();
        CheckBox bathroomBakeCheck = (CheckBox) findViewById(R.id.is_bathroom_bake);
        boolean bathroomBakePresent = bathroomBakeCheck.isChecked();
        SwitchCompat bathroomBakeSwitch = findViewById(R.id.bathroom_bake);
        boolean bathroomBakeChecked = bathroomBakeSwitch.isChecked();
        TextView equipmentCommentsTextView = (TextView) findViewById(R.id.equipment_comments);
        String equipmentComments = equipmentCommentsTextView.getText().toString();
        TextView co2TextView = (TextView) findViewById(R.id.co2);
        String co2 = co2TextView.getText().toString();

//        validate required fields

        if (kitchenChecked) {
            if (((kitchenGridX.isEmpty() || kitchenGridY.isEmpty()) && kitchenGridRound.isEmpty()) || kitchenAirflowClosed.isEmpty() || kitchenAirflowMicro.isEmpty()) {
                displayValidationError();
                return;
            }
        } else {
            if (kitchenComments.isEmpty()) {
                displayValidationError();
                return;
            }
        }
        if (bathChecked) {
            if (((bathGridX.isEmpty() || bathGridY.isEmpty()) && bathGridRound.isEmpty()) || bathAirflowClosed.isEmpty() || bathAirflowMicro.isEmpty()) {
                displayValidationError();
                return;
            }
        } else {
            if (bathroomComments.isEmpty()) {
                displayValidationError();
                return;
            }
        }
        if (toiletChecked) {
            if (((toiletGridX.isEmpty() || toiletGridY.isEmpty()) && toiletGridRound.isEmpty()) || toiletAirflowClosed.isEmpty() || toiletAirflowMicro.isEmpty()) {
                displayValidationError();
                return;
            }
        } else {
            if (toiletComments.isEmpty()) {
                displayValidationError();
                return;
            }
        }
        if (flueChecked) {
            if (flueAirflowClosed.isEmpty() || flueAirflowMicro.isEmpty()) {
                displayValidationError();
                return;
            }
        } else {
            if (flueComments.isEmpty()) {
                displayValidationError();
                return;
            }
        }
        if (!co2.isEmpty()) {
            if (Float.parseFloat(co2) <= 1500) {
                protocol.set_co2(Float.parseFloat(co2));
            } else {
                displayValidationError();
                return;
            }
        }

        protocol.set_kitchen_enabled(kitchenChecked);
        if (kitchenChecked) {
            if (kitchenGridRound.equals("")) {
                protocol.set_kitchen_grid_dimension_x(Float.parseFloat(kitchenGridX));
                protocol.set_kitchen_grid_dimension_y(Float.parseFloat(kitchenGridY));
            } else {
                protocol.set_kitchen_grid_dimension_round(Double.parseDouble(kitchenGridRound));
            }
            protocol.set_kitchen_airflow_windows_closed(Float.parseFloat(kitchenAirflowClosed));
            protocol.set_kitchen_airflow_microventilation(Float.parseFloat(kitchenAirflowMicro));
        }
        protocol.set_kitchen_comments(kitchenComments);

        protocol.set_bathroom_enabled(bathChecked);
        if (bathChecked) {
            if (bathGridRound.equals("")) {
                protocol.set_bathroom_grid_dimension_x(Float.parseFloat(bathGridX));
                protocol.set_bathroom_grid_dimension_y(Float.parseFloat(bathGridY));
            } else {
                protocol.set_bathroom_grid_dimension_round(Double.parseDouble(bathGridRound));
            }
            protocol.set_bathroom_airflow_windows_closed(Float.parseFloat(bathAirflowClosed));
            protocol.set_bathroom_airflow_microventilation(Float.parseFloat(bathAirflowMicro));
        }
        protocol.set_bathroom_comments(bathroomComments);

        protocol.set_toilet_enabled(toiletChecked);
        if (toiletChecked) {
            if (toiletGridRound.equals("")) {
                protocol.set_toilet_grid_dimension_x(Float.parseFloat(toiletGridX));
                protocol.set_toilet_grid_dimension_y(Float.parseFloat(toiletGridY));
            } else {
                protocol.set_toilet_grid_dimension_round(Double.parseDouble(toiletGridRound));
            }
            protocol.set_toilet_airflow_windows_closed(Float.parseFloat(toiletAirflowClosed));
            protocol.set_toilet_airflow_microventilation(Float.parseFloat(toiletAirflowMicro));
        }
        protocol.set_toilet_comments(toiletComments);

        protocol.set_flue_enabled(flueChecked);
        if (flueChecked) {
            protocol.set_flue_airflow_windows_closed(Float.parseFloat(flueAirflowClosed));
            protocol.set_flue_airflow_microventilation(Float.parseFloat(flueAirflowMicro));
        }
        protocol.set_flue_comments(flueComments);

        protocol.set_gas_fittings_present(gasFittingsPresent);
        protocol.set_gas_fittings_working(gasFittingsChecked);
        protocol.set_gas_fittings_comments(gasFittingsComments);
        protocol.set_gas_cooker_present(gasCookerPresent);
        protocol.set_gas_cooker_working(gasCookerChecked);
        protocol.set_bathroom_bake_present(bathroomBakePresent);
        protocol.set_bathroom_bake_working(bathroomBakeChecked);
        protocol.set_equipment_comments(equipmentComments);
        protocol.set_comments_for_user(userCommentsTextView.getText().toString());
        protocol.set_comments_for_manager(managerCommentsTextView.getText().toString());
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        protocol.set_created(dateFormat.format(new Date()));

        //get address
        if (intent.hasExtra(Utils.ADDRESS_ID)) {
            int addressId = intent.getIntExtra(Utils.ADDRESS_ID, -1);
            protocol.set_address_id(addressId);
            address = addressDataSource.getAddressById(addressId);
        } else {
            Context context = getApplicationContext();
            CharSequence text = "wybierz adres, do którego chcesz dodać formularz";
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
//      generate files
        GeneratePDF pdfGenerator = new GeneratePDF();
        try {
            PROTOCOL = protocol;
            pdfFilePath = pdfGenerator.generatePdf(address, protocol);

            //      save in database
            protocolDataSource.insertProtocolSiemianowice(protocol);
            protocolSaved = true;

            Context context = getApplicationContext();
            CharSequence text = String.format("Plik został poprawnie zapisany w pamięci urządzenia");
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

            Button sendButton = (Button) findViewById(R.id.send_button);
            sendButton.setEnabled(true);

            openDrobpoxApp();
        } catch (Exception e) {
            Context context = getApplicationContext();
            e.printStackTrace();
            Log.d("IGH_DEBUG", e.toString());
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
        i.putExtra(Intent.EXTRA_TEXT, "Protokół PDF w załączniku");

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
            Toast.makeText(EnterDataActivity.this, "Brak klienta email na urządzeniu.", Toast.LENGTH_SHORT).show();
        }

    }

    private void openDrobpoxApp() {
        Uri uri = FileProvider.getUriForFile(this, "com.ihg.fileprovider", new File(pdfFilePath));

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_STREAM, uri);

        try {
            startActivity(Intent.createChooser(intent, "Wybierz aplikację Dropbox"));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(EnterDataActivity.this, "Brak klienta Dropbox na urządzeniu.", Toast.LENGTH_SHORT).show();
        }
    }

    public void close(View view) {

        if (!protocolSaved) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    EnterDataActivity.this);
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

    private void displayValidationError() {
        Context context = getApplicationContext();
        CharSequence text = "nie wszystkie wymagane pola zostały uzupełnione";
        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    /*
    print button
     */
    public void printData(View view) throws ZebraPrinterConnectionException, InterruptedException {

        Intent intent = getIntent();
        Protocol protocol = new Protocol();

        //get worker
        if (intent.hasExtra(Utils.WORKER_NAME)) {
            protocol.set_worker_name(intent.getStringExtra(Utils.WORKER_NAME));
        }
        if (intent.hasExtra(Utils.TEMP_INSIDE)) {
            protocol.set_temp_inside(Float.parseFloat(intent.getStringExtra(Utils.TEMP_INSIDE)));
        }
        if (intent.hasExtra(Utils.WORKER_NAME)) {
            protocol.set_temp_outside(Float.parseFloat(intent.getStringExtra(Utils.TEMP_OUTSIDE)));
        }

//        get fields from form
        SwitchCompat kitchenAvailableSwitch = findViewById(R.id.kitchen_availability);
        boolean kitchenChecked = kitchenAvailableSwitch.isChecked();
        TextView kitchenGridXTextView = (TextView) findViewById(R.id.kitchen_grid_dimension_1);
        String kitchenGridX = kitchenGridXTextView.getText().toString();
        TextView kitchenGridYTextView = (TextView) findViewById(R.id.kitchen_grid_dimension_2);
        String kitchenGridY = kitchenGridYTextView.getText().toString();
        TextView kitchenGridRoundTextView = (TextView) findViewById(R.id.kitchen_grid_dimension_round);
        String kitchenGridRound = kitchenGridRoundTextView.getText().toString();
        String kitchenAirflowClosed = kitchenClosedSpinner.getSelectedItem().toString();
        String kitchenAirflowMicro = kitchenMicroventSpinner.getSelectedItem().toString();
        TextView kitchenCommentsTextView = (TextView) findViewById(R.id.kitchen_comments);
        String kitchenComments = kitchenCommentsTextView.getText().toString();

        SwitchCompat bathAvailableSwitch = findViewById(R.id.bathroom_availability);
        boolean bathChecked = bathAvailableSwitch.isChecked();
        TextView bathGridXTextView = (TextView) findViewById(R.id.bathroom_grid_dimension_1);
        String bathGridX = bathGridXTextView.getText().toString();
        TextView bathGridYTextView = (TextView) findViewById(R.id.bathroom_grid_dimension_2);
        String bathGridY = bathGridYTextView.getText().toString();
        TextView bathGridRoundTextView = (TextView) findViewById(R.id.bathroom_grid_dimension_round);
        String bathGridRound = bathGridRoundTextView.getText().toString();
        String bathAirflowClosed = bathroomClosedSpinner.getSelectedItem().toString();
        String bathAirflowMicro = bathroomMicroventSpinner.getSelectedItem().toString();
        TextView bathroomCommentsTextView = (TextView) findViewById(R.id.bathroom_comments);
        String bathroomComments = bathroomCommentsTextView.getText().toString();

        SwitchCompat toiletAvailableSwitch = findViewById(R.id.toilet_availability);
        boolean toiletChecked = toiletAvailableSwitch.isChecked();
        TextView toiletGridXTextView = (TextView) findViewById(R.id.toilet_grid_dimension_1);
        String toiletGridX = toiletGridXTextView.getText().toString();
        TextView toiletGridYTextView = (TextView) findViewById(R.id.toilet_grid_dimension_2);
        String toiletGridY = toiletGridYTextView.getText().toString();
        TextView toiletGridRoundTextView = (TextView) findViewById(R.id.toilet_grid_dimension_round);
        String toiletGridRound = toiletGridRoundTextView.getText().toString();
        String toiletAirflowClosed = toiletClosedSpinner.getSelectedItem().toString();
        String toiletAirflowMicro = toiletMicroventSpinner.getSelectedItem().toString();
        TextView toiletCommentsTextView = (TextView) findViewById(R.id.toilet_comments);
        String toiletComments = toiletCommentsTextView.getText().toString();

        SwitchCompat flueAvailableSwitch = findViewById(R.id.flue_availability);
        boolean flueChecked = flueAvailableSwitch.isChecked();
        String flueAirflowClosed = flueClosedSpinner.getSelectedItem().toString();
        String flueAirflowMicro = flueMicroventSpinner.getSelectedItem().toString();
        TextView flueCommentsTextView = (TextView) findViewById(R.id.flue_comments);
        String flueComments = flueCommentsTextView.getText().toString();

        CheckBox gasFittingsCheck = (CheckBox) findViewById(R.id.is_gas_fittings);
        boolean gasFittingsPresent = gasFittingsCheck.isChecked();
        SwitchCompat gasFittingsSwitch = findViewById(R.id.gas_fittings);
        boolean gasFittingsChecked = gasFittingsSwitch.isChecked();
        TextView gasFittingsCommentsTextView = (TextView) findViewById(R.id.gas_fittings_comments);
        String gasFittingsComments = gasFittingsCommentsTextView.getText().toString();
        CheckBox gasCookerCheck = (CheckBox) findViewById(R.id.is_gas_cooker);
        boolean gasCookerPresent = gasCookerCheck.isChecked();
        SwitchCompat gasCookerSwitch = findViewById(R.id.gas_cooker);
        boolean gasCookerChecked = gasCookerSwitch.isChecked();
        CheckBox bathroomBakeCheck = (CheckBox) findViewById(R.id.is_bathroom_bake);
        boolean bathroomBakePresent = bathroomBakeCheck.isChecked();
        SwitchCompat bathroomBakeSwitch = findViewById(R.id.bathroom_bake);
        boolean bathroomBakeChecked = bathroomBakeSwitch.isChecked();
        TextView equipmentCommentsTextView = (TextView) findViewById(R.id.equipment_comments);
        String equipmentComments = equipmentCommentsTextView.getText().toString();
        TextView co2TextView = (TextView) findViewById(R.id.co2);
        String co2 = co2TextView.getText().toString();

//        validate required fields
        if (kitchenChecked) {
            if (((kitchenGridX.isEmpty() || kitchenGridY.isEmpty()) && kitchenGridRound.isEmpty()) || kitchenAirflowClosed.isEmpty() || kitchenAirflowMicro.isEmpty()) {
                displayValidationError();
                return;
            }
        } else {
            if (kitchenComments.isEmpty()) {
                displayValidationError();
                return;
            }
        }
        if (bathChecked) {
            if (((bathGridX.isEmpty() || bathGridY.isEmpty()) && bathGridRound.isEmpty()) || bathAirflowClosed.isEmpty() || bathAirflowMicro.isEmpty()) {
                displayValidationError();
                return;
            }
        } else {
            if (bathroomComments.isEmpty()) {
                displayValidationError();
                return;
            }
        }
        if (toiletChecked) {
            if (((toiletGridX.isEmpty() || toiletGridY.isEmpty()) && toiletGridRound.isEmpty()) || toiletAirflowClosed.isEmpty() || toiletAirflowMicro.isEmpty()) {
                displayValidationError();
                return;
            }
        } else {
            if (toiletComments.isEmpty()) {
                displayValidationError();
                return;
            }
        }
        if (flueChecked) {
            if (flueAirflowClosed.isEmpty() || flueAirflowMicro.isEmpty()) {
                displayValidationError();
                return;
            }
        } else {
            if (flueComments.isEmpty()) {
                displayValidationError();
                return;
            }
        }
        if (!co2.isEmpty()) {
            if (Float.parseFloat(co2) <= 1500) {
                protocol.set_co2(Float.parseFloat(co2));
            } else {
                displayValidationError();
                return;
            }
        }

        protocol.set_kitchen_enabled(kitchenChecked);
        if (kitchenChecked) {
            if (kitchenGridRound.equals("")) {
                protocol.set_kitchen_grid_dimension_x(Float.parseFloat(kitchenGridX));
                protocol.set_kitchen_grid_dimension_y(Float.parseFloat(kitchenGridY));
            } else {
                protocol.set_kitchen_grid_dimension_round(Double.parseDouble(kitchenGridRound));
            }
            protocol.set_kitchen_airflow_windows_closed(Float.parseFloat(kitchenAirflowClosed));
            protocol.set_kitchen_airflow_microventilation(Float.parseFloat(kitchenAirflowMicro));
        }
        protocol.set_kitchen_comments(kitchenComments);

        protocol.set_bathroom_enabled(bathChecked);
        if (bathChecked) {
            if (bathGridRound.equals("")) {
                protocol.set_bathroom_grid_dimension_x(Float.parseFloat(bathGridX));
                protocol.set_bathroom_grid_dimension_y(Float.parseFloat(bathGridY));
            } else {
                protocol.set_bathroom_grid_dimension_round(Double.parseDouble(bathGridRound));
            }
            protocol.set_bathroom_airflow_windows_closed(Float.parseFloat(bathAirflowClosed));
            protocol.set_bathroom_airflow_microventilation(Float.parseFloat(bathAirflowMicro));
        }
        protocol.set_bathroom_comments(bathroomComments);

        protocol.set_toilet_enabled(toiletChecked);
        if (toiletChecked) {
            if (toiletGridRound.equals("")) {
                protocol.set_toilet_grid_dimension_x(Float.parseFloat(toiletGridX));
                protocol.set_toilet_grid_dimension_y(Float.parseFloat(toiletGridY));
            } else {
                protocol.set_toilet_grid_dimension_round(Double.parseDouble(toiletGridRound));
            }
            protocol.set_toilet_airflow_windows_closed(Float.parseFloat(toiletAirflowClosed));
            protocol.set_toilet_airflow_microventilation(Float.parseFloat(toiletAirflowMicro));
        }
        protocol.set_toilet_comments(toiletComments);

        protocol.set_flue_enabled(flueChecked);
        if (flueChecked) {
            protocol.set_flue_airflow_windows_closed(Float.parseFloat(flueAirflowClosed));
            protocol.set_flue_airflow_microventilation(Float.parseFloat(flueAirflowMicro));
        }
        protocol.set_flue_comments(flueComments);

        protocol.set_gas_fittings_present(gasFittingsPresent);
        protocol.set_gas_fittings_working(gasFittingsChecked);
        protocol.set_gas_fittings_comments(gasFittingsComments);
        protocol.set_gas_cooker_present(gasCookerPresent);
        protocol.set_gas_cooker_working(gasCookerChecked);
        protocol.set_bathroom_bake_present(bathroomBakePresent);
        protocol.set_bathroom_bake_working(bathroomBakeChecked);
        protocol.set_equipment_comments(equipmentComments);
        protocol.set_comments_for_user(userCommentsTextView.getText().toString());
        protocol.set_comments_for_manager(managerCommentsTextView.getText().toString());

        PROTOCOL = protocol;

        //get address
        if (intent.hasExtra(Utils.ADDRESS_ID)) {
            int addressId = intent.getIntExtra(Utils.ADDRESS_ID, -1);
            address = addressDataSource.getAddressById(addressId);
        } else {
            Context context = getApplicationContext();
            CharSequence text = "wybierz adres, do którego chcesz dodać formularz";
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
        BluetoothDiscoverer.findPrinters(getApplicationContext(), new DiscoveryHandler() {
            @Override
            public void foundPrinter(DiscoveredPrinter discoveredPrinter) {
                PRINTER_MACS.add(discoveredPrinter.address);
            }

            @Override
            public void discoveryFinished() {
                SharedPreferences settings = getSharedPreferences(Utils.PREFS_NAME, 0);
                BluetoothConnection bluetoothConnection = new BluetoothConnection();
//                bluetoothConnection.test(settings.getString("printerMac", ""), address, PROTOCOL);
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
    }

//    signature disabled
//    public void getSignature(View view) {
//        Intent intent = new Intent(this, CaptureSignatureActivity.class);
//        startActivity(intent);
//    }

    @Override
    protected void onResume() {
        addressDataSource.open();
        protocolDataSource.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        addressDataSource.close();
        protocolDataSource.close();
        super.onPause();

    }

    // max length 225 characters
    private String addCommentsForUser(@Nullable String comment, @Nullable String userComments) {
        if (userComments == null) {
            userComments = "";
        }
        if (comment == null) {
            comment = "";
        }
        if (!userComments.contains(comment)) {
            if (userComments.length() + comment.length() + 2 <= Utils.USER_COMMENTS_LENGTH) {
                if (userComments.length() == 0) {
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

    private String removeCommentsForUser(@Nullable String comment, @Nullable String userComments) {
        if (userComments == null) {
            userComments = "";
        }
        if (comment == null) {
            comment = "";
        }
        if (userComments.contains(comment)) {
            userComments = userComments.replace(", " + comment, "");
            userComments = userComments.replace(comment, "");
        }
        return userComments;
    }


    private void enableOptionIfPreAppendedTextDoesNotContainEntry(String preAppendedText, boolean enabled) {
        int index = userCommentsMultiSelectionViewHelper.indexOfEntries(preAppendedText);
        if (index != -1) {
            userCommentsMultiSelectionViewHelper.setEnabledOption(index, enabled);
            userCommentsMultiSelectionViewHelper.setSelectedOption(index, !enabled);
        }
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