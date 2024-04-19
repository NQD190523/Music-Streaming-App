package com.project.appealic.ui.view

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.AdapterView
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.firebase.auth.FirebaseAuth
import com.project.appealic.data.model.SongEntity
import com.project.appealic.data.model.Track
import com.project.appealic.ui.view.Adapters.NewReleaseAdapter
import com.project.appealic.ui.viewmodel.SongViewModel

fun ListView.setOnItemClickListener(
    context: Context,
    songViewModel: SongViewModel,
    trackList: List<Track>
) {
    this.onItemClickListener = AdapterView.OnItemClickListener { parent, _, position, _ ->
        val selectedSong = trackList[position]
        val user = FirebaseAuth.getInstance().currentUser?.uid

        val trackUrlList = ArrayList<String>()

        val song = selectedSong.trackId?.let {
            SongEntity(
                it,
                selectedSong.trackUrl,
                selectedSong.trackImage,
                selectedSong.trackTitle,
                selectedSong.artist,
                user,
                null,
                System.currentTimeMillis(),
                null,
                selectedSong.duration?.toLong(),
                selectedSong.artistId,
            )
        }

        //Lưu vào room db recent play
        if (song != null) {
            songViewModel.insertSong(song)
            Log.d(" test status", "success")
        }

        for (i in 0 until parent.count) {
            val item = trackList[i]
            item.trackUrl?.let { trackUrl ->
                println(trackUrl)
                trackUrlList.add(trackUrl)
            }
        }
        val intent = Intent(context, ActivityMusicControl::class.java).apply {
            putExtra("SONG_TITLE", selectedSong.trackTitle)
            putExtra("SINGER_NAME", selectedSong.artist)
            putExtra("SONG_NAME", selectedSong.trackTitle)
            putExtra("TRACK_IMAGE", selectedSong.trackImage)
            putExtra("DURATION", selectedSong.duration)
            putExtra("TRACK_INDEX",position)
            putStringArrayListExtra("TRACK_LIST", trackUrlList)
        }
        context.startActivity(intent)
    }
}
