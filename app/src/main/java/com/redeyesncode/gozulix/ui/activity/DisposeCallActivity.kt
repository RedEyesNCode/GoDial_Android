package com.redeyesncode.gozulix.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.redeyesncode.gozulix.databinding.ActivityDisposeCallBinding
import com.redeyesncode.gozulix.room.ContactDatabase
import com.redeyesncode.gozulix.room.ContactEntity
import com.redeyesncode.redbet.base.BaseActivity
import kotlinx.coroutines.launch


class DisposeCallActivity : BaseActivity() {
    lateinit var binding:ActivityDisposeCallBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityDisposeCallBinding.inflate(layoutInflater)

        initClicks()

        setupRoomDb()

        setContentView(binding.root)
    }

    private fun setupRoomDb() {
        val contactDatabase = ContactDatabase.getDatabase(this@DisposeCallActivity)
        val contactDao = contactDatabase.contactDao()
        var userContact:ContactEntity?=null
        lifecycleScope.launch {
            userContact = contactDao.findContactByNumber( intent.getStringExtra("NUMBER").toString())

        }
        binding.edtNote.setText(userContact?.note.toString())



    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        showToast("NO BACK PRESS ALLOWED")
    }

    private fun initClicks() {
        binding.tvCallNumber.text = intent.getStringExtra("NUMBER")
        binding.ivClose.setOnClickListener {
            val resultIntent = Intent()
            resultIntent.putExtra("action", "close")
            setResult(RESULT_OK, resultIntent)
            finish()
        }
        binding.btnPause.setOnClickListener {
            val resultIntent = Intent()
            resultIntent.putExtra("action", "pause")
            setResult(RESULT_OK, resultIntent)
            finish()
        }
        binding.btnDone.setOnClickListener {
            val contactDatabase = ContactDatabase.getDatabase(this@DisposeCallActivity)
            val contactDao = contactDatabase.contactDao()
            lifecycleScope.launch {
                contactDao.updateStatusToDone(intent.getStringExtra("NUMBER").toString())
            }
            val resultIntent = Intent()
            resultIntent.putExtra("action", "done")
            setResult(RESULT_OK, resultIntent)
            finish()        }
        binding.btnUpdate.setOnClickListener {

            if(binding.edtNote.text.toString().isEmpty()){
                showToast("No Empty Note")

            }else{
                val contactDatabase = ContactDatabase.getDatabase(this@DisposeCallActivity)
                val contactDao = contactDatabase.contactDao()
                var userContact:ContactEntity?=null
                lifecycleScope.launch {
                    userContact = contactDao.findContactByNumber( intent.getStringExtra("NUMBER").toString())
                    userContact?.note = binding.edtNote.text.toString()
                    contactDao.update(userContact!!)

                }
                showToast("Updated Note !")
            }


        }


    }
    fun makePhoneCall(phoneNumber: String) {
        // Check for the CALL_PHONE permission before making the call
        val callIntent = Intent(Intent.ACTION_CALL)
        callIntent.data = Uri.parse("tel:$phoneNumber")
        startActivity(callIntent)
    }
}