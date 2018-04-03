package com.example.yevgeniyshatrovskiy.steepr.Activities;

/**
 * Created by yevgeniyshatrovskiy on 4/2/18.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.yevgeniyshatrovskiy.steepr.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean test = true;
        mAuth = FirebaseAuth.getInstance();
        if(test){
            Intent intent = new Intent(this, MainActivity.class);
            anonymousLogin(intent);
        } else {
            // Login to activity
        }


    }

    private void anonymousLogin(final Intent intent) {
        //Test
        mAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            startActivity(intent);
                        }
                    }
                });
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
