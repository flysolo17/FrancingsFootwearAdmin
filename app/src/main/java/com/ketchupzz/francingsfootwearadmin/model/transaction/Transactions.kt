package com.ketchupzz.francingsfootwearadmin.model.transaction

import android.os.Parcelable
import com.ketchupzz.francingsfootwearadmin.model.customer.Addresses
import com.ketchupzz.francingsfootwearadmin.utils.generateRandomNumber

import kotlinx.parcelize.Parcelize
import java.util.Date


@Parcelize

data class Transactions(
    val id : String  = generateRandomNumber(20),
    val customerID : String = "",
    val items : List<Items> = mutableListOf(),
    val status: TransactionStatus = TransactionStatus.PENDING,
    var payment: Payment = Payment(),
    val shippingFee : Double = 100.00,
    val address: Addresses = Addresses(),
    val details: List<Details> = mutableListOf(),
    val orderDate : Date = Date()
) : Parcelable {}


enum class TransactionStatus {
    PENDING,
    ACCEPTED,
    ON_DELIVERY,
    COMPLETE,
    CANCELLED,
    DECLINED
}

