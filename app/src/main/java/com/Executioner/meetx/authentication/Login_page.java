package com.Executioner.meetx.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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
import android.widget.Toast;

import com.Executioner.meetx.Homepage.homepage;
import com.Executioner.meetx.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class Login_page extends AppCompatActivity {
    String TAG="login_page";
Button login;
EditText email,password;
ImageView hideBtn;
FirebaseAuth auth;

LinearLayout accountSignUp;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        accountSignUp=findViewById(R.id.accountSignUp);
        email=findViewById(R.id.login_email);
        password=findViewById(R.id.login_password);
        hideBtn=findViewById(R.id.eye_icon);
        login=findViewById(R.id.login_btn);
        final boolean[] visibility = {false};


        auth = FirebaseAuth.getInstance();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(confirmInput()){
                    String Email = email.getText().toString();
                    String Password = password.getText().toString();
                    auth.signInWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {

                                Intent intent = new Intent(getApplicationContext(), homepage.class);
                                startActivity(intent);
                                finish();

                            }
                            else {
                                Toast.makeText(Login_page.this, Objects.requireNonNull(task.getException()).getMessage() , Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
        accountSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), Signup_page.class);
                startActivity(intent);
                finish();
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
        if (!helper.validateEmail(email) | !helper.validatePassword(password)) {
            return false;
        }
        return true;
    }
}