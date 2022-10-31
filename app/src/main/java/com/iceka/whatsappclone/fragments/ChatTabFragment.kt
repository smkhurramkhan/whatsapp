package com.iceka.whatsappclone.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.iceka.whatsappclone.ContactActivity
import com.iceka.whatsappclone.R
import com.iceka.whatsappclone.adapters.ChatListAdapter
import com.iceka.whatsappclone.models.Conversation

class ChatTabFragment : Fragment() {
    private var mRecyclerView: RecyclerView? = null
    private var mStartChatLayout: LinearLayout? = null
    private var mConversationReference: DatabaseReference? = null
    private var mAdapter: ChatListAdapter? = null
    private val conversationList: MutableList<Conversation> = mutableListOf()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_chat_tab, container, false)
        mRecyclerView = rootView.findViewById(R.id.recyvlerview_chat_tab)
        mStartChatLayout = rootView.findViewById(R.id.layout_start_chat)
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        mRecyclerView?.layoutManager = linearLayoutManager
        mRecyclerView?.itemAnimator = DefaultItemAnimator()
        val mFabBottom = rootView.findViewById<FloatingActionButton>(R.id.fab_bottom)
        val mFirebaseDatabase = FirebaseDatabase.getInstance()
        val mAuth = FirebaseAuth.getInstance()
        val mFirebaseUser = mAuth.currentUser
        mConversationReference = mFirebaseDatabase.reference.child("conversation").child(
            mFirebaseUser!!.uid
        )
        data
        mFabBottom.setOnClickListener {
            startActivity(
                Intent(
                    requireContext(),
                    ContactActivity::class.java
                )
            )
        }
        return rootView
    }

    private val data: Unit
        get() {
            val myQuery = mConversationReference!!.orderByChild("timestamp")
            myQuery.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    conversationList.clear()
                    if (dataSnapshot.exists()) {
                        mStartChatLayout!!.visibility = View.GONE
                        for (snapshot in dataSnapshot.children) {
                            val conversation = snapshot.getValue(
                                Conversation::class.java
                            )
                            conversation?.let { conversationList.add(it) }
                            mAdapter = ChatListAdapter(requireActivity(), conversationList)
                            mRecyclerView?.adapter = mAdapter
                        }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
        }
}