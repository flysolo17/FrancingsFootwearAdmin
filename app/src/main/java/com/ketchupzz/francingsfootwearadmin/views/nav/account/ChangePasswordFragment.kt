package com.ketchupzz.francingsfootwearadmin.views.nav.account

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.ketchupzz.francingsfootwearadmin.utils.LoadingDialog
import com.ketchupzz.francingsfootwearadmin.utils.UiState
import com.ketchupzz.francingsfootwearadmin.databinding.FragmentChangePasswordBinding
import com.ketchupzz.francingsfootwearadmin.viewmodels.AuthViewModel

class ChangePasswordFragment : Fragment() {

    private lateinit var binding : FragmentChangePasswordBinding
    private val authViewModel : AuthViewModel by activityViewModels()


    private lateinit var loadingDialog: LoadingDialog
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChangePasswordBinding.inflate(inflater,container,false)
        loadingDialog = LoadingDialog(binding.root.context)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonLogin.setOnClickListener {
            val old = binding.inputCurrentPassword.text.toString()
            val new = binding.inputNewPassword.text.toString()
            val confirm = binding.inputRepeatPassword.text.toString()
            if (old.isEmpty()) {
                binding.layoutCurrentPassword.error = "Invalid Password"
            } else if (new.isEmpty()) {
                binding.layoutNewPassword.error = "Invalid Password"
            } else if (new != confirm) {
                binding.layoutRepeatPassword.error = "Password does not match"
            } else {
                FirebaseAuth.getInstance().currentUser?.let {
                    reAuthenticate(it,new,old)
                }
            }
        }
    }

    private fun reAuthenticate(user: FirebaseUser, newPassword: String, oldPassword : String) {
        authViewModel.reauthenticate(user,user.email!!,oldPassword) {
            when (it) {
                is UiState.FAILED -> {
                    loadingDialog.closeDialog()
                    Toast.makeText(binding.root.context,it.message, Toast.LENGTH_SHORT).show()
                }
                UiState.LOADING -> {
                    loadingDialog.showDialog("Authenticating....")
                }
                is UiState.SUCCESS -> {
                    loadingDialog.closeDialog()
                    changePassword(it.data,newPassword)
                }
            }
        }
    }
    private fun changePassword(user: FirebaseUser, newPassword : String) {
        authViewModel.changePassword(user,newPassword) {
            when (it) {
                is UiState.FAILED -> {
                    loadingDialog.closeDialog()
                    Toast.makeText(binding.root.context,it.message, Toast.LENGTH_SHORT).show()
                }
                UiState.LOADING -> {
                    loadingDialog.showDialog("Changing password....")
                }
                is UiState.SUCCESS -> {
                    loadingDialog.closeDialog()
                    Toast.makeText(binding.root.context,it.data, Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                }
            }
        }
    }


}