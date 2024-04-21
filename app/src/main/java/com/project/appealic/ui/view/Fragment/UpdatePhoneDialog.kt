package com.project.appealic.ui.view.Fragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.project.appealic.data.model.User
import com.project.appealic.databinding.DialogUpdatePhoneBinding
import com.project.appealic.ui.dialog.BaseUpdateProfileDialog

class UpdatePhoneDialog : BaseUpdateProfileDialog<DialogUpdatePhoneBinding>(
    DialogUpdatePhoneBinding::class.java
) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profileViewModel.updatePhoneLiveData.observe(viewLifecycleOwner) { newPhone ->
            if (newPhone != null) {
                Toast.makeText(requireContext(), "Cập nhật số điện thoại thành công", Toast.LENGTH_SHORT).show()
                binding.edtPhoneProfile.setText(newPhone)
            } else {
                Toast.makeText(requireContext(), "Cập nhật số điện thoại thất bại", Toast.LENGTH_SHORT).show()
            }
        }
    }

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

    private fun getCurrentUser(): User? {
        val currentUser = FirebaseAuth.getInstance().currentUser
        return currentUser?.let {
            User(
                phone = it.phoneNumber ?: "",
            )
        }
    }
}