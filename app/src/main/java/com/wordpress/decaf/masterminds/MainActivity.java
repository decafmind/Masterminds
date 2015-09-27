package com.wordpress.decaf.masterminds;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private boolean isOn = false;
    private ImageButton powerButton;
    private RelativeLayout relativeLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        powerButton = (ImageButton)findViewById(R.id.iBtnPower);
        relativeLayout = (RelativeLayout)findViewById(R.id.mainLayout);
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

        Drawable black = ContextCompat.getDrawable(this, R.drawable.power_button_black);
        Drawable white = ContextCompat.getDrawable(this, R.drawable.power_button_white);

        if (isOn){
            isOn = false;
            powerButton.setImageDrawable(white);
            relativeLayout.setBackgroundColor(Color.rgb(85, 85, 85));
        }else{
            isOn = true;
            powerButton.setImageDrawable(black);
            relativeLayout.setBackgroundColor(Color.WHITE);
        }
    }

    private void toast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }


}
