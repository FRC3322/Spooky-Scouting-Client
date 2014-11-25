package com.example.frc3322_04.scoutingclient;

import android.app.Activity;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import java.util.UUID;

public class BluetoothService {
    private BluetoothManager m_BluetoothManager;
    private BluetoothAdapter m_BluetoothAdapter;
    private BluetoothGatt m_BluetoothGatt;
    private boolean m_ServicesDiscoveredDelayed;


    public static UUID CHARACTERISTIC_UPDATE_NOTIFICATION_DESCRIPTOR_UUID = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
    public static UUID NOTIFIABLE_GROUP_VALUES_UUID = UUID.fromString("5957BE8F-C01F-4531-A529-0924398E4FE9");
    private static UUID NOTIFIABLE_GROUP_UUID = UUID.fromString("B4A265CD-2786-432D-8E92-819B9113AA10");
    public static UUID MASSAGE_LEVEL_UUID = UUID.fromString("25bfe8a4-786d-458d-a4ad-f710d4e7efc6");


    private BluetoothGattCharacteristic m_NotifiableGroupValues;
    private BluetoothGattCharacteristic m_NotifiableGroup;


    public final static String ACTION_DEVICE_DISCOVERED = "org.skylinerobotics.ACTION_DEVICE_DISCOVERED";
    public final static String ACTION_GATT_CONNECTED = "org.skylinerobotics.ACTION_GATT_CONNECTED";
    public final static String ACTION_GATT_DISCONNECTED = "org.skylinerobotics.ACTION_GATT_DISCONNECTED";
    public final static String ACTION_GATT_SERVICES_DISCOVERED = "org.skylinerobotics.ACTION_GATT_SERVICES_DISCOVERED";
    public final static String ACTION_DATA_AVAILABLE = "org.skylinerobotics.ACTION_DATA_AVAILABLE";

    public final static String DEVICE = "org.skylinerobotics.DEVICE";
    public final static String CHARACTERISTIC = "org.skylinerobotics.CHARACTERISTIC";
    public final static String VALUE = "org.skylinerobotics.VALUE";
    public final static String TIME = "org.skylinerobotics.TIME";
    public final Main activity;
    public BluetoothService(Main activity){
        this.activity = activity;
    }
    private final BluetoothGattCallback m_GattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            String intentAction;
            Log.d("Spooky","Connected to a device");
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                intentAction = ACTION_GATT_CONNECTED;
                m_BluetoothGatt.discoverServices();
            }
            else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                intentAction = ACTION_GATT_DISCONNECTED;
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                for (BluetoothGattService service : m_BluetoothGatt.getServices()) {
                    for (BluetoothGattCharacteristic characteristic : service.getCharacteristics()) {
                        Log.d("SPOOKY", String.format("found UUID %s", characteristic.getUuid().toString()));
                        if (NOTIFIABLE_GROUP_VALUES_UUID.equals(characteristic.getUuid())) {
                            m_NotifiableGroupValues = characteristic;
                        }
                        else if (NOTIFIABLE_GROUP_UUID.equals(characteristic.getUuid())) {
                            m_NotifiableGroup = characteristic;
                            Log.d("Spooky","found things");
                            m_NotifiableGroup.setValue("hi");
                            m_BluetoothGatt.writeCharacteristic(m_NotifiableGroup);
                        }
                        if(MASSAGE_LEVEL_UUID.equals(characteristic.getUuid())){
                            Log.d("SPOOKY", "FOUND THE MASSAGE");
                        }
                    }
                }

                if (m_NotifiableGroupValues != null) {
                    setCharacteristicNotification(m_NotifiableGroupValues, true);
                    m_ServicesDiscoveredDelayed = true;
                }
                else {
                }
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
            }
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            // Android does not allow simultaneous Bluetooth operations. If you
            // write to seat characteristics, you will probably have to listen for
            // the response to avoid writing another characteristic too soon.
            // See writeCharacteristic() below for another place you'll need to
            // handle write status.

            // The same problem happens with changing characteristic properties
            // such as notification status. See below for how the service delays
            // broadcasting service discovery until the descriptor write completes.
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
        }

        @Override
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            if (m_ServicesDiscoveredDelayed) {
                m_ServicesDiscoveredDelayed = false;
            }
        }
    };

    private void broadcastUpdate(final String action) {
        final Intent intent = new Intent(action);
       // LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void broadcastUpdate(final String action, final BluetoothDevice device) {
        final Intent intent = new Intent(action);
        intent.putExtra(DEVICE, device);
        //LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void broadcastUpdate(final String action, final BluetoothGattCharacteristic characteristic) {
        final Intent intent = new Intent(action);
        UUID uuid = characteristic.getUuid();
        intent.putExtra(CHARACTERISTIC, uuid);
        byte[] value = characteristic.getValue();

        if (NOTIFIABLE_GROUP_VALUES_UUID.equals(characteristic.getUuid()) && value.length >= 4) {
            intent.putExtra(TIME, characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT32, 0).intValue());

            if (value.length == 8) {
                int[] values = new int[4];
                values[0] = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, 4);
                values[1] = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, 5);
                values[2] = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, 6);
                values[3] = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, 7);
                intent.putExtra(VALUE, values);
            }

           // LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        }
    }

    public class LocalBinder extends Binder {
        BluetoothService getService() {
            return BluetoothService.this;
        }
    }

    private final IBinder mBinder = new LocalBinder();
