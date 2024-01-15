package com.ketchupzz.francingsfootwearadmin.repository.customer

import com.ketchupzz.francingsfootwearadmin.model.customer.Customer
import com.ketchupzz.francingsfootwearadmin.utils.UiState

interface CustomerRepository {
    fun getAllCustomers(result : (UiState<List<Customer>>) -> Unit)

}