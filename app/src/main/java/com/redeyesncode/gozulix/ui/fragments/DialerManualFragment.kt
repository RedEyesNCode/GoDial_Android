package com.redeyesncode.gozulix.ui.fragments

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.redeyesncode.gozulix.databinding.FragmentManualDialerBinding
import com.redeyesncode.pay2kart.base.BaseFragment

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DialerManualFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DialerManualFragment : BaseFragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding:FragmentManualDialerBinding



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
        // Inflate the layout for this fragment


        binding = FragmentManualDialerBinding.inflate(inflater,container,false)
        setupDialerEditText()


        initClicks()

        return binding.root
    }

    private fun initClicks() {
        binding.btnCall.setOnClickListener {
            if(binding.editTextPhoneNumber.text.toString().length==10){
                makePhoneCall(binding.editTextPhoneNumber.text.toString())
            }else{
                showMessageDialog("INVALID NUMBER","Information")
            }

        }

    }

    private fun setupDialerEditText() {
        binding.apply {
            btnOne.setOnClickListener {
                appendDigitToEditText("1")

            }
            btnTwo.setOnClickListener {
                appendDigitToEditText("2")
            }
            btnThree.setOnClickListener {
                appendDigitToEditText("3")

            }
            btnFour.setOnClickListener {
                appendDigitToEditText("4")

            }
            btnFive.setOnClickListener {
                appendDigitToEditText("5")
            }
            btnSix.setOnClickListener {
                appendDigitToEditText("6")
            }
            btnSeven.setOnClickListener {
                appendDigitToEditText("7")
            }
            btnEight.setOnClickListener {
                appendDigitToEditText("8")
            }
            btnNine.setOnClickListener {
                appendDigitToEditText("9")
            }
            btnDelete.setOnClickListener {
                deleteLastDigitFromEditText()
            }
            btnZero.setOnClickListener {
                appendDigitToEditText("0")
            }
        }

        binding.editTextPhoneNumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val text = s.toString()
                if (text.length < 10) {
                    // Text is red if length is less than 10
                    binding.editTextPhoneNumber.setTextColor(Color.RED)
                } else {
                    // Text is green if length is 10 or more
                    binding.editTextPhoneNumber.setTextColor(Color.GREEN)
                }
            }
        })

    }
    private fun appendDigitToEditText(digit: String) {
        val currentText = binding.editTextPhoneNumber.text.toString()
        binding.editTextPhoneNumber.setText(currentText + digit)
    }

    private fun deleteLastDigitFromEditText() {
        val currentText = binding.editTextPhoneNumber.text.toString()
        if (currentText.isNotEmpty()) {
            binding.editTextPhoneNumber.setText(currentText.substring(0, currentText.length - 1))
        }
    }
    fun makePhoneCall(phoneNumber: String) {
        // Check for the CALL_PHONE permission before making the call
        val callIntent = Intent(Intent.ACTION_CALL)
        callIntent.data = Uri.parse("tel:$phoneNumber")
        startActivity(callIntent)
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DialerFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DialerManualFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}