package com.elkfrawy.engaz.presentation.splashscreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.datastore.core.DataStore
import com.elkfrawy.engaz.R
import com.elkfrawy.engaz.presentation.PREFERENCE_INTEGER_KEY
import com.elkfrawy.engaz.presentation.PREFERENCE_STRING_KEY
import com.elkfrawy.engaz.presentation.auth.AuthActivity
import com.elkfrawy.engaz.presentation.auth.AuthFragment
import com.elkfrawy.engaz.presentation.home.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*

@AndroidEntryPoint
class SplashScreenActivity : AppCompatActivity() {

    val viewModel: SplashScreenViewModel by viewModels()
    val scope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        scope.launch {

            delay(2500)
            val token = viewModel.getToken(PREFERENCE_STRING_KEY)
            Log.d("User Token", "$token")
            if (token.length > 0){
                val i = Intent(this@SplashScreenActivity,  MainActivity::class.java)
                startActivity(i)
            }else{
                val i = Intent(this@SplashScreenActivity,  AuthActivity::class.java)
                startActivity(i)
            }
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }
}