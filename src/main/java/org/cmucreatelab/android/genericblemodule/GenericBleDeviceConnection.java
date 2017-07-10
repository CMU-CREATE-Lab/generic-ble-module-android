package org.cmucreatelab.android.genericblemodule;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.util.Log;

/**
 * Created by mike on 7/7/17.
 */

public class GenericBleDeviceConnection extends BluetoothGattCallback {

    private static final String LOG_TAG = "genericblemodule";

    public BluetoothGatt gatt;
    final private GenericBleServiceDiscoveryListener listener;

    public GenericBleDeviceConnection(BluetoothDevice device, Context appContext, final GenericBleServiceDiscoveryListener listener) {
        this.listener = listener;
        this.gatt = device.connectGatt(appContext,false,this);
    }

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

    @Override
    public void onCharacteristicRead(BluetoothGatt gatt,
                                     BluetoothGattCharacteristic characteristic,
                                     int status) {
        if (status == BluetoothGatt.GATT_SUCCESS) {
            String value = new String(characteristic.getValue());
            Log.i(LOG_TAG, "onCharacteristicRead! value="+value);
        }
    }

    @Override
    public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        if (status == BluetoothGatt.GATT_SUCCESS) {
            Log.i(LOG_TAG, "onCharacteristicWrite!");
        }
    }

    public void writeCharacteristic(BluetoothGattCharacteristic characteristic, byte[] value) {
        // TODO check null pointer?
        characteristic.setValue(value);
        if(gatt.writeCharacteristic(characteristic) == false){
            Log.w(LOG_TAG, "Failed to write characteristic");
        }
    }

    public void readCharacteristic(BluetoothGattCharacteristic characteristic) {
        // TODO check null pointer?
        if(gatt.readCharacteristic(characteristic) == false){
            Log.w(LOG_TAG, "Failed to read characteristic");
        }
    }

    public void setCharacteristicNotification(BluetoothGattCharacteristic characteristic, BluetoothGattDescriptor descriptor, boolean enabled) {
        // TODO check null pointer?
        gatt.setCharacteristicNotification(characteristic, enabled);
        descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
        gatt.writeDescriptor(descriptor);
    }

}
