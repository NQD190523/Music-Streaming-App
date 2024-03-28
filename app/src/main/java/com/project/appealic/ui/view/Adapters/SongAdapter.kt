import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.project.appealic.R
import com.project.appealic.data.model.Song

class SongAdapter(context: Context, private val songs: List<Song>) : ArrayAdapter<Song>(context, 0, songs) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_song, parent, false)
        val song = songs[position]

        val songNameTextView = view.findViewById<TextView>(R.id.txtSongName)
        val singerTextView = view.findViewById<TextView>(R.id.txtSinger)
        val photoImageView = view.findViewById<ImageView>(R.id.imvPhoto)

        songNameTextView.text = song.songName
        singerTextView.text = song.singer
        photoImageView.setImageResource(song.photoId)

        return view
    }
}
