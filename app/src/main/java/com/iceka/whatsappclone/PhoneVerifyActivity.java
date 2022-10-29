package com.iceka.whatsappclone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import timber.log.Timber;

public class PhoneVerifyActivity extends AppCompatActivity {

    private String phoneNumber;
    private String verificationId;

    private FirebaseAuth mFirebaseAuth;

    private EditText mEtCode;
    private TextView mBtNext;
    private TextView mTvWaiting;
    private TextView mTvCountdownSMS;

    private ProgressDialog mProgressDialog;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                Timber.d("otp code : %s", code);
                mEtCode.setText(code);
                showProgressDialog(code);
//                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(PhoneVerifyActivity.this, "Something went wrong: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_verify);

        phoneNumber = getIntent().getStringExtra("phonenumber");

        mEtCode = findViewById(R.id.et_verification_code);
        mBtNext = findViewById(R.id.bt_next_main);
        mTvWaiting = findViewById(R.id.tv_wating_text);
        //mToolbarTitle = findViewById(R.id.toolbar_title_input_number);
        mTvCountdownSMS = findViewById(R.id.tv_countdown_sms);

        String test = getString(R.string.waiting_sms, phoneNumber);
        mTvWaiting.setText(test);
        //mToolbarTitle.setText("Verify " + phoneNumber);

        mEtCode.requestFocus();

        mFirebaseAuth = FirebaseAuth.getInstance();

        sendVerificationCode(phoneNumber);

        mProgressDialog = new ProgressDialog(this);

        mBtNext.setOnClickListener(view -> {
            String code = mEtCode.getText().toString();
            showProgressDialog(code);
        });
    }

    private void verifyCode(String code) {
        try {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
            signInWithCredential(credential);
        } catch (Exception e) {
            Toast.makeText(this, "Verification code wrong", Toast.LENGTH_SHORT).show();
        }

    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(PhoneVerifyActivity.this, CreateProfileActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        } else {
                            Toast.makeText(PhoneVerifyActivity.this, "Cannot verify : " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void sendVerificationCode(String number) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(number, 60, TimeUnit.SECONDS, TaskExecutors.MAIN_THREAD, mCallback);
    }

    private void showProgressDialog(final String code) {
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage("Verifying");
        mProgressDialog.show();
        new Thread(() -> {
            int loading = 0;
            while (loading < 100) {
                try {
                    Thread.sleep(150);
                    loading += 20;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            mProgressDialog.dismiss();
            PhoneVerifyActivity.this.runOnUiThread(() -> verifyCode(code));

        }).start();
    }

}
