package com.wordpress.decaf.masterminds;


import android.app.ProgressDialog;
import android.app.admin.DevicePolicyManager;
import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_ENABLE_BT = 9;
    private static final int REQUEST_CODE_ENABLE_ADMIN = 47;

    private boolean isOn = false;
    private ImageButton powerButton;
    private RelativeLayout relativeLayout;
    private TextView textView;

    private Intent serviceIntent;
    private static final String PREF_NAME = "server_status";
    private BluetoothAdapter bluetoothAdapter;
    private Drawable green, white;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        green = ContextCompat.getDrawable(this, R.drawable.power_button_green);
        white = ContextCompat.getDrawable(this, R.drawable.power_button_white);

        powerButton = (ImageButton)findViewById(R.id.iBtnPower);
        relativeLayout = (RelativeLayout)findViewById(R.id.mainLayout);
        textView = (TextView)findViewById(R.id.lblIndicator);
        serviceIntent = new Intent(MainActivity.this, RemoteService.class);
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences settings = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        isOn = settings.getBoolean("isOn", false);
        setStatus(isOn);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }

    public void togglePower(View view){

        String message;

        if (isOn) message = "Switching off";
        else message = "Switching on";

        final ProgressDialog progressDialog = ProgressDialog.show(this, "Loading", message);

        CountDownTimer countDownTimer = new CountDownTimer(500, 500) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                if (isOn){
                    setStatus(false);   // turn off since it is one
                }else{
                    setStatus(true);    // turn on since it is off
                }
                progressDialog.dismiss();
                writeServerStatusPreference();
            }
        };
        countDownTimer.start();
    }

    private void setStatus(boolean on){
        isOn = on;
        Log.d(TAG, "isOn = " + on);

        if(on){
            powerButton.setImageDrawable(green);
            relativeLayout.setBackgroundColor(Color.WHITE);
            textView.setText("On");
            textView.setTextColor(Color.GREEN);

            askToBeAdmin(); // request for admin permission

            startService(serviceIntent);
        }else{
            powerButton.setImageDrawable(white);
            relativeLayout.setBackgroundColor(Color.rgb(85, 85, 85));
            textView.setText("Off");
            textView.setTextColor(Color.WHITE);

            stopService(serviceIntent); // stop the service
        }
    }

    private boolean askToActivateBluetooth(){

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }else{
            return true;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK){
            startService(serviceIntent);    // start the service
        }
    }

    private void askToBeAdmin(){

        DevicePolicyManager devicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        ComponentName deviceAdminComponentName = new ComponentName(MainActivity.this, ScreenOffAdminReceiver.class);
        boolean isActive = devicePolicyManager.isAdminActive(deviceAdminComponentName);

        if (!isActive){
            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, deviceAdminComponentName);
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                    "You must enable device administration for certain features of the app to function."
            );
            startActivityForResult(intent, REQUEST_CODE_ENABLE_ADMIN);
        }

    }

    private void writeServerStatusPreference(){
        SharedPreferences settings = getSharedPreferences(PREF_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("isOn", isOn);
        editor.commit();    // commit the edits
    }

}
