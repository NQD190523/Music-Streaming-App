package com.project.appealic.ui.view.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import com.project.appealic.R

class UpdatePassDialogFragment : DialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dialog_update_profile, container, false)

        setOnClickListeners(view)
        return view
    }

    private fun setOnClickListeners(view: View) {
        val btnUpdate = view.findViewById<Button>(R.id.btnUpdate)
        val imgBtnCancel = view.findViewById<ImageView>(R.id.imvCancel1)
        val imgBtnCancelPass = view.findViewById<ImageView>(R.id.imvCancel2)
        val cancelButton = view.findViewById<ImageButton>(R.id.imvCancel)
//        Tắt Dialog khi click ra ngoài
        dialog?.setCanceledOnTouchOutside(true)
//        Tắt Dialog khi click Image Cancel
        cancelButton.setOnClickListener {
            dismiss()
        }

        val edtOldPass = view.findViewById<EditText>(R.id.edtOldPass)
        val edtNewPass = view.findViewById<EditText>(R.id.edtNewPass)

        btnUpdate.setOnClickListener {
            val oldPass = edtOldPass.text.toString()
            val newPass = edtNewPass.text.toString()

            // Add your logic to handle the update password event here
            //...

            dismiss()
        }

        imgBtnCancel.setOnClickListener {
            edtOldPass.setText("")
        }

        imgBtnCancelPass.setOnClickListener {
            edtNewPass.setText("")
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