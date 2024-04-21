package com.project.appealic.ui.view.Fragment
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.project.appealic.data.model.User
import com.project.appealic.databinding.DialogUpdateEmailBinding
import com.project.appealic.ui.dialog.BaseUpdateProfileDialog


class UpdateEmailDialog : BaseUpdateProfileDialog<DialogUpdateEmailBinding>(
    DialogUpdateEmailBinding::class.java
) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profileViewModel.updateEmailLiveData.observe(viewLifecycleOwner) { newEmail ->
            if (newEmail != null) {
                Toast.makeText(requireContext(), "Cập nhật email thành công", Toast.LENGTH_SHORT).show()
                binding.edtEmailProfile.setText(newEmail)
            } else {
                Toast.makeText(requireContext(), "Cập nhật email thất bại", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun setupViews() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        binding.edtEmailProfile.setText(currentUser?.email)
    }

    override fun updateValue() {
        val newEmail = binding.edtEmailProfile.text.toString().trim()
        if (newEmail.isNotBlank()) {
            val uid = FirebaseAuth.getInstance().currentUser?.uid
            if (uid != null) {
                profileViewModel.updateUserEmail(uid, newEmail)
            }
        }
    }

    private fun getCurrentUser(): User? {
        val currentUser = FirebaseAuth.getInstance().currentUser
        return currentUser?.let {
            User(
                email = it.email ?: "",
            )
        }
    }
}
