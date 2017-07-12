package org.cmucreatelab.android.genericblemodule.ble_actions;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;

import org.cmucreatelab.android.genericblemodule.generic_ble.GenericBleAction;

/**
 * Created by mike on 7/11/17.
 */

public class ActionCharacteristicSetNotification extends GenericBleAction {

    private BluetoothGattCharacteristic characteristic;
    private BluetoothGattDescriptor descriptor;
    private boolean enabled;


    public ActionCharacteristicSetNotification(BluetoothGattCharacteristic characteristic, BluetoothGattDescriptor descriptor, boolean enabled) {
        this.characteristic = characteristic;
        this.descriptor = descriptor;
        this.enabled = enabled;
    }


    @Override
    public void doAction(BluetoothGatt gatt) {
        gatt.setCharacteristicNotification(characteristic, enabled);
        descriptor.setValue(this.enabled ? BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE : BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE);
        gatt.writeDescriptor(descriptor);
    }

}
