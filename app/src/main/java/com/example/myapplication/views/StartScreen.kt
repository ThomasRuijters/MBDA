package com.example.myapplication.views

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.R
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.google.android.gms.location.LocationServices
import java.io.IOException
import java.util.Locale
import android.Manifest

@Composable
fun StartScreen(
    modifier: Modifier = Modifier,
    onNext: () -> Unit,
    imageResource: Int = R.drawable.helldivers_2,
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Image(
            painter = painterResource(id = imageResource),
            contentDescription = "Main logo",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .sizeIn(
                    minWidth = 200.dp,
                    maxWidth = 300.dp,
                )
        )
        TownNameScreen()
        Spacer(modifier = Modifier.height(200.dp))

        Button(
            onClick = onNext,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.DarkGray
            ),
            modifier = modifier
                .padding(
                    horizontal = 16.dp,
                    vertical = 80.dp)
                .width(200.dp)
        ) {
            Text(
                text = "We dive",
                color = Color(0xFFffe900)
            )
        }
    }
}

@Composable
fun TownNameScreen() {
    val context = LocalContext.current
    var townName by remember { mutableStateOf("Unknown") }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            getLocation(context) { town ->
                townName = town ?: "Unavailable"
            }
        } else {
            townName = "Permission denied"
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(16.dp)
    ) {
        Button(onClick = {
            launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }) {
            Text("Get Current Town")
        }

        Text("Current town: $townName", color = Color.White)
    }
}

@SuppressLint("MissingPermission")
fun getLocation(context: Context, onTownResolved: (String?) -> Unit) {
    val fusedClient = LocationServices.getFusedLocationProviderClient(context)

    fusedClient.lastLocation.addOnSuccessListener { location ->
        if (location != null) {
            val geocoder = Geocoder(context, Locale.getDefault())
            try {
                val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                val townName = addresses?.firstOrNull()?.locality
                onTownResolved(townName)
            } catch (e: IOException) {
                onTownResolved("Geocoder failed")
            }
        } else {
            onTownResolved("Location unavailable")
        }
    }.addOnFailureListener {
        onTownResolved("Location error")
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