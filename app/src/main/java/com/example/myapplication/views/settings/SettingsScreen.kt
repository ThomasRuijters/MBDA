package com.example.myapplication.views.settings

import android.content.res.Configuration
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.R

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = viewModel(),
    updateTopBar: (String, @Composable () -> Unit) -> Unit
) {
    updateTopBar("Settings", {})

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

    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SetProfilePicture(viewModel)
        }

        Column(
            modifier = Modifier
                .weight(3f)
                .verticalScroll(rememberScrollState())
        ) {
            Column(
                modifier = Modifier.padding(bottom = 24.dp)
            ) {
                Text(
                    stringResource(R.string.settings_group_userinfo),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                OutlinedTextField(
                    value = userName,
                    onValueChange = { viewModel.updateUserName(it) },
                    label = { Text(stringResource(R.string.settings_group_userinfo_user_name)) },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Column(
                modifier = Modifier.padding(bottom = 24.dp)
            ) {
                Text(
                    stringResource(R.string.settings_group_appearance),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(stringResource(R.string.settings_group_appearance_dark_mode))
                    Switch(
                        checked = darkMode,
                        onCheckedChange = { viewModel.updateDarkMode(it) },
                        modifier = Modifier.padding(start = 12.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun VerticalSettingsScreen(viewModel: SettingsViewModel) {

    val darkMode by viewModel.darkModeFlow.collectAsState()
    val userName by viewModel.userNameFlow.collectAsState()

    Column(
        modifier = Modifier
            .padding(24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        SetProfilePicture(viewModel)

        Spacer(Modifier.height(24.dp))

        Column(modifier = Modifier.padding(bottom = 24.dp)) {
            Text(
                stringResource(R.string.settings_group_userinfo),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            OutlinedTextField(
                value = userName,
                onValueChange = { viewModel.updateUserName(it) },
                label = { Text(stringResource(R.string.settings_group_userinfo_user_name)) },
                modifier = Modifier.fillMaxWidth()
            )
        }

        Column(modifier = Modifier.padding(bottom = 24.dp)) {
            Text(
                stringResource(R.string.settings_group_appearance),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(stringResource(R.string.settings_group_appearance_dark_mode))
                Switch(
                    checked = darkMode,
                    onCheckedChange = { viewModel.updateDarkMode(it) },
                    modifier = Modifier.padding(start = 12.dp)
                )
            }
        }
    }
}

@Composable
fun SetProfilePicture(viewModel: SettingsViewModel) {
    val profilePicture by viewModel.profilePictureUriFlow.collectAsState()

    val pickImageLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let { viewModel.saveProfileImageFromUri(it) }
    }
    ProfilePicture(profilePicture)

    Button(onClick = { pickImageLauncher.launch("image/*") }) {
        Text(stringResource(R.string.settings_select_profile_picture_button))
    }
}

@Composable
@Preview(showBackground = true)
fun SettingsScreenPreview() {
    MaterialTheme {
        SettingsScreen(
            viewModel = viewModel(),
            updateTopBar = { title, actions ->

            }
        )
    }
}