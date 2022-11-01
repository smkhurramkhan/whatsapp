package com.iceka.whatsappclone

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationBarView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.iceka.whatsappclone.adapters.HourlyAdapter
import com.iceka.whatsappclone.adapters.WeeklyAdapter
import com.iceka.whatsappclone.databinding.ActivityHomeBinding
import com.iceka.whatsappclone.databinding.WeatherDialogBinding
import com.iceka.whatsappclone.fragments.ChatTabFragment
import com.iceka.whatsappclone.models.User
import com.iceka.whatsappclone.models.WeatherModel
import com.iceka.whatsappclone.newfrags.FragmentHome
import com.iceka.whatsappclone.weather.WeatherWebview


class HomeActivity : AppCompatActivity() {
    var doubleBackToExitPressedOnce = false
    private lateinit var binding: ActivityHomeBinding

    private var userUid: String? = null
    private var mFirebaseDatabase: FirebaseDatabase? = null
    private var mFirebaseAuth: FirebaseAuth? = null
    private var mFirebaseUser: FirebaseUser? = null
    private var mUserReference: DatabaseReference? = null


    private var hourlyAdapter: HourlyAdapter? = null
    private var hourlyDataList = mutableListOf<WeatherModel>()

    private var weeklyAdapter: WeeklyAdapter? = null
    private var weeklyDataList = mutableListOf<WeatherModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setHourlyData()
        setWeeklyWeatherData()

        mFirebaseDatabase = FirebaseDatabase.getInstance()
        mFirebaseAuth = FirebaseAuth.getInstance()
        mFirebaseUser = mFirebaseAuth?.currentUser

        userUid = mFirebaseUser?.uid
        mUserReference = mFirebaseDatabase?.reference?.child("users")

        setSupportActionBar(binding.appbarHome.toolbar)
        val toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.appbarHome.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        binding.navView.setNavigationItemSelectedListener { item: MenuItem ->

            when (item.itemId) {
                R.id.action_whats_new -> {

                }
                R.id.action_recharge -> {

                }
                R.id.action_support -> {

                }
                R.id.action_optin -> {


                }
                R.id.action_settings -> {
                }

                R.id.action_contact -> {

                }
                R.id.action_logout -> {
                    mFirebaseAuth?.signOut()
                    val intent = Intent(this, InputPhoneNumberActivity::class.java)
                    startActivity(intent)
                    finishAffinity()
                }
            }
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
        binding.navView.itemIconTintList = null

        loadFragment(FragmentHome())
        bottomNavigationClicks()

        onNavHeaderClicks()

        getUserDetails()

    }

    private fun getUserDetails() {
        mUserReference?.child(userUid!!)
            ?.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val user = dataSnapshot.getValue(
                        User::class.java
                    )
                    val hView = binding.navViewHead.getHeaderView(0)
                    val username: TextView = hView.findViewById(R.id.username)
                    username.text = user?.username

                    val userProfile: ImageView = findViewById(R.id.iv_user)

                    Glide.with(applicationContext)
                        .load(user?.photoUrl)
                        .into(userProfile)

                    val userNumber: TextView = findViewById(R.id.phone_number)
                    userNumber.text = user?.phone
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
    }


