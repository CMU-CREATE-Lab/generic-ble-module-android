package org.cmucreatelab.android.genericblemodule.generic_ble;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

/**
 * Created by mike on 7/6/17.
 */

public class GenericBleScanner {

    private BluetoothAdapter bluetoothAdapter;
    private Handler handler;
    private boolean isScanning;

    private static final int REQUEST_ENABLE_BT = 0;
    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 10000;


    private void getBluetoothAdapter(Activity activity) {
        final BluetoothManager bluetoothManager = (BluetoothManager) activity.getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();
    }


    public GenericBleScanner() {
        this.handler = new Handler();
        this.isScanning = false;
    }


    public boolean isScanning() {
        return isScanning;
    }


    // ASSERT: the application has sufficient permissions at this point to enable bluetooth; see ContextCompat.checkSelfPermission and ActivityCompat.requestPermissions
    // NOTE: api level 23 and above requires Manifest.permission.ACCESS_FINE_LOCATION
    public boolean needsToRequestBluetoothEnabled(Activity activity) {
        getBluetoothAdapter(activity);
        // Ensures Bluetooth is available on the device and it is enabled. If not,
        // displays a dialog requesting user permission to enable Bluetooth.
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            return true;
        }
        return false;
    }


    public void scanLeDevice(final boolean enable, final ScannerCallback scannerCallback) {
        if (bluetoothAdapter == null) {
            Log.e(GenericBleDeviceConnection.LOG_TAG, "Tried to scanLeDevice before needsToRequestBluetoothEnabled");
            return;
        }
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    isScanning = false;
                    bluetoothAdapter.stopLeScan(scannerCallback);
                    scannerCallback.onScanTimerExpired();
                }
            }, SCAN_PERIOD);
            isScanning = true;
            bluetoothAdapter.startLeScan(scannerCallback);
        } else {
            isScanning = false;
            bluetoothAdapter.stopLeScan(scannerCallback);
        }
    }


    public interface ScannerCallback extends BluetoothAdapter.LeScanCallback {
        void onScanTimerExpired();
    }

}
