package com.redeyesncode.gozulix.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.redeyesncode.gozulix.R
import com.redeyesncode.gozulix.databinding.FragmentDialerBinding
import com.redeyesncode.gozulix.databinding.FragmentManualDialerBinding
import com.redeyesncode.gozulix.ui.activity.DialerActivity
import com.redeyesncode.gozulix.ui.activity.SelectContactsActivity
import com.redeyesncode.pay2kart.base.BaseFragment

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentDialer.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentDialer : BaseFragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var binding: FragmentDialerBinding

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
        binding = FragmentDialerBinding.inflate(inflater,container,false)


        binding.fabDialer.setOnClickListener {

            val intent = Intent(requireContext(), DialerActivity::class.java)
            startActivity(intent)
            activity?.overridePendingTransition(com.redeyesncode.gozulix.R.anim.slide_up, 0)



        }
        binding.fabAdd.setOnClickListener {
            val intent = Intent(requireContext(), SelectContactsActivity::class.java)
            startActivity(intent)
            activity?.overridePendingTransition(com.redeyesncode.gozulix.R.anim.slide_up, 0)


        }

        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FragmentDialer.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentDialer().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}