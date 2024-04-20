package com.project.appealic.ui.view.Fragment


import com.google.firebase.auth.FirebaseAuth
import com.project.appealic.databinding.DialogUpdateDobBinding
import com.project.appealic.ui.dialog.BaseUpdateProfileDialog

class UpdateDOBDialog : BaseUpdateProfileDialog<DialogUpdateDobBinding>(DialogUpdateDobBinding::class.java) {

    override fun setupViews() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        binding.edtDobProfile.setText(currentUser?.displayName)
    }

    override fun updateValue() {
        val newDOB = binding.edtDobProfile.text.toString().trim()
        if (newDOB.isNotBlank()) {
            val uid = FirebaseAuth.getInstance().currentUser?.uid
            if (uid != null) {
                profileViewModel.updateUserEmail(uid, newDOB)
            }
        }
    }


}
