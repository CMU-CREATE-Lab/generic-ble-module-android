package org.cmucreatelab.android.genericblemodule.generic_ble;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by mike on 7/6/17.
 */

public class GenericBleScanner {

    private BluetoothAdapter mBluetoothAdapter;

    private int REQUEST_ENABLE_BT;
    private boolean mScanning;
    private Handler mHandler;

    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 10000;


    public GenericBleScanner() {
        this.mHandler = new Handler();
    }

    private void getBluetoothAdapter(Activity activity) {
        // Initializes Bluetooth adapter.
        final BluetoothManager bluetoothManager =
                (BluetoothManager) activity.getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
    }


    // ASSERT: the application has sufficient permissions at this point to enable bluetooth; see ContextCompat.checkSelfPermission and ActivityCompat.requestPermissions
    // NOTE: api level 23 and above requires Manifest.permission.ACCESS_FINE_LOCATION
    public void enableBluetooth(Activity activity) {
        getBluetoothAdapter(activity);
        // Ensures Bluetooth is available on the device and it is enabled. If not,
        // displays a dialog requesting user permission to enable Bluetooth.
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }

    public void scanLeDevice(final boolean enable, final BluetoothAdapter.LeScanCallback mLeScanCallback) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                }
            }, SCAN_PERIOD);

            mScanning = true;
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
    }

}
