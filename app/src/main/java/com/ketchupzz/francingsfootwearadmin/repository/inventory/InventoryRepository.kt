package com.ketchupzz.francingsfootwearadmin.repository.inventory

import android.net.Uri
import com.ketchupzz.francingsfootwearadmin.utils.UiState
import com.ketchupzz.francingsfootwearadmin.model.products.Product
import com.ketchupzz.francingsfootwearadmin.model.products.Variation
import org.checkerframework.checker.guieffect.qual.UI


interface InventoryRepository {
    suspend fun createProduct(product: Product) : UiState<String>
    suspend fun uploadPhoto(productID: String, uri : Uri, imageType : String) : UiState<Uri>
    //fun uploadVariations(productID : String ,variations : List<Variation>,result: (UiState<String>) -> Unit)
    suspend fun saveVariations(productID : String ,variation: List<Variation>) : UiState<String>
    suspend fun saveVariation(productID: String,variation: Variation) : UiState<String>
    suspend fun uploadVariationPhoto(productID: String, uri : Uri, imageType : String) : UiState<Uri>
     fun getAllProducts(result: (UiState<List<Product>>) -> Unit)
}