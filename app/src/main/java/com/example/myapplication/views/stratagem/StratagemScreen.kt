package com.example.myapplication.views.stratagem

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R

@Composable
fun StratagemScreen(
    state: StratagemState,
    onEvent: (StratagemEvent) -> Unit,
    updateTopBar: (String, @Composable () -> Unit) -> Unit
) {
    updateTopBar(
        "New stratagem"
    ) {
        if (state.id != null) {
            IconButton(onClick = { onEvent(StratagemEvent.DeleteStratagem) }) {
                Icon(
                    imageVector = Icons.Rounded.Delete,
                    contentDescription = "delete",
                    tint = Color.Red
                )
            }
        }
    }

    val context = LocalContext.current
    val formattedName = state.name
        .lowercase()
        .replace(" ", "_")
        .replace("-", "_")
        .replace("\"", "")
    val resourceId: Int = try {
        context.resources.getIdentifier(formattedName, "drawable", context.packageName)
    } catch (e: Exception) {
        0
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 15.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(id = if (resourceId != 0) resourceId else R.drawable.eagle_500kg_bomb),
            contentDescription = "Helldivers Icon",
            tint = Color.Unspecified,
            modifier = Modifier.size(100.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        StratagemTextField("Name", state.name) { onEvent(StratagemEvent.UpdateField("name", it)) }
        StratagemTextField("Codename", state.codename) { onEvent(StratagemEvent.UpdateField("codename", it)) }
        StratagemTextField("Uses", state.uses) { onEvent(StratagemEvent.UpdateField("uses", it)) }
        StratagemTextField("Cooldown", state.cooldown.toString()) { onEvent(StratagemEvent.UpdateField("cooldown", it)) }
        StratagemTextField("Activation", state.activation.toString()) { onEvent(StratagemEvent.UpdateField("activation", it)) }

        if (state.id !== null) {
            StratagemTextField("Group ID", state.groupId.toString()) { onEvent(StratagemEvent.UpdateField("groupId", it)) }
            StratagemTextField("Image URL", state.imageUrl) { onEvent(StratagemEvent.UpdateField("imageUrl", it)) }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = { onEvent(StratagemEvent.Save) },
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Yellow, contentColor = Color.Black),
            shape = RoundedCornerShape(8.dp),
        ) {
            Text("SAVE", fontWeight = FontWeight.Bold)
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
                color = Color.Yellow
            )
        },
        modifier = Modifier.fillMaxWidth(),
        textStyle = androidx.compose.ui.text.TextStyle(
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        ),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = Color.Yellow,
            focusedBorderColor = Color.White,
            cursorColor = Color.Yellow
        )
    )
}

