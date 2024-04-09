package com.project.appealic.ui.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.project.appealic.R

class Activity_confirm_student: AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm_student)

        val monthAutoCompleteTextView: AutoCompleteTextView = findViewById(R.id.autoCompleteMonth)
        val dayAutoCompleteTextView: AutoCompleteTextView = findViewById(R.id.autoCompleteDay)
        val yearAutoCompleteTextView: AutoCompleteTextView = findViewById(R.id.autoCompleteYear)
        // Danh sách tháng và ngày
        val months = arrayOf("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")
        val days = (1..31).map { it.toString() }.toTypedArray()
        val years = (1950..2010).map { it.toString() }.toTypedArray()

        // Tạo adapter cho AutoCompleteTextView
        val monthAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, months)
        val dayAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, days)
        val yearAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, years)

        // Set adapter cho AutoCompleteTextView
        monthAutoCompleteTextView.setAdapter(monthAdapter)
        dayAutoCompleteTextView.setAdapter(dayAdapter)
        yearAutoCompleteTextView.setAdapter(yearAdapter)

        // Thiết lập sự kiện khi bấm vào AutoCompleteTextView
        monthAutoCompleteTextView.setOnClickListener {
            monthAutoCompleteTextView.showDropDown()
        }

        dayAutoCompleteTextView.setOnClickListener {
            dayAutoCompleteTextView.showDropDown()
        }
        yearAutoCompleteTextView.setOnClickListener {
            yearAutoCompleteTextView.showDropDown()
        }
    }}