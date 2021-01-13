package com.optic.projectofinal.UI.activities.options_profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.optic.projectofinal.R;
import com.optic.projectofinal.UI.activities.SplashScreenActivity;
import com.optic.projectofinal.UI.activities.login.LoginActivity;
import com.optic.projectofinal.UI.activities.options_profile.settings.EditProfileActivity;
import com.optic.projectofinal.UI.activities.options_profile.settings.EditSettingsWorkerActivity;
import com.optic.projectofinal.databinding.ActivitySettingsBinding;
import com.optic.projectofinal.providers.AuthenticationProvider;
import com.optic.projectofinal.providers.UserDatabaseProvider;
import com.optic.projectofinal.utils.Utils;

import static com.optic.projectofinal.utils.Utils.TAG_LOG;
import static com.optic.projectofinal.utils.Utils.getCurrentLocation;

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

        binding.tagLanguage.setText(getCurrentLocation(this).getLanguage().toUpperCase());

        binding.chooseLanguage.setOnClickListener(view ->
                {
                    int position=0;
                    Log.d(TAG_LOG, "onCreate: "+Utils.getCurrentLocation(SettingsActivity.this).toLanguageTag()+"-"+Utils.LANGUAGE.SPANISH.getCode());
                    if(Utils.getCurrentLocation(SettingsActivity.this).toLanguageTag().equalsIgnoreCase(Utils.LANGUAGE.SPANISH.getCode())){
                        position=0;
                    }else{
                        position=1;
                    }
                    int finalPosition = position;
                    Log.d(TAG_LOG, "onCreate: 0"+finalPosition);
                    new MaterialAlertDialogBuilder(SettingsActivity.this)
                            .setTitle(R.string.select_language)
                            .setSingleChoiceItems(new String[]{getString(R.string.spanish), getString(R.string.english)}, finalPosition, (dialogInterface, i) -> {
                                if(finalPosition !=i){
                                    if(i==0){
                                        Utils.setLanguage(Utils.LANGUAGE.SPANISH.getCode(),SettingsActivity.this);
                                    }else{
                                        Utils.setLanguage(Utils.LANGUAGE.ENGLISH.getCode(),SettingsActivity.this);
                                    }
                                    dialogInterface.dismiss();
                                    finishAffinity();
                                    startActivity(new Intent(SettingsActivity.this,SplashScreenActivity.class));
                                }
                            })
                            .setNegativeButton(R.string.create_edit_job_dialog_photo_negative_button,null)
                            .show();
                }
        );
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}