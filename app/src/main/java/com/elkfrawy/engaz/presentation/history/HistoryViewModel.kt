package com.elkfrawy.engaz.presentation.history

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elkfrawy.engaz.domain.model.FirebaseHistory
import com.elkfrawy.engaz.domain.model.FirebaseUser
import com.elkfrawy.engaz.domain.model.History
import com.elkfrawy.engaz.domain.model.Rate
import com.elkfrawy.engaz.domain.usecases.datastoreUsecases.LoadInt
import com.elkfrawy.engaz.domain.usecases.datastoreUsecases.LoadString
import com.elkfrawy.engaz.domain.usecases.history.GetHistory
import com.elkfrawy.engaz.domain.util.onFailure
import com.elkfrawy.engaz.domain.util.onSuccess
import com.elkfrawy.engaz.presentation.PREFERENCE_INTEGER_KEY
import com.elkfrawy.engaz.presentation.PREFERENCE_STRING_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val getHistory: GetHistory,
    private val loadInt: LoadInt,
    private val loadString: LoadString,
    ) : ViewModel() {

    private val _history_liveData = MutableLiveData<List<History>>()
    val historyLiveData: LiveData<List<History>> = _history_liveData
    private val _exepection: MutableLiveData<Throwable> = MutableLiveData()
    val exepection:LiveData<Throwable> = _exepection
    lateinit var token: String
    val historyItem = ArrayList<FirebaseHistory>()
    lateinit var otherToken: String
    lateinit var item: FirebaseHistory
    lateinit var user: FirebaseUser
    lateinit var rate: Rate
    lateinit var rateKey: String

    init {
        getToken()
    }

    fun getToken(){
        viewModelScope.launch {
            token = loadString.execute(PREFERENCE_STRING_KEY)
        }
    }

    fun getHistory() {
        viewModelScope.launch {
            val userId = loadInt.execute(PREFERENCE_INTEGER_KEY)
            getHistory.execute(userId).onSuccess {
                _history_liveData.value = it
            }.onFailure {
                _exepection.value = it
                Log.d("Retrofit Call Error", "${it.message}")
            }
        }
    }


}