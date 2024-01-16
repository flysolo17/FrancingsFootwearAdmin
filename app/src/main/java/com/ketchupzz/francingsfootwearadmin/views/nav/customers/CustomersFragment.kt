package com.ketchupzz.francingsfootwearadmin.views.nav.customers

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ketchupzz.francingsfootwearadmin.utils.LoadingDialog
import com.ketchupzz.francingsfootwearadmin.databinding.FragmentCustomersBinding
import com.ketchupzz.francingsfootwearadmin.utils.UiState
import com.ketchupzz.francingsfootwearadmin.viewmodels.CustomersViewModel
import com.ketchupzz.francingsfootwearadmin.viewmodels.TransactionViewModel
import com.ketchupzz.francingsfootwearadmin.views.adapters.CustomersAdapter


class CustomersFragment : Fragment() {
    private lateinit var binding : FragmentCustomersBinding
    private lateinit var loadingDialog: LoadingDialog
    private val customerViewModel by activityViewModels<CustomersViewModel>()
    private val transactionsViewModel by activityViewModels<TransactionViewModel>()
    private lateinit var customersAdapter: CustomersAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCustomersBinding.inflate(inflater,container,false)
        loadingDialog = LoadingDialog(binding.root.context)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        customersAdapter = CustomersAdapter(binding.root.context, listOf(), listOf())
        customerViewModel.getAllCustomers()
        observers()

    }
    private fun observers() {
        customerViewModel.customers.observe(viewLifecycleOwner) {
            when(it) {
                is UiState.FAILED -> {
                    loadingDialog.closeDialog()
                    Toast.makeText(binding.root.context,it.message,Toast.LENGTH_SHORT).show()
                }
                is UiState.LOADING -> {
                    loadingDialog.showDialog("Getting all customers")
                }
                is UiState.SUCCESS -> {
                    loadingDialog.closeDialog()
                    customersAdapter.updateCustomers(it.data)
                    binding.recyclerviewCustomer.apply {
                        layoutManager = LinearLayoutManager(binding.root.context)
                        adapter = customersAdapter
                    }.also {
                        transactionsViewModel.getAllMyTransactions()
                    }
                }
            }
        }
        transactionsViewModel.transactions.observe(viewLifecycleOwner) {
            when(it) {
                is UiState.FAILED -> {
                    loadingDialog.closeDialog()
                    Toast.makeText(binding.root.context,it.message,Toast.LENGTH_SHORT).show()
                }

                is UiState.LOADING -> {
                    loadingDialog.showDialog("Getting all transactions")
                }

                is UiState.SUCCESS -> {
                    loadingDialog.closeDialog()
                    customersAdapter.updateTransaction(it.data)
                }
            }
        }
    }

}