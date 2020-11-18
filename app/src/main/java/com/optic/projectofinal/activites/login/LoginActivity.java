package com.optic.projectofinal.activites.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.optic.projectofinal.R;
import com.optic.projectofinal.activites.MainActivity;
import com.optic.projectofinal.activites.SignInActivity;
import com.optic.projectofinal.databinding.ActivityLoginBinding;
import com.optic.projectofinal.databinding.ActivityMainBinding;
import com.optic.projectofinal.databinding.BottomSheet2Binding;
import com.optic.projectofinal.databinding.LayoutLoginBottomSheetBinding;
import com.optic.projectofinal.databinding.LayoutRegisterBottomSheetBinding;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding loginActivityBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginActivityBinding=ActivityLoginBinding.inflate(getLayoutInflater());
        View view=loginActivityBinding.getRoot();
        setContentView(view);

        loginActivityBinding.loginCreateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(LoginActivity.this,R.style.BottomSheetDialogTheme);

//                View vista= LayoutInflater.from(getApplicationContext())
//                        .inflate(R.layout.layout_login_bottom_sheet,null);

                LayoutRegisterBottomSheetBinding fragmentBinding=LayoutRegisterBottomSheetBinding.inflate(getLayoutInflater());
                View vista=fragmentBinding.getRoot();
                fragmentBinding.btnContinueEmail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                    }
                });
                fragmentBinding.closeFragment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bottomSheetDialog.dismiss();
                    }
                });
                bottomSheetDialog.setContentView(vista);
                bottomSheetDialog.show();


            }
        });

        loginActivityBinding.loginHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(LoginActivity.this,R.style.BottomSheetDialogTheme);

//                View vista= LayoutInflater.from(getApplicationContext())
//                        .inflate(R.layout.layout_login_bottom_sheet,null);

                LayoutLoginBottomSheetBinding fragmentBinding=LayoutLoginBottomSheetBinding.inflate(getLayoutInflater());
                View vista=fragmentBinding.getRoot();
                fragmentBinding.btnContinueEmail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(LoginActivity.this, SignInActivity.class));
                    }
                });
                fragmentBinding.closeFragment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bottomSheetDialog.dismiss();
                    }
                });
                bottomSheetDialog.setContentView(vista);
                bottomSheetDialog.show();

            }
        });
    }
}