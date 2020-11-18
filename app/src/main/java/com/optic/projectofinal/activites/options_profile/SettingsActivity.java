package com.optic.projectofinal.activites.options_profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.optic.projectofinal.R;
import com.optic.projectofinal.activites.options_profile.settings.EditProfileActivity;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar mToolbar = findViewById(R.id.TOOLBAR);
        setSupportActionBar(mToolbar);
      getSupportActionBar().setTitle("Ajustes");
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);

      findViewById(R.id.option_edit_profile).setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              startActivity(new Intent(SettingsActivity.this, EditProfileActivity.class));
          }
      });
    }
}