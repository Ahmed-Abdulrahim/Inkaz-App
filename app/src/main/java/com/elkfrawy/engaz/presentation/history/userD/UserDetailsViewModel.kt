package com.elkfrawy.engaz.presentation.history.userD

import androidx.lifecycle.ViewModel
import com.elkfrawy.engaz.domain.model.FirebaseUser
import com.elkfrawy.engaz.domain.model.Rate
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserDetailsViewModel @Inject constructor(): ViewModel() {

    var rates= ArrayList<Rate>()
    lateinit var userId: String
    var users = ArrayList<FirebaseUser>()


}