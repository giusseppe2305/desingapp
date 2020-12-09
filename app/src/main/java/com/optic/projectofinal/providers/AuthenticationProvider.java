package com.optic.projectofinal.providers;

import android.content.Context;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class AuthenticationProvider {
    private FirebaseAuth auth;

    public AuthenticationProvider() {
        auth = FirebaseAuth.getInstance();
    }

    public Task<AuthResult> createAuthentication(String email,String password) {
        return auth.createUserWithEmailAndPassword(email, password);

    }

    public Task<AuthResult> logIn(String email, String pass) {
        return auth.signInWithEmailAndPassword(email, pass);
    }

    public String getIdCurrentUser() {
        return auth.getCurrentUser().getUid();
    }
    public FirebaseUser getCurrentUser() {
        return auth.getCurrentUser();
    }

    public FirebaseAuth getFirebaseAuth() {
        return auth;
    }

    public boolean existSession() {
        return auth.getCurrentUser() != null;
    }

    public void logOut(Context context) {
        auth.signOut();
        GoogleSignIn.getClient(
                context,
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
        ).signOut();
        ///cerrar en facebbok
        LoginManager.getInstance().logOut();
    }
   
    public Task logInGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        return auth.signInWithCredential(credential);
    }
    public Task logInFacebook(String idToken) {
        AuthCredential credential = FacebookAuthProvider.getCredential(idToken);
        return auth.signInWithCredential(credential);
    }


}
