package com.optic.projectofinal.UI.activities.options_profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.optic.projectofinal.R;
import com.optic.projectofinal.UI.activities.login.LoginActivity;
import com.optic.projectofinal.UI.activities.options_profile.settings.EditProfileActivity;
import com.optic.projectofinal.UI.activities.options_profile.settings.EditSettingsWorkerActivity;
import com.optic.projectofinal.databinding.ActivitySettingsBinding;
import com.optic.projectofinal.providers.AuthenticationProvider;
import com.optic.projectofinal.providers.UserDatabaseProvider;

public class SettingsActivity extends AppCompatActivity {
    private ActivitySettingsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar.ownToolbar);
        getSupportActionBar().setTitle(R.string.settings_activity_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding.optionEditProfile.setOnClickListener(v -> startActivity(new Intent(SettingsActivity.this, EditProfileActivity.class)));
        binding.optionEditSettingsWorker.setOnClickListener(v -> startActivity(new Intent(SettingsActivity.this, EditSettingsWorkerActivity.class)));
        binding.optionVerify.setOnClickListener(v->startActivity(new Intent(SettingsActivity.this,VerifyAccountActivity.class)));
        binding.signOut.setOnClickListener(v->{

            new UserDatabaseProvider().updateOnline(false);
            new AuthenticationProvider().logOut(SettingsActivity.this);
            finishAffinity();
            startActivity(new Intent(SettingsActivity.this,LoginActivity.class));
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}