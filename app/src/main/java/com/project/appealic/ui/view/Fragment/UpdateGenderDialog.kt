package com.project.appealic.ui.view.Fragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.project.appealic.databinding.DialogUpdateGenderBinding
import com.project.appealic.ui.dialog.BaseUpdateProfileDialog
import com.project.appealic.ui.viewmodel.ProfileViewModel


class UpdateGenderDialog :BaseUpdateProfileDialog<DialogUpdateGenderBinding>(DialogUpdateGenderBinding::class.java) {
    override fun setupViews() {
        binding.checkBoxSelectMale.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                Toast.makeText(requireContext(), "Male selected", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Male unselected", Toast.LENGTH_SHORT).show()
            }
        }

        binding.checkBoxSelectFemale.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                Toast.makeText(requireContext(), "Female selected", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Female unselected", Toast.LENGTH_SHORT).show()
            }
        }

        binding.checkBoxSelectOther.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                Toast.makeText(requireContext(), "Other selected", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Other unselected", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun updateValue() {
        val gender = when {
            binding.checkBoxSelectMale.isChecked -> "Male"
            binding.checkBoxSelectFemale.isChecked -> "Female"
            binding.checkBoxSelectOther.isChecked -> "Other"
            else -> null
        }
        if (gender != null) {
            val uid = FirebaseAuth.getInstance().currentUser?.uid
            if (uid != null) {
                profileViewModel.updateUserGender(uid, gender)
            }
        }
    }
}