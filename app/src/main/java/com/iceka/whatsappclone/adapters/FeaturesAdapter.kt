package com.iceka.whatsappclone.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iceka.whatsappclone.adapters.vh.FeaturesVH
import com.iceka.whatsappclone.databinding.ItemFeaturesBinding
import com.iceka.whatsappclone.models.ModelFeatures

class FeaturesAdapter(
    val context: Context,
    val featuresList: List<ModelFeatures>
) : RecyclerView.Adapter<FeaturesVH>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeaturesVH {
        return FeaturesVH(
            ItemFeaturesBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: FeaturesVH, position: Int) {
        val features = featuresList[position]
        holder.binding.apply {
            feature.text = features.text


        }
    }

    override fun getItemCount(): Int {
        return featuresList.size
    }
}