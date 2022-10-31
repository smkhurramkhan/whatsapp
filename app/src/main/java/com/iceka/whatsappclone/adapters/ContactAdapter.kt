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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.iceka.whatsappclone.ChatRoomActivity
import com.iceka.whatsappclone.R
import com.iceka.whatsappclone.models.User
import de.hdodenhof.circleimageview.CircleImageView

class ContactAdapter(private val mContext: Context, private val contactList: List<User>) :
    RecyclerView.Adapter<ContactAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_contact_list, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentUser = contactList[position]
        val userReference = FirebaseDatabase.getInstance().reference.child("users")
        userReference.child(currentUser.uid).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue(
                    User::class.java
                )
                holder.mContactItem.setOnClickListener {
                    val intent = Intent(mContext, ChatRoomActivity::class.java)
                    intent.putExtra(ChatRoomActivity.EXTRAS_USER, user)
                    intent.putExtra("userUid", currentUser.uid)
                    mContext.startActivity(intent)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
        holder.mTvUsername.text = currentUser.username
        holder.mTvAbout.text = currentUser.about
        holder.mAvatar.visibility = View.VISIBLE
        Glide.with(holder.mAvatar.context)
            .load(currentUser.photoUrl)
            .into(holder.mAvatar)
    }

    override fun getItemCount(): Int {
        return contactList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mTvUsername: TextView
        val mTvAbout: TextView
        val mAvatar: CircleImageView
        val mContactItem: RelativeLayout

        init {
            mTvUsername = itemView.findViewById(R.id.tv_username_contact)
            mTvAbout = itemView.findViewById(R.id.tv_about_contact)
            mAvatar = itemView.findViewById(R.id.avatar_user_contact)
            mContactItem = itemView.findViewById(R.id.item_contact_layout)
        }
    }
}