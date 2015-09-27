package com.wordpress.decaf.masterminds;


import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private boolean isOn = false;
    private ImageButton powerButton;
    private RelativeLayout relativeLayout;
    private TextView textView;

    private Intent serviceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        powerButton = (ImageButton)findViewById(R.id.iBtnPower);
        relativeLayout = (RelativeLayout)findViewById(R.id.mainLayout);
        textView = (TextView)findViewById(R.id.lblIndicator);
        serviceIntent = new Intent(MainActivity.this, RemoteService.class);
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

        final Drawable green = ContextCompat.getDrawable(this, R.drawable.power_button_green);
        final Drawable white = ContextCompat.getDrawable(this, R.drawable.power_button_white);


        String message;

        if (isOn)
            message = "Switching off";
        else
            message = "Switching on";

        final ProgressDialog progressDialog = ProgressDialog.show(this, "Loading", message);

        CountDownTimer countDownTimer = new CountDownTimer(500, 500) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {

                if (isOn){
                    powerButton.setImageDrawable(white);
                    relativeLayout.setBackgroundColor(Color.rgb(85, 85, 85));
                    textView.setText("Off");
                    textView.setTextColor(Color.WHITE);
                    isOn = false;

                    stopService(serviceIntent);

                }else{
                    powerButton.setImageDrawable(green);
                    relativeLayout.setBackgroundColor(Color.WHITE);
                    textView.setText("On");
                    textView.setTextColor(Color.GREEN);
                    isOn = true;

                    startService(serviceIntent);

                }

                progressDialog.dismiss();
            }
        };

        countDownTimer.start();
    }

    private void toast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

}
