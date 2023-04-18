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
import com.elkfrawy.engaz.databinding.FragmentChangePassBinding
import com.elkfrawy.engaz.presentation.auth.AuthViewModel
import com.elkfrawy.engaz.presentation.profile.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangePassFragment : Fragment() {

    lateinit var binding: FragmentChangePassBinding
    val viewModel: ProfileViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View{
        binding = FragmentChangePassBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.changePassToolbar.setOnClickListener { findNavController().popBackStack() }
        binding.changePassToolbar.setOnMenuItemClickListener {
            binding.apply {

                if (edNewPass1.text.toString().isEmpty() || edNewPass2.text.toString()
                        .isEmpty() || edOldPass.text.toString().isEmpty()
                )
                    Toast.makeText(requireContext(), "Fill Empty Field", Toast.LENGTH_LONG).show()
                else {
                    if (edNewPass1.text.toString() != edNewPass2.text.toString())
                        Toast.makeText(
                            requireContext(),
                            "The confirm password confirmation does not match",
                            Toast.LENGTH_LONG
                        ).show()
                    else {
                        viewModel.updatePassword(viewModel.userId!!, edNewPass2.text.toString())
                        findNavController().popBackStack()
                        Toast.makeText(requireContext(), "Email Changed Succssful", Toast.LENGTH_LONG).show()
                    }
                }

            }
            true
        }
    }


}