package com.elkfrawy.engaz.presentation.splashscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elkfrawy.engaz.domain.usecases.datastoreUsecases.LoadInt
import com.elkfrawy.engaz.domain.usecases.datastoreUsecases.LoadString
import com.elkfrawy.engaz.domain.usecases.datastoreUsecases.SaveString
import com.elkfrawy.engaz.presentation.PREFERENCE_STRING_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(val getString: LoadString, val saveString: SaveString): ViewModel() {

    suspend fun getToken(key: String): String = getString.execute(key)
    fun saveString(token: String){
        viewModelScope.launch {
            saveString.execute(PREFERENCE_STRING_KEY, token)
        }
    }


}