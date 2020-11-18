package com.optic.projectofinal.activites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;

import com.optic.projectofinal.R;
import com.optic.projectofinal.databinding.ActivityRegiterBinding;
import com.optic.projectofinal.databinding.ActivitySignInBinding;

public class SignInActivity extends AppCompatActivity {
    ActivitySignInBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        binding= ActivitySignInBinding.inflate(getLayoutInflater());
        View vista=binding.getRoot();
        setContentView(vista);

        setSupportActionBar(binding.toolbar.TOOLBAR);
        getSupportActionBar().setTitle("Iniciar sesion");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
}