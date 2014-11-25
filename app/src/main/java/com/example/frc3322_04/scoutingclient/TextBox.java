package com.example.frc3322_04.scoutingclient;

import android.content.Context;
import android.util.Log;
import android.widget.EditText;
import com.example.frc3322_04.scoutingclient.FormWidget;

class TextBox extends FormWidget{
    EditText textBox;
    TextBox(Context context, String labelText, String keyValue, String initialValue) {
        super(context, labelText, keyValue);
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
        //FIXME THIS DOES NOT WORK YET
        Log.i("AOUT","TEXT IS\"" + textBox.getText().toString() + "\"");
        return textBox.getText().toString().trim() != "";
    }
}