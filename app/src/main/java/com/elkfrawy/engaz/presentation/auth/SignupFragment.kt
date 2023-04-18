package com.elkfrawy.engaz.presentation.auth

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.elkfrawy.engaz.R
import com.elkfrawy.engaz.databinding.FragmentSignupBinding
import com.elkfrawy.engaz.domain.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthOptions
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class SignupFragment : Fragment() {

    lateinit var binding: FragmentSignupBinding
    lateinit var controller: NavController

    @Inject
    lateinit var auth:FirebaseAuth
    val viewModel: AuthViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View{
        binding = FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        controller = findNavController()
         binding.btnRegistration.setOnClickListener {

             if (binding.edConfirmPassword.text.toString().isEmpty() ||
                 binding.registrationEdPassword.text.toString().isEmpty() ||
                 binding.registrationEdPhone.text.toString().isEmpty())
                 Toast.makeText(requireContext(), "Fill Empty Field", Toast.LENGTH_LONG).show()
             else{
                 if (binding.registrationEdPassword.text.toString() != binding.edConfirmPassword.text.toString()) {
                     Toast.makeText(requireContext(), "The confirm password confirmation does not match", Toast.LENGTH_LONG).show()
                 }else{

                     viewModel.user = User(
                         name = "",
                         password = binding.edConfirmPassword.text.toString(),
                         number = binding.registrationEdPhone.text.toString().toLong(),
                         address = "",
                         NationalId = "",
                         latitude = "",
                         longtitude = "",
                        rating = 0,
                     )
                     val direction = SignupFragmentDirections.actionSignupFragmentToAuthFragment()
                     controller.navigate(direction)
                 }
             }
         }

        binding.tvLogin.setOnClickListener {
            controller.popBackStack()
        }

    }


}