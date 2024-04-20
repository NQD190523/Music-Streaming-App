package com.project.appealic.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.project.appealic.databinding.DialogUpdateNameBinding
import com.project.appealic.databinding.DialogUpdatePhoneBinding

class ChangeNameDialog :BaseUpdateProfileDialog<DialogUpdateNameBinding>(
    DialogUpdateNameBinding::class.java) {


    override fun setupViews() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        binding.edtNameProfile.setText(currentUser?.displayName)
    }

    override fun updateValue() {
        val newName = binding.edtNameProfile.text.toString().trim()
        if (newName.isNotBlank()) {
            val uid = FirebaseAuth.getInstance().currentUser?.uid
            if (uid != null) {
                profileViewModel.updateUserName(uid, newName)
            }
        }
    }
}