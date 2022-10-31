package com.iceka.whatsappclone.adapters

import android.content.Context
import android.util.Log
import com.iceka.whatsappclone.models.Chat
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import com.iceka.whatsappclone.R
import com.iceka.whatsappclone.adapters.ChatRoomAdapter.IncomingViewHolder
import com.iceka.whatsappclone.adapters.ChatRoomAdapter.OutgoingViewHolder
import androidx.constraintlayout.widget.ConstraintSet
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import android.widget.LinearLayout

class ChatRoomAdapter(private val mContext: Context, var chatList: List<Chat>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val TYPE_INCOMING = 1
    private val TYPE_OUTGOING = 2
    override fun getItemViewType(position: Int): Int {
        val chat = chatList[position]
        return if (tes(chat)) {
            TYPE_OUTGOING
        } else TYPE_INCOMING
    }

    private fun tes(chat: Chat): Boolean {
        val mAuth = FirebaseAuth.getInstance()
        return mAuth.currentUser!!.uid.equals(chat.senderUid, ignoreCase = true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View
        return if (viewType == TYPE_INCOMING) {
            view =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_chat_incoming, parent, false)
            IncomingViewHolder(view)
        } else {
            view =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_chat_outgoing, parent, false)
            OutgoingViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == TYPE_INCOMING) {
            val holder1 = holder as IncomingViewHolder
            configureViewHolderIncoming(holder1, position)
        } else {
            val holder2 = holder as OutgoingViewHolder
            configureViewholderOutgoing(holder2, position)
        }
    }

    override fun getItemCount(): Int {
        return chatList.size
    }

    private fun configureViewHolderIncoming(holder: IncomingViewHolder, position: Int) {
        val chat = chatList[position]
        holder.message.text = chat.message
        holder.message.post {
            val linecount = holder.message.lineCount
            val constraintSet = ConstraintSet()
            constraintSet.clone(holder.constraintLayout)
            if (linecount == 1) {
                constraintSet.connect(
                    R.id.layout_chat_incoming,
                    ConstraintSet.END,
                    R.id.tv_time_chat_incoming,
                    ConstraintSet.START,
                    10
                )
                constraintSet.applyTo(holder.constraintLayout)
            }
            Log.i("MYTAG", "Lines : $linecount")
        }
    }

    private fun configureViewholderOutgoing(holder: OutgoingViewHolder, position: Int) {
        val chat = chatList[position]
        holder.message.text = chat.message
        holder.message.post {
            val linecount = holder.message.lineCount
            val constraintSet = ConstraintSet()
            constraintSet.clone(holder.constraintLayout)
            if (linecount == 1) {
                constraintSet.connect(
                    R.id.layout_chat,
                    ConstraintSet.END,
                    R.id.tv_time_chat_outgoing,
                    ConstraintSet.START,
                    10
                )
                constraintSet.applyTo(holder.constraintLayout)
            }
            Log.i("MYTAG", "Lines : $linecount")
        }
    }

    inner class IncomingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val message: TextView
        val constraintLayout: ConstraintLayout
         val layout: LinearLayout
         val time: TextView

        init {
            message = itemView.findViewById(R.id.tv_chat_incoming)
            constraintLayout = itemView.findViewById(R.id.layout_first_incoming)
            layout = itemView.findViewById(R.id.layout_chat_incoming)
            time = itemView.findViewById(R.id.tv_time_chat_incoming)
        }
    }

    inner class OutgoingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val message: TextView
        val constraintLayout: ConstraintLayout
        private val layout: LinearLayout
        private val time: TextView

        init {
            message = itemView.findViewById(R.id.tv_chat_outgoing)
            constraintLayout = itemView.findViewById(R.id.layout_first)
            layout = itemView.findViewById(R.id.layout_chat)
            time = itemView.findViewById(R.id.tv_time_chat_outgoing)
        }
    }
}