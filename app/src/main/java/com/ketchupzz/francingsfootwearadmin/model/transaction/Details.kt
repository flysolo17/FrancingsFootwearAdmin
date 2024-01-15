package com.ketchupzz.francingsfootwearadmin.model.transaction

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import java.util.Date


@Parcelize
data class Details(
    val title : String  = "",
    val description : String = "",
    val date : Date = Date()
) : Parcelable {
}