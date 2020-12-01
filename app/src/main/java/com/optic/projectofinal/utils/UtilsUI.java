package com.optic.projectofinal.utils;

import android.content.Context;
import android.util.Patterns;

import com.google.android.material.textfield.TextInputLayout;
import com.optic.projectofinal.R;

import java.util.regex.Pattern;

public class UtilsUI {
    private Context context;

    public UtilsUI(Context context) {
        this.context = context;
    }


    public boolean isPasswordsValid(TextInputLayout inputLayouPassword) {
        String stringPassword = inputLayouPassword.getEditText().getText().toString();
        String errorTextReturn = "";

        boolean CHECK_PASSWORD_PATTERN_DIGITS = true;
        boolean CHECK_PASSWORD_PATTERN_LOWER_CASE = true;
        boolean CHECK_PASSWORD_PATTERN_UPPER_CASE = true;
        boolean CHECK_PASSWORD_PATTERN_LENGTH = true;

        String PASSWORD_PATTERN_DIGITS = "((?=.*\\d).*)";///must contains one digit from 0-9
        String PASSWORD_PATTERN_LOWER_CASE = "((?=.*[a-z]).*)";///must contains one lowercase characters
        String PASSWORD_PATTERN_UPPER_CASE = "((?=.*[A-Z]).*)";// must contains one uppercase characters
        String PASSWORD_PATTERN_LENGTH = "(.{8,20})";/// length at least 8 characters and maximum of 20

        if (!Pattern.matches(PASSWORD_PATTERN_DIGITS, stringPassword)) {
            errorTextReturn += context.getString(R.string.pattern_password_digits_between_0_9) + "\r\n";
            CHECK_PASSWORD_PATTERN_DIGITS = false;
        }
        if (!Pattern.matches(PASSWORD_PATTERN_LOWER_CASE, stringPassword)) {
            errorTextReturn += context.getString(R.string.pattern_password_min_lower_case) + "\r\n";
            CHECK_PASSWORD_PATTERN_LOWER_CASE = false;
        }
        if (!Pattern.matches(PASSWORD_PATTERN_UPPER_CASE, stringPassword)) {
            errorTextReturn += context.getString(R.string.pattern_password_min_upper_case) + "\r\n";
            CHECK_PASSWORD_PATTERN_UPPER_CASE = false;
        }
        if (!Pattern.matches(PASSWORD_PATTERN_LENGTH, stringPassword)) {
            errorTextReturn += context.getString(R.string.pattern_password_corretly_length);
            CHECK_PASSWORD_PATTERN_LENGTH = false;
        }
        if (errorTextReturn.length() == 0) {
            inputLayouPassword.setError(null);
        } else {
            inputLayouPassword.setError(context.getString(R.string.pattern_pasword_header_result) + "\r\n" + errorTextReturn);
        }

        return CHECK_PASSWORD_PATTERN_DIGITS && CHECK_PASSWORD_PATTERN_LOWER_CASE && CHECK_PASSWORD_PATTERN_UPPER_CASE && CHECK_PASSWORD_PATTERN_LENGTH;
    }

    public boolean isEmailValid(TextInputLayout inputLayoutEmail) {
        String email = inputLayoutEmail.getEditText().getText().toString().trim();
        if (Patterns.EMAIL_ADDRESS.matcher(email).find()) {
            inputLayoutEmail.setError(null);
            return true;
        } else if (email.length() == 0) {
            inputLayoutEmail.setError(context.getString(R.string.pattern_email_void_field));
            return false;
        }
        inputLayoutEmail.setError(context.getString(R.string.pattern_email_wrong_pattern));
        return false;
    }


}
