package com.project.appealic.ui.view

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.project.appealic.data.model.SongEntity
import com.project.appealic.ui.viewmodel.SongViewModel

fun RecyclerView.setOnItemClickListener(
    context: Context,
    songViewModel: SongViewModel,
    trackList: List<SongEntity>
) {
    this.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
        override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
            return false
        }

        override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
            // Do nothing for touch events
        }

        override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
            // Do nothing for disallowIntercept
        }
    })

    this.addOnChildAttachStateChangeListener(object : RecyclerView.OnChildAttachStateChangeListener {
        override fun onChildViewAttachedToWindow(view: View) {
            view.setOnClickListener {
                val holder = getChildViewHolder(view)
                val position = holder.adapterPosition
                val selectedSong = trackList[position]
                val user = FirebaseAuth.getInstance().currentUser?.uid

                val trackUrlList = ArrayList<String>()

                val song = SongEntity(
                    selectedSong.songId,
                    selectedSong.songUrl,
                    selectedSong.thumbUrl,
                    selectedSong.songName,
                    selectedSong.singer,
                    user,
                    null,
                    System.currentTimeMillis(),
                    null,
                    selectedSong.duration?.toLong(),
                    selectedSong.artistId,
                    selectedSong.albumId,
                    selectedSong.genre,
                    selectedSong.releaseDate,
                )
                songViewModel.insertSong(song)
                Log.d(" test status", "success")


                for (element in trackList) {
                    element.songUrl?.let { trackUrl ->
                        println(trackUrl)
                        println(position)
                        trackUrlList.add(trackUrl)
                    }
                }
                val intent = Intent(context, ActivityMusicControl::class.java).apply {
                    putExtra("SONG_TITLE", selectedSong.songName)
                    putExtra("SINGER_NAME", selectedSong.singer)
                    putExtra("SONG_NAME", selectedSong.songName)
                    putExtra("TRACK_IMAGE", selectedSong.thumbUrl)
                    putExtra("DURATION", selectedSong.duration)
                    putExtra("TRACK_INDEX",position)
                    putStringArrayListExtra("TRACK_LIST", trackUrlList)
                }
                context.startActivity(intent)
            }
        }

        override fun onChildViewDetachedFromWindow(view: View) {
            // Do nothing when child view detached from window
        }
    })
}