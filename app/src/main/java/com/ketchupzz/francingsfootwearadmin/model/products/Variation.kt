package com.ketchupzz.francingsfootwear.models.products

import android.os.Parcelable
import com.google.firebase.firestore.Exclude
import kotlinx.android.parcel.Parcelize


@Parcelize
class Variation(
    var id: String = "",
    var image : String = "",
    val name: String = "",
    val sizes: MutableList<Size> = mutableListOf(),
) : Parcelable {
    @Exclude
    fun getEffectiveVariationPrice(): String {
        val minPrice = this.sizes.minByOrNull { it.price }?.price ?: 0.00
        val maxPrice = this.sizes.maxByOrNull { it.price }?.price ?: 0.00

        return if (minPrice == maxPrice) {
            "₱ ${minPrice.format()}"

        } else {
            "₱ ${minPrice.format()} - ₱ ${maxPrice.format()}"
        }
    }
    @Exclude
    fun getSizeNames(): String {
        return this.sizes.joinToString(", ") { it.size }
    }

    private fun Double.format(): String {
        return String.format(" %.2f", this)
    }
}


