package com.example.frc3322_04.scoutingclient;

import android.content.Context;
import android.util.Log;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class FormPage extends LinearLayout{
    //TODO maybe it is possible to iterate over the contents of the linearlayout
    //maybe this arraylist is redundant
    ArrayList<FormWidget> widgets;
    public FormPage(Context context) {
        super(context);
        this.setOrientation(VERTICAL);
        widgets = new ArrayList<FormWidget>();
    }
    public void add(FormWidget widget) {
        widgets.add(widget);
        this.addView(widget);
    }
    public boolean isFilled() {
        for(FormWidget i: widgets) {
            if(!i.isFilled()){
                return false;
            }
        }
        return true;
    }
    public void getValues(){
        if(isFilled()) {
            for(FormWidget i: widgets) {
                Log.i("AOUT",i.getValue().toString());
            }
        } else {
            Log.i("AOUT","Form is not filled out");
        }
    }
}
