package com.iceka.whatsappclone.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iceka.whatsappclone.adapters.vh.HourlyVH
import com.iceka.whatsappclone.databinding.ItemHourlyWeatherBinding
import com.iceka.whatsappclone.models.WeatherModel

class HourlyAdapter(
    var context: Context,
    var hourlyList: MutableList<WeatherModel>
) : RecyclerView.Adapter<HourlyVH>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyVH {
        return HourlyVH(
            ItemHourlyWeatherBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: HourlyVH, position: Int) {
        val hourly = hourlyList[position]

        holder.binding.apply {
            Glide.with(context)
                .load(hourly.image)
                .into(weatherImage)

            weatherDegree.text = "${hourly.tempDegree}"
            weatherhighorLow.text = hourly.isHighOrLow

        }
    }

    override fun getItemCount(): Int {
        return hourlyList.size
    }
}