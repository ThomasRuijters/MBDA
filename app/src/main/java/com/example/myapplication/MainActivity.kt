package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.myapplication.domain.repository.StratagemRepository
import com.example.myapplication.domain.service.StratagemService
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.utils.PermissionHelper
import com.example.myapplication.utils.settingsDataStore

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