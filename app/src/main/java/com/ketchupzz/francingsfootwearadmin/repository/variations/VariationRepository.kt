package com.ketchupzz.francingsfootwearadmin.repository.variations

import android.net.Uri
import com.ketchupzz.francingsfootwearadmin.model.products.Size
import com.ketchupzz.francingsfootwearadmin.model.products.Variation
import com.ketchupzz.francingsfootwearadmin.utils.UiState

interface VariationRepository {
    fun createVariation(productID : String,variation: Variation,result : (UiState<String>) -> Unit)
    fun deleteVariation(productID: String,variationID : String,imageURL : String,result: (UiState<String>) -> Unit)
    fun getVariationByProductID(productID: String,result: (UiState<List<Variation>>) -> Unit)
    fun stockIn(productID: String,variationID : String,size: List<Size>,result: (UiState<String>) -> Unit)
    fun stockOut(productID: String,variationID : String,size: List<Size>,result: (UiState<String>) -> Unit)
    suspend fun uploadVariationPhoto(productID: String, uri : Uri, imageType : String) : UiState<Uri>
}