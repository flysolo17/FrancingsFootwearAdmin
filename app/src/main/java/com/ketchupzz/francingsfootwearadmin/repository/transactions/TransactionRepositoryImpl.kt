package com.ketchupzz.francingsfootwearadmin.repository.transactions

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.WriteBatch
import com.google.firebase.firestore.toObjects
import com.google.firebase.storage.FirebaseStorage
import com.ketchupzz.francingsfootwear.models.products.Size
import com.ketchupzz.francingsfootwear.models.products.Variation
import com.ketchupzz.francingsfootwearadmin.model.transaction.Details
import com.ketchupzz.francingsfootwearadmin.model.transaction.Items
import com.ketchupzz.francingsfootwearadmin.model.transaction.TransactionStatus
import com.ketchupzz.francingsfootwearadmin.model.transaction.Transactions
import com.ketchupzz.francingsfootwearadmin.repository.products.PRODUCT_COLLECTION
import com.ketchupzz.francingsfootwearadmin.repository.variations.VARIATION_SUB_COLLECTION
import com.ketchupzz.francingsfootwearadmin.utils.UiState

const val TRANSACTION_COLLECTION = "transactions";
data class TransactionUpdates(val items: Items,val ref : DocumentReference)

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

        val transactionRef = firestore.collection(TRANSACTION_COLLECTION).document(transactions.id)
        val details = Details(title = "Order Accepted", description = "Your order has been Accepted")

        firestore.runTransaction { transaction ->
            // Fetch sizes data outside of the transaction
            val allSizes: MutableMap<DocumentReference, List<Size>> = mutableMapOf()
            transactions.items.forEach { item ->
                val ref = firestore.collection(PRODUCT_COLLECTION)
                    .document(item.productID)
                    .collection(VARIATION_SUB_COLLECTION)
                    .document(item.variationID)
                try {
                    val sizes = transaction.get(ref).toObject(Variation::class.java)?.sizes ?: emptyList()
                    allSizes[ref] = sizes
                } catch (e: Exception) {
                    Log.e(TRANSACTION_COLLECTION, "Error getting sizes: ${e.message}")
                }
            }

            // Update sizes data within the transaction
            allSizes.forEach { (ref, sizes) ->
                sizes.forEach { size ->
                    transactions.items.find { item ->
                        item.productID == ref.parent.parent?.id && item.variationID == ref.id && size.size == item.size
                    }?.let { item ->
                        size.stock -= item.quantity
                        Log.d(TRANSACTION_COLLECTION, "Stocks : ${size.stock}")
                    }
                }
                Log.d(TRANSACTION_COLLECTION, "List : $sizes")
                transaction.update(ref, "sizes", sizes)
            }

            // Update transaction status and details
            transaction.update(transactionRef, "status", TransactionStatus.ACCEPTED)
            transaction.update(transactionRef, "details", FieldValue.arrayUnion(details))

            null
        }.addOnSuccessListener {
            result.invoke(UiState.SUCCESS("Order Accepted!"))
            result.invoke(UiState.SUCCESS("Inventory updated!"))
        }.addOnFailureListener { exception ->
            Log.d(TRANSACTION_COLLECTION, exception.message.toString())
            result.invoke(UiState.FAILED("Transaction failed to accept"))
        }
    }




//    override fun acceptTransaction(transactions: Transactions, result: (UiState<String>) -> Unit) {
//        result.invoke(UiState.LOADING)
//        val details = Details(title = "Order Accepted", description = "Your order is accepted and ready to be packed.!")
//
//        firestore.collection(TRANSACTION_COLLECTION)
//            .document(transactions.id)
//            .update("status",TransactionStatus.ACCEPTED,
//                "details",FieldValue.arrayUnion(details))
//            .addOnCompleteListener {
//                if (it.isSuccessful) {
//
//                    updateQuantity(transactions.items).addOnCompleteListener {
//                        result.invoke(UiState.SUCCESS("Order Accepted!"))
//                        result.invoke(UiState.SUCCESS("Inventory updated!"))
//                    }.addOnFailureListener {
//
//                        Log.d(TRANSACTION_COLLECTION,it.message.toString())
//                        result.invoke(UiState.FAILED(it.message.toString()))
//                    }
//                } else {
//                    Log.d(TRANSACTION_COLLECTION,it.exception?.message.toString())
//                    result.invoke(UiState.FAILED("Transaction failed to accept"))
//                }
//            }.addOnFailureListener {
//                result.invoke(UiState.FAILED(it.message.toString()))
//            }
//    }



    override fun declineTransaction(transactions: Transactions, result: (UiState<String>) -> Unit) {
        result.invoke(UiState.LOADING)


        val details = Details(title = "Order Declined", description = "Your order has been declined")
        firestore.collection(TRANSACTION_COLLECTION)
            .document(transactions.id)
            .update("status",TransactionStatus.DECLINED,
                "details",FieldValue.arrayUnion(details))
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    result.invoke(UiState.SUCCESS("Order Declined!"))
                } else {
                    Log.d(TRANSACTION_COLLECTION,it.exception?.message.toString())
                    result.invoke(UiState.FAILED("Transaction failed to Decline"))
                }
            }.addOnFailureListener {
                Log.d(TRANSACTION_COLLECTION,it.message.toString())
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
                    Log.d(TRANSACTION_COLLECTION,it.exception?.message.toString())
                    result.invoke(UiState.FAILED("Transaction failed to accept"))
                }
            }.addOnFailureListener {
                Log.d(TRANSACTION_COLLECTION,it.message.toString())
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
                    Log.d(TRANSACTION_COLLECTION,it.exception?.message.toString())
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
                    Log.d(TRANSACTION_COLLECTION,it.exception?.message.toString())
                    result.invoke(UiState.FAILED("Transaction failed to accept"))
                }
            }.addOnFailureListener {
                result.invoke(UiState.FAILED(it.message.toString()))
            }
    }

    private fun updateQuantity(items: List<Items>) : Task<Void> {
        val batch = firestore.batch()
        val references = mutableListOf<DocumentReference>()
        items.forEach { item ->
                val ref = firestore.collection(PRODUCT_COLLECTION)
                    .document(item.productID)
                    .collection(VARIATION_SUB_COLLECTION)
                    .document(item.variationID)

                ref.get().addOnSuccessListener {snap ->
                    if (snap.exists()) {
                        val variation = snap.toObject(Variation::class.java)
                        variation?.sizes?.forEach { size ->
                            if (size.size == item.size) {
                                size.stock -= item.quantity
                                Log.d(TRANSACTION_COLLECTION, "Stocks : ${size.stock}")
                            }
                        }
                        Log.d(TRANSACTION_COLLECTION, "List : ${variation?.sizes}")
                        batch.update(ref, "sizes", variation?.sizes)
                }
            }
        }
        return batch.commit()
    }


}