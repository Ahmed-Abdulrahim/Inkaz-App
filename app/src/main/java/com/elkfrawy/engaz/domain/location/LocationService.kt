package com.elkfrawy.engaz.domain.location

import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.elkfrawy.engaz.R
import com.elkfrawy.engaz.domain.usecases.datastoreUsecases.LoadInt
import com.elkfrawy.engaz.domain.usecases.datastoreUsecases.LoadString
import com.elkfrawy.engaz.domain.usecases.user.UpdateLocation
import com.elkfrawy.engaz.domain.util.Notification_ID
import com.elkfrawy.engaz.presentation.PREFERENCE_INTEGER_KEY
import com.elkfrawy.engaz.presentation.PREFERENCE_STRING_KEY
import com.elkfrawy.engaz.presentation.home.MainActivity
import com.google.android.gms.location.LocationServices
import com.google.firebase.database.DatabaseReference
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import kotlin.properties.Delegates

@AndroidEntryPoint
class LocationService : Service() {

    @Inject lateinit var client: LocationClient
    @Inject lateinit var loadInt: LoadString
    @Inject lateinit var databaseReference: DatabaseReference
    @Inject lateinit var updateLocation: UpdateLocation

    var userId: String? = ""

    val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())


    override fun onCreate() {
        super.onCreate()
        scope.launch {
            userId = loadInt.execute(PREFERENCE_STRING_KEY)
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        when(intent?.getStringExtra("LOCATION_KEY")){
            ACTION_START -> start()
            ACTION_STOP -> stop()
        }

        return super.onStartCommand(intent, flags, startId)
    }

    private fun start(){
        FirebaseMessaging.getInstance().subscribeToTopic("Receive")
        val i = Intent(this, MainActivity::class.java)
        val pi = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_IMMUTABLE and PendingIntent.FLAG_ONE_SHOT)

        val notification = NotificationCompat.Builder(this, "LOCATION_CHANNEL_ID")
            .setContentTitle("Tracking Location.....")
            .setContentText("latitude:0.000  |  longitude:0.000")
            .setSmallIcon(R.mipmap.ic_launcher_foreground)
            .setOngoing(true)
            .setContentIntent(pi)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        client.getLocationUpdates(5000)
            .catch {  }
            .onEach {
                val lat = it.latitude
                val long = it.longitude

                val map = mutableMapOf<String, Any>()
                map.put("latitude", lat.toString())
                map.put("longitude", long.toString())
                databaseReference.child("Users").child(userId!!).child("class").updateChildren(map)
                //updateLocation.execute(userId!!, lat.toString(), long.toString())
                val mNotification = notification.setContentText("latitude:${lat}  |  longitude:${long}")
                notificationManager.notify(Notification_ID, mNotification.build())
            }.launchIn(scope)

        startForeground(Notification_ID, notification.build())
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun stop(){
        FirebaseMessaging.getInstance().unsubscribeFromTopic("Receive")
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }

    companion object{
        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP = "ACTION_STOP"
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

}