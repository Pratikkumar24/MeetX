package com.Executioner.meetx.authentication.changePassword;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.Executioner.meetx.Homepage.homepage;
import com.Executioner.meetx.R;
import com.Executioner.meetx.authentication.Constants;
import com.Executioner.meetx.authentication.Login_page;
import com.Executioner.meetx.authentication.Users;
import com.Executioner.meetx.authentication.helper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class reEnterPassword extends AppCompatActivity {

    private final String TAG = "reEnterPassword";
    EditText newPassword, reenterPassword;
    ImageView verification1, verification2;
    private DatabaseReference mDatabase;
    SharedPreferences sharedPreferences;
    Button submit;
    ImageView hideEye1, hideEye2;
    boolean visibility1, visibility2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();

        setContentView(R.layout.activity_re_enter_password);
        init();
        submit.setOnClickListener(view -> {
            verification2.setVisibility(View.VISIBLE);
            verification1.setVisibility(View.VISIBLE);
            if(confirmPassword(newPassword, reenterPassword))
            {
                verification1.setImageResource(R.drawable.checksymbol);
                verification2.setImageResource(R.drawable.checksymbol);
                Log.i(TAG," New Password: "+ newPassword.getText().toString());
                Log.i(TAG," Confirm Password: "+ reenterPassword.getText().toString());

                //Changing of password
                DatabaseReference refs = mDatabase.child(Constants.USERS);
                String OTP_email = sharedPreferences.getString(Constants.OTP_EMAIL,Constants.NULL);
                Map<String, Object> map = new HashMap<>();
                map.put(Constants.PASSWORD,newPassword.getText().toString());
                refs.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot Snapshot) {
                        for (DataSnapshot dataSnapshot : Snapshot.getChildren())
                        {
                            String Uid = dataSnapshot.getKey();
                            String user_email =  Objects.requireNonNull(dataSnapshot.getValue(Users.class)).getEmail();
                            if(user_email.equals(OTP_email))
                            {
                                assert Uid != null;
                                Log.i(TAG," first step: "+ mDatabase.child(Constants.USERS).getKey());
                                Log.i(TAG," second step: "+ mDatabase.child(Constants.USERS).child(Uid).getKey());
                                Log.i(TAG," Third step: "+ mDatabase.child(Constants.USERS).child(Uid).child(Constants.PASSWORD).getKey());

                                refs.child(Uid).updateChildren(map);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.remove(Constants.OTP_EMAIL);
                                editor.apply();
                                Toast.makeText(getApplicationContext(),"Password Successfully changed!", Toast.LENGTH_SHORT).show();
                                break;
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                //end of changing password
                Intent newIntent = new Intent(getApplicationContext(), homepage.class);
                startActivity(newIntent);
                finish();
            }
            else {
                verification1.setImageResource(R.drawable.wrongsymbol);
                verification2.setImageResource(R.drawable.wrongsymbol);
                Toast.makeText(getApplicationContext(), "Re-enter password is not same", Toast.LENGTH_SHORT).show();
            }
        });
        hideEye1.setOnClickListener(view -> {
            if (!visibility1) {
                hideEye1.setImageResource(R.drawable.unhideeye);
                newPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                hideEye1.setImageResource(R.drawable.hide_eye);
                newPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
            visibility1 = !visibility1;
        });
        hideEye2.setOnClickListener(view -> {
            if (!visibility2) {
                hideEye2.setImageResource(R.drawable.unhideeye);
                reenterPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                hideEye2.setImageResource(R.drawable.hide_eye);
                reenterPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
            visibility2 = !visibility2;
        });
    }
    private void init() {
        newPassword = findViewById(R.id.newPassword);
        reenterPassword = findViewById(R.id.cnfPasword);
        submit = findViewById(R.id.submitPassword);
        hideEye1 = findViewById(R.id.eye_icon1);
        hideEye2 = findViewById(R.id.eye_icon2);
        verification1 = findViewById(R.id.verification_check1);
        verification2 = findViewById(R.id.verification_check2);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        sharedPreferences = getSharedPreferences(Constants.PREFERENCE, MODE_PRIVATE);
        visibility1 = false;
        visibility2 = false;
    }
    private boolean confirmPassword(EditText password, EditText reenterPassword) {
        if(helper.validatePassword(password) && helper.validatePassword(reenterPassword))
        {
            String new_Password = password.getText().toString();
            String confirm_Password = reenterPassword.getText().toString();
            return new_Password.equals(confirm_Password);
        }

        return false;
    }
}