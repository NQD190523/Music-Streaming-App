package com.project.appealic.ui.view.Fragment

import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.project.appealic.R

class AddPlaylistFragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_playlist, container, false)
    }
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            val dialog = Dialog(requireActivity())

            // Khai báo dialog không có tiêu đề
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

            // Gán background cho dialog
            dialog.window?.setBackgroundDrawableResource(R.drawable.background)

            // Tạo và gán layout cho dialog
            val view = LayoutInflater.from(context).inflate(R.layout.fragment_add_playlist, null)
            dialog.setContentView(view)

            // Tùy chỉnh cài đặt của Window
            val window = dialog.window
            window?.setBackgroundDrawableResource(R.drawable.radius_background)
            val layoutParams = window?.attributes
            layoutParams?.gravity = Gravity.BOTTOM or Gravity.START or Gravity.END
            layoutParams?.width = WindowManager.LayoutParams.MATCH_PARENT
            layoutParams?.height = WindowManager.LayoutParams.WRAP_CONTENT
            window?.attributes = layoutParams

            return dialog
        }
    }
