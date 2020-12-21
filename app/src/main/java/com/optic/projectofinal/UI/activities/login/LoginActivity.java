package com.optic.projectofinal.UI.activities.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.firestore.DocumentSnapshot;
import com.optic.projectofinal.R;
import com.optic.projectofinal.UI.activities.MainActivity;
import com.optic.projectofinal.UI.activities.login.register.RegisterStep1Activity;
import com.optic.projectofinal.databinding.ActivityLoginBinding;
import com.optic.projectofinal.databinding.LayoutLoginBottomSheetBinding;
import com.optic.projectofinal.databinding.LayoutRegisterBottomSheetBinding;
import com.optic.projectofinal.models.BasicInformationUser;
import com.optic.projectofinal.models.User;
import com.optic.projectofinal.providers.AuthenticationProvider;
import com.optic.projectofinal.providers.UserDatabaseProvider;
import com.optic.projectofinal.utils.Utils;

import java.util.Arrays;
import java.util.Date;

public class LoginActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_GOOGLE = 1;
    private GoogleSignInClient mGoogleSignInClient;
    private ActivityLoginBinding binding;
    private AuthenticationProvider mAuth;
    private UserDatabaseProvider mUserDatabase;
    private GoogleSignInOptions gso;
    private CallbackManager callbackManager = CallbackManager.Factory.create();
    private String[] permissionsFacebook=new String[]{"email","user_birthday","public_profile","user_gender"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //set binding
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ///intance object
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getResources().getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mAuth = new AuthenticationProvider();
        mUserDatabase = new UserDatabaseProvider();

        binding.loginCreateProfile.setOnClickListener(click -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(LoginActivity.this, R.style.BottomSheetDialogTheme);
            ///SET BINDING REGISTER FRAGMENT
            LayoutRegisterBottomSheetBinding fragmentBinding = LayoutRegisterBottomSheetBinding.inflate(getLayoutInflater());
            View vista = fragmentBinding.getRoot();
            fragmentBinding.btnContinueEmail.setOnClickListener(view -> startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));
            fragmentBinding.btnRegisterWithGoogle.setOnClickListener(view ->{
                signInGoogleIntent();
                bottomSheetDialog.dismiss();
            });
            fragmentBinding.btnRegisterWithFacebook.setOnClickListener(view ->
                    LoginManager.getInstance().logIn(LoginActivity.this, Arrays.asList(permissionsFacebook) ) );
            fragmentBinding.closeFragment.setOnClickListener(view -> bottomSheetDialog.dismiss());
            bottomSheetDialog.setContentView(vista);
            bottomSheetDialog.show();


        });


        binding.loginHaveAccount.setOnClickListener(click -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(LoginActivity.this, R.style.BottomSheetDialogTheme);
            //set binding FRAME SIGN IN FRAGMENT
            LayoutLoginBottomSheetBinding fragmentBinding = LayoutLoginBottomSheetBinding.inflate(getLayoutInflater());
            View vista = fragmentBinding.getRoot();
            bottomSheetDialog.setContentView(vista);

            fragmentBinding.btnContinueEmail.setOnClickListener(view ->
                    startActivity(new Intent(LoginActivity.this, SignInActivity.class)));
            fragmentBinding.btnSignInGoogle.setOnClickListener(view ->
            {
                signInGoogleIntent();
                bottomSheetDialog.dismiss();
            });


            fragmentBinding.btnSignInFacebook.setOnClickListener(view -> LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList(permissionsFacebook) ));


            fragmentBinding.closeFragment.setOnClickListener(view -> bottomSheetDialog.dismiss());
            bottomSheetDialog.show();

        });

        registerCallbackLoginFacebook();

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent
        if (requestCode == REQUEST_CODE_GOOGLE) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately

            }
        }
    }

    private void registerCallbackLoginFacebook() {
        LoginManager.getInstance().setLoginBehavior(LoginBehavior.WEB_VIEW_ONLY);
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                Toast.makeText(LoginActivity.this, "Facebook success", Toast.LENGTH_SHORT).show();
                mAuth.logInFacebook(loginResult.getAccessToken().getToken()).addOnCompleteListener(task -> {
                    if (task.isComplete()) {
                        checkUserExist(mAuth.getIdCurrentUser(), Utils.REGISTER.FACEBOOK);


                        Toast.makeText(LoginActivity.this, "Facebbok firebase OK", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(LoginActivity.this, "Facebook firebase BAD", Toast.LENGTH_SHORT).show();
                    }
                });

            }
            @Override
            public void onCancel() {
                Toast.makeText(LoginActivity.this, "Facebook cancel", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(LoginActivity.this, "Facebook error", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void firebaseAuthWithGoogle(String idToken) {

        mAuth.logInGoogle(idToken).addOnCompleteListener(this, (OnCompleteListener<AuthResult>) task -> {
            if (task.isSuccessful()) {
                checkUserExist(task.getResult().getUser().getUid(),Utils.REGISTER.GOOGLE);
            } else {
                Toast.makeText(LoginActivity.this, "No se pudo iniciar con google " + task.getException().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkUserExist(final String id, Utils.REGISTER option) {
        mUserDatabase.getUser(id).addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                ///the user exist
                ///set share preference
                saveSharePreference(documentSnapshot);

                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            } else {
                //the user not exist so we create it
                User  mUser = new User();
                mUser.setEmail(mAuth.getFirebaseAuth().getCurrentUser().getEmail());
                mUser.setId(id);
                mUser.setTimestamp(new Date().getTime());
                mUserDatabase.createUser(mUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            ///set share preference
                            saveSharePreference(documentSnapshot);
                            Intent i = new Intent(LoginActivity.this, RegisterStep1Activity.class);
                            i.putExtra("idUser",id);
                            i.putExtra("optionRegister",option);
                            startActivity(i);

                            Toast.makeText(LoginActivity.this, "Registrado database normal ", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LoginActivity.this, "Ha habido un error database " + task.getException(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        });
    }

    private void saveSharePreference(DocumentSnapshot documentSnapshot) {
        BasicInformationUser basicInformationUser = new BasicInformationUser();
        basicInformationUser.setPhotoUser(documentSnapshot.getString("profileImage"));
        basicInformationUser.setName(documentSnapshot.getString("name"));
        basicInformationUser.setLastName(documentSnapshot.getString("lastName"));
        Utils.setPersistantBasicUserInformation(basicInformationUser, LoginActivity.this);
        Utils.setLanguage("es-Es", this);
    }


    private void signInGoogleIntent() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, REQUEST_CODE_GOOGLE);
    }
}