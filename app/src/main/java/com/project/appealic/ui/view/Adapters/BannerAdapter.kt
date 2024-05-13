package com.project.appealic.ui.view.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.appealic.R
import com.project.appealic.data.model.Album

class BannerAdapter(private val albums: List<Album>, private val defaultBannerImages: List<Int>) :
    RecyclerView.Adapter<BannerAdapter.BannerViewHolder>() {

    interface OnBannerClickListener {
        fun onBannerClick(album: Album?)
    }

    private var onBannerClickListener: OnBannerClickListener? = null

    fun setOnBannerClickListener(listener: OnBannerClickListener) {
        onBannerClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
        return BannerViewHolder(view)
    }

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        val album = albums.getOrNull(position)
        if (album != null && album.thumbUrl?.isNotEmpty() == true) {
            val imageResId = holder.itemView.resources.getIdentifier(album.thumbUrl, "drawable", holder.itemView.context.packageName)
            holder.bind(imageResId)
        } else {
            val defaultImageIndex = position % defaultBannerImages.size
            holder.bind(defaultBannerImages[defaultImageIndex])
        }
        holder.itemView.setOnClickListener {
            onBannerClickListener?.onBannerClick(album)
        }
    }

    override fun getItemCount(): Int {
        return albums.size
    }

    inner class BannerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val bannerImageView: ImageView = itemView.findViewById(R.id.rrBanner)

        fun bind(imageResId: Int) {
            bannerImageView.setImageResource(imageResId)
        }
    }
}