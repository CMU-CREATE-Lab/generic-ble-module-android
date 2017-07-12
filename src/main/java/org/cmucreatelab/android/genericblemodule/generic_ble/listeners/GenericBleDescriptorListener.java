package org.cmucreatelab.android.genericblemodule.generic_ble.listeners;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;

/**
 * Created by mike on 7/7/17.
 */

public interface GenericBleDescriptorListener {

    void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status);

    void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status);

}
