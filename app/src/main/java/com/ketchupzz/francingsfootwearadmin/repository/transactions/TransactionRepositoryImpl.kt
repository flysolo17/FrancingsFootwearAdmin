package com.ketchupzz.francingsfootwearadmin.repository.transactions

import android.util.Log
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.toObjects
import com.google.firebase.storage.FirebaseStorage
import com.ketchupzz.francingsfootwearadmin.model.transaction.Details
import com.ketchupzz.francingsfootwearadmin.model.transaction.TransactionStatus
import com.ketchupzz.francingsfootwearadmin.model.transaction.Transactions
import com.ketchupzz.francingsfootwearadmin.utils.UiState
const val TRANSACTION_COLLECTION = "transactions";
class TransactionRepositoryImpl(private val firestore: FirebaseFirestore ,private val storage : FirebaseStorage): TransactionRepository {

    override fun getAllTransactions(result: (UiState<List<Transactions>>) -> Unit) {
        result.invoke(UiState.LOADING)
        firestore.collection(TRANSACTION_COLLECTION)
            .orderBy("orderDate",Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
                error?.let {
                    Log.d(TRANSACTION_COLLECTION,it.message.toString())
                    result.invoke(UiState.FAILED(it.message.toString()))
                }
                value?.let {
                    Log.d(TRANSACTION_COLLECTION,it.toObjects<Transactions>().size.toString())
                    result.invoke(UiState.SUCCESS(it.toObjects(Transactions::class.java)))
                }
            }
    }


    override fun updateTransaction() {
        TODO("Not yet implemented")
    }

    override fun acceptTransaction(transactions: Transactions, result: (UiState<String>) -> Unit) {
        result.invoke(UiState.LOADING)
        val details = Details(title = "Order Accepted", description = "Your order is accepted and ready to be packed.!")

        firestore.collection(TRANSACTION_COLLECTION)
            .document(transactions.id)
            .update("status",TransactionStatus.ACCEPTED,
                "details",FieldValue.arrayUnion(details))
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    result.invoke(UiState.SUCCESS("Order Accepted!"))
                } else {
                    result.invoke(UiState.FAILED("Transaction failed to accept"))
                }
            }.addOnFailureListener {
                result.invoke(UiState.FAILED(it.message.toString()))
            }
    }

    override fun declineTransaction(transactions: Transactions, result: (UiState<String>) -> Unit) {
        result.invoke(UiState.LOADING)
        val details = Details(title = "Order Declined", description = "Your order has been declined")
        firestore.collection(TRANSACTION_COLLECTION)
            .document(transactions.id)
            .update("status",TransactionStatus.DECLINED,
                "details",FieldValue.arrayUnion(details))
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    result.invoke(UiState.SUCCESS("Order Accepted!"))
                } else {
                    result.invoke(UiState.FAILED("Transaction failed to accept"))
                }
            }.addOnFailureListener {
                result.invoke(UiState.FAILED(it.message.toString()))
            }
    }

    override fun deliverItems(transactions: Transactions, result: (UiState<String>) -> Unit) {
        result.invoke(UiState.LOADING)
        val details = Details(title = "Delivery update", description = "Your order is on delivery.!")
        firestore.collection(TRANSACTION_COLLECTION)
            .document(transactions.id)
            .update("status",TransactionStatus.ON_DELIVERY,
                "details",FieldValue.arrayUnion(details))
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    result.invoke(UiState.SUCCESS("Order is on delivery now!"))
                } else {
                    result.invoke(UiState.FAILED("Transaction failed to accept"))
                }
            }.addOnFailureListener {
                result.invoke(UiState.FAILED(it.message.toString()))
            }
    }

    override fun markAsPaid(transactions: Transactions, result: (UiState<String>) -> Unit) {
        result.invoke(UiState.LOADING)
        val details = Details(title = "Payment update", description = "Your order is paid.")
        firestore.collection(TRANSACTION_COLLECTION)
            .document(transactions.id)
            .update("payment" ,transactions.payment,"details",FieldValue.arrayUnion(details))
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    result.invoke(UiState.SUCCESS("Order is paid!"))
                } else {
                    result.invoke(UiState.FAILED("Transaction failed to accept"))
                }
            }.addOnFailureListener {
                result.invoke(UiState.FAILED(it.message.toString()))
            }
    }

    override fun markAsCompleted(transactions: Transactions, result: (UiState<String>) -> Unit) {
        result.invoke(UiState.LOADING)
        val details = Details(title = "Completed", description = "Your order is complete.")
        firestore.collection(TRANSACTION_COLLECTION)
            .document(transactions.id)
            .update("status",TransactionStatus.COMPLETE,
                "details",FieldValue.arrayUnion(details))
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    result.invoke(UiState.SUCCESS("Order is paid!"))
                } else {
                    result.invoke(UiState.FAILED("Transaction failed to accept"))
                }
            }.addOnFailureListener {
                result.invoke(UiState.FAILED(it.message.toString()))
            }
    }
}