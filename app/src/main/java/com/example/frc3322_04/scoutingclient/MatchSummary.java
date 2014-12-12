package com.example.frc3322_04.scoutingclient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class MatchSummary extends Activity {
    ListView listView;
    Button view_summary_button;
    ArrayAdapter<String> arrayAdapter;
    ArrayList<Tuple<String, Serializable> > values;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_summary);
        values = (ArrayList<Tuple<String, Serializable> >)getIntent().getSerializableExtra("FORM_CONTENTS");
        listView = (ListView)findViewById(R.id.match_summary);
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        listView.setAdapter(arrayAdapter);
        Log.i("AOUT", values.toString());
        for(Tuple<String, Serializable> i: values) {
            arrayAdapter.add(i.x + ": " + i.y.toString());
        }
        view_summary_button = (Button)findViewById(R.id.view_scouting_data);
        view_summary_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(),ViewScoutingData.class);
                startActivity(intent);
            }
        });
        saveToFile("match1");
    }
    public void saveToFile(String fileName) {
        File file = new File(this.getFilesDir(),fileName);
        ObjectOutputStream objectOutputStream;
        try {
            objectOutputStream = new ObjectOutputStream(new FileOutputStream(file));
            objectOutputStream.writeObject(values);
            objectOutputStream.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.match_summary, menu);
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