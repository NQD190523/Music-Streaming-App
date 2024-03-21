package com.project.appealic

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class ActivityHome : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home) // Replace with your actual layout file name

        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    // Handle Home item selection
                    true
                }
                R.id.navigation_search -> {
                    // Handle Search item selection
                    true
                }
                R.id.navigation_library -> {
                    // Handle Library item selection
                    true
                }
                R.id.navigation_profile -> {
                    // Handle Profile item selection
                    true
                }
                else -> false
            }
        }
    }
}
