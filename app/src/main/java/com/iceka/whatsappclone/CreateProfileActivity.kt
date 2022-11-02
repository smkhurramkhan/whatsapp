package com.iceka.whatsappclone

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.iceka.whatsappclone.models.User
import de.hdodenhof.circleimageview.CircleImageView

class CreateProfileActivity : AppCompatActivity() {
    private var mUserReference: DatabaseReference? = null
    private var mFirebaseUser: FirebaseUser? = null
    private var mStorageReference: StorageReference? = null
    private var mEtUsername: EditText? = null
    private var mImgAvatar: CircleImageView? = null
    private var mProgressBar: ProgressBar? = null
    private var selectedImage: Uri? = null
    private val defaultProfileAbout = "Hello there! I am using JAZZ T-VAS APP."


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_profile)
        val mBtNext = findViewById<TextView>(R.id.bt_next_main)
        mEtUsername = findViewById(R.id.et_create_profile_username)
        mImgAvatar = findViewById(R.id.img_avatar_create)
        mProgressBar = findViewById(R.id.progressbar_create_profile)
        val mFirebaseDatabase = FirebaseDatabase.getInstance()
        val mFirebaseStorage = FirebaseStorage.getInstance()
        val mFirebaseAuth = FirebaseAuth.getInstance()
        mFirebaseUser = mFirebaseAuth.currentUser
        mUserReference = mFirebaseDatabase.reference.child("users").child(mFirebaseUser!!.uid)
        mStorageReference = mFirebaseStorage.reference.child("avatar").child(mFirebaseUser!!.uid)
        mImgAvatar?.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/jpeg"
            intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
            startActivityForResult(
                Intent.createChooser(intent, "Complete action using"),
                RC_PHOTO_PICKER
            )
        }
        mBtNext.setOnClickListener {
            mProgressBar?.visibility = View.VISIBLE
            createUserData()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK) {
            if (data != null) {
                selectedImage = data.data
                mImgAvatar!!.visibility = View.VISIBLE
                Glide.with(this)
                    .load(selectedImage)
                    .into(mImgAvatar!!)
            } else {
                Toast.makeText(this, "data is null", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
        }
    }

    private fun createUserData() {
        mUserReference!!.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (!dataSnapshot.exists()) {
                    val photoRef = mStorageReference!!.child(selectedImage!!.lastPathSegment!!)
                    photoRef.putFile(selectedImage!!)
                        .addOnSuccessListener { taskSnapshot: UploadTask.TaskSnapshot? ->
                            photoRef.downloadUrl.addOnSuccessListener { uri ->
                                val username = mEtUsername!!.text.toString()
                                val profileUpdates = UserProfileChangeRequest.Builder()
                                    .setDisplayName(username)
                                    .setPhotoUri(uri)
                                    .build()
                                mFirebaseUser!!.updateProfile(profileUpdates)
                                    .addOnCompleteListener { task: Task<Void?> ->
                                        if (task.isSuccessful) {
                                            val intent = Intent(
                                                this@CreateProfileActivity,
                                                HomeActivity::class.java
                                            )
                                            intent.flags =
                                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                            startActivity(intent)
                                        }
                                    }
                                val user = User(
                                    mFirebaseUser!!.uid,
                                    username,
                                    mFirebaseUser!!.phoneNumber,
                                    uri.toString(),
                                    defaultProfileAbout,
                                    true,
                                    0
                                )
                                mUserReference!!.setValue(user)
                                mProgressBar!!.visibility = View.GONE
                            }
                        }
                } else {
                    val intent = Intent(this@CreateProfileActivity, HomeActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(
                    this@CreateProfileActivity,
                    "Error : " + databaseError.message,
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }

    companion object {
        private const val RC_PHOTO_PICKER = 2
    }
}