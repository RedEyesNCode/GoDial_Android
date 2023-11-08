package com.redeyesncode.gozulix.service

import android.annotation.SuppressLint
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import androidx.annotation.RequiresApi
import com.redeyesncode.gozulix.databinding.ActivityCallRecorderBinding
import com.redeyesncode.redbet.base.BaseActivity

class CallRecorderActivity : BaseActivity() {
    private val REQUEST_CODE = 0
    private lateinit var mDPM: DevicePolicyManager
    private lateinit var mAdminName: ComponentName
    lateinit var binding:ActivityCallRecorderBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityCallRecorderBinding.inflate(layoutInflater)

        initClicks()
        setupAdminDevice()

        setContentView(binding.root)
    }

    private fun setupAdminDevice() {
        try {
            // Initiate DevicePolicyManager.
            mDPM = getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
            mAdminName = ComponentName(this, DeviceAdminDemo::class.java)

            if (!mDPM.isAdminActive(mAdminName)) {
                val intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN)
                intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mAdminName)
                intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "Click on Activate button to secure your application.")
                startActivityForResult(intent, REQUEST_CODE)
            } else {
                // mDPM.lockNow()
                // val intent = Intent(this, TrackDeviceService::class.java)
                // startService(intent)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }


    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (REQUEST_CODE == requestCode) {
            val intent = Intent(this, MyRecorderService::class.java)
            startService(intent)
        }
    }
    @SuppressLint("NewApi")
    private fun initClicks() {

        binding.btnStart.setOnClickListener {
            val intent = Intent(this, MyRecorderService::class.java)
            startService(intent)
            if(Environment.isExternalStorageManager()){
                startRecordingService()
                showToast("START")
            }else{
                val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                startActivity(intent)

            }

        }
        binding.btnCancel.setOnClickListener {
            val intent = Intent(this, MyRecorderService::class.java)
            stopService(intent)
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