package com.redeyesncode.gozulix.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.redeyesncode.gozulix.ui.fragments.CallHistoryFragment
import com.redeyesncode.gozulix.ui.fragments.DialerManualFragment

class DialerPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return 2  // Number of tabs
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {

            0 -> DialerManualFragment()
            1 -> CallHistoryFragment()

            else -> throw IllegalArgumentException("Invalid position")
        }
    }
}