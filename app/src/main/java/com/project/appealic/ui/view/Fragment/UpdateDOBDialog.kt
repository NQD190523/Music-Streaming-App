package com.project.appealic.ui.view.Fragment


import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.project.appealic.data.model.User
import com.project.appealic.databinding.DialogUpdateDobBinding
import com.project.appealic.ui.dialog.BaseUpdateProfileDialog

class UpdateDOBDialog : BaseUpdateProfileDialog<DialogUpdateDobBinding>(
    DialogUpdateDobBinding::class.java
) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profileViewModel.updateDOBLiveData.observe(viewLifecycleOwner) { newDob ->
            if (newDob != null) {
                Toast.makeText(requireContext(), "Cập nhật ngày sinh thành công", Toast.LENGTH_SHORT).show()
                binding.edtDobProfile.setText(newDob)
            } else {
                Toast.makeText(requireContext(), "Cập nhật ngày sinh thất bại", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun setupViews() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        binding.edtDobProfile.setText(currentUser?.metadata?.creationTimestamp.toString())
    }

    override fun updateValue() {
        val newDOB = binding.edtDobProfile.text.toString().trim()
        if (newDOB.isNotBlank()) {
            val uid = FirebaseAuth.getInstance().currentUser?.uid
            if (uid != null) {
                profileViewModel.updateUserDOB(uid, newDOB)
            }
        }
    }

    private fun getCurrentUser(): User? {
        val currentUser = FirebaseAuth.getInstance().currentUser
        return currentUser?.let {
            User(
                day_of_birth = it.metadata?.creationTimestamp.toString() ?: "",

            )
        }
    }
}