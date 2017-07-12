package org.cmucreatelab.android.genericblemodule;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.util.Log;

import org.cmucreatelab.android.genericblemodule.ble_actions.GenericBleAction;

import java.util.UUID;

/**
 * Created by mike on 7/7/17.
 */

public class GenericBleDeviceConnection extends BluetoothGattCallback {

    public static final String LOG_TAG = "genericblemodule";

    private BluetoothGatt gatt;
    private boolean isConnected = false;
    private Context appContext;
    private BluetoothDevice device;

    final private GenericBleServiceDiscoveryListener serviceDiscoveryListener;
    final private GenericBleConnectionListener connectionListener;
    final private GenericBleCharacteristicListener characteristicListener;
    final private GenericBleDescriptorListener descriptorListener;

    private static final GenericBleConnectionListener defaultConnectionListener = new GenericBleConnectionListener() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            if (newState == BluetoothProfile.STATE_CONNECTED) gatt.discoverServices();
        }
    };

    public GenericBleDeviceConnection(BluetoothDevice device, Context appContext, final GenericBleConnectionListener connectionListener, final GenericBleServiceDiscoveryListener serviceDiscoveryListener, final GenericBleCharacteristicListener characteristicListener, final GenericBleDescriptorListener descriptorListener) {
        this.device = device;
        this.appContext = appContext;

        this.connectionListener = connectionListener;
        this.serviceDiscoveryListener = serviceDiscoveryListener;
        this.characteristicListener = characteristicListener;
        this.descriptorListener = descriptorListener;
        //this.gatt = device.connectGatt(appContext,false,this);
    }

    public GenericBleDeviceConnection(BluetoothDevice device, Context appContext, final GenericBleServiceDiscoveryListener serviceDiscoveryListener, final GenericBleCharacteristicListener characteristicListener, final GenericBleDescriptorListener descriptorListener) {
        this(device, appContext, defaultConnectionListener, serviceDiscoveryListener, characteristicListener, descriptorListener);
    }

    // ---- (GenericBleConnectionListener)

    @Override
    public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
        if (newState == BluetoothProfile.STATE_CONNECTED) {
            Log.i(LOG_TAG, "Connected to GATT server.");
            this.isConnected = true;
        } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
            Log.i(LOG_TAG, "Disconnected from GATT server.");
            this.isConnected = false;
        }
        connectionListener.onConnectionStateChange(gatt, status, newState);
    }

    // ---- (GenericBleServiceDiscoveryListener)

    @Override
    public void onServicesDiscovered(BluetoothGatt gatt, int status) {
        serviceDiscoveryListener.onServicesDiscovered(gatt, status);
    }

    // ---- (GenericBleCharacteristicListener)

    @Override
    public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
        characteristicListener.onCharacteristicChanged(gatt, characteristic);
    }

    @Override
    public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        characteristicListener.onCharacteristicRead(gatt, characteristic, status);
    }

    @Override
    public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        characteristicListener.onCharacteristicWrite(gatt, characteristic, status);
    }

    // ---- (GenericBleDescriptorListener)

    @Override
    public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
        descriptorListener.onDescriptorRead(gatt, descriptor, status);
    }

    @Override
    public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
        descriptorListener.onDescriptorWrite(gatt, descriptor, status);
    }

    // ----

    // ---- Class helpers

    public boolean isConnected() {
        return this.isConnected;
    }

    public void connect() {
        this.gatt = device.connectGatt(appContext,false,this);
    }

    public void disconnect() {
        // TODO double-check this is all we need (vs. disconnect, and do we need to track isConnected)
        this.gatt.close();
    }

    public BluetoothGattService getService(UUID uuid) {
        return gatt.getService(uuid);
    }

    public void send(GenericBleAction action) {
        action.doAction(this.gatt);
    }

}
