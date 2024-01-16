package com.ketchupzz.francingsfootwearadmin.views.nav.dashboard.orders

import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.ketchupzz.francingsfootwearadmin.utils.LoadingDialog
import com.ketchupzz.francingsfootwearadmin.databinding.FragmentOrderActionsBinding
import com.ketchupzz.francingsfootwearadmin.model.transaction.PaymentStatus
import com.ketchupzz.francingsfootwearadmin.model.transaction.TransactionStatus
import com.ketchupzz.francingsfootwearadmin.model.transaction.Transactions
import com.ketchupzz.francingsfootwearadmin.utils.UiState
import com.ketchupzz.francingsfootwearadmin.utils.setStatusBgColor
import com.ketchupzz.francingsfootwearadmin.viewmodels.TransactionViewModel
import com.ketchupzz.francingsfootwearadmin.views.nav.dashboard.DashboardFragmentDirections
import java.util.Date

const val ARG_TRANSACTION= "transaction"



class OrderActionsFragment : BottomSheetDialogFragment() {

    private var transaction: Transactions? = null
    private lateinit var binding : FragmentOrderActionsBinding
    private lateinit var loadingDialog: LoadingDialog
    private val transactionViewModel  by activityViewModels<TransactionViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            transaction = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.getParcelable(ARG_TRANSACTION,Transactions::class.java)
            } else {
                it.getParcelable(ARG_TRANSACTION)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentOrderActionsBinding.inflate(inflater,container,false)
        loadingDialog = LoadingDialog(binding.root.context)
        binding.textStatus.text = transaction?.status?.name
        binding.textStatus.backgroundTintList  = ColorStateList.valueOf(binding.root.context.setStatusBgColor(transaction?.status ?: TransactionStatus.PENDING))
        attachActions(transaction?.status ?: TransactionStatus.PENDING)
        transaction?.let {
            if (it.payment.status == PaymentStatus.PAID) {
                binding.buttonAddPayment.visibility = View.GONE
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.textOrderNumber.text =  "${transaction?.id}"
        binding.buttonViewDetails.setOnClickListener {
            transaction?.let {
                val directions = DashboardFragmentDirections.actionNavigationDashboardToReviewTransactions(it)
                findNavController().navigate(directions).also {
                    dismiss()
                }
            }
        }
        binding.buttonAddPayment.setOnClickListener {
            transaction?.let {
                it.payment.status = PaymentStatus.PAID
                it.payment.paymentDate = Date()
                transactionViewModel.transactionRepository.markAsPaid(it) {
                    when(it) {
                        is UiState.FAILED -> {
                            loadingDialog.closeDialog()
                            Toast.makeText(view.context,it.message,Toast.LENGTH_SHORT).show()
                        }
                        is UiState.LOADING -> {
                            loadingDialog.showDialog("Adding Payment...")
                        }
                        is UiState.SUCCESS -> {
                            loadingDialog.closeDialog()
                            Toast.makeText(view.context,it.data,Toast.LENGTH_SHORT).show().also {
                                dismiss()
                            }
                        }
                    }
                }
            }
        }
        binding.buttonCompleted.setOnClickListener {
            if (transaction?.payment?.status == PaymentStatus.UNPAID) {
                Toast.makeText(binding.root.context,"Please complete the payment first",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            transaction?.let {
                transactionViewModel.transactionRepository.markAsCompleted(it) {
                    when(it) {
                        is UiState.FAILED -> {
                            loadingDialog.closeDialog()
                            Toast.makeText(view.context,it.message,Toast.LENGTH_SHORT).show()
                        }
                        is UiState.LOADING -> {
                            loadingDialog.showDialog("Updating order status...")
                        }
                        is UiState.SUCCESS -> {
                            loadingDialog.closeDialog()
                            Toast.makeText(view.context,it.data,Toast.LENGTH_SHORT).show().also {
                                dismiss()
                            }
                        }
                    }
                }
            }
        }
        binding.buttonDecline.setOnClickListener {
            MaterialAlertDialogBuilder(view.context)
                .setTitle("Decline Transaction")
                .setMessage("Are you sure you want to decline this transaction ?")
                .setPositiveButton("Yes") { dialog,_ ->
                    transaction?.let {
                        transactionViewModel.transactionRepository.declineTransaction(it) {
                            when(it) {
                                is UiState.FAILED -> {
                                    loadingDialog.closeDialog()
                                    Toast.makeText(view.context,it.message,Toast.LENGTH_SHORT).show()
                                }
                                is UiState.LOADING -> {
                                    loadingDialog.showDialog("Updating order status...")
                                }
                                is UiState.SUCCESS -> {
                                    loadingDialog.closeDialog()
                                    Toast.makeText(view.context,it.data,Toast.LENGTH_SHORT).show().also {
                                        dismiss()
                                    }
                                }
                            }
                        }
                    }
                }
                .setNegativeButton("Cancel") { dialog,_ ->
                    dialog.dismiss()
                }
                .show()
        }
        binding.buttonAccept.setOnClickListener {

            transaction?.let {
                transactionViewModel.transactionRepository.acceptTransaction(it) {
                    when(it) {
                        is UiState.FAILED -> {
                            loadingDialog.closeDialog()
                            Toast.makeText(view.context,it.message,Toast.LENGTH_SHORT).show()
                        }
                        is UiState.LOADING -> {
                            loadingDialog.showDialog("Updating order status...")
                        }
                        is UiState.SUCCESS -> {
                            loadingDialog.closeDialog()
                            Toast.makeText(view.context,it.data,Toast.LENGTH_SHORT).show().also {
                                dismiss()
                            }
                        }
                    }
                }
            }
        }
        binding.buttonDeliver.setOnClickListener {
            transaction?.let {
                transactionViewModel.transactionRepository.deliverItems(it) {
                    when(it) {
                        is UiState.FAILED -> {
                            loadingDialog.closeDialog()
                            Toast.makeText(view.context,it.message,Toast.LENGTH_SHORT).show()
                        }
                        is UiState.LOADING -> {
                            loadingDialog.showDialog("Updating order status...")
                        }
                        is UiState.SUCCESS -> {
                            loadingDialog.closeDialog()
                            Toast.makeText(view.context,it.data,Toast.LENGTH_SHORT).show().also {
                                dismiss()
                            }
                        }
                    }
                }
            }

        }
    }
    companion object {
        @JvmStatic fun newInstance(transaction : Transactions) =
                OrderActionsFragment().apply {
                    arguments = Bundle().apply {
                        putParcelable(ARG_TRANSACTION, transaction)
                    }
                }
    }
    private fun attachActions(transactionStatus: TransactionStatus) {
        when(transactionStatus) {
            TransactionStatus.PENDING -> {
                binding.buttonAccept.visibility = View.VISIBLE
                binding.buttonDecline.visibility = View.VISIBLE
            }
            TransactionStatus.ACCEPTED -> {
                binding.buttonDeliver.visibility = View.VISIBLE
            }
            TransactionStatus.ON_DELIVERY -> {
                binding.buttonCompleted.visibility = View.VISIBLE
            }
            TransactionStatus.COMPLETE -> {
                binding.buttonCompleted.visibility = View.GONE
            }
            TransactionStatus.CANCELLED -> {

            }
            TransactionStatus.DECLINED -> {
                binding.buttonCompleted.visibility = View.GONE
            }
        }
    }
}