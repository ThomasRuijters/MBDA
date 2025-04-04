package com.example.myapplication.views.stratagem_list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.myapplication.domain.model.Stratagem
import com.example.myapplication.R
import com.example.myapplication.utils.BitmapUtils

@Composable
fun StratagemListItem(
    stratagem: Stratagem,
    onClick: (Stratagem) -> Unit
) {
    val resourceId = BitmapUtils.getResourceId(LocalContext.current, stratagem.name)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(stratagem) }
            .background(Color.Black, shape = RectangleShape)
            .height(200.dp)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .height(120.dp)
                .fillMaxWidth()
                .background(Color.Black, shape = RectangleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = if (resourceId != 0) resourceId else R.drawable.seismic_probe),
                contentDescription = stratagem.name,
                tint = Color.Unspecified,
                modifier = Modifier.size(80.dp)
            )
        }

        Text(
            text = stratagem.name,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = Color.Yellow,
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth()
                .weight(1f)
        )
    }
}