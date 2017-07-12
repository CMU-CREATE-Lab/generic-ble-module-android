package org.cmucreatelab.android.genericblemodule.ble_actions;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattDescriptor;
import android.util.Log;

import org.cmucreatelab.android.genericblemodule.generic_ble.GenericBleAction;
import org.cmucreatelab.android.genericblemodule.generic_ble.GenericBleDeviceConnection;

/**
 * Created by mike on 7/11/17.
 */

public class ActionDescriptorRead extends GenericBleAction {

    private BluetoothGattDescriptor descriptor;


    public ActionDescriptorRead(BluetoothGattDescriptor descriptor) {
        this.descriptor = descriptor;
    }


    @Override
    public void doAction(BluetoothGatt gatt) {
        if (gatt.readDescriptor(descriptor) == false) {
            Log.w(GenericBleDeviceConnection.LOG_TAG, "Failed to read descriptor");
        }
    }

}
