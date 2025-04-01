package com.example.myapplication.views

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.R
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.views.stratagem.StratagemEvent

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