package com.example.myapplication.views.stratagem_list

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.myapplication.R
import com.example.myapplication.domain.model.Stratagem

@Composable
fun StratagemListScreen(
    viewModel: StratagemListViewModel,
    onStratagemClick: (Stratagem) -> Unit,
    onAddStratagemClick: () -> Unit,
    onSettingsClick: () -> Unit,
    updateTopBar: (String, @Composable () -> Unit) -> Unit
) {
    updateTopBar(
        stringResource(R.string.stratagem_screen_title)
    ) {
        IconButton(onClick = onAddStratagemClick) {
            Icon(
                imageVector = Icons.Rounded.Add,
                contentDescription = stringResource(R.string.stratagem_screen_create_button_description),
                tint = MaterialTheme.colorScheme.primary
            )
        }
        IconButton(onClick = onSettingsClick) {
            Icon(
                imageVector = Icons.Rounded.Settings,
                contentDescription = stringResource(R.string.settings_title),
                tint = MaterialTheme.colorScheme.tertiary
            )
        }
    }

    val stratagems by viewModel.stratagems.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(key1 = true) {
        viewModel.loadStratagems()
    }

    when {
        stratagems.isEmpty() && error == null -> LoadingScreen(modifier = Modifier.fillMaxSize())
        stratagems.isNotEmpty() -> ResultScreen(
            stratagems,
            modifier = Modifier.padding(),
            onStratagemClick = onStratagemClick
        )
        error != null -> ErrorScreen(modifier = Modifier.fillMaxSize())
    }
}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.secondary),
        contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = Modifier.size(200.dp),
            painter = painterResource(R.drawable.loading_img),
            contentDescription = stringResource(R.string.loading)
        )
    }
}

@Composable
fun ErrorScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_connection_error),
            contentDescription = ""
        )
        Text(
            text = stringResource(R.string.loading_failed),
            modifier = Modifier.padding(16.dp),
            style = TextStyle(color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
        )
    }
}

@Composable
fun ResultScreen(
    stratagems: List<Stratagem>,
    modifier: Modifier = Modifier,
    onStratagemClick: (Stratagem) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(stratagems.sortedBy { it.groupId }) { stratagem ->
            StratagemListItem(stratagem = stratagem, onClick = onStratagemClick)
        }
    }
}
