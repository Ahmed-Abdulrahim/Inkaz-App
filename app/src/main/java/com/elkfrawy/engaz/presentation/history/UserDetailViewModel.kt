package com.elkfrawy.engaz.presentation.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elkfrawy.engaz.domain.model.Rate
import com.elkfrawy.engaz.domain.usecases.history.addRate
import com.elkfrawy.engaz.domain.usecases.rate.GetRate
import com.elkfrawy.engaz.domain.util.onFailure
import com.elkfrawy.engaz.domain.util.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserDetailViewModel @Inject constructor(
    val addRate: addRate,
    val getRate: GetRate
) : ViewModel() {

    private val _rate = MutableLiveData<List<Rate>>()
    val rateViewModel:LiveData<List<Rate>> = _rate

    fun addRate(user_id: Long, user_id2: Long, rating: Int, message: String) {
        viewModelScope.launch {
            addRate.execute(user_id, user_id2, rating, message).onSuccess {

            }.onFailure {

            }
        }
    }

    fun getRate(user_id: Long){
        viewModelScope.launch {
            getRate.execute(user_id).onSuccess {
                _rate.value = it
            }.onFailure {

            }
        }
    }

}