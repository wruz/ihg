package com.wruzjan.ihg;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.wruzjan.ihg.utils.StringUtils;
import com.wruzjan.ihg.utils.Utils;

public class PrinterSettingActivity extends Activity {

    private static final String DEFAULT_PRINTER_VALUE = "00:22:58:02:29:0C";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_printer);

        EditText printerEditText = findViewById(R.id.printers_edit);

        //get data from last entry and fill form
        SharedPreferences settings = getSharedPreferences(Utils.PREFS_NAME, 0);
        printerEditText.setText(settings.getString(Utils.PRINTER_MAC, DEFAULT_PRINTER_VALUE));
    }

    public void selectPrinter(View view) {
        //get selected worker
        EditText printerEditText = findViewById(R.id.printers_edit);
        String printer = printerEditText.getText().toString();

        if (StringUtils.isValidMacAddress(printer)) {
            SharedPreferences settings = getSharedPreferences(Utils.PREFS_NAME, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(Utils.PRINTER_MAC, printer);

            // remove obsolete settings param
            if (settings.contains(Utils.PRINTER_POSITION)) {
                editor.remove(Utils.PRINTER_POSITION);
            }

            editor.commit();
            finish();
        } else {
            Toast.makeText(this, getString(R.string.incorrect_printer_mac_address), Toast.LENGTH_SHORT).show();
        }
    }
}