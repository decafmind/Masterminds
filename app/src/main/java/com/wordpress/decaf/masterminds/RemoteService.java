package com.wordpress.decaf.masterminds;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

public class RemoteService extends Service {

    private int mNotificationId = 555;

    public RemoteService() {

    }

    @Override
    public void onCreate(){
        createNotification("status : on");
    }

    @Override
    public void onDestroy(){
        createNotification("status : off");
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
