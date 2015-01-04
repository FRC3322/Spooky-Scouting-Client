package com.example.frc3322_04.scoutingclient;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;


public class ViewScoutingData extends Activity {

    ListView listview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_scouting_data);
        File[] files = this.getFilesDir().listFiles();
        ArrayList<String> data = new ArrayList<String>();
        for(File f : files) {
            Log.i("AOUT", f.getName());
            data.add(f.getName().replace(".txt",""));
        }
        final File directory = this.getFilesDir();
        Log.i("AOUT", directory.toString());
        listview = (ListView)findViewById(R.id.localDataListView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,data);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String item = ((TextView)view).getText().toString();
                File file = new File(directory,item + ".txt");
                ObjectInputStream objectInputStream;
                String temp;
                try {
                    objectInputStream = new ObjectInputStream(new FileInputStream(file));
                    temp = (String)objectInputStream.readObject();
                    objectInputStream.close();
                    Log.i("AOUT: form data ", temp);
                } catch(Exception e) {
                    e.printStackTrace();
                }
                //TODO parse data into a ArrayList<Tuple<String, Serializable> > to send to MatchSummary
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.view_scouting_data, menu);
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
