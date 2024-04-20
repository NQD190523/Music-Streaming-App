package com.project.appealic.ui.view.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.project.appealic.databinding.DialogUpdateGenderBinding
import com.project.appealic.databinding.DialogUpdatePhoneBinding
import com.project.appealic.ui.dialog.BaseUpdateProfileDialog

class UpdatePhoneDialog :BaseUpdateProfileDialog<DialogUpdatePhoneBinding>(
    DialogUpdatePhoneBinding::class.java) {

    override fun setupViews() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        binding.edtPhoneProfile.setText(currentUser?.phoneNumber)
    }

    override fun updateValue() {
        val newPhone = binding.edtPhoneProfile.text.toString().trim()
        if (newPhone.isNotBlank()) {
            val uid = FirebaseAuth.getInstance().currentUser?.uid
            if (uid != null) {
                profileViewModel.updateUserPhone(uid, newPhone)
            }
        }
    }

}
