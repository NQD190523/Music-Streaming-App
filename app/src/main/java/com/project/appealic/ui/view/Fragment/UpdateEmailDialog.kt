package com.project.appealic.ui.view.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.project.appealic.databinding.DialogUpdateEmailBinding
import com.project.appealic.databinding.DialogUpdateGenderBinding
import com.project.appealic.ui.dialog.BaseUpdateProfileDialog


class UpdateEmailDialog : BaseUpdateProfileDialog<DialogUpdateEmailBinding>(
    DialogUpdateEmailBinding::class.java) {
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

}
