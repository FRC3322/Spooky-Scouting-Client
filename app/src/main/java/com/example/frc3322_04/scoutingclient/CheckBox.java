package com.example.frc3322_04.scoutingclient;

import android.content.Context;

public class CheckBox extends FormWidget {
    android.widget.CheckBox checkBox;
    CheckBox(Context context, String labelText, String keyValue, boolean initialValue) {
        super(context, labelText, keyValue);
        checkBox = new android.widget.CheckBox(context);
        checkBox.setChecked(initialValue);
        this.addView(checkBox);
    }
    public Boolean getValue() {
        return checkBox.isChecked();
    }
}
