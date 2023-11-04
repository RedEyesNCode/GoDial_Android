package com.redeyesncode.gozulix.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.redeyesncode.gozulix.R
import com.redeyesncode.gozulix.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    lateinit var binding:ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)

        binding.btnCancel.setOnClickListener {
            finish()

        }
        binding.btnLogin.setOnClickListener {
            val intent = Intent(this, PermissionActivity::class.java)
            startActivity(intent)
            overridePendingTransition(com.redeyesncode.gozulix.R.anim.slide_up, 0)


        }

        setContentView(binding.root)
    }


}