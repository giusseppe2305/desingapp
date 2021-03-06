package com.optic.projectofinal.UI.activities.options_profile;

import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.optic.projectofinal.R;
import com.optic.projectofinal.databinding.ActivityVerifyAccountBinding;
import com.optic.projectofinal.models.User;
import com.optic.projectofinal.providers.UserDatabaseProvider;

import java.util.concurrent.TimeUnit;

import static com.optic.projectofinal.utils.Utils.TAG_LOG;

public class VerifyAccountActivity extends AppCompatActivity {
    
    private ActivityVerifyAccountBinding binding;
    private String mVerificationId;
    private FirebaseAuth mAuth= FirebaseAuth.getInstance();
    private UserDatabaseProvider mUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityVerifyAccountBinding.inflate(getLayoutInflater());

        setSupportActionBar(binding.toolbar.ownToolbar);
        getSupportActionBar().setTitle(R.string.verify_account_activity_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mUser=new UserDatabaseProvider();
        setContentView(binding.getRoot());
        PhoneNumberFormattingTextWatcher watcher = new PhoneNumberFormattingTextWatcher();
        watcher.beforeTextChanged("+34",0,3,3);
        binding.numberPhone.getEditText().addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        binding.btnConfirmCode.setOnClickListener(view -> {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, binding.confirmCode.getEditText().getText().toString());
            updateNumberPhone(credential);

        });
        binding.btnSendSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.load.setVisibility(View.VISIBLE);
                PhoneAuthOptions options =
                        PhoneAuthOptions.newBuilder(mAuth)
                                .setPhoneNumber("+34"+binding.numberPhone.getEditText().getText().toString())       // Phone number to verify
                                .setTimeout(120L, TimeUnit.SECONDS) // Timeout and unit
                                    .setActivity(VerifyAccountActivity.this)                 // Activity (for callback binding)
                                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                    @Override
                                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                        Log.d(TAG_LOG, "onVerificationCompleted: verifico");
                                    }

                                    @Override
                                    public void onVerificationFailed(@NonNull FirebaseException e) {
                                        Log.d(TAG_LOG, "onVerificationCompleted: fallo verifico "+e.getMessage());
                                        Snackbar.make(binding.getRoot(), R.string.fail_to_send_sms, BaseTransientBottomBar.LENGTH_LONG).show();
                                    }

                                    @Override
                                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                        super.onCodeSent(s, forceResendingToken);
                                        binding.load.setVisibility(View.GONE);
                                        mVerificationId=s;
                                        Log.d(TAG_LOG, "onCodeSent: "+s+" "+forceResendingToken.toString());
                                        updateUI(true);
                                    }
                                })          // OnVerificationStateChangedCallbacks
                                .build();
                PhoneAuthProvider.verifyPhoneNumber(options);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateUI(boolean option) {
        binding.numberPhone.setVisibility(option?View.GONE:View.VISIBLE);
        binding.btnSendSMS.setVisibility(option?View.GONE:View.VISIBLE);

        binding.btnConfirmCode.setVisibility(!option?View.GONE:View.VISIBLE);
        binding.confirmCode.setVisibility(!option?View.GONE:View.VISIBLE);

    }

    private void updateNumberPhone(PhoneAuthCredential credential) {
        FirebaseAuth.getInstance().getCurrentUser().updatePhoneNumber(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG_LOG, "signInWithCredential:success");
                    User update=new User();
                    update.setId(mAuth.getCurrentUser().getUid());
                    update.setVerified(true);
                    update.setPhoneNumber(Integer.parseInt(binding.numberPhone.getEditText().getText().toString().replaceAll("\\s+","")));
                    mUser.updateUser(update).addOnFailureListener(error-> Log.e(TAG_LOG, "onComplete: "+error.getMessage()));
                    Toast.makeText(VerifyAccountActivity.this, getString(R.string.code_confirmed), Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    // Sign in failed, display a message and update the UI
                    Log.w(TAG_LOG, "signInWithCredential:failure", task.getException());
                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                        binding.confirmCode.setError(getString(R.string.wrong_code));
                    }

                }
            }
        });
    }



}