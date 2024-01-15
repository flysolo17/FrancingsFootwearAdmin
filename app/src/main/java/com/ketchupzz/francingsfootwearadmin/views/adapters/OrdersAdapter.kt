package com.ketchupzz.francingsfootwearadmin.views.adapters

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.ketchupzz.francingsfootwearadmin.R
import com.ketchupzz.francingsfootwearadmin.model.transaction.TransactionStatus
import com.ketchupzz.francingsfootwearadmin.model.transaction.TransactionStatus.*
import com.ketchupzz.francingsfootwearadmin.model.transaction.Transactions
import com.ketchupzz.francingsfootwearadmin.utils.countItems
import com.ketchupzz.francingsfootwearadmin.utils.toPHP
import org.w3c.dom.Text

interface OrderClickListener {
    fun onOrderClick(transactions: Transactions)

}
class OrdersAdapter(private val context: Context ,private val orders : List<Transactions>,private val orderClickListener: OrderClickListener) : RecyclerView.Adapter<OrdersAdapter.OrdersViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdersViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.adapter_orders,parent,false)
        return OrdersViewHolder(view)
    }

    override fun getItemCount(): Int {
        return orders.size
    }

    override fun onBindViewHolder(holder: OrdersViewHolder, position: Int) {
        val transaction = orders[position]
        holder.textTransactionID.text = transaction.id
        holder.textRecipientName.text = transaction.address.contacts?.name ?:""
        holder.textRecipientPhone.text = transaction.address.contacts?.phone ?:""
        holder.textAddress.text = transaction.address.name ?:""
        holder.textStatus.backgroundTintList  = ColorStateList.valueOf(setStatusBgColor(transaction.status))
        holder.textStatus.text = transaction.status.name
        holder.textItems.text = countItems(transaction.items).toString()
        holder.textShipping.text = transaction.shippingFee.toPHP()
        holder.textTotal.text = transaction.payment.total.toPHP()
        holder.textPayment.text = transaction.payment.method.name +" - " + transaction.payment.status.name
        holder.itemView.setOnClickListener {
            orderClickListener.onOrderClick(transaction)
        }

    }
    class OrdersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val textRecipientName : TextView = itemView.findViewById(R.id.textFullname)
        val textRecipientPhone : TextView = itemView.findViewById(R.id.textPhone)
        val textAddress : TextView = itemView.findViewById(R.id.textAddress)
        val textTransactionID : TextView = itemView.findViewById(R.id.textTransactionID)
        val textItems : TextView = itemView.findViewById(R.id.textItems)
        val textStatus : TextView = itemView.findViewById(R.id.textStatus)
        val textTotal : TextView = itemView.findViewById(R.id.textTotal)
        val textShipping : TextView = itemView.findViewById(R.id.textShipping)
        val textPayment : TextView = itemView.findViewById(R.id.textPayment)
    }
    private fun setStatusBgColor(transactionStatus: TransactionStatus): Int {
       return when(transactionStatus) {
            PENDING -> ContextCompat.getColor(context, R.color.pending)
            ACCEPTED -> ContextCompat.getColor(context, R.color.accepted)
            ON_DELIVERY -> ContextCompat.getColor(context, R.color.ondelivery)
            COMPLETE -> ContextCompat.getColor(context, R.color.completed)
            CANCELLED -> ContextCompat.getColor(context, R.color.cancelled)
            DECLINED -> ContextCompat.getColor(context, R.color.declined)
        }
    }
}