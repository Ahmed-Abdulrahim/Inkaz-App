package com.elkfrawy.engaz.presentation.profile

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elkfrawy.engaz.domain.model.*
import com.elkfrawy.engaz.domain.usecases.car.*
import com.elkfrawy.engaz.domain.usecases.datastoreUsecases.LoadInt
import com.elkfrawy.engaz.domain.usecases.datastoreUsecases.LoadString
import com.elkfrawy.engaz.domain.usecases.datastoreUsecases.SaveString
import com.elkfrawy.engaz.domain.usecases.user.*
import com.elkfrawy.engaz.domain.util.onFailure
import com.elkfrawy.engaz.domain.util.onSuccess
import com.elkfrawy.engaz.presentation.PREFERENCE_INTEGER_KEY
import com.elkfrawy.engaz.presentation.PREFERENCE_STRING_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getUser: GetUser,
    private val updateEmail: UpdateEmail,
    private val updateMobile: UpdateMobile,
    private val updatePassword: UpdatePassword,
    private val updateUserInfo: UpdateUserInfo,
    private val updateCar: UpdateCar,
    private val getCar: GetCar,
    private val loadInt: LoadInt,
    private val saveString: SaveString,
    private val loadString: LoadString
) : ViewModel() {

    private val _car_liveData: MutableLiveData<List<Car>> = MutableLiveData()
    val carLiveData: LiveData<List<Car>> = _car_liveData
    private val _exepection: MutableLiveData<Throwable> = MutableLiveData()
    val exepection:LiveData<Throwable> = _exepection

    private val _token: MutableLiveData<String> = MutableLiveData()
    val tokenLiveData: LiveData<String> = _token

    var imageUri = Uri.parse("")

    fun getToken(){
        viewModelScope.launch {
            _token.value = loadString.execute(PREFERENCE_STRING_KEY)
        }
    }

    init {
        gettoken()
    }
    var car: Car? = null
    var car2: UserCar? = null
    var userId :Long? = null
    var userToken :String? = null
    var rates = ArrayList<Rate>()
    var rateUsers = ArrayList<FirebaseUser>()

    fun getCar(user_id: Long) {
        viewModelScope.launch {
            getCar.execute(user_id).onSuccess {
                _car_liveData.value = it
                car = it[0]
            }.onFailure {
                _exepection.value = it
                Log.d("Responses Error", "${it.message}")
                Log.d("Code from retrofit", "code failed: ${it}")
            }
        }
    }

    fun saveString(token: String){
        viewModelScope.launch {
            saveString.execute(PREFERENCE_STRING_KEY, token)
        }
    }

    fun getUserId(){
        viewModelScope.launch {
            userId = loadInt.execute(PREFERENCE_INTEGER_KEY)
        }
    }

    fun gettoken(){
        viewModelScope.launch {
            userToken = loadString.execute(PREFERENCE_STRING_KEY)
        }
    }


    private val _liveData: MutableLiveData<User> = MutableLiveData()
    val liveData: LiveData<User> = _liveData

    var user: User? = null
    var user2: FirebaseUser? = null



    fun getUserNCar(){
        getUser(userId!!)
        getCar(userId!!)
    }

    fun updateUserInfo(id: Long, name: String, address: String, nationalID: String){
        viewModelScope.launch {
            updateUserInfo.execute(id, name, address, nationalID).onSuccess {

            }.onFailure {
                Log.d("Responses Error", "${it.message}")
            }
        }

    }

    fun updateCarInfo(name: String, car_model: Int,
                      car_number: Int, car_color: String,
                      car_license: String, user_id: Long){
        viewModelScope.launch {
            updateCar.execute(name, car_model, car_number, car_color, car_license, user_id).onSuccess {

            }.onFailure {
                Log.d("Responses Error", "${it.message}")
            }
        }
    }

    fun getUser(id: Long) {
        viewModelScope.launch {
            getUser.execute(id).onSuccess {
                _liveData.value = it
                user = it
            }.onFailure {
                Log.d("Responses Error", "${it.message}")
                _exepection.value = it
                Log.d("Code from retrofit", "code failed: ${it}")
            }
        }
    }


    fun updateMobile(id: Long, mobile: Long) {
        viewModelScope.launch {
            updateMobile.execute(id, mobile).onSuccess {

            }.onFailure {
                Log.d("Responses Error", "${it.message}")
            }
        }
    }

    fun updateEmail(id: Long, email: String) {
        viewModelScope.launch {
            updateEmail.execute(id, email).onSuccess {

            }.onFailure {
                Log.d("Responses Error", "${it.message}")
            }
        }
    }


    fun updatePassword(id: Long, password: String) {
        viewModelScope.launch {
            updatePassword.execute(id, password).onSuccess {

            }.onFailure {
                Log.d("Responses Error", "${it.message}")
            }
        }
    }
}