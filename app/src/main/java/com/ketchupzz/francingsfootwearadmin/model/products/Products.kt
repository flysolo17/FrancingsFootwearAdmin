package com.ketchupzz.francingsfootwear.models.products

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.Date

@Parcelize
data class Product(
    val id: String ? = "",
    var image : String? = "",
    val name: String ?= "",
    val category : String ?= "",
    val description: String ? = "",
    val createdAt : Date = Date()
) : Parcelable