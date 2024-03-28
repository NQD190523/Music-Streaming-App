package com.project.appealic

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import com.project.appealic.R

class ActivityLibrary : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_library)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        return true
    }
}
