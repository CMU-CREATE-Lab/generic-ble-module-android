package org.cmucreatelab.android.genericblemodule;

import android.bluetooth.BluetoothGatt;

/**
 * Created by mike on 7/7/17.
 */

public interface GenericBleServiceDiscoveryListener {

    void onServicesDiscovered(BluetoothGatt gatt, int status);

}
