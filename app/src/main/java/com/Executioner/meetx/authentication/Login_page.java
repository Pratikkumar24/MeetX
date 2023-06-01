package com.Executioner.meetx.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.Executioner.meetx.Homepage.homepage;
import com.Executioner.meetx.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class Login_page extends AppCompatActivity {
    String TAG = "login_page";
    Button login, getOTP;
    EditText email, password;
    ImageView hideBtn;
    FirebaseAuth auth;
    CheckBox remember_box;
    EditText otpEmail;
    TextView forgot_password;
    LinearLayout accountSignUp;
    SharedPreferences sharedPreferences;
    AlertDialog.Builder OTP_generator;
    ArrayList<Users> UserArrayList;
    private DatabaseReference mDatabase;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        Objects.requireNonNull(getSupportActionBar()).hide();

        init();
        final boolean[] visibility = {false};
        //storing data of the sharedpreference file into sharedpreference object
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String remember_password = sharedPreferences.getString(Constants.PASSWORD, Constants.NULL);
        String remember_email = sharedPreferences.getString(Constants.EMAIL, Constants.NULL);

        //todo- if clicked on forgot password -> redirect to a page -> enter a email and OTP -> verify OTP
        forgot_password.setOnClickListener(view -> {
            OTP_generator = new AlertDialog.Builder(Login_page.this);
            View mView = getLayoutInflater().inflate(R.layout.forgotpassworddialogbox, null);
            OTP_generator.setCancelable(false);
            OTP_generator.setView(mView);
            AlertDialog alertDialog = OTP_generator.create();

            Button close = mView.findViewById(R.id.button_close);
            close.setOnClickListener(v -> alertDialog.dismiss());
            getOTP = mView.findViewById(R.id.send_otp);
            otpEmail = mView.findViewById(R.id.OTP_email);
            getOTP.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (helper.validateEmail(otpEmail)) {
                        DatabaseReference refs = mDatabase.child(Constants.USERS);
                        refs.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                //getting all ids of users
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    Users idDetails = dataSnapshot.getValue(Users.class);
                                    UserArrayList.add(idDetails);
                                }
                                for(Users user:UserArrayList){
                                    Log.i(TAG,"Email: "+user.getEmail());
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }
            });
            alertDialog.show();

        });

        if (remember_password.equals(Constants.NULL) && remember_email.equals(Constants.NULL)) {
            remember_box.setChecked(false);
        } else {
            remember_box.setChecked(true);
            email.setText(remember_email);
            password.setText(remember_password);
        }
        login.setOnClickListener(view -> {
            if (confirmInput()) {
                String Email = email.getText().toString();
                String Password = password.getText().toString();
                if (remember_box.isChecked()) {
                    editor.putString(Constants.EMAIL, Email);
                    editor.putString(Constants.PASSWORD, Password);
                    editor.apply();
                } else if (sharedPreferences.contains(Constants.EMAIL) && sharedPreferences.contains(Constants.PASSWORD)) {
                    editor.remove(Constants.EMAIL);
                    editor.remove(Constants.PASSWORD);
                    editor.apply();
                }
                auth.signInWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Intent intent = new Intent(getApplicationContext(), homepage.class);
                            startActivity(intent);
                            finish();

                        } else {
                            Toast.makeText(Login_page.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        accountSignUp.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), Signup_page.class);
            startActivity(intent);
            finish();
        });
        hideBtn.setOnClickListener(view -> {

            if (!visibility[0]) {
                hideBtn.setImageResource(R.drawable.unhideeye);
                password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                hideBtn.setImageResource(R.drawable.hide_eye);
                password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
            visibility[0] = !visibility[0];
        });
    }

    public void init() {
        accountSignUp = findViewById(R.id.accountSignUp);
        email = findViewById(R.id.login_email);
        password = findViewById(R.id.login_password);
        hideBtn = findViewById(R.id.eye_icon);
        login = findViewById(R.id.login_btn);
        remember_box = findViewById(R.id.remember_box);
        forgot_password = findViewById(R.id.forgotpassword);
        auth = FirebaseAuth.getInstance();
        sharedPreferences = getSharedPreferences(Constants.PREFERENCE, MODE_PRIVATE);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        UserArrayList = new ArrayList<>();
    }

    public boolean confirmInput() {
        return !(!helper.validateEmail(email) | !helper.validatePassword(password));
    }
}

