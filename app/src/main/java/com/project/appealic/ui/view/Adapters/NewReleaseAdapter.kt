import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.project.appealic.R
import com.project.appealic.data.model.Song
import de.hdodenhof.circleimageview.CircleImageView

class NewReleaseAdapter(private val context: Context, private val songs: List<Song>) :
    RecyclerView.Adapter<NewReleaseAdapter.SongViewHolder>() {

    private val storage = FirebaseStorage.getInstance()

    inner class SongViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val songImageView: CircleImageView = itemView.findViewById(R.id.imvSong)
        val songNameTextView: TextView = itemView.findViewById(R.id.txtSongName)
        val artistTextView: TextView = itemView.findViewById(R.id.txtArtistName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.item_new_release,
            parent,
            false
        )
        return SongViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val currentSong = songs[position]
        val gsReference = currentSong.imageResource?.let { storage.getReferenceFromUrl(it) }

        // Load image using Glide
        Glide.with(context)
            .load(gsReference)
            .into(holder.songImageView)

        // Set song name and artist name
        holder.songNameTextView.text = currentSong.name
        holder.artistTextView.text = currentSong.artist
    }

    override fun getItemCount() = songs.size
}
