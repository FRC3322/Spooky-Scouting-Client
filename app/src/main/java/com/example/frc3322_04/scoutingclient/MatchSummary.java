package com.example.frc3322_04.scoutingclient;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class MatchSummary extends Activity {
    ListView listView;
    Button view_summary_button, scout_a_new_match_button;
    ArrayAdapter<String> arrayAdapter;
    ArrayList<Tuple<String, Serializable> > values;
    private BluetoothService m_BluetoothService;
    private BluetoothAdapter m_BluetoothAdapter;
    private boolean m_DiscoveringDevices;
    String Data[];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        m_BluetoothAdapter = null;
        m_BluetoothService = null;
        m_DiscoveringDevices = false;
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
        scout_a_new_match_button = (Button)findViewById(R.id.scout_button);
        view_summary_button = (Button)findViewById(R.id.view_scouting_data);
        view_summary_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(),ViewScoutingData.class);
                startActivity(intent);
            }
        });
        saveToFile("match "+values.get(0).toString().replaceAll("[^0-9]", ""));
    }
    public void scoutNewMatch(View view){
        Intent intent = new Intent(getBaseContext(),Scout.class);
        startActivity(intent);
    }
    @Override
    protected void onStart(){
        super.onStart();
        //Log.i("MSG");
        BluetoothManager m_BluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        m_BluetoothAdapter = (m_BluetoothManager == null) ? null : m_BluetoothManager.getAdapter();

        // Intent gattServiceIntent = new Intent(this, BluetoothService.class);
        // bindService(gattServiceIntent, m_ServiceConnection, BIND_AUTO_CREATE);
        String t1 = values.get(0).toString() + values.get(1).toString() + values.get(2).toString() + values.get(3).toString();
        String t2 = values.get(4).toString() + values.get(5).toString() + values.get(6).toString() + values.get(7).toString() + values.get(8).toString();
        String t3 = values.get(9).toString() + values.get(10).toString() + values.get(11).toString() + values.get(12).toString();
        String t4 =  values.get(13).toString();
        String[] DataArrayList = {t1,t2,t3,t4};
        m_BluetoothService = new BluetoothService(this, DataArrayList);
        if (m_BluetoothService.initialize()) {
            configureWithBluetooth();
        }
        else {
        }
    }
    private void configureWithBluetooth() {
        enableBluetooth();
        discoverDevices();

    }
    public void discoverDevices() {
        if (m_DiscoveringDevices) {
            return;
        }
        else {
            m_DiscoveringDevices = true;
            m_BluetoothService.startDiscovery();
        }
    }
    private void enableBluetooth() {
        if (m_BluetoothAdapter == null) {
        }
        else if (!m_BluetoothAdapter.isEnabled()) {
            Intent turnOnIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnOnIntent, 1);
        }
        else {
        }
    }
    public void saveToFile(String fileName) {
        File file = new File(this.getFilesDir(),fileName+".txt");
        Log.e("file", this.getFilesDir().toString());
        ObjectOutputStream objectOutputStream;
        try {
            objectOutputStream = new ObjectOutputStream(new FileOutputStream(file));
            objectOutputStream.writeObject(values.toString());
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