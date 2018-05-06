package com.example.yevgeniyshatrovskiy.steepr.Activities;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.provider.Settings;
import android.provider.SyncStateContract;
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

import com.example.yevgeniyshatrovskiy.steepr.NotificationPublisher;
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
    private TextView mCountDownText;

    private CountDownTimer mCountDown;

    private Button mButtonStart;
    private Button mButtonStop;

    private boolean mTimerOn = false;
    long endTime;
    private Recipe rec;
    private LinearLayout imageLayout;
    private LinearLayout entireLayout;
    private ImageView mainImage;
    private TextView textTitle;
    private TextView textDescription;
    private TextView textTemperature;
    private boolean english;
    private boolean favorite;
    private long startTime;
    private boolean firstStart;
    SharedPreferences prefs;
    private boolean used;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.timer);
        mCountDownText = findViewById(R.id.textViewCountdown);
        Log.v("TTTT", "restart");

        String jsonObject;
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            jsonObject = bundle.getString("reci");
            rec = new Gson().fromJson(jsonObject, Recipe.class);
            msteepTimeInMiliseconds = (long)bundle.getInt("time") * 1000;
            endTime = msteepTimeInMiliseconds + System.currentTimeMillis();
            favorite = (boolean)bundle.get("fav");
            Log.v("HERE", ":" + msteepTimeInMiliseconds);
            english = bundle.getBoolean("english");
        }else{
            //default 1 Minute
            Log.v("HERE", "HERE");
            msteepTimeInMiliseconds = (long)60 * 1000;
        }

        textDescription = findViewById(R.id.textDescription);
        if(english) {
            textDescription.setText(rec.getDescription());
        }else{
            textDescription.setText(rec.getChineseDescription());
        }


        textTemperature = findViewById(R.id.textTemperature);
        if(english) {
            textTemperature.setText("Steep Temperature: " + Integer.toString(rec.getTemperature()) + "°F");//To be honest I'm not sure if it's correct practice.
        }else{
            textTemperature.setText("温度: " + Integer.toString(((rec.getTemperature() + - 32)*5)/9) + "°C");
        }

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
        mCountDownText = findViewById(R.id.textViewCountdown);

        mButtonStart = findViewById(R.id.buttonStart);
        mButtonStop = findViewById(R.id.buttonStop);
        updateCountDownTime();

        mButtonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("TIMER", "CLICK");
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
        rep.setChineseCategory("喜爱"); //theoretical to get chinese favorites tab to actually say favorite - seems to work.
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference myRef = database.child("users");

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        String userID = firebaseUser.getUid();
        myRef.child(userID).child("Favorites").child(rep.getName()).setValue(rep);
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
        if(firstStart){
            Log.v("TTTT", "startTimer");
            startTime = System.currentTimeMillis();
            endTime = msteepTimeInMiliseconds + System.currentTimeMillis();
            firstStart = false;
            scheduleNotification(getNotification("Steepr"), (int)mtimeLeftInMiliseconds);
        }
        mCountDown = new CountDownTimer(mtimeLeftInMiliseconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished){
                Log.v("TIMER", "ON TICK: " + millisUntilFinished);
                mtimeLeftInMiliseconds = millisUntilFinished;
                updateCountDownTime();
                used = true;

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
            endTime = 0;
            mCountDown.cancel();
            mTimerOn = false;
            firstStart = true;
        }
        cancelNotification(getNotification("Steepr"), (int)mtimeLeftInMiliseconds);
    }



    private void updateCountDownTime(){
        int minutes = (int) (mtimeLeftInMiliseconds / 1000) / 60;
        int seconds = (int) (mtimeLeftInMiliseconds / 1000) % 60;

        Log.v("UPDATE", ":" + msteepTimeInMiliseconds + " " + System.currentTimeMillis());
        String timeLeftFormatted = String.format(Locale.getDefault(),"%02d:%02d", minutes, seconds);
        mCountDownText.setText(timeLeftFormatted);

    }

    private void resetTimer(){
        Log.v("TTTT", "reset");
        endTime = 0;
        mCountDown = null;
        mTimerOn = false;
        mtimeLeftInMiliseconds = msteepTimeInMiliseconds;
        firstStart = true;
        used = false;
        updateCountDownTime();
        prefs = getSharedPreferences("prefs" + rec.getName(), MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear().commit();

    }



    @Override
    protected void onPause() {
        super.onPause();

        prefs = getSharedPreferences("prefs" + rec.getName(), MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        if(used){
            Log.v("TTTT", "onPause");
            editor.putString("prev" + rec.getName(), "prev");
            editor.putBoolean("used" + rec.getName(), used);
            editor.putLong("remaining" + rec.getName(), mtimeLeftInMiliseconds);
            editor.putLong("millisEnd" + rec.getName(), endTime).commit();
            editor.putBoolean("first" + rec.getName(), firstStart);
            editor.putBoolean("running" + rec.getName(), mTimerOn);

            editor.commit();
            if (mCountDown != null) {
                mCountDown.cancel();
            }
        }else{
            Log.v("TTTT", "clear prefs");
            editor.clear().commit();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

//        prefs = getSharedPreferences("prefs" + rec.getName(), MODE_PRIVATE);
//        SharedPreferences.Editor editor = prefs.edit();
//
//        if(used){
//            Log.v("TTTT", "onDestroy");
//
//            editor.putString("prev", "prev");
//            editor.putBoolean("used", used);
//            editor.putLong("remaining", mtimeLeftInMiliseconds);
//            editor.putLong("millisEnd", endTime);
//            editor.putBoolean("first", firstStart);
//            editor.putBoolean("running", mTimerOn);
//
//            editor.apply();
//            if (mCountDown != null) {
//                mCountDown.cancel();
//            }
//        }else{
//            editor.clear();
//        }



    }

    @Override
    protected void onStop() {
        super.onStop();

//        prefs = getSharedPreferences("prefs" + rec.getName(), MODE_PRIVATE);
//        SharedPreferences.Editor editor = prefs.edit();
//
//        if(used){
//            Log.v("TTTT", "onStop");
//
//            editor.putString("prev", "prev");
//            editor.putBoolean("used", used);
//            editor.putLong("remaining", mtimeLeftInMiliseconds);
//            editor.putLong("millisEnd", endTime);
//            editor.putBoolean("first", firstStart);
//            editor.putBoolean("running", mTimerOn);
//
//            editor.apply();
//            if (mCountDown != null) {
//                mCountDown.cancel();
//            }
//        }else{
//            editor.clear();
//        }



    }

    @Override
    protected void onResume() {
        super.onResume();
        prefs = getSharedPreferences("prefs" + rec.getName(), MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        if(prefs.contains("first"))
            Log.v("TTTT", "CONTAINSR:");

        used = prefs.getBoolean("used" + rec.getName(), used);
        if(used){
            firstStart = prefs.getBoolean("first" + rec.getName(), firstStart);
            Log.v("TTTT", "got prefs :" + used);
            endTime = prefs.getLong("millisEnd" + rec.getName(), endTime);
            Log.v("TTTT", endTime + "-");

            if (endTime > System.currentTimeMillis()) {
                mTimerOn = prefs.getBoolean("running" + rec.getName(), mTimerOn);
                mtimeLeftInMiliseconds = endTime - System.currentTimeMillis();
                if (mTimerOn)
                    startTimer();
                updateCountDownTime();
            }else{
                mCountDownText.setText("Your tea is ready!");
//                updateCountDownTime()
            }

        }else{
            mTimerOn = false;
            firstStart = true;
            resetTimer();
            used = false;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.v("TTTT", "On Start");
        firstStart = true;


    }

    //Not my code, found example on stackexchange
    public static int lighter(int color, float factor) {
        int red = (int) ((Color.red(color) * (1 - factor) / 255 + factor) * 255);
        int green = (int) ((Color.green(color) * (1 - factor) / 255 + factor) * 255);
        int blue = (int) ((Color.blue(color) * (1 - factor) / 255 + factor) * 255);
        return Color.argb(Color.alpha(color), red, green, blue);
    }

    private void scheduleNotification(Notification notification, int delay) {

        Log.v("NOTIF", "starting notification");
        Intent notificationIntent = new Intent(this, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long futureInMillis = SystemClock.elapsedRealtime() + delay;
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
    }

    private void cancelNotification(Notification notification, int delay){

        Log.v("NOTIF", "starting notification");
        Intent notificationIntent = new Intent(this, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long futureInMillis = SystemClock.elapsedRealtime() + delay;
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

    private Notification getNotification(String content) {
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle(rec.getName() + " Finished!");
        builder.setContentText(content);
        builder.setSmallIcon(R.mipmap.ic_steepr_bg_round);
        builder.setVibrate(new long[] {0, 250, 250, 250});
        builder.setSound(alarmSound);
        return builder.build();
    }

}

