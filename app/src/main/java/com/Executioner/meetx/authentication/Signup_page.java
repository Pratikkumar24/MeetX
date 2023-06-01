package com.Executioner.meetx.authentication;

import androidx.annotation.NonNull;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;
import java.util.regex.Pattern;

public class Signup_page extends AppCompatActivity {

EditText username, password, email;
Button signUp;
ImageView hide;
LinearLayout alreadyMember;
FirebaseAuth auth;
FirebaseDatabase database;
String TAG = "signup_page";
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_page);
        getSupportActionBar().hide();


        final boolean[] visibility = {false};
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
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
                    Log.i(TAG,username+"$"+password+"$"+email);
                    //putting into database
                    auth.createUserWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                String id= Objects.requireNonNull(task.getResult().getUser()).getUid();
                                DatabaseReference reference = database.getReference().child("users").child(id);
                                Users user=new Users();
                                user.setUsername(UserName);
                                user.setPassword(Password);
                                user.setEmail(Email);
                                reference.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Intent intent=new Intent(getApplicationContext(), homepage.class);
                                            startActivity(intent);
                                            finish();
                                        }else{
                                            Log.i(TAG,"Error in creating account");
                                            Toast.makeText(getApplicationContext(),"Error in creating account",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                            else{
                                Log.i(TAG,"Exception in creating user");
                                Toast.makeText(getApplicationContext(), Objects.requireNonNull(task.getException()).getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
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