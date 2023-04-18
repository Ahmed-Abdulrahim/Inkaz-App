package com.elkfrawy.engaz.presentation.profile.edit_profile

import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.elkfrawy.engaz.R
import com.elkfrawy.engaz.databinding.FragmentEditOptionsBinding
import com.elkfrawy.engaz.domain.model.FirebaseUser
import com.elkfrawy.engaz.presentation.auth.AuthActivity
import com.elkfrawy.engaz.presentation.loadImage
import com.elkfrawy.engaz.presentation.profile.ProfileViewModel
import com.elkfrawy.engaz.presentation.util.hide
import com.elkfrawy.engaz.presentation.util.show
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class EditOptionsFragment : Fragment() {

    lateinit var binding: FragmentEditOptionsBinding
    val viewModel: ProfileViewModel by activityViewModels()

    lateinit var activityResult: ActivityResultLauncher<Uri>
    @Inject
    lateinit var auth: FirebaseAuth

    @Inject
    lateinit var databaseReference: DatabaseReference

    lateinit var storageReference:StorageReference

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentEditOptionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSaveImg.hide()
        storageReference = FirebaseStorage.getInstance().getReference("userImage")
        viewModel.gettoken()
        viewModel.getToken()
        showProgress(false)

        viewModel.tokenLiveData.observe(viewLifecycleOwner){
            databaseReference.child("Users").child(it)
                .addChildEventListener(object : ChildEventListener {
                    override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                        if (snapshot.exists()) {
                            val user = snapshot.getValue(FirebaseUser::class.java)
                            loadImage(binding.editProfileImg, user?.url)
                        }
                    }

                    override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                        if (snapshot.exists()) {
                        }
                    }
                    override fun onChildRemoved(snapshot: DataSnapshot) {
                    }

                    override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                })
        }


        val content = registerForActivityResult(ActivityResultContracts.GetContent()){
            if (it != null){
                viewModel.imageUri = it
                binding.editProfileImg.setImageURI(it)
                binding.btnSaveImg.show()
            }else{
                binding.btnSaveImg.hide()
            }
        }

        binding.historyDetailsToolbar.setNavigationOnClickListener {
            requireActivity().finish()
        }

        binding.btnChangeImg.setOnClickListener {
            content.launch("image/*")
        }

        binding.editPersonalInfo.setOnClickListener { navigate(EditOptionsFragmentDirections.actionEditOptionsFragmentToUserInfoFragment()) }
        binding.editCarInfo.setOnClickListener { navigate(EditOptionsFragmentDirections.actionEditOptionsFragmentToCarInfoFragment()) }
        binding.signout.setOnClickListener {
            auth.signOut()
            viewModel.saveString("")
            requireActivity().finishAffinity()
            val i = Intent(requireContext(), AuthActivity::class.java)
            startActivity(i)
        }

        binding.btnSaveImg.setOnClickListener {
            showProgress(true)
            uploadFile(viewModel.imageUri)
        }
    }

    private fun uploadFile(uri: Uri?){
        if (uri != null){
            val file = storageReference.child(viewModel.userToken!!)
            file.putFile(uri).addOnSuccessListener {

                val map = mutableMapOf<String, Any>()

                it.metadata?.reference?.downloadUrl!!.addOnSuccessListener {
                    map["url"] = it.toString()
                    databaseReference.child("Users").child(viewModel.userToken!!).child("class").updateChildren(map)
                }
                databaseReference.child("Users").child(viewModel.userToken!!).child("class").updateChildren(map)
                showProgress(false)
                requireActivity().finish()

            }.addOnProgressListener {

            }.addOnFailureListener{
                Log.d("Upload Image Output", "failure: ${it.message}")
                Toast.makeText(requireContext(), "Failed uploading your image, Try Again", Toast.LENGTH_LONG).show()
                showProgress(false)
            }
        }
    }

    fun navigate(navDirection: NavDirections){
        findNavController().navigate(navDirection)
    }

    fun showProgress(bool:Boolean){
        if (bool){
            binding.progressBackgroundEd.show()
            binding.edProgress.show()
        }else{
            binding.progressBackgroundEd.hide()
            binding.edProgress.hide()
        }
    }

}