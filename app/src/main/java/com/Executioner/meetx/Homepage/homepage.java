package com.Executioner.meetx.Homepage;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.Executioner.meetx.MainActivity;
import com.Executioner.meetx.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class homepage extends AppCompatActivity {
ImageView logout;
FirebaseAuth auth;
    FirebaseDatabase database;

String TAG = "myHomepage";
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        Objects.requireNonNull(getSupportActionBar()).hide();
        init();
        //todo - Button to logout
        logout = findViewById(R.id.logout);
        logout.setOnClickListener(view -> {
            auth.signOut();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            Toast.makeText(getApplicationContext(),"You have been successfully logged out!!", Toast.LENGTH_SHORT).show();
        });
        //todo - logout

        //todo - button to sign out

        //todo - Integrating Log out and Sign out into SIDEBAR
    }
    public void init()
    {
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
    }
}