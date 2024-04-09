package com.project.appealic.ui.view

import LibraryFragment
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.project.appealic.R
import com.project.appealic.ui.view.Fragment.HomeFragment
import com.project.appealic.ui.view.Fragment.ProfileFragment
import com.project.appealic.ui.view.Fragment.SearchFragment

class ActivityHome : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView

    private val homeFragment = HomeFragment()
    private val searchFragment = SearchFragment()
    private val libraryFragment = LibraryFragment()
    private val profileFragment = ProfileFragment()

    private val onNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_home -> {
                    replaceFragment(homeFragment)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_search -> {
                    replaceFragment(searchFragment)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_library -> {
                    replaceFragment(libraryFragment)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_profile -> {
                    replaceFragment(profileFragment)
                    return@OnNavigationItemSelectedListener true
                }
                else -> false
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Set default fragment
        replaceFragment(homeFragment)
        // Cấu hình BottomNavigationView
        bottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setItemIconTintList(
            ContextCompat.getColorStateList(
                this,
                R.color.bottom_nav_icon_selector
            )
        )
        // Cấu hình BottomNavigationView
        bottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.fragmenthome, fragment).commit()
    }
}
