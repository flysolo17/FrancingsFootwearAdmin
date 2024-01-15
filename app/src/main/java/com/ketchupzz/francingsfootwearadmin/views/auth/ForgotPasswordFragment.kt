package com.ketchupzz.francingsfootwearadmin.views.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ketchupzz.francingsfootwearadmin.utils.UiState
import com.ketchupzz.francingsfootwearadmin.databinding.FragmentForgotPasswordBinding
import com.ketchupzz.francingsfootwearadmin.viewmodels.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForgotPasswordFragment : BottomSheetDialogFragment() {
    private lateinit var binding : FragmentForgotPasswordBinding
    private val authViewModel by activityViewModels<AuthViewModel>()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentForgotPasswordBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonLogin.setOnClickListener {
            val email = binding.inputEmail.text.toString()
            if (email.isEmpty()) {
                binding.layoutEmail.error = "Enter email"
                return@setOnClickListener
            }
            forgotPassword(email)
        }
    }
    private fun forgotPassword(email :String) {
        authViewModel.forgotPassword(email) {
            when (it) {
                is UiState.FAILED -> {

                    binding.buttonLogin.isEnabled = true
                    binding.buttonLogin.text = "Submit"
                    Toast.makeText(binding.root.context, it.message, Toast.LENGTH_SHORT).show()
                }

                is UiState.LOADING -> {
                    binding.buttonLogin.isEnabled = false
                    binding.buttonLogin.text = "Loading...."
                }

                is UiState.SUCCESS -> {

                    binding.buttonLogin.isEnabled = true
                    binding.buttonLogin.text = "Submit"
                    Toast.makeText(binding.root.context, it.data, Toast.LENGTH_SHORT).show()
                    dismiss()
                }
            }
        }
    }
}