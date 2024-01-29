package com.ketchupzz.francingsfootwearadmin.repository.products

import android.net.Uri
import com.ketchupzz.francingsfootwearadmin.model.products.Product
import com.ketchupzz.francingsfootwearadmin.utils.UiState

interface ProductsRepository {

    fun addProduct(product: Product ,result : (UiState<String>) -> Unit)
    suspend fun uploadProductImage(productID: String, uri : Uri, imageType : String) : UiState<Uri>
    fun updateProduct(product: Product,result: (UiState<String>) -> Unit)
    fun deleteProduct(productID : String,result: (UiState<String>) -> Unit)
    fun getAllProduct(result: (UiState<List<Product>>) -> Unit)

}