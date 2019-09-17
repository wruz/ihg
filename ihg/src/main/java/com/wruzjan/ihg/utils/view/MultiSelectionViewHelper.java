package com.wruzjan.ihg.utils.view;

import android.content.DialogInterface;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import java.util.Arrays;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

public class MultiSelectionViewHelper implements DialogInterface.OnMultiChoiceClickListener {

    @NonNull
    private final TextView textView;
    @NonNull
    private final String[] entries;
    @NonNull
    private final boolean[] selection;

    @Nullable
    private String preAppendedText;

    public MultiSelectionViewHelper(@NonNull TextView textView, @NonNull String[] entries) {
        this.textView = textView;
        this.entries = entries;
        this.selection = new boolean[entries.length];

        configureEditText();
    }

    @Nullable
    public String getPreAppendedText() {
        return preAppendedText;
    }

    public void setPreAppendedText(@Nullable String preAppendedText) {
        this.preAppendedText = preAppendedText;
        textView.setText(buildSelectedItemString());
    }

    public String getSelectionIndicesString() {
        StringBuilder builder = new StringBuilder();
        boolean foundAtLeastOneSelected = false;

        for (int i = 0; i < entries.length; ++i) {
            if (selection[i]) {
                if (foundAtLeastOneSelected) {
                    builder.append(",");
                }
                foundAtLeastOneSelected = true;
                builder.append(i);
            }
        }
        return builder.toString();
    }

    public void setSelection(String selectionString) {
        String[] splitComments = selectionString.split(", ");

        Arrays.fill(selection, false);

        StringBuilder preAppendedBuilder = new StringBuilder();

        for (String comment : splitComments) {
            for (int i = 0; i < entries.length; ++i) {
                if (entries[i].equalsIgnoreCase(comment)) {
                    selection[i] = true;
                } else {
                    preAppendedBuilder.append(comment);
                }
            }
        }
        preAppendedText = preAppendedBuilder.toString();
        textView.setText(buildSelectedItemString());
    }

    @Override
    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
        if (which < selection.length) {
            selection[which] = isChecked;
            textView.setText(buildSelectedItemString());
        } else {
            throw new IllegalArgumentException("Argument 'which' is out of bounds.");
        }
    }

    private void configureEditText() {
        // make EditText readonly
        textView.setFocusable(false);
        textView.setFocusableInTouchMode(false);
        textView.setInputType(InputType.TYPE_NULL);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(textView.getContext())
                        .setMultiChoiceItems(entries, selection, MultiSelectionViewHelper.this)
                        .setPositiveButton(android.R.string.ok, null)
                        .show();
            }
        });
    }

    private String buildSelectedItemString() {
        StringBuilder builder = new StringBuilder();
        boolean foundAtLeastOneSelected = false;

        if (!TextUtils.isEmpty(preAppendedText)) {
            builder.append(preAppendedText);
            foundAtLeastOneSelected = true;
        }

        for (int i = 0; i < entries.length; ++i) {
            if (selection[i]) {
                if (foundAtLeastOneSelected) {
                    builder.append(", ");
                }
                foundAtLeastOneSelected = true;
                builder.append(entries[i]);
            }
        }
        return builder.toString();
    }
}
