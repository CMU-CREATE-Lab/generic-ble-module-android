package org.cmucreatelab.android.genericblemodule.ble_actions;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattDescriptor;
import android.util.Log;

import org.cmucreatelab.android.genericblemodule.GenericBleDeviceConnection;

/**
 * Created by mike on 7/11/17.
 */

public class ActionDescriptorWrite extends GenericBleAction {

    private BluetoothGattDescriptor descriptor;
    private byte[] value;


    public ActionDescriptorWrite(BluetoothGattDescriptor descriptor, byte[] value) {
        this.descriptor = descriptor;
        this.value = value;
    }


    @Override
    public void doAction(BluetoothGatt gatt) {
        descriptor.setValue(value);
        if (gatt.writeDescriptor(descriptor) == false) {
            Log.w(GenericBleDeviceConnection.LOG_TAG, "Failed to write descriptor");
        }
    }

}
