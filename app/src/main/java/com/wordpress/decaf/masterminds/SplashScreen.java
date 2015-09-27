package com.wordpress.decaf.masterminds;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.EventListener;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class SplashScreen extends AppCompatActivity {

    private String[] masterMindAnagram;
    private Random random;
    private int anagramLength;
    private CountDownTimer countDownTimer = new CountDownTimer(3000, 100) {
        @Override
        public void onTick(long millisUntilFinished) {
            TextView textView = (TextView)findViewById(R.id.txtTitle);
            if (millisUntilFinished <= 200)
                textView.setText("mastermind");
            else
                textView.setText(masterMindAnagram[random.nextInt(anagramLength)]);
        }

        @Override
        public void onFinish() {
            Intent intent = new Intent(SplashScreen.this, MainActivity.class);
            startActivity(intent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        if (getSupportActionBar()!= null)
            getSupportActionBar().hide();

        masterMindAnagram = new String[]{
            "mastermind",
            "starmemind",
            "mindmestar",
            "iamahacker",
            "mansterdim",
        };

        random = new Random();
        anagramLength = masterMindAnagram.length - 1;
        countDownTimer.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_splash_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onRestart(){
        finish();
        super.onRestart();
    }
}
