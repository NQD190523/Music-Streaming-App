package com.project.appealic.ui.view.Fragment
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioButton
import androidx.fragment.app.Fragment
import com.project.appealic.R
import com.project.appealic.ui.view.ActivityRegister
import com.project.appealic.ui.view.BankTransfer

class StudentPaymentFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_payment_student, container, false)

        view.findViewById<Button>(R.id.btnPaymentCard).setOnClickListener {
            var amount = 0.0
            val oneMonthBtn = view.findViewById<RadioButton>(R.id.oneMonth)
            val threeMonthsBtn = view.findViewById<RadioButton>(R.id.threeMonths)
            val sixMonthsBtn = view.findViewById<RadioButton>(R.id.sixMonths)
            val twelveMonthsBtn = view.findViewById<RadioButton>(R.id.twelveMonths)
            if (oneMonthBtn.isChecked) {
                amount = 1.5
            } else if (threeMonthsBtn.isChecked) {
                amount = 4.5
            } else if (sixMonthsBtn.isChecked) {
                amount = 9.0
            } else if (twelveMonthsBtn.isChecked) {
                amount = 14.4
            }

            val intent = Intent(activity, BankTransfer::class.java)
            intent.putExtra("AMOUNT", amount)
            startActivity(intent)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btnPaymentMomo = view.findViewById<Button>(R.id.btnPaymentMomo)
        val btnPaymentCard = view.findViewById<Button>(R.id.btnPaymentCard)

        btnPaymentMomo.setOnClickListener {
            navigateToConfirmStudentFragment()
        }

        btnPaymentCard.setOnClickListener {
            navigateToConfirmStudentFragment()
        }
    }

    private fun navigateToConfirmStudentFragment() {
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        transaction?.replace(R.id.fragmenthome, ConfirmStudentFragment())
        transaction?.addToBackStack(null)
        transaction?.commit()
    }

    companion object {
        fun newInstance(): StudentPaymentFragment {
            return StudentPaymentFragment()
        }
    }

    class ConfirmStudentFragment : Fragment() {
        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val view = inflater.inflate(R.layout.fragment_confirm_student, container, false)

            val emailEditText = view.findViewById<EditText>(R.id.etEmail)
            val submitButton = view.findViewById<Button>(R.id.btnSubmit)

            submitButton.setOnClickListener {
                val email = emailEditText.text.toString().trim()
                if (isValidStudentEmail(email)) {
                    navigateToBankTransferActivity()
                } else {
                    navigateToSignUpActivity()
                }
            }

            return view
        }

        private fun isValidStudentEmail(email: String): Boolean {
            return email.contains("edu", ignoreCase = true)
        }

        private fun navigateToBankTransferActivity() {
            val intent = Intent(requireActivity(), BankTransfer::class.java)
            startActivity(intent)
        }

        private fun navigateToSignUpActivity() {
            val intent = Intent(requireActivity(), ActivityRegister::class.java)
            startActivity(intent)
        }
    }
}