package com.wruzjan.ihg.utils.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatEditText;

// https://github.com/r-cohen/macaddress-edittext - taken for better local maintenance
public class MacAddressEditText extends AppCompatEditText {

    String mPreviousMac = null;

    public MacAddressEditText(Context context) {
        super(context);
        init();
    }

    public MacAddressEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MacAddressEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private static String formatMacAddress(String cleanMac) {
        int grouppedCharacters = 0;
        StringBuilder formattedMacBuilder = new StringBuilder();
        for (int i = 0; i < cleanMac.length(); ++i) {
            formattedMacBuilder.append(cleanMac.charAt(i));
            ++grouppedCharacters;
            if (grouppedCharacters == 2) {
                formattedMacBuilder.append(":");
                grouppedCharacters = 0;
            }
        }
        String formattedMac = formattedMacBuilder.toString();
        if (cleanMac.length() == 12) {
            formattedMac = formattedMac.substring(0, formattedMac.length() - 1);
        }
        return formattedMac;
    }

    private static String clearNonMacCharacters(String mac) {
        return mac.replaceAll("[^A-Fa-f0-9]", "");
    }

    private static int colonCount(String formattedMac) {
        return formattedMac.replaceAll("[^:]", "").length();
    }

    private void init() {
        this.addTextChangedListener(new TextWatcher() {

            private void setMacEdit(String cleanMac, String formattedMac, int selectionStart, int lengthDiff) {
                removeTextChangedListener(this);
                if (cleanMac.length() <= 12) {
                    setText(formattedMac);
                    setSelection(selectionStart + lengthDiff);
                    mPreviousMac = formattedMac;
                } else {
                    setText(mPreviousMac);
                    setSelection(mPreviousMac.length());
                }
                addTextChangedListener(this);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence sequence, int start, int before, int count) {
                String enteredMac = sequence.toString().toUpperCase();
                String cleanMac = clearNonMacCharacters(enteredMac);
                String formattedMac = formatMacAddress(cleanMac);
                int selectionStart = getSelectionStart();
                formattedMac = handleColonDeletion(enteredMac, formattedMac, selectionStart);
                int lengthDiff = formattedMac.length() - enteredMac.length();
                setMacEdit(cleanMac, formattedMac, selectionStart, lengthDiff);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private String handleColonDeletion(String enteredMac, String formattedMac, int selectionStart) {
        if (mPreviousMac != null && mPreviousMac.length() > 1) {
            int previousColonCount = colonCount(mPreviousMac);
            int currentColonCount = colonCount(enteredMac);

            if (currentColonCount < previousColonCount) {
                formattedMac = formattedMac.substring(0, selectionStart - 1) + formattedMac.substring(selectionStart);
                String cleanMac = clearNonMacCharacters(formattedMac);
                formattedMac = formatMacAddress(cleanMac);
            }
        }
        return formattedMac;
    }
}