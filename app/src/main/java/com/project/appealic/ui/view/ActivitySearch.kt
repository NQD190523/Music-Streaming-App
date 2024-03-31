package com.project.appealic.ui.view

import android.content.Intent
import android.os.Bundle
import android.widget.GridView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.project.appealic.R
import com.project.appealic.ui.view.Adapters.PlaylistAdapter

class ActivitySearch : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView

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

        // Cấu hình BottomNavigationView
        bottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setItemIconTintList(ContextCompat.getColorStateList(this, R.color.bottom_nav_icon_selector))
        bottomNavigationView.selectedItemId = R.id.navigation_search
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    val intent = Intent(this, ActivityHome::class.java)
                    startActivity(intent)
                    true
                }
                R.id.navigation_search -> {
                    // Do nothing if already on the search page
                    true
                }
                R.id.navigation_library ->{
                    val intent = Intent(this, ActivityLibrary::class.java)
                    startActivity(intent)
                    true
                }
                R.id.navigation_profile -> {
                    val intent = Intent(this, ActivityProfile::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
    }
}