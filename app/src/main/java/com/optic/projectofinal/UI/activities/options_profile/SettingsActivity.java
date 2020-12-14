package com.optic.projectofinal.UI.activities.options_profile;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.optic.projectofinal.UI.activities.login.LoginActivity;
import com.optic.projectofinal.UI.activities.options_profile.settings.EditProfileActivity;
import com.optic.projectofinal.UI.activities.options_profile.settings.EditSettingsWorkerActivity;
import com.optic.projectofinal.databinding.ActivitySettingsBinding;
import com.optic.projectofinal.providers.AuthenticationProvider;

public class SettingsActivity extends AppCompatActivity {
    private ActivitySettingsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar.ownToolbar);
        getSupportActionBar().setTitle("Ajustes");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding.optionEditProfile.setOnClickListener(v -> startActivity(new Intent(SettingsActivity.this, EditProfileActivity.class)));
        binding.optionEditSettingsWorker.setOnClickListener(v -> startActivity(new Intent(SettingsActivity.this, EditSettingsWorkerActivity.class)));
        binding.optionVerify.setOnClickListener(v->startActivity(new Intent(SettingsActivity.this,VerifyAccountActivity.class)));
        binding.signOut.setOnClickListener(v->{

            new AuthenticationProvider().logOut(SettingsActivity.this);
            finishAffinity();
            startActivity(new Intent(SettingsActivity.this,LoginActivity.class));
        });
    }
}