package com.wruzjan.ihg.utils;

import android.app.Activity;
import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Locale;

import androidx.annotation.ArrayRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

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

    @SuppressWarnings("unchecked")
    public static void setItemToSpinner(@NonNull Spinner spinner, String value) {
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinner.getAdapter();
        int position = adapter.getPosition(value);
        spinner.setSelection(position);
    }

    public static void setItemToSpinner(@NonNull Spinner spinner, @NonNull ArrayAdapter<String> adapter, float value) {
        int selectedItemIndex = adapter.getPosition(String.format(Locale.US, "%.1f", value));
        spinner.setSelection(selectedItemIndex);
    }

    public static void setupSpinnerWithHint(@NonNull final Activity activity, @NonNull final Spinner spinner, @ArrayRes int itemsArrayRes, @StringRes int hintStringRes) {
        spinner.setAdapter(HintHandlingArrayAdapter.create(
                activity,
                android.R.layout.simple_spinner_item,
                activity.getResources().getStringArray(itemsArrayRes),
                activity.getResources().getString(hintStringRes)
        ));
    }

    public static boolean isHintSelected(@NonNull final Spinner spinner) {
        return spinner.getSelectedItemPosition() == 0;
    }

    @NonNull
    public static ArrayAdapter<String> createAdapterAndAssignToSpinner(@NonNull Spinner spinner, @NonNull String value) {
        ArrayAdapter<String> adapter = AdapterUtils.createInRangeAdapter(spinner.getContext(), -2.0f, 3.0f, 0.1f);
        spinner.setAdapter(adapter);
        spinner.setSelection(adapter.getPosition(value));
        return adapter;
    }
}
