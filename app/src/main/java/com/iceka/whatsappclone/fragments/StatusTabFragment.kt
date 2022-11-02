package com.iceka.whatsappclone.fragments

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.format.DateFormat
import android.text.format.DateUtils
import android.util.Base64
import android.view.*
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.devlomi.circularstatusview.CircularStatusView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.iceka.whatsappclone.CameraActivity
import com.iceka.whatsappclone.R
import com.iceka.whatsappclone.adapters.StatusAdapter
import com.iceka.whatsappclone.models.Status
import com.iceka.whatsappclone.models.StatusItem
import com.iceka.whatsappclone.models.Viewed
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*
import java.util.concurrent.TimeUnit

class StatusTabFragment : Fragment() {
    private var broadcastReceiver: BroadcastReceiver? = null
    private var mStatusReference: DatabaseReference? = null
    private var mFirebaseUser: FirebaseUser? = null
    private var myId: String? = null
    private var mAvatar: CircleImageView? = null
    private var mTimeStatus: TextView? = null
    private var layout: RelativeLayout? = null
    private var mCircularStatusCount: CircularStatusView? = null
    private var mRecentStatusRv: RecyclerView? = null
    private var mViewedStatusRv: RecyclerView? = null
    private var mLayoutRecentStatus: LinearLayout? = null
    private var mLayoutViewedStatus: LinearLayout? = null
    private val statusList = mutableListOf<Status>()
    private val statusListViewed = mutableListOf<Status>()
    private val viewedList: MutableList<String> = ArrayList()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_status_tab, container, false)
        setHasOptionsMenu(true)
        mAvatar = rootView.findViewById(R.id.avatar_user_status)
        mTimeStatus = rootView.findViewById(R.id.tv_time_status)
        layout = rootView.findViewById(R.id.layout_self_status)
        mCircularStatusCount = rootView.findViewById(R.id.circular_status_count)
        mRecentStatusRv = rootView.findViewById(R.id.rv_recent_updates_status)
        mViewedStatusRv = rootView.findViewById(R.id.rv_viewed_updates_status)
        mLayoutRecentStatus = rootView.findViewById(R.id.layout_recent_updates_status)
        mLayoutViewedStatus = rootView.findViewById(R.id.layout_viewed_updates_status)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)
        val viewedLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)
        mRecentStatusRv?.layoutManager = layoutManager
        mViewedStatusRv?.layoutManager = viewedLayoutManager
        val mFirebaseAuth = FirebaseAuth.getInstance()
        mFirebaseUser = mFirebaseAuth.currentUser
        mStatusReference = FirebaseDatabase.getInstance().reference.child("status")
        myId = mFirebaseAuth.currentUser?.uid
        myStatus
        otherStatus
        viewed
        checkStatusViewed()
        if (statusList.size == 0) {
            mLayoutRecentStatus?.visibility = View.GONE
        }
        broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (intent.action?.compareTo(Intent.ACTION_TIME_TICK) == 0) {
                    checkStatusExpire()
                }
            }
        }
        requireActivity().registerReceiver(broadcastReceiver, IntentFilter(Intent.ACTION_TIME_TICK))
        return rootView
    }

    private val viewed: Unit
        get() {
            mStatusReference?.child(myId!!)?.child("statusItem")
                ?.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        for (snapshot in dataSnapshot.children) {
                            val tesref = dataSnapshot.ref.child("viewed")
                            tesref.addValueEventListener(object : ValueEventListener {
                                override fun onDataChange(dataSnapshot: DataSnapshot) {
                                    for (snapshot1 in dataSnapshot.children) {
                                        val viewed1 = snapshot1.getValue(Viewed::class.java)
                                        viewedList.add(viewed1?.uid!!)
                                    }
                                }

                                override fun onCancelled(databaseError: DatabaseError) {}
                            })
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {}
                })
        }
    private val myStatus: Unit
        get() {
            mStatusReference?.child(myId!!)?.child("statusItem")
                ?.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.exists()) {
                            val count = dataSnapshot.childrenCount.toInt()
                            mCircularStatusCount!!.setPortionsCount(count)
                            val cek = dataSnapshot.ref
                            val query = cek.orderByKey().limitToLast(1)
                            query.addValueEventListener(object : ValueEventListener {
                                override fun onDataChange(dataSnapshot: DataSnapshot) {
                                    for (snapshot in dataSnapshot.children) {
                                        if (dataSnapshot.exists()) {
                                            val statusItem = snapshot.getValue(
                                                StatusItem::class.java
                                            )
                                            val byteArray = Base64.decode(
                                                statusItem?.thumbnail, Base64.DEFAULT
                                            )
                                            val bitmap = BitmapFactory.decodeByteArray(
                                                byteArray,
                                                0,
                                                byteArray.size
                                            )
                                            Glide.with(requireContext())
                                                .load(bitmap)
                                                .into(mAvatar!!)
                                        } else {
                                            Glide.with(requireContext())
                                                .load(mFirebaseUser?.photoUrl)
                                                .into(mAvatar!!)
                                        }
                                    }
                                }

                                override fun onCancelled(databaseError: DatabaseError) {}
                            })
                            for (snapshot in dataSnapshot.children) {
                                val statusItem = snapshot.getValue(StatusItem::class.java)
                                mCircularStatusCount?.visibility = View.VISIBLE
                                assert(statusItem != null)
                                val timeFromServer = statusItem!!.timestamp
                                val calendar = Calendar.getInstance(Locale.ENGLISH)
                                calendar.timeInMillis = timeFromServer
                                val co = calendar.timeInMillis
                                DateFormat.format("M/dd/yyyy", calendar)
                                val now = DateUtils.getRelativeTimeSpanString(
                                    co,
                                    System.currentTimeMillis(),
                                    DateUtils.MINUTE_IN_MILLIS
                                )
                                mTimeStatus?.text = now
                            }
                        } else {
                            layout!!.setOnClickListener {
                                startActivity(
                                    Intent(
                                        context,
                                        CameraActivity::class.java
                                    )
                                )
                            }
                            Glide.with(requireContext())
                                .load(mFirebaseUser?.photoUrl)
                                .into(mAvatar!!)
                            mCircularStatusCount?.visibility = View.GONE
                            mTimeStatus?.text = "Tap to add status"
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {}
                })
        }
    private val otherStatus: Unit
        get() {
            val countList: MutableList<Int> = ArrayList()
            mStatusReference?.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    statusList.clear()
                    countList.clear()
                    if (dataSnapshot.exists()) {
                        for (snapshot in dataSnapshot.children) {
                            if (snapshot.exists()) {
                                val status = snapshot.getValue(
                                    Status::class.java
                                )
                                if (status!!.uid != myId && status.statuscount > 0) {
                                    countList.add(status.statuscount)
                                    statusList.add(status)
                                }
                                val adapter = StatusAdapter(requireActivity(), statusList)
                                mRecentStatusRv!!.adapter = adapter
                            }
                        }
                        var sum = 0
                        for (num in countList) {
                            sum += num
                        }
                        if (sum <= 0) {
                            mLayoutRecentStatus!!.visibility = View.GONE
                        } else {
                            mLayoutRecentStatus!!.visibility = View.VISIBLE
                        }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
        }

    private fun checkStatusExpire() {
        mStatusReference?.child(myId!!)?.child("statusItem")
            ?.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (snapshot in dataSnapshot.children) {
                        val statusItem = snapshot.getValue(StatusItem::class.java)
                        val timeList: MutableList<Long> = ArrayList()
                        timeList.add(statusItem!!.timestamp)
                        for (tesa in timeList) {
                            tesa + TimeUnit.MILLISECONDS.convert(3, TimeUnit.HOURS)
                            val timeNow = System.currentTimeMillis()
                            if (tesa <= timeNow) {
                                snapshot.ref.removeValue()
                            }
                        }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
        val query =
            mStatusReference?.child(myId!!)?.child("statusItem")?.orderByKey()?.limitToLast(1)
        query?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    val statusItem1 = snapshot.getValue(StatusItem::class.java)
                    val tes =
                        statusItem1!!.timestamp + TimeUnit.MILLISECONDS.convert(3, TimeUnit.HOURS)
                    val now = System.currentTimeMillis()
                    if (tes <= now) {
                        mStatusReference!!.child(myId!!).child("allseen").removeValue()
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    private fun checkStatusViewed() {
        statusListViewed.clear()
        mStatusReference?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                statusListViewed.clear()
                for (snapshot in dataSnapshot.children) {
                    val status = snapshot.getValue(
                        Status::class.java
                    )
                    mStatusReference?.child(status?.uid!!)?.child("allseen")
                        ?.addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    for (snapshot1 in dataSnapshot.children) {
                                        val viewed = snapshot1.getValue(Viewed::class.java)
                                        if (viewed?.uid == myId) {
                                            for (i in statusList.indices) {
                                                if (statusList[i].uid == status.uid) {
                                                    statusList.removeAt(i)
                                                }
                                            }
                                            if (statusList.size == 0) {
                                                mLayoutRecentStatus?.visibility = View.GONE
                                            }
                                            statusListViewed.add(status)
                                            val adapter = StatusAdapter(
                                                requireActivity(),
                                                statusListViewed
                                            )
                                            mViewedStatusRv?.adapter = adapter
                                        }
                                    }
                                    mLayoutViewedStatus?.visibility = View.VISIBLE
                                } else {
                                    mLayoutViewedStatus?.visibility = View.GONE
                                }
                            }

                            override fun onCancelled(databaseError: DatabaseError) {}
                        })
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_status, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onDestroy() {
        super.onDestroy()
        requireActivity().unregisterReceiver(broadcastReceiver)
    }
}