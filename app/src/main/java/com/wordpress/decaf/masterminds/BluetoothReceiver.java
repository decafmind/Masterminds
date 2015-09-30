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
    private BluetoothServerThread bluetoothServerThread;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ACTION)){
            Bundle bundle = intent.getExtras();

            if (bundle != null){

                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
                        BluetoothAdapter.ERROR);

                switch (state) {
                    case BluetoothAdapter.STATE_OFF:
                        Toast.makeText(context, "The bluetooth was turned off.", Toast.LENGTH_SHORT).show();
                        break;
                    case BluetoothAdapter.STATE_ON:
                        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                        if (bluetoothAdapter != null){
                            startBluetoothListening();
                        }
                        break;
                }
            }
        }

    }

    public synchronized void startBluetoothListening(){
        if (bluetoothServerThread == null) {
            bluetoothServerThread = new BluetoothServerThread();
            bluetoothServerThread.start();
        }
    }

}
