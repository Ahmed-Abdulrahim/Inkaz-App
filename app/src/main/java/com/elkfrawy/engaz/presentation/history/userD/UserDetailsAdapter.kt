package com.elkfrawy.engaz.presentation.history.userD

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.elkfrawy.engaz.databinding.RatingViewBinding
import com.elkfrawy.engaz.domain.model.FirebaseUser
import com.elkfrawy.engaz.domain.model.Rate
import com.elkfrawy.engaz.presentation.loadImage

class UserDetailsAdapter() : RecyclerView.Adapter<UserDetailsAdapter.HistoryViewHolder>() {

    lateinit var context:Context
    var rates:List<Rate> = ArrayList<Rate>()
    var users:List<FirebaseUser> = ArrayList<FirebaseUser>()

    class HistoryViewHolder(val binding: RatingViewBinding): ViewHolder(binding.root){
    }

    fun submitList(rates:List<Rate>, users:List<FirebaseUser>){
        this.rates = rates
        this.users = users
        notifyDataSetChanged()
    }

    fun addItem(rates:List<Rate>, users:List<FirebaseUser>, pos: Int){
        this.rates = rates
        this.users = users
        notifyItemInserted(pos)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        context = parent.context
        val binding = RatingViewBinding.inflate(LayoutInflater.from(context), parent, false)
        val holder = HistoryViewHolder(binding)
        return holder
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val rate = rates[position]
        val user = users[position]
        holder.binding.apply {
            rvUserRate.rating = rate.rate!!
            rvUserName.text = user.name
            rateReview.text = rate.message
            loadImage(rvUserImg, user.url)

        }
    }


    override fun getItemCount(): Int {
        if (rates.size > 3) return 3
        else return rates.size
    }


}
