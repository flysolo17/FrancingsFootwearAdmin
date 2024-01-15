package com.ketchupzz.francingsfootwearadmin.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ketchupzz.francingsfootwearadmin.model.customer.Customer
import com.ketchupzz.francingsfootwearadmin.repository.customer.CustomerRepository
import com.ketchupzz.francingsfootwearadmin.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CustomersViewModel @Inject constructor(val customerRepository: CustomerRepository): ViewModel() {
    private val _customers = MutableLiveData<UiState<List<Customer>>>()
    val customers : LiveData<UiState<List<Customer>>> get() = _customers

    fun getAllCustomers() {
        viewModelScope.launch {
           customerRepository.getAllCustomers {
               _customers.value = it
           }
        }
    }
}