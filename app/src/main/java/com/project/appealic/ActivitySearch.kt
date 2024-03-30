package com.project.appealic

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.GridView
import androidx.appcompat.app.AppCompatActivity
import com.project.appealic.ui.view.Adapters.PlaylistAdapter

class ActivitySearch : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activiy_search)

        val gridView: GridView = findViewById(R.id.gridviewSearch)

        // Danh sách hình ảnh từ thư mục drawable
        val imageList = listOf(
            R.drawable.playlist1,
            R.drawable.playlistkpop,
            R.drawable.playlistvpop,
            R.drawable.playlisttiktok,
        )

        // Tạo adapter và thiết lập cho GridView
        val adapter = PlaylistAdapter(this, imageList)
        gridView.adapter = adapter
    }
}
