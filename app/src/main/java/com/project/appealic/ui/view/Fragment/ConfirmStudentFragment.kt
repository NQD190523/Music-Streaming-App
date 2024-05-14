package com.project.appealic.ui.view.Fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.project.appealic.R
import com.project.appealic.ui.view.ActivityRegister
import com.project.appealic.ui.view.BankTransfer

class ConfirmStudentFragment : Fragment() {
    private lateinit var fullNameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var monthAutoCompleteTextView: AutoCompleteTextView
    private lateinit var dayAutoCompleteTextView: AutoCompleteTextView
    private lateinit var yearAutoCompleteTextView: AutoCompleteTextView
    private lateinit var schoolEditText: EditText
    private lateinit var studentIdEditText: EditText
    private lateinit var confirmCheckBox: CheckBox
    private lateinit var submitButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_confirm_student, container, false)
        initializeViews(view)
        setupAutoCompleteTextViews()
        setClickListeners()

        val amount = arguments?.getDouble("AMOUNT") ?: 0.0

        return view
    }

    private fun initializeViews(view: View) {
        fullNameEditText = view.findViewById(R.id.etFullName)
        emailEditText = view.findViewById(R.id.etEmail)
        monthAutoCompleteTextView = view.findViewById(R.id.autoCompleteMonth)
        dayAutoCompleteTextView = view.findViewById(R.id.autoCompleteDay)
        yearAutoCompleteTextView = view.findViewById(R.id.autoCompleteYear)
        schoolEditText = view.findViewById(R.id.etSchool)
        studentIdEditText = view.findViewById(R.id.etStudentID)
        confirmCheckBox = view.findViewById(R.id.chkConfirm)
        submitButton = view.findViewById(R.id.btnSubmit)
    }

    private fun setupAutoCompleteTextViews() {
        val months = arrayOf("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")
        val days = (1..31).map { it.toString() }.toTypedArray()
        val years = (1900..2024).map { it.toString() }.toTypedArray()

        val monthAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, months)
        monthAutoCompleteTextView.setAdapter(monthAdapter)

        val dayAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, days)
        dayAutoCompleteTextView.setAdapter(dayAdapter)

        val yearAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, years)
        yearAutoCompleteTextView.setAdapter(yearAdapter)
    }

    private fun setClickListeners() {
        submitButton.setOnClickListener {
            validateAndSubmit()
        }

        confirmCheckBox.setOnClickListener {
            handleConfirmCheckBoxClick()
        }
    }

    private fun validateAndSubmit() {
        val fullName = fullNameEditText.text.toString().trim()
        val email = emailEditText.text.toString().trim()
        val month = monthAutoCompleteTextView.text.toString().trim()
        val day = dayAutoCompleteTextView.text.toString().trim()
        val year = yearAutoCompleteTextView.text.toString().trim()
        val school = schoolEditText.text.toString().trim()
        val studentId = studentIdEditText.text.toString().trim()
        val isConfirmed = confirmCheckBox.isChecked
        val amount = arguments?.getDouble("AMOUNT") ?: 0.0

        // Perform validation checks
        if (fullName.isEmpty() || email.isEmpty() || month.isEmpty() || day.isEmpty() || year.isEmpty() || school.isEmpty() || studentId.isEmpty() || !isConfirmed) {
            // Display error message or highlight invalid fields
            return
        }

        if (isValidStudentEmail(email)) {
            navigateToBankTransferActivity(amount)
        } else {
            navigateToSignUpActivity()
        }
    }

    private fun handleConfirmCheckBoxClick() {
        val isChecked = confirmCheckBox.isChecked
        if (isChecked) {
            // Checkbox đã được chọn, cho phép submit
            submitButton.isEnabled = true
        } else {
            // Checkbox không được chọn, không cho phép submit
            submitButton.isEnabled = false
            showToast("Please agree to the terms and conditions to proceed.")
        }
    }
    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun isValidStudentEmail(email: String): Boolean {
        // Implement your email validation logic here
        return email.contains("edu", ignoreCase = true)
    }

    private fun navigateToBankTransferActivity(amount: Double) {
        val intent = Intent(requireActivity(), BankTransfer::class.java)
        intent.putExtra("AMOUNT", amount)
        startActivity(intent)
    }

    private fun navigateToSignUpActivity() {
        val intent = Intent(requireActivity(), ActivityRegister::class.java)
        startActivity(intent)
    }

    companion object {
        fun newInstance(bundle: Bundle? = null): ConfirmStudentFragment {
            val fragment = ConfirmStudentFragment()
            bundle?.let { fragment.arguments = it }
            return fragment
        }
    }
}