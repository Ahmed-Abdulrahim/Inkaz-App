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
import com.elkfrawy.engaz.databinding.FragmentUserInfoBinding
import com.elkfrawy.engaz.presentation.profile.ProfileViewModel
import com.google.firebase.database.DatabaseReference
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class UserInfoFragment : Fragment() {

    lateinit var binding: FragmentUserInfoBinding
    val viewModel: ProfileViewModel by activityViewModels()
    @Inject lateinit var databaseReference: DatabaseReference

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentUserInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.personalInfoToolbar.setOnClickListener{ findNavController().popBackStack() }
        viewModel.gettoken()
        binding.apply {
            viewModel.user2?.let {
                edName.setText(it.name)
                edAddress.setText(it.address)
                edNatioinalId.setText(it.nationalId)
            }
        }

        binding.personalInfoToolbar.setOnMenuItemClickListener {

            if (binding.edName.text.toString().isEmpty() &&
                binding.edAddress.text.toString().isEmpty() &&
                binding.edNatioinalId.text.toString().isEmpty())
                Toast.makeText(requireContext(), "Fill Empty Field", Toast.LENGTH_LONG).show()
            else{
                viewModel.user2?.let {user->
                    user.name = binding.edName.text.toString()
                    user.nationalId = binding.edNatioinalId.text.toString()
                    user.address = binding.edAddress.text.toString()

                    val map = mutableMapOf<String, Any>()
                    map.put("name", binding.edName.text.toString())
                    map.put("address", binding.edAddress.text.toString())
                    map.put("nationalId", binding.edNatioinalId.text.toString())
                    databaseReference.child("Users").child(viewModel.userToken!!).child("class").updateChildren(map)
                    findNavController().popBackStack()
                }
            }
            true
        }

    }

}