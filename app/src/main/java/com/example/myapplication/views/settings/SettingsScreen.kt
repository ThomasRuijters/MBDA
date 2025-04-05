package com.example.myapplication.views.settings

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.example.myapplication.utils.SettingsKeys
import com.example.myapplication.utils.settingsDataStore
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(
    context: Context = LocalContext.current,
    dataStore: DataStore<Preferences> = context.settingsDataStore
) {
    val scope = rememberCoroutineScope()

    val settings = dataStore.data.map { prefs ->
        val darkMode = prefs[SettingsKeys.DARK_MODE] ?: false
        val message = prefs[SettingsKeys.WELCOME_MESSAGE] ?: "Hello!"
        darkMode to message
    }.collectAsState(initial = false to "Hello!")

    val (darkMode, welcomeMessage) = settings.value
    var textFieldValue by remember { mutableStateOf(welcomeMessage) }

    Column(modifier = Modifier.padding(24.dp)) {
        Text("Settings", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(24.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Dark mode?")
            Switch(
                checked = darkMode,
                onCheckedChange = { newValue ->
                    scope.launch {
                        dataStore.edit { it[SettingsKeys.DARK_MODE] = newValue }
                    }
                },
                modifier = Modifier.padding(start = 12.dp)
            )
        }

        Spacer(Modifier.height(24.dp))

        OutlinedTextField(
            value = textFieldValue,
            onValueChange = { newValue ->
                textFieldValue = newValue
                scope.launch {
                    dataStore.edit { prefs ->
                        prefs[SettingsKeys.WELCOME_MESSAGE] = newValue
                    }
                }
            },
            label = { Text("Welcome Message") }
        )
    }
}

@Composable
@Preview(showBackground = true)
fun SettingsScreenPreview() {

}