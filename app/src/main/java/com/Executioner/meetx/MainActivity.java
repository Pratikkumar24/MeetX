package com.Executioner.meetx;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.Executioner.meetx.authentication.Authentication;
import com.Executioner.meetx.authentication.Login_page;

public class MainActivity extends AppCompatActivity {
    //Context context = getApplicationContext();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       new Handler().postDelayed(new Runnable() {
           @Override
           public void run() {
               Intent intent=new Intent(getApplicationContext(), Authentication.class);
               startActivity(intent);
               finish();
           }
       },4000);

    }
}
