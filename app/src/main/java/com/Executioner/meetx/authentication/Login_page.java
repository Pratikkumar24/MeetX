package com.Executioner.meetx.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsManager;
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
import com.chaos.view.PinView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class Login_page extends AppCompatActivity {
    String TAG = "login_page";
    Button login, getOTP, verifyEmail, verifyEmail_close, verifyOTP, verifyOTP_close;
    PinView pinViewOTP;
    ImageView Verification;
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
    private String OtpPhoneNumber;

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
            View verifyEmailView = getLayoutInflater().inflate(R.layout.forgotpassworddialogbox, null);
            OTP_generator.setCancelable(false);
            OTP_generator.setView(verifyEmailView);
            AlertDialog alertDialog = OTP_generator.create();

            verifyEmail_close = verifyEmailView.findViewById(R.id.button_close);
            getOTP = verifyEmailView.findViewById(R.id.send_otp);
            otpEmail = verifyEmailView.findViewById(R.id.OTP_email);
            Verification = verifyEmailView.findViewById(R.id.verification_check);
            verifyEmail = verifyEmailView.findViewById(R.id.verifyEmail);


            verifyEmail_close.setOnClickListener(closingDialog -> alertDialog.dismiss());
            verifyEmail.setOnClickListener(verifyEmailview -> {
                if (helper.validateEmail(otpEmail)) {
                    String OtpEmail = otpEmail.getText().toString();
                    OtpPhoneNumber = Constants.DEFAULT_PHONE_NUMBER;
                    DatabaseReference refs = mDatabase.child(Constants.USERS);
                    refs.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            //getting all ids of users
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                Users idDetails = dataSnapshot.getValue(Users.class);
                                UserArrayList.add(idDetails);
                            }
                            boolean EmailFound = false;
                            for (Users user : UserArrayList) {
                                String searchEmail = user.getEmail();
                                if (searchEmail.equals(OtpEmail)) {
                                    EmailFound = true;
                                    OtpPhoneNumber = user.getPhoneNo();
                                    Verification.setVisibility(View.VISIBLE);
                                    Verification.setImageResource(R.drawable.checksymbol);
                                }
                            }
                            if (!EmailFound) {
                                Verification.setVisibility(View.VISIBLE);
                                Verification.setImageResource(R.drawable.wrongsymbol);
                            } else {
                                //Enabling the getOTP button as the email is perfectly verified
                                getOTP.setEnabled(true);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            });
            getOTP.setOnClickListener(getOtpEmail -> {
                View verifyOTPview = getLayoutInflater().inflate(R.layout.verificationotp, null);
                OTP_generator.setView(verifyOTPview);
                AlertDialog verifydialog = OTP_generator.create();
                alertDialog.dismiss();

                verifyOTP_close = verifyOTPview.findViewById(R.id.button_close_OTP);
                verifyOTP = verifyOTPview.findViewById(R.id.verfyOTP);
                pinViewOTP = verifyOTPview.findViewById(R.id.pinview);
                Random rand= new Random();
                int high=Constants.HIGH_BOUND;
                int low=Constants.LOW_BOUND;
                int generatedOTP=rand.nextInt(high-low)+low;
                sendSMS(OtpPhoneNumber, String.valueOf(generatedOTP));
                Log.i(TAG,"Generated OTP on mobile Number: " + OtpPhoneNumber + " : "+generatedOTP);
                verifyOTP_close.setOnClickListener(closingDialog -> verifydialog.dismiss());
                verifyOTP.setOnClickListener(verifingOTP -> {
                    int enteredOTP = Integer.parseInt(Objects.requireNonNull(pinViewOTP.getText()).toString());
                    if(generatedOTP==enteredOTP) {
                        Toast.makeText(getApplicationContext(), "Verified", Toast.LENGTH_SHORT).show();
                    }
                   else Toast.makeText(getApplicationContext(), "Wrong OTP entered", Toast.LENGTH_SHORT).show();
                });
                verifydialog.show();
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
                auth.signInWithEmailAndPassword(Email, Password).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        Intent intent = new Intent(getApplicationContext(), homepage.class);
                        startActivity(intent);
                        finish();

                    } else {
                        Toast.makeText(Login_page.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
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
    public void sendSMS(String phoneNo, String msg) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, msg, null, null);
            Toast.makeText(getApplicationContext(), "Message Sent",
                    Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(),ex.getMessage().toString(),
                    Toast.LENGTH_LONG).show();
            Log.i(TAG,"SMS Exception:"+ex.getMessage().toString());
            ex.printStackTrace();
        }
    }
}