    private fun onNavHeaderClicks() {
        val hView = binding.navViewHead.getHeaderView(0)
        val closeBtn = hView.findViewById<ImageView>(R.id.closebtn)

        closeBtn.setOnClickListener {
            if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            }
        }
    }

    private fun bottomNavigationClicks() {
        binding.appbarHome.navigation.setOnItemSelectedListener(
            mOnNavigationItemSelectedListener
        )
    }

    private fun loadFragment(fragment: Fragment) {
        // load fragment
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.contentLayout, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private val mOnNavigationItemSelectedListener =
        NavigationBarView.OnItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.location -> {
                    Toast.makeText(this, "Coming Soon", Toast.LENGTH_SHORT).show()
                }
                R.id.messaging -> {
                    loadFragment(ChatTabFragment())
                }
                R.id.home -> {
                    loadFragment(FragmentHome())
                }
                R.id.bag -> {
                    Toast.makeText(this, "Coming Soon", Toast.LENGTH_SHORT).show()
                }
                R.id.history -> {
                    Toast.makeText(this, "Coming Soon", Toast.LENGTH_SHORT).show()
                }
            }
            true
        }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.weather_menu -> {
                val intent= Intent(this, WeatherWebview::class.java)
                startActivity(intent)

               // weatherDialog()
                true
            }
            R.id.notification_menu -> {
                Toast.makeText(this, "Coming Soon", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }

        if (doubleBackToExitPressedOnce) {
            finish()
            return
        }
        doubleBackToExitPressedOnce = true
        Toast.makeText(this, getString(R.string.exit_info), Toast.LENGTH_SHORT).show()
        Handler(Looper.getMainLooper())
            .postDelayed(
                { doubleBackToExitPressedOnce = false },
                2000
            )
    }


    private fun weatherDialog() {
        val dialogBuilder: AlertDialog.Builder =
            AlertDialog.Builder(this, R.style.CustomAlertDialog)
        val inflater = this.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.weather_dialog, null)

        val dialogBinding = WeatherDialogBinding.bind(dialogView);

        dialogBuilder.setView(dialogBinding.root)

        val alertDialog = dialogBuilder.create()
        alertDialog.setCanceledOnTouchOutside(false)
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.show()

        dialogBinding.btnClose.setOnClickListener {
            alertDialog?.dismiss()
        }

        setHourlyAdapter(dialogBinding)
        setWeeklyAdapter(dialogBinding)


    }

    private fun setWeeklyAdapter(dialogBinding: WeatherDialogBinding) {
        weeklyAdapter = WeeklyAdapter(
            this,
            weeklyDataList
        )
        val horizontalManager = LinearLayoutManager(
            this
        )
        dialogBinding.weeklyRecycler.setHasFixedSize(true)
        dialogBinding.weeklyRecycler.isNestedScrollingEnabled = false
        dialogBinding.weeklyRecycler.layoutManager = horizontalManager
        dialogBinding.weeklyRecycler.itemAnimator = DefaultItemAnimator()
        dialogBinding.weeklyRecycler.adapter = weeklyAdapter
    }

    private fun setHourlyAdapter(dialogBinding: WeatherDialogBinding) {

        hourlyAdapter = HourlyAdapter(
            this,
            hourlyDataList
        )
        val horizontalManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        dialogBinding.hourlyRecycler.setHasFixedSize(true)
        dialogBinding.hourlyRecycler.isNestedScrollingEnabled = false
        dialogBinding.hourlyRecycler.layoutManager = horizontalManager
        dialogBinding.hourlyRecycler.itemAnimator = DefaultItemAnimator()
        dialogBinding.hourlyRecycler.adapter = hourlyAdapter

    }

    private fun setWeeklyWeatherData() {
        weeklyDataList.add(
            WeatherModel(
                15,
                30,
                "Sunny",
                31,
                R.drawable.ic_sunny,
                "Monday",
                "High"
            )
        )

        weeklyDataList.add(
            WeatherModel(
                15,
                31,
                "Sunny",
                30,
                R.drawable.ic_sunny,
                "Tuesday",
                "Low"
            )
        )


        weeklyDataList.add(
            WeatherModel(
                14,
                31,
                "Cloudy",
                33,
                R.drawable.ic_cloudy,
                "Wednesday",
                "High"
            )
        )

        weeklyDataList.add(
            WeatherModel(
                18,
                33,
                "Sunny",
                33,
                R.drawable.ic_sunny,
                "Thursday",
                "Low"

            )
        )


        weeklyDataList.add(
            WeatherModel(
                19,
                30,
                "Sunny",
                30,
                R.drawable.ic_sunny,
                "Friday",
                "Low"
            )
        )

        weeklyDataList.add(
            WeatherModel(
                21,
                35,
                "Cloudy",
                33,
                R.drawable.ic_cloudy,
                "Saturday",
                "Low"
            )
        )

        weeklyDataList.add(
            WeatherModel(
                18,
                34,
                "Cloudy",
                32,
                R.drawable.ic_cloudy,
                "Sunday",
                "High"
            )
        )
    }

    private fun setHourlyData() {
        hourlyDataList.add(
            WeatherModel(
                15,
                30,
                "sunny",
                31,
                R.drawable.ic_sunny,
                "Monday",
                "High"
            )
        )

        hourlyDataList.add(
            WeatherModel(
                15,
                30,
                "sunny",
                30,
                R.drawable.ic_sunny,
                "Monday",
                "High"
            )
        )

        hourlyDataList.add(
            WeatherModel(
                15,
                30,
                "sunny",
                31,
                R.drawable.ic_cloudy,
                "Monday",
                "High"
            )
        )
        hourlyDataList.add(
            WeatherModel(
                15,
                30,
                "sunny",
                29,
                R.drawable.ic_cloudy,
                "Monday",
                "Low"
            )
        )
        hourlyDataList.add(
            WeatherModel(
                15,
                30,
                "sunny",
                28,
                R.drawable.ic_cloudy,
                "Monday",
                "Low"
            )
        )

        hourlyDataList.add(
            WeatherModel(
                15,
                30,
                "sunny",
                31,
                R.drawable.ic_sunny,
                "Monday",
                "High"
            )
        )
        hourlyDataList.add(
            WeatherModel(
                15,
                30,
                "sunny",
                32,
                R.drawable.ic_cloudy,
                "Monday",
                "Low"
            )
        )


    }

}