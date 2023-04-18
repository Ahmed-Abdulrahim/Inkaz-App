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
import com.elkfrawy.engaz.databinding.FragmentEditEmailBinding
import com.elkfrawy.engaz.presentation.profile.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditEmailFragment : Fragment() {

    lateinit var binding: FragmentEditEmailBinding
    val viewModel: ProfileViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditEmailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setView()
        binding.changeEmailToolbar.setNavigationOnClickListener { findNavController().popBackStack() }
        binding.edEmailNext.setOnClickListener {

            if (binding.edEmail.text.toString().isEmpty())
                Toast.makeText(requireContext(), "Fill Empty Field", Toast.LENGTH_LONG).show()
            else {

                //viewModel.updateEmail(viewModel.userId!!, binding.edEmail.text.toString())
                val direction = EditEmailFragmentDirections.actionEditEmailFragmentToCodeFragment()
                findNavController().navigate(direction)
            }
        }
    }

    fun setView(){
        binding.edEmail.setText(viewModel.user!!.email)
    }

}