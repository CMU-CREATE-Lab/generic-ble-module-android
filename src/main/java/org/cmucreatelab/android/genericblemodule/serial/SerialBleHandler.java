package org.cmucreatelab.android.genericblemodule.serial;

import android.bluetooth.BluetoothGattCharacteristic;

import org.cmucreatelab.android.genericblemodule.generic_ble.GenericBleDeviceConnection;
import org.cmucreatelab.android.genericblemodule.generic_ble.GenericBleScanner;
import org.cmucreatelab.android.genericblemodule.generic_ble.listeners.GenericBleCharacteristicListener;
import org.cmucreatelab.android.genericblemodule.generic_ble.listeners.GenericBleConnectionListener;
import org.cmucreatelab.android.genericblemodule.generic_ble.listeners.GenericBleDescriptorListener;
import org.cmucreatelab.android.genericblemodule.generic_ble.listeners.GenericBleServiceDiscoveryListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by mike on 7/12/17.
 */

public class SerialBleHandler {

    private GenericBleScanner scanner;
    private ActionQueue actionQueue;
    private GenericBleDeviceConnection deviceConnection;
    // listeners for BluetoothGattCallback methods (implement?)
    private GenericBleServiceDiscoveryListener serviceDiscoveryListener;
    private GenericBleConnectionListener connectionListener;
    private GenericBleCharacteristicListener characteristicListener;
    private GenericBleDescriptorListener descriptorListener;

    // a hashmap with characteristics (specific UUIDs) as keys, and the list of notification messages as values
    private HashMap<BluetoothGattCharacteristic,ArrayList<String>> characteristicResponses;
    // TODO confirm the response has terminated string, then getCurrentAction() will retrieve the message you sent and you construct the response

    // message-sending
    public synchronized void sendMessageForResult(String message, NotificationListener notificationListener) {
        // TODO split message into parts
        // TODO send write actions
        // TODO send ActionWaitForNotify
    }

    // connect/disconnect
    // BLE scanning
    // +abstract methods, as needed

    public interface NotificationListener {
        void onNotificationReceived(String messageSent, String response);
    }

}
