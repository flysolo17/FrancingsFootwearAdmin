package com.ketchupzz.francingsfootwearadmin.views.nav.account

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ketchupzz.francingsfootwearadmin.utils.LoadingDialog
import com.ketchupzz.francingsfootwearadmin.utils.UiState
import com.ketchupzz.francingsfootwearadmin.databinding.FragmentUpdateAccountBinding
import com.ketchupzz.francingsfootwearadmin.viewmodels.AuthViewModel

class UpdateAccountFragment : Fragment() {
    private lateinit var binding : FragmentUpdateAccountBinding
    private lateinit var loadingDialog: LoadingDialog
    private val args by navArgs<UpdateAccountFragmentArgs>()
    private val authViewModel by activityViewModels<AuthViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUpdateAccountBinding.inflate(inflater,container,false)
        loadingDialog = LoadingDialog(binding.root.context)
        binding.inputFullname.setText(args.users.name)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonSave.setOnClickListener {
            val name = binding.inputFullname.text.toString()
            if (name.isEmpty()) {
                binding.layoutFullname.error ="Please enter name"
                return@setOnClickListener
            }
            updateAccount(args.users.id ?: "",name)
        }
    }

    private fun updateAccount(uid: String, name: String) {
        authViewModel.editProfile(uid, name) {
            when (it) {
                is UiState.FAILED -> {
                    loadingDialog.closeDialog()
                    Toast.makeText(binding.root.context, it.message, Toast.LENGTH_SHORT).show()
                }

                is UiState.LOADING -> {
                    loadingDialog.showDialog("Updating Account...")
                }

                is UiState.SUCCESS -> {
                    loadingDialog.closeDialog()
                    Toast.makeText(binding.root.context, it.data, Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                }
            }
        }
    }

}