package com.iceka.whatsappclone

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {

    private var mFirebaseAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        mFirebaseAuth = FirebaseAuth.getInstance()

    }


    override fun onStart() {
        super.onStart()
        Handler(mainLooper).postDelayed({
            if (mFirebaseAuth?.currentUser != null) {
                onAuthSuccess()
            } else {
                startActivity(Intent(this@SplashActivity, InputPhoneNumberActivity::class.java))
                finish()
            }
        }, 2500)
    }

    private fun onAuthSuccess() {
        startActivity(Intent(this@SplashActivity, HomeActivity::class.java))
        finish()
    }
}