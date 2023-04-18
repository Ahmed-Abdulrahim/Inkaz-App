package com.elkfrawy.engaz.presentation.profile.edit_profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.elkfrawy.engaz.R
import com.elkfrawy.engaz.databinding.FragmentCodeBinding

class CodeFragment : Fragment() {

    lateinit var binding: FragmentCodeBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCodeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnConfirmCode.setOnClickListener {
            val direction = CodeFragmentDirections.actionCodeFragmentToEditOptionsFragment()
            findNavController().navigate(direction)
        }


    }

}