package com.iceka.whatsappclone

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationBarView
import com.iceka.whatsappclone.databinding.ActivityHomeBinding
import com.iceka.whatsappclone.newfrags.FragmentHome

class HomeActivity : AppCompatActivity() {
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
                }
                R.id.messaging -> {
                    loadFragment(ChatFragment())
                }
                R.id.home -> {
                    loadFragment(FragmentHome())
                }
                R.id.bag -> {
                }
                R.id.history -> {

                }
            }
            true
        }
}