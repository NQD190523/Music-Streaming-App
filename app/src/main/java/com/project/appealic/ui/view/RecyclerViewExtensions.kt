package com.project.appealic.ui.view

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.project.appealic.data.model.SongEntity
import com.project.appealic.ui.view.Adapters.RecentlySongAdapter
import com.project.appealic.ui.viewmodel.SongViewModel

fun RecyclerView.Adapter<*>.setOnTrackClickListener(context: Context, songViewModel: SongViewModel, songList: List<SongEntity>) {
    if (this is RecentlySongAdapter) {
        this.onItemClick = { selectedTrack ->
            val user = FirebaseAuth.getInstance().currentUser?.uid
            val intent = Intent(context, ActivityMusicControl::class.java)
            val trackUrlList = ArrayList<String>()

            val song = selectedTrack.songId?.let {
                SongEntity(
                    it,
                    selectedTrack.thumbUrl,
                    selectedTrack.songName,
                    selectedTrack.singer,
                    user,
                    null,
                    System.currentTimeMillis(),
                    0,
                    selectedTrack.duration,
                    selectedTrack.artistId,
                )
            }

            if (song != null) {
                songViewModel.insertSong(song)
                Log.d("test status", "success")
            }

            for (i in 0 until songList.size) {
                val item = songList[i]
                item.thumbUrl?.let { trackUrl ->
                    trackUrlList.add(trackUrl)
                }
            }

            intent.putExtra("SONG_TITLE", selectedTrack.songName)
            intent.putExtra("SINGER_NAME", selectedTrack.singer)
            intent.putExtra("SONG_NAME", selectedTrack.songName)
            intent.putExtra("TRACK_IMAGE", selectedTrack.thumbUrl)
            intent.putExtra("DURATION", selectedTrack.duration)
            intent.putStringArrayListExtra("TRACK_LIST", trackUrlList)
            context.startActivity(intent)
        }
    }
}