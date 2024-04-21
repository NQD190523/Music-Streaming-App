package com.project.appealic.ui.view.Fragment

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.project.appealic.R

class UpdatePassDialogFragment : DialogFragment() {
    private lateinit var edtEmailProfile: EditText

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dialog_update_profile, container, false)

        edtEmailProfile = view.findViewById(R.id.edtEmailProfile)
        setOnClickListeners(view)
        return view
    }

    private fun setOnClickListeners(view: View) {
        val btnUpdate = view.findViewById<Button>(R.id.btnUpdate)
        val cancelButton = view.findViewById<ImageButton>(R.id.imvCancel)

        // Tắt Dialog khi click ra ngoài
        dialog?.setCanceledOnTouchOutside(true)
        // Tắt Dialog khi click Image Cancel
        cancelButton.setOnClickListener {
            dismiss()
        }

        btnUpdate.setOnClickListener {
            val email = edtEmailProfile.text.toString().trim()
            if (isValidEmail(email)) {
                sendPasswordResetEmail(email)
            } else {
                showToast("Địa chỉ email không hợp lệ")
            }
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun sendPasswordResetEmail(email: String) {
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    showToast("Đã gửi liên kết reset mật khẩu đến email của bạn")
                } else {
                    showToast("Lỗi: ${task.exception?.message}")
                }
            }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onStart() {
        super.onStart()

        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            dialog.window?.setLayout(width, height)
        }
    }
}