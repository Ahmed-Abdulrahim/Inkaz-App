package com.elkfrawy.engaz.presentation.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.elkfrawy.engaz.R
import com.elkfrawy.engaz.data.util.ClientException
import com.elkfrawy.engaz.data.util.DataNotAvailableException
import com.elkfrawy.engaz.data.util.ServerException
import com.elkfrawy.engaz.databinding.FragmentProfileBinding
import com.elkfrawy.engaz.domain.model.Car
import com.elkfrawy.engaz.domain.model.FirebaseUser
import com.elkfrawy.engaz.domain.model.User
import com.elkfrawy.engaz.domain.model.UserCar
import com.elkfrawy.engaz.presentation.loadImage
import com.elkfrawy.engaz.presentation.util.CAR_EDIT_PROFILE_KEY
import com.elkfrawy.engaz.presentation.util.USER_EDIT_PROFILE_KEY
import com.elkfrawy.engaz.presentation.util.hide
import com.elkfrawy.engaz.presentation.util.show
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.getValue
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.StorageReference
import dagger.hilt.android.AndroidEntryPoint
import dagger.multibindings.IntKey
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    lateinit var binding: FragmentProfileBinding
    val viewModel: ProfileViewModel by activityViewModels()

    @Inject
    lateinit var databaseReference: DatabaseReference


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showHideViews(false)
        //viewModel.getUserId()
        binding.btnEditProfile.setOnClickListener {
            val i = Intent(requireActivity(), EditProfileActivity::class.java)
            i.putExtra(USER_EDIT_PROFILE_KEY, viewModel.user2)
            i.putExtra(CAR_EDIT_PROFILE_KEY, viewModel.car2)
            startActivity(i)
        }
        viewModel.gettoken()
        viewModel.getToken()

        viewModel.tokenLiveData.observe(viewLifecycleOwner) {
            databaseReference.child("Car").child(it)
                .addChildEventListener(object : ChildEventListener {
                    override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                        //Log.d("Data form firebase", "data2 : ${snapshot.getValue(UserCar::class.java)}")
                        if (snapshot.exists()) {
                            Log.d("Notification Error", "onChildAdded1: ${snapshot.getValue()}")
                            Log.d(
                                "Notification Error",
                                "onChildAdded1: ${snapshot.getValue<UserCar>()}")
                            val car = snapshot.getValue(UserCar::class.java)
                            viewModel.car2 = car
                            updateCar(car!!)

                        }
                    }

                    override fun onChildChanged(
                        snapshot: DataSnapshot,
                        previousChildName: String?
                    ) {
                        if (snapshot.exists()) {
                                    val car = snapshot.getValue(UserCar::class.java)
                                    viewModel.car2 = car
                                    updateCar(car!!)
                        }
                    }

                    override fun onChildRemoved(snapshot: DataSnapshot) {
                    }

                    override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                })

            databaseReference.child("Users").child(it)
                .addChildEventListener(object : ChildEventListener {
                    override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                        if (snapshot.exists()) {
                            Log.d("Notification Error", "${snapshot.getValue()}")
                            val user = snapshot.getValue(FirebaseUser::class.java)
                            viewModel.user2 = user
                            val rate = (user?.total_rate!!) / user.total_rater!!.toFloat()
                            binding.profileStar.text = "${rate}"
                            updateUser(user)
                        }
                    }

                    override fun onChildChanged(snapshot: DataSnapshot,
                        previousChildName: String?) {
                        if (snapshot.exists()) {
                            val user = snapshot.getValue(FirebaseUser::class.java)
                            viewModel.user2 = user
                            updateUser(user!!)
                        }
                    }
                    override fun onChildRemoved(snapshot: DataSnapshot) {
                    }

                    override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                })

            binding.profileStar.setOnClickListener {
                val i = Intent(requireContext(), RatesActivity::class.java)
                startActivity(i)
            }

        }

        viewModel.exepection.observe(viewLifecycleOwner) {
            if (it == ServerException())
                setExceptionView(false)
            else
                setExceptionView(true)
        }
    }

    fun updateCar(car: UserCar) {
        binding.apply {
            userCarColor.text = car.car_color
            userCarType.text = "${car.car_name} - ${car.car_year}"
            carLicenseInfo.text = "${car.car_number} - ${car.car_letter}"
        }

    }

    fun updateUser(it: FirebaseUser) {
        viewModel.user2 = it
        binding.userName.text = it.name
        loadImage(binding.userImg, it.url)
        binding.userAddress.text = it.address
        binding.userId.text = it.nationalId
        binding.userMobile.text = "${it.mobile}"
        showHideViews(true)
    }

    private fun setExceptionView(internetError: Boolean) {
        binding.allViewGroub.hide()
        binding.profileProgress.hide()
        binding.exceptionImg.show()
        if (internetError)
            binding.exceptionImg.setImageResource(R.drawable.no_internet)
        else
            binding.exceptionImg.setImageResource(R.drawable.server_error)
    }


    private fun showHideViews(show: Boolean) {
        if (show) {
            binding.allViewGroub.show()
            binding.profileProgress.hide()
            binding.exceptionImg.hide()
        } else {
            binding.allViewGroub.hide()
            binding.profileProgress.show()
            binding.exceptionImg.hide()
        }
    }

}