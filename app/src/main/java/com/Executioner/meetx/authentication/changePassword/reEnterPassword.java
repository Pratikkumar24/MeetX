package com.Executioner.meetx.authentication.changePassword;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.Executioner.meetx.R;
import com.Executioner.meetx.authentication.Login_page;
import com.Executioner.meetx.authentication.helper;

import java.util.Objects;

public class reEnterPassword extends AppCompatActivity {

    private final String TAG = "reEnterPassword";
    EditText newPassword, reenterPassword;
    ImageView verification1, verification2;
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
                Intent newIntent = new Intent(getApplicationContext(), Login_page.class);
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