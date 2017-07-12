package org.cmucreatelab.android.genericblemodule.generic_ble;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;

import org.cmucreatelab.android.genericblemodule.generic_ble.listeners.GenericBleCharacteristicListener;
import org.cmucreatelab.android.genericblemodule.generic_ble.listeners.GenericBleDescriptorListener;
import org.cmucreatelab.android.genericblemodule.generic_ble.listeners.GenericBleServiceDiscoveryListener;

/**
 * Created by mike on 7/12/17.
 */

public class GenericBleCallback extends BluetoothGattCallback {

    private GenericBleDeviceConnection parent;
    final private GenericBleServiceDiscoveryListener serviceDiscoveryListener;
    final private GenericBleCharacteristicListener characteristicListener;
    final private GenericBleDescriptorListener descriptorListener;

    public GenericBleCallback(GenericBleDeviceConnection parent, GenericBleServiceDiscoveryListener serviceDiscoveryListener, GenericBleCharacteristicListener characteristicListener, GenericBleDescriptorListener descriptorListener) {
        this.parent = parent;
        this.serviceDiscoveryListener = serviceDiscoveryListener;
        this.characteristicListener = characteristicListener;
        this.descriptorListener = descriptorListener;
    }

    @Override
    public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
        // handles logic in parent first, before calling other listeners
        parent.onConnectionStateChange(gatt,status,newState);
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

}
