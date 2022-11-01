package com.iceka.whatsappclone

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.iceka.whatsappclone.models.CountryCallingCode
import java.util.*

class ContohActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contoh)
        findViewById<ImageView>(R.id.flag_img)
        val textView = findViewById<TextView>(R.id.bebeas)
        val list = this.resources.getStringArray(R.array.CountryCodes)
        val g = list[0].split(",").toTypedArray()
        g[1].trim { it <= ' ' }.lowercase(Locale.getDefault())
        //        imageView.setImageResource(getResources().getIdentifier("drawable/" + pngName, null, getPackageName()));
        val firebaseDatabase = FirebaseDatabase.getInstance()
        val reference = firebaseDatabase.reference.child("country_codes")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
//                    Log.i("MYTAG", "info : " + snapshot.getKey());
                    val countryCallingCode = snapshot.getValue(
                        CountryCallingCode::class.java
                    )
                    Log.i("MYTAG", "name : " + countryCallingCode!!.name)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
        val flagOffset = 0x1F1E6
        val asciiOffset = 0x41
        val coba = 0x1F60A
        String(Character.toChars(coba))
        val country = "SZ"
        val firstChar = Character.codePointAt(country, 0) - asciiOffset + flagOffset
        val secondChar = Character.codePointAt(country, 1) - asciiOffset + flagOffset
        val flag = (String(Character.toChars(firstChar))
                + String(Character.toChars(secondChar)))
        textView.text = flag
    }

}