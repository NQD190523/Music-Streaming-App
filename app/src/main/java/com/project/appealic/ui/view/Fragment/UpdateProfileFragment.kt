package com.project.appealic.ui.view.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.project.appealic.R
import com.project.appealic.ui.viewmodel.ProfileViewModel

class UpdateProfileFragment : Fragment(){
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_upgrade_account, container, false)
        setOnClickListeners(view)
        return view

    }

    private fun setOnClickListeners(view: View) {
        view.findViewById<ImageView>(R.id.btn_buy_now_solo).setOnClickListener {
            // Replace UpdateProfileFragment with ProfileFragment
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.fragmenthome, SoloPaymentFragment())
            transaction?.addToBackStack(null)
            transaction?.commit()
        }
        view.findViewById<ImageView>(R.id.btn_buy_now_student).setOnClickListener {
            // Replace UpdateProfileFragment with ProfileFragment
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.fragmenthome, StudentPaymentFragment())
            transaction?.addToBackStack(null)
            transaction?.commit()
        }
        view.findViewById<ImageView>(R.id.btn_buy_now_mini).setOnClickListener {
            // Replace UpdateProfileFragment with ProfileFragment
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.fragmenthome, MiniPaymentFragment())
            transaction?.addToBackStack(null)
            transaction?.commit()
        }
    }
}