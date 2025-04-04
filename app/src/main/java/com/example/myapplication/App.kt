package com.example.myapplication

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context

class App: Application() {

    override fun onCreate() {
        super.onCreate()

        val stratagemChannel = NotificationChannel(
            "stratagem_channel",
            "Stratagem Channel",
            NotificationManager.IMPORTANCE_LOW
        )

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(stratagemChannel)
    }
}