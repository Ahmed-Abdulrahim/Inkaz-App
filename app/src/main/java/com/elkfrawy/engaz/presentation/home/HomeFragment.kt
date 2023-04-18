package com.elkfrawy.engaz.presentation.home

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.elkfrawy.engaz.R
import com.elkfrawy.engaz.databinding.FragmentHomeBinding
import com.elkfrawy.engaz.domain.location.LocationService
import com.elkfrawy.engaz.domain.model.Problem
import com.elkfrawy.engaz.presentation.PREFERENCE_BOOLEAN_KEY
import com.elkfrawy.engaz.presentation.home.received.ReceivedActivity
import com.elkfrawy.engaz.presentation.permissionGranted
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    val viewModel: HomeViewModel by viewModels()
    lateinit var binding: FragmentHomeBinding
    @Inject
    @ApplicationContext
    lateinit var applicationContext: Context
    lateinit var activityResult: ActivityResultLauncher<Array<String>>
    lateinit var intent: Intent
    @Inject
    lateinit var fused: FusedLocationProviderClient
    @Inject
    lateinit var databaseReference: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        viewModel.loadSwitchState(PREFERENCE_BOOLEAN_KEY)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        intent = Intent(applicationContext, LocationService::class.java)
        updateViews(viewModel.switchState)
        viewModel.getToken()


        binding.fabAskHelp.setOnClickListener {
            if (requireActivity().permissionGranted()){
                if (isGpsEnabled()) {
                    val i = Intent(requireActivity(), ProblemActivity::class.java)
                    startActivity(i)
                }else{
                    Log.d("Location Manager Check", "GPS Disabled")
                }
            }else{
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    activityResult.launch(
                        arrayOf(
                            android.Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION,
                            android.Manifest.permission.ACCESS_FINE_LOCATION
                        )
                    )
                } else {
                    activityResult.launch(
                        arrayOf(
                            android.Manifest.permission.ACCESS_COARSE_LOCATION,
                            android.Manifest.permission.ACCESS_FINE_LOCATION
                        )
                    )
                }
            }
        }

        databaseReference.child("Problem")
            .addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    if (snapshot.exists()) {
                        if (!snapshot.key.equals(viewModel.token)) {
                            viewModel.problemList.clear()
                            if (!snapshot.key.equals(viewModel.token)) {
                                val problem = snapshot.child("class").getValue(Problem::class.java)
                                viewModel.problemList.add(problem!!)
                                binding.tvShowPeople.text = "${viewModel.problemList.size}"
                            }
                        }
                    }
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    if (snapshot.exists()) {
                        viewModel.problemList.clear()
                        if (!snapshot.key.equals(viewModel.token)) {
                            val problem = snapshot.child("class").getValue(Problem::class.java)
                            viewModel.problemList.add(problem!!)
                            binding.tvShowPeople.text = "${viewModel.problemList.size}"
                        }
                    }
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    viewModel.problemList.clear()
                    if (!snapshot.key.equals(viewModel.token)) {
                        val problem = snapshot.child("class").getValue(Problem::class.java)
                        viewModel.problemList.add(problem!!)
                        binding.tvShowPeople.text = "${viewModel.problemList.size}"
                    }
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })

        activityResult =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {

                if (it.values.contains(false)) {
                    binding.locationSwitcher.isChecked = false
                    viewModel.switchState = false
                    Toast.makeText(
                        requireContext(),
                        "Please allow Permissions first",
                        Toast.LENGTH_LONG
                    ).show()
                }

            }

        binding.btnReceivedActivity.setOnClickListener {
            val i = Intent(requireActivity(), ReceivedActivity::class.java)
            startActivity(i)
        }

        binding.locationSwitcher.setOnCheckedChangeListener { buttonView, isChecked ->

            if (isChecked) {
                if (requireActivity().permissionGranted()) {
                    if (isGpsEnabled()) {
                        viewModel.switchState = isChecked
                        startService()
                    } else {
                        viewModel.switchState = false
                        binding.locationSwitcher.isChecked = false
                    }

                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        activityResult.launch(
                            arrayOf(
                                android.Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                                android.Manifest.permission.ACCESS_FINE_LOCATION
                            )
                        )
                    } else {
                        activityResult.launch(
                            arrayOf(
                                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                                android.Manifest.permission.ACCESS_FINE_LOCATION
                            )
                        )
                    }
                }
            } else {
                stopService()
                viewModel.switchState = isChecked
            }
        }
    }

    private fun isGpsEnabled(): Boolean {
        Log.d("Location Manager Check", "isGpsEnabled: Checking if gps enabled or not")
        val lm = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            if (!lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
                val builder = AlertDialog.Builder(requireContext())
                    .setTitle("Location Services Not Active")
                    .setMessage("Please enable Location Services and GPS")
                    .setPositiveButton(
                        "OK"
                    ) { dialog, which ->
                        val i = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(i)
                    }
                val dialog = builder.create()
                dialog.setCanceledOnTouchOutside(false)
                dialog.show()
            }else
                return true
        }else{
            if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER) || !lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                val builder = AlertDialog.Builder(requireContext())
                    .setTitle("Location Services Not Active")
                    .setMessage("Please enable Location Services and GPS")
                    .setPositiveButton(
                        "OK"
                    ) { dialog, which ->
                        val i = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(i)
                    }
                val dialog = builder.create()
                dialog.setCanceledOnTouchOutside(false)
                dialog.show()
            } else
                return true
        }
        return false
    }

    override fun onStart() {
        super.onStart()
        viewModel.loadSwitchState(PREFERENCE_BOOLEAN_KEY)
        Log.d("Testing Un/Subscribe", "onStart checker: ${viewModel.switchState}:")
        if (viewModel.switchState) {
            Log.d("Testing Un/Subscribe", "Location enabled in home fragment")
            FirebaseMessaging.getInstance().subscribeToTopic("Receive")
        } else {
            Log.d("Testing Un/Subscribe", "Location is disabled in home fragment")
            FirebaseMessaging.getInstance().unsubscribeFromTopic("Receive")
        }
    }

    fun updateViews(boolean: Boolean) {
        binding.locationSwitcher.isChecked = boolean
        val text = if (boolean) "enabled" else "disabled"
        binding.tvLocationTracker.text = "Location tracker is $text"
    }

    private fun startService() {
        intent.putExtra("LOCATION_KEY", LocationService.ACTION_START)
        requireActivity().startService(intent)
        binding.tvLocationTracker.text = "Location tracker is enabled"
    }

    private fun stopService() {
        intent.putExtra("LOCATION_KEY", LocationService.ACTION_STOP)
        requireActivity().startService(intent)
        binding.tvLocationTracker.text = "Location tracker is disabled"
    }

    override fun onStop() {
        super.onStop()
        viewModel.saveSwitchState(PREFERENCE_BOOLEAN_KEY, binding.locationSwitcher.isChecked)
    }


}