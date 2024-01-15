package com.ketchupzz.francingsfootwearadmin.model.transaction

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class Items(
    val productID : String = "",
    val variationID : String = "",
    val name : String = "",
    val image : String = "",
    val variation : String = "",
    val size : String = "",
    val price : Double = 0.00,
    var quantity : Int = 1,

    ) : Parcelable
