package com.iceka.whatsappclone.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iceka.whatsappclone.adapters.vh.WeeklyVH
import com.iceka.whatsappclone.databinding.ItemWeeklyWeatherBinding
import com.iceka.whatsappclone.models.WeatherModel

class WeeklyAdapter(
    val context: Context,
    val weeklyList: List<WeatherModel>
) : RecyclerView.Adapter<WeeklyVH>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeeklyVH {
        return WeeklyVH(
            ItemWeeklyWeatherBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: WeeklyVH, position: Int) {
        val weekly = weeklyList[position]

        holder.binding.apply {
            Glide.with(context)
                .load(weekly.image)
                .into(weatherIcon)

            day.text = weekly.day
            lowTemp.text = "${weekly.lowTemp}"
            highTemp.text = "${weekly.hightTemp}"


        }
    }

    override fun getItemCount(): Int {
        return weeklyList.size
    }
}