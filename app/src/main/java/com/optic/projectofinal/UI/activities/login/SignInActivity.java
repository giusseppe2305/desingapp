package com.optic.projectofinal.UI.activities.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.optic.projectofinal.R;
import com.optic.projectofinal.UI.activities.MainActivity;
import com.optic.projectofinal.databinding.ActivitySignInBinding;
import com.optic.projectofinal.providers.AuthenticationProvider;
import com.optic.projectofinal.utils.UtilsUI;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener {
    private ActivitySignInBinding binding;
    private AuthenticationProvider mAuth;
    private UtilsUI utilsUI;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ///set binding
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        View vista = binding.getRoot();
        setContentView(vista);
        ///set toolbar
        setSupportActionBar(binding.toolbar.ownToolbar);
        getSupportActionBar().setTitle(R.string.sign_in_activity_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //instance objects
        mAuth = new AuthenticationProvider();
        utilsUI=new UtilsUI(this);
        ///onclick
        binding.btnSignIn.setOnClickListener(this);
        //on focuschanged
        binding.txtEmail.getEditText().setOnFocusChangeListener(this);
        binding.txtPassword.getEditText().setOnFocusChangeListener(this);
    }



    @Override
    public void onClick(View view) {
        int idView=view.getId();

        if(idView==binding.btnSignIn.getId()) {
            if(fieldsAreValidated()){
                signInWithEmail();
            }
        }
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        int idView=view.getId();
        if(idView==binding.txtEmail.getEditText().getId()){
            if (!b) {
                utilsUI.isEmailValid(binding.txtEmail);
            }
        }

        if(idView==binding.txtPassword.getEditText().getId()){
            if(!b){
                utilsUI.isPasswordsValid(binding.txtPassword);
            }
        }
    }
    private boolean fieldsAreValidated() {

        boolean b = utilsUI.isEmailValid(binding.txtEmail);
        boolean c = utilsUI.isPasswordsValid(binding.txtPassword);
        return b && c;
    }

    private void signInWithEmail() {
        String email = binding.txtEmail.getEditText().getText().toString();
        String pass = binding.txtPassword.getEditText().getText().toString();
        mAuth.logIn(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(SignInActivity.this, "Login correcto!!!!", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(SignInActivity.this, MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                } else {
                    Toast.makeText(SignInActivity.this, "Login fallido!!!!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}