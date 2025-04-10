package com.example.myapplication.views.stratagem

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R
import com.example.myapplication.utils.BitmapUtils

@Composable
fun StratagemScreen(
    state: StratagemState,
    onEvent: (StratagemEvent) -> Unit,
    updateTopBar: (String, @Composable () -> Unit) -> Unit
) {
    updateTopBar(
        if (state.id != null)
            stringResource(R.string.stratagem_screen_edit_button)
        else
            stringResource(R.string.stratagem_screen_create_button)
    ) {
        if (state.id != null) {
            IconButton(onClick = { onEvent(StratagemEvent.DeleteStratagem) }) {
                Icon(
                    imageVector = Icons.Rounded.Delete,
                    contentDescription = stringResource(R.string.stratagem_screen_delete_button_description),
                    tint = MaterialTheme.colorScheme.tertiary,
                )
            }
        }
    }

    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE
    val resourceId = BitmapUtils.getResourceId(LocalContext.current, state.name)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 15.dp)
    ) {
        if (isLandscape) {
            HorizontalStratagemScreen(resourceId, state, onEvent)
        } else {
            VerticalStratagemScreen(resourceId, state, onEvent)
        }
    }
}

@Composable
fun VerticalStratagemScreen(
    resourceId: Int,
    state: StratagemState,
    onEvent: (StratagemEvent) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(id = if (resourceId != 0) resourceId else R.drawable.helldivers_2__icon_),
            contentDescription = stringResource(R.string.stratagem_screen_helldivers_icon_description),
            tint = Color.Unspecified,
            modifier = Modifier.size(100.dp).background(Color.Black)
        )

        Spacer(modifier = Modifier.height(10.dp))

        FormFields(state, onEvent)
    }
}

@Composable
fun HorizontalStratagemScreen(
    resourceId: Int,
    state: StratagemState,
    onEvent: (StratagemEvent) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Icon(
            painter = painterResource(id = if (resourceId != 0) resourceId else R.drawable.helldivers_2__icon_),
            contentDescription = stringResource(R.string.stratagem_screen_helldivers_icon_description),
            tint = Color.Unspecified,
            modifier = Modifier
                .size(150.dp)
                .align(Alignment.CenterVertically)
                .background(Color.Black)
        )

        FormFields(state, onEvent)
    }
}

@Composable
fun FormFields(state: StratagemState, onEvent: (StratagemEvent) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        StratagemTextField(stringResource(R.string.stratagem_form_name), state.name) {
            onEvent(StratagemEvent.UpdateField("name", it)) }
        StratagemTextField(
            stringResource(R.string.stratagem_form_codename),
            state.codename
        ) { onEvent(StratagemEvent.UpdateField("codename", it)) }
        StratagemTextField(stringResource(R.string.stratagem_form_uses), state.uses)
        { onEvent(StratagemEvent.UpdateField("uses", it)) }
        StratagemTextField(
            stringResource(R.string.stratagem_form_cooldown),
            state.cooldown.toString()
        ) { onEvent(StratagemEvent.UpdateField("cooldown", it)) }
        StratagemTextField(
            stringResource(R.string.stratagem_form_activation),
            state.activation.toString()
        ) { onEvent(StratagemEvent.UpdateField("activation", it)) }

        if (state.id != null) {
            StratagemTextField(
                stringResource(R.string.stratagem_form_group_id),
                state.groupId.toString()
            ) { onEvent(StratagemEvent.UpdateField("groupId", it)) }
            StratagemTextField(
                stringResource(R.string.stratagem_form_image_url),
                state.imageUrl
            ) { onEvent(StratagemEvent.UpdateField("imageUrl", it)) }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = { onEvent(StratagemEvent.Save) },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            shape = RoundedCornerShape(8.dp),
        ) {
            Text(stringResource(R.string.stratagem_form_save), fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun StratagemTextField(label: String, value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(
                label.uppercase(),
                color = MaterialTheme.colorScheme.primary,
            )
        },
        modifier = Modifier.fillMaxWidth(),
        textStyle = androidx.compose.ui.text.TextStyle(
            color = MaterialTheme.colorScheme.onSecondary,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        ),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = MaterialTheme.colorScheme.primary,
            focusedBorderColor = MaterialTheme.colorScheme.onSecondary,
            cursorColor = MaterialTheme.colorScheme.primary
        )
    )
}

