package com.elkfrawy.engaz.presentation.auth

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elkfrawy.engaz.domain.model.Car
import com.elkfrawy.engaz.domain.model.User
import com.elkfrawy.engaz.domain.usecases.car.UpdateCar
import com.elkfrawy.engaz.domain.usecases.datastoreUsecases.LoadString
import com.elkfrawy.engaz.domain.usecases.datastoreUsecases.SaveInt
import com.elkfrawy.engaz.domain.usecases.datastoreUsecases.SaveString
import com.elkfrawy.engaz.domain.usecases.user.AddUser
import com.elkfrawy.engaz.domain.usecases.user.Login
import com.elkfrawy.engaz.domain.util.onFailure
import com.elkfrawy.engaz.domain.util.onSuccess
import com.elkfrawy.engaz.presentation.PREFERENCE_INTEGER_KEY
import com.elkfrawy.engaz.presentation.PREFERENCE_STRING_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val addUser: AddUser,
    private val login: Login,
    private val saveInt: SaveInt,
    private val saveString: SaveString,
    private val loadString: LoadString,
) : ViewModel() {

    var user: User? = null
    var car: Car? = null
    var number: String? = null

    val _login_liveData = MutableLiveData<Boolean>()
    val loginLiveData:LiveData<Boolean> = _login_liveData


    fun login(number: Long, password: String){
        viewModelScope.launch {
            login.execute(number, password).onSuccess {
                if (it > 0){
                    _login_liveData.value = true
                    saveInt.execute(PREFERENCE_INTEGER_KEY, it)
                }else
                    _login_liveData.value = false
            }.onFailure {
                _login_liveData.value = false
            }
        }
    }

     fun saveToken(token: String){
        viewModelScope.launch {
            saveString.execute(PREFERENCE_STRING_KEY, token)
        }
    }

    public fun loadToken(){
        viewModelScope.launch {
            loadString.execute(PREFERENCE_STRING_KEY)
        }
    }


    fun insertUser(
        name: String,
        number: Long,
        nationalID: String,
        password: String,
        address:String,
        car_name: String,
        car_model: Int,
        car_number: Int,
        car_color: String,
        car_license: String,
    ) {

        viewModelScope.launch {
            addUser.execute(name, number, nationalID,
                password, address, car_name, car_model,
                car_number, car_color, car_license).onSuccess {
                saveInt.execute(PREFERENCE_INTEGER_KEY, it)
            }.onFailure {
                Log.d("Responses Error", "Error: ${it.message}")
            }
        }

    }



}