package com.Executioner.meetx.authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.Executioner.meetx.Homepage.homepage;
import com.Executioner.meetx.R;

public class Login_page extends AppCompatActivity {
    String TAG="NEHA";
Button login;
EditText username,password;
ImageView hideBtn;
LinearLayout accountSignUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        accountSignUp=findViewById(R.id.accountSignUp);
        username=findViewById(R.id.login_username);
        password=findViewById(R.id.login_password);
        hideBtn=findViewById(R.id.eye_icon);
        login=findViewById(R.id.login_btn);
        final boolean[] visibility = {false};
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(confirmInput()){
                    String UserName = username.getText().toString();
                    String Password = password.getText().toString();
                    Log.i(TAG," UserName: "+ UserName +" Password:" + Password);
                    Intent intent=new Intent(getApplicationContext(), homepage.class);
                    startActivity(intent);
                }
            }
        });
        accountSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), Signup_page.class);
                startActivity(intent);
            }
        });
        hideBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!visibility[0])
                {
                    hideBtn.setImageResource(R.drawable.unhideeye);
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else {
                    hideBtn.setImageResource(R.drawable.hide_eye);
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                visibility[0] = !visibility[0];
            }
        });
    }
    public boolean confirmInput() {
        if (!helper.validateUsername(username) | !helper.validatePassword(password)) {
            return false;
        }
        return true;
    }
}