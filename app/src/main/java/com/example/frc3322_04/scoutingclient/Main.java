package com.example.frc3322_04.scoutingclient;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.support.v4.content.LocalBroadcastManager;


public class Main extends Activity {
    Button scout_button, view_summary_button;
    private BluetoothService m_BluetoothService;
    private BluetoothAdapter m_BluetoothAdapter;
    private boolean m_DiscoveringDevices;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scout_button = (Button)findViewById(R.id.scout_button);
        scout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(),Scout.class);
                startActivity(intent);
            }
        });
        view_summary_button = (Button)findViewById(R.id.view_scouting_data);
        view_summary_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(),ViewScoutingData.class);
                startActivity(intent);
            }
        });
        m_BluetoothAdapter = null;
        m_BluetoothService = null;
        m_DiscoveringDevices = false;
    }

    protected void onStart() {
        super.onStart();

        BluetoothManager m_BluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        m_BluetoothAdapter = (m_BluetoothManager == null) ? null : m_BluetoothManager.getAdapter();

       // Intent gattServiceIntent = new Intent(this, BluetoothService.class);
       // bindService(gattServiceIntent, m_ServiceConnection, BIND_AUTO_CREATE);
        m_BluetoothService = new BluetoothService(this);
        if (m_BluetoothService.initialize()) {
            configureWithBluetooth();
        }
        else {
        }

    }

    private final ServiceConnection m_ServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            m_BluetoothService = ((BluetoothService.LocalBinder) service).getService();
            if (m_BluetoothService.initialize()) {
                configureWithBluetooth();
            }
            else {
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            m_BluetoothService = null;
        }
    };


    private final BroadcastReceiver m_ServiceReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            // These are all of the intent actions the BioFit service will send.
            // You won't normally act on all of them--see DeviceInfoActivity for
            // responding to the BioFit notifiable group data.

            if (BluetoothService.ACTION_DEVICE_DISCOVERED.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothService.DEVICE);
            }
            else if (BluetoothService.ACTION_GATT_CONNECTED.equals(action)) {
            }
            else if (BluetoothService.ACTION_GATT_DISCONNECTED.equals(action)) {
            }
            else if (BluetoothService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
            }
            else if (BluetoothService.ACTION_DATA_AVAILABLE.equals(action)) {
            }
        }
    };

    @Override
    protected void onPause() {
        super.onPause();

        //stopDiscoveringDevices();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(m_ServiceReceiver);
    }

    @Override
    protected void onStop() {
        super.onStop();

//        unbindService(m_ServiceConnection);
        m_BluetoothService = null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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

    private void configureWithBluetooth() {
        enableBluetooth();
        discoverDevices();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
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
