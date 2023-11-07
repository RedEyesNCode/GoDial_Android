package com.redeyesncode.gozulix.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.redeyesncode.gozulix.R
import com.redeyesncode.gozulix.databinding.FragmentUserContactInfoBinding
import com.redeyesncode.gozulix.room.ContactDatabase
import com.redeyesncode.gozulix.room.ContactEntity
import com.redeyesncode.gozulix.ui.dialogs.UpdateNoteDialog
import com.redeyesncode.pay2kart.base.BaseFragment
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [UserContactInfoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UserContactInfoFragment : BaseFragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding:FragmentUserContactInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentUserContactInfoBinding.inflate(inflater,container,false)

        initClicks()
        setupRoomDb()

        return binding.root
    }

    private fun setupRoomDb() {
        val contactDatabase = ContactDatabase.getDatabase(fragmentContext)
        val contactDao = contactDatabase.contactDao()
        var userContact: ContactEntity?=null
        lifecycleScope.launch {
            userContact = contactDao.findContactByNumber(param1.toString())
        }
        binding.edtPhone.setText(userContact?.contactNumber.toString())
        binding.edtContactName.setText(userContact?.contactName?.toString())
        binding.tvLeadScore.text = "Lead score ${userContact?.leadScore.toString()}%"
        binding.seekBar.progress = userContact?.leadScore!!.toInt()
        val emojiIndex = binding.seekBar.progress / 10 // Assuming 10% intervals

        // Get the corresponding emoji resource ID
        val emojiResourceId = getEmojiResource(emojiIndex)

        // Set the emoji as the thumb drawable
        val thumbDrawable = resources.getDrawable(emojiResourceId)
        binding.seekBar.thumb = thumbDrawable

    }

    private fun initClicks() {
        val contactDatabase = ContactDatabase.getDatabase(fragmentContext)
        val contactDao = contactDatabase.contactDao()
        var userContact: ContactEntity?=null
        lifecycleScope.launch {
            userContact = contactDao.findContactByNumber(param1.toString())
        }
        binding.btnNote.setOnClickListener {
            val dialog = UpdateNoteDialog(userContact?.contactNumber.toString())
            dialog.show(requireFragmentManager(),"update_note_dialog")
        }
        binding.btnUpdate.setOnClickListener {
            lifecycleScope.launch {
                userContact = contactDao.findContactByNumber(param1.toString())
                userContact?.leadScore = binding.seekBar.progress
                userContact?.contactNumber = binding.edtPhone.text.toString()
                userContact?.contactName = binding.edtContactName.text.toString()

                contactDao.update(userContact!!)
            }
            showToast("Update Contact Record !")

        }


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
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment UserContactInfoFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            UserContactInfoFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}