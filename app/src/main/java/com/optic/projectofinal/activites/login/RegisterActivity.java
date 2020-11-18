package com.optic.projectofinal.activites.login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toolbar;

import com.optic.projectofinal.R;
import com.optic.projectofinal.databinding.ActivityRegiterBinding;

public class RegisterActivity extends AppCompatActivity {
    com.optic.projectofinal.databinding.ActivityRegiterBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= com.optic.projectofinal.databinding.ActivityRegiterBinding.inflate(getLayoutInflater());
        View vista=binding.getRoot();
        setContentView(vista);

        setSupportActionBar(binding.toolbar.TOOLBAR);
        getSupportActionBar().setTitle("Iniciar sesion");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}