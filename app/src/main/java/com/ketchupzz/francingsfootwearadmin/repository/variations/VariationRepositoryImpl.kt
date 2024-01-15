package com.ketchupzz.francingsfootwearadmin.repository.variations

import android.net.Uri
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.ketchupzz.francingsfootwearadmin.model.products.Size
import com.ketchupzz.francingsfootwearadmin.model.products.Variation
import com.ketchupzz.francingsfootwearadmin.repository.inventory.INVENTORY_COLLECTION
import com.ketchupzz.francingsfootwearadmin.repository.products.PRODUCT_COLLECTION
import com.ketchupzz.francingsfootwearadmin.utils.UiState
import kotlinx.coroutines.tasks.await
import org.checkerframework.checker.guieffect.qual.UI

const val VARIATION_SUB_COLLECTION = "variations"
class VariationRepositoryImpl(private val firestore: FirebaseFirestore,private val storage: FirebaseStorage) : VariationRepository {
    override fun createVariation(
        productID: String,
        variation: Variation,
        result: (UiState<String>) -> Unit
    ) {
        result.invoke(UiState.LOADING)
        firestore.collection(PRODUCT_COLLECTION)
            .document(productID)
            .collection(VARIATION_SUB_COLLECTION)
            .document(variation.id)
            .set(variation)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    result.invoke(UiState.SUCCESS("Successfully Added!"))
                } else {
                    result.invoke(UiState.FAILED("Failed Adding Variation"))
                }
            }
    }

    override fun deleteVariation(
        productID: String,
        variationID: String,
        imageURL : String,
        result: (UiState<String>) -> Unit
    ) {
        result.invoke(UiState.LOADING)
        storage
            .reference
            .child(imageURL)
            .delete()
            .addOnSuccessListener {
                firestore.collection(PRODUCT_COLLECTION)
                    .document(productID)
                    .collection(VARIATION_SUB_COLLECTION)
                    .document(variationID)
                    .delete()
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            result.invoke(UiState.SUCCESS("Successfully Deleted!"))
                        } else {
                            result.invoke(UiState.FAILED("Failed deleting variation!"))
                        }
                    }.addOnFailureListener {
                        result.invoke(UiState.FAILED(it.message.toString()))
                    }
            }.addOnFailureListener {
                result.invoke(UiState.FAILED(it.message.toString()))
            }
    }

    override fun getVariationByProductID(
        productID: String,
        result: (UiState<List<Variation>>) -> Unit
    ) {
        result.invoke(UiState.LOADING)
        firestore.collection(PRODUCT_COLLECTION)
            .document(productID)
            .collection(VARIATION_SUB_COLLECTION)
            .addSnapshotListener { value, error ->
                error?.let {
                    result.invoke(UiState.FAILED(it.message.toString()))
                }
                value?.let {
                    result.invoke(UiState.SUCCESS(it.toObjects(Variation::class.java)))
                }
            }
    }

    override fun stockIn(
        productID: String,
        variationID: String,
        size: List<Size>,
        result: (UiState<String>) -> Unit
    ) {
        result.invoke(UiState.LOADING)
        firestore.collection(PRODUCT_COLLECTION)
            .document(productID)
            .collection(VARIATION_SUB_COLLECTION)
            .document(variationID)
            .update("sizes",size)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    result.invoke(UiState.SUCCESS("New Stocks Added!"))
                } else {
                    result.invoke(UiState.FAILED("Failed Adding new Stocks!"))
                }
            }.addOnFailureListener {
                result.invoke(UiState.FAILED(it.message.toString()))
            }

    }

    override fun stockOut(
        productID: String,
        variationID: String,
        size: List<Size>,
        result: (UiState<String>) -> Unit
    ) {
        result.invoke(UiState.LOADING)
        firestore.collection(PRODUCT_COLLECTION)
            .document(productID)
            .collection(VARIATION_SUB_COLLECTION)
            .document(variationID)
            .update("sizes",size)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    result.invoke(UiState.SUCCESS("Stocks out success!"))
                } else {
                    result.invoke(UiState.FAILED("Failed stock out stocks!"))
                }
            }.addOnFailureListener {
                result.invoke(UiState.FAILED(it.message.toString()))
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
}