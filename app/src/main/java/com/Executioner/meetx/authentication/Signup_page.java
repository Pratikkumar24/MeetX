package com.Executioner.meetx.authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import com.Executioner.meetx.R;

public class Signup_page extends AppCompatActivity {
EditText firstName, lastName, email;
ImageView hide;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_page);
        final boolean[] visibility = {false};


        hide = findViewById(R.id.hideBtn);
        hide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!visibility[0])
                {
                    hide.setImageResource(R.drawable.unhideeye);
                }
                else {
                    hide.setImageResource(R.drawable.hide_eye);
                }
                visibility[0] = !visibility[0];
            }
        });
    }
}