package com.iceka.whatsappclone

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.nabinbhandari.android.permissions.PermissionHandler
import com.nabinbhandari.android.permissions.Permissions


class SplashActivity : AppCompatActivity() {
    var permissions =
        arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.SEND_SMS,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,

            )
    private var mFirebaseAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        mFirebaseAuth = FirebaseAuth.getInstance()

    }


    override fun onStart() {
        super.onStart()
        Handler(mainLooper).postDelayed({
            Permissions.check(
                this /*context*/,
                permissions,
                null /*rationale*/,
                null /*options*/,
                object : PermissionHandler() {
                    override fun onGranted() {
                        if (mFirebaseAuth?.currentUser != null) {
                            onAuthSuccess()
                        } else {
                            startActivity(
                                Intent(
                                    this@SplashActivity,
                                    InputPhoneNumberActivity::class.java
                                )
                            )
                            finish()
                        }
                    }
                })


        }, 2500)
    }

    private fun onAuthSuccess() {
        startActivity(Intent(this@SplashActivity, HomeActivity::class.java))
        finish()
    }
}