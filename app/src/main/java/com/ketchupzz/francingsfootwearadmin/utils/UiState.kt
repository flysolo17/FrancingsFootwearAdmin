package com.ketchupzz.francingsfootwearadmin.utils

sealed class UiState<out T> {

    data object LOADING : UiState<Nothing>()
    data class SUCCESS<out T>(val data : T) : UiState<T>()
    data class FAILED(val message : String) : UiState<Nothing>()

}