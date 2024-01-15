package com.ketchupzz.francingsfootwearadmin.model.customer

import android.os.Parcelable
import com.ketchupzz.francingsfootwearadmin.model.customer.Addresses
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class Customer(
    val id : String ? = "",
    val name : String ? = "",
    val profile : String ? = "",
    val email : String ? = "",
    val addresses : MutableList<Addresses> = mutableListOf()
) : Parcelable