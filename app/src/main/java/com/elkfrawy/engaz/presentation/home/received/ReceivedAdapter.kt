package com.elkfrawy.engaz.presentation.home.received

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.elkfrawy.engaz.R
import com.elkfrawy.engaz.databinding.HistoryItemBinding
import com.elkfrawy.engaz.databinding.ReceivedItemBinding
import com.elkfrawy.engaz.domain.model.FirebaseHistory
import com.elkfrawy.engaz.domain.model.History
import com.elkfrawy.engaz.domain.model.Problem

class ReceivedAdapter(val listener: OnReceivedClicked) : RecyclerView.Adapter<ReceivedAdapter.HistoryViewHolder>() {

    lateinit var context:Context
    var receivedList:List<Problem> = ArrayList<Problem>()

    class HistoryViewHolder(val binding: ReceivedItemBinding): ViewHolder(binding.root){
    }

    fun submitList(receivedList:List<Problem>){
        this.receivedList = receivedList
        notifyDataSetChanged()
    }

    fun itemAdded(receivedList:List<Problem>, pos: Int){
        this.receivedList = receivedList
        notifyItemInserted(pos)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        context = parent.context
        val binding = ReceivedItemBinding.inflate(LayoutInflater.from(context), parent, false)
        val holder = HistoryViewHolder(binding)
        binding.root.setOnClickListener {
            listener.onReceivedItemClicked(holder.adapterPosition)
        }
        return holder
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {

        if (receivedList.isNotEmpty()){
            val historyItem = receivedList[position]
            holder.binding.apply {

                receivedTitle.text = historyItem.title
                receivedDate.text = historyItem.date
                receivedType.text = historyItem.problem_type
            }
        }
    }


    override fun getItemCount(): Int = receivedList.size



    interface OnReceivedClicked{
        fun onReceivedItemClicked(position: Int)
    }

}
