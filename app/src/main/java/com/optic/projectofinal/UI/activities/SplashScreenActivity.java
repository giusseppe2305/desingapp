package com.optic.projectofinal.UI.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.optic.projectofinal.UI.activities.login.LoginActivity;
import com.optic.projectofinal.databinding.ActivitySplashScreenBinding;
import com.optic.projectofinal.providers.AuthenticationProvider;

public class SplashScreenActivity extends AppCompatActivity {
    ActivitySplashScreenBinding binding;
    private int TIME_TO_LOAD_SPLASH=2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.lottieSplashScreen.animate().translationX(-1600).setDuration(1000).setStartDelay(TIME_TO_LOAD_SPLASH);
        binding.plane.animate().translationX(-1600).setDuration(1000).setStartDelay(TIME_TO_LOAD_SPLASH);

        new Handler(Looper.myLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                AuthenticationProvider mAuth=new AuthenticationProvider();
                if(mAuth.existSession()){
                    startActivity(new Intent(SplashScreenActivity.this,MainActivity.class));
                }else{
                    startActivity(new Intent(SplashScreenActivity.this,LoginActivity.class));
                }
                finishAffinity();///destroy all activities until this time
            }
        }, TIME_TO_LOAD_SPLASH);
    }
}