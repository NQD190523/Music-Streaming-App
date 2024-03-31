package com.project.appealic.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.net.URL

object ImageUtils {
    fun loadImageFromUrl(url : String) : Bitmap?{
        return try {
            val url = URL(url)
            val connection = url.openConnection()
            connection.doInput = true
            connection.connect()
            val  input = connection.getInputStream()
            BitmapFactory.decodeStream(input)
        } catch (e :Exception){
            e.printStackTrace()
            null
        }
    }
}