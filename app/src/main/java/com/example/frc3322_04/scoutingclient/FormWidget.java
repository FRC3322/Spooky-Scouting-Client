package com.example.frc3322_04.scoutingclient;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.Serializable;

public class FormWidget extends LinearLayout {
    TextView label;
    String type;    //listing types in code is not nice
    String key;
    FormWidget(Context context, String labelText, String keyValue) {
        super(context);
        label = new TextView(context);
        label.setText(labelText);
        key = keyValue;
        this.addView(label);
    }
    FormWidget(Context context) {
        this(context,"Give this a label","Give this a value");
    }
    public Serializable getValue() {
        return null;
    }
    public boolean isFilled() {
        return true;
    }
}
