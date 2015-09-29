package com.wordpress.decaf.masterminds;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

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
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        createNotification("status : off");
        unregisterReceiver(mSMSreceiver);
    }


    private void createNotification(String message){
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_power_button_white)
                        .setContentTitle("mastermind server")
                        .setContentText(message);

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
