package com.example.myapplication.views.settings

import android.content.Context
import android.content.res.Configuration
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = viewModel()
) {
    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE
    if (isLandscape) {
        HorizontalSettingsScreen(viewModel)
    } else {
        VerticalSettingsScreen(viewModel)
    }
}

@Composable
fun HorizontalSettingsScreen(viewModel: SettingsViewModel) {

    val darkMode by viewModel.darkModeFlow.collectAsState()
    val userName by viewModel.userNameFlow.collectAsState()

    Column(modifier = Modifier.padding(50.dp)) {
        Text("Settings", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(24.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Dark mode?")
            Switch(
                checked = darkMode,
                onCheckedChange = { viewModel.updateDarkMode(it) },
                modifier = Modifier.padding(start = 12.dp)
            )
        }

        Spacer(Modifier.height(24.dp))

        OutlinedTextField(
            value = userName,
            onValueChange = {
                viewModel.updateUserName(it)
            },
            label = { Text("Name") }
        )
    }
}

@Composable
fun VerticalSettingsScreen(viewModel: SettingsViewModel) {

    val darkMode by viewModel.darkModeFlow.collectAsState()
    val userName by viewModel.userNameFlow.collectAsState()
    val profilePicture by viewModel.profilePictureUriFlow.collectAsState()

    Column(modifier = Modifier.padding(24.dp)) {
        Text("Settings", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(24.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Dark mode?")
            Switch(
                checked = darkMode,
                onCheckedChange = { viewModel.updateDarkMode(it) },
                modifier = Modifier.padding(start = 12.dp)
            )
        }

        Spacer(Modifier.height(24.dp)) // This could come from DataStore or SharedPreferences

        val context = LocalContext.current
        val pickImageLauncher =
            rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
                uri?.let { imageUri ->
                    val inputStream = context.contentResolver.openInputStream(uri)
                    inputStream?.use { stream ->
                        // Convert the InputStream into a Bitmap
                        MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
                    }

                    // Handle the selected image URI (e.g., save to internal storage, or display it)
                    viewModel.updateProfileImageUri(imageUri.toString())
                }
            }
        Text("Profile Picture: ${profilePicture ?: "No image selected"}")

        Button(onClick = { pickImageLauncher.launch("image/*") }) {
            Text("Select Profile Picture")
        }

        OutlinedTextField(
            value = userName,
            onValueChange = {
                viewModel.updateUserName(it)
            },
            label = { Text("Name") }
        )
    }
}

@Composable
@Preview(showBackground = true)
fun SettingsScreenPreview() {
    MaterialTheme {
        SettingsScreen()
    }
}