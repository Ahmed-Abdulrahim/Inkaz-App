package com.elkfrawy.engaz.presentation.profile

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.elkfrawy.engaz.R
import com.elkfrawy.engaz.domain.model.Car
import com.elkfrawy.engaz.domain.model.FirebaseUser
import com.elkfrawy.engaz.domain.model.User
import com.elkfrawy.engaz.domain.model.UserCar
import com.elkfrawy.engaz.presentation.util.CAR_EDIT_PROFILE_KEY
import com.elkfrawy.engaz.presentation.util.USER_EDIT_PROFILE_KEY
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditProfileActivity : AppCompatActivity() {

    val viewModel: ProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        viewModel.getUserId()
        viewModel.getUserNCar()
        val i = intent
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            viewModel.user2 = i.getParcelableExtra(USER_EDIT_PROFILE_KEY, FirebaseUser::class.java)
            viewModel.car2 = i.getParcelableExtra(CAR_EDIT_PROFILE_KEY, UserCar::class.java)
        } else {
            viewModel.user2 = i.getParcelableExtra<FirebaseUser>(USER_EDIT_PROFILE_KEY)
            viewModel.car2 = i.getParcelableExtra<UserCar>(CAR_EDIT_PROFILE_KEY)
        }
    }
}