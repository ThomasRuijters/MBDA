package com.example.myapplication.utils

import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat

class PermissionHelper(
    private val context: Context,
) {

    fun requestPermissions(
        permissionsLauncher: ActivityResultLauncher<Array<String>>,
        permissions: Array<String>,
        grantedAction: () -> Unit,
        deniedAction: (String) -> Unit
    ) {
        val permissionsToRequest = permissions.filter {
            ContextCompat.checkSelfPermission(context, it) != PackageManager.PERMISSION_GRANTED
        }

        if (permissionsToRequest.isEmpty()) {
            grantedAction()
        } else {
            permissionsLauncher.launch(permissionsToRequest.toTypedArray())
        }
    }

    fun handlePermissionResults(
        permissions: Map<String, Boolean>,
        grantedAction: () -> Unit,
        deniedAction: (String) -> Unit
    ) {
        permissions.forEach { (permission, isGranted) ->
            if (isGranted) {
                grantedAction()
            } else {
                deniedAction(permission)
            }
        }
    }
}
