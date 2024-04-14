package com.project.appealic.ui.view.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.project.appealic.R

class ProfileFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        setOnClickListeners(view)
        return view
    }

    private fun setOnClickListeners(view: View) {
        view.findViewById<ImageView>(R.id.solo_card).setOnClickListener {
            showDialog(MembershipSoloDialog())
        }

        view.findViewById<ImageView>(R.id.mini_card).setOnClickListener {
            showDialog(MembershipMiniDialog())
        }

        view.findViewById<ImageView>(R.id.student_card).setOnClickListener {
            showDialog(MembershipStudentDialog())
        }
    }

    private fun showDialog(dialog: DialogFragment) {
        val tag = dialog::class.java.simpleName
        if (childFragmentManager.findFragmentByTag(tag) == null) {
            if (dialog is MembershipMiniDialog) {
                dialog.listener = object : MembershipMiniDialog.OnBuyNowClickedListener {
                    override fun onBuyNowClicked() {
                        // Replace ProfileFragment with PaymentFragment
                        val transaction = activity?.supportFragmentManager?.beginTransaction()
                        transaction?.replace(R.id.fragmenthome, MiniPaymentFragment())
                        transaction?.addToBackStack(null)
                        transaction?.commit()
                    }
                }
            }
            dialog.show(childFragmentManager, tag)
        }
    }

    class MembershipMiniDialog : DialogFragment() {
        interface OnBuyNowClickedListener {
            fun onBuyNowClicked()
        }

        var listener: OnBuyNowClickedListener? = null

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val view = inflater.inflate(R.layout.dialog_membership_mini, container, false)
            view.findViewById<Button>(R.id.btn_buy_now_mini).setOnClickListener {
                listener?.onBuyNowClicked()
                dismiss()
            }
            return view
        }
    }
    class MembershipStudentDialog : DialogFragment() {
        // Remove unnecessary code
    }

    class MembershipSoloDialog : DialogFragment() {
        // Remove unnecessary code
    }
}