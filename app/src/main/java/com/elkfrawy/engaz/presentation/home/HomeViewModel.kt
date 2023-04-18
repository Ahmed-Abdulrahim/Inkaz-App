package com.elkfrawy.engaz.presentation.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elkfrawy.engaz.domain.model.NotificationResponse
import com.elkfrawy.engaz.domain.model.Problem
import com.elkfrawy.engaz.domain.usecases.SendNotification
import com.elkfrawy.engaz.domain.usecases.datastoreUsecases.*
import com.elkfrawy.engaz.domain.util.onFailure
import com.elkfrawy.engaz.domain.util.onSuccess
import com.elkfrawy.engaz.presentation.PREFERENCE_BOOLEAN_KEY
import com.elkfrawy.engaz.presentation.PREFERENCE_STRING_KEY
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val loadBoolean: LoadBoolean,
    private val saveBoolean: SaveBoolean,
    private val loadString: LoadString,
    private val sendNotification: SendNotification
) : ViewModel() {

    init {
        loadSwitchState(PREFERENCE_BOOLEAN_KEY)
    }

    var switchState = false
    lateinit var token:String
    var problemList = ArrayList<Problem>()
    var ids = ArrayList<String>()
    var problemToken: String = ""
    var problem: Problem? = null

    var locationState = false
    val _notification_state: MutableLiveData<Boolean> = MutableLiveData()
    val notificationState:LiveData<Boolean> = _notification_state

    fun saveSwitchState(key: String, boolean: Boolean) {
        viewModelScope.launch {
            saveBoolean.execute(key, boolean)
        }
    }

    fun getToken(){
        viewModelScope.launch{
            token = loadString.execute(PREFERENCE_STRING_KEY)
        }
    }

    fun sendNotification(notificationResponse: NotificationResponse){
        viewModelScope.launch {
            sendNotification.execute(notificationResponse).onSuccess {
                _notification_state.value = true
                Log.d("Testing Un/Subscribe", "onSuccess: $switchState")
                if (switchState)
                    FirebaseMessaging.getInstance().subscribeToTopic("Receive")
                else
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("Receive")
            }.onFailure {
                _notification_state.value = false
                Log.d("Testing Un/Subscribe", "onFailure: $switchState")
                if (switchState)
                    FirebaseMessaging.getInstance().subscribeToTopic("Receive")
                else
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("Receive")
                Log.d("Notification Error", "Error: ${it.message}")
            }
        }
    }

    fun loadSwitchState(key: String) {
        viewModelScope.launch {
            switchState = loadBoolean.execute(key)
        }
    }


}