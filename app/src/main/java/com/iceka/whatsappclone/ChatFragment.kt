package com.iceka.whatsappclone

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.iceka.whatsappclone.adapters.ChatRoomAdapter
import com.iceka.whatsappclone.models.Chat
import com.iceka.whatsappclone.models.Conversation
import java.util.concurrent.TimeUnit

class ChatFragment : Fragment() {
    private var mFirebaseDatabase: FirebaseDatabase? = null
    private var mFirebaseAuth: FirebaseAuth? = null
    private var mFirebaseUser: FirebaseUser? = null
    private var mChatReference: DatabaseReference? = null
    private var mConversationReference: DatabaseReference? = null
    private var id: String? = null
    private var userUid: String? = null
    private var chatId: String? = null
    private var unreadCount = 0
    private var mMessageText: EditText? = null
    private var mFab: FloatingActionButton? = null
    private var mRecyclerView: RecyclerView? = null
    private var mAttachPict: ImageView? = null
    private var adapters: ChatRoomAdapter? = null
    private val chatList: MutableList<Chat> = ArrayList()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_chat, container, false)
        mMessageText = view.findViewById(R.id.et_message_chat)
        mFab = view.findViewById(R.id.fab_chat)
        mRecyclerView = view.findViewById(R.id.rv_chat)
        mAttachPict = view.findViewById(R.id.img_attach_picture)
        val layoutManager = LinearLayoutManager(context)
        mRecyclerView?.layoutManager = layoutManager
        mRecyclerView?.itemAnimator = DefaultItemAnimator()
        mFirebaseDatabase = FirebaseDatabase.getInstance()
        mFirebaseAuth = FirebaseAuth.getInstance()
        mFirebaseUser = mFirebaseAuth!!.currentUser
        id = ChatRoomActivity.idFromContact
        userUid = mFirebaseUser?.uid
        chatId = if (userUid!!.compareTo(id!!) < id!!.compareTo(userUid!!)) {
            userUid + id
        } else {
            id + userUid
        }
        mChatReference = mFirebaseDatabase!!.reference.child("chats").child(chatId!!)
        mConversationReference = mFirebaseDatabase!!.reference.child("conversation")
        mMessageText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if (charSequence.isEmpty()) {
                    showSendButton()
                    mAttachPict?.visibility = View.GONE
                    mFab?.setOnClickListener {
                        val contoh = mMessageText?.text.toString()
                        val timestamp = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())
                        val chat = Chat(contoh, mFirebaseUser?.uid, id, timestamp)
                        mChatReference?.push()?.setValue(chat)
                        mMessageText?.setText("")
                        unreadCount += 1
                        val conversationSender =
                            Conversation(mFirebaseUser?.uid, id, contoh, timestamp)
                        val conversationReceiver =
                            Conversation(id, mFirebaseUser?.uid, contoh, timestamp, unreadCount)
                        val senderReference = mConversationReference!!.child(
                            mFirebaseUser!!.uid
                        ).child(id!!)
                        senderReference.setValue(conversationSender)
                        val receiverReference = mConversationReference!!.child(id!!).child(
                            mFirebaseUser?.uid!!
                        )
                        receiverReference.setValue(conversationReceiver)
                    }
                }
            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                if (editable.isEmpty()) {
                    showVoiceButton()
                    mAttachPict?.visibility = View.VISIBLE
                    mFab?.setOnClickListener { }
                }
            }
        })
        mChatReference?.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                val chat = dataSnapshot.getValue(Chat::class.java)
                chat?.let { chatList.add(it) }
                adapters = ChatRoomAdapter(requireActivity(), chatList)
                mRecyclerView?.smoothScrollToPosition(chatList.size - 1)
                mRecyclerView?.adapter = adapters
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {}
            override fun onChildRemoved(dataSnapshot: DataSnapshot) {}
            override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {}
            override fun onCancelled(databaseError: DatabaseError) {}
        })
        return view
    }

    private fun showSendButton() {
        mFab?.setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.ic_send_black_24dp))
        mFab?.tag = "send_image"
    }

    private fun showVoiceButton() {
        mFab?.setImageDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.ic_keyboard_voice_black_24dp
            )
        )
        mFab?.tag = "mic_image"
    }

    companion object {
        fun newInstance(bundle: Bundle?): ChatFragment {
            val fragment = ChatFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}