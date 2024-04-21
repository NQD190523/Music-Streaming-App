// BaseUpdateProfileDialog.kt
package com.project.appealic.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.project.appealic.R
import com.project.appealic.databinding.DialogBaseUpdateProfileBinding
import com.project.appealic.ui.viewmodel.ProfileViewModel

abstract class BaseUpdateProfileDialog<T : ViewBinding>(private val bindingClass: Class<T>) : DialogFragment() {
    private var _binding: T? = null
    protected val binding get() = _binding ?: throw IllegalStateException("Binding is null")
    protected lateinit var profileViewModel: ProfileViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = bindingClass.getMethod("inflate", LayoutInflater::class.java, ViewGroup::class.java, Boolean::class.java)
            .invoke(null, inflater, container, false) as T
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        profileViewModel = ViewModelProvider(requireActivity()).get(ProfileViewModel::class.java)
        setupViews()
        setOnClickListeners()
    }

    abstract fun setupViews()

    abstract fun updateValue()

    private fun setOnClickListeners() {
        val btnUpdate = binding.root.findViewById<View>(R.id.btnUpdate)
        val imvCancel = binding.root.findViewById<View>(R.id.imvCancel)
        val imvCancel1 = binding.root.findViewById<View>(R.id.imvCancel1)
        val editText = binding.root.findViewById<EditText>(R.id.editText)

        btnUpdate?.setOnClickListener {
            updateValue()
            dismiss()
        }

        imvCancel?.setOnClickListener {
            dismiss()
        }

        imvCancel1?.setOnClickListener {
            editText?.text = null
        }
    }
    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val width = 315
            val height = 285
            val params = dialog.window?.attributes
            params?.width = (width * resources.displayMetrics.density).toInt()
            params?.height = (height * resources.displayMetrics.density).toInt()
            dialog.window?.attributes = params as WindowManager.LayoutParams

            val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.bg_update_profile)
            dialog.window?.setBackgroundDrawable(drawable)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}