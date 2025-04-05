package com.example.myapplication

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.myapplication.domain.repository.StratagemRepository
import com.example.myapplication.domain.service.StratagemService
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {

    private lateinit var stratagemRepository: StratagemRepository
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                startStratagemService()
            } else {
                Toast.makeText(
                    this,
                    "Permission denied. Cannot load active stratagems.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        maybeRequestNotificationPermission()

        stratagemRepository = StratagemRepository(this)
        setContent {
            MyApplicationTheme {
                Navigation(stratagemRepository = stratagemRepository)
            }
        }
    }

    private fun startStratagemService() {
        Intent(this@MainActivity, StratagemService::class.java).also {
            it.action = StratagemService.StratagemServiceAction.START.name
            startService(it)
        }
    }

    private fun maybeRequestNotificationPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED -> {
                startStratagemService()
            }
            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) -> {
                AlertDialog.Builder(this)
                    .setIcon(R.drawable.sos_beacon)
                    .setTitle("Permission Required for active stratagems")
                    .setMessage("In order to use this feature, we need permission to send you notifications in order to create a notification with the active stratagem.")
                    .setPositiveButton("Allow") { _, _ ->
                        requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    }
                    .setNegativeButton("Deny") { dialog, _ ->
                        dialog.dismiss()
                        Toast.makeText(
                            this,
                            "Permission is required for active stratagems.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    .setCancelable(false)
                    .show()
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}