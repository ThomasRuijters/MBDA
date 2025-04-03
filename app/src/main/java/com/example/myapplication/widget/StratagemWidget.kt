package com.example.myapplication.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import com.example.myapplication.R

class StratagemWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val sharedPreferences = context.getSharedPreferences("stratagem_preferences", Context.MODE_PRIVATE)
        val imageIndex = sharedPreferences.getInt("active_stratagem_index", 0)

        val stratagemImages = listOf(
            R.drawable.eagle_500kg_bomb,
            R.drawable.anti_personnel_minefield,
            R.drawable.orbital_laser
        )

        val stratagemImage = stratagemImages[imageIndex]

        provideContent {
            MyContent(stratagemImage)
        }
    }

    @Composable
    private fun MyContent(stratagemImage: Int) {
        Column(
            modifier = GlanceModifier.padding(16.dp)
        ) {
            Image(
                provider = ImageProvider(stratagemImage),
                contentDescription = "Stratagem Icon",
                modifier = GlanceModifier.fillMaxWidth()
            )
        }
    }

}