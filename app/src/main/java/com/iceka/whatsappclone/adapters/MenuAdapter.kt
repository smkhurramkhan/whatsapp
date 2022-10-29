package com.iceka.whatsappclone.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iceka.whatsappclone.adapters.vh.MenuVH
import com.iceka.whatsappclone.databinding.ItemSubMenuBinding
import com.iceka.whatsappclone.models.MenuModel

class MenuAdapter(
    val context: Context,
    val dataList: List<MenuModel>,
    val onClick: (position: Int) -> Unit
) : RecyclerView.Adapter<MenuVH>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuVH {
        return MenuVH(
            ItemSubMenuBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MenuVH, position: Int) {
        val menu = dataList[position]

        holder.binding.apply {
            Glide.with(context)
                .load(menu.icon)
                .into(imageView)

            itemName.text = menu.title
            if (menu.isNew) {
                newBg.visibility = View.VISIBLE
            } else {
                newBg.visibility = View.GONE
            }
        }


        holder.itemView.setOnClickListener {
            onClick(position)
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}