package org.cmucreatelab.android.genericblemodule.generic_ble;

import android.bluetooth.BluetoothGatt;

/**
 * Created by mike on 7/11/17.
 */

public abstract class GenericBleAction {

    /**
     * Called by GenericBleDeviceConnection.send to perform the class-specific action.
     *
     * @param gatt instance of BluetoothGatt from GenericBleDeviceConnection
     */
    public abstract void doAction(BluetoothGatt gatt);

}
