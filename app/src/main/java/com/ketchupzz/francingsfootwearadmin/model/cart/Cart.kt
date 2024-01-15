package com.ketchupzz.francingsfootwearadmin.model.cart

import java.util.Date

data class Cart(
    var id : String = "",
    val customerID : String = "",
    val productID : String = "",
    val variationID : String = "",
    val size : String = "",
    val quantity : Int = 0,
    val addedAt : Date = Date()
)
