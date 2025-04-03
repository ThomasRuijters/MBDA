package com.example.myapplication.domain.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.myapplication.R

class StratagemService : Service() {
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var notificationManager: NotificationManagerCompat

    private var imageIndex: Int = 0
    private val sharedPreferences: SharedPreferences by lazy {
        getSharedPreferences("stratagem_preferences", MODE_PRIVATE)
    }

    private val images = listOf(
        R.drawable.eagle_500kg_bomb,
        R.drawable.anti_personnel_minefield,
        R.drawable.orbital_laser
    )

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            Actions.START.toString() -> startUpdatingNotifications()
            Actions.STOP.toString() -> stopSelf()
        }
        return START_STICKY
    }

    private fun startUpdatingNotifications() {
        notificationManager = NotificationManagerCompat.from(this)

        imageIndex = sharedPreferences.getInt("active_stratagem_index", 0)
        startForeground(1, createNotification(images[imageIndex]))

        handler.post(updateNotificationRunnable)
    }

    private val updateNotificationRunnable = object : Runnable {
        override fun run() {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
                ContextCompat.checkSelfPermission(this@StratagemService, android.Manifest.permission.POST_NOTIFICATIONS)
                == PackageManager.PERMISSION_GRANTED
            ) {
                imageIndex = (imageIndex + 1) % images.size
                sharedPreferences.edit().putInt("active_stratagem_index", imageIndex).apply()

                notificationManager.notify(1, createNotification(images[imageIndex]))

                handler.postDelayed(this, 30_000)
            }
        }
    }

    private fun createNotification(imageRes: Int): android.app.Notification {
        val bitmap = getBitmapFromVectorDrawable(this, imageRes)

        return NotificationCompat.Builder(this, "stratagem_notifications")
            .setSmallIcon(R.drawable.helldivers_2__icon_)
            .setContentTitle("Active stratagem")
            .setContentText("Current active stratagem:")
            .setLargeIcon(bitmap)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(false)
            .setOngoing(true)
            .build()
    }

    private fun getBitmapFromVectorDrawable(context: Context, drawableId: Int): Bitmap {
        val drawable = ContextCompat.getDrawable(context, drawableId)
            ?: return Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
        val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)

        return bitmap
    }

    override fun onDestroy() {
        handler.removeCallbacks(updateNotificationRunnable)
        super.onDestroy()
    }

    enum class Actions {
        START, STOP
    }
}