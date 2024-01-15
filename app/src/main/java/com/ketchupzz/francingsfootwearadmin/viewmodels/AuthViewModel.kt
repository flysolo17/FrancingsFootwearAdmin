package com.ketchupzz.francingsfootwearadmin.viewmodels

import com.ketchupzz.francingsfootwearadmin.model.Users
import com.ketchupzz.francingsfootwearadmin.repository.auth.AuthRepository



import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser

import com.ketchupzz.francingsfootwearadmin.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class AuthViewModel @Inject constructor(private  val authRepository: AuthRepository) : ViewModel() {

    private val _currentUser= MutableLiveData<FirebaseUser>()
    val currentUser : LiveData<FirebaseUser> get() = _currentUser


    private val _users= MutableLiveData<UiState<Users>>()
    val users : LiveData<UiState<Users>> get() = _users
    fun setCurrentUser(user : FirebaseUser) {
        _currentUser.value = user
    }
    fun login(email: String,password: String,result: (UiState<FirebaseUser>) -> Unit) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                authRepository.login(email, password, result)
            }
        }
    }
    fun forgotPassword(email: String,result: (UiState<String>) -> Unit) {
        return authRepository.forgotPassword(email,result)
    }
    fun getCustomerInfo(uid : String) {
        authRepository.getAccountByID(uid) {
            _users.value = it
        }
    }
    fun changeProfile(uid: String,
                      uri: Uri,
                      imageType: String,
                      result: (UiState<String>) -> Unit) {
        return authRepository.changeProfile(uid, uri, imageType, result)
    }

    fun editProfile(uid: String,fullname : String,result: (UiState<String>) -> Unit) {
        return authRepository.updateFullname(uid, fullname, result)
    }

    fun changePassword(user: FirebaseUser,password: String,result: (UiState<String>) -> Unit) {
        return authRepository.changePassword(user,password,result)
    }

    fun reauthenticate(user: FirebaseUser,email: String,password: String,result: (UiState<FirebaseUser>) -> Unit) {
        return authRepository.reAuthenticateAccount(user,email,password,result)
    }
}