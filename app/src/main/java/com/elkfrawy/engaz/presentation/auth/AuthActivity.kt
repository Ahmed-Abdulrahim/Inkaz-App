package com.elkfrawy.engaz.presentation.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.elkfrawy.engaz.R
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)


    }
}