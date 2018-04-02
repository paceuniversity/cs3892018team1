package com.example.yevgeniyshatrovskiy.steepr.Activities;

/**
 * Created by yevgeniyshatrovskiy on 4/2/18.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.yevgeniyshatrovskiy.steepr.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean test = true;
        if(test){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else {
            // Login to activity
        }

        finish();
    }
}
