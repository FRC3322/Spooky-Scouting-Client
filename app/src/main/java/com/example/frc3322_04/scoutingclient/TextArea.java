package com.example.frc3322_04.scoutingclient;

import android.content.Context;
import android.widget.EditText;

public class TextArea extends FormWidget {
    EditText textArea;
    TextArea(Context context, String labelText, String keyValue, String initialValue) {
        super(context,labelText,keyValue);
        textArea = new EditText(context);
        textArea.setText(initialValue);
        this.addView(textArea);
    }
    //TODO: add is filled
    public String getValue() {
        return textArea.getText().toString();
    }
}
