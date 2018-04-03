package com.example.yevgeniyshatrovskiy.steepr.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.yevgeniyshatrovskiy.steepr.R;

import java.util.Locale;

public class Timer extends AppCompatActivity {

    private long msteepTimeInMiliseconds;
    private long mtimeLeftInMiliseconds;

    private TextView mCountDownText;

    private CountDownTimer mCountDown;

    private Button mButtonStart;
    private Button mButtonStop;

    private CountDownTimer mTimer;

    private boolean mTimerOn = false;
    private boolean mTimerOff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timer);
       Bundle bundle = getIntent().getExtras();
        msteepTimeInMiliseconds = (long) bundle.getFloat("timeToSteep") * 1000;
        Log.v(msteepTimeInMiliseconds+"", "TIMER");

        mtimeLeftInMiliseconds = msteepTimeInMiliseconds;
        Log.v(mtimeLeftInMiliseconds+"", "TIMER2");
        mCountDownText = findViewById(R.id.textViewCountdown);

        mButtonStart = findViewById(R.id.buttonStart);
        mButtonStop = findViewById(R.id.buttonStop);
        updateCountDownTime();

        mButtonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("TIMER", "ON");
                if (!mTimerOn){
                    Log.v("TIMER", "TIMER ON");
                    startTimer();
                    mTimerOn = true;
                }

            }
        });

        mButtonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("TIMER", "STOP");
                if (mTimerOn){
                    pauseTimer();
                    mTimerOn = false;
                }else{
                    resetTimer();
                }

            }
        });

    }

    private void startTimer() {
        mCountDown = new CountDownTimer(mtimeLeftInMiliseconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished){
                Log.v("TIMER", "ON TICK: " + millisUntilFinished);
                mtimeLeftInMiliseconds = millisUntilFinished;
                updateCountDownTime();

            }

            @Override
            public void onFinish(){

            }
        }.start();

        Log.v("TIMER", "STARTED");
        mTimerOn = true;
    }

    private void pauseTimer(){
        mCountDown.cancel();
        mTimerOn = false;
    }

    private void updateCountDownTime(){
        int minutes = (int) (mtimeLeftInMiliseconds / 1000) / 60;
        int seconds = (int) (mtimeLeftInMiliseconds / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(),"%02d:%02d", minutes, seconds);
        mCountDownText.setText(timeLeftFormatted);

    }

    private void resetTimer(){
        mtimeLeftInMiliseconds = msteepTimeInMiliseconds;
        updateCountDownTime();
    }

}

