package com.elkfrawy.engaz.presentation.auth

import android.content.Intent
import android.os.Bundle
import android.telecom.PhoneAccount
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.elkfrawy.engaz.R
import com.elkfrawy.engaz.databinding.FragmentLoginBinding
import com.elkfrawy.engaz.presentation.home.MainActivity
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class LoginFragment : Fragment() {

    lateinit var binding: FragmentLoginBinding
    lateinit var controller: NavController
    lateinit var auth: FirebaseAuth
    lateinit var firebaseUser: FirebaseUser
    val viewModel: AuthViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        controller = findNavController()
        auth = FirebaseAuth.getInstance()

        binding.btnNext.setOnClickListener {
            if (binding.edPhoneNumber.text.toString().isEmpty())
                Toast.makeText(requireActivity(), "Fill Empty Field", Toast.LENGTH_LONG).show()
            else {
                viewModel.number = binding.edPhoneNumber.text.toString()
                val direction = LoginFragmentDirections.actionLoginFragmentToAuthFragment()
                findNavController().navigate(direction)
            }
        }

    }


}


