package com.Executioner.meetx.Homepage;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.Executioner.meetx.R;

public class homepage extends AppCompatActivity {
ImageView logout;
String TAG = "myHomepage";
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        getSupportActionBar().hide();
        Log.i(TAG,"[+] homepage");
        //todo - Button to logout
        logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getApplicationContext()," Logout button is working... ", Toast.LENGTH_SHORT).show();
            }
        });
        //todo - button to sign out

        //todo - Integrating Log out and Sign out into SIDEBAR
    }
}