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
import android.view.WindowManager
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.project.appealic.R
import com.project.appealic.ui.viewmodel.ProfileViewModel
import java.io.InputStream

class EditAccountFragment : Fragment() {
    private val PICK_IMAGE = 1
    private lateinit var profileImageView: ImageView
    private lateinit var profileViewModel: ProfileViewModel
    private var uid = FirebaseAuth.getInstance().currentUser?.uid

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_edit_account, container, false)
        profileImageView = view.findViewById(R.id.circleImageView2)
        profileViewModel = ViewModelProvider(this)[ProfileViewModel::class.java]

        val user = uid?.let { profileViewModel.getUserInfo(it) }
        view.findViewById<TextView>(R.id.txtProfileName).text = user?.name
        println(user?.name)
        view.findViewById<TextView>(R.id.txtProfileName).text = user?.email

        setOnClickListeners(view)

        view.findViewById<ImageView>(R.id.imv_back).setOnClickListener {
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.fragmenthome, ProfileFragment())
            transaction?.addToBackStack(null)
            transaction?.commit()
        }

        return view
    }

    private fun setOnClickListeners(view: View?) {
        view?.findViewById<View>(R.id.btnChangeImg)?.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE)
        }
//        show dialog change name
        view?.findViewById<ConstraintLayout>(R.id.ll_name)?.setOnClickListener {
            showDialog(ChangeNameDialog())
        }
//        show dialog change email
        view?.findViewById<ConstraintLayout>(R.id.ll_email)?.setOnClickListener {
            showDialog(UpdateEmailDialogFragment())
        }
//        show dialog change phone
        view?.findViewById<ConstraintLayout>(R.id.ll_phone)?.setOnClickListener {
            showDialog(UpdatePhoneDialogFragment())
        }
//        show dialog change dob
        view?.findViewById<ConstraintLayout>(R.id.ll_dob)?.setOnClickListener {
            showDialog(UpdateDOBDialogFragment())
        }
