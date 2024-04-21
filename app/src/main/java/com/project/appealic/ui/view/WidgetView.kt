package com.project.appealic.ui.view

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.widget.RemoteViews
import com.project.appealic.R
import com.project.appealic.data.repository.service.MusicPlayerService
import java.net.URL
import java.util.concurrent.Executors

class WidgetView: AppWidgetProvider() {
    @SuppressLint("RemoteViewLayout")
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        val musicPlayerServiceIntent = Intent(context, MusicPlayerService::class.java)
        context.startService(musicPlayerServiceIntent)
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
//    companion object {
//        internal fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
//            val views = RemoteViews(context.packageName, R.layout.widget_playsong)
//
//            val playIntent = Intent(context, MusicPlayerService::class.java).apply {
//                action = MusicPlayerService.ACTION_PLAY
//            }
//            val pauseIntent = Intent(context, MusicPlayerService::class.java).apply {
//                action = MusicPlayerService.ACTION_PAUSE
//            }
//            val nextIntent = Intent(context, MusicPlayerService::class.java).apply {
//                action = MusicPlayerService.ACTION_NEXT
//            }
//            val prevIntent = Intent(context, MusicPlayerService::class.java).apply {
//                action = MusicPlayerService.ACTION_PREVIOUS
//            }
//
//            views.setOnClickPendingIntent(R.id.play_button, PendingIntent.getService(context, 0, playIntent, 0))
//            views.setOnClickPendingIntent(R.id.pause_button, PendingIntent.getService(context, 0, pauseIntent, 0))
//            views.setOnClickPendingIntent(R.id.next_button, PendingIntent.getService(context, 0, nextIntent, 0))
//            views.setOnClickPendingIntent(R.id.prev_button, PendingIntent.getService(context, 0, prevIntent, 0))
//
//            appWidgetManager.updateAppWidget(appWidgetId, views)
//        }
//    }
}
