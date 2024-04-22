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
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        // Đối với mỗi widget
        appWidgetIds.forEach { appWidgetId ->
            // Tạo Intent để khởi động MusicPlayerService
            val musicPlayerServiceIntent = Intent(context, MusicPlayerService::class.java)
            context.startService(musicPlayerServiceIntent)

            // Tạo PendingIntent cho các Intent play, pause, next và previous
            val playPendingIntent = createPendingIntent(context, MusicPlayerService.ACTION_PLAY)
            val pausePendingIntent = createPendingIntent(context, MusicPlayerService.ACTION_PAUSE)
            val nextPendingIntent = createPendingIntent(context, MusicPlayerService.ACTION_NEXT)
            val prevPendingIntent = createPendingIntent(context, MusicPlayerService.ACTION_PREVIOUS)

            // Thiết lập sự kiện click cho các Button trong widget
            val remoteViews = RemoteViews(context.packageName, R.layout.widget_playsong)
            remoteViews.setOnClickPendingIntent(R.id.imvPlay, playPendingIntent)
            remoteViews.setOnClickPendingIntent(R.id.imvPlay, pausePendingIntent)
            remoteViews.setOnClickPendingIntent(R.id.imvNext, nextPendingIntent)

            // Cập nhật widget với các RemoteViews đã cập nhật
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews)
        }
    }

    // Hàm để tạo PendingIntent cho các Intent
    private fun createPendingIntent(context: Context, action: String): PendingIntent {
        val intent = Intent(context, MusicPlayerService::class.java).apply {
            this.action = action
        }
        return PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
    }
}
