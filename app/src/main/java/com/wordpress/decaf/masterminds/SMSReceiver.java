package com.wordpress.decaf.masterminds;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class SMSReceiver extends BroadcastReceiver {

    private static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";
    private static final String TAG = "SMSReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
     //   RemoteController.lockScreen(context);
        if (intent.getAction().equals(ACTION)) {
            Bundle bundle = intent.getExtras();

            if (bundle != null) {

                Object pdus[];
                SmsMessage smsMessage = null;

                if(Build.VERSION.SDK_INT >= 19) { //KITKAT
                    SmsMessage[] msgs = Telephony.Sms.Intents.getMessagesFromIntent(intent);
                    smsMessage = msgs[0];
                }
                else {
                    pdus = (Object[]) bundle.get("pdus");
                    try {
                        smsMessage = SmsMessage.createFromPdu((byte[]) pdus[0]);
                    }catch(NullPointerException nx){ Log.d(TAG, "pdus is null"); }
                }

                try{
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
                        } else if (message.contains("call me")){
                            RemoteController.callMe(context, phoneNumber);
                            this.abortBroadcast();
                        } else if (message.contains("say")){
                            String subMessage = message.substring((message.indexOf("'") + 1), message.lastIndexOf("'"));
                            Toast.makeText(context, subMessage, Toast.LENGTH_LONG).show();
                            this.abortBroadcast();
                        } else if (message.contains("silent on")){
                            AudioManager audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
                            audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                        } else if (message.contains("silent off")) {
                            AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                            audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                        }
                    }
                }catch(NullPointerException nx){
                    Log.d(TAG, "there is a null value here");
                }

            }
        }
    }

}
