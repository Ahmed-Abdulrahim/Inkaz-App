package com.elkfrawy.engaz.presentation.profile.edit_profile

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.elkfrawy.engaz.R
import com.elkfrawy.engaz.databinding.FragmentCarInfoBinding
import com.elkfrawy.engaz.presentation.profile.ProfileViewModel
import com.google.firebase.database.DatabaseReference
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CarInfoFragment : Fragment() {

    lateinit var binding: FragmentCarInfoBinding
    val viewModel: ProfileViewModel by activityViewModels()
    @Inject lateinit var databaseReference: DatabaseReference

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCarInfoBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setView()
        binding.carInfoToolbar.setNavigationOnClickListener { findNavController().popBackStack() }
        viewModel.gettoken()
        binding.carInfoToolbar.setOnMenuItemClickListener {


            binding.apply {
                val model = edCarModel.text.toString()
                val year = edCarYear.text.toString()
                val number =  edCarNumber.text.toString()
                val color = edCarColor.text.toString()
                val letter = edCarLetter.text.toString()

                if (color.isEmpty() || letter.isEmpty() || model.isEmpty()
                    ||year.isEmpty() || number.isEmpty())
                    Toast.makeText(requireContext(), "Fill Empty Field", Toast.LENGTH_LONG).show()
                else{
                    /*viewModel.userId?.let {
                        viewModel.updateCarInfo(model, year.toInt(), number.toInt(), color, letter, viewModel.userId!!)
                    }*/
                    val map = mutableMapOf<String, Any>()
                    map.put("car_year", year.toInt())
                    map.put("car_name", model)
                    map.put("car_number", number.toInt())
                    map.put("car_color", color)
                    map.put("car_letter", letter)
                    databaseReference.child("Car").child(viewModel.userToken!!).child("class").updateChildren(map)
                    findNavController().popBackStack()
                }
            }
            true
        }

    }

    private fun setView(){
        binding.apply {
            viewModel.car2?.let {
                apply {
                    edCarColor.setText(it.car_color)
                    edCarLetter.setText(it.car_letter)
                    edCarModel.setText(it.car_name)
                    edCarYear.setText("${it.car_year}")
                    edCarNumber.setText("${it.car_number}")
                }
            }
        }
    }

}