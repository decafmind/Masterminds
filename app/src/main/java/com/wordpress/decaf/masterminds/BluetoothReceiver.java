package com.wordpress.decaf.masterminds;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Created by decaf on 9/30/15.
 */
public class BluetoothReceiver extends BroadcastReceiver {

    private static final String ACTION = "android.bluetooth.adapter.action.STATE_CHANGED";
    private static final String TAG = "BluetoothReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ACTION)){
            Bundle bundle = intent.getExtras();

            if (bundle != null){

                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
                        BluetoothAdapter.ERROR);

                switch (state) {
                    case BluetoothAdapter.STATE_OFF:
                        Toast.makeText(context, "The bluetooth was turned off.", Toast.LENGTH_LONG).show();
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        Toast.makeText(context, "The bluetooth is turning off.", Toast.LENGTH_LONG).show();
                        break;
                    case BluetoothAdapter.STATE_ON:
                        BluetoothServerThread bluetoothServerThread = new BluetoothServerThread();
                        bluetoothServerThread.run();
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        Toast.makeText(context, "The bluetooth is turning on.", Toast.LENGTH_LONG).show();
                        break;
                }
            }
        }

    }
}
