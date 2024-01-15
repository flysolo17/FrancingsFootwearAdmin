package com.ketchupzz.francingsfootwearadmin.viewmodels

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ketchupzz.francingsfootwearadmin.model.products.Product
import com.ketchupzz.francingsfootwearadmin.repository.products.ProductsRepository
import com.ketchupzz.francingsfootwearadmin.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class ProductViewModel @Inject constructor(private val productsRepository: ProductsRepository) : ViewModel(){
    private val _products = MutableLiveData<UiState<List<Product>>>()
    val product : LiveData<UiState<List<Product>>> get() = _products

    fun getAllProducts() {
        productsRepository.getAllProduct {
            _products.value = it
        }
    }
    fun createProduct(product: Product,result : (UiState<String>) -> Unit)  {
       return productsRepository.addProduct(product,result)
    }

    fun uploadProductImages(productID : String, uri: Uri, imageType : String, result: (UiState<Uri>) -> Unit)  = viewModelScope.launch {
        result.invoke(UiState.LOADING)
        val data = productsRepository.uploadProductImage(productID, uri, imageType)
        result.invoke(data)
    }
}