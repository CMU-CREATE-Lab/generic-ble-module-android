package org.cmucreatelab.android.genericblemodule.ble_actions;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.util.Log;

import org.cmucreatelab.android.genericblemodule.generic_ble.GenericBleAction;
import org.cmucreatelab.android.genericblemodule.generic_ble.GenericBleDeviceConnection;

/**
 * Created by mike on 7/11/17.
 */

public class ActionCharacteristicWrite extends GenericBleAction {

    private BluetoothGattCharacteristic characteristic;
    private byte[] value;


    public ActionCharacteristicWrite(BluetoothGattCharacteristic characteristic, byte[] value) {
        this.characteristic = characteristic;
        this.value = value;
    }


    @Override
    public void doAction(BluetoothGatt gatt) {
        characteristic.setValue(value);
        if(gatt.writeCharacteristic(characteristic) == false){
            Log.w(GenericBleDeviceConnection.LOG_TAG, "Failed to write characteristic");
        }
    }

}
