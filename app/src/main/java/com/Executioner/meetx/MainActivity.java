package com.Executioner.meetx;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.Executioner.meetx.Homepage.homepage;
import com.Executioner.meetx.authentication.Login_page;
import com.Executioner.meetx.authentication.Signup_page;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    String TAG="[+] mainActivity";
    Button login_btn;
    Button signup_btn;
    FirebaseAuth auth;
    private final int STORAGE_PERMISSION_CODE = 1;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide();

        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED)
        {
            Toast.makeText(getApplicationContext(), "Permission already given", Toast.LENGTH_SHORT).show();
        }
        else {
            requestStoragePermission();
        }

        init();

        //todo - redirect to the homepage if a user is already logged in
        if(auth.getCurrentUser() != null)
        {
            Intent intent = new Intent(getApplicationContext(), homepage.class);
            startActivity(intent);
            finish();
        }

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), Login_page.class);
                startActivity(intent);
            }
        });

        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG,"Moving to signup page");
                Intent intent = new Intent(getApplicationContext(), Signup_page.class);
                startActivity(intent);
            }
        });
    }
    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS)) {
            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed for OTP")
                    .setPositiveButton("ok", (dialog, which) -> ActivityCompat.requestPermissions(MainActivity.this,
                            new String[] {Manifest.permission.SEND_SMS}, STORAGE_PERMISSION_CODE))
                    .setNegativeButton("cancel", (dialog, which) -> dialog.dismiss())
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.SEND_SMS}, STORAGE_PERMISSION_CODE);
        }
    }
    private void init()
    {
        auth = FirebaseAuth.getInstance();
        login_btn=findViewById(R.id.Login_page);
        signup_btn = findViewById(R.id.Signup_page);

    }
}
