package com.wordpress.decaf.masterminds;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by decaf on 9/28/15.
 */
public class SMSReceiver extends BroadcastReceiver {

    private static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";

    @Override
    public void onReceive(Context context, Intent intent) {
     //   RemoteController.lockScreen(context);
        if (intent.getAction().equals(ACTION)) {
            Bundle bundle = intent.getExtras();

            if (bundle != null) {

                Object pdus[] = null;
                SmsMessage smsMessage;

                if(Build.VERSION.SDK_INT >= 19) { //KITKAT
                    SmsMessage[] msgs = Telephony.Sms.Intents.getMessagesFromIntent(intent);
                    smsMessage = msgs[0];
                }
                else {
                    pdus = (Object[]) bundle.get("pdus");
                    smsMessage = SmsMessage.createFromPdu((byte[]) pdus[0]);
                }

                String phoneNumber = smsMessage.getOriginatingAddress();
                String message = smsMessage.getMessageBody();

                if (message.contains("mmind ")){
                    if (message.contains("lock")){
                        RemoteController.turnScreenOff(context);
                        Toast.makeText(context, "The screen was locked by " + phoneNumber, Toast.LENGTH_LONG).show();
                        this.abortBroadcast();
                    } else if (message.contains("vibrate")){
                        RemoteController.vibrateOn(context);
                        this.abortBroadcast();
                    }
                }
            }
        }
    }

}
