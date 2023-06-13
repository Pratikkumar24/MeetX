package com.Executioner.meetx.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Objects;

public class Signup_page extends AppCompatActivity {
    EditText username, password, email, phoneNum;
    int retrieve;
    Button signUp;
    ImageView hide;
    LinearLayout alreadyMember;
    FirebaseAuth auth;
    FirebaseDatabase database;
    int UsernameFound, PhoneNumFound;
    private DatabaseReference mDatabase;
    String TAG = "signup_page";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_page);
        Objects.requireNonNull(getSupportActionBar()).hide();


        final boolean[] visibility = {false};
        init();
        signUp.setOnClickListener(view -> {
            if (confirmInput()) {
                String UserName = username.getText().toString();
                String Password = password.getText().toString();
                String Email = email.getText().toString();
                String PhoneNo = phoneNum.getText().toString();
                Log.i(TAG, UserName + "$" + Password + "$" + Email + "&" + Password);
                DatabaseReference refs = mDatabase.child(Constants.USERS);
                refs.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ArrayList<Users> UserArrayList = new ArrayList<>();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Users idDetails = dataSnapshot.getValue(Users.class);
                            UserArrayList.add(idDetails);
                        }
                        int duplicacyFlag = 0;
                        for(Users user: UserArrayList)
                        {
                            String user_name = user.getUsername();
                            String phone_num = user.getPhoneNo();
                            if(user_name.equals(UserName))
                            {
                                Toast.makeText(Signup_page.this, "Username already exists", Toast.LENGTH_SHORT).show();
                                duplicacyFlag = 1;
                            }
                            else if(phone_num.equals(PhoneNo))
                            {
                                Toast.makeText(Signup_page.this, "Phone number already exists", Toast.LENGTH_SHORT).show();
                                duplicacyFlag = 2;

                            }
                            
                        }
                        checkFunctionality(duplicacyFlag, Email, Password,UserName, PhoneNo);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
        hide.setOnClickListener(view -> {

            if (!visibility[0]) {
                hide.setImageResource(R.drawable.unhideeye);
                password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                hide.setImageResource(R.drawable.hide_eye);
                password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
            visibility[0] = !visibility[0];
        });
        alreadyMember.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), Login_page.class);
            startActivity(intent);
            finish();
        });
    }
    private void CreateUserWithEmailAndPassword(String email, String password, String userName, String phoneNo) {
        //putting into database
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    String id = Objects.requireNonNull(task.getResult().getUser()).getUid();
                    DatabaseReference reference = database.getReference().child(Constants.USERS).child(id);
                    Users user = new Users();
                    user.setUsername(userName);
                    user.setPassword(password);
                    user.setEmail(email);
                    user.setPhoneNo(phoneNo);
                    reference.setValue(user).addOnCompleteListener(completionTask -> {
                        if (completionTask.isSuccessful()) {
                            Intent intent = new Intent(getApplicationContext(), homepage.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Log.i(TAG, "Error in creating account");
                            Toast.makeText(getApplicationContext(), "Error in creating account", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Log.i(TAG, "Exception in creating user");
                    Toast.makeText(getApplicationContext(), Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void init() {
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        alreadyMember = findViewById(R.id.alreadyMemberLogin);
        hide = findViewById(R.id.hideBtn);
        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        signUp = findViewById(R.id.signup_btn);
        password = findViewById(R.id.password);
        phoneNum = findViewById(R.id.phoneNum);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        retrieve = 0;
    }
    public boolean confirmInput() {
        return !(!helper.validateEmail(email) | !helper.validateUsername(username) |
                !helper.validatePassword(password) | !helper.validatePhoneNumber(phoneNum));
    }
    public void checkFunctionality(int duplicacyFlag, String Email, String Password, String UserName, String PhoneNo) {
        switch (duplicacyFlag) {
            case 1:
                Toast.makeText(getApplicationContext(), "Username exist", Toast.LENGTH_SHORT).show();
                break;
            case 2:
                Toast.makeText(getApplicationContext(), "Phone number exist", Toast.LENGTH_SHORT).show();
                break;
            default:
                CreateUserWithEmailAndPassword(Email, Password, UserName, PhoneNo);
                break;
        }
    }
}