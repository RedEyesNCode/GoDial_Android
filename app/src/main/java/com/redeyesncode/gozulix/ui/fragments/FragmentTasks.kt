package com.redeyesncode.gozulix.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.redeyesncode.gozulix.R
import com.redeyesncode.gozulix.data.CalendarDate
import com.redeyesncode.gozulix.databinding.FragmentTasksBinding
import com.redeyesncode.gozulix.room.ContactDatabase
import com.redeyesncode.gozulix.room.ContactEntity
import com.redeyesncode.gozulix.ui.adapters.CalendarAdapter
import com.redeyesncode.gozulix.ui.adapters.ContactStatusAdapter
import com.redeyesncode.pay2kart.base.BaseFragment
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentTasks.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentTasks : BaseFragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var binding:FragmentTasksBinding
    lateinit var pendingContacts:List<ContactEntity>

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

        binding = FragmentTasksBinding.inflate(layoutInflater)
        val data = generateCalendarDatesForYearWithStartingMonth(2023,10)
        binding.recvCalender.adapter = CalendarAdapter(data)
        binding.recvCalender.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)


        setupContactTable()
        return binding.root
    }

    private fun setupContactTable() {
        val contactDatabase = ContactDatabase.getDatabase(fragmentContext)
        val contactDao = contactDatabase.contactDao()

        pendingContacts = contactDao.getPendingContacts()

        if(pendingContacts.isEmpty()){
            showMessageDialog("NO CONTACTS ARE ADDED","DIALER")
        }else {
            binding.recvContactEntity.adapter = ContactStatusAdapter(fragmentContext,pendingContacts)
            binding.recvContactEntity.layoutManager = LinearLayoutManager(fragmentContext,LinearLayoutManager.VERTICAL,false)
        }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FragmentTasks.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentTasks().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
    fun generateCalendarDatesForYearWithStartingMonth(year: Int, startingMonth: Int): List<CalendarDate> {
        val calendarDates = mutableListOf<CalendarDate>()
        val calendar = Calendar.getInstance()
        calendar.set(year, startingMonth, 1) // Set to the desired starting month
        val dateFormat = SimpleDateFormat("EEEE", Locale.getDefault())

        while (calendar.get(Calendar.YEAR) == year && calendar.get(Calendar.MONTH) <= startingMonth + 2) {
            val weekday = dateFormat.format(calendar.time)
            val date = calendar.get(Calendar.DAY_OF_MONTH).toString()
            val monthName = SimpleDateFormat("MMMM", Locale.getDefault()).format(calendar.time)
            calendarDates.add(CalendarDate(weekday, date, monthName))
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        return calendarDates
    }
}