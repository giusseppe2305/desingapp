package com.optic.projectofinal.UI.activities.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.optic.projectofinal.R;
import com.optic.projectofinal.UI.activities.MainActivity;
import com.optic.projectofinal.databinding.ActivityRegiterBinding;
import com.optic.projectofinal.models.User;
import com.optic.projectofinal.providers.AuthenticationProvider;
import com.optic.projectofinal.providers.UserDatabaseProvider;
import com.optic.projectofinal.utils.UtilsUI;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    private ActivityRegiterBinding binding;
    private AuthenticationProvider mAuth;
    private UserDatabaseProvider mUserProvider;
    private UtilsUI utilsUI;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set binding
        binding = com.optic.projectofinal.databinding.ActivityRegiterBinding.inflate(getLayoutInflater());
        View vista = binding.getRoot();
        setContentView(vista);

        //set toolbar
        setSupportActionBar(binding.toolbar.TOOLBAR);
        getSupportActionBar().setTitle(getResources().getString(R.string.register_activity_title));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //instace objects
        mAuth = new AuthenticationProvider();
        mUserProvider = new UserDatabaseProvider();
        utilsUI=new UtilsUI(this);
        ////
        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fieldsAreValidated()) {
                    createUserWithAuthentication();
                }
            }
        });
        binding.txtEmail.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    utilsUI.isEmailValid(binding.txtEmail);
                }
            }
        });
        binding.txtPassword.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b){
                    utilsUI.isPasswordsValid(binding.txtPassword);
                }
            }
        });
        binding.txtPasswordRepeat.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b){
                    if(utilsUI.isPasswordsValid(binding.txtPassword)){
                        isEqualBothPasswords();
                    }
                }
            }
        });
    }

    private boolean fieldsAreValidated() {

        boolean b = utilsUI.isEmailValid(binding.txtEmail);
        boolean c = utilsUI.isPasswordsValid(binding.txtPassword);
        boolean a = isEqualBothPasswords();
        if (a && b && c) {
            return true;
        }
        return false;
    }
    private boolean isEqualBothPasswords(){
        if(binding.txtPassword.getEditText().getText().toString().equals(binding.txtPasswordRepeat.getEditText().getText().toString())){
            return true;
        }else{
            binding.txtPassword.setError(getString(R.string.pattern_password_not_equal_passwords));
            binding.txtPasswordRepeat.setError(getString(R.string.pattern_password_not_equal_passwords));
            return false;
        }
    }



    private void createUserWithAuthentication() {
        String email = binding.txtEmail.getEditText().getText().toString();
        String pass = binding.txtPassword.getEditText().getText().toString();

        mAuth.createAuthentication(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    final User mUser = new User();
                    mUser.setEmail(email);
                    mUser.setId(mAuth.getIdCurrentUser());
                    mUserProvider.createUser(mUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(RegisterActivity.this, "Registrado database normal ", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(RegisterActivity.this, MainActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);
                            } else {
                                Toast.makeText(RegisterActivity.this, "Ha habido un error database " + task.getException(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                    Toast.makeText(RegisterActivity.this, "Usuario registrado Authentication", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(RegisterActivity.this, "Ha habido un error AUTHENTICATION " + task.getException(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}