package com.example.myapplication.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.core.content.ContextCompat
import com.example.myapplication.R
import com.example.myapplication.domain.model.Stratagem

object BitmapUtils {
    fun getBitmapFromVectorDrawable(context: Context, drawableId: Int): Bitmap {
        val drawable = ContextCompat.getDrawable(context, drawableId)
            ?: return Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)

        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )

        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)

        return bitmap
    }

    fun getResourceId(context: Context, name: String): Int {
        val formattedName = name
            .lowercase()
            .replace(" ", "_")
            .replace("-", "_")
            .replace("\"", "")

        return try {
            context.resources.getIdentifier(formattedName, "drawable", context.packageName)
        } catch (e: Exception) {
            R.drawable.helldivers_2__icon_
        }
    }
}
