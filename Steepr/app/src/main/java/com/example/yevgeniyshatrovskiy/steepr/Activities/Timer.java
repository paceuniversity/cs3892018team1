package com.example.yevgeniyshatrovskiy.steepr.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.yevgeniyshatrovskiy.steepr.Objects.Recipe;
import com.example.yevgeniyshatrovskiy.steepr.R;
import com.google.gson.Gson;

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
    private Recipe rec;
    private LinearLayout imageLayout;
    private LinearLayout entireLayout;
    private ImageView mainImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);





        setContentView(R.layout.timer);

        String jsonObject;
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            jsonObject = bundle.getString("reci");
            rec = new Gson().fromJson(jsonObject, Recipe.class);
            msteepTimeInMiliseconds = (long)rec.getSecondsToSteep() * 1000;
        }else{
            //default 1 Minute
            msteepTimeInMiliseconds = (long)60 * 1000;
        }
        imageLayout = findViewById(R.id.topLayout);
        imageLayout.setBackgroundColor(Color.parseColor(rec.getBackGroundColor()));
        entireLayout = findViewById(R.id.entireLayout);
        entireLayout.setBackgroundColor(Color.parseColor(rec.getTextColor()));

        mainImage = findViewById(R.id.mainImage);
        int draws = getResources().getIdentifier(rec.getBackGroundImage()
                , "drawable"
                , getPackageName());

        Drawable draw = getDrawable(draws);
        mainImage.setImageDrawable(draw);

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
                    mButtonStop.setText("Reset");
                    pauseTimer();
                    mTimerOn = false;
                }else{
                    resetTimer();
                    mButtonStop.setText("Stop");
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

    @Override
    protected void onPause() {
        super.onPause();
    }
}

