package com.example.yevgeniyshatrovskiy.steepr.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.yevgeniyshatrovskiy.steepr.Objects.Recipe;
import com.example.yevgeniyshatrovskiy.steepr.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.util.Locale;

public class Timer extends AppCompatActivity {

    private long msteepTimeInMiliseconds;
    private long mtimeLeftInMiliseconds;
    private long mEndTime;

    private TextView mCountDownText;

    private CountDownTimer mCountDown;

    private Button mButtonStart;
    private Button mButtonStop;

    private boolean mTimerOn = false;
    private boolean mTimerOff;
    private Recipe rec;
    private LinearLayout imageLayout;
    private LinearLayout entireLayout;
    private ImageView mainImage;
    private TextView textTitle;
    private TextView textDescription;
    private TextView textTemperature;
    private boolean english;
    private boolean favorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timer);
        mCountDownText = findViewById(R.id.textViewCountdown);

        String jsonObject;
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            jsonObject = bundle.getString("reci");
            rec = new Gson().fromJson(jsonObject, Recipe.class);
            msteepTimeInMiliseconds = (long)bundle.getInt("time") * 1000;
            favorite = (boolean)bundle.get("fav");
            Log.v("HERE", ":" + msteepTimeInMiliseconds);
            english = bundle.getBoolean("english");
        }else{
            //default 1 Minute
            Log.v("HERE", "HERE");
            msteepTimeInMiliseconds = (long)60 * 1000;
        }

        textDescription = findViewById(R.id.textDescription);
        textDescription.setText(rec.getDescription());

        textTemperature = findViewById(R.id.textTemperature);
        textTemperature.setText("Steep Temperature: " + Integer.toString(rec.getTemperature()) + "°F"); //To be honest I'm not sure if it's correct practice.


        textTitle = findViewById(R.id.textTitle);
        mButtonStart = findViewById(R.id.buttonStart);
        mButtonStop = findViewById(R.id.buttonStop);
        if(english){
            textTitle.setText(rec.getName());
            mButtonStart.setText("Start");
            mButtonStop.setText("Stop");
        }else{
            textTitle.setText(rec.getChineseName());
            mButtonStart.setText("开始");
            mButtonStop.setText("停止");
        }

        imageLayout = findViewById(R.id.topLayout);
        imageLayout.setBackgroundColor(Color.parseColor(rec.getBackGroundColor()));
        entireLayout = findViewById(R.id.entireLayout);

        int color1 = Color.parseColor((rec.getTextColor()));
        int color2 = lighter(color1, 1);
        int[] colors = {color1,color2};

        GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, colors);
//        entireLayout.setBackgroundColor(Color.parseColor(rec.getTextColor()));
        entireLayout.setBackground(gradientDrawable);

        mainImage = findViewById(R.id.mainImage);
        int draws = getResources().getIdentifier(rec.getBackGroundImage()
                , "drawable"
                , getPackageName());

        Drawable draw = getDrawable(draws);
        mainImage.setImageDrawable(draw);

        mtimeLeftInMiliseconds = msteepTimeInMiliseconds;
        updateCountDownTime();
        Log.v(mtimeLeftInMiliseconds+"", "TIMER2");
//        mCountDownText = findViewById(R.id.textViewCountdown);

        mButtonStart = findViewById(R.id.buttonStart);
        mButtonStop = findViewById(R.id.buttonStop);
