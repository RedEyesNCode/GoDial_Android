package com.redeyesncode.gozulix.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.tabs.TabLayoutMediator
import com.redeyesncode.gozulix.R
import com.redeyesncode.gozulix.databinding.ActivityDialerBinding
import com.redeyesncode.gozulix.ui.adapters.DialerPagerAdapter
import com.redeyesncode.redbet.base.BaseActivity

class DialerActivity : BaseActivity() {

    lateinit var binding:ActivityDialerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityDialerBinding.inflate(layoutInflater)
        setupTabViewPager()
        setContentView(binding.root)
    }
    private fun setupTabViewPager() {

        val fragmentAdapter = DialerPagerAdapter(this)
        binding.rechargeViewPager.adapter = fragmentAdapter
        binding.rechargeViewPager.offscreenPageLimit = 1

        TabLayoutMediator(binding.rechargeTabs, binding.rechargeViewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Manual Dialer"
                1 -> "Recent Calls"
                else -> ""
            }
        }.attach()

    }
}