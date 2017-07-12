package org.cmucreatelab.android.genericblemodule.generic_ble;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.util.Log;

import org.cmucreatelab.android.genericblemodule.generic_ble.listeners.GenericBleCharacteristicListener;
import org.cmucreatelab.android.genericblemodule.generic_ble.listeners.GenericBleConnectionListener;
import org.cmucreatelab.android.genericblemodule.generic_ble.listeners.GenericBleDescriptorListener;
import org.cmucreatelab.android.genericblemodule.generic_ble.listeners.GenericBleServiceDiscoveryListener;

import java.util.UUID;

/**
 * Created by mike on 7/7/17.
 */

public class GenericBleDeviceConnection {

    private BluetoothGatt gatt;
    private boolean isConnected = false;
    private Context appContext;
    private BluetoothDevice device;
    // listeners for BluetoothGattCallback methods
    final private GenericBleServiceDiscoveryListener serviceDiscoveryListener;
    final private GenericBleConnectionListener connectionListener;
    final private GenericBleCharacteristicListener characteristicListener;
    final private GenericBleDescriptorListener descriptorListener;

    public static final String LOG_TAG = "genericblemodule";

    private static final GenericBleConnectionListener defaultConnectionListener = new GenericBleConnectionListener() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            if (newState == BluetoothProfile.STATE_CONNECTED) gatt.discoverServices();
        }
    };


    /**
     * Create a new instance of GenericBleDeviceConnection.
     *
     * @param device the ble device that is to be connected
     * @param appContext instance of the application context
     * @param connectionListener BluetoothGattCallback methods related to the connection status
     * @param serviceDiscoveryListener BluetoothGattCallback methods related to the service discovery
     * @param characteristicListener BluetoothGattCallback methods related to characteristics
     * @param descriptorListener BluetoothGattCallback methods related to descriptors
     */
    public GenericBleDeviceConnection(BluetoothDevice device, Context appContext, final GenericBleConnectionListener connectionListener, final GenericBleServiceDiscoveryListener serviceDiscoveryListener, final GenericBleCharacteristicListener characteristicListener, final GenericBleDescriptorListener descriptorListener) {
        this.device = device;
        this.appContext = appContext;

        this.connectionListener = connectionListener;
        this.serviceDiscoveryListener = serviceDiscoveryListener;
        this.characteristicListener = characteristicListener;
        this.descriptorListener = descriptorListener;
    }


    /**
     * Create a new instance of GenericBleDeviceConnection using the default GenericBleConnectionListener.
     *
     * @param device the ble device that is to be connected
     * @param appContext instance of the application context
     * @param serviceDiscoveryListener BluetoothGattCallback methods related to the service discovery
     * @param characteristicListener BluetoothGattCallback methods related to characteristics
     * @param descriptorListener BluetoothGattCallback methods related to descriptors
     */
    public GenericBleDeviceConnection(BluetoothDevice device, Context appContext, final GenericBleServiceDiscoveryListener serviceDiscoveryListener, final GenericBleCharacteristicListener characteristicListener, final GenericBleDescriptorListener descriptorListener) {
        this(device, appContext, defaultConnectionListener, serviceDiscoveryListener, characteristicListener, descriptorListener);
    }


    /**
     * Called when the connection state changes (see BluetoothGattCallback).
     *
     * @param gatt (see BluetoothGattCallback)
     * @param status (see BluetoothGattCallback)
     * @param newState (see BluetoothGattCallback)
     */
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


    /**
     * Check the current state of the connection.
     *
     * @return Returns true if the BLE device has been connected and has not disconnected yet; false otherwise.
     */
    public boolean isConnected() {
        return this.isConnected;
    }


    /**
     * Connect to BLE device.
     */
    public void connect() {
        GenericBleCallback callback = new GenericBleCallback(this, serviceDiscoveryListener, characteristicListener, descriptorListener);
        this.gatt = device.connectGatt(appContext,false,callback);
    }


    /**
     * Disconnect from the BLE device.
     */
    public void disconnect() {
        // TODO double-check this is all we need (vs. disconnect, and do we need to track isConnected)
        this.gatt.close();
    }


    /**
     * Get the GATT service with specific UUID.
     *
     * @param uuid The UUID of the service being requested
     * @return (see BluetoothGatt.getService)
     */
    public BluetoothGattService getService(UUID uuid) {
        return gatt.getService(uuid);
    }


    /**
     * Send information to the BLE device.
     *
     * @param action The BLE action you wish to perform (e.g. write characteristc, read descriptor, etc.).
     */
    public void send(GenericBleAction action) {
        action.doAction(this.gatt);
    }

}