//        updateCountDownTime();

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
                if(msteepTimeInMiliseconds != mtimeLeftInMiliseconds){
                    if (mTimerOn){
                        if(english){
                            mButtonStop.setText("Reset");
                        }else
                            mButtonStop.setText("重启");
                        pauseTimer();
                        mTimerOn = false;
                    }else{
                        resetTimer();
                        if(english)
                            mButtonStop.setText("Stop");
                        else
                            mButtonStop.setText("停止");
                    }
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        this.invalidateOptionsMenu();
        super.onBackPressed();
    }

    public void addFavorite(Recipe rep){
        rep.setCategory("Favorite");
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference myRef = database.child("users");

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        String userID = firebaseUser.getUid();
        myRef.child(userID).child("Favorites").child(rep.getName()).setValue(rep);

////                Recipe rep = new Recipe("Awesome", "Weird", 60, null,
////                        "greentea","Favorite", "#ffffff","greentea",
////                        "#BD8300","茶","茶");
////                myRef.child(userID).child("Favorites").child(rep.getName()).removeValue();
//
////                Log.v("REC:",  teaCategories.get(position).getRecipes().get(position);
        this.invalidateOptionsMenu();
    }

    public void removeFavorite(Recipe rep){
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference myRef = database.child("users");

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        String userID = firebaseUser.getUid();
        myRef.child(userID).child("Favorites").child(rep.getName()).removeValue();
        this.invalidateOptionsMenu();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (favorite) {
            removeFavorite(rec);
            favorite = false;
            return true;
        }else{
            Log.v("ID", "No Heart");
            addFavorite(rec);
            favorite = true;
            return true;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.time, menu);
        if(favorite){
            menu.getItem(0).setIcon(R.drawable.heart);
        }else{
            menu.getItem(0).setIcon(R.drawable.emptyheart);
        }
        return true;
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
                mCountDownText.setText("Your tea is ready!");
            }
        }.start();

        Log.v("TIMER", "STARTED");
        mTimerOn = true;
    }

    private void pauseTimer(){
        if (mTimerOn) {
            mCountDown.cancel();
            mTimerOn = false;
        }
    }



    private void updateCountDownTime(){
        int minutes = (int) (mtimeLeftInMiliseconds / 1000) / 60;
        int seconds = (int) (mtimeLeftInMiliseconds / 1000) % 60;

        Log.v("UPDATE", ":" + msteepTimeInMiliseconds);
        String timeLeftFormatted = String.format(Locale.getDefault(),"%02d:%02d", minutes, seconds);
        mCountDownText.setText(timeLeftFormatted);

    }

    private void resetTimer(){
        mCountDown.cancel();
        mTimerOn = false;
        mtimeLeftInMiliseconds = msteepTimeInMiliseconds;
        updateCountDownTime();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("millisLeft", mtimeLeftInMiliseconds);
        outState.putBoolean("timerRunning", mTimerOn);
        outState.putLong("endTime", mEndTime);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

//        mtimeLeftInMiliseconds = savedInstanceState.getLong("millisLeft");
//        mTimerOn = savedInstanceState.getBoolean("timerRunning");
//        updateCountDownTime();
//
//        if (mTimerOn) {
//            mEndTime = savedInstanceState.getLong("endTime");
//            mtimeLeftInMiliseconds = mEndTime - System.currentTimeMillis();
//            startTimer();
//        }
    }



    @Override
    protected void onPause() {
        super.onPause();

//        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
//        SharedPreferences.Editor editor = prefs.edit();
//
//        editor.putLong("millisLeft", mtimeLeftInMiliseconds);
//        editor.putBoolean("timerRunning", mTimerOff);
//        editor.putLong("endTime", mEndTime);
//
//        editor.apply();
//
//        if (mCountDown != null) {
//            mCountDown.cancel();
//        }

    }

    @Override
    protected void onResume() {
        super.onResume();

//        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
//
//        mtimeLeftInMiliseconds = prefs.getLong("millisLeft", msteepTimeInMiliseconds);
//        Log.v("SAVED", ":" + msteepTimeInMiliseconds);
//        mTimerOn = prefs.getBoolean("timerRunning", false);
//
//        updateCountDownTime();
//
//        if (mTimerOn) {
//            mEndTime = prefs.getLong("endTime", 0);
//            mtimeLeftInMiliseconds = mEndTime - System.currentTimeMillis();
//
//            if (mtimeLeftInMiliseconds < 0) {
//                mtimeLeftInMiliseconds = 0;
//                mTimerOn = false;
//            } else {
//                startTimer();
//            }
//        }
    }

    @Override
    protected void onStart() {
        super.onStart();

//        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
//
//        mtimeLeftInMiliseconds = prefs.getLong("millisLeft", msteepTimeInMiliseconds);
//        mTimerOn = prefs.getBoolean("timerRunning", false);
//
//        updateCountDownTime();
//
//        if (mTimerOn) {
//            mEndTime = prefs.getLong("endTime", 0);
//            mtimeLeftInMiliseconds = mEndTime - System.currentTimeMillis();
//
//            if (mtimeLeftInMiliseconds < 0) {
//                mtimeLeftInMiliseconds = 0;
//                mTimerOn = false;
//            } else {
//                startTimer();
//            }
//        }
    }

    //Not my code, found example on stackexchange
    public static int lighter(int color, float factor) {
        int red = (int) ((Color.red(color) * (1 - factor) / 255 + factor) * 255);
        int green = (int) ((Color.green(color) * (1 - factor) / 255 + factor) * 255);
        int blue = (int) ((Color.blue(color) * (1 - factor) / 255 + factor) * 255);
        return Color.argb(Color.alpha(color), red, green, blue);
    }

}

