package com.iceka.whatsappclone

import android.animation.Animator
import android.animation.ObjectAnimator
import android.os.Bundle
import android.text.format.DateFormat
import android.text.format.DateUtils
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.view.animation.LinearInterpolator
import android.widget.AdapterViewFlipper
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.iceka.whatsappclone.adapters.StatusFlipperAdapter
import com.iceka.whatsappclone.models.StatusItem
import com.iceka.whatsappclone.models.User
import com.iceka.whatsappclone.models.Viewed
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*
import java.util.concurrent.TimeUnit

class ShowOtherStatusActivity : AppCompatActivity() {
    private var mAdapterViewFlipper: AdapterViewFlipper? = null
    private var mSeenCount: TextView? = null
    private var mToolbar: Toolbar? = null
    private var mAvatarUser: CircleImageView? = null
    private var mUsername: TextView? = null
    private var mDate: TextView? = null
    private var mViewedCount: LinearLayout? = null
    private lateinit var mProgressBar: Array<ProgressBar?>
    private var mAdapter: StatusFlipperAdapter? = null
    private var mStatusReference: DatabaseReference? = null
    private var mFirebaseAuth: FirebaseAuth? = null
    private var mFirebaseUser: FirebaseUser? = null
    private var mUserReference: DatabaseReference? = null
    private var myId: String? = null
    private var flipperCount = 0
    private var animation: ObjectAnimator? = null
    private var id: String? = null
    private val statusItemList = mutableListOf<StatusItem>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_other_status)
        mAdapterViewFlipper = findViewById(R.id.status_view_flipper)
        mSeenCount = findViewById(R.id.tv_seen_count)
        mToolbar = findViewById(R.id.toolbar_status)
        mViewedCount = findViewById(R.id.layout_viewed_by_status)
        mAvatarUser = findViewById(R.id.avatar_status_user)
        mDate = findViewById(R.id.tv_status_date)
        mUsername = findViewById(R.id.tv_status_username)
        val firebaseDatabase = FirebaseDatabase.getInstance()
        mFirebaseAuth = FirebaseAuth.getInstance()
        mFirebaseUser = mFirebaseAuth!!.currentUser
        myId = mFirebaseUser!!.uid
        mStatusReference = firebaseDatabase.reference.child("status")
        mUserReference = firebaseDatabase.reference.child("users")
        id = intent.getStringExtra("uid")
        mToolbar?.bringToFront()
        showStatus()
    }

    private fun showStatus() {
        mUserReference!!.child(id!!).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue(
                    User::class.java
                )
                mUsername!!.text = user!!.username
                Glide.with(applicationContext)
                    .load(user.photoUrl)
                    .into(mAvatarUser!!)
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
        mStatusReference!!.child(id!!).child("statusItem")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (snapshot in dataSnapshot.children) {
                        val statusItem = snapshot.getValue(StatusItem::class.java)
                        statusItem?.let { statusItemList.add(it) }
                        mAdapter = StatusFlipperAdapter(applicationContext, statusItemList)
                        mAdapterViewFlipper?.adapter = mAdapter
                        mAdapterViewFlipper?.flipInterval = 2500
                        flipperCount = mAdapterViewFlipper!!.count
                        mAdapterViewFlipper?.startFlipping()
                    }
                    mProgressBar = arrayOfNulls(flipperCount)
                    for (i in 0 until flipperCount) {
                        mProgressBar[i] = ProgressBar(
                            applicationContext,
                            null,
                            android.R.attr.progressBarStyleHorizontal
                        )
                        mProgressBar[i]?.layoutParams =
                            LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1F)
                        setMargins(mProgressBar[i])
                        setProgressMax(mProgressBar[i])
                        mProgressBar[i]?.progress
                        val mViewGroup = findViewById<ViewGroup>(R.id.parent_progress_bar_layout)
                        mViewGroup.addView(mProgressBar[i])
                        mAdapterViewFlipper?.addOnLayoutChangeListener { view, i, i1, i2, i3, i4, i5, i6, i7 ->
                            setProgressAnimate(mProgressBar[mAdapterViewFlipper!!.displayedChild])
                            val statusItem = statusItemList[mAdapterViewFlipper!!.displayedChild]
                            val timeFromServer = statusItem.timestamp
                            val calendar = Calendar.getInstance(Locale.ENGLISH)
                            calendar.timeInMillis = timeFromServer * 1000
                            val co = calendar.timeInMillis
                            DateFormat.format("M/dd/yyyy", calendar)
                            val now = DateUtils.getRelativeTimeSpanString(
                                co,
                                System.currentTimeMillis(),
                                DateUtils.MINUTE_IN_MILLIS
                            )
                            mDate?.text = now
                            val timestamp =
                                TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())
                            val viewed = Viewed(myId, timestamp)
                            mStatusReference?.child(id!!)?.child("statusItem")
                                ?.child(statusItem.id!!)
                                ?.child("viewed")?.orderByKey()?.startAt(myId)?.endAt(myId)
                                ?.addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                                        if (!dataSnapshot.exists()) {
                                            mStatusReference?.child(id!!)?.child("statusItem")!!
                                                .child(
                                                    statusItem.id!!
                                                ).child("viewed").child(myId!!).setValue(viewed)
                                        }
                                    }

                                    override fun onCancelled(databaseError: DatabaseError) {}
                                })
                            if (mAdapterViewFlipper?.displayedChild == mAdapterViewFlipper!!.count - 1) {
                                mAdapterViewFlipper?.stopFlipping()
                                animation!!.addListener(object : Animator.AnimatorListener {
                                    override fun onAnimationStart(animator: Animator) {
                                        val timestamp =
                                            TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())
                                        val viewed = Viewed(myId, timestamp)
                                        if (id != myId) {
                                            mStatusReference!!.child(id!!).child("allseen")
                                                .orderByKey().startAt(myId).endAt(myId)
                                                .addListenerForSingleValueEvent(object :
                                                    ValueEventListener {
                                                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                                                        if (!dataSnapshot.exists()) {
                                                            mStatusReference!!.child(id!!)
                                                                .child("allseen").child(
                                                                myId!!
                                                            ).setValue(viewed)
                                                        }
                                                    }

                                                    override fun onCancelled(databaseError: DatabaseError) {}
                                                })
                                        }
                                    }

                                    override fun onAnimationEnd(animator: Animator) {
                                        finish()
                                    }

                                    override fun onAnimationCancel(animator: Animator) {}
                                    override fun onAnimationRepeat(animator: Animator) {}
                                })
                            }
                            animation!!.start()
                        }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
    }

    fun setProgressMax(pb: ProgressBar?) {
        pb!!.max = 100 * 100
    }

    private fun setProgressAnimate(pb: ProgressBar?) {
        animation = pb?.progress?.let { ObjectAnimator.ofInt(pb, "progress", it, 100 * 100) }
        animation?.duration = 2500
        animation?.interpolator = LinearInterpolator()
    }

    private fun setMargins(view: View?) {
        if (view!!.layoutParams is MarginLayoutParams) {
            val p = view.layoutParams as MarginLayoutParams
            p.setMargins(0, 0, 8, 0)
            view.requestLayout()
        }
    }
}