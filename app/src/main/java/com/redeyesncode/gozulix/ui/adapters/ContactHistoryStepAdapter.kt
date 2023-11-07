package com.redeyesncode.gozulix.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.redeyesncode.gozulix.databinding.ItemContactStepHistoryBinding

class ContactHistoryStepAdapter(var context: Context,var dataList:ArrayList<String>):RecyclerView.Adapter<ContactHistoryStepAdapter.MyViewHolder>() {


    lateinit var binding:ItemContactStepHistoryBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        binding = ItemContactStepHistoryBinding.inflate(LayoutInflater.from(context))


        return MyViewHolder(binding)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = dataList[position]


    }

    override fun getItemCount(): Int {

        return dataList.size

    }

    class MyViewHolder(var binding:ItemContactStepHistoryBinding):RecyclerView.ViewHolder(binding.root)
}