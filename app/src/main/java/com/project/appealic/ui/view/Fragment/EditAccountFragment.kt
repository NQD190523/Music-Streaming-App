package com.project.appealic.ui.view.Fragment

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.google.firebase.auth.FirebaseAuth
import com.project.appealic.R
import com.project.appealic.databinding.FragmentEditAccountBinding
import com.project.appealic.ui.dialog.BaseUpdateProfileDialog
import com.project.appealic.ui.dialog.ChangeNameDialog
import com.project.appealic.ui.viewmodel.ProfileViewModel
import java.io.InputStream

class EditAccountFragment : Fragment() {

        private val PICK_IMAGE = 1
        private lateinit var binding: FragmentEditAccountBinding
        private lateinit var profileViewModel: ProfileViewModel

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
        ): View {
            binding = FragmentEditAccountBinding.inflate(inflater, container, false)
            return binding.root
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            profileViewModel = ViewModelProvider(this)[ProfileViewModel::class.java]
            setupInitialViews()
            setOnClickListeners()

            val uid = FirebaseAuth.getInstance().currentUser?.uid
            if (uid != null) {
                profileViewModel.getUserProfile(uid)
            }

            profileViewModel.userLiveData.observe(viewLifecycleOwner) { user ->
                if (user != null) {
                    user.name.also { binding.txtProfileName.text = it }
                }
                if (user != null) {
                    user.email.also { it.also { binding.txtProfileEmail.text = it } }
                }
                if (user != null) {
                    user.phone.toString().also { binding.txtProfilePhone.text = it }
                }
                if (user != null) {
                    user.day_of_birth.also { binding.txtProfileDob.text = it }
                }
                if (user != null) {
                    user.gender.also { binding.txtProfileGender.text = it }
                }
            }
        }

        private fun setupInitialViews() {
            val user = profileViewModel.getCurrentUser()
            if (user != null) {
                binding.txtProfileName.text = user.name
            }
            if (user != null) {
                binding.txtProfileEmail.text = user.email
            }
        }

        private fun setOnClickListeners() {
            binding.btnChangeImg.setOnClickListener {
                val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(intent, PICK_IMAGE)
            }

            binding.llName.setOnClickListener { showDialog(ChangeNameDialog()) }
            binding.llEmail.setOnClickListener { showDialog(UpdateEmailDialog()) }
            binding.llPhone.setOnClickListener { showDialog(UpdatePhoneDialog()) }
            binding.llDob.setOnClickListener { showDialog(UpdateDOBDialog()) }
            binding.llGender.setOnClickListener { showDialog(UpdateGenderDialog()) }

            binding.imvBack.setOnClickListener {
                val transaction = activity?.supportFragmentManager?.beginTransaction()
                transaction?.replace(R.id.fragmenthome, ProfileFragment())
                transaction?.addToBackStack(null)
                transaction?.commit()
            }
        }

        private fun <T : ViewBinding> showDialog(dialogFragment: BaseUpdateProfileDialog<T>) {
            dialogFragment.show(childFragmentManager, dialogFragment.tag)
        }

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)
            if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
                val selectedImage: Uri? = data.data
                val imageStream: InputStream? = selectedImage?.let {
                    activity?.contentResolver?.openInputStream(it)
                }
                imageStream?.use {
                    val bitmap: Bitmap = BitmapFactory.decodeStream(it)
                    binding.circleImageView2.setImageBitmap(bitmap)
                }
            }
        }
    }

