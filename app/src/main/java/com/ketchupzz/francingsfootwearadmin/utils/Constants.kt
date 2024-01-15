package com.ketchupzz.francingsfootwearadmin.utils
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import androidx.core.content.ContextCompat
import com.ketchupzz.francingsfootwearadmin.R
import com.ketchupzz.francingsfootwearadmin.model.messages.Messages
import com.ketchupzz.francingsfootwearadmin.model.products.Variation
import com.ketchupzz.francingsfootwearadmin.model.transaction.Items
import com.ketchupzz.francingsfootwearadmin.model.transaction.PaymentStatus
import com.ketchupzz.francingsfootwearadmin.model.transaction.TransactionStatus
import com.ketchupzz.francingsfootwearadmin.model.transaction.Transactions
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random


fun Context.getImageTypeFromUri(imageUri: Uri?): String? {
    val contentResolver: ContentResolver = contentResolver
    val mimeTypeMap = MimeTypeMap.getSingleton()
    return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(imageUri!!))
}

fun getConsonants(input: String): String {
    val vowels = setOf('a', 'e', 'i', 'o', 'u', 'A', 'E', 'I', 'O', 'U')
    val consonants = StringBuilder()

    for (char in input) {
        if (!vowels.contains(char) && char.isLetter()) {
            consonants.append(char)
        }
    }

    return consonants.toString().trim().uppercase()
}

fun generateRandomNumber(): String {
    // Generate a random 10-digit number
    val randomNumber = buildString {
        repeat(10) {
            append(Random.nextInt(0,10))
        }
    }
    return randomNumber
}

fun setVariationID(productID : String,variation: MutableList<Variation>) : MutableList<Variation> {
    variation.map {
        it.id =productID+"-"+getConsonants(it.name)
    }
    return variation
}

fun getEffectiveProductPrice(variations: List<Variation>): String {
    val minPrices = variations.flatMap { it.sizes.map { size -> size.price } }.minOrNull() ?: 0.00
    val maxPrices = variations.flatMap { it.sizes.map { size -> size.price } }.maxOrNull() ?: 0.00
    return if (minPrices == maxPrices) {
        "₱ ${minPrices.format()}"

    } else {
        "₱ ${minPrices.format()} - ₱ ${maxPrices.format()}"
    }
}

fun Double.format(): String {
    return String.format(" %.2f", this)
}




fun countOrderItemsTotalQuantity(transactions: Transactions) : Int {
    var count = 0
    transactions.items.forEach {
        count += it.quantity
    }
    return count;
}

fun generateRandomNumber(repeat : Int): String {
    // Generate a random 10-digit number
    val randomNumber = buildString {
        repeat(repeat) {
            append(Random.nextInt(0,10))
        }
    }
    return randomNumber
}
fun computeItemSubtotal(items : List<Items>) : Double {
    var subtotal  = 0.00;
    items.map {
        subtotal += (it.quantity * it.price)
    }
    return subtotal
}

fun countItems(items: List<Items>) : Int {
    var count = 0
    items.map {
        count += it.quantity
    }
    return count;
}
fun Double.toPHP(): String {
    val formattedString = String.format("₱ %,.2f", this)
    return formattedString
}


fun calculateShippingFee(numberOfItems: Int): Double {
    return when (numberOfItems) {
        in 1..10 -> 100.00
        in 11..20 -> 200.00
        in 21..30 -> 300.00
        in 31..40 -> 400.00
        in 41..50 -> 500.00
        in 51..60 -> 600.00
        in 61..70 -> 700.00
        in 71..80 -> 800.00
        in 81..90-> 900.00
        else -> 1000.00
    }
}



 fun Context.setStatusBgColor(transactionStatus: TransactionStatus): Int {
    return when(transactionStatus) {
        TransactionStatus.PENDING -> ContextCompat.getColor(this, R.color.pending)
        TransactionStatus.ACCEPTED -> ContextCompat.getColor(this, R.color.accepted)
        TransactionStatus.ON_DELIVERY -> ContextCompat.getColor(this, R.color.ondelivery)
        TransactionStatus.COMPLETE -> ContextCompat.getColor(this, R.color.completed)
        TransactionStatus.CANCELLED -> ContextCompat.getColor(this, R.color.cancelled)
        TransactionStatus.DECLINED -> ContextCompat.getColor(this, R.color.declined)
    }
}

fun getTotalSales(transactions: List<Transactions>)  : Double {
    var total = 0.00
    transactions.forEach {
        if (it.status== TransactionStatus.COMPLETE && it.payment.status== PaymentStatus.PAID) {
            total += (it.payment.total - it.shippingFee)
        }
    }
    return total
}

fun Date.toTimeAgoOrDateTimeFormat(): String {
    val now = Date()
    val timeDifference = now.time - this.time
    val minutesInMillis = 60 * 1000
    val hoursInMillis = 60 * minutesInMillis
    val daysInMillis = 24 * hoursInMillis

    return when {
        timeDifference < minutesInMillis -> "just now"
        timeDifference < hoursInMillis -> "${timeDifference / minutesInMillis} minute${if (timeDifference / minutesInMillis > 1) "s" else ""} ago"
        timeDifference < daysInMillis -> "${timeDifference / hoursInMillis} hour${if (timeDifference / hoursInMillis > 1) "s" else ""} ago"
        else -> SimpleDateFormat("MMM/dd/yyyy HH:mm", Locale.getDefault()).format(this)
    }
}



fun List<Messages>.getNewMessages(myID : String) : Int {
    var count = 0
    for (message in this) {
        if (message.senderID == myID) {
            break
        }
        count += 1
    }
    return count
}
