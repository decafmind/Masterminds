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
import android.widget.ArrayAdapter;

import java.util.Set;

public class RemoteService extends Service {

    private int mNotificationId = 555;
    private SMSReceiver mSMSreceiver;
    private IntentFilter mIntentFilter;

    public RemoteService() {

    }

    @Override
    public void onCreate(){
        super.onCreate();
        createNotification("status : on");

        mSMSreceiver = new SMSReceiver();
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");

        registerReceiver(mSMSreceiver, mIntentFilter);
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter.isEnabled()) {
            BluetoothServerThread bluetoothServerThread = new BluetoothServerThread();
            bluetoothServerThread.run();
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        createNotification("status : off");
        unregisterReceiver(mSMSreceiver);
    }


    private void createNotification(String message){
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
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
