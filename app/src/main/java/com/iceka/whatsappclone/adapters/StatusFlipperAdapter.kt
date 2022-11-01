package com.iceka.whatsappclone.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.firebase.database.FirebaseDatabase
import com.iceka.whatsappclone.R
import com.iceka.whatsappclone.models.StatusItem
import com.iceka.whatsappclone.models.Viewed

class StatusFlipperAdapter(
    private val mContext: Context,
    private val statusItemList: List<StatusItem>
) : BaseAdapter() {
    private val viewedList: List<Viewed>? = null
    override fun getCount(): Int {
        return statusItemList.size
    }

    override fun getItem(p0: Int): Int {
        return 0
    }

    override fun getItemId(i: Int): Long {
        return 0
    }

    override fun getView(i: Int, view: View, viewGroup: ViewGroup): View {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_status_text, viewGroup, false)
        val image = view.findViewById<ImageView>(R.id.img_status_picture)
        val layout = view.findViewById<RelativeLayout>(R.id.layout_item_status_text)
        val text = view.findViewById<TextView>(R.id.tv_text_item_status)
        val viewCount = view.findViewById<TextView>(R.id.tv_seen_count)
        val statusItem = statusItemList[i]
        if (viewedList != null) {
            Log.i("MYTAG", "viewed : " + viewedList.size)
        }
        if (statusItem.type == "image") {
            image.visibility = View.VISIBLE
            Glide.with(mContext.applicationContext)
                .load(statusItem.url)
                .into(image)
        } else if (statusItem.type == "text") {
            image.visibility = View.INVISIBLE
            text.visibility = View.VISIBLE
            layout.setBackgroundColor(statusItem.backgroundColor)
            text.text = statusItem.text
            val vieweds: MutableList<Viewed> = ArrayList()
            if (statusItem.viewed != null) {
                vieweds.add(statusItem.viewed)
            }
            viewCount.text = vieweds.size.toString()
        }
        return view
    }
}