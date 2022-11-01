package com.iceka.whatsappclone

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.iceka.whatsappclone.models.StatusItem
import java.io.ByteArrayOutputStream
import java.util.concurrent.TimeUnit

class EditStatusActivity : AppCompatActivity() {
    private var mImageView: ImageView? = null
    private var mEditText: EditText? = null
    private var mFab: FloatingActionButton? = null
    private var mFirebaseAuth: FirebaseAuth? = null
    private var mFirebaseUser: FirebaseUser? = null
    private var mFirebaseDatabase: FirebaseDatabase? = null
    private var mFirebaseStorage: FirebaseStorage? = null
    private var mStatusReference: DatabaseReference? = null
    private var mStatusStorageReference: StorageReference? = null
    private var imageSource: Uri? = null
    private var imageResourceFromString: String? = null
    private var previousActivity: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_status)
        mImageView = findViewById(R.id.preview_image)
        mEditText = findViewById(R.id.et_caption)
        mFab = findViewById(R.id.fab_send_status)
        mFirebaseDatabase = FirebaseDatabase.getInstance()
        mFirebaseStorage = FirebaseStorage.getInstance()
        mFirebaseAuth = FirebaseAuth.getInstance()
        mFirebaseUser = mFirebaseAuth!!.currentUser
        mStatusReference = mFirebaseDatabase!!.reference.child("status")
        mStatusStorageReference = mFirebaseStorage!!.reference.child("status").child(
            mFirebaseUser!!.uid
        )
        imageSource = intent.data
        imageResourceFromString = intent.getStringExtra("file")
        previousActivity = intent.getStringExtra("from_activity")
        if (imageSource == null) {
            Glide.with(this)
                .load(imageResourceFromString)
                .into(mImageView!!)
        } else if (imageResourceFromString == null) {
            Glide.with(this)
                .load(imageSource)
                .into(mImageView!!)
        }
        mFab?.setOnClickListener { sendMediaStatus() }
    }

    private fun sendMediaStatus() {
        val timestamp = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())
        val expireTime = timestamp + TimeUnit.MILLISECONDS.convert(1, TimeUnit.MINUTES)
        mImageView?.isDrawingCacheEnabled = true
        mImageView?.buildDrawingCache()
        val bitmap = mImageView?.drawingCache
        val stream = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val byteArray = stream.toByteArray()
        val encoded = Base64.encodeToString(byteArray, Base64.DEFAULT)
        val mediaReference = mStatusStorageReference!!.child(imageSource!!.lastPathSegment!!)
        mediaReference.putFile(imageSource!!).addOnSuccessListener {
            mediaReference.downloadUrl.addOnSuccessListener { uri ->
                val newRef =
                    mStatusReference!!.child(mFirebaseUser!!.uid).child("statusItem").push()
                val statusItem = StatusItem(
                    newRef.key,
                    "image",
                    uri.toString(),
                    mEditText!!.text.toString(),
                    timestamp,
                    expireTime,
                    encoded,
                    null
                )
                newRef.setValue(statusItem)
                finish()
            }
        }
    }
}