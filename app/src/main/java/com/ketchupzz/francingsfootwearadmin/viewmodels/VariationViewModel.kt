package com.ketchupzz.francingsfootwearadmin.viewmodels

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ketchupzz.francingsfootwearadmin.model.products.Size
import com.ketchupzz.francingsfootwearadmin.model.products.Variation
import com.ketchupzz.francingsfootwearadmin.repository.variations.VariationRepository
import com.ketchupzz.francingsfootwearadmin.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class VariationViewModel @Inject constructor(private val variationRepository: VariationRepository) : ViewModel() {

    private val _variations = MutableLiveData<UiState<List<Variation>>>()
    val variations : LiveData<UiState<List<Variation>>> get() = _variations

    fun getVariationByProductID(productID : String) {
        variationRepository.getVariationByProductID(productID) {
            _variations.value = it
        }
    }
    fun uploadVariationImage(productID : String, uri: Uri, imageType : String, result: (UiState<Uri>) -> Unit)  = viewModelScope.launch {
        result.invoke(UiState.LOADING)
        val data = variationRepository.uploadVariationPhoto(productID, uri, imageType)
        result.invoke(data)
    }
    fun createVariation(productID: String,variation: Variation,result : (UiState<String>) -> Unit) {
        return variationRepository.createVariation(productID, variation, result)
    }
    fun deleteVariation(productID: String,variationID : String,result: (UiState<String>) -> Unit) {
        return variationRepository.deleteVariation(productID, variationID, result)
    }
    fun stockIn(productID: String,variationID : String,size: List<Size>,result: (UiState<String>) -> Unit) {
        return variationRepository.stockIn(productID, variationID, size, result)
    }
    fun stockOut(productID: String,variationID : String,size: List<Size>,result: (UiState<String>) -> Unit) {
        return variationRepository.stockOut(productID, variationID, size, result)
    }
}