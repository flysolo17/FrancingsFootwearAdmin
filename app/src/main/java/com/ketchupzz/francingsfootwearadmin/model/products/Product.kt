package com.ketchupzz.francingsfootwearadmin.model.products

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
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

