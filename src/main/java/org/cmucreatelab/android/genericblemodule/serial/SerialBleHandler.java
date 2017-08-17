package org.cmucreatelab.android.genericblemodule.serial;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.util.Log;

import org.cmucreatelab.android.genericblemodule.ble_actions.ActionCharacteristicWrite;
import org.cmucreatelab.android.genericblemodule.ble_actions.ActionWaitForNotify;
import org.cmucreatelab.android.genericblemodule.generic_ble.GenericBleDeviceConnection;
import org.cmucreatelab.android.genericblemodule.generic_ble.GenericBleScanner;
import org.cmucreatelab.android.genericblemodule.generic_ble.listeners.GenericBleCharacteristicListener;
import org.cmucreatelab.android.genericblemodule.generic_ble.listeners.GenericBleConnectionListener;
import org.cmucreatelab.android.genericblemodule.generic_ble.listeners.GenericBleDescriptorListener;
import org.cmucreatelab.android.genericblemodule.generic_ble.listeners.GenericBleServiceDiscoveryListener;

import java.util.ArrayList;

/**
 * Created by mike on 7/12/17.
 */

public class SerialBleHandler {

    private final Context appContext;
    private static final int CONNECTION_TIMEOUT = 5000; // milliseconds
    private Timer connectionTimer;

    public GenericBleScanner getScanner() {
        return scanner;
    }
    public GenericBleDeviceConnection getDeviceConnection() {
        return deviceConnection;
    }

    private static final String terminatingString = new String(new byte[]{ 0xa });

    private final GenericBleScanner scanner;
    private ActionQueue actionQueue;
    private GenericBleDeviceConnection deviceConnection;
    // listeners for BluetoothGattCallback methods (implement?)
    private final GenericBleCharacteristicListener characteristicListener = new GenericBleCharacteristicListener() {
        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            byte[] response = characteristic.getValue();
            String value = new String();
            for (byte b: response) {
                value += Integer.toHexString(b);
                value += " ";
            }
            Log.i(GenericBleDeviceConnection.LOG_TAG, "onCharacteristicChanged! value="+value);

            // notify
            if (actionQueue == null || actionQueue.getCurrentAction().getClass() != ActionWaitForNotify.class) {
                Log.e(GenericBleDeviceConnection.LOG_TAG, "onCharacteristicChanged on bad response");
                return;
            }
            ActionWaitForNotify action = ((ActionWaitForNotify) actionQueue.getCurrentAction());
            action.concatResponse(new String(response));
            if (new String(response).contains(terminatingString)) {
                Log.i(GenericBleDeviceConnection.LOG_TAG, "onCharacteristicChanged FINISHED RESPONSE!");
                actionQueue.notifyResponseReceived();
                action.notifyReceivedWithResponse();
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {

        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            Log.i(GenericBleDeviceConnection.LOG_TAG, "onCharacteristicWrite!");
            if (actionQueue == null || actionQueue.getCurrentAction().getClass() != ActionCharacteristicWrite.class) {
                Log.e(GenericBleDeviceConnection.LOG_TAG, "onCharacteristicWrite on bad response");
                return;
            }
            actionQueue.notifyResponseReceived();
        }
    };
    private final GenericBleDescriptorListener descriptorListener = new GenericBleDescriptorListener() {
        @Override
        public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {

        }

        @Override
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {

        }
    };

    public SerialBleHandler(Context appContext) {
        this.appContext = appContext;
        this.scanner = new GenericBleScanner();
    }

    public void connectDevice(BluetoothDevice device, final ConnectionListener connectionListener) {
        final GenericBleConnectionListener genericBleConnectionListener = new GenericBleConnectionListener() {
            @Override
            public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
                if (newState == BluetoothProfile.STATE_CONNECTED) gatt.discoverServices();
                else if (newState == BluetoothProfile.STATE_DISCONNECTED) connectionListener.onDisconnected();
            }
        };
        connectionTimer = new Timer(CONNECTION_TIMEOUT) {
            @Override
            public void timerExpires() {
                Log.w(GenericBleDeviceConnection.LOG_TAG, "timerExpires in SerialBleHandler.connectDevice");
                deviceConnection.disconnect();
                connectionTimer = null;
                connectionListener.onTimeout();
            }
        };
        final GenericBleServiceDiscoveryListener serviceDiscoveryListener = new GenericBleServiceDiscoveryListener() {
            @Override
            public void onServicesDiscovered(BluetoothGatt gatt, int status) {
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    if (connectionTimer != null) {
                        connectionTimer.stopTimer();
                        connectionListener.onConnected(gatt);
                    }
                } else {
                    Log.e(GenericBleDeviceConnection.LOG_TAG, "got bad status="+status+", treating as timeout");
                    if (connectionTimer != null) {
                        connectionTimer.stopTimer();
                        connectionTimer.timerExpires();
                    }
                }
            }
        };
        this.deviceConnection = new GenericBleDeviceConnection(device, this.appContext, genericBleConnectionListener, serviceDiscoveryListener, characteristicListener, descriptorListener);
        this.actionQueue = new ActionQueue(deviceConnection);
        this.deviceConnection.connect();
        connectionTimer.startTimer();
    }

    // message-sending
    public synchronized void sendMessageForResult(BluetoothGattCharacteristic bleWriteCharacteristic, BluetoothGattCharacteristic bleNotifyCharacteristic, String message, NotificationListener notificationListener) {
        if (actionQueue == null) {
            Log.e(GenericBleDeviceConnection.LOG_TAG, "sendMessageForResult but actionQueue is null.");
            return;
        }

        // split message into parts
        String temp = message.concat(terminatingString);
        ArrayList<String> packets = new ArrayList<>();
        while (temp.length() > 20) {
            packets.add( temp.substring(0,20) );
            temp = temp.substring(20);
        }
        packets.add(temp);

        for (String packet : packets) {
            Log.i(GenericBleDeviceConnection.LOG_TAG, "write action message="+packet);
            actionQueue.addAction(new ActionCharacteristicWrite(bleWriteCharacteristic, packet.getBytes()));
        }

        // send ActionWaitForNotify
        actionQueue.addAction(new ActionWaitForNotify(message, notificationListener));
    }

    // connect/disconnect
    // BLE scanning
    // +abstract methods, as needed

    public interface NotificationListener {
        void onNotificationReceived(String messageSent, String response);
    }

    public interface ConnectionListener {
        void onConnected(BluetoothGatt gatt);
        void onDisconnected();
        void onTimeout();
    }

}
