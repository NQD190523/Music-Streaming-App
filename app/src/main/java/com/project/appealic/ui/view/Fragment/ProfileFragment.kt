package com.project.appealic.ui.view.Fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.project.appealic.R
import com.project.appealic.ui.view.GoogleLoginActivity
import com.project.appealic.ui.view.Fragment.EditAccountFragment
import com.project.appealic.ui.viewmodel.AuthViewModel

class ProfileFragment : Fragment() {
    private lateinit var authViewModel : AuthViewModel
    private lateinit var googleSignInClient: GoogleSignInClient
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        setOnClickListeners(view)
        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireActivity(),gso)

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
        view.findViewById<ImageView>(R.id.imageUpdate).setOnClickListener {
            // Replace ProfileFragment with UpdateProfileFragment
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.fragmenthome, UpdateProfileFragment())
            transaction?.addToBackStack(null)
            transaction?.commit()
        }
        view.findViewById<ConstraintLayout>(R.id.ll_Profile)
            .setOnClickListener(View.OnClickListener {
                // Replace ProfileFragment with UpdateProfileFragment
                val transaction = activity?.supportFragmentManager?.beginTransaction()
                transaction?.replace(R.id.fragmenthome, EditAccountFragment())
                transaction?.addToBackStack(null)
                transaction?.commit()
            })
//        Hiện dialog fragment contact
        view.findViewById<ConstraintLayout>(R.id.ll_Contact)
            .setOnClickListener(View.OnClickListener {
                showDialog(ContactFragmentDialog())
            })
        view.findViewById<ConstraintLayout>(R.id.ll_Legal).setOnClickListener(View.OnClickListener {
            showDialog(LegalFragmentDialog())
        })

//        Dialog change pass word
        view.findViewById<ConstraintLayout>(R.id.ll_ChangePassword)
            .setOnClickListener(View.OnClickListener {
                showDialog(UpdatePassDialogFragment())

            })

//        logout
        view.findViewById<Button>(R.id.btnSignout).setOnClickListener {
            authViewModel.signOut(googleSignInClient)
            val intent = Intent(requireContext(),GoogleLoginActivity::class.java)
            startActivity(intent)
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
            if (dialog is MembershipStudentDialog) {
                dialog.listener = object : MembershipStudentDialog.OnBuyNowClickedListener {
                    override fun onBuyNowClicked() {
                        // Replace ProfileFragment with PaymentFragment
                        val transaction = activity?.supportFragmentManager?.beginTransaction()
                        transaction?.replace(R.id.fragmenthome, StudentPaymentFragment())
                        transaction?.addToBackStack(null)
                        transaction?.commit()
                    }
                }
            }
            if (dialog is MembershipSoloDialog) {
                dialog.listener = object : MembershipSoloDialog.OnBuyNowClickedListener {
                    override fun onBuyNowClicked() {
                        // Replace ProfileFragment with PaymentFragment
                        val transaction = activity?.supportFragmentManager?.beginTransaction()
                        transaction?.replace(R.id.fragmenthome, SoloPaymentFragment())
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
            val imageView = view.findViewById<ImageView>(R.id.imvBack)
            imageView.setOnClickListener {
                dismiss()
            }

            view.findViewById<ImageView>(R.id.imvBack).setOnClickListener {
                dismiss()
            }
            return view
        }
    }

    class MembershipStudentDialog : DialogFragment() {
        interface OnBuyNowClickedListener {
            fun onBuyNowClicked()
        }

        var listener: OnBuyNowClickedListener? = null

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val view = inflater.inflate(R.layout.dialog_membership_student, container, false)
            view.findViewById<Button>(R.id.btn_buy_now_student).setOnClickListener {
                listener?.onBuyNowClicked()
                dismiss()
            }

            view.findViewById<ImageView>(R.id.imvBack).setOnClickListener {
                dismiss()
            }
            return view
        }
    }

    class MembershipSoloDialog : DialogFragment() {
        interface OnBuyNowClickedListener {
            fun onBuyNowClicked()
        }

        var listener: OnBuyNowClickedListener? = null

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val view = inflater.inflate(R.layout.dialog_membership_solo, container, false)
            view.findViewById<Button>(R.id.btn_buy_now_solo).setOnClickListener {
                listener?.onBuyNowClicked()
                dismiss()
            }
            val imageView = view.findViewById<ImageView>(R.id.imvBack)
            imageView.setOnClickListener {
                dismiss()
            }

            view.findViewById<ImageView>(R.id.imvBack).setOnClickListener {
                dismiss()
            }
            return view
        }
    }

    class ContactFragmentDialog : DialogFragment() {
        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val view = inflater.inflate(R.layout.dialog_fragment_contact, container, false)

            view.findViewById<ImageView>(R.id.imageView3).setOnClickListener {
                dismiss()
            }
            return view
        }

    }

    class LegalFragmentDialog : DialogFragment() {
        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val view = inflater.inflate(R.layout.dialog_fragment_legal, container, false)

            view.findViewById<ImageView>(R.id.imageView3).setOnClickListener {
                dismiss()
            }
            return view
        }

    }
}