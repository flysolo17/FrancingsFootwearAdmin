package com.ketchupzz.francingsfootwearadmin.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class Users(
    val id : String ? = "",
    val name : String ? = "",
    val profile : String ? = "",
    val email : String ? = "",
) : Parcelable