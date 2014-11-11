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
import com.example.frc3322_04.scoutingclient.*;

import java.text.Normalizer;
import java.util.ArrayList;

public class Scout extends Activity {

    ScrollView scrollView;
    ArrayList<FormWidget> widgets;
    ArrayList<LinearLayout> pages;
    int currentPage;
    Button back;
    Button next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scout);
        pages = new ArrayList<LinearLayout>();
        scrollView = (ScrollView)findViewById(R.id.scouting_scroll);
        back = (Button)findViewById(R.id.back_button);
        next = (Button)findViewById(R.id.next_button);
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
    private void fixLabels() {
        if(currentPage == 0) {
            back.setVisibility(View.INVISIBLE);
        } else {
            back.setVisibility(View.VISIBLE);
        }
        if(currentPage >= pages.size() - 1) {
            next.setVisibility(View.INVISIBLE);
        } else {
            next.setVisibility(View.VISIBLE);
        }
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
