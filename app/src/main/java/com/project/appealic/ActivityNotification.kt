package com.project.appealic

import android.os.Bundle
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.project.appealic.data.model.Notification
import com.project.appealic.ui.view.Adapters.NotificationAdapter

class ActivityNotification : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)

        val notifications = listOf(
            Notification(R.drawable.song1, "Sat, Mar 16 at 7:00 p.m", "New playlist: “Acoustic Chill”", "Unwind with the soothing strums and calm melodies for relaxing evenings."),
            Notification(R.drawable.song1, "Sat, Mar 16 at 7:00 p.m", "New playlist: “Acoustic Chill”", "Unwind with the soothing strums and calm melodies for relaxing evenings."),
            Notification(R.drawable.song1, "Sat, Mar 16 at 7:00 p.m", "New playlist: “Acoustic Chill”", "Unwind with the soothing strums and calm melodies for relaxing evenings."),
            Notification(R.drawable.song1, "Sat, Mar 16 at 7:00 p.m", "New playlist: “Acoustic Chill”", "Unwind with the soothing strums and calm melodies for relaxing evenings."),
            Notification(R.drawable.song1, "Sat, Mar 16 at 7:00 p.m", "New playlist: “Acoustic Chill”", "Unwind with the soothing strums and calm melodies for relaxing evenings."),
            Notification(R.drawable.song1, "Sat, Mar 16 at 7:00 p.m", "New playlist: “Acoustic Chill”", "Unwind with the soothing strums and calm melodies for relaxing evenings."),
            Notification(R.drawable.song1, "Sat, Mar 16 at 7:00 p.m", "New playlist: “Acoustic Chill”", "Unwind with the soothing strums and calm melodies for relaxing evenings."),
            Notification(R.drawable.song1, "Sat, Mar 16 at 7:00 p.m", "New playlist: “Acoustic Chill”", "Unwind with the soothing strums and calm melodies for relaxing evenings."),
            Notification(R.drawable.song1, "Sat, Mar 16 at 7:00 p.m", "New playlist: “Acoustic Chill”", "Unwind with the soothing strums and calm melodies for relaxing evenings."),
            Notification(R.drawable.song1, "Sat, Mar 16 at 7:00 p.m", "New playlist: “Acoustic Chill”", "Unwind with the soothing strums and calm melodies for relaxing evenings."),
            Notification(R.drawable.song1, "Sat, Mar 16 at 7:00 p.m", "New playlist: “Acoustic Chill”", "Unwind with the soothing strums and calm melodies for relaxing evenings."),
            Notification(R.drawable.song1, "Sat, Mar 16 at 7:00 p.m", "New playlist: “Acoustic Chill”", "Unwind with the soothing strums and calm melodies for relaxing evenings."),
        )
        val listView = findViewById<ListView>(R.id.lvNotification)
        val adapter = NotificationAdapter(this, notifications)
        listView.adapter = adapter

    }
}