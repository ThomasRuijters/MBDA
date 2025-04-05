package com.example.myapplication.views.settings

import android.app.Application
import android.net.Uri
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.domain.model.AppSettings
import com.example.myapplication.utils.SettingsKeys
import com.example.myapplication.utils.settingsDataStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val dataStore = application.settingsDataStore

    // Use MutableStateFlow to manually manage state
    private val _darkModeFlow = MutableStateFlow(AppSettings.DEFAULT.darkMode)
    val darkModeFlow: StateFlow<Boolean> = _darkModeFlow

    private val _userNameFlow = MutableStateFlow(AppSettings.DEFAULT.userName)
    val userNameFlow: StateFlow<String> = _userNameFlow

    private val _profilePictureUriFlow = MutableStateFlow(AppSettings.DEFAULT.profilePictureUri)
    val profilePictureUriFlow: StateFlow<String> = _profilePictureUriFlow

    init {
        // Collecting data from DataStore and updating StateFlow manually
        viewModelScope.launch {
            dataStore.data
                .map { prefs ->
                    prefs[SettingsKeys.USER_NAME] ?: AppSettings.DEFAULT.userName
                }
                .distinctUntilChanged()
                .collect { name ->
                    _userNameFlow.value = name
                }
        }

        viewModelScope.launch {
            dataStore.data
                .map { prefs ->
                    prefs[SettingsKeys.DARK_MODE] ?: AppSettings.DEFAULT.darkMode
                }
                .distinctUntilChanged()
                .collect { darkMode ->
                    _darkModeFlow.value = darkMode
                }
        }

        viewModelScope.launch {
            dataStore.data
                .map { prefs ->
                    prefs[SettingsKeys.PROFILE_PICTURE_PATH]
                        ?: AppSettings.DEFAULT.profilePictureUri
                }
                .distinctUntilChanged()
                .collect { uri ->
                    _profilePictureUriFlow.value = uri
                }
        }
    }

    fun updateDarkMode(enabled: Boolean) {
        viewModelScope.launch {
            dataStore.edit { prefs ->
                prefs[SettingsKeys.DARK_MODE] = enabled
            }
        }
    }

    fun updateUserName(name: String) {
        viewModelScope.launch {
            dataStore.edit { prefs ->
                prefs[SettingsKeys.USER_NAME] = name
            }
        }
    }

    fun updateProfileImageUri(uri: String) {
        viewModelScope.launch {
            dataStore.edit { prefs ->
                prefs[SettingsKeys.PROFILE_PICTURE_PATH] = uri
            }
        }
    }
}