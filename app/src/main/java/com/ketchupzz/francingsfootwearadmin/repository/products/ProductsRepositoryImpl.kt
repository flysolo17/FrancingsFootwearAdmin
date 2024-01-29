package com.ketchupzz.francingsfootwearadmin.repository.products

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.ketchupzz.francingsfootwearadmin.model.products.Product
import com.ketchupzz.francingsfootwearadmin.repository.inventory.INVENTORY_COLLECTION
import com.ketchupzz.francingsfootwearadmin.repository.variations.VARIATION_SUB_COLLECTION

import com.ketchupzz.francingsfootwearadmin.utils.UiState
import kotlinx.coroutines.tasks.await


const val PRODUCT_COLLECTION = "products";
class ProductsRepositoryImpl(private val firestore: FirebaseFirestore,private val storage: FirebaseStorage) : ProductsRepository {
    override fun addProduct(product: Product, result: (UiState<String>) -> Unit) {
        firestore.collection(PRODUCT_COLLECTION)
            .document(product.id!!)
            .set(product)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    result.invoke(UiState.SUCCESS("Successfully Added"))
                } else {
                    result.invoke(UiState.FAILED("Failed updating product"))
                }
            }
    }

    override suspend fun uploadProductImage(
        productID: String,
        uri: Uri,
        imageType: String
    ): UiState<Uri> {
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

    override fun updateProduct(product: Product, result: (UiState<String>) -> Unit) {
        firestore.collection(PRODUCT_COLLECTION)
            .document(product.id ?: "")
            .set(product)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    result.invoke(UiState.SUCCESS("Successfully Updated"))
                } else {
                    result.invoke(UiState.FAILED("Failed updating product"))
                }
            }
    }

    override fun deleteProduct(productID: String, result: (UiState<String>) -> Unit) {
        val productRef = firestore.collection(PRODUCT_COLLECTION).document(productID)
        deleteSubcollections(productRef.collection(VARIATION_SUB_COLLECTION))
            .addOnCompleteListener { subcollectionsDeleteTask ->
                if (subcollectionsDeleteTask.isSuccessful) {
                    productRef.delete()
                        .addOnCompleteListener { deleteTask ->
                            if (deleteTask.isSuccessful) {
                                val imagesRef = storage.reference.child(PRODUCT_COLLECTION).child(productID)
                                imagesRef.delete()
                                    .addOnCompleteListener { imagesDeleteTask ->
                                        if (imagesDeleteTask.isSuccessful) {
                                            result(UiState.SUCCESS("Product deleted successfully"))
                                        } else {
                                            result(UiState.FAILED("Error deleting images"))
                                        }
                                    }
                            } else {
                                result(UiState.FAILED("Error deleting product"))
                            }
                        }
                } else {
                    result(UiState.FAILED("Error deleting subcollections"))
                }
            }

    }

    override fun getAllProduct(result: (UiState<List<Product>>) -> Unit) {
        firestore.collection(PRODUCT_COLLECTION)
            .addSnapshotListener { value, error ->
                error?.let {
                    result.invoke(UiState.FAILED(it.message.toString()))
                }
                value?.let {
                    result.invoke(UiState.SUCCESS(it.toObjects(Product::class.java)))
                }
            }
    }



    private fun deleteSubcollections(collection: CollectionReference): Task<Void> {

        return collection.get().continueWithTask { querySnapshotTask ->
            val deleteTasks = mutableListOf<Task<Void>>()
            for (document in querySnapshotTask.result!!.documents) {
                val deleteTask = deleteSubcollections(document.reference.collection("YOUR_SUBCOLLECTION"))
                deleteTasks.add(deleteTask)
            }
            Tasks.whenAll(deleteTasks)
        }
    }
}