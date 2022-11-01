package com.iceka.whatsappclone

import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.telephony.TelephonyManager
import android.view.Menu
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import com.iceka.whatsappclone.models.CountryCallingCode
import java.util.*

class InputPhoneNumberActivity : AppCompatActivity() {
    private var mEtPhoneNumber: EditText? = null
    private var mCountryCode: EditText? = null

    //private EditText mEtCountryName;
    private var mPhoneNumber: String? = null
    private var countryDialCode: String? = null
    private var mProgressDialog: ProgressDialog? = null
    private var mCountryCodesReference: DatabaseReference? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input_phone_number)
        mEtPhoneNumber = findViewById(R.id.et_phone_number)
        mCountryCode = findViewById(R.id.et_country_code)
        val mNext = findViewById<TextView>(R.id.bt_next_input_number)
        val firebaseDatabase = FirebaseDatabase.getInstance()
        mCountryCodesReference = firebaseDatabase.reference.child("country_codes")
        mEtPhoneNumber?.requestFocus()
        mNext.setOnClickListener {
            mPhoneNumber =
                "+" + mCountryCode?.text.toString() + mEtPhoneNumber?.text.toString()
            if (mEtPhoneNumber?.text.toString().length < 9) {
                AlertDialog.Builder(this@InputPhoneNumberActivity)
                    .setMessage(
                        """
    The phone number you entered is too short for the country: 
    
    Include your area code if you haven't.
    """.trimIndent()
                    )
                    .setPositiveButton("OK") { dialogInterface: DialogInterface, i: Int -> dialogInterface.dismiss() }
                    .show()
            } else {
                showProgressDialog()
            }
        }
        mProgressDialog = ProgressDialog(this)
        data
    }

    private val data: Unit
         get() {
            val telephonyManager = this.getSystemService(TELEPHONY_SERVICE) as TelephonyManager
            val phoneDefaultLocation =
                telephonyManager.networkCountryIso.uppercase(Locale.getDefault())
            val myQuery =
                mCountryCodesReference?.orderByChild("code")?.equalTo(phoneDefaultLocation)
            myQuery?.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (snapshot in dataSnapshot.children) {
                        val countryCallingCode = snapshot.getValue(
                            CountryCallingCode::class.java
                        )
                        if (countryDialCode != null) {
                            val tes = countryDialCode?.replace("+", "")
                            mCountryCode?.setText(tes)
                        } else {
                            val tes = countryCallingCode!!.dial_code.replace("+", "")
                            mCountryCode?.setText(tes)
                        }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
        }

    private fun showDialog() {
        val builder = AlertDialog.Builder(this@InputPhoneNumberActivity)
        builder.setMessage("We will be verifying the phone number:\n\n$mPhoneNumber\n\nIs this OK, or would you like to edit the number?")
            .setPositiveButton("OK") { dialogInterface: DialogInterface?, i: Int ->
                val intent = Intent(this@InputPhoneNumberActivity, PhoneVerifyActivity::class.java)
                intent.putExtra("phonenumber", mPhoneNumber)
                startActivity(intent)
            }
            .setNegativeButton("Edit") { dialogInterface: DialogInterface, i: Int -> dialogInterface.dismiss() }
        val dialog = builder.create()
        dialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == COUNTRY_REQUEST) {
            if (resultCode == RESULT_OK) {
                countryDialCode = data?.getStringExtra("countryDialCode")
                val tes = countryDialCode?.replace("+", "")
                mCountryCode!!.setText(tes)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_input_phone_number, menu)
        return true
    }

    private fun showProgressDialog() {
        mProgressDialog?.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        mProgressDialog?.setCancelable(false)
        mProgressDialog?.setMessage("Connecting")
        mProgressDialog?.show()
        Thread {
            var loading = 0
            while (loading < 100) {
                try {
                    Thread.sleep(150)
                    loading += 20
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
            mProgressDialog?.dismiss()
            runOnUiThread { showDialog() }
        }.start()
    }

    companion object {
        private const val COUNTRY_REQUEST = 1
    }
}