package com.optic.projectofinal.UI.activities.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.optic.projectofinal.R;
import com.optic.projectofinal.UI.activities.login.register.RegisterStep1Activity;
import com.optic.projectofinal.databinding.ActivityRegiterBinding;
import com.optic.projectofinal.models.User;
import com.optic.projectofinal.providers.AuthenticationProvider;
import com.optic.projectofinal.providers.UserDatabaseProvider;
import com.optic.projectofinal.utils.Utils;
import com.optic.projectofinal.utils.UtilsUI;

import static com.optic.projectofinal.utils.Utils.TAG_LOG;

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
        setSupportActionBar(binding.toolbar.ownToolbar);
        getSupportActionBar().setTitle( getResources().getString(R.string.register_activity_title));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //instace objects
        mAuth = new AuthenticationProvider();
        mUserProvider = new UserDatabaseProvider();
        utilsUI=new UtilsUI(this);
        ////
        binding.btnRegister.setOnClickListener(view -> {
            if (fieldsAreValidated()) {
                createUserWithAuthentication();
            }
        });
        binding.txtEmail.getEditText().setOnFocusChangeListener((view, b) -> {
            if (!b) {
                utilsUI.isEmailValid(binding.txtEmail);
            }
        });
        binding.txtPassword.getEditText().setOnFocusChangeListener((view, b) -> {
            if(!b){
                utilsUI.isPasswordsValid(binding.txtPassword);
            }
        });
        binding.txtPasswordRepeat.getEditText().setOnFocusChangeListener((view, b) -> {
            if(!b){
                if(utilsUI.isPasswordsValid(binding.txtPassword)){
                    isEqualBothPasswords();
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

        mAuth.createAuthentication(email,pass).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                final User mUser = new User();
                mUser.setDefaultData();
                mUser.setEmail(email);
                mUser.setId(mAuth.getIdCurrentUser());
                mUserProvider.createUser(mUser).addOnSuccessListener(aVoid -> {
                    Intent i = new Intent(RegisterActivity.this, RegisterStep1Activity.class);
                    i.putExtra("idUser",mAuth.getIdCurrentUser());
                    i.putExtra("optionRegister",Utils.REGISTER.EMAIL);
                    startActivity(i);
                    Log.d(TAG_LOG, "User registered "+task.getResult().getUser().getUid());
                }).addOnFailureListener(error-> Log.e(TAG_LOG, "createUserWithAuthentication: error "+error.getMessage() ));
            } else {
                Log.e(TAG_LOG, "Ha habido un error AUTHENTICATION " + task.getException());
            }
        });
    }

}