package com.optic.projectofinal.UI.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.optic.projectofinal.UI.activities.login.LoginActivity;
import com.optic.projectofinal.databinding.ActivitySplashScreenBinding;
import com.optic.projectofinal.providers.AuthenticationProvider;

public class SplashScreenActivity extends AppCompatActivity {
    private static final String TAG = "own";
    ActivitySplashScreenBinding binding;
    private int TIME_TO_LOAD_SPLASH=2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.lottieSplashScreen.animate().translationX(-1600).setDuration(1000).setStartDelay(TIME_TO_LOAD_SPLASH);
        binding.plane.animate().translationX(-1600).setDuration(1000).setStartDelay(TIME_TO_LOAD_SPLASH);

//        new Handler(Looper.myLooper()).postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                AuthenticationProvider mAuth=new AuthenticationProvider();
//                if(mAuth.existSession()){
//                    startActivity(new Intent(SplashScreenActivity.this,MainActivity.class));
//                }else{
//                    startActivity(new Intent(SplashScreenActivity.this,LoginActivity.class));
//                }
//                finishAffinity();///destroy all activities until this time
//            }
//        }, TIME_TO_LOAD_SPLASH);

        tryToGetDynamicLink();
    }
    public void tryToGetDynamicLink() {
        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        // Get deep link from result (may be null if no link is found)
                        Uri deepLink = null;
                        if (pendingDynamicLinkData != null) {
                            deepLink = pendingDynamicLinkData.getLink();
                            if (deepLink.getQueryParameter("id") != null) {
                                Log.e(TAG, "onSuccess: tiene link" );
                                String id = deepLink.getQueryParameter("id");
                                Intent intent=new Intent(SplashScreenActivity.this,MainActivity.class);
                                intent.putExtra("idUserToSee",id);
                                startActivity(intent);
                            }
                        } else {
                            AuthenticationProvider mAuth=new AuthenticationProvider();
                            if(mAuth.existSession()){
                                startActivity(new Intent(SplashScreenActivity.this,MainActivity.class));
                            }else{
                                startActivity(new Intent(SplashScreenActivity.this,LoginActivity.class));
                            }

                            Log.e(TAG, "onSuccess: ERROR WITH DYNAMIC LINK OR NO LINK AT ALL" );
                        }
                        finishAffinity();///destroy all activities until this time

                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onSuccess: ERROR WITH DYNAMIC LINK" );
                        AuthenticationProvider mAuth=new AuthenticationProvider();
                        if(mAuth.existSession()){
                            startActivity(new Intent(SplashScreenActivity.this,MainActivity.class));
                        }else{
                            startActivity(new Intent(SplashScreenActivity.this,LoginActivity.class));
                        }
                        finishAffinity();///destroy all activities until this time

                    }
                });
    }
}