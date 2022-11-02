package com.iceka.whatsappclone.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.text.format.DateFormat
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.iceka.whatsappclone.ChatRoomActivity
import com.iceka.whatsappclone.R
import com.iceka.whatsappclone.models.Conversation
import com.iceka.whatsappclone.models.User
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*

class ChatListAdapter(
    private val mContext: Context,
    private val conversationList: List<Conversation>
) : RecyclerView.Adapter<ChatListAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chats, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: MyViewHolder,
        @SuppressLint("RecyclerView") position: Int
    ) {
        val mUserReference = FirebaseDatabase.getInstance().reference.child("users")
        val conversation = conversationList[position]
        val id = conversation.chatWithId
        id?.let {
            mUserReference.child(it).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val user = dataSnapshot.getValue(
                        User::class.java
                    )
                    holder.username.text = user!!.username
                    Glide.with(mContext)
                        .load(user.photoUrl)
                        .into(holder.avatar)
                    holder.layout.setOnClickListener {
                        val conversation1 = conversationList[position]
                        clearUnreadChat(conversation1.chatWithId!!)
                        val intent = Intent(mContext, ChatRoomActivity::class.java)
                        intent.putExtra(ChatRoomActivity.EXTRAS_USER, user)
                        intent.putExtra("userUid", conversation1.chatWithId)
                        intent.putExtra("otherUid", user.uid)
                        mContext.startActivity(intent)
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
        }
        holder.message.text = conversation.lastMessage
        if (conversation.unreadChatCount == 0) {
            holder.unreadCount.visibility = View.GONE
        } else {
            holder.unreadCount.text = conversation.unreadChatCount.toString()
        }
        val calendar = Calendar.getInstance(Locale.ENGLISH)
        calendar.timeInMillis = conversation.timestamp * 1000
        val tes = calendar.timeInMillis
        DateFormat.format("M/dd/yyyy", calendar)
        val now = DateUtils.getRelativeTimeSpanString(
            tes,
            System.currentTimeMillis(),
            DateUtils.MINUTE_IN_MILLIS
        )
        holder.chatTime.text = now
    }

    override fun getItemCount(): Int {
        return conversationList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val username: TextView
        val message: TextView
        val avatar: CircleImageView
        val layout: RelativeLayout
        val unreadCount: TextView
        val chatTime: TextView

        init {
            username = itemView.findViewById(R.id.tv_username)
            message = itemView.findViewById(R.id.tv_message)
            avatar = itemView.findViewById(R.id.avatar_user)
            layout = itemView.findViewById(R.id.layout_user_chat)
            unreadCount = itemView.findViewById(R.id.tv_unread_count)
            chatTime = itemView.findViewById(R.id.tv_chat_time)
        }
    }

    private fun clearUnreadChat(chatWithId: String) {
        val mFirebaseUser = FirebaseAuth.getInstance().currentUser
        val conversationReference =
            FirebaseDatabase.getInstance().reference.child("conversation").child(
                mFirebaseUser!!.uid
            ).child(chatWithId).child("unreadChatCount")
        conversationReference.setValue(0)
    }
}