package com.project.appealic.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.project.appealic.data.model.User
import com.project.appealic.databinding.DialogUpdateNameBinding
import com.project.appealic.databinding.DialogUpdatePhoneBinding

class ChangeNameDialog : BaseUpdateProfileDialog<DialogUpdateNameBinding>(
    DialogUpdateNameBinding::class.java
) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profileViewModel.updateNameLiveData.observe(viewLifecycleOwner) { newName ->
            if (newName != null) {
                Toast.makeText(requireContext(), "Cập nhật tên thành công", Toast.LENGTH_SHORT).show()
                binding.edtNameProfile.setText(newName)
            } else {
                Toast.makeText(requireContext(), "Cập nhật tên thất bại", Toast.LENGTH_SHORT).show()
            }
        }
    }

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

    private fun getCurrentUser(): User? {
        val currentUser = FirebaseAuth.getInstance().currentUser
        return currentUser?.let {
            User(
                name = it.displayName ?: "",
            )
        }
    }
}