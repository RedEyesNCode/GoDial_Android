package com.redeyesncode.gozulix.ui

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.redeyesncode.gozulix.R
import com.redeyesncode.gozulix.databinding.ActivityIntroBinding
import com.redeyesncode.gozulix.ui.activity.LoginActivity
import com.redeyesncode.gozulix.ui.adapters.ImageAdapter


class IntroActivity : AppCompatActivity() {

    lateinit var binding:ActivityIntroBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityIntroBinding.inflate(layoutInflater)
        setupViewPager()
        binding.btnLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            overridePendingTransition(com.redeyesncode.gozulix.R.anim.slide_up, 0)



        }
        setContentView(binding.root)

    }

    private fun setupViewPager() {
        val imageResources = listOf(R.drawable.baseline_android_24, R.drawable.baseline_android_24, R.drawable.baseline_android_24) // Replace with your image resources
        val adapter = ImageAdapter(imageResources)
        binding.viewPager.adapter = adapter
        binding.tabLayout.tabRippleColor = null // Disable the ripple effect
        binding.tabLayout.tabIconTint = null // Disable the tint on icons
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            val tabDot = tab.customView?.findViewById<ImageView>(R.id.tabDot)

            // Set the selected dot to white and others to black
            if (tab.position == position) {
                tabDot?.setImageResource(R.drawable.selected_dot) // Set this to your white dot drawable
            } else {
                tabDot?.setImageResource(R.drawable.unselected_dot) // Set this to your black dot drawable
            }
        }.attach()

    }
}