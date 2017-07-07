package org.cmucreatelab.android.genericblemodule;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothProfile;
import android.content.Intent;
import android.util.Log;

import java.util.UUID;

/**
 * Created by mike on 7/7/17.
 */

public class GenericBleDeviceConnection {

    private static final String LOG_TAG = "genericblemodule";

    public BluetoothGatt gatt;

    private GenericBleDeviceConnection(BluetoothGatt gatt) {
        this.gatt = gatt;
    }

    public static GenericBleDeviceConnection connectDevice(BluetoothDevice device, final Activity activity, final GenericBleServiceListener listener) {
        BluetoothGatt gatt = device.connectGatt(activity.getApplicationContext(), false, new BluetoothGattCallback() {

            @Override
            public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
                super.onConnectionStateChange(gatt, status, newState);
                if (newState == BluetoothProfile.STATE_CONNECTED) {
                    Log.i(LOG_TAG, "Connected to GATT server.");
                    gatt.discoverServices();
                } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                    Log.i(LOG_TAG, "Disconnected from GATT server.");
                }
            }

            @Override
            public void onServicesDiscovered(BluetoothGatt gatt, int status) {
                listener.onServicesDiscovered(gatt, status);
            }

            @Override
            public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
                super.onCharacteristicChanged(gatt, characteristic);
                String value = new String(characteristic.getValue());
                Log.i(LOG_TAG, "onCharacteristicChanged! value="+value);
            }

        });

        return new GenericBleDeviceConnection(gatt);
    }

}
