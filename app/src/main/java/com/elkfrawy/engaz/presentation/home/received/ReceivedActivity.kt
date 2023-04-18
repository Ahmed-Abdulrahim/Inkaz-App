package com.elkfrawy.engaz.presentation.home.received

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.elkfrawy.engaz.R
import com.elkfrawy.engaz.databinding.ActivityReceivedBinding
import com.elkfrawy.engaz.presentation.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReceivedActivity : AppCompatActivity() {
    lateinit var binding: ActivityReceivedBinding
    val viewModel: HomeViewModel by viewModels()
    lateinit var controller: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReceivedBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val host = supportFragmentManager.findFragmentById(R.id.fragmentContainerView4) as NavHostFragment
        controller = host.navController
        viewModel.getToken()
    }
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        controller.handleDeepLink(intent)
    }
}