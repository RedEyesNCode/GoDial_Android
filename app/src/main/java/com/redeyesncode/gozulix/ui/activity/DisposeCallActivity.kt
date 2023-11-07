package com.redeyesncode.gozulix.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.SeekBar
import androidx.lifecycle.lifecycleScope
import com.redeyesncode.gozulix.R
import com.redeyesncode.gozulix.databinding.ActivityDisposeCallBinding
import com.redeyesncode.gozulix.room.ContactDatabase
import com.redeyesncode.gozulix.room.ContactEntity
import com.redeyesncode.gozulix.ui.dialogs.UpdateNoteDialog
import com.redeyesncode.redbet.base.BaseActivity
import kotlinx.coroutines.launch


class DisposeCallActivity : BaseActivity() {
    lateinit var binding:ActivityDisposeCallBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityDisposeCallBinding.inflate(layoutInflater)

        initClicks()

        setupRoomDb()
        setupDropDown()
        setupEmojiSeekbar()

        setContentView(binding.root)
    }

    private fun setupEmojiSeekbar() {

        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // Calculate the emoji index based on the progress

                binding.tvLeadScore.text = "Lead Score ${progress}%"

                val emojiIndex = progress / 10 // Assuming 10% intervals

                // Get the corresponding emoji resource ID
                val emojiResourceId = getEmojiResource(emojiIndex)

                // Set the emoji as the thumb drawable
                val thumbDrawable = resources.getDrawable(emojiResourceId)
                binding.seekBar.thumb = thumbDrawable
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // Not needed for this example
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // Not needed for this example
            }
        })



    }
    private fun getEmojiResource(emojiIndex: Int): Int {
        val emojiResources = intArrayOf(
            R.drawable.thumb_10,
            R.drawable.thumb_20,
            R.drawable.thumb_30,
            R.drawable.thumb_40,
            R.drawable.thumb_50,
            R.drawable.thumb_60,
            R.drawable.thumb_70,
            R.drawable.thumb_80,
            R.drawable.thumb_90,
            R.drawable.thumb_100,
            R.drawable.thumb_100,
            R.drawable.thumb_100,
        )
        return emojiResources[emojiIndex]
    }

    private fun setupDropDown() {
        val options = resources.getStringArray(R.array.call_dispose_options)
        val arrayAdapter = ArrayAdapter(this, R.layout.dropdown_item, options)
        binding.autoCompleteTextView.setAdapter(arrayAdapter)

    }

    private fun setupRoomDb() {
        val contactDatabase = ContactDatabase.getDatabase(this@DisposeCallActivity)
        val contactDao = contactDatabase.contactDao()
        var userContact:ContactEntity?=null
        lifecycleScope.launch {
            userContact = contactDao.findContactByNumber( intent.getStringExtra("NUMBER").toString())

        }
        binding.tvCallNumber.text = "You Just Spoke to ${intent.getStringExtra("NUMBER").toString()}"
        binding.tvContactHint.text = userContact?.contactName?.substring(0,1)
        binding.tvContactName.text = userContact?.contactName.toString()
        binding.autoCompleteTextView.setText(userContact?.callEvent.toString())
        binding.tvLeadScore.text = "Lead score ${userContact?.leadScore.toString()}%"
        binding.seekBar.progress = userContact?.leadScore!!.toInt()
        val emojiIndex = binding.seekBar.progress / 10 // Assuming 10% intervals

        // Get the corresponding emoji resource ID
        val emojiResourceId = getEmojiResource(emojiIndex)

        // Set the emoji as the thumb drawable
        val thumbDrawable = resources.getDrawable(emojiResourceId)
        binding.seekBar.thumb = thumbDrawable
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
            finish()
        }

        binding.btnNote.setOnClickListener {
            val dialog = UpdateNoteDialog(intent.getStringExtra("NUMBER").toString())
            dialog.show(supportFragmentManager,"update_note_dialog")
        }

        binding.btnUpdate.setOnClickListener {
            val contactDatabase = ContactDatabase.getDatabase(this@DisposeCallActivity)
            val contactDao = contactDatabase.contactDao()
            var userContact:ContactEntity?=null
            lifecycleScope.launch {
                userContact = contactDao.findContactByNumber( intent.getStringExtra("NUMBER").toString())
                userContact?.leadScore = binding.seekBar.progress
                userContact?.callEvent = binding.autoCompleteTextView.text.toString()
                contactDao.update(userContact!!)
            }
            showToast("Update Contact Record !")

        }


    }
    fun makePhoneCall(phoneNumber: String) {
        // Check for the CALL_PHONE permission before making the call
        val callIntent = Intent(Intent.ACTION_CALL)
        callIntent.data = Uri.parse("tel:$phoneNumber")
        startActivity(callIntent)
    }
}