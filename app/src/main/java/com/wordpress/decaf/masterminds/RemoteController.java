package com.wordpress.decaf.masterminds;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.os.PowerManager;
import android.os.Vibrator;
import android.widget.Toast;

/**
 * Created by decaf on 9/28/15.
 */
public class RemoteController {

    public RemoteController(){

    }


    public static void vibrateOn(final Context context){
        Vibrator vibrator = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);

        if(Build.VERSION.SDK_INT >= 11) {
            if (vibrator.hasVibrator())
                vibrator.vibrate(2000);
            else
                Toast.makeText(context, "This device has no vibrator", Toast.LENGTH_SHORT).show();
        }else{
            try {
                vibrator.vibrate(2000);
            }catch (Exception ex){
                Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

    }

    public static void turnScreenOff(final Context context) {
        DevicePolicyManager policyManager = (DevicePolicyManager) context
                .getSystemService(Context.DEVICE_POLICY_SERVICE);
        ComponentName adminReceiver = new ComponentName(context,
                ScreenOffAdminReceiver.class);
        boolean admin = policyManager.isAdminActive(adminReceiver);
        if (admin) {
            policyManager.lockNow();
        } else {
            Toast.makeText(context, "Admin device not enables",
                    Toast.LENGTH_LONG).show();
        }
    }


}
