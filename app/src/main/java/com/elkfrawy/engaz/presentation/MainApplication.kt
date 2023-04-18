package com.elkfrawy.engaz.presentation

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =  NotificationChannel(
                "LOCATION_CHANNEL_ID",
                "Location Channel",
                NotificationManager.IMPORTANCE_LOW
            )
            val channel2 =  NotificationChannel(
                "Problem_CHANNEL_ID",
                "Received Problem Channel",
                NotificationManager.IMPORTANCE_LOW
            )

            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
            manager.createNotificationChannel(channel2)
        }
    }

}