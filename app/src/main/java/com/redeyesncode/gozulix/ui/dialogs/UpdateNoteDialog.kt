package com.redeyesncode.gozulix.ui.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.redeyesncode.gozulix.R
import com.redeyesncode.gozulix.databinding.LayoutDialogUpdateNoteBinding
import com.redeyesncode.gozulix.room.ContactDatabase
import com.redeyesncode.gozulix.room.ContactEntity
import kotlinx.coroutines.launch

class UpdateNoteDialog(var number:String): DialogFragment() {
    lateinit var binding:LayoutDialogUpdateNoteBinding
    lateinit var mdialog: Dialog

    override fun onStart() {
        super.onStart()
        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val height = ViewGroup.LayoutParams.WRAP_CONTENT // Adjust this as needed
        mdialog.window?.setLayout(width, height)
        mdialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val builder = AlertDialog.Builder(requireContext())
        val inflater = requireActivity().layoutInflater
        setStyle(STYLE_NORMAL, R.style.FullScreenDialogStyle)

        binding = LayoutDialogUpdateNoteBinding.inflate(inflater)

        builder.setView(binding.root)

        mdialog = builder.create()
        binding.btnUpdate.setOnClickListener {
            showToast("Update Note")
        }

        mdialog.setCancelable(true)
        mdialog.setCanceledOnTouchOutside(true)
        val contactDatabase = ContactDatabase.getDatabase(requireContext())
        val contactDao = contactDatabase.contactDao()
        var userContact: ContactEntity?=null
        lifecycleScope.launch {
            userContact = contactDao.findContactByNumber(number)
        }
        binding.edtNote.setText(userContact?.note.toString())
        binding.btnUpdate.setOnClickListener {

            if(binding.edtNote.text.toString().isEmpty()){
                showToast("No Empty Note")

            }else{
                val contactDatabase = ContactDatabase.getDatabase(requireContext())
                val contactDao = contactDatabase.contactDao()
                var userContact: ContactEntity?=null
                lifecycleScope.launch {
                    userContact = contactDao.findContactByNumber(number)
                    userContact?.note = binding.edtNote.text.toString()
                    contactDao.update(userContact!!)

                }
                showToast("Updated Note !")
                mdialog.dismiss()
            }


        }




        return mdialog
    }
    fun showToast(message: String) {
        // Show a toast message
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}