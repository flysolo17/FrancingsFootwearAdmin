package com.ketchupzz.francingsfootwearadmin.repository.auth

import android.net.Uri
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.ketchupzz.francingsfootwearadmin.utils.UiState
import com.ketchupzz.francingsfootwearadmin.model.Users
import java.util.UUID
const val USER_COLLECTION = "admin";
class AuthRepositoryImpl(private  val firestore : FirebaseFirestore, private  val auth : FirebaseAuth, private val storage: FirebaseStorage) : AuthRepository {
    override fun login(username: String, password: String, result: (UiState<FirebaseUser>) -> Unit) {
        result.invoke(UiState.LOADING)
        auth.signInWithEmailAndPassword(username, password)
            .addOnCompleteListener { signInTask ->
                if (signInTask.isSuccessful) {
                    val user = signInTask.result?.user
                    if (user != null) {
                        result.invoke(UiState.SUCCESS(user))
                    } else {
                        result.invoke(UiState.FAILED("User not found!"))
                    }
                } else {

                    result.invoke(UiState.FAILED("Failed to log in: ${signInTask.exception?.message}"))
                }
            }
            .addOnFailureListener { signInException ->

                result.invoke(UiState.FAILED("Failed to log in: ${signInException.message}"))
            }
    }

    override fun forgotPassword(email: String, result: (UiState<String>) -> Unit) {
        result.invoke(UiState.LOADING)
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    result.invoke(UiState.SUCCESS("We sent password reset link to : ${email}"))

                } else {
                    result.invoke(UiState.FAILED("Failed to logged in.."))
                }
            }.addOnFailureListener {
                result.invoke(UiState.FAILED(it.message.toString()))
            }
    }

    override fun getAccountByID(uid: String, result: (UiState<Users>) -> Unit) {
        result.invoke(UiState.LOADING)
        firestore.collection(USER_COLLECTION)
            .document(uid)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    result.invoke(UiState.FAILED(error.message.toString()))
                }
                if (value != null && value.exists()) {
                    val customer = value.toObject(Users::class.java)
                    if (customer != null) {
                        result.invoke(UiState.SUCCESS(customer))
                    } else {
                        result.invoke(UiState.FAILED("User does not exist"))
                    }
                } else {
                    result.invoke(UiState.FAILED("User does not exist"))
                }
            }
    }
    override fun reAuthenticateAccount(
        user : FirebaseUser,
        email: String,
        password: String,
        result: (UiState<FirebaseUser>) -> Unit
    ) {
        result.invoke(UiState.LOADING)
        val credential = EmailAuthProvider.getCredential(email, password)
        user.reauthenticate(credential)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    result.invoke(UiState.SUCCESS(user))
                } else  {
                    result.invoke(UiState.FAILED("Wrong Password!"))
                }
            }.addOnFailureListener {
                result.invoke(UiState.FAILED(it.message.toString()))
            }
    }


    override fun changePassword(
        user: FirebaseUser,
        password: String,
        result: (UiState<String>) -> Unit
    ) {
        result.invoke(UiState.LOADING)
        user.updatePassword(password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    result.invoke(UiState.SUCCESS("Password changed successfully"))
                } else  {
                    result.invoke(UiState.FAILED("Wrong Password!"))
                }
            }.addOnFailureListener {
                result.invoke(UiState.FAILED(it.message!!))
            }
    }

    override fun changeProfile(
        uid: String,
        uri: Uri,
        imageType: String,
        result: (UiState<String>) -> Unit
    ) {
        result.invoke(UiState.LOADING)
        val uniqueImageName = UUID.randomUUID().toString()
        val imagesRef: StorageReference = storage.reference.child("students/${uid}/$uniqueImageName.jpg")
        val uploadTask: UploadTask = imagesRef.putFile(uri)

        uploadTask.addOnCompleteListener { taskSnapshot ->
            if (taskSnapshot.isSuccessful) {
                imagesRef.downloadUrl.addOnCompleteListener { urlTask: Task<Uri> ->
                    if (urlTask.isSuccessful) {
                        val imageUrl: String = urlTask.result.toString()
                        val userDocRef = firestore.collection(USER_COLLECTION).document(uid)
                        userDocRef.update("profile", imageUrl)
                            .addOnSuccessListener {
                                result(UiState.SUCCESS("Profile image updated successfully."))
                            }
                            .addOnFailureListener { e ->
                                result(UiState.FAILED(e.message.toString()))
                            }
                    } else {
                        result(UiState.FAILED("Failed to get image URL."))
                    }
                }
            } else {
                result(UiState.FAILED("Failed to upload image."))
            }
        }
    }

    override fun updateFullname(uid: String, fullname: String, result: (UiState<String>) -> Unit) {
        result.invoke(UiState.LOADING)
        firestore.collection(USER_COLLECTION)
            .document(uid)
            .update("name",fullname)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    result.invoke(UiState.SUCCESS("Successfully Updated!"))
                } else {
                    result.invoke(UiState.FAILED("unknown error!"))
                }
            }.addOnFailureListener {
                result.invoke(UiState.FAILED(it.message.toString()))
            }
    }
}