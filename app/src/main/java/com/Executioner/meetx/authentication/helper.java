package com.Executioner.meetx.authentication;

import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;

import com.Executioner.meetx.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class helper {
    public static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    //"(?=.*[a-z])"  +    //at least 1 lower case letter
                    //"(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{4,}" +               //at least 4 characters
                    "$");


    public static boolean validateEmail(EditText email) {
        String emailInput = email.getText().toString();

        if (emailInput.isEmpty()) {
            email.setError("Field can't be empty");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            //todo - EmailAddress need to be checked for pattern
            email.setError("Please enter a valid email address");
            return false;
        } else {
            email.setError(null);
            return true;
        }
    }

    public static boolean validateUsername(EditText username) {
        String usernameInput = username.getText().toString().trim();

        if (usernameInput.isEmpty()) {
            username.setError("Field can't be empty");
            return false;
        } else if (usernameInput.length() > Constants.USERNAME_MAX_LENGTH) {
            username.setError("Username too long");
            return false;
        } else {
            username.setError(null);
            return true;
        }
    }

    public static boolean validatePassword(EditText password) {
        String passwordInput = password.getText().toString().trim();

        if (passwordInput.isEmpty()) {
            password.setError("Field can't be empty");
            return false;
        } else if (!helper.PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            password.setError("Need minimum 4 characters \nNeed special character (@#$%^&+=)\nNeed a digit\nNo white spaces ");
            return false;
        } else {
            password.setError(null);
            return true;
        }
    }

    public static boolean validatePhoneNumber(EditText PhoneNum) {
        String phoneNumInput = PhoneNum.getText().toString().trim();

        if (phoneNumInput.isEmpty()) {
            PhoneNum.setError("Field can't be empty");
            return false;
        } else if (phoneNumInput.length()!=10) {
            PhoneNum.setError("Please enter 10 digits phone number");
            return false;
        } else {
            PhoneNum.setError(null);
            return true;
        }
    }
}
