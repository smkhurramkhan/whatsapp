package com.iceka.whatsappclone.newfrags

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.iceka.whatsappclone.databinding.FragmentUserBinding
import com.iceka.whatsappclone.databinding.HomeFragmentBinding
import com.iceka.whatsappclone.models.User

class FragmentUser : Fragment() {


    private lateinit var binding : FragmentUserBinding
    private var userUid: String? = null
    private var mFirebaseDatabase: FirebaseDatabase? = null
    private var mFirebaseAuth: FirebaseAuth? = null
    private var mFirebaseUser: FirebaseUser? = null
    private var mUserReference: DatabaseReference? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserBinding.inflate(layoutInflater)
        return binding.root
        //Toast.makeText(requireActivity(),"sdfsdfes fdfd",Toast.LENGTH_LONG).show()
        mFirebaseDatabase = FirebaseDatabase.getInstance()
        mFirebaseAuth = FirebaseAuth.getInstance()
        mFirebaseUser = mFirebaseAuth?.currentUser

        userUid = mFirebaseUser?.uid
        mUserReference = mFirebaseDatabase?.reference?.child("users")
        getUserDetails()

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun getUserDetails() {
        mUserReference?.child(userUid!!)
            ?.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val user = dataSnapshot.getValue(
                        User::class.java
                    )

                    binding.etCreateProfileUsername?.text = user?.username
                    activity?.let {
                        binding.imgAvatarCreate?.let { it1 ->
                            Glide.with(it)
                                .load(user?.photoUrl)
                                .into(it1)
                        }
                    }


                    binding.etCreateProfilePhoneNumber?.text = user?.phone
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
    }

}