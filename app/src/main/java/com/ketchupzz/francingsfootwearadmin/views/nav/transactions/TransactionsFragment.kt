package com.ketchupzz.francingsfootwearadmin.views.nav.transactions

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ketchupzz.francingsfootwear.utils.LoadingDialog
import com.ketchupzz.francingsfootwearadmin.R
import com.ketchupzz.francingsfootwearadmin.databinding.FragmentTransactionsBinding
import com.ketchupzz.francingsfootwearadmin.model.transaction.TransactionStatus
import com.ketchupzz.francingsfootwearadmin.model.transaction.Transactions
import com.ketchupzz.francingsfootwearadmin.utils.UiState
import com.ketchupzz.francingsfootwearadmin.utils.getTotalSales
import com.ketchupzz.francingsfootwearadmin.utils.toPHP
import com.ketchupzz.francingsfootwearadmin.viewmodels.TransactionViewModel
import com.ketchupzz.francingsfootwearadmin.views.adapters.OrderClickListener
import com.ketchupzz.francingsfootwearadmin.views.adapters.OrdersAdapter


class TransactionsFragment : Fragment() ,OrderClickListener{
    private lateinit var binding : FragmentTransactionsBinding
    private lateinit var loadingDialog: LoadingDialog
    private val transactionViewModel by activityViewModels<TransactionViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {
        binding = FragmentTransactionsBinding.inflate(inflater,container,false)
        loadingDialog = LoadingDialog(binding.root.context)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        transactionViewModel.transactions.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.FAILED -> {
                    loadingDialog.closeDialog()
                    Toast.makeText(binding.root.context, it.message, Toast.LENGTH_SHORT).show()
                }

                is UiState.LOADING -> {
                    loadingDialog.showDialog("getting orders..")
                }

                is UiState.SUCCESS -> {
                    loadingDialog.closeDialog()
                    binding.recyclerviewTransactions.apply {
                        layoutManager = LinearLayoutManager(view.context)
                        adapter = OrdersAdapter(view.context,it.data,this@TransactionsFragment)
                    }
                }
            }
        }
    }

    override fun onOrderClick(transactions: Transactions) {
        val directions = TransactionsFragmentDirections.actionNavigationTransactionsToReviewTransactions(transactions)
        findNavController().navigate(directions)
    }
}