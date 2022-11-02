package com.iceka.whatsappclone.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.devlomi.circularstatusview.CircularStatusView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.iceka.whatsappclone.R
import com.iceka.whatsappclone.ShowOtherStatusActivity
import com.iceka.whatsappclone.models.Status
import com.iceka.whatsappclone.models.User
import de.hdodenhof.circleimageview.CircleImageView

class StatusAdapter(private val mContext: Context, private val statusList: List<Status>) :
    RecyclerView.Adapter<StatusAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_status, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val mUserReference = FirebaseDatabase.getInstance().reference.child("users")
        val currentStatus = statusList[position]
        val userUid = currentStatus.uid
        mUserReference.child(userUid!!).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                val user = dataSnapshot.getValue(
                    User::class.java
                )
                holder.mUsername.text = user!!.username
                Glide.with(holder.mThumbnail.context)
                    .load(user.photoUrl)
                    .into(holder.mThumbnail)
                //                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
        holder.mLayout.setOnClickListener {
            val intent = Intent(mContext, ShowOtherStatusActivity::class.java)
            intent.putExtra("uid", userUid)
            mContext.startActivity(intent)
        }
        holder.mStatusCount.setPortionsCount(currentStatus.statuscount)
    }

    override fun getItemCount(): Int {
        return statusList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mThumbnail: CircleImageView
        val mStatusCount: CircularStatusView
         val mUsername: TextView
         val mDate: TextView
        val mLayout: RelativeLayout

        init {
            mThumbnail = itemView.findViewById(R.id.thumbnail_status)
            mStatusCount = itemView.findViewById(R.id.circular_status_counts)
            mUsername = itemView.findViewById(R.id.username_status)
            mDate = itemView.findViewById(R.id.date_status)
            mLayout = itemView.findViewById(R.id.layout_other_status)
        }
    }
}