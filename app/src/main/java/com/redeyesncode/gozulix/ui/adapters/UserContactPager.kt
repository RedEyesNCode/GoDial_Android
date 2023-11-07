package com.redeyesncode.gozulix.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.redeyesncode.gozulix.ui.fragments.CallHistoryFragment
import com.redeyesncode.gozulix.ui.fragments.DialerManualFragment
import com.redeyesncode.gozulix.ui.fragments.UserContactHistoryFragment
import com.redeyesncode.gozulix.ui.fragments.UserContactInfoFragment

class UserContactPager (fragmentActivity: FragmentActivity,var number:String) :FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {

        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {

            0 -> UserContactInfoFragment.newInstance(number,number)
            1 -> UserContactHistoryFragment.newInstance(number,number)
            else -> throw IllegalArgumentException("Invalid position")
        }
    }


    //    override fun getCount(): Int {
//        return 2
//    }
//
//    override fun getItem(position: Int): Fragment {
//
//        return when (position) {
//
//            0 -> UserContactInfoFragment.newInstance(number,number)
//            1 -> UserContactHistoryFragment.newInstance(number,number)
//            else -> throw IllegalArgumentException("Invalid position")
//        }


}