package com.example.frc3322_04.scoutingclient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;

public class Scout extends Activity {

    LinearLayout container;
    ArrayList<FormPage> formPages;
    int currentPage;
    Button back;
    Button next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scout_teleop);
        container = (LinearLayout)findViewById(R.id.scouting_container);
        back = (Button)findViewById(R.id.back_button);
        next = (Button)findViewById(R.id.next_button);
        formPages = new ArrayList<FormPage>();
        FormPage p1 = new FormPage(container.getContext());
        p1.add(new NumberBox(p1.getContext(),"Match Number","matchNum",0,false,true,true,1000, 0, NumberBox.DisplayMode.NUMBERPAD_ONLY));
        String[] opts = {"red","blue"};
        p1.add(new OptionPicker(p1.getContext(),"Choose team color","Optpick",opts));
        p1.add(new MapCordinatePicker(p1.getContext(),"autonPossition"));
        p1.add(new CheckBox(p1.getContext(),"Auton move","autonMove",false));
        Toast.makeText(this,"Press the BACK key to hide the numpad and continue in the app!", Toast.LENGTH_SHORT).show();
        formPages.add(p1);
        FormPage p2 = new FormPage(container.getContext());
        p2.add(new NumberBox(p1.getContext(),"Possesion","possesion",0,true,false,true,100, 0, NumberBox.DisplayMode.BUTTONS_ONLY));
        p2.add(new NumberBox(p1.getContext(),"Truss throw success","trussThrowSuccess",0,true,false,true,0, 0, NumberBox.DisplayMode.BUTTONS_ONLY));
        p2.add(new NumberBox(p1.getContext(),"Truss throw miss","trussThrowMiss",0,true,false,true,100, 0, NumberBox.DisplayMode.BUTTONS_ONLY));
        p2.add(new NumberBox(p1.getContext(),"Truss catch","trussCatch",0,true,false,true,100, 0, NumberBox.DisplayMode.BUTTONS_ONLY));
        p2.add(new NumberBox(p1.getContext(),"Teleop hit","teleopHit",0,true,false,true,100, 0, NumberBox.DisplayMode.BUTTONS_ONLY));
        p2.add(new NumberBox(p1.getContext(),"Teleop miss","teleopMiss",0,true,false,true,100, 0, NumberBox.DisplayMode.BUTTONS_ONLY));
        p2.add(new NumberBox(p1.getContext(),"Inbounds","inbounds",0,true,false,true,100, 0, NumberBox.DisplayMode.BUTTONS_ONLY));
        p2.add(new NumberBox(p1.getContext(),"Drops","drops",0,true,false,true,100, 0, NumberBox.DisplayMode.BOTH));
        formPages.add(p2);
        FormPage p3 = new FormPage(container.getContext());
        p3.add(new CheckBox(p3.getContext(),"Fail","fail",false));
        p3.add(new TextArea(p3.getContext(),"Notes","notes",null));
        formPages.add(p3);
        if(formPages.size() > 0)
            container.addView(formPages.get(0));
        currentPage = 0;
        fixLabels();
    }
    private void fixLabels() {
        if(currentPage == 0) {
            back.setVisibility(View.INVISIBLE);
        } else {
            back.setVisibility(View.VISIBLE);
        }
        if(currentPage >= formPages.size() - 1) {
            next.setText("Submit");
        } else {
            next.setText("Next");
        }
    }
    public void next(View view) {
        if(currentPage < formPages.size() - 1) {
            container.removeAllViews();
            currentPage++;
            container.addView(formPages.get(currentPage));
        }
        else {
            ArrayList<Tuple<String, Serializable> > values = new ArrayList<Tuple<String, Serializable> >();
            for(FormPage page : formPages) {
                values.addAll(page.getValues());
            }
            Intent intent = new Intent(getBaseContext(), MatchSummary.class);
            intent.putExtra("FORM_CONTENTS", values);
            startActivity(intent);
        }
        fixLabels();
    }
    public void back(View view) {
        if(currentPage > 0) {
            container.removeAllViews();
            currentPage--;
            container.addView(formPages.get(currentPage));
            fixLabels();
        }
    }
    @Override
    public void onBackPressed(){
        if(currentPage > 0) {
            container.removeAllViews();
            currentPage--;
            container.addView(formPages.get(currentPage));
            fixLabels();
        } else {
            super.onBackPressed();
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
