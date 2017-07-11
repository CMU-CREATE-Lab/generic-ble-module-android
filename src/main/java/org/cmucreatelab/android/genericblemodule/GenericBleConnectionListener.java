package org.cmucreatelab.android.genericblemodule;

import android.bluetooth.BluetoothGatt;

/**
 * Created by mike on 7/7/17.
 */

public interface GenericBleConnectionListener {

    void onConnectionStateChange(BluetoothGatt gatt, int status, int newState);

}
