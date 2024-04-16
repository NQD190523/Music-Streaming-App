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
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.project.appealic.R
import java.io.InputStream

class EditAccountFragment : Fragment() {

    private val PICK_IMAGE = 1
    private lateinit var profileImageView: ImageView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_edit_account, container, false)
        profileImageView = view.findViewById(R.id.circleImageView2)

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
//        show dialog change password
        view?.findViewById<ConstraintLayout>(R.id.ll_password)?.setOnClickListener {
            showDialog(UpdatePassDialogFragment())
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
//        function choose gender
        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            val view = inflater.inflate(R.layout.dialog_update_gender, container, false)
            setOnClickListeners(view)
            return view

    }

        private fun setOnClickListeners(view: View?) {
//            choose gender
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
                val width = ViewGroup.LayoutParams.MATCH_PARENT
                val height = ViewGroup.LayoutParams.WRAP_CONTENT
                dialog.window?.setLayout(width, height)
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
                val width = ViewGroup.LayoutParams.MATCH_PARENT
                val height = ViewGroup.LayoutParams.WRAP_CONTENT
                dialog.window?.setLayout(width, height)
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

            val edtNewEmail = view.findViewById<View>(R.id.edtNewEmail)

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
                val width = ViewGroup.LayoutParams.MATCH_PARENT
                val height = ViewGroup.LayoutParams.WRAP_CONTENT
                dialog.window?.setLayout(width, height)
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

            val dialog = dialog
            if (dialog != null) {
                val width = ViewGroup.LayoutParams.MATCH_PARENT
                val height = ViewGroup.LayoutParams.WRAP_CONTENT
                dialog.window?.setLayout(width, height)
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