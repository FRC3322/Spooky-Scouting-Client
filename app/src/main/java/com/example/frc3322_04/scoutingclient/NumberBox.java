package com.example.frc3322_04.scoutingclient;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

class NumberBox extends FormWidget {
    //TODO make sure all the validation works
    EditText textBox;
    Button inc, decr;
    int max, min;
    final boolean hMax, hMin;
    public enum DisplayMode {
        BUTTONS_ONLY, NUMBERPAD_ONLY, BOTH
    }
    DisplayMode displayMode;
    NumberBox(Context context, String labelText, String keyValue, int initialValue, boolean hasMax, boolean hasMin, int maximum, int minimum, DisplayMode mode) {
        super(context, labelText, keyValue);
        displayMode = mode;
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
        /*textBox.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {

                }
            }
        });*/
        textBox.addTextChangedListener(new TextWatcher() {//make sure max and min rules are enforced
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                if(textBox.getText().toString().isEmpty()) {
                    return;
                }else if (hMax && getValue() > max) {
                    textBox.setText(String.valueOf(max));
                } else if (hMin && getValue() < min) {
                    textBox.setText(String.valueOf(min));
                }
            }
        });
        this.addView(textBox);
        if(mode != DisplayMode.NUMBERPAD_ONLY) {
            RelativeLayout container = new RelativeLayout(context);
            LinearLayout buttonContainer = new LinearLayout(container.getContext());
            inc = new Button(buttonContainer.getContext());
            inc.setText("+");
            inc.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    int old = getValue();
                    if (!hMax || old < max) {
                        textBox.setText(String.valueOf(old + 1));
                    }
                }
            });
            decr = new Button(buttonContainer.getContext());
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
            buttonContainer.addView(inc);
            buttonContainer.addView(decr);
            container.addView(buttonContainer);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)buttonContainer.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            container.setLayoutParams(params);
            this.addView(container);
        }
        if(mode != DisplayMode.NUMBERPAD_ONLY) {
            inc.requestFocus();
        }
    }
    public Integer getValue() {
        int ret = 0;
        try {
            ret = Integer.parseInt(textBox.getText().toString());
        } catch (NumberFormatException e){}
        return ret;
    }
}
