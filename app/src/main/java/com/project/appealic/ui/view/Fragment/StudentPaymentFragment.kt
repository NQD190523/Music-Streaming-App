package com.project.appealic.ui.view.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.project.appealic.R

class StudentPaymentFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_payment_student, container, false)

        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btnPaymentMomo = view.findViewById<Button>(R.id.btnPaymentMomo)
        val btnPaymentCard = view.findViewById<Button>(R.id.btnPaymentCard)

        // Thiết lập sự kiện click cho các nút
        btnPaymentMomo.setOnClickListener {
            navigateToConfirmStudentFragment()
        }

        btnPaymentCard.setOnClickListener {
            navigateToConfirmStudentFragment()
        }
    }

    private fun navigateToConfirmStudentFragment() {

        // Khởi tạo một FragmentTransaction
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        transaction?.replace(R.id.fragmenthome, ConfirmStudentFragment())
        transaction?.addToBackStack(null)
        transaction?.commit()
    }

    class ConfirmStudentFragment : Fragment() {
        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            // Inflate the layout for this fragment
            return inflater.inflate(R.layout.fragment_confirm_student, container, false)

    }
}
}