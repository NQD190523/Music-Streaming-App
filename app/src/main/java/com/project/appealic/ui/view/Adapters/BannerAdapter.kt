package com.project.appealic.ui.view.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.project.appealic.R

class BannerAdapter(private val bannerImages: List<Int>) : RecyclerView.Adapter<BannerAdapter.BannerViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
        return BannerViewHolder(view)
    }

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        holder.bind(bannerImages[position])
    }

    override fun getItemCount(): Int {
        return bannerImages.size
    }

    class BannerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val bannerImageView: ImageView = itemView.findViewById(R.id.rrBanner)

        fun bind(imageResId: Int) {
            bannerImageView.setImageResource(imageResId)
        }
    }
}
