package org.cmucreatelab.android.genericblemodule.ble_actions;

import android.bluetooth.BluetoothGatt;

/**
 * Created by mike on 7/11/17.
 */

public abstract class GenericBleAction {

    public abstract void doAction(BluetoothGatt gatt);

}
