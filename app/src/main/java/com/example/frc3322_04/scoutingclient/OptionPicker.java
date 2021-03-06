package com.example.frc3322_04.scoutingclient;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

class OptionPicker extends FormWidget{
    Spinner spinner;
    ArrayAdapter<String> arrayAdapter;
    OptionPicker(Context context, String labelText, String keyValue, String[] opts) {
        super(context,labelText, keyValue);
        spinner = new Spinner(context);
        arrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, opts);
        spinner.setAdapter(arrayAdapter);
        this.addView(spinner);
    }
    @Override
    public String getValue() {
        return spinner.getSelectedItem().toString();
    }
}