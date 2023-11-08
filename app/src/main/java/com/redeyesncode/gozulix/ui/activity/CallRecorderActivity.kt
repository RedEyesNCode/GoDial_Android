package com.redeyesncode.gozulix.ui.activity

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import androidx.annotation.RequiresApi
import com.redeyesncode.gozulix.R
import com.redeyesncode.gozulix.databinding.ActivityCallRecorderBinding
import com.redeyesncode.gozulix.service.CallRecordingService
import com.redeyesncode.redbet.base.BaseActivity

class CallRecorderActivity : BaseActivity() {

    lateinit var binding:ActivityCallRecorderBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityCallRecorderBinding.inflate(layoutInflater)

        initClicks()

        setContentView(binding.root)
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun initClicks() {

        binding.btnStart.setOnClickListener {
            if(Environment.isExternalStorageManager()){
                startRecordingService()
                showToast("START")
            }else{
                val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                startActivity(intent)

            }

        }
        binding.btnCancel.setOnClickListener {
            val stopRecordingIntent = Intent(this, CallRecordingService::class.java)
            stopService(stopRecordingIntent)
            showToast("STOP")

        }

    }
    fun startRecordingService() {
        val serviceIntent = Intent(this, CallRecordingService::class.java)
        startService(serviceIntent)
    }
}