package com.ketchupzz.francingsfootwearadmin.repository.transactions

import com.ketchupzz.francingsfootwearadmin.model.transaction.Transactions
import com.ketchupzz.francingsfootwearadmin.utils.UiState

interface TransactionRepository {
    fun getAllTransactions(result : (UiState<List<Transactions>>) -> Unit)
    fun updateTransaction()
    fun acceptTransaction(transactions: Transactions,result: (UiState<String>) -> Unit)
    fun declineTransaction(transactions: Transactions,result: (UiState<String>) -> Unit)
    fun deliverItems(transactions: Transactions,result: (UiState<String>) -> Unit)
    fun markAsPaid(transactions: Transactions,result: (UiState<String>) -> Unit)
    fun markAsCompleted(transactions: Transactions,result: (UiState<String>) -> Unit)
}