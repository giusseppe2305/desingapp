package com.optic.projectofinal.UI.activities.options_profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

import com.optic.projectofinal.R;

public class Favourites_Workers_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites_workers_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.ownToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Favoritos");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}