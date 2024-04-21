package com.project.appealic.ui.view.Fragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.project.appealic.data.model.User
import com.project.appealic.databinding.DialogUpdateGenderBinding
import com.project.appealic.ui.dialog.BaseUpdateProfileDialog

class UpdateGenderDialog : BaseUpdateProfileDialog<DialogUpdateGenderBinding>(DialogUpdateGenderBinding::class.java) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profileViewModel.updateGenderLiveData.observe(viewLifecycleOwner) { newGender ->
            if (newGender != null) {
                Toast.makeText(requireContext(), "Cập nhật giới tính thành công", Toast.LENGTH_SHORT).show()
                // Không cần cập nhật giao diện ở đây
            } else {
                Toast.makeText(requireContext(), "Cập nhật giới tính thất bại", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun setupViews() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val currentGender = currentUser?.let { getCurrentUser()?.gender } ?: ""

        binding.checkBoxSelectMale.isChecked = currentGender == "Male"
        binding.checkBoxSelectFemale.isChecked = currentGender == "Female"
        binding.checkBoxSelectOther.isChecked = currentGender == "Other"
    }

    override fun updateValue() {
        val newGender = when {
            binding.checkBoxSelectMale.isChecked -> "Male"
            binding.checkBoxSelectFemale.isChecked -> "Female"
            binding.checkBoxSelectOther.isChecked -> "Other"
            else -> return
        }

        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid != null) {
            profileViewModel.updateUserGender(uid, newGender)
        }
    }

    private fun getCurrentUser(): User? {
        val currentUser = FirebaseAuth.getInstance().currentUser
        return currentUser?.let {
            User(
                gender = it.displayName ?: "",
            )
        }
    }
}