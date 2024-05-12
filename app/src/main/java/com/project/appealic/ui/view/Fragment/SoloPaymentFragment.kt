package com.project.appealic.ui.view.Fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridView
import android.widget.RadioButton
import androidx.fragment.app.Fragment
import com.project.appealic.R
import com.project.appealic.ui.view.Adapters.PlaylistAdapter
import com.project.appealic.ui.view.BankTransfer

class SoloPaymentFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_payment_solo, container, false)

        // Lấy số tiền khi người dùng chọn radio button và chuyển sang BankTransferActivity
        view.findViewById<Button>(R.id.btnPaymentCard).setOnClickListener {
            // Lấy số tiền dựa vào radio button được chọn
            var amount = 0.0

            // Kiểm tra xem radio button nào được chọn
            val oneMonthBtn = view.findViewById<RadioButton>(R.id.oneMonth)
            val threeMonthsBtn = view.findViewById<RadioButton>(R.id.threeMonths)
            val sixMonthsBtn = view.findViewById<RadioButton>(R.id.sixMonths)
            val twelveMonthsBtn = view.findViewById<RadioButton>(R.id.twelveMonths)
            if (oneMonthBtn.isChecked) {
                amount = 2.5 // Giả sử số tiền cho 1 ngày là 1
            } else if (threeMonthsBtn.isChecked) {
                amount = 7.5 // Giả sử số tiền cho 1 tuần là 7
            } else if (sixMonthsBtn.isChecked) {
                amount = 15.0 // Giả sử số tiền cho 1 tuần là 7
            } else if (twelveMonthsBtn.isChecked) {
                amount = 24.0 // Giả sử số tiền cho 1 tuần là 7
            }

            // Tạo Intent để chuyển sang BankTransferActivity
            val intent = Intent(activity, BankTransfer::class.java)
            // Truyền dữ liệu amount thông qua Intent
            intent.putExtra("AMOUNT", amount)
            // Bắt đầu Activity mới
            startActivity(intent)
        }

        return view
    }
}