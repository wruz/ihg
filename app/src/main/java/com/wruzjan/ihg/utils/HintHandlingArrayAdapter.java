package com.wruzjan.ihg.utils;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

import androidx.annotation.NonNull;

public class HintHandlingArrayAdapter extends ArrayAdapter<String> {

    private static final int HINT_POSITION = 0;

    public static HintHandlingArrayAdapter create(@NonNull Context context, int resource, @NonNull String[] items, String hint) {
        ArrayList<String> mutableItems = new ArrayList<>(Arrays.asList(items));
        mutableItems.add(HINT_POSITION, hint);
        HintHandlingArrayAdapter adapter = new HintHandlingArrayAdapter(context, resource, mutableItems.toArray(new String[0]));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    private HintHandlingArrayAdapter(@NonNull Context context, int resource, @NonNull String[] objects) {
        super(context, resource, objects);
    }

    @Override
    public boolean isEnabled(int position) {
        return position != HINT_POSITION;
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        View view = super.getDropDownView(position, convertView, parent);
        TextView itemTextView = (TextView) view;
        if (position == HINT_POSITION) {
            itemTextView.setTextColor(Color.GRAY);
        } else {
            itemTextView.setTextColor(Color.BLACK);
        }
        return view;
    }
}
