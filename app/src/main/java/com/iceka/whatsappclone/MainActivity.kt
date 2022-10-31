package com.iceka.whatsappclone

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.iceka.whatsappclone.adapters.TabAdapter
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private var mTabAdapter: TabAdapter? = null
    private var mViewPager: ViewPager? = null
    private var mTabLayout: TabLayout? = null
    private var mFabBottom: FloatingActionButton? = null
    private var mFabTop: FloatingActionButton? = null
    private var mToolbar: Toolbar? = null
    private var mAppBarLayout: AppBarLayout? = null
    private var mFirebaseAuth: FirebaseAuth? = null
    private var mFirebaseUser: FirebaseUser? = null
    private var userReference: DatabaseReference? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mTabLayout = findViewById(R.id.tab_layout)
        mViewPager = findViewById(R.id.viewpager)
        mFabBottom = findViewById(R.id.fab_bottom)
        mFabTop = findViewById(R.id.fab_top)
        mToolbar = findViewById(R.id.toolbar)
        mAppBarLayout = findViewById(R.id.appbar_layout)
        if (mToolbar != null) {
            setSupportActionBar(mToolbar)
        }
        setSupportActionBar(mToolbar)
        mTabAdapter = TabAdapter(this, supportFragmentManager)
        mViewPager?.offscreenPageLimit = 4
        mViewPager?.adapter = mTabAdapter
        mViewPager?.currentItem = 1
        mTabLayout?.setupWithViewPager(mViewPager)
        // mTabLayout.getTabAt(0).setIcon(R.drawable.ic_camera_alt_white_24dp);
        val layout = (mTabLayout?.getChildAt(0) as LinearLayout).getChildAt(0) as LinearLayout
        val layoutParams = layout.layoutParams as LinearLayout.LayoutParams
        layoutParams.weight = 0.5f
        layout.layoutParams = layoutParams
        mFirebaseAuth = FirebaseAuth.getInstance()
        mFirebaseUser = mFirebaseAuth!!.currentUser
        userReference = FirebaseDatabase.getInstance().reference.child("users").child(
            mFirebaseUser?.uid!!
        )
        fabSettings()
    }

    private fun fabSettings() {
        if (mViewPager?.currentItem == 1) {
            mFabBottom?.setOnClickListener {
                startActivity(
                    Intent(
                        this@MainActivity,
                        ContactActivity::class.java
                    )
                )
            }
        }
        mTabLayout!!.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val position = tab.position
                when (position) {
                    0 -> {
                        mFabTop?.hide()
                        mFabBottom?.hide()
                    }
                    1 -> {
                        mFabBottom?.hide()
                        mFabBottom?.setImageResource(R.drawable.ic_comment_white_24dp)
                        mFabTop?.hide()
                        mFabBottom?.show()
                        mFabBottom?.setOnClickListener {
                            startActivity(
                                Intent(
                                    this@MainActivity,
                                    ContactActivity::class.java
                                )
                            )
                        }
                    }
                    2 -> {
                        mFabTop?.hide()
                        mFabTop?.show()
                        mFabBottom?.hide()
                        mFabBottom?.show()
                        mFabTop?.setOnClickListener {
                            startActivity(
                                Intent(
                                    this@MainActivity,
                                    StatusTextActivity::class.java
                                )
                            )
                        }
                        mFabBottom!!.setOnClickListener {
                            val intent = Intent(Intent.ACTION_GET_CONTENT)
                            intent.type = "image/jpeg"
                            intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
                            startActivityForResult(
                                Intent.createChooser(
                                    intent,
                                    "Complete action using"
                                ), RC_PHOTO_PICKER
                            )
                        }
                    }
                    else -> {
                        mFabTop?.hide()
                        mFabBottom?.hide()
                        mFabBottom?.setImageResource(R.drawable.ic_phone_black_white_24dp)
                        mFabBottom?.show()
                        mFabBottom?.setOnClickListener {
                            Toast.makeText(
                                this@MainActivity,
                                "view 3",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
                if (position == 0) {
                    mToolbar?.visibility = View.GONE
                    mAppBarLayout?.setExpanded(false, true)
                } else {
                    mToolbar?.visibility = View.VISIBLE
                    mAppBarLayout?.setExpanded(true, true)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK) {
            if (data != null) {
                val selectedImage = data.data
                val intent = Intent(this, EditStatusActivity::class.java)
                intent.data = selectedImage
                startActivity(intent)
            } else {
                Toast.makeText(applicationContext, "data is null", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(applicationContext, "Something went wrong", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onStart() {
        super.onStart()
        userReference?.child("online")?.setValue(true)
    }

    override fun onDestroy() {
        super.onDestroy()
        val lastSeen = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())
        userReference?.child("online")?.setValue(false)
        userReference?.child("lastSeen")?.setValue(lastSeen)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // getMenuInflater().inflate(R.menu.menu_main, menu);
        return false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout -> {
                mFirebaseAuth?.signOut()
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        if (mViewPager?.currentItem == 1) {
            super.onBackPressed()
        } else {
            mViewPager?.currentItem = 1
        }
    }

    companion object {
        private const val RC_PHOTO_PICKER = 2
    }
}