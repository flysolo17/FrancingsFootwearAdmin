package com.ketchupzz.francingsfootwearadmin.views.nav.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ketchupzz.francingsfootwear.utils.LoadingDialog
import com.ketchupzz.francingsfootwearadmin.R

import com.ketchupzz.francingsfootwearadmin.databinding.FragmentDashboardBinding
import com.ketchupzz.francingsfootwearadmin.model.transaction.TransactionStatus
import com.ketchupzz.francingsfootwearadmin.model.transaction.Transactions
import com.ketchupzz.francingsfootwearadmin.utils.UiState
import com.ketchupzz.francingsfootwearadmin.utils.getTotalSales
import com.ketchupzz.francingsfootwearadmin.utils.toPHP
import com.ketchupzz.francingsfootwearadmin.viewmodels.TransactionViewModel
import com.ketchupzz.francingsfootwearadmin.views.adapters.OrderClickListener
import com.ketchupzz.francingsfootwearadmin.views.adapters.OrdersAdapter
import com.ketchupzz.francingsfootwearadmin.views.nav.dashboard.orders.OrderActionsFragment
import dagger.hilt.android.AndroidEntryPoint


const val POSITION = "position"
@AndroidEntryPoint
class DashboardFragment : Fragment() ,OrderClickListener{

    private lateinit var binding : FragmentDashboardBinding
    private lateinit var loadingDialog: LoadingDialog
    private val transactionsViewModel by activityViewModels<TransactionViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDashboardBinding.inflate(inflater,container,false)
        loadingDialog = LoadingDialog(binding.root.context)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        transactionsViewModel.getAllMyTransactions()
        transactionsViewModel.transactions.observe(viewLifecycleOwner) {
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
                    binding.textTotalSales.text = getTotalSales(it.data).toPHP()
                    binding.textCompleted.text = it.data.filter { it.status == TransactionStatus.COMPLETE }.size.toString()
                    binding.textOngoind.text = it.data.filter { it.status == TransactionStatus.ACCEPTED }.size.toString()
                    binding.textOnDelivery.text = it.data.filter { it.status == TransactionStatus.ON_DELIVERY }.size.toString()
                    val transactions = it.data.filter { it.status != TransactionStatus.COMPLETE && it.status != TransactionStatus.DECLINED && it.status != TransactionStatus.CANCELLED }
                    binding.recyclerviewShipping.apply {
                        layoutManager = LinearLayoutManager(view.context)
                        adapter = OrdersAdapter(view.context,transactions,this@DashboardFragment)
                    }
                }
            }
        }

    }

    override fun onOrderClick(transactions: Transactions) {
        val fragment = OrderActionsFragment.newInstance(transactions)
        if (!fragment.isAdded) {
            fragment.show(childFragmentManager,"transaction")
        }
    }
}