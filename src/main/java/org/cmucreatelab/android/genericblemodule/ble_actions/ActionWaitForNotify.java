package org.cmucreatelab.android.genericblemodule.ble_actions;

import android.bluetooth.BluetoothGatt;

import org.cmucreatelab.android.genericblemodule.generic_ble.GenericBleAction;
import org.cmucreatelab.android.genericblemodule.serial.SerialBleHandler;

/**
 * Created by mike on 7/14/17.
 */

public class ActionWaitForNotify extends GenericBleAction {

    private String message;
    private String response;
    private SerialBleHandler.NotificationListener notificationListener;

    public ActionWaitForNotify(String message, SerialBleHandler.NotificationListener notificationListener) {
        this.message = message;
        this.notificationListener = notificationListener;
        this.response = new String();
    }

    public void concatResponse(String value) {
        this.response = response.concat(value);
    }

    @Override
    public void doAction(BluetoothGatt gatt) {
        // does nothing
    }

    public void notifyReceivedWithResponse() {
        // remove terminating character
        String trimmedResponse = response.substring(0,response.length()-1);
        notificationListener.onNotificationReceived(message,trimmedResponse);
    }

}
