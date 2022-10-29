package com.iceka.whatsappclone.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iceka.whatsappclone.adapters.vh.SliderVH
import com.iceka.whatsappclone.databinding.ItemSlidersBinding
import com.iceka.whatsappclone.models.ModelSlider

class HomeSliderAdapter(
    var context: Context,
    var dataList: List<ModelSlider>
) : RecyclerView.Adapter<SliderVH>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderVH {
        return SliderVH(
            ItemSlidersBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: SliderVH, position: Int) {
        val model = dataList[position]

        holder.binding.apply {
            Glide.with(context)
                .load(model.icon)
                .into(imageView)
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}