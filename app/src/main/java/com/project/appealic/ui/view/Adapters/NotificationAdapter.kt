package com.project.appealic.ui.view.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.project.appealic.R
import com.project.appealic.data.model.Notification

class NotificationAdapter(context: Context, private val notifications: List<Notification>): ArrayAdapter<Notification>(context, 0, notifications) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_notify, parent, false)
        val notification = notifications[position]

        val notiPhoto = view.findViewById<ImageView>(R.id.imvNotiPhoto)
        val notiDateTime = view.findViewById<TextView>(R.id.txtNotiDateTime)
        val notiTitle = view.findViewById<TextView>(R.id.txtNotiTitle)
        val notiDescription = view.findViewById<TextView>(R.id.txtNotiDescription)

        notiPhoto.setImageResource(notification.photoId)
        notiDateTime.text = notification.dateTime
        notiTitle.text = notification.title
        notiDescription.text = notification.description

        return view
    }
}