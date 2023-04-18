package com.elkfrawy.engaz.presentation.auth

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.elkfrawy.engaz.R
import com.elkfrawy.engaz.databinding.FragmentGetUserInfoBinding
import com.elkfrawy.engaz.domain.model.Car
import com.elkfrawy.engaz.domain.model.FirebaseUser
import com.elkfrawy.engaz.domain.model.UserCar
import com.elkfrawy.engaz.presentation.home.MainActivity
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import org.w3c.dom.Text
import javax.inject.Inject
import kotlin.math.log

@AndroidEntryPoint
class GetUserInfoFragment : Fragment() {

    lateinit var binding: FragmentGetUserInfoBinding
    val viewModel: AuthViewModel by activityViewModels()

    @Inject
    lateinit var databaseReference: DatabaseReference
    var isNewUser = true
    lateinit var token: String

    @Inject
    lateinit var databaseReference2: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGetUserInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        token = GetUserInfoFragmentArgs.fromBundle(requireArguments()).authId

        databaseReference.child("Users").child(token).addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {

                if (snapshot.exists()) {
                    val user = snapshot.getValue(FirebaseUser::class.java)
                    Log.d("Check User", "user: ${snapshot.getValue(FirebaseUser::class.java)}")
                    Log.d("Check User", "user is null: ${user == null}")
                    if (user != null) isNewUser = false
                    updateUserViews(user!!)
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        databaseReference.child("Car").child(token).addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                if (snapshot.exists()) {
                    val car = snapshot.getValue(UserCar::class.java)
                    updateCarViews(car!!)

                }

            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildRemoved(snapshot: DataSnapshot) {

            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

        binding.btnConfirm.setOnClickListener {

            val address = binding.edAddress.text.toString()
            val carColor = binding.edCarColor.text.toString()
            val carLetter = binding.edCarLetter.text.toString()
            val carNumber = binding.edCarNumber.text.toString()
            val nationalId = binding.edNationalId.text.toString()
            val name = binding.edName.text.toString()
            val year = binding.edCarYear.text.toString()
            val model = binding.edCarModel.text.toString()

            if (address.isEmpty() || carColor.isEmpty() ||
                carLetter.isEmpty() || carNumber.isEmpty() ||
                nationalId.isEmpty() || name.isEmpty() ||
                year.isEmpty() || model.isEmpty()
            )
                Toast.makeText(requireContext(), "Fill Empty Fields", Toast.LENGTH_LONG).show()
            else {

                viewModel.user?.address = address
                viewModel.user?.name = name
                viewModel.user?.NationalId = nationalId
                viewModel.car = Car(
                    car_name = model, car_color = carColor, car_license = carLetter,
                    car_model = year.toInt(), car_number = carNumber.toInt(), user_id = 0
                )


                if (isNewUser) {
                    addNewUser(
                        name,
                        address,
                        nationalId,
                        year.toInt(),
                        model,
                        carNumber.toInt(),
                        carColor,
                        carLetter
                    )
                    Log.d("Check User", "-> its a new user")

                } else {
                    updateUser(
                        name,
                        address,
                        nationalId,
                        year.toInt(),
                        model,
                        carNumber.toInt(),
                        carColor,
                        carLetter
                    )
                    Log.d("Check User", "-> its a not new user")
                }


/*                viewModel.insertUser(
                    name, viewModel.user?.number!!, nationalId,
                    viewModel.user?.password!!, address, model,
                    year.toInt(), carNumber.toInt(), carColor, carLetter
                )*/
                val i = Intent(requireActivity(), MainActivity::class.java)
                startActivity(i)
                requireActivity().finish()

            }
        }
    }

    fun updateUser(
        name: String, address: String, nationalId: String, year: Int,
        model: String, number: Int, color: String, letter: String
    ) {
        val map2 = mutableMapOf<String, Any>()
        map2.put("name", name)
        map2.put("address", address)
        map2.put("nationalId", nationalId)
        databaseReference.child("Users").child(token).child("class").updateChildren(map2)

        val map = mutableMapOf<String, Any>()
        map.put("car_model", year)
        map.put("car_name", model)
        map.put("car_number", number)
        map.put("car_color", color)
        map.put("car_letter", letter)
        databaseReference.child("Car").child(token).child("class").updateChildren(map)
    }

    fun addNewUser(
        name: String, address: String, nationalId: String, year: Int,
        model: String, number: Int, color: String, letter: String
    ) {

        val newUser = databaseReference.child("Users").child(token).child("class");
        newUser.child("name").setValue(name)
        newUser.child("address").setValue(address)
        newUser.child("mobile").setValue(viewModel.number!!)
        newUser.child("nationalId").setValue(nationalId)
        newUser.child("latitude").setValue("")
        newUser.child("longitude").setValue("")
        newUser.child("total_rate").setValue(0.0)
        newUser.child("total_rater").setValue(0)

        val newCar = databaseReference2.child("Car").child(token).child("class")
        newCar.child("car_name").setValue(model)
        newCar.child("car_year").setValue(year)
        newCar.child("car_number").setValue(number)
        newCar.child("car_letter").setValue(letter)
        newCar.child("car_color").setValue(color)
    }

    fun updateUserViews(user: FirebaseUser?) {
        Log.d("Check User", "updateUser: ${user}")
        Log.d("Check User", "updateUser: ${user == null}")
        if (user != null) isNewUser = false
        user?.let {
            binding.apply {
                edName.setText(it.name)
                edNationalId.setText(it.nationalId)
                edAddress.setText(it.address)
            }
        }
    }

    fun updateCarViews(car: UserCar) {

        binding.apply {
            edCarModel.setText(car.car_name)
            edCarYear.setText("${car.car_year}")
            edCarColor.setText(car.car_color)
            edCarNumber.setText("${car.car_number}")
            edCarLetter.setText(car.car_letter)
        }
    }


}