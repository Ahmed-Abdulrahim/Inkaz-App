package com.elkfrawy.engaz.domain.services

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.icu.text.CaseMap.Title
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.os.bundleOf
import androidx.navigation.NavDeepLinkBuilder
import com.elkfrawy.engaz.R
import com.elkfrawy.engaz.domain.util.Notification_ID2
import com.elkfrawy.engaz.presentation.home.MainActivity
import com.elkfrawy.engaz.presentation.home.received.ReceivedActivity
import com.google.android.gms.location.DetectedActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@AndroidEntryPoint
class MyFirebaseInstanceIDService : FirebaseMessagingService() {

    override fun onCreate() {
        super.onCreate()
        Log.d("Notification Error", "Notification onCreate")

    }

    override fun onMessageReceived(message: RemoteMessage) {
        Log.d("Notification Error", "Notification onMessageReceived Called")
        val data: Map<String, String> = message.data
        val title = data.get("title")?: "NULL"
        val type = data.get("type")?: "NULL"
        val userToken = data.get("user_token") ?: ""
        notify(title, type, userToken)

    }

    fun notify(title: String, message:String, userToken: String){

        val bundle = bundleOf()
        bundle.putString("token", userToken)
        val pendingIntent = NavDeepLinkBuilder(this)
            .setComponentName(ReceivedActivity::class.java)
            .setGraph(R.navigation.received_graph)
            .setDestination(R.id.receivedDetailsFragment)
            .setArguments(bundle)
            .createPendingIntent()

        Log.d("Notification Error", "Creating Pending Intent")

        val notification = NotificationCompat.Builder(this, "Problem_CHANNEL_ID")
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.car_accident)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_MAX).build()

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        Log.d("Notification Error", "Notification Notify")
        notificationManager.notify(Notification_ID2, notification)
    }


}