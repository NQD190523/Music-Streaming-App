package com.project.appealic.ui.view.Fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.project.appealic.R
import com.project.appealic.databinding.ActivityEditAccountBinding
import com.project.appealic.databinding.DialogMembershipMiniBinding
import com.project.appealic.databinding.DialogMembershipSoloBinding
import com.project.appealic.databinding.DialogMembershipStudentBinding
import com.project.appealic.databinding.FragmentProfileBinding

class ProfileFragment : Fragment(){
    private var _binding: FragmentProfileBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListeners()

    }
        private fun setOnClickListeners() {
            binding.soloCard.setOnClickListener {
                showDialog(MembershipSoloDialog())
            }

            binding.miniCard.setOnClickListener {
                showDialog(MembershipMiniDialog())
            }

            binding.studentCard.setOnClickListener {
                showDialog(MembershipStudentDialog())
            }

        }
    private fun showDialog(dialog: DialogFragment) {
        dialog.show(parentFragmentManager, dialog::class.simpleName)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


class MembershipMiniDialog : DialogFragment() {
    private var _binding: DialogMembershipMiniBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DialogMembershipMiniBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Thêm các sự kiện cần thiết cho dialog_membership_mini tại đây
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

class MembershipStudentDialog : DialogFragment() {
    private var _binding: DialogMembershipStudentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DialogMembershipStudentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Thêm các sự kiện cần thiết cho dialog_membership_student tại đây
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


class MembershipSoloDialog : DialogFragment() {
    private var _binding: DialogMembershipSoloBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DialogMembershipSoloBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Giả sử bạn muốn thêm sự kiện click vào một ImageView trong dialog
        // Ví dụ: binding.yourImageViewId.setOnClickListener {
        //     // Thực hiện hành động khi nhấn vào ImageView
        // }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

