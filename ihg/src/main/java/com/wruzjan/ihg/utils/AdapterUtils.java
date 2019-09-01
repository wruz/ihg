package com.wruzjan.ihg.utils;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Locale;

import androidx.annotation.NonNull;

public class AdapterUtils {

    private AdapterUtils() {
        //no-op
    }

    public static ArrayAdapter<String> createInRangeAdapter(@NonNull Context context, float startValue, float endValue, float step) {
        ArrayList<String> values = new ArrayList<>();

        for (float i = startValue; i < endValue; i += step) {
            values.add(String.format(Locale.US, "%.1f", i));
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, values);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return arrayAdapter;
    }

    public static void setItemToSpinner(@NonNull Spinner spinner, @NonNull ArrayAdapter<String> adapter, float value) {
        int selectedItemIndex = adapter.getPosition(String.format(Locale.US, "%.1f", value));
        spinner.setSelection(selectedItemIndex);
    }

    @NonNull
    public static ArrayAdapter<String> createAdapterAndAssignToSpinner(@NonNull Spinner spinner, @NonNull String value) {
        ArrayAdapter<String> adapter = AdapterUtils.createInRangeAdapter(spinner.getContext(), -2.0f, 3.0f, 0.1f);
        spinner.setAdapter(adapter);
        spinner.setSelection(adapter.getPosition(value));
        return adapter;
    }
}
