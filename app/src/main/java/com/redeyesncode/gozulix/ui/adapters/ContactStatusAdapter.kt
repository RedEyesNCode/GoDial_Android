package com.redeyesncode.gozulix.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.redeyesncode.gozulix.databinding.ItemContactStatusBinding
import com.redeyesncode.gozulix.room.ContactEntity

class ContactStatusAdapter(var context: Context,var dataList:List<ContactEntity>,var onClickActivity:ContactStatusAdapter.onClick):RecyclerView.Adapter<ContactStatusAdapter.MyViewholder>() {

    lateinit var binding:ItemContactStatusBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewholder {
        binding = ItemContactStatusBinding.inflate(LayoutInflater.from(context))



        return MyViewholder(binding)

    }

    override fun onBindViewHolder(holder: MyViewholder, position: Int) {
        val data = dataList[position]
        holder.binding.apply {
            tvContactName.text = data.contactName.toString()
            tvNote.text = data.note.toString()
            tvContactNumber.text = data.contactNumber.toString()
            tvStatus.text = "Status ${data.status.toString()}"
        }
        holder.binding.mainCard.setOnClickListener {
            onClickActivity.onContactStatusClick(data)
        }


    }

    override fun getItemCount(): Int {

        return dataList.size
    }

    interface onClick{

        fun onContactStatusClick(data:ContactEntity)

    }

    class MyViewholder(var binding:ItemContactStatusBinding):RecyclerView.ViewHolder(binding.root)

}