package com.elkfrawy.engaz.presentation.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.elkfrawy.engaz.R
import com.elkfrawy.engaz.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    val viewModel: HomeViewModel by viewModels()
    lateinit var controller: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val host = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        controller = host.navController

        viewModel.getToken()

        NavigationUI.setupWithNavController(binding.bottomNav, controller)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        controller.handleDeepLink(intent)
    }

}