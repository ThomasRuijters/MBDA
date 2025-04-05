package com.example.myapplication

import android.Manifest
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.app.ActivityCompat
import com.example.myapplication.domain.repository.StratagemRepository
import com.example.myapplication.domain.service.StratagemService
import com.example.myapplication.ui.theme.MyApplicationTheme
import android.content.Context
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.myapplication.utils.SettingsKeys
import com.example.myapplication.utils.settingsDataStore
import com.example.myapplication.views.StartScreen
import kotlinx.coroutines.flow.map


class MainActivity : ComponentActivity() {
    private lateinit var stratagemRepository: StratagemRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        requestNotificationPermission()

        Intent(this@MainActivity, StratagemService::class.java).also {
            it.action = StratagemService.StratagemServiceAction.START.name
            startService(it)
        }

        stratagemRepository = StratagemRepository(this)

        setContent {
            MyApplicationTheme {
                Navigation(
                    stratagemRepository = stratagemRepository,
                    settingsStore = applicationContext.settingsDataStore
                )
            }
        }
    }

    private fun requestNotificationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.POST_NOTIFICATIONS),
            100
        )
    }
}

@Composable
fun AppContent(
    dataStore: DataStore<Preferences>,
    inner: @Composable () -> Unit
) {
    val darkmode = dataStore.data.map {
        it[SettingsKeys.DARK_MODE] ?: false
    }.collectAsState(initial = false)

    MaterialTheme(
        colorScheme = if (darkmode.value) darkColorScheme() else lightColorScheme()
    ) {
        inner()
    }
}