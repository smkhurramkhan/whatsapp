package com.iceka.whatsappclone.adapters

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.iceka.whatsappclone.fragments.ChatTabFragment

class TabAdapter(private val mContext: Context, fragmentManager: FragmentManager?) :
    FragmentPagerAdapter(
        fragmentManager!!
    ) {
    override fun getItem(position: Int): Fragment {
        return ChatTabFragment()

    }

    override fun getCount(): Int {
        return 1
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return "Chats"

    }
}