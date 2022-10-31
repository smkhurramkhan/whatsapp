package com.iceka.whatsappclone

import android.os.Bundle
import android.view.Menu
import android.view.accessibility.AccessibilityEvent
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.iceka.whatsappclone.adapters.ContactAdapter
import com.iceka.whatsappclone.models.User

class ContactActivity : AppCompatActivity() {
    private var mDatabaseReference: DatabaseReference? = null
    private var mFirebaseUser: FirebaseUser? = null
    private var mRecyclerView: RecyclerView? = null
    private var mContactCount: TextView? = null
    private var mAdapter: ContactAdapter? = null
    private val contactList = mutableListOf<User>()
    override fun dispatchPopulateAccessibilityEvent(event: AccessibilityEvent): Boolean {
        return super.dispatchPopulateAccessibilityEvent(event)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact)
        mRecyclerView = findViewById(R.id.recycler_view_contact)
        val mToolbar = findViewById<Toolbar>(R.id.toolbar_contact)
        mContactCount = findViewById(R.id.tv_contact_counts)
        setSupportActionBar(mToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        mToolbar.setNavigationOnClickListener { finish() }
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(applicationContext)
        mRecyclerView?.layoutManager = layoutManager
        mRecyclerView?.itemAnimator = DefaultItemAnimator()
        val mFirebaseDatabase = FirebaseDatabase.getInstance()
        val mFirebaseAuth = FirebaseAuth.getInstance()
        mFirebaseUser = mFirebaseAuth.currentUser
        mDatabaseReference = mFirebaseDatabase.reference.child("users")
        data
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_contact, menu)
        return true
    }

    private val data: Unit
         get() {
            val myQuery = mDatabaseReference!!.orderByChild("username")
            myQuery.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    contactList.clear()
                    for (snapshot in dataSnapshot.children) {
                        val user = snapshot.getValue(
                            User::class.java
                        )
                        if (user?.uid != mFirebaseUser?.uid) {
                            user?.let { contactList.add(it) }
                            mContactCount?.text = contactList.size.toString() + " contacts"
                        }
                    }
                    mAdapter = ContactAdapter(this@ContactActivity, contactList)
                    mRecyclerView?.adapter = mAdapter
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
        }
}