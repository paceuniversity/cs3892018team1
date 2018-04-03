package com.example.yevgeniyshatrovskiy.steepr.Activities;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.yevgeniyshatrovskiy.steepr.R;

import java.util.Locale;

public class Timer extends AppCompatActivity {

    private long msteepTimeInMiliseconds = 20000;
    private long mtimeLeftInMiliseconds = msteepTimeInMiliseconds;

    private TextView mCountDownText;

    private CountDownTimer mCountDown;

    private Button mButtonStart;
    private Button mButtonStop;

    private CountDownTimer mTimer;

    private boolean mTimerOn;
    private boolean mTimerOff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timer);

        mCountDownText = findViewById(R.id.textViewCountdown);

        mButtonStart = findViewById(R.id.buttonStart);
        mButtonStop = findViewById(R.id.buttonStop);

        mButtonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTimerOn){
                    startTimer();
                }

            }
        });

        mButtonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTimerOn){
                    pauseTimer();
                }

            }
        });

    }

    private void startTimer() {
        mCountDown = new CountDownTimer(msteepTimeInMiliseconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished){
                msteepTimeInMiliseconds = millisUntilFinished;
                updateCountDownTime();

            }

            @Override
            public void onFinish(){

            }
        }.start();

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

}

