package com.example.myapplication.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.myapplication.utils.SettingsKeys
import kotlinx.coroutines.flow.map

private val DarkColorScheme = darkColorScheme(
    background = Color(0xFF1C1C1C),
    primary = Color(0xFFFFEB3B),
    secondary = Color(0xFF232323),
    onPrimary = Color.Black,
    onSecondary = Color.White,
    tertiary = Color(0xFFD32F2F),
)

private val LightColorScheme = lightColorScheme(
    background = Color(0xFFE0E0E0),
    primary = Color(0xFF3B3B3B),
    secondary = Color.White,
    onPrimary = Color(0xFFFFEB3B),
    onSecondary = Color.Black,
    tertiary = Color(0xFFD32F2F),
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colors = if (!darkTheme) {
        LightColorScheme
    } else {
        DarkColorScheme
    }

    MaterialTheme(
        colorScheme = colors,
        content = content
    )
}

@Composable
fun AppContent(
    dataStore: DataStore<Preferences>,
    inner: @Composable () -> Unit
) {
    val darkMode = dataStore.data
        .map { it[SettingsKeys.DARK_MODE] ?: false }
        .collectAsState(initial = false)

    MaterialTheme(
        colorScheme = if (darkMode.value) DarkColorScheme else LightColorScheme,
        typography = Typography,
        content = inner
    )
}