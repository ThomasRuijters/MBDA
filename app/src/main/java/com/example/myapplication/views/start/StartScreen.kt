package com.example.myapplication.views.start

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.R
import com.example.myapplication.ui.theme.MyApplicationTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import android.content.res.Configuration
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import com.example.myapplication.views.settings.VerticalSettingsScreen

@Composable
fun StartScreen(
    modifier: Modifier = Modifier,
    viewModel: StartViewModel = viewModel(),
    onNext: () -> Unit,
    imageResource: Int = R.drawable.helldivers_2,
) {
    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE
    if (isLandscape) {
        HorizontalStartScreen(modifier, viewModel, onNext, imageResource)
    } else {
        VerticalStartScreen(modifier, viewModel, onNext, imageResource)
    }
}

@Composable
fun HorizontalStartScreen(
    modifier: Modifier = Modifier,
    viewModel: StartViewModel = viewModel(),
    onNext: () -> Unit,
    imageResource: Int = R.drawable.helldivers_2,
) {
    val username by viewModel.username.collectAsState()
    val townName by viewModel.townName.collectAsState()

    val context = LocalContext.current

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Image(
            painter = painterResource(id = imageResource),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .sizeIn(
                    minWidth = 200.dp,
                    maxWidth = 300.dp,
                )
        )

        Text("${stringResource(R.string.startscreen_helldiving_from)}: $townName.", color = MaterialTheme.colorScheme.onSecondary)

        Button(
            onClick = onNext,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.background
            ),
            modifier = modifier
                .padding(
                    horizontal = 16.dp,
                    vertical = 20.dp)
        ) {
            Text(
                text = "${stringResource(R.string.startscreen_proceed_button)}, $username",
                color = MaterialTheme.colorScheme.onSecondary
            )
        }
    }
}

@Composable
fun VerticalStartScreen(
    modifier: Modifier = Modifier,
    viewModel: StartViewModel = viewModel(),
    onNext: () -> Unit,
    imageResource: Int = R.drawable.helldivers_2,
) {
    val username by viewModel.username.collectAsState()
    val townName by viewModel.townName.collectAsState()

    val context = LocalContext.current

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Image(
            painter = painterResource(id = imageResource),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .sizeIn(
                    minWidth = 200.dp,
                    maxWidth = 300.dp,
                )
        )

        Text("${stringResource(R.string.startscreen_helldiving_from)}: $townName.", color = MaterialTheme.colorScheme.primary)

        Spacer(modifier = Modifier.height(200.dp))

        Button(
            onClick = onNext,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            modifier = modifier
                .padding(
                    horizontal = 16.dp,
                    vertical = 80.dp)
                .width(200.dp)
        ) {
            Text(
                text = "${stringResource(R.string.startscreen_proceed_button)}, $username",
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StartScreenPreview() {
    MyApplicationTheme {
        StartScreen(
            onNext = {},
            imageResource = R.drawable.helldivers_2,
            modifier = Modifier
        )
    }
}
