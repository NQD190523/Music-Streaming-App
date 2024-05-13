package com.project.appealic.ui.view.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.project.appealic.R
import com.project.appealic.data.model.Album

class BannerAdapter(
    private val bannerImageResources: List<Int>,
    private val albums: List<Album>,
    private val onBannerClickListener: (Int) -> Unit
) : RecyclerView.Adapter<BannerAdapter.BannerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
        return BannerViewHolder(view)
    }

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        val album = albums[position]
        val imageResource = bannerImageResources[position]
        holder.bind(imageResource, album, onBannerClickListener)
    }

    override fun getItemCount(): Int {
        return albums.size
    }

    class BannerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val bannerImageView: ImageView = itemView.findViewById(R.id.rrBanner)

        fun bind(imageResId: Int, album: Album, onBannerClickListener: (Int) -> Unit) {
            bannerImageView.setImageResource(imageResId)

            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onBannerClickListener(position)
                }
            }
        }
    }
}