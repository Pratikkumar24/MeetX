package com.Executioner.meetx.authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.LinearLayout;
import android.widget.Toast;
//import com.Executioner.meetx.authentication.helper;
import com.Executioner.meetx.Homepage.homepage;
import com.Executioner.meetx.R;

import java.util.regex.Pattern;

public class Signup_page extends AppCompatActivity {

EditText username, password, email;
Button signUp;
ImageView hide;
LinearLayout alreadyMember;
String TAG = "pratik";
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_page);
        final boolean[] visibility = {false};
        alreadyMember = findViewById(R.id.alreadyMemberLogin);
        hide = findViewById(R.id.hideBtn);
        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        signUp = findViewById(R.id.signup_btn);
        password = findViewById(R.id.password);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(confirmInput())
                {
                    String UserName = username.getText().toString();
                    String Password = password.getText().toString();
                    String Email = email.getText().toString();
                    Intent intent=new Intent(getApplicationContext(), homepage.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
        hide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!visibility[0])
                {
                    hide.setImageResource(R.drawable.unhideeye);
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else {
                    hide.setImageResource(R.drawable.hide_eye);
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                visibility[0] = !visibility[0];
            }
        });
        alreadyMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), Login_page.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public boolean confirmInput() {
        if (!helper.validateEmail(email) | !helper.validateUsername(username) | !helper.validatePassword(password)) {
            return false;
        }
        return true;
    }
}