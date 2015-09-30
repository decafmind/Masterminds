package com.wordpress.decaf.masterminds;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.util.Set;

public class RemoteService extends Service {

    private int mNotificationId = 555;
    private SMSReceiver mSMSreceiver;
    private IntentFilter mSMSIntentFilter;
    private IntentFilter mBluetoothIntentFilter;

    private BluetoothReceiver mBluetoothReceiver;
    private static final String TAG = "RemoteService";

    public RemoteService() {

    }

    @Override
    public void onCreate(){
        super.onCreate();
        Log.d(TAG, "The service is alive");

        createNotification("status : on");

        mSMSreceiver = new SMSReceiver();
        mSMSIntentFilter = new IntentFilter();
        mSMSIntentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(mSMSreceiver, mSMSIntentFilter);

        mBluetoothReceiver = new BluetoothReceiver();
        mBluetoothIntentFilter = new IntentFilter();
        mBluetoothIntentFilter.addAction("android.bluetooth.adapter.action.STATE_CHANGED");
        registerReceiver(mBluetoothReceiver, mBluetoothIntentFilter);

        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (bluetoothAdapter != null && bluetoothAdapter.isEnabled()) {
            BluetoothServerThread bluetoothServerThread = new BluetoothServerThread();
            bluetoothServerThread.run();
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d(TAG, "The service is dead");
        createNotification("status : off");
        unregisterReceiver(mSMSreceiver);
    }


    private void createNotification(String message){
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_power_button_white)
                        .setContentTitle("mastermind server")
                        .setContentText(message)
                        .setContentIntent(pendingIntent);

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(mNotificationId, mBuilder.build());
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
