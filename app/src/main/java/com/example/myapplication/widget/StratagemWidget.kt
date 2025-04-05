package com.example.myapplication.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.res.stringResource
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
import com.example.myapplication.persistence.StratagemFileStore


class StratagemWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val resourceId = StratagemFileStore(context).loadResourceId()

        provideContent {
            MyContent(resourceId)
        }
    }

    @Composable
    private fun MyContent(stratagemResourceId: Int) {
        key(stratagemResourceId) {
            Column(
                modifier = GlanceModifier.padding(16.dp)
            ) {
                Image(
                    provider = ImageProvider(resId = stratagemResourceId),
                    contentDescription = stringResource(R.string.stratagem_widget_image_description),
                    modifier = GlanceModifier.fillMaxWidth()
                )
            }
        }
    }
}