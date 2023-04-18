package com.elkfrawy.engaz.presentation.auth

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isEmpty
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.elkfrawy.engaz.databinding.FragmentAuthBinding
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.database.DatabaseReference
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class AuthFragment : Fragment() {

    @Inject
    lateinit var auth: FirebaseAuth
    lateinit var firebaseUser:FirebaseUser
    lateinit var binding: FragmentAuthBinding
    lateinit var controller: NavController
    val viewModel: AuthViewModel by activityViewModels()
    var code = ""


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAuthBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        controller = findNavController()

        auth.setLanguageCode("eg")
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber("+2${viewModel.number}")
            .setActivity(requireActivity())
            .setTimeout(30L, TimeUnit.SECONDS)
            .setCallbacks(callback)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)

        binding.btnConfirmCode.setOnClickListener {

            if (binding.otpCode.isEmpty())
                Toast.makeText(requireContext(), "Enter The code first", Toast.LENGTH_LONG).show()
            else{
                if (code.isEmpty())
                    Toast.makeText(requireContext(), "Code is not correct", Toast.LENGTH_LONG).show()
                else{
                    val credential: PhoneAuthCredential = PhoneAuthProvider.getCredential(
                        code, binding.otpCode.otp!!
                    )
                    signInWithPhoneAuthCredential(credential)
                }
            }
        }
    }

    val callback =  object : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            binding.otpCode.setOTP(credential.smsCode!!)
            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {

            if (e is FirebaseAuthInvalidCredentialsException) {
            } else if (e is FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
                Toast.makeText(requireContext(), "The SMS quota for the project has been exceeded", Toast.LENGTH_LONG).show()
            }
        }

        override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
            code = verificationId
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val token = task.result.user?.uid
                    Log.d("User Token", "signInWithPhoneAuthCredential: Token $token")
                    viewModel.saveToken(token!!)
                    firebaseUser = task.result.user!!
                    val direction = AuthFragmentDirections.actionAuthFragmentToGetUserInfoFragment(token)
                    findNavController().navigate(direction)
                    // Sign in success, update UI with the signed-in user's information
                } else {
                    // Sign in failed, display a message and update the UI
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                        Toast.makeText(requireContext(), "Invalid Code", Toast.LENGTH_LONG).show()
                    }
                    // Update UI
                }
            }
    }


}