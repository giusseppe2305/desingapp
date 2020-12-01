package com.optic.projectofinal.UI.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.optic.projectofinal.R;
import com.optic.projectofinal.databinding.ActivityCreateJobBinding;

public class EditJobActivity extends AppCompatActivity {
    private ActivityCreateJobBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityCreateJobBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ////toolbar
        setSupportActionBar(binding.toolbar.ownToolbar);
        getSupportActionBar().setTitle("editar");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }
}