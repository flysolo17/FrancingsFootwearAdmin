package com.ketchupzz.francingsfootwearadmin.repository.inventory

import android.net.Uri
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.ketchupzz.francingsfootwearadmin.utils.UiState
import com.ketchupzz.francingsfootwearadmin.model.products.Product
import com.ketchupzz.francingsfootwearadmin.model.products.Variation
import com.ketchupzz.francingsfootwearadmin.repository.variations.VARIATION_SUB_COLLECTION
import kotlinx.coroutines.tasks.await


const val INVENTORY_COLLECTION = "products";


class InventoryRepositoryImpl(private val firestore: FirebaseFirestore,private val storage: FirebaseStorage) : InventoryRepository {
    override suspend fun createProduct(product: Product) : UiState<String> {
        return try {
            firestore.collection(INVENTORY_COLLECTION)
                .document(product.id!!)
                .set(product)
                .await()
            UiState.SUCCESS("Successfully Saved!")
        } catch (e : Exception) {
            UiState.FAILED(e.message.toString())
        }
    }

    override suspend fun uploadPhoto(productID: String, uri: Uri, imageType: String): UiState<Uri> {
        return try {
            val downloadUrl = storage.reference.child(INVENTORY_COLLECTION)
                .child(productID)
                .child("${System.currentTimeMillis()}" + ".${imageType}")
                .putFile(uri)
                .await()
                .storage
                .downloadUrl
                .await()
            UiState.SUCCESS(downloadUrl)
        } catch (e: Exception) {
            UiState.FAILED(e.toString())
        }
    }

    override suspend fun saveVariations(productID: String,variations: List<Variation>) : UiState<String> {
        val variationCollectionRef = firestore.collection(INVENTORY_COLLECTION)
            .document(productID)
            .collection(VARIATION_SUB_COLLECTION)
        return try {
            firestore.runBatch { batch ->
                variations.forEach { variation ->
                    val variationId = variation.id
                    batch.set(variationCollectionRef.document(variationId), variation)
                }
            }
            UiState.SUCCESS("Variation Uploaded")
        } catch (e: Exception) {
            UiState.FAILED(e.toString())
        }
    }

    override suspend fun saveVariation(productID: String, variation: Variation): UiState<String> {
        return try {
            firestore.collection(INVENTORY_COLLECTION)
                .document(productID)
                .collection(VARIATION_SUB_COLLECTION)
                .document(variation.id)
                .set(variation)
                .await()
            UiState.SUCCESS("${variation.name} saved!!")
        } catch (e : Exception) {
            UiState.FAILED(e.message.toString())
        }
    }

    override suspend fun uploadVariationPhoto(
        productID: String,
        uri: Uri,
        imageType: String
    ): UiState<Uri> {
        return try {
            val downloadUrl = storage.reference.child(INVENTORY_COLLECTION)
                .child(productID)
                .child("variations")
                .child("${System.currentTimeMillis()}" + ".${imageType}")
                .putFile(uri)
                .await()
                .storage
                .downloadUrl
                .await()
            UiState.SUCCESS(downloadUrl)
        } catch (e: Exception) {
            UiState.FAILED(e.toString())
        }
    }
    override fun getAllProducts(result : ( UiState<List<Product>> ) -> Unit) {
        result.invoke(UiState.LOADING)
        firestore.collection(INVENTORY_COLLECTION)
            .addSnapshotListener { value, error ->
                error?.let {
                    UiState.FAILED(it.message.toString())
                }
                value?.let {
                    val products: List<Product> = it.toObjects(Product::class.java)
                   result.invoke( UiState.SUCCESS(products))
                }
            }

    }
}