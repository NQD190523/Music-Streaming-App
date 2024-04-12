package com.project.appealic.ui.view

import android.annotation.SuppressLint
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.widget.RemoteViews
import com.project.appealic.R
import java.net.URL
import java.util.concurrent.Executors

class WidgetView: AppWidgetProvider() {
    @SuppressLint("RemoteViewLayout")
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        val sharedPreferences = context.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE)
        val songTitle = sharedPreferences.getString("SONG_TITLE", "")
        val singerName = sharedPreferences.getString("SINGER_NAME", "")
        val trackImage = sharedPreferences.getString("TRACK_IMAGE", "")

        val remoteViews = RemoteViews(context.packageName, R.layout.widget_playsong)
        remoteViews.setTextViewText(R.id.txtSongName, songTitle)
        remoteViews.setTextViewText(R.id.txtArtistName, singerName)

        val executor = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())

        executor.execute {
            val url = URL(trackImage)
            val bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream())
            handler.post {
                remoteViews.setImageViewBitmap(R.id.imvSongPhoto, bitmap)
                appWidgetManager.updateAppWidget(
                    ComponentName(context, WidgetView::class.java),
                    remoteViews
                )
            }
        }
    }
}
