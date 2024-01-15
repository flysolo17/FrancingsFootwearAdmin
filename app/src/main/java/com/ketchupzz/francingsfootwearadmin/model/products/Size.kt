package com.ketchupzz.francingsfootwear.models.products

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Size(
    val size: String = "",
    val stock: Int = 0,
    val cost: Double = 0.00,
    val price : Double = 0.00,
) : Parcelable