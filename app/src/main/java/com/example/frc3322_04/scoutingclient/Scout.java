package com.example.frc3322_04.scoutingclient;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import com.example.frc3322_04.scoutingclient.FormWidget;

import java.text.Normalizer;
import java.util.ArrayList;

public class Scout extends Activity {

    ScrollView scrollView;
    ArrayList<FormWidget> widgets;
    ArrayList<LinearLayout> pages;
    int currentPage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scout);
        pages = new ArrayList<LinearLayout>();
        scrollView = (ScrollView)findViewById(R.id.scouting_scroll);
        Log.i("AOUT",scrollView.getContext().toString());
        LinearLayout linearLayout = new LinearLayout(scrollView.getContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        widgets = new ArrayList<FormWidget>();
        String[] opts = {"red","blue"};
        widgets.add(new TextBox(linearLayout.getContext(),"Sample",null));
        widgets.add(new OptionPicker(linearLayout.getContext(),"Choose",opts));
        for(int i = 0; i < 10; i++) {
            widgets.add(new NumberBox(linearLayout.getContext(), "Num " + String.valueOf(i), i, false, true, 1110, 0));
        }
        for(FormWidget i: widgets) {
            linearLayout.addView(i);
        }
        pages.add(linearLayout);
        pages.add(new LinearLayout(scrollView.getContext()));
        pages.get(1).setOrientation(LinearLayout.VERTICAL);
        pages.get(1).addView(new TextBox(pages.get(1).getContext(),"Sample","default"));
        scrollView.addView(pages.get(0));
        currentPage = 0;
    }
    public void getValue(View view) {
        for(FormWidget i: widgets) {
            Log.i("AOUT",i.getValue().toString());
        }
    }
    public void next(View view) {
        if(currentPage < pages.size() - 1) {
            scrollView.removeAllViews();
            currentPage++;
            scrollView.addView(pages.get(currentPage));
        }
        getValue(view);
    }
    public void back(View view) {
        if(currentPage > 0) {
            scrollView.removeAllViews();
            currentPage--;
            scrollView.addView(pages.get(currentPage));
        }
    }
    private class TextBox extends FormWidget{
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
    }
    private class NumberBox extends FormWidget {
        //TODO make sure all the validation works
        EditText textBox;
        Button inc, decr;
        int max, min;
        final boolean hMax, hMin;
        NumberBox(Context context, String labelText, int initialValue, boolean hasMax, boolean hasMin, int maximum, int minimum) {
            super(context, labelText);
            hMax = hasMax;
            hMin = hasMin;
            max = maximum;
            min = minimum;
            if(hMax && hMin && max < min) {//dont bother validating min <= max if neither matter
                int temp = max;
                max = min;
                min = temp;
            }
            if(hMin && initialValue < min) {
                //log error
                initialValue = min;
            }else if(hMax && initialValue > max) {
                //log error
                initialValue = max;
            }
            textBox = new EditText(context);
            textBox.setText(String.valueOf(initialValue));
            DigitsKeyListener dkl = new DigitsKeyListener(min < 0, false);
            textBox.setKeyListener(dkl);
            textBox.addTextChangedListener(new TextWatcher() {//make sure max and min rules are enforced
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                }
                @Override
                public void afterTextChanged(Editable editable) {
                    if(hMax && getValue() > max) {
                        textBox.setText(String.valueOf(max));
                    } else if(hMin && getValue() < min) {
                        textBox.setText(String.valueOf(min));
                    }
                }
            });
            inc = new Button(context);
            inc.setText("+");
            inc.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    int old = getValue();
                    if (!hMax || old < max) {
                        textBox.setText(String.valueOf(old+1));
                    }
                }
            });
            decr = new Button(context);
            decr.setText("-");
            decr.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    int old = getValue();
                    if (!hMin || old > min) {
                        textBox.setText(String.valueOf(old - 1));
                    }
                }
            });
            this.addView(textBox);
            this.addView(inc);
            this.addView(decr);
        }
        public Integer getValue() {
            int ret = 0;
            try {
                ret = Integer.parseInt(textBox.getText().toString());
            } catch (NumberFormatException e){}
            return ret;
        }
    }
    private class OptionPicker extends FormWidget{
        Spinner spinner;
        ArrayAdapter<String> arrayAdapter;
        OptionPicker(Context context, String labelText, String[] opts) {
            super(context,labelText);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.scout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
