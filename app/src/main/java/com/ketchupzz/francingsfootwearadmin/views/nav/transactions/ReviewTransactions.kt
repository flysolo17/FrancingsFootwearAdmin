package com.ketchupzz.francingsfootwearadmin.views.nav.transactions

import android.content.res.ColorStateList
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.ketchupzz.francingsfootwearadmin.utils.LoadingDialog
import com.ketchupzz.francingsfootwearadmin.R
import com.ketchupzz.francingsfootwearadmin.databinding.FragmentReviewTransactionsBinding
import com.ketchupzz.francingsfootwearadmin.model.transaction.TransactionStatus
import com.ketchupzz.francingsfootwearadmin.utils.countItems
import com.ketchupzz.francingsfootwearadmin.utils.toPHP
import com.ketchupzz.francingsfootwearadmin.viewmodels.TransactionViewModel
import com.ketchupzz.francingsfootwearadmin.views.adapters.ItemsAdapter
import com.ketchupzz.francingsfootwearadmin.views.adapters.TransactionStatusAdapter


class ReviewTransactions : Fragment() {

    private lateinit var binding : FragmentReviewTransactionsBinding
    private lateinit var loadingDialog: LoadingDialog
    private val transactionViewModel by activityViewModels<TransactionViewModel>()
    private val args by navArgs<ReviewTransactionsArgs>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentReviewTransactionsBinding.inflate(inflater,container,false)
        loadingDialog = LoadingDialog(binding.root.context)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val transaction  = args.transactions
        binding.textTransactionID.text = transaction.id
        binding.textFullname.text = transaction.address.contacts?.name ?:""
        binding.textPhone.text = transaction.address.contacts?.phone ?:""
        binding.textAddress.text = transaction.address.name ?:""
        binding.textStatus.backgroundTintList  = ColorStateList.valueOf(setStatusBgColor(transaction.status))
        binding.textStatus.text = transaction.status.name
        binding.textItems.text = countItems(transaction.items).toString()
        binding.textShipping.text = transaction.shippingFee.toPHP()
        binding.textTotal.text = transaction.payment.total.toPHP()
        binding.textPayment.text = transaction.payment.method.name +" - " + transaction.payment.status.name
        binding.recyclerviewItems.apply {
            layoutManager  =LinearLayoutManager(view.context)
            adapter = ItemsAdapter(view.context,args.transactions.items)
        }
        binding.recyclerviewStatus.apply {
            layoutManager  =LinearLayoutManager(view.context)
            adapter = TransactionStatusAdapter(view.context,args.transactions.details.reversed())
        }
    }
    private fun setStatusBgColor(transactionStatus: TransactionStatus): Int {
        return when(transactionStatus) {
            TransactionStatus.PENDING -> ContextCompat.getColor(binding.root.context, R.color.pending)
            TransactionStatus.ACCEPTED -> ContextCompat.getColor(binding.root.context, R.color.accepted)
            TransactionStatus.ON_DELIVERY -> ContextCompat.getColor(binding.root.context, R.color.ondelivery)
            TransactionStatus.COMPLETE -> ContextCompat.getColor(binding.root.context, R.color.completed)
            TransactionStatus.CANCELLED -> ContextCompat.getColor(binding.root.context, R.color.cancelled)
            TransactionStatus.DECLINED -> ContextCompat.getColor(binding.root.context, R.color.declined)
        }
    }
}