package com.redeyesncode.gozulix.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.redeyesncode.gozulix.R
import com.redeyesncode.gozulix.data.ContactHistoryItem
import com.redeyesncode.gozulix.databinding.ItemCallHistoryBinding

class ContactHistoryAdapter(var context: Context,var contactHistoryList: List<ContactHistoryItem>) :
    RecyclerView.Adapter<ContactHistoryAdapter.MyViewHolder>() {
    lateinit var binding: ItemCallHistoryBinding

    companion object {
        private val differCallback = object : DiffUtil.ItemCallback<ContactHistoryItem>() {
            override fun areItemsTheSame(
                oldItem: ContactHistoryItem,
                newItem: ContactHistoryItem
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ContactHistoryItem,
                newItem: ContactHistoryItem
            ): Boolean {
                return oldItem.contactDuration == newItem.contactDuration
            }

        }
    }


    private val differ = AsyncListDiffer(this, differCallback)
    var match: List<ContactHistoryItem>
        get() = differ.currentList
        set(value) = differ.submitList(value)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = contactHistoryList[position]
        binding.apply {
            if(item.contactName.isEmpty()){
                tvContactNumber.text = item.callNumber

            }
            tvContactNumber.text = item.contactName

            tvContactTime.text = item.contactDuration
            if(item.callType.equals("OUTGOING")){
                btnCallType.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.baseline_call_made_24))
            }else if(item.callType.equals("MISSED")){
                btnCallType.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.baseline_call_missed_24))

            }else if(item.callType.equals("INCOMING")){
                btnCallType.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.baseline_call_received_24))

            }
            tvContactTime.text = item.callDate
            tvCallDuration.text = "Duration ${item.contactDuration}"

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        binding = ItemCallHistoryBinding.inflate(LayoutInflater.from(context),parent,false)

        return MyViewHolder(binding)
    }



    override fun getItemCount(): Int {
        return contactHistoryList.size
    }

    class MyViewHolder(var binding:ItemCallHistoryBinding) : RecyclerView.ViewHolder(binding.root)
}