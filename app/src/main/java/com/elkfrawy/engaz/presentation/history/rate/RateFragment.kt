package com.elkfrawy.engaz.presentation.history.rate

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Message
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.elkfrawy.engaz.R
import com.elkfrawy.engaz.databinding.FragmentRateBinding
import com.elkfrawy.engaz.presentation.history.HistoryViewModel
import com.elkfrawy.engaz.presentation.util.EDIT_RATE_ID
import com.google.firebase.database.DatabaseReference
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RateFragment : DialogFragment() {

    lateinit var binding: FragmentRateBinding
    val viewModel: HistoryViewModel by viewModels()
    @Inject
    lateinit var databaseReference: DatabaseReference

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentRateBinding.inflate(inflater, container, false)
        if (dialog != null && dialog?.window != null) {
            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getToken()

        val userId = requireArguments().getString("rate_user_id")
        var totalRate = requireArguments().getFloat("total_rate_user")
        val totalRater = requireArguments().getInt("total_rater_user")
        val isEdit = requireArguments().getInt("editRate")
        val rateKey = requireArguments().getString("rate_key")
        val number = requireArguments().getFloat("rating_number")
        val message = requireArguments().getString("rating_message")
        Log.d("Edit Rate", "total rate: $totalRate")
        Log.d("Edit Rate", "total rate: $number")
        if (isEdit == EDIT_RATE_ID){
            setRate(number, message!!)
            Log.d("Edit Rate", "total rate: ${totalRate - number}")
            totalRate -= number
        }

        binding.btnAddRate.setOnClickListener {

            val rate = binding.rateResult.rating
            val message = binding.edComment.text.toString()


            if (message.isEmpty())
                Toast.makeText(requireContext(), "Write Your Comment", Toast.LENGTH_LONG).show()
            else{

                if (isEdit == EDIT_RATE_ID){
                    val map = mutableMapOf<String, Any>()
                    map["rate"] = rate
                    map["message"] = message
                    databaseReference.child("Rate").child(userId!!).child("class").child(rateKey!!).updateChildren(map)

                    val map2 = mutableMapOf<String, Any>()
                    map2["total_rate"] = totalRate + rate
                    databaseReference.child("Users").child(userId).child("class").updateChildren(map2)
                }else{
                    val ratef = databaseReference.child("Rate").child(userId!!).child("class").push()
                    ratef.child("rate").setValue(rate)
                    ratef.child("message").setValue(message)
                    ratef.child("from").setValue(viewModel.token)

                    val map = mutableMapOf<String, Any>()
                    map.put("total_rate", totalRate + rate)
                    map.put("total_rater", totalRater + 1)
                    databaseReference.child("Users").child(userId).child("class").updateChildren(map)
                }
                dismiss()
            }
        }

    }


    fun setRate(number: Float, message: String){
        binding.rateResult.rating = number
        binding.edComment.setText(message)
    }

}