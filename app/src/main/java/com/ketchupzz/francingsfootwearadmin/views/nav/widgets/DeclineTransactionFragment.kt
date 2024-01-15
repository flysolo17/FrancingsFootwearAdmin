package com.ketchupzz.francingsfootwearadmin.views.nav.widgets

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ketchupzz.francingsfootwear.utils.LoadingDialog
import com.ketchupzz.francingsfootwearadmin.R
import com.ketchupzz.francingsfootwearadmin.databinding.FragmentDeclineTransactionBinding
import com.ketchupzz.francingsfootwearadmin.viewmodels.TransactionViewModel


class DeclineTransactionFragment : BottomSheetDialogFragment() {
    private lateinit var binding : FragmentDeclineTransactionBinding
    private lateinit var loadingDialog: LoadingDialog
    private val transactionViewModel by activityViewModels<TransactionViewModel>()
    private val args by navArgs<DeclineTransactionFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDeclineTransactionBinding.inflate(inflater,container,false)
        loadingDialog = LoadingDialog(binding.root.context)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}