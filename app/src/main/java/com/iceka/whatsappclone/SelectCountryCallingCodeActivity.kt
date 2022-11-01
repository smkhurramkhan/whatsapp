package com.iceka.whatsappclone

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.iceka.whatsappclone.adapters.AdapterCountryCallingCode
import com.iceka.whatsappclone.models.CountryCallingCode

class SelectCountryCallingCodeActivity : AppCompatActivity() {
    private var mRvCountry: RecyclerView? = null
    private var mToolbar: Toolbar? = null
    private val countryCallingCodeList = mutableListOf<CountryCallingCode>()
    private var mCountryCodeReference: DatabaseReference? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_country_calling_code)
        mRvCountry = findViewById(R.id.rv_country_calling_code)
        mToolbar = findViewById(R.id.toolbar_select_calling_code)
        setSupportActionBar(mToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        mToolbar?.setNavigationOnClickListener { finish() }
        val firebaseDatabase = FirebaseDatabase.getInstance()
        mCountryCodeReference = firebaseDatabase.reference.child("country_codes")
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(applicationContext)
        mRvCountry?.layoutManager = layoutManager
        mRvCountry?.itemAnimator = DefaultItemAnimator()
        mRvCountry?.addItemDecoration(
            DividerItemDecoration(
                applicationContext,
                DividerItemDecoration.VERTICAL
            )
        )
        data
    }

    private val data: Unit
        get() {
            mCountryCodeReference?.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (snapshot in dataSnapshot.children) {
                        val countryCallingCode = snapshot.getValue(
                            CountryCallingCode::class.java
                        )
                        countryCallingCodeList.add(countryCallingCode!!)
                        val adapter =
                            AdapterCountryCallingCode(applicationContext, countryCallingCodeList)
                        mRvCountry?.adapter = adapter
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
        }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_select_country_calling_code, menu)
        return true
    }
}