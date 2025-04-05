package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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

import com.example.myapplication.utils.PermissionHelper

class MainActivity : ComponentActivity() {

    private lateinit var stratagemRepository: StratagemRepository
    private lateinit var permissionHelper: PermissionHelper
    private lateinit var requestPermissionsLauncher: ActivityResultLauncher<Array<String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        stratagemRepository = StratagemRepository(this)
        setContent {
            MyApplicationTheme {
                Navigation(
                    stratagemRepository = stratagemRepository,
                    settingsStore = applicationContext.settingsDataStore
                )
            }
        }

        askForPermissions()
    }

    private fun askForPermissions() {
        permissionHelper = PermissionHelper(this)

        requestPermissionsLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            permissionHelper.handlePermissionResults(permissions, {
                startStratagemService()
            }, { permission ->
                when (permission) {
                    android.Manifest.permission.POST_NOTIFICATIONS -> {
                        Toast.makeText(
                            this,
                            "Notification permission denied. Cannot load active stratagems.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    android.Manifest.permission.ACCESS_FINE_LOCATION -> {
                        Toast.makeText(
                            this,
                            "Location permission denied. Cannot share your location to regroup.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            })
        }

        permissionHelper.requestPermissions(requestPermissionsLauncher, arrayOf(
            android.Manifest.permission.POST_NOTIFICATIONS,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ), {
            startStratagemService()
        }, { permission ->
            if (permission == android.Manifest.permission.POST_NOTIFICATIONS) {
                Toast.makeText(
                    this,
                    "Notification permission denied. Cannot load active stratagems.",
                    Toast.LENGTH_SHORT
                ).show()
            }
            if (permission == android.Manifest.permission.ACCESS_FINE_LOCATION) {
                Toast.makeText(
                    this,
                    "Location permission denied. Cannot share your location to regroup.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun startStratagemService() {
        Intent(this@MainActivity, StratagemService::class.java).also {
            it.action = StratagemService.StratagemServiceAction.START.name
            startService(it)
        }
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