package com.example.frc3322_04.scoutingclient;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FormWidget extends LinearLayout {
    TextView label;
    String type;
    FormWidget(Context context, String labelText) {
        super(context);
        label = new TextView(context);
        label.setText(labelText);
        this.addView(label);
    }
    FormWidget(Context context) {
        this(context,"Give this a label");
    }
    public Object getValue() {
        return null;
    }
}
