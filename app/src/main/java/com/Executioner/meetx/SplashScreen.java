package com.Executioner.meetx;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.Executioner.meetx.Homepage.homepage;
import com.google.firebase.auth.FirebaseAuth;

public class SplashScreen extends AppCompatActivity {
    private final String TAG = "splash_screen[+]";
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getSupportActionBar().hide();
        init();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // todo - if user already logged in before
                //  then redirect to homepage else main(authentication page)
                if(auth.getCurrentUser() != null)
                {
                    Intent intent = new Intent(getApplicationContext(), homepage.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        },3000);
    }
    public void init()
    {
        auth = FirebaseAuth.getInstance();
    }
}