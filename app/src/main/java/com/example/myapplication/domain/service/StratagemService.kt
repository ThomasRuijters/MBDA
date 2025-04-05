package com.example.myapplication.domain.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.glance.appwidget.updateAll
import com.example.myapplication.R
import com.example.myapplication.domain.model.Stratagem
import com.example.myapplication.persistence.StratagemFileStore
import com.example.myapplication.utils.BitmapUtils
import com.example.myapplication.widget.StratagemWidget
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class StratagemService : Service() {

    private lateinit var stratagemFileStore: StratagemFileStore
    private lateinit var stratagems: List<Stratagem>

    private var isRunning: Boolean = false

    override fun onCreate() {
        super.onCreate()

        stratagemFileStore = StratagemFileStore(this)
        tryLoadStratagems()
    }

    private fun tryLoadStratagems() {
        stratagems = try {
            stratagemFileStore.loadStratagems()
        } catch (e: Exception) {
            emptyList<Stratagem>()
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            StratagemServiceAction.START.name -> start()
            StratagemServiceAction.STOP.name -> stop()
        }

        return super.onStartCommand(intent, flags, startId)
    }

    private fun start() {
        isRunning = true
        notification(R.drawable.hellbomb)

        CoroutineScope(Dispatchers.Default).launch {
            stratagemFlow().collect { stratagemResourceId ->
                Log.d("StratagemFlow", "Collected resourceId: $stratagemResourceId")

                stratagemFileStore.saveResourceId(stratagemResourceId)

                withContext(Dispatchers.Main) {
                    StratagemWidget().updateAll(this@StratagemService)
                }

                notification(stratagemResourceId)
            }
        }
    }

    private fun stratagemFlow(): Flow<Int> = flow {
        while (isRunning) {
            if (stratagems.isNotEmpty()) {
                val stratagem = stratagems.random()
                val resourceId = BitmapUtils.getResourceId(this@StratagemService, stratagem.name)

                emit(resourceId)
            } else {
                tryLoadStratagems()
            }

            delay(30_000)
        }
    }

    private fun notification(stratagemResourceId: Int) {
        val stratagemNotification = NotificationCompat
            .Builder(this, "stratagem_channel")
            .setSmallIcon(R.drawable.helldivers_2__icon_)
            .setContentTitle("Active Stratagem")
            .setLargeIcon(BitmapUtils.getBitmapFromVectorDrawable(this, stratagemResourceId))
            .setStyle(NotificationCompat.BigPictureStyle())
            .build()

        startForeground(1, stratagemNotification)
    }

    private fun stop() {
        isRunning = false
        stopSelf()
    }

    enum class StratagemServiceAction {
        START, STOP
    }
}