package com.redeyesncode.gozulix

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.PhoneNumberUtils
import android.telephony.SmsManager
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.google.android.material.tabs.TabLayoutMediator
import com.redeyesncode.gozulix.databinding.ActivityUserContactBinding
import com.redeyesncode.gozulix.databinding.FragmentUserContactInfoBinding
import com.redeyesncode.gozulix.room.ContactDatabase
import com.redeyesncode.gozulix.room.ContactEntity
import com.redeyesncode.gozulix.ui.adapters.DialerPagerAdapter
import com.redeyesncode.gozulix.ui.adapters.UserContactPager
import com.redeyesncode.redbet.base.BaseActivity
import kotlinx.coroutines.launch

class ActivityUserContact : BaseActivity() {

    lateinit var binding:ActivityUserContactBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserContactBinding.inflate(layoutInflater)


        initClicks()

        setContentView(binding.root)
    }

    private fun initClicks() {
        binding.ivClose.setOnClickListener {
            finish()
        }
        // fetch the user table.
        val contactDatabase = ContactDatabase.getDatabase(this)
        val contactDao = contactDatabase.contactDao()
        var userContact: ContactEntity?=null
        lifecycleScope.launch {
            userContact = contactDao.findContactByNumber( intent.getStringExtra("NUMBER").toString())
        }

        if(userContact==null){
            showToast("User Not Found in CRM ")
            finish()
        }else{
            binding.tvContactName.text = userContact?.contactName.toString()
            binding.tvContactNumber.text = userContact?.contactNumber.toString()
            val fragmentAdapter = UserContactPager(this,userContact?.contactNumber.toString())
            binding.rechargeViewPager.adapter = fragmentAdapter
            binding.rechargeViewPager.isSaveFromParentEnabled = false;

            TabLayoutMediator(binding.rechargeTabs, binding.rechargeViewPager) { tab, position ->
                tab.text = when (position) {
                    0 -> "Contact Info"
                    1 -> "Call History"
                    else -> ""
                }
            }.attach()

        }
        binding.btnCall.setOnClickListener {

            makePhoneCall(userContact?.contactNumber.toString())

        }
        binding.btnSms.setOnClickListener {
            sendSMS(this,userContact?.contactNumber.toString(),"CRM_ANDROID_ZULIX")
        }
        binding.btnWhatsApp.setOnClickListener {
            sendWhatsAppMessage(this,userContact?.contactNumber.toString(),"WHATSAPP_ZULIX_CRM")
        }
        binding.btnEmail.setOnClickListener {
            sendEmail(this,"vancher571@gmail.com","ZULIX_CRM_ANDROID","ANDROID_NATIVE")

        }






    }
    fun makePhoneCall(phoneNumber: String) {
        // Check for the CALL_PHONE permission before making the call
        val callIntent = Intent(Intent.ACTION_CALL)
        callIntent.data = Uri.parse("tel:$phoneNumber")
        startActivity(callIntent)
    }
    fun sendWhatsAppMessage(context: Context, phoneNumber: String, message: String) {
        try {
            val packageManager = context.packageManager
            val isWhatsAppInstalled = isAppInstalled("com.whatsapp", packageManager)

            if (isWhatsAppInstalled) {
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/plain"
                intent.setPackage("com.whatsapp")
                intent.putExtra(Intent.EXTRA_TEXT, message)
                intent.putExtra("jid", PhoneNumberUtils.stripSeparators(phoneNumber) + "@s.whatsapp.net")

                context.startActivity(intent)
            } else {
                // Handle the case when WhatsApp is not installed on the device.
                Toast.makeText(context, "WhatsApp is not installed.", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun isAppInstalled(packageName: String, packageManager: PackageManager): Boolean {
        return try {
            packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }
    fun sendSMS(context: Context, phoneNumber: String, message: String) {
        val smsManager = SmsManager.getDefault()
        val sentIntent = PendingIntent.getBroadcast(context, 0, Intent("SMS_SENT"), 0)
        val deliveredIntent = PendingIntent.getBroadcast(context, 0, Intent("SMS_DELIVERED"), 0)

        smsManager.sendTextMessage(phoneNumber, null, message, sentIntent, deliveredIntent)
    }
    fun sendEmail(context: Context, toEmail: String, subject: String, message: String) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "message/rfc822"
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(toEmail))
        intent.putExtra(Intent.EXTRA_SUBJECT, subject)
        intent.putExtra(Intent.EXTRA_TEXT, message)

        try {
            context.startActivity(Intent.createChooser(intent, "Send Email"))
        } catch (e: Exception) {
            // Handle exceptions, e.g., if there's no email client installed on the device.
        }
    }
}