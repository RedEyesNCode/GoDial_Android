package com.redeyesncode.gozulix.ui.fragments

import android.content.ContentResolver
import android.content.Context
import android.os.Bundle
import android.provider.CallLog
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.redeyesncode.gozulix.R
import com.redeyesncode.gozulix.data.ContactHistoryItem
import com.redeyesncode.gozulix.databinding.FragmentCallHistoryBinding
import com.redeyesncode.gozulix.ui.adapters.ContactHistoryAdapter
import com.redeyesncode.pay2kart.base.BaseFragment
import com.redeyesncode.redbet.base.BaseActivity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CallHistoryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CallHistoryFragment : BaseFragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var binding:FragmentCallHistoryBinding


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


        binding = FragmentCallHistoryBinding.inflate(inflater,container,false)

        val contacts = fetchCallRecords(fragmentContext)

        binding.recvCalls.adapter = ContactHistoryAdapter(fragmentContext,contacts)
        binding.recvCalls.layoutManager = LinearLayoutManager(fragmentContext,LinearLayoutManager.VERTICAL,false)

        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CallHistoryFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CallHistoryFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
    fun fetchCallRecords(context: Context): List<ContactHistoryItem> {
        val contactHistoryList = mutableListOf<ContactHistoryItem>()
        val contentResolver: ContentResolver = context.contentResolver

        val projection = arrayOf(
            CallLog.Calls.CACHED_NAME, // Contact name
            CallLog.Calls.DURATION,    // Call duration
            CallLog.Calls.NUMBER,      // Phone number
            CallLog.Calls.TYPE,        // Call type (1: Incoming, 2: Outgoing, 3: Missed)
            CallLog.Calls.DATE         // Call date
        )

        val cursor = contentResolver.query(
            CallLog.Calls.CONTENT_URI,
            projection,
            null,
            null,
            CallLog.Calls.DEFAULT_SORT_ORDER
        )

        cursor?.use {
            val nameIndex = it.getColumnIndex(CallLog.Calls.CACHED_NAME)
            val durationIndex = it.getColumnIndex(CallLog.Calls.DURATION)
            val numberIndex = it.getColumnIndex(CallLog.Calls.NUMBER)
            val typeIndex = it.getColumnIndex(CallLog.Calls.TYPE)
            val dateIndex = it.getColumnIndex(CallLog.Calls.DATE)

            while (it.moveToNext()) {
                val contactName = it.getString(nameIndex) ?: "Unknown"
                val contactDuration = it.getString(durationIndex)
                val callNumber = it.getString(numberIndex)
                val callType = when (it.getInt(typeIndex)) {
                    CallLog.Calls.INCOMING_TYPE -> "Incoming"
                    CallLog.Calls.OUTGOING_TYPE -> "Outgoing"
                    CallLog.Calls.MISSED_TYPE -> "Missed"
                    else -> "Unknown"
                }
                val callDateMillis = it.getLong(dateIndex)
                val callDate = formatCallDate(callDateMillis)

                val contact = ContactHistoryItem(contactName, contactDuration, callNumber, callType, callDate)
                contactHistoryList.add(contact)
            }
        }

        return contactHistoryList
    }
    private fun formatCallDate(callDateMillis: Long): String {
        val sdf = SimpleDateFormat("MMM d, h:mma", Locale.getDefault())
        val callDate = Date(callDateMillis)
        return sdf.format(callDate)
    }
}