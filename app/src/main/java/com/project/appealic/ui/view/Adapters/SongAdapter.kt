import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.appealic.R
import com.project.appealic.data.model.Track
import com.project.appealic.ui.view.Fragment.AddPlaylistFragment
import com.project.appealic.ui.view.Fragment.MoreActionFragment

class SongAdapter(private val context: Context, private val tracks: List<Track>) :
    RecyclerView.Adapter<SongAdapter.SongViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.item_song,
            parent,
            false
        )
        return SongViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val currentTrack = tracks[position]
        holder.songNameTextView.text = currentTrack.trackTitle
        holder.singerTextView.text = currentTrack.artistId
        Glide.with(context)
            .load(currentTrack.trackImage)
            .into(holder.songImageView)

        holder.btnAddPlaylist.setOnClickListener{
        }

        holder.btnMoreAction.setOnClickListener {

        }
    }

    override fun getItemCount() = tracks.size

    inner class SongViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val songImageView: ImageView = itemView.findViewById(R.id.imvPhoto)
        val songNameTextView: TextView = itemView.findViewById(R.id.txtSongName)
        val singerTextView: TextView = itemView.findViewById(R.id.txtSinger)
        val btnAddPlaylist: ImageView  = itemView.findViewById(R.id.btnAddPlaylist)
        val btnMoreAction: ImageView  = itemView.findViewById(R.id.btnMoreAction)
    }
}



