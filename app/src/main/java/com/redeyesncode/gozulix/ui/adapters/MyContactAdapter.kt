package com.redeyesncode.gozulix.ui.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.redeyesncode.gozulix.data.ContactItem
import com.redeyesncode.gozulix.databinding.ItemPhoneContactBinding
import java.util.Locale

class MyContactAdapter(var context:Context,var dataList:List<ContactItem>) :RecyclerView.Adapter<MyContactAdapter.MyViewHolder>(){


    lateinit var binding:ItemPhoneContactBinding
    private var filteredContactList: List<ContactItem> = dataList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        binding = ItemPhoneContactBinding.inflate(LayoutInflater.from(context),parent,false)


        return MyViewHolder(binding)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val data = dataList[position]

        holder.binding.apply {

            contactCheck.text = "${data.contactName.toString()}\n ${data.contactNumber.toString()}"

        }

// Set the background or appearance based on the selection state
        if (data.selected) {
            holder.itemView.setBackgroundColor(Color.LTGRAY)
        } else {
            holder.itemView.setBackgroundColor(Color.WHITE)
        }

        holder.binding.contactCheck.isChecked = data.selected

        holder.binding.contactCheck.setOnClickListener {
            data.selected = !data.selected

            // Update the checkbox state
            holder.binding.contactCheck.isChecked = data.selected
        }


    }
    fun getSelectedContacts(): List<ContactItem> {
        return dataList.filter { it.selected }
    }
    override fun getItemCount(): Int {
        return dataList.size
    }
    fun filter(query: String) {
        val lowercaseQuery = query.toLowerCase(Locale.getDefault())
        filteredContactList = if (query.isEmpty()) {
            dataList
        } else {
            dataList.filter { it.contactName.toLowerCase(Locale.getDefault()).contains(lowercaseQuery) }
        }
        notifyDataSetChanged()
    }
    class MyViewHolder(var binding:ItemPhoneContactBinding):RecyclerView.ViewHolder(binding.root)
}