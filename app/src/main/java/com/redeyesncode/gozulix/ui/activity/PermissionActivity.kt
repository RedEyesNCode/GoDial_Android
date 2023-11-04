package com.redeyesncode.gozulix.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.redeyesncode.gozulix.R
import com.redeyesncode.gozulix.databinding.ActivityPermissionBinding
import com.redeyesncode.pay2kart.base.BaseAdapter

class PermissionActivity : AppCompatActivity() {
    lateinit var binding:ActivityPermissionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPermissionBinding.inflate(layoutInflater)

        binding.btnFinish.setOnClickListener {

            val intent = Intent(this, DashboardActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            overridePendingTransition(com.redeyesncode.gozulix.R.anim.slide_up, 0)


        }


        binding.recvPermission.adapter = BaseAdapter<String>(this@PermissionActivity,R.layout.item_permission,8)
        binding.recvPermission.layoutManager = LinearLayoutManager(this@PermissionActivity,LinearLayoutManager.VERTICAL,false)
        setContentView(binding.root)
    }
}