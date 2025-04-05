package com.example.myapplication.views.start

import android.app.Application
import android.location.Geocoder
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.domain.model.AppSettings
import com.example.myapplication.utils.SettingsKeys
import com.example.myapplication.utils.settingsDataStore
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.Locale
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

class StartViewModel(application: Application) : AndroidViewModel(application) {

    private val context = { getApplication<Application>().applicationContext }
    private val dataStore = context().settingsDataStore

    private val _username = MutableStateFlow(AppSettings.DEFAULT.userName)
    val username: StateFlow<String> = _username.asStateFlow()

    private val _townName = MutableStateFlow("Unknown")
    val townName: StateFlow<String> = _townName.asStateFlow()

    private val _permissionGranted = MutableStateFlow(false)

    init {
        observeSettings()
        checkPermissionAndResolveTown()
    }

    private fun observeSettings() {
        viewModelScope.launch {
            dataStore.data
                .map { prefs ->
                    prefs[SettingsKeys.USER_NAME] ?: AppSettings.DEFAULT.userName
                }
                .distinctUntilChanged()
                .collect { name ->
                    _username.value = name
                }
        }
    }

    private fun checkPermissionAndResolveTown() {
        val permission = Manifest.permission.ACCESS_FINE_LOCATION
        val permissionStatus = ContextCompat.checkSelfPermission(context(), permission)

        _permissionGranted.value = permissionStatus == PackageManager.PERMISSION_GRANTED

        if (_permissionGranted.value) {
            resolveTownName()
        } else {
            // Handle case where permission is not granted
            _townName.value = "somewhere"
        }
    }

    fun resolveTownName() {
        if (!_permissionGranted.value) {
            _townName.value = "somewhere"
            return
        }

        val fusedClient = LocationServices.getFusedLocationProviderClient(context())

        try {
            fusedClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    val geocoder = Geocoder(context(), Locale.getDefault())
                    viewModelScope.launch {
                        try {
                            val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                            val town = addresses?.firstOrNull()?.locality ?: "Unavailable"
                            _townName.value = town
                        } catch (e: IOException) {
                            _townName.value = "Geocoder failed"
                        }
                    }
                } else {
                    _townName.value = "Location unavailable"
                }
            }.addOnFailureListener {
                _townName.value = "Location error"
            }
        } catch (e: SecurityException) {
            _townName.value = "Permission denied"
        }
    }
}

