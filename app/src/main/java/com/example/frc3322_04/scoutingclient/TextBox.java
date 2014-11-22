package com.example.frc3322_04.scoutingclient;

import android.content.Context;
import android.widget.EditText;
import com.example.frc3322_04.scoutingclient.FormWidget;

class TextBox extends FormWidget{
    EditText textBox;
    TextBox(Context context, String labelText, String initialValue) {
        super(context, labelText);
        textBox = new EditText(context);
        if(initialValue != null) {
            textBox.setText(initialValue);
        }
        this.addView(textBox);
    }
    @Override
    public String getValue() {
        return textBox.getText().toString();
    }
    @Override
    public boolean isFilled() {
        return textBox.getText().toString() != "";
    }
}