//        show dialog gender
        view?.findViewById<ConstraintLayout>(R.id.ll_gender)?.setOnClickListener {
            showDialog(UpdateGenderDialogFragment())
        }
    }


    private fun showDialog(dialogFragment: DialogFragment) {
        val fragmentManager = childFragmentManager // Use childFragmentManager if inside a Fragment
        dialogFragment.show(fragmentManager, dialogFragment.tag)
    }
    class UpdateGenderDialogFragment : DialogFragment() {
        // function choose gender
        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            return inflater.inflate(R.layout.dialog_update_gender, container, false)
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            // Tìm kiếm CheckBox trong layout
            val checkBoxMale = view.findViewById<CheckBox>(R.id.checkBoxSelectMale)
            val checkBoxFemale = view.findViewById<CheckBox>(R.id.checkBoxSelectFemale)
            val checkBoxOther = view.findViewById<CheckBox>(R.id.checkBoxSelectOther)

            checkBoxMale.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    Toast.makeText(requireContext(), "Male selected", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Male unselected", Toast.LENGTH_SHORT).show()
                }
            }

            checkBoxFemale.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    Toast.makeText(requireContext(), "Female selected", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Female unselected", Toast.LENGTH_SHORT).show()
                }
            }

            checkBoxOther.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    Toast.makeText(requireContext(), "Other selected", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Other unselected", Toast.LENGTH_SHORT).show()
                }
            }
        }

        override fun onStart() {
            super.onStart()

            val dialog = dialog
            if (dialog != null) {
                // Đặt kích thước dialog
                val width = 315 // Kích thước dp
                val height = 315 // Kích thước dp
                val params = dialog.window?.attributes
                params?.width = (width * resources.displayMetrics.density).toInt()
                params?.height = (height * resources.displayMetrics.density).toInt()
                dialog.window?.attributes = params as WindowManager.LayoutParams

                // Thiết lập background cho dialog
                val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.bg_update_profile)
                dialog.window?.setBackgroundDrawable(drawable)
            }
        }
    }

    class UpdateDOBDialogFragment : DialogFragment() {
        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            val view = inflater.inflate(R.layout.dialog_update_dob, container, false)
            setOnClickListeners(view)
            return view
        }

        private fun setOnClickListeners(view: View) {
            val btnUpdate = view.findViewById<View>(R.id.btnUpdate)
            val imgBtnCancel = view.findViewById<View>(R.id.imvCancel1)
            val cancelButton = view.findViewById<View>(R.id.imvCancel)
            dialog?.setCanceledOnTouchOutside(true)
            cancelButton.setOnClickListener {
                dismiss()
            }

            val edtDOB = view.findViewById<View>(R.id.edtDobProfile)

            btnUpdate.setOnClickListener {
                val dob = edtDOB.toString()
                // Add your logic to handle the update dob event here
                //...

                dismiss()
            }

            imgBtnCancel.setOnClickListener {
                edtDOB.toString()
            }
        }

            override fun onStart() {
                super.onStart()

                val dialog = dialog
                if (dialog != null) {
                    // Đặt kích thước dialog
                    val width = 315 // Kích thước dp
                    val height = 285 // Kích thước dp
                    val params = dialog.window?.attributes
                    params?.width = (width * resources.displayMetrics.density).toInt()
                    params?.height = (height * resources.displayMetrics.density).toInt()
                    dialog.window?.attributes = params as WindowManager.LayoutParams

                    // Thiết lập background cho dialog
                    val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.bg_update_profile)
                    dialog.window?.setBackgroundDrawable(drawable)
                }
            }


    }

    class UpdatePhoneDialogFragment : DialogFragment() {
        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            val view = inflater.inflate(R.layout.dialog_update_phone, container, false)
            setOnClickListeners(view)
            return view
        }

        private fun setOnClickListeners(view: View) {
            val btnUpdate = view.findViewById<View>(R.id.btnUpdate)
            val imgBtnCancel = view.findViewById<View>(R.id.imvCancel1)
            val cancelButton = view.findViewById<View>(R.id.imvCancel)
            dialog?.setCanceledOnTouchOutside(true)
            cancelButton.setOnClickListener {
                dismiss()
            }

            val edtPhone = view.findViewById<View>(R.id.edtPhoneProfile)

            btnUpdate.setOnClickListener {
                val phone = edtPhone.toString()
                // Add your logic to handle the update phone event here
                //...

                dismiss()
            }

            imgBtnCancel.setOnClickListener {
                edtPhone.toString()
            }
        }

        override fun onStart() {
            super.onStart()
            val dialog = dialog
            if (dialog != null) {
                // Đặt kích thước dialog
                val width = 315 // Kích thước dp
                val height = 285 // Kích thước dp
                val params = dialog.window?.attributes
                params?.width = (width * resources.displayMetrics.density).toInt()
                params?.height = (height * resources.displayMetrics.density).toInt()
                dialog.window?.attributes = params as WindowManager.LayoutParams

                // Thiết lập background cho dialog
                val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.bg_update_profile)
                dialog.window?.setBackgroundDrawable(drawable)
            }
        }

    }

    class UpdateEmailDialogFragment : DialogFragment() {
        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            val view = inflater.inflate(R.layout.dialog_update_email, container, false)
            setOnClickListeners(view)
            return view
        }

        private fun setOnClickListeners(view: View) {
            val btnUpdate = view.findViewById<View>(R.id.btnUpdate)
            val imgBtnCancel = view.findViewById<View>(R.id.imvCancel1)
            val cancelButton = view.findViewById<View>(R.id.imvCancel)
            dialog?.setCanceledOnTouchOutside(true)
            cancelButton.setOnClickListener {
                dismiss()
            }

            val edtNewEmail = view.findViewById<View>(R.id.edtEmailProfile)

            btnUpdate.setOnClickListener {
                val newEmail = edtNewEmail.toString()
                // Add your logic to handle the update email event here
                //...

                dismiss()
            }

            imgBtnCancel.setOnClickListener {
                edtNewEmail.toString()
            }
        }

        override fun onStart() {
            super.onStart()

            val dialog = dialog
            if (dialog != null) {
                // Đặt kích thước dialog
                val width = 315 // Kích thước dp
                val height = 285 // Kích thước dp
                val params = dialog.window?.attributes
                params?.width = (width * resources.displayMetrics.density).toInt()
                params?.height = (height * resources.displayMetrics.density).toInt()
                dialog.window?.attributes = params as WindowManager.LayoutParams

                // Thiết lập background cho dialog
                val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.bg_update_profile)
                dialog.window?.setBackgroundDrawable(drawable)
            }
        }

    }

    class ChangeNameDialog : DialogFragment() {
        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            val view = inflater.inflate(R.layout.dialog_update_name, container, false)
            setOnClickListeners(view)
            return view
        }

        private fun setOnClickListeners(view: View) {
            val btnUpdate = view.findViewById<View>(R.id.btnUpdate)
            val imgBtnCancel = view.findViewById<View>(R.id.imvCancel1)
            val cancelButton = view.findViewById<View>(R.id.imvCancel)
            dialog?.setCanceledOnTouchOutside(true)
            cancelButton.setOnClickListener {
                dismiss()
            }

            val edtName = view.findViewById<View>(R.id.edtNameProfile)

            btnUpdate.setOnClickListener {
                val edtName = edtName.toString()
                // Add your logic to handle the update password event here
                //...

                dismiss()
            }

            imgBtnCancel.setOnClickListener {
                edtName.toString()
            }

        }

        override fun onStart() {
            super.onStart()

            // Cóp khúc này vô là đẹp
            val dialog = dialog
            if (dialog != null) {
                // Đặt kích thước dialog
                val width = 315 // Kích thước dp
                val height = 285 // Kích thước dp
                val params = dialog.window?.attributes
                params?.width = (width * resources.displayMetrics.density).toInt()
                params?.height = (height * resources.displayMetrics.density).toInt()
                dialog.window?.attributes = params as WindowManager.LayoutParams

                // Thiết lập background cho dialog
                val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.bg_update_profile)
                dialog.window?.setBackgroundDrawable(drawable)
            }
        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            val selectedImage: Uri? = data.data
            val imageStream: InputStream? = selectedImage?.let { activity?.contentResolver?.openInputStream(it) }
            val bitmap: Bitmap = BitmapFactory.decodeStream(imageStream)
            profileImageView.setImageBitmap(bitmap)
        }

    }
}