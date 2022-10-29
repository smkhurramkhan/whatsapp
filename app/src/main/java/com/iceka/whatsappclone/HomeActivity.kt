package com.iceka.whatsappclone

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationBarView
import com.iceka.whatsappclone.databinding.ActivityHomeBinding
import com.iceka.whatsappclone.fragments.ChatTabFragment
import com.iceka.whatsappclone.newfrags.FragmentHome


class HomeActivity : AppCompatActivity() {
    var doubleBackToExitPressedOnce = false
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
            }
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
        binding.navView.itemIconTintList = null

        loadFragment(FragmentHome())
        bottomNavigationClicks()

        onNavHeaderClicks()
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
                Toast.makeText(this, "Coming Soon", Toast.LENGTH_SHORT).show()
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
}