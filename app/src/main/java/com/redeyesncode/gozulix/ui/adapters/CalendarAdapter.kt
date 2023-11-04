package com.redeyesncode.gozulix.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.redeyesncode.gozulix.R
import com.redeyesncode.gozulix.data.CalendarDate
import java.util.*

class CalendarAdapter(private val dateList: List<CalendarDate>) : RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_calender_card, parent, false)
        return CalendarViewHolder(view)
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        val date = dateList[position]

        holder.weekdayTextView.text = date.weekday
        holder.dateTextView.text = date.date
        holder.monthTextView.text = date.month
    }

    override fun getItemCount(): Int {
        return dateList.size
    }

    inner class CalendarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val weekdayTextView: TextView = itemView.findViewById(R.id.weekdayTextView)
        val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)
        val monthTextView: TextView = itemView.findViewById(R.id.monthTextView)
    }
}
