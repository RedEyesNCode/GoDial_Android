package com.redeyesncode.gozulix.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.redeyesncode.gozulix.R
import com.redeyesncode.gozulix.databinding.ActivityDashboardBinding
import com.redeyesncode.gozulix.service.CallRecorderActivity
import com.redeyesncode.gozulix.service.CallRecordingService
import com.redeyesncode.redbet.base.BaseActivity

class DashboardActivity : BaseActivity() {
    lateinit var binding:ActivityDashboardBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setupNavController()

        initClicks()
        setContentView(binding.root)
    }

    private fun initClicks() {


        binding.tvTitle.setOnClickListener {
            startActivity(Intent(this@DashboardActivity, CallRecorderActivity::class.java))

        }

    }

    private fun setupNavController() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_bumble) as NavHostFragment
        val navController = navHostFragment.navController
        NavigationUI.setupWithNavController(binding.bottomNavigation, navController)

    }
    fun startRecordingService() {
        val serviceIntent = Intent(this, CallRecordingService::class.java)
        startService(serviceIntent)
    }
}