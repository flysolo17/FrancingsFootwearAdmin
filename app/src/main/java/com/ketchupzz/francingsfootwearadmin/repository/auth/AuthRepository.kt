package com.ketchupzz.francingsfootwearadmin.repository.auth

import android.net.Uri
import com.google.firebase.auth.FirebaseUser
import com.ketchupzz.francingsfootwearadmin.utils.UiState
import com.ketchupzz.francingsfootwearadmin.model.Users

interface AuthRepository {
    fun login(username : String , passwod : String,result : (UiState<FirebaseUser>) -> Unit)
    fun forgotPassword(email : String ,result: (UiState<String>) -> Unit)
    fun getAccountByID(uid : String,result: (UiState<Users>) -> Unit)
    fun reAuthenticateAccount(user: FirebaseUser, email: String, password: String, result: (UiState<FirebaseUser>) -> Unit)
    fun changePassword(user: FirebaseUser, password: String, result: (UiState<String>) -> Unit)
    fun changeProfile(uid: String, uri : Uri, imageType : String, result: (UiState<String>) -> Unit)
    fun updateFullname(uid: String  ,fullname : String,result: (UiState<String>) -> Unit)
}