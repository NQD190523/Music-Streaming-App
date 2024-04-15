import android.content.Context
import android.database.DataSetObserver
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ListAdapter
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.appealic.R
import com.project.appealic.data.model.Track
import com.project.appealic.ui.view.Fragment.AddPlaylistFragment
import com.project.appealic.ui.view.Fragment.MoreActionFragment

class SongAdapter(private val context: Context, private val tracks: List<Track>) :
    RecyclerView.Adapter<SongAdapter.SongViewHolder>(), ListAdapter {

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

    override fun registerDataSetObserver(observer: DataSetObserver?) {
        TODO("Not yet implemented")
    }

    override fun unregisterDataSetObserver(observer: DataSetObserver?) {
        TODO("Not yet implemented")
    }

    override fun getCount(): Int {
        TODO("Not yet implemented")
    }

    override fun getItem(position: Int): Any {
        TODO("Not yet implemented")
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        TODO("Not yet implemented")
    }

    override fun getViewTypeCount(): Int {
        TODO("Not yet implemented")
    }

    override fun isEmpty(): Boolean {
        TODO("Not yet implemented")
    }

    override fun areAllItemsEnabled(): Boolean {
        // Trả về true nếu tất cả các mục trong danh sách đều được kích hoạt
        return false // hoặc trả về giá trị phù hợp với logic của bạn
    }

    override fun isEnabled(position: Int): Boolean {
        // Kiểm tra xem một mục cụ thể có được kích hoạt không
        return true // hoặc trả về giá trị phù hợp với logic của bạn
    }

}



