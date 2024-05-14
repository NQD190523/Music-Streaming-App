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

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnPaymentMomo = view.findViewById<Button>(R.id.btnPaymentMomo)
        btnPaymentMomo.setOnClickListener {
            val amount = getSelectedAmount(view)
            navigateToConfirmStudentFragment(amount)
        }
        val btnPaymentCard = view.findViewById<Button>(R.id.btnPaymentCard)
        btnPaymentCard.setOnClickListener {
            val amount = getSelectedAmount(view)
            navigateToConfirmStudentFragment(amount)
        }

    }

    private fun getSelectedAmount(view: View): Double {
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

        return amount
    }

    private fun navigateToConfirmStudentFragment(amount: Double) {
        val bundle = Bundle().apply {
            putDouble("AMOUNT", amount)
        }
        val confirmStudentFragment = ConfirmStudentFragment.newInstance(bundle)
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        transaction?.replace(R.id.fragmenthome, confirmStudentFragment)
        transaction?.addToBackStack(null)
        transaction?.commit()
    }

    private fun navigateToBankTransferActivity(amount: Double) {
        val intent = Intent(requireActivity(), BankTransfer::class.java)
        intent.putExtra("AMOUNT", amount)
        startActivity(intent)
    }

    companion object {
        fun newInstance(): StudentPaymentFragment {
            return StudentPaymentFragment()
        }
    }
}