package com.project.appealic.ui.view.Adapters
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import com.project.appealic.R
import com.project.appealic.data.model.Artist
import com.project.appealic.data.model.Track
import com.project.appealic.ui.viewmodel.ArtistViewModel

class FavouriteArtistAdapter(
    context: Context,
    private val artists: List<Artist>,
    private val artistViewModel: ArtistViewModel,
    private val listener: OnArtistClickListener
    ) :
    ArrayAdapter<Artist>(context, R.layout.item_favourite_artist, artists) {


    private val storage = Firebase.storage
    val userId = FirebaseAuth.getInstance().currentUser?.uid

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        val viewHolder: ViewHolder

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_favourite_artist, parent, false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        val currentArtist = getItem(position)
        val gsReference = currentArtist?.ImageResource?.let { storage.getReferenceFromUrl(it) }
        Glide.with(context)
            .load(gsReference)
            .into(viewHolder.artistImageView)
        viewHolder.artistNameTextView.text = currentArtist?.Name

        val artistId = currentArtist?.Id.toString()

        // Xử lý sự kiện click cho item
        view.setOnClickListener {
            listener.onArtistClick(currentArtist!!) // Gọi hàm trong listener khi một nghệ sĩ được nhấn
        }

        // Xác định trạng thái của nút follow và cập nhật màu nền và văn bản
        if (userId != null) {

            artistViewModel.getFollowArtistFromUser(userId)
            artistViewModel.likedArtist.observe(context as LifecycleOwner, Observer {likedArtist ->
                val isFollowed = artists.any { it.Id == artistId }
                if (isFollowed) {
                    viewHolder.followBtn.text = "Following"
                    viewHolder.followBtn.setBackgroundResource(R.drawable.btn_following)
                } else {
                    viewHolder.followBtn.text = "Follow"
                    viewHolder.followBtn.setBackgroundResource(R.drawable.btn_not_follow)
                }
            })

        } else {
            // Hiển thị thông báo yêu cầu đăng nhập nếu người dùng chưa đăng nhập
            Toast.makeText(context, "You need to sign in to use this feature", Toast.LENGTH_SHORT).show()
        }

        // Xử lý sự kiện click cho nút follow
        viewHolder.followBtn.setOnClickListener {
            if (userId != null) {

                val isFollowed = artistViewModel.likedArtist.value?.any { it.Id == artistId } ?: false
                if (currentArtist != null) {
                    if (isFollowed) {
                        // Nếu đang following thì remove khỏi danh sách
                        artistViewModel.removeArtistToUserFollowArtist(userId, artistId)
                        viewHolder.followBtn.text = "Follow"
                        viewHolder.followBtn.setBackgroundResource(R.drawable.btn_not_follow)
                    } else {
                        // Nếu chưa following thì add vào danh sách
                        artistViewModel.addArtistToUserFollowArtist(userId, artistId)
                        viewHolder.followBtn.text = "Following"
                        viewHolder.followBtn.setBackgroundResource(R.drawable.btn_following)
                    }
                }
            } else {
                // Hiển thị thông báo yêu cầu đăng nhập nếu người dùng chưa đăng nhập
                Toast.makeText(context, "You need to sign in to use this feature", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    private class ViewHolder(view: View) {
        val artistImageView: ImageView = view.findViewById(R.id.imvPhotoArtist)
        val artistNameTextView: TextView = view.findViewById(R.id.txtArtistName)
        val followBtn: Button = view.findViewById(R.id.btnFollowArtist)
    }

    // Interface cho sự kiện nhấn vào nghệ sĩ
    interface OnArtistClickListener {
        fun onArtistClick(artist: Artist)
    }
}
