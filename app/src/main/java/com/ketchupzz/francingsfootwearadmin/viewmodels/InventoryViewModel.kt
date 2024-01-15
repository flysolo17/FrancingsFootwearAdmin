package com.ketchupzz.francingsfootwearadmin.viewmodels

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ketchupzz.francingsfootwearadmin.utils.UiState
import com.ketchupzz.francingsfootwearadmin.model.products.Product
import com.ketchupzz.francingsfootwearadmin.model.products.Variation
import com.ketchupzz.francingsfootwearadmin.repository.inventory.InventoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InventoryViewModel @Inject constructor(private val inventoryRepository: InventoryRepository):ViewModel() {

    private val _products = MutableLiveData<UiState<List<Product>>>()
    val products : LiveData<UiState<List<Product>>> get() = _products
    fun saveProduct(product: Product,result :(UiState<String>)  -> Unit) = viewModelScope.launch{
        result.invoke(UiState.LOADING)
        val data = inventoryRepository.createProduct(product)
        result.invoke(data)
    }
    fun uploadProductImages(productID : String,uri: Uri,imageType : String,result: (UiState<Uri>) -> Unit)  = viewModelScope.launch {
        result.invoke(UiState.LOADING)
        val data = inventoryRepository.uploadPhoto(productID, uri, imageType)
        result.invoke(data)
    }

    fun saveVariationImage(productID : String,uri: Uri,imageType : String,result: (UiState<Uri>) -> Unit)  = viewModelScope.launch {
        result.invoke(UiState.LOADING)
        val data = inventoryRepository.uploadPhoto(productID, uri, imageType)
        result.invoke(data)
    }

    fun saveVariation(productID: String, variation: Variation, result: (UiState<String>) -> Unit) = viewModelScope.launch {
        result.invoke(UiState.LOADING)
        val data = inventoryRepository.saveVariation(productID,variation)
        result.invoke(data)
    }

    fun getAllProducts() {
        inventoryRepository.getAllProducts {
            _products.value = it
        }
    }
}