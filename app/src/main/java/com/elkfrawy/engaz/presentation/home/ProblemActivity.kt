package com.elkfrawy.engaz.presentation.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.elkfrawy.engaz.databinding.ActivityProblemBinding
import com.elkfrawy.engaz.domain.model.Data
import com.elkfrawy.engaz.domain.model.Notification
import com.elkfrawy.engaz.domain.model.NotificationResponse
import com.elkfrawy.engaz.presentation.util.hide
import com.elkfrawy.engaz.presentation.util.show
import com.google.android.gms.location.*
import com.google.firebase.database.DatabaseReference
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject


@AndroidEntryPoint
class ProblemActivity : AppCompatActivity() {
    @Inject
    lateinit var databaseReference2: DatabaseReference
    val viewModel: HomeViewModel by viewModels()
    lateinit var lm:LocationManager

    @Inject
    lateinit var fused:FusedLocationProviderClient

    lateinit var location: Location

    lateinit var binding: ActivityProblemBinding
    @SuppressLint("MissingPermission")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProblemBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showLoading(false)
        //lm = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        val request = LocationRequest.Builder(0)
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            .build()

        val locationCallback = object : LocationCallback(){
            override fun onLocationResult(p0: LocationResult) {
                super.onLocationResult(p0)
                p0.locations.lastOrNull()?.let {
                    Log.d("Location Manager Check", "onLocationResult: Called")
                    location = it
                    fused.removeLocationUpdates(this)
                }
                //location = p0.lastLocation!!
            }
        }

        fused.requestLocationUpdates(request, locationCallback, Looper.getMainLooper())

/*        fused.lastLocation.addOnSuccessListener {
            location = it
        }*/

         viewModel.notificationState.observe(this){
             if (it){
                 finish()
                 showLoading(false)
                 Toast.makeText(this, "Sent Successfully", Toast.LENGTH_LONG).show()
             }else{
                 showLoading(false)
                 Toast.makeText(this, "Error, Try Again ", Toast.LENGTH_LONG).show()
             }
         }

        //location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER)!!
        binding.problemToolbar.setNavigationOnClickListener { finish() }
        viewModel.getToken()
        binding.reliefNumber.setOnClickListener {
            val i = Intent(Intent.ACTION_DIAL)
            i.data = Uri.parse("tel:01221110000")
            startActivity(i)
        }

        binding.submitProblem.setOnClickListener {

            val current = LocalDateTime.now()

            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
            val formatted = current.format(formatter)


            val title = binding.problemTitle.text.toString()
            val message = binding.problemDesc.text.toString()
            val type = binding.spType.selectedItem.toString()

            if (title.isEmpty() && message.isEmpty())
                Toast.makeText(this, "Fill Empty Field", Toast.LENGTH_LONG).show()
            else{
                showLoading(true)
                FirebaseMessaging.getInstance().unsubscribeFromTopic("Receive")
                val data = databaseReference2.child("Problem").child(viewModel.token).child("class")
                data.child("title").setValue(title)
                data.child("description").setValue(message)
                data.child("problem_type").setValue(type)
                data.child("latitude").setValue("${location.latitude}")
                data.child("longitude").setValue("${location.longitude}")
                data.child("date").setValue(formatted)

                val notification = NotificationResponse("/topics/Receive", Data(title, type, viewModel.token))
                viewModel.sendNotification(notification)

            }
        }
    }

    fun showLoading(boolean: Boolean){
        if (boolean){
            binding.progressBackground.show()
            binding.problemProgress.show()
        }else{
            binding.progressBackground.hide()
            binding.problemProgress.hide()
        }
    }
}