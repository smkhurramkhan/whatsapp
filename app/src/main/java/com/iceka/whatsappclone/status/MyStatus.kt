package com.iceka.whatsappclone.status

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.iceka.whatsappclone.R
import com.iceka.whatsappclone.databinding.ActivityHomeBinding
import com.iceka.whatsappclone.databinding.ActivityMyStatusBinding

class MyStatus : AppCompatActivity()
{
    private lateinit var binding: ActivityMyStatusBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyStatusBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}