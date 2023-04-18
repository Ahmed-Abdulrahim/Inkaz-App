package com.elkfrawy.engaz.presentation.history

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.elkfrawy.engaz.R
import com.elkfrawy.engaz.databinding.HistoryItemBinding
import com.elkfrawy.engaz.domain.model.FirebaseHistory
import com.elkfrawy.engaz.domain.model.History

class HistoryAdapter(val listener: OnHistoryClicked) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    lateinit var context:Context
    var historyList:List<FirebaseHistory> = ArrayList<FirebaseHistory>()

    class HistoryViewHolder(val binding: HistoryItemBinding): ViewHolder(binding.root){
    }

    fun submitList(historyList:List<FirebaseHistory>){
        this.historyList = historyList
        notifyDataSetChanged()
    }

    fun itemAdded(historyList:List<FirebaseHistory>, pos: Int){
        this.historyList = historyList
        notifyItemInserted(pos)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        context = parent.context
        val binding = HistoryItemBinding.inflate(LayoutInflater.from(context), parent, false)
        val holder = HistoryViewHolder(binding)
        binding.root.setOnClickListener {
            listener.onHistoryItemClicked(holder.adapterPosition)
        }
        return holder
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {

        if (historyList.isNotEmpty()){
            val historyItem = historyList[position]

            holder.binding.apply {
                helpTitle.text = historyItem.title
                helpDate.text = historyItem.date
                helpText.text = historyItem.problem_type
                if (historyItem.state!!)
                    askedHelp(this)
                else
                    receivedHelp(this)
            }
        }
    }


    override fun getItemCount(): Int = historyList.size

    private fun askedHelp(binding: HistoryItemBinding){
        binding.apply {
            helpState.text = "Asked"
            helpState.background = ContextCompat.getDrawable(context, R.drawable.help_asked)
            helpState.setTextColor(ContextCompat.getColor(context, R.color.green))
        }
    }

    private fun receivedHelp(binding: HistoryItemBinding){
        binding.apply {
            helpState.text = "Received"
            helpState.background = ContextCompat.getDrawable(context, R.drawable.help_state_received)
            helpState.setTextColor(ContextCompat.getColor(context, android.R.color.holo_red_dark))
        }
    }

    interface OnHistoryClicked{
        fun onHistoryItemClicked(position: Int)
    }

}
