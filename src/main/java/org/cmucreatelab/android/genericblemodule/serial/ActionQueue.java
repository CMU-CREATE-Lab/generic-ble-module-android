package org.cmucreatelab.android.genericblemodule.serial;

import org.cmucreatelab.android.genericblemodule.generic_ble.GenericBleAction;

/**
 * Created by mike on 7/12/17.
 */

public abstract class ActionQueue {

    private GenericBleAction currentAction;

    public ActionQueue() {
        // TODO instantiate timer, queue
        // TODO connect SerialBleHandler objects
    }

    /**
     * Clear the queue
     */
    public void clear() {

    }

    /**
     * add action to queue
     * @param action instance of GenericBleAction to be added to the queue
     */
    public void addAction(GenericBleAction action) {

    }

    /**
     * Get instance of the action we are waiting for.
     * @return null if there is no action we are waiting for
     */
    public GenericBleAction getCurrentAction() {
        return this.currentAction;
    }

    private synchronized void nextAction() {
        // TODO poll next action and timer to timeout
    }

    public synchronized void notifyResponseReceived() {
        // TODO confirm we got the right response for the current action
        // TODO send next action
    }

}
