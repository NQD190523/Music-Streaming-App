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
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import com.project.appealic.R
import com.project.appealic.data.model.Artist
import com.project.appealic.data.repository.ArtistRepository
import com.project.appealic.data.repository.SongRepository
import com.project.appealic.data.repository.UserRepository
import com.project.appealic.ui.viewmodel.ArtistViewModel
import com.project.appealic.ui.viewmodel.SongViewModel
import com.project.appealic.utils.ArtistViewModelFactory
import com.project.appealic.utils.SongViewModelFactory

class ArtistResultAdapter(context: Context, artists: List<Artist>,
                          private val artistViewModel: ArtistViewModel
) :
    ArrayAdapter<Artist>(context, R.layout.item_search_result_artist, artists) {

    private val storage = Firebase.storage
    val userId = FirebaseAuth.getInstance().currentUser?.uid
    private lateinit var artistId: String


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        val viewHolder: ViewHolder



        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_search_result_artist, parent, false)
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

        // Xác định trạng thái của nút follow và cập nhật màu nền và văn bản
        artistId = currentArtist?.Id.toString()
        if (userId != null) {
            artistViewModel.getFollowArtistFromUser(userId)
            val isFollowed = artistViewModel.likedArtist.value?.any { it.Id == artistId } ?: false

            if (isFollowed) {
                viewHolder.followBtn.text = "Following"
                viewHolder.followBtn.setBackgroundResource(R.drawable.btn_following)
            }
            else {
                viewHolder.followBtn.text = "Follow"
                viewHolder.followBtn.setBackgroundResource(R.drawable.btn_not_follow)
            }
        } else {
            // Hiển thị thông báo yêu cầu đăng nhập nếu người dùng chưa đăng nhập
            Toast.makeText(context, "You need to sign in to use this feature", Toast.LENGTH_SHORT).show()
        }

        // Xử lý sự kiện click cho nút follow
        viewHolder.followBtn.setOnClickListener {
            if (userId != null) {
                artistViewModel.getFollowArtistFromUser(userId)
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
}
