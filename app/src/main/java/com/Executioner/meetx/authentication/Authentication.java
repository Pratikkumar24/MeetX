package com.Executioner.meetx.authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.Executioner.meetx.R;

public class Authentication extends AppCompatActivity {
Button login_btn;
Button signup_btn;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
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
                Intent intent = new Intent(getApplicationContext(), Signup_page.class);
                startActivity(intent);
            }
        });
    }
}