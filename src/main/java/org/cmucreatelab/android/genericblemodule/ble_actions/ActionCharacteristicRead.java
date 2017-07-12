package org.cmucreatelab.android.genericblemodule.ble_actions;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.util.Log;

import org.cmucreatelab.android.genericblemodule.GenericBleDeviceConnection;

/**
 * Created by mike on 7/11/17.
 */

public class ActionCharacteristicRead extends GenericBleAction {

    private BluetoothGattCharacteristic characteristic;


    public ActionCharacteristicRead(BluetoothGattCharacteristic characteristic) {
        this.characteristic = characteristic;
    }


    @Override
    public void doAction(BluetoothGatt gatt) {
        if(gatt.readCharacteristic(characteristic) == false){
            Log.w(GenericBleDeviceConnection.LOG_TAG, "Failed to read characteristic");
        }
    }

}
