package com.ketchupzz.francingsfootwearadmin.views.nav.dashboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ketchupzz.francingsfootwearadmin.utils.LoadingDialog
import com.ketchupzz.francingsfootwearadmin.databinding.FragmentOrderByStatusBinding
import com.ketchupzz.francingsfootwearadmin.model.transaction.TransactionStatus
import com.ketchupzz.francingsfootwearadmin.model.transaction.Transactions
import com.ketchupzz.francingsfootwearadmin.utils.UiState
import com.ketchupzz.francingsfootwearadmin.viewmodels.TransactionViewModel
import com.ketchupzz.francingsfootwearadmin.views.adapters.TransactionAdapter
import com.ketchupzz.francingsfootwearadmin.views.adapters.TransactionClickListener


class OrderByStatusFragment : Fragment(), TransactionClickListener{


    private lateinit var binding : FragmentOrderByStatusBinding
    private lateinit var loadingDialog : LoadingDialog
    private val transactionViewModel by activityViewModels<TransactionViewModel>()

    private var position: Int ? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            position = it.getInt(POSITION)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOrderByStatusBinding.inflate(inflater,container,false)
        loadingDialog = LoadingDialog(binding.root.context)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        transactionViewModel.transactions.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.FAILED -> {
                    loadingDialog.closeDialog()
                    Toast.makeText(view.context, it.message, Toast.LENGTH_SHORT).show()
                }

                is UiState.LOADING -> {
                    loadingDialog.showDialog("Getting All Transactions")
                }

                is UiState.SUCCESS -> {
                    loadingDialog.closeDialog()
                    binding.recyclerviewTransactions.apply {
                        layoutManager = LinearLayoutManager(view.context)
                        adapter = TransactionAdapter(
                            view.context,
                            it.data.filter {
                                it.status == TransactionStatus.entries[position ?: 0]
                            },
                            this@OrderByStatusFragment
                        )
                    }
                }
            }
        }
    }
    override fun declineTransaction(transactions: Transactions) {
        val directions = DashboardFragmentDirections.actionNavigationDashboardToDeclineTransactionFragment(transactions)
        findNavController().navigate(directions)
    }

    override fun acceptTransaction(transactions: Transactions) {
        accept(transactions)
    }

    private fun accept(transactions: Transactions) {
        transactionViewModel.transactionRepository.acceptTransaction(transactions) {
            when (it) {
                is UiState.FAILED -> {
                    loadingDialog.closeDialog()
                    Toast.makeText(binding.root.context, it.message, Toast.LENGTH_SHORT).show()
                }

                is UiState.LOADING -> {
                    loadingDialog.showDialog("Accepting order..")
                }

                is UiState.SUCCESS -> {
                    loadingDialog.closeDialog()
                    Toast.makeText(binding.root.context,it.data,Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}