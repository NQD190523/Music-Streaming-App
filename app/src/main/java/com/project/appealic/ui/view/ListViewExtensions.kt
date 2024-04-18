import android.content.Context
import android.content.Intent
import android.widget.ListView
import com.project.appealic.data.model.SongEntity
import com.project.appealic.data.model.Track
import com.project.appealic.ui.view.ActivityMusicControl
import com.project.appealic.ui.view.Adapters.NewReleaseAdapter
import com.project.appealic.ui.viewmodel.SongViewModel

fun ListView.setOnItemClickListener(context: Context, songViewModel: SongViewModel, songList: List<Track>) {
    if (adapter is NewReleaseAdapter) {
        val newReleaseAdapter = adapter as NewReleaseAdapter
        newReleaseAdapter.onItemClick = { selectedTrack ->
            // Construct a SongEntity from the selected track
            val song = selectedTrack.trackId?.let {
                SongEntity(
                    it,
                    selectedTrack.trackImage,
                    selectedTrack.trackTitle,
                    selectedTrack.artist,
                    null,
                    null,
                    System.currentTimeMillis(),
                    null,
                    selectedTrack.duration?.toLong(),
                    selectedTrack.artistId,
                )
            }

            // Insert the SongEntity into the database
            song?.let {
                songViewModel.insertSong(it)
            }

            // Collect track URLs from the songList
            val trackUrlList = ArrayList<String>()
            songList.forEach { item ->
                item.trackUrl?.let { trackUrl ->
                    trackUrlList.add(trackUrl)
                }
            }

            // Prepare the Intent to start ActivityMusicControl
            val intent = Intent(context, ActivityMusicControl::class.java).apply {
                putExtra("SONG_TITLE", selectedTrack.trackTitle)
                putExtra("SINGER_NAME", selectedTrack.artist)
                putExtra("SONG_NAME", selectedTrack.trackTitle)
                putExtra("TRACK_IMAGE", selectedTrack.trackImage)
                putStringArrayListExtra("TRACK_LIST", trackUrlList)
            }

            // Start ActivityMusicControl with the prepared Intent
            context.startActivity(intent)
        }
    }
}
