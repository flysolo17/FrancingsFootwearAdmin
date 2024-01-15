package com.ketchupzz.francingsfootwearadmin.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ketchupzz.francingsfootwearadmin.model.transaction.Transactions
import com.ketchupzz.francingsfootwearadmin.repository.transactions.TransactionRepository
import com.ketchupzz.francingsfootwearadmin.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(val transactionRepository: TransactionRepository): ViewModel() {
    private val _transactions = MutableLiveData<UiState<List<Transactions>>>()
    val transactions :LiveData<UiState<List<Transactions>>> get() = _transactions

    fun getAllMyTransactions() {
        transactionRepository.getAllTransactions {
            _transactions.value = it
        }
    }
}