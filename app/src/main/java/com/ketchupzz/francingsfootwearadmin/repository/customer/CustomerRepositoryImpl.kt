package com.ketchupzz.francingsfootwearadmin.repository.customer

import com.google.firebase.firestore.FirebaseFirestore
import com.ketchupzz.francingsfootwearadmin.model.customer.Customer
import com.ketchupzz.francingsfootwearadmin.utils.UiState
const val CUSTOMER_COLLECTION = "users"
class CustomerRepositoryImpl(private val firestore: FirebaseFirestore): CustomerRepository {
    override fun getAllCustomers(result: (UiState<List<Customer>>) -> Unit) {
        result.invoke(UiState.LOADING)
        firestore.collection(CUSTOMER_COLLECTION)
            .addSnapshotListener { value, error ->
                error?.let {
                    result.invoke(UiState.FAILED(it.message.toString()))
                }
                value?.let {
                    result.invoke(UiState.SUCCESS(it.toObjects(Customer::class.java)))
                }
            }
    }
}