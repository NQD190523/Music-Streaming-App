package com.project.appealic.ui.view.Fragment

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.project.appealic.R
import com.project.appealic.databinding.BottomSleepBinding

class SleepFragmentDialog : DialogFragment() {
    private lateinit var binding: BottomSleepBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomSleepBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.mins3.setOnClickListener {
            listener?.onSleepTimeSelected(180)
            dismiss()
        }
        binding.mins10.setOnClickListener {
            listener?.onSleepTimeSelected(600)
            dismiss()
        }
        binding.mins15.setOnClickListener {
            listener?.onSleepTimeSelected(900)
            dismiss()
        }
        binding.mins30.setOnClickListener {
            listener?.onSleepTimeSelected(1800)
            dismiss()
        }
        binding.mins45.setOnClickListener {
            listener?.onSleepTimeSelected(2700)
            dismiss()
        }
        binding.mins60.setOnClickListener {
            listener?.onSleepTimeSelected(3600)
            dismiss()
        }
        binding.endofsong.setOnClickListener {
            listener?.onSleepTimeSelected(-1)
            dismiss()
        }
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            window?.setBackgroundDrawableResource(R.drawable.more_background)
            window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            window?.setGravity(Gravity.BOTTOM or Gravity.START or Gravity.END)
        }
    }

    interface OnSleepTimeSelectedListener {
        fun onSleepTimeSelected(seconds: Long)
    }

    var listener: OnSleepTimeSelectedListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnSleepTimeSelectedListener) {
            listener = context
        }
    }
}