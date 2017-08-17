package org.cmucreatelab.android.genericblemodule.serial;

import android.util.Log;

import org.cmucreatelab.android.genericblemodule.generic_ble.GenericBleAction;
import org.cmucreatelab.android.genericblemodule.generic_ble.GenericBleDeviceConnection;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by mike on 7/12/17.
 */

public class ActionQueue {

    private GenericBleAction currentAction;
    private GenericBleDeviceConnection deviceConnection;
    private final Timer queueTimeout;
    private final ConcurrentLinkedQueue<GenericBleAction> actions;
    private static final int TIMEOUT_IN_MILLISECONDS = 5000;
    private boolean isWaitingForResponse = false;

    public ActionQueue(final GenericBleDeviceConnection deviceConnection) {
        this.deviceConnection = deviceConnection;
        queueTimeout = new Timer(TIMEOUT_IN_MILLISECONDS) {
            @Override
            public void timerExpires() {
                Log.e(GenericBleDeviceConnection.LOG_TAG, "ActionQueue.timerExpires");
                ActionQueue.this.deviceConnection.disconnect();
            }
        };
        actions = new ConcurrentLinkedQueue<>();
    }

    /**
     * Clear the actions
     */
    public void clear() {
        actions.clear();
        this.currentAction = null;
        isWaitingForResponse = false;
    }

    /**
     * add action to actions
     * @param action instance of GenericBleAction to be added to the actions
     */
    public void addAction(GenericBleAction action) {
        actions.add(action);
        nextAction();
    }

    /**
     * Get instance of the action we are waiting for.
     * @return null if there is no action we are waiting for
     */
    public GenericBleAction getCurrentAction() {
        return this.currentAction;
    }

    private synchronized void nextAction() {
        if (!isWaitingForResponse) {
            if (actions.isEmpty()) {
                this.currentAction = null;
            } else {
                this.currentAction = actions.poll();
                isWaitingForResponse = true;
                deviceConnection.send(currentAction);
                queueTimeout.startTimer();
            }
        }
    }

    public synchronized void notifyResponseReceived() {
        queueTimeout.stopTimer();
        if (isWaitingForResponse) {
            isWaitingForResponse = false;
            nextAction();
        }
    }

}
