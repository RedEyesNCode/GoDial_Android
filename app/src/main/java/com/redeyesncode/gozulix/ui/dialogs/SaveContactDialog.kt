package com.redeyesncode.gozulix.ui.dialogs

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.redeyesncode.gozulix.R
import com.redeyesncode.gozulix.databinding.LayoutDialogUpdateNoteBinding
import com.redeyesncode.gozulix.databinding.LayoutSaveNumberDialogBinding
import com.redeyesncode.gozulix.room.ContactDatabase
import com.redeyesncode.gozulix.room.ContactEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class SaveContactDialog(var number:String): DialogFragment() {
    lateinit var binding: LayoutSaveNumberDialogBinding
    lateinit var mdialog: Dialog

    override fun onStart() {
        super.onStart()
        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val height = ViewGroup.LayoutParams.WRAP_CONTENT // Adjust this as needed
        mdialog.window?.setLayout(width, height)
        mdialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

    }
    @SuppressLint("NewApi")
    fun getCurrentTime(): String {
        val currentTime = LocalTime.now()
        val formatter = DateTimeFormatter.ofPattern("HH:mm:ss") // Format as "HH:mm:ss" for 24-hour time

        return currentTime.format(formatter)
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val builder = AlertDialog.Builder(requireContext())
        val inflater = requireActivity().layoutInflater
        setStyle(STYLE_NORMAL, R.style.FullScreenDialogStyle)

        binding = LayoutSaveNumberDialogBinding.inflate(inflater)

        builder.setView(binding.root)

        mdialog = builder.create()
        binding.btnUpdate.setOnClickListener {
            showToast("Update Note")
        }

        mdialog.setCancelable(true)
        mdialog.setCanceledOnTouchOutside(true)
        val contactDatabase = ContactDatabase.getDatabase(requireContext())
        val contactDao = contactDatabase.contactDao()

        binding.edtPhone.setText(number)

        binding.edtContactName.setText("${getCurrentTime()}_ZULIX_CRM")

        binding.btnCall.setOnClickListener {
            makePhoneCall(binding.edtPhone.text.toString())

        }

        binding.btnUpdate.setOnClickListener {

            if (binding.edtContactName.text.toString().isEmpty()) {
                showToast("Please enter name")

            } else {
                val contactDatabase = ContactDatabase.getDatabase(requireContext())
                val contactDao = contactDatabase.contactDao()
                val newContact = ContactEntity(
                    contactName = binding.edtContactName.text.toString(),
                    contactNumber = binding.edtPhone.text.toString(),
                    status = "Pending",
                    note = "No Note Present",
                    leadScore = 60,
                    callEvent = "INTERESTED"

                )
                if (contactDao.findContactByNumber(number) == null) {
                    lifecycleScope.launch {
                        contactDao.insertOrUpdate(newContact)
                    }
                } else {
                    showToast("You have already saved this number to CRM.")
                }
                showToast("CONTACT SAVE TO CRM !")
            }


        }




        return mdialog
    }
    fun makePhoneCall(phoneNumber: String) {
        // Check for the CALL_PHONE permission before making the call
        val callIntent = Intent(Intent.ACTION_CALL)
        callIntent.data = Uri.parse("tel:$phoneNumber")
        startActivity(callIntent)
    }
    fun showToast(message: String) {
        // Show a toast message
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}