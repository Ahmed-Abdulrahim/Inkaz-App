package com.elkfrawy.engaz.presentation.profile.edit_profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.elkfrawy.engaz.R
import com.elkfrawy.engaz.databinding.FragmentEditMobileBinding
import com.elkfrawy.engaz.presentation.profile.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditMobileFragment : Fragment() {

    lateinit var binding: FragmentEditMobileBinding
    val viewModel: ProfileViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentEditMobileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setView()
        binding.mobileToolbar.setNavigationOnClickListener { findNavController().popBackStack() }
        binding.btnMobileNext.setOnClickListener {

            if (binding.edMobile.text.toString().isEmpty())
                Toast.makeText(requireContext(), "Fill Empty Field", Toast.LENGTH_LONG).show()
            else{
                if (binding.edMobile.text.toString().length != 11)
                    Toast.makeText(requireContext(), "Enter Valid Number", Toast.LENGTH_LONG).show()
                else{
                    //viewModel.updateMobile(viewModel.userId!!, binding.edMobile.text.toString().toLong())
                    val direction = EditMobileFragmentDirections.actionEditMobileFragmentToCodeFragment()
                    findNavController().navigate(direction)
                }
            }

        }
    }
    fun setView(){
        binding.edMobile.setText("${viewModel.user!!.number}")
    }
}
