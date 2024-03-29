package com.project.appealic

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView

class ActivityAccount : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wel_account)

        val nestedScrollView = findViewById<NestedScrollView>(R.id.nestedScrollView)
        val newContent = findViewById<LinearLayout>(R.id.newContent)
        val llInfoAccount = findViewById<LinearLayout>(R.id.ll_Info_Account)

        // Ẩn NestedScrollView và hiển thị newContent khi tương tác với ll_Info_Account
        llInfoAccount.setOnClickListener {
            nestedScrollView.visibility = View.GONE
            newContent.visibility = View.VISIBLE
        }
    }
}
