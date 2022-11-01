package com.iceka.whatsappclone

import android.graphics.Bitmap
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.iceka.whatsappclone.models.StatusItem
import java.io.ByteArrayOutputStream
import java.util.*

class StatusTextActivity : AppCompatActivity() {
    private var mText: EditText? = null
    private var mFabPost: FloatingActionButton? = null
    private var mLayout: RelativeLayout? = null
    private var mColorChange: ImageView? = null
    private var mStatusReference: DatabaseReference? = null
    private var mFirebaseAuth: FirebaseAuth? = null
    private var mFirebaseUser: FirebaseUser? = null
    private var myId: String? = null
    private var p = 0
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_status_text)
        mText = findViewById(R.id.et_text_status)
        mFabPost = findViewById(R.id.fab_status_text)
        mLayout = findViewById(R.id.layout_text_status)
        mColorChange = findViewById(R.id.icon_color_status)
        val mFirebaseDatabase = FirebaseDatabase.getInstance()
        mFirebaseAuth = FirebaseAuth.getInstance()
        mFirebaseUser = mFirebaseAuth!!.currentUser
        mStatusReference = mFirebaseDatabase.reference.child("status")
        myId = mFirebaseUser!!.uid
        mText?.requestFocus()
        val colorBackgrounds =
            this@StatusTextActivity.resources.getIntArray(R.array.statusBackground)
        val randomColor = colorBackgrounds[Random().nextInt(colorBackgrounds.size)]
        mLayout?.setBackgroundColor(randomColor)
        mColorChange?.setOnClickListener {
            p++
            if (p < colorBackgrounds.size) {
                mLayout?.setBackgroundColor(colorBackgrounds[p])
            } else {
                p = 0
                mLayout?.setBackgroundColor(colorBackgrounds[p])
            }
        }
        mText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if (charSequence.isEmpty()) {
                    mFabPost?.show()
                }
            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                if (editable.isEmpty()) {
                    mFabPost?.hide()
                }
            }
        })
        mFabPost?.setOnClickListener {
            postData()
            finish()
        }
    }

    private fun postData() {
        val backgroundColor = (mLayout!!.background as ColorDrawable).color
        val timestamp = System.currentTimeMillis()
        val newRef = mStatusReference?.child(mFirebaseUser!!.uid)?.child("statusItem")?.push()
        mLayout?.isDrawingCacheEnabled = true
        mLayout?.buildDrawingCache()
        val bitmap = mLayout?.drawingCache
        val stream = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val byteArray = stream.toByteArray()
        val encoded = Base64.encodeToString(byteArray, Base64.DEFAULT)

        val statusItem = StatusItem(
            newRef?.key,
            "text",
            mText?.text.toString(),
            timestamp,
            timestamp,
            backgroundColor,
            encoded,
            null
        )
        mStatusReference?.child(mFirebaseUser?.uid!!)?.child("uid")
            ?.setValue(mFirebaseUser?.uid)
        mStatusReference?.child(mFirebaseUser?.uid!!)?.child("allseen")?.removeValue()
        newRef?.setValue(statusItem)
    }
}