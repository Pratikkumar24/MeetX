package com.Executioner.meetx;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.Executioner.meetx.Homepage.homepage;
import com.Executioner.meetx.authentication.Login_page;
import com.Executioner.meetx.authentication.Signup_page;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    String TAG="mainActivity";
    Button login_btn;
    Button signup_btn;
    FirebaseAuth auth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();
        getSupportActionBar().hide();

        //todo - redirect to the homepage if a user is already logged in

        login_btn=findViewById(R.id.Login_page);
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), Login_page.class);
                startActivity(intent);
            }
        });

        signup_btn = findViewById(R.id.Signup_page);
        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG,"Moving to signup page");
                Intent intent = new Intent(getApplicationContext(), Signup_page.class);
                startActivity(intent);
            }
        });
    }
}
