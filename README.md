Generic BLE Module for Android
==============================


Overview
--------
This Android module is used to interface with BLE devices.


Add Module to Project
---------------------
A way to include the module in another Android project, clone the repository at the root of the Android project to create a ```generic-ble-module-android``` directory.

If you are using version control for the android project, another option would be to add the repository as a submodule to your Android project's repository. You can do this with ```git submodule add https://github.com/CMU-CREATE-Lab/generic-ble-module-android.git```, then add the following line to the ```.gitmodules``` file:

```
ignore = dirty
```


Modifying Gradle Files in Project
---------------------------------
To include the module in the project, modify the ```settings.gradle``` file:

```
include ':app', ':generic-ble-module-android'
```

Then, add the following to the ```app/build.gradle``` file:

```
repositories {
    flatDir {
        dirs 'libs'
        dirs project(':generic-ble-module-android').file('libs')
    }
}
...
dependencies {
    compile project(':generic-ble-module-android')
    ...
}
```

This document was last written for Android Studio version 3.0.1 using Gradle version 2.2.3.


Code Implementation
-------------------
The two main classes to use are GenericBleScanner, SerialBleHandler. GenericBleScanner allows for the app to scan for BLE devices that are in range. SerialBleHandler provides callbacks for connecting/disconnecting and sending messages to the BLE device.

### Connecting to a BLE Device ###

First, the app needs to request the Bluetooth permission.
```
genericBleScanner.needsToRequestBluetoothEnabled(this);
```

Then, the app can scan for BLE devices.
```
genericBleScanner.scanLeDevice(true, scannerCallback);
```

The scanner callback is an interface that can be defined as an anonymous inner class for what should be done when the app detects BLE devices.
```
public GenericBleScanner.ScannerCallback scannerCallback = new GenericBleScanner.ScannerCallback()
{
  @Override
  public void onScanTimerExpired()
  {
    ...
  }

  @Override
  public void onLeScan(final BluetoothDevice bluetoothDevice, int i, byte[] bytes)
  {
    ...
  }
};
```

Finally, the app can connect with any BLE device.
```
serialBleHandler.connectDevice(device, new SerialBleHandler.ConnectionListener()
{
  private boolean timedOut = false;

  @Override
  public void onConnected(BluetoothGatt gatt)
  {
    //Stop scanning
    genericBleScanner.scanLeDevice(false, scannerCallback);
    ...
  }

  @Override
  public void onDisconnected()
  {
    ...
  }

  @Override
  public void onTimeout()
  {
    ...
  }
});
```

For specific implementations of this module, please visit [the Flutter Links app](https://github.com/CMU-CREATE-Lab/flutter-app-android) and [the Honeybee Application](https://github.com/CMU-CREATE-Lab/honeybee-application).