/*
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        disconnect();
        return super.onUnbind(intent);
    }
*/
    public boolean initialize() {
        if (m_BluetoothManager == null) {
            m_BluetoothManager = (BluetoothManager) activity.getSystemService(Context.BLUETOOTH_SERVICE);
            if (m_BluetoothManager == null) {
                return false;
            }
        }

        m_BluetoothAdapter = m_BluetoothManager.getAdapter();
        if (m_BluetoothAdapter == null) {
            return false;
        }

        return true;
    }

    private BluetoothAdapter.LeScanCallback m_DiscoveryCallback;

    public void startDiscovery() {
        if (m_BluetoothAdapter.isEnabled()) {
            m_DiscoveryCallback = new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
                    Log.d("Spooky",String.format("Scan found %s,",device.getName()));
                    if("BioFit-D".equals(device.getName())){
                        m_BluetoothGatt = device.connectGatt(activity,false, m_GattCallback);
                        stopDiscovery();
                    }
                }
            };
            m_BluetoothAdapter.startLeScan(m_DiscoveryCallback);
        }
    }

    public void stopDiscovery() {
        if (m_DiscoveryCallback != null && m_BluetoothAdapter.isEnabled()) {
            m_BluetoothAdapter.stopLeScan(m_DiscoveryCallback);
            m_DiscoveryCallback = null;
        }
    }

    public boolean connect(final BluetoothDevice device) {
        m_BluetoothGatt = device.connectGatt(activity, false, m_GattCallback);
        return true;
    }

    public void disconnect() {
        if (m_BluetoothAdapter == null || m_BluetoothGatt == null) {
            return;
        }
        m_BluetoothGatt.disconnect();
        m_BluetoothGatt.close();
        m_BluetoothGatt = null;
    }

    public void subscribeToCharacteristics(int... cs) {
        if (m_BluetoothAdapter == null || m_BluetoothGatt == null) {
            return;
        }
        byte[] group = new byte[cs.length];
        int i = 0;
        for (int c : cs) {
            group[i++] = (byte)c;
        }
        writeCharacteristic(m_NotifiableGroup, group);
    }

    public void writeCharacteristic(BluetoothGattCharacteristic characteristic, byte[] value) {
        if (m_BluetoothAdapter == null || m_BluetoothGatt == null || characteristic == null) {
            return;
        }
        characteristic.setValue(value);
        if (!m_BluetoothGatt.writeCharacteristic(characteristic)) {
            // If you modify onCharacteristicWrite, you'll need failure handling here
            // so you don't wait forever for a write callback that isn't coming.
        }
    }

    public void readCharacteristic(BluetoothGattCharacteristic characteristic) {
        if (m_BluetoothAdapter == null || m_BluetoothGatt == null) {
            return;
        }
        m_BluetoothGatt.readCharacteristic(characteristic);
    }

    public void setCharacteristicNotification(BluetoothGattCharacteristic characteristic, boolean enabled) {
        if (m_BluetoothAdapter == null || m_BluetoothGatt == null) {
            return;
        }
        m_BluetoothGatt.setCharacteristicNotification(characteristic, enabled);
        BluetoothGattDescriptor descriptor = characteristic.getDescriptor(CHARACTERISTIC_UPDATE_NOTIFICATION_DESCRIPTOR_UUID);
        if (descriptor != null) {
            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            m_BluetoothGatt.writeDescriptor(descriptor);
        }
    }
}
