package com.optic.projectofinal.UI.activities.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.firestore.DocumentSnapshot;
import com.optic.projectofinal.R;
import com.optic.projectofinal.UI.activities.MainActivity;
import com.optic.projectofinal.databinding.ActivityLoginBinding;
import com.optic.projectofinal.databinding.LayoutLoginBottomSheetBinding;
import com.optic.projectofinal.databinding.LayoutRegisterBottomSheetBinding;
import com.optic.projectofinal.models.User;
import com.optic.projectofinal.providers.AuthenticationProvider;
import com.optic.projectofinal.providers.UserDatabaseProvider;

public class LoginActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_GOOGLE = 1;
    private GoogleSignInClient mGoogleSignInClient;
    private ActivityLoginBinding binding;
    private AuthenticationProvider mAuth;
    private UserDatabaseProvider mUserDatabase;
    private GoogleSignInOptions gso;
    private CallbackManager callbackManager = CallbackManager.Factory.create();;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //set binding
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        ///intance object
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getResources().getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mAuth = new AuthenticationProvider();
        mUserDatabase = new UserDatabaseProvider();
        binding.loginCreateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(LoginActivity.this, R.style.BottomSheetDialogTheme);
                ///SET BINDING REGISTER FRAGMENT
                LayoutRegisterBottomSheetBinding fragmentBinding = LayoutRegisterBottomSheetBinding.inflate(getLayoutInflater());
                View vista = fragmentBinding.getRoot();
                fragmentBinding.btnContinueEmail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                    }
                });
                fragmentBinding.btnRegisterWithGoogle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        signInGoogleIntent();
                    }
                });
                fragmentBinding.btnRegisterWithFacebook.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        LoginManager.getInstance().logIn(LoginActivity.this,null);
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

        binding.loginHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(LoginActivity.this, R.style.BottomSheetDialogTheme);
                //set binding FRAME SIGN IN FRAGMENT
                LayoutLoginBottomSheetBinding fragmentBinding = LayoutLoginBottomSheetBinding.inflate(getLayoutInflater());
                View vista = fragmentBinding.getRoot();
                fragmentBinding.btnContinueEmail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(LoginActivity.this, SignInActivity.class));
                    }
                });
                fragmentBinding.btnSignInGoogle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        signInGoogleIntent();
                    }
                });

                //fragmentBinding.btnSignInFacebook.setLoginText(getString(R.string.sign_in_with_facebook));

                fragmentBinding.btnSignInFacebook.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        LoginManager.getInstance().logIn(LoginActivity.this,null);
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
                mAuth.logInFacebook(loginResult.getAccessToken().getToken()).addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isComplete()){
                            Toast.makeText(LoginActivity.this, "Facebbok firebase OK", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(LoginActivity.this, "Facebook firebase BAD", Toast.LENGTH_SHORT).show();
                        }
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

        mAuth.logInGoogle(idToken).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    checkUserExist(task.getResult().getUser().getUid());
                } else {
                    Toast.makeText(LoginActivity.this, "No se pudo iniciar con google " + task.getException().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void checkUserExist(final String id) {
        mUserDatabase.getUser(id).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                } else {
                    User mUser = new User();
                    mUser.setEmail(mAuth.getFirebaseAuth().getCurrentUser().getEmail());
                    mUser.setId(id);
                    mUserDatabase.createUser(mUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(i);
                                Toast.makeText(LoginActivity.this, "Registrado database normal ", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(LoginActivity.this, "Ha habido un error database " + task.getException(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                }
            }
        });
    }


    private void signInGoogleIntent() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, REQUEST_CODE_GOOGLE);
    }
}