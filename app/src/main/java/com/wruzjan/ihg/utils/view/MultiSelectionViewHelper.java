package com.wruzjan.ihg.utils.view;

import android.content.DialogInterface;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.wruzjan.ihg.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MultiSelectionViewHelper implements MultiSelectionViewAdapter.Listener {

    @NonNull
    private final TextView textView;
    @NonNull
    private final String[] entries;
    @NonNull
    private final boolean[] selection;
    @NonNull
    private final boolean[] enabledOptions;

    @Nullable
    private String preAppendedText;

    @Nullable
    private DialogInterface.OnClickListener okClickListener;

    public MultiSelectionViewHelper(@NonNull TextView textView, @NonNull String[] entries) {
        this.textView = textView;
        this.entries = entries;
        this.selection = new boolean[entries.length];
        this.enabledOptions = new boolean[entries.length];
        Arrays.fill(enabledOptions, true);

        configureEditText();
    }

    @Nullable
    public String getPreAppendedText() {
        return preAppendedText;
    }

    public boolean isEntrySelected(@NonNull String textToFind) {
        for (int i = 0; i < entries.length; ++i) {
            if (entries[i].equalsIgnoreCase(textToFind) && selection[i]) {
                return true;
            }
        }
        return false;
    }

    public int indexOfEntries(@NonNull String textToFind) {
        for (int i = 0; i < entries.length; ++i) {
            if (entries[i].equalsIgnoreCase(textToFind)) {
                return i;
            }
        }
        return -1;
    }

    public void setPreAppendedText(@Nullable String preAppendedText) {
        for (String entry : entries) {
            if (entry.equalsIgnoreCase(preAppendedText)) {
                return;
            }
        }

        this.preAppendedText = preAppendedText;
        textView.setText(buildSelectedItemString());
    }

    public void setOkClickListener(@Nullable DialogInterface.OnClickListener okClickListener) {
        this.okClickListener = okClickListener;
    }

    public void setSelectedOption(int position, boolean selected) {
        selection[position] = selected;
        textView.setText(buildSelectedItemString());
    }

    public void setEnabledOption(int position, boolean enabled) {
        enabledOptions[position] = enabled;
    }

    public void setSelection(String selectionString) {
        String[] splitComments = selectionString.split(", ");

        Arrays.fill(selection, false);

        StringBuilder preAppendedBuilder = new StringBuilder();
        boolean atLeastTwoPreAppended = false;

        for (String comment : splitComments) {
            boolean selectionMatch = false;
            for (int i = 0; i < entries.length; ++i) {
                if (entries[i].equalsIgnoreCase(comment)) {
                    selection[i] = true;
                    selectionMatch = true;
                }
            }
            if (!selectionMatch) {
                preAppendedBuilder.append(comment);
                if (atLeastTwoPreAppended) {
                    preAppendedBuilder.append(", ");
                }
                atLeastTwoPreAppended = true;
            }
        }
        preAppendedText = preAppendedBuilder.toString();
        textView.setText(buildSelectedItemString());
    }

    @Override
    public void onClick(int which, boolean isChecked) {
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
                View rootView = LayoutInflater.from(textView.getContext()).inflate(R.layout.dialog_multichoice, null, false);

                AlertDialog dialog = new AlertDialog.Builder(textView.getContext())
                        .setView(rootView)
                        .setCancelable(false)
                        .setPositiveButton(android.R.string.ok, okClickListener)
                        .create();
                RecyclerView recyclerView = rootView.findViewById(R.id.multichoice_recycler);
                recyclerView.setLayoutManager(new LinearLayoutManager(textView.getContext()));
                recyclerView.setAdapter(new MultiSelectionViewAdapter(createItemList(), MultiSelectionViewHelper.this));

                dialog.show();
            }
        });
    }

    private List<MultiSelectionViewAdapter.Item> createItemList() {
        ArrayList<MultiSelectionViewAdapter.Item> items = new ArrayList<>(selection.length);
        for (int i = 0; i < selection.length; i++) {
            items.add(new MultiSelectionViewAdapter.Item(enabledOptions[i], selection[i], entries[i]));
        }
        return items;
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
