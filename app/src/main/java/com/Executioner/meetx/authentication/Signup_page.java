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
        getSupportActionBar().hide();


        final boolean[] visibility = {false};
        init();
        signUp.setOnClickListener(view -> {
            if (confirmInput()) {
                String UserName = username.getText().toString();
                String Password = password.getText().toString();
                String Email = email.getText().toString();
                String PhoneNo = phoneNum.getText().toString();
                Log.i(TAG, UserName + "$" + Password + "$" + Email + "&" + Password);
                int k = 1000;
                int duplicacyFlag = -2;
                while (k > 0) {
                    duplicacyFlag = VerifyDuplicacy(UserName, PhoneNo);
                    if (retrieve == 1) {
                        break;
                    }
                    k--;
                }
                Log.i(TAG, "k :" + k);
                Log.i(TAG, "duplicay flag :" + duplicacyFlag);
                switch (duplicacyFlag) {
                    case 1:
                        Toast.makeText(getApplicationContext(), "Username exist", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(getApplicationContext(), "Phone number exist", Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Toast.makeText(getApplicationContext(), "Both username and phone number exist", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        CreateUserWithEmailAndPassword(Email, Password, UserName, PhoneNo);
                        break;
                }
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
        alreadyMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Login_page.class);
                startActivity(intent);
                finish();
            }
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

    public Integer VerifyDuplicacy(String username, String PhoneNum) {
        DatabaseReference refs = mDatabase.child(Constants.USERS);
        UsernameFound = -1;
        PhoneNumFound = -1;
        Log.i(TAG, "Value one");
        refs.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Users> UserArrayList = new ArrayList<>();
                //getting all ids of users
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Users idDetails = dataSnapshot.getValue(Users.class);
                    UserArrayList.add(idDetails);
                }
                Log.i(TAG, "Reached inside verify duplicacy");
                Log.i(TAG, "Value two");
                for (Users user : UserArrayList) {
                    String searchUsername = user.getUsername();
                    String searchPhoneno = user.getPhoneNo();
                    if (searchUsername.equals(username)) {
                        UsernameFound = 0;
                    }
                    if (searchPhoneno.equals(PhoneNum)) {
                        PhoneNumFound = 0;
                    }
                    if (UsernameFound == 0 && PhoneNumFound == 0) break;
                }
                retrieve = 1;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        // 0 ->Both not found in DB, 1-> Username found in DB , 2-> Phone Num found in DB ,3 -> Both found in DB
        Log.i(TAG, "Username value: " + UsernameFound + " Phone no value: " + PhoneNumFound);
        Log.i(TAG, "Value three");
        if (UsernameFound == -1 && PhoneNumFound == -1) return 0;
        else if (UsernameFound == 0 && PhoneNumFound == -1) return 1;
        else if (UsernameFound == -1 && PhoneNumFound == 0) return 2;
        else return 3;
    }
}