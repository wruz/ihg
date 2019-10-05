package com.wruzjan.ihg;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.wruzjan.ihg.utils.Utils;

public class PrinterSettingActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_printer);

        Spinner spinner = (Spinner) findViewById(R.id.printers_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.printers, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        //get data from last entry and fill form
        SharedPreferences settings = getSharedPreferences(Utils.PREFS_NAME, 0);
        spinner.setSelection(settings.getInt(Utils.PRINTER_POSITION, 0));
    }

    public void selectPrinter(View view) {
        //get selected worker
        Spinner printersSpinner = (Spinner) findViewById(R.id.printers_spinner);
        String printer = (String) printersSpinner.getSelectedItem();

        SharedPreferences settings = getSharedPreferences(Utils.PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(Utils.PRINTER_MAC, printer);
        editor.putInt(Utils.PRINTER_POSITION, printersSpinner.getSelectedItemPosition());
        editor.commit();
        finish();
    }

